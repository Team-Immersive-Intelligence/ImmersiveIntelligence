package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Pabilo8
 * @since 15.04.2021
 */
public class IIBipedLayerRenderer implements LayerRenderer<EntityLivingBase>
{
	public static boolean rendersAssigned = false;
	public static Map<UUID, Pair<ItemStack, Integer>> ADVANCED_POWERPACK_PLAYERS = new HashMap<>();

	@Override
	public void doRenderLayer(EntityLivingBase living, float limbSwing, float prevLimbSwing, float partialTicks, float rotation, float yaw, float pitch, float scale)
	{
		if(!living.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()&&ItemNBTHelper.hasKey(living.getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack))
		{
			ItemStack powerpack = ItemNBTHelper.getItemStack(living.getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack);
			addWornAdvancedPowerpack(living, powerpack);
		}

		if(ADVANCED_POWERPACK_PLAYERS.containsKey(living.getUniqueID()))
		{
			Pair<ItemStack, Integer> entry = ADVANCED_POWERPACK_PLAYERS.get(living.getUniqueID());
			renderAdvancedPowerpack(entry.getLeft(), living, limbSwing, prevLimbSwing, partialTicks, rotation, yaw, pitch, scale);
			int time = entry.getValue()-1;
			if(time <= 0)
				ADVANCED_POWERPACK_PLAYERS.remove(living.getUniqueID());
			else
				ADVANCED_POWERPACK_PLAYERS.put(living.getUniqueID(), Pair.of(entry.getLeft(), time));
		}
	}

	public static void addWornAdvancedPowerpack(EntityLivingBase living, ItemStack powerpack)
	{
		ADVANCED_POWERPACK_PLAYERS.put(living.getUniqueID(), Pair.of(powerpack, 5));
	}

	private void renderAdvancedPowerpack(ItemStack powerpack, EntityLivingBase living, float limbSwing, float prevLimbSwing, float partialTicks, float rotation, float yaw, float pitch, float scale)
	{
		if(!powerpack.isEmpty())
		{
			GlStateManager.pushMatrix();
			ModelBiped model = IIContent.itemAdvancedPowerPack.getArmorModel(living, powerpack, EntityEquipmentSlot.CHEST, null);
			ClientUtils.bindTexture(IIContent.itemAdvancedPowerPack.getArmorTexture(powerpack, living, EntityEquipmentSlot.CHEST, null));
			model.render(living, limbSwing, prevLimbSwing, rotation, yaw, pitch, scale);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}
}
