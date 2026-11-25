package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.AbstractConnection;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;

import java.util.Map;

/**
 * Supplies energy stored in a capacitor backpack to an LV or MV wire network.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class TileEntityCapacitorBackpack extends TileEntityBackpack implements ITickable, IOBJModelCallback<IBlockState>
{
	public TileEntityCapacitorBackpack()
	{
		super(BackpackType.CAPACITOR);
	}

	@Override
	public boolean acceptsWireType(WireType wire)
	{
		String category = wire.getCategory();
		return wire.isEnergyWire()&&(WireType.LV_CATEGORY.equals(category)||WireType.MV_CATEGORY.equals(category));
	}

	@Override
	public void update()
	{
		if(world.isRemote||limitType==null)
			return;

		int rate = limitType.getTransferRate();
		int available = EnergyHelper.extractFlux(backpack, rate, true);
		if(available <= 0)
			return;

		ImmersiveNetHandler network = ImmersiveNetHandler.INSTANCE;
		Map<Connection, Integer> transferred = network.getTransferedRates(world.provider.getDimension());
		for(AbstractConnection path : network.getIndirectEnergyConnections(pos, world, true))
		{
			if(!world.isBlockLoaded(path.end)||path.subConnections.length==0)
				continue;
			IImmersiveConnectable receiver = ApiUtils.toIIC(path.end, world, false);
			if(receiver==null||!receiver.isEnergyOutput())
				continue;

			int budget = available;
			for(Connection wire : path.subConnections)
				budget = Math.min(budget, wire.cableType.getTransferRate()-transferred.getOrDefault(wire, 0));
			if(budget <= 0)
				continue;

			int offered = receiver.outputEnergy(budget, true, 0);
			int loss = (int)(offered*path.getPreciseLossRate(offered, rate));
			if(offered <= loss)
				continue;
			int afterLoss = offered-loss;
			int delivered = receiver.outputEnergy(afterLoss, false, 0);
			if(delivered <= 0)
				continue;

			int used = delivered+(int)((long)delivered*loss/afterLoss);
			EnergyHelper.extractFlux(backpack, used, false);
			available -= used;
			markDirty();
			for(Connection wire : path.subConnections)
			{
				transferred.merge(wire, used, Integer::sum);
				IImmersiveConnectable start = ApiUtils.toIIC(wire.start, world, false);
				if(start!=null)
					start.onEnergyPassthrough(used);
			}
			receiver.onEnergyPassthrough(delivered);
			if(available <= 0)
				break;
		}
	}

	@Override
	public String getCacheKey(IBlockState object)
	{
		return String.valueOf(IIContent.itemAdvancedPowerPack.getColor(this.backpack));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderColour(IBlockState object, String group)
	{
		if("capacitorpack_paint".equals(group))
			return 0xff000000+IIContent.itemAdvancedPowerPack.getColor(this.backpack);
		return super.getRenderColour(object, group);
	}
}
