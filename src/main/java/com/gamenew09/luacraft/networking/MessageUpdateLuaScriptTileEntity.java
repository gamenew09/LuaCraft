package com.gamenew09.luacraft.networking;

import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.io.FileInputStream;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class MessageUpdateLuaScriptTileEntity implements IMessage {

    private int x, y, z, dim;
    private String script;

    private boolean corrupted = false;

    public MessageUpdateLuaScriptTileEntity(TileEntityLuaScript te){
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
        dim = te.getWorldObj().provider.dimensionId;
        script = "";
    }

    /**
     * Needed for the Network Handler to create the message on the server end.
     */
    public MessageUpdateLuaScriptTileEntity(){

    }

    public MessageUpdateLuaScriptTileEntity(int x, int y, int z, int dim, String script){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.script = script;
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

    public int getDim() {
        return dim;
    }

    public String getScript() {
        return script;
    }

    /**
     * Did the packet error while reading?
     * @return If the packet errored while reading.
     */
    public boolean isCorrupted() {
        return corrupted;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public boolean updateTileEntity(){
        World w = MinecraftServer.getServer().worldServerForDimension(dim);
        TileEntityLuaScript scr = (TileEntityLuaScript) w.getTileEntity(x, y, z);
        scr.setScriptString(this.script);
        w.setTileEntity(x, y, z, scr);
        return scr.saveFile();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        script = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufUtils.writeUTF8String(buf, script);
    }

    public static class Handler implements IMessageHandler<MessageUpdateLuaScriptTileEntity, IMessage> {

        @Override
        public IMessage onMessage(MessageUpdateLuaScriptTileEntity message, MessageContext ctx) {
            if(!message.isCorrupted()){
                String str = "("+message.x+","+message.y+","+message.z+")";
                System.out.println("Tried to update tile entity at "+str+ " in DIM "+ message.dim + ". Success: " + message.updateTileEntity());
            }
            return null; // no response in this case
        }
    }
}
