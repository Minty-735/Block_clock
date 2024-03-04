package org.minty.block_clock.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class inventGUI implements Listener {

    public static Inventory Myinventory = Bukkit.createInventory(null, 36, "settings menu");

    static {
        Myinventory.setItem(0, new ItemStack(Material.BARRIER, 2));
        Myinventory.setItem(35, new ItemStack(Material.BARRIER, 64));

    }

    @EventHandler
    public void OnInventoryClicked(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        if (event.getView().getTitle().equals("settings menu")) {

            p.sendMessage(String.valueOf(item.getAmount()));


            event.setCancelled(true);


        }
    }
}
