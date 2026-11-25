package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie;

/**
 * Provides a 4-tube Nixie display.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.10.2026
 */
public class TileEntityQuadNixieDisplay extends TileEntityNixieDisplayBase
{
	@Override
	public int getTubeCount()
	{
		return 4;
	}
}
