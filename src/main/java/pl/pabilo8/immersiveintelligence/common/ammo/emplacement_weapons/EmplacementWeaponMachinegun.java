package pl.pabilo8.immersiveintelligence.common.ammo.emplacement_weapons;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
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
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Machinegun;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmplacementWeaponMachinegun extends EmplacementWeapon
{
	/**
	 * *reloads the gun* [H] E A V Y  M A C H I N E G U N<br>
	 * <s>Rawket Lawnchair!</s>
	 */
	private AxisAlignedBB vision;
	float shootDelay = Machinegun.bulletFireTime;
	int reloadDelay = 0;
	private Vec3d vv;

	int setupDelay = 0;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(36, ItemStack.EMPTY);
	private NonNullList<ItemStack> inventoryPlatform = NonNullList.withSize(12, ItemStack.EMPTY);
	private boolean requiresPlatformRefill = false;

	private ArrayDeque<ItemStack> magazine = new ArrayDeque<>();
	private ItemStack s2 = ItemStack.EMPTY;

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

		if(!te.getWorld().isRemote)
		{
			s2 = magazine.size() > 0?magazine.removeFirst(): ItemStack.EMPTY;
			if(!s2.isEmpty())
			{
				IIPacketHandler.playRangedSound(te.getWorld(), new Vec3d(te.getPos()),
						IISounds.machinegunShotHeavyBarrel, SoundCategory.PLAYERS, 75, 1.5f,
						0.8f+(float)(Utils.RAND.nextGaussian()*0.02)
				);
				EntityBullet a = AmmoUtils.createBullet(te.getWorld(), s2, te.getWeaponCenter(), vv.scale(-1f));
				a.setShootPos(te.getAllBlocks());
				if(entity!=null)
					a.setShooters(entity, entity.partArray);
				te.getWorld().spawnEntity(a);
			}
		}
		else if(magazine.size() > 0)
		{
			Vec3d weaponCenter = te.getWeaponCenter().add(vv.scale(-1.85))
					//.add(vv.rotatePitch(-90).scale(0.125))
					.add(vv.rotateYaw(90).scale(0.25));
			Vec3d vg = vv.scale(3f);
			ParticleUtils.spawnGunfireFX(weaponCenter, vg, 3f);

			weaponCenter = weaponCenter.add(vv.rotateYaw(90).scale(-0.5));
			ParticleUtils.spawnGunfireFX(weaponCenter, vg, 3f);

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
		float force = IIContent.itemAmmoMachinegun.getDefaultVelocity();
		s2 = magazine.size() > 0?magazine.peekFirst(): ItemStack.EMPTY;
		float mass = s2.isEmpty()?0: IIContent.itemAmmoMachinegun.getMass(s2);

		vv = posTurret.subtract(posTarget.add(motion));

		double dist = vv.distanceTo(new Vec3d(0, vv.y, 0));
		double gravityMotionY = 0, motionY = 0, baseMotionY = vv.normalize().y, baseMotionYC;
		while(dist > 0)
		{
			force -= EntityBullet.DRAG*force*EntityBullet.DEV_SLOMO;
			gravityMotionY -= EntityBullet.GRAVITY*mass*EntityBullet.DEV_SLOMO;
			baseMotionYC = baseMotionY*(force/(IIContent.itemAmmoMachinegun.getDefaultVelocity()));
			motionY += (baseMotionYC+gravityMotionY)*EntityBullet.DEV_SLOMO;
			dist -= EntityBullet.DEV_SLOMO*force;
		}

		vv = vv.addVector(0, motionY-baseMotionY, 0).normalize();


		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));

		return new float[]{yy, pp};
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
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(Machinegun.detectionRadius);
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
		//pp=(((te.getWorld().getTotalWorldTime()%40)/40f))*360f;
		float setupProgress = (MathHelper.clamp(setupDelay+(setupDelay > 0?(te.isDoorOpened?(te.progress==Emplacement.lidTime?partialTicks: 0): -partialTicks): 0), 0, InfraredObserver.setupTime)/(float)InfraredObserver.setupTime);

		float inserterYaw = 0, inserterPitch1 = 95, inserterPitch2 = 265, inserterProgress = 0;

		IIClientUtils.bindTexture(EmplacementRenderer.textureMachinegun);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.baseModel)
			mod.render();

		GlStateManager.rotate(yy, 0, 1, 0);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.turretBaseModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 15/16f, 2/16f);
		GlStateManager.rotate(pp, 1, 0, 0);

		if(isSetUp(te.isDoorOpened)||this!=te.currentWeapon)
		{
			//-75 100 220 0
			//-75 100 220 -0.5f

			inserterYaw = -75;
			inserterPitch1 = 100;
			inserterPitch2 = 220;
			inserterProgress = -0.5f;
			if(reloadDelay > 0)
			{
				for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.ammoCrateModel)
					mod.render();
				for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.ammoCrateLidModel)
					mod.render();
			}
			else
			{
				for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.ammoCrateModel)
					mod.render();
				for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.ammoCrateLidModel)
					mod.render();
			}

			for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.barrelsModel)
				mod.render();

		}
		else
		{
			float machineBox = MathHelper.clamp(setupProgress/0.35f, 0, 1);
			float projector = MathHelper.clamp((setupProgress-0.35f)/0.35f, 0, 1);
			float deskStuff = MathHelper.clamp((setupProgress-0.7f)/0.3f, 0, 1);

			GlStateManager.pushMatrix();
			for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.barrelsModel)
				mod.render();
			GlStateManager.popMatrix();


			for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.ammoCrateModel)
				mod.render();
			for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.ammoCrateLidModel)
				mod.render();

		}
		for(ModelRendererTurbo mod : EmplacementRenderer.modelMachinegun.turretModel)
			mod.render();

		GlStateManager.rotate(-90, 1, 0, 0);
		GlStateManager.translate(-0.0625, 0.75+0.0625, 0.0625);
		EmplacementRenderer.renderInserter(true, inserterYaw, inserterPitch1, inserterPitch2, inserterProgress, () -> {
		});

		GlStateManager.popMatrix();


		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN.getProgressRequired();
		final int l = EmplacementRenderer.modelMachinegunConstruction.length;
		double maxClientProgress = IIUtils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrenchUpgradeProgress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		IIClientUtils.bindTexture(EmplacementRenderer.textureMachinegun);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f));
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				EmplacementRenderer.modelMachinegunConstruction[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				EmplacementRenderer.modelMachinegunConstruction[i].render(0.0625f);
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
			EmplacementRenderer.modelMachinegunConstruction[i].render(0.0625f);
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
		return Machinegun.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return Machinegun.maxHealth;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected Tuple<ResourceLocation, List<ModelRendererTurbo>> getDebris()
	{
		return new Tuple<>(EmplacementRenderer.textureMachinegun, Arrays.asList(EmplacementRenderer.modelMachinegunConstruction));
	}

	@Nullable
	@Override
	public IItemHandler getItemHandler(boolean in)
	{
		return in?inventoryHandler: super.getItemHandler(in);
	}
}
