package com.gamenew09.luacraft.lua.types;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class LuaIconRegister extends LuaBase{

    private IIconRegister register;

    public LuaIconRegister(IIconRegister register){
        this.register = register;
    }

    public IIcon registerIcon(String name){
        return register.registerIcon(name);
    }

}
