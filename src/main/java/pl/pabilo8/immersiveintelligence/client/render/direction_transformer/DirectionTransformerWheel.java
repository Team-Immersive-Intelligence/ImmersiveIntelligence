package pl.pabilo8.immersiveintelligence.client.render.direction_transformer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.util.tmt.DirectionTransformer;

/**
 * @author Pabilo8
 * @since 04-01-2020
 */
public class DirectionTransformerWheel extends DirectionTransformer
{
	@Override
	public void transformTileDirection(EnumFacing facing)
	{
		//GlStateManager.translate(-0.5f,0,-0.5f);
		switch(facing)
		{
			case NORTH:
			{
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 0f);
			}
			break;
			case SOUTH:
			{
				GlStateManager.translate(0, 0f, 1f);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-1f, 0f, 1f);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(270F, 0F, 1F, 0F);

			}
			break;
			case UP:
			{
				GlStateManager.rotate(90F, 0F, 0F, 1F);
			}
			break;
			case DOWN:
			{
				GlStateManager.rotate(270F, 1F, 0F, 0F);
			}
			break;
		}
		//GlStateManager.translate(0.5f,0,0.5f);
	}
}
