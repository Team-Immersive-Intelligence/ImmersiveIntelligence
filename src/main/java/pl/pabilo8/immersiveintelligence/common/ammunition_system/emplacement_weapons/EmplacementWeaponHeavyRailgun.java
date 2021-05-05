package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.HeavyRailgun;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

import java.util.ArrayList;

public class EmplacementWeaponHeavyRailgun extends EmplacementWeapon
{
	/**
	 * To Blu:
	 * I do as I promised, I promised to not add a railgun turret
	 * so I added a Heavy Railgun emplacement
	 */
	float shootDelay = 0;
	int reloadDelay = 0;
	int bulletsShot = 0;
	private Vec3d vv;

	static ItemStack s2=ItemStack.EMPTY;

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
			if(s2.isEmpty())
			{
				s2 = IIContent.itemRailgunGrenade.getBulletWithParams("core_brass", "canister", "hmx", "tracer_powder");
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("colour", 0x00ff00);
				IIContent.itemRailgunGrenade.setComponentNBT(s2, new NBTTagCompound(), tag);
			}

			te.getWorld().playSound(null, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), IISounds.machinegun_shot, SoundCategory.PLAYERS, 1.25f, 0.25f);
			EntityBullet a = BulletHelper.createBullet(te.getWorld(), s2, new Vec3d(te.getBlockPosForPos(49).up()).addVector(0.5, 0, 0.5), vv.scale(-1f), 12f);
			a.setShootPos(te.getAllBlocks());
			te.getWorld().spawnEntity(a);
		}
		shootDelay = HeavyRailgun.shotFireTime;
		bulletsShot++;
	}

	public boolean isSetUp(boolean door)
	{
		return true;
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		float force = 12f;
		float mass = IIContent.itemRailgunGrenade.getMass(s2);

		vv = posTurret.subtract(posTarget);
		float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
		Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z);
		float motionTime = (float)Math.abs(motionVec.lengthSquared())/force/0.98f;
		motionVec = motionVec.scale(motionTime);
		vv = vv.add(motionVec).subtract(0, EntityBullet.GRAVITY/mass/force*motionXZ, 0).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0,vv.y,0)))));
		//float pp = Utils.calculateBallisticAngle(Math.abs(vv.lengthSquared()),posTurret.y-(posTarget.y+motion.y),force,EntityBullet.GRAVITY/mass,0.98f);
		pp = MathHelper.clamp(pp, -90, 75);
		return new float[]{yy, pp};
	}

	@Override
	public void tick()
	{
		if(bulletsShot>=8)
		{
			reloadDelay++;
			if(reloadDelay>= HeavyRailgun.reloadAmmoBoxTime)
			{
				bulletsShot=0;
				reloadDelay=0;
			}
		}

		if(shootDelay > 0)
			shootDelay--;
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("yaw", yaw);
		tag.setFloat("pitch", pitch);
		return tag;
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return te.isDoorOpened&&shootDelay==0&&bulletsShot<8;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		yaw = tagCompound.getFloat("yaw");
		pitch = tagCompound.getFloat("pitch");

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileEntityEmplacement te, float partialTicks)
	{
		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		pp = pitch+Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks*getPitchTurnSpeed();
		yy = yaw+Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks*getYawTurnSpeed();

		//pp=((te.getWorld().getTotalWorldTime()+partialTicks)%40)/40f*90;

		//setupProgress = ((te.getWorld().getTotalWorldTime()+partialTicks)%100)/100f;
		ClientUtils.bindTexture(EmplacementRenderer.textureHeavyRailgun);

		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyRailgun.baseModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 20/16f, 13F/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyRailgun.gunModel)
			mod.render();
		GlStateManager.popMatrix();

		GlStateManager.translate(-0.75f,0.5f,-0.75f);
		//125 0.35f, 0f
		//0, 0.75f, 0f
		float craneYaw=0,craneDist=0.75f,craneDrop=0f,craneGrab=0f;
		if(reloadDelay>0)
		{
			float craneProgress = Math.min((reloadDelay+partialTicks)/(float)HeavyRailgun.reloadAmmoBoxTime,1f);
			if(craneProgress<0.2)
				craneDrop=craneProgress/0.2f;
			else if(craneProgress<0.25)
			{
				craneDrop=1f;
				craneGrab=(craneProgress-0.2f)/0.05f;
			}
			else if(craneProgress<0.4)
			{
				craneGrab=1f;
				craneDrop=1f-(craneProgress-0.25f)/0.15f;
				craneDist=0.75f-(((craneProgress-0.25f)/0.15f)*0.4f);
			}
			else if(craneProgress<0.65)
			{
				craneGrab=1f;
				craneDrop=0f;
				craneDist=0.35f;
				craneYaw=125f*((craneProgress-0.4f)/0.25f);
			}
			else if(craneProgress<0.75f)
			{
				craneDrop=((craneProgress-0.65f)/0.1f)*0.125f;
				craneDist=0.35f;
				craneYaw=125f;
			}
			else if(craneProgress<0.8f)
			{
				craneDrop=0.125f*(1f-((craneProgress-0.75f)/0.05f));
				craneGrab=(1f-((craneProgress-0.75f)/0.05f));
				craneDist=0.35f;
				craneYaw=125f;
			}
			else if(craneProgress<0.925f)
			{
				craneYaw=125f*(1f-((craneProgress-0.8f)/0.125f));
				craneDist=0.35f;
			}
			else
			{

			}

		}
		EmplacementRenderer.renderCrane(craneYaw, craneDist, craneDrop,craneGrab,()->{});

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		ClientUtils.bindTexture(EmplacementRenderer.textureHeavyRailgun);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyRailgun.baseModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 20/16f, 13F/16f);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyRailgun.gunModel)
			mod.render();
		GlStateManager.popMatrix();
		GlStateManager.translate(-0.75f,0.5f,-0.75f);
		EmplacementRenderer.renderCrane(0, 0.75f, 0,0,()->{});

		GlStateManager.popMatrix();
	}
}
