package pl.pabilo8.immersiveintelligence.client.model;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCore;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;

/**
 * @author Pabilo8
 * @since 04-10-2019
 */
public interface IBulletModel extends IReloadableModelContainer
{
	/**
	 * Renders casing and core.
	 * When your bullet has a gunpowder filling animation it is preferred to override this method and render a simpler casing model (less geometry -> more performance)
	 *
	 * @param coreColour of the bullet, see {@link IBulletCore#getColour()}
	 * @param coreType of the bullet, see {@link IBullet#getAllowedCoreTypes()}
	 * @param paintColour in rgbInt format
	 */
	default void renderBulletUnused(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		renderCasing(1f, paintColour);
		renderCore(coreColour, coreType);
	}

	/**
	 * Same as {@link #renderBulletUnused(int, EnumCoreTypes, int)}, use according to your needs
	 * @param stack to be rendered
	 */
	default void renderBulletUnused(ItemStack stack)
	{
		IBullet b = (IBullet)stack.getItem();
		renderBulletUnused(b.getCore(stack).getColour(), b.getCoreType(stack), b.getPaintColor(stack));
	}

	/**
	 * By default, shot bullets render only the core (as casing is expelled)
	 * Override it when rendering grenades (so the stick/casing would be rendered too)
	 *
	 * @param coreColour of the bullet, see {@link IBulletCore#getColour()}
	 * @param coreType of the bullet, see {@link IBullet#getAllowedCoreTypes()}
	 * @param paintColour in rgbInt format
	 */
	default void renderBulletUsed(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		renderCore(coreColour, coreType);
	}

	/**
	 * Renders the casing.
	 * If your bullet has a gunpowder filling animation it's preferred you add a simpler casing model and override {@link #renderBulletUnused(int, EnumCoreTypes, int)}
	 *
	 * @param gunpowderPercentage how much is the casing filled with gunpowder
	 * @param paintColour in rgbInt format
	 */
	void renderCasing(float gunpowderPercentage, int paintColour);

	void renderCore(int coreColour, EnumCoreTypes coreType);
}
