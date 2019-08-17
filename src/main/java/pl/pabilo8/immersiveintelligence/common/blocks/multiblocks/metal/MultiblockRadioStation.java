package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
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
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class MultiblockRadioStation implements IMultiblock
{

	public static MultiblockRadioStation instance = new MultiblockRadioStation();

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
						if(l==0&&w==0)
						{
							structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta());
						}
						else if((l==2&&w==0)||(l==0&&w==2)||(l==2&&w==2))
						{
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta());
						}
						else if(l==1&&w < 2)
						{
							structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta());
						}
						else if(l==2&&w==1)
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_LV.getMeta());
						}
						else
						{
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());
						}
					}
					else if(h==1)
					{
						if(l==0&&w==0)
						{
							structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta());
						}
						else if((l==2&&w==0)||(l==0&&w==2)||(l==2&&w==2))
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						}
					}
					else if(h==2||h==4||h==5)
					{
						if((l==0&&w==0)||(l==2&&w==0)||(l==0&&w==2)||(l==2&&w==2))
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						}
					}
					else if(h==3)
					{
						if(!(l==1&&w==1))
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						}
					}
					else if(h==6)
					{
						if(l==1&&w==1)
						{
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						}
					}
					else if(h==7)
					{
						if(l==1&&w==1)
						{
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						}
					}
				}
			}
		}
	}

	TileEntityRadioStation te;

	@Override
	public String getUniqueName()
	{
		return "II:RadioStation";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==CommonProxy.block_metal_decoration&&
				(state.getBlock().getMetaFromState(state)==IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta());
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

		for(int h = -1; h < 7; h++)
			for(int l = 0; l < 3; l++)
				for(int w = 0; w < 3; w++)
				{
					if(h > -1&&h < 5&&l==1&&w==1)
					{
						continue;
					}

					if(h > -1&&h < 2&&l==0&&w==1)
					{
						continue;
					}
					if(h > -1&&h < 2&&l==1&&w==0)
					{
						continue;
					}
					if(h > -1&&h < 2&&l==2&&w==1)
					{
						continue;
					}
					if(h > -1&&h < 2&&l==1&&w==2)
					{
						continue;
					}

					if(h > 2&&h < 7&&l==0&&w==1)
					{
						continue;
					}
					if(h > 2&&h < 7&&l==1&&w==0)
					{
						continue;
					}
					if(h > 2&&h < 7&&l==2&&w==1)
					{
						continue;
					}
					if(h > 2&&h < 7&&l==1&&w==2)
					{
						continue;
					}

					if(h > 4&&h < 7&&l==0&&w==0)
					{
						continue;
					}
					if(h > 4&&h < 7&&l==0&&w==2)
					{
						continue;
					}
					if(h > 4&&h < 7&&l==2&&w==0)
					{
						continue;
					}
					if(h > 4&&h < 7&&l==2&&w==2)
					{
						continue;
					}

					int ww = w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, CommonProxy.block_metal_multiblock.getStateFromMeta(IIBlockTypes_MetalMultiblock.RADIO_STATION.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityRadioStation)
					{
						TileEntityRadioStation tile = (TileEntityRadioStation)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = (h+1)*9+(l)*3+(w);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_metal_multiblock, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 7; h++)
		{
			for(int l = 0; l < 3; l++)
			{
				for(int w = 0; w < 3; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					//I would really like to see an if-shortening function in Intellij.

					if((h > 2&&h < 7&&l==1&&w==0))
					{
						if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						{

							return false;
						}
					}
					else if((h > -1&&h < 5&&l==1&&w==1)||(h > -1&&h < 2&&l==0&&w==1)||(h > -1&&h < 2&&l==1&&w==0)||(h > -1&&h < 2&&l==2&&w==1)||(h > -1&&h < 2&&l==1&&w==2)||(h > 2&&h < 7&&l==0&&w==1)||(h > 2&&h < 7&&l==2&&w==1)||(h > 2&&h < 7&&l==1&&w==2)||(h > 4&&h < 7&&l==0&&w==0)||(h > 4&&h < 7&&l==0&&w==2)||(h > 4&&h < 7&&l==2&&w==0)||(h > 4&&h < 7&&l==2&&w==2))
					{
						if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						{

							return false;
						}
					}

					if(h==-1)
					{
						if(l==0&&w==0)
						{
							if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta()))
							{

								return false;
							}
						}
						else if((l==2&&w==0)||(l==0&&w==2)||(l==2&&w==2))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta()))
							{

								return false;
							}
						}
						else if(l==1&&w < 2)
						{
							if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta()))
							{

								return false;
							}
						}
						else if(l==2&&w==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.COIL_LV.getMeta()))
							{

								return false;
							}
						}
						else
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
							{

								return false;
							}
						}
					}
					else if(h==0)
					{
						if(l==0&&w==0)
						{
							if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta()))
							{

								return false;
							}
						}
						else if((l==2&&w==0)||(l==0&&w==2)||(l==2&&w==2))
						{
							if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel"))
							{

								return false;
							}
						}
					}
					else if(h==1||h==3||h==4)
					{
						if((l==0&&w==0)||(l==2&&w==0)||(l==0&&w==2)||(l==2&&w==2))
						{
							if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel"))
							{

								return false;
							}
						}
					}
					else if(h==2)
					{
						if(!(l==1&&w==1))
						{
							if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel"))
							{

								return false;
							}
						}
					}
					else if(h==5)
					{
						if(l==1&&w==1)
						{
							if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel"))
							{

								return false;
							}
						}
					}
					else if(h==6)
					{
						if(l==1&&w==1)
						{
							if(!Utils.isOreBlockAt(world, pos, "blockSteel"))
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

	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack("scaffoldingSteel", 24),
			new IngredientStack("blockSteel", 1),
			new IngredientStack(new ItemStack(IEContent.blockStoneDecoration, 3, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockStoneDecoration, 2, BlockTypes_StoneDecoration.CONCRETE.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_LV.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 2, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 2, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta()))
	};

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
		return 10;
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
			te = new TileEntityRadioStation();
			te.facing = EnumFacing.SOUTH;
		}

		ImmersiveIntelligence.proxy.renderTile(te);
	}

}
