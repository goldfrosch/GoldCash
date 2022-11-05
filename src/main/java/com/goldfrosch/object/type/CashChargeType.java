package com.goldfrosch.object.type;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CashChargeType {
  CREDIT_CARD("계좌"),
  GIFT_CARD("문상"),
  ETC("기타");

  private final String type;

  private static final Map<String, String> TYPE_MAP = Collections.unmodifiableMap(
      Stream.of(values()).collect(
          Collectors.toMap(CashChargeType::getType, CashChargeType::name)));

  private static CashChargeType of(String type) {
    return CashChargeType.valueOf(TYPE_MAP.get(type));
  }
}