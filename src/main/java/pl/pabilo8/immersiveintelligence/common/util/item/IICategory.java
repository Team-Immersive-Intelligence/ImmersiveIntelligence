package pl.pabilo8.immersiveintelligence.common.util.item;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * @author GabrielV (gabriel@iiteam.net)
 */
public enum IICategory implements ISerializableEnum
{
	NULL,
	RESOURCES,
	TOOLS,
	ELECTRONICS,
	LOGISTICS,
	WARFARE,
	ROTARY;

	private final ResLoc creativeTabTexture, creativeIconTexture;

	IICategory()
	{
		creativeTabTexture = ResLoc.of(IIReference.RES_TEXTURES_CREATIVE, "tab_background_", getName())
				.withExtension(ResLoc.EXT_PNG);
		creativeIconTexture = ResLoc.of(IIReference.RES_II, "gui/creative_gui/tab_icon_", getName());
	}

	public ResLoc getCreativeTabTexture()
	{
		return creativeTabTexture;
	}

	public ResourceLocation getCreativeIconTexture()
	{
		return creativeIconTexture;
	}
}
