package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 30.12.2025
 * @since 04.09.2025
 */
public abstract class EmplacementWeaponGunBase<A extends EntityAmmoBase<A>> extends EmplacementWeaponTurretBase implements IAimedEmplacementWeapon
{
	/**
	 * Used to fire ammo for the weapon
	 */
	protected AmmoFactory<A> ammoFactory;
	/**
	 * Inventory inside the weapons platform
	 */
	protected NonNullList<ItemStack> inventoryPlatform;
	/**
	 * Inventory inside the multiblock base
	 */
	protected NonNullList<ItemStack> inventoryBase;
	@Nullable
	protected ItemStackHandler inventoryPlatformHandler;
	@Nullable
	protected ItemStackHandler inventoryBaseHandler;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	public void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory = new AmmoFactory<A>(te.getWorld())
				.setIgnoredBlocks(te.getAllBlocks())
				.setShooterAndGun(te.getOwnerIdentity().getFirstResponsibleMember(te.getWorld()), entity);
		if(!te.getWorld().isRemote&&entity!=null)
			ammoFactory.setIgnoredEntities(Arrays.asList(entity.partArray));
	}

	@Nullable
	@Override
	public IItemHandler getBaseItemHandler()
	{
		return inventoryBaseHandler;
	}


	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		aimVector = posTurret.subtract(posTarget).normalize();
		return ammoFactory.getAnglePrediction(posTurret, Vec3d.ZERO, posTarget, motion);
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return false;
	}


	//--- NBT ---//

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		if(nbt.hasKey("inventory_platform"))
		{
			NonNullList<ItemStack> stacks = Utils.readInventory(nbt.getTagList("inventory_platform", 10), inventoryPlatform.size());
			for(int i = 0; i < stacks.size(); i++)
				inventoryPlatform.set(i, stacks.get(i));
		}
		if(nbt.hasKey("inventory_base"))
		{
			NonNullList<ItemStack> stacks = Utils.readInventory(nbt.getTagList("inventory_base", 10), inventoryBase.size());
			for(int i = 0; i < stacks.size(); i++)
				inventoryBase.set(i, stacks.get(i));
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setTag("inventory_platform", Utils.writeInventory(inventoryPlatform));
		nbt.setTag("inventory_base", Utils.writeInventory(inventoryBase));
		return nbt;
	}
}
