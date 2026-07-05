package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.EnergyHelper.ItemEnergyStorage;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Common base for II electric tools.
 * <p>
 * The item damage/meta is intentionally not used as durability. Instead, it is kept as a tiny
 * recipe discriminator for {@link IngredientStack},
 * since that path compares item id and metadata, but not the tool's energy NBT.
 * </p>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.05.2019
 */
public abstract class ItemIIElectricTool extends ItemIIBase implements ITool, IIEEnergyItem
{
	/**
	 * Metadata rejected by recipes for a tool without enough energy for another crafting use.
	 */
	public static final int META_CRAFTING_EMPTY = 0;
	/**
	 * Metadata accepted by recipes for a powered tool.
	 */
	public static final int META_CRAFTING_USABLE = 1;
	private final String descriptionKey;

	protected ItemIIElectricTool(String name, String descriptionKey)
	{
		super(name, 1);
		this.descriptionKey = descriptionKey;

		//Meta is used as an invisible crafting-state discriminator, not as a family of real sub-items.
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+descriptionKey)));
		list.add(IIStringUtil.getItalicString(I18n.format(IIReference.INFO_KEY+"charge_with_if")));
		list.add(I18n.format(Lib.DESC+"info.energyStored", TextFormatting.GOLD+stored+TextFormatting.RESET));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(stack.isEmpty())
			return null;

		return new IEItemStackHandler(stack)
		{
			final ItemEnergyStorage energyStorage = new ItemEnergyStorage(stack)
			{
				@Override
				public int receiveEnergy(int maxReceive, boolean simulate)
				{
					int result = super.receiveEnergy(maxReceive, simulate);
					if(!simulate)
						setCraftingMeta(stack, getEnergyStored());
					return result;
				}

				@Override
				public int extractEnergy(int maxExtract, boolean simulate)
				{
					int result = super.extractEnergy(maxExtract, simulate);
					if(!simulate)
						setCraftingMeta(stack, getEnergyStored());
					return result;
				}
			};

			@Override
			public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
			{
				return capability==CapabilityEnergy.ENERGY||super.hasCapability(capability, facing);
			}

			@SuppressWarnings("unchecked")
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
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
	{
		return getToolClasses(stack).contains(toolClass)&&hasEnoughEnergy(stack)?4: -1;
	}

	@Override
	public boolean isDamaged(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isTool(ItemStack item)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1f-(this.getEnergyStored(stack)/(float)this.getMaxEnergyStored(stack));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return this.getEnergyStored(stack) < this.getMaxEnergyStored(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return 0xff0000;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		if(hasEnoughEnergy(stack))
			for(String type : this.getToolClasses(stack))
				if(state.getBlock().isToolEffective(type, state))
					return 16;
		return super.getDestroySpeed(stack, state);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		drainEnergy(stack, getEnergyPerUse(stack), false);
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!worldIn.isRemote)
			updateCraftingMeta(stack);
		;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getUnlocalizedName();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		ReflectionHelper.setPrivateValue(EntityLivingBase.class, entityLiving, 40, "ticksSinceLastSwing", "field_184617_aD");
		return true;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if(slot!=EntityEquipmentSlot.MAINHAND&&slot!=EntityEquipmentSlot.OFFHAND)
			return multimap;

		//slot switching / melee attack speed
		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER,
				"Slot equip time", -3.25f, 0));
		return multimap;
	}

	//--- Energy helpers ---//

	public boolean hasEnoughEnergy(ItemStack stack)
	{
		return hasEnergy(stack, getEnergyPerUse(stack));
	}

	protected boolean hasEnergy(ItemStack stack, int amount)
	{
		IEnergyStorage cap = getEnergyStorage(stack);
		return amount <= 0||cap!=null&&cap.getEnergyStored() >= amount;
	}

	protected boolean drainEnergy(ItemStack stack, int amount, boolean simulate)
	{
		IEnergyStorage cap = getEnergyStorage(stack);
		if(amount <= 0)
			return true;
		if(cap==null||cap.extractEnergy(amount, true) < amount)
			return false;
		if(!simulate)
			cap.extractEnergy(amount, false);
		return true;
	}

	@Nullable
	protected IEnergyStorage getEnergyStorage(ItemStack stack)
	{
		if(stack.isEmpty()||!stack.hasCapability(CapabilityEnergy.ENERGY, null))
			return null;
		return stack.getCapability(CapabilityEnergy.ENERGY, null);
	}

	/**
	 * @return the default energy cost for using this tool once in-world and in crafting.
	 */
	protected abstract int getEnergyPerUse(ItemStack stack);

	//--- Container Item ---//

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack stack)
	{
		ItemStack container = stack.copy();
		drainEnergy(container, getEnergyPerUse(container), false);
		updateCraftingMeta(container);
		return container;
	}

	public ItemStack getValidCraftingStack()
	{
		ItemStack stack = getStack(1);
		stack.setItemDamage(META_CRAFTING_USABLE);
		return stack;
	}

	@Override
	public IngredientStack getIngredientStack(int amount)
	{
		ItemStack stack = getValidCraftingStack();
		stack.setCount(amount);
		return new IngredientStack(stack);
	}

	protected void updateCraftingMeta(ItemStack stack)
	{
		IEnergyStorage cap = getEnergyStorage(stack);
		setCraftingMeta(stack, cap!=null&&cap.getEnergyStored() >= getEnergyPerUse(stack));
	}

	private void setCraftingMeta(ItemStack stack, int storedEnergy)
	{
		setCraftingMeta(stack, storedEnergy >= getEnergyPerUse(stack));
	}

	private void setCraftingMeta(ItemStack stack, boolean usable)
	{
		if(stack.isEmpty()||stack.getItem()!=this)
			return;

		int meta = usable?META_CRAFTING_USABLE: META_CRAFTING_EMPTY;
		if(stack.getMetadata()!=meta)
			stack.setItemDamage(meta);
		ItemNBTHelper.setInt(stack, "HideFlags", 2);
	}
}
