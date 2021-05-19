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
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.CPDS;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

public class EmplacementWeaponCPDS extends EmplacementWeapon
{
	/**
	 * CPDS Q&A
	 *
	 * Q: Is CPDS a real life thing?
	 * A: Not really, it's based on CIWS, but in II it performs a counter-projectile role with anti-aircraft as secondary task
	 *
	 * Q: What does CPDS stand for?
	 * A: Counter-Projectile Defense System
	 *
	 * Q: Why gatling?
	 * A: It's the most high-tech II can get ^^ Historically, gatling guns were used since the US Civil War, so yes, they existed in interwar/ww2
	 * Decided to choose it because of the unique design
	 *
	 * Q: Isn't it OP? it's 8 barrels
	 * A: Yes, but it costs a lot
	 */
	float shootDelay = 0;
	int reloadDelay = 0;
	int bulletsShot = 0;
	private Vec3d vv;
	static ItemStack s2=ItemStack.EMPTY;

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
			if(s2.isEmpty())
			{
				s2 = IIContent.itemAmmoMachinegun.getBulletWithParams("core_tungsten", "piercing","tracer_powder");
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("colour", 0xff0000);
				IIContent.itemAmmoMachinegun.setComponentNBT(s2, new NBTTagCompound(), tag);
			}

			te.getWorld().playSound(null, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), IISounds.machinegun_shot, SoundCategory.PLAYERS, 1.25f, 0.25f);
			EntityBullet a = BulletHelper.createBullet(te.getWorld(), s2, te.getWeaponCenter(), vv.scale(-1f), 12f);
			a.setShootPos(te.getAllBlocks());
			te.getWorld().spawnEntity(a);
		}
		if(shootDelay<20)
		shootDelay+=6;
		//bulletsShot++;
	}

	public boolean isSetUp(boolean door)
	{
		return true;
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		float force = 12f;
		float mass = IIContent.itemAmmoMachinegun.getMass(s2);

		vv = posTurret.subtract(posTarget.add(motion));
		float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
		float motionTime = (float)Math.abs(vv.lengthSquared())/force/0.8f;
		Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z).scale(1f).addVector(0, 0f, 0f);
		vv = vv.subtract(0, EntityBullet.GRAVITY/mass*motionXZ/force, 0).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0,vv.y,0)))));
		//float pp = Utils.calculateBallisticAngle(Math.abs(vv.lengthSquared()),posTurret.y-(posTarget.y+motion.y),force,EntityBullet.GRAVITY/mass,0.98f);
		pp = MathHelper.clamp(pp, -90, 75);
		return new float[]{yy, pp};
	}

	@Override
	public void tick()
	{
		if(bulletsShot >= 64)
		{
			reloadDelay++;
			if(reloadDelay >= Autocannon.reloadTime)
			{
				bulletsShot = 0;
				reloadDelay = 0;
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
		return te.isDoorOpened;
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

		float f = (((te.getWorld().getTotalWorldTime()%4+partialTicks))/4f)*(shootDelay/20f);

		ClientUtils.bindTexture(EmplacementRenderer.textureCPDS);

		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelCPDS.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelCPDS.internalsModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelCPDS.hatchModel)
			mod.render();

		for(ModelRendererTurbo mod : EmplacementRenderer.modelCPDS.observeModel)
			mod.render();

		GlStateManager.translate(0, 19.5/16f, 7.5F/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelCPDS.gunModel)
			mod.render();
		GlStateManager.translate(0, -0.5f/16f, -0.5f/16f);
		GlStateManager.rotate(f*180f, 0, 0, 1);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelCPDS.barrelsModel)
			mod.render();

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_CPDS.getProgressRequired();
		final int l = EmplacementRenderer.modelCPDSConstruction.length;
		double maxClientProgress = Utils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrench_upgrade_progress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		ClientUtils.bindTexture(EmplacementRenderer.textureCPDS);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f)/1f);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				EmplacementRenderer.modelCPDSConstruction[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				EmplacementRenderer.modelCPDSConstruction[i].render(0.0625f);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.98f, 0.98f, 0.98f);
		GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
		//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

		ShaderUtil.blueprint_static(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
		for(int i = l-1; i >= Math.max((l*progress)-1, 0); i--)
		{
			EmplacementRenderer.modelCPDSConstruction[i].render(0.0625f);
		}

		ShaderUtil.releaseShader();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
