package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ETokenInfoRes {
    private String token;
    private String expiresIn;
    private String refreshToken;
}
