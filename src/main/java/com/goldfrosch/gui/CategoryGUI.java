package com.goldfrosch.gui;

import static com.goldfrosch.GoldCash.plugin;

import com.goldfrosch.config.ShopCategoryConfig;
import com.goldfrosch.object.model.CashShopCategory;

import java.util.*;

import com.goldfrosch.object.model.CashShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CategoryGUI implements Listener {

  private final static int INVENTORY_SIZE = 54;
  private final static Inventory categoryGui = Bukkit.createInventory(null, INVENTORY_SIZE,
      "캐시 상점 카테고리");

  public CategoryGUI() {
    this.initializeItems();
  }

  public void initializeItems() {
    var configSection = ShopCategoryConfig.categoryConfig.getConfigurationSection("category");
    var itemArray = Objects.requireNonNull(configSection).getKeys(false);
    var itemKeyList = new ArrayList<>(itemArray);
    for (int i = 0; i < INVENTORY_SIZE; i++) {
      if ((i / 9) == 0 || (i / 9) == 5 || (i % 9) == 0 || (i % 9) == 8) {
        categoryGui.setItem(i, createGuiItem(CashShopItem.builder().title("").material(Material.GRAY_STAINED_GLASS_PANE).lore(new ArrayList<>()).build()));
      } else {
        var itemObject = (CashShopItem) configSection.get(itemKeyList.get(0));
        if (itemObject != null) {
          categoryGui.addItem(createGuiItem(itemObject));
        }
      }
    }
  }

  protected ItemStack createGuiItem(final CashShopItem cashShopItem) {
    final var item = new ItemStack(cashShopItem.getMaterial(), cashShopItem.getAmount());
    final var meta = item.getItemMeta();

    Objects.requireNonNull(meta).setDisplayName(cashShopItem.getTitle());

    meta.setLore(cashShopItem.getLore());

    item.setItemMeta(meta);

    return item;
  }

  public static void openInventory(final HumanEntity player) {
    var configSection = ShopCategoryConfig.categoryConfig.getConfigurationSection("category");
    var itemArray = Objects.requireNonNull(configSection).getKeys(false);
    itemArray.forEach(item -> {

    });
    player.openInventory(categoryGui);
  }

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent e) {
    if (!e.getInventory().equals(categoryGui)) {
      return;
    }

    e.setCancelled(true);

    final ItemStack clickedItem = e.getCurrentItem();

    if (clickedItem == null || clickedItem.getType().isAir()) {
      return;
    }

    final var player = (Player) e.getWhoClicked();

    player.sendMessage("You clicked at slot " + e.getRawSlot());
  }

  @EventHandler
  public void onInventoryClick(final InventoryDragEvent e) {
    if (e.getInventory().equals(categoryGui)) {
      e.setCancelled(true);
    }
  }
}
