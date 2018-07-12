package com.dmz.yzt.feign.service;


import com.dmz.yzt.model.LoanCommonDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zhangf on 2018/6/27.
 */

@FeignClient(value = "HOST-SERVER-NETLOAN")
public interface YZTFeignService {
    /**
     * 接口名称	发起贷款申请
     * 接口地址	合作方提供
     * 说明	实现接收贷款申请，并完成自动放款功能
     * 交易     120051
     */
    @PostMapping(value = "base/wzyzt/loanApply")
    String loanApp(String requestMsg);


    /**
     * 接口名称	文件批量上传
     * 接口地址	合作方提供
     * 说明	实现接收贷款申请相关的证件材料、客户电子签章贷款合同等信息。
     * 备注	接口应允许重复调用，最后一次的调用会覆盖相同订单号下的所有文件。
     * 交易     120052
     */
     @PostMapping(value = "base/imageUpload")
     String imageUpload(String requestMsg);


     /**
     * 接口名称	查询放款状态
     * 接口地址	合作方提供
     * 说明	提供放款结果查询接口
     * 交易     120056
     */
    @PostMapping(value = "base/queryLuState")
    String queryLuState(String requestMsg);

    /**
     * 接口名称	下载合同文件
     * 接口地址	合作方提供
     * 说明	合作方对客户签名的电子合同加上合作方电子签章，并提供下载
     * 交易     120054
     */
    @PostMapping(value = "yztOtsService/imageContFile")
    String imageContFile(String requestMsg);

    /**
     * 接口名称	创建还款计划
     * 接口地址	合作方提供
     * 说明	合作方在放款完成以后提供还款计划查询
     * 交易     120053
     */
    @PostMapping(value = "yztOtsService/createRepayPlan")
    String createRepayPlan(String requestMsg);

    /**
     * 接口名称	修改还款人的信息
     * 接口地址	合作方提供
     * 说明	支持修改还款银行卡信息
     * 交易    120055
     */
    @PostMapping(value = "yztOtsService/modifyRepayInfo")
    String modifyRepayInfo(String requestMsg);

    /**
     * 接口名称	查询最新还款计划
     * 接口地址	合作方提供
     * 交易    120061
     */
    @PostMapping(value = "yztOtsService/queryNewRepayPlan")
    String queryNewRepayPlan(String requestMsg);

    /**
     * 接口名称	下载对账文件
     * 接口地址	合作方提供
     * 说明	合作方提供放款流水对账信息
     * 交易     120066
     */
    @PostMapping(value = "yztOtsService/downReconciliationFile")
    String downReconciliationFile(String requestMsg);

}
