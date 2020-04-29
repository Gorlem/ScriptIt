package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.events.AbstractEvent;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.libraries.types.PositionModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.ListenerSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;

public class SoundEvent extends AbstractEvent implements ListenerSoundInstance {
    public SoundEvent() {
        MinecraftClient.getInstance().getSoundManager().registerListener(this);
    }

    @Override
    public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet) {
        SoundModel soundModel = new SoundModel(sound);
        dispatch(soundModel);
    }

    public static class SoundModel extends AnnotationBasedModel {
        private SoundInstance soundInstance;

        public SoundModel(SoundInstance soundInstance) {
            this.soundInstance = soundInstance;
        }

        @Getter
        public String getIdentifier() {
            return soundInstance.getId().toString();
        }

        @Getter
        public PositionModel getPosition() {
            return PositionModel.From(soundInstance.getX(), soundInstance.getY(), soundInstance.getZ());
        }

        @Getter
        public SoundCategory getCategory() {
            return soundInstance.getCategory();
        }
    }
}
