package pl.pabilo8.immersiveintelligence.common.util.sound;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.util.carversound.TimedCompoundSound;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Stores playing sound data for a tile entity
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.08.2025
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SoundHandler
{
	private final List<TimedCompoundSound> current = new ArrayList<>();
	private final Supplier<BlockPos> position;

	public SoundHandler(TileEntity tileEntity)
	{
		this.position = tileEntity::getPos;
	}

	public List<TimedCompoundSound> getCurrentPlayingList()
	{
		return current;
	}

	public BlockPos getPosition()
	{
		return position.get();
	}
}
