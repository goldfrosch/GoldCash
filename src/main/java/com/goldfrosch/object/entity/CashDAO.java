package com.goldfrosch.object.entity;

import com.goldfrosch.object.type.CashChargeType;
import com.goldfrosch.object.type.CashUseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
@Builder
public class CashDAO {
  private Player player;

  private int amount;

  private CashChargeType cashChargeType;

  private CashUseStatus cashUseStatus;

  private Player manager;

  private String product;
}
