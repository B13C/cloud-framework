package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.code.GXResultStatusCode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:00
 * <p>
 * 数据库主键重复异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GXDBDuplicateKeyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String msg;

    private final int code;

    private final Dict data;

    public GXDBDuplicateKeyException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXDBDuplicateKeyException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXDBDuplicateKeyException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXDBDuplicateKeyException(String msg, int code) {
        this(msg, code, (Dict) null);
    }

    public GXDBDuplicateKeyException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXDBDuplicateKeyException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode) {
        this(resultCode, "");
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, @NotNull String msg) {
        this(resultCode, msg, Dict.create());
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, Throwable e) {
        this(resultCode, e, Dict.create());
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, @NotNull String msg, @NotNull Dict data) {
        this(resultCode, msg, data, null);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, Throwable e, @NotNull Dict data) {
        this(resultCode, "", data, e);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, @NotNull String msg, @NotNull Dict data, Throwable e) {
        super(CharSequenceUtil.format("{}{}", msg, resultCode.getMsg()), e);
        this.msg = CharSequenceUtil.format("{}{}", msg, resultCode.getMsg());
        this.code = resultCode.getCode();
        if (MapUtil.isEmpty(data)) {
            data = Dict.create();
        }
        data.putAll(resultCode.getExtraData());
        this.data = data;
    }
}
