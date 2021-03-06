package midnight.data.tags;

import midnight.common.block.MnBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;

public class MnBlockTagsProvider extends TagsProvider<Block> {
    @SuppressWarnings("deprecation") // We need Registry.BLOCK. Sorry Forge...
    public MnBlockTagsProvider(DataGenerator gen) {
        super(gen, Registry.BLOCK);
    }

    @Override
    protected void registerTags() {
        getBuilder(BlockTags.ENDERMAN_HOLDABLE).replace(false).add(
            MnBlocks.NIGHT_DIRT,
            MnBlocks.NIGHT_GRASS_BLOCK,
            MnBlocks.NIGHT_STONE,
            MnBlocks.DECEITFUL_PEAT,
            MnBlocks.DECEITFUL_MUD,
            MnBlocks.TRENCHSTONE,
            MnBlocks.STRANGE_SAND,
            MnBlocks.COARSE_NIGHT_DIRT,
            MnBlocks.GHOST_PLANT
        );
    }

    @Override
    protected void setCollection(TagCollection<Block> collection) {
        BlockTags.setCollection(collection);
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Midnight - Block tags";
    }
}
