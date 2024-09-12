package pl.pabilo8.immersiveintelligence.client.fx.prefab;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 17.05.2024
 */
public interface ITexturedParticle
{
	//--- Texture ---//

	/**
	 * Sets the texture of the particle
	 *
	 * @param textureID       ID of the texture
	 * @param textureLocation location of the texture
	 */
	void retexture(int textureID, ResLoc textureLocation);

	/**
	 * Sets the texture array of the particle
	 *
	 * @param textures array of textures
	 */
	void setTextures(TextureAtlasSprite[] textures);

	/**
	 * Shifts texture IDs right by the given amount
	 *
	 * @param textureShift amount of shift
	 */
	void setTextureShift(int textureShift);

	/**
	 * @return texture of this particle
	 */
	float getTexturesCount();

	//--- Color ---//

	/**
	 * Sets the color of the particle
	 *
	 * @param color color of the particle
	 */
	void setColor(IIColor color);

	/**
	 * @return color of this particle
	 */
	IIColor getColor();
}
