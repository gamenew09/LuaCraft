package com.gamenew09.luacraft.lua.types;

import com.gamenew09.repack.org.luaj.vm2.lib.jse.IgnoreInLua;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by TylerDesktop on 2/3/2016.
 */
public class LuaNBT extends LuaBase {

    private NBTTagCompound compound;

    public LuaNBT(NBTTagCompound compound) {
        this.compound = compound;
    }

    public int getInteger(String name) {
        return compound.getInteger(name);
    }

    public long getLong(String name) {
        return compound.getLong(name);
    }

    public float getFloat(String name) {
        return compound.getFloat(name);
    }

    public double getDouble(String name) {
        return compound.getDouble(name);
    }

    public String getString(String name) {
        return compound.getString(name);
    }

    public LuaNBT getCompoundTag(String name) {
        return new LuaNBT(compound.getCompoundTag(name));
    }

    public boolean getBoolean(String name) {
        return compound.getBoolean(name);
    }

    public boolean hasNoTags() {
        return compound.hasNoTags();
    }

    public byte getByte(String name) {
        return compound.getByte(name);
    }

    public boolean hasKey(String name) {
        return compound.hasKey(name);
    }

    public void setInteger(String name, int value) {
        compound.setInteger(name, value);
    }

    public void setLong(String name, long value) {
        compound.setLong(name, value);
    }

    public void setFloat(String name, float value) {
        compound.setFloat(name, value);
    }

    public void setDouble(String name, double value) {
        compound.setDouble(name, value);
    }

    public void setString(String name, String value) {
        compound.setString(name, value);
    }

    public void setCompoundTag(String name, LuaNBT value) {
        compound.setTag(name, value.toCompound());
    }

    public LuaNBT copy() {
        return new LuaNBT((NBTTagCompound) compound.copy());
    }

    @IgnoreInLua
    public NBTTagCompound toCompound() {
        return compound;
    }

}
