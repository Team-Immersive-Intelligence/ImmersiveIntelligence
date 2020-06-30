package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelPacker;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityPacker;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class PackerRenderer extends TileEntitySpecialRenderer<TileEntityPacker> implements IReloadableModelContainer<PackerRenderer>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelPacker model;
	private static ModelPacker modelFlipped;

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/packer.png";

	@Override
	public void render(TileEntityPacker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			ModelPacker modelCurrent = te.mirrored?modelFlipped: model;

			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			modelCurrent.render();


			ClientUtils.bindTexture("textures/atlas/blocks.png");
			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?-2: 1f, 1f, -2f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
			GlStateManager.translate(0, 0, -2);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
			GlStateManager.translate(0, 0, -1);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
			GlStateManager.translate(0, 0, -1);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", EnumFacing.NORTH);
			GlStateManager.popMatrix();


			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?-3: 0f, 0f, -3f);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.mirrored?EnumFacing.EAST: EnumFacing.WEST);
			GlStateManager.translate(1, 0, 0);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.mirrored?EnumFacing.EAST: EnumFacing.WEST);
			GlStateManager.translate(1, 0, 0);
			ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:covered", te.mirrored?EnumFacing.EAST: EnumFacing.WEST);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(3, 0.5f, -2.5);

			switch(te.animation)
			{
				case 0:
				{
					GlStateManager.translate(-((float)te.processTime/(float)Packer.conveyorTime)*1.5f, 0f, 0);
				}
				break;
				case 1:
				{
					GlStateManager.translate(-1.5f, 0f, 0f);
				}
				break;
				case 2:
				{
					GlStateManager.translate(-1.5f-((float)te.processTime/(float)Packer.conveyorTime*1.5f), 0f, 0);
				}
				break;
			}

			GlStateManager.scale(0.85, 0.85, 0.85);
			renderItem.renderItem(te.inventory.get(0), TransformType.NONE);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5f, 0.25f, 1f);

			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

			//ImmersiveIntelligence.logger.info(ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing));
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelPacker();
		modelFlipped = new ModelPacker();
		for(ModelRendererTurbo[] mod : modelFlipped.parts.values())
		{
			for(ModelRendererTurbo m : mod)
			{
				m.doMirror(true, false, false);
				if(!m.flip)
					m.setRotationPoint(-m.rotationPointX, m.rotationPointY, m.rotationPointZ);
				else
					m.setRotationPoint(m.rotationPointX, -m.rotationPointY, m.rotationPointZ);
				m.rotateAngleY *= -1;
			}

		}
	}
}
