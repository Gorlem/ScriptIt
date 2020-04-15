package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.util.math.Vec3d;

public class PositionModel extends AnnotationBasedModel {
    public static PositionModel From(Vec3d vector) {
        PositionModel positionModel = new PositionModel();
        positionModel.vector = vector;
        return positionModel;
    }

    public static PositionModel From(double x, double y, double z) {
        return From(new Vec3d(x, y, z));
    }

    private Vec3d vector;

    @Getter
    public double getX() {
        return vector.x;
    }

    @Getter
    public double getY() {
        return vector.y;
    }

    @Getter
    public double getZ() {
        return vector.z;
    }
}
