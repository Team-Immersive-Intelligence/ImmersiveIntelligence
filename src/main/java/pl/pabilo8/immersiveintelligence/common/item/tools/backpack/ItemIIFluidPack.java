package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.InvWrapper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.Backpacks;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Stores and transfers fluid between a wearable canister and held containers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.11.2026
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "fluid_backpack", texturePath = "backpack/fluidpack")
public class ItemIIFluidPack extends ItemIIBackpackBase
{
	public static final String NBT_KEY_OUTPUTS_CONTENTS = "outputs_contents";
	private static final SideConfig[] TRANSFER_MODES = SideConfig.values();

	public ItemIIFluidPack()
	{
		super("fluid_backpack", "fluidpack");
	}

	public SideConfig getOutputsContents(ItemStack stack)
	{
		String name = ItemNBTHelper.getString(stack, NBT_KEY_OUTPUTS_CONTENTS);
		for(SideConfig mode : TRANSFER_MODES)
			if(mode.name().equals(name))
				return mode;
		return SideConfig.NONE;
	}

	public void setOutputsContents(ItemStack stack, SideConfig mode)
	{
		ItemNBTHelper.setString(stack, NBT_KEY_OUTPUTS_CONTENTS, mode.name());
	}

	public void toggleFluidMode(World world, EntityPlayer player, ItemStack stack)
	{
		SideConfig mode = SideConfig.next(getOutputsContents(stack));
		setOutputsContents(stack, mode);
		player.sendStatusMessage(new TextComponentTranslation(IIReference.INFO_KEY+"backpack.fluid_mode."+mode.name().toLowerCase(java.util.Locale.ROOT)), true);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new FluidHandlerItemStack(stack, Backpacks.fluidpackCapacity);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking())
			return super.onItemRightClick(world, player, hand);
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote)
			toggleFluidMode(world, player, stack);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
									  float hitX, float hitY, float hitZ)
	{
		return player.isSneaking()?super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ):
				onItemRightClick(world, player, hand).getType();
	}

	@Override
	public boolean onBackpackAction(EntityPlayer player, WornBackpack worn)
	{
		super.onBackpackAction(player, worn);
		World world = player.world;
		if(!world.isRemote)
			toggleFluidMode(world, player, worn.getStack());
		return true;
	}

	@Override
	public boolean onWornTick(EntityLivingBase wearer, ItemStack stack)
	{
		super.onWornTick(wearer, stack);
		SideConfig mode = getOutputsContents(stack);
		if(wearer.world.isRemote||mode==SideConfig.NONE||wearer.ticksExisted%10!=0)
			return false;
		IFluidHandlerItem tank = FluidUtil.getFluidHandler(stack);
		if(tank==null)
			return false;
		boolean changed = false;
		for(EnumHand hand : EnumHand.values())
		{
			ItemStack held = wearer.getHeldItem(hand);
			if(held.isEmpty()||held.getItem() instanceof ItemIIBackpackBase)
				continue;
			FluidActionResult result;
			if(wearer instanceof EntityPlayer)
			{
				InvWrapper inventory = new InvWrapper(((EntityPlayer)wearer).inventory);
				result = mode==SideConfig.OUTPUT?
						FluidUtil.tryFillContainerAndStow(held, tank, inventory, Backpacks.fluidpackTransferRate, null, true):
						FluidUtil.tryEmptyContainerAndStow(held, tank, inventory, Backpacks.fluidpackTransferRate, null, true);
			}
			else if(held.getCount()==1)
				result = mode==SideConfig.OUTPUT?FluidUtil.tryFillContainer(held, tank, Backpacks.fluidpackTransferRate, null, true):
						FluidUtil.tryEmptyContainer(held, tank, Backpacks.fluidpackTransferRate, null, true);
			else
				continue;
			if(result.isSuccess())
			{
				wearer.setHeldItem(hand, result.getResult());
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean handleWearingPlayerInteraction(ItemStack stack, EntityPlayer wearer, EntityPlayer visitor, EnumHand hand)
	{
		IFluidHandlerItem tank = FluidUtil.getFluidHandler(stack);
		return tank!=null&&(visitor.world.isRemote?FluidUtil.getFluidHandler(visitor.getHeldItem(hand))!=null:
				FluidUtil.interactWithFluidHandler(visitor, hand, tank));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String[] getBackpackOverlayText(ItemStack stack)
	{
		return new String[]{stack.getDisplayName(), IIUtils.getFluidNameOverlayText(FluidUtil.getFluidContained(stack))};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(IIUtils.getFluidNameOverlayText(FluidUtil.getFluidContained(stack)));
		tooltip.add(I18n.format(IIReference.INFO_KEY+"backpack.fluid_mode."+getOutputsContents(stack).name().toLowerCase(java.util.Locale.ROOT)));
	}
}
