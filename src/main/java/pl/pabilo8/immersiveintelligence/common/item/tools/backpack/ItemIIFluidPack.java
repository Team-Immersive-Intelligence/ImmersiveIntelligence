package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Stores one fluid in a wearable canister.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.11.2026
 * @since 23.01.2021
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "fluid_backpack", texturePath = "backpack/fluidpack")
public class ItemIIFluidPack extends ItemIIBackpackBase
{
	private static final int CAPACITY = 16000;

	public ItemIIFluidPack()
	{
		super("fluid_backpack");
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new FluidHandlerItemStack(stack, CAPACITY);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
									  float hitX, float hitY, float hitZ)
	{
		return FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing)?EnumActionResult.SUCCESS: EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		FluidStack fluid = FluidUtil.getFluidContained(stack);
		String name = fluid==null?I18n.format(IIReference.DESCRIPTION_KEY+"fluid_backpack_empty"): fluid.getLocalizedName();
		tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"fluid_backpack_contents", name, fluid==null?0: fluid.amount, CAPACITY));
	}
}
