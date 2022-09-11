package pl.pabilo8.immersiveintelligence.common.item.crafting;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.ISawblade;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIISawBlade.SawBlades;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 19-08-2019
 */
// TODO: 05.09.2022 move to Capabilities
public class ItemIISawBlade extends ItemIISubItemsBase<SawBlades> implements ISawblade
{
	public ItemIISawBlade()
	{
		super("sawblade", 1, SawBlades.values());

		for(SawBlades sawBlade : SawBlades.values())
			SawmillRecipe.registerSawblade(sawBlade.getName(), this);
	}

	@Override
	public void onCreated(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		ItemNBTHelper.setInt(stack, "damage", getSawbladeMaxDamage(stack));
	}

	@Override
	public String getMaterialName(ItemStack stack)
	{
		return stackToSub(stack).getName();
	}

	@Override
	public void damageSawblade(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			ItemNBTHelper.setInt(stack, "damage", getSawbladeMaxDamage(stack));

		ItemNBTHelper.setInt(stack, "damage", getSawbladeDamage(stack)-amount);

		if(getSawbladeDamage(stack) < 0)
			stack.setCount(0);
	}

	@Override
	public int getSawbladeDamage(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "damage"))
			return getSawbladeMaxDamage(stack);
		return ItemNBTHelper.getInt(stack, "damage");
	}

	@Override
	public int getSawbladeMaxDamage(ItemStack stack)
	{
		return stackToSub(stack).durability;
	}

	@Override
	public int getHardness(ItemStack stack)
	{
		return stackToSub(stack).hardness;
	}

	@Override
	public boolean showDurabilityBar(@Nonnull ItemStack stack)
	{
		return getSawbladeDamage(stack)!=getSawbladeMaxDamage(stack);
	}

	@Override
	public double getDurabilityForDisplay(@Nonnull ItemStack stack)
	{
		return 1d-((double)getSawbladeDamage(stack)/(double)getSawbladeDamage(stack));
	}

	@Nonnull
	@Override
	public ItemStack getToolPresentationStack(String toolName)
	{
		Optional<SawBlades> found = Arrays.stream(SawBlades.values())
				.filter(s -> s.getName().equals(toolName))
				.findFirst();
		return getStack(found.orElse(SawBlades.IRON));
	}

	public int getSawbladeDisplayMeta(ItemStack stack)
	{
		return stackToSub(stack).displayMeta;
	}

	public enum SawBlades implements IIItemEnum
	{
		IRON(2, Tools.sawbladeIronDurability, 8),
		STEEL(3, Tools.sawbladeSteelDurability, 9),
		TUNGSTEN(4, Tools.sawbladeTungstenDurability, 10);

		// TODO: 05.09.2022 Replace displayMeta with obj model path
		final int hardness, durability, displayMeta;

		SawBlades(int hardness, int durability, int displayMeta)
		{
			this.hardness = hardness;
			this.durability = durability;
			this.displayMeta = displayMeta;
		}
	}

}
