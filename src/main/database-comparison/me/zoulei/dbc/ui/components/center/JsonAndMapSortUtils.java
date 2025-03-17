package me.zoulei.dbc.ui.components.center;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonAndMapSortUtils {
 
    /**
     * map排序
     * @param map
     * @param keySort
     * @param <k>
     * @param <v>
     * @return
     */
    public static <k,v> List mapByKeyToSort(Map<k,v> map , final Comparator keySort){
        List<Map.Entry<k,v>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, (o1, o2) -> keySort.compare(o1.getKey(),o2.getKey()));
        System.out.println("排序=====");
        entryList.forEach(m->{
            System.out.println(m.getKey()+"===>"+m.getValue());
        });
        return entryList;
    }
 
    /**
     * JSONArray排序
     * @param jsonArray
     * @param fildName
     * @param isAsc
     * @return
     */
    public static JSONArray jsonArrayToSort(JSONArray jsonArray,final String fildName,final boolean isAsc){
        JSONArray afterSortJsonArray = new JSONArray();
        List<JSONObject> objectList = new ArrayList<>();
        jsonArray.forEach(obj ->{
            objectList.add((JSONObject)obj);
        });
        Collections.sort(objectList, (o1, o2) -> {
            String fildValueA = o1.getString(fildName);
            String fildValueB = o2.getString(fildName);
            if (isAsc){
                return fildValueA.compareTo(fildValueB);
            }
            return fildValueB.compareTo(fildValueA);
        });
        objectList.forEach(obj->{
            afterSortJsonArray.add(obj);
        });
        return afterSortJsonArray;
    }
 
    /**
     *准备map测试数据
     */
    public static Map<String,String> getMapData(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("key1","测试1");
        map.put("key3","测试3");
        map.put("key5","测试5");
        map.put("key2","测试2");
        map.put("key4","测试4");
        return map;
    }
    /**
     *准备json测试数据
     */
    public static JSONArray getJsonArrayData(){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("userId","1001");
        jsonObject1.put("name","测试1");
        jsonArray.add(jsonObject1);
 
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("userId","1003");
        jsonObject3.put("name","测试3");
        jsonArray.add(jsonObject3);
 
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("userId","1002");
        jsonObject2.put("name","测试2");
        jsonArray.add(jsonObject2);
 
        return jsonArray;
    }
     
    public static void main(String[] args) {
        Map<String,String> map = JsonAndMapSortUtils.getMapData();
        JSONArray jsonArray = JsonAndMapSortUtils.getJsonArrayData();
        //List afterSortMap = JsonAndMapSortUtils.mapByKeyToSort(map,);
 
        JSONArray afterSortJsonArray_isAsc = JsonAndMapSortUtils.jsonArrayToSort(jsonArray,"userId",true);
        JSONArray afterSortJsonArray_noAsc = JsonAndMapSortUtils.jsonArrayToSort(jsonArray,"userId",false);
 
        System.out.println("map排序前："+map);
        //System.out.println("map排序后："+afterSortMap+"\n");
 
        System.out.println("JsonArray排序前："+jsonArray);
        System.out.println("JsonArray排序后==》升序："+afterSortJsonArray_isAsc);
        System.out.println("JsonArray排序后==》降序："+afterSortJsonArray_noAsc);
    }
 
}
