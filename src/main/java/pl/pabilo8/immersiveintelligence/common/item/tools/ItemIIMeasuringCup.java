package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.util.IEItemFluidHandler;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 13-07-2019
 */
public class ItemIIMeasuringCup extends ItemIIBase implements ITool, IAdvancedFluidItem, IItemScrollable
{

	public ItemIIMeasuringCup()
	{
		super("measuring_cup", 1);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		list.add(I18n.format(IIReference.DESCRIPTION_KEY+"measuring_cup_size", getCapacity(stack, Tools.measuringCupCapacity), Tools.measuringCupCapacity));
		FluidStack fs = FluidUtil.getFluidContained(stack);
		if(fs!=null)
		{
			TextFormatting rarity = fs.getFluid().getRarity()==EnumRarity.COMMON?TextFormatting.GRAY: fs.getFluid().getRarity().getColor();
			list.add(rarity+fs.getLocalizedName()+TextFormatting.GRAY+": "+fs.amount+"mB");
		}
		else
			list.add(I18n.format(Lib.DESC_FLAVOUR+"drill.empty"));
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, "measuringCupDrain")||FluidUtil.getFluidContained(stack)!=null;
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		ItemNBTHelper.setInt(stack, "maxCap", getCapacity(stack, Tools.measuringCupCapacity));
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "measuringCupDrain"))
		{
			ItemStack ret = stack.copy();
			IFluidHandler handler = FluidUtil.getFluidHandler(ret);
			handler.drain(ItemNBTHelper.getInt(ret, "measuringCupDrain"), true);
			ItemNBTHelper.remove(ret, "measuringCupDrain");
			return ret;
		}
		else if(FluidUtil.getFluidContained(stack)!=null)
		{
			ItemStack ret = stack.copy();
			IFluidHandler handler = FluidUtil.getFluidHandler(ret);
			handler.drain(getCapacity(stack, Tools.measuringCupCapacity), true);
			return ret;
		}
		return stack;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IEItemFluidHandler(stack, Tools.measuringCupCapacity);
		return null;
	}

	@Override
	public boolean isTool(ItemStack item)
	{
		return true;
	}

	@Override
	public int getCapacity(ItemStack stack, int baseCapacity)
	{
		if(ItemNBTHelper.hasKey(stack, "maxCap"))
		{
			return ItemNBTHelper.getInt(stack, "maxCap");
		}
		return Tools.measuringCupCapacity;
	}

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		FluidStack fs = FluidUtil.getFluidContained(stack);
		if(fs==null)
		{
			ItemNBTHelper.setInt(stack, "maxCap", Math.min(Math.max(ItemNBTHelper.getInt(stack, "maxCap")+(forward?10: -10), 10), Tools.measuringCupCapacity));
			SPacketTitle packet = new SPacketTitle(Type.ACTIONBAR, new TextComponentTranslation(IIReference.DESCRIPTION_KEY+"measuring_cup_size", ItemNBTHelper.getInt(stack, "maxCap"), Tools.measuringCupCapacity), 0, 20, 0);
			player.connection.sendPacket(packet);
		}
		else
		{
			SPacketTitle packet = new SPacketTitle(Type.ACTIONBAR, new TextComponentTranslation(IIReference.DESCRIPTION_KEY+"measuring_cup_cant_resize"), 0, 20, 0);
			player.connection.sendPacket(packet);
		}
	}
}
