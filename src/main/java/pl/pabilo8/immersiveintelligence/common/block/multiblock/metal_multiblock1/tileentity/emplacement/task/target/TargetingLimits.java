package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import net.minecraft.util.math.MathHelper;

/**
 * Defines validation limits for Emplacement target configuration.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public final class TargetingLimits
{
	public static final int MAX_PRESETS = 16;
	public static final int MAX_NODES = 128;
	public static final int MAX_TREE_DEPTH = 12;
	public static final int MAX_DIRECT_CHILDREN = 16;
	public static final int MAX_STRING_LENGTH = 64;
	public static final int MAX_NODE_ID_LENGTH = 64;
	public static final int MAX_WEIGHT = 1_000_000;
	public static final int MAX_FIRE_MISSIONS = 64;
	public static final int MAX_SHOTS = 1_000_000;
	public static final double MAX_DISTANCE = 4096.0;

	private TargetingLimits()
	{
	}

	public static int clampWeight(int weight)
	{
		return MathHelper.clamp(weight, -MAX_WEIGHT, MAX_WEIGHT);
	}

	public static String clampString(String value)
	{
		String safe = value==null?"": value;
		return safe.length() <= MAX_STRING_LENGTH?safe: safe.substring(0, MAX_STRING_LENGTH);
	}

	public static double clampDistance(double value)
	{
		if(Double.isNaN(value)||Double.isInfinite(value))
			return 0;
		return Math.max(0, Math.min(MAX_DISTANCE, value));
	}
}
