package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

public class EmplacementWeaponAutocannon extends EmplacementWeapon
{
	float flaps = 0;
	float shootDelay = 0;
	int reloadDelay = 0;
	int bulletsShot = 0;
	NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
	static ItemStack s2=ItemStack.EMPTY;


	@SideOnly(Side.CLIENT)
	private static final Runnable INSERTER_ANIM_NONE = () -> {
		ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
	};
	@SideOnly(Side.CLIENT)
	private static final Runnable INSERTER_ANIM_LEFT = () -> {
		ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		GlStateManager.rotate(-55, 1, 0, 0);
		GlStateManager.translate(0, 0, 0.0625f);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftModel)
			mod.render();
	};
	@SideOnly(Side.CLIENT)
	private static final Runnable INSERTER_ANIM_RIGHT = () -> {
		ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
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
		float force = 6f;
		float mass = IIContent.itemAmmoAutocannon.getMass(s2);

		vv = posTurret.subtract(posTarget);
		float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
		float motionTime = (float)Math.abs(vv.lengthSquared())/force/0.8f;
		Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z).scale(1f).addVector(0, 0f, 0f);
		vv = vv.subtract(motionVec).subtract(0, EntityBullet.GRAVITY/mass*motionXZ/force, 0).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)((Math.atan2(vv.y, motionXZ)*180D));
		//float pp = Utils.calculateBallisticAngle(Math.abs(vv.lengthSquared()),posTurret.y-(posTarget.y+motion.y),force,EntityBullet.GRAVITY/mass,0.98f);
		pp = MathHelper.clamp(pp, -90, 75);
		return new float[]{yy, pp};
	}

	@Override
	public void tick()
	{
		if(bulletsShot>=64)
		{
			reloadDelay++;
			if(reloadDelay >= 2)
			{
				bulletsShot = 0;
				reloadDelay = 0;
			}
		}

		if(shootDelay > 0)
			shootDelay--;
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		super.shoot(te);
		if(!te.getWorld().isRemote)
		{
			if(s2.isEmpty())
			{
				s2 = IIContent.itemAmmoAutocannon.getBulletWithParams("core_tungsten", "shaped", "hmx", "tracer_powder");
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("colour", 0xff0000);
				IIContent.itemAmmoAutocannon.setComponentNBT(s2, new NBTTagCompound(), tag);
			}

			te.getWorld().playSound(null, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), IISounds.machinegun_shot, SoundCategory.PLAYERS, 1.25f, 0.25f);
			EntityBullet a = BulletHelper.createBullet(te.getWorld(), s2, new Vec3d(te.getBlockPosForPos(49).up()).addVector(0.5, 0, 0.5), vv.scale(-1f), 6f);
			a.setShootPos(te.getAllBlocks());
			te.getWorld().spawnEntity(a);
		}
		shootDelay = Autocannon.bulletFireTime;
		bulletsShot++;
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
		return te.isDoorOpened&&shootDelay==0&&bulletsShot<64;
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
		float reloadAnim = Math.min(1, (this.reloadDelay+(bulletsShot >= 64?partialTicks: 0))/(float)Autocannon.reloadTime);

		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		double cannonAnim;
		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		float flaps = this.flaps;
		if(this.pitch < -20&&this.pitch > -75)
			flaps = Math.min(flaps+(0.075f*partialTicks), 1);
		else
			flaps = Math.max(flaps-(0.075f*partialTicks), 0);

		if(te.progress < Emplacement.lidTime)
			cannonAnim = 0;
		else
			cannonAnim = Math.abs((((te.getWorld().getTotalWorldTime()+partialTicks)%(Autocannon.bulletFireTime*4))/(double)(Autocannon.bulletFireTime*4))-0.5)/0.5;

		pp = pitch+Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks;
		yy = yaw+Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks;

		double b1 = 0, b2 = 0;
		if(te.isShooting&&te.progress > Emplacement.lidTime*0.98f)
		{
			if(cannonAnim < 0.5f)
			{
				b1 = (float)(cannonAnim/0.5f);
				if(canShoot(te))
					b2 = 1f-(cannonAnim/0.5f);
			}
			else
			{
				b1 = 1f-((cannonAnim-0.5f)/0.5f);
				b2 = ((cannonAnim-0.5f)/0.5f);
			}
		}

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel1Model)
			mod.rotationPointZ = (float)(-8f*b1);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel2Model)
			mod.rotationPointZ = (float)(-8f*b2);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel3Model)
			mod.rotationPointZ = (float)(-8f*b1);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel4Model)
			mod.rotationPointZ = (float)(-8f*b2);

		ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
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

		reloadAnim=Math.abs(reloadAnim-0.5f)*2;

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
				ins_y = 65f;
				ins_p2 = 105;
				ins_p1 = 40;
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
				ins_y = 65f;
				ins_p2 = 105;
				ins_p1 = 40;
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
		ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);

		int all = EmplacementRenderer.modelAutocannon.baseModel.length+EmplacementRenderer.modelAutocannon.turretModel.length+
				EmplacementRenderer.modelAutocannon.ammoBoxLidModel.length+EmplacementRenderer.modelAutocannon.turretTopFlapsModel.length+
				EmplacementRenderer.modelAutocannon.gunModel.length+EmplacementRenderer.modelAutocannon.barrel1Model.length+
				EmplacementRenderer.modelAutocannon.barrel2Model.length+EmplacementRenderer.modelAutocannon.barrel3Model.length+
				EmplacementRenderer.modelAutocannon.barrel4Model.length-1;

		int left = (int)((clientProgress/(float)IIContent.UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON.getProgressRequired())*all);

		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.baseModel.length); i++)
			EmplacementRenderer.modelAutocannon.baseModel[i].render();
		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.turretModel.length); i++)
			EmplacementRenderer.modelAutocannon.turretModel[i].render();

		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.ammoBoxLidModel.length); i++)
		{
			EmplacementRenderer.modelAutocannon.ammoBoxLidModel[i].rotateAngleZ = 0;
			EmplacementRenderer.modelAutocannon.ammoBoxLidModel[i].render();
		}

		EmplacementRenderer.modelAutocannon.turretTopFlapsModel[0].rotateAngleY = 0;
		EmplacementRenderer.modelAutocannon.turretTopFlapsModel[1].rotateAngleY = 0;
		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.turretTopFlapsModel.length); i++)
			EmplacementRenderer.modelAutocannon.turretTopFlapsModel[i].render();

		GlStateManager.translate(0, 1.125f, 0);
		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.gunModel.length); i++)
			EmplacementRenderer.modelAutocannon.gunModel[i].render();

		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.barrel1Model.length); i++)
		{
			EmplacementRenderer.modelAutocannon.barrel1Model[i].rotationPointZ = 0;
			EmplacementRenderer.modelAutocannon.barrel1Model[i].render();
		}
		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.barrel2Model.length); i++)
		{
			EmplacementRenderer.modelAutocannon.barrel2Model[i].rotationPointZ = 0;
			EmplacementRenderer.modelAutocannon.barrel2Model[i].render();
		}
		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.barrel3Model.length); i++)
		{
			EmplacementRenderer.modelAutocannon.barrel3Model[i].rotationPointZ = 0;
			EmplacementRenderer.modelAutocannon.barrel3Model[i].render();
		}
		for(int i = 0; i < Math.min(left--, EmplacementRenderer.modelAutocannon.barrel4Model.length); i++)
		{
			EmplacementRenderer.modelAutocannon.barrel4Model[i].rotationPointZ = 0;
			EmplacementRenderer.modelAutocannon.barrel4Model[i].render();
		}

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.98f, 0.98f, 0.98f);
		//GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
		//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

		ShaderUtil.blueprint_static(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.turretModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.ammoBoxLidModel)
			mod.render();

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.ammoBoxLidModel)
		{
			mod.rotateAngleZ = 0;
			mod.render();
		}

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.turretTopFlapsModel)
			mod.render();

		GlStateManager.translate(0, 1.125f, 0);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.gunModel)
			mod.render();

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel1Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel2Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel3Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel4Model)
			mod.render();

		ShaderUtil.releaseShader();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
