package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.entities.EntityChemthrowerShot;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.HeavyChemthrower;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;

import javax.annotation.Nullable;

public class EmplacementWeaponHeavyChemthrower extends EmplacementWeapon
{
	int setupDelay = 0;
	float shootDelay = 0;
	boolean shouldIgnite = false;
	FluidTank tank = new FluidTank(8000);
	private Vec3d vv;
	SidedFluidHandler fluidHandler = new SidedFluidHandler(this);

	private static final Runnable INSERTER_ANIM_LENS = () -> {
		ClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);
		GlStateManager.translate(-0.3125, 0.4225, 1.375);
		GlStateManager.rotate(180, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.lensModel)
			mod.render();
	};

	private static final Runnable INSERTER_ANIM_NONE = () -> {
	};

	@Override
	public String getName()
	{
		return "heavy_chemthrower";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return HeavyChemthrower.yawRotateSpeed;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return HeavyChemthrower.pitchRotateSpeed;
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		super.aimAt(yaw,pitch);
	}

	public boolean isSetUp(boolean door)
	{
		return setupDelay==(door?HeavyChemthrower.setupTime: 0);
	}

	@Override
	public void doSetUp(boolean door)
	{
		if(door)
		{
			if(setupDelay < HeavyChemthrower.setupTime)
				setupDelay += 1;
		}
		else
		{
			if(!isAimedAt(0, -90))
			{
				aimAt(0, -90);
				return;
			}
			if(setupDelay > 0)
				setupDelay -= 1;
		}
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		// TODO: 08.03.2021 make it work 
		vv = posTurret.subtract(posTarget);
		float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
		Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z);
		float motionTime = (float)Math.abs(motionVec.lengthSquared());
		motionVec = motionVec.scale(motionTime);

		vv = vv.add(motionVec).subtract(0, getStackMass()*motionXZ, 0).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0,vv.y,0)))));
		pp = MathHelper.clamp(pp, -90, 75);
		return new float[]{yy, pp};
	}

	@Override
	public void tick()
	{
		if(shootDelay > 0)
			shootDelay--;
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		Vec3d gun = te.getWeaponCenter().add(vv.scale(-3));
		super.shoot(te);
		float range = 5;
		float scatter = 0.025f;
		//4mB per shot
		int split = Math.min(tank.getFluidAmount()/4,6);

		if(tank.getFluid()==null)
		{
			tank.fill(new FluidStack(IEContent.fluidCreosote, 1000),true);
		}
		else if(!te.getWorld().isRemote)
		{
			for(int i = 0; i < split; i++)
			{
				Vec3d vecDir = vv.scale(-1f).normalize().scale(2).add(new Vec3d(Utils.RAND.nextGaussian()*scatter, Utils.RAND.nextGaussian()*scatter, Utils.RAND.nextGaussian()*scatter));

				Vec3d g1 = gun.add(vv.rotateYaw(90).scale(0.25f));
				Vec3d g2 = gun.add(vv.rotateYaw(-90).scale(0.25f));

				EntityChemthrowerShot chem = new EntityChemthrowerShot(te.getWorld(), g1.x, g1.y, g1.z, vecDir.x, vecDir.y, vecDir.z, tank.getFluid());
				EntityChemthrowerShot chem2 = new EntityChemthrowerShot(te.getWorld(), g2.x, g2.y, g2.z, vecDir.x, vecDir.y, vecDir.z, tank.getFluid());

				// Apply momentum from the player.
				chem.motionX = vecDir.x;
				chem.motionY = vecDir.y;
				chem.motionZ = vecDir.z;
				chem2.motionX = vecDir.x;
				chem2.motionY = vecDir.y;
				chem2.motionZ = vecDir.z;

				tank.drain(4,true);

				if(shouldIgnite)
				{
					chem.setFire(10);
					chem2.setFire(10);
				}
				te.getWorld().spawnEntity(chem);
				te.getWorld().spawnEntity(chem2);
			}
		}
		shootDelay = Autocannon.bulletFireTime;
		//bulletsShot++;
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("yaw", yaw);
		tag.setFloat("pitch", pitch);
		tag.setInteger("setupDelay", setupDelay);
		tag.setTag("tank",tank.writeToNBT(new NBTTagCompound()));
		return tag;
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return shootDelay<=0&&te.isDoorOpened&&tank.getFluidAmount()>0;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		yaw = tagCompound.getFloat("yaw");
		pitch = tagCompound.getFloat("pitch");
		setupDelay = tagCompound.getInteger("setupDelay");
		tank.readFromNBT(tagCompound.getCompoundTag("tank"));
	}

	@Nullable
	@Override
	public IFluidHandler getFluidHandler(boolean in)
	{
		return in?fluidHandler:null;
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
		float setupProgress = 1f-(MathHelper.clamp(setupDelay+(pitch==-90?(te.isDoorOpened?(te.progress==Emplacement.lidTime?partialTicks: 0): -partialTicks): 0), 0, HeavyChemthrower.setupTime)/(float)HeavyChemthrower.setupTime);

		ClientUtils.bindTexture(EmplacementRenderer.textureHeavyChemthrower);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.baseModel)
			mod.render();


		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.turretModel)
			mod.render();
		GlStateManager.translate(4.5f/16f, 14/16f, 4/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelStartModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelMidModel)
		{
			mod.rotateAngleX=0.00000001f;
			mod.hasOffset=true;
			mod.offsetZ=(MathHelper.clamp((setupProgress-0.5f)/0.5f,0,1f)*-9);
			mod.render();
		}

		GlStateManager.disableCull();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelEndModel)
		{
			mod.rotateAngleX=0.00000001f;
			mod.hasOffset=true;
			mod.offsetZ=(Math.min(setupProgress/0.5f,1f)*-9)+(MathHelper.clamp((setupProgress-0.5f)/0.5f,0,1f)*-9);
			mod.render();
		}
		GlStateManager.enableCull();

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER.getProgressRequired();
		final int l = EmplacementRenderer.modelHeavyChemthrowerConstruction.length;
		double maxClientProgress = pl.pabilo8.immersiveintelligence.api.Utils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrench_upgrade_progress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		ClientUtils.bindTexture(EmplacementRenderer.textureHeavyChemthrower);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f)/1f);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				EmplacementRenderer.modelHeavyChemthrowerConstruction[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				EmplacementRenderer.modelHeavyChemthrowerConstruction[i].render(0.0625f);
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
			EmplacementRenderer.modelHeavyChemthrowerConstruction[i].render(0.0625f);
		}

		ShaderUtil.releaseShader();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	private double getStackMass()
	{
		if(tank.getFluid()!=null)
			return (tank.getFluid().getFluid().isGaseous()?0.025F: 0.05F)*(float)(tank.getFluid().getFluid().getDensity(tank.getFluid()) < 0?-1: 1);
		else
			return 0;
	}

	static class SidedFluidHandler implements IFluidHandler
	{
		EmplacementWeaponHeavyChemthrower barrel;

		SidedFluidHandler(EmplacementWeaponHeavyChemthrower barrel)
		{
			this.barrel = barrel;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null)
				return 0;
			return barrel.tank.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			if(resource==null)
				return null;
			return this.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return barrel.tank.drain(maxDrain, doDrain);
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return barrel.tank.getTankProperties();
		}
	}
}
