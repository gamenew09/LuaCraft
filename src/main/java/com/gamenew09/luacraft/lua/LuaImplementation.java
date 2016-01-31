package com.gamenew09.luacraft.lua;

import com.gamenew09.luacraft.Resources;
import com.gamenew09.luacraft.block.tilentity.TileEntityLuaScript;
import com.gamenew09.luacraft.lua.types.LuaItemstack;
import cpw.mods.fml.common.registry.GameRegistry;
import li.cil.repack.org.luaj.vm2.*;
import li.cil.repack.org.luaj.vm2.lib.VarArgFunction;
import li.cil.repack.org.luaj.vm2.lib.jse.CoerceJavaToLua;
import li.cil.repack.org.luaj.vm2.lib.jse.CoerceLuaToJava;
import li.cil.repack.org.luaj.vm2.lib.jse.JsePlatform;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TylerDesktop on 1/28/2016.
 */

public class LuaImplementation {

    private Globals globals;

    private static final Varargs VARARGS_NONE = new Varargs() {
        @Override
        public LuaValue arg(int i) {
            return LuaValue.NIL;
        }

        @Override
        public int narg() {
            return 0;
        }

        @Override
        public LuaValue arg1() {
            return LuaValue.NIL;
        }

        @Override
        public Varargs subargs(int i) {
            return this.eval();
        }
    };

    public LuaImplementation(){
        globals = JsePlatform.standardGlobals();
    }

    public LuaImplementation(TileEntityLuaScript te){
        this();
        tileEntityLuaScript = te;
    }

    /**
     * Helper function to create a Userdata object using CoerceJavaToLua.
     * @param obj
     * @return LuaUserdata or LuaValue.NIL.
     */
    public static LuaValue getUserdata(Object obj){
        return CoerceJavaToLua.coerce(obj);
    }


    private void registerEnums(){
        HashMap<String, LuaValue> enumMap = new HashMap<String, LuaValue>();

        HashMap<String, LuaValue> enumBlockFace = new HashMap<String, LuaValue>();
        enumBlockFace.put("Down", LuaInteger.valueOf(1));
        enumBlockFace.put("Up", LuaInteger.valueOf(0));
        enumBlockFace.put("North", LuaInteger.valueOf(2));
        enumBlockFace.put("South", LuaInteger.valueOf(3));
        enumBlockFace.put("West", LuaInteger.valueOf(4));
        enumBlockFace.put("East", LuaInteger.valueOf(5));

        enumMap.put("BlockFace", createTableFromHashMap(enumBlockFace));

        globals.set("Enum", createTableFromHashMap(enumMap));
    }

    TileEntityLuaScript tileEntityLuaScript = null;

