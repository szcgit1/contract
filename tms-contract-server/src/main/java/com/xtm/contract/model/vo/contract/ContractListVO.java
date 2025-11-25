package com.xtm.contract.model.vo.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 15:21
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractListVO {
    /**
     * 合同信息集合
     */
    List<ContractInfoQryVO> contractInfoList;
}
