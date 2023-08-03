package cn.maple.core.datasource.listener;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.event.GXMyBatisModelDeleteSoftEvent;
import cn.maple.core.datasource.event.GXMyBatisModelSaveEntityEvent;
import cn.maple.core.datasource.event.GXMyBatisModelUpdateEntityEvent;
import cn.maple.core.datasource.event.GXMyBatisModelUpdateFieldEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@SuppressWarnings("all")
public class GXMyBatisSyncListener implements GXMyBatisBaseListener {
    /**
     * 监听保存实体(Entity)事件
     *
     * @param saveEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).SYNC_SAVE_ENTITY.eventType)")
    public void listenerSaveEntity(GXMyBatisModelSaveEntityEvent<Dict> saveEntityEvent) {
        GXMyBatisBaseListener.super.listenerSaveEntity(saveEntityEvent);
    }

    /**
     * 监听更新实体(Entity)事件
     *
     * @param updateEntityEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).SYNC_UPDATE_ENTITY.eventType)")
    public void listenerUpdateEntity(GXMyBatisModelUpdateEntityEvent<Dict> updateEntityEvent) {
        GXMyBatisBaseListener.super.listenerUpdateEntity(updateEntityEvent);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param updateFieldEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).SYNC_UPDATE_FIELD.eventType)")
    public void listenerUpdateField(GXMyBatisModelUpdateFieldEvent<Dict> updateFieldEvent) {
        GXMyBatisBaseListener.super.listenerUpdateField(updateFieldEvent);
    }

    /**
     * 监听更新指定字段事件
     *
     * @param deleteSoftEvent 事件对象
     */
    @EventListener(condition = "#root.event.eventType.equals(T(cn.maple.core.datasource.enums.GXModelEventNamingEnums).SYNC_DELETE_SOFT.eventType)")
    public void listenerDeleteSoft(GXMyBatisModelDeleteSoftEvent<Dict> deleteSoftEvent) {
        GXMyBatisBaseListener.super.listenerDeleteSoft(deleteSoftEvent);
    }
}
