package pl.pabilo8.immersiveintelligence.common.item.crafting;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIISawBlade.SawBlades;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.08.2019
 */
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIISawBlade extends ItemIISubItemsBase<SawBlades> implements ISawblade
{
	private final static String NBT_DAMAGE = "damage";

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
		ItemNBTHelper.setInt(stack, NBT_DAMAGE, getToolMaxDamage(stack));
	}

	@Override
	@ParametersAreNonnullByDefault
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format(IIReference.INFO_KEY_TOOL_DURABILITY, TextFormatting.GOLD.toString()+getToolDamage(stack)+TextFormatting.GRAY, TextFormatting.GOLD.toString()+getToolMaxDamage(stack)+TextFormatting.GRAY));
	}

	@Override
	public String getToolID(ItemStack stack)
	{
		return stackToSub(stack).getName();
	}

	@Override
	public void damageTool(ItemStack stack, int amount)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_DAMAGE))
			ItemNBTHelper.setInt(stack, NBT_DAMAGE, getToolMaxDamage(stack));

		ItemNBTHelper.setInt(stack, NBT_DAMAGE, getToolDamage(stack)-amount);

		if(getToolDamage(stack) < 0)
			stack.setCount(0);
	}

	@Override
	public int getToolDamage(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_DAMAGE))
			return getToolMaxDamage(stack);
		return ItemNBTHelper.getInt(stack, NBT_DAMAGE);
	}

	@Override
	public int getToolMaxDamage(ItemStack stack)
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
		return getToolDamage(stack)!=getToolMaxDamage(stack);
	}

	@Override
	public double getDurabilityForDisplay(@Nonnull ItemStack stack)
	{
		return 1d-((double)getToolDamage(stack)/(double)getToolDamage(stack));
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

	@Override
	public ResourceLocation getSawbladeTexture(ItemStack stack)
	{
		return stackToSub(stack).texture;
	}

	@GeneratedItemModels(itemName = "sawblade")
	public enum SawBlades implements IIItemEnum
	{
		IRON(2, Tools.sawbladeIronDurability),
		STEEL(3, Tools.sawbladeSteelDurability),
		TUNGSTEN(4, Tools.sawbladeTungstenDurability);

		final int hardness, durability;
		final ResourceLocation texture;

		SawBlades(int hardness, int durability)
		{
			this.hardness = hardness;
			this.durability = durability;
			this.texture = ResLoc.of(IIReference.RES_II, "blocks/multiblock/sawmill/"+getName());
		}
	}

}
