package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtm.contract.model.domain.FrameAgreement;
import com.xtm.contract.model.param.frameAgreement.FrameAgreementParam;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  19:57
 *@Description: 框架合同协议Mapper
 */
@Mapper
public interface FrameAgreementMapper extends BaseMapper<FrameAgreement> {
    IPage<FrameAgreementVo> queryPageList(Page<FrameAgreementVo> pageParam, @Param("queryParam") FrameAgreementParam queryParam);

    /**
     * 根据合同编号查询虚拟协议为否且已启用的物流合同协议
     * @param contractCode
     * @return
     */
    List<FrameAgreementVo> queryVirtualEnableByCode(@Param("contractCode") String contractCode);
}
