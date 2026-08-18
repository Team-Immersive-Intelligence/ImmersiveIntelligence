package pl.pabilo8.immersiveintelligence.api.ammocrate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 11.08.2026
 */
@FunctionalInterface
public interface ReloadHandler
{
	/**
	 * Starts a reload action.
	 *
	 * @return true if the action was accepted
	 */
	boolean reload(TileEntityAmmunitionCrate crate, EntityPlayer player, EnumHand hand, ItemStack held);
}
