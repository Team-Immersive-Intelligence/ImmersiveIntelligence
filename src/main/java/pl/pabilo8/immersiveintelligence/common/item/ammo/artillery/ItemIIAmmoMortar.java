package pl.pabilo8.immersiveintelligence.common.item.ammo.artillery;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@IIAmmoProjectile(artillery = true)
@GeneratedItemModels(itemName = "bullet_mortar_6bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoMortar extends ItemIIAmmoBase<EntityAmmoArtilleryProjectile>
{
	public ItemIIAmmoMortar()
	{
		super("mortar_6bCal", Casing.MORTAR_6BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.65f;
	}

	@Override
	public int getPropellantNeeded()
	{
		return 350;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.SOLID;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getCasingMass()
	{
		return 1.125f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.mortarVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 6;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public Function<ItemIIAmmoBase<EntityAmmoArtilleryProjectile>,
			IAmmoModel<ItemIIAmmoBase<EntityAmmoArtilleryProjectile>, EntityAmmoArtilleryProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createEncasedProjectileModel;
	}

	@Override
	public float getDamage()
	{
		return 30;
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.PIERCING, CoreType.SHAPED, CoreType.CANISTER};
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT, FuseType.TIMED, FuseType.PROXIMITY};
	}

	@Override
	public float getSupressionRadius()
	{
		return 3;
	}

	@Override
	public int getSuppressionPower()
	{
		return 20;
	}

	@Nonnull
	@Override
	public EntityAmmoArtilleryProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoArtilleryProjectile(world);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint");

		for(CoreType coreType : getAllowedCoreTypes())
		{
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/"+coreType.getName());
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/base_"+coreType.getName());
		}

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
						return getCore(stack).getColor().getPackedARGB();
					case 1:
						return 0xffffffff;
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
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> a = new ArrayList<>();
		switch(stackToSub(stack))
		{
			case BULLET:
			{
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/base_"+getCoreType(stack).getName()));
				if(getPaintColor(stack)!=null)
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

	@Override
	public float getPenetrationDepth()
	{
		return 3;
	}
}
