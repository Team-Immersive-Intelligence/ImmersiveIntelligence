package pl.pabilo8.immersiveintelligence.client.model;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoItem;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;

/**
 * @author Pabilo8
 * @since 04-10-2019
 */
@SideOnly(Side.CLIENT)
public interface IBulletModel extends IReloadableModelContainer<IBulletModel>
{
	/**
	 * Renders casing and core.
	 * When your bullet has a gunpowder filling animation it is preferred to override this method and render a simpler casing model (less geometry -> more performance)
	 *
	 * @param coreColour  of the bullet, see {@link IAmmoCore#getColour()}
	 * @param coreType    of the bullet, see {@link IAmmoItem#getAllowedCoreTypes()}
	 * @param paintColour in rgbInt format
	 */
	default void renderBulletUnused(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		renderCasing(1f, paintColour);
		renderCore(coreColour, coreType);
	}

	/**
	 * Same as {@link #renderBulletUnused(int, EnumCoreTypes, int)}, use according to your needs
	 *
	 * @param stack to be rendered
	 */
	default void renderBulletUnused(ItemStack stack)
	{
		IAmmoItem b = (IAmmoItem)stack.getItem();
		renderBulletUnused(b.getCore(stack).getColour(), b.getCoreType(stack), b.getPaintColor(stack));
	}

	/**
	 * By default, shot bullets render only the core (as casing is expelled)
	 * Override it when rendering grenades (so the stick/casing would be rendered too)
	 *
	 * @param coreColour  of the bullet, see {@link IAmmoCore#getColour()}
	 * @param coreType    of the bullet, see {@link IAmmoItem#getAllowedCoreTypes()}
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
	 * @param paintColour         in rgbInt format, -1 if unpainted
	 */
	void renderCasing(float gunpowderPercentage, int paintColour);

	void renderCore(int coreColour, EnumCoreTypes coreType);
}
