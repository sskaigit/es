package com.example.es.transport;

import com.example.es.utils.EsUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class EsTransport {

    /**
     * 建立mapping
     * @return
     */
    public static XContentBuilder getMapping() {
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("original_img")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("title")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("model")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("title_en")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("barcode")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("number")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("thumb")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("content")
                    .field("type", "text")
                    .endObject()
                    .startObject("is_open")
                    .field("type", "boolean")
                    .endObject()
                    .startObject("status")
                    .field("type", "boolean")
                    .endObject()
                    .startObject("is_del")
                    .field("type", "boolean")
                    .endObject()
                    .endObject()
                    .endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    /**
     * 创建mapping结构,需先创建索性
     */
    @Test
    public void createMapping() {
        XContentBuilder mapping1 = getMapping();
        PutMappingRequest mapping = Requests.putMappingRequest("products").type("product_two").source(mapping1);
        EsUtils.transportClient().admin().indices().putMapping(mapping).actionGet();
    }

    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        EsUtils.transportClient().admin()
                .indices()
                .create(new CreateIndexRequest("products"))
                .actionGet();
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        IndicesExistsResponse indicesExistsResponse = EsUtils.transportClient().admin().indices()
                .exists(new IndicesExistsRequest(new String[]{"products"}))
                .actionGet();
        if (indicesExistsResponse.isExists()) {
            EsUtils.transportClient().admin().indices().delete(new DeleteIndexRequest("products"))
                    .actionGet();
        }
    }

    /**
     * 删除Index下的某个Type
     */
    @Test
    public void deleteType() {
        EsUtils.transportClient().prepareDelete().setIndex("IndexName").setType("TypeName").execute().actionGet();
    }
}
