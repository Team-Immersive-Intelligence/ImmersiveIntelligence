package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVehicleWorkshop;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;

public class MultiblockVehicleWorkshop implements IMultiblock
{
	// TODO: 20.09.2022 fully replace
	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 11, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 10, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockMetalDecoration, 2, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 2, BlockTypes_MetalDevice0.BARREL.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockStoneDecorationSlabs, 8, BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta())),
			new IngredientStack("scaffoldingTreatedWood", 2),
			new IngredientStack("blockSteel", 6),
			new IngredientStack("scaffoldingAluminum", 2)
	};
	public static MultiblockVehicleWorkshop INSTANCE;
	static ItemStack[][][] structure = new ItemStack[4][4][5];

	static
	{
		for(int h = 0; h < 4; h++)
		{
			for(int l = 0; l < 4; l++)
			{
				for(int w = 0; w < 5; w++)
				{
					structure[h][l][w] = ItemStack.EMPTY;
					if(h==0)
					{
						if(w < 2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else if(w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==3)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
					}
					else if(w==3&&l < 2)
						structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
					else if(h==1)
					{
						if(w < 2)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecorationSlabs, 1, BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta());
						else if(w==2)
							structure[h][l][w] = l < 2?
									new ItemStack(IEContent.blockWoodenDecoration, 2, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()):
									new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_0.getMeta());
						else if(w==3)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else
							structure[h][l][w] = l < 2?
									new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.BARREL.getMeta()):
									new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
					}
					else if(h==2)
					{
						if(w==3)
							structure[h][l][w] = new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta());
						else if(w==4&&l==3)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta());
					}

				}
			}
		}
	}

	TileEntityVehicleWorkshop te;

	public MultiblockVehicleWorkshop()
	{
		INSTANCE = this;
	}

	@Override
	public String getUniqueName()
	{
		return "II:VehicleWorkshop";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		return Utils.compareToOreName(stack, "scaffoldingAluminum");
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{

		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		boolean mirrored = false;
		boolean b = structureCheck(world, pos, side, false);
		if(!b)
		{
			mirrored = true;
			b = structureCheck(world, pos, side, true);
		}
		if(!b)
			return false;

		ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER)?player.getHeldItemMainhand(): player.getHeldItemOffhand();
		if(MultiblockHandler.fireMultiblockFormationEventPost(player, this, pos, hammer).isCanceled())
			return false;

		//(h+hoff)*(lsize*wsize)+(l+loff)*wsize+(w+woff)

		for(int h = -1; h < 3; h++)
			for(int l = -2; l < 2; l++)
				for(int w = -2; w < 3; w++)
				{
					if(h==-1&&w==2&&l!=1)
						continue;
					if(h > 0&&w==-2)
						continue;
					else if(h > 1&&l > -1&&w==1)
						continue;
					else if(h > 0&&w<1)
						continue;
					else if(h > 0&&w==2&&l!=1)
						continue;
					else if(h==2&&w==2)
						continue;

					int ww = mirrored?-l: l;
					BlockPos pos2 = pos.offset(side, w).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.VEHICLE_WORKSHOP.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityVehicleWorkshop)
					{
						TileEntityVehicleWorkshop tile = (TileEntityVehicleWorkshop)curr;
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;
						tile.pos = (h+1)*20+(l+2)*5+(w+2);
						tile.offset = new int[]{(side==EnumFacing.WEST?-w: side==EnumFacing.EAST?w: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-w: side==EnumFacing.SOUTH?w: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.blockMetalMultiblock1, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 3; h++)
			for(int l = -2; l < 2; l++)
				for(int w = -2; w < 3; w++)
				{
					int ww = mirror?-l: l;
					BlockPos pos = startPos.offset(dir, w).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-1&&w==2&&l!=1)
						continue;

					if(h > 0&&w==-2)
					{
						if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						{
							return false;
						}
					}
					else if(h > 1&&l > -1&&w==1)
					{
						if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						{
							return false;
						}
					}
					else if(h==-1)
					{
						if(w < 0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else if(w < 2)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else if(l==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
					}
					else if(w==1&&l < 0)
					{
						if(!Utils.isOreBlockAt(world, pos, "blockSteel"))
						{
							return false;
						}
					}
					else if(h==0)
					{
						if(w < 0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecorationSlabs, BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta()))
							{
								return false;
							}
						}
						else if(w==0)
						{
							if(l < 0)
							{
								if(!Utils.isOreBlockAt(world, pos, "scaffoldingTreatedWood"))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isOreBlockAt(world, pos, "scaffoldingAluminum"))
								{
									return false;
								}
							}
						}
						else if(w==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else
						{
							if(l < 0)
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDevice0, BlockTypes_MetalDevice0.BARREL.getMeta()))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
								{
									return false;
								}
							}
						}
					}
					else if(h==1)
					{
						if(w==1)
						{
							if(!Utils.isBlockAt(world, pos, IIContent.blockMetalDecoration, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else if(w==2&&l==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.COIL_HV.getMeta()))
							{
								return false;
							}
						}
					}
				}
		return true;
	}

	@Override
	public ItemStack[][][] getStructureManual()
	{
		return structure;
	}

	@Override
	public IngredientStack[] getTotalMaterials()
	{
		return materials;
	}

	@Override
	public boolean overwriteBlockRender(ItemStack stack, int iterator)
	{
		return false;
	}

	@Override
	public float getManualScale()
	{
		return 12;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderFormedStructure()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderFormedStructure()
	{
		if(te==null)
		{
			te = new TileEntityVehicleWorkshop();
			te.facing = EnumFacing.NORTH;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2, 0);
		//ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
