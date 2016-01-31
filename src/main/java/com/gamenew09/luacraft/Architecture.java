package com.gamenew09.luacraft;

/**
 * Created by TylerDesktop on 1/28/2016.
 */
public class Architecture {

    public static String OS_ARCH = System.getProperty("os.arch");

    private static boolean isOSArchMatch(String archPrefix){
        return OS_ARCH != null && OS_ARCH.startsWith(archPrefix);
    }

    public static boolean IS_OS_ARM = isOSArchMatch("arm");

    public static boolean IS_OS_X86 = isOSArchMatch("x86") || isOSArchMatch("i386");

    public static boolean IS_OS_X64 = isOSArchMatch("x86_64") || isOSArchMatch("amd64");

}
