package com.gamenew09.luacraft.gui;

import com.gamenew09.luacraft.LuacraftMod;
import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import com.gamenew09.luacraft.networking.MessageUpdateLuaScriptTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by TylerDesktop on 1/30/2016.
 */
public class GuiLuaScript extends GuiScreen {

    private TileEntityLuaScript tileEntity;

    public GuiLuaScript(TileEntityLuaScript script){
        tileEntity = script;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    GuiTextArea field;

    @Override
    public void initGui() {
        //field = new GuiTextArea(fontRendererObj, 0, 0, 200, 200);
        //field.setText("");
    }

    public void updateTileEntity(){
        LuacraftMod.network.sendToServer(new MessageUpdateLuaScriptTileEntity(tileEntity));
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        //field.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
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
            s += str + " ";
        }
        return s;
    }


    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if(p_73869_2_ == 16){
            String script = "";
            try {
                // TODO: REMOVE THIS LINE TO HAVE THE MOD WORK WITH DEDICATED SERVERS.
                InputStream stream = new FileInputStream(MinecraftServer.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "\\lua\\blockexample.lua".replace("\\.", ""));
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));;
                script = getCLines(getLinesFromReader(reader));
                reader.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.println(script);
            tileEntity.setScriptString(script);
            System.out.println("Sending TileEntityUpdate Packet to server. "+tileEntity.getScriptString());
            updateTileEntity();
        }
        super.keyTyped(p_73869_1_, p_73869_2_);
    }

    @Override
    public void updateScreen() {
        //field.updateCursorCounter();
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        this.drawDefaultBackground();

        //field.drawTextBox();
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
