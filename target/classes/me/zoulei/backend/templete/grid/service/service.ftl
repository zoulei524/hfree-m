	//接口
	List<Map<String, Object>> initCodeType(String codeType, String filter);
	List<${entity}> get${entity}List(JSONObject pageData);
<#if config.iscrud>
	public void get${entity}InfoById(JSONObject pageData);
	public void save${entity}Info(JSONObject pageData) throws AppException;
	public void delete${entity}ById(JSONObject pageData);
</#if>
<#if config.exportExcel>
	public String export${entity}Excel(JSONObject pageData) throws AppException;
</#if>
	
	
