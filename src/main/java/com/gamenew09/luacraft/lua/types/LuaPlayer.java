package com.gamenew09.luacraft.lua.types;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class LuaPlayer {

    private EntityPlayer player;

    public LuaPlayer(EntityPlayer ply){
        player = ply;
    }

    public boolean addItemStack(LuaItemstack stack){
        return player.inventory.addItemStackToInventory(stack.getJavaStack());
    }

    public String getName(){
        return player.getDisplayName();
    }

    public int getGamemode(){
        return ((EntityPlayerMP)player).theItemInWorldManager.getGameType().getID();
    }

    public void playSound(String soundName, float volume, float pitch){
        player.worldObj.playSoundAtEntity(player, soundName, volume, pitch);
    }

    public void playSoundExceptSelf(String soundName, float volume, float pitch){
        player.worldObj.playSoundToNearExcept(player, soundName, volume, pitch);
    }

}
