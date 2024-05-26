package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @updated 02.01.2024
 * @ii-approved 0.3.1
 * @since 2019-05-11
 */
public abstract class ItemIIAmmoBase<E extends EntityAmmoBase<? super E>> extends ItemIISubItemsBase<AmmoParts> implements IAmmoTypeItem<ItemIIAmmoBase<E>, E>, IIIItemTextureOverride
{
	public final String NAME;
	@Nullable
	private final Casings casing;

	public ItemIIAmmoBase(String name, @Nullable Casings casing)
	{
		this("bullet_"+name.toLowerCase(), name, casing);
	}

	public ItemIIAmmoBase(String fullName, String name, @Nullable Casings casing)
	{
		super(fullName, casing==null?64: casing.getStackSize(), AmmoParts.values());
		this.NAME = name;
		this.casing = casing;
	}

	public enum AmmoParts implements IIItemEnum
	{
		@IIItemProperties(hidden = true)
		BULLET,
		@IIItemProperties(hidden = true)
		CORE
	}

	public void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE))
			ItemNBTHelper.setString(stack, NBT_CORE, "core_brass");
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE_TYPE))
			ItemNBTHelper.setString(stack, NBT_CORE_TYPE, getAllowedCoreTypes()[0].getName());
		if(stackToSub(stack)==AmmoParts.BULLET&&!ItemNBTHelper.hasKey(stack, NBT_FUSE))
			ItemNBTHelper.setString(stack, NBT_FUSE, getAllowedFuseTypes()[0].getName());
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return casing!=null?IIContent.itemAmmoCasing.getStack(casing, amount): ItemStack.EMPTY;
	}


	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/base");
		for(CoreTypes coreType : getAllowedCoreTypes())
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/"+coreType.getName());
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/paint");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/core");
	}

	@Override
	@ParametersAreNonnullByDefault
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		makeDefault(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nonnull
	public String getItemStackDisplayName(@Nonnull ItemStack stack)
	{
		switch(stackToSub(stack))
		{
			case BULLET:
				return I18n.format("item.immersiveintelligence."+NAME+".bullet.name");
			case CORE:
				return I18n.format("item.immersiveintelligence."+NAME+".core.name");
		}
		return "DO NOT USE, MAY CRASH";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemColours()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColourForIEItem(ItemStack stack, int pass)
	{
		switch(stackToSub(stack))
		{
			case BULLET:
			{
				switch(pass)
				{
					case 0:
						return 0xffffffff;
					case 1:
						return getCore(stack).getColour();
					case 2:
						return getPaintColor(stack);
				}
			}
			case CORE:
				return getCore(stack).getColour();
		}
		return 0xffffffff;
	}

	public ItemStack getBulletWithParams(String core, String coreType, String... components)
	{
		ItemStack stack = getStack(AmmoParts.BULLET);
		ItemNBTHelper.setString(stack, NBT_CORE, core);
		ItemNBTHelper.setString(stack, NBT_CORE_TYPE, coreType);
		NBTTagList tagList = new NBTTagList();
		Arrays.stream(components).map(NBTTagString::new).forEachOrdered(tagList::appendTag);

		if(tagList.tagCount() > 0)
		{
			ItemNBTHelper.getTag(stack).setTag(NBT_COMPONENTS, tagList);
			NBTTagList nbt = new NBTTagList();
			for(int i = 0; i < tagList.tagCount(); i += 1)
				nbt.appendTag(new NBTTagCompound());

			ItemNBTHelper.getTag(stack).setTag(NBT_COMPONENTS_NBT, nbt);
		}

		return stack;
	}

	@Override
	public ItemStack getBulletCore(String core, String coreType)
	{
		ItemStack stack = getStack(AmmoParts.CORE);
		ItemNBTHelper.setString(stack, NBT_CORE, core);
		ItemNBTHelper.setString(stack, NBT_CORE_TYPE, coreType);
		return stack;
	}

	@Override
	public boolean isBulletCore(ItemStack stack)
	{
		return stackToSub(stack)==AmmoParts.CORE;
	}

	@Override
	public String getName()
	{
		return NAME;
	}


	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(@Nonnull ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return String.format("%s%s_%s%s", stackToSub(stack).name(), NAME, getPaintColor(stack)==-1?"no_": "paint_", getCoreType(stack).getName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> a = new ArrayList<>();

		switch(stackToSub(stack))
		{
			case BULLET:
			{
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/base"));
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
				if(getPaintColor(stack)!=-1)
					a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint"));

			}
			break;
			case CORE:
			{
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core"));
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
			}
			break;
		}

		return a;
	}
}
