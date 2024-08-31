package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIBinoculars.Binoculars;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Pabilo8
 * @since 15-09-2019
 */
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIIBinoculars extends ItemIISubItemsBase<Binoculars> implements IAdvancedZoomTool, IIEEnergyItem, IIIItemTextureOverride
{
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/item/binoculars.png");
	public static UUID visionUUID = Utils.generateNewUUID();

	public ItemIIBinoculars()
	{
		super("binoculars", 1, Binoculars.values());
	}

	public enum Binoculars implements IIItemEnum
	{
		BINOCULARS,
		INFRARED_BINOCULARS
	}

	public boolean isAdvanced(ItemStack stack)
	{
		return stackToSub(stack)==Binoculars.INFRARED_BINOCULARS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		list.add(IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"binoculars")));
		if(isAdvanced(stack))
		{
			String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
			list.add(I18n.format(Lib.DESC+"info.energyStored", TextFormatting.GOLD+stored+TextFormatting.RESET));
			list.add(I18n.format(IIReference.DESCRIPTION_KEY+(ItemNBTHelper.getBoolean(stack, "enabled")?"infrared_enabled": "infrared_disabled"), stored));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		boolean do_tick = worldIn.getTotalWorldTime()%20==0;

		if(worldIn.isRemote)
			ItemNBTHelper.setBoolean(stack, "sneaking", entityIn.isSneaking()&&isSelected);

		if(!worldIn.isRemote&&entityIn instanceof EntityLivingBase)
		{
			if(!isSelected||!(entityIn.isSneaking()||entityIn.getLowestRidingEntity() instanceof EntityFieldHowitzer)&&do_tick)
			{
				if(isAdvanced(stack))
				{
					ItemNBTHelper.setBoolean(stack, "enabled", false);
					if(ItemNBTHelper.getBoolean(stack, "wasUsed"))
					{
						ItemNBTHelper.setBoolean(stack, "wasUsed", false);
						((EntityLivingBase)entityIn).removePotionEffect(MobEffects.NIGHT_VISION);
					}
				}
			}
			else
			{
				if(entityIn instanceof EntityPlayerMP&&worldIn.getTotalWorldTime()%5==0&&(entityIn.isSneaking()||entityIn.getLowestRidingEntity() instanceof EntityFieldHowitzer))
				{
					float yaw = (360+((EntityLivingBase)entityIn).rotationYawHead)%360;
					IIPacketHandler.sendChatTranslation(((EntityPlayerMP)entityIn), IIReference.INFO_KEY+"yaw", yaw);
				}

				if(do_tick&&isEnabled(stack)&&isAdvanced(stack))
				{
					if(worldIn.getLightBrightness(entityIn.getPosition()) <= 0.5f)
						((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 240, 1, true, false));
					else
						((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 240, 1, false, false));
					ItemNBTHelper.setBoolean(stack, "wasUsed", true);

					extractEnergy(stack, Tools.advancedBinocularsEnergyUsage, false);
				}

			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemNBTHelper.setBoolean(playerIn.getHeldItem(handIn), "enabled", !ItemNBTHelper.getBoolean(playerIn.getHeldItem(handIn), "enabled"));
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean shouldZoom(ItemStack stack, EntityPlayer player)
	{
		return player.isSneaking()&&player.moveForward==0;
	}

	@Override
	public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
	{
		return isAdvanced(stack)?Tools.advancedBinocularsMaxZoom: Tools.binocularsMaxZoom;
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return isAdvanced(container)?Tools.advancedBinocularsEnergyCapacity: 0;
	}

	@Override
	public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
	{
		return OVERLAY_TEXTURE;
	}

	// TODO: 15.07.2021 make it properly, not the current lazy way 
	@Override
	@SideOnly(Side.CLIENT)
	public String getModelCacheKey(ItemStack stack)
	{
		return ItemNBTHelper.getBoolean(stack, "sneaking")?"invisible": (isAdvanced(stack)?("infrared_binoculars_"+((isEnabled(stack))?"on": "off")): "binoculars");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		if(key.equals("invisible"))
			return Collections.emptyList();
		return Collections.singletonList(
				new ResourceLocation(ImmersiveIntelligence.MODID+":items/binoculars/"+key)
		);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/binoculars/binoculars");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_off");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_on");
	}

	boolean isEnabled(ItemStack stack)
	{
		return ItemNBTHelper.getBoolean(stack, "enabled")&&getEnergyStored(stack) >= Tools.advancedBinocularsEnergyUsage;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty()&&isAdvanced(stack))
			return new IEItemStackHandler(stack)
			{
				final EnergyHelper.ItemEnergyStorage energyStorage = new EnergyHelper.ItemEnergyStorage(stack);

				@Override
				public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
				{
					return capability==CapabilityEnergy.ENERGY||
							super.hasCapability(capability, facing);
				}

				@Override
				public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
				{
					if(capability==CapabilityEnergy.ENERGY)
						return (T)energyStorage;
					if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
						return (T)this;
					return null;
				}
			};
		return null;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1f-(this.getEnergyStored(stack)/(float)this.getMaxEnergyStored(stack));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return stack.getMetadata()==1&&this.getEnergyStored(stack) < this.getMaxEnergyStored(stack);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if(slot==EntityEquipmentSlot.MAINHAND)
			multimap.put(SharedMonsterAttributes.FOLLOW_RANGE.getName(), new AttributeModifier(visionUUID, "Increased Sight Range (Mobs)", 30.0D, 0));
		return multimap;
	}
}
