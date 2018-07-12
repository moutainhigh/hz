package com.dmz.common.exception.util;

/**
 * Created by zhangf on 2018/7/3.
 */
public enum ErrorEnum {

    NON_WORK("W", "非工作时间"),
    TRANX_FAIL("E", "交易失败"),
    TRANX_CODE_ERR("R", "交易码错误"),
    SYS_ERROR("ERROR_SYS","系统错误"),
    TRANX_CONN("CONN_ERR","连接错误"),;



    private String code;
    private String dscp;

    private ErrorEnum(String code, String dscp) {
        this.setCode(code);
        this.setDscp(dscp);
    }


    public String getDscp() {
        return dscp;
    }

    public void setDscp(String dscp) {
        this.dscp = dscp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ErrorEnum{" +
                "code='" + code + '\'' +
                ", dscp='" + dscp + '\'' +
                '}';
    }
}
