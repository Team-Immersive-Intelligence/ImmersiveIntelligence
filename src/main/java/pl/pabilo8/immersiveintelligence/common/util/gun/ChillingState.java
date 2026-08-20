package pl.pabilo8.immersiveintelligence.common.util.gun;

import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Stores timing and progress for optional Emplacement idle animations.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.08.2026
 */
public class ChillingState implements INBTSerializable<NBTTagCompound>
{
	private final int minimumIdleTicks, repeatIntervalTicks, animationDuration;
	@Getter
	private int ticksSinceFireMission = 0, ticksSinceChill = 0;
	private int animationTick = -1;
	private boolean fireMissionActive = false, hasChilled = false;

	/**
	 * Creates an idle-animation state with fixed timing.
	 *
	 * @param minimumIdleTicks    idle time required before the first animation
	 * @param repeatIntervalTicks minimum time between idle animations
	 * @param animationDuration   animation duration in ticks
	 */
	public ChillingState(int minimumIdleTicks, int repeatIntervalTicks, int animationDuration)
	{
		this.minimumIdleTicks = Math.max(0, minimumIdleTicks);
		this.repeatIntervalTicks = Math.max(0, repeatIntervalTicks);
		this.animationDuration = Math.max(1, animationDuration);
	}

	/**
	 * Updates authoritative idle state.
	 *
	 * @param hasFireMission true while a fire mission is active
	 * @param canChill       true when the weapon can play its idle animation
	 * @return true when state must be synchronized with the client
	 */
	public boolean updateServer(boolean hasFireMission, boolean canChill)
	{
		boolean changed = fireMissionActive!=hasFireMission;
		fireMissionActive = hasFireMission;

		if(hasFireMission)
		{
			ticksSinceFireMission = 0;
			hasChilled = false;
			if(animationTick >= 0)
			{
				animationTick = -1;
				ticksSinceChill = 0;
				changed = true;
			}
		}
		else
			ticksSinceFireMission = increment(ticksSinceFireMission);

		ticksSinceChill = increment(ticksSinceChill);
		if(!canChill&&animationTick >= 0)
		{
			animationTick = -1;
			ticksSinceChill = 0;
			changed = true;
		}
		advanceAnimation();

		if(!hasFireMission&&canChill&&animationTick < 0&&ticksSinceFireMission >= minimumIdleTicks
				&&(!hasChilled||ticksSinceChill >= repeatIntervalTicks))
		{
			animationTick = 0;
			ticksSinceChill = 0;
			hasChilled = true;
			changed = true;
		}
		return changed;
	}

	/**
	 * Advances only synchronized counters and animation progress on the client.
	 */
	public void updateClient()
	{
		if(fireMissionActive)
			ticksSinceFireMission = 0;
		else
			ticksSinceFireMission = increment(ticksSinceFireMission);
		ticksSinceChill = increment(ticksSinceChill);
		advanceAnimation();
	}

	private void advanceAnimation()
	{
		if(animationTick < 0)
			return;
		if(++animationTick > animationDuration)
		{
			animationTick = -1;
			ticksSinceChill = 0;
		}
	}

	private int increment(int value)
	{
		return value==Integer.MAX_VALUE?value: value+1;
	}

	//--- Getters ---//

	/**
	 * @return idle animation progress in the 0.0-1.0 range, or 0 when inactive
	 */
	public float getProgress(float partialTicks)
	{
		if(animationTick < 0)
			return 0f;
		return MathHelper.clamp((animationTick+partialTicks)/Math.max(1f, animationDuration), 0f, 1f);
	}

	/**
	 * @return true while the chill animation is active
	 */
	public boolean isChilling()
	{
		return animationTick >= 0;
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("since_mission", ticksSinceFireMission);
		nbt.setInteger("since_chill", ticksSinceChill);
		nbt.setInteger("animation_tick", animationTick);
		nbt.setBoolean("mission_active", fireMissionActive);
		nbt.setBoolean("has_chilled", hasChilled);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		ticksSinceFireMission = Math.max(0, nbt.getInteger("since_mission"));
		ticksSinceChill = Math.max(0, nbt.getInteger("since_chill"));
		animationTick = nbt.hasKey("animation_tick")?nbt.getInteger("animation_tick"): -1;
		fireMissionActive = nbt.getBoolean("mission_active");
		hasChilled = nbt.getBoolean("has_chilled");
	}
}
