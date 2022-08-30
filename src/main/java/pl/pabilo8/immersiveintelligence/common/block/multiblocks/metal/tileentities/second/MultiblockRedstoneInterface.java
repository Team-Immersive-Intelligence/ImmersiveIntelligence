package pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Connector;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
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
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_MetalMultiblock1;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockRedstoneInterface implements IMultiblock
{
	public static MultiblockRedstoneInterface instance = new MultiblockRedstoneInterface();
	public static ItemStack[][][] structure = new ItemStack[1][3][2];
	static IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IIContent.blockMetalDecoration, 2, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.COIL_DATA.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_REDSTONE.getMeta()))
	};

	static
	{
		structure[0][0][0] = new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.COIL_DATA.getMeta());
		structure[0][0][1] = new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta());
		structure[0][1][0] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
		structure[0][1][1] = new ItemStack(IIContent.blockMetalDecoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta());
		structure[0][2][0] = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_REDSTONE.getMeta());
	}

	TileEntityRedstoneInterface te;

	@Override
	public String getUniqueName()
	{
		return "II:RedstoneDataInterface";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IIContent.blockMetalDecoration&&
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

		world.setBlockState(pos.offset(side, 2), Blocks.AIR.getDefaultState());

		for(int h = 0; h < 1; h++)
			for(int l = 0; l < 3; l++)
				for(int w = 0; w < 2; w++)
				{
					if(l==2&&w==1)
						continue;


					int ww = mirrored?-w: w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.REDSTONE_DATA_INTERFACE.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityRedstoneInterface)
					{
						TileEntityRedstoneInterface tile = (TileEntityRedstoneInterface)curr;
						tile.facing = side;
						tile.mirrored = mirrored;
						tile.formed = true;
						tile.pos = w+(l*2);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, IIContent.blockMetalMultiblock1, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = 0; h < 1; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 2; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(w==0)
					{
						if(l==0)
						{
							if(!Utils.isBlockAt(world, pos, IIContent.blockMetalDecoration, IIBlockTypes_MetalDecoration.COIL_DATA.getMeta()))
							{
								return false;
							}
						}
						else if(l==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						else if(l==2)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockConnectors, BlockTypes_Connector.CONNECTOR_REDSTONE.getMeta()))
							{
								return false;
							}
						}

					}
					else
					{
						if(l!=2)
						{
							if(!Utils.isBlockAt(world, pos, IIContent.blockMetalDecoration, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta()))
							{
								return false;
							}
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
			te = new TileEntityRedstoneInterface();
			te.facing = EnumFacing.NORTH;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
