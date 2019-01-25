//package com.example.es;
//
//import com.alibaba.fastjson.JSONObject;
//import com.example.es.mapper.ProductMapper;
//import com.example.es.utils.EsUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class EsApplicationTests {
//
//    @Autowired
//    private ProductMapper productMapper;
//    @Autowired
//    private EsUtils esUtils;
//
//    /**
//     * 测试rest批次导入
//     */
//    @Test
//    public void restTest() {
//        List<JSONObject> jsonObjects = productMapper.selectAll();
//        esUtils.esRestBatchPush(jsonObjects,false);
//    }
//
//    /**
//     * 测试transport批次导入
//     */
//    @Test
//    public void transportTest() {
//        List<JSONObject> jsonObjects = productMapper.selectAll();
//        esUtils.esTransportBatchPush(jsonObjects);
//    }
//
//    @Test
//    public void transportSelectTest() {
//        List<JSONObject> jsonObjects = new ArrayList<>();
////        System.out.println(jsonObjects.get(0));
//        String json = "{'number':'EGSCMATM3D-','original_img':'http://img.egscm.com/Uploads/Picture/2015-06-13/fa373a8650370508518d5ea4484af70d.jpg','is_open':true,'thumb':'http://img.egscm.com/Uploads/Picture/2015-06-13/3fc1916df01eb44fa97c64439dbc2e79.jpg','is_del':false,'model':'800g','title_en':'Aptamil Pronutra+ 3 Folgemilch','id':14,'title':'德国爱他美奶粉 3 段','barcode':'4008976022343','content':'&lt;p style=&quot;text-align:center&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/a5b35d6e9febef3dbcc7956679d2aa8f.jpg&quot; title=&quot;a5b35d6e9febef3dbcc7956679d2aa8f.jpg&quot;/&gt;&lt;/p&gt;&lt;p style=&quot;text-align:center&quot;&gt;&lt;br/&gt;&lt;/p&gt;&lt;p style=&quot;text-align:center&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/a94520e7397e5ee0adfef61c69da48f1.jpg&quot; title=&quot;a94520e7397e5ee0adfef61c69da48f1.jpg&quot;/&gt;&lt;/p&gt;&lt;p style=&quot;text-align: center;&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/ad8bca15f1576c5c68c66b80c1f26d54.jpg&quot; title=&quot;ad8bca15f1576c5c68c66b80c1f26d54.jpg&quot;/&gt;&amp;nbsp;&lt;/p&gt;&lt;p style=&quot;text-align:center&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/7a48942ecbc8003cd4f75f5009a987bf.jpg&quot; title=&quot;7a48942ecbc8003cd4f75f5009a987bf.jpg&quot;/&gt;&lt;/p&gt;&lt;p style=&quot;text-align:center&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/aaebaaa0cd9de097821748cfe80221ba.jpg&quot; title=&quot;aaebaaa0cd9de097821748cfe80221ba.jpg&quot;/&gt;&lt;/p&gt;&lt;p style=&quot;text-align:center&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/63c9fd9445ab115d55611b60aa19fd91.jpg&quot; title=&quot;63c9fd9445ab115d55611b60aa19fd91.jpg&quot;/&gt;&lt;/p&gt;&lt;p style=&quot;text-align:center&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/a405e2a1c97b755dfb3a8af9f0386ff3.jpg&quot; title=&quot;a405e2a1c97b755dfb3a8af9f0386ff3.jpg&quot;/&gt;&lt;/p&gt;&lt;p style=&quot;text-align: center;&quot;&gt;&lt;img src=&quot;http://img.egscm.com/Uploads/Editor/2015-06-09/efeaa7acf5345b82078f166137639440.jpg&quot; title=&quot;efeaa7acf5345b82078f166137639440.jpg&quot;/&gt;&lt;/p&gt;','status':true}";
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        System.out.println(jsonObject);
//        jsonObjects.add(jsonObject);
//        esUtils.esBatchUpdate("product", "product_info",jsonObjects);
//    }
//}
