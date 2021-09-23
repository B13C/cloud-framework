package com.geoxus.shiro.constant;

import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.common.util.GXFrameworkCommonUtils;

public class GXRoleConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "role";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "role";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "role";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "framework";

    @GXFieldComment("核心模型ID")
    public static final int CORE_MODEL_ID = GXFrameworkCommonUtils.getEnvironmentValue("app.core-model-id.role", Integer.class, 100);

    private GXRoleConstant() {
    }
}
