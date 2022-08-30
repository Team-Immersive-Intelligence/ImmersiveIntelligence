package pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
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
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_MetalMultiblock1;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockFuelStation implements IMultiblock
{
	public static MultiblockFuelStation instance = new MultiblockFuelStation();
	public static ItemStack[][][] structure = new ItemStack[3][3][2];
	static IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack("scaffoldingTreatedWood", 6),
			new IngredientStack("blockSheetmetalSteel", 8),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
	};

	static
	{
		for(int h = 0; h < 3; h++)
			for(int l = 0; l < 3; l++)
				for(int w = 0; w < 2; w++)
				{
					if(h==0)
						structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
					else if(l > 0)
						structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.STEEL.getMeta());
					else if(h==1)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
				}
	}

	TileEntityFuelStation te;

	@Override
	public String getUniqueName()
	{
		return "II:FuelStation";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockMetalDecoration0&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{
		//non-mirrorable
		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		boolean b = structureCheck(world, pos, side, false);
		if(!b)
			return false;

		ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER)?player.getHeldItemMainhand(): player.getHeldItemOffhand();
		if(MultiblockHandler.fireMultiblockFormationEventPost(player, this, pos, hammer).isCanceled())
			return false;

		world.setBlockState(pos.offset(side, 2), Blocks.AIR.getDefaultState());

		for(int h = -1; h < 2; h++)
			for(int l = 0; l < 3; l++)
				for(int w = 0; w < 2; w++)
				{
					if(h==1&&l==0)
						continue;

					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), w).add(0, h, 0);

					world.setBlockState(pos2, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.FUEL_STATION.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityFuelStation)
					{
						TileEntityFuelStation tile = (TileEntityFuelStation)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = (h+1)*6+(l)*2+w;
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?w: -w), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?w: -w)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.blockMetalMultiblock1, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 2; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 2; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-1)
					{
						if(!Utils.isOreBlockAt(world, pos, "scaffoldingTreatedWood"))
						{
							return false;
						}
					}
					else if(l > 0)
					{
						if(!Utils.isOreBlockAt(world, pos, "blockSheetmetalSteel"))
						{
							return false;
						}
					}
					else if(h==0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0,BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
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
			te = new TileEntityFuelStation();
			te.facing = EnumFacing.NORTH;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
