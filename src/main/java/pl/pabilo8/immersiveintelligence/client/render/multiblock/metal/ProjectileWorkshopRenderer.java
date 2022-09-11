package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorTile;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;

import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class ProjectileWorkshopRenderer extends TileEntitySpecialRenderer<TileEntityProjectileWorkshop> implements IReloadableModelContainer<ProjectileWorkshopRenderer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/projectile_workshop.png");

	static RenderItem renderItem = ClientUtils.mc().getRenderItem();

	private static IConveyorBelt con;
	private static ModelProjectileWorkshop model, modelFlipped;

	@Override
	public void render(TileEntityProjectileWorkshop te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			IIClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			boolean conveyorInConnected=false;
			boolean conveyorOutRunning = false;
			float drawer1 = MathHelper.clamp(te.drawerAngle[0]+(te.isDrawerOpened[0]?partialTicks: -partialTicks), 0, 20)/20f;
			float drawer2 = MathHelper.clamp(te.drawerAngle[1]+(te.isDrawerOpened[1]?partialTicks: -partialTicks), 0, 20)/20f;

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 0, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				conveyorInConnected = te.getWorld().getTileEntity(te.getBlockPosForPos(15).offset(te.mirrored?te.facing.rotateYCCW(): te.facing.rotateY())) instanceof IConveyorTile;
			}

			//render

			ModelProjectileWorkshop modelCurrent = te.mirrored?modelFlipped: model;

			modelCurrent.getBlockRotation(te.facing, te.mirrored);

			for(ModelRendererTurbo mod : modelCurrent.baseModel)
				mod.render();

			for(ModelRendererTurbo mod : modelCurrent.insidesModel)
				mod.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.3125f+0.385*Math.min(drawer1/0.3, 1), 0);
			GlStateManager.rotate(-45f*Math.min(Math.max(drawer1-0.2f, 0f)/0.2f, 1f), 1, 0, 0);
			GlStateManager.translate(0, 0.425*Math.min(Math.max(drawer1-0.4f, 0f)/0.3f, 1f), 0);
			GlStateManager.rotate(-45f*Math.min(Math.max(drawer1-0.6f, 0f)/0.2f, 1f), 1, 0, 0);
			GlStateManager.translate(0, 0.425*Math.min(Math.max(drawer1-0.8f, 0f)/0.2f, 1f), 0);
			for(ModelRendererTurbo mod : modelCurrent.lidLeftModel)
				mod.render();
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.3125f+0.385*Math.min(drawer2/0.3, 1), 0);
			GlStateManager.rotate(-45f*Math.min(Math.max(drawer2-0.2f, 0f)/0.2f, 1f), 1, 0, 0);
			GlStateManager.translate(0, 0.425*Math.min(Math.max(drawer2-0.4f, 0f)/0.3f, 1f), 0);
			GlStateManager.rotate(-45f*Math.min(Math.max(drawer2-0.6f, 0f)/0.2f, 1f), 1, 0, 0);
			GlStateManager.translate(0, 0.425*Math.min(Math.max(drawer2-0.8f, 0f)/0.2f, 1f), 0);
			for(ModelRendererTurbo mod : modelCurrent.lidRightModel)
				mod.render();
			GlStateManager.popMatrix();

			List<BakedQuad> conveyorIn = ModelConveyor.getBaseConveyor(
					EnumFacing.NORTH, 1, new Matrix4(EnumFacing.NORTH), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(con.getActiveTexture()), new boolean[]{!conveyorInConnected, true}, new boolean[]{true, true}, null, 0);

			ResourceLocation outputRes = conveyorOutRunning?con.getActiveTexture(): con.getInactiveTexture();
			List<BakedQuad> conveyorOut = ModelConveyor.getBaseConveyor(
					EnumFacing.SOUTH, 1, new Matrix4(EnumFacing.SOUTH), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(outputRes), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);

			List<BakedQuad> conveyorOut2 = ModelConveyor.getBaseConveyor(
					te.mirrored?EnumFacing.EAST: EnumFacing.WEST, 1, new Matrix4(te.mirrored?EnumFacing.EAST: EnumFacing.WEST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(outputRes), new boolean[]{false, true}, new boolean[]{true, true}, null, 0);

			ClientUtils.bindAtlas();

			GlStateManager.translate(te.mirrored?0: -1, 0, -1);
			ClientUtils.renderQuads(conveyorIn, 1, 1, 1, 1);
			GlStateManager.translate(te.mirrored?2.995: -2.995, 0, 0);
			ClientUtils.renderQuads(conveyorOut2, 1, 1, 1, 1);
			GlStateManager.translate(0, 0, -1);
			ClientUtils.renderQuads(conveyorOut, 1, 1, 1, 1);

			GlStateManager.popMatrix();
		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.25, y-0.25, z);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.23, 0.23, 0.23);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			renderWithUpgrades(new MachineUpgrade[0]);

			GlStateManager.popMatrix();
		}
	}

	public static void renderWithUpgrades(MachineUpgrade[] upgrades)
	{
		IIClientUtils.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5, 0.5, 0.5);
		GlStateManager.translate(2, 1, 1.5);

		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : model.lidLeftModel)
			mod.render();
		for(ModelRendererTurbo mod : model.lidRightModel)
			mod.render();

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelProjectileWorkshop();
		model.flipAllX();
		model.parts.values().forEach(mods -> Arrays.stream(mods).filter(mm -> mm.flip).forEach(e -> e.rotationPointY *= -1));

		for(int i : new int[]{28, 70, 71})
		{
			model.baseModel[i].doMirror(false, false, true);
			model.baseModel[i].rotateAngleY += 3.14f;
			model.baseModel[i].rotationPointX += 1;
		}

		modelFlipped = new ModelProjectileWorkshop();
		con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);
	}

}
