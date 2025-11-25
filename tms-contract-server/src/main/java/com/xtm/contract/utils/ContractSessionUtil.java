package com.xtm.contract.utils;

import com.xtm.common.model.SysUser;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/6/22 18:58
 * @version: 1.0
 */
public class ContractSessionUtil {

    public static String getCurrentCompanyID(SysUser sessionInfo)
    {
        if (sessionInfo==null) {
            return null;
        }
        return sessionInfo.getCompanyId();
    }

    public static String getCurrentUserID(SysUser sessionInfo)
    {
        if (sessionInfo==null) {
            return null;
        }
        return sessionInfo.getId();
    }

}

