package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.ITextureOverride;
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
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
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
 * @since 2019-05-11
 */
public abstract class ItemIIAmmoBase extends ItemIISubItemsBase<AmmoParts> implements IAmmo, ITextureOverride
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
		if(!ItemNBTHelper.hasKey(stack, "core"))
			ItemNBTHelper.setString(stack, "core", "core_brass");
		if(!ItemNBTHelper.hasKey(stack, "core_type"))
			ItemNBTHelper.setString(stack, "core_type", getAllowedCoreTypes()[0].getName());
		if(stackToSub(stack)==AmmoParts.BULLET&&!ItemNBTHelper.hasKey(stack, "fuse"))
			ItemNBTHelper.setString(stack, "fuse", getAllowedFuseTypes()[0].getName());
	}

	@Override
	public IAmmoCore getCore(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "core"))
			makeDefault(stack);
		return AmmoRegistry.INSTANCE.getCore(ItemNBTHelper.getString(stack, "core"));
	}

	@Override
	public EnumCoreTypes getCoreType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "core_type"))
			makeDefault(stack);
		return EnumCoreTypes.v(ItemNBTHelper.getString(stack, "core_type"));
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return casing!=null?IIContent.itemAmmoCasing.getStack(casing, amount): ItemStack.EMPTY;
	}

	@Override
	public int getPaintColor(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "paint_color"))
			return ItemNBTHelper.getInt(stack, "paint_color");
		return -1;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/base");
		for(EnumCoreTypes coreType : getAllowedCoreTypes())
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/"+coreType.getName());
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/paint");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/core");
	}

	@Override
	public IAmmoComponent[] getComponents(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "components"))
		{
			ArrayList<IAmmoComponent> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)ItemNBTHelper.getTag(stack).getTag("components");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(AmmoRegistry.INSTANCE.getComponent(components.getStringTagAt(i)));
			return arrayList.toArray(new IAmmoComponent[0]);
		}
		return new IAmmoComponent[0];
	}

	@Override
	public NBTTagCompound[] getComponentsNBT(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, "component_nbt"))
		{
			ArrayList<NBTTagCompound> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)ItemNBTHelper.getTag(stack).getTag("component_nbt");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(components.getCompoundTagAt(i));
			return arrayList.toArray(new NBTTagCompound[0]);
		}
		return new NBTTagCompound[0];
	}

	@Override
	public void addComponents(ItemStack stack, IAmmoComponent component, NBTTagCompound componentNBT)
	{
		NBTTagList comps = ItemNBTHelper.getTag(stack).getTagList("components", 8);
		NBTTagList nbts = ItemNBTHelper.getTag(stack).getTagList("component_nbt", 10);

		comps.appendTag(new NBTTagString(component.getName()));
		nbts.appendTag(componentNBT.copy());

		ItemNBTHelper.getTag(stack).setTag("components", comps);
		ItemNBTHelper.getTag(stack).setTag("component_nbt", nbts);
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
		ItemNBTHelper.setString(stack, "core", core);
		ItemNBTHelper.setString(stack, "core_type", coreType);
		NBTTagList tagList = new NBTTagList();
		Arrays.stream(components).map(NBTTagString::new).forEachOrdered(tagList::appendTag);

		if(tagList.tagCount() > 0)
		{
			ItemNBTHelper.getTag(stack).setTag("components", tagList);
			NBTTagList nbt = new NBTTagList();
			for(int i = 0; i < tagList.tagCount(); i += 1)
				nbt.appendTag(new NBTTagCompound());

			ItemNBTHelper.getTag(stack).setTag("component_nbt", nbt);
		}

		return stack;
	}

	@Override
	public ItemStack getBulletCore(String core, String coreType)
	{
		ItemStack stack = getStack(AmmoParts.CORE);
		ItemNBTHelper.setString(stack, "core", core);
		ItemNBTHelper.setString(stack, "core_type", coreType);
		return stack;
	}

	@Override
	public boolean isBulletCore(ItemStack stack)
	{
		return stackToSub(stack)==AmmoParts.CORE;
	}

	@Override
	public ItemStack setPaintColour(ItemStack stack, int color)
	{
		ItemNBTHelper.setInt(stack, "paint_color", color);
		return stack;
	}

	@Override
	public ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds)
	{
		NBTTagList component_nbt = new NBTTagList();
		for(NBTTagCompound tagCompound : tagCompounds)
			component_nbt.appendTag(tagCompound);
		assert stack.getTagCompound()!=null;
		stack.getTagCompound().setTag("component_nbt", component_nbt);
		return stack;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public void setFuseType(ItemStack stack, EnumFuseTypes type)
	{
		ItemNBTHelper.setString(stack, "fuse", type.getName());
	}

	@Override
	public EnumFuseTypes getFuseType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "fuse"))
			makeDefault(stack);
		return EnumFuseTypes.v(ItemNBTHelper.getString(stack, "fuse"));
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
