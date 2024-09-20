package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Ammo Type interface, used to define ammo types for guns and other ammo-consuming items
 *
 * @param <T> this
 * @param <E> entity created by this ammo
 * @author Pabilo8
 * @updated 30.12.2023
 * @ii-approved 0.3.1
 * @since 31-07-2019
 */
public interface IAmmoType<T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>>
{
	/**
	 * @return Type Name, ie. revolver_1bCal for a Revolver Cartridge
	 */
	String getName();

	/**
	 * @return The mass of the casing (used to calculate gravity in combination with the components
	 */
	float getCasingMass();

	/**
	 * @return Default speed modifier of the projectile (blocks/tick)
	 * Guns can apply additional velocity by multiplying the result of this method.
	 */
	float getVelocity();

	/**
	 * @return How much damage this ammunition deals on contact (in half-hearts)
	 */
	float getDamage();

	/**
	 * @return Component amount multiplier, can influence explosion size, smoke cloud duration, etc.
	 */
	float getComponentMultiplier();

	/**
	 * @return How many blocks potentially can be penetrated by a projectile of this ammo type
	 */
	default float getPenetrationDepth()
	{
		return 0.5f;
	}

	/**
	 * @return Bullet caliber in Block-Caliber (bCal) = 1/16 of a block
	 */
	int getCaliber();

	/**
	 * @return Returns allowed core types, these affect the final damage, penetration and overall performance
	 * @see CoreType
	 */
	CoreType[] getAllowedCoreTypes();

	/**
	 * @param amount of items to return
	 * @return the casing ItemStack, often requested by guns after firing a bullet
	 */
	ItemStack getCasingStack(int amount);

	/**
	 * @return A bullet model, it's required to extend {@link IAmmoModel},<br>
	 * {@link pl.pabilo8.immersiveintelligence.client.util.amt.AMT} is preferred, but you can use any renderer you like
	 */
	@Nonnull
	@SideOnly(Side.CLIENT)
	Function<T, IAmmoModel<T, E>> get3DModel();

	//--- Suppression ---//

	/**
	 * @return A radius in which living entities will be given the {@link pl.pabilo8.immersiveintelligence.common.IIPotions#suppression} effect, 0 if there is no suppression effect.
	 */
	default float getSupressionRadius()
	{
		return 0;
	}

	/**
	 * @return Effect amplifier added by {@link #getSupressionRadius()}
	 */
	default int getSuppressionPower()
	{
		return 0;
	}

	@Nonnull
	E getAmmoEntity(World world);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return Bullet core from stack NBT
	 */
	AmmoCore getCore(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return bullet core type from stack NBT
	 */
	CoreType getCoreType(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return bullet components in appropriate order from stack NBT
	 */
	AmmoComponent[] getComponents(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack        bullet stack
	 * @param component    added component
	 * @param componentNBT
	 * @return bullet components in appropriate order from stack NBT
	 */
	boolean addComponents(ItemStack stack, AmmoComponent component, NBTTagCompound componentNBT);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return NBT Tags for each bullet component in appropriate order from stack NBT
	 */
	NBTTagCompound[] getComponentsNBT(ItemStack stack);

	/**
	 * @param stack        bullet stack
	 * @param tagCompounds for each bullet component in appropriate order
	 * @return stack with bullet component
	 */
	ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return paint color in RGBint format from stack NBT
	 */
	@Nullable
	IIColor getPaintColor(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @param color to be applied on the bullet stack
	 * @return painted bullet stack
	 */
	ItemStack setPaintColor(ItemStack stack, @Nullable IIColor color);

	double getMass(ItemStack stack);

	double getCoreMass(AmmoCore core, AmmoComponent[] components);

	/**
	 * @param stack
	 * @param type  type of the fuse
	 */
	void setFuseType(ItemStack stack, FuseType type);

	/**
	 * @param stack
	 * @return fuse type used in the bullet
	 */
	FuseType getFuseType(ItemStack stack);

	boolean hasFreeComponentSlots(ItemStack stack);

	int getFuseParameter(ItemStack stack);

	void setFuseParameter(ItemStack stack, int p);
}
