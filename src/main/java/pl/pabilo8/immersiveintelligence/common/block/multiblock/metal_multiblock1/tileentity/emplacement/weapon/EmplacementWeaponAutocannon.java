package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class EmplacementWeaponAutocannon extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	private static final int[] reloadTimers = new int[]{
			(int)(0.15*Autocannon.reloadTime), (int)(0.25*Autocannon.reloadTime), (int)(0.35*Autocannon.reloadTime), (int)(0.45*Autocannon.reloadTime),
			(int)(0.55*Autocannon.reloadTime), (int)(0.65*Autocannon.reloadTime), (int)(0.75*Autocannon.reloadTime), (int)(0.85*Autocannon.reloadTime)
	};
	float flaps = 0;
	float shootDelay = 0;
	int reloadDelay = 0;
	int bulletsShot = 0;
	private AxisAlignedBB vision;
	private NonNullList<ItemStack> inventory = NonNullList.withSize(18, ItemStack.EMPTY);
	private final IItemHandler inventoryHandler = new ItemStackHandler(inventory)
	{
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			if(!OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.AUTOCANNON), false))
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
	private NonNullList<ItemStack> inventoryPlatform = NonNullList.withSize(8, ItemStack.EMPTY);
	private int casingsToDrop = 0;
	private boolean requiresPlatformRefill = false;
	private ArrayDeque<ItemStack> magazine = new ArrayDeque<>();
	private ItemStack s2 = ItemStack.EMPTY;
	private Vec3d vv;

	public EmplacementWeaponAutocannon()
	{

	}

	@Override
	public String getName()
	{
		return "autocannon";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return Autocannon.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return Autocannon.pitchRotateSpeed;
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		s2 = magazine.peekFirst();
		if(s2==null)
			s2 = ItemStack.EMPTY;

		vv = posTurret.subtract(posTarget).normalize();

		return IIAmmoUtils.getInterceptionAngles(
				posTurret, Vec3d.ZERO,
				posTarget, motion,
				IIContent.itemAmmoAutocannon.getVelocity(),
				s2.isEmpty()?1: IIContent.itemAmmoAutocannon.getMass(s2)
		);
	}

	@Override
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		this.vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(Autocannon.detectionRadius);

	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{
		if(active&&magazine.isEmpty())
		{
			if(reloadDelay==0)
			{
				if(inventoryPlatform.stream().anyMatch(stack -> OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.AUTOCANNON), false)
						&&IIContent.itemBulletMagazine.getRemainingBulletCount(stack) > 0))
					reloadDelay = 1;
				else
					requiresPlatformRefill = true;
			}
			else
			{
				reloadDelay++;
			}

			if(reloadDelay >= Autocannon.reloadTime)
			{

				int magsLoaded = 0;
				for(ItemStack stack : inventoryPlatform)
				{
					if(magsLoaded >= 4)
						break;
					if(OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.AUTOCANNON), false)
							&&IIContent.itemBulletMagazine.getRemainingBulletCount(stack) > 0)
					{
						magazine.addAll(IIContent.itemBulletMagazine.takeAll(stack));
						magsLoaded++;
					}
				}

				reloadDelay = 0;
				syncWithClient(te);
			}
			else if(te.getWorld().isRemote)
			{
				playSoundsClient(te);
			}
		}

		if(shootDelay > 0)
			shootDelay--;
	}

	@SideOnly(Side.CLIENT)
	private void playSoundsClient(TileEntityEmplacement te)
	{
		if(reloadDelay==reloadTimers[0]||reloadDelay==reloadTimers[1]||reloadDelay==reloadTimers[2]||reloadDelay==reloadTimers[3])
			te.getWorld().playSound(ClientUtils.mc().player, te.getBlockPosForPos(49), IISounds.autocannonUnload, SoundCategory.NEUTRAL, 1, 0.75f);
		if(reloadDelay==reloadTimers[4]||reloadDelay==reloadTimers[5]||reloadDelay==reloadTimers[6]||reloadDelay==reloadTimers[7])
			te.getWorld().playSound(ClientUtils.mc().player, te.getBlockPosForPos(49), IISounds.autocannonReload, SoundCategory.NEUTRAL, 1, 0.75f);
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		super.shoot(te);
		if(!te.getWorld().isRemote)
		{
			s2 = !magazine.isEmpty()?magazine.removeFirst(): ItemStack.EMPTY;
			if(!s2.isEmpty())
			{
				Vec3d weaponCenter = te.getWeaponCenter();
				IIPacketHandler.playRangedSound(te.getWorld(), weaponCenter,
						IISounds.autocannonShot, SoundCategory.PLAYERS, 85, 1.5f,
						1.25f+(float)(Utils.RAND.nextGaussian()*0.02)
				);

				ammoFactory.setStack(s2)
						.setPosition(te.getWeaponCenter())
						.setDirection(vv.scale(-1))
						.create();
			}
		}
		else if(!magazine.isEmpty())
		{
			Vec3d weaponCenter = te.getWeaponCenter().add(vv.scale(-1.85));
			weaponCenter = weaponCenter.add(vv.rotateYaw(bulletsShot%2==0?90: -90).scale(0.55));
			weaponCenter = weaponCenter.add(vv.rotatePitch(bulletsShot < 2?90: -90).scale(0.25));

			ParticleRegistry.spawnGunfireFX(weaponCenter, vv, 4f);

			magazine.removeFirst();
		}
		shootDelay = Autocannon.bulletFireTime;
		casingsToDrop++;
		bulletsShot = (bulletsShot+1)%4;
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		super.aimAt(yaw, pitch);
		if(this.pitch < -20&&this.pitch > -75)
			flaps = Math.min(flaps+0.075f, 1);
		else
			flaps = Math.max(flaps-0.075f, 0);
	}

	@Override
	public boolean isSetUp(boolean door)
	{
		return true;
	}

	@Override
	public boolean requiresPlatformRefill()
	{
		return requiresPlatformRefill;
	}

	@Nonnull
	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound nbt = super.saveToNBT(forClient);
		nbt.setFloat("flaps", flaps);

		nbt.setFloat("shootDelay", shootDelay);
		nbt.setInteger("reloadDelay", reloadDelay);

		nbt.setTag("inventory", blusunrize.immersiveengineering.common.util.Utils.writeInventory(inventory));
		nbt.setTag("inventoryPlatform", blusunrize.immersiveengineering.common.util.Utils.writeInventory(inventoryPlatform));
		if(!forClient)
			nbt.setTag("magazine", blusunrize.immersiveengineering.common.util.Utils.writeInventory(magazine));
		nbt.setInteger("magazine_amount", magazine.size());

		nbt.setBoolean("requiresPlatformRefill", requiresPlatformRefill);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		flaps = tagCompound.getInteger("flaps");

		shootDelay = tagCompound.getFloat("shootDelay");
		reloadDelay = tagCompound.getInteger("reloadDelay");

		inventory = blusunrize.immersiveengineering.common.util.Utils.readInventory(tagCompound.getTagList("inventory", 10), inventory.size());
		inventoryPlatform = blusunrize.immersiveengineering.common.util.Utils.readInventory(tagCompound.getTagList("inventoryPlatform", 10), inventoryPlatform.size());
		magazine = new ArrayDeque<>(blusunrize.immersiveengineering.common.util.Utils.readInventory(tagCompound.getTagList("magazine", 10), tagCompound.getInteger("magazine_amount")));

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
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.5f,
				new Vec3d(0, 1, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "ammoBox", 0.625f, 0.75f,
				new Vec3d(1, 0.625, 0), Vec3d.ZERO, 2));

		list.add(new EmplacementHitboxEntity(entity, "shieldRight", 0.75f, 2f,
				new Vec3d(-0.5, 1, -0.625), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "shieldLeft", 0.75f, 2f,
				new Vec3d(-0.5, 1, 0.625), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "barrelRight", 0.5f, 0.5f,
				new Vec3d(-0.5, 1.125, -0.625), new Vec3d(-0.5, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrelRight", 0.5f, 0.5f,
				new Vec3d(-0.5, 1.125, -0.625), new Vec3d(-1, 0, 0), 12));

		list.add(new EmplacementHitboxEntity(entity, "barrelLeft", 0.5f, 0.5f,
				new Vec3d(-0.5, 1, 0.625), new Vec3d(-0.5, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrelLeft", 0.5f, 0.5f,
				new Vec3d(-0.5, 1, 0.625), new Vec3d(-1, 0, 0), 12));

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
		while(casingsToDrop > 0)
		{
			//te.doProcessOutput(IIContent.itemAmmoAutocannon.getCasingStack(Math.min(casingsToDrop, 24)));
			casingsToDrop = Math.max(casingsToDrop-24, 0);
		}
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

			ItemStack s = inventory.get(i);//Magazines shouldn't stack, but why not
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

	@Nullable
	@Override
	public IItemHandler getItemHandler(boolean in)
	{
		return in?inventoryHandler: super.getItemHandler(in);
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Autocannon.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return Autocannon.maxHealth;
	}

}
