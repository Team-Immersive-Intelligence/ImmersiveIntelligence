package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyChemthrower;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmplacementWeaponHeavyChemthrower extends EmplacementWeapon
{
	private AxisAlignedBB vision;
	int setupDelay = 0;
	float shootDelay = HeavyChemthrower.sprayTime;
	boolean shouldIgnite = false;
	FluidTank tank = new FluidTank(HeavyChemthrower.tankCapacity);
	private Vec3d vv;
	SidedFluidHandler fluidHandler = new SidedFluidHandler(this);

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
		super.aimAt(yaw, pitch);
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
	public boolean requiresPlatformRefill()
	{
		return false;
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		shouldIgnite = true;
		float force = 4f;

		vv = posTurret.subtract(posTarget.add(motion));

		double dist = vv.distanceTo(new Vec3d(0, vv.y, 0));

		double gravity = 0;
		FluidStack fluid = tank.getFluid();
		if(fluid!=null)
		{
			boolean isGas = fluid.getFluid().isGaseous()||ChemthrowerHandler.isGas(fluid.getFluid());
			gravity = (isGas?.025f: .05F)*(fluid.getFluid().getDensity(fluid) < 0?-1: 1);
		}
		double initialY = vv.normalize().y, motionY = initialY;
		while(dist > 0)
		{
			dist -= force;
			force *= 0.99;
			motionY *= 0.99;
			motionY -= gravity;
		}

		vv = vv.addVector(0, motionY-initialY, 0).normalize();

		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));

		return new float[]{yy, pp};
	}

	@Override
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(HeavyChemthrower.detectionRadius);
	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{
		if(shootDelay > 0)
			shootDelay--;
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		super.shoot(te);

		Vec3d gun = te.getWeaponCenter().add(vv.scale(-3.5));
		super.shoot(te);
		float range = 4;

		float scatter = 0.025f;
		//4mB per shot
		int split = Math.min(tank.getFluidAmount()/4, 6);

		if(tank.getFluid()==null)
			tank.fill(new FluidStack(IEContent.fluidCreosote, 1000), true);
		else if(!te.getWorld().isRemote)
		{
			Vec3d g1 = gun.add(vv.rotateYaw(90).scale(0.25f));
			Vec3d g2 = gun.add(vv.rotateYaw(-90).scale(0.25f));
			List<BlockPos> allBlocks = te.getAllBlocks();

			for(int i = 0; i < split; i++)
			{
				Vec3d vecDir = vv.scale(-1.0f).normalize().scale(range).add(new Vec3d(Utils.RAND.nextGaussian()*scatter, Utils.RAND.nextGaussian()*scatter, Utils.RAND.nextGaussian()*scatter));

				EntityIIChemthrowerShot chem = new EntityIIChemthrowerShot(te.getWorld(), g1.x, g1.y, g1.z, vecDir.x, vecDir.y, vecDir.z, tank.getFluid())
						.withShooters(allBlocks)
						.withMotion(vecDir);
				EntityIIChemthrowerShot chem2 = new EntityIIChemthrowerShot(te.getWorld(), g2.x, g2.y, g2.z, vecDir.x, vecDir.y, vecDir.z, tank.getFluid())
						.withShooters(allBlocks)
						.withMotion(vecDir);

				if(shouldIgnite)
				{
					chem.setFire(10);
					chem2.setFire(10);
				}

				te.getWorld().spawnEntity(chem);
				te.getWorld().spawnEntity(chem2);
			}
			tank.drain(4*split, true);
		}
		shootDelay = Autocannon.bulletFireTime;
		//bulletsShot++;
	}

	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound tag = super.saveToNBT(forClient);
		tag.setInteger("setupDelay", setupDelay);
		tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		setupDelay = tagCompound.getInteger("setupDelay");
		tank.readFromNBT(tagCompound.getCompoundTag("tank"));
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return vv!=null&&shootDelay <= 0&&te.isDoorOpened&&tank.getFluidAmount() > 0;
	}

	@Nullable
	@Override
	public IFluidHandler getFluidHandler(boolean in)
	{
		return in?fluidHandler: null;
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
		float setupProgress = 1f-(MathHelper.clamp(setupDelay+(pitch==-90?(te.isDoorOpened?(te.progress==Emplacement.lidTime?partialTicks: 0): -partialTicks): 0), 0, HeavyChemthrower.setupTime)/(float)HeavyChemthrower.setupTime);

		IIClientUtils.bindTexture(EmplacementRenderer.textureHeavyChemthrower);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.baseModel)
			mod.render();


		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.turretModel)
			mod.render();
		GlStateManager.translate(0.28125f, 0.875f, 0.25f);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelStartModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelMidModel)
		{
			mod.rotateAngleX = 0.00000001f;
			mod.hasOffset = true;
			mod.offsetZ = (MathHelper.clamp((setupProgress-0.5f)/0.5f, 0, 1f)*-9);
			mod.render();
		}

		GlStateManager.disableCull();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelEndModel)
		{
			mod.rotateAngleX = 0.00000001f;
			mod.hasOffset = true;
			mod.offsetZ = (Math.min(setupProgress/0.5f, 1f)*-9)+(MathHelper.clamp((setupProgress-0.5f)/0.5f, 0, 1f)*-9);
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
		double maxClientProgress = IIUtils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrenchUpgradeProgress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		IIClientUtils.bindTexture(EmplacementRenderer.textureHeavyChemthrower);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f));
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
		GlStateManager.translate(0.03125f, 0f, -0.01325f);
		//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

		ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
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
		{
			entity.aabb = new AxisAlignedBB(-3, 0, -3, 3, 3, 3);
			if((setupDelay!=0&&setupDelay!=HeavyChemthrower.setupTime)&&entity.ticksExisted%20==0)
			{
				entity.partArray = getCollisionBoxes();
			}
		}
	}

	@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)

		float t = this.setupDelay/(float)HeavyChemthrower.setupTime;

		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1.25f,
				new Vec3d(0, 0.5, 0), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "baseBoxTop", 0.5f, 0.25f,
				new Vec3d(-0.25, 1.25, 0.25), Vec3d.ZERO, 12));
		list.add(new EmplacementHitboxEntity(entity, "baseBoxTop", 0.5f, 0.25f,
				new Vec3d(-0.25, 1.25, -0.25), Vec3d.ZERO, 12));

		//Increase amount of B A R R E L S
		list.add(new EmplacementHitboxEntity(entity, "barrelLeft", 0.5f, 1,
				new Vec3d(0.75, 0.8125, 0.5), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "barrelRight", 0.5f, 1,
				new Vec3d(0.75, 0.8125, -0.5), Vec3d.ZERO, 4));

		for(float f = -0.25f; f <= 0.25f; f += 0.5f)
		{
			list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
					new Vec3d(0, 0.8125, -0.25f), new Vec3d(-0.8125, 0, 0), 12));

			if(t > 0.35f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-1.125, 0, 0), 12));
			if(t > 0.5f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-1.4375, 0, 0), 12));
			if(t > 0.65f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-1.75, 0, 0), 12));
			if(t > 0.75f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.3125f, 0.3125f,
						new Vec3d(0, 0.8125, f), new Vec3d(-2.0625, 0, 0), 12));
			if(t > 0.9f)
				list.add(new EmplacementHitboxEntity(entity, "gunBarrelLeft", 0.425f, 0.425f,
						new Vec3d(0, 0.8125, f), new Vec3d(-2.487500011920929, 0, 0), 12));
		}

		return list.toArray(new EmplacementHitboxEntity[0]);
	}

	@Override
	public NonNullList<ItemStack> getBaseInventory()
	{
		return NonNullList.create();
	}

	@Override
	public void renderStorageInventory(GuiEmplacementPageStorage gui, int mx, int my, float partialTicks, boolean first)
	{
		//gui.drawString(gui.mc.fontRenderer, "turururu", gui.getGuiLeft(), gui.getGuiTop(), 0);
		if(first)
		{
			gui.bindIcons();
			gui.drawTexturedModalRect(gui.getGuiLeft()+4, gui.getGuiTop()+18, 0, 0, 20, 50);
			ClientUtils.handleGuiTank(tank, gui.getGuiLeft()+6, gui.getGuiTop()+20,
					16, 46, 20, 0, 20, 50,
					mx, my, gui.TEXTURE_ICONS.toString(), null);
		}
		else
		{
			ArrayList<String> tooltip = new ArrayList<>();
			if(IIMath.isPointInRectangle(gui.getGuiLeft()+6, gui.getGuiTop()+20, gui.getGuiLeft()+6+20, gui.getGuiTop()+20+50, mx, my))
				ClientUtils.handleGuiTank(tank, gui.getGuiLeft()+6, gui.getGuiTop()+20,
						16, 46, 20, 0, 20, 50,
						mx, my, gui.TEXTURE_ICONS.toString(), tooltip);

			if(!tooltip.isEmpty())
			{
				ClientUtils.drawHoveringText(tooltip, mx-gui.getGuiLeft(), my-gui.getGuiTop(), gui.mc.fontRenderer, gui.getGuiLeft()+gui.getXSize(), -1);
				RenderHelper.enableGUIStandardItemLighting();
			}
		}
	}

	@Override
	public void performPlatformRefill(TileEntityEmplacement te)
	{

	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyChemthrower.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyChemthrower.maxHealth;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected Tuple<ResourceLocation, List<ModelRendererTurbo>> getDebris()
	{
		return new Tuple<>(EmplacementRenderer.textureHeavyChemthrower, Arrays.asList(EmplacementRenderer.modelHeavyChemthrowerConstruction));
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
