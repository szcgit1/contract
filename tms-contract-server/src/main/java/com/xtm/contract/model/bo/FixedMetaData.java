package com.xtm.contract.model.bo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/***
 * Description mongodb 固定元数据
 * Date 2025/3/21 15:25
 * Version 1.0
 * @author zhangshichuang
 */
@Data
public class FixedMetaData<T> {
    @Field(name = "fixed_metadata")
    private T fixedMetadata;

}