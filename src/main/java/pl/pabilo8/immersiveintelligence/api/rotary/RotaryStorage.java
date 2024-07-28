/*
 * Minecraft Forge
 * Copyright (c) 2016-2018.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * Reference implementation of {@link IEnergyStorage}. Use/extend this or implement your own.
 * <p>
 * Derived from the Redstone Flux power system designed by King Lemming and originally utilized in Thermal Expansion and related mods.
 * Created with consent and permission of King Lemming and Team CoFH. Released with permission under LGPL 2.1 when bundled with Forge.
 */
public class RotaryStorage implements IRotaryEnergy, INBTSerializable<NBTTagCompound>
{
	protected float torque = 0, speed = 0;

	public RotaryStorage()
	{
	}

	public RotaryStorage(float torque, float speed)
	{
		this.torque = torque;
		this.speed = speed;
	}

	@Override
	public float getTorque()
	{
		return this.torque;
	}

	@Override
	public void setTorque(float torque)
	{
		this.torque = torque;
	}

	@Override
	public float getRotationSpeed()
	{
		return this.speed;
	}

	@Override
	public void setRotationSpeed(float rpm)
	{
		this.speed = rpm;
	}

	/**
	 * @param facing of the TileEntity, null when not important
	 * @return side type
	 * @implNote WHEN IMPLEMENTING ENERGY IN MOTOR BELT CONNECTOR, DO NOT SET TO INPUT OR OUTPUT,
	 * THEY ARE DIFFERENT FROM EVERY OTHER ROTATIONAL ENERGY TILEENTITY!
	 * And yes, caps was needed here.
	 */
	@Override
	public RotationSide getSide(@Nullable EnumFacing facing)
	{
		return RotationSide.NONE;
	}

	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return toNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		fromNBT(nbt);
	}
}