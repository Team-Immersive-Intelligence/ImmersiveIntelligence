package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityCO2Filter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityCO2Filter.CO2Handler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 2019-05-24
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".CO2Input")
@ZenRegister
public class CO2InputTweaker
{
	@ZenMethod
	public static void addMultiblock(String classPath, int time, int amount, int[] pos)
	{
		try
		{
			Class c = Class.forName(classPath);
			if(c.isAssignableFrom(TileEntityMultiblockMetal.class))
			{
				TileEntityCO2Filter.handlerMap.put(c, new MultiblockCO2Handler(c, time, amount, pos));
				CraftTweakerAPI.getLogger().logInfo("CO2 Collector will now recognise "+classPath+" as a CO2 source");
			}
			else
				CraftTweakerAPI.getLogger().logError("Couldn't add "+classPath+" as a CO2 source, class is not a multiblock");
		} catch(ClassNotFoundException e)
		{
			CraftTweakerAPI.getLogger().logError("Couldn't add "+classPath+" as a CO2 source, class doesn't exist");
		}
	}

	@ZenMethod
	public static void addTile(String classPath, int time, int amount)
	{
		try
		{
			Class c = Class.forName(classPath);
			if(c.isAssignableFrom(TileEntity.class))
			{
				TileEntityCO2Filter.handlerMap.put(c, new TileEntityCO2Handler(c, time, amount));
				CraftTweakerAPI.getLogger().logInfo("CO2 Collector will now recognise "+classPath+" as a CO2 source");
			}
		} catch(ClassNotFoundException e)
		{
			CraftTweakerAPI.getLogger().logError("Couldn't add "+classPath+" as a CO2 source, class doesn't exist");
		}
	}

	private static class MultiblockCO2Handler extends TileEntityCO2Handler
	{
		private final Class<TileEntityMultiblockMetal> c;
		private final List<Integer> pos;


		public MultiblockCO2Handler(Class<TileEntityMultiblockMetal> c, int time, int amount, int[] pos)
		{
			super(((Class)c), time, amount);
			this.c = c;
			this.pos = Arrays.stream(pos).boxed().collect(Collectors.toList());
		}

		@Override
		public int getOutput(TileEntity tile)
		{
			if(!c.isInstance(tile))
				return 0;
			if(pos.contains(c.cast(tile).pos))
				return 0;

			TileEntityMultiblockMetal<?,?> machine = c.cast(c.cast(tile).master());
			if(machine==null)
				return 0;
			int i = 0;
			for(MultiblockProcess<?> p : machine.processQueue)
				if(p.canProcess(machine)&&p.processTick%time==0)
					i += amount;
			return i;
		}
	}

	private static class TileEntityCO2Handler extends CO2Handler
	{
		private final Class<TileEntity> c;
		protected final int time;
		protected final int amount;

		public TileEntityCO2Handler(Class<TileEntity> c, int time, int amount)
		{
			this.c = c;
			this.time = time;
			this.amount = amount;
		}

		@Override
		public int getOutput(TileEntity tile)
		{
			if(!c.isInstance(tile))
				return 0;

			if(tile.getWorld().getTotalWorldTime()%time==0)
				return amount;
			return 0;
		}
	}

}
