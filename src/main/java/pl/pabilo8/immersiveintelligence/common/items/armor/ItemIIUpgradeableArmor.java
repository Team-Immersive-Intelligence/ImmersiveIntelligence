package pl.pabilo8.immersiveintelligence.common.items.armor;

import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.IESlot.Upgrades;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIArmorItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Pabilo8
 * @since 13.09.2020
 *
 * Based on work on Immersive Energy's armor
 * in cooperation with:
 * @author Kuruma
 */
public abstract class ItemIIUpgradeableArmor extends ItemArmor implements IUpgradeableTool
{
	static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	String upgradeType;

	public ItemIIUpgradeableArmor(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, String upgradeType)
	{
		super(materialIn, -1, equipmentSlotIn);
		this.upgradeType = upgradeType;

		String name = (materialIn.toString()+"_"+getNameForPart(equipmentSlotIn)).replace(ImmersiveIntelligence.MODID+":", "");
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(ImmersiveIntelligence.creativeTab);
		this.setMaxStackSize(1);
		IIContent.ITEMS.add(this);
		//MinecraftForge.EVENT_BUS.register(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{

	}

	protected String getNameForPart(EntityEquipmentSlot equipmentSlotIn)
	{
		switch(equipmentSlotIn)
		{
			case HEAD:
				return "helmet";
			case CHEST:
				return "chestplate";
			case LEGS:
				return "leggings";
			default:
			case FEET:
				return "boots";
		}
	}

	@Override
	public NBTTagCompound getUpgrades(ItemStack stack)
	{
		return ItemNBTHelper.getTagCompound(stack, "upgrades");
	}

	@Override
	public void clearUpgrades(ItemStack stack)
	{
		ItemNBTHelper.remove(stack, "upgrades");
	}

	@Override
	public void finishUpgradeRecalculation(ItemStack stack)
	{
	}

	@Override
	public void recalculateUpgrades(ItemStack stack)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		clearUpgrades(stack);
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		NBTTagCompound upgradeTag = getUpgradeBase(stack).copy();
		if(inv!=null)
		{
			for(int i = 0; i < inv.getSlots(); i++)
			{
				ItemStack u = inv.getStackInSlot(i);
				if(!u.isEmpty()&&u.getItem() instanceof IUpgrade)
				{
					IUpgrade upg = (IUpgrade)u.getItem();
					if(upg.getUpgradeTypes(u).contains(upgradeType)&&upg.canApplyUpgrades(stack, u))
						upg.applyUpgrades(stack, u, upgradeTag);
				}
			}
			ItemNBTHelper.setTagCompound(stack, "upgrades", upgradeTag);
			finishUpgradeRecalculation(stack);
		}
	}

	public NBTTagCompound getUpgradeBase(ItemStack stack)
	{
		return new NBTTagCompound();
	}

	@Override
	public boolean canTakeFromWorkbench(ItemStack stack)
	{
		return true;
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
	}

	@Override
	public boolean canModify(ItemStack stack)
	{
		return true;
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		List<Upgrades> upgrades = new ArrayList<>();
		for(int i = 0; i < getSlotCount(); i += 1)
			upgrades.add(new IESlot.Upgrades(container, inv, i, 80+(i*20), 32, upgradeType, stack, true));

		return upgrades.toArray(new Upgrades[]{});
	}

	public int getSlotCount()
	{
		return 2;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IIArmorItemStackHandler(stack);
		return null;
	}

	public void setContainedItems(ItemStack stack, NonNullList<ItemStack> inventory)
	{
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(handler instanceof IItemHandlerModifiable)
		{
			if(inventory.size()!=handler.getSlots())
				throw new IllegalArgumentException("Parameter inventory has "+inventory.size()+" slots, capability inventory has "+handler.getSlots());
			for(int i = 0; i < handler.getSlots(); i++)
				((IItemHandlerModifiable)handler).setStackInSlot(i, inventory.get(i));
		}
		else
			IELogger.warn("No valid inventory handler found for "+stack);
	}

	public NonNullList<ItemStack> getContainedItems(ItemStack stack)
	{
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(handler instanceof IIArmorItemStackHandler)
			return ((IIArmorItemStackHandler)handler).getContainedItems();
		else if(handler!=null)
		{
			IELogger.warn("Inefficiently getting contained items. Why does "+stack+" have a non-IE IItemHandler?");
			NonNullList<ItemStack> inv = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
			for(int i = 0; i < handler.getSlots(); i++)
				inv.set(i, handler.getStackInSlot(i));
			return inv;
		}
		else
			IELogger.info("No valid inventory handler found for "+stack);
		return NonNullList.create();
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update
	 * it's contents.
	 */
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if(ItemNBTHelper.hasKey(stack, "Inv"))
		{
			NBTTagList list = ItemNBTHelper.getTag(stack).getTagList("Inv", 10);
			setContainedItems(stack, Utils.readInventory(list, getSlotCount()));
			ItemNBTHelper.remove(stack, "Inv");
			//Sync the changes
			if(entityIn instanceof EntityPlayerMP&&!worldIn.isRemote)
				((EntityPlayerMP)entityIn).connection.sendPacket(new SPacketSetSlot(-2, itemSlot, stack));
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if(equipmentSlot==this.armorType)
		{
			NBTTagCompound nbt = getUpgrades(stack);

			multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", damageReduceAmount+nbt.getInteger("armor_increase"), 0));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Toughness modifier", toughness, 0));

		}
		return multimap;
	}

	//Armor should use a model instead of layered textures
	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return ImmersiveIntelligence.MODID+":textures/armor/empty.png";
	}

	@Override
	public boolean hasOverlay(ItemStack stack)
	{
		return false;
	}
}
