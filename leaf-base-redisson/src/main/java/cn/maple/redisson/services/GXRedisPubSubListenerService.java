package cn.maple.redisson.services;

public interface GXRedisPubSubListenerService {
    /**
     * 在这个方法这中添加监听redis发布的主题
     * 进行自己的业务逻辑处理
     * {@code
     * RTopic topic = redissonClient.getTopic("topic");
     * topic.addListener(Object.class, new MessageListener<Object>() {
     *
     * @Override
     * public void onMessage(CharSequence channel, Object msg) {
     *      System.out.println("topic msg" + JSONUtil.toJsonStr(msg));
     * }
     * });
     * }
     */
    void registerRedissonListener();
}
