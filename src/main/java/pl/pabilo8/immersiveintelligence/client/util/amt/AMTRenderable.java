package pl.pabilo8.immersiveintelligence.client.util.amt;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

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
}
