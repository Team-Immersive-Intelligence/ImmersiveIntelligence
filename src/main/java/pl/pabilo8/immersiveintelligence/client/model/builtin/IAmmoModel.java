package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;

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
	 * @param used         true for flying projectiles
	 * @param paintColor   in rgbInt format
	 * @param coreMaterial of the ammo, see {@link AmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	void renderAmmoComplete(boolean used, IIColor paintColor, AmmoCore coreMaterial, CoreType coreType);

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
		renderAmmoComplete(true, entity.getPaintColor(), entity.getCore(), entity.getCoreType());
	}

	/**
	 * Renders the casing.
	 * If your bullet has a gunpowder-filling animation, it is preferred you add a simpler casing model
	 *
	 * @param gunpowderPercentage how much of the casing is filled with gunpowder (0.0-1.0)
	 * @param paintColor          color of markings on the casing
	 */
	void renderCasing(float gunpowderPercentage, @Nullable IIColor paintColor);

	/**
	 * @param coreMaterial of the ammo, see {@link AmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	void renderCore(AmmoCore coreMaterial, CoreType coreType);

	@SuppressWarnings("unchecked")
	default void renderCore(ItemStack stack)
	{
		if(stack.isEmpty())
			return;
		IAmmoTypeItem<T, E> ammo = (IAmmoTypeItem<T, E>)stack.getItem();
		renderCore(ammo.getCore(stack), ammo.getCoreType(stack));
	}
}
