package com.gamenew09.luacraft;

import com.gamenew09.luacraft.block.BlockRegistry;
import com.gamenew09.luacraft.lua.LuaImplementation;
import com.gamenew09.luacraft.networking.luamessages.MessageSpawnParticle;
import com.gamenew09.luacraft.networking.MessageUpdateLuaScriptTileEntity;
import com.gamenew09.luacraft.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import li.cil.repack.org.luaj.vm2.LuaValue;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@Mod(modid = Resources.MOD_ID, version = Resources.MOD_VERSION)
public class LuacraftMod
{

    private static LuaImplementation coreLua;
    private static LuaImplementation worldLua;

    public static LuaImplementation getWorldLua(){
        return worldLua;
    }

    @Mod.Instance
    public static LuacraftMod instance;

    @SidedProxy(clientSide=Resources.MOD_PROXY_CLIENT, serverSide=Resources.MOD_PROXY_SERVER)
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper network;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Resources.MOD_ID+"channel");
        network.registerMessage(MessageUpdateLuaScriptTileEntity.Handler.class, MessageUpdateLuaScriptTileEntity.class, 0, Side.SERVER);
        network.registerMessage(MessageSpawnParticle.Handler.class, MessageSpawnParticle.class, 0, Side.CLIENT);

        coreLua = new LuaImplementation();
        worldLua = new LuaImplementation();

        coreLua.register();
        worldLua.register();

        coreLua.runResourceFile("main.lua");

        MinecraftForge.EVENT_BUS.register(new LuaCraftEvents());
        FMLCommonHandler.instance().bus().register(new LuaCraftEvents());

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new LuaCraftGuiHandler());

        BlockRegistry.registerBlocks();
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        File luaDir = new File(MinecraftServer.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "\\lua");

        if(!luaDir.exists())
            luaDir.mkdir();

        File luaBlockDir = new File(MinecraftServer.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "\\lua\\scriptblock\\");

        if(!luaBlockDir.exists())
            luaBlockDir.mkdir();


        try {
            System.out.println(worldLua.runWorldLua(MinecraftServer.getServer().getEntityWorld(), "main.lua"));
        }catch (Exception ex){  }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    public int callAllHooks(String type, LuaValue... args){
        int a = coreLua.callHook(type, args);
        int b = worldLua.callHook(type, args);
        return a + b;
    }

}
