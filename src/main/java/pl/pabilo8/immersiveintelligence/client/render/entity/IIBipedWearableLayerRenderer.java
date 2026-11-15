package pl.pabilo8.immersiveintelligence.client.render.entity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Renders backpacks attached to chestplates or worn in the Baubles body slot.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.11.2026
 * @since 15.04.2021
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IIBipedWearableLayerRenderer<T extends EntityLivingBase> implements LayerRenderer<T>
{
	private static final Set<RenderLivingBase<?>> REGISTERED = Collections.newSetFromMap(new WeakHashMap<>());
	private final RenderLivingBase<T> renderer;

	/**
	 * Adds the backpack layer to a biped renderer once or after its layers are rebuilt.
	 */
	public static <T extends EntityLivingBase> void install(RenderLivingBase<T> renderer, boolean rebuilt)
	{
		if(renderer.getMainModel() instanceof ModelBiped&&(REGISTERED.add(renderer)||rebuilt))
			renderer.addLayer(new IIBipedWearableLayerRenderer<>(renderer));
	}

	@Override
	public void doRenderLayer(T living, float limbSwing, float limbAmount, float partialTicks, float age, float yaw, float pitch, float scale)
	{
		ItemStack chest = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if(chest.getItem() instanceof ItemIIBackpackBase)
			return;

		ItemStack backpack = ItemIIBackpackBase.getAttachedBackpack(chest);
		if(backpack.isEmpty()&&IICompatModule.baubles&&living instanceof EntityPlayer)
			backpack = BaublesHelper.getBodyBauble((EntityPlayer)living);

		if(backpack.getItem() instanceof ItemIIBackpackBase&&renderer.getMainModel() instanceof ModelBiped)
		{
			IIArmorRenderer<?> model = IIArmorRenderer.get(backpack);
			if(model!=null)
			{
				IIClientUtils.bindAtlas();
				model.renderOnBiped(backpack, living, (ModelBiped)renderer.getMainModel(), partialTicks, scale);
			}
		}
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}
}
