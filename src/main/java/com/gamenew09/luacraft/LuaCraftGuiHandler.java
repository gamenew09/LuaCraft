package com.gamenew09.luacraft;

import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import com.gamenew09.luacraft.gui.GuiLuaScript;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class LuaCraftGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return null;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0:
                return new GuiLuaScript((TileEntityLuaScript)world.getTileEntity(x, y, z));
        }
        return null;
    }
}
