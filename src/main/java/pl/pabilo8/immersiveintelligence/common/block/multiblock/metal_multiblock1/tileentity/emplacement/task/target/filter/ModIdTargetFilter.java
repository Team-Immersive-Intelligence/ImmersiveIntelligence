package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetingLimits;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Matches the namespace of an entity registry identifier.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class ModIdTargetFilter implements TargetFilter
{
	@Getter
	private String modId = "";
	private boolean valid;

	public ModIdTargetFilter()
	{
	}

	public ModIdTargetFilter(String modId)
	{
		setModId(modId);
	}

	public void setModId(String modId)
	{
		this.modId = TargetingLimits.clampString(modId==null?"": modId.trim().toLowerCase(Locale.ENGLISH));
		try
		{
			valid = !this.modId.isEmpty()&&new ResourceLocation(this.modId, "target").getResourceDomain().equals(this.modId);
		} catch(RuntimeException ignored)
		{
			valid = false;
		}
	}

	@Override
	public boolean matches(@Nonnull TargetEvaluationContext context)
	{
		String candidate = context.getRegistryModId();
		return valid&&modId.equals(candidate);
	}

	@Nonnull
	@Override
	public TargetFilterType getType()
	{
		return TargetFilterType.MOD_ID;
	}

	@Nonnull
	@Override
	public String getSummary()
	{
		return modId;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("value", modId).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		String value = nbt.getString("value");
		if(value.length() > TargetingLimits.MAX_STRING_LENGTH)
			throw new IllegalArgumentException("Invalid mod ID target filter");
		setModId(value);
		if(!value.isEmpty()&&!valid)
			throw new IllegalArgumentException("Invalid mod ID target filter");
	}
}
