package com.example.es;

import com.alibaba.fastjson.JSONObject;
import com.example.es.mapper.ProductMapper;
import com.example.es.utils.EsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsApplicationTests {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EsUtils esUtils;

    /**
     * 测试rest批次导入
     */
    @Test
    public void restTest() {
        List<JSONObject> jsonObjects = productMapper.selectAll();
        esUtils.esRestBatchPush(jsonObjects,false);
    }

    /**
     * 测试transport批次导入
     */
    @Test
    public void transportTest() {
        List<JSONObject> jsonObjects = productMapper.selectAll();
        esUtils.esTransportBatchPush(jsonObjects);
    }

}
