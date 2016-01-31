package com.gamenew09.luacraft.lua.types;

import net.minecraft.item.ItemStack;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class LuaItemstack {

    private ItemStack itemStack;

    public LuaItemstack(ItemStack stack){
        itemStack = stack;
    }

    public ItemStack getJavaStack(){
        return itemStack;
    }

}
