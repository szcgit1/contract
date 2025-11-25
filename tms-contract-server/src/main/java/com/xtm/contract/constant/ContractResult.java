package com.xtm.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/1 10:12
 * @desc 合同结果集
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ContractResult {
    /**
     * 错误码（11XXXX：配置类错误,12XXXX:逻辑错误,14XXXX:业务类错误）
     */
    private int code;

    /**
     * 错误描述
     */
    private String msg;


    /**
     * 没有e签宝配置信息
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_110001 = new ContractResult(110001, "没有e签宝配置信息");
    /**
     *  字体文件未配置
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_110002 = new ContractResult(110002, "字体文件未配置");
    /**
     *  e签宝白名单未配置
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_110003 = new ContractResult(110003, "e签宝白名单未配置");


    /**
     *  LOCAL_HTML生成错误
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_120001 = new ContractResult(120001, "LOCAL_HTML生成错误");
    /**
     *  LOCAL_PFD生成错误
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_120002 = new ContractResult(120002, "LOCAL_PDF生成错误");
    /**
     *  网络文件下载失败，请稍后再试
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_120003 = new ContractResult(120003, "网络文件下载失败，请稍后再试");
    /**
     *  文件上传失败
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_120004 = new ContractResult( 120004, "网络文件下载失败，文件服务异常");

    /**
     * 管理员不能为空
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140000 = new ContractResult(140000, "管理员不能为空");
    /**
     * 身份证号不能为空
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140001 = new ContractResult(140001, "身份证号不能为空");
    /**
     * 用户名（企业名称）参数不能为空
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140002 = new ContractResult(140002, "用户名（企业名称）参数不能为空");
    /**
     * 统一社会信用码不能为空
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140003 = new ContractResult(140003, "统一社会信用码不能为空");
    /**
     * 统一社会信用码格式不正确
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140004 = new ContractResult(140004, "统一社会信用码格式不正确");
    /**
     * 合同主题格式不正确
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140005 = new ContractResult(140005, "合同主题格式不正确");
    /**
     * 当前平台在系统未授权e签宝，请联系管理员授权
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140006 = new ContractResult(140006, "当前平台在系统未授权e签宝，请联系管理员授权");
    /**
     * 平台账户余额已不足，请及时充值
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140007 = new ContractResult(140007, "平台账户余额已不足，请及时充值");
    /**
     * 手机号不能为空
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140008 = new ContractResult(140008, "手机号不能为空");
    /**
     *  您未通过实名认证，请至用户中心完成实名认证
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_140009 = new ContractResult(140009, "您未通过实名认证，请至用户中心完成实名认证");
    /**
     * 成功
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_0 = new ContractResult(200001, "成功");

    /**
     * 签署失败
     */
    public static final ContractResult XIAONIU_CONTRACT_BACK_ERROR = new ContractResult(-1, "合同签署失败");
}
