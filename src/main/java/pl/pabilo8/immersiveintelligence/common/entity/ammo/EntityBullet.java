package pl.pabilo8.immersiveintelligence.common.entity.ammo;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer.MultipleTracerBuilder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Pabilo8 on 30-08-2019.
 * Yes, I stole this one from Flan's Mod too! (Thanks Flan!)
 * Major update on 08-03-2020.
 * Total rewrite on 31-10-2020, almost no old code left
 */
public class EntityBullet extends Entity implements IEntityAdditionalSpawnData
{
	public static final int MAX_TICKS = 600;
	public static final float DRAG = 0.01f;
	public static final float GRAVITY = 0.15f;

	//For testing purposes
	public static float DEV_SLOMO = 1f;
	public static boolean DEV_DECAY = true;

	public IAmmoItem bullet;
	public IAmmoCore core;
	public EnumCoreTypes coreType;
	public EnumFuseTypes fuseType = EnumFuseTypes.CONTACT;
	public int fuseParameter = 0;
	public IAmmoComponent[] components = new IAmmoComponent[0];
	public NBTTagCompound[] componentNBT = new NBTTagCompound[0];

	protected Entity shooter = null;

	boolean isPainted = false;
	//Set true only for artillery type bullets, forge can't give the mod unlimited tickets for each entity
	boolean shouldLoadChunks = false;
	//Additional motion variables, multiplied by force, that can decrease when the bullet hits an obstacle
	public double baseMotionX = 0, baseMotionY = 0, baseMotionZ = 0;

	//Whether the bullet has already played its flyby sound
	boolean flybySound = false;

	public double gravityMotionY = 0;

	/*
	Paint color and fuse is -1 if not existant
	 */
	public int paintColor = -1, fuse = -1;
	/*
	Penetration hardness is the base hardness the bullet core can penetrate
	Base damage is density*initial damage (NOT MASS)
	On hit, they are multiplied by core type bonuses, so a softpoint bullet has less penetration than an AP round, but has more damage vs unarmored targets
	 */
	public float penetrationHardness = 1, force = 1, initialForce = 1, baseDamage = 0, mass = 0;

	ArrayList<Entity> hitEntities = new ArrayList<>();
	ArrayList<BlockPos> hitPos = new ArrayList<>();

	protected boolean wasSynced;
	private Ticket ticket = null;

	public EntityBullet(World worldIn)
	{
		super(worldIn);
		hitEntities.add(this);
		setSize(0.5f, 0.5f);
		wasSynced = !worldIn.isRemote;
	}

