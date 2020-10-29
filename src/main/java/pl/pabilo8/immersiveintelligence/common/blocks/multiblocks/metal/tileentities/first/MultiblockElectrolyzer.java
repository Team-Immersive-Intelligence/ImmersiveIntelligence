package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
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
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockElectrolyzer implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{

			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 4, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 6, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 4, BlockTypes_MetalDevice0.BARREL.getMeta())),
			new IngredientStack("blockSteel", 2)
	};
	public static MultiblockElectrolyzer instance = new MultiblockElectrolyzer();
	static ItemStack[][][] structure = new ItemStack[2][3][3];

	static
	{
		for(int h = 0; h < 2; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 3; w++)
				{

					if(w==1)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
					}
					else
					{
						if(h==0)
						{
							if(l < 2)
							{
								structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
							}
							else
							{
								structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
							}
						}
						else if(l < 2)
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.BARREL.getMeta());
						}
					}
				}
			}
		}
	}

	TileEntityElectrolyzer te;

	@Override
	public String getUniqueName()
	{
		return "II:Electrolyzer";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockMetalDecoration0&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
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

		for(int h = -1; h < 1; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -1; w < 2; w++)
				{

					if(h==0&&w!=0&&l==2)
					{
						continue;
					}

					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), w).add(0, h, 0);

					world.setBlockState(pos2, CommonProxy.block_metal_multiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityElectrolyzer)
					{
						TileEntityElectrolyzer tile = (TileEntityElectrolyzer)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = (h+1)*9+(l)*3+(w+1);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?w: -w), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?w: -w)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_metal_multiblock0, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 1; h++)
			for(int l = 0; l < 3; l++)
				for(int w = -1; w < 2; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==0&&w!=0&&l==2)
					{
						continue;
					}


					if(w==0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else
					{
						if(h==-1)
						{
							if(l < 2)
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isOreBlockAt(world, pos, "blockSteel"))
								{
									return false;
								}
							}
						}
						else if(l < 2)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDevice0, BlockTypes_MetalDevice0.BARREL.getMeta()))
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
			te = new TileEntityElectrolyzer();
			te.facing = EnumFacing.NORTH;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2, 0);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
