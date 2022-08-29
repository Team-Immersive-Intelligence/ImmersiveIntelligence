package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Bullets;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet1bCal;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoSubmachinegun extends ItemIIBulletBase
{
	public ItemIIAmmoSubmachinegun()
	{
		super("smg_1bCal", 48);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.0625f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 10;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getInitialMass()
	{
		return 0.0625f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Bullets.smgVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 1f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelBullet1bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 3;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return Utils.getStackWithMetaName(IIContent.itemAmmoCasing,"smg_1bcal",amount);
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING};
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT};
	}
}
