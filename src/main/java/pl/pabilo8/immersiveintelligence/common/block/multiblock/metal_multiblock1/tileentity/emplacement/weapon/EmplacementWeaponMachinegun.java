package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class EmplacementWeaponMachinegun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	float shootDelay = Machinegun.bulletFireTime;
	int reloadDelay = 0;
	int setupDelay = 0;
	/**
	 * *reloads the gun* [H] E A V Y  M A C H I N E G U N<br>
	 * <s>Rawket Lawnchair!</s>
	 */
	private AxisAlignedBB vision;
	private Vec3d vv;
	private NonNullList<ItemStack> inventory = NonNullList.withSize(36, ItemStack.EMPTY);
	private final IItemHandler inventoryHandler = new ItemStackHandler(inventory)
	{
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			if(stack.getItem()!=IIContent.itemAmmoMachinegun)
				return false;
			return super.isItemValid(slot, stack);
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
		{
			if(!isItemValid(slot, stack))
				return stack;
			ItemStack itemStack = super.insertItem(slot, stack, simulate);
			inventory.set(slot, stacks.get(slot));
			return itemStack;
		}
	};
	private NonNullList<ItemStack> inventoryPlatform = NonNullList.withSize(12, ItemStack.EMPTY);
	private boolean requiresPlatformRefill = false;
	private ArrayDeque<ItemStack> magazine = new ArrayDeque<>();
	private ItemStack s2 = ItemStack.EMPTY;

	public EmplacementWeaponMachinegun()
	{

	}

	@Override
	public String getName()
	{
		return "machinegun";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return Machinegun.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return Machinegun.pitchRotateSpeed;
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		super.shoot(te);

		if(vv==null)
			return;

		if(!te.getWorld().isRemote)
		{
			s2 = !magazine.isEmpty()?magazine.removeFirst(): ItemStack.EMPTY;
			if(!s2.isEmpty())
			{
				IIPacketHandler.playRangedSound(te.getWorld(), new Vec3d(te.getPos()),
						IISounds.machinegunShotHeavyBarrel, SoundCategory.PLAYERS, 75, 1.5f,
						0.8f+(float)(Utils.RAND.nextGaussian()*0.02)
				);
				ammoFactory.setStack(s2)
						.setPosition(te.getWeaponCenter())
						.setDirection(vv.scale(-1))
						.create();
			}
		}
		else if(!magazine.isEmpty())
		{
			Vec3d weaponCenter = te.getWeaponCenter().add(vv.scale(-1.85))
					//.add(vv.rotatePitch(-90).scale(0.125))
					.add(vv.rotateYaw(90).scale(0.25));
			Vec3d vg = vv.scale(3f);
			ParticleRegistry.spawnGunfireFX(weaponCenter, vg, 3f);

			weaponCenter = weaponCenter.add(vv.rotateYaw(90).scale(-0.5));
			ParticleRegistry.spawnGunfireFX(weaponCenter, vg, 3f);

			magazine.removeFirst();
		}

		shootDelay = Machinegun.bulletFireTime;
	}

	public boolean isSetUp(boolean door)
	{
		return setupDelay==(door?Machinegun.setupTime: 0);
	}

	@Override
	public void doSetUp(boolean door)
	{
		if(door)
		{
			if(!isAimedAt(yaw, 0))
			{
				aimAt(yaw, 0);
				return;
			}
			if(setupDelay < Machinegun.setupTime)
				setupDelay += 1;
		}
		else
		{
			if(!isAimedAt(yaw, 0))
			{
				aimAt(yaw, 0);
				return;
			}
			if(setupDelay > 0)
				setupDelay -= 1;
		}
	}

	@Override
	public boolean requiresPlatformRefill()
	{
		return requiresPlatformRefill;
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		vv = posTurret.subtract(posTarget).normalize();
		return IIAmmoUtils.getInterceptionAngles(
				posTurret, Vec3d.ZERO,
				posTarget, motion,
				IIContent.itemAmmoAutocannon.getVelocity(),
				s2.isEmpty()?0: IIContent.itemAmmoAutocannon.getMass(s2)
		);
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		nextPitch = MathHelper.clamp(pitch, -15, 20);
		super.aimAt(yaw, MathHelper.clamp(pitch, -15, 20));
	}

	public void aimAtUnrestricted(float yaw, float pitch)
	{
		super.aimAt(yaw, pitch);
	}

	@Override
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		this.vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(Machinegun.detectionRadius);

	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{
		if(active&&magazine.isEmpty())
		{
			if(reloadDelay==0)
			{
				if(inventoryPlatform.stream().anyMatch(stack -> stack.getItem()==IIContent.itemAmmoMachinegun))
					reloadDelay = 1;
				else
					requiresPlatformRefill = true;
			}
			else
				reloadDelay++;

			if(reloadDelay >= Machinegun.reloadTime)
			{

				for(ItemStack stack : inventoryPlatform)
				{
					if(magazine.size() >= 256) //shouldn't be more, but who knows
						break;

					while(!stack.isEmpty()&&magazine.size() < 256)
					{
						ItemStack copy = stack.copy();
						copy.setCount(1);
						magazine.addLast(copy);
						stack.shrink(1);
					}
				}

				reloadDelay = 0;
				syncWithClient(te);
			}
		}

		if(shootDelay > 0)
			shootDelay--;
	}

	@Nonnull
	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound nbt = super.saveToNBT(forClient);
		nbt.setFloat("shootDelay", shootDelay);
		nbt.setInteger("reloadDelay", reloadDelay);

		nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setTag("inventoryPlatform", Utils.writeInventory(inventoryPlatform));
		if(!forClient)
			nbt.setTag("magazine", Utils.writeInventory(magazine));
		nbt.setInteger("magazine_amount", magazine.size());

		nbt.setBoolean("requiresPlatformRefill", requiresPlatformRefill);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		shootDelay = tagCompound.getFloat("shootDelay");
		reloadDelay = tagCompound.getInteger("reloadDelay");

		inventory = Utils.readInventory(tagCompound.getTagList("inventory", 10), inventory.size());
		inventoryPlatform = Utils.readInventory(tagCompound.getTagList("inventoryPlatform", 10), inventoryPlatform.size());
		magazine = new ArrayDeque<>(Utils.readInventory(tagCompound.getTagList("magazine", 10), tagCompound.getInteger("magazine_amount")));

		requiresPlatformRefill = tagCompound.getBoolean("requiresPlatformRefill");
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return vv!=null&&shootDelay==0&&!magazine.isEmpty();
	}

	@Override
	public AxisAlignedBB getVisionAABB()
	{
		return vision;
	}

	@Override
	public void syncWithEntity(EntityEmplacementWeapon entity)
	{
		super.syncWithEntity(entity);
		if(entity==this.entity)
			entity.aabb = new AxisAlignedBB(-2, 0, -2, 2, 3, 2);
	}

	@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.25f,
				new Vec3d(0, 0.75, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0.75, 0.5, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0.75, 0.5, 0.75), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0.75, 0.5, -0.75), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0, 0.5, 0.75), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(0, 0.5, -0.75), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(-0.75, 0.5, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(-0.75, 0.5, 0.75), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "baseSandbags", 0.75f, 0.75f,
				new Vec3d(-0.75, 0.5, -0.75), Vec3d.ZERO, 4));


		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.45f, 0.45f,
				new Vec3d(-0.5, 1, 0), new Vec3d(-0.625f, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.45f, 0.45f,
				new Vec3d(-0.5, 1, 0), new Vec3d(-1.25, 0, 0), 12));

		return list.toArray(new EmplacementHitboxEntity[0]);
	}

	@Override
	public NonNullList<ItemStack> getBaseInventory()
	{
		return inventory;
	}

	@Override
	public void renderStorageInventory(GuiEmplacementPageStorage gui, int mx, int my, float partialTicks, boolean first)
	{

	}

	@Override
	public void performPlatformRefill(TileEntityEmplacement te)
	{
		for(int i = 0; i < inventoryPlatform.size(); i++)
		{
			//te.doProcessOutput(inventoryPlatform.get(i));
			inventoryPlatform.set(i, ItemStack.EMPTY);
		}
		int moved = 0;
		for(int i = 0; i < inventory.size(); i++)
		{
			if(moved >= inventoryPlatform.size())
				break;

			ItemStack s = inventory.get(i);
			if(!s.isEmpty())
			{
				inventoryPlatform.set(moved, s);
				inventory.set(i, ItemStack.EMPTY);
				moved++;
			}
		}

		if(inventoryPlatform.stream().anyMatch(stack -> !stack.isEmpty()))
		{
			requiresPlatformRefill = false;
			syncWithClient(te);
		}
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Machinegun.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return Machinegun.maxHealth;
	}

	@Nullable
	@Override
	public IItemHandler getItemHandler(boolean in)
	{
		return in?inventoryHandler: super.getItemHandler(in);
	}
}
