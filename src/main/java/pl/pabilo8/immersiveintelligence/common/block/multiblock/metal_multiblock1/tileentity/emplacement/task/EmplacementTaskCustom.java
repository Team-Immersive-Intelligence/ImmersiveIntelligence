package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */
public class EmplacementTaskCustom extends EmplacementTaskEntities
{
	private final NBTTagCompound tagCompound;

	public EmplacementTaskCustom(NBTTagCompound tagCompound)
	{
		//input -> input instanceof EntityLivingBase&&(input instanceof IMob||input.getTeam()!=null)&&input.isEntityAlive()
		super(buildPredicate(tagCompound));
		this.tagCompound = tagCompound;
	}

	private static Predicate<Entity> buildPredicate(NBTTagCompound nbt)
	{
		Predicate<Entity> mainFilter = entity -> entity!=null&&entity.isEntityAlive();
		ArrayList<Predicate<Entity>> typeFilter = new ArrayList<>();
		ArrayList<Predicate<Entity>> nameFilter = new ArrayList<>();
		ArrayList<Predicate<Entity>> teamFilter = new ArrayList<>();

		NBTTagList tagList = nbt.getTagList("filters", 10);
		for(NBTBase nbtBase : tagList)
			if(nbtBase instanceof NBTTagCompound)
			{
				NBTTagCompound tag = (NBTTagCompound)nbtBase;
				final String filter = tag.getString("filter");
				final boolean negation = tag.getBoolean("negation");

				switch(tag.getString("type"))
				{
					case "mobs":
					{
						if(!filter.isEmpty())
							teamFilter.add(entity -> {
								EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
								if(entry==null)
									return false;
								return negation^new ResourceLocation(filter).equals(entry.getRegistryName());
							});
						else
							typeFilter.add(entity -> entity instanceof IMob);
					}
					break;
					case "animals":
					{
						if(!filter.isEmpty())
							teamFilter.add(entity -> {
								EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
								if(entry==null)
									return false;
								return negation^new ResourceLocation(filter).equals(entry.getRegistryName());
							});
						else
							typeFilter.add(entity -> entity instanceof EntityAnimal);
					}
					break;
					case "players":
						typeFilter.add(entity -> entity instanceof EntityPlayer);
					case "npcs":
						typeFilter.add(entity -> entity instanceof INpc);
						break;
					case "vehicles":
						typeFilter.add(entity -> entity instanceof IVehicleMultiPart);
					case "shells":
						typeFilter.add(entity -> entity instanceof EntityAmmoArtilleryProjectile);
						break;
					case "team":
					{
						if(!filter.isEmpty())
							teamFilter.add(entity -> entity.getTeam()!=null&&(negation^entity.getTeam().getName().equals(filter)));
						else
							teamFilter.add(entity -> entity.getTeam()==null);
					}
					break;
					case "name":
					{
						if(!filter.isEmpty())
							nameFilter.add(entity -> negation^entity.getName().equals(filter));
						else
							nameFilter.add(entity -> entity.getName().isEmpty());
					}
					break;
				}
			}
		if(!typeFilter.isEmpty())
			mainFilter = mainFilter.and(predicateFromList(typeFilter));
		if(!nameFilter.isEmpty())
			mainFilter = mainFilter.and(predicateFromList(nameFilter));
		if(!teamFilter.isEmpty())
			mainFilter = mainFilter.and(predicateFromList(teamFilter));


		return mainFilter;
	}

	private static Predicate<Entity> predicateFromList(ArrayList<Predicate<Entity>> list)
	{
		Predicate<Entity> pred = list.get(0);
		if(list.size() > 1)
			for(int i = 1; i < list.size(); i++)
			{
				pred = pred.or(list.get(i));
			}

		return pred;
	}

	@Override
	public String getName()
	{
		return "target_custom";
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound nbt = super.saveToNBT();
		nbt.merge(tagCompound);
		return nbt;
	}
}
