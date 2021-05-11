package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;

public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	int setupDelay = 0;

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
		return "infrared_observer";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return InfraredObserver.yawRotateTime;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return InfraredObserver.pitchRotateSpeed;
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		//Only pitch, no yaw rotation
		nextPitch = pitch;
		float p = pitch-this.pitch;
		this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.getPitchTurnSpeed());
		this.pitch = this.pitch%180;
	}

	public boolean isSetUp(boolean door)
	{
		return setupDelay==(door?InfraredObserver.setupTime: 0);
	}

	@Override
	public void doSetUp(boolean door)
	{
		if(door)
		{
			if(!isAimedAt(0, -90))
			{
				aimAt(0, -90);
				return;
			}
			if(setupDelay < InfraredObserver.setupTime)
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
	public void tick()
	{

	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("yaw", yaw);
		tag.setFloat("pitch", pitch);
		tag.setInteger("setupDelay", setupDelay);
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
		setupDelay = tagCompound.getInteger("setupDelay");
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
		float setupProgress = (MathHelper.clamp(setupDelay+(pitch==-90?(te.isDoorOpened?(te.progress==Emplacement.lidTime?partialTicks: 0): -partialTicks): 0), 0, InfraredObserver.setupTime)/(float)InfraredObserver.setupTime);

		float setupHalf = 1f-(Math.abs(0.5f-setupProgress)*2f);
		GlStateManager.enableBlend();


		ClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.baseModel)
			mod.render();

		GlStateManager.translate(0, 24/16f, 3/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.observerModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.hatchModel)
		{
			//1.57079633F
			if(setupProgress < 0.1)
				mod.rotateAngleX = 1.25f-(2.82079633f*(1f-(setupProgress/0.1f)));
			else if(setupProgress < 0.9f)
				mod.rotateAngleX = 1.25f;
			else if(setupProgress < 1f)
				mod.rotateAngleX = 1.25f-(((setupProgress-0.9f)/0.1f)*2.82079633f);
			else
				mod.rotateAngleX = -1.5707963f;

			mod.render();
		}

		if(setupProgress > 0.5f)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.lensModel)
				mod.render();

		float p1 = 95, p2 = 255, prog = 0.75f, h=0;
		if(setupProgress > 0.1f&&setupProgress < 0.9f)
		{
			if(setupHalf<0.4f)
			{
				p1=(1f-((setupHalf-0.2f)/0.2f))*95;
				p2=p1+160;
			}
			else if(setupHalf<0.6f)
			{
				p1=0;
				h=(setupHalf-0.4f)/0.2f*0.75f;
				p2=p1+160;
			}
			else
			{
				p1=((setupHalf-0.6f)/0.4f)*95;
				h=0.75f;
				p2=p1+160-(((setupHalf-0.6f)/0.4f)*85);

			}

			prog=(1f-(setupProgress>0.45f?(setupProgress<0.65f?((setupProgress-0.45f)/0.2f):1f):0f))*0.75f;
			//0.2, 0.2, 0.2, 0.4
		}

		GlStateManager.translate(0.3125, -0.625, -0.3751);
		GlStateManager.translate(0, 0, h);
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.rotate(180, 0, 1, 0);
		//-100, 90, 165
		//0 95

		EmplacementRenderer.renderInserter(false, 0, p1, p2, prog, setupProgress>0.5?INSERTER_ANIM_NONE:INSERTER_ANIM_LENS);

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER.getProgressRequired();
		final int l = EmplacementRenderer.modelInfraredObserverConstruction.length;
		double maxClientProgress = Utils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrench_upgrade_progress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		ClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f)/1f);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, (float)Math.min(scale, 1));
				GlStateManager.translate(0, scale*1.5f, 0);

				EmplacementRenderer.modelInfraredObserverConstruction[i].render(0.0625f);
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.popMatrix();
			}
			else
				EmplacementRenderer.modelInfraredObserverConstruction[i].render(0.0625f);
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
			EmplacementRenderer.modelInfraredObserverConstruction[i].render(0.0625f);
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
