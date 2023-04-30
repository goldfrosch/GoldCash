package com.goldfrosch.object.model;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class CashShopItem {
  private Material material;
  private int customModelData;
  private int amount;
}
