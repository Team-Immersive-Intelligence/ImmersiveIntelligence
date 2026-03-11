package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleControls implements INBTSerializable<NBTTagCompound>
{
	private final Map<String, Boolean> states = new HashMap<>();
	@SideOnly(Side.CLIENT)
	private Map<KeyBinding, String> keybinds;
	private boolean dirty = false;

	public VehicleControls()
	{

	}

	public VehicleControls withState(String name)
	{
		states.put(name, false);
		this.dirty = true;
		return this;
	}

	public VehicleControls withStates(String... names)
	{
		for(String s : names)
			states.put(s, false);
		this.dirty = true;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public VehicleControls withKeyBinding(KeyBinding key, String name)
	{
		states.put(name, false);
		if(keybinds==null)
			keybinds = new HashMap<>();
		keybinds.put(key, name);
		this.dirty = true;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public boolean clientUpdate()
	{
		this.dirty = false;
		if(keybinds!=null)
			for(Entry<KeyBinding, String> entry : keybinds.entrySet())
			{
				KeyBinding keyBinding = entry.getKey();
				states.compute(entry.getValue(), (name, current) -> {
					boolean keyDown = keyBinding.isKeyDown();
					if(keyDown!=current)
						this.dirty = true;
					return keyDown;
				});

			}
		return isDirty();
	}

	public void setKey(String key, boolean pressed)
	{
		states.computeIfPresent(key, (k, v) -> {
			if(pressed!=v)
				this.dirty = true;
			return pressed;
		});
	}

	public boolean getKey(String key)
	{
		return states.getOrDefault(key, false);
	}

	public boolean isDirty()
	{
		return dirty;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		states.forEach((tag::setBoolean));
		this.dirty = false;
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		for(String key : nbt.getKeySet())
			states.put(key, nbt.getBoolean(key));
		this.dirty = false;
	}
}
