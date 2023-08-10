package com.msk.result;

import lombok.Getter;

/**
 * @author MSK
 * @date 2023/8/10
 */
@Getter
public enum ResultEnum {
    SUCCESS(200, "成功"),
    ERROR(99, "失败"),
    TABLE_EXIST(101, "表已存在");

    private final Integer code;
    private final String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
