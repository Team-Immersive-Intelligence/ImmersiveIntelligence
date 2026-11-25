package pl.pabilo8.immersiveintelligence.client.util.amt.renderer;


import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Modifies {@link ModelBiped} rendering for a {@link net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 20.11.2026
 */
public interface IBipedModifyingRenderer
{
	/**
	 * Called during {@link ModelBiped} rendering for an {@link net.minecraft.entity.EntityLivingBase}.<br>
	 * Use to adjust the biped model angles or draw a special model variant specific.
	 *
	 * @param livingBase
	 * @param biped         biped model being rendered
	 * @param stack         item stack held
	 * @param hand          entity hand holding the stack
	 * @param swingProgress current swing animation progress
	 * @param partialTicks  partial ticks
	 * @return true if custom biped render handling was performed, false otherwise
	 */
	boolean onBipedRender(EntityLivingBase livingBase, ModelBiped biped, ItemStack stack, EnumHand hand, float swingProgress, float partialTicks);
}
