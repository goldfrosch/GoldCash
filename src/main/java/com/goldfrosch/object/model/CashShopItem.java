package com.goldfrosch.object.model;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@Builder
public class CashShopItem {
  private Material material;
  private int customModelData;
  private int amount;

  private String title;

  private List<String> lore;
}
