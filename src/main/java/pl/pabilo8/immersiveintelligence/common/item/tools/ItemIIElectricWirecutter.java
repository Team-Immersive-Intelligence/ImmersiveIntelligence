package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.Set;

import static blusunrize.immersiveengineering.api.Lib.TOOL_WIRECUTTER;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.06.2019
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "electric_wirecutter", type = ItemModelType.ITEM_SIMPLE_TOOL, texturePath = "tools/electric_wirecutter")
public class ItemIIElectricWirecutter extends ItemIIElectricTool
{
	public ItemIIElectricWirecutter()
	{
		super("electric_wirecutter", "electric_wirecutter");
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
		ItemStack stack = player.getHeldItem(hand);

		if(!world.isRemote&&tileEntity instanceof IImmersiveConnectable&&hasEnoughEnergy(stack))
		{
			TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
			IImmersiveConnectable nodeHere = (IImmersiveConnectable)tileEntity;

			ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(nodeHere), world, target);
			IESaveData.setDirty(world.provider.getDimension());

			drainEnergy(stack, Tools.electricWirecutterEnergyPerUse, false);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;

	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if(!world.isRemote&&hasEnoughEnergy(stack))
		{
			double reachDistance = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue();
			Connection target = ApiUtils.getTargetConnection(world, player, null, reachDistance);

			if(target!=null)
			{
				ImmersiveNetHandler.INSTANCE.removeConnectionAndDrop(target, world, player.getPosition());
				drainEnergy(stack, Tools.electricWirecutterEnergyPerUse, false);
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return Tools.electricWirecutterCapacity;
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

	@Override
	protected int getEnergyPerUse(ItemStack stack)
	{
		return Tools.electricWirecutterEnergyPerUse;
	}
}
