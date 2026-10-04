package pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Serialises one registered clipboard value type and supplies its human-readable preview.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public abstract class ClipboardProvider<T>
{
	public final String id;
	@Nullable
	protected final Class<T> type;

	protected ClipboardProvider(String id, @Nullable Class<T> type)
	{
		this.id = id;
		this.type = type;
	}

	public boolean accepts(Object value)
	{
		return type!=null&&type.isInstance(value);
	}

	public abstract NBTTagCompound write(T value);

	@Nullable
	public abstract T read(NBTTagCompound nbt);

	public abstract String preview(T value);

	public boolean isLink()
	{
		return false;
	}

	public void activate(T value)
	{
	}
}
