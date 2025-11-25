package com.xtm.contract.model.query.eqbDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EContextInfoDTO {
    /**认证发起来源, BROWSER - 浏览器；APP - 移动端APP。不填默认为为BROWSER*/
    private String origin;
}
