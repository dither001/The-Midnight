package com.mushroom.midnight.common.biome.surface;

import com.mushroom.midnight.common.biome.MidnightBiomeConfigurator;
import com.mushroom.midnight.common.biome.MidnightSurfaceBuilders;

public class FungiForestBiome extends SurfaceBiome {
    public FungiForestBiome() {
        super(new Properties()
                .surfaceBuilder(MidnightSurfaceBuilders.SURFACE, MidnightSurfaceBuilders.GRASS_DIRT_MUD_CONFIG)
                .category(Category.FOREST)
                .grassColor(0x8489B5)
                .skyColor(0x15292D)
                .depth(0.155F)
                .scale(0.1F)
        );

        MidnightBiomeConfigurator.addStructureFeatures(this);
        MidnightBiomeConfigurator.addMoltenCrater(this);

        MidnightBiomeConfigurator.addGlobalOres(this);

        MidnightBiomeConfigurator.addLargeFungis(this);
        MidnightBiomeConfigurator.addLumen(this);

        MidnightBiomeConfigurator.addNightshroomFlowers(this);
        MidnightBiomeConfigurator.addDewshroomFlowers(this);
        MidnightBiomeConfigurator.addViridshroomFlowers(this);

        MidnightBiomeConfigurator.addBladeshrooms(this);
        MidnightBiomeConfigurator.addDeadViridShroomAndCache(this);
        MidnightBiomeConfigurator.addGrasses(this);

        MidnightBiomeConfigurator.addGlobalFeatures(this);

        MidnightBiomeConfigurator.addStandardCreatureSpawns(this);
        MidnightBiomeConfigurator.addStandardMonsterSpawns(this);
        MidnightBiomeConfigurator.addForestSpawns(this);
    }
}