    public void register(){
        registerEnums();

        HashMap<String, LuaValue> worldMap = new HashMap<String, LuaValue>();

        worldMap.put("inWorld", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if(MinecraftServer.getServer() == null){
                    return createVarArgs(LuaBoolean.valueOf(false));
                }
                return boolVarArgs(MinecraftServer.getServer().getEntityWorld() != null);
            }
        });

        worldMap.put("setBlock", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if(MinecraftServer.getServer() == null){
                    return createVarArgs(LuaBoolean.valueOf(false));
                }

                World w = MinecraftServer.getServer().getEntityWorld();

                int mdata = 0;

                if(args.narg() >= 5){
                    mdata = args.toint(4);
                }

                return boolVarArgs(w.setBlock(args.toint(0), args.toint(1), args.toint(2), Block.getBlockFromName(args.tojstring(3)), mdata, 3));
            }
        });

        globals.set("world", createTableFromHashMap(worldMap));

        HashMap<String, LuaValue> hooksMap = new HashMap<String, LuaValue>();

        hooksMap.put("add", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                funcs.put(args.tojstring(2), new HookInfo(args.checkfunction(3), args.tojstring(1)));
                return VARARGS_NONE;
            }
        });

        globals.set("hook", createTableFromHashMap(hooksMap));

        HashMap<String, LuaValue> itemstackMap = new HashMap<String, LuaValue>();

        itemstackMap.put("create", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                String[] split = args.tojstring(1).split(":");
                String name = "";
                String mod = "";

                int count = 1;

                if(args.narg() > 1){
                    count = args.toint(2);
                }

                if(split.length == 1){
                    name = args.tojstring(1);
                    mod = "minecraft";
                }else{
                    name = split[1];
                    mod = split[0];
                }
                System.out.println(mod + ":" + name);
                return createVarArgs(getUserdata(new LuaItemstack(GameRegistry.findItemStack(mod, name, 1))));
            }
        });

        globals.set("itemstack", createTableFromHashMap(itemstackMap));

        HashMap<String, LuaValue> redstoneMap = new HashMap<String, LuaValue>();

        redstoneMap.put("output", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if(tileEntityLuaScript == null)
                    throw new LuaError("\"redstone.output\" can only be used in \"LuaScript\" blocks.");

                int side = args.toint(1);
                int power = args.toint(2);

                return createVarArgs(LuaValue.valueOf(tileEntityLuaScript.setPowerStatus(side, power)));
            }
        });

        redstoneMap.put("isPoweredIndirectly", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int x = 0;
                int y = 0;
                int z = 0;
                int dim = 0;
                if(args.narg() == 0){
                    if(tileEntityLuaScript == null)
                        throw new LuaError("\"redstone.isPoweredIndirectly\" with no parameters can only be used in \"LuaScript\" blocks.");
                    x = tileEntityLuaScript.xCoord;
                    y = tileEntityLuaScript.yCoord;
                    z = tileEntityLuaScript.zCoord;
                }else{
                    x = args.toint(1);
                    y = args.toint(2);
                    z = args.toint(3);
                    if(args.narg() > 3)
                        dim = args.toint(4);
                }
                return createVarArgs(LuaValue.valueOf(MinecraftServer.getServer().worldServerForDimension(dim).isBlockIndirectlyGettingPowered(x, y, z)));
            }
        });

        globals.set("redstone", createTableFromHashMap(redstoneMap));
    }

    public void setTileEntity(TileEntityLuaScript tileEntityLuaScript) {
        this.tileEntityLuaScript = tileEntityLuaScript;
    }

    public static class HookInfo{
        public LuaFunction Function;
        public String HookType;

        public HookInfo(LuaFunction func, String type){
            Function = func;
            HookType = type;
        }

        public void invoke(LuaValue... values){
            Function.invoke(values);
        }
    }

    public int callHook(String hook, LuaValue... values){
        int c = 0;
        for (HookInfo info : funcs.values()) {
            System.out.println(info.HookType);
            if(info.HookType.equals(hook)){
                c++;
                info.invoke(values);
            }
        }
        return c;
    }

    HashMap<String, HookInfo> funcs = new HashMap<String, HookInfo>();

    public void clearHooks(){
        funcs.clear();
    }

    Varargs boolVarArgs(boolean b){
        return createVarArgs(LuaBoolean.valueOf(b));
    }

    Varargs createVarArgs(final LuaValue... vals){
        return new ArrayVarargs(vals);
    }

    LuaTable createTableFromHashMap(HashMap<String, LuaValue> map){
        LuaTable tbl = new LuaTable(map.size(), map.size() >> 1);
        for (Map.Entry<String, LuaValue> entry : map.entrySet()) {
            if(!entry.getValue().isnil()) {
                tbl.rawset(LuaString.valueOf(entry.getKey()), entry.getValue());
            }
        }
        return tbl;
    }

    LuaValue[] varargsToArray(Varargs args, int offset){
        LuaValue[] vals = new LuaValue[args.narg() - offset];
        int c = 0;
        for(int i = offset; i < args.narg(); i++){
            vals[c] = args.arg(i);
            c++;
        }
        return vals;
    }

    LuaValue[] varargsToArray(Varargs args){
        return varargsToArray(args, 0);
    }

    public LuaValue runResourceFile(String luaPath){
        return globals.loadfile("assets/"+ Resources.MOD_ID+"/lua/"+luaPath).call();
    }

    public LuaValue runFile(String file){
        return globals.loadfile(file).call();
    }

    public LuaValue run(String script){
        return globals.load(script).call();
    }

    public LuaValue runWorldLua(World w, String path){
        return globals.loadfile(w.getSaveHandler().getWorldDirectory().getAbsolutePath() + "\\lua\\"+path).call();
    }

}
