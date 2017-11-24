package com.caoyong.core.service.product;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.ColorQuery;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQuery;
import com.caoyong.core.bean.product.ProductQuery.Criteria;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 产品service
 * 
 * @author yong.cao
 * @time 2017年6月26日下午9:57:34
 */

@Slf4j
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao  productDao;
    @Autowired
    private ColorDao    colorDao;
    @Autowired
    private SkuDao      skuDao;
    @Autowired
    private Jedis       jedis;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public Page<Product> selectPageByQuery(ProductQueryDTO query) throws BizException {
        log.info("selectPageByQuery start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        Page<Product> page = new Page<>();
        StringBuilder params = new StringBuilder();
        //设置分页查询条件
        ProductQuery example = new ProductQuery();
        example.setPageNo(query.getPageNo());
        example.setPageSize(query.getLimit());
        example.setStartRow(query.getStart());
        //查询条件
        Criteria createCriteria = example.createCriteria();
        //删除的不查询
        createCriteria.andIsDeletedEqualTo(Constants.CONSTANTS_N);
        //设置查询条件及分页栏参数
        if (StringUtils.isNotBlank(query.getName())) {
            createCriteria.andNameLike("%" + query.getName() + "%");
            params.append("name=").append(query.getName());
        }
        if (null != query.getBrandId()) {
            createCriteria.andBrandIdEqualTo(query.getBrandId());
            params.append("&brandId=").append(query.getBrandId());
        }
        if (null != query.getIsShow()) {
            createCriteria.andIsShowEqualTo(query.getIsShow());
            params.append("&isShow=").append(query.getIsShow());
        }
        try {
            Integer count = productDao.countByExample(example);
            List<Product> rows = productDao.selectByExample(example);
            //设置结果及分页对象
            if (null != rows && !rows.isEmpty()) {
                log.info("selectPageByQuery results:{}", count);
                log.info("selectPageByQuery rows:{}",
                        ToStringBuilder.reflectionToString(rows, ToStringStyle.DEFAULT_STYLE));
                page.setStart(query.getStart());
                page.setResults(count);
                page.setLimit(query.getLimit());
                page.setPage(query.getPage());
                page.setPageNo(query.getPageNo());
                page.setRows(rows);
                page.setIsSuccess(true);
            }
        } catch (Exception e) {
            log.error("selectPageByQuery error:{}", e.getMessage(), e);
            page.setError("数据库查询产品分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
        }
        log.info("selectPageByQuery end.");
        //分页展示
        String url = "/product/productList.do";
        page.pageView(url, params.toString());
        return page;
    }

    @Override
    public ResultBase<List<Color>> selectColorList() throws BizException {
        log.info("selectColorList start.");
        ResultBase<List<Color>> result = new ResultBase<List<Color>>();
        result.setSuccess(false);
        ColorQuery example = new ColorQuery();
        example.createCriteria().andParentIdNotEqualTo(0).andIsDeletedEqualTo(Constants.CONSTANTS_N);
        try {
            List<Color> colors = colorDao.selectByExample(example);
            if (null != colors && !colors.isEmpty()) {
                result.setValue(colors);
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("查询颜色结果集失败");
            log.error("selectColorList error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("selectColorList end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }

    @Override
    public ResultBase<Integer> saveProduct(Product product) throws BizException {
        log.info("saveProduct start. product:{}",
                ToStringBuilder.reflectionToString(product, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<Integer>();
        //返回影响的行数
        Integer count = 0;
        try {
            //redis生成商品id
            Long proudctId = jedis.incr("pno");
            product.setId(proudctId);
            //保存商品
            product.setIsDeleted(Constants.CONSTANTS_N);
            product.setIsShow(false);
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            product.setCreator(Constants.SYSTEM);
            product.setModifier(Constants.SYSTEM);
            count += productDao.insertSelective(product);

            String[] colors = product.getColors().split(",");
            String[] sizes = product.getSizes().split(",");
            for (String color : colors) {
                for (String size : sizes) {
                    //保存sku
                    Sku sku = new Sku();
                    sku.setIsDeleted(Constants.CONSTANTS_N);
                    sku.setProductId(product.getId());
                    sku.setColorId(Long.parseLong(color));
                    sku.setSize(size);
                    sku.setMarketPrice(Constants.DEAFAULT_PRICE);
                    sku.setPrice(Constants.DEAFAULT_PRICE);
                    sku.setDeliveFee(Constants.DEAFAULT_DELIVE_FEE);
                    sku.setUpperLimit(Constants.DEAFAULT_UPPER_LIMIT);
                    sku.setCreateTime(new Date());
                    sku.setUpdateTime(new Date());
                    sku.setCreator(Constants.SYSTEM);
                    sku.setModifier(Constants.SYSTEM);
                    sku.setStock(Constants.DEAFAULT_STOCK);
                    count += skuDao.insert(sku);
                }
            }
            result.setSuccess(true);
        } catch (NumberFormatException e) {
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("保存商品信息失败");
            log.error("saveProduct NumberFormat error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.NUM_FORMATE_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("保存商品信息失败");
            log.error("saveProduct error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        result.setValue(count);
        return result;
    }

    @Override
    public ResultBase<Integer> isShow(Long[] ids) throws BizException {
        log.info("isShow start:{}", ToStringBuilder.reflectionToString(ids, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<Integer>();
        int count = 0;
        try {
            Product product = new Product();
            product.setIsShow(true);
            //循环
            for (Long id : ids) {
                //上架，更改商品状态
                product.setId(id);
                count += productDao.updateByPrimaryKeySelective(product);
                //发送消息到ActiveMQ java8 lambda 表达式实现函数式接口，两个订阅者：1，solr上传，2静态化
                jmsTemplate.send(session -> session.createTextMessage(String.valueOf(id)));
            }
            result.setValue(count);
            result.setSuccess(true);
            log.info("isShow success, result:{}",
                    ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("商品上架失败");
            log.error("isShow error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        return result;
    }
}
