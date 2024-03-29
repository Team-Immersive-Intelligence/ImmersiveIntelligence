package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

/**
 * @author Pabilo8
 * @updated 19.02.2024
 * @ii-approved 0.3.1
 * @since 04-10-2019
 */
@SideOnly(Side.CLIENT)
public interface IAmmoModel<T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>>
{
	/**
	 * Renders core and casing (depends on ammo type).
	 * When your bullet has a gunpowder filling animation it is preferred to override this method and render a simpler casing model (less geometry -> more performance)
	 *
	 * @param tes
	 * @param buf
	 * @param paintColour  in rgbInt format
	 * @param coreMaterial of the ammo, see {@link AmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	void renderAmmoComplete(boolean used, int paintColour, AmmoCore coreMaterial, CoreTypes coreType);

	@SuppressWarnings("unchecked")
	default void renderAmmoComplete(boolean used, ItemStack stack)
	{
		if(stack.isEmpty())
			return;
		IAmmoTypeItem<T, E> ammo = (IAmmoTypeItem<T, E>)stack.getItem();
		renderAmmoComplete(used, ammo.getPaintColor(stack), ammo.getCore(stack), ammo.getCoreType(stack));
	}

	default void renderAmmoComplete(E entity, float partialTicks)
	{
		renderAmmoComplete(true, entity.getPaintColour(), entity.getCore(), entity.getCoreType());
	}

	/**
	 * Renders the casing.
	 * If your bullet has a gunpowder filling animation it's preferred you add a simpler casing model
	 *
	 * @param gunpowderPercentage how much is the casing filled with gunpowder
	 * @param paintColour         in rgbInt format, -1 if unpainted
	 */
	void renderCasing(float gunpowderPercentage, int paintColour);

	/**
	 * @param coreMaterial of the ammo, see {@link AmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	void renderCore(AmmoCore coreMaterial, CoreTypes coreType);

	@SuppressWarnings("unchecked")
	default void renderCore(ItemStack stack)
	{
		if(stack.isEmpty())
			return;
		IAmmoTypeItem<T, E> ammo = (IAmmoTypeItem<T, E>)stack.getItem();
		renderCore(ammo.getCore(stack), ammo.getCoreType(stack));
	}
}
