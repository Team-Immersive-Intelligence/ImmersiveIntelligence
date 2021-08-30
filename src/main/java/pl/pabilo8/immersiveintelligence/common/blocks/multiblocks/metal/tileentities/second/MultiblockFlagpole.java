package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration2;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
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
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;

public class MultiblockFlagpole implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IIContent.blockConcreteDecoration, 9, IIBlockTypes_ConcreteDecoration.CONCRETE_BRICKS.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 3, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockSandbags, 16+4, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockWoodenFortification, 1, IIBlockTypes_WoodenFortification.WOODEN_STEEL_CHAIN_FENCE.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration2, 3, BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta())),
			new IngredientStack("fenceTreatedWood", 4)
	};
	public static MultiblockFlagpole instance = new MultiblockFlagpole();
	static ItemStack[][][] structure = new ItemStack[8][3][3];

	static
	{
		for(int h = 0; h < 8; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 3; w++)
				{

					if(h==0)
					{
						structure[h][l][w] = new ItemStack(IIContent.blockConcreteDecoration, 1, IIBlockTypes_ConcreteDecoration.CONCRETE_BRICKS.getMeta());
					}
					else if(h < 3)
					{
						if(w==1&&l==1)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else
							structure[h][l][w] = new ItemStack(IIContent.blockSandbags, 1, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta());
					}
					else
					{
						if(w==1&&l==1)
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.FENCE.getMeta());
						else if(h==3)
						{
							if(w==0&&l==2)
								structure[h][l][w] = new ItemStack(IIContent.blockWoodenFortification, 1, IIBlockTypes_WoodenFortification.WOODEN_STEEL_CHAIN_FENCE.getMeta());
							else if(l==0)
								structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration2, 1, BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta());
							else
								structure[h][l][w] = new ItemStack(IIContent.blockSandbags, 1, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta());
						}
					}
				}
			}
		}
	}

	TileEntityFlagpole te;

	@Override
	public String getUniqueName()
	{
		return "II:Flagpole";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockMetalDecoration2&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta());
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

		for(int h = -3; h < 5; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -1; w < 2; w++)
				{
					if(h>0&&(w!=0||l!=1))
						continue;

					int ww = mirrored?-w: w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.FLAGPOLE.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityFlagpole)
					{
						TileEntityFlagpole tile = (TileEntityFlagpole)curr;
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;
						tile.pos = (h+3)*9+(l)*3+(w+1);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.blockMetalMultiblock1, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -3; h < 5; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -1; w < 2; w++)
				{
					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-3)
					{
						if(!Utils.isBlockAt(world, pos, IIContent.blockConcreteDecoration, IIBlockTypes_ConcreteDecoration.CONCRETE_BRICKS.getMeta()))
						{
							return false;
						}
					}
					else if(h < 0)
					{
						if(w==0&&l==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else
						{
							if(!Utils.isBlockAt(world, pos, IIContent.blockSandbags, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta()))
							{
								return false;
							}
						}
					}
					else
					{
						if(w==0&&l==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.FENCE.getMeta()))
							{
								return false;
							}
						}
						else if(h==0)
						{
							if(w==-1&&l==2)
							{
								if(!Utils.isBlockAt(world, pos, IIContent.blockWoodenFortification, IIBlockTypes_WoodenFortification.WOODEN_STEEL_CHAIN_FENCE.getMeta()))
								{
									return false;
								}
							}
							else if(l==0)
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration2, BlockTypes_MetalDecoration2.RAZOR_WIRE.getMeta()))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isBlockAt(world, pos, IIContent.blockSandbags, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta()))
								{
									return false;
								}
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
			te = new TileEntityFlagpole();
			te.facing = EnumFacing.NORTH;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
