-- drop TABLE if exists "HY_ZGGL_ZZGB"."${tablename}";
CREATE TABLE "HY_ZGGL_ZZGB"."${tablename}"
(
"${tablename}00" VARCHAR2(128) NOT NULL,
"Y0000" VARCHAR2(128),
"M0100" VARCHAR2(128),
"B0111_ZGBM" VARCHAR2(200),
"B0101_ZGBM" VARCHAR2(200),
"B0111" VARCHAR2(200),
"B0101" VARCHAR2(200),
"${tablename}01" VARCHAR2(500),
"${tablename}02" VARCHAR2(20),



/*
"${tablename}60" VARCHAR2(20),
"${tablename}61" VARCHAR2(2000),
"${tablename}62" VARCHAR2(20),
"${tablename}63" VARCHAR2(2000),
"${tablename}64" VARCHAR2(20),
"${tablename}65" VARCHAR2(2000),
"${tablename}66" VARCHAR2(20),
"${tablename}67" VARCHAR2(2000),
"${tablename}68" VARCHAR2(20),
*/
"${tablename}91" VARCHAR2(128),
"${tablename}92" TIMESTAMP(0),
"${tablename}93" VARCHAR2(128),
"${tablename}94" TIMESTAMP(0),
"${tablename}98" VARCHAR2(20),
"${tablename}99" VARCHAR2(20),
NOT CLUSTER PRIMARY KEY("${tablename}00"));

COMMENT ON TABLE "HY_ZGGL_ZZGB"."${tablename}" IS '${comment}';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."B0101" IS '用人单位';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."B0101_ZGBM" IS '主管部门';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."B0111" IS '用人单位编码';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."B0111_ZGBM" IS '主管部门编码';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."M0100" IS '业务模型编码';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."Y0000" IS '业务流程唯一编码';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}00" IS '业务唯一流水号';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}01" IS '请示名称';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}02" IS '上报时间yyyymmdd';

/*
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}60" IS '业务状态';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}61" IS '用人单位填写上报意见';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}62" IS '用人单位填写上报意见时间';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}63" IS '组织部门填写审核意见';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}64" IS '组织部门填写审核意见时间';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}65" IS '意见';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}66" IS '意见时间';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}67" IS '意见';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}68" IS '意见时间';
*/
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}91" IS '创建用户';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}92" IS '创建时间';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}93" IS '修改用户';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}94" IS '修改时间';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}98" IS '业务状态代码';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename}"."${tablename}99" IS '有效性标识';











-- drop TABLE if exists "HY_ZGGL_ZZGB"."${tablename2}";
CREATE TABLE "HY_ZGGL_ZZGB"."${tablename2}"
(
"${tablename2}00" VARCHAR2(128) NOT NULL,
"${tablename}00" VARCHAR2(128),

/*
"A0000" VARCHAR2(128),
"A0101" VARCHAR2(20),
"A0104" VARCHAR2(20),
"A0184" VARCHAR2(20),
"A0107" VARCHAR2(20),
"A0192A" VARCHAR2(20),
*/
"${tablename2}91" VARCHAR2(128),
"${tablename2}92" TIMESTAMP(0),
"${tablename2}99" VARCHAR2(20),
NOT CLUSTER PRIMARY KEY("${tablename2}00"))  ;

COMMENT ON TABLE "HY_ZGGL_ZZGB"."${tablename2}" IS '${comment2}';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."${tablename}00" IS '业务主表主键';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."${tablename2}00" IS '主键';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."${tablename2}91" IS '创建用户';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."${tablename2}92" IS '创建时间';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."${tablename2}99" IS '有效标识';

/*
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."A0000" IS '人员主键';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."A0101" IS '姓名';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."A0104" IS '性别';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."A0107" IS '出生年月';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."A0184" IS '身份证号码';
COMMENT ON COLUMN "HY_ZGGL_ZZGB"."${tablename2}"."A0192A" IS '单位职务';
*/




