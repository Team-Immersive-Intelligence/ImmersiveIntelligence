package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nullable;

/**
 * Provides common equipment and chestplate attachment handling for backpacks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.11.2026
 */
public abstract class ItemIIBackpackBase extends ItemArmor implements ISpecialArmor
{
	public static final String NBT_BACKPACK = "II:Backpack";

	protected ItemIIBackpackBase(String name)
	{
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.CHEST);
		setMaxDamage(0);
		setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		setCreativeTab(IIContent.II_CREATIVE_TAB);
		IIContent.ITEMS.add(this);
	}

	/**
	 * Gets the backpack attached to a chestplate.
	 */
	public static ItemStack getAttachedBackpack(ItemStack armor)
	{
		if(armor.isEmpty())
			return ItemStack.EMPTY;
		ItemStack backpack = ItemNBTHelper.getItemStack(armor, NBT_BACKPACK);
		if(backpack.isEmpty())
			backpack = ItemNBTHelper.getItemStack(armor, IIContent.NBT_AdvancedPowerpack);
		return backpack.getItem() instanceof ItemIIBackpackBase?backpack: ItemStack.EMPTY;
	}

	/**
	 * Saves the backpack and removes the old attachment tag.
	 */
	public static void setAttachedBackpack(ItemStack armor, ItemStack backpack)
	{
		ItemNBTHelper.remove(armor, IIContent.NBT_AdvancedPowerpack);
		if(backpack.isEmpty())
			ItemNBTHelper.remove(armor, NBT_BACKPACK);
		else
			ItemNBTHelper.setItemStack(armor, NBT_BACKPACK, backpack);
	}

	@Override
	public final void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		if(!world.isRemote)
			onWornTick(player, stack);
	}

	/**
	 * Updates an equipped backpack on the server. Returns true if its stack changed.
	 */
	public boolean onWornTick(EntityLivingBase wearer, ItemStack stack)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, @Nullable ModelBiped defaultModel)
	{
		IIArmorRenderer<?> model = IIArmorRenderer.get(stack);
		return model==null?defaultModel: model.prepare(stack, defaultModel);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot)
	{
		return ImmutableMultimap.of();
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase wearer, ItemStack stack, DamageSource source, double damage, int slot)
	{
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack stack, int slot)
	{
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase wearer, ItemStack stack, DamageSource source, int damage, int slot)
	{
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}
}
