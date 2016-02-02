package com.gamenew09.luacraft.lua;

import com.gamenew09.luacraft.lua.types.LuaPlant;
import li.cil.repack.org.luaj.vm2.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class LuaProxyBlock extends Block {

    private LuaTable table;

    LuaValue getFromTable(String name){
        return table.rawget(name);
    }

    LuaValue getUserdata(Object o){
        return LuaImplementation.getUserdata(o);
    }

    public LuaProxyBlock(Material p_i45394_1_, LuaTable tbl) {
        super(p_i45394_1_);
        table = tbl;
        setBlockName(getFromTable("Name").tojstring());
        setBlockTextureName(getFromTable("Texture").tojstring());
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        try {
            LuaFunction func = table.rawget("CanHarvestBlock").checkfunction();
            Varargs args = func.invoke(new LuaValue[]{
                    LuaImplementation.getUserdata(player),
                    LuaValue.valueOf(meta)
            });
            if (args.narg() == 0)
                return super.canHarvestBlock(player, meta);
            return args.toboolean(1);
        }catch (Exception ex){
            return super.canHarvestBlock(player, meta);
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        try {
            LuaFunction func = table.rawget("CanSustainPlant").checkfunction();
            Varargs args = func.invoke(new LuaValue[]{
                    getUserdata(new LuaPlant(world, x, y, z, direction, plantable))
            });

            if (args.narg() == 0)
                return super.canSustainPlant(world, x, y, z, direction, plantable);
            return args.toboolean(1);
        }catch (Exception ex){
            return super.canSustainPlant(world, x, y, z, direction, plantable);
        }
    }
}
