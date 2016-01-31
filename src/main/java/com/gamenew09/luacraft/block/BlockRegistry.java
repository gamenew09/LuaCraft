package com.gamenew09.luacraft.block;

import com.gamenew09.luacraft.RegistryHelper;
import com.gamenew09.luacraft.Resources;
import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class BlockRegistry {

    public static BlockLuaScript blockLuaScript;

    public static void registerTileEntities(){
        GameRegistry.registerTileEntity(TileEntityLuaScript.class, "TileEntityLuaScript");
    }

    public static void registerBlocks(){
        registerTileEntities();

        // Instantiate blocks
        blockLuaScript = (BlockLuaScript)new BlockLuaScript().setBlockName("blockLuaScript").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName(Resources.MOD_ID+":blockLuaScript");

        // Registers blocks for use.
        RegistryHelper.register(blockLuaScript);
    }

}
