package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.common.util.RotationUtil;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IWrench;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.05.2019
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "electric_wrench", type = ItemModelType.ITEM_SIMPLE_TOOL, texturePath = "tools/electric_wrench")
public class ItemIIElectricWrench extends ItemIIElectricTool implements IWrench
{
	public ItemIIElectricWrench()
	{
		super("electric_wrench", "wrench");
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		IUpgradableDevice te = UpgradeUtils.getUpgradeMaster(world, pos);
		if(te==null||te.getCurrentUpgrade()==null)
			return EnumActionResult.PASS;

		ItemStack stack = player.getHeldItem(hand);
		boolean free = IIItemUtils.canUpgradeFreeOfCharge(player);
		if(!free&&!hasEnoughEnergy(stack))
			return EnumActionResult.PASS;

		if(te.addUpgradeInstallProgress(free?999999: Tools.electricWrenchUpgradeProgress))
		{
			world.playSound(null, pos, IISounds.constructionElectricWrench, SoundCategory.PLAYERS, 0.5f, 1);
			if(!free)
				damageWrench(stack, player);
		}
		return EnumActionResult.SUCCESS;

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
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return !player.world.isRemote&&RotationUtil.rotateEntity(entity, player);
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return Tools.electricWrenchCapacity;
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
			if(state.getBlock().isToolEffective(IIReference.TOOL_WRENCH, state))
				return true;
			else return state.getBlock().isToolEffective(IIReference.TOOL_ADVANCED_WRENCH, state);
		return false;
	}

	@Override
	protected int getEnergyPerUse(ItemStack stack)
	{
		return Tools.electricWrenchEnergyPerUse;
	}

	@Override
	public boolean canBeUsed(ItemStack stack)
	{
		return hasEnoughEnergy(stack);
	}

	@Override
	public void damageWrench(ItemStack stack, EntityPlayer player)
	{
		drainEnergy(stack, Tools.electricWrenchEnergyPerUse, false);
	}
}
