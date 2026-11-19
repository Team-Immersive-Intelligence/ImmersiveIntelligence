package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;
import pl.pabilo8.immersiveintelligence.common.compat.BaublesHelper;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Provides common equipment and chestplate attachment handling for backpacks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.11.2026
 * @since 04.11.2026
 */
public abstract class ItemIIBackpackBase extends ItemArmor implements ISpecialArmor, IIIItemTextureOverride, IItemScrollable
{
	public static final String NBT_KEY_BACKPACK = "II:Backpack";
	public static final String NBT_KEY_ACCESS = "backpack_access";
	private final String backpackName, textureName;

	protected ItemIIBackpackBase(String name, String textureName)
	{
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.CHEST);
		this.backpackName = name;
		this.textureName = textureName;

		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		IIContent.ITEMS.add(this);
	}

	//--- Getters and Setters ---//

	/**
	 * Gets the backpack attached to a chestplate.
	 */
	public static ItemStack getAttachedBackpack(ItemStack armor)
	{
		if(armor.isEmpty())
			return ItemStack.EMPTY;
		ItemStack backpack = ItemNBTHelper.getItemStack(armor, NBT_KEY_BACKPACK);
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
			ItemNBTHelper.remove(armor, NBT_KEY_BACKPACK);
		else
			ItemNBTHelper.setItemStack(armor, NBT_KEY_BACKPACK, backpack);
	}

	public BackpackAccess getBackpackAccess(ItemStack stack)
	{
		return EasyNBT.wrapNBT(stack).getEnum(NBT_KEY_ACCESS, BackpackAccess.class);
	}

	public void setBackpackAccess(ItemStack stack, BackpackAccess access)
	{
		EasyNBT.wrapNBT(stack).withEnum(NBT_KEY_ACCESS, access);
	}

	//--- Update ---//

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
		if(wearer instanceof EntityPlayer player)
		{
			player.prevChasingPosX = player.chasingPosX = 0;
			player.prevChasingPosY = player.chasingPosY = 0;
			player.prevChasingPosZ = player.chasingPosZ = 0;
		}
		return false;
	}

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		BackpackAccess access = IIUtils.cycleEnum(true, BackpackAccess.class, getBackpackAccess(stack));
		setBackpackAccess(stack, access);
		TextComponentTranslation accessText = new TextComponentTranslation(IIReference.INFO_KEY+"backpack.access."+access.name().toLowerCase(Locale.ROOT));
		accessText.setStyle(accessText.getStyle().setColor(access.color.getTextFormatting()));
		player.sendStatusMessage(new TextComponentTranslation(IIReference.INFO_KEY+"backpack.access", accessText), true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(!player.isSneaking())
			return super.onItemRightClick(world, player, hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
									  float hitX, float hitY, float hitZ)
	{
		if(!player.isSneaking())
			return EnumActionResult.PASS;

		BackpackType type = BackpackType.forItem(this);
		if(type==null)
			return EnumActionResult.PASS;

		BlockIIBackpack block = IIContent.blockBackpack;
		ItemStack stack = player.getHeldItem(hand);
		IBlockState clicked = world.getBlockState(pos);
		BlockPos target = clicked.getBlock().isReplaceable(world, pos)?pos: pos.offset(facing);
		if(!player.canPlayerEdit(target, facing, stack)||!world.mayPlace(block, target, false, facing, player))
			return EnumActionResult.FAIL;

		if(!world.isRemote)
		{
			IBlockState placed = block.getDefaultState().withProperty(block.property, type)
					.withProperty(IEProperties.FACING_HORIZONTAL, player.getHorizontalFacing());
			if(!world.setBlockState(target, placed, 3))
				return EnumActionResult.FAIL;
			block.onIEBlockPlacedBy(world, target, placed, facing, hitX, hitY, hitZ, player, stack);
			world.playSound(null, target, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 1f, 0.8f);
			if(!player.capabilities.isCreativeMode)
				stack.shrink(1);
		}
		return EnumActionResult.SUCCESS;
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

	@Nullable
	public static WornBackpack getWornBackpack(EntityLivingBase wearer)
	{
		WornBackpack chest = getWornBackpack(wearer, 0);
		return chest!=null?chest: getWornBackpack(wearer, 1);
	}

	@Nullable
	public static WornBackpack getWornBackpack(EntityLivingBase wearer, int slot)
	{
		ItemStack equipment = getBackpackEquipment(wearer, slot);
		ItemStack stack = equipment.getItem() instanceof ItemIIBackpackBase?equipment: getAttachedBackpack(equipment);
		return stack.isEmpty()?null: new WornBackpack(wearer, slot, equipment);
	}

	private static ItemStack getBackpackEquipment(EntityLivingBase wearer, int slot)
	{
		if(slot==0)
			return wearer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		return slot==1&&IICompatModule.baubles&&wearer instanceof EntityPlayer?BaublesHelper.getBodyBauble((EntityPlayer)wearer): ItemStack.EMPTY;
	}

	//--- Abstract ---//

	public boolean onBackpackAction(EntityPlayer player, WornBackpack worn)
	{
		return false;
	}

	public abstract boolean handleWearingPlayerInteraction(ItemStack stack, EntityPlayer wearer, EntityPlayer visitor, EnumHand hand);

	@SideOnly(Side.CLIENT)
	public abstract String[] getBackpackOverlayText(ItemStack stack);

	//--- Client-Side ---//


	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		BackpackAccess access = getBackpackAccess(stack);
		tooltip.add(I18n.format(IIReference.INFO_KEY+"backpack.access",
				access.color.getHexCol(I18n.format(IIReference.INFO_KEY+"backpack.access."+access.getName()))
		));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return backpackName+"_"+getBackpackAccess(stack).getName();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> textures = new ArrayList<>();
		//Backpack item texture
		textures.add(IIReference.RES_II.with("items/backpack/"+textureName));
		//Show lock icon for all but public access
		BackpackAccess access = getBackpackAccess(stack);
		if(access!=BackpackAccess.PUBLIC)
			textures.add(IIReference.RES_II.with("items/backpack/lock_"+access.getName()));
		return textures;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerSprites(TextureMap map)
	{
		//Register individual item icons
		ApiUtils.getRegisterSprite(map, IIReference.RES_II.with("items/backpack/"+textureName));
		//Register lock icons
		for(BackpackAccess access : BackpackAccess.values())
			if(access!=BackpackAccess.PUBLIC)
				ApiUtils.getRegisterSprite(map, IIReference.RES_II.with("items/backpack/lock_"+access.getName()));
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

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	/**
	 * Resolves a backpack from its live equipment slot and saves attached stack changes.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 15.11.2026
	 */
	public static final class WornBackpack
	{
		public final EntityLivingBase wearer;
		public final int slot;
		private final ItemStack equipment;

		private WornBackpack(EntityLivingBase wearer, int slot, ItemStack equipment)
		{
			this.wearer = wearer;
			this.slot = slot;
			this.equipment = equipment;
		}

		public boolean isValid()
		{
			return wearer.isEntityAlive()&&getBackpackEquipment(wearer, slot)==equipment;
		}

		public ItemStack getStack()
		{
			if(!isValid())
				return ItemStack.EMPTY;
			return equipment.getItem() instanceof ItemIIBackpackBase?equipment: getAttachedBackpack(equipment);
		}

		public void save(ItemStack stack)
		{
			if(!isValid())
				return;
			if(!(equipment.getItem() instanceof ItemIIBackpackBase))
				setAttachedBackpack(equipment, stack);
			if(wearer instanceof EntityPlayer)
				((EntityPlayer)wearer).inventory.markDirty();
		}
	}
}
