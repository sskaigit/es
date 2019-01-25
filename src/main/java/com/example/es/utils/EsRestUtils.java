package com.example.es.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.es.transport.EsBulkProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

@Service
public class EsRestUtils {
    private static BulkProcessor bulkProcessor = EsBulkProcessor.bulkProcessor();

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * RestClient
     */
    private static RestClient restClient = RestClient.builder(
            new HttpHost("localhost",9200,"http")).build();

    public static RestClient restClient(){
        return restClient;
    }

    /**
     * Rest批次导入数据
     *
     * @param proDatas
     * @param isCreate
     */
    public void esRestBatchPush(List<JSONObject> proDatas, boolean isCreate) {
        //一千条一次
        if (proDatas != null && proDatas.size() > 0) {
            int cnt = proDatas.size() / 1000;
            int mod = proDatas.size() % 1000;
            String endpoint = "/_bulk";

            for (int i = 0; i <= cnt; i++) {
                StringBuilder sb = new StringBuilder();
                StringBuilder update = new StringBuilder();

                for (int j = 0; j < 1000 && i < cnt || i == cnt && j < mod; ++j) {
                    JSONObject proData = proDatas.get(i * 1000 + j);
                    //新增
                    String create = "{\"create\":{\"_index\":\"product\",\"_type\":\"product_info\",\"_id\":\"" + proData.getLong("id") + "\"}}";
                    sb.append(create).append("\n");
                    sb.append(proData.toString()).append("\n");
                    //更新
                    String upd = "{\"update\":{\"_index\":\"product\",\"_type\":\"product_info\",\"_id\":\"" + proData.getLong("id") + "\"}}";
                    update.append(upd).append("\n");
                    update.append("{\"doc\":").append(proData.toString()).append("}").append("\n");
                    System.out.println(sb.toString() + "=======");

                }
                if (isCreate) {
                    try {
                        Response response = restClient.performRequest("POST", endpoint, Collections.emptyMap(), new NStringEntity(sb.toString(), ContentType.APPLICATION_JSON));
                        Response response1 = restClient.performRequest("POST", endpoint, Collections.emptyMap(), new NStringEntity(update.toString(), ContentType.APPLICATION_JSON));
                        System.out.println(EntityUtils.toString(response.getEntity()));
                        System.out.println("---------------------------------------------");
                        System.out.println(EntityUtils.toString(response1.getEntity()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        restClient.performRequest("POST", endpoint, Collections.emptyMap(), new NStringEntity(update.toString(), ContentType.APPLICATION_JSON));
                        restClient.performRequest("POST", endpoint, Collections.emptyMap(), new NStringEntity(update.toString(), ContentType.APPLICATION_JSON));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * ids删除数据
     *
     * @param ids
     */
    public void deleteById(String ids) {
        String endpoint = "/_bulk";
        StringBuilder sb = new StringBuilder();
        for (Object o : JSONObject.parseArray(ids)) {
            //{"delete":{"_index":"index","_type":"type","_id":"id"}}
            String deleStr = "{\"delete\":{\"_index\":\"product\",\"_type\":\"product_info\",\"_id\":\"" + o + "\"}}";
            sb.append(deleStr).append("\n");
        }
        try {
            restClient.performRequest("POST", endpoint, Collections.emptyMap(), new NStringEntity(sb.toString(), ContentType.APPLICATION_JSON));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
