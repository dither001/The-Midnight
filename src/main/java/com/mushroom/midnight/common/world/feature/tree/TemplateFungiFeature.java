package com.mushroom.midnight.common.world.feature.tree;

import com.mojang.datafixers.Dynamic;
import com.mushroom.midnight.common.registry.MidnightBlocks;
import com.mushroom.midnight.common.world.template.ShelfAttachProcessor;
import com.mushroom.midnight.common.world.template.TemplateCompiler;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

import java.util.function.Function;

public abstract class TemplateFungiFeature extends TemplateTreeFeature {
    private static final int MAX_DEPTH = 4;

    public TemplateFungiFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> deserialize, ResourceLocation[] templates, BlockState stem, BlockState hat) {
        super(deserialize, templates, stem, hat);

        this.setSapling((IPlantable) MidnightBlocks.NIGHTSHROOM);
    }

    @Override
    protected TemplateCompiler buildCompiler() {
        return super.buildCompiler()
                .withPostProcessor(new ShelfAttachProcessor(this::canPlaceShelf, ShelfAttachProcessor.FOREST_SHELF_BLOCKS));
    }

    @Override
    protected boolean canGrow(IWorld world, BlockPos minCorner, BlockPos maxCorner) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (BlockPos pos : BlockPos.getAllInBoxMutable(minCorner, maxCorner)) {
            mutablePos.setPos(pos);
            mutablePos.move(Direction.DOWN);

            int depth = 0;
            while (isAirOrLeaves(world, mutablePos)) {
                mutablePos.move(Direction.DOWN);
                if (depth++ >= MAX_DEPTH) {
                    return false;
                }
            }

            if (!isSoil(world, mutablePos, this.getSapling())) {
                return false;
            }
        }

        return true;
    }

    private boolean canPlaceShelf(IWorld world, BlockPos pos) {
        if (World.isOutsideBuildHeight(pos)) {
            return false;
        }

        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == MidnightBlocks.FUNGI_INSIDE) {
            return false;
        }

        return state.getBlock().isAir(state, world, pos) || state.isIn(BlockTags.LEAVES) || state.getMaterial() == Material.PLANTS;
    }
}
