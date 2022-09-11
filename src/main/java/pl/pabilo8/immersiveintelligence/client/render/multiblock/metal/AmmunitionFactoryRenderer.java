package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityAmmunitionFactory;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class AmmunitionFactoryRenderer extends TileEntitySpecialRenderer<TileEntityAmmunitionFactory>
{
	private static final ModelAmmunitionFactory model = new ModelAmmunitionFactory();
	private static final ModelInserter modelInserter = new ModelInserter();

	@Override
	public void render(@Nullable TileEntityAmmunitionFactory te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.setLightmapDisabled(false);
			String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/ammunition_factory.png";
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);

			if(Minecraft.isAmbientOcclusionEnabled())
				GlStateManager.shadeModel(7425);
			else
				GlStateManager.shadeModel(7424);

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, false);
			model.render();

			GlStateManager.pushMatrix();
			//GlStateManager.scale(0.99f,0.99f,0.99f);
			GlStateManager.translate(0f, 0.9375f, -1f);
			ClientUtils.bindTexture("textures/atlas/blocks.png");

			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);
			GlStateManager.translate(0f, 0f, -1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.SOUTH);

			GlStateManager.translate(0f, 0f, -1f);

			GlStateManager.pushMatrix();
			IConveyorBelt con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);
			List<BakedQuad> quads = ModelConveyor.getBaseConveyor(EnumFacing.SOUTH, 1, new Matrix4(EnumFacing.SOUTH), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(con.getActiveTexture()), new boolean[]{false, true}, new boolean[]{true, true}, null, 0);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);
			GlStateManager.popMatrix();

			GlStateManager.translate(1f, 0f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.WEST);
			GlStateManager.translate(1f, 0f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.WEST);

			GlStateManager.translate(1f, 0f, 0f);
			GlStateManager.pushMatrix();
			quads = ModelConveyor.getBaseConveyor(EnumFacing.WEST, 1, new Matrix4(EnumFacing.WEST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(con.getActiveTexture()), new boolean[]{false, true}, new boolean[]{true, true}, null, 0);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0f, 0.0625f, -1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.WEST);
			GlStateManager.translate(-1f, 0f, 0f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.WEST);
			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
			GlStateManager.translate(0f, 0f, 1f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			String textureInserter = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/inserter.png";
			ClientUtils.bindTexture(textureInserter);

			GlStateManager.popMatrix();
		}
	}
}
