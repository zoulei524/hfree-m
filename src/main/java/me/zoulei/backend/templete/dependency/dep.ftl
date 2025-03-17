package com.insigma.business.components.hyfield;

import java.io.Serializable;

import lombok.Data;

/**
 * 字段 这种字段结构主要针对代码字段，通过MapToHYDtoUtil.java将Map<String, Object>类型转成对应的dto
 * hibernate方式获取：
 * 实体类对象上添加注解如
 *  @Type(type ="com.insigma.business.components.hyfield.HYfieldType", 
 * 		parameters = { @Parameter(name ="codetype", value ="GB8561"), @Parameter(name ="p", value ="E")})
 * 2023年10月26日11:08:56 zoulei
 *
 *
 * 2024年1月19日11:15:49
 * 加上时间显示4位点2位
 */
@Data
public class HYField  implements Serializable{
	
	/**值*/
	private String value = "";
	/**若有代码，就有代码值，没有就为空*/
	private String key;
	/**R—必填，E—可编辑，H—隐藏，D—不可编辑, E,R—编辑可变为必填*/
	private String p = "E";
	
	/**代码类别*/
	private String codetype;
	
	/**如果是时间显示4位点2位*/
	private String time = "";

	public HYField() {
	}
	
	public HYField(String codetype) {
		this.codetype = codetype;
	}
	
	public HYField(String codetype, String p) {
		this.codetype = codetype;
		if(p!=null&&!"".equals(p))
			this.p = p;
	}

	@Override
	public String toString() {
		if(this.codetype!=null && !"".equals(this.codetype)) {
			if(this.key!=null && !"".equals(this.key)) {
				return this.key;
			}else {
				return this.value;
			}
		}
		return this.value;
	}
	
}



/******************************分隔符****************************************************************************/








package com.insigma.business.components.hyfield;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.insigma.business.ZZGBGL.ZZGBGL_GBZLGL.ZZGBGL_GBZLGL001.ZZGBGL_GBZLGL001_0001.controller.CadreRecruitmentController;
import com.insigma.business.components.pub.SysCodeUtil;
import com.insigma.common.exception.ServiceException;
import com.insigma.framework.exception.AppException;

import cn.hutool.core.util.ObjectUtil;

/**
 * 2023年10月27日09:31:43 zoulei
 * f_rmb1: {
                // 任免表（一）
                status: { value: "", p: "H" },
                a0000: { value: this.propsa0000, p: "H" },
                a0101: { value: "", p: "R" },
                a0104: { key: "", value: "", p: "R" }, // 性别
                a0107: { key: "", value: "", p: "R" }, // 出生年月
            }
 	为了让bean可以直接生成上面的这种结构，增加HYField类型作为字段类型。
    可以将将Map<String, Object>类型或上面这种结构的JSONObject转成对应的dto bean或实体bean。 bean可以有HYField类型的字段。
 */
@Component
public class HYBeanUtil {
	
	@Autowired
	SysCodeUtil sysCodeUtil;

    Log log= LogFactory.getLog(CadreRecruitmentController.class);
	
	/**
     * 将数据库查询返回的map对象转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     */
    public <E> List<E> toHYBean(List<Map<String, Object>> mapList, Class<E> entityClass) throws AppException {
        if (ObjectUtil.isNotEmpty(mapList)) {
            List<E> eList = new ArrayList<>();
            for (Map<String, Object> objectMap : mapList) {
            	eList.add(toHYBean(objectMap, entityClass));
			}
            return eList;
        }
        return null;
    }
 
