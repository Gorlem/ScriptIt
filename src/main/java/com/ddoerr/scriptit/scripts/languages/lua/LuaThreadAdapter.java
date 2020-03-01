package com.ddoerr.scriptit.scripts.languages.lua;

import com.ddoerr.scriptit.api.scripts.ScriptThread;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaThreadAdapter implements ScriptThread {
    private LuaThread thread;

    public LuaThreadAdapter(LuaThread thread) {
        this.thread = thread;
    }

    @Override
    public boolean resume() {
        if (thread == null)
            return false;

        if (thread.state.status > 1)
            return false;

        Varargs result = null;
        try {
            result = thread.resume(LuaValue.NIL);
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }

        if (thread.state.status > 1 && result.isstring(2)) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(result.tojstring(2)));
        }

        return result.checkboolean(1);
    }
}
