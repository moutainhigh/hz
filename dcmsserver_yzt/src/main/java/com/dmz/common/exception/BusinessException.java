package com.dmz.common.exception;

import com.dmz.common.exception.util.ErrorEnum;

/**
 * Created by zhangf on 2018/7/3.
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private String dscp;

    public BusinessException(Object Obj) {
        super(Obj.toString());
    }

    public BusinessException(String Code) {
        super();
        this.code = code;
        this.dscp = ErrorEnum.valueOf(code).getDscp();
    }

    public String getDscp() {
        return dscp;
    }

    public String getCode() {
        return code;
    }
}
