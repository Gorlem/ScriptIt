package com.ddoerr.scriptit.models;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class TargetModel extends AnnotationBasedModel {
    public static TargetModel From(HitResult hitResult) {
        TargetModel targetModel = new TargetModel();
        targetModel.hitResult = hitResult;
        return targetModel;
    }

    private HitResult hitResult;

    @Getter
    public HitResult.Type getType() {
        return hitResult.getType();
    }

    @Getter
    public PositionModel getPosition() {
        return PositionModel.From(hitResult.getPos());
    }

    @Getter
    public BlockModel getBlock() {
        if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockState = MinecraftClient.getInstance().world.getBlockState(blockPos);
            return BlockModel.From(blockState);
        }
        return null;
    }

    @Getter
    public EntityModel getEntity() {
        if (hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();
            return EntityModel.From(entity);
        }
        return null;
    }
}
