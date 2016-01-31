package com.gamenew09.luacraft.block.tilentity;

import com.gamenew09.luacraft.LuacraftMod;
import li.cil.repack.org.luaj.vm2.LuaValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.io.File;
import java.io.IOException;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class TileEntityLuaScript extends TileEntity {

    private String scriptString;

    public String getScriptString() {
        return scriptString;
    }

    String start;

    public TileEntityLuaScript(String start){
        this.start = start;

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

    public LuaValue runLua(){
        return LuacraftMod.getWorldLua().run(scriptString);
    }
}
