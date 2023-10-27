package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleExplosion;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet6bCal;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_artillery_6bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoLightArtillery extends ItemIIAmmoBase
{
	public ItemIIAmmoLightArtillery()
	{
		super("artillery_6bCal", Casings.LIGHT_ARTILLERY_6BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.65f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 450;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getInitialMass()
	{
		return 0.85f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Ammunition.lightHowiVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 6f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelBullet6bCal.class;
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

	@SideOnly(Side.CLIENT)
	public void doPuff(EntityBullet bullet)
	{
		for(int i = 0; i < 20; i += 1)
		{
			Vec3d v = bullet.getBaseMotion().rotatePitch(-90f).rotateYaw(i/20f*360f);
			ParticleExplosion particle = new ParticleExplosion(ClientUtils.mc().world, bullet.getPositionVector(), v.scale(0.125), 3.25f);
			ParticleUtils.particleRenderer.addEffect(particle);
		}
	}
}
