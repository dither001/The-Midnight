package com.mushroom.midnight.common.blocks;

import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.client.IModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBasic extends Block implements IModelProvider {
    public BlockBasic(Material material) {
        super(material);
        this.setCreativeTab(Midnight.MIDNIGHT_TAB);
    }
}
