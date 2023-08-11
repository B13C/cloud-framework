package cn.maple.sse.service;

import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.sse.GXSseBusinessException;
import cn.maple.sse.dto.GXMessageDto;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * 基于Spring Boot实现的Server-sent events
 */
public interface GXSseEmitterService extends GXBusinessService {
    /**
     * 创建连接
     *
     * @param clientId 客户端ID
     */
    SseEmitter createConnect(String clientId);

    /**
     * 根据客户端id获取SseEmitter对象
     *
     * @param clientId 客户端ID
     */
    SseEmitter getSseEmitterByClientId(String clientId);

    /**
     * 发送消息给所有客户端
     *
     * @param msg 消息内容
     */
    void sendMessageToAllClient(String msg) throws IOException;

    /**
     * 给指定客户端发送消息
     *
     * @param clientId 客户端ID
     * @param msg      消息内容
     */
    void sendMessageToOneClient(String clientId, String msg) throws IOException;

    /**
     * 关闭连接
     *
     * @param clientId 客户端ID
     */
    void closeConnect(String clientId);

    @Retryable(value = {IOException.class, GXSseBusinessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 10000))
    void retrySend(String clientId, SseEmitter sseEmitter, SseEmitter.SseEventBuilder builder) throws IOException;

    @Recover
    void recover(Exception ex, String clientId, GXMessageDto messageDto);
}
