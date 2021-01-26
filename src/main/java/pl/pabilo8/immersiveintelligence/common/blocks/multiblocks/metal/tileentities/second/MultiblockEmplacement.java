package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
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
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_ConcreteDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock1;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_StoneDecoration;

public class MultiblockEmplacement implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 12, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockConcreteDecoration, 8, IIBlockTypes_ConcreteDecoration.STURDY_CONCRETE_BRICKS.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockStoneDecoration, 16, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta())),
			new IngredientStack("slabSheetmetalIron", 9)
	};
	public static MultiblockEmplacement instance = new MultiblockEmplacement();
	static ItemStack[][][] structure = new ItemStack[6][3][3];

	static
	{
		for(int h = 0; h < 6; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 3; w++)
				{

					if(h==0)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
					}
					else if(h < 3)
					{
						if(w==1&&l==1)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else
							structure[h][l][w] = new ItemStack(IIContent.blockConcreteDecoration, 1, IIBlockTypes_ConcreteDecoration.STURDY_CONCRETE_BRICKS.getMeta());
					}
					else if(h < 5)
					{
						if(w==1&&l==1)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else
							structure[h][l][w] = new ItemStack(IIContent.blockStoneDecoration, 16, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta());
					}
					else
						structure[h][l][w] = new ItemStack(IEContent.blockSheetmetalSlabs, 1, BlockTypes_MetalsAll.IRON.getMeta());
				}
			}
		}
	}

	TileEntityEmplacement te;

	@Override
	public String getUniqueName()
	{
		return "II:Emplacement";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IIContent.blockStoneDecoration&&
				(state.getBlock().getMetaFromState(state)==IIBlockTypes_StoneDecoration.SANDBAGS.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{

		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		//unmirrorable (symmetrical)
		boolean bool = this.structureCheck(world, pos, side, false);

		if(!bool)
		{
			return false;
		}

		for(int h = -4; h < 2; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -1; w < 2; w++)
				{

					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), w).add(0, h, 0);

					world.setBlockState(pos2, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.EMPLACEMENT.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityEmplacement)
					{
						TileEntityEmplacement tile = (TileEntityEmplacement)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = (h+4)*9+(l)*3+(w+1);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?w: -w), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?w: -w)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.blockMetalMultiblock1, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -4; h < 2; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -1; w < 2; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-4)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else if(h!=1)
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
							if(h > -2)
							{
								if(!Utils.isBlockAt(world, pos, IIContent.blockStoneDecoration, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta()))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isBlockAt(world, pos, IIContent.blockConcreteDecoration, IIBlockTypes_ConcreteDecoration.STURDY_CONCRETE_BRICKS.getMeta()))
								{
									return false;
								}
							}

						}
					}
					else
					{
						if(!Utils.isOreBlockAt(world, pos, "slabSheetmetalIron"))
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
			te = new TileEntityEmplacement();
			te.facing = EnumFacing.NORTH;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
