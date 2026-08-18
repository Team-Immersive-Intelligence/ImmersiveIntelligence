package pl.pabilo8.immersiveintelligence.common.entity.ammo;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 30.01.2024
 */
@Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public abstract class EntityAmmoBase<T extends EntityAmmoBase<? super T>> extends Entity implements IEntityLightEventConsumer, ISyncNBTEntity<EntityAmmoBase<T>>
{
	//--- Properties ---//
	/**
	 * The ammo type
	 */
	@Getter
	protected IAmmoType<?, T> ammoType;
	/**
	 * The ammo core
	 */
	@Getter
	protected AmmoCore core;
	/**
	 * The ammo core type
	 */
	@Getter
	protected CoreType coreType;
	/**
	 * The fuse type
	 */
	@Getter
	protected FuseType fuseType;
	/**
	 * The fuse parameter, for a timer fuse it's the fuse time in ticks, for a proximity fuse it's the fuse range in blocks<br>
	 * Not used by impact fuses
	 */
	@Getter
	protected int fuseParameter = 0;
	/**
	 * The paint color of the bullet, in rgbInt format
	 */
	@Getter
	protected IIColor paintColor = null;
	/**
	 * List of component tuples, containing the component and its NBT (can be empty but not null)
	 */
	@Getter
	protected List<Tuple<AmmoComponent, NBTTagCompound>> components;
	/**
	 * The owner of this bullet, used for statistics
	 */
	@Getter
	protected Entity owner;
	/**
	 * Axis alligned bounding box of the bullet, because fuck minecraft's bloody AABB (de)sync wankfest.
	 */
	protected AxisAlignedBB aabb;
	protected boolean clientLoaded = false;

	//--- Initialization ---//

	public EntityAmmoBase(World world)
	{
		super(world);
		ammoType = null;
	}

	@Override
	protected void entityInit()
	{

	}

	public void setFromStack(@Nonnull ItemStack stack)
	{
		//Only applies if the stack is an ammo item
		if(!(stack.getItem() instanceof IAmmoTypeItem))
			return;
		IAmmoTypeItem<?, T> bullet = (IAmmoTypeItem<?, T>)stack.getItem();

		//NBT can be null, but components can't
		AmmoComponent[] components = bullet.getComponents(stack);
		NBTTagCompound[] componentsNBT = bullet.getComponentsNBT(stack);

		//Create a list of tuples for the components
		List<Tuple<AmmoComponent, NBTTagCompound>> list = new ArrayList<>();
		for(int i = 0; i < components.length; i++)
			//Add the component and its NBT or an empty NBT if it's null
			list.add(new Tuple<>(components[i], componentsNBT[i]==null?new NBTTagCompound(): componentsNBT[i]));

		//Set properties
		setFromParameters(bullet,
				bullet.getCore(stack),
				bullet.getCoreType(stack),
				bullet.getFuseType(stack), bullet.getFuseParameter(stack),
				list
		);

	}

	@ParametersAreNonnullByDefault
	public void setFromParameters(IAmmoType<?, T> ammoType, AmmoCore core, CoreType coreType, FuseType fuseType, int fuseParameter,
	                              List<Tuple<AmmoComponent, NBTTagCompound>> components)
	{
		this.ammoType = ammoType;
		this.core = core;
		this.coreType = coreType;
		this.fuseType = fuseType;
		this.fuseParameter = fuseParameter;
		this.components = components;
		this.height = this.width = Math.max(0.25f, (ammoType.getCaliber()/16f));
		float fraction = height/2f;
		this.aabb = new AxisAlignedBB(-fraction, -fraction, -fraction, fraction, fraction, fraction);
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	@Override
	public void setPosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		float fraction = this.width/2.0F;
		this.setEntityBoundingBox(new AxisAlignedBB(x-fraction, y-fraction, z-fraction, x+fraction, y+fraction, z+fraction));
	}

	@Override
	public void resetPositionToBB()
	{
		AxisAlignedBB box = this.getEntityBoundingBox();
		this.posX = (box.minX+box.maxX)/2.0D;
		this.posY = (box.minY+box.maxY)/2.0D; // We use center instead of minY
		this.posZ = (box.minZ+box.maxZ)/2.0D;
	}

	@Override
	public void onUpdate()
	{
		if(world.isRemote&&!clientLoaded)
			return;

		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;

		if(shouldDecay())
			setDead();
	}

	//--- Update ---//

	public void detonate()
	{
		if(!world.isRemote)
		{
			//Get the values
			Vec3d dir = getDirection();
			Vec3d pos = new Vec3d(posX, posY, posZ);
			float multiplier = getComponentMultiplier();

			//Get the NBT of the bullet
			NBTTagCompound tag = new NBTTagCompound();
			writeEntityToNBT(tag);

			//Call the effect method on all components
			for(Tuple<AmmoComponent, NBTTagCompound> component : components)
				component.getFirst().onEffect(world, pos, dir,
						coreType.getEffectShape(), tag, ammoType.getComponentSize(), multiplier, owner);
			setDead();
		}
	}

	protected float getComponentMultiplier()
	{
		return core.getExplosionModifier()*coreType.getComponentEffectivenessMod();
	}

	@Nonnull
	protected abstract Vec3d getDirection();

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		setFromParameters(
				(IAmmoType<?, T>)AmmoRegistry.getAmmoItem(compound.getString("ammoType")),
				AmmoRegistry.getCore(compound.getString("core")),
				CoreType.values()[compound.getInteger("coreType")],
				FuseType.values()[compound.getInteger("fuseType")],
				compound.getInteger("fuseParameter"),
				compound.getTagList("components", 10).tagList.stream().map(t ->
				{
					NBTTagCompound nbt = (NBTTagCompound)t;
					return new Tuple<>(AmmoRegistry.getComponent(nbt.getString("component")), nbt.getCompoundTag("nbt"));
				}).collect(Collectors.toList())
		);
		owner = world.getEntityByID(compound.getInteger("owner"));
	}

	//--- NBT ---//

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("ammoType", ammoType.getName());
		compound.setString("core", core.getName());
		compound.setInteger("coreType", coreType.ordinal());
		compound.setInteger("fuseType", fuseType.ordinal());
		compound.setInteger("fuseParameter", fuseParameter);
		compound.setInteger("paintColor", paintColor==null?-1: paintColor.getPackedRGB());
		compound.setTag("components", components.stream().map(t ->
				EasyNBT.newNBT()
						.withString("component", t.getFirst().getName())
						.withTag("nbt", t.getSecond())
						.unwrap()
		).collect(NBTTagCollector.collect()));
		compound.setInteger("owner", owner==null?-1: owner.getEntityId());

	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound tag = ByteBufUtils.readTag(additionalData);
		if(tag!=null)
		{
			readEntityFromNBT(tag);
			this.clientLoaded = true;
		}
	}

	//--- Abstract ---//
	protected abstract boolean shouldDecay();

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	//--- Misc ---//

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public void setFire(int seconds)
	{

	}

	//--- Setters ---//

	/**
	 * @param owner The owner of this bullet, used for statistics
	 */
	public void setOwner(@Nullable Entity owner)
	{
		this.owner = owner;
	}

	//--- Mirage Compat ---//

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent evt, Entity entity)
	{
		components.stream().filter(component -> component.getFirst().isGlowing())
				.map(component -> component.getFirst().getColor(component.getSecond()))
				.map(color -> Light.builder().pos(this)
						.radius(ammoType.getComponentSize()*16f)
						.color(color.red/255f, color.green/255f, color.red/255f, color.alpha/255f)
						.build()
				).forEach(evt::add);
	}
}
