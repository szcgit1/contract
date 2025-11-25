package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.FrameAgreementSub;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  19:57
 *@Description: 框架合同协议子表Mapper
 */
@Mapper
public interface FrameAgreementSubMapper extends BaseMapper<FrameAgreementSub> {

    /**
     * 查询nc销售合同主键已经关联的框架合同子表信息
     * @param ncSalesContractId
     */
    FrameAgreementSub getExistSub(@Param("ncSalesContractId") String ncSalesContractId);
}
