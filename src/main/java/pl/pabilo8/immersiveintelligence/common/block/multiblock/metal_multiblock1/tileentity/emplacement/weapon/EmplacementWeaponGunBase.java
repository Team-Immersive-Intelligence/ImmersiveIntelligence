package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 04.09.2025
 */
public abstract class EmplacementWeaponGunBase<A extends EntityAmmoBase<A>> extends EmplacementWeapon
{
	/**
	 * Used to fire ammo for the weapon
	 */
	protected AmmoFactory<A> ammoFactory;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		//Default action, in most cases sending "I'm attacking xyz" signals is unnecessary
		//Exception is the IR Observer, which overrides this method
		if(firstTime)
		{
			this.ammoFactory = new AmmoFactory<A>(te.getWorld())
					.setIgnoredBlocks(te.getAllBlocks())
					.setOwner(entity);

			if(!te.getWorld().isRemote&&entity!=null)
				ammoFactory.setIgnoredEntities(Arrays.asList(entity.partArray));
		}
	}

}
