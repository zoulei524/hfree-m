[
<#list config.tableMetaData as item>
	{
		field: "${(item.column_name)?lower_case}",
	<#if item.codetype?string == "">
		codeType: null,
	<#else>
		codeType: "${item.codetype}",
	</#if>
		title: "${item.comments}",
		type: "VARCHAR",
		width: "${item.width}",
	<#if item.visible?string == "不显示且不维护"||item.visible?string == "不显示但维护">
		hide: true,
	</#if>
		align: "${item.align}",
		showOverflowTooltip: true,
	},
</#list>
]	
 
