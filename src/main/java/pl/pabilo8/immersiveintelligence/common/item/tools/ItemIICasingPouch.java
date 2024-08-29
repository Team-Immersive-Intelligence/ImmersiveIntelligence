package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IGuiItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler.IInventoryItem;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23.09.2023
 */
@GeneratedItemModels(itemName = "casing_pouch", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED)
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIICasingPouch extends ItemIIBase implements IIIItemTextureOverride, IInventoryItem, IGuiItem
{
	public ItemIICasingPouch()
	{
		super("casing_pouch", 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> info, ITooltipFlag flag)
	{
		super.addInformation(stack, world, info, flag);

		info.add(IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"casing_pouch")));
	}

	//--- GUI Opening ---//

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking())
			return super.onItemRightClick(world, player, hand);
		CommonProxy.openGuiForItem(player, hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			ItemStack stack = player.getHeldItem(hand);
			IItemHandler capStack = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			boolean inserted = false;

			if(tileEntity!=null&&capStack!=null)
				for(int i = 0; i < getSlotCount(); i++)
				{
					ItemStack stackInside = capStack.extractItem(i, 64, false);
					if(stackInside.isEmpty())
						continue;

					stackInside = Utils.insertStackIntoInventory(tileEntity, stackInside, facing);
					if(stackInside.isEmpty())
						inserted = true;
					capStack.insertItem(i, stackInside, false);
				}

			if(inserted)
				return EnumActionResult.SUCCESS;
			player.swingArm(hand);
		}

		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public IIGuiList getGUI(ItemStack stack)
	{
		return IIGuiList.GUI_CASING_POUCH;
	}

	//--- Model ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "open"))
			return ItemNBTHelper.getBoolean(stack, "contains")?"filled": "empty";
		return "closed";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		switch(key)
		{
			case "filled":
				return Collections.singletonList(ResLoc.of(IIReference.RES_II, "items/casing_pouch/filled"));
			case "empty":
				return Collections.singletonList(ResLoc.of(IIReference.RES_II, "items/casing_pouch/empty"));
			case "closed":
			default:
				return Collections.singletonList(ResLoc.of(IIReference.RES_II, "items/casing_pouch/closed"));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/casing_pouch/empty");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/casing_pouch/filled");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/casing_pouch/closed");
	}

	//--- Inventory Handling ---//

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IIItemStackHandler(stack)
			{
				@Nonnull
				@Override
				public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
				{
					if(isItemValid(slot, stack))
						return super.insertItem(slot, stack, simulate);
					return stack;
				}

				@Override
				public boolean isItemValid(int slot, @Nonnull ItemStack stack)
				{
					return slot < 12?IIRecipes.AMMO_CASINGS.matchesItemStackIgnoringSize(stack): stack.getItem() instanceof ItemIIBulletMagazine;
				}
			};
		return null;
	}

	@Override
	public int getSlotCount()
	{
		return 18;
	}
}
