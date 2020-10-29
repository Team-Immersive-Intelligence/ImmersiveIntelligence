package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Conveyor;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
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
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockConveyorScanner implements IMultiblock
{
	static IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta())),
			new IngredientStack(Utils.copyStackWithAmount(ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID+":covered"), 1)),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
	};
	public static MultiblockConveyorScanner instance = new MultiblockConveyorScanner();
	public static ItemStack[][][] structure = new ItemStack[3][1][1];
	public static ItemStack conveyorStack = ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID+":covered");

	static
	{
		structure[0][0][0] = new ItemStack(CommonProxy.block_metal_decoration, 1, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta());
		structure[1][0][0] = conveyorStack;
		structure[2][0][0] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
	}

	TileEntityConveyorScanner te;

	@Override
	public String getUniqueName()
	{
		return "II:ConveyorScanner";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockConveyor&&
				(state.getBlock().getMetaFromState(state)==BlockTypes_Conveyor.CONVEYOR.getMeta());
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
			for(int l = 0; l < 1; l++)
				for(int w = 0; w < 1; w++)
				{

					BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), w).add(0, h, 0);

					world.setBlockState(pos2, CommonProxy.block_metal_multiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.CONVEYOR_SCANNER.getMeta()));
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityConveyorScanner)
					{
						TileEntityConveyorScanner tile = (TileEntityConveyorScanner)curr;
						tile.facing = side;
						tile.mirrored = false;
						tile.formed = true;
						tile.pos = h+1;
						tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?w: -w), h, (side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?w: -w)};
						tile.markDirty();
						world.addBlockEvent(pos2, CommonProxy.block_metal_multiblock0, 255, 0);
					}
				}
		return true;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h < 2; h++)
		{
			for(int l = 0; l < 1; l++)
			{
				for(int w = 0; w < 1; w++)
				{

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-1)
					{
						if(!Utils.isBlockAt(world, pos, CommonProxy.block_metal_decoration, IIBlockTypes_MetalDecoration.ADVANCED_ELECTRONIC_ENGINEERING.getMeta()))
						{
							return false;
						}
					}
					else if(h==0)
					{
						if(!ConveyorHandler.isConveyor(world, pos, ImmersiveEngineering.MODID+":covered", dir))
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
		}
		return true;
	}

	@Override
	public ItemStack[][][] getStructureManual()
	{
		//h w l
		structure[1][0][0]=ConveyorHandler.getConveyorStack(ImmersiveEngineering.MODID+":covered");
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
		if(iterator==1)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5,0.5,0.5);
			GlStateManager.rotate(90,0,1,0);
			ClientUtils.mc().getRenderItem().renderItem(conveyorStack, TransformType.NONE);
			GlStateManager.popMatrix();
			return true;

		}
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
			te = new TileEntityConveyorScanner();
			te.facing = EnumFacing.SOUTH;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2, -1);
		ImmersiveIntelligence.proxy.renderTile(te);
		GlStateManager.popMatrix();
	}
}
