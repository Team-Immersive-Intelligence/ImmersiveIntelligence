package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IWrench;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 2019-05-30
 */
public class ItemIIWrench extends ItemIIBase implements ITool, IItemDamageableIE, IWrench
{
	public ItemIIWrench()
	{
		super("wrench", 1);
		canRepair = false;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		list.add(IIUtils.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"wrench")));
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack stack)
	{
		ItemStack container = stack.copy();
		this.damageIETool(container, 1, Utils.RAND, null);
		return container;
	}

	private void damageIETool(ItemStack stack, int amount, Random rand, @Nullable EntityPlayer player)
	{
		if(amount <= 0)
			return;

		int unbreakLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
		for(int i = 0; unbreakLevel > 0&&i < amount; i++)
			if(EnchantmentDurability.negateDamage(stack, unbreakLevel, rand))
				amount--;
		if(amount <= 0)
			return;

		int curDamage = ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE);
		curDamage += amount;

		if(player instanceof EntityPlayerMP)
			CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger((EntityPlayerMP)player, stack, curDamage);

		if(curDamage >= Tools.wrenchDurability)
		{
			if(player!=null)
			{
				player.renderBrokenItemStack(stack);
				player.addStat(StatList.getObjectBreakStats(this));
			}
			stack.shrink(1);
			return;
		}
		ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, curDamage);
	}

	@Override
	public boolean isDamageable()
	{
		return true;
	}

	@Override
	public int getMaxDamageIE(ItemStack stack)
	{
		return Tools.wrenchDurability;
	}

	@Override
	public int getItemDamageIE(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE);
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	@Override
	public boolean isEnchantable(@Nonnull ItemStack stack)
	{
		return true;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	@Override
	public int getItemEnchantability()
	{
		return 14;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment==Enchantments.EFFICIENCY||enchantment==Enchantments.UNBREAKING||enchantment==Enchantments.MENDING;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		if(player!=null&&!player.world.isRemote&&!player.capabilities.isCreativeMode)
		{
			IBlockState state = player.world.getBlockState(pos);
			boolean effective = false;
			for(String tool : getToolClasses(itemstack))
				if(state.getBlock().isToolEffective(tool, state))
				{
					effective = true;
					break;
				}
			this.damageIETool(itemstack, effective?1: 2, player.getRNG(), player);
		}
		return false;
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
				te.addUpgradeInstallProgress(player.isCreative()?999999: Tools.wrenchUpgradeProgress);
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

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return false;
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
	{
		if(getToolClasses(stack).contains(toolClass))
			return 2;
		else
			return -1;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE) > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		double max = getMaxDamageIE(stack);
		return ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE)/max;
	}

	@Nonnull
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		int meta = stack.getMetadata();
		return ImmutableSet.of(IIReference.TOOL_WRENCH);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		for(String type : this.getToolClasses(stack))
			if(state.getBlock().isToolEffective(type, state))
				return 6;
		return super.getDestroySpeed(stack, state);
	}

	@Override
	public boolean isTool(ItemStack item)
	{
		return true;
	}

	@Override
	public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack)
	{

		if(state.getBlock().isToolEffective(IIReference.TOOL_WRENCH, state))
			return true;

		return super.canHarvestBlock(state, stack);
	}

	@Override
	public boolean canBeUsed(ItemStack stack)
	{
		return true;
	}

	@Override
	public void damageWrench(ItemStack stack, EntityPlayer player)
	{
		damageIETool(stack, 1, player.getRNG(), player);
	}
}