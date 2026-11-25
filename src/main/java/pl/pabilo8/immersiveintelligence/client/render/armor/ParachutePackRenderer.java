package pl.pabilo8.immersiveintelligence.client.render.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer.RegisteredArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIParachutePack;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * Renders the parachute backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.11.2026
 */
@RegisteredArmorRenderer(name = "armor/backpack/parachute_backpack")
public class ParachutePackRenderer extends IIArmorRenderer<ItemIIParachutePack>
{
	public IIAnimationCachedMap parachuteOpening;

	public ParachutePackRenderer()
	{
		super(IIContent.itemParachutePack, ARMOR_MODELS.with("backpack/parachute_backpack"));
	}

	@Override
	public void reloadModels()
	{
		super.reloadModels();
		this.parachuteOpening = IIAnimationCachedMap.create(model, IIReference.RES_II.with("backpack/parachute_opening"));
	}

	@Override
	protected AMTModel getModelVariant(AMTCachedModel<ItemStack> cache, ItemStack stack, EntityLivingBase wearer, float partialTicks)
	{
		AMTModel model = super.getModelVariant(cache, stack, wearer, partialTicks);
		//Parachute opening animation
		if(wearer!=null&&wearer.getRidingEntity() instanceof EntityParachute parachute)
			parachuteOpening.apply(AMTUtils.getAnimationProgress(parachute.time, 20, true, partialTicks));
		else
			parachuteOpening.apply(0);
		return model;
	}
}
