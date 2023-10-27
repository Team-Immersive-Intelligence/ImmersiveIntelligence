package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.util.IEItemFluidHandler;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradableTool;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIIMachinegun extends ItemIIUpgradableTool implements IAdvancedFluidItem, ISkinnable
{
	public ItemIIMachinegun()
	{
		super("machinegun", 1, "MACHINEGUN");
		//Use interfaces pls Blu
		fixupItem();
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
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 0, 80, 32, "MACHINEGUN", stack, true),
						new IESlot.Upgrades(container, inv, 1, 100, 32, "MACHINEGUN", stack, true),
						new IESlot.Upgrades(container, inv, 2, 120, 32, "MACHINEGUN", stack, true)
				};
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
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

	@Override
	public IRarity getForgeRarity(ItemStack stack)
	{
		IRarity skin = getSkinRarity(stack);
		return skin!=null?skin: super.getForgeRarity(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch+(playerIn.rotationPitch-playerIn.prevRotationPitch);
		float f2 = playerIn.prevRotationYaw+(playerIn.rotationYaw-playerIn.prevRotationYaw);
		double d0 = playerIn.prevPosX+(playerIn.posX-playerIn.prevPosX);
		double d1 = playerIn.prevPosY+(playerIn.posY-playerIn.prevPosY)+(double)playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ+(playerIn.posZ-playerIn.prevPosZ);
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2*0.017453292F-(float)Math.PI);
		float f4 = MathHelper.sin(-f2*0.017453292F-(float)Math.PI);
		float f5 = -MathHelper.cos(-f1*0.017453292F);
		float f6 = MathHelper.sin(-f1*0.017453292F);
		float f7 = f4*f5;
		float f8 = f3*f5;
		double d3 = 5.0D;
		Vec3d vec3d1 = vec3d.addVector((double)f7*5.0D, (double)f6*5.0D, (double)f8*5.0D);
		RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, false);

		if(raytraceresult==null)
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		else
		{
			Vec3d vec3d2 = playerIn.getLook(1.0F);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec3d2.x*5.0D, vec3d2.y*5.0D, vec3d2.z*5.0D).grow(1.0D));

			for(Entity entity : list)
				if(entity.canBeCollidedWith())
				{
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());

					if(axisalignedbb.contains(vec3d))
						flag = true;
				}

			if(flag)
				return new ActionResult<>(EnumActionResult.PASS, itemstack);
			else if(raytraceresult.typeOfHit!=RayTraceResult.Type.BLOCK)
				return new ActionResult<>(EnumActionResult.PASS, itemstack);
			else
			{
				AxisAlignedBB aabb = worldIn.getBlockState(raytraceresult.getBlockPos()).getCollisionBoundingBox(playerIn.world, raytraceresult.getBlockPos());
				EnumFacing facing = EnumFacing.fromAngle(playerIn.rotationYaw);
				float yaw = facing.getHorizontalAngle();
				float pitch = 25f;

				AxisAlignedBB fence;
				switch(facing)
				{
					case SOUTH:
						fence = new AxisAlignedBB(0, 0, 0.5, 1, 1, 1);
						break;
					case WEST:
						fence = new AxisAlignedBB(0, 0, 0, 0.5, 1, 1);
						break;
					case EAST:
						fence = new AxisAlignedBB(0.5, 0, 0, 1, 1, 1);
						break;
					default:
					case NORTH:
						fence = new AxisAlignedBB(0, 0, 0, 1, 1, 0.5);
						break;
				}

				if(aabb==null)
					return new ActionResult<>(EnumActionResult.FAIL, itemstack);

				boolean intersects = IIUtils.isAABBContained(fence, aabb);

				if(!intersects)
					return new ActionResult<>(EnumActionResult.PASS, itemstack);

				EntityMachinegun maschinengewehr = new EntityMachinegun(worldIn, raytraceresult.getBlockPos(), playerIn.getRotationYawHead(), pitch, itemstack.copy());

				if(!worldIn.isRemote)
					worldIn.spawnEntity(maschinengewehr);
				playerIn.startRiding(maschinengewehr);

				itemstack.shrink(1);

				playerIn.addStat(StatList.getObjectUseStats(this));
				return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
			}
		}
	}

	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);

		//And add it to our registries.
		IIContent.ITEMS.add(this);
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		if(hasIIUpgrades(stack, WeaponUpgrades.HEAVY_BARREL, WeaponUpgrades.SECOND_MAGAZINE, WeaponUpgrades.INFRARED_SCOPE))
			IIUtils.unlockIIAdvancement(player, "main/let_me_show_you_its_features");
		if(hasIIUpgrades(stack, WeaponUpgrades.BELT_FED_LOADER, WeaponUpgrades.SHIELD, WeaponUpgrades.WATER_COOLING))
			IIUtils.unlockIIAdvancement(player, "main/hans_9000");
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IEItemStackHandler(stack)
			{
				final IEItemFluidHandler fluids = new IEItemFluidHandler(stack, 0);

				@Override
				public boolean hasCapability(Capability<?> capability, EnumFacing facing)
				{
					return capability==CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY||
							super.hasCapability(capability, facing);
				}

				@Override
				public <T> T getCapability(Capability<T> capability, EnumFacing facing)
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
		return getUpgrades(stack).hasKey("water_cooling")?Machinegun.waterCoolingTankCapacity: 0;
	}

	@Override
	public boolean allowFluid(ItemStack container, FluidStack fluid)
	{
		return getUpgrades(container).hasKey("water_cooling")&&MachinegunCoolantHandler.isValidCoolant(fluid);
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

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
