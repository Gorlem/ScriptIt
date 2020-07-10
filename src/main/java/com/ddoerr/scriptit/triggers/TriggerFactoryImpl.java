package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerFactory;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

public class TriggerFactoryImpl implements TriggerFactory {
    private ScriptItRegistry registry;

    public TriggerFactoryImpl(ScriptItRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Trigger createTrigger(Identifier identifier, Map<String, String> data) {
        Supplier<Trigger> triggerSupplier = registry.triggers.get(identifier);

        if (triggerSupplier == null) {
            return null;
        }

        Trigger trigger = triggerSupplier.get();

        if (data != null) {
            trigger.setData(data);
        }

        return trigger;
    }
}
