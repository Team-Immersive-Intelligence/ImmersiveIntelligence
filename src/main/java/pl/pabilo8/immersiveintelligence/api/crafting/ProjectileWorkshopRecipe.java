package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.06.2025
 **/
public class ProjectileWorkshopRecipe extends IIMultiblockRecipe
{
	public IAmmoTypeItem<?, ?> ammo;
	public boolean isFilling;
	public ItemStack effect, ingredient;

	/**
	 * Production recipe
	 *
	 * @param ammo     Ammo to be produced
	 * @param coreType Core type
	 */
	public ProjectileWorkshopRecipe(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType coreType)
	{
		super(core, coreType);
		this.ammo = ammo;
		this.isFilling = false;

		this.ingredient = core.getMaterial().getExampleStack();
		this.effect = ammo.getAmmoCoreStack(core, coreType);

		this.setTimeAndEnergy(
				ProjectileWorkshop.productionTime*ammo.getCaliber(),
				ProjectileWorkshop.productionEnergyUsage*ammo.getCaliber()
		);
	}

	//Filling recipe
	/*public ProjectileWorkshopRecipe(ItemStack inputStack, BulletComponentStack component)
	{
		assert inputStack.getItem() instanceof IAmmoTypeItem;

		this.ammo = (IAmmoTypeItem<?, ?>)inputStack.getItem();
		this.isFilling = true;

		this.totalProcessTime = ProjectileWorkshop.fillingTime;
		this.energyPerTick = ProjectileWorkshop.fillingEnergyUsage;

		this.effect = inputStack.copy();
		this.effect.setCount(1);
		ammo.addComponents(this.effect, component.getComponent(), component.tagCompound);
	}*/

	public ItemStack getEffect()
	{
		return effect;
	}
}
