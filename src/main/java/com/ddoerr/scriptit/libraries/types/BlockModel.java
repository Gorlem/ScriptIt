package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.util.Map;
import java.util.stream.Collectors;

public class BlockModel extends AnnotationBasedModel {
    public static BlockModel From(BlockState blockState) {
        BlockModel blockModel = new BlockModel();
        blockModel.blockState = blockState;
        return blockModel;
    }

    private BlockState blockState;

    @Getter
    public ItemModel getItem() {
        return ItemModel.From(blockState.getBlock().asItem().getStackForRender());
    }

    @Getter
    public Map<String, String> getProperties() {
        return blockState.getProperties()
                .stream()
                .collect(Collectors.toMap(Property::getName, p -> blockState.get(p).toString()));
    }
}
