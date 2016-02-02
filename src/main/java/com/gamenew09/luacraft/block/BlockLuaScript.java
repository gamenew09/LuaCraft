package com.gamenew09.luacraft.block;

import com.gamenew09.luacraft.LuacraftMod;
import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class BlockLuaScript extends BlockContainer{

    protected BlockLuaScript() {
        super(Material.rock);
    }

    String getRandomFileName(int len){
        String str = "";

        for(int i = 0; i < len; i++)
            str += new Random().nextInt(9);

        return str;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }


    @Override
    public int isProvidingStrongPower(IBlockAccess w, int x, int y, int z, int side) {
        return ((TileEntityLuaScript)w.getTileEntity(x, y, z)).isProvidingStrongPower(w, x, y, z, side);
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int m, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        ply.openGui(LuacraftMod.instance, 0, w, x, y, z);
        return true;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityLuaScript();
    }
}
