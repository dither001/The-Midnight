package midnight.common.block.color;

import midnight.client.MidnightClient;
import midnight.common.world.biome.MnBiomeColors;
import midnight.core.util.ColorUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class NightGrassColor implements IColoredBlock {
    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(BlockState state, @Nullable ILightReader lworld, @Nullable BlockPos pos, int tintIndex) {
        if(pos == null || lworld == null) return 0x8C74A1;
        int color = MidnightClient.get().getNightGrassColorCache().getColor(pos, MnBiomeColors.NIGHT_GRASS);
        color = ColorUtil.darker(color, 0.3);
        return color;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack, int tintIndex) {
        return 0x8C74A1;
    }
}