	/**
	 * This is a general method, use {@link IIAmmoUtils#createBullet(World, ItemStack, Vec3d, Vec3d, float)} inside your code
	 */
	public EntityBullet(World worldIn, ItemStack stack, double x, double y, double z, float velocityMod, double motionX, double motionY, double motionZ)
	{
		super(worldIn);

		fromStack(stack);

		this.setPosition(x, y, z);
		this.force = bullet.getDefaultVelocity()*velocityMod;
		this.initialForce = bullet.getDefaultVelocity();
		this.baseMotionX = motionX;
		this.baseMotionY = motionY;
		this.baseMotionZ = motionZ;

		setMotion();

		//Should not be called on client
		if(!world.isRemote&&shouldLoadChunks)
		{
			ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, this.getEntityWorld(), Type.ENTITY);
			if(ticket!=null)
				ticket.bindEntity(this);
		}
	}

	public void setShooters(@Nullable Entity shooter, Entity... others)
	{
		this.shooter = shooter;
		hitEntities.add(shooter);
		if(shooter!=null&&shooter.isRiding())
		{
			hitEntities.add(shooter.getLowestRidingEntity());
			hitEntities.addAll(shooter.getLowestRidingEntity().getRecursivePassengers());
		}
		hitEntities.addAll(Arrays.asList(others));
	}

	public void setShootPos(BlockPos... others)
	{
		hitPos.addAll(Arrays.asList(others));
	}

	private void setMotion()
	{
		this.motionX = baseMotionX*DEV_SLOMO*force;
		this.motionY = (baseMotionY*force+gravityMotionY)*DEV_SLOMO;
		this.motionZ = baseMotionZ*DEV_SLOMO*force;
	}

	protected void fromStack(ItemStack stack)
	{
		if(stack.getItem() instanceof IAmmoItem)
		{
			bullet = (IAmmoItem)stack.getItem();
			core = bullet.getCore(stack);
			coreType = bullet.getCoreType(stack);
			fuseType = bullet.getFuseType(stack);
			fuseParameter = bullet.getFuseParameter(stack);
			components = bullet.getComponents(stack);
			componentNBT = bullet.getComponentsNBT(stack);
			paintColor = bullet.getPaintColor(stack);

			if(fuseType==EnumFuseTypes.TIMED)
				fuse = fuseParameter;

			refreshBullet();
		}
	}

	private void refreshBullet()
	{
		if(core==null||bullet==null||coreType==null)
		{
			setDead();
			return;
		}
		penetrationHardness = fuseType==EnumFuseTypes.PROXIMITY?1: core.getPenetrationHardness();
		double compMass = 1d+Arrays.stream(components).mapToDouble(IAmmoComponent::getDensity).sum();
		compMass += core.getDensity();
		baseDamage = (float)(bullet.getDamage()*compMass*core.getDamageModifier());

		shouldLoadChunks = bullet.shouldLoadChunks();

		mass = bullet.getCoreMass(core, components);

		if(paintColor==-1)
			isPainted = false;

		setBulletSize();

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(isDead)
			return;

		if(!world.isRemote&&ticksExisted==1)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			IIPacketHandler.sendToClient(this, new MessageEntityNBTSync(this, nbt));
		}
		else if(world.isRemote&&!wasSynced)
			return;

		if(world.isRemote)
		{
			prevRotationPitch = this.rotationPitch;
			prevRotationYaw = this.rotationYaw;
		}
		else
		{
			if((!shouldLoadChunks&&ticksExisted > MAX_TICKS&&DEV_DECAY)||(posY < 0))
			{
				setDead();
				return;
			}
		}

		//flight and hit detection code

		if(fuse!=-1&&ticksExisted > fuse)
		{
			performEffect(new RayTraceResult(this));
			return;
		}

		setMotion();
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		setPosition(posX, posY, posZ);

		//Play artillery shell flyby sound
		if(Weapons.artilleryImpactSound&&!flybySound&&ticksExisted > 5&&bullet.shouldLoadChunks())
		{
			if(motionY < 0)
			{
				BlockPos top = world.getTopSolidOrLiquidBlock(new BlockPos(getNextPositionVector())).up();
				IIPacketHandler.playRangedSound(world, new Vec3d(top), IISounds.artilleryImpact, SoundCategory.PLAYERS, 24, 0.75f, 1.3f);
				flybySound = true;
			}
		}

		if(world.isRemote)
		{
			if(penetrationHardness!=0)
			{
				Vec3d normalized = new Vec3d(motionX, motionY, motionZ).normalize();
				float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
				this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/3.1415927410125732D);
				this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/3.1415927410125732D);

				for(int j = 0; j < components.length; j++)
				{
					IAmmoComponent c = components[j];
					c.spawnParticleTrail(this, componentNBT[j]);
				}
			}
		}
		else
			ForgeChunkManager.forceChunk(this.ticket, this.world.getChunkFromBlockCoords(this.getPosition()).getPos());
	}

	private void stopAtPoint(RayTraceResult hit)
	{
		gravityMotionY = 0;
		penetrationHardness = 0;
		baseMotionX = 0;
		baseMotionY = 0;
		baseMotionZ = 0;
		setPositionAndUpdate(hit.hitVec.x, hit.hitVec.y+height, hit.hitVec.z);
		setMotion();

		if(!world.isRemote)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			nbt.setBoolean("stopped", true);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, nbt), IIPacketHandler.targetPointFromEntity(this, 32));
		}
	}

	private void ricochet(float force, BlockPos currentPos)
	{
		this.force = force;
		baseMotionX *= -1;
		baseMotionY *= -1;
		hitEntities.clear();
		hitEntities.add(this);
		hitPos.clear();
		hitPos.add(currentPos);
	}

	protected void performEffect(RayTraceResult hit)
	{
		setDead();
		setPosition(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z);
		float str = bullet.getComponentMultiplier()*core.getExplosionModifier()*coreType.getComponentEffectivenessMod();
		for(int i = 0; i < components.length; i++)
		{
			if(components[i]!=null&&i < componentNBT.length&&componentNBT[i]!=null)
				components[i].onEffect(world, hit.hitVec, new Vec3d(baseMotionX, baseMotionY, baseMotionZ).normalize(), str, componentNBT[i], coreType,
						);
		}
	}

	public Entity getShooter()
	{
		return shooter;
	}

	public Vec3d getNextPositionVector()
	{
		return getPositionVector().addVector(motionX, motionY, motionZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("basemotion_x"))
		{
			this.baseMotionX = compound.getDouble("basemotion_x");
			this.baseMotionY = compound.getDouble("basemotion_y");
			this.baseMotionZ = compound.getDouble("basemotion_z");
			this.force = compound.getFloat("force");
			this.initialForce = compound.getFloat("initalForce");

			float motionXZ = MathHelper.sqrt(baseMotionX*baseMotionX+baseMotionZ*baseMotionZ);
			this.rotationYaw = (float)((Math.atan2(baseMotionX, baseMotionZ)*180D)/3.1415927410125732D);
			this.rotationPitch = -(float)((Math.atan2(baseMotionY, motionXZ)*180D)/3.1415927410125732D);
		}

		if(compound.hasKey("casing"))
			this.bullet = IIAmmoRegistry.getBulletItem(compound.getString("casing"));
		if(compound.hasKey("core"))
			this.core = IIAmmoRegistry.getCore(compound.getString("core"));
		if(compound.hasKey("core_type"))
			this.coreType = EnumCoreTypes.v(compound.getString("core_type"));
		if(compound.hasKey("fuse_type"))
			this.fuseType = EnumFuseTypes.v(compound.getString("fuse_type"));
		if(compound.hasKey("fuse_parameter"))
			this.fuseParameter = compound.getInteger("fuse_parameter");

		if(compound.hasKey("components"))
		{
			ArrayList<IAmmoComponent> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)compound.getTag("components");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(IIAmmoRegistry.getComponent(components.getStringTagAt(i)));
			this.components = arrayList.toArray(new IAmmoComponent[0]);
		}

		if(compound.hasKey("component_nbt"))
		{
			ArrayList<NBTTagCompound> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)compound.getTag("component_nbt");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(components.getCompoundTagAt(i));
			this.componentNBT = arrayList.toArray(new NBTTagCompound[0]);
		}

		if(compound.hasKey("paint_color"))
			this.paintColor = compound.getInteger("paint_color");

		refreshBullet();

		if(compound.hasKey("stopped"))
		{
			gravityMotionY = 0;
			penetrationHardness = 0;
			baseMotionX = 0;
			baseMotionY = 0;
			baseMotionZ = 0;
		}

		if(world.isRemote)
		{
			wasSynced = true;
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("casing", bullet.getName());
		compound.setString("core", core.getName());
		compound.setString("core_type", coreType.getName());
		compound.setString("fuse_type", fuseType.getName());
		compound.setInteger("fuse_parameter", fuseParameter);

		NBTTagList tagList = new NBTTagList();
		Arrays.stream(components).map(IAmmoComponent::getName).map(NBTTagString::new).forEachOrdered(tagList::appendTag);
		if(tagList.tagCount() > 0)
			compound.setTag("components", tagList);

		tagList = new NBTTagList();
		Arrays.stream(componentNBT).forEachOrdered(tagList::appendTag);
		if(tagList.tagCount() > 0)
			compound.setTag("component_nbt", tagList);

		compound.setInteger("paint_color", paintColor);

		compound.setDouble("basemotion_x", this.baseMotionX);
		compound.setDouble("basemotion_y", this.baseMotionY);
		compound.setDouble("basemotion_z", this.baseMotionZ);
		compound.setFloat("force", this.force);
		compound.setFloat("initalForce", this.initialForce);

	}

	private void playHitSound(HitEffect effect, World world, BlockPos pos, IPenetrationHandler handler)
	{
		IBlockState state = world.getBlockState(pos);
		SoundEvent event = handler.getSpecialSound(effect);
		if(event==null)
		{
			SoundType type = state.getBlock().getSoundType(state, world, pos, this);
			event = effect==HitEffect.IMPACT?type.getBreakSound(): type.getStepSound();
		}
		MinecraftServer server = world.getMinecraftServer();
		if(server!=null)
			server.getPlayerList().sendToAllNearExcept(null, posX, posY, posZ, 24D, this.world.provider.getDimension(), new SPacketSoundEffect(event, SoundCategory.BLOCKS, posX, posY, posZ, 0.5f, 1f));
	}

	public Vec3d getBaseMotion()
	{
		return new Vec3d(baseMotionX, baseMotionY, baseMotionZ);
	}

	private void setBulletSize()
	{
		float cal = bullet.getCaliber()/16f;
		this.width = cal;
		this.height = cal;
		cal *= 16f;
		setEntityBoundingBox(new AxisAlignedBB(-cal, -cal, -cal, cal, cal, cal));
	}
}
