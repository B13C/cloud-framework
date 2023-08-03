package cn.maple.core.datasource.enums;

import lombok.Getter;

@Getter
public enum GXModelEventNamingEnums {
    SYNC_SAVE_ENTITY("sync_save_entity", "syncSaveEntity", "同步保存实体对象"),
    SYNC_UPDATE_ENTITY("sync_update_entity", "syncUpdateEntity", "同步更新实体对象"),
    SYNC_UPDATE_FIELD("sync_update_field", "syncUpdateField", "同步更新指定字段"),
    SYNC_DELETE_SOFT("sync_delete_soft", "syncDeleteSoft", "同步通过条件软删除"),
    SYNC_DELETE("sync_delete", "syncDelete", "同步通过条件物理删除"),

    ASYNC_SAVE_ENTITY("async_save_entity", "asyncSaveEntity", "异步保存实体对象"),
    ASYNC_UPDATE_ENTITY("async_update_entity", "asyncUpdateEntity", "异步更新实体对象"),
    ASYNC_UPDATE_FIELD("async_update_field", "asyncUpdateField", "异步更新指定字段"),
    ASYNC_DELETE_SOFT("async_delete_soft", "asyncDeleteSoft", "异步通过条件软删除"),
    ASYNC_DELETE("async_delete", "asyncDelete", "异步通过条件物理删除");

    /**
     * 事件类型
     */
    private final String eventType;

    /**
     * 事件名字
     */
    private final String eventName;

    /**
     * 事件描述
     */
    private final String desc;

    GXModelEventNamingEnums(String eventType, String eventName, String desc) {
        this.eventType = eventType;
        this.eventName = eventName;
        this.desc = desc;
    }
}
