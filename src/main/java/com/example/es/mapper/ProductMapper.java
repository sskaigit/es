package com.example.es.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductMapper {

    @Select("select product_id id,barcode,content,is_del,is_open,model,number,original_img,status,thumb,title,title_en from erp.product")
    List<JSONObject> selectAll();
}
