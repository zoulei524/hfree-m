	
	@Autowired
	HYBeanUtil hybean;
	
	//实现类
	/**
	 * 获取代码
	 */
	@Override
	public List<Map<String, Object>> initCodeType(String codeType, String filter) {
		return dao.initCodeType(codeType, filter);
	}
	
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护信息: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	public List<${entity}> get${entity}List(JSONObject pageData) {
	    List<${entity}> ${tablename}List = dao.get${entity}List(pageData);
	    return ${tablename}List;
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
	@Override
	public void get${entity}InfoById(JSONObject pageData) {
	
		String ${config.pk} = pageData.getString("${config.pk}");
	
		${entity} ${tablename} = session.get(${entity}.class, ${config.pk});
		
		pageData.put("${tablename}", ${tablename});
	}
	
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--保存修改<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	@Transactional
	public void save${entity}Info(JSONObject pageData)  throws AppException{
//		JSONObject ${tablename}form = pageData.getJSONObject("${tablename}EntityData");
		
		String ${config.pk} = pageData.getString("${config.pk}");
//      String orgname = userService.getUserOrg().getOrgname();//b0101
//      String b0111 = userService.getUserOrg().getB0111();//机构id
		String userid = userService.getCurrentUserId();//用户id
//		String username = userService.getCurrentUserName();//用户名

		//${entity} ${tablename} = JSON.parseObject(pageData.toJSONString(), ${entity}.class);
		//${entity} ${tablename} = pageData.toJavaObject(${entity}.class);
		//${entity} ${tablename} = hybean.pageElementToBean(pageData, ${entity}.class);
		${entity}Dto ${tablename}Dto = hybean.pageElementToBean(pageData, ${entity}Dto.class);
  		if(StringUtil.isEmpty(${config.pk})) {
			${entity} ${tablename} = new ${entity}();
			BeanUtil.copyProperties(${tablename}Dto, ${tablename}, false);
			${config.pk} = IDGenertor.uuidgenerate();
			
			${tablename}.set${config.pkU}(${config.pk});
			
//			${tablename}.setB0101(orgname);
//			${tablename}.setB0111(b0111);
//			${tablename}.setY0000(${config.pk});
			
//			${tablename}.set${entity}60(${entity}60Enum.草稿.getCode());
			${tablename}.set${entity}99(Consiants.String_1);
			
			${tablename}.set${entity}91(userid);
			${tablename}.set${entity}92(new Date());
			${tablename}.set${entity}93(userid);
			${tablename}.set${entity}94(new Date());
  			session.save(${tablename});
			pageData.put("${config.pk}", ${config.pk});
  		}else {
  			${entity} ${tablename} = session.get(${entity}.class, ${config.pk});
  			
  			//BeanUtil.copyProperties(${tablename}Dto, ${tablename}, false);
  			//写上需要修改的列
  			
  			${tablename}.set${entity}93(userid);
			${tablename}.set${entity}94(new Date());
			
  			session.update(${tablename});
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
	@Override
	public void delete${entity}ById(JSONObject pageData) {
	
		//String ${config.pk} = pageData.getString("${config.pk}");
		//${entity} ${tablename} = session.get(${entity}.class, ${config.pk});
		//session.delete(${tablename});
		
		String ${config.pk}s = pageData.getString("${config.pk}s");
		//批量删除
		if(StringUtil.isNotEmpty(${config.pk}s)) {
        	String[] ${config.pk}Arr = ${config.pk}s.split(",");
        	for (int i = 0; i < ${config.pk}Arr.length; i++) {
        		String ${config.pk} = ${config.pk}Arr[i];
        		${entity} ${tablename} = session.get(${entity}.class, ${config.pk});
    			session.delete(${tablename});
    			//删除子表
    			//session.executeUpdate("delete from ${tablename}x where ${config.pk}=?",new Object[] {${config.pk}});
			}
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
	@Override
	public String export${entity}Excel(JSONObject pageData) throws AppException {
		//导出excel绝对路径
		String path = DownFileDirUtil.getTempFilePath(EHyFile.DOWNLOAD).getAbsolutePath()+"/export.xlsx";
		
		//设置分页信息从第1页开始，导出total全部数据
		JSONObject pageInfo = pageData.getJSONObject("pageInfo");
		pageInfo.put("currentPage",1);
		int total = pageInfo.getIntValue("total");
		pageInfo.put("pageSize",total);
		
		//通过分页方法获取数据
		List<Map<String,Object>> ${tablename}List = dao.get${entity}MapList(pageData);
		try {
			${tablename}Excel.writeSheetData(path, ${tablename}List);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("导出失败：" + e.getMessage());
		}
		
		return path;
	}
	
	


</#if>