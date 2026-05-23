package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.util.IEItemFluidHandler;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkinnable;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponType;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.IItemEntityPlacer;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradableTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.11.2019
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIMachinegun extends ItemIIUpgradableTool implements IAdvancedFluidItem, ISkinnable, IItemEntityPlacer<EntityMachinegun>
{
	public ItemIIMachinegun()
	{
		super("machinegun", 1, WeaponType.MACHINEGUN.getName().toUpperCase());
		//Use interfaces pls Blu
		IIItemUtils.fixupItem(this, "machinegun");
	}

	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 3;
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
		String type = WeaponType.MACHINEGUN.getName().toUpperCase();
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 0, 80, 32, type, stack, true),
						new IESlot.Upgrades(container, inv, 1, 100, 32, type, stack, true),
						new IESlot.Upgrades(container, inv, 2, 120, 32, type, stack, true)
				};
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);

		if(getUpgrades(stack).hasKey("second_magazine"))
		{
			if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, IIReference.DESCRIPTION_KEY+"weapon.magazine1", tooltip))
				IIContent.itemBulletMagazine.addInformation(
						ItemNBTHelper.getItemStack(stack, "magazine1")
						, world, tooltip, flag);
			if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, IIReference.DESCRIPTION_KEY+"weapon.magazine2", tooltip))
				IIContent.itemBulletMagazine.addInformation(
						ItemNBTHelper.getItemStack(stack, "magazine2"),
						world, tooltip, flag);
		}
		else
			IIContent.itemBulletMagazine.addInformation(
					ItemNBTHelper.getItemStack(stack, "magazine1"),
					world, tooltip, flag);


		addSkinTooltip(stack, tooltip);
	}

	@Nonnull
	@Override
	public IRarity getForgeRarity(@Nonnull ItemStack stack)
	{
		IRarity skin = getSkinRarity(stack);
		return skin!=null?skin: super.getForgeRarity(stack);
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		if(hasIIUpgrades(stack, WeaponUpgrade.HEAVY_BARREL, WeaponUpgrade.SECOND_MAGAZINE, WeaponUpgrade.INFRARED_SCOPE))
			IIUtils.unlockIIAdvancement(player, "main/let_me_show_you_its_features");
		if(hasIIUpgrades(stack, WeaponUpgrade.BELT_FED_LOADER, WeaponUpgrade.SHIELD, WeaponUpgrade.WATER_COOLING))
			IIUtils.unlockIIAdvancement(player, "main/hans_9000");
	}

	//--- IItemEntityPlacer ---//


	@Nonnull
	@Override
	public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand,
									  @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return IItemEntityPlacer.super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getPlacedSpace(BlockPos pos)
	{
		return new AxisAlignedBB(pos);
	}

	@Nonnull
	@Override
	public EntityMachinegun getPlacedEntity(World world, double x, double y, double z, ItemStack stack, float playerYaw, float playerPitch)
	{
		return new EntityMachinegun(world, new BlockPos(x, y, z), playerYaw, stack);
	}

	//--- IAdvancedFluidItem ---//

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IEItemStackHandler(stack)
			{
				final IEItemFluidHandler fluids = new IEItemFluidHandler(stack, 0);

				@Override
				public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
				{
					return capability==CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY||
							super.hasCapability(capability, facing);
				}

				@Override
				public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
				{
					if(capability==CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
						return (T)fluids;
					return super.getCapability(capability, facing);
				}
			};
		return null;
	}

	@Override
	public int getCapacity(ItemStack stack, int baseCapacity)
	{
		return hasIIUpgrade(stack, WeaponUpgrade.WATER_COOLING)?Machinegun.waterCoolingTankCapacity: 0;
	}

	@Override
	public boolean allowFluid(ItemStack stack, FluidStack fluid)
	{
		return hasIIUpgrade(stack, WeaponUpgrade.WATER_COOLING)&&MachinegunCoolantHandler.isValidCoolant(fluid);
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(@Nonnull ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	//--- ISkinnable ---//

	@Override
	public String getSkinnableName()
	{
		return "machinegun";
	}

	@Override
	public String getSkinnableDefaultTextureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/items/weapons/";
	}
}
