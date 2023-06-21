package cn.maple.core.datasource.model;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.model.GXBaseModel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXMyBatisModel extends GXBaseModel {
    @TableField(fill = FieldFill.INSERT)
    protected Integer createdAt;

    @TableField(fill = FieldFill.UPDATE)
    protected Integer updatedAt;

    @TableField(fill = FieldFill.INSERT)
    protected String createdBy;

    @TableField(fill = FieldFill.UPDATE)
    protected String updatedBy;

    @TableField(typeHandler = JacksonTypeHandler.class)
    protected Dict ext;

    @TableLogic(delval = "id")
    protected Number isDeleted;

    protected Number deletedAt;
}
