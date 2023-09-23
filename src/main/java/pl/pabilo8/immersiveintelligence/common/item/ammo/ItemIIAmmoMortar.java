package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBulletMortar6bCal;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_mortar_6bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoMortar extends ItemIIAmmoBase
{
	public ItemIIAmmoMortar()
	{
		super("mortar_6bCal", Casings.MORTAR_6BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.65f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 350;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getInitialMass()
	{
		return 1.125f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Ammunition.mortarVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 6f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Nonnull
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBulletMortar6bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 30;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.PIERCING, EnumCoreTypes.SHAPED, EnumCoreTypes.CANISTER};
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT, EnumFuseTypes.TIMED, EnumFuseTypes.PROXIMITY};
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

	@Override
	public boolean shouldLoadChunks()
	{
		return true;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint");

		for(EnumCoreTypes coreType : getAllowedCoreTypes())
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
						return getCore(stack).getColour();
					case 1:
						return 0xffffffff;
					case 2:
						return getPaintColor(stack);
				}
			}
			case CORE:
				return getCore(stack).getColour();
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
