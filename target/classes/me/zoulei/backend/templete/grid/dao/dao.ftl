	
	@Autowired
	HYBeanUtil hybean;
	
	
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法功能描述: ${tablecomment}--列表 分页查询<br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	public String get${entity}ListSQL(JSONObject pageData) {
	
		SQLProcessor sqlPro = new SQLProcessor(session, this.getClass());
		
		Map<String,Object> params = new HashMap<>();
		String sql = sqlPro.getSQLFromXml("get${entity}List", pageData);
		
		//分页sql，不需要分页的注释该行代码。
		${config.pagination?then('','//')}sql = this.getPageSql(pageData, sql);
		
		return sql;
	}
	
	public List<${entity}> get${entity}List(JSONObject pageData) {
		String querySQL = this.get${entity}ListSQL(pageData);
		List<${entity}> ${tablename}List = session.getCurrentSession().createNativeQuery(querySQL).addEntity(${entity}.class).list();
		return ${tablename}List;
	}

	public List<Map<String,Object>> get${entity}MapList(JSONObject pageData) {
		String querySQL = this.get${entity}ListSQL(pageData);
		List<Map<String,Object>> ${tablename}MapList = session.queryForList(querySQL);
		return ${tablename}MapList;
	}
	
	
	public List<${entity}Dto> get${entity}DtoList(JSONObject pageData) throws AppException {
		List<Map<String,Object>> ${tablename}MapList = this.get${entity}MapList(pageData);
		List<${entity}Dto> ${tablename}DtoList = hybean.toHYBean(${tablename}MapList, ${entity}Dto.class);
		return ${tablename}DtoList;
	}
	
	
	