package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
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
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockPrecissionAssembler implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack("scaffoldingTreatedWood", 1),
			new IngredientStack(new ItemStack(Blocks.GLASS, 12, 0)),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 7, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 14, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockWoodenDevice0, 4, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
	};
	public static MultiblockPrecissionAssembler instance = new MultiblockPrecissionAssembler();
	static ItemStack[][][] structure = new ItemStack[3][3][5];

	static
	{
		for(int h = 0; h < 3; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 5; w++)
				{

					if((h==2&&l==0)||(h==1&&w==2&&l==0))
					{
						continue;
					}

					if(h==0)
					{

						if(w==2&&l==0)
						{
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
						}
						else
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						}
					}
					else if(h==1&&l==0&&w!=2)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta());
					}
					else
					{
						if(w!=0&&w!=4)
							structure[h][l][w] = new ItemStack(Blocks.GLASS, 1, 0);
						else if(h==1&l==2&&w==0)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
						else
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
					}

				}
			}
		}
	}

	TileEntityPrecissionAssembler te;

	@Override
	public String getUniqueName()
	{
		return "II:PrecissionAssembler";
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

		for(int h = 0; h < 3; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -2; w < 3; w++)
				{

					if((h==2&&l==0)||(h==1&&w==0&&l==0))
					{
						continue;
					}

					int ww = mirrored?-w: w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, IIContent.block_metal_multiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityPrecissionAssembler)
					{
						TileEntityPrecissionAssembler tile = (TileEntityPrecissionAssembler)curr;
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;
						tile.pos = (h*15)+(l*5)+(w+2);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.block_metal_multiblock0, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = 0; h < 3; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -2; w < 3; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if((h==2&&l==0)||(h==1&&w==0&&l==0))
					{
						if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						{
							return false;
						}
						continue;
					}


					if(h==0)
					{

						if(w==0&&l==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
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
					else if(h==1&&l==0&&w!=0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDevice0, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
						{
							return false;
						}
					}
					else
					{
						if(w!=-2&&w!=2)
						{
							if(!Utils.isBlockAt(world, pos, Blocks.GLASS, 0))
							{
								return false;
							}
						}
						else if(h==1&l==2&&w==-2)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
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
			te = new TileEntityPrecissionAssembler();
			te.facing = EnumFacing.NORTH;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(1, 1, 5);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
