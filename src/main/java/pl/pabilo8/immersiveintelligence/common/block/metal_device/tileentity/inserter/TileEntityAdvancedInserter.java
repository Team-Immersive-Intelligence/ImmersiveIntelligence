package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter;

import blusunrize.immersiveengineering.api.energy.wires.WireType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.AdvancedInserter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 15-07-2019
 */
public class TileEntityAdvancedInserter extends TileEntityInserter
{
	public static final HashMap<String, Function<NBTTagCompound, InserterTask>> TASKS = new HashMap<>();
	private static final Set<String> WIRES = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);

	static
	{
		TASKS.put("item", InserterTaskItem::new);
		TASKS.put("place_block", InserterTaskPlaceBlock::new);
		TASKS.put("from_minecart", InserterTaskFromMinecart::new);
		TASKS.put("into_minecart", InserterTaskIntoMinecart::new);
	}

	@Nonnull
	@Override
	protected Set<String> getAcceptedPowerWires()
	{
		return WIRES;
	}

	@Override
	public int getPickupSpeed()
	{
		return AdvancedInserter.taskTime;
	}

	@Override
	public int getEnergyUsage()
	{
		return AdvancedInserter.energyUsage;
	}

	@Override
	public int getEnergyCapacity()
	{
		return AdvancedInserter.energyCapacity;
	}

	@Override
	public int getMaxTakeAmount()
	{
		return AdvancedInserter.maxTake;
	}

	@Nonnull
	@Override
	protected HashMap<String, Function<NBTTagCompound, InserterTask>> getAvailableTasks()
	{
		return TASKS;
	}

}
