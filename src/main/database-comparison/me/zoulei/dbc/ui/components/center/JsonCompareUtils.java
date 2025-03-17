package me.zoulei.dbc.ui.components.center;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

public class JsonCompareUtils {
    //标志位：对json报文中含有JsonArray类型的数据是否进行排序
    private static boolean isSort;
 
    private Map<String, Object> oldJsonToMap = new LinkedHashMap<>();
    private Map<String, Object> newJsonToMap = new LinkedHashMap<>();
 
    //每一个实体里的排序字段
    private static Map<String, String> filedNameMap = new HashMap<>();
 
    static {
        filedNameMap.put("ojb1", "id");
        filedNameMap.put("ojb2", "id");
    }
 
    //可以跳过比对的字段
    private static String[] skipCompareFiledNameMap = {};
 
    /**
     * 两json报文比对入口
     *
     * @param oldJsonStr
     * @param newJsonStr
     * @return
     */
    public JSONObject compare2Json(String JsonStr1, String JsonStr2) {
        /**
         * 递归遍历json对象所有的key-value，以map形式的path:value进行存储
         * 然后对两个map进行比较
         */
        convertJsonToMap(JSON.parseObject(JsonStr1,Feature.OrderedField), "", false);
        convertJsonToMap(JSON.parseObject(JsonStr2,Feature.OrderedField), "", true);
        //获取比较结果
        Map<String, Object> differenceMap = compareTwoMaps(oldJsonToMap, newJsonToMap);
        //String diffJsonResult = convertMapToJson(differenceMap);
        return convertMapToJson(differenceMap);
    }
 
