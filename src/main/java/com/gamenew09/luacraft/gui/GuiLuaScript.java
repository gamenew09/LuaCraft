package com.gamenew09.luacraft.gui;

import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

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
        field = new GuiTextArea(fontRendererObj, 0, 0, 200, 200);
        field.setText("");
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        field.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        field.textboxKeyTyped(p_73869_1_, p_73869_2_);
        super.keyTyped(p_73869_1_, p_73869_2_);
    }

    @Override
    public void updateScreen() {
        field.updateCursorCounter();
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        this.drawDefaultBackground();

        field.drawTextBox();
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
