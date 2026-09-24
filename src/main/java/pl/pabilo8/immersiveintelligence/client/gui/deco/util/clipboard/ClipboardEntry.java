package pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Provider identifier and payload persisted as one clipboard list entry.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
@Getter
public class ClipboardEntry
{
	private String typeId;
	private NBTTagCompound value;

	ClipboardEntry(String typeId, NBTTagCompound value)
	{
		this.typeId = typeId;
		this.value = value==null?new NBTTagCompound(): value.copy();
	}

	@Nullable
	public Object getValue()
	{
		ClipboardProvider<?> provider = DecoClipboardUtils.getProvider(typeId);
		return provider==null?null: provider.read(value.copy());
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
		return nbt;
	}

	@Nullable
	public static ClipboardEntry fromNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey(DecoClipboardUtils.TYPE, 8)||!nbt.hasKey(DecoClipboardUtils.VALUE, 10))
			return null;
		return new ClipboardEntry(nbt.getString(DecoClipboardUtils.TYPE), nbt.getCompoundTag(DecoClipboardUtils.VALUE));
	}

	public void replaceWith(ClipboardEntry other)
	{
		this.typeId = other.typeId;
		this.value = other.value.copy();
	}
}
