package com.caoyong.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caoyong.common.utlis.MoneyFormatUtil;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
/**
 * 全文检索
 * @author yong.cao
 * @time 2017年7月5日下午9:33:35
 */
@Slf4j
@Service("searchService")
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SolrServer solrServer;
	/**
	 * 全文检索
	 * @param keywords
	 * @return
	 */
	@Override
	public Page<Product> selectPageByQuery(ProductQueryDTO query) throws BizException{
		Page<Product> page = new Page<Product>();
		log.info("selectProductByQuery start. query=", ToStringBuilder.
				reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
		query.setLimit(15);
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
			if(null != query.getBrandId()){
				solrQuery.addFilterQuery("brandId:" + query.getBrandId());
			}
			if(StringUtils.isNotBlank(query.getPrice())){
				if(query.getPrice().contains("-")){
					String[] prices = query.getPrice().split("-");
					solrQuery.addFilterQuery("price:[" + prices[0] + " TO " + prices[1] + "]");
				}else{
					solrQuery.addFilterQuery("price:[" + query.getPrice() + " TO *]");
				}
			}
			QueryResponse response = solrServer.query(solrQuery);
			//取高亮
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			//结果集
			SolrDocumentList docs = response.getResults();
			//总条数
			Integer results = null != Long.valueOf(docs.getNumFound()) 
					? Long.valueOf(docs.getNumFound()).intValue() : 0;
			//拼接查询条件
			StringBuilder params = new StringBuilder();
			params.append("keyword=").append(query.getKeyword());
			for(SolrDocument doc : docs){
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
				@SuppressWarnings("unchecked")
				List<String> urls = (ArrayList<String>) doc.get("url");
				product.setImgUrl(urls.get(0));
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
			log.error("solr server error:",e.getMessage(), e);
			page.setError("搜索商品分页失败");
			page.setErrorCode(e.getMessage());
			page.setErrorCode(e.getMessage());
			page.setResults(0);
			throw new BizException(ErrorCodeEnum.SOLR_SERVER_ERROR,e.getMessage(),e);
		} catch (Exception e) {
			log.error("selectProductByQuery error:{}", e.getMessage(), e);
			page.setError("查询商品分页失败");
			page.setErrorCode(e.getMessage());
			page.setErrorCode(e.getMessage());
			page.setResults(0);
			throw new BizException(ErrorCodeEnum.UNKOWN_ERROR,e.getMessage(),e);
		}
		log.info("selectProductByQuery end.");
		return page;
	}
}
