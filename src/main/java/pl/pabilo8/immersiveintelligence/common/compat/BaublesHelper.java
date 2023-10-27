package pl.pabilo8.immersiveintelligence.common.compat;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import blusunrize.immersiveengineering.common.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.client.render.IIBipedLayerRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 21.07.2022
 */
public class BaublesHelper extends IICompatModule
{
	private ResourceLocation res = new ResourceLocation("baubles", "bauble_cap");
	private static final IBauble BAUBLE_POWERPACK = new IBauble()
	{
		@Override
		public BaubleType getBaubleType(ItemStack itemStack)
		{
			return BaubleType.BODY;
		}

		@Override
		public void onWornTick(ItemStack itemstack, EntityLivingBase player)
		{
			if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
				IIBipedLayerRenderer.addWornAdvancedPowerpack(player, itemstack);
			if(player instanceof EntityPlayer)
				IIContent.itemAdvancedPowerPack.onArmorTick(player.world, (EntityPlayer)player, itemstack);
		}
	};

	@Override
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		Config.manual_bool.put("baublesHere", true);
		baubles = true;
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
	}

	@Override
	public void postInit()
	{
	}

	public static ItemStack getWornPouch(EntityPlayer player)
	{
		return BaublesApi.getBaublesHandler(player).getStackInSlot(BaubleType.AMULET.getValidSlots()[0]);
	}

	@SubscribeEvent
	public void onCapabilitiesAttach(AttachCapabilitiesEvent<ItemStack> event)
	{
		if(event.getObject().getItem()==IIContent.itemAdvancedPowerPack)
			event.addCapability(res, provideBaubles(BAUBLE_POWERPACK));

		if(event.getObject().getItem()==IIContent.itemCasingPouch)
			event.addCapability(res, provideBaubles(stack -> BaubleType.AMULET));
	}

	public ICapabilityProvider provideBaubles(final IBauble bauble)
	{
		return new ICapabilityProvider()
		{
			@Override
			public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
			{
				return capability==BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
			{
				return capability==BaublesCapabilities.CAPABILITY_ITEM_BAUBLE?BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(bauble): null;
			}
		};
	}
}
