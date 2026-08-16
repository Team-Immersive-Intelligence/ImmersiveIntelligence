package pl.pabilo8.immersiveintelligence.api.ammocrate;

import blusunrize.immersiveengineering.common.items.ItemSpeedloader;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

import java.util.function.Predicate;

/**
 * Defines storage rules for one Ammunition Crate mode.
 */
@RequiredArgsConstructor
public enum AmmunitionCrateMode implements ILocalizedEnum
{
	REVOLVER(
			AmmunitionCrateHandler::isRevolverRound,
			AmmunitionCrateHandler::isRevolverCasing,
			stack -> stack.getItem() instanceof ItemSpeedloader,
			true
	),
	SUBMACHINEGUN(
			stack -> AmmunitionCrateHandler.isLooseRound(stack, IIContent.itemAmmoSubmachinegun)
					||AmmunitionCrateHandler.isLiveMagazine(stack, Magazines.SUBMACHINEGUN, Magazines.SUBMACHINEGUN_DRUM),
			AmmunitionCrateHandler.casingPredicate(IIContent.itemAmmoSubmachinegun),
			stack -> AmmunitionCrateHandler.isSpentMagazine(stack, Magazines.SUBMACHINEGUN, Magazines.SUBMACHINEGUN_DRUM),
			true
	),
	ASSAULT_RIFLE(
			stack -> AmmunitionCrateHandler.isLooseRound(stack, IIContent.itemAmmoAssaultRifle)||AmmunitionCrateHandler.isLiveMagazine(stack, Magazines.ASSAULT_RIFLE),
			AmmunitionCrateHandler.casingPredicate(IIContent.itemAmmoAssaultRifle),
			stack -> AmmunitionCrateHandler.isSpentMagazine(stack, Magazines.ASSAULT_RIFLE),
			true
	),
	RIFLE_BULLETS(
			stack -> AmmunitionCrateHandler.isLooseRound(stack, IIContent.itemAmmoMachinegun),
			AmmunitionCrateHandler.casingPredicate(IIContent.itemAmmoMachinegun),
			stack -> false,
			false
	),
	RIFLE_MAGAZINES(
			stack -> AmmunitionCrateHandler.isLooseRound(stack, IIContent.itemAmmoMachinegun)||AmmunitionCrateHandler.isLiveMagazine(stack, Magazines.RIFLE),
			AmmunitionCrateHandler.casingPredicate(IIContent.itemAmmoMachinegun),
			stack -> AmmunitionCrateHandler.isSpentMagazine(stack, Magazines.RIFLE),
			true
	),
	MACHINEGUN_MAGAZINES(
			stack -> AmmunitionCrateHandler.isLooseRound(stack, IIContent.itemAmmoMachinegun)||AmmunitionCrateHandler.isLiveMagazine(stack, Magazines.MACHINEGUN),
			AmmunitionCrateHandler.casingPredicate(IIContent.itemAmmoMachinegun),
			stack -> AmmunitionCrateHandler.isSpentMagazine(stack, Magazines.MACHINEGUN),
			true
	),
	MACHINEGUN_BELT_FED(
			stack -> AmmunitionCrateHandler.isLooseRound(stack, IIContent.itemAmmoMachinegun),
			AmmunitionCrateHandler.casingPredicate(IIContent.itemAmmoMachinegun),
			stack -> false,
			false
	);

	private final Predicate<ItemStack> ammunition;
	private final Predicate<ItemStack> spentCasing;
	private final Predicate<ItemStack> spentMagazine;
	private final boolean spentMagazineSlots;

	/**
	 * @return true if the stack is usable ammunition
	 */
	public boolean isAmmunition(ItemStack stack)
	{
		return ammunition.test(stack);
	}

	/**
	 * @return true if the stack can be used by the Revolver pattern
	 */
	public boolean isPatternAmmunition(ItemStack stack)
	{
		return this==REVOLVER&&AmmunitionCrateHandler.isRevolverRound(stack);
	}

	/**
	 * @return true if the stack is a spent casing
	 */
	public boolean isSpentCasing(ItemStack stack)
	{
		return spentCasing.test(stack);
	}

	/**
	 * @return true if the stack is a spent magazine
	 */
	public boolean isSpentMagazine(ItemStack stack)
	{
		return spentMagazine.test(stack);
	}

	/**
	 * @return true if the mode has separate spent-magazine slots
	 */
	public boolean hasSpentMagazineSlots()
	{
		return spentMagazineSlots;
	}

	@Override
	public String geLocaleKey()
	{
		return IIReference.GUI_LABEL_KEY+"ammunition_crate.mode.";
	}
}
