package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */ //Yes, this had to be done
//Else I'd have to do ATs on internal classes and get it somehow
public enum EnumTaskType implements IStringSerializable
{
	MOBS(() ->
			ArrayUtils.add(
					GameData.getEntityClassMap().values().stream()
							.filter(entityEntry -> IMob.class.isAssignableFrom(entityEntry.getEntityClass()))
							.map(entityEntry -> entityEntry.delegate.name())
							.map(ResourceLocation::toString)
							.toArray(String[]::new),
					0,
					""
			)

	),
	ANIMALS(() ->
			ArrayUtils.add(
					GameData.getEntityClassMap().values().stream()
							.filter(entityEntry -> EntityAnimal.class.isAssignableFrom(entityEntry.getEntityClass()))
							.map(entityEntry -> entityEntry.delegate.name())
							.map(ResourceLocation::toString)
							.toArray(String[]::new),
					0,
					""
			)
	),
	PLAYERS,
	NPCS(() ->
			ArrayUtils.add(
					GameData.getEntityClassMap().values().stream()
							.filter(entityEntry -> INpc.class.isAssignableFrom(entityEntry.getEntityClass()))
							.map(entityEntry -> entityEntry.delegate.name())
							.map(ResourceLocation::toString)
							.toArray(String[]::new),
					0,
					""
			)
	),
	VEHICLES,
	SHELLS,
	TEAM,
	NAME;

	private final Supplier<String[]> entries;

	EnumTaskType()
	{
		this(() -> new String[0]);
	}

	EnumTaskType(Supplier<String[]> entries)
	{
		this.entries = entries;
	}

	@Nonnull
	@Override
	public String getName()
	{
		return this.toString().toLowerCase(Locale.ENGLISH);
	}

	public String[] getDropdownEntries()
	{
		return entries.get();
	}
}
