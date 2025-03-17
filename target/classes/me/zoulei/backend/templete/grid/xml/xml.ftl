	<!--${tablename}（${tablecomment}）列表查询-->
	<sql method="get${entity}List" sqlid="get${entity}List">
		<oracle>
			${listDataSQL}
		</oracle>
		<mysql>
			${listDataSQL}
		</mysql>
		<dm>
			${listDataSQL}
		</dm>
		<KingBase></KingBase>
		<gbase></gbase>
	</sql>
	
