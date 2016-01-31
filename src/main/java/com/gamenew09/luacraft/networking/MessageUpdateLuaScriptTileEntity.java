package com.gamenew09.luacraft.networking;

import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class MessageUpdateLuaScriptTileEntity implements IMessage {

    private int x, y, z, dim;
    private NBTTagCompound compound;

    private boolean corrupted = false;

    public MessageUpdateLuaScriptTileEntity(TileEntityLuaScript te){
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
        dim = te.getWorldObj().provider.dimensionId;
        compound = new NBTTagCompound();
        te.writeToNBT(compound);
    }

    /**
     * Needed for the Network Handler to create the message on the server end.
     */
    public MessageUpdateLuaScriptTileEntity(){

    }

    public MessageUpdateLuaScriptTileEntity(int x, int y, int z, int dim, NBTTagCompound compound){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.compound = compound;
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

    public NBTTagCompound getCompound() {
        return compound;
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

    public void setCompound(NBTTagCompound compound) {
        this.compound = compound;
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

    public boolean updateTileEntity(){
        if(compound == null)
            return false;
        TileEntityLuaScript script = new TileEntityLuaScript(compound);
        World w = MinecraftServer.getServer().worldServerForDimension(dim);
        w.setTileEntity(x, y, z, script);
        return true;
    }

    boolean writeNbt(ByteBuf buf, NBTTagCompound compound) {
        try {
            if (compound == null) {
                buf.writeShort(-1);
            } else {
                byte[] abyte = CompressedStreamTools.compress(compound);
                buf.writeShort((short) abyte.length);
                buf.writeBytes(abyte);
            }
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    NBTTagCompound readNbt(ByteBuf buf) {
        corrupted = false;
        try {
            short len = buf.readShort();
            if (len == -1) {
                return null;
            } else {
                byte[] bytes = new byte[len];
                buf.readBytes(bytes);
                return CompressedStreamTools.func_152457_a(bytes, new NBTSizeTracker(2097152L));
            }
        }catch (Exception ex){
            corrupted = true;
            return null;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        compound = readNbt(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        writeNbt(buf, compound);
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
