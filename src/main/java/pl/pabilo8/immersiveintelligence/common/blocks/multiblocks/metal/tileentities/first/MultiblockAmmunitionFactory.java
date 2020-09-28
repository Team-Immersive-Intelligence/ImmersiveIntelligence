package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */

public class MultiblockAmmunitionFactory implements IMultiblock {
    static final IngredientStack[] materials = new IngredientStack[]{
            new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 25, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
            new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 12, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
            new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.BARREL.getMeta())),
            new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta())),
            new IngredientStack(Utils.copyStackWithAmount(ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID + ":conveyor"), 10))
    };
    public static MultiblockAmmunitionFactory instance = new MultiblockAmmunitionFactory();
    static ItemStack[][][] structure = new ItemStack[3][5][5];

    static {
        for (int h = 0; h < 3; h++) {
            for (int l = 0; l < 5; l++) {
                for (int w = 0; w < 5; w++) {

                    if (h == 1 && w == 4 && l == 2) {
                        continue;
                    }

                    if (h == 2 && (w != 4 || l != 4)) {
                        continue;
                    }

                    if (h == 0) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                    } else if (h == 1) {
                        if (w == 2 && l < 2) {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.BARREL.getMeta());
                        } else if (w == 0 && l < 3) {
                            structure[h][l][w] = ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID + ":conveyor");
                        } else if (l == 2 && w < 4) {
                            structure[h][l][w] = ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID + ":conveyor");
                        } else if (w == 3 && l < 4) {
                            structure[h][l][w] = ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID + ":conveyor");
                        } else if (w == 2 && l == 3) {
                            structure[h][l][w] = ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID + ":conveyor");
                        } else {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
                        }
                    } else {
                        if (h == 2) {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta());
                        }
                    }

                }
            }
        }
    }

    TileEntityAmmunitionFactory te;

    @Override
    public String getUniqueName() {
        return "II:AmmunitionFactory";
    }

    @Override
    public boolean isBlockTrigger(IBlockState state) {
        return state.getBlock() == IEContent.blockMetalDevice0 &&
                (state.getBlock().getMetaFromState(state) == BlockTypes_MetalDevice0.BARREL.getMeta());
    }

    @Override
    public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {

        side = side.getOpposite();
        if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
            side = EnumFacing.fromAngle(player.rotationYaw);
        }

        boolean bool = this.structureCheck(world, pos, side, false);

        if (!bool) {
            return false;
        }

        for (int h = -1; h < 2; h++)
            for (int l = 0; l < 5; l++)
                for (int w = -2; w < 3; w++) {

                    if (h == 0 && w == 2 && l == 2) {
                        continue;
                    }

                    if (h == 1 && (w != 2 || l != 4)) {
                        continue;
                    }

                    int ww = w;
                    BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

                    world.setBlockState(pos2, CommonProxy.block_metal_multiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY.getMeta()));
                    TileEntity curr = world.getTileEntity(pos2);
                    if (curr instanceof TileEntityAmmunitionFactory) {
                        TileEntityAmmunitionFactory tile = (TileEntityAmmunitionFactory) curr;
                        tile.facing = side;
                        tile.mirrored = false;
                        tile.formed = true;
                        tile.pos = (h + 1) * 25 + (l) * 5 + (w + 2);
                        tile.offset = new int[]{(side == EnumFacing.WEST ? -l : side == EnumFacing.EAST ? l : side == EnumFacing.NORTH ? ww : -ww), h, (side == EnumFacing.NORTH ? -l : side == EnumFacing.SOUTH ? l : side == EnumFacing.EAST ? ww : -ww)};
                        tile.markDirty();
                        world.addBlockEvent(pos2, CommonProxy.block_metal_multiblock0, 255, 0);
                    }
                }
        return true;
    }

    boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror) {
        for (int h = -1; h < 2; h++)
            for (int l = 0; l < 5; l++)
                for (int w = -2; w < 3; w++) {

                    int ww = mirror ? -w : w;
                    BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

                    if (h == 0 && w == 2 && l == 2) {
                        continue;
                    }

                    if (h == 1 && (w != 2 || l != 4)) {
                        continue;
                    }

                    if (h == -1) {
                        if (!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
                            return false;
                    } else if (h == 0) {
                        if (w == 0 && l < 2) {
                            if (!Utils.isBlockAt(world, pos, IEContent.blockMetalDevice0, BlockTypes_MetalDevice0.BARREL.getMeta()))
                                return false;
                        } else if (w == -2 && l < 3) {
                            if (!ConveyorHandler.isConveyor(world, pos, ImmersiveEngineering.MODID + ":conveyor", null))
                                return false;
                        } else if (l == 2 && w < 2) {
                            if (!ConveyorHandler.isConveyor(world, pos, ImmersiveEngineering.MODID + ":conveyor", null))
                                return false;
                        } else if (w == 1 && l < 4) {
                            if (!ConveyorHandler.isConveyor(world, pos, ImmersiveEngineering.MODID + ":conveyor", null))
                                return false;
                        } else if (w == 0 && l == 3) {
                            if (!ConveyorHandler.isConveyor(world, pos, ImmersiveEngineering.MODID + ":conveyor", null))
                                return false;
                        } else {
                            if (!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
                                return false;
                        }
                    } else {
                        if (h == 1) {
                            if (!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.COIL_HV.getMeta()))
                                return false;
                        }
                    }

                }
        return true;
    }

    @Override
    public ItemStack[][][] getStructureManual() {
        return structure;
    }

    @Override
    public IngredientStack[] getTotalMaterials() {
        return materials;
    }

    @Override
    public boolean overwriteBlockRender(ItemStack stack, int iterator) {
        if (iterator == 28 || iterator == 33) {
            return ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.EAST);
        }
        if (iterator == 25 || iterator == 30 || iterator == 35) {
            return ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.WEST);
        }
        if (iterator == 36 || iterator == 37 || iterator == 38 || iterator == 41 || iterator == 42) {
            return ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
        }
        return false;
    }

    @Override
    public float getManualScale() {
        return 12;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderFormedStructure() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderFormedStructure() {
        if (te == null) {
            te = new TileEntityAmmunitionFactory();
            te.facing = EnumFacing.NORTH;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-1, 2, 1);
        ImmersiveIntelligence.proxy.renderTile(te);
        GlStateManager.popMatrix();
    }
}
