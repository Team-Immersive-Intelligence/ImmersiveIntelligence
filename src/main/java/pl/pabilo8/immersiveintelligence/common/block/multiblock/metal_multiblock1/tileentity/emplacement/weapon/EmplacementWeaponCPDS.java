package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.CPDS;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class EmplacementWeaponCPDS extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	int reloadDelay = 0;
	int bulletsShot = 0;
	float shootDelay = 0;
	NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
	private final IItemHandler inventoryHandler = new ItemStackHandler(inventory)
	{
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			if(!OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.CPDS_DRUM), false))
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
	NonNullList<ItemStack> inventoryPlatform = NonNullList.withSize(3, ItemStack.EMPTY);
	ArrayDeque<ItemStack> magazine = new ArrayDeque<>();
	/**
	 * CPDS Q&A
	 * <p>
	 * Q: Is CPDS a real life thing?
	 * A: Not really, it's based on CIWS, but in II it performs a counter-projectile role with anti-aircraft as secondary task
	 * <p>
	 * Q: What does CPDS stand for?
	 * A: Counter-Projectile Defense System
	 * <p>
	 * Q: Why gatling?
	 * A: It's the most high-tech II can get ^^ Historically, gatling guns were used since the US Civil War, so yes, they existed in interwar/ww2
	 * Decided to choose it because of the unique design
	 * <p>
	 * Q: Isn't it OP? it's 8 barrels
	 * A: Yes, but it costs a lot
	 */
	private AxisAlignedBB vision;
	private Vec3d vv;
	private int casingsToDrop = 0;
	private boolean requiresPlatformRefill = false;
	private ItemStack s2 = ItemStack.EMPTY;

	public EmplacementWeaponCPDS()
	{

	}

	@Override
	public String getName()
	{
		return "cpds";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return CPDS.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return CPDS.pitchRotateSpeed;
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
						IISounds.autocannonShot, SoundCategory.PLAYERS, 65, 1.5f,
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
			Vec3d weaponCenter = te.getWeaponCenter().add(vv.scale(-2.5));
			Vec3d vg = vv.scale(4f);
			ParticleRegistry.spawnGunfireFX(weaponCenter, vg, 4f);

			magazine.removeFirst();
		}
		casingsToDrop++;
		bulletsShot = (bulletsShot+1)%4;

		if(!magazine.isEmpty()&&shootDelay < 20)
			shootDelay += 6;
	}

	public boolean isSetUp(boolean door)
	{
		return true;
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
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		this.vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(CPDS.detectionRadius);
	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{
		if(active&&magazine.isEmpty())
		{
			if(reloadDelay==0)
			{
				if(inventoryPlatform.stream().anyMatch(stack -> OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.CPDS_DRUM), false)
						&&IIContent.itemBulletMagazine.getRemainingBulletCount(stack) > 0))
					reloadDelay = 1;
				else
					requiresPlatformRefill = true;
			}
			else
			{
				reloadDelay++;
			}

			if(reloadDelay >= CPDS.reloadTime)
			{

				for(ItemStack stack : inventoryPlatform)
				{
					if(OreDictionary.itemMatches(stack, IIContent.itemBulletMagazine.getMagazine(Magazines.CPDS_DRUM), false)
							&&IIContent.itemBulletMagazine.getRemainingBulletCount(stack) > 0)
					{
						magazine.addAll(IIContent.itemBulletMagazine.takeAll(stack));
						break;
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

		reloadDelay = tagCompound.getInteger("reloadDelay");

		inventory = Utils.readInventory(tagCompound.getTagList("inventory", 10), inventory.size());
		inventoryPlatform = Utils.readInventory(tagCompound.getTagList("inventoryPlatform", 10), inventoryPlatform.size());
		magazine = new ArrayDeque<>(Utils.readInventory(tagCompound.getTagList("magazine", 10), tagCompound.getInteger("magazine_amount")));

		requiresPlatformRefill = tagCompound.getBoolean("requiresPlatformRefill");
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return vv!=null&&!magazine.isEmpty();
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
			entity.aabb = new AxisAlignedBB(-3, 0, -3, 3, 3, 3);
	}

	@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 2f, 0.75f,
				new Vec3d(0, 0.75, 0), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "topBox", 1.75f, 0.75f+0.5f,
				new Vec3d(0.25, 0.75+0.5, 0), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "camera", 0.75f, 0.75f,
				new Vec3d(-0.125, 2.25, -0.625), Vec3d.ZERO, 6));

		list.add(new EmplacementHitboxEntity(entity, "barrel1", 0.5f, 0.5f,
				new Vec3d(0, 1, 0), new Vec3d(-1.25, 0, -0.25), 20));
		list.add(new EmplacementHitboxEntity(entity, "barrel2", 0.5f, 0.5f,
				new Vec3d(0, 1, 0), new Vec3d(-1.75, 0, -0.25), 20));
		list.add(new EmplacementHitboxEntity(entity, "barrel3", 0.5f, 0.5f,
				new Vec3d(0, 1, 0), new Vec3d(-2.25, 0, -0.25), 20));


		return list.toArray(new EmplacementHitboxEntity[0]);
	}

	@Override
	public NonNullList<ItemStack> getBaseInventory()
	{
		return inventory;
	}

	@Override
	public void performPlatformRefill(TileEntityEmplacement te)
	{
		while(casingsToDrop > 0)
		{
			//te.doProcessOutput(IIContent.itemAmmoMachinegun.getCasingStack(Math.min(casingsToDrop, 24)));
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
	public void renderStorageInventory(GuiEmplacementPageStorage gui, int mx, int my, float partialTicks, boolean first)
	{

	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return CPDS.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return CPDS.maxHealth;
	}

}
