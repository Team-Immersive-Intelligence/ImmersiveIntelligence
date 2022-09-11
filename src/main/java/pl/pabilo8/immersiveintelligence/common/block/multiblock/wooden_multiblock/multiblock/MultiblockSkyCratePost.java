package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
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
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.IIBlockTypes_WoodenMultiblock;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class MultiblockSkyCratePost implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{

			new IngredientStack(new ItemStack(IEContent.blockWoodenDecoration, 2, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.FENCE.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.MECHANICAL_ENGINEERING.getMeta()))
	};
	public static MultiblockSkyCratePost INSTANCE;
	static ItemStack[][][] structure = new ItemStack[3][2][1];

	static
	{
		for(int h = 0; h < 3; h++)
		{
			for(int l = 0; l < 2; l++)
			{
				for(int w = 0; w < 1; w++)
				{

					if(h==0&&w==0&&l==0)
					{
						structure[h][l][w] = new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.MECHANICAL_ENGINEERING.getMeta());
					}
					else if(h==1&&l==0)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.FENCE.getMeta());
					}
					else if(h==2)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
					}

				}
			}
		}
	}

	TileEntitySkyCratePost te;

	public MultiblockSkyCratePost()
	{
		INSTANCE = this;
	}

	@Override
	public String getUniqueName()
	{
		return "II:SkyCratePost";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockWoodenDecoration&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_WoodenDecoration.FENCE.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{

		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		boolean bool = this.structureCheck(world, pos, side, false);

		if(!bool)
		{
			return false;
		}

		for(int h = -1; h < 2; h++)
			for(int l = 0; l < 2; l++)
				for(int w = 0; w < 1; w++)
				{
					if(h!=1&&l==1)
						continue;

					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), w).add(0, h, 0);

					world.setBlockState(pos2, IIContent.blockWoodenMultiblock.getStateFromMeta(IIBlockTypes_WoodenMultiblock.SKYCRATE_POST.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntitySkyCratePost)
					{
						TileEntitySkyCratePost tile = (TileEntitySkyCratePost)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = ((h+1)*2)+(l)+w;
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?w: -w), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?w: -w)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.blockWoodenMultiblock, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 2; h++)
			for(int l = 0; l < 2; l++)
				for(int w = 0; w < 1; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-1&&l==0)
					{
						if(!Utils.isBlockAt(world, pos, IIContent.blockMetalDecoration, IIBlockTypes_MetalDecoration.MECHANICAL_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else if(h==0&&l==0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.FENCE.getMeta()))
						{
							return false;
						}
					}
					else if(h==1)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
						{
							return false;
						}
					}
					else if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						return false;


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
			te = new TileEntitySkyCratePost();

		}
		te.facing = EnumFacing.EAST;

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}