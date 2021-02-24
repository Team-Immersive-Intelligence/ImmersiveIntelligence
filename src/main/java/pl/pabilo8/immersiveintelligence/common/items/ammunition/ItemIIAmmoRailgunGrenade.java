package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelGrenade;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelRailgunGrenade;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoRailgunGrenade extends ItemIIBulletBase
{
	public ItemIIAmmoRailgunGrenade()
	{
		super("railgun_grenade_4bCal", 8);
	}

	@Override
	public float getComponentCapacity()
	{
		return 0.3f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 0;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getInitialMass()
	{
		return 0.35f;
	}

	@Override
	public float getCaliber()
	{
		return 0.25f;
	}

	@Override
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelRailgunGrenade.class;
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.PIERCING, EnumCoreTypes.PIERCING_FIN_STABILIZED, EnumCoreTypes.SHAPED, EnumCoreTypes.SOFTPOINT, EnumCoreTypes.CANISTER};
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/base");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/core");
		for(EnumCoreTypes coreType : getAllowedCoreTypes())
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/"+getName().toLowerCase()+"/core_"+coreType.getName());
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/paint");
	}

	@Override
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> a = new ArrayList<>();
		if(stack.getMetadata()==BULLET)
		{
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/base"));
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/core_"+getCoreType(stack).getName()));
			if(getPaintColor(stack)!=-1)
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/paint"));

		}
		else if(stack.getMetadata()==CORE)
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/"+NAME.toLowerCase()+"/core"));
		return a;
	}
}
