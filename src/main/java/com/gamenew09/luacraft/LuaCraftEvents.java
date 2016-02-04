package com.gamenew09.luacraft;

import com.gamenew09.luacraft.lua.LuaImplementation;
import com.gamenew09.luacraft.lua.types.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import com.gamenew09.repack.org.luaj.vm2.LuaString;
import com.gamenew09.repack.org.luaj.vm2.LuaValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class LuaCraftEvents {

    int callAllHooks(String type, LuaValue... args){
        return LuacraftMod.instance.callAllHooks(type, args);
    }

    @SubscribeEvent
    public void onChatted(ServerChatEvent event)
    {
        callAllHooks("Chatted", LuaImplementation.getUserdata(new LuaPlayer(event.player)), LuaString.valueOf(event.message));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event){
        if(event.entity instanceof EntityPlayer){
            callAllHooks("PlayerDeath", LuaImplementation.getUserdata(new LuaPlayer((EntityPlayer) event.entity)), LuaImplementation.getUserdata(event.source), LuaImplementation.getUserdata(event.entityLiving));
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        callAllHooks("PlayerAdded", LuaImplementation.getUserdata(new LuaPlayer(event.player)));
    }

}
