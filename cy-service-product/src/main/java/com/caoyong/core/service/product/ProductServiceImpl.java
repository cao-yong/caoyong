package com.caoyong.core.service.product;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.caoyong.common.enums.ProductIsShowEnum;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.ColorQuery;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductIsShowVO;
import com.caoyong.core.bean.product.ProductQuery;
import com.caoyong.core.bean.product.ProductQuery.Criteria;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;
import com.caoyong.utils.CheckParamsUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 产品service
 * 
 * @author yong.cao
 * @time 2017年6月26日下午9:57:34
 */

@Slf4j
@Service(version = "1.0.0")
//@Transactional(rollbackFor = Exception.class)
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
    @Autowired
    private SkuService  skuService;

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
        ResultBase<List<Color>> result = new ResultBase<>();
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
        CheckParamsUtil.check(product, Product.class, "name", "color", "size", "weight", "images", "brandId");
        ResultBase<Integer> result = new ResultBase<>();
        //返回影响的行数
        Integer count = 0;
        try {
            product.setSizes(StringUtils.join(product.getSize(), ","));
            product.setColors(StringUtils.join(product.getColor(), ","));
            product.setImgUrl(StringUtils.join(product.getImages(), ","));
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

            String[] colors = product.getColor();
            String[] sizes = product.getSize();
            if (null != colors && colors.length > 0) {
                for (String color : colors) {
                    if (null != sizes && sizes.length > 0) {
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
    public ResultBase<Integer> isShow(ProductIsShowVO isShowVO) throws BizException {
        log.info("isShow start:{}", ToStringBuilder.reflectionToString(isShowVO, ToStringStyle.DEFAULT_STYLE));
        CheckParamsUtil.check(isShowVO, ProductIsShowVO.class, "ids", "showType");
        ResultBase<Integer> result = new ResultBase<>();
        int count = 0;
        try {
            Product product = new Product();
            product.setIsShow(true);
            if (ProductIsShowEnum.PUT_OFF.getValue().equals(isShowVO.getShowType())) {
                product.setIsShow(false);
            }
            //循环
            for (Long id : isShowVO.getIds()) {
                //上架，更改商品状态
                product.setId(id);
                count += productDao.updateByPrimaryKeySelective(product);
                //发送消息到ActiveMQ java8 lambda 表达式实现函数式接口，两个订阅者：1，solr上传，2静态化
                jmsTemplate
                        .send(session -> session.createTextMessage(String.valueOf(id) + ":" + isShowVO.getShowType()));
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

    @Override
    public ResultBase<Integer> updateProductById(Product product) throws BizException {
        ResultBase<Integer> result = new ResultBase<>();
        result.setValue(0);
        log.info("updateproductById start. brand:{}",
                ToStringBuilder.reflectionToString(product, ToStringStyle.DEFAULT_STYLE));
        CheckParamsUtil.check(product, Product.class, "id");
        try {
            product.setSizes(StringUtils.join(product.getSize(), ","));
            product.setColors(StringUtils.join(product.getColor(), ","));
            product.setImgUrl(StringUtils.join(product.getImages(), ","));
            product.setUpdateTime(new Date());
            //没有必要每次都生成sku信息，当colors和sizes都与原来不一样时，才重新生成
            ResultBase<Product> productResult = selectProductById(product.getId());
            String orgSizes = null;
            String orgColous = null;
            if (productResult.isSuccess() && null != productResult.getValue()) {
                orgSizes = productResult.getValue().getSizes();
                orgColous = productResult.getValue().getColors();
            }
            int count = productDao.updateByPrimaryKeySelective(product);
            if (!product.getColors().equals(orgColous) || !product.getSizes().equals(orgSizes)) {
                //删除原来的sku信息，重新插入
                ResultBase<Integer> skuResult = skuService.deleteSkuByProductId(product.getId());
                if (skuResult.isSuccess() && null != skuResult.getValue()) {
                    count += skuResult.getValue();
                }
                String[] colors = product.getColor();
                String[] sizes = product.getSize();
                if (null != colors && colors.length > 0) {
                    for (String color : colors) {
                        if (null != sizes && sizes.length > 0) {
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
                    }

                }
            }
            result.setValue(count);
            if (count > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("updateproductById Exception:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("updateproductById end, result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }

    @Override
    public ResultBase<Integer> deleteProductByIds(Long[] ids) throws BizException {
        log.info("deleteProductByIds start. ids:{}",
                ToStringBuilder.reflectionToString(ids, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        if (ids == null) {
            result.setSuccess(false);
            result.setValue(0);
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL);
        }
        try {
            ProductQuery example = new ProductQuery();
            List<Long> values = Arrays.asList(ids);
            example.createCriteria().andIdIn(values);
            Product record = new Product();
            record.setUpdateTime(new Date());
            record.setIsDeleted(Constants.CONSTANTS_Y);
            int count = productDao.updateByExampleSelective(record, example);
            if (count > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("deleteProductByIds Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteProductByIds end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }

    @Override
    public ResultBase<Integer> deleteProductById(Long id) throws BizException {
        log.info("deleteProductById start. id:{}", id);
        ResultBase<Integer> result = new ResultBase<>();
        if (id == null) {
            result.setSuccess(false);
            result.setValue(0);
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL);
        }
        try {
            ProductQuery example = new ProductQuery();
            example.createCriteria().andIdEqualTo(id);
            Product record = new Product();
            record.setUpdateTime(new Date());
            record.setIsDeleted(Constants.CONSTANTS_Y);
            int count = productDao.updateByExampleSelective(record, example);
            if (count > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("deleteProductById Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteProductById end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }

    @Override
    public ResultBase<Product> selectProductById(Long id) throws BizException {
        log.info("selectProductById start. id:{}", id);
        ResultBase<Product> result = new ResultBase<>();
        if (id == null) {
            result.setSuccess(false);
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL);
        }
        try {
            Product product = productDao.selectByPrimaryKey(id);
            if (null != product) {
                result.setSuccess(true);
                result.setValue(product);
            }
            log.info("selectProductById success");
        } catch (Exception e) {
            log.error("selectProductById Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("selectProductById end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }
}
