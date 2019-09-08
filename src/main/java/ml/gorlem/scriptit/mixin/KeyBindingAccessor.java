package ml.gorlem.scriptit.mixin;

import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

    @Accessor
    int getTimesPressed();
    @Accessor
    void setTimesPressed(int timesPressed);

    @Accessor
    void setPressed(boolean pressed);

    @Invoker
    void invokeReset();

    @Accessor
    static Map<String, KeyBinding> getKeysById() {
        return null;
    };
}
