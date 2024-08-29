package pl.pabilo8.immersiveintelligence.common.util.item;

import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

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

	private final ResLoc creativeTabTexture;

	IICategory()
	{
		creativeTabTexture = ResLoc.of(IIReference.RES_TEXTURES_CREATIVE, "tab_background_", getName())
				.withExtension(ResLoc.EXT_PNG);
	}

	public ResLoc getCreativeTabTexture()
	{
		return creativeTabTexture;
	}
}
