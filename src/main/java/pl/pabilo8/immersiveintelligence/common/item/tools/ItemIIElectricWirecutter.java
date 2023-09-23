package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
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
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static blusunrize.immersiveengineering.api.Lib.TOOL_WIRECUTTER;

/**
 * @author Pabilo8
 * @since 07-06-2019
 */
public class ItemIIElectricWirecutter extends ItemIIBase implements ITool, IIEEnergyItem
{
	public ItemIIElectricWirecutter()
	{
		super("electric_wirecutter", 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(IIUtils.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"electric_wirecutter")));
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

		return EnumActionResult.PASS;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if(!world.isRemote&&tileEntity instanceof IImmersiveConnectable&&hasEnoughEnergy(player.getHeldItem(hand)))
		{

			TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
			BlockPos masterPos = ((IImmersiveConnectable)tileEntity).getConnectionMaster(null, target);
			IImmersiveConnectable nodeHere = (IImmersiveConnectable)tileEntity;

			boolean cut = ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(nodeHere), world, target);
			IESaveData.setDirty(world.provider.getDimension());

			this.extractEnergy(player.getHeldItem(hand), Tools.electricWirecutterEnergyPerUse, false);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;

	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if(!world.isRemote)
		{
			double reachDistance = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue();
			Connection target = ApiUtils.getTargetConnection(world, player, null, reachDistance);

			if(target!=null)
				ImmersiveNetHandler.INSTANCE.removeConnectionAndDrop(target, world, player.getPosition());
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return false;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
	{
		if(hasEnoughEnergy(stack))
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
		return Tools.electricWirecutterCapacity;
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
		return ImmutableSet.of(Lib.TOOL_WIRECUTTER);
	}

	@Override
	public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack)
	{
		if(hasEnoughEnergy(stack))
		{
			if(state.getBlock() instanceof BlockIEBase)
			{
				return ((BlockIEBase<?>)state.getBlock()).allowWirecutterHarvest(state);
			}
			else return state.getBlock().isToolEffective(TOOL_WIRECUTTER, state);
		}
		return false;
	}

	public boolean hasEnoughEnergy(ItemStack stack)
	{
		return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() >= Tools.electricWirecutterEnergyPerUse;
	}
}
