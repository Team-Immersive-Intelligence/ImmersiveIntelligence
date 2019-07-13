package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
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
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation.TileEntitySkyCrateStationParent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_WoodenMultiblock;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class MultiblockSkyCrateStation implements IMultiblock
{
	public static MultiblockSkyCrateStation instance = new MultiblockSkyCrateStation();

	static ItemStack[][][] structure = new ItemStack[3][1][2];

	static
	{
		for(int h = 0; h < 3; h++)
			for(int l = 0; l < 2; l++)
				if(h==0&&l==0)
				{
					structure[h][0][l] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
				}
				else if(h==1&&l==0)
				{
					structure[h][0][l] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.FENCE.getMeta());
				}
				else if(h==2&&(l==0||l==1))
				{
					structure[h][0][l] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
				}
	}

	@Override
	public ItemStack[][][] getStructureManual()
	{
		return structure;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean overwriteBlockRender(ItemStack stack, int iterator)
	{
		return false;
	}

	@Override
	public float getManualScale()
	{
		return 16;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderFormedStructure()
	{
		return true;
	}

	TileEntitySkyCrateStation te;

	@Override
	@SideOnly(Side.CLIENT)
	public void renderFormedStructure()
	{
		if(te==null)
		{
			te = new TileEntitySkyCrateStationParent();
			te.facing = EnumFacing.SOUTH;
		}

		ImmersiveIntelligence.proxy.renderTile(te);
	}

	@Override
	public String getUniqueName()
	{
		return "II:SkyCrateStation";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockWoodenDecoration&&(state.getBlock().getMetaFromState(state)==BlockTypes_WoodenDecoration.FENCE.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{
		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		boolean bool = this.structureCheck(world, pos, side);
		if(!bool)
		{
			return false;
		}

		for(int h = -1; h <= 1; h++)
			for(int l = 0; l <= 1; l++)
				for(int w = 0; w <= 0; w++)
				{

					if((h==0||h==-1)&&l==1)
					{
						continue;
					}

					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), w).add(0, h, 0);

					if(l==0&&h==-1)
					{
						world.setBlockState(pos2, CommonProxy.block_wooden_multiblock.getStateFromMeta(IIBlockTypes_WoodenMultiblock.SKYCRATE_STATION_PARENT.getMeta()));
					}
					else
					{
						world.setBlockState(pos2, CommonProxy.block_wooden_multiblock.getStateFromMeta(IIBlockTypes_WoodenMultiblock.SKYCRATE_STATION.getMeta()));
					}

					TileEntity curr = world.getTileEntity(pos2);

					if(curr instanceof TileEntitySkyCrateStation)
					{
						TileEntitySkyCrateStation tile = (TileEntitySkyCrateStation)curr;
						tile.facing = side;
						tile.formed = true;
						//02.06 Trying to figure that out... really Blu, give us the Addon makers some documentation on things pls!
						//05.06 Haha, it was tricky but I think it's the structure's w and l
						tile.pos = (h+1)*2+(l)*3+(w);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?w: -w), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?w: -w)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_wooden_multiblock, 255, 0);
					}

				}
		return true;

	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir)
	{
		//Calculate - begins from the main block
		for(int h = -1; h <= 1; h++)
			for(int l = 0; l <= 1; l++)
			{
				if((h==0||h==-1)&&l==1)
				{
					continue;
				}

				BlockPos pos = startPos.offset(dir, l).add(0, h, 0);

				if(h==-1&&l==0)
				{
					if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
						return false;
				}
				else if(h==0&&l==0)
				{
					if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.FENCE.getMeta()))
						return false;
				}
				else if(h==1&&l <= 1)
				{
					if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
						return false;
				}
			}
		return true;
	}

	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.FENCE.getMeta()))
	};

	@Override
	public IngredientStack[] getTotalMaterials()
	{
		return materials;
	}
}