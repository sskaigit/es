package com.example.es.transport;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class EsBulkProcessor {


    public static final Logger logger = LoggerFactory.getLogger(EsBulkProcessor.class);

    @Bean
    public BulkProcessor bulkProcessor() throws UnknownHostException {

        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();

        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), Integer.parseInt("9300")));

        return BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                logger.error("{} data bulk failed,reason :{}", bulkRequest.numberOfActions(), throwable);
            }
            //每添加1000个request，执行一次bulk操作
        }).setBulkActions(1000)
                //每达到5M的请求size时，执行一次bulk操作
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                //每5s执行一次bulk操作
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                //默认是1，表示积累bulk requests和发送bulk是异步的
                .setConcurrentRequests(1)
                //当ES由于资源不足发生异常
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }
}
