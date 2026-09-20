package pl.pabilo8.immersiveintelligence.client.util.amt.models;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * A standard interface for the many AMT model and component classes
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.08.2025
 */
public interface AMTRenderable
{
	/**
	 * Set all variables to default values
	 */
	void defaultize();

	/**
	 * Draw this AMT
	 */
	void render(Tessellator tes, BufferBuilder buf);

	/**
	 * Clean up when this AMT is no longer needed. Remove GL CallLists so they won't waste space.
	 */
	void disposeOf();

	/**
	 * Applies properties from an NBT structure
	 */
	default void applyProperties(EasyNBT nbt)
	{

	}

	/**
	 * @return A bounding box box spanning the entire area used to render this AMT.
	 */
	@Nonnull
	AxisAlignedBB getBoundingBox();
}
