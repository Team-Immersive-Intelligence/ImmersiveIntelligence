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
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmo;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @ii-approved
 * @since 30.01.2024
 */
@Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public abstract class EntityAmmoBase extends Entity implements IEntityAdditionalSpawnData, IEntityLightEventConsumer
{
	//--- Properties ---//
	/**
	 * The ammo type
	 */
	protected IAmmo bullet;
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
		bullet = null;
	}

	@Override
	protected void entityInit()
	{

	}

	public void setFromStack(@Nonnull ItemStack stack)
	{
		//Only applies if the stack is an ammo item
		if(!(stack.getItem() instanceof IAmmoItem))
			return;
		IAmmoItem<?> bullet = (IAmmoItem<?>)stack.getItem();

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
	public void setFromParameters(IAmmo bullet, IAmmoCore core, EnumCoreTypes coreType, EnumFuseTypes fuseType, int fuseParameter,
								  List<Tuple<IAmmoComponent, NBTTagCompound>> components)
	{
		this.bullet = bullet;
		this.core = core;
		this.coreType = coreType;
		this.fuseType = fuseType;
		this.fuseParameter = fuseParameter;
		this.components = components;
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
		super.onUpdate();
		if(shouldDecay())
			setDead();
	}

	protected void detonate()
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
		return 0;
	}

	protected abstract Vec3d getDirection();

	//--- NBT ---//

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{

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
					.radius(bullet.getComponentMultiplier()*16f)
					.color(color, false)
					.build());
		}
	}
}
