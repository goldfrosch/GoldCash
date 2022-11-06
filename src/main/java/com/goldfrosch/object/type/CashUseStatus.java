package com.goldfrosch.object.type;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CashUseStatus {
  ADMIN_CHARGE("어드민_테스트_지급"),
  ADMIN_TAKE("어드민_강제_회수"),
  BOUGHT("구매"),
  CHARGE("충전"),
  EVENT_CHARGE("이벤트_지급");

  private final String desc;

  private static final Map<String, String> STATUS_MAP = Collections.unmodifiableMap(
      Stream.of(values()).collect(
          Collectors.toMap(CashUseStatus::getDesc, CashUseStatus::name)));

  public static CashUseStatus of(String type) {
    return CashUseStatus.valueOf(STATUS_MAP.get(type));
  }
}

