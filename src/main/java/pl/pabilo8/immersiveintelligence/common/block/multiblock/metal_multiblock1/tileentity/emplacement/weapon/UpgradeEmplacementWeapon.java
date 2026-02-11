package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;

import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public class UpgradeEmplacementWeapon<W extends EmplacementWeapon> extends Upgrade
{
	private final Supplier<W> weaponSupplier;

	public UpgradeEmplacementWeapon(String name, Supplier<W> weaponSupplier)
	{
		super(IIReference.RES_II.with("emplacement/"+name));
		this.weaponSupplier = weaponSupplier;
		this.withType(UpgradePurpose.PRIMARY_WEAPON);

		//Instantianize to register type for synchronization
		NBTSerialisation.registerPolimorphicTypeClass(weaponSupplier.get().getClass());
	}

	public W createWeapon()
	{
		return weaponSupplier.get();
	}
}
