package com.gamenew09.luacraft.lua;

import com.gamenew09.repack.org.luaj.vm2.*;

/**
 * Created by TylerDesktop on 1/29/2016.
 */
public class ArrayVarargs extends Varargs {
    private final int offset;
    private final LuaValue[] v;
    private final int length;
    private final Varargs more;

    public ArrayVarargs(LuaValue[] v, int offset, int length) {
        this.v = v;
        this.offset = offset;
        this.length = length;
        this.more = LuaValue.NONE;
    }

    public  ArrayVarargs(LuaValue[] v){
        this(v, 0, v.length);
    }

    public ArrayVarargs(LuaValue[] v, int offset, int length, Varargs more) {
        this.v = v;
        this.offset = offset;
        this.length = length;
        this.more = more;
    }

    public LuaValue arg(int i) {
        return i < 1?LuaValue.NIL:(i <= this.length?this.v[this.offset + i - 1]:this.more.arg(i - this.length));
    }

    public int narg() {
        return this.length + this.more.narg();
    }

    public LuaValue arg1() {
        return this.length > 0?this.v[this.offset]:this.more.arg1();
    }

    public Varargs subargs(int start) {
        if(start <= 0) {
            LuaValue.argerror(1, "start must be > 0");
        }

        return start == 1 ? this : (start > this.length ? this.more.subargs(start - this.length) : LuaValue.varargsOf(this.v, this.offset + start - 1, this.length - (start - 1), this.more));
    }
}
