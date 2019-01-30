package com.example.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    private LoginService loginService;
    private static ObjectMapper defaultMapper = new ObjectMapper();

    @GetMapping("/test")
    public String test() {
        String messge = loginService.getMessge();
        return messge;
    }

    public static void main(String[] args) {
//        ByteStreams.copy()
//        String stockTerm = Joiner.on("@").join(new String[]{"1001201", "5","100", "1"});
//        System.out.println(stockTerm);
//        String s = "hello";

//        Date d = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        String currdate = format.format(d);
//        System.out.println("现在的日期是：" + d);
//        Calendar ca = Calendar.getInstance();
//        // num为增加的天数，可以改变的
//        for (int i=0;i<10;i++){
//            ca.add(Calendar.DATE, 1);
//            d = ca.getTime();
//            String enddate = format.format(d);
//            System.out.println("增加天数以后的日期：" + enddate);
//        }

//        List a = new ArrayList();
////        Preconditions.checkArgument(Strings.isNullOrEmpty(a.toString()), "Redis key is not null");
////        System.out.println("++++++++++++");
//        a.add("a");
//        a.add("b");
//        a.add("c");
//        String join = StringUtils.join(a);
//        int i = join.indexOf("[");
//        join.replace("[","");
//        join.replace("]","");
//        System.out.println(join);
//        String join = Joiner.on("@").join(a);
//        System.out.println(join);
//        List b = new ArrayList();
//        b.add(4);
//        b.add(5);
//        b.add(6);
//        a.addAll(b);
//        for (Object obj:a){
//            System.out.println(obj);
//        }

//        String date = "2016年10月24日 上午12:00:00";
//        Date d = null;
//        try {
//            d = DateFormat.getDateTimeInstance().parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(d);

//        Date date = new Date();
//        String d = DateFormat.getDateTimeInstance().format(date);
//        System.out.println(d);

//        String string = "2016-10-24";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date parse = null;
//        try {
//            parse = sdf.parse(string);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(parse);
//
//
//        String d = DateFormat.getDateTimeInstance().format(parse);
//        System.out.println(d);
//        User user = get();
//        System.out.println("----------"+user);
//        if (Strings.isNullOrEmpty(user.getMail())){
//            System.out.println("+++++++");
//        }
//        Preconditions.checkArgument(!Strings.isNullOrEmpty(user.getMail()), "Redis key is not null");
//        System.out.println("----------+");
//        double pow = Math.pow(2, 3);
//        System.out.println(pow-1);
//        User user = get();
//        if (user!=null){
//            System.out.println("+++++++");
//        }

//        for(int i=0;i<7;i++){
//            System.out.print("*\t");
//        }
//        Preconditions.checkArgument(false, "请选择需要查询的用户!");

//        JSONObject json = new JSONObject();
//        List<Long> ids = new ArrayList<>();
//        if (ids.isEmpty()){
//            System.out.println("++++++++++");
//        }
//        System.out.println("______________");

//        UserInfo userInfo = new UserInfo();
//        User user = get(userInfo);
//        if (user==null){
//            System.out.println(user);
//        }
//        String s = "BigDecimal";
//        System.out.println(s.toLowerCase());
//        String str;
//        String type = "String";
//        switch (type.toLowerCase()) {
//            case "string":
//                str = "keyword";
//                break;
//
//            case "bigdecimal":
//                str = "double";
//                break;
//
//            default:
//                str = type;
//                break;
//        }
//        System.out.println(str);

//        String a = "a,b,c,d,";
//        int i = a.lastIndexOf(",");
//        String substring = a.substring(0, i);
//        System.out.println(substring);


//        String body="{'docOrder':{'id':null,'version':null,'creator':null,'creatorId':null,'gmtCreate':null,'lastOperator':null,'lastOperatorId':null,'gmtModified':null,'pageNum':null,'pageSize':null,'orderBy':null,'fields':null,'map':null,'tenantId':null,'typeBiz':1000020,'typeId':1001201,'no':null,'sourceNo':null,'sourcePlatformNo':'H1811271578618553410','uid':null,'shopId':1,'channelId':null,'makeTime':1543332294000,'makeAid':null,'deleted':null,'status':null,'orderStatus':null,'total':null,'amountPayment':16673,'amount':16673,'amountTax':1773,'amountGoods':14900,'deposit':0,'amountDiscount':0,'amountOther':0,'amountFreight':0,'currency':'CNY','remark':null,'remarkAdmin':null,'paymentMethod':0,'payId':10517,'payIdNo':'10517','payNo':null,'payTime':null,'checkAid':null,'checkTime':null,'departmentId':null,'statusBack':null,'statusMake':null,'statusMsg':null,'couponGetId':null,'couponGetNo':'','activityId':null,'activityNo':'','uniqueMark':null,'sellerId':null,'sellerNo':null,'new':true},'docOrderConsignee':{'id':null,'consignee':'李清','mobile':'13757122378','country':0,'province':330000,'city':330100,'district':330103,'countryName':'0','provinceName':null,'cityName':null,'districtName':null,'address':'绍兴路161号野风现代中心北楼5A02','addressAll':' 浙江 杭州 下城 绍兴路161号野风现代中心北楼5A02','idCard':'','expressNo':'0','expressId':0,'expressIdNo':0,'buyerName':'李清','buyerIdCard':'320522198212170025'},'goodsinfoLists':[{'docOrderGoods':{'id':null,'orderId':null,'goodsId':null,'productId':null,'name':'爱他美白金版婴幼儿奶粉3段','num':1,'numUnit':1,'numTotal':1,'number':null,'sku':'A200001','barcode':'9421902960055','priceMarket':16800,'priceShop':14900,'price':14900,'amount':14900,'freight':0,'remark':null,'taxRate':0,'salesFee':0,'vatFee':1773,'priceTax':1773,'activityId':null,'activityNo':'','gtsNo':null,'expirationDate':null,'warehouseId':null,'warehouseNo':'W1810291073106938557','mark':null,'markId':null,'conditionWarehouse':1005001,'conditionBatch':1005001,'conditionExpirationDate':1005001,'conditionError':1005201},'docOrderGoodsExt':null,'docOrderGoodsStructure':null,'docOrderGoodsStructureExt':{'id':null,'costPrice':14000,'costAmount':14000,'numDeclare':1,'priceDeclare':0,'amountDeclare':0,'priceTaxDeclare':0,'vatFeeDeclare':0,'salesFeeDeclare':0,'freightDeclare':0,'taxRateDeclare':0,'salesRate':0,'vatRate':0}}]}";
//        JSONObject jsonObject = JSONObject.parseObject(body);
//
//        Object orderInfo="OrderInfo(order=Order(uidNo=null, shopNo=1, channelNo=, typeBiz=1000020, typeId=1000201, sourceNo=null, sourcePlatformNo=H1811271578618553410, makeTime=Tue Nov 27 23:24:54 CST 2018, amountPayment=16673, amount=16673, amountTax=1773, amountGoods=14900, deposit=0, amountDiscount=0, amountOther=0, amountFreight=0, currency=CNY, remark=null, remarkAdmin=null, paymentMethod=0, payIdNo=10517, payNo=null, payTime=null, couponGetNo=, activityNo=, supplierNo=), orderConsignee=OrderConsignee(consignee=李清, mobile=13757122378, country=0, province=330000, city=330100, district=330103, countryName=0, provinceName=null, cityName=null, districtName=null, address=绍兴路161号野风现代中心北楼5A02, addressAll= 浙江 杭州 下城 绍兴路161号野风现代中心北楼5A02, idCard=, expressNo=0, expressIdNo=0, buyerName=李清, buyerIdCard=320522198212170025), goodsLists=[GoodsList(goods=Goods(name=爱他美白金版婴幼儿奶粉3段, num=1, numUnit=1, sku=A200001, barcode=9421902960055, priceMarket=16800, priceShop=14900, price=14900, amount=14900, freight=0, remark=null, taxRate=0, salesFee=0, vatFee=1773, priceTax=1773, activityNo=, gtsNo=null, expirationDate=null, warehouseNo=W1810291073106938557, conditionWarehouse=1005001, conditionBatch=1005001, conditionExpirationDate=1005001, conditionError=1005201), declare=Declare(costPrice=14000, costAmount=14000, numDeclare=1, priceDeclare=0, amountDeclare=0, priceTaxDeclare=0, vatFeeDeclare=0, salesFeeDeclare=0, freightDeclare=0, taxRateDeclare=0, salesRate=0, vatRate=0), childDataList=null, typeId=1001201)], header=Header(appId=0, shopId=0, tenantId=0, channelNo=, token=, version=0))\n";
//
////        OrderInfoDto OrderInfoDto = (OrderInfoDto)orderInfo;
////        System.out.println(OrderInfoDto);
//
//        JSONObject orderJson = JSONObject.parseObject(jsonObject.get("docOrder").toString());
//        JSONObject orderConsigneeJson = JSONObject.parseObject(jsonObject.get("docOrderConsignee").toString());
//        orderJson.putAll(orderConsigneeJson);
//        List goodsinfoLists = (List)jsonObject.get("goodsinfoLists");
//        JSONObject parseObject = new JSONObject();
//        for (Object o :goodsinfoLists){
//            parseObject = JSONObject.parseObject(o.toString());
//        }
//
//        try {
//            GoodsInfo goodsInfo = defaultMapper.readValue(parseObject.toJSONString(), GoodsInfo.class);
//            System.out.println("goodsInfo"+goodsInfo.getDocOrderGoods());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(orderJson);
//        System.out.println(orderConsigneeJson);
//        System.out.println(parseObject.get("docOrderGoodsStructureExt"));
//        System.out.println(parseObject.get("docOrderGoods"));


//        List<User> users = new ArrayList<>();
//
//        for (int i=0 ; i<3;i++){
//            User user = new User();
//            user.setName("tt"+i);
//            user.setMail(i+"hh");
//            users.add(user);
//        }
//
//        try {
//            String s = objectMapper.writeValueAsString(users);
//            System.out.println(s);
//            JSONArray jsonArray = JSONObject.parseArray(s);
//            List<User> users1 = JSONArray.parseArray(s, User.class);
//            System.out.println(users1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        UserInfo userInfo = new UserInfo();
//        User user = new User();
//        userInfo.setUser(user);
//        userInfo.getUser().setName("heiheihei");
//        userInfo.getUser().setMail("3241654");
//        UacUserProfile uacUserProfile = new UacUserProfile();
//        userInfo.setUacUserProfile(uacUserProfile);
//        userInfo.getUacUserProfile().setAddress("43546123");
//        userInfo.getUacUserProfile().setHead("sefgnlk");
//        try {
//            String s = objectMapper.writeValueAsString(userInfo);
//            System.out.println(s);
//            JSONArray jsonObject = JSONObject.parseArray(s);
//
//
//
//
//            System.out.println(jsonObject);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

//        AtomicInteger transactionIndex = new AtomicInteger(4);
//        System.out.println(transactionIndex);
//        int retState = transactionIndex.getAndIncrement();
//        System.out.println(retState);
//        int status = 1 % 3;
//        System.out.println(status);
//        int transId = 1;
//        System.out.printf("------ !!! checkLocalTransaction is executed once," +
//                        " msgTransactionId=%s, TransactionState=%s status=%s %n",
//                transId, retState, status);

//        User user = new User();
//        user.setName("heiheihei");
//        user.setMail("3241654");
//        user.setSourceNo(11L);
//
//        String json = "{'mail':'3241654','name':'heiheihei','sourceNo':11}";
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        User user1 = JSONObject.toJavaObject(jsonObject, user.getClass());
//        System.out.println(user1);


//        Map map = JSON.parseObject(JSON.toJSONString(user), Map.class);
//        String s1 = JSON.toJSONString(user);
//
//        System.out.println(JSONObject.parseObject(s1));
//        JSONObject mapjson =new JSONObject(map);
//        System.out.println(mapjson);
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(objectMapper.writeValueAsString(user));
//            byte[] bytes = jsonObject.toJSONString().getBytes();
//            byte[] json = objectMapper.writeValueAsBytes(jsonObject);
//            String s = new String(json);
//            String s2 = new String(bytes);
//            System.out.println(s);
//            System.out.println(s2);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


    }

    public static String set(String id) {
        String vo = id +"++";

        return vo;
    }

    public static User get(UserInfo userInfo) {
        User user = userInfo.getUser();
        UacUserProfile uacUserProfile = userInfo.getUacUserProfile();
        System.out.println("+++++++");

        return user;
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 一个实体类含有多个对象数据(可含有基本类型)转一个json数据
     *
     * @return
     */
    public static <T> JSONObject getJson(T t) {
        Preconditions.checkArgument(t != null, "object不能为空!");
        JSONObject json = new JSONObject();
        String jsonStr = "";

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            String type = field.getType().toString();
            String comName = "com";
            try {
                if (type.contains(comName)) {
                    Object o = field.get(t);
//                    jsonStr += JacksonUtil.toJson(o) + ",";
                    JSONObject jsonObject = JSONObject.parseObject(objectMapper.writeValueAsString(o));


                } else {
                    json.put(field.getName(), field.get(t));
                }
            } catch (Exception e) {
                e.printStackTrace();
//                log.error("对象数据转一个json数据出错! - e={}", e.getMessage());
            }
        }
        jsonStr += json.toJSONString();
//        log.info("对象数据转一个json数据 - {}", jsonStr);
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * 多个对象数据转多个json数据
     *
     * @param list
     * @return
     */
    public static List<JSONObject> getJsons(List<Object> list) {
        Preconditions.checkArgument(list != null && !list.isEmpty(), "list不能为空!");
        List<JSONObject> jsons = new ArrayList<>();

        for (Object obj : list) {
            jsons.add(getJson(obj));
        }
//        log.info("多个对象数据转多个json数据 - {}", jsons);
        return jsons;
    }
}
