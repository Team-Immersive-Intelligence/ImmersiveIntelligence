package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.RotationUtil;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IWrench;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 2019-05-30
 */
public class ItemIIElectricWrench extends ItemIIBase implements ITool, IIEEnergyItem, IWrench
{
	public ItemIIElectricWrench()
	{
		super("electric_wrench", 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(IIUtils.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"wrench")));
		list.add(IIUtils.getItalicString(I18n.format(IIReference.INFO_KEY+"charge_with_if")));
		list.add(I18n.format(Lib.DESC+"info.energyStored", TextFormatting.GOLD+stored+TextFormatting.RESET));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
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


	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(world.getTileEntity(pos) instanceof IUpgradableMachine)
		{
			IUpgradableMachine te = ((IUpgradableMachine)world.getTileEntity(pos)).getUpgradeMaster();
			if(te!=null&&te.getCurrentlyInstalled()!=null)
			{
				te.addUpgradeInstallProgress(player.isCreative()?999999: Tools.electricWrenchUpgradeProgress);
				if(te.getInstallProgress() >= te.getCurrentlyInstalled().getProgressRequired())
				{
					if(te.addUpgrade(te.getCurrentlyInstalled(), false))
						te.resetInstallProgress();
				}
				damageWrench(player.getHeldItem(hand), player);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
	{
		if(getToolClasses(stack).contains(toolClass)&&hasEnoughEnergy(stack))
			return 4;
		else
			return -1;
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
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return !player.world.isRemote&&RotationUtil.rotateEntity(entity, player);
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
		{
			for(String type : this.getToolClasses(stack))
				if(state.getBlock().isToolEffective(type, state))
					return 16;
		}
		return super.getDestroySpeed(stack, state);
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return Tools.electricWrenchCapacity;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		stack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(Tools.electricWirecutterEnergyPerUse, false);
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of(IIReference.TOOL_WRENCH, IIReference.TOOL_ADVANCED_WRENCH);
	}

	@Override
	public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack)
	{
		if(hasEnoughEnergy(stack))
		{
			if(state.getBlock().isToolEffective(IIReference.TOOL_WRENCH, state))
				return true;
			else return state.getBlock().isToolEffective(IIReference.TOOL_ADVANCED_WRENCH, state);
		}
		return false;
	}

	public boolean hasEnoughEnergy(ItemStack stack)
	{
		return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() >= Tools.electricWirecutterEnergyPerUse;
	}

	@Override
	public boolean canBeUsed(ItemStack stack)
	{
		return hasEnoughEnergy(stack);
	}

	@Override
	public void damageWrench(ItemStack stack, EntityPlayer player)
	{
		extractEnergy(stack, Tools.electricWrenchEnergyPerUse, false);
	}
}
