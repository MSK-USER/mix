package com.msk.exception;

import com.msk.result.ResultEnum;
import lombok.Data;

/**
 * @描述 统一异常处理
 */
@Data
public class OwnException extends RuntimeException {

    private Integer code;  // 错误代码
    private String msg;

    /**
     * 给自定义业务异常类用的方法
     */
    public OwnException(ResultEnum responseEnum) {
        this(responseEnum.getCode(), responseEnum.getMsg());
    }

    public OwnException(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
