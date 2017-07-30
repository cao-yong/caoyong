package com.caoyong;

import java.io.StringWriter;

import org.junit.Test;

import com.caoyong.common.utlis.JSONConversionUtil;
import com.caoyong.core.bean.user.Buyer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJSON {

    @Test
    public void testJSON() throws Exception {
        Buyer buyer = new Buyer();
        buyer.setRealName("yong.cao");
        buyer.setAddr("上海");
        buyer.setCity("浦东");
        buyer.setPassword("232323");
        ObjectMapper om = new ObjectMapper();
        //非空
        om.setSerializationInclusion(Include.NON_NULL);
        StringWriter w = new StringWriter();
        om.writeValue(w, buyer);
        System.out.println(w.toString());

        Buyer readValue = om.readValue(w.toString(), Buyer.class);
        System.out.println(readValue);

        String convertObjString = JSONConversionUtil.objToString(buyer);
        System.out.println("util:" + convertObjString);
    }
}