    /**
     * 将json数据转换为map存储--用于后续比较map
     *
     * @param json
     * @param root
     * @param isNew 区别新旧报文
     */
    private void convertJsonToMap(Object json, String root, boolean isNew) {
        if (json instanceof JSONObject) {
            JSONObject jsonObject = ((JSONObject) json);
            Iterator iterator = jsonObject.keySet().iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Object value = jsonObject.get(key);
                String newRoot = "".equals(root) ? key + "" : root + "." + key;
                fillInResultMap(value, newRoot, isNew);
            }
        } else if (json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            //将jsonArray进行排序
            if (isSort) {
                //需要排序
                String sortEntityName = root.substring(root.lastIndexOf(".") + 1);
                //需要排序 获取排序字段
                String sortFiledName = filedNameMap.get(sortEntityName);
                if (!StringUtils.isEmpty(sortFiledName)) {
                    jsonArray = JsonAndMapSortUtils.jsonArrayToSort(jsonArray, sortFiledName, true);
                }
            }
            final JSONArray jsonArray1 = jsonArray;
            Stream.iterate(0, integer -> integer + 1).limit(jsonArray1.size()).forEach(index -> {
                Object value = jsonArray1.get(index);
                String newRoot = "".equals(root) ? "[" + index + "]" : root + ".[" + index + "]";
                fillInResultMap(value, newRoot, isNew);
            });
        }
    }
 
    /**
     * 封装json转map后的数据
     *
     * @param value
     * @param newRoot
     * @param isNew   区别新旧json
     */
    public void fillInResultMap(Object value, String newRoot, boolean isNew) {
        if (value instanceof JSONObject || value instanceof JSONArray) {
            convertJsonToMap(value, newRoot, isNew);
        } else {
            //设置跳过比对的字段，直接不装入map
            boolean check = ArrayUtils.contains(JsonCompareUtils.skipCompareFiledNameMap, newRoot);
            if (!check){
                if (!isNew) {
                    oldJsonToMap.put(newRoot, value);
                } else {
                    newJsonToMap.put(newRoot, value);
                }
            }
        }
    }
 
    /**
     * 比较两个map，将不同的数据以map形式存储并返回
     *
     * @param oldJsonMap
     * @param newJsonMap
     * @return
     */
    private Map<String, Object> compareTwoMaps(Map<String, Object> oldJsonMap, Map<String, Object> newJsonMap) {
        //1.将newJsonMap的不同数据装进oldJsonMap，同时删除oldJsonMap中与newJsonMap相同的数据
        newJsonMap.forEach((k, v) -> {
            Map<String, Object> differenceMap = new LinkedHashMap<>();
            if (oldJsonMap.containsKey(k)) {
                Object oldValue = oldJsonMap.get(k);
                if(v==null) {
                	v="";
                }
                if(oldValue==null) {
                	oldValue="";
                }
                if (v.equals(oldValue)) {
                    oldJsonMap.remove(k);
                } else {
                    differenceMap.put("库1", oldValue);
                    differenceMap.put("库2", v);
                    oldJsonMap.put(k, differenceMap);
                }
            } else {
                differenceMap.put("库1", "不存在 " + k);
                differenceMap.put("库2", v);
                differenceMap.put("info", 1);
                oldJsonMap.put(k, differenceMap);
            }
        });
        //2.统一oldJsonMap中newMap不存在的数据的数据结构，便于解析
        oldJsonMap.forEach((k, v) -> {
            if (!(v instanceof Map)) {
                Map<String, Object> differenceMap = new LinkedHashMap<>();
                differenceMap.put("库1", v);
                differenceMap.put("库2", "不存在 " + k);
                differenceMap.put("info", 2);
                oldJsonMap.put(k, differenceMap);
            }
        });
        return oldJsonMap;
    }
 
    /**
     * 将已经找出不同数据的map根据key的层级结构封装成json返回
     *
     * @param map
     * @return
     */
    private JSONObject convertMapToJson(Map<String, Object> map) {
        JSONObject resultJSONObject = new JSONObject(true);
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> item = it.next();
            String key = item.getKey();
            Object value = item.getValue();
            String[] paths = key.split("\\.");
            int i = 0;
            //用於深度標識對象
            Object remarkObject = null;
            int indexAll = paths.length - 1;
            while (i <= paths.length - 1) {
                String path = paths[i];
                if (i == 0) {
                    //初始化对象标识
                    if (resultJSONObject.containsKey(path)) {
                        remarkObject = resultJSONObject.get(path);
                    } else {
                        if (indexAll > i) {
                            if (paths[i + 1].matches("\\[[0-9]+\\]")) {
                                remarkObject = new JSONArray();
                            } else {
                                remarkObject = new JSONObject(true);
                            }
                            resultJSONObject.put(path, remarkObject);
                        } else {
                            resultJSONObject.put(path, value);
                        }
                    }
                    i++;
                    continue;
                }
                //匹配集合对象
                if (path.matches("\\[[0-9]+\\]")) {
                    int startIndex = path.lastIndexOf("[");
                    int endIndext = path.lastIndexOf("]");
                    int index = Integer.parseInt(path.substring(startIndex + 1, endIndext));
                    if (indexAll > i) {
                        if (paths[i + 1].matches("\\[[0-9]+\\]")) {
                            while (((JSONArray) remarkObject).size() <= index) {
                                if (((JSONArray) remarkObject).size() == index) {
                                    ((JSONArray) remarkObject).add(index, new JSONArray());
                                } else {
                                    ((JSONArray) remarkObject).add(null);
                                }
                            }
                        } else {
                            while (((JSONArray) remarkObject).size() <= index) {
                                if (((JSONArray) remarkObject).size() == index) {
                                    ((JSONArray) remarkObject).add(index, new JSONObject(true));
                                } else {
                                    ((JSONArray) remarkObject).add(null);
                                }
                            }
                        }
                        remarkObject = ((JSONArray) remarkObject).get(index);
                    } else {
                        while (((JSONArray) remarkObject).size() <= index) {
                            if (((JSONArray) remarkObject).size() == index) {
                                ((JSONArray) remarkObject).add(index, value);
                            } else {
                                ((JSONArray) remarkObject).add(null);
                            }
                        }
                    }
                } else {
                    if (indexAll > i) {
                        if (paths[i + 1].matches("\\[[0-9]+\\]")) {
                            if (!((JSONObject) remarkObject).containsKey(path)) {
                                ((JSONObject) remarkObject).put(path, new JSONArray());
                            }
                        } else {
                            if (!((JSONObject) remarkObject).containsKey(path)) {
                                ((JSONObject) remarkObject).put(path, new JSONObject(true));
                            }
                        }
                        remarkObject = ((JSONObject) remarkObject).get(path);
                    } else {
                        ((JSONObject) remarkObject).put(path, value);
                    }
                }
                i++;
            }
        }
        return resultJSONObject;
    }
 
    public boolean isSort() {
        return isSort;
    }
 
    public void setSort(boolean sort) {
        isSort = sort;
    }
 
    public static void main(String[] args) {
        String oldStr = "{\"abilityLevel\":\"2020101240000000000002\",\"activityId\":\"202208301413310000412\",\"addLibTime\":1662083360000,\"ansKnowModelIds\":\"1#201812051814150000475\",\"answer\":\"AB\",\"ascriptionItemLib\":\"0\",\"attributeList\":[{\"content\":\"<p>A</p>\",\"id\":\"202209020949170005783\",\"itemId\":\"202209020949150001521\",\"knowModelRelId\":\"201812051814150000475\",\"name\":\"A\",\"sort\":1,\"type\":\"option\"},{\"content\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">B</p>\",\"id\":\"202209020949170005784\",\"itemId\":\"202209020949150001521\",\"name\":\"B\",\"sort\":2,\"type\":\"option\"},{\"content\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">C</p>\",\"id\":\"202209020949170005785\",\"itemId\":\"202209020949150001521\",\"name\":\"C\",\"sort\":3,\"type\":\"option\"},{\"content\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">D</p>\",\"id\":\"202209020949170005786\",\"itemId\":\"202209020949150001521\",\"name\":\"D\",\"sort\":4,\"type\":\"option\"},{\"content\":\"11\",\"createTime\":1662083368000,\"createTimeString\":\"2022-09-02 09:49:28\",\"creater\":\"10001\",\"id\":\"202209020949170005787\",\"itemId\":\"202209020949150001521\",\"name\":\"大纲依据\",\"searchFlag\":\"1\",\"sort\":1,\"subItemTypeAttrId\":\"100041\",\"type\":\"expandAttr\"},{\"content\":\"11\",\"createTime\":1662083370000,\"createTimeString\":\"2022-09-02 09:49:30\",\"creater\":\"10001\",\"id\":\"202209020949280005788\",\"itemId\":\"202209020949150001521\",\"name\":\"教材依据\",\"searchFlag\":\"1\",\"sort\":2,\"subItemTypeAttrId\":\"100042\",\"type\":\"expandAttr\"},{\"content\":\"11\",\"createTime\":1662083371000,\"createTimeString\":\"2022-09-02 09:49:31\",\"creater\":\"10001\",\"id\":\"202209020949290005789\",\"itemId\":\"202209020949150001521\",\"name\":\"关键字\",\"searchFlag\":\"1\",\"sort\":3,\"subItemTypeAttrId\":\"100043\",\"type\":\"expandAttr\"}],\"auditCount\":0,\"auditStatus\":0,\"childItemList\":[],\"createTime\":1662083350000,\"createTimeString\":\"2022-09-02 09:49:10\",\"creater\":\"10001\",\"delFlag\":\"0\",\"difficult\":\"3\",\"id\":\"202209020949150001521\",\"isComposite\":0,\"isTopVersion\":0,\"itemCode\":\"KJCJ202209020949140001501\",\"itemContent\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">2.<span style=\\\"color: #333333; font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 14px; text-align: justify; background-color: #FFFFFF;\\\">按照并购双方行业相关性划分，判断并购类型（横向并购or混合并购）</span></p>\",\"itemKnowledgeList\":[{\"id\":\"202209020949300001537\",\"itemId\":\"202209020949150001521\",\"knowledgeId\":\"1240004\",\"knowledgeStr\":\"2020年经济法基础/第一章总论/第一节法律基础/一、法和法律/（一）法和法律的概念\",\"level\":\"1\",\"pid\":\"202209020946580001519\"}],\"itemLevel\":1,\"itemSource\":\"202208301413310000412\",\"itemTypeId\":\"4\",\"itemVersion\":\"1\",\"keyWord\":\"\",\"knowledgeId\":\"1240004\",\"knowledgeStr\":\"2020年经济法基础/第一章总论/第一节法律基础/一、法和法律/（一）法和法律的概念\",\"knowledgeSyllabusName\":\"2020年经济法基础\",\"lockStatus\":1,\"modifyChlidItems\":[],\"paperCount\":0,\"paperItemRelation\":{},\"paperStructure\":{},\"parentId\":\"202209020946580001519\",\"processedItemContent\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">2.<span style=\\\"color: #333333; font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 14px; text-align: justify; background-color: #FFFFFF;\\\">按照并购双方行业相关性划分，判断并购类型（横向并购or混合并购）</span></p>\",\"score\":2,\"sort\":2,\"sourceType\":\"2\",\"structId\":\"\",\"subItemTypeId\":\"20180228155501000004\",\"subjectId\":\"4\",\"updateTime\":1662083350000,\"updateTimeString\":\"2022-09-02 09:49:10\",\"updater\":\"10001\"}";
        String newStr = "{\"abilityLevel\":\"2020101240000000000002\",\"activityId\":\"202208301413310000412\",\"addLibTime\":1662083360000,\"analysis\":\"\",\"ansKnowModelIds\":\"1#201812051814150000475\",\"answer\":\"AB\",\"ascriptionItemLib\":\"0\",\"attributeList\":[{\"content\":\"<p>A</p>\",\"id\":\"202209021135210005855\",\"itemId\":\"202209020949150001521\",\"knowModelRelId\":\"201812051814150000475\",\"name\":\"A\",\"sort\":1,\"type\":\"option\"},{\"content\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">B</p>\",\"id\":\"202209021135210005856\",\"itemId\":\"202209020949150001521\",\"name\":\"B\",\"sort\":2,\"type\":\"option\"},{\"content\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">C</p>\",\"id\":\"202209021135210005857\",\"itemId\":\"202209020949150001521\",\"name\":\"C\",\"sort\":3,\"type\":\"option\"},{\"content\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">D</p>\",\"id\":\"202209021135210005858\",\"itemId\":\"202209020949150001521\",\"name\":\"D\",\"sort\":4,\"type\":\"option\"},{\"content\":\"11\",\"createTime\":1662089721000,\"createTimeString\":\"2022-09-02 11:35:21\",\"creater\":\"10001\",\"id\":\"202209021135210005859\",\"itemId\":\"202209020949150001521\",\"name\":\"大纲依据\",\"searchFlag\":\"1\",\"sort\":1,\"subItemTypeAttrId\":\"100041\",\"type\":\"expandAttr\"},{\"content\":\"11\",\"createTime\":1662089721000,\"createTimeString\":\"2022-09-02 11:35:21\",\"creater\":\"10001\",\"id\":\"202209021135210005860\",\"itemId\":\"202209020949150001521\",\"name\":\"教材依据\",\"searchFlag\":\"1\",\"sort\":2,\"subItemTypeAttrId\":\"100042\",\"type\":\"expandAttr\"},{\"content\":\"11\",\"createTime\":1662089722000,\"createTimeString\":\"2022-09-02 11:35:22\",\"creater\":\"10001\",\"id\":\"202209021135210005861\",\"itemId\":\"202209020949150001521\",\"name\":\"关键字\",\"searchFlag\":\"1\",\"sort\":3,\"subItemTypeAttrId\":\"100043\",\"type\":\"expandAttr\"}],\"auditCount\":0,\"auditStatus\":0,\"childItemList\":[],\"createTime\":1662083350000,\"createTimeString\":\"2022-09-02 09:49:10\",\"creater\":\"10001\",\"delFlag\":\"0\",\"difficult\":\"5\",\"id\":\"202209020949150001521\",\"isComposite\":0,\"isTopVersion\":0,\"itemCode\":\"KJCJ202209020949140001501\",\"itemContent\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">2.<span style=\\\"color: #333333; font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 14px; text-align: justify; background-color: #FFFFFF;\\\">按照并购双方行业相关性划分，判断并购类型（横向并购or混合并购）</span></p>\",\"itemKnowledgeList\":[{\"id\":\"202209021135230001557\",\"itemId\":\"202209020949150001521\",\"knowledgeId\":\"1240004\",\"knowledgeStr\":\"2020年经济法基础/第一章总论/第一节法律基础/一、法和法律/（一）法和法律的概念\",\"level\":\"1\",\"pid\":\"202209020946580001519\"}],\"itemLevel\":1,\"itemSource\":\"202208301413310000412\",\"itemTypeId\":\"4\",\"itemVersion\":\"2\",\"keyWord\":\"\",\"knowledgeId\":\"1240004\",\"knowledgeStr\":\"2020年经济法基础/第一章总论/第一节法律基础/一、法和法律/（一）法和法律的概念\",\"knowledgeSyllabusName\":\"2020年经济法基础\",\"lockStatus\":1,\"modifyChlidItems\":[],\"paperCount\":0,\"paperItemRelation\":{},\"paperStructure\":{},\"parentId\":\"202209020946580001519\",\"processedItemContent\":\"<p style=\\\"font-family:&#39;Times New Roman&#39;,&#39;宋体&#39;;font-size:12pt;padding:0;margin:0;\\\">2.<span style=\\\"color: #333333; font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 14px; text-align: justify; background-color: #FFFFFF;\\\">按照并购双方行业相关性划分，判断并购类型（横向并购or混合并购）</span></p>\",\"score\":2,\"sort\":2,\"sourceType\":\"2\",\"structId\":\"\",\"subItemTypeId\":\"20180228155501000004\",\"subjectId\":\"4\",\"updateTime\":1662089720000,\"updateTimeString\":\"2022-09-02 11:35:20\",\"updater\":\"10001\"}";
        System.out.println(new JsonCompareUtils().compare2Json(oldStr, newStr));
        System.out.println("\n========测试复杂json的比对============");
    }
}
