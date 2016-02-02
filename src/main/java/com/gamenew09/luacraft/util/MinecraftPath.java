package com.gamenew09.luacraft.util;

import java.io.File;

/**
 * Created by TylerDesktop on 2/2/2016.
 */
public class MinecraftPath {

    public static String getWorkingDirectory(){
        return new File(".").getAbsolutePath().replace("\\.", "");
    }

}
