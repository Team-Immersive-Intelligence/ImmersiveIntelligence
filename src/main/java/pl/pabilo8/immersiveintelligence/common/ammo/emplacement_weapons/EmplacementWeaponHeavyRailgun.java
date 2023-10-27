package pl.pabilo8.immersiveintelligence.common.ammo.emplacement_weapons;

import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.entities.EntityRailgunShot;
import blusunrize.immersiveengineering.common.util.IESounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyRailgun;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.common.ammo.emplacement_weapons.EmplacementWeaponCPDS.getInterceptTime;

public class EmplacementWeaponHeavyRailgun extends EmplacementWeapon
{
	/**
	 * To Blu:
	 * I do as I promised, I promised to not add a railgun turret
	 * so I added a Heavy Railgun emplacement
	 */
	private AxisAlignedBB vision;
	float shootDelay = HeavyRailgun.shotFireTime;
	int reloadDelay = 0;
	private Vec3d vv;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	private NonNullList<ItemStack> inventoryPlatform = NonNullList.withSize(6, ItemStack.EMPTY);
	private boolean requiresPlatformRefill = false;

	private ArrayDeque<ItemStack> magazine = new ArrayDeque<>();
	private ItemStack s2 = ItemStack.EMPTY;

	private final IItemHandler inventoryHandler = new ItemStackHandler(inventory)
	{
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			if(!ItemIIRailgunOverride.isAmmo(stack))
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

	@Override
	public String getName()
	{
		return "heavy_railgun";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return HeavyRailgun.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return HeavyRailgun.pitchRotateSpeed;
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		super.shoot(te);

		if(!te.getWorld().isRemote)
		{
			s2 = magazine.size() > 0?magazine.removeFirst(): ItemStack.EMPTY;
			if(!s2.isEmpty())
			{
				te.getWorld().playSound(null, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), IESounds.railgunFire, SoundCategory.PLAYERS, 1.5f, 0f);
				if(s2.getItem()==IIContent.itemRailgunGrenade)
				{
					EntityBullet a = AmmoUtils.createBullet(te.getWorld(), s2, te.getWeaponCenter(), vv.scale(-1f), 3);
					a.setShootPos(te.getAllBlocks());
					if(entity!=null)
						a.setShooters(entity, entity.partArray);
					te.getWorld().spawnEntity(a);
				}
				else
				{
					Vec3d weaponCenter = te.getWeaponCenter();
					Vec3d scale = vv.scale(-1f).normalize();
					float speed = 20;
					EntityRailgunShot shot = new EntityRailgunShot(te.getWorld(), entity,
							scale.x*speed, scale.y*speed, scale.z*speed, s2.copy());
					shot.setPosition(weaponCenter.x, weaponCenter.y, weaponCenter.z);
					te.getWorld().spawnEntity(shot);
				}
			}
		}
		else if(magazine.size() > 0)
			magazine.removeFirst();

		shootDelay = HeavyRailgun.shotFireTime;
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
		s2 = magazine.size() > 0?magazine.peekFirst(): ItemStack.EMPTY;

		double dist;
		if(s2.getItem()==IIContent.itemRailgunGrenade)
		{
			float force = IIContent.itemRailgunGrenade.getDefaultVelocity()*3;
			float mass = s2.isEmpty()?0: IIContent.itemRailgunGrenade.getMass(s2);

			vv = posTurret.subtract(posTarget.add(motion.scale(getInterceptTime(force, posTurret.subtract(posTarget), motion))));
			dist = vv.distanceTo(new Vec3d(0, vv.y, 0));

			double gravityMotionY = 0, motionY = 0, baseMotionY = vv.normalize().y, baseMotionYC;
			while(dist > 0)
			{
				force -= EntityBullet.DRAG*force*EntityBullet.DEV_SLOMO;
				gravityMotionY -= EntityBullet.GRAVITY*mass*EntityBullet.DEV_SLOMO;
				baseMotionYC = baseMotionY*(force/(IIContent.itemRailgunGrenade.getDefaultVelocity()*3));
				motionY += (baseMotionYC+gravityMotionY)*EntityBullet.DEV_SLOMO;
				dist -= EntityBullet.DEV_SLOMO*force;
			}
			vv = vv.addVector(0, motionY-baseMotionY, 0).normalize();
		}
		else
		{
			vv = posTurret.subtract(posTarget.add(motion));
			dist = vv.distanceTo(new Vec3d(0, vv.y, 0));
			RailgunProjectileProperties p = RailgunHandler.getProjectileProperties(s2);
			if(p!=null)
			{
				float force = 20;
				float gravity = (float)p.gravity;

				double gravityMotionY = 0, motionY = 0, baseMotionY = vv.normalize().y, baseMotionYC = baseMotionY;
				while(dist > 0)
				{
					dist -= force;
					force *= 0.99;
					baseMotionYC *= 0.99f;
					gravityMotionY -= gravity;
					motionY += (baseMotionYC+gravityMotionY);

					/*
					add motion
					this.motionX *= movementDecay;
					this.motionY *= movementDecay;
					this.motionY -= getGravity();
					*/

				}
				vv = vv.addVector(0, motionY-baseMotionY, 0).normalize();
			}

		}

		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));

		return new float[]{yy, pp};
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		if(reloadDelay==0)
		{
			super.aimAt(yaw, pitch);
		}
		else
			super.aimAt(yaw, 0);
	}

	@Override
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(HeavyRailgun.detectionRadius);
	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{
		if(active&&magazine.isEmpty())
		{
			if(reloadDelay==0)
			{
				if(inventoryPlatform.stream().anyMatch(ItemIIRailgunOverride::isAmmo))
					reloadDelay = 1;
				else
					requiresPlatformRefill = true;
			}
			else if(pitch==0)
			{
				reloadDelay++;
			}

			if(reloadDelay >= HeavyRailgun.reloadAmmoBoxTime)
			{

				for(ItemStack stack : inventoryPlatform)
				{
					if(magazine.size() >= 8) //shouldn't be more, but who knows
						break;

					while(!stack.isEmpty()&&magazine.size() < 8)
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

	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound nbt = super.saveToNBT(forClient);
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
		if(shootDelay > 0)
		{
			if(shootDelay==HeavyRailgun.shotFireTime)
				te.getWorld().playSound(null, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), IESounds.chargeSlow, SoundCategory.PLAYERS, 1.5f, 0f);
			shootDelay--;
		}
		return vv!=null&&te.isDoorOpened&&shootDelay==0&&magazine.size() > 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileEntityEmplacement te, float partialTicks)
	{
		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		boolean power = te.energyStorage.getEnergyStored() >= getEnergyUpkeepCost();
		pp = pitch+(power?(Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks*getPitchTurnSpeed()): 0);
		yy = yaw+(power?(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks*getYawTurnSpeed()): 0);

		IIClientUtils.bindTexture(EmplacementRenderer.textureHeavyRailgun);

		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyRailgun.baseModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 20/16f, 4F/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyRailgun.gunModel)
			mod.render();
		GlStateManager.popMatrix();

		GlStateManager.translate(-0.75f, 0.5f, -0.75f);
		//125 0.35f, 0f
		//0, 0.75f, 0f
		float craneYaw = 0, craneDist = 0.75f, craneDrop = 0f, craneGrab = 0f;
		if(reloadDelay > 0&&pitch==0)
		{
			float craneProgress = Math.min((reloadDelay+partialTicks)/(float)HeavyRailgun.reloadAmmoBoxTime, 1f);
			if(craneProgress < 0.2)
				craneDrop = craneProgress/0.2f;
			else if(craneProgress < 0.25)
			{
				craneDrop = 1f;
				craneGrab = (craneProgress-0.2f)/0.05f;
			}
			else if(craneProgress < 0.4)
			{
				craneGrab = 1f;
				craneDrop = 1f-(craneProgress-0.25f)/0.15f;
				craneDist = 0.75f-(((craneProgress-0.25f)/0.15f)*0.4f);
			}
			else if(craneProgress < 0.65)
			{
				craneGrab = 1f;
				craneDrop = 0f;
				craneDist = 0.35f;
				craneYaw = 125f*((craneProgress-0.4f)/0.25f);
			}
			else if(craneProgress < 0.75f)
			{
				craneDrop = ((craneProgress-0.65f)/0.1f)*0.125f;
				craneDist = 0.35f;
				craneYaw = 125f;
			}
			else if(craneProgress < 0.8f)
			{
				craneDrop = 0.125f*(1f-((craneProgress-0.75f)/0.05f));
				craneGrab = (1f-((craneProgress-0.75f)/0.05f));
				craneDist = 0.35f;
				craneYaw = 125f;
			}
			else if(craneProgress < 0.925f)
			{
				craneYaw = 125f*(1f-((craneProgress-0.8f)/0.125f));
				craneDist = 0.35f;
			}
			else
			{

			}

		}
		EmplacementRenderer.renderCrane(craneYaw, craneDist, craneDrop, craneGrab, () -> {
		});

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN.getProgressRequired();
		final int l = EmplacementRenderer.modelHeavyRailgunConstruction.length;
		double maxClientProgress = IIUtils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrenchUpgradeProgress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		IIClientUtils.bindTexture(EmplacementRenderer.textureHeavyRailgun);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f));
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				EmplacementRenderer.modelHeavyRailgunConstruction[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				EmplacementRenderer.modelHeavyRailgunConstruction[i].render(0.0625f);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.98f, 0.98f, 0.98f);
		GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
		//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

		ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
		for(int i = l-1; i >= Math.max((l*progress)-1, 0); i--)
		{
			EmplacementRenderer.modelHeavyRailgunConstruction[i].render(0.0625f);
		}

		ShaderUtil.releaseShader();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
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
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.5f,
				new Vec3d(0, 1, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "shieldRight", 0.75f, 2f,
				new Vec3d(-0.5, 1, -0.625), Vec3d.ZERO, 14));
		list.add(new EmplacementHitboxEntity(entity, "shieldRightBack", 0.75f, 2f,
				new Vec3d(0, 1, -0.625), Vec3d.ZERO, 14));
		list.add(new EmplacementHitboxEntity(entity, "shieldMiddle", 0.75f, 0.5f,
				new Vec3d(-0.5, 0, -0.625), Vec3d.ZERO, 14));
		list.add(new EmplacementHitboxEntity(entity, "shieldLeft", 0.75f, 2f,
				new Vec3d(-0.5, 1, 0.625), Vec3d.ZERO, 14));

		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.625f, 0.625f,
				new Vec3d(-0.5, 1.5, 0), new Vec3d(-0.625f, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.625f, 0.625f,
				new Vec3d(-0.5, 1.5, 0), new Vec3d(-1.25, 0, 0), 12));
		list.add(new EmplacementHitboxEntity(entity, "barrel", 0.625f, 0.625f,
				new Vec3d(-0.5, 1.5, 0), new Vec3d(-1.875, 0, 0), 12));

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
			te.doProcessOutput(inventoryPlatform.get(i));
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
		return HeavyRailgun.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyRailgun.maxHealth;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected Tuple<ResourceLocation, List<ModelRendererTurbo>> getDebris()
	{
		return new Tuple<>(EmplacementRenderer.textureHeavyRailgun, Arrays.asList(EmplacementRenderer.modelHeavyRailgunConstruction));
	}

	@Nullable
	@Override
	public IItemHandler getItemHandler(boolean in)
	{
		return in?inventoryHandler: super.getItemHandler(in);
	}
}
