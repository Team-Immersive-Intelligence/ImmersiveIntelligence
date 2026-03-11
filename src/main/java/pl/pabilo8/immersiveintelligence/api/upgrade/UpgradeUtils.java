package pl.pabilo8.immersiveintelligence.api.upgrade;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2025
 */
public class UpgradeUtils
{
	public static float getMaxClientProgress(float current, Upgrade upgrade)
	{
		if(upgrade==null)
			return 0;
		return current-(current%(upgrade.getProgressRequired()/(float)upgrade.getProgressStages()));
	}

	@Nullable
	public static IUpgradableDevice getUpgradeMaster(World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(!(te instanceof IUpgradableDevice))
			return null;
		return ((IUpgradableDevice)te).master();
	}

	/**
	 * The purpose of an upgrade, used for categorization and limiting certain upgrades to certain slots.
	 */
	public enum UpgradePurpose implements ILocalizedEnum
	{
		SPEED,
		EFFICIENCY,
		CAPACITY,
		QUALITY,
		DATA,
		FULL_CONVERSION,
		PRIMARY_WEAPON,
		SECONDARY_WEAPON,
		ARMOR,
		DEFENSE_SYSTEM,
		SPECIAL;

		@Override
		public String geLocaleKey()
		{
			return IIReference.INFO_KEY+"machineupgrade.purpose.";
		}
	}

	/**
	 * The tier of the upgrade inside of a tech tree, used for display organisation.
	 */
	public enum UpgradeTier implements ILocalizedEnum
	{
		CORE,
		TIER_1,
		TIER_2,
		TIER_3,
		TIER_4;

		@Override
		public String geLocaleKey()
		{
			return IIReference.INFO_KEY+"machineupgrade.tier.";
		}
	}

	/**
	 * The tier of the device, purely cosmetic, changes the style of the upgrade GUI.
	 */
	public enum MachineStyle
	{
		WOODEN,
		STEEL,
		BRICKS,
		CONCRETE,
		SANDBAGS
	}

	public enum UpgradeOperation
	{
		PROBE,
		INSTALL,
		FORCE_ADD
	}
}
