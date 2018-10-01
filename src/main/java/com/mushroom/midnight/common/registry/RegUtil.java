package com.mushroom.midnight.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;

public class RegUtil {
    public static <T extends Block> T withName(T block, String name) {
        ResourceLocation registryName = GameData.checkPrefix(name);
        block.setRegistryName(registryName);
        block.setTranslationKey(registryName.getNamespace() + "." + registryName.getPath());
        return block;
    }

    public static <T extends Item> T withName(T item, String name) {
        ResourceLocation registryName = GameData.checkPrefix(name);
        item.setRegistryName(registryName);
        item.setTranslationKey(registryName.getNamespace() + "." + registryName.getPath());
        return item;
    }
}