package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints.PaintStyleConstraint;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

import java.util.Collections;

public class MultiblockEmplacement extends MultiblockStuctureBase<TileEntityEmplacement>
{
	public static MultiblockEmplacement INSTANCE;
	public static StyleConstraints STYLE_CONSTRAINTS;
	public static ResLoc animationPlatform;
	public final IISoundAnimation openingSoundAnimation;
	public final IISoundAnimation closingSoundAnimation;

	public MultiblockEmplacement()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/emplacement"));
		offset = new Vec3i(1, 4, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.DATA, "data");
		addPOI(MultiblockPOI.MISC_WEAPON, "weapon");
		addPOI(MultiblockPOI.MISC_HATCH, "hatch");

//		addPOI(MultiblockPOI.ITEM_INPUT, "input");
//		addPOI(MultiblockPOI.FLUID_INPUT, "input");

//		addPOI(MultiblockPOI.ITEM_OUTPUT, "output");
//		addPOI(MultiblockPOI.FLUID_OUTPUT, "output");

		//Customzation
		STYLE_CONSTRAINTS = new StyleConstraints("sandbags", PaintStyleConstraint.NOT_APPLICABLE,
				Sets.newHashSet("sandbags", "wooden", "steel", "bricks", "concrete"),
				Collections.emptySet()
		);

		//Tactile AMT
		animationPlatform = ResLoc.of(IIReference.RES_II, "emplacement/open");

		//Sound timing follows the moving ranges in emplacement/open.json.
		double firstTick = 1d/Math.max(1, Emplacement.lidTime);
		openingSoundAnimation = new IISoundAnimation(1d)
				.withRepeatedSound(0.03241, 0.19908, IISounds.slidingDoorOpenLoop)
				.withRepeatedSound(firstTick, 0.61111, IISounds.platformRaiseLoop)
				.withRepeatedSound(0.62500, 0.75000, IISounds.slidingDoorCloseLoop)
				.withRepeatedSound(0.70370, 0.86111, IISounds.platformLowerLoop)
				.compile(Emplacement.lidTime);
		closingSoundAnimation = new IISoundAnimation(1d)
				//Reverse traversal of the same tactile keyframes.
				.withRepeatedSound(0.13889, 0.29630, IISounds.platformRaiseLoop)
				.withRepeatedSound(0.25000, 0.37500, IISounds.slidingDoorOpenLoop)
				.withRepeatedSound(0.38889, 1.00000, IISounds.platformLowerLoop)
				.withRepeatedSound(0.80092, 0.96759, IISounds.slidingDoorCloseLoop)
				.compile(Emplacement.lidTime);

		//Upgrades
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
