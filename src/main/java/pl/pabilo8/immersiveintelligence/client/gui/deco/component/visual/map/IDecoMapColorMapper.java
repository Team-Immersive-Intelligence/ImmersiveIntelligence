package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

/**
 * Abstract base class for map color mappers that convert block positions to colors.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.12.2025
 */
public interface IDecoMapColorMapper extends ILocalizedEnum
{
	/**
	 * Gets the RGB color for a specific block position in the world.
	 *
	 * @param state   The block state at the position
	 * @param pos     The position in the world
	 * @param world   The world instance
	 * @param sampleY The Y-coordinate being sampled (may differ from pos.getY())
	 * @return ARGB color (0xAARRGGBB)
	 */
	int getColor(IBlockState state, BlockPos pos, World world, int sampleY);

	/**
	 * Optional method to apply height-based shading to a color.
	 * Default implementation uses vanilla-style height shading.
	 *
	 * @param color   The base color to shade
	 * @param sampleY The Y-coordinate being sampled
	 * @return The shaded color
	 */
	default int applyHeightShading(int color, int sampleY)
	{
		//Simple deterministic shading based on height
		int shadeLevel = (sampleY%4+4)%4; //Ensure positive
		float shadeFactor = 0.8f+(shadeLevel*0.1f);
		return applyShading(color, shadeFactor);
	}

	/**
	 * Helper method to apply shading factor to a color.
	 *
	 * @param color  The original color
	 * @param factor The shading factor (1.0 = no change)
	 * @return The shaded color
	 */
	default int applyShading(int color, float factor)
	{
		int a = (color>>24)&0xFF;
		int r = (color>>16)&0xFF;
		int g = (color>>8)&0xFF;
		int b = color&0xFF;

		r = net.minecraft.util.math.MathHelper.clamp((int)(r*factor), 0, 255);
		g = net.minecraft.util.math.MathHelper.clamp((int)(g*factor), 0, 255);
		b = net.minecraft.util.math.MathHelper.clamp((int)(b*factor), 0, 255);

		return (a<<24)|(r<<16)|(g<<8)|b;
	}

	/**
	 * Called once per map update to allow the mapper to perform any initialization.
	 *
	 * @param world The world being mapped
	 * @param size  The map size, 128 is the default
	 */
	default void prepare(World world, int size)
	{

	}
}
