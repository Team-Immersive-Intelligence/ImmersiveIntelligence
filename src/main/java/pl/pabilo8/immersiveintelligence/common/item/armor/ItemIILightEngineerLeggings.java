package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.LightEngineerArmor;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageItemKeybind;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
public class ItemIILightEngineerLeggings extends ItemIILightEngineerArmorBase implements IElectricEquipment
{
	public ItemIILightEngineerLeggings()
	{
		super(EntityEquipmentSlot.LEGS, "LIGHT_ENGINEER_LEGGINGS");
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		super.onArmorTick(world, player, stack);
		if(hasUpgrade(stack, "exoskeleton"))
		{
			int rammingCooldown = ItemNBTHelper.getInt(stack, "rammingCooldown")-1;
			if(rammingCooldown > 0)
				ItemNBTHelper.setInt(stack, "rammingCooldown", rammingCooldown);
			else
				ItemNBTHelper.remove(stack, "rammingCooldown");

			if(world.isRemote)
			{
				if(ClientProxy.keybind_armorExosuit.isPressed())
					IIPacketHandler.sendToServer(new MessageItemKeybind(MessageItemKeybind.KEYBIND_EXOSKELETON));
			}

			int mode = ItemNBTHelper.getInt(stack, "exoskeletonMode");
			int energy = (int)(mode/2f*LightEngineerArmor.exoskeletonEnergyUsage);

			if(mode > 0&&player.isSprinting()&&extractEnergy(stack, energy, false)==energy)
			{
				//keep player saturated, so he won't slow down
				player.getFoodStats().addStats(0, 20);

				//ramming
				if(mode==2&&player.distanceWalkedOnStepModified > 8&&player.isPotionActive(IIPotions.movementAssist))
				{
					List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox(), null);
					if(mobs.size() > 0)
					{
						mobs.forEach(entityMob -> {
							entityMob.knockBack(player, 3, MathHelper.sin(player.rotationYaw*0.017453292F), -MathHelper.cos(player.rotationYaw*0.017453292F));
							entityMob.attackEntityFrom(IEDamageSources.crusher, player.getTotalArmorValue());
						});
						player.getArmorInventoryList().forEach(s -> s.damageItem(2, player));
						player.removePotionEffect(IIPotions.movementAssist);
						ItemNBTHelper.setInt(stack, "rammingCooldown", 40);
					}

				}

				//slow down after ram
				if(ItemNBTHelper.getInt(stack, "rammingCooldown") <= 0&&world.getTotalWorldTime()%4==0)
					player.addPotionEffect(new PotionEffect(IIPotions.movementAssist, 4, mode, false, false));
			}
		}

	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		return ModelLightEngineerArmor.getModel(armorSlot, itemStack);
	}

	@Override
	protected String getMaterialName(ArmorMaterial material)
	{
		return "light_engineer_armor";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> list, @Nonnull ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack)
	{
		return 0.1f;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot, @Nonnull ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

		if(equipmentSlot==this.armorType)
		{
			//multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.03, 1));
		}
		return multimap;
	}

	@Override
	public void onStrike(ItemStack s, EntityEquipmentSlot eqSlot, EntityLivingBase p, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
		{
		}
		// TODO: 13.09.2020 tesla coil interaction
	}

	@Override
	public int getSlotCount()
	{
		return 3;
	}
}
