package com.goldfrosch.object.entity;

import com.goldfrosch.object.type.CashChargeType;
import com.goldfrosch.object.type.CashUseStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashDAO {

  private UUID uuid;

  private int amount;

  private CashChargeType cashChargeType;

  private CashUseStatus cashUseStatus;

  private String manager;

  private String product;
}
