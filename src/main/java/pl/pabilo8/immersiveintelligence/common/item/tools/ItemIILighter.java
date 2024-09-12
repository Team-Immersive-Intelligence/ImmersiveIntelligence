package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.LighterFuelHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 2019-05-23
 */
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIILighter extends ItemIIBase implements ITool
{

	public ItemIILighter()
	{
		super("lighter", 1);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		FluidStack fs = FluidUtil.getFluidContained(stack);
		if(fs!=null)
		{
			TextFormatting rarity = fs.getFluid().getRarity()==EnumRarity.COMMON?TextFormatting.GRAY: fs.getFluid().getRarity().getColor();
			list.add(rarity+fs.getLocalizedName()+TextFormatting.GRAY+": "+fs.amount+"/"+Tools.lighterCapacity+"mB");
		}
		else
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drill.empty"));
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		pos = pos.offset(side);
		ItemStack stack = player.getHeldItem(hand);

		IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

		if(!player.canPlayerEdit(pos, side, stack))
		{
			return EnumActionResult.PASS;
		}
		else if(LighterFuelHandler.isValidFuel(FluidUtil.getFluidContained(stack))&&LighterFuelHandler.getBurnQuantity(FluidUtil.getFluidContained(stack)) <= FluidUtil.getFluidContained(stack).amount)
		{
			if(world.isAirBlock(pos))
			{
				world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat()*0.4F+0.8F);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
			}

			if(player instanceof EntityPlayerMP)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
			}

			cap.drain(LighterFuelHandler.getBurnQuantity(FluidUtil.getFluidContained(stack)), true);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "lighterDrain")||FluidUtil.getFluidContained(stack)!=null;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "lighterDrain"))
		{
			ItemStack ret = stack.copy();
			IFluidHandler handler = FluidUtil.getFluidHandler(ret);
			handler.drain(ItemNBTHelper.getInt(ret, "lighterDrain"), true);
			ItemNBTHelper.remove(ret, "lighterDrain");
			return ret;
		}
		else if(FluidUtil.getFluidContained(stack)!=null)
		{
			ItemStack ret = stack.copy();
			IFluidHandler handler = FluidUtil.getFluidHandler(ret);
			handler.drain(Tools.lighterCapacity, true);
			return ret;
		}
		return stack;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		return fluidStack!=null;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		return fluidStack!=null?fluidStack.getFluid().getColor(): 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		return fluidStack!=null?1f-(fluidStack.amount/(float)Tools.lighterCapacity): 0;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new FluidHandlerItemStack(stack, Tools.lighterCapacity);
		return null;
	}

	@Override
	public boolean isTool(ItemStack item)
	{
		return true;
	}
}
