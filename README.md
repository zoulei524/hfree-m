# hfree 代码生成工具
支持oracle，mysql，达梦
1.数据库表最好是表名备注和字段备注写全。 
2.数据库表必须要有唯一主键。

##UI##
1. src/me/zoulei/backend/templete/grid/TableMetaDataConfig.java      
存储生成代码的配置项。读取数据库表格和接收ui表格配置的信息

2. src/me/zoulei/ui/components/SearchComponent.java    
用于搜索数据库表的组件， 设置表后可以加载表格参数配置组件。  查询模式和表名。 初始化代码类别codetype。

3.src/me/zoulei/ui/components/DataSourceComponent.java
数据库配置

3. src/me/zoulei/ui/components/EditorGrid.java    
ui表格界面，genTableMetaData 获取全部表格上配置的信息

##生成代码##
1. src/me/zoulei/gencode/Gencode.java    
获取代码生成tab页

###配置
1、src/me/zoulei/Config.java
配置类 第一次初始化生成Constants.java的ini文件，生成数据库配置文件。


###更新
2024年5月10日09:42:07 Excel导出加代码转换和时间转换，实体类达梦库number为bigdecimal类型,integer转为long类型。<br>
2024年5月10日11:06:27 分页查询代码和部分代码初始化提到公共类中<br>
2024年5月            输出文件增加异常处理类<br>
2024年5月22日17:06:14 输出文件实体类添加备注信息<br>
2024年5月22日17:07:29 输出文件增加常量类<br>
2024年6月7日10:17:00  实体类加计算机名和用户信息<br>
2024年6月12日			枚举和常量生成的报错<br>
2024年6月13日09:50:24 js分页参数名字更改<br>
2024年6月20日20:25:23 实体类保存，前端对象转实体类使用hybean<br>
2024年6月27日10:45:58 没有选择分页代码注释掉<br>
2024年6月27日10:59:16 接口名称生成，从包名中获取<br>
2024年8月8日15:33:27  日志脚本生成，改成全部生成。code_table_col脚本设置去掉。脚本加上code_type列<br>
2024年8月23日10:09:47 库表比对加上选择表<br>
2024年8月30日10:46:22 增删改部分逻辑修改<br>
2024年9月10日10:25:24 表结构比对生成修改ddl，加上模式名<br>
2024年9月11日17:29:31 达梦获取表结构数据，select里有子查询查不出来。改成表关联查询。