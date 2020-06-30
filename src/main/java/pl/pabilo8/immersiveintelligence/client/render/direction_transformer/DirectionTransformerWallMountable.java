package pl.pabilo8.immersiveintelligence.client.render.direction_transformer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.tmt.DirectionTransformer;

/**
 * @author Pabilo8
 * @since 04-01-2020
 */
public class DirectionTransformerWallMountable extends DirectionTransformer
{
	@Override
	public void transformTileDirection(EnumFacing facing)
	{
		GlStateManager.translate(-0.5f, 0, 0.5f);
		switch(facing)
		{
			case UP:
			{
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(-2, -1, 0);
			}
			break;
			case DOWN:
			{
				//model.rotateAll(0f, 0f, 0f);
			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.translate(0, -0.5, -0.5);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(270, 1, 0, 0);
				GlStateManager.translate(0, -0.5, 0.5);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.translate(1.5, -1, -0.5);
				GlStateManager.rotate(90, 0, 0, 1);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.translate(0.5, 1, -0.5);
				GlStateManager.rotate(-90, 0, 0, 1);
			}
			break;
		}
		GlStateManager.translate(0.5f, 0, 0.5f);
	}
}
