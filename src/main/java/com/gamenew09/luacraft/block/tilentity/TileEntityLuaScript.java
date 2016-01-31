package com.gamenew09.luacraft.block.tilentity;

import com.gamenew09.luacraft.LuacraftMod;
import com.gamenew09.luacraft.lua.LuaImplementation;
import li.cil.repack.org.luaj.vm2.LuaValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import java.io.File;
import java.io.IOException;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class TileEntityLuaScript extends TileEntity {

    private LuaImplementation luaEnv;

    public LuaImplementation getLuaEnv() {
        return luaEnv;
    }

    private String scriptString;

    public String getScriptString() {
        return scriptString;
    }

    public TileEntityLuaScript(NBTTagCompound compound){
        this();
        readFromNBT(compound);
    }

    public TileEntityLuaScript(){
        luaEnv = new LuaImplementation();
        luaEnv.setTileEntity(this);
        luaEnv.register();
    }

    public TileEntityLuaScript(String start){
        this();
        this.scriptString = start;
    }

    public void setScriptString(String scriptString) {
        this.scriptString = scriptString;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("ScriptFile", scriptString);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        scriptString = compound.getString("ScriptFile");
    }

    public File getFileObject(){
        return new File(scriptString);
    }

    public boolean createFile() {
        try {
            return getFileObject().createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    boolean ran = false;

    @Override
    public void updateEntity() {
        if(worldObj != null && !worldObj.isRemote){
            if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && !ran){
                runLua();
                ran = true;
            }
            else if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
                ran = false;
        }
    }

    public LuaImplementation resetLuaEnv(){
        return luaEnv = new LuaImplementation();
    }

    public LuaValue runLua(){
        try {
            return luaEnv.run(scriptString);
        }catch (Exception ex){
            System.out.println("Block at ("+ xCoord +","+ yCoord +","+ zCoord +") errored. Stacktrace:"+ex);
            return LuaValue.NIL;
        }
    }

    private int[] powerArray = new int[6];

    public int isProvidingStrongPower(IBlockAccess w, int x, int y, int z, int side) {
        try {
            //System.out.println("Power for side \""+side+"\": "+powerArray[side]);
            return powerArray[side];
        }catch (Throwable ex){
            return 0;
        }
    }

    public boolean setPowerStatus(int side, int power) {
        try {
            powerArray[side] = power;
            return true;
        }catch (Throwable t){
            return false;
        }
    }
}