    /**
     * 将数据库查询返回的map对象转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     */
    public <E> E toHYBean(Map<String, Object> map, Class<E> entityClass) throws AppException {
        try {
           
            E entity = entityClass.newInstance();
            List<Field> declaredFields = getAllFiled(entity);
            for (Field field : declaredFields) {
                String key = field.getName().toLowerCase();
                
                Object object = map.get(key);
                
                if (object != null) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    //主要是针对代码字段
                    if (field.getType() == HYField.class) {
                    	
                    	//获取注解
                    	Map<String, String> anno = new HashMap<String, String>();
                    	Type[] annotationsByType = field.getAnnotationsByType(org.hibernate.annotations.Type.class);
                    	Type type = annotationsByType[0];
                    	Parameter[] parameters = type.parameters();
                    	for(Parameter p : parameters) {
                    		anno.put(p.name(), p.value());
                    	}
                    	// 2024年1月18日10:14:02 根据注解设置对象的值
                    	String p_type = anno.get("type");
                    	String p_codetype = anno.get("codetype");
                    	String p_p = anno.get("p");
                    	
                    	//dto里只允许这种HYField类型的字段
                    	// 2024年1月18日10:14:54 HYField直接创建新对象
                    	HYField hf = new HYField();
                    	field.set(entity, hf);
                        
                    	if(object.getClass() == JSONObject.class) {
                    		JSONObject hyObject = (JSONObject) object;
                        	String k = hyObject.getString("key");
                        	String v = hyObject.getString("value");
                        	String p = hyObject.getString("p");
                            hf.setValue(v);
    						hf.setKey(k);
    						hf.setP(p);
    						hf.setCodetype(hyObject.getString("codetype"));
    						hf.setTime(hyObject.getString("time"));
                    	}else {
                    		hf.setCodetype(p_codetype);
                    		hf.setP(p_p);
                    		//2024年1月18日17:32:55  下拉框或弹出框， 或者未定义类型按照代码转换赋值
                    		if(HYfieldType.POPWIN.equals(p_type)||HYfieldType.SELECT.equals(p_type)||p_type==null||p_type.equals("")) {
                    			//设置codetype的值
                            	String codetype = hf.getCodetype();
                            	if(codetype!=null) {
            						String codeName = sysCodeUtil.getCodeName(codetype, object.toString());
            						hf.setValue(codeName);
            						hf.setKey(object.toString());
                            	}else {
                            		hf.setValue(object.toString());
                            	}
                    		}else if(HYfieldType.DATE.equals(p_type)) {
                    			hf.setValue(object.toString());
        						hf.setKey(object.toString());
        						String v = object.toString();
        		                v = v.replace(".", "");
        						if(v.length()>=6) {
        							hf.setTime(v.substring(0,4)+"."+v.substring(4,6));
        						}else {
        							hf.setTime("");
        						}
                    		}
                    		
                    		
                    	}
                    	
                    }else
                    //其他类型
                    if (object instanceof BigInteger) {
                        BigInteger bigInteger = (BigInteger) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, bigInteger.longValue());
                        }
                    } else if (object instanceof Timestamp) {
                        Timestamp timestamp = (Timestamp) object;
                        if (field.getType() == LocalDateTime.class) {
                            field.set(entity, timestamp.toLocalDateTime());
                        } else if (field.getType() == Timestamp.class) {
                            field.set(entity, timestamp);
                        }else if (field.getType() == java.util.Date.class) {
                        	//Timestamp->java.util.Date
                        	// 将Timestamp转换为LocalDateTime
                            LocalDateTime localDateTime = timestamp.toLocalDateTime();
                            // 指定要转换到的时区（这里使用默认系统时区）
                            ZoneId zoneId = ZoneId.systemDefault();
                            // 根据时区获取ZonedDateTime对象
                            java.time.ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
                            // 将ZonedDateTime转换为Date对象
                            java.util.Date date = Date.from(zonedDateTime.toInstant());
                        	field.set(entity, date);
                        }
                    } else if (object instanceof Date) {
                        Date date = (Date) object;
                        if (field.getType() == LocalDate.class) {
                            field.set(entity, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        } else if (field.getType() == Date.class) {
                            field.set(entity, date);
                        }
                    } else if (object instanceof Byte) {
                        Byte bytes = (Byte) object;
                        if (field.getType() == Byte.class) {
                            field.set(entity, bytes);
                        } else if (field.getType() == Boolean.class) {
                            field.set(entity, bytes.intValue() == 1);
                        }
                    } else if (object instanceof Integer) {
                    	Integer integer = (Integer) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, integer.longValue());
                        } else if (field.getType() == object.getClass()) {
                            field.set(entity, object);
                        }
                    } else if (field.getType() == object.getClass()) {
                        field.set(entity, object);
                    }
                    Object value = field.get(entity);
                    field.setAccessible(accessible);
                    if (value == null) {
                        throw new ClassCastException(String.format("%s.%s 无法将 %s 转换为 %s ", entity.getClass(), field.getName(), object.getClass(), field.getType()));
                    }
                }
                
            }
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AppException(ex.getMessage());
 
        }
 
 
    }
 
    /**
     * 获取自身所以属性且超类属性
     *
     * @param object
     * @return
     */
    private List<Field> getAllFiled(Object object) {
        Class<Field> clazz = (Class<Field>) object.getClass();
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<Field>) clazz.getSuperclass();
        }
        return fields;
    }
    
    
    
    /**
     * 将JSONObject转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     * 需JSONObject的key都设置成小写
     * 如a0104: { key: "", value: "", p: "R" }
     */
    public <T> T pageElementToBean(JSONObject pageData, String formName, Class<T> clazz) throws AppException {
        JSONObject target = pageData.getJSONObject(formName);
        return pageElementToBean(target, clazz);
    }

    /**
     * 将JSONObject转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     * 需JSONObject的key都设置成小写
     * 如a0104: { key: "", value: "", p: "R" }
     */
    public <T> T pageElementToBean(JSONObject pageData, Class<T> clazz) throws AppException {
        
        try {
            T entity = clazz.newInstance();
            List<Field> declaredFields = getAllFiled(entity);
            String k, v, p;
            for (Field field : declaredFields) {
                String key = field.getName().toLowerCase();
                
                Object object = pageData.get(key);
                
                if (object != null&&!"".equals(object)) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    //主要是针对代码字段
                    if (field.getType() == HYField.class) {
                    	//获取注解
                    	Map<String, String> anno = new HashMap<String, String>();
                    	Type[] annotationsByType = field.getAnnotationsByType(org.hibernate.annotations.Type.class);
                    	Type type = annotationsByType[0];
                    	Parameter[] parameters = type.parameters();
                    	for(Parameter par : parameters) {
                    		anno.put(par.name(), par.value());
                    	}
                    	// 2024年1月18日10:14:02 根据注解设置对象的值
                    	String p_type = anno.get("type");
                    	String p_codetype = anno.get("codetype");
                    	String p_p = anno.get("p");
                    	
                    	//dto里只允许这种HYField类型的字段
                    	// 2024年1月18日10:14:54 HYField直接创建新对象
                    	HYField hf = new HYField();
                    	field.set(entity, hf);
                        
                    	if(object.getClass() == JSONObject.class) {
                    		JSONObject hyObject = (JSONObject) object;
                        	k = hyObject.getString("key");
                        	v = hyObject.getString("value");
                        	p = hyObject.getString("p");
                            hf.setValue(v);
    						hf.setKey(k);
    						hf.setP(p);
    						hf.setCodetype(hyObject.getString("codetype"));
    						hf.setTime(hyObject.getString("time"));
                    	}else {
                    		hf.setCodetype(p_codetype);
                    		hf.setP(p_p);
                    		//2024年1月18日17:32:55  下拉框或弹出框， 或者未定义类型按照代码转换赋值
                    		if(HYfieldType.POPWIN.equals(p_type)||HYfieldType.SELECT.equals(p_type)||p_type==null||p_type.equals("")) {
                    			//设置codetype的值
                            	String codetype = hf.getCodetype();
                            	if(codetype!=null) {
            						String codeName = sysCodeUtil.getCodeName(codetype, object.toString());
            						hf.setValue(codeName);
            						hf.setKey(object.toString());
                            	}else {
                            		hf.setValue(object.toString());
                            	}
                    		}else if(HYfieldType.DATE.equals(p_type)) {
                    			hf.setValue(object.toString());
        						hf.setKey(object.toString());
        						v = object.toString();
        		                v = v.replace(".", "");
        						if(v.length()>=6) {
        							hf.setTime(v.substring(0,4)+"."+v.substring(4,6));
        						}else {
        							hf.setTime("");
        						}
                    		}
                    		
                    		
                    	}
                        
                        
                    }else if(object.getClass() == JSONObject.class) {
                    	JSONObject hyObject = (JSONObject) object;
                    	k = hyObject.getString("key");
                        v = hyObject.getString("value");
                        if(k!=null&&!"".equals(k)) {
                        	field.set(entity, k);
                        }else {
                        	field.set(entity, v);
                        }
                        
                    }else
                    //其他类型
                    if (object instanceof BigInteger) {
                        BigInteger bigInteger = (BigInteger) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, bigInteger.longValue());
                        }
                    } else if (object instanceof Timestamp) {
                        Timestamp timestamp = (Timestamp) object;
                        if (field.getType() == LocalDateTime.class) {
                            field.set(entity, timestamp.toLocalDateTime());
                        }else if (field.getType() == Timestamp.class) {
                            field.set(entity, timestamp);
                        }else if (field.getType() == java.util.Date.class) {
                        	//Timestamp->java.util.Date
                        	// 将Timestamp转换为LocalDateTime
                            LocalDateTime localDateTime = timestamp.toLocalDateTime();
                            // 指定要转换到的时区（这里使用默认系统时区）
                            ZoneId zoneId = ZoneId.systemDefault();
                            // 根据时区获取ZonedDateTime对象
                            java.time.ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
                            // 将ZonedDateTime转换为Date对象
                            java.util.Date date = Date.from(zonedDateTime.toInstant());
                        	field.set(entity, date);
                        }
                    } else if (object instanceof Date) {
                        Date date = (Date) object;
                        if (field.getType() == LocalDate.class) {
                            field.set(entity, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        } else if (field.getType() == Date.class) {
                            field.set(entity, date);
                        }
                    } else if (object instanceof Byte) {
                        Byte bytes = (Byte) object;
                        if (field.getType() == Byte.class) {
                            field.set(entity, bytes);
                        } else if (field.getType() == Boolean.class) {
                            field.set(entity, bytes.intValue() == 1);
                        }
                    }else if (object instanceof Integer) {
                    	Integer integer = (Integer) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, integer.longValue());
                        } else if (field.getType() == object.getClass()) {
                            field.set(entity, object);
                        }
                    }else if (object instanceof Long) {
                    	Long lo = (Long) object;
                    	java.util.Date d = new java.util.Date(lo);
                        if (field.getType() == java.util.Date .class) {
                            field.set(entity, d);
                        } else if (field.getType() == object.getClass()) {
                            field.set(entity, object);
                        }
                    } else if (field.getType() == object.getClass()) {
                        field.set(entity, object);
                    }
                    Object value = field.get(entity);
                    field.setAccessible(accessible);
                    if (value == null) {
//                        throw new ClassCastException(String.format("%s.%s 无法将 %s 转换为 %s ", entity.getClass(), field.getName(), object.getClass(), field.getType()));
                        log.info(String.format("%s.%s 无法将 %s 转换为 %s ", entity.getClass(), field.getName(), object.getClass(), field.getType()));
                    }
                }
                
            }
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException(ex.getMessage());
 
        }
        
    }
    
}


