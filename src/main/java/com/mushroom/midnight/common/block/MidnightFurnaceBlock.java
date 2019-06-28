package com.mushroom.midnight.common.block;

import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.client.particle.MidnightParticles;
import com.mushroom.midnight.common.network.GuiHandler;
import com.mushroom.midnight.common.registry.MidnightBlocks;
import com.mushroom.midnight.common.registry.MidnightItemGroups;
import com.mushroom.midnight.common.tile.base.TileEntityMidnightFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;

import javax.annotation.Nullable;
import java.util.Random;

public class MidnightFurnaceBlock extends ContainerBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private final boolean isBurning;
    private static boolean keepInventory;

    public MidnightFurnaceBlock(boolean isBurning) {
        super(Material.ROCK);
        this.isBurning = isBurning;

        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.NORTH));
        this.setCreativeTab(MidnightItemGroups.DECORATION);

        if (isBurning) {
            this.setLightLevel(0.4F);
        }
    }

    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(MidnightBlocks.MIDNIGHT_FURNACE);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.isRemote) {
            BlockState BlockState = worldIn.getBlockState(pos.north());
            BlockState BlockState1 = worldIn.getBlockState(pos.south());
            BlockState BlockState2 = worldIn.getBlockState(pos.west());
            BlockState BlockState3 = worldIn.getBlockState(pos.east());
            Direction Direction = state.get(FACING);

            if (Direction == Direction.NORTH && BlockState.isFullBlock() && !BlockState1.isFullBlock()) {
                Direction = Direction.SOUTH;
            } else if (Direction == Direction.SOUTH && BlockState1.isFullBlock() && !BlockState.isFullBlock()) {
                Direction = Direction.NORTH;
            } else if (Direction == Direction.WEST && BlockState2.isFullBlock() && !BlockState3.isFullBlock()) {
                Direction = Direction.EAST;
            } else if (Direction == Direction.EAST && BlockState3.isFullBlock() && !BlockState2.isFullBlock()) {
                Direction = Direction.WEST;
            }

            worldIn.setBlockState(pos, state.with(FACING, Direction), 2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("incomplete-switch")
    @Override
    public void randomTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (this.isBurning) {
            Direction facing = state.get(FACING);
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double z = pos.getZ() + 0.5D;
            double outwardOffset = 0.52D;
            double sidewardOffset = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (facing) {
                case WEST:
                    worldIn.addParticle(ParticleTypes.SMOKE, x - outwardOffset, y, z + sidewardOffset, 0.0D, 0.0D, 0.0D);
                    MidnightParticles.FURNACE_FLAME.spawn(worldIn, x - outwardOffset, y, z + sidewardOffset, 0d, 0d, 0d);
                    break;
                case EAST:
                    worldIn.addParticle(ParticleTypes.SMOKE, x + outwardOffset, y, z + sidewardOffset, 0.0D, 0.0D, 0.0D);
                    MidnightParticles.FURNACE_FLAME.spawn(worldIn, x + outwardOffset, y, z + sidewardOffset, 0d, 0d, 0d);
                    break;
                case NORTH:
                    worldIn.addParticle(ParticleTypes.SMOKE, x + sidewardOffset, y, z - outwardOffset, 0.0D, 0.0D, 0.0D);
                    MidnightParticles.FURNACE_FLAME.spawn(worldIn, x + sidewardOffset, y, z - outwardOffset, 0d, 0d, 0d);
                    break;
                case SOUTH:
                    worldIn.addParticle(ParticleTypes.SMOKE, x + sidewardOffset, y, z + outwardOffset, 0.0D, 0.0D, 0.0D);
                    MidnightParticles.FURNACE_FLAME.spawn(worldIn, x + sidewardOffset, y, z + outwardOffset, 0d, 0d, 0d);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity playerIn, EnumHand hand, Direction facing, float hitX, float hitY, float hitZ) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof TileEntityMidnightFurnace) {
            playerIn.openGui(Midnight.instance, GuiHandler.MIDNIGHT_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
            playerIn.addStat(Stats.FURNACE_INTERACTION);
        }
        return true;
    }

    public static void setState(boolean active, World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active) {
            worldIn.setBlockState(pos, MidnightBlocks.MIDNIGHT_FURNACE_LIT.getDefaultState().with(FACING, state.get(FACING)), 3);
            worldIn.setBlockState(pos, MidnightBlocks.MIDNIGHT_FURNACE_LIT.getDefaultState().with(FACING, state.get(FACING)), 3);
        } else {
            worldIn.setBlockState(pos, MidnightBlocks.MIDNIGHT_FURNACE.getDefaultState().with(FACING, state.get(FACING)), 3);
            worldIn.setBlockState(pos, MidnightBlocks.MIDNIGHT_FURNACE.getDefaultState().with(FACING, state.get(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMidnightFurnace();
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityMidnightFurnace) {
                ((TileEntityMidnightFurnace) tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityMidnightFurnace) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityMidnightFurnace) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(MidnightBlocks.MIDNIGHT_FURNACE);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        Direction facing = Direction.byIndex(meta);
        if (facing.getAxis() == Direction.Axis.Y) {
            facing = Direction.NORTH;
        }

        return this.getDefaultState().with(FACING, facing);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return (state.get(FACING)).getIndex();
    }

    @Override
    public BlockState withRotation(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState withMirror(BlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || (layer == BlockRenderLayer.CUTOUT && this.isBurning);
    }

    @Override
    public int getPackedLightmapCoords(BlockState state, IEnviromentBlockReader worldIn, BlockPos pos) {
        if (this.isBurning && MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT) {
            return source.getCombinedLight(pos, 15);
        }
        return source.getCombinedLight(pos, 0);
    }
}
