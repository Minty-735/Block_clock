package org.minty.block_clock.trash;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.minty.block_clock.METADATA;
import org.minty.block_clock.utils.IconMenu;

public class Number {


    public static void main(String[] args) {
        Plugin plugin = METADATA.PLUGIN;
        IconMenu menu = new IconMenu("Clocks settings", 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {


                event.setWillClose(true);
            }
        }, plugin)
                .setOption(3, new ItemStack(Material.APPLE, 1), "Food", "The food is delicious").setOption(4, new ItemStack(Material.IRON_SWORD, 1), "Weapon", "Weapons are for awesome people").setOption(5, new ItemStack(Material.EMERALD, 1), "Money", "Money brings happiness");


        menu.setOption(1,new ItemStack(Material.CLOCK,1),"clock1","description???? ? ? ? ?");


    }

}
