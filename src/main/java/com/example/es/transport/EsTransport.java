package com.example.es.transport;

import com.example.es.utils.EsUtils;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class EsTransport {

    /**
     * 建立mapping
     *
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
        PutMappingRequest mapping = Requests.putMappingRequest("product").type("product_info").source(getMapping());
        EsUtils.transportClient().admin().indices().putMapping(mapping).actionGet();
    }

    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        EsUtils.transportClient().admin().indices().create(new CreateIndexRequest("product")).actionGet();
    }

    /**
     * 添加别名
     */
    @Test
    public void createAlias(){
        EsUtils.transportClient().admin().indices().prepareAliases().addAlias("product","my_index").execute().actionGet();
    }

    /**
     * 删除别名
     */
    @Test
    public void deleteAlias(){
        EsUtils.transportClient().admin().indices().prepareAliases().removeAlias("my_index_v1","my_index").execute().actionGet();
    }

    /**
     * 使用别名替换索引
     */
    @Test
    public void replaceAlias(){
        EsUtils.transportClient().admin().indices().prepareAliases()
                .removeAlias("my_index_v1","my_index")
                .addAlias("product","my_index")
                .execute().actionGet();
    }

    /**
     * 创建单个文档
     */
    @Test
    public void createDoc() {
        EsUtils.transportClient().prepareIndex().setIndex("IndexName").setType("TypeName").setId("id").setSource("source").execute().actionGet();
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        IndicesExistsResponse indicesExistsResponse = EsUtils.transportClient().admin().indices()
                .exists(new IndicesExistsRequest(new String[]{"product"}))
                .actionGet();
        if (indicesExistsResponse.isExists()) {
            EsUtils.transportClient().admin().indices().delete(new DeleteIndexRequest("product"))
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

    /**
     * 删除doc文档
     */
    @Test
    public void deleteDoc() {
        EsUtils.transportClient().prepareDelete().setIndex("IndexName").setType("TypeName").setId("id").execute().actionGet();
    }

    /**
     * 使用matchAllQuery删除所有文档
     */
    @Test
    public void deleteAll(){
		   DeleteByQueryAction.INSTANCE.newRequestBuilder(EsUtils.transportClient())
	         .source("product")
	         .filter(QueryBuilders.matchAllQuery())
	         .get();
    }

    /**
     * 查询
     */
    @Test
    public void select() {
        Client client = EsUtils.transportClient();
        // 组合查询条件
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery("title", "德国喜宝有机营养米粉 （2830）"));
        // 查询
        SearchResponse response = client
                // index
                .prepareSearch("product")
                // type
                .setTypes("product_info")
                // 查询条件
                .setQuery(query)
                .setFrom(0)
                .setSize(60)
                .execute()
                .actionGet();
        // 响应内容
        SearchHits shs = response.getHits();
        for (SearchHit hit : shs) {
            System.out.println(hit.getSourceAsString());
        }
        client.close();
    }
}
