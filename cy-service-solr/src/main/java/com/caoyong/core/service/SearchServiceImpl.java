package com.caoyong.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.caoyong.common.utlis.MoneyFormatUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuQuery;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 全文检索
 * 
 * @author yong.cao
 * @time 2017年7月5日下午9:33:35
 */
@Slf4j
@Service(version = "1.0.0")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao     skuDao;

    /**
     * 全文检索
     * 
     * @param keywords
     * @return
     */
    @Override
    public Page<Product> selectPageByQuery(ProductQueryDTO query) throws BizException {
        Page<Product> page = new Page<Product>();
        log.info("selectProductByQuery start. query=",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        query.setLimit(12);
        List<Product> products = new ArrayList<>();
        try {
            SolrQuery solrQuery = new SolrQuery();
            //设置开始行
            solrQuery.setStart(query.getStart());
            //设置每页数
            solrQuery.setRows(query.getLimit());
            //关键字
            solrQuery.set("q", "name_ik:" + query.getKeyword());
            //排序
            solrQuery.addSort("price", ORDER.asc);
            //设置高亮
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("name_ik");
            solrQuery.setHighlightSimplePre("<span style='color:red'>");
            solrQuery.setHighlightSimplePost("</span>");

            //设置过滤条件
            if (null != query.getBrandId()) {
                solrQuery.addFilterQuery("brandId:" + query.getBrandId());
            }
            if (StringUtils.isNotBlank(query.getPrice())) {
                if (query.getPrice().contains("-")) {
                    String[] prices = query.getPrice().split("-");
                    solrQuery.addFilterQuery("price:[" + prices[0] + " TO " + prices[1] + "]");
                } else {
                    solrQuery.addFilterQuery("price:[" + query.getPrice() + " TO *]");
                }
            }
            QueryResponse response = solrClient.query(solrQuery);
            //取高亮
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //结果集
            SolrDocumentList docs = response.getResults();
            //总条数
            Integer results = null != Long.valueOf(docs.getNumFound()) ? Long.valueOf(docs.getNumFound()).intValue()
                    : 0;
            //拼接查询条件
            StringBuilder params = new StringBuilder();
            params.append("keyword=").append(query.getKeyword());
            for (SolrDocument doc : docs) {
                Product product = new Product();
                //商品id
                String id = (String) doc.get("id");
                product.setId(Long.valueOf(id));
                //商品名称
                //String name = (String) doc.get("name_ik");
                //取出高亮
                Map<String, List<String>> highlightings = highlighting.get(id);
                List<String> highlightingNames = highlightings.get("name_ik");
                String name = highlightingNames.get(0);
                product.setName(name);
                //图片
                //此字段，由于solr服务器的差异，导致在solr中储存格式不一样
                String url = null;
                //list时的处理
                if (null != doc.get("url") && doc.get("url") instanceof ArrayList) {
                    @SuppressWarnings("unchecked")
                    List<String> urls = (ArrayList<String>) doc.get("url");
                    if (null != urls && !urls.get(0).isEmpty()) {
                        url = urls.get(0);
                    }
                }
                //string时处理
                if (null != doc.get("url") && doc.get("url") instanceof String) {
                    url = (String) doc.get("url");
                }
                product.setImgUrl(url);
                //最低价
                Float price = (Float) doc.get("price");
                product.setPrice(MoneyFormatUtil.format(String.valueOf(price)));
                //品牌id
                Integer brandId = (Integer) doc.get("brandId");
                product.setBrandId(null != brandId ? brandId.longValue() : 0);

                products.add(product);
            }
            page.setRows(products);
            page.setPageNo(query.getPageNo());
            page.setLimit(query.getLimit());
            page.setIsSuccess(true);
            page.setPage(true);
            page.setResults(results);
            String url = "/search";
            page.pageView(url, params.toString());
        } catch (SolrServerException e) {
            log.error("solr server error:", e.getMessage(), e);
            page.setError("搜索商品分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
            throw new BizException(ErrorCodeEnum.SOLR_SERVER_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            log.error("selectProductByQuery error:{}", e.getMessage(), e);
            page.setError("查询商品分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("selectProductByQuery end.");
        return page;
    }

    @Override
    public void insertProductToSolr(Long id) throws BizException {
        log.info("insertProductToSolr start, id:{}", id);
        try {
            //保存到solr服务器
            SolrInputDocument doc = new SolrInputDocument();
            //商品id
            doc.setField("id", id);
            Product p = productDao.selectByPrimaryKey(id);
            //商品名称
            doc.setField("name_ik", p.getName());
            //图片
            doc.setField("url", p.getImages()[0]);
            //查询最低价格
            SkuQuery example = new SkuQuery();
            example.createCriteria().andProductIdEqualTo(id).andIsDeletedEqualTo(Constants.CONSTANTS_N);
            example.setOrderByClause("price * 1 asc");
            example.setPageNo(1);
            example.setPageSize(1);
            example.setFields("price");
            List<Sku> skus = skuDao.selectByExample(example);
            //价格
            Float price = Float.parseFloat((null == skus || skus.isEmpty()) ? "0.00" : skus.get(0).getPrice());
            doc.setField("price", price);
            //品牌id
            doc.setField("brandId", p.getBrandId());
            solrClient.add(doc);
            solrClient.commit();
            log.info("insertProductToSolr end.");
        } catch (NumberFormatException e) {
            log.error("insertProductToSolr number format error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.NUM_FORMATE_ERROR, e.getMessage(), e);
        } catch (SolrServerException e) {
            log.error("insertProductToSolr solr error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.SOLR_SERVER_ERROR, e.getMessage(), e);
        } catch (IOException e) {
            log.error("insertProductToSolr io error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.IO_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            log.error("insertProductToSolr error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void deleteProductToSolr(Long id) throws BizException {
        log.info("deleteProductToSolr start, id:{}", id);
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (SolrServerException e) {
            log.error("deleteProductToSolr solr error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.SOLR_SERVER_ERROR, e.getMessage(), e);
        } catch (IOException e) {
            log.error("deleteProductToSolr io error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.IO_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            log.error("deleteProductToSolr error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteProductToSolr end.");
    }
}
