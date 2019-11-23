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
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import java.util.List;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ammunitionFactory;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class AmmunitionFactoryRenderer extends TileEntitySpecialRenderer<TileEntityAmmunitionFactory>
{
	private static ModelAmmunitionFactory model = new ModelAmmunitionFactory();
	private static ModelInserter modelInserter = new ModelInserter();

	static RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/ammunition_factory.png";
	private static String textureInserter = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/inserter.png";

	@Override
	public void render(TileEntityAmmunitionFactory te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-2, (float)z+2);
			GlStateManager.rotate(180F, 0F, 1F, 0F);

			//Test?
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

			model.getBlockRotation(te.facing, model);
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
			ClientUtils.bindTexture(textureInserter);

			//
			GlStateManager.pushMatrix();

			float dir = 0, progress = 0.5f;
			ItemStack picked_stack = te.inventory.get(0);

			GlStateManager.translate(1f, 1f, -3f);
			GlStateManager.rotate(dir, 0f, 1, 0);

			modelInserter.baseModel[1].render(0.0625f);
			modelInserter.baseModel[2].render(0.0625f);

			GlStateManager.translate(0.5f, .375f, -0.5f);

			for(ModelRendererTurbo mod : modelInserter.inserterBaseTurntable)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.125f, 0);
			GlStateManager.rotate(15+55*progress, 1, 0, 0);

			for(ModelRendererTurbo mod : modelInserter.inserterLowerArm)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.875f, 0);
			GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
			GlStateManager.translate(0f, 0.0625f, 0.03125f);

			for(ModelRendererTurbo mod : modelInserter.inserterMidAxle)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : modelInserter.inserterUpperArm)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.625f, 0.03125f);

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.125f, -0.03125f, -0.03125f);

			GlStateManager.rotate(-45f*progress, 0f, 0f, 1f);

			for(ModelRendererTurbo mod : modelInserter.inserterItemPicker1)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);

			GlStateManager.rotate(45f*progress, 0f, 0f, 1f);

			for(ModelRendererTurbo mod : modelInserter.inserterItemPicker2)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			renderItem.renderItem(picked_stack, TransformType.GROUND);

			GlStateManager.popMatrix();
			//

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			boolean[] core_can_move = new boolean[]{te.coreQueue.get(0).isEmpty(), te.coreQueue.get(1).isEmpty(), te.coreQueue.get(2).isEmpty()};
			boolean[] gunpowder_can_move = new boolean[]{te.gunpowderQueue.get(0).isEmpty(), te.gunpowderQueue.get(1).isEmpty(), te.gunpowderQueue.get(2).isEmpty()};
			boolean[] casing_can_move = new boolean[]{te.casingQueue.get(0).isEmpty(), te.casingQueue.get(1).isEmpty(), te.casingQueue.get(2).isEmpty(), te.casingQueue.get(3).isEmpty(), te.casingQueue.get(4).isEmpty(), te.casingQueue.get(5).isEmpty()};
			boolean[] paint_can_move = new boolean[]{te.paintQueue.get(0).isEmpty(), te.paintQueue.get(1).isEmpty(), te.paintQueue.get(2).isEmpty()};
			boolean[] output_can_move = new boolean[]{te.outputQueue.get(0).isEmpty(), te.outputQueue.get(1).isEmpty()};

			GlStateManager.translate(1.0f, 1f, -2f);

			//TODO: Gunpowder filling
			float gp = (float)te.gunpowderProgress/(float)ammunitionFactory.gunpowderTime;
			float cp = Math.min((float)te.conveyorProgress/(float)ammunitionFactory.conveyorTime, 1);
			boolean is_moved = false;

			//Casings
			boolean casing_being_picked = false;

			GlStateManager.pushMatrix();
			if(!casing_being_picked&&!casing_can_move[5]&&te.casingQueue.get(5).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.casingQueue.get(5).getItem()).getName());
				m.getConveyorOffset();
				m.renderCasing(false, 0, 1f);
			}
			GlStateManager.popMatrix();

			//ende!

			GlStateManager.translate(0.5f, 0f, 0f);

			GlStateManager.pushMatrix();
			if(!casing_can_move[4]&&te.casingQueue.get(4).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.casingQueue.get(4).getItem()).getName());

				if(casing_can_move[5])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(-cp*0.5f, 0f, 0f);

				m.getConveyorOffset();
				m.renderCasing(false, 0, 1f);
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0.5f, 0f, 0f);

			GlStateManager.pushMatrix();
			if(!casing_can_move[3]&&te.casingQueue.get(3).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.casingQueue.get(3).getItem()).getName());
				if(casing_can_move[4])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(-cp*0.5f, 0f, 0f);
				m.getConveyorOffset();
				m.renderCasing(false, 0, 1f);
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0.5f, 0f, 0f);

			GlStateManager.pushMatrix();
			if(!casing_can_move[2]&&te.casingQueue.get(2).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.casingQueue.get(2).getItem()).getName());
				if(casing_can_move[3])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(-cp*0.5f, 0f, 0f);
				m.getConveyorOffset();
				m.renderCasing(false, 0, 1f);
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0.5f, 0f, 0f);

			GlStateManager.pushMatrix();
			if(!casing_can_move[1]&&te.casingQueue.get(1).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.casingQueue.get(1).getItem()).getName());
				if(casing_can_move[2])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(-cp*0.5f, 0f, 0f);
				m.getConveyorOffset();
				m.renderCasing(false, 0, 1f);
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 0.5f);

			GlStateManager.pushMatrix();
			if(!casing_can_move[0]&&te.casingQueue.get(0).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.casingQueue.get(0).getItem()).getName());
				if(casing_can_move[1])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(0f, 0f, -cp*0.5f);
				m.getConveyorOffset();
				m.renderCasing(false, 0, 1f);
			}
			GlStateManager.popMatrix();


			GlStateManager.translate(0f, 0f, 0.75f);

			//Core

			GlStateManager.pushMatrix();

			GlStateManager.translate(0f, 0f, -1f);

			GlStateManager.pushMatrix();
			if(!core_can_move[2]&&te.coreQueue.get(2).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.coreQueue.get(2).getItem()).getName());
				m.getConveyorOffset();
				m.renderCore(ImmersiveIntelligence.proxy.item_bullet.getCore(te.coreQueue.get(2)).getColour());
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 0.5f);

			GlStateManager.pushMatrix();
			if(!core_can_move[1]&&te.coreQueue.get(1).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.coreQueue.get(1).getItem()).getName());
				if(core_can_move[2])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(0f, 0f, -cp*0.5f);
				m.getConveyorOffset();
				m.renderCore(ImmersiveIntelligence.proxy.item_bullet.getCore(te.coreQueue.get(1)).getColour());
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 0.5f);

			is_moved = false;

			if(te.coreProgress > 0)
			{
				//TODO: Bullet rendering from list
			}
			else
			{
				GlStateManager.pushMatrix();
				if(!core_can_move[0]&&te.coreQueue.get(0).getItem() instanceof ItemIIBullet)
				{
					ImmersiveIntelligence.logger.info("otak!");
					IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(ItemIIBullet.getCasing(te.coreQueue.get(0)));
					if(core_can_move[1])
						is_moved = true;
					if(is_moved)
						GlStateManager.translate(0f, 0f, -cp*0.5f);
					if(m!=null)
					{
						m.getConveyorOffset();
						m.renderCore(ItemIIBullet.getCore(te.coreQueue.get(0)).getColour());
					}
					else
						ImmersiveIntelligence.logger.info("Model is NULL!");
				}
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();


			//Gunpowder

			is_moved = false;


			GlStateManager.pushMatrix();
			if(!gunpowder_can_move[2]&&te.gunpowderQueue.get(2).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.gunpowderQueue.get(2).getItem()).getName());
				m.getConveyorOffset();
				GlStateManager.pushMatrix();
				if(gp > 0.5f&&casing_can_move[0])
				{
					is_moved = true;
					GlStateManager.translate(0f, 0f, -1f*((gp-0.5f)/(gp*0.5f)));
				}
				m.renderCasing(false, 0, Math.min(gp/0.5f, 1f));
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 0.5f);

			GlStateManager.pushMatrix();
			if(!gunpowder_can_move[1]&&te.gunpowderQueue.get(1).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.gunpowderQueue.get(1).getItem()).getName());
				if(gunpowder_can_move[2])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(0f, 0f, -cp*0.5f);
				m.getConveyorOffset();
				m.renderCasing(false, 0, 0f);
			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 0.5f);

			GlStateManager.pushMatrix();
			if(!gunpowder_can_move[0]&&te.gunpowderQueue.get(0).getItem() instanceof IBulletCasingType)
			{
				IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(((IBulletCasingType)te.gunpowderQueue.get(0).getItem()).getName());
				if(gunpowder_can_move[1])
					is_moved = true;
				if(is_moved)
					GlStateManager.translate(0f, 0f, -cp*0.5f);
				m.getConveyorOffset();
				m.renderCasing(false, 0, 0f);
			}
			GlStateManager.popMatrix();


			GlStateManager.popMatrix();

			ClientUtils.bindTexture(texture);


			GlStateManager.popMatrix();

		}
	}
}
