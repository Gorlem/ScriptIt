package com.ddoerr.scriptit.languages.lua;

import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.languages.LanguageInitializer;
import com.ddoerr.scriptit.api.languages.LanguageRegistry;
import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import java.util.Collection;
import java.util.Collections;

public class LuaLanguage implements LanguageInitializer, Language {
    private static final int MAX_INSTRUCTIONS_PER_TICK = 100;

    private Globals globals;
    private LuaValue luaSetHookFunction;
    private LuaValue luaYieldFunction;

    private LuaContainedResultFactory factory = new LuaContainedResultFactory();

    @Override
    public void onInitialize(LanguageRegistry registry) {
        registry.registerLanguage(getName(), this);

        globals = createGlobals();
        extractFunctions(globals);
        globals.finder = new ScriptResourceFinder();
    }

    @Override
    public String getName() {
        return "lua";
    }

    @Override
    public Collection<String> getExtensions() {
        return Collections.singletonList("lua");
    }

    private void extractFunctions(Globals globals) {
        globals.load(new DebugLib());
        luaSetHookFunction = globals.get("debug").get("sethook");
        globals.set("debug", LuaValue.NIL);

        luaYieldFunction = globals.get("coroutine").get("yield");
    }

    @Override
    public void loadLibrary(Library library) {
        globals.set(library.getName(), factory.fromModel(library.getModel()));
    }

    @Override
    public LuaContainedValue runScriptInstantly(Script script) {
        Collection<Library> libraries = script.getLibraries();

        for (Library library : libraries) {
            globals.set(library.getName(), factory.fromModel(library.getModel()));
        }

        LuaValue chunk = loadChunk(script);
        LuaValue result = chunk.call();

        for (Library library : libraries) {
            globals.set(library.getName(), LuaValue.NIL);
        }

        return new LuaContainedValue(result);
    }

    @Override
    public ScriptThread runScriptThreaded(Script script) {
        LuaValue chunk = loadChunk(script);
        LuaThread thread = new LuaThread(globals, chunk);

        luaSetHookFunction.invoke(LuaValue.varargsOf(new LuaValue[]{
                thread,
                luaYieldFunction,
                LuaValue.EMPTYSTRING,
                LuaValue.valueOf(MAX_INSTRUCTIONS_PER_TICK)
        }));

        return new LuaThreadAdapter(thread);
    }

    private LuaValue loadChunk(Script script) {
        if (script.hasStringSource())
            return globals.load(script.getStringSource(), script.getName());

        if (script.hasFileSource())
            return globals.loadfile(script.getFileSource());

        return LuaValue.NIL;
    }

    private Globals createGlobals() {
        Globals globals = createDefaultGlobals();

        LuaC.install(globals);
        LoadState.install(globals);

        return globals;
    }

    private Globals createDefaultGlobals() {
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new JseMathLib());
        globals.load(new CoroutineLib());
        return globals;
    }
}
