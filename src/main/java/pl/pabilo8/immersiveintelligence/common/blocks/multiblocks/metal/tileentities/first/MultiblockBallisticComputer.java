package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
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
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class MultiblockBallisticComputer implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{

			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 3, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.COIL_DATA.getMeta())),
			new IngredientStack("blockSteel", 3)
	};
	public static MultiblockBallisticComputer instance = new MultiblockBallisticComputer();
	static ItemStack[][][] structure = new ItemStack[2][2][2];

	static
	{
		for(int h = 0; h < 2; h++)
		{
			for(int l = 0; l < 2; l++)
			{
				for(int w = 0; w < 2; w++)
				{

					if(h==0&&w==0&&l==0)
					{
						structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.COIL_DATA.getMeta());
					}
					else if(l==0)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
					}
					else if(w==0&&h==1)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta());
					}
					else
					{
						structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta());
					}

				}
			}
		}
	}

	TileEntityBallisticComputer te;

	@Override
	public String getUniqueName()
	{
		return "II:BallisticComputer";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==CommonProxy.block_metal_decoration&&
				(state.getBlock().getMetaFromState(state)==IIBlockTypes_MetalDecoration.COIL_DATA.getMeta());
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

		for(int h = 0; h < 2; h++)
			for(int l = 0; l < 2; l++)
				for(int w = 0; w < 2; w++)
				{

					int ww = mirrored?-w: w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, CommonProxy.block_metal_multiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.BALLISTIC_COMPUTER.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityBallisticComputer)
					{
						TileEntityBallisticComputer tile = (TileEntityBallisticComputer)curr;
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;
						tile.pos = (h*4)+(l*2)+w;
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_metal_multiblock0, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = 0; h < 2; h++)
			for(int l = 0; l < 2; l++)
				for(int w = 0; w < 2; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==0&&w==0&&l==0)
					{
						if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.COIL_DATA.getMeta()))
						{
							return false;
						}
					}
					else if(l==0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockStorage, BlockTypes_MetalsIE.STEEL.getMeta()))
						{
							return false;
						}
					}
					else if(w==0&&h==1)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.COIL_HV.getMeta()))
						{
							return false;
						}
					}
					else
					{
						if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta()))
						{
							return false;
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
			te = new TileEntityBallisticComputer();
			te.facing = EnumFacing.NORTH;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(2, 1, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