/*****************************分隔符*****************************************************************************/




package com.insigma.business.components.hyfield;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext implements ApplicationContextAware {
    
	public static ApplicationContext applicationContext;

    public AppContext() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	AppContext.applicationContext = applicationContext;
    }
}




/******************************分隔符****************************************************************************/






package com.insigma.business.components.hyfield;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.jdbc.core.JdbcTemplate;

import com.insigma.business.QGGWY.QGGWY_PUB001.QGGWY_PUB001_0004.enums.ECODE;
import com.insigma.business.util.EhCacheUtil;

/**
 * 2024年1月16日10:05:02 zoulei
 * @see HYField
 * hibernate实体的转换
 */
public class HYfieldType implements UserType, DynamicParameterizedType{
	
	private static final int[] SQL_TYPES={Types.VARCHAR}; 
	
	private String codetype;
	private String p;
	private String type;
	
	@Override
	public void setParameterValues(Properties parameters) {
		this.codetype = parameters.getProperty("codetype");
		this.p = parameters.getProperty("p");
		this.type = parameters.getProperty("type");
	}
	
	@Override
	public Object assemble(Serializable serializable, Object obj)
			throws HibernateException {
		return serializable;
	}

	@Override
	public Object deepCopy(Object obj) throws HibernateException {
		return obj; 
	}

