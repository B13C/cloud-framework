package cn.maple.core.framework.deserializer.req.protocol;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 处理
 * Cannot deserialize value of type `java.lang.Integer` from String "Invalid date": not a valid `java.lang.Integer`
 * 错误
 */
@Slf4j
public class IntegerDeserializerProtocol extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ct) throws IOException {
        try {
            // 如果是整数，直接返回  
            return p.getIntValue();
        } catch (Exception e) {
            // 如果是字符串，尝试处理  
            String text = p.getText();
            try {
                // 用于可解析为数字的字符串
                return Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                log.info("Invalid value for Integer field: {}", text);
                // 返回默认值或其他逻辑，比如 null  
                return null;
            }
        }
    }
}