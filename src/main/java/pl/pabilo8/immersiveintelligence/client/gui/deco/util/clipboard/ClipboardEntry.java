package pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Provider identifier and payload persisted as one clipboard list entry.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.10.2026
 * @since 21.09.2026
 */
@Getter
public class ClipboardEntry
{
	private String typeId;
	private NBTTagCompound value;
	@Setter
	private boolean todo;
	@Setter
	private boolean checked;

	ClipboardEntry(String typeId, NBTTagCompound value)
	{
		this.typeId = typeId;
		this.value = value==null?new NBTTagCompound(): value.copy();
	}

	@Nullable
	public Object getValue()
	{
		ClipboardProvider<?> provider = DecoClipboardUtils.getProvider(typeId);
		if(provider==null)
			return null;
		try
		{
			return provider.read(value.copy());
		} catch(RuntimeException ignored) {return null;}
	}

	@SuppressWarnings("unchecked")
	public String getPreview()
	{
		ClipboardProvider<Object> provider = (ClipboardProvider<Object>)DecoClipboardUtils.getProvider(typeId);
		Object decoded = getValue();
		return provider==null||decoded==null?"": provider.preview(decoded);
	}

	public boolean isValid()
	{
		return DecoClipboardUtils.getProvider(typeId)!=null;
	}

	public NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString(DecoClipboardUtils.TYPE, typeId);
		nbt.setTag(DecoClipboardUtils.VALUE, value.copy());
		if(todo)
		{
			nbt.setBoolean("todo", true);
			nbt.setBoolean("checked", checked);
		}
		return nbt;
	}

	@Nullable
	public static ClipboardEntry fromNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey(DecoClipboardUtils.TYPE, 8)||!nbt.hasKey(DecoClipboardUtils.VALUE, 10))
			return null;
		ClipboardEntry entry = new ClipboardEntry(nbt.getString(DecoClipboardUtils.TYPE), nbt.getCompoundTag(DecoClipboardUtils.VALUE));
		entry.todo = nbt.getBoolean("todo");
		entry.checked = entry.todo&&nbt.getBoolean("checked");
		return entry;
	}

	boolean hasSameValue(ClipboardEntry other)
	{
		return typeId.equals(other.typeId)&&value.equals(other.value);
	}

	/**
	 * Replaces the value and keeps the task state.
	 */
	public void replaceWith(ClipboardEntry other)
	{
		this.typeId = other.typeId;
		this.value = other.value.copy();
	}
}