	@Override
	public Serializable disassemble(Object obj) throws HibernateException {
		return (Serializable) obj;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if(x==y){  
            return true;  
        }else if(x==null||y==null){  
            return false;  
        }else {  
            return x.equals(y);  
        }  
	}

	@Override
	public int hashCode(Object obj) throws HibernateException {
		return obj.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object replace(Object obj, Object obj1, Object obj2)
			throws HibernateException {
		return obj;
	}

	@Override
	public Class returnedClass() {
		return HYField.class;
	}

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String data = rs.getString(names[0]);
		if(rs.wasNull()){  
            return new HYField(codetype, p);  
        }else{  
            HYField hyField = new HYField(codetype, p);
            if(data==null){
            	return hyField;
            }
            
            //2024年1月18日17:32:55  下拉框或弹出框， 或者未定义类型按照代码转换赋值
            if(SELECT.equals(this.type)||POPWIN.equals(this.type)||"".equals(this.type)||this.type==null) {
            	hyField.setKey(data);
                String v = this.getCodeName(codetype, data, "");
                hyField.setValue(v);
            }else if(DATE.equals(this.type)){//日期
            	hyField.setKey(data);
                String v = data;
                hyField.setValue(v);
                v = v.replace(".", "");
				if(v.length()>=6) {
					hyField.setTime(v.substring(0,4)+"."+v.substring(4,6));
				}else {
					hyField.setTime("");
				}
    		}
            return hyField;  
        }  
	}
	
