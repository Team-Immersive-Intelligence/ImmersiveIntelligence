package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * @author Pabilo8
 * @since 21.04.2021
 */
public class ItemIIArmorUpgrade extends ItemIIBase implements IUpgrade
{
	public ItemIIArmorUpgrade()
	{
		super("armor_upgrade", 1, ArmorUpgrades.parse());
	}

	public enum ArmorUpgrades
	{
		//Protects from gases
		GASMASK(ImmutableSet.of("LIGHT_ENGINEER_HELMET"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> modifications.setBoolean("gasmask", true));

		private ImmutableSet<String> toolset;
		private int stackSize = 1;
		private BiPredicate<ItemStack, ItemStack> applyCheck;
		private BiConsumer<ItemStack, NBTTagCompound> function;

		ArmorUpgrades(ImmutableSet<String> toolset, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, 1, function);
		}

		ArmorUpgrades(ImmutableSet<String> toolset, int stackSize, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, stackSize, null, function);
		}

		ArmorUpgrades(ImmutableSet<String> toolset, int stackSize, BiPredicate<ItemStack, ItemStack> applyCheck, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this.toolset = toolset;
			this.stackSize = stackSize;
			this.applyCheck = applyCheck;
			this.function = function;
		}

		static String[] parse()
		{
			String[] ret = new String[values().length];
			for(int i = 0; i < ret.length; i++)
				ret[i] = values()[i].toString().toLowerCase(Locale.US);
			return ret;
		}

		static ArmorUpgrades get(int meta)
		{
			if(meta >= 0&&meta < values().length)
				return values()[meta];
			return GASMASK;
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		if(stack.getItemDamage() < getSubNames().length)
		{
			String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"toolupgrade."+this.getSubNames()[stack.getItemDamage()]), 200);
			list.addAll(Arrays.asList(flavour));
		}
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return ArmorUpgrades.get(stack.getMetadata()).stackSize;
	}

	@Override
	public Set<String> getUpgradeTypes(ItemStack upgrade)
	{
		return ArmorUpgrades.get(upgrade.getMetadata()).toolset;
	}

	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		BiPredicate<ItemStack, ItemStack> check = ArmorUpgrades.get(upgrade.getMetadata()).applyCheck;
		if(check!=null&&target.getItem() instanceof IUpgradeableTool)
			return check.test(target, upgrade);
		return true;
	}

	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		ArmorUpgrades.get(upgrade.getMetadata()).function.accept(upgrade, modifications);
	}
}
