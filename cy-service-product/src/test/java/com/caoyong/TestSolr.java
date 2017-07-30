package com.caoyong;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * solr测试
 * 
 * @author yong.cao
 * @time 2017年7月30日 下午9:24:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestSolr {
    @Autowired
    private SolrServer solrServer;

    @Test
    public void testSolrJSpring() throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", 3);
        //doc.setField("name", "刘一手");
        solrServer.add(doc);
        solrServer.commit();
    }
    /*
     * @Test public void testSolrJ() throws Exception { String baseUrl =
     * "http://192.168.128.128:8080/solr/collection1"; SolrServer solrServer =
     * new HttpSolrServer(baseUrl); SolrInputDocument doc = new
     * SolrInputDocument(); doc.setField("id", 2); //doc.setField("name",
     * "刘一手"); solrServer.add(doc); solrServer.commit(); }
     */
}
