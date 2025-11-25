package com.xtm.contract.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * BigDecimal序列化器
 * @author wangzhenjun
 * @date 2023/5/17 16:29
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

 private DecimalFormat df = new DecimalFormat("#.########");
 @SneakyThrows
 @Override
 public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
  if (null != bigDecimal ) {
   String plainString = df.format(bigDecimal);
   jsonGenerator.writeString(plainString);
  } else {
   jsonGenerator.writeString(BigDecimal.ZERO.toPlainString());
  }
 }
}