package com.goldfrosch.object.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CashShopCategory {
  private String title;
  private List<String> lore;
  private CashShopItem item;
}
