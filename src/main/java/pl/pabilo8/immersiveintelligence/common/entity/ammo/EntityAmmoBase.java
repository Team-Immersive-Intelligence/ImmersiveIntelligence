package pl.pabilo8.immersiveintelligence.common.entity.ammo;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 30.01.2024
 */
@Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public abstract class EntityAmmoBase<T extends EntityAmmoBase<? super T>> extends Entity implements IEntityAdditionalSpawnData, IEntityLightEventConsumer
{
	//--- Properties ---//
	/**
	 * The ammo type
	 */
	protected IAmmoType<?, T> ammoType;
	/**
	 * The ammo core
	 */
	protected IAmmoCore core;
	/**
	 * The ammo core type
	 */
	protected EnumCoreTypes coreType;
	/**
	 * The fuse type
	 */
	protected EnumFuseTypes fuseType;
	/**
	 * The fuse parameter, for a timer fuse it's the fuse time in ticks, for a proximity fuse it's the fuse range in blocks<br>
	 * Not used by impact fuses
	 */
	protected int fuseParameter = 0;
	/**
	 * The paint colour of the bullet, in rgbInt format
	 */
	protected int paintColour = -1;
	/**
	 * List of component tuples, containing the component and its NBT (can be empty but not null)
	 */
	protected List<Tuple<IAmmoComponent, NBTTagCompound>> components;
	/**
	 * The owner of this bullet, used for statistics
	 */
	protected Entity owner;

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
		IAmmoComponent[] components = bullet.getComponents(stack);
		NBTTagCompound[] componentsNBT = bullet.getComponentsNBT(stack);

		//Create a list of tuples for the components
		List<Tuple<IAmmoComponent, NBTTagCompound>> list = new ArrayList<>();
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
	public void setFromParameters(IAmmoType<?, T> ammoType, IAmmoCore core, EnumCoreTypes coreType, EnumFuseTypes fuseType, int fuseParameter,
								  List<Tuple<IAmmoComponent, NBTTagCompound>> components)
	{
		this.ammoType = ammoType;
		this.core = core;
		this.coreType = coreType;
		this.fuseType = fuseType;
		this.fuseParameter = fuseParameter;
		this.components = components;
		this.setSize(ammoType.getCaliber()/2f, ammoType.getCaliber()/2f);
	}

	/**
	 * @param owner The owner of this bullet, used for statistics
	 */
	public void setOwner(@Nullable Entity owner)
	{
		this.owner = owner;
	}

	//--- Update ---//

	@Override
	public void onUpdate()
	{
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;

		if(shouldDecay())
			setDead();
	}

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
			for(Tuple<IAmmoComponent, NBTTagCompound> component : components)
				component.getFirst().onEffect(world, pos, dir, multiplier, tag, coreType, owner);
			setDead();
		}
	}

	protected float getComponentMultiplier()
	{
		return core.getExplosionModifier()*ammoType.getComponentMultiplier()*coreType.getComponentEffectivenessMod();
	}

	@Nonnull
	protected abstract Vec3d getDirection();

	//--- NBT ---//

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		setFromParameters(
				(IAmmoType<?, T>)IIAmmoRegistry.getAmmoItem(compound.getString("ammoType")),
				IIAmmoRegistry.getCore(compound.getString("core")),
				EnumCoreTypes.values()[compound.getInteger("coreType")],
				EnumFuseTypes.values()[compound.getInteger("fuseType")],
				compound.getInteger("fuseParameter"),
				compound.getTagList("components", 10).tagList.stream().map(t ->
				{
					NBTTagCompound nbt = (NBTTagCompound)t;
					return new Tuple<>(IIAmmoRegistry.getComponent(nbt.getString("component")), nbt.getCompoundTag("nbt"));
				}).collect(Collectors.toList())
		);
		owner = world.getEntityByID(compound.getInteger("owner"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("ammoType", ammoType.getName());
		compound.setString("core", core.getName());
		compound.setInteger("coreType", coreType.ordinal());
		compound.setInteger("fuseType", fuseType.ordinal());
		compound.setInteger("fuseParameter", fuseParameter);
		compound.setInteger("paintColour", paintColour);
		compound.setTag("components", components.stream().map(t ->
				EasyNBT.newNBT()
						.withString("component", t.getFirst().getName())
						.withTag("nbt", t.getSecond())
						.unwrap()
		).collect(new NBTTagCollector()));
		compound.setInteger("owner", owner==null?-1: owner.getEntityId());

	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeEntityToNBT(compound);
		ByteBufUtils.writeTag(buffer, compound);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound compound = ByteBufUtils.readTag(additionalData);
		if(compound!=null)
			readEntityFromNBT(compound);
	}

	//--- Abstract ---//
	protected abstract boolean shouldDecay();

	//--- Misc ---//

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public void setFire(int seconds)
	{

	}

	//--- Getters ---//

	public IAmmoType<?, T> getAmmoType()
	{
		return ammoType;
	}

	public IAmmoCore getCore()
	{
		return core;
	}

	public EnumCoreTypes getCoreType()
	{
		return coreType;
	}

	public EnumFuseTypes getFuseType()
	{
		return fuseType;
	}

	public int getFuseParameter()
	{
		return fuseParameter;
	}

	public List<Tuple<IAmmoComponent, NBTTagCompound>> getComponents()
	{
		return components;
	}

	public int getPaintColour()
	{
		return paintColour;
	}

	public Entity getOwner()
	{
		return owner;
	}

	//--- Mirage Compat ---//

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent evt, Entity entity)
	{
		for(Tuple<IAmmoComponent, NBTTagCompound> component : components)
		{
			int color = component.getFirst().getNBTColour(component.getSecond());

			evt.add(Light.builder().pos(this)
					.radius(ammoType.getComponentMultiplier()*16f)
					.color(color, false)
					.build());
		}
	}
}
