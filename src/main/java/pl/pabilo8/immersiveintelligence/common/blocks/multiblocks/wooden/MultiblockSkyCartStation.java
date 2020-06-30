package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
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
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_ClothDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_WoodenMultiblock;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class MultiblockSkyCartStation implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{

			new IngredientStack(new ItemStack(IEContent.blockWoodenDecoration, 4, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockStoneDecorationSlabs, 2, BlockTypes_StoneDecoration.CONCRETE.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 8, IIBlockTypes_MetalDecoration.HEAVY_MECHANICAL_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 2, IIBlockTypes_MetalDecoration.MECHANICAL_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_cloth_decoration, 1, IIBlockTypes_ClothDecoration.COIL_ROPE.getMeta()))
	};
	public static MultiblockSkyCartStation instance = new MultiblockSkyCartStation();
	static ItemStack[][][] structure = new ItemStack[3][3][3];

	static
	{
		for(int h = 0; h < 3; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 3; w++)
				{

					if(h==0&&w==0)
					{
						if(l==1)
							structure[h][l][w] = new ItemStack(CommonProxy.block_cloth_decoration, 1, IIBlockTypes_ClothDecoration.COIL_ROPE.getMeta());
						else
							structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.MECHANICAL_ENGINEERING.getMeta());
					}
					else if(h==0&&l==0)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockStoneDecorationSlabs, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());
					}
					else if(w >= 1&&l >= 1)
					{
						if(h==1&&w==1&&l==1)
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
						else if(h==2&&l==2)
							continue;
						else if(h==2&&w==1&&l==1)
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
						else
							structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.HEAVY_MECHANICAL_ENGINEERING.getMeta());

					}
					else if(h==2&&l==0&&w!=0)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
					}

				}
			}
		}
	}

	TileEntitySkyCartStation te;

	@Override
	public String getUniqueName()
	{
		return "II:SkyCartStation";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockWoodenDecoration&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
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

		for(int h = -1; h < 2; h++)
			for(int l = -1; l < 2; l++)
				for(int w = -1; w < 2; w++)
				{

					if(h==0&&(w==-1||l==-1))
						continue;
					if(h==1&&(w==-1||l==1))
						continue;

					int ww = mirrored?-w: w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, CommonProxy.block_wooden_multiblock.getStateFromMeta(IIBlockTypes_WoodenMultiblock.SKYCART_STATION.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntitySkyCartStation)
					{
						TileEntitySkyCartStation tile = (TileEntitySkyCartStation)curr;
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;
						tile.pos = ((h+1)*9)+((l+1)*3)+(w+1);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_wooden_multiblock, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 2; h++)
			for(int l = -1; l < 2; l++)
				for(int w = -1; w < 2; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-1&&w==-1)
					{
						if(l==0)
						{
							if(!Utils.isBlockAt(world, pos, CommonProxy.block_cloth_decoration, IIBlockTypes_ClothDecoration.COIL_ROPE.getMeta()))
							{
								return false;
							}
						}
						else
						{
							if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.MECHANICAL_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
					}
					else if(h==-1&&l==-1)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecorationSlabs, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
						{
							return false;
						}
					}
					else if(w >= 0&&l >= 0)
					{
						if(h==0&&w==0&&l==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
							{
								return false;
							}
						}
						else if(h==1&&l==1)
						{
							if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
							{
								return false;
							}
						}
						else if(h==1&&w==0&&l==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
							{
								return false;
							}
						}
						else
						{
							if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.HEAVY_MECHANICAL_ENGINEERING.getMeta()))
							{
								return false;
							}
						}

					}
					else if(h==1&&l==-1&&w!=-1)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
						{
							return false;
						}
					}
					else if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
					{
						return false;
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
		return 13;
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
			te = new TileEntitySkyCartStation();
			te.facing = EnumFacing.EAST;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(1, 1, 1);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}