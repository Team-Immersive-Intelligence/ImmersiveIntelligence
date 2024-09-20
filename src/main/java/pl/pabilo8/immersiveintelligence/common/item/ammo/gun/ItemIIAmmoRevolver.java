package pl.pabilo8.immersiveintelligence.common.item.ammo.gun;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@IIAmmoProjectile
@GeneratedItemModels(itemName = "bullet_revolver_1bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoRevolver extends ItemBullet implements IAmmoTypeItem<ItemIIAmmoRevolver, EntityAmmoProjectile>, BulletHandler.IBullet, IIIItemTextureOverride
{
	//I hope Blu starts designing things that are extendable, unlike this excuse of a bullet system
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
		IIUtils.fixupItem(this, this.itemName);

		//should be initialized before II
		BulletHandler.emptyCasing = new ItemStack(IEContent.itemBullet, 1, 0);
		BulletHandler.emptyShell = new ItemStack(IEContent.itemBullet, 1, 1);
		BulletHandler.basicCartridge = new ItemStack(IEContent.itemBullet, 1, 2);
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
		for(CoreType coreType : getAllowedCoreTypes())
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
						return getCore(stack).getColor().getPackedARGB();
					case 2:
						return getPaintColor(stack).getPackedARGB();
				}
			}
			case CORE:
				return getCore(stack).getColor().getPackedARGB();
		}
		return 0xffffffff;
	}

	@Override
	public boolean isBulletCore(ItemStack stack)
	{
		return stack.getMetadata()==CORE;
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT};
	}

	@Override
	public ItemStack getAmmoStack(AmmoCore core, CoreType coreType, FuseType fuse, AmmoComponent... components)
	{
		ItemStack stack = new ItemStack(this, 1, BULLET);
		EasyNBT.wrapNBT(stack)
				.withString(NBT_CORE, core.getName())
				.withString(NBT_CORE_TYPE, coreType.getName())
				.withString(NBT_FUSE, fuse.getName())
				.withList(NBT_COMPONENTS, c -> new NBTTagString(c.getName()), components)
				.withList(NBT_COMPONENTS_DATA, c -> new NBTTagCompound(), components);

		return stack;
	}

	@Override
	public ItemStack getAmmoCoreStack(AmmoCore core, CoreType coreType)
	{
		ItemStack stack = new ItemStack(this, 1, CORE);
		EasyNBT.wrapNBT(stack)
				.withString(NBT_CORE, core.getName())
				.withString(NBT_CORE_TYPE, coreType.getName());
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
		return String.format("%s%s_%s%s", stack.getMetadata()==CORE?"core": "bullet", NAME,
				getPaintColor(stack)==null?"no_": "paint_", getCoreType(stack).getName());
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
			if(getPaintColor(stack)!=null)
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
	public int getPropellantNeeded()
	{
		return 25;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.SOLID;
	}


	@Override
	public int getCoreMaterialNeeded()
	{
		return 6;
	}

	@Override
	public float getCasingMass()
	{
		return 0.0625f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.revolverVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 1;
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public Function<ItemIIAmmoRevolver, IAmmoModel<ItemIIAmmoRevolver, EntityAmmoProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createProjectileModel;
	}

	@Nonnull
	@Override
	public EntityAmmoProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoProjectile(world);
	}

	@Override
	public float getPenetrationDepth()
	{
		return 2;
	}

	@Override
	public float getDamage()
	{
		return 8;
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.SOFTPOINT, CoreType.PIERCING, CoreType.CANISTER};
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
		return new AmmoFactory<>(projectile.world)
				.setPosition(projectile.getPositionVector())
				.setDirection(IIEntityUtils.getEntityMotion(projectile).normalize())
				.setStack(cartridge)
				.setOwner(shooter)
				.create();
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
