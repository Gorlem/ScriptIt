package com.ddoerr.scriptit.languages.lua;

import com.ddoerr.scriptit.api.languages.LanguageRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import com.ddoerr.scriptit.api.languages.LanguageImplementation;
import com.ddoerr.scriptit.api.languages.LanguageInitializer;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LuaLanguage implements LanguageInitializer, LanguageImplementation {
    private static final int MAX_INSTRUCTIONS_PER_TICK = 100;

    private Globals globals;
    private LuaValue luaSetHookFunction;
    private LuaValue luaYieldFunction;

    private List<LibraryAdapter> libraries = new ArrayList<>();

    @Override
    public void onInitialize(LanguageRegistry registry) {
        registry.registerLanguage("lua", this);

        globals = createGlobals();
        extractFunctions(globals);
        globals.finder = new ScriptResourceFinder();
    }

    @Override
    public Collection<String> getExtensions() {
        return Arrays.asList(".lua");
    }

    private void extractFunctions(Globals globals) {
        globals.load(new DebugLib());
        luaSetHookFunction = globals.get("debug").get("sethook");
        globals.set("debug", LuaValue.NIL);

        luaYieldFunction = globals.get("coroutine").get("yield");
    }

    @Override
    public void loadRegistry(NamespaceRegistry registry) {
        LibraryAdapter library = new LibraryAdapter(registry);
        globals.load(library);
        libraries.add(library);
    }

    @Override
    public Object runScriptInstantly(Script script) {
        Collection<NamespaceRegistry> additionalRegistries = script.getAdditionalRegistries();

        for (NamespaceRegistry additionalRegistry : additionalRegistries) {
            LibraryAdapter library = new LibraryAdapter(additionalRegistry);
            globals.load(library);
            library.tick();
        }

        LuaValue chunk = loadChunk(script);
        LuaValue result = chunk.call();

        for (NamespaceRegistry additionalRegistry : additionalRegistries) {
            globals.set(additionalRegistry.getName(), LuaValue.NIL);
        }

        return ValueConverter.toObject(result);
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

    @Override
    public void tick() {
        for (LibraryAdapter adapter : libraries) {
            adapter.tick();
        }
    }
}
