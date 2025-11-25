package com.xtm.contract.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.setting.feign.SettingFeign;
import com.xtm.setting.model.dto.AddressDto;
import com.xtm.setting.model.dto.DictionaryDto;
import com.xtm.setting.model.vo.AddressDetailsVo;
import com.xtm.setting.model.vo.CargoOwnerInfoResult;
import com.xtm.setting.model.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SettingServiceFeign {

    @Resource
    private SettingFeign settingFeign;

    /**
     *  获取地址信息
     * @param addressId
     * @return
     */
    public AddressDetailsVo getAddressInfoById(String addressId) {
        Result<List<AddressDetailsVo>> settingResult = settingFeign.getProvinceCityDistrictByIds(Arrays.asList(addressId));
        log.info("获取地址信息:{}",settingResult);
        if(settingResult.isSuccess()){
            List<AddressDetailsVo> addressList = settingResult.getData();
            if (CollectionUtil.isNotEmpty(addressList)){
                return addressList.get(0);
            }
            return null;
        }
        throw new BusinessException("获取地址信息失败:"+settingResult.getMsg());
    }

    /**
     *  获取地址信息
     */
    public String detailAddressByAreaCode(AddressDto addressDto){
        Result<String> settingResult = settingFeign.detailAddressByAreaCode(addressDto);
        log.info("获取地址信息:{}",settingResult);
        if(settingResult.isSuccess()){
            return settingResult.getData();
        }
        throw new BusinessException("获取地址信息失败:"+settingResult.getMsg());
    }

    public List<DictionaryVo> listDictionaries(List<Long> contractTypeList){
        DictionaryDto dictionaryDto = new DictionaryDto();
        dictionaryDto.setIds(contractTypeList);
        Result<List<DictionaryVo>> settingResult = settingFeign.listDictionaries(dictionaryDto);
        log.info("查询到的字典值 : {}" ,settingResult);
        if(settingResult.isSuccess()){
            List<DictionaryVo> dicList = settingResult.getData();
            if (CollectionUtil.isNotEmpty(dicList)){
                return dicList;
            }
            return null;
        }
        throw new BusinessException("获取字典信息失败:"+settingResult.getMsg());
    }

    public DictionaryVo getDictionaryById(Long id){
        List<Long> ids = Arrays.asList(id);
        List<DictionaryVo> dictionaryVos = listDictionaries(ids);
        if (CollectionUtil.isNotEmpty(dictionaryVos)){
            return dictionaryVos.get(0);
        }
        return new DictionaryVo();
    }

    /**
     *  根据 id 查询字典
     * @return
     */
    public String getDicNameById(Long id) {
        return getDictionaryById(id).getName();
    }

}