	/**
	 * 通过code_type及code_value获取code_name、code_name2、code_name3
	 *
	 * 样例: SysCodeUtil.getCodeName("ZB01", "110000", "3");
	 */
	@SuppressWarnings("unchecked")
	public  String getCodeName(String codetype,String codevalue, String codeNameNum) {
		if(codevalue == null || "".equals(codevalue)){
			return  "";
		}
		if(codetype == null || "".equals(codetype)){
			return  codevalue;
		}
		
		
		
		String codetypeNum = "".equals(codeNameNum) ? codetype : codetype + "_" + codeNameNum;
		HashMap<String,String> hashmap=(HashMap<String,String>)EhCacheUtil.getObjectInCache(codetypeNum+ECODE.SELECT.getCode());
		if(hashmap!=null) {
			return hashmap.get(codevalue);
		}else {
			String sql="select code_name from code_value where code_type='"+codetype+"' and code_value='"+codevalue+"' and CODE_STATUS = '1'";
			JdbcTemplate jdbcTemplate = (JdbcTemplate)(AppContext.applicationContext).getBean("jdbcTemplate");
			List<Map<String, Object>> codeDetail = jdbcTemplate.queryForList(sql);
			if (codeDetail != null && codeDetail.size() > 0) {
				Map<String, Object> codes = codeDetail.get(0);
				return codes.get("code_name").toString();
			}else {
				return "";
			}
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if(value==null){  
			st.setNull(index, Types.VARCHAR);  
        }else {  
        	HYField hyField = (HYField) value;
        	String k = hyField.getKey();
        	if(k==null || "".equals(k)) {
        		k = hyField.getValue();
        	}
        	st.setString(index, k);  
        }
		
	}

	/**
	 * 下拉框
	 */
	public final static String SELECT = "1";
	/**
	 * 弹出框
	 */
	public final static String POPWIN = "2";
	/**
	 * 日期6位点2位
	 */
	public final static String DATE = "3";
	
}







