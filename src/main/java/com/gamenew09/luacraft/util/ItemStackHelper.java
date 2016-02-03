package com.gamenew09.luacraft.util;

import com.gamenew09.luacraft.lua.types.LuaItemstack;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class ItemStackHelper {

    public static ItemStack getStackFromString(String path, int count){
        String[] split = path.split(":");
        String name = "";
        String mod = "";

        if(split.length == 1){
            name = path;
            mod = "minecraft";
        }else{
            name = split[1];
            mod = split[0];
        }
        return GameRegistry.findItemStack(mod, name, count);
    }

    public static ItemStack getStackFromString(String path){
        return getStackFromString(path);
    }

}
