package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet1bCal;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_revolver_1bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoRevolver extends ItemBullet implements IAmmoItem<EntityProjectile>, BulletHandler.IBullet
{
	//I hope Blu starts designing things that are extendable, unlike this bullet system
	public static final int UNUSED = 0;
	public static final int CORE = 1;
	public static final int BULLET = 2;
	public final String NAME = "revolver_1bCal";

	public ItemIIAmmoRevolver()
	{
		super();
		this.itemName = "bullet_"+NAME.toLowerCase();
		this.subNames = new String[]{"casing", "core", "bullet"};
		this.setHasSubtypes(true);
		setMetaHidden(0, 1, 2);
		fixupItem();

		//should be initialized before II
		BulletHandler.emptyCasing = new ItemStack(IEContent.itemBullet, 1, 0);
		BulletHandler.emptyShell = new ItemStack(IEContent.itemBullet, 1, 1);
		BulletHandler.basicCartridge = new ItemStack(IEContent.itemBullet, 1, 2);
	}

	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+this.itemName);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);

		//And add it to our registries.
		IIContent.ITEMS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
	{

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
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case BULLET:
				return I18n.format("item.immersiveintelligence."+NAME+".bullet.name");
			case UNUSED:
				return I18n.format("item.immersiveintelligence."+NAME+".casing.name");
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
		switch(stack.getMetadata())
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
		ItemStack stack = new ItemStack(this, 1, BULLET);
		ItemNBTHelper.setString(stack, "core", core);
		ItemNBTHelper.setString(stack, "core_type", coreType);
		ItemNBTHelper.setString(stack, "bullet", "ii_bullet");
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
		ItemStack stack = new ItemStack(this, 1, CORE);
		ItemNBTHelper.setString(stack, "core", core);
		ItemNBTHelper.setString(stack, "core_type", coreType);
		return stack;
	}

	@Override
	public boolean isBulletCore(ItemStack stack)
	{
		return stack.getMetadata()==CORE;
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT};
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
	public ItemStack getCasingStack(int amount)
	{
		return getCasing(null);
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	@Override
	public String getModelCacheKey(ItemStack stack)
	{
		return String.format("%s%s_%s%s", stack.getMetadata()==CORE?"core": "bullet", NAME, getPaintColor(stack)==-1?"no_": "paint_", getCoreType(stack).getName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> a = new ArrayList<>();
		if(stack.getMetadata()==BULLET)
		{
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/base"));
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
			if(getPaintColor(stack)!=-1)
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint"));

		}
		else if(stack.getMetadata()==CORE)
		{
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core"));
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
		}
		return a;
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.125f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 25;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 6;
	}

	@Override
	public float getInitialMass()
	{
		return 0.0625f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Ammunition.revolverVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 1f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull
	Class<? extends IBulletModel> getModel()
	{
		return ModelBullet1bCal.class;
	}

	@Override
	public EntityProjectile getBulletEntity(World world)
	{
		return new EntityProjectile(world);
	}

	@Override
	public float getDamage()
	{
		return 8;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING, EnumCoreTypes.CANISTER};
	}

	//IE, a place where things both work and not at the same time
	@Override
	public boolean isProperCartridge()
	{
		return false;
	}

	@Override
	public boolean isValidForTurret()
	{
		return true;
	}

	@Override
	public Entity getProjectile(@Nullable EntityPlayer shooter, ItemStack cartridge, Entity projectile, boolean charged)
	{
		//TODO: 02.01.2024 use new code
		Vec3d vec = new Vec3d(projectile.motionX, projectile.motionY, projectile.motionZ).normalize();
		Vec3d vv = projectile.getPositionVector();
		EntityBullet e = IIAmmoUtils.createBullet(projectile.world, cartridge, vv, vec);
		if(shooter!=null)
			e.setShooters(shooter);
		return e;
	}

	@Override
	public void onHitTarget(World world, RayTraceResult target, EntityLivingBase shooter, Entity projectile, boolean headshot)
	{

	}

	@Override
	public ItemStack getCasing(ItemStack stack)
	{
		return BulletHandler.emptyCasing.copy();
	}

	@Override
	public ResourceLocation[] getTextures()
	{
		return new ResourceLocation[0];
	}

	@Override
	public int getColour(ItemStack stack, int layer)
	{
		return 0;
	}
}
