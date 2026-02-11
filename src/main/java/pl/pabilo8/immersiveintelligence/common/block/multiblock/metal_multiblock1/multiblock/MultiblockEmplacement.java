package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.*;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import java.util.Collections;

public class MultiblockEmplacement extends MultiblockStuctureBase<TileEntityEmplacement>
{
	public static MultiblockEmplacement INSTANCE;
	public static StyleConstraints STYLE_CONSTRAINTS;

	public MultiblockEmplacement()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/emplacement"));
		offset = new Vec3i(1, 4, 0);
		INSTANCE = this;
		STYLE_CONSTRAINTS = new StyleConstraints("sandbags", false,
				Sets.newHashSet("sandbags", "wooden", "steel", "bricks", "concrete"),
				Collections.emptySet()
		);

		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.reset()
				//Generic Upgrades
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_STURDY_BEARINGS, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_FALLBACK_GRENADES, UpgradeTier.TIER_1)

				//Machinegun
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED, UpgradeTier.TIER_1)
				.withDependency(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN, IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL)
				.withDependency(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN, IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED)
				.withLockOut(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL, IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_BUNKER, UpgradeTier.TIER_2)
				.withDependency(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN, IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_BUNKER)

				//Other Weapons
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_SEARCHLIGHT, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_TESLA, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MORTAR, UpgradeTier.TIER_2)

				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER, UpgradeTier.TIER_3)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_LIGHT_HOWITZER, UpgradeTier.TIER_3)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_MLRS, UpgradeTier.TIER_3)

				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_CPDS, UpgradeTier.TIER_4)
				.withUpgrade(IIContent.UPGRADE_EMPLACEMENT_WEAPON_GUIDED_MISSILE_LAUNCHER, UpgradeTier.TIER_3)

				//Weapons lockout
				.withLockOut(UpgradePurpose.PRIMARY_WEAPON);

		//Register fire mission classes for serialization
		NBTSerialisation.registerPolimorphicTypeClass(EmplacementFireMissionCustom.class);
		NBTSerialisation.registerPolimorphicTypeClass(EmplacementFireMissionEntities.class);
		NBTSerialisation.registerPolimorphicTypeClass(EmplacementFireMissionEntity.class);
		NBTSerialisation.registerPolimorphicTypeClass(EmplacementFireMissionPosition.class);
		NBTSerialisation.registerPolimorphicTypeClass(EmplacementFireMissionShells.class);
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.EMPLACEMENT.getMeta();
	}

	@Override
	protected TileEntityEmplacement getMBInstance()
	{
		return new TileEntityEmplacement();
	}
}
