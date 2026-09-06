package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores saved target-tree presets and the selected autonomous Job.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public class TargetConfiguration implements INBTSerializable<NBTTagCompound>
{
	public static final String KEY_PRESETS = "target_presets";
	public static final String KEY_ACTIVE = "active_target_preset";

	@Getter
	private EasyCollection<TargetDecisionTreePreset, NBTTagCompound> presets =
			new EasyCollection<>(TargetDecisionTreePreset::new);
	@Getter
	private String activePresetId = "";
	private boolean valid = true;

	public TargetConfiguration()
	{
	}

	public boolean addPreset(@Nullable TargetDecisionTreePreset preset)
	{
		if(preset==null||!preset.isValid()||presets.size() >= TargetingLimits.MAX_PRESETS||findPreset(preset.getId())!=null)
			return false;
		return presets.add(preset);
	}

	public boolean removePreset(String id)
	{
		TargetDecisionTreePreset preset = findPreset(id);
		if(preset==null)
			return false;
		presets.remove(preset);
		if(activePresetId.equals(id))
			activePresetId = "";
		return true;
	}

	@Nullable
	public TargetDecisionTreePreset findPreset(String id)
	{
		if(id==null)
			return null;
		for(TargetDecisionTreePreset preset : presets)
			if(id.equals(preset.getId()))
				return preset;
		return null;
	}

	@Nullable
	public TargetDecisionTreePreset getActivePreset()
	{
		return findPreset(activePresetId);
	}

	public void setActivePresetId(@Nullable String activePresetId)
	{
		String id = activePresetId==null?"": activePresetId;
		this.activePresetId = id.isEmpty()||findPreset(id)!=null?id: "";
	}

	public boolean isValid()
	{
		if(!valid||presets.size() > TargetingLimits.MAX_PRESETS)
			return false;
		Set<String> ids = new HashSet<>();
		for(TargetDecisionTreePreset preset : presets)
			if(!preset.isValid()||!ids.add(preset.getId()))
				return false;
		return activePresetId.isEmpty()||ids.contains(activePresetId);
	}

	public boolean restoreBuiltIn(String id)
	{
		TargetDecisionTreePreset replacement = TargetPresetDefaults.create(id);
		TargetDecisionTreePreset existing = findPreset(id);
		if(replacement==null||existing==null||!existing.isBuiltin())
			return false;
		int index = presets.indexOf(existing);
		presets.set(index, replacement);
		return true;
	}

	public TargetConfiguration copy()
	{
		TargetConfiguration copy = new TargetConfiguration();
		copy.deserializeNBT(serializeNBT());
		return copy;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withSerializable(KEY_PRESETS, presets)
				.withString(KEY_ACTIVE, activePresetId)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyCollection<TargetDecisionTreePreset, NBTTagCompound> parsed =
				new EasyCollection<>(TargetDecisionTreePreset::new);
		this.valid = false;
		if(nbt==null||!nbt.hasKey(KEY_PRESETS, EasyNBT.TAG_LIST))
		{
			this.presets = parsed;
			this.activePresetId = "";
			return;
		}

		NBTTagList list = nbt.getTagList(KEY_PRESETS, EasyNBT.TAG_COMPOUND);
		if(list.tagCount() > TargetingLimits.MAX_PRESETS)
		{
			this.presets = parsed;
			this.activePresetId = "";
			return;
		}

		Set<String> ids = new HashSet<>();
		for(int i = 0; i < list.tagCount(); i++)
		{
			TargetDecisionTreePreset preset = new TargetDecisionTreePreset();
			preset.deserializeNBT(list.getCompoundTagAt(i));
			if(!preset.isValid()||!ids.add(preset.getId()))
			{
				this.presets = new EasyCollection<>(TargetDecisionTreePreset::new);
				this.activePresetId = "";
				return;
			}
			parsed.add(preset);
		}

		String active = nbt.getString(KEY_ACTIVE);
		if(active.length() > TargetingLimits.MAX_STRING_LENGTH)
			active = "";
		this.presets = parsed;
		this.activePresetId = active;
		if(!active.isEmpty()&&findPreset(active)==null)
			this.activePresetId = "";
		this.valid = true;
	}

	/**
	 * Creates an independent default target configuration.
	 */
	@Nonnull
	public static TargetConfiguration createDefault()
	{
		return TargetPresetDefaults.createConfiguration();
	}
}
