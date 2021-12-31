package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Connector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityInserter;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
public class InserterRenderer extends TileEntitySpecialRenderer<TileEntityInserter> implements IReloadableModelContainer<InserterRenderer>
{
	public static ItemStack conn_data, conn_mv;
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelInserter model;
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/metal_device/inserter.png");

	@Override
	public void render(@Nullable TileEntityInserter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			Utils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			float f5 = 0.0625f;

			model.getBlockRotation(EnumFacing.NORTH, false);

			model.rotate(model.inserterOutput, 0f, -1.5707964f*(te.defaultOutputFacing.getHorizontalIndex()-1), 0f);
			model.rotate(model.inserterInput, 0f, -1.5707964f*(te.defaultInputFacing.getHorizontalIndex()-1), 0f);

			for(ModelRendererTurbo mod : model.inserterInput)
				mod.render(f5);
			for(ModelRendererTurbo mod : model.inserterOutput)
				mod.render(f5);

			model.render();

			double duration = (double)te.getPickupSpeed()*(1+(te.current!=null?te.current.getTimeModifier(): 0));
			double totalProgress = (te.pickProgress+(te.current!=null?partialTicks: 0))/duration;
			float dirIn = -MathHelper.wrapDegrees((
					te.current==null||te.current.facingIn==null?te.defaultInputFacing: te.current.facingIn
			).getHorizontalAngle()+180);

			float dir = dirIn, progress = 0;
			if(totalProgress > 0)
			{
				float dirOut = -MathHelper.wrapDegrees((
						te.current==null||te.current.facingOut==null?te.defaultOutputFacing: te.current.facingOut
				).getHorizontalAngle()+180);

				if(totalProgress < 0.15f)
				{
					progress = (float)(totalProgress/0.1f);
				}
				else if(totalProgress < 0.3f)
				{
					progress = (float)(1f-((totalProgress-0.15f)/0.1f));
				}
				else if(totalProgress < 0.5f)
				{
					float vv = (float)((totalProgress-0.3f)/0.2f);
					dir = (float)MathHelper.clampedLerp(Math.min(dirIn, dirOut), Math.max(dirIn, dirOut), dirIn > dirOut?1f-vv: vv);
				}
				else if(totalProgress < 0.65f)
				{
					dir = dirOut;
					progress = (float)((totalProgress-0.5f)/0.15f);
				}
				else if(totalProgress < 0.75f)
				{
					dir = dirOut;
					progress = (float)(1f-((totalProgress-0.65f)/0.15f));
				}
				else
				{
					float vv = (float)(1f-(((totalProgress-0.8f)/0.2f)));
					dir = (float)MathHelper.clampedLerp(Math.min(dirIn, dirOut), Math.max(dirIn, dirOut), dirIn > dirOut?1f-vv: vv);
				}

			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5f, 0.375f, -0.5);
			GlStateManager.rotate(dir, 0, 1, 0);
			for(ModelRendererTurbo mod : model.inserterBaseTurntable)
				mod.render(0.0625f);
			GlStateManager.translate(0f, 0.125f, 0);
			GlStateManager.rotate(15+55*progress, 1, 0, 0);
			for(ModelRendererTurbo mod : model.inserterLowerArm)
				mod.render(0.0625f);
			GlStateManager.translate(0f, 0.875f, 0);
			GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
			GlStateManager.translate(0f, 0.0625f, 0.03125f);
			for(ModelRendererTurbo mod : model.inserterMidAxle)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.inserterUpperArm)
				mod.render(0.0625f);
			GlStateManager.translate(0f, 0.625f, 0.03125f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.125f, -0.03125f, -0.03125f);
			GlStateManager.rotate(-45.0f*progress, 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.inserterItemPicker1)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);
			GlStateManager.rotate(45f*progress, 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.inserterItemPicker2)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			if(totalProgress > 0.15f&&totalProgress < 0.65f)
				renderItem.renderItem(te.insertionHandler.getStackInSlot(0), TransformType.GROUND);

			GlStateManager.popMatrix();

			if(conn_data!=null)
			{
				GlStateManager.scale(2f, 2f, 2f);
				GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
				renderItem.renderItem(conn_data, TransformType.GROUND);
				GlStateManager.translate(0.375f, 0.1875f, 0.375f);
				GlStateManager.scale(0.65f, 0.65f, 0.65f);
				renderItem.renderItem(conn_mv, TransformType.GROUND);
			}

			GlStateManager.popMatrix();


		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			Utils.bindTexture(TEXTURE);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);


			model.getBlockRotation(EnumFacing.NORTH, false);
			model.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5f, 0.375f, -0.5);
			for(ModelRendererTurbo mod : model.inserterBaseTurntable)
				mod.render(0.0625f);
			GlStateManager.translate(0f, 0.125f, 0);
			GlStateManager.rotate(23.25f, 1, 0, 0);
			for(ModelRendererTurbo mod : model.inserterLowerArm)
				mod.render(0.0625f);
			GlStateManager.translate(0f, 0.875f, 0);
			GlStateManager.rotate(120.75f, 1, 0, 0);
			GlStateManager.translate(0f, 0.0625f, 0.03125f);
			for(ModelRendererTurbo mod : model.inserterMidAxle)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.inserterUpperArm)
				mod.render(0.0625f);
			GlStateManager.translate(0f, 0.625f, 0.03125f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.125f, -0.03125f, -0.03125f);
			GlStateManager.rotate(-6.75f, 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.inserterItemPicker1)
				mod.render(0.0625f);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);
			GlStateManager.rotate(6.7500005f, 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.inserterItemPicker2)
				mod.render(0.0625f);
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();

			GlStateManager.scale(2f, 2f, 2f);
			GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
			if(conn_data!=null)
				renderItem.renderItem(conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(conn_mv!=null)
				renderItem.renderItem(conn_mv, TransformType.GROUND);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelInserter();
		conn_data = new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());
	}
}
