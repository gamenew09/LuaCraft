package com.gamenew09.luacraft;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class RegistryHelper {

    public static void register(Block block){
        GameRegistry.registerBlock(block, block.getClass().getSimpleName());
    }

    public static void register(Item item){
        GameRegistry.registerItem(item, item.getClass().getSimpleName());
    }

}
