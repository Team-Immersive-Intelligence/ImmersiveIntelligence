package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public class UpgradeEmplacementWeapon<W extends EmplacementWeapon> extends Upgrade
{
	public static final HashMap<String, Supplier<EmplacementWeapon>> WEAPONS = new HashMap<>();
	private final Supplier<W> weaponSupplier;

	public UpgradeEmplacementWeapon(String name, Supplier<W> weaponSupplier)
	{
		super(IIReference.RES_II.with("emplacement/"+name));
		this.weaponSupplier = weaponSupplier;
	}

	public static EmplacementWeapon getWeaponFromName(String weaponName)
	{
		if(weaponName==null)
			return null;
		if(WEAPONS.containsKey(weaponName))
			return WEAPONS.get(weaponName).get();
		return null;
	}
}
