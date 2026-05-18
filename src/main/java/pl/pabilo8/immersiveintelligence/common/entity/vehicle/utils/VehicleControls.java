package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
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
	private Map<MouseBinding, String> mousebinds;
	private boolean dirty = false;

	public VehicleControls()
	{

	}

	public VehicleControls withState(@Nonnull String name)
	{
		states.put(name, false);
		this.dirty = true;
		return this;
	}

	public VehicleControls withStates(@Nonnull String... names)
	{
		for(String s : names)
			states.put(s, false);
		this.dirty = true;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public VehicleControls withKeyBinding(@Nonnull KeyBinding key, @Nonnull String name)
	{
		states.put(name, false);
		if(keybinds==null)
			keybinds = new HashMap<>();
		keybinds.put(key, name);
		this.dirty = true;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public VehicleControls withMouseBinding(@Nonnull MouseBinding key, @Nonnull String name)
	{
		states.put(name, false);
		if(mousebinds==null)
			mousebinds = new HashMap<>();
		mousebinds.put(key, name);
		this.dirty = true;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public boolean clientUpdate()
	{
		this.dirty = false;
		if(keybinds==null)
			keybinds = new HashMap<>();

		for(Entry<KeyBinding, String> entry : keybinds.entrySet())
		{
			KeyBinding keyBinding = entry.getKey();
			states.compute(entry.getValue(), (name, current) -> {
				boolean keyDown = keyBinding.isKeyDown();
				//noinspection DataFlowIssue
				if(keyDown!=current)
					this.dirty = true;
				return keyDown;
			});
		}

		if(mousebinds==null)
			mousebinds = new HashMap<>();
		for(Entry<MouseBinding, String> entry : mousebinds.entrySet())
		{
			MouseBinding keyBinding = entry.getKey();
			states.compute(entry.getValue(), (name, current) -> {
				boolean keyDown = keyBinding.pressed;
				//noinspection DataFlowIssue
				if(keyDown!=current)
					this.dirty = true;
				return keyDown;
			});
		}

		return isDirty();
	}

	@SideOnly(Side.CLIENT)
	public boolean passMouseButtonEvent(@Nonnull MouseEvent event)
	{
		if(event.getDwheel()==0)
		{
			MouseBinding.MOUSE_WHEELUP.pressed = false;
			MouseBinding.MOUSE_WHEELDOWN.pressed = false;
		}
		else
		{
			MouseBinding.MOUSE_WHEELUP.pressed = event.getDwheel() > 0;
			MouseBinding.MOUSE_WHEELDOWN.pressed = event.getDwheel() < 0;
		}

		switch(event.getButton())
		{
			//Mouse main
			case 0:
				MouseBinding.MOUSE_LEFT.pressed = event.isButtonstate();
				break;
			case 1:
				MouseBinding.MOUSE_RIGHT.pressed = event.isButtonstate();
				break;
			case 2:
				MouseBinding.MOUSE_MIDDLE.pressed = event.isButtonstate();
				break;
			//Mouse extra buttons
			case 3:
				MouseBinding.MOUSE_NEXT.pressed = event.isButtonstate();
				break;
			case 4:
				MouseBinding.MOUSE_PREV.pressed = event.isButtonstate();
				break;
		}
		return true;
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

	@SideOnly(Side.CLIENT)
	public static class MouseBinding
	{
		public static MouseBinding MOUSE_LEFT = new MouseBinding("mouse_left");
		public static MouseBinding MOUSE_RIGHT = new MouseBinding("mouse_right");
		public static MouseBinding MOUSE_MIDDLE = new MouseBinding("mouse_middle");
		public static MouseBinding MOUSE_NEXT = new MouseBinding("mouse_next");
		public static MouseBinding MOUSE_PREV = new MouseBinding("mouse_prev");
		public static MouseBinding MOUSE_WHEELUP = new MouseBinding("mouse_wheelup");
		public static MouseBinding MOUSE_WHEELDOWN = new MouseBinding("mouse_wheeldown");

		public String key;
		public boolean pressed;

		private MouseBinding(String key)
		{
			this.key = key;
			this.pressed = false;
		}
	}
}
