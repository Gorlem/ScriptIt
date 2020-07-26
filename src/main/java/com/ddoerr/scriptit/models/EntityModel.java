package com.ddoerr.scriptit.models;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.entity.Entity;

public class EntityModel extends AnnotationBasedModel {
    public static EntityModel From(Entity entity) {
        EntityModel entityModel = new EntityModel();
        entityModel.entity = entity;
        return entityModel;
    }

    private Entity entity;

    @Getter
    public String getName() {
        return entity.getName().asFormattedString();
    }

    @Getter
    public String getUuid() {
        return entity.getUuidAsString();
    }
}
