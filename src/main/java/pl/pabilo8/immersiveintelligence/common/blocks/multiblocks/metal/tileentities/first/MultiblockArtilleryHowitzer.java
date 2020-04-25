package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
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
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

import java.util.ArrayList;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class MultiblockArtilleryHowitzer implements IMultiblock
{
	//9 36
	//36 + 24 + 4 + 12 + 1
	//4*9*4 - leaded concrete, 9 heavy eng, 9 radiator, 4*9+4*6+4 concrete, 3*4*3 heavy engineering, 4*3 advanced electronic, 4*3 concrete, 4*3 wooden scaffolding, 6 steel block, 1 redstone engineering, 1 hv wire coil, 1 electronic engineering, 1 concrete,
	//door 5 steel block, 16 steel sheetmetal
	//cannon 5 steel block, 6 steel sheetmetal
	//ammo door heavy engineering
	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IEContent.blockStoneDecoration, 144, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockStoneDecoration, 77, BlockTypes_StoneDecoration.CONCRETE.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 46, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 12, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 9, BlockTypes_MetalDecoration0.RADIATOR.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockWoodenDecoration, 12, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockWoodenDevice0, 12, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta())),
			new IngredientStack("blockSteel", 16),
			new IngredientStack("sheetmetalSteel", 22)

	};
	public static MultiblockArtilleryHowitzer instance = new MultiblockArtilleryHowitzer();
	static ItemStack[][][] structure = new ItemStack[7][9][9];

	static
	{
		for(int h = 0; h < 7; h++)
		{
			for(int l = 0; l < 9; l++)
			{
				for(int w = 0; w < 9; w++)
				{
					if(h==0)
					{


						if((l==2&&w==2)||(l==2&&w==6)||(l==6&&w==2)||(l==6&&w==6))
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());
							//Schweres Ingueniuring
						else if(w >= 3&&w <= 5&&l >= 3&&l <= 5)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
							//Radiators
						else if(w >= 2&&w <= 6&&l==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RADIATOR.getMeta());
						else if(w >= 2&&w <= 6&&l==6)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RADIATOR.getMeta());
						else if(w==2&&l >= 2&&l <= 6)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RADIATOR.getMeta());
						else if(w==6&&l >= 2&&l <= 6)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RADIATOR.getMeta());
							//Concrete
						else
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());

					}
					else if(((w >= 0&&w <= 2&&l >= 0&&l <= 2)||(w >= 0&&w <= 2&&l >= 6&&l <= 8)||(w >= 6&&w <= 8&&l >= 0&&l <= 2)||(w >= 6&&w <= 8&&l >= 6&&l <= 8))&&h < 5)
					{
						structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta());
					}
					else if(w < 2&&l <= 5&&l >= 3&&h < 5)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
					else if(w >= 7&&l <= 8&&l >= 3&&h < 5)
						structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta());
					else if(w >= 3&&w <= 5&&l <= 1&&h < 5)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
					else if(w >= 3&&w <= 5&&l >= 7&&l <= 8&&h < 5)
						structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());


					else if(l==4&&w==4&&h >= 1&&h <= 5)
						structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						//Cannon
					else if(h==3&&w >= 3&&w <= 5&&l >= 3&&l <= 5&&w!=4)
						structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.STEEL.getMeta());

					else if(h==5)
					{
						if(w >= 3&&w <= 5&&(l==1||l==7))
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());
						else if(l >= 3&&l <= 5&&(w==1||w==7))
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());

							//Scafoldings
						else if(w >= 1&&w <= 2&&l >= 1&&l <= 2&&!(w==1&&l==1))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
						else if(w >= 6&&w <= 7&&l >= 1&&l <= 2&&!(w==7&&l==1))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
						else if(w >= 1&&w <= 2&&l >= 6&&l <= 7&&!(w==1&&l==7))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());
						else if(w >= 6&&w <= 7&&l >= 6&&l <= 7&&!(w==7&&l==7))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDecoration, 1, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta());


						else if(w >= 3&&w <= 5&&l==8)
						{
							if(w!=4)
								structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
							else
								structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());

						}
						else if(l >= 3&&l <= 5&&(w==0||w==8))
						{
							if(l==4&&w==0)
								structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta());
							else if(l==4&&w==8)
								structure[h][l][w] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta());
							else
								structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						}
						else if(w==4&&l==0)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE.getMeta());

					}
					else if(h==6)
					{

						if(w >= 1&&w <= 2&&l >= 1&&l <= 2&&!(w==1&&l==1))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta());
						else if(w >= 6&&w <= 7&&l >= 1&&l <= 2&&!(w==7&&l==1))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta());
						else if(w >= 1&&w <= 2&&l >= 6&&l <= 7&&!(w==1&&l==7))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta());
						else if(w >= 6&&w <= 7&&l >= 6&&l <= 7&&!(w==7&&l==7))
							structure[h][l][w] = new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta());

						else if(w >= 2&&w <= 6&&l >= 2&&l <= 6&&!((w==2&&l==2)||(w==6&&l==2)||(w==2&&l==6)||(w==6&&l==6)))
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.STEEL.getMeta());
					}
				}
			}
		}
	}

	TileEntityArtilleryHowitzer te;

	@Override
	public String getUniqueName()
	{
		return "II:ArtilleryHowitzer";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockStoneDecoration&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_StoneDecoration.CONCRETE.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{

		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
		{
			side = EnumFacing.fromAngle(player.rotationYaw);
		}

		ArrayList<BlockPos> checklist = new ArrayList<BlockPos>();

		boolean bool = this.structureCheck(world, pos, side, false, checklist);

		if(!bool)
		{
			return false;
		}

		for(int h = -5; h < 2; h++)
			for(int l = 0; l < 9; l++)
				for(int w = -4; w < 5; w++)
				{
					boolean valid = true;

					int ww = w;
					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					//I DON"T LIKE MANUAL POSITION INPUTTING! Once again, Blu please change the system to something easier (nbt structures maybe?)!
					for(BlockPos posv : checklist)
					{
						if(posv.getX()==pos2.getX()&&posv.getY()==pos2.getY()&&posv.getZ()==pos2.getZ())
						{
							valid = false;
							break;
						}
					}
					if(!valid)
					{
						continue;
					}

					world.setBlockState(pos2, CommonProxy.block_metal_multiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.ARTILLERY_HOWITZER.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityArtilleryHowitzer)
					{
						TileEntityArtilleryHowitzer tile = (TileEntityArtilleryHowitzer)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = ((h+5)*81)+(l*9)+(w+4);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww: -ww)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_metal_multiblock0, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror, ArrayList<BlockPos> list)
	{
		for(int h = -5; h < 2; h++)
			for(int l = 0; l < 9; l++)
				for(int w = -4; w < 5; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);


					if(h==-5)
					{
						if((l==2&&w==-2)||(l==2&&w==2)||(l==6&&w==-2)||(l==6&&w==2))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
							{

								return false;

							}
						}
						//Schweres Ingueniuring
						else if(w >= -1&&w <= 1&&l >= 3&&l <= 5)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
							{
								return false;
							}
						}
						//Radiators
						else if(w >= -2&&w <= 2&&l==2)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RADIATOR.getMeta()))
							{
								return false;
							}

						}
						else if(w >= -2&&w <= 2&&l==6)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RADIATOR.getMeta()))
							{
								return false;
							}
						}
						else if(w==-2&&l >= 2&&l <= 6)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RADIATOR.getMeta()))
							{
								return false;
							}
						}
						else if(w==2&&l >= 2&&l <= 6)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RADIATOR.getMeta()))
							{
								return false;
							}
						}
						//Concrete
						else
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
							{
								return false;
							}
						}

					}
					else if(((w >= -4&&w <= -2&&l >= 0&&l <= 2)||(w >= -4&&w <= -2&&l >= 6&&l <= 8)||(w >= 2&&w <= 4&&l >= 0&&l <= 2)||(w >= 2&&w <= 4&&l >= 6&&l <= 8))&&h < 0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta()))
						{
							return false;
						}
					}
					else if(w < -2&&l <= 5&&l >= 3&&h < 0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else if(w >= 3&&l <= 8&&l >= 3&&h < 0)
					{
						if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else if(w >= -1&&w <= 1&&l <= 1&&h < 0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else if(w >= -1&&w <= 1&&l >= 7&&l <= 8&&h < 0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
						{
							return false;
						}
					}


					else if(l==4&&w==0&&h >= -4&&h <= 0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockStorage, BlockTypes_MetalsIE.STEEL.getMeta()))
						{
							return false;
						}
					}
					//Cannon
					else if(h==-2&&w >= -1&&w <= 1&&l >= 3&&l <= 5&&w!=0)
					{
						if(!Utils.isBlockAt(world, pos, IEContent.blockSheetmetal, BlockTypes_MetalsAll.STEEL.getMeta()))
						{
							return false;
						}
					}

					else if(h==0)
					{
						if(w >= -1&&w <= 1&&(l==1||l==7))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
							{
								return false;
							}
						}
						else if(l >= 3&&l <= 5&&(w==-3||w==3))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
							{
								return false;
							}
						}

						//Scafoldings
						else if(w >= -3&&w <= -2&&l >= 1&&l <= 2&&!(w==-3&&l==1))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
							{
								return false;
							}
						}
						else if(w >= 2&&w <= 3&&l >= 1&&l <= 2&&!(w==3&&l==1))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
							{
								return false;
							}
						}
						else if(w >= -3&&w <= -2&&l >= 6&&l <= 7&&!(w==-3&&l==7))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
							{
								return false;
							}
						}
						else if(w >= 2&&w <= 3&&l >= 6&&l <= 7&&!(w==3&&l==7))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDecoration, BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()))
							{
								return false;
							}
						}


						else if(w >= -1&&w <= 1&&l==8)
						{
							if(w!=0)
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockStorage, BlockTypes_MetalsIE.STEEL.getMeta()))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()))
								{
									return false;
								}
							}

						}
						else if(l >= 3&&l <= 5&&(w==-4||w==4))
						{
							if(l==4&&w==-4)
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.COIL_HV.getMeta()))
								{
									return false;
								}
							}
							else if(l==4&&w==4)
							{
								if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ELECTRONIC_ENGINEERING.getMeta()))
								{
									return false;
								}
							}
							else
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockStorage, BlockTypes_MetalsIE.STEEL.getMeta()))
								{
									return false;
								}
							}
						}
						else if(w==0&&l==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE.getMeta()))
							{
								return false;
							}
						}
						else
							list.add(pos);

					}
					else if(h==1)
					{

						if(w >= -3&&w <= -2&&l >= 1&&l <= 2&&!(w==-3&&l==1))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDevice0, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
							{
								return false;
							}
						}
						else if(w >= 2&&w <= 3&&l >= 1&&l <= 2&&!(w==3&&l==1))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDevice0, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
							{
								return false;
							}
						}
						else if(w >= -3&&w <= -2&&l >= 6&&l <= 7&&!(w==-3&&l==7))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDevice0, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
							{
								return false;
							}
						}
						else if(w >= 2&&w <= 3&&l >= 6&&l <= 7&&!(w==3&&l==7))
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockWoodenDevice0, BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()))
							{
								return false;
							}
						}

						else if(w >= -2&&w <= 2&&l >= 2&&l <= 6)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockSheetmetal, BlockTypes_MetalsAll.STEEL.getMeta()))
							{
								return false;
							}
						}

						else
							list.add(pos);
					}
					else
					{
						if(!Utils.isBlockAt(world, pos, Blocks.AIR, 0))
						{
							return false;
						}
						else
							list.add(pos);
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
		return 8;
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
			te = new TileEntityArtilleryHowitzer();
			te.facing = EnumFacing.NORTH;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 6, 9);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
