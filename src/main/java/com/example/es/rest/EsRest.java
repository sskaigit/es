package com.example.es.rest;

import com.example.es.utils.EsUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

public class EsRest {

    /**
     * 查看api信息
     * @throws Exception
     */
    @Test
    public void catApi() throws Exception{
        Response response = EsUtils.restClient().performRequest("GET", "/", Collections.singletonMap("pretty", "true"));
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 创建索引
     * @throws Exception
     */
    @Test
    public void createIndex() throws Exception{
        String method = "PUT";
        String endpoint = "test-mapping";
        Response response = EsUtils.restClient().performRequest(method, endpoint);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 创建文档
     * @throws Exception
     */
    @Test
    public void CreateDocument() throws Exception {
        String method = "PUT";
        String endpoint = "/test-index/test/1";
        HttpEntity entity = new NStringEntity(
                "{\n" +
                        "    \"user\" : \"kimchy\",\n" +
                        "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                        "    \"message\" : \"trying out Elasticsearch\"\n" +
                        "}", ContentType.APPLICATION_JSON
        );

        Response response = EsUtils.restClient().performRequest(method, endpoint, Collections.<String, String>emptyMap(), entity);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 获取文档
     * @throws Exception
     */
    @Test
    public void getDocument()throws Exception{
        String method = "GET";
        String endpoint = "/test-index/test/1";
        Response response = EsUtils.restClient().performRequest(method,endpoint);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 查询所有数据
     * @throws Exception
     */
    @Test
    public void QueryAll() throws Exception {
        String method = "POST";
        String endpoint = "/product/product_info/_search";
        HttpEntity entity = new NStringEntity("{\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {}\n" +
                "  }\n" +
                "}", ContentType.APPLICATION_JSON);

        Response response = EsUtils.restClient().performRequest(method,endpoint,Collections.<String, String>emptyMap(),entity);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 删除索引
     * @throws Exception
     */
    @Test
    public void deleteIndex() throws Exception {
        String method = "DELETE";
        String endpoint = "/product";
        Response response = EsUtils.restClient().performRequest(method,endpoint,Collections.<String, String>emptyMap());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 创建mapping结构
     */
    @Test
    public void createMapping() {
        String mapping = "{\n" +
                "  \"mappings\":{\n" +
                "    \"product_info\":{\n" +
                "      \"properties\": {\n" +
                "        \"original_img\":{\n" +
                "          \"type\":\"keyword\"\n" +
                "         \n" +
                "        },\n" +
                "        \"title\":{\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"model\":{\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"title_en\":{\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"barcode\":{\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"number\":{\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"thumb\":{\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"content\":{\n" +
                "          \"type\": \"text\"\n" +
                "        },\n" +
                "        \"is_open\":{\n" +
                "          \"type\": \"boolean\"\n" +
                "        },\n" +
                "        \"status\":{\n" +
                "          \"type\": \"boolean\"\n" +
                "        },\n" +
                "        \"is_del\":{\n" +
                "          \"type\": \"boolean\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String endpoint = "/product";
        HttpEntity httpEntity = new NStringEntity(mapping, ContentType.APPLICATION_JSON);
        try {
            EsUtils.restClient().performRequest("PUT", endpoint, Collections.emptyMap(), httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
