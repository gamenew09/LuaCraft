package com.gamenew09.luacraft.gui;

import com.gamenew09.luacraft.LuacraftMod;
import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import com.gamenew09.luacraft.networking.MessageUpdateLuaScriptTileEntity;
import net.minecraft.client.gui.GuiScreen;

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

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if(p_73869_2_ == 16){
            tileEntity.setScriptString("print(redstone.output(0, 15))");
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
