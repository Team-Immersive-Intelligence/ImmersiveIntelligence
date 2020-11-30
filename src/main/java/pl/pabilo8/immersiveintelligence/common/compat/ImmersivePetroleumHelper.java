package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import flaxbeard.immersivepetroleum.api.event.SchematicPlaceBlockPostEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ImmersivePetroleumHelper extends IICompatModule
{
	@Override
	public void preInit()
	{

	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		//for compat that requires events
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void postInit()
	{

	}

	@SubscribeEvent
	public void handleConveyorPlace(SchematicPlaceBlockPostEvent event)
	{
		IMultiblock mb = event.getMultiblock();
		if(mb.getUniqueName().startsWith("II:"))
		{
			IBlockState state = event.getBlockState();
			TileEntity te = event.getWorld().getTileEntity(event.getPos());

			if(te instanceof TileEntityConveyorBelt)
			{
				TileEntityConveyorBelt conveyor = (TileEntityConveyorBelt)te;
				EnumFacing facing = null;
				ResourceLocation rl = null;

				switch(mb.getUniqueName())
				{
					case "II:ConveyorScanner":
						rl = new ResourceLocation(ImmersiveEngineering.MODID+":covered");
						break;
					case "II:Packer":
						if(event.getH()==0)
							rl = new ResourceLocation(ImmersiveEngineering.MODID+":covered");
						else
						{
							rl = new ResourceLocation(ImmersiveEngineering.MODID+":conveyor");
							facing = event.getRotate().rotateYCCW();
						}
						break;
					default:

						break;
				}

				if(rl==null)
					rl = new ResourceLocation("immersiveengineering", "conveyor");
				if(facing==null)
					facing = event.getRotate();

				IConveyorBelt subType = ConveyorHandler.getConveyor(rl, conveyor);
				conveyor.setConveyorSubtype(subType);
				conveyor.setFacing(facing);

			}
		}

	}
}