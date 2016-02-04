package com.gamenew09.luacraft.block.tilentity;

import com.gamenew09.luacraft.LuacraftMod;
import com.gamenew09.luacraft.lua.LuaImplementation;
import com.gamenew09.repack.org.luaj.vm2.LuaValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

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
        //scriptName = generateFileString(5);
    }

    private String generateFileString(int length) {
        String str = "";
        Random rnd = new Random();
        for(int i = 0; i < length; i++){
            str += rnd.nextInt(9);
        }
        return str;
    }

    public void setScriptString(String scriptString) {
        this.scriptString = scriptString;
    }


    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    public String getFileName(){
        return "DIM"+ getWorldObj().provider.dimensionId +"("+xCoord+","+yCoord+","+zCoord+")";
    }

    public File getFileObject(){
        return new File(MinecraftServer.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "\\lua\\scriptblock\\"+getFileName()+".lua".replace("\\.", ""));
    }

    public boolean createFile() {
        if(!getFileObject().exists())
            return false;
        try {
            return getFileObject().createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    boolean ran = false;

    @Override
    public void updateEntity() {
        if(!getFileObject().exists()) {
            saveFile();
        }

        if(!isRegistered() || luaEnv == null){
            luaEnv.setTileEntity(this);
            luaEnv.register();
            registered = true;
        }
        if(worldObj != null && !worldObj.isRemote){
            if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && !ran){
                runLua();
                ran = true;
            }
            else if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
                ran = false;
        }
    }

    boolean registered = false;

    public boolean isRegistered() {
        return registered;
    }

    public LuaImplementation resetLuaEnv(){
        registered = false;
        return luaEnv = new LuaImplementation();
    }

    String[] getLinesFromReader(BufferedReader reader){
        ArrayList<String> strs = new ArrayList<String>();

        String next = "";

        while(next != null) {
            try {
                next = reader.readLine();
                if(next != null)
                    strs.add(next);
            } catch (Exception ex) {
                break;
            }
        }

        String[] str = new String[strs.size()];
        return strs.toArray(str);
    }

    String getCLines(String[] strs){
        String s = "";
        for (String str : strs) {
            s += str + "\n";
        }
        return s;
    }

    public boolean loadLua(){
        createFile();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFileObject()), "UTF-8"));
            String script = getCLines(getLinesFromReader(reader));
            reader.close();
            scriptString = script;
            return true;
        }catch (Throwable ex){
            return false;
        }
    }

    String fileName = "";

    public LuaValue runLua(){
        loadLua();
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

    public boolean saveFile() {
        createFile();
        try{
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(getFileObject()));

            writer.write(scriptString);

            writer.close();

            return true;
        }catch (Exception ex){
            return false;
        }
    }

    private int comparatorInput = 0;

    public void setComparatorInput(int comparatorInput) {
        this.comparatorInput = comparatorInput;
    }

    public int getComparatorInput() {
        return comparatorInput;
    }

    public int getComparatorInputOverride(World w, int x, int y, int z, int side) {
        return getComparatorInput();
    }
}
