package pl.pabilo8.immersiveintelligence.common.items.tools;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.ITextureOverride;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.tools;

/**
 * Created by Pabilo8 on 15-09-2019.
 */
public class ItemIIBinoculars extends ItemIIBase implements IAdvancedZoomTool, IIEEnergyItem, ITextureOverride
{
	public ItemIIBinoculars()
	{
		super("binoculars", 1, "binoculars", "infrared_binoculars");
	}

	public boolean isAdvanced(ItemStack stack)
	{
		return stack.getMetadata()==1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		if(isAdvanced(stack))
		{
			String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
			list.add(I18n.format(Lib.DESC+"info.energyStored", stored));
			list.add(I18n.format(ImmersiveIntelligence.proxy.description_key+(ItemNBTHelper.getBoolean(stack, "enabled")?"infrared_enabled": "infrared_disabled"), stored));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		boolean do_tick = worldIn.getTotalWorldTime()%20==0;

		if(!worldIn.isRemote&&entityIn instanceof EntityLivingBase)
		{
			if(!isSelected||!entityIn.isSneaking()&&do_tick)
			{
				if(isAdvanced(stack))
				{
					ItemNBTHelper.setBoolean(stack, "enabled", false);
					if(ItemNBTHelper.getBoolean(stack, "wasUsed"))
					{
						ItemNBTHelper.setBoolean(stack, "wasUsed", false);
						((EntityLivingBase)entityIn).removePotionEffect(Potion.getPotionFromResourceLocation("minecraft:night_vision"));
					}
				}
			}
			else
			{
				if(worldIn.getTotalWorldTime()%5==0&&entityIn.isSneaking()&&entityIn instanceof EntityPlayerMP)
				{
					float yaw = (360+((EntityLivingBase)entityIn).rotationYawHead)%360;
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.info_key+"yaw", yaw)), (EntityPlayerMP)entityIn);
				}

				if(do_tick&&isEnabled(stack)&&isAdvanced(stack))
				{
					if(worldIn.getLightBrightness(entityIn.getPosition()) <= 0.5f)
						((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:night_vision"), 240, 1, true, false));
					else
						((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:blindness"), 240, 1, false, false));
					ItemNBTHelper.setBoolean(stack, "wasUsed", true);

					extractEnergy(stack, tools.advanced_binoculars_energy_usage, false);
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
	public boolean canZoom(ItemStack stack, EntityPlayer player)
	{
		if((player).moveForward==0)
		{
			//ImmersiveEngineering.packetHandler.
			return true;
		}
		return false;
	}

	@Override
	public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
	{
		return isAdvanced(stack)?tools.advanced_binoculars_max_zoom: tools.binoculars_max_zoom;
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return isAdvanced(container)?tools.advanced_binoculars_energy_capacity: 0;
	}

	@Override
	public String getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
	{
		return ImmersiveIntelligence.MODID+":textures/gui/item/binoculars.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getModelCacheKey(ItemStack stack)
	{
		return "infrared_binoculars_"+((isEnabled(stack))?"on": "off");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		return Arrays.asList(
				new ResourceLocation(ImmersiveIntelligence.MODID+":items/binoculars/"+key)
		);
	}

	boolean isEnabled(ItemStack stack)
	{
		return ItemNBTHelper.getBoolean(stack, "enabled")&&getEnergyStored(stack) >= tools.advanced_binoculars_energy_usage;
	}
}
