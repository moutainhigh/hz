package com.dmz.yzt.util;

import lombok.Data;

/**
 * Created by zhangf on 2018/6/28.
 */

public enum TransEnum {

    fqdksq("120051", "发起贷款申请"),
    yxwjplsc("120052", "影像文件批量上传"),
    cjhkjh("120053", "创建还款计划"),
    xzhzfqmhtwj("120054", "下载合作方签名合同文件"),
    xghkrxx("120055", "修改还款人的信息"),
    cxfkzt("120056", "查询放款状态"),
    plcxfkzt("120061", "批量查询放款状态"),
    xzdzwj("120066", "下载对账文件"),;


    private String transCode;
    private String transName;


    private TransEnum(String transCode, String transName) {
        this.transCode = transCode;
        this.transName = transName;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }


}
