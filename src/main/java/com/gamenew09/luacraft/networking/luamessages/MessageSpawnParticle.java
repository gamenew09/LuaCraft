package com.gamenew09.luacraft.networking.luamessages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class MessageSpawnParticle implements IMessage {

    public  MessageSpawnParticle(){}

    String pName;
    double x, y, z, xv, yv, zv;
    int dim;

    public MessageSpawnParticle(String pName, double x, double y, double z, double vx, double vy, double vz, int dim){
        this.pName = pName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xv = vx;
        this.yv = vy;
        this.zv = vz;
        this.dim = dim;
    }

    public String getParticleName() {
        return pName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getXVelocity() {
        return xv;
    }

    public double getYVelocity() {
        return yv;
    }

    public double getZVelocity() {
        return zv;
    }

    public int getDimensionId() {
        return dim;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pName = ByteBufUtils.readUTF8String(buf);
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        xv = buf.readDouble();
        yv = buf.readDouble();
        zv = buf.readDouble();
        dim = buf.readInt();
    }

    public void spawnParticle(){
        if(Minecraft.getMinecraft() != null){
            if(dim == Minecraft.getMinecraft().theWorld.provider.dimensionId){
                Minecraft.getMinecraft().theWorld.spawnParticle(pName, x, y, z, xv, yv, zv);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, pName);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(xv);
        buf.writeDouble(yv);
        buf.writeDouble(zv);
        buf.writeInt(dim);
    }

    public static class Handler implements IMessageHandler<MessageSpawnParticle, IMessage> {

        @Override
        public IMessage onMessage(MessageSpawnParticle message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT){
                message.spawnParticle();
            }
            return null; // no response in this case
        }
    }
}
