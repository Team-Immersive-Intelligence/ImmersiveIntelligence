package pl.pabilo8.immersiveintelligence.common.compat;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides Baubles support for backpacks and the casing pouch.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.07.2022
 */
public class BaublesHelper extends IICompatModule
{
	private static final IBauble BAUBLE_BACKPACK = new IBauble()
	{
		@Override
		public BaubleType getBaubleType(ItemStack itemStack)
		{
			return BaubleType.BODY;
		}

		@Override
		public void onWornTick(ItemStack itemstack, EntityLivingBase player)
		{
			if(!player.world.isRemote&&itemstack.getItem() instanceof ItemIIBackpackBase)
				((ItemIIBackpackBase)itemstack.getItem()).onWornTick(player, itemstack);
		}
	};
	private final ResourceLocation res = new ResourceLocation("baubles", "bauble_cap");

	public static ItemStack getWornPouch(EntityPlayer player)
	{
		return BaublesApi.getBaublesHandler(player).getStackInSlot(BaubleType.AMULET.getValidSlots()[0]);
	}

	/**
	 * Gets the item in the player's Baubles body slot.
	 */
	public static ItemStack getBodyBauble(EntityPlayer player)
	{
		IBaublesItemHandler handler = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
		return handler==null?ItemStack.EMPTY: handler.getStackInSlot(BaubleType.BODY.getValidSlots()[0]);
	}

	@Override
	public String getName()
	{
		return "Baubles";
	}

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

	@SubscribeEvent
	public void onCapabilitiesAttach(AttachCapabilitiesEvent<ItemStack> event)
	{
		if(event.getObject().getItem() instanceof ItemIIBackpackBase)
			event.addCapability(res, provideBaubles(BAUBLE_BACKPACK));

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
