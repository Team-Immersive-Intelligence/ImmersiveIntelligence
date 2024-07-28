package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

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
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmplacementWeaponAutocannon extends EmplacementWeapon<EntityAmmoProjectile>
{
	private AxisAlignedBB vision;
	float flaps = 0;
	float shootDelay = 0;
	int reloadDelay = 0;
	int bulletsShot = 0;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(18, ItemStack.EMPTY);
	private NonNullList<ItemStack> inventoryPlatform = NonNullList.withSize(8, ItemStack.EMPTY);
	private int casingsToDrop = 0;
	private boolean requiresPlatformRefill = false;

	private ArrayDeque<ItemStack> magazine = new ArrayDeque<>();
	private ItemStack s2 = ItemStack.EMPTY;

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

	private static final int[] reloadTimers = new int[]{
			(int)(0.15*Autocannon.reloadTime), (int)(0.25*Autocannon.reloadTime), (int)(0.35*Autocannon.reloadTime), (int)(0.45*Autocannon.reloadTime),
			(int)(0.55*Autocannon.reloadTime), (int)(0.65*Autocannon.reloadTime), (int)(0.75*Autocannon.reloadTime), (int)(0.85*Autocannon.reloadTime)
	};

	private static final Runnable INSERTER_ANIM_NONE = () -> IIClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
	private static final Runnable INSERTER_ANIM_LEFT = () -> {
		IIClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		GlStateManager.rotate(-55, 1, 0, 0);
		GlStateManager.translate(0, 0, 0.0625f);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftModel)
			mod.render();
	};
	private static final Runnable INSERTER_ANIM_RIGHT = () -> {
		IIClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		GlStateManager.rotate(-55, 1, 0, 0);
		GlStateManager.translate(0, 0, 0.0625f);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineRightModel)
			mod.render();
	};
	private Vec3d vv;

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

			Vec3d vg = vv.scale(3f);
			ParticleRegistry.spawnGunfireFX(weaponCenter, vg, 4f);

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
		return vv!=null&&te.isDoorOpened&&shootDelay==0&&!magazine.isEmpty();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileEntityEmplacement te, float partialTicks)
	{
		float reloadAnim = reloadDelay > 0?Math.min(1, (this.reloadDelay+(magazine.isEmpty()?partialTicks: 0))/(float)Autocannon.reloadTime): 0;

		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		double cannonAnim;
		double b1 = 0, b2 = 0, b3 = 0, b4 = 0;

		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		float flaps = this.flaps;
		if(this.pitch < -20&&this.pitch > -75)
			flaps = Math.min(flaps+(0.075f*partialTicks), 1);
		else
			flaps = Math.max(flaps-(0.075f*partialTicks), 0);

		if(te.finishedDoorAction())
		{
			cannonAnim = 1d-Math.min(((shootDelay+partialTicks)/(double)Autocannon.bulletFireTime), 1d);
			switch(bulletsShot)
			{
				case 0:
					b1 = te.isShooting?Math.min(cannonAnim/0.5f, 1f): 0;
					//b4 = 1f-b1;
					break;
				case 1:
					b2 = Math.min(cannonAnim/0.5f, 1f);
					//b1 = 1f-b2;
					break;
				case 2:
					b3 = Math.min(cannonAnim/0.5f, 1f);
					//b2 = 1f-b3;
					break;
				case 3:
					b4 = Math.min(cannonAnim/0.5f, 1f);
					//b3 = 1f-b4;
					break;
			}
		}

		boolean power = te.energyStorage.getEnergyStored() >= getEnergyUpkeepCost();
		pp = pitch+(power?(Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks*getPitchTurnSpeed()): 0);
		yy = yaw+(power?(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks*getYawTurnSpeed()): 0);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel1Model)
			mod.rotationPointZ = (float)(-8f*b1);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel2Model)
			mod.rotationPointZ = (float)(-8f*b2);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel3Model)
			mod.rotationPointZ = (float)(-8f*b3);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel4Model)
			mod.rotationPointZ = (float)(-8f*b4);

		IIClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.baseModel)
			mod.render();
		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.turretModel)
			mod.render();

		GlStateManager.pushMatrix();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.ammoBoxLidModel)
		{
			if(reloadAnim < 0.1)
				mod.rotateAngleZ = (reloadAnim/0.1f)*-4.125f;
			else if(reloadAnim < 0.9f)
				mod.rotateAngleZ = -4.125f;
			else if(reloadAnim < 1f)
				mod.rotateAngleZ = (1f-((reloadAnim-0.9f)/0.1f))*-4.125f;
			else
				mod.rotateAngleZ = 0;
			mod.render();
		}
		GlStateManager.popMatrix();

		reloadAnim = Math.abs(reloadAnim-0.5f)*2;

		EmplacementRenderer.modelAutocannon.turretTopFlapsModel[0].rotateAngleY = -flaps*1.55f;
		EmplacementRenderer.modelAutocannon.turretTopFlapsModel[1].rotateAngleY = flaps*1.55f;
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.turretTopFlapsModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1.125f, 0);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.gunModel)
			mod.render();

		GlStateManager.pushMatrix();

		if(reloadAnim > 0.3-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineRightBottomModel)
				mod.render();
		if(reloadAnim > 0.5-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineRightTopModel)
				mod.render();
		if(reloadAnim > 0.7-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftBottomModel)
				mod.render();
		if(reloadAnim > 0.9-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftTopModel)
				mod.render();

		GlStateManager.popMatrix();


		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel1Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel2Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel3Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel4Model)
			mod.render();

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.625f, 1f, -0.88125f);

		float ins_y = 0f, ins_p1 = 15f, ins_p2 = 175f, ins_progress = 0f;
		Runnable r = INSERTER_ANIM_NONE;

		if(reloadAnim > 0.1&&reloadAnim < 0.9)
		{
			if(((reloadAnim-0.1)%0.2)/0.2 < 0.5)
			{
				double p_anim = (((reloadAnim-0.1)%0.2)/0.2)/0.5f;
				if(p_anim < 0.25)
				{
					ins_y = (float)(90*(p_anim/0.25));
				}
				else if(p_anim < 0.5)
				{
					ins_y = 90f;
					ins_p1 = 15+(float)(50*((p_anim-0.25)/0.25));
				}
				else if(p_anim < 0.75)
				{
					ins_y = 90f;
					ins_p1 = 15+(float)(50*(1f-((p_anim-0.5)/0.25)));
				}
				else
				{
					if(reloadAnim < 0.5)
						ins_y = 90f-(float)(90*((p_anim-0.75)/0.25));
					else
					{
						ins_y = 90f-((float)(25*((p_anim-0.75)/0.25)));
						ins_p2 = 175f-((float)(70*((p_anim-0.75)/0.25)));
						ins_p1 = 15f+((float)(25*((p_anim-0.75)/0.25)));
					}
				}

				if(p_anim > 0.5)
					if(reloadAnim > 0.5)
						r = INSERTER_ANIM_LEFT;
					else
						r = INSERTER_ANIM_RIGHT;
			}
			//right
			else if(reloadAnim < 0.3)
			{
				double p_anim = (reloadAnim-0.1)/0.2;
				//-25, 15,145, 1f
				if(p_anim < 0.25)
				{
					//ins_p1 = 15f, ins_p2 = 175f
					ins_y = (float)(-25*(p_anim/0.25));
				}
				else if(p_anim < 0.5)
				{
					ins_y = -25;
					ins_p1 = 15f+(float)(10*((p_anim-0.25)/0.25));
					ins_p2 = 175f-(float)(30*((p_anim-0.25)/0.25));
				}
				else if(p_anim < 0.75)
				{
					ins_y = -25;
					ins_p1 = 25f-(float)(10*((p_anim-0.5)/0.25));
					ins_p2 = 145f+(float)(30*((p_anim-0.5)/0.25));
				}
				else
				{
					ins_y = (float)(-25*(1f-((p_anim-0.75)/0.25)));
				}

			}
			else if(reloadAnim < 0.5)
			{
				double p_anim = (reloadAnim-0.3)/0.2;
				if(p_anim < 0.25)
				{
					//ins_p1 = 15f, ins_p2 = 175f
					ins_y = (float)(-25*(p_anim/0.25));
				}
				else if(p_anim < 0.5)
				{
					ins_y = -25;
					ins_p1 = 15f+(float)(15*((p_anim-0.25)/0.25));
					ins_p2 = 175f-(float)(45*((p_anim-0.25)/0.25));
				}
				else if(p_anim < 0.75)
				{
					ins_y = -25;
					ins_p1 = 30f-(float)(15*((p_anim-0.5)/0.25));
					ins_p2 = 130f+(float)(45*((p_anim-0.5)/0.25));
				}
				else
				{
					ins_y = (float)(-25*(1f-((p_anim-0.75)/0.25)));
				}
			}
			//left
			else if(reloadAnim < 0.7)
			{
				double p_anim = (reloadAnim-0.5)/0.2;
				if(p_anim < 0.2)
				{
					ins_y = (float)(65f-(12.5f*(p_anim/0.2f)));
					ins_p1 = (float)(40+(50f*(p_anim/0.2f)));
					ins_p2 = (float)(105+(15f*(p_anim/0.2f)));
				}
				else if(p_anim < 0.4)
				{
					ins_y = 52.5f;
					ins_p1 = 90;
					ins_p2 = 120;
				}
				else if(p_anim < 0.6)
				{
					ins_y = (float)(52.5f+(37.5f*((p_anim-0.4)/0.2f)));
					ins_p1 = (float)(90-(50f*((p_anim-0.4)/0.2f)));
					ins_p2 = (float)(120-(15f*((p_anim-0.4)/0.2f)));
				}
				else if(p_anim < 0.8)
				{
					ins_p1 = (float)(40-(25f*((p_anim-0.6)/0.2f)));
					ins_p2 = (float)(105+(70f*((p_anim-0.6)/0.2f)));
					ins_y = 90;
				}
				else
				{
					ins_p1 = 15f;
					ins_p2 = 175f;
					ins_y = (float)(90f*(1f-((p_anim-0.8)/0.2f)));
				}
			}
			else if(reloadAnim < 0.9)
			{
				double p_anim = (reloadAnim-0.7)/0.2;
				ins_p2 = 105;
				if(p_anim < 0.2)
				{
					ins_y = (float)(65f-(12.5f*(p_anim/0.2f)));
					ins_p1 = (float)(40+(20f*(p_anim/0.2f)));
				}
				else if(p_anim < 0.4)
				{
					ins_y = 52.5f;
					ins_p1 = 65;
				}
				else if(p_anim < 0.6)
				{
					ins_y = (float)(52.5f+(37.5f*((p_anim-0.4)/0.2f)));
					ins_p1 = (float)(65-(25f*((p_anim-0.4)/0.2f)));
				}
				else if(p_anim < 0.8)
				{
					ins_p1 = (float)(40-(25f*((p_anim-0.6)/0.2f)));
					ins_p2 = (float)(105+(70f*((p_anim-0.6)/0.2f)));
					ins_y = 90;
				}
				else
				{
					ins_p1 = 15f;
					ins_p2 = 175f;
					ins_y = (float)(90f*(1f-((p_anim-0.8)/0.2f)));
				}
			}
		}

		/*

		 */

		EmplacementRenderer.renderInserter(true, ins_y, ins_p1, ins_p2, ins_progress, r);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON.getProgressRequired();
		final int l = EmplacementRenderer.modelAutocannonConstruction.length;
		double maxClientProgress = IIUtils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrenchUpgradeProgress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		IIClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f));
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				EmplacementRenderer.modelAutocannonConstruction[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				EmplacementRenderer.modelAutocannonConstruction[i].render(0.0625f);
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
			EmplacementRenderer.modelAutocannonConstruction[i].render(0.0625f);
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
			te.doProcessOutput(IIContent.itemAmmoAutocannon.getCasingStack(Math.min(casingsToDrop, 24)));
			casingsToDrop = Math.max(casingsToDrop-24, 0);
		}
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

	@SideOnly(Side.CLIENT)
	@Override
	protected Tuple<ResourceLocation, List<ModelRendererTurbo>> getDebris()
	{
		return new Tuple<>(EmplacementRenderer.textureAutocannon, Arrays.asList(EmplacementRenderer.modelAutocannonConstruction));
	}

}
