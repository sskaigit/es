package com.example.es.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class EsTransportUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static volatile TransportClient client;
    private static volatile BulkProcessor bulkProcessor;

    /**
     * 解决第一次调用报错,jar包冲突
     */
    static {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    /**
     * client
     *
     * @return
     */
    public static TransportClient client() {
        if (client == null) {
            synchronized (EsTransportUtil.class) {
                //双重检查
                if (client == null) {
                    //cluster_name,name
                    Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
                    try {
                        client = new PreBuiltTransportClient(settings)
                                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), Integer.parseInt("9300")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("创建Client出错! - e={}", e.getMessage());
                    }
                }
            }
        }

        return client;
    }

    /**
     * 批次处理client
     *
     * @return
     */
    public static BulkProcessor bulkProcessor() {
        if (bulkProcessor == null) {
            synchronized (EsTransportUtil.class) {
                //双重检查
                if (bulkProcessor == null) {
                    bulkProcessor = BulkProcessor.builder(EsTransportUtil.client(), new BulkProcessor.Listener() {
                        @Override
                        public void beforeBulk(long l, BulkRequest bulkRequest) {
                            log.info("请求信息---> :{}" + bulkRequest.requests());
                            log.info("请求数量---> :{}" + bulkRequest.numberOfActions());
                        }

                        @Override
                        public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                            log.info("---尝试操作 :{}" + bulkRequest.numberOfActions() + "条数据成功---");
                            log.info("响应结果--->" + bulkResponse.buildFailureMessage());
                        }

                        @Override
                        public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                            log.info("---尝试操作 :{}" + bulkRequest.numberOfActions() + "条数据失败---");
                            log.error("{} data bulk failed,reason :{}", bulkRequest.numberOfActions(), throwable);
                        }
                        //每添加1000个request，执行一次bulk操作  ** 没到数量就不会请求
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
        }
        return bulkProcessor;
    }

    /**
     * 新增一条数据
     *
     * @param index
     * @param type
     * @param id
     * @param t
     * @param <T>
     */
    public static <T> void esPush(String index, String type, String id, T t) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type) || !Strings.isNullOrEmpty(id), "index or type or id不能为空!");
        Preconditions.checkArgument(t != null, "新增数据不能为空!");
        try {
            String json = objectMapper.writeValueAsString(t);
            EsTransportUtil.client().prepareIndex().setIndex(index).setType(type).setId(id).setSource(json, XContentType.JSON).execute().actionGet();
            log.info("创建单个文档成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建单个文档失败! - e={}", e.getMessage());
        }
    }

    /**
     * 批次导入数据
     *
     * @param index
     * @param type
     * @param list
     */
    public static <T> void esBatchPush(String index, String type, List<T> list) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type), "index or type不能为空!");
        Preconditions.checkArgument(!list.isEmpty() || list != null, "json新增数据不能为空!");
        for (T t : list) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(objectMapper.writeValueAsString(t));
                String id = jsonObject.get("id").toString();
                Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "json新增数据必须含有ID!");
                byte[] json = objectMapper.writeValueAsBytes(t);
                // 新版的API中使用setSource时，参数的个数必须是偶数,需加XContentType.JSON
                EsTransportUtil.bulkProcessor().add(new IndexRequest(index, type, id).source(json, XContentType.JSON));
                log.info("批次导入数据成功!");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("批次导入数据失败! - e={}", e.getMessage());
            }
        }
    }

    /**
     * 删除Index下的某个Type或某个ID的一个doc文档
     *
     * @param index
     * @param type
     * @param id
     */
    public static void deleteDoc(String index, String type, String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "type不能为空!");
        try {
            if (id == null) {
                EsTransportUtil.client().prepareDelete().setIndex(index).setType(type).execute().actionGet();
            } else {
                EsTransportUtil.client().prepareDelete().setIndex(index).setType(type).setId(id).execute().actionGet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除失败! - e={}", e.getMessage());
        }
        log.info("删除成功!");
    }

    /**
     * 使用matchAllQuery删除index下所有文档
     *
     * @param index
     */
    public static void deleteAll(String index) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        try {
            DeleteByQueryAction.INSTANCE.newRequestBuilder(EsTransportUtil.client())
                    .source(index)
                    .filter(QueryBuilders.matchAllQuery())
                    .get();
            log.info("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除失败! - e={}", e.getMessage());
        }
    }


    /**
     * 批量删除数据
     *
     * @param index
     * @param type
     * @param ids
     */
    public static void esBatchDelete(String index, String type, List ids) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "type不能为空!");
        Preconditions.checkArgument(!ids.isEmpty() || ids != null, "ids不能为空!");
        for (Object id : ids) {
            try {
                EsTransportUtil.bulkProcessor().add(new DeleteRequest(index, type, id.toString()));
                log.info("批量删除数据成功!");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("批量删除数据失败! - e={}", e.getMessage());
            }
        }
    }

    /**
     * 更新一条数据
     *
     * @param index
     * @param type
     * @param id
     * @param t
     * @param <T>
     */
    public static <T> void esUpdate(String index, String type, String id, T t) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type) || !Strings.isNullOrEmpty(id), "index or type or id不能为空!");
        Preconditions.checkArgument(t != null, "更新数据不能为空!");
        try {
            byte[] json = objectMapper.writeValueAsBytes(t);
            EsTransportUtil.client().prepareUpdate(index, type, id).setDoc(json, XContentType.JSON).get();
            log.info("更新数据成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新数据失败! - e={}", e.getMessage());
        }
    }

    /**
     * 批量更新数据
     *
     * @param index
     * @param type
     * @param list
     */
    public static <T> void esBatchUpdate(String index, String type, List<T> list) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type), "index or type不能为空!");
        Preconditions.checkArgument(!list.isEmpty() || list != null, "json更新数据不能为空!");
        for (T t : list) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(objectMapper.writeValueAsString(t));
                String id = jsonObject.get("id").toString();
                Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "json更新数据必须含有ID!");

                byte[] json = objectMapper.writeValueAsBytes(t);
                EsTransportUtil.bulkProcessor().add(new UpdateRequest(index, type, id).doc(json, XContentType.JSON));
                log.error("批量更新数据成功!");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("批量更新数据失败! - e={}", e.getMessage());
            }
        }
    }

    /**
     * 列表分页查询
     *
     * @param index
     * @param type
     * @param pageNum
     * @param pageSize
     */
    public static <T> List<T> findByPage(String index, String type, Integer pageNum, Integer pageSize, Class<T> valueType) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type), "index or type不能为空!");
        Preconditions.checkArgument(pageNum != null || pageSize != null, "pageNum or pageSize不能为空!");

        SearchResponse response = EsTransportUtil.client()
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .addSort("id", SortOrder.ASC)
                // 从第几条开始,查询多少条,以ID排序
                .setFrom((pageNum - 1) * pageSize)
                .setSize(pageSize)
                .get();

        return getResponse(response, valueType);
    }

    /**
     * 条件查询并分页
     *
     * @param index
     * @param type
     * @param json
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static <T> List<T> findByTerm(String index, String type, JSONObject json, Integer pageNum, Integer pageSize, Class<T> valueType) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type), "index or type不能为空!");
        Preconditions.checkArgument(pageNum != null || pageSize != null, "pageNum or pageSize不能为空!");
        Preconditions.checkArgument(!json.isEmpty(), "查询条件不能为空!");
        // 组合查询条件
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        Iterator<String> iterator = json.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            query.must(QueryBuilders.termQuery(key, json.get(key)));
        }

        // 查询
        SearchResponse response = EsTransportUtil.client()
                // index
                .prepareSearch(index)
                // type
                .setTypes(type)
                // 查询条件
                .setQuery(query)
                .setFrom((pageNum - 1) * pageSize)
                .setSize(pageSize)
                .execute()
                .actionGet();

        return getResponse(response, valueType);
    }

    /**
     * ids查询
     *
     * @param index
     * @param type
     * @param ids
     * @return
     */
    public static <T> List<T> findbyIds(String index, String type, String ids, Class<T> valueType) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index) || !Strings.isNullOrEmpty(type) || !Strings.isNullOrEmpty(ids), "index or type不能为空!");
        QueryBuilder queryBuilder = QueryBuilders.idsQuery()
                .addIds(ids);
        SearchResponse response = EsTransportUtil.client()
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(queryBuilder)
                .get();

        return getResponse(response, valueType);
    }

    /**
     * 获取响应结果
     *
     * @param response
     * @return
     */
    private static <T> List<T> getResponse(SearchResponse response, Class<T> valueType) {
        Preconditions.checkArgument(valueType != null, "对象不能为空!");
        // 响应内容
        List<JSONObject> jsons = new ArrayList<>();
        List<T> list = new ArrayList<>();
        SearchHits searchHits = response.getHits();
        log.info("一共的记录数: TotalHits={}", searchHits.getTotalHits());

        for (SearchHit searchHit : searchHits) {
            try {
                T t = objectMapper.readValue(searchHit.getSourceAsString(), valueType);
                list.add(t);
                log.info("获取响应结果 - " + valueType + "={}", t);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("获取响应结果失败 - e={}", e.getMessage());
            }
        }
        return list;
    }
}