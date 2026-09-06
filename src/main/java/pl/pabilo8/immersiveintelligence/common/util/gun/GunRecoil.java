package pl.pabilo8.immersiveintelligence.common.util.gun;

import blusunrize.immersiveengineering.common.util.Utils;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Used to store and calculate gun recoil and overheating.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 16.08.2026
 * @since 15.05.2026
 */
public class GunRecoil implements INBTSerializable<NBTTagCompound>
{
	//--- Angles ---//
	private float recoilYaw = 0, recoilPitch = 0;
	private float maxRecoilPitch = 45f, maxRecoilYaw = 45f;
	private float recoilStrengthVertical = 0.7f, recoilStrengthHorizontal = 0.3f, recoilDamping = 0.97f, recoilRandomness = 0.05f;

	//--- Overheating ---//
	@Getter
	private boolean isOverheated = false;
	private float overheat = 0;
	private float maxOverheat = 0, overheatDecrease = 1, overheatStep = 0.5f;
	@Nullable
	private Supplier<FluidTank> coolantTank = null;
	private int coolantPerTick = 10;

	public GunRecoil()
	{

	}

	public void update()
	{
		//Apply damping to recoil, and gradually return it to 0
		this.recoilYaw *= recoilDamping;
		this.recoilPitch *= recoilDamping;

		if(Math.abs(recoilYaw) < 0.01f)
			recoilYaw = 0;
		if(Math.abs(recoilPitch) < 0.01f)
			recoilPitch = 0;

		//Decrease overheat
		if(overheat > 0)
		{
			overheat -= overheatDecrease;
			if(coolantTank!=null&&MachinegunCoolantHandler.isValidCoolant(coolantTank.get().getFluid()))
			{
				FluidStack drained = coolantTank.get().drain(coolantPerTick, true);
				assert drained!=null;
				overheat -= MachinegunCoolantHandler.getCoolAmount(coolantTank.get().getFluid())*((float)drained.amount/coolantPerTick);
			}
		}

		this.overheat = Math.max(0, overheat);
		this.isOverheated = (overheat!=0)&&(isOverheated||overheat > maxOverheat);
	}

	//--- With ---//

	public GunRecoil withRecoilLimits(float maxRecoilYaw, float maxRecoilPitch)
	{
		this.maxRecoilYaw = maxRecoilYaw;
		this.maxRecoilPitch = maxRecoilPitch;
		return this;
	}

	public GunRecoil withRecoilStrength(float recoilStrengthVertical, float recoilStrengthHorizontal, float recoilDamping, float recoilRandomness)
	{
		this.recoilStrengthVertical = recoilStrengthVertical;
		this.recoilStrengthHorizontal = recoilStrengthHorizontal;
		this.recoilDamping = recoilDamping;
		this.recoilRandomness = recoilRandomness;
		return this;
	}

	public GunRecoil withOverheating(float maxOverheat, float overheatDecrease, float overheatStep)
	{
		this.maxOverheat = maxOverheat;
		this.overheatDecrease = overheatDecrease;
		this.overheatStep = overheatStep;
		return this;
	}

	public GunRecoil withCoolantTank(@Nullable Supplier<FluidTank> coolantTank, int coolantPerTick)
	{
		this.coolantTank = coolantTank;
		this.coolantPerTick = coolantPerTick;
		return this;
	}

	//--- Adding Recoil ---//

	public void addRecoil()
	{
		//Add recoil strength to current recoil and clamp the value
		this.recoilPitch = MathHelper.clamp(recoilPitch+recoilStrengthVertical*(1f+recoilRandomness*(float)(Utils.RAND.nextGaussian()*2-1)),
				-maxRecoilPitch, maxRecoilPitch);
		this.recoilYaw = MathHelper.clamp(recoilYaw+recoilStrengthHorizontal*(1f+recoilRandomness*(float)(Utils.RAND.nextGaussian()*2-1)),
				-maxRecoilYaw, maxRecoilYaw);

		//Add overheat
		this.overheat = Math.min(maxOverheat, overheat+4f);
		this.isOverheated = overheat==maxOverheat;
	}

	//--- Getters ---//

	public float getRecoilYaw(float partialTicks)
	{
		return (float)IIMath.clampedLerp(recoilYaw*recoilDamping, recoilYaw, 1f-partialTicks);
	}

	public float getRecoilPitch(float partialTicks)
	{
		return (float)IIMath.clampedLerp(recoilPitch*recoilDamping, recoilPitch, 1f-partialTicks);
	}

	public float getOverheat(float partialTicks)
	{
		return MathHelper.clamp((overheat+(isOverheated?0.5f: 0))/maxOverheat, 0, 1);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("recoil_yaw", recoilYaw);
		nbt.setFloat("recoil_pitch", recoilPitch);
		nbt.setFloat("overheat", overheat);
		nbt.setBoolean("is_overheated", isOverheated);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		recoilYaw = nbt.getFloat("recoil_yaw");
		recoilPitch = nbt.getFloat("recoil_pitch");
		overheat = nbt.getFloat("overheat");
		isOverheated = nbt.getBoolean("is_overheated");
	}
}
