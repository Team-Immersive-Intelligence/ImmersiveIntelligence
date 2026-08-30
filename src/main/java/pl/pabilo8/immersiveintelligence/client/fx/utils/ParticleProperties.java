package pl.pabilo8.immersiveintelligence.client.fx.utils;

import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.function.Supplier;

/**
 * Defines values that particle factories, programs, and particles can exchange.
 * Properties use a common NBT representation for resource files and network data.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.12.2024
 **/
public enum ParticleProperties implements ISerializableEnum
{
	//--- Movement Properties ---//
	PREVIOUS_POSITION(Vector3f.class, true, Vector3f::new),
	POSITION(Vector3f.class, Vector3f::new),
	MOTION(Vector3f.class, Vector3f::new),
	ROTATION(Vector2f.class, Vector2f::new),
	ROTATION_PITCH(float.class, true, () -> 0f),
	ROTATION_YAW(float.class, true, () -> 0f),
	STRETCH(Vector3f.class, Vector3f::new),
	SCALE(float.class, () -> 1f),
	SIZE(float.class, () -> 1f),

	//--- Common Properties ---//
	LIFETIME(int.class, () -> 0),
	MAX_LIFETIME(int.class, () -> 20),
	PROGRESS(float.class, true, () -> 0f),
	IS_ALIVE(boolean.class, true, () -> true),
	ON_GROUND(boolean.class, true, () -> false),

	//--- Color ---//
	COLOR(IIColor.class, () -> IIColor.WHITE),
	COLOR_SECONDARY(IIColor.class, () -> IIColor.WHITE),
	RED(float.class, true, () -> 1f),
	GREEN(float.class, true, () -> 1f),
	BLUE(float.class, true, () -> 1f),
	ALPHA(float.class, true, () -> 0f),

	//--- Textures ---//
	TEXTURES(ResourceLocation[].class, () -> new ResourceLocation[0]),
	TEXTURES_COUNT(int.class, true, () -> 1),
	TEXTURE_SHIFT(int.class, true, () -> 0),

	//--- System ---//
	DRAW_STAGE(ParticleDrawStages.class, () -> ParticleDrawStages.CUSTOM),
	AABB(AxisAlignedBB.class, () -> new AxisAlignedBB(-0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.25f));

	private final Class<?> propertyClass;
	private final Supplier<?> defaultValue;
	private final boolean nbt;


	<T> ParticleProperties(Class<T> propertyClass, Supplier<T> defaultValue)
	{
		this.propertyClass = propertyClass;
		this.defaultValue = defaultValue;
		this.nbt = false;
	}

	ParticleProperties(Class<?> propertyClass, boolean getterOnly, Supplier<?> defaultValue)
	{
		this.propertyClass = propertyClass;
		this.nbt = getterOnly;
		this.defaultValue = defaultValue;
	}

	@SuppressWarnings("unchecked")
	public <T> T getPropertyFromNBT(EasyNBT nbt)
	{
		if(propertyClass==Vector3f.class)
			return (T)nbt.getVector3f(this.getName());
		else if(propertyClass==Vector2f.class)
			return (T)nbt.getVector2f(this.getName());
		else if(propertyClass==float.class)
			return (T)(Float)nbt.getFloat(this.getName());
		else if(propertyClass==int.class)
			return (T)(Integer)nbt.getInt(this.getName());
		else if(propertyClass==boolean.class)
			return (T)(Boolean)nbt.getBoolean(this.getName());
		else if(propertyClass==ResourceLocation[].class)
			return (T)nbt.streamList(NBTTagString.class, this.getName())
					.map(NBTTagString::getString)
					.map(ResourceLocation::new)
					.toArray(ResourceLocation[]::new);
		else if(propertyClass==IIColor.class)
			return (T)IIColor.fromHex(nbt.getString(this.getName()));
		else if(propertyClass==ParticleDrawStages.class)
			return (T)nbt.getEnum(this.getName(), ParticleDrawStages.class);
		else if(propertyClass==AxisAlignedBB.class)
		{
			double side = nbt.getDouble(this.getName());
			return (T)new AxisAlignedBB(-side, -side, -side, side, side, side);
		}
		else if(propertyClass==ParticleProgram.class)
			return (T)ParticleRegistry.getProgram(nbt.getString(this.getName()));
		else
			return getDefault();
	}

	public <T> void setPropertyToNBT(EasyNBT nbt, T value)
	{
		if(propertyClass==Vector3f.class)
			nbt.withVec3d(this.getName(), (Vector3f)value);
		else if(propertyClass==Vector2f.class)
			nbt.withVec2d(this.getName(), (Vector2f)value);
		else if(propertyClass==float.class)
			nbt.withFloat(this.getName(), (Float)value);
		else if(propertyClass==int.class)
			nbt.withInt(this.getName(), (Integer)value);
		else if(propertyClass==boolean.class)
			nbt.withBoolean(this.getName(), (Boolean)value);
		else if(propertyClass==ResourceLocation[].class)
			nbt.withList(this.getName(), o -> new NBTTagString(o.toString()), (ResourceLocation[])value);
		else if(propertyClass==IIColor.class)
			nbt.withString(this.getName(), ((IIColor)value).getHexARGB());
		else if(propertyClass==ParticleDrawStages.class)
			nbt.withEnum(this.getName(), (ParticleDrawStages)value);
		else if(propertyClass==AxisAlignedBB.class)
			nbt.withDouble(this.getName(), ((AxisAlignedBB)value).getAverageEdgeLength());
		else if(propertyClass==ParticleProgram.class)
			nbt.withString(this.getName(), ((ParticleProgram)value).getProgramName());
		else
			throw new IllegalArgumentException("Unsupported property class: "+propertyClass);
	}

	public <T> T getDefault()
	{
		return (T)defaultValue.get();
	}

	public boolean isNBT()
	{
		return nbt;
	}
}
