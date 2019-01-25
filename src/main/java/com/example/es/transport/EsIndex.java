package com.example.es.transport;

import com.alibaba.fastjson.JSONObject;
import com.example.es.utils.EsTransportUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class EsIndex {

    /**
     * 获取Mapping key和类型
     *
     * @param obj  对象
     * @param list mapping keys
     * @return
     */
    public static JSONObject getMappingKey(Object obj, List<String> list) {
        Preconditions.checkArgument(obj != null, "对象不能为空!");
        Field[] fields = obj.getClass().getDeclaredFields();
        JSONObject json = new JSONObject();

        for (Field field : fields) {
            String name = field.getName();
            if (list != null) {
                if (list.contains(name)) {
                    continue;
                }
            }
            String typeName = field.getType().toString();
            String type = typeName.substring(typeName.lastIndexOf(".") + 1);

            json.put(name, getMappingType(type));
        }
        return json;
    }

    /**
     * 获取mapping
     *
     * @param json
     * @return
     */
    public static XContentBuilder getMapping(JSONObject json) {
        Preconditions.checkArgument(!json.isEmpty(), "json不能为空!");
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                    .startObject("properties");
            for (String key : json.keySet()) {
                String value = json.getString(key);
                mapping.startObject(key);
                if (value.equals("date")){
                        mapping.field("type", "date")
                                .field("format","strict_date_optional_time||epoch_millis")
                                .endObject();
                }else{
                    mapping.field("type", value)
                            .endObject();
                }
            }
            mapping.endObject().endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    /**
     * 创建mapping结构,需先创建索性
     *
     * @param index
     * @param type
     * @param mapping
     */
    public static void createMapping(String index, String type, XContentBuilder mapping) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "type不能为空!");
        Preconditions.checkArgument(mapping != null, "mapping不能为空!");
        try {
            EsTransportUtil.client().admin().indices().putMapping(Requests.putMappingRequest(index).type(type).source(mapping)).actionGet();
            log.info("创建mapping结构成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("mapping结构创建失败! - e={}", e.getMessage());
        }
    }

    /**
     * 创建索引
     *
     * @param index
     */
    public static void createIndex(String index) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        try {
            EsTransportUtil.client().admin().indices().create(new CreateIndexRequest(index)).actionGet();
            log.info("创建索引成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建索引失败! - e={}", e.getMessage());
        }
    }

    /**
     * 添加别名
     *
     * @param index
     * @param alias
     */
    public static void createAlias(String index, String alias) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(alias), "别名不能为空!");
        try {
            EsTransportUtil.client().admin().indices().prepareAliases().addAlias(index, alias).execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加别名失败! - e={}", e.getMessage());
        }
        log.info("添加别名成功!");
    }

    /**
     * 删除别名
     *
     * @param index
     * @param alias
     */
    public static void deleteAlias(String index, String alias) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(alias), "别名不能为空!");
        try {
            EsTransportUtil.client().admin().indices().prepareAliases().removeAlias(index, alias).execute().actionGet();
            log.info("删除别名成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除别名失败! - e={}", e.getMessage());
        }
    }

    /**
     * 使用别名替换索引
     *
     * @param newIndex
     * @param oldIndex
     * @param alias
     */
    public static void replaceAlias(String newIndex, String oldIndex, String alias) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(newIndex), "新索引不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(oldIndex), "旧索引不能为空!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(alias), "别名不能为空!");
        try {
            EsTransportUtil.client().admin().indices().prepareAliases()
                    .removeAlias(oldIndex, alias)
                    .addAlias(newIndex, alias)
                    .execute().actionGet();
            log.info("使用别名替换索引成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("使用别名替换索引失败! - e={}", e.getMessage());
        }
    }

    /**
     * 删除索引
     *
     * @param index
     */
    public static void deleteIndex(String index) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index不能为空!");
        IndicesExistsResponse indicesExistsResponse = EsTransportUtil.client().admin().indices()
                .exists(new IndicesExistsRequest(new String[]{index}))
                .actionGet();
        if (indicesExistsResponse.isExists()) {
            try {
                EsTransportUtil.client().admin().indices().delete(new DeleteIndexRequest(index))
                        .actionGet();
                log.info("删除索引成功!");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("删除索引失败! - e={}", e.getMessage());
            }
        } else {
            log.info("索引不存在!");
        }
    }

    /**
     * 转换类型为ES Mapping 类型
     *
     * @param type
     * @return
     */
    private static String getMappingType(String type) {
        String str;
        switch (type.toLowerCase()) {
            case "string":
                str = "keyword";
                break;

            case "bigdecimal":
                str = "double";
                break;

            case "boolean":
                str = "integer";
                break;

            default:
                str = type.toLowerCase();
                break;
        }
        return str;
    }
}
