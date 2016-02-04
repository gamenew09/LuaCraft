package com.gamenew09.luacraft.lua;

import com.gamenew09.luacraft.lua.types.LuaIconRegister;
import com.gamenew09.luacraft.lua.types.LuaItemstack;
import com.gamenew09.luacraft.lua.types.LuaPlant;
import com.gamenew09.luacraft.util.ItemStackHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.gamenew09.repack.org.luaj.vm2.*;
import com.gamenew09.repack.org.luaj.vm2.lib.jse.CoerceLuaToJava;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class LuaProxyBlock extends Block {

    LuaValue getFromTable(String name){
        return tbl.rawget(name);
    }

    LuaValue getUserdata(Object o){
        return LuaImplementation.getUserdata(o);
    }

    Varargs invoke(String name, Object... params){
        return (Varargs) lua.invoke(name, params);
    }

    LuaImplementation lua;
    LuaTable tbl;

    public LuaProxyBlock(Material p_i45394_1_, LuaTable tbl, LuaImplementation lua) {
        super(p_i45394_1_);
        this.tbl = tbl;
        this.lua = lua;
        setBlockName(getFromTable("Name").tojstring());
        setBlockTextureName(getFromTable("Texture").tojstring());
    }

    @Override
    public Material getMaterial() {
        if(getFromTable("Material") != LuaValue.NIL)
            return (Material) CoerceLuaToJava.coerce(getFromTable("Material"), Material.class);
        else
            return super.getMaterial();
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        try {
            Varargs args = invoke("CanHarvestBlock", player, meta);
            if (args.narg() == 0)
                return super.canHarvestBlock(player, meta);
            return args.toboolean(1);
        }catch (Exception ex){
            ex.printStackTrace();
            return super.canHarvestBlock(player, meta);
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        try {
            Varargs args = invoke("CanSustainPlant", new LuaPlant(world, x, y, z, direction, plantable));

            if (args.narg() == 0)
                return super.canSustainPlant(world, x, y, z, direction, plantable);
            return args.toboolean(1);
        }catch (Exception ex){
            ex.printStackTrace();
            return super.canSustainPlant(world, x, y, z, direction, plantable);
        }
    }

    @Override
    protected boolean canSilkHarvest() {
        try {
            Varargs args = invoke("CanSilkHarvest");

            if (args.narg() == 0)
                return super.canSilkHarvest();
            return args.toboolean(1);
        }catch (Exception ex){
            return super.canSilkHarvest();
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        try {
            Varargs args = invoke("GetDrops", world, x, y, z, metadata, fortune);

            if (args.narg() == 0)
                return super.getDrops(world, x, y, z, metadata, fortune);
            LuaTable tbl = args.checktable(1);

            ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
            for (int i = 1; i <= tbl.length(); i++){
                LuaValue value = tbl.rawget(i);
                if(value.isuserdata(LuaItemstack.class)){
                    System.out.println(((LuaItemstack)CoerceLuaToJava.coerce(value, LuaItemstack.class)).getJavaStack());
                    if(((LuaItemstack)CoerceLuaToJava.coerce(value, LuaItemstack.class)).getJavaStack() == null)
                        continue;
                    stacks.add(((LuaItemstack)CoerceLuaToJava.coerce(value, LuaItemstack.class)).getJavaStack());
                }else if(value.isuserdata(ItemStack.class)){
                    stacks.add((ItemStack) CoerceLuaToJava.coerce(value, LuaItemstack.class));
                }else if(value.isstring()){
                    stacks.add(ItemStackHelper.getStackFromString(value.tojstring()));
                }else{
                    System.out.println("Object in index \""+i+"\" failed to be put under a drop.");
                }
            }
            return stacks;
        }catch (Exception ex){
            ex.printStackTrace();

            return super.getDrops(world, x, y, z, metadata, fortune);
        }
    }



    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        try {
            Varargs args = invoke("GetIcon", side, meta);

            if (args.narg() == 0)
                return super.getIcon(side, meta);

            if(CoerceLuaToJava.coerce(args.arg(1), IIcon.class) == null)
                return  super.getIcon(side, meta);

            return (IIcon) CoerceLuaToJava.coerce(args.arg(1), IIcon.class);
        }catch (Exception ex){
            ex.printStackTrace();
            return super.getIcon(side, meta);
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        try {
            LuaIconRegister reg = new LuaIconRegister(p_149651_1_);
            LuaValue val = getUserdata(reg);
            System.out.println("RegisterBlockIcons: "+val.isnil());
            Varargs args = invoke("RegisterBlockIcons", val);
        }catch (Exception ex){
            ex.printStackTrace();
            super.registerBlockIcons(p_149651_1_);
        }
    }
}
