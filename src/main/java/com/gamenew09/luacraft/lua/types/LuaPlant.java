package com.gamenew09.luacraft.lua.types;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class LuaPlant {

    private int x, y, z;
    private ForgeDirection direction;
    private IPlantable plantable;
    private IBlockAccess access;

    public LuaPlant(IBlockAccess access, int x, int y, int z, ForgeDirection direction, IPlantable plantable){
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.plantable = plantable;
        this.access = access;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public ForgeDirection getDirection() {
        return direction;
    }

    public IPlantable getPlantable() {
        return plantable;
    }

    public EnumPlantType getPlantType(){
        return plantable.getPlantType(access, x, y, z);
    }

    public Block getPlant(){
        return plantable.getPlant(access, x, y, z);
    }

    public int getPlantMetadata(){
        return plantable.getPlantMetadata(access, x, y, z);
    }
}
