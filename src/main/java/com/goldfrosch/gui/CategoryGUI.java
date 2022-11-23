package com.goldfrosch.gui;

import java.util.Arrays;
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
  private final Inventory categoryGui = Bukkit.createInventory(null, 9, "캐시 상점 카테고리");
  
  public CategoryGUI() {
    this.initializeItems();
  }

  public void initializeItems() {
    categoryGui.addItem(createGuiItem(Material.DIAMOND_SWORD, "Example Sword", "§aFirst line of the lore", "§bSecond line of the lore"));
    categoryGui.addItem(createGuiItem(Material.IRON_HELMET, "§bExample Helmet", "§aFirst line of the lore", "§bSecond line of the lore"));
  }

  protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
    final var item = new ItemStack(material, 1);
    final var meta = item.getItemMeta();

    // Set the name of the item
    meta.setDisplayName(name);

    // Set the lore of the item
    meta.setLore(Arrays.asList(lore));

    item.setItemMeta(meta);

    return item;
  }

  public void openInventory(final HumanEntity player) {
    player.openInventory(categoryGui);
  }

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent e) {
    if (!e.getInventory().equals(categoryGui)) return;

    e.setCancelled(true);

    final ItemStack clickedItem = e.getCurrentItem();

    if (clickedItem == null || clickedItem.getType().isAir()) return;

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
