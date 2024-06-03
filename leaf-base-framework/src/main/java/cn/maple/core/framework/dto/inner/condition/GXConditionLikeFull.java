package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXDBStringEscapeUtils;

public class GXConditionLikeFull extends GXCondition<String> {
    public GXConditionLikeFull(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "like";
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("\"%{}%\"", GXDBStringEscapeUtils.escapeRawString(value.toString()));
    }
}
