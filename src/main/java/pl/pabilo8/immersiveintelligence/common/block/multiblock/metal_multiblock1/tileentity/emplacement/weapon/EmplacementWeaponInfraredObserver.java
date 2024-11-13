package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IInfraredProtectionEquipment;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	private AxisAlignedBB vision;
	int setupDelay = 0;
	boolean requiresPlatformRefill = false;

	private static final Runnable INSERTER_ANIM_LENS = () -> {
		IIClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);
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
			if(!isAimedAt(yaw, -90))
			{
				aimAt(yaw, -90);
				return;
			}
			if(setupDelay < InfraredObserver.setupTime)
				setupDelay += 1;
		}
		else
		{
			if(!isAimedAt(yaw, -90))
			{
				aimAt(yaw, -90);
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
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		EnumFacing facing = EnumFacing.fromAngle(MathHelper.wrapDegrees(yaw));
		Vec3i vv = facing.getDirectionVec();
		Vec3i vv2 = facing.rotateY().getDirectionVec();
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5)
				.expand(vv.getX()*InfraredObserver.detectionRadius, 0, vv.getZ()*InfraredObserver.detectionRadius)
				.grow(vv2.getX()*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius, vv2.getZ()*InfraredObserver.detectionRadius);
		if(firstTime)
			te.sendAttackSignal = true;
	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{

	}

	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound tag = super.saveToNBT(forClient);
		tag.setInteger("setupDelay", setupDelay);
		tag.setBoolean("requiresPlatformRefill", requiresPlatformRefill);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		setupDelay = tagCompound.getInteger("setupDelay");
		requiresPlatformRefill = tagCompound.getBoolean("requiresPlatformRefill");
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return te.isDoorOpened;
	}

	@Override
	public void handleDataPacket(DataPacket packet)
	{
		super.handleDataPacket(packet);
		String c = packet.getPacketVariable('c').toString();
		if(c.equals("facing"))
		{
			DataType f = packet.getPacketVariable('f');
			if(f instanceof DataTypeInteger)
				nextYaw = EnumFacing.getHorizontal(((DataTypeInteger)f).value).getHorizontalAngle();
			else if(f instanceof DataTypeString)
			{
				EnumFacing facing = EnumFacing.byName(f.toString());
				if(facing==EnumFacing.NORTH||facing==EnumFacing.SOUTH)
					facing = facing.getOpposite();
				if(facing!=null)
					nextYaw = facing.getHorizontalAngle();
			}

			if(nextYaw!=yaw)
				requiresPlatformRefill = true;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileEntityEmplacement te, float partialTicks)
	{
		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		p = this.nextPitch-this.pitch;
		boolean power = te.energyStorage.getEnergyStored() >= getEnergyUpkeepCost();
		pp = pitch+(power?(Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks*getPitchTurnSpeed()): 0);
		yy = yaw;//+(power?(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks*getYawTurnSpeed()):0);
		float setupProgress = (MathHelper.clamp(setupDelay+(pitch==-90?(te.isDoorOpened?(te.progress==Emplacement.lidTime?partialTicks: 0): -partialTicks): 0), 0, InfraredObserver.setupTime)/(float)InfraredObserver.setupTime);

		float setupHalf = 1f-(Math.abs(0.5f-setupProgress)*2f);
		GlStateManager.enableBlend();


		IIClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);

		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.baseModel)
			mod.render();

		GlStateManager.translate(0, 24/16f, 3/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
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

		float p1 = 95, p2 = 255, prog = 0.75f, h = 0;
		if(setupProgress > 0.1f&&setupProgress < 0.9f)
		{
			if(setupHalf < 0.4f)
			{
				p1 = (1f-((setupHalf-0.2f)/0.2f))*95;
				p2 = p1+160;
			}
			else if(setupHalf < 0.6f)
			{
				p1 = 0;
				h = (setupHalf-0.4f)/0.2f*0.75f;
				p2 = p1+160;
			}
			else
			{
				p1 = ((setupHalf-0.6f)/0.4f)*95;
				h = 0.75f;
				p2 = p1+160-(((setupHalf-0.6f)/0.4f)*85);

			}

			prog = (1f-(setupProgress > 0.45f?(setupProgress < 0.65f?((setupProgress-0.45f)/0.2f): 1f): 0f))*0.75f;
			//0.2, 0.2, 0.2, 0.4
		}

		GlStateManager.translate(0.3125, -0.625, -0.3751);
		GlStateManager.translate(0, 0, h);
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.rotate(180, 0, 1, 0);
		//-100, 90, 165
		//0 95

		EmplacementRenderer.renderInserter(false, 0, p1, p2, prog, setupProgress > 0.5?INSERTER_ANIM_NONE: INSERTER_ANIM_LENS);

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpgradeProgress(int clientProgress, int serverProgress, float partialTicks)
	{
		GlStateManager.pushMatrix();

		final int req = IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER.getProgressRequired();
		final int l = EmplacementRenderer.modelInfraredObserverConstruction.length;
		double maxClientProgress = IIUtils.getMaxClientProgress(serverProgress, req, l);

		double cc = (int)Math.min(clientProgress+((partialTicks*(Tools.wrenchUpgradeProgress/2f))), maxClientProgress);
		double progress = MathHelper.clamp(cc/req, 0, 1);

		IIClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);
		for(int i = 0; i < l*progress; i++)
		{
			if(1+i > Math.round(l*progress))
			{
				GlStateManager.pushMatrix();
				double scale = 1f-(((progress*l)%1f));
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

		ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
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
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 0.25f,
				new Vec3d(0, 0.25, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "backBox", 0.5f, 0.75f,
				new Vec3d(0.75, 0.75, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "observeBox", 1.25f, 1.25f,
				new Vec3d(-0.25, 1.5, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "observeBox", 0.5f, 0.5f,
				new Vec3d(-0.25, 1.5, 0), new Vec3d(-1, 0, -0.25), 4));
		list.add(new EmplacementHitboxEntity(entity, "observeBox", 0.25f, 0.25f,
				new Vec3d(-0.25, 1.5, 0), new Vec3d(-1.325, 0, -0.25), 4));

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

	}

	@Override
	public void performPlatformRefill(TileEntityEmplacement te)
	{
		float y = MathHelper.wrapDegrees(360+nextYaw-this.yaw);
		if(Math.abs(y) < this.getYawTurnSpeed()*0.5f)
			this.yaw = this.nextYaw;
		else
			this.yaw = MathHelper.wrapDegrees(this.yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.getYawTurnSpeed())));

		if(yaw==nextYaw)
		{
			requiresPlatformRefill = false;
			syncWithClient(te);
		}

		EnumFacing facing = EnumFacing.fromAngle(MathHelper.wrapDegrees(yaw)).getOpposite();
		Vec3i vv = facing.getDirectionVec();
		Vec3i vv2 = facing.rotateY().getDirectionVec();
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5)
				.expand(vv.getX()*InfraredObserver.detectionRadius, 0, vv.getZ()*InfraredObserver.detectionRadius)
				.grow(vv2.getX()*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius, vv2.getZ()*InfraredObserver.detectionRadius);
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return InfraredObserver.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return InfraredObserver.maxHealth;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected Tuple<ResourceLocation, List<ModelRendererTurbo>> getDebris()
	{
		return new Tuple<>(EmplacementRenderer.textureInfraredObserver, Arrays.asList(EmplacementRenderer.modelInfraredObserverConstruction));
	}

	@Override
	public boolean canSeeEntity(Entity entity)
	{
		return StreamSupport.stream(entity.getArmorInventoryList().spliterator(), false).noneMatch(stack -> stack.getItem() instanceof IInfraredProtectionEquipment&&((IInfraredProtectionEquipment)stack.getItem()).invisibleToInfrared(stack));
	}
}
