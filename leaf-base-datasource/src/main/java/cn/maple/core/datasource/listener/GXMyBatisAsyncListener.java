package cn.maple.core.datasource.listener;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.event.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@SuppressWarnings("all")
@Async(value = "myBatisEventAsyncTaskExecutor")
public class GXMyBatisAsyncListener implements GXMyBatisBaseListener {
    /**
     * 监听保存实体(Entity)事件
     *
     * @param saveEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).ASYNC_SAVE_ENTITY.eventType)")
    public void listenerSaveEntity(GXMyBatisModelSaveEntityEvent<Dict> saveEntityEvent) {
        GXMyBatisBaseListener.super.listenerSaveEntity(saveEntityEvent);
    }

    /**
     * 监听更新实体(Entity)事件
     *
     * @param updateEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).ASYNC_UPDATE_ENTITY.eventType)")
    public void listenerUpdateEntity(GXMyBatisModelUpdateEntityEvent<Dict> updateEntityEvent) {
        GXMyBatisBaseListener.super.listenerUpdateEntity(updateEntityEvent);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param updateFieldEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).ASYNC_UPDATE_FIELD.eventType)")
    public void listenerUpdateField(GXMyBatisModelUpdateFieldEvent<Dict> updateFieldEvent) {
        GXMyBatisBaseListener.super.listenerUpdateField(updateFieldEvent);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param deleteSoftEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).ASYNC_DELETE_SOFT.eventType)")
    public void listenerDeleteSoft(GXMyBatisModelDeleteSoftEvent<Dict> deleteSoftEvent) {
        GXMyBatisBaseListener.super.listenerDeleteSoft(deleteSoftEvent);
    }

    /**
     * 监听批量新增与更新事件
     *
     * @param saveBatchEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).ASYNC_SAVE_BATCH_ENTITY.eventType)")
    public void listenerSaveBatch(GXMyBatisModelSaveBatchEntityEvent<Dict> saveBatchEntityEvent) {
        GXMyBatisBaseListener.super.listenerSaveBatch(saveBatchEntityEvent);
    }
}
