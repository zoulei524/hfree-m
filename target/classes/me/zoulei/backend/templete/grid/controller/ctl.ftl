	
	
	@PostMapping("/doInit")
	public ResponseMessage doInit(@RequestBody JSONObject pageData) {
		
		List<String> codeTypeList = new ArrayList<String>();
	    List<String> filterList = new ArrayList<String>();
	    
	    JSONObject codeTypes = pageData.getJSONObject("codeTypes");
	    String encodeFilters = pageData.getString("codeTypeFilters");
	    
	    byte[] bytes = Base64.decode(encodeFilters);
	    JSONObject codeTypeFilters = JSONObject.parseObject(new String(bytes));
	    
	    codeTypeList.addAll(codeTypes.keySet());
	    filterList.addAll(codeTypeFilters.keySet());
	    //需要全加载的code_type
	    codeTypeList.removeAll(filterList);
	    JSONObject codeTypeResult = this.getCodeJsonArrayList(codeTypeList);
	    
	    //需要过滤的代码
	    codeTypeFilters.forEach((codeType, filter)->{
	    	List<Map<String,Object>> c = service.initCodeType(codeType, (String)filter);
	    	codeTypeResult.put(codeType,c);
	    });
	    
	    pageData.put("codeTypes", codeTypeResult);
		
		return ResponseMessage.ok("初始化成功！", pageData);
	}
	
	/**
	 * 获取字典jsonobject对象
	 * @param codetype
	 * @return
	 */
	public  JSONObject getCodeJsonArrayList(List<String> codetypes) {
		JSONObject jsonobjectResult = new JSONObject();
		codetypes.forEach(s->{
			HashMap<String,String> hashmap = EhCacheUtil.getCacheHashMap(s+SupportECODE.SELECT.getCode());
			if(hashmap!=null) {
				JSONArray jsonArray = new JSONArray();
				hashmap.forEach((key, value) -> {
					JSONObject temp = new JSONObject();
					temp.put("key", key);
					temp.put("value", value);
		            jsonArray.add(temp);
		        });
				jsonobjectResult.put(s,jsonArray);
			}
		});
		return jsonobjectResult;		
	}

	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/get${entity}List")
	public ResponseMessage get${entity}List(@RequestBody JSONObject pageData ${otherParams}) {
		try {
			//hygrid
//			int page = pageData.get("page") == null?1:Integer.parseInt(pageData.getString("page"));
//	        int size = pageData.get("size") == null?50:Integer.parseInt(pageData.getString("size"));
//	        JSONArray dataCols = pageData.getJSONArray("dataCols");
	        
//	        String sql = dao.get${entity}ListSQL(pageData);
//	        JSONObject jsonObject = gridTableService.pageQuery(sql,size,page,dataCols,pageData);
//	        pageData.put("tableData",jsonObject);
		
			//List<${entity}> ${tablename}List = service.get${entity}List(pageData);
			List<${entity}Dto> ${tablename}List = dao.get${entity}DtoList(pageData);
			pageData.put("tableData", ${tablename}List);
			return ResponseMessage.ok(pageData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
	
<#if config.iscrud>	
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--根据id获取数据<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/get${entity}InfoById")
	public ResponseMessage get${entity}InfoById(@RequestBody JSONObject pageData) {
		try {
		    
		    service.get${entity}InfoById(pageData);
		    
		    return ResponseMessage.ok(pageData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
    
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--保存修改接口<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/save${entity}Info")
	public ResponseMessage save${entity}Info(@RequestBody JSONObject pageData) {
		try {
	        service.save${entity}Info(pageData);
	        return ResponseMessage.ok(pageData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
    
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--根据主键id删除<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/delete${entity}ById")
	public ResponseMessage delete${entity}ById(@RequestBody JSONObject pageData) {
		try {
			service.delete${entity}ById(pageData);
			return ResponseMessage.ok(pageData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
 </#if>  
 
 
 <#if config.exportExcel>
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--导出全部数据到excel<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/export${entity}Excel")
	public ResponseMessage export${entity}Excel(@RequestBody JSONObject pageData) {
		try {
			//导出excel，返回excel绝对路径
			String path = service.export${entity}Excel(pageData);
			pageData.put("path", FilePathStoreUtil.storePath(path));
			pageData.put("filename", "${tablecomment}");
			return ResponseMessage.ok(pageData);
		} catch (AppException e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
</#if>  