package pl.pabilo8.immersiveintelligence.common.util.item;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.ITextureOverride;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */

public interface IIIItemTextureOverride extends ITextureOverride
{
	/**
	 * Used for registering sprites to be used for items
	 *
	 * @param map the TextureMap to register to
	 */
	@SideOnly(Side.CLIENT)
	void registerSprites(TextureMap map);
}
