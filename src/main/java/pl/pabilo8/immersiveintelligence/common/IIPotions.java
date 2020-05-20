package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.IEPotions.IEPotion;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * Created by Pabilo8 on 03-03-2020.
 */
public class IIPotions
{
	public static Potion suppression, broken_armor, corrosion;

	public static void init()
	{
		suppression = new IIPotion(new ResourceLocation(ImmersiveIntelligence.MODID, "suppression"), true, 0xe3bb19, 0, false, 0, true, true);
		suppression.setPotionName("potion."+ImmersiveIntelligence.MODID+".suppression");
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, Utils.generateNewUUID().toString(), -0.007843138f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, Utils.generateNewUUID().toString(), -0.007843138f, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, Utils.generateNewUUID().toString(), -0.125, 2);
		suppression.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		broken_armor = new IIPotion(new ResourceLocation(ImmersiveIntelligence.MODID, "broken_armor"), true, 0x755959, 0, false, 1, true, true);
		broken_armor.setPotionName("potion."+ImmersiveIntelligence.MODID+".broken_armor");
		broken_armor.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, Utils.generateNewUUID().toString(), -0.003921569f, 2);

		corrosion = new IIPotion(new ResourceLocation(ImmersiveIntelligence.MODID, "corrosion"), true, 0x567b46, 0, false, 2, true, true)
		{
			@Override
			public void performEffect(EntityLivingBase living, int amplifier)
			{
				living.getArmorInventoryList().forEach(stack -> stack.damageItem(amplifier, living));
			}
		};
		corrosion.setPotionName("potion."+ImmersiveIntelligence.MODID+".corrosion");
		corrosion.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, Utils.generateNewUUID().toString(), -0.003921569f, 2);


	}

	public static class IIPotion extends IEPotion
	{
		static ResourceLocation tex = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/potioneffects.png");

		public IIPotion(ResourceLocation resource, boolean isBad, int colour, int tick, boolean halveTick, int icon, boolean showInInventory, boolean showInHud)
		{
			super(resource, isBad, colour, tick, halveTick, icon, showInInventory, showInHud);
		}

		@Override
		public int getStatusIconIndex()
		{
			//An absolute trick
			int iconindex = super.getStatusIconIndex();
			Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
			return iconindex;
		}
	}
}
