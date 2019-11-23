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
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelAmmunitionFactory model = new ModelAmmunitionFactory();
	private static ModelInserter modelInserter = new ModelInserter();
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

			float ip = Math.min((float)te.casingProgress/(float)ammunitionFactory.casingTime, 1);
			boolean core_being_picked = false;
			boolean core_placed = false;

			if(ip < 0.2)
			{
				dir = Math.min((ip/0.1f)*90, 90);
				if(ip >= 0.1)
					progress = ip/0.1f;
				else
					progress = 0;
			}
			else if(ip < 0.4)
			{
				float prgrs = (ip-0.2f)/0.2f;
				core_being_picked = true;
				dir = Math.max(90-(prgrs/0.5f)*90, 0);
				if(prgrs >= 0.5)
					progress = (prgrs-0.5f)/0.5f;
				else
					progress = 1;
			}
			else if(ip < 0.6)
			{
				float prgrs = (ip-0.4f)/0.2f;
				dir = Math.min(prgrs*90, 90);
				if(prgrs >= 0.5)
					progress = 1f-Math.min((prgrs)/0.5f, 1f);
				else
					progress = 0;
				core_placed = true;
			}
			else if(ip < 0.8)
			{
				core_placed = true;
			}
			else
			{
				core_placed = true;
			}

			progress *= 0.5;

			GlStateManager.translate(1f, 1f, -3f);
			modelInserter.baseModel[1].render(0.0625f);
			modelInserter.baseModel[2].render(0.0625f);
			GlStateManager.translate(0.5f, .375f, -0.5f);

			GlStateManager.rotate(dir, 0f, 1, 0);

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

			boolean[] gunpowder_can_move = new boolean[]{te.gunpowderQueue.get(0).isEmpty(), te.gunpowderQueue.get(1).isEmpty()};
			boolean[] core_can_move = new boolean[]{te.coreQueue.get(0).isEmpty(), te.coreQueue.get(1).isEmpty()};
			boolean[] casing_can_move = new boolean[]{te.casingQueue.get(0).isEmpty(), te.casingQueue.get(1).isEmpty(), te.casingQueue.get(2).isEmpty()};
			boolean[] paint_can_move = new boolean[]{te.paintQueue.get(0).isEmpty(), te.paintQueue.get(1).isEmpty()};
			boolean[] output_can_move = new boolean[]{te.outputQueue.get(0).isEmpty()};

			GlStateManager.translate(0f, 1f, 0f);

			float gp = (float)te.gunpowderProgress/(float)ammunitionFactory.gunpowderTime;
			float cp = Math.min((float)te.conveyorProgress/(float)ammunitionFactory.conveyorTime, 1);
			boolean is_moved = false;

			GlStateManager.pushMatrix();
			if(!output_can_move[0]&&!(te.outputQueue.get(0).isEmpty()))
			{
				GlStateManager.pushMatrix();

				GlStateManager.translate(0f, 0f, cp);

				renderBulletOnConveyor(te.outputQueue.get(0), 1f, false);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(0f, 0f, -1f);

			if(!paint_can_move[1]&&!(te.paintQueue.get(1).isEmpty()))
			{
				GlStateManager.pushMatrix();
				if(output_can_move[0])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(0f, 0f, cp);

				renderBulletOnConveyor(te.paintQueue.get(1), 1f, false);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(0f, 0f, -1f);

			if(!paint_can_move[0]&&!(te.paintQueue.get(0).isEmpty()))
			{
				GlStateManager.pushMatrix();
				if(paint_can_move[1])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(0f, 0f, cp);
				renderBulletOnConveyor(te.paintQueue.get(0), 1f, false);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(1f, 0f, 0f);

			is_moved = false;

			if(!casing_can_move[2]&&!(te.casingQueue.get(2).isEmpty()))
			{
				if(!core_placed)
					renderBulletOnConveyor(te.casingQueue.get(2), 1f, false);
				else
					renderBulletOnConveyor(te.coreQueue.get(1), 1f, false);
			}

			GlStateManager.translate(1f, 0f, 0f);

			if(!casing_can_move[1]&&!(te.casingQueue.get(1).isEmpty()))
			{
				GlStateManager.pushMatrix();
				if(casing_can_move[2])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(-cp, 0f, 0f);
				renderBulletOnConveyor(te.casingQueue.get(1), 1f, false);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(1f, 0f, 0f);

			if(!casing_can_move[0]&&!(te.casingQueue.get(0).isEmpty()))
			{
				GlStateManager.pushMatrix();
				if(casing_can_move[1])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(-cp, 0f, 0f);
				renderBulletOnConveyor(te.casingQueue.get(0), 1f, false);
				GlStateManager.popMatrix();
			}

			GlStateManager.pushMatrix();

			GlStateManager.translate(-1f, 0f, -1f);

			if(!core_placed&&!core_being_picked&&!core_can_move[1]&&!(te.coreQueue.get(1).isEmpty()))
			{
				renderBulletOnConveyor(te.coreQueue.get(1), 0f, true);
			}

			GlStateManager.translate(1f, 0f, 0f);

			if(!core_can_move[0]&&!(te.coreQueue.get(0).isEmpty()))
			{
				GlStateManager.pushMatrix();

				if(core_can_move[1])
					GlStateManager.translate(-cp, 0f, 0f);

				renderBulletOnConveyor(te.coreQueue.get(0), 0f, true);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 1f);

			if(!gunpowder_can_move[1]&&!(te.gunpowderQueue.get(1).isEmpty()))
			{
				if(casing_can_move[0]&&gp > 0.5)
					GlStateManager.translate(0f, 0f, -Math.min((gp-0.5)/0.5f, 1f));
				renderBulletOnConveyor(te.gunpowderQueue.get(1), Math.min(gp/0.5f, 1f), false);
			}

			GlStateManager.translate(0f, 0f, 1f);

			if(!gunpowder_can_move[0]&&!(te.gunpowderQueue.get(0).isEmpty()))
			{
				GlStateManager.pushMatrix();

				if(gunpowder_can_move[1])
					GlStateManager.translate(0f, 0f, -cp);
				renderBulletOnConveyor(te.gunpowderQueue.get(0), 0f, false);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

			ClientUtils.bindTexture(texture);


			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

		}
	}

	void renderBulletOnConveyor(ItemStack stack, float gunpowderPercentage, boolean coreOnly)
	{
		ItemStack casing;
		boolean has_core = false;
		int core_colour = 0;

		if(stack.getItem() instanceof ItemIIBullet)
		{
			casing = ItemIIBullet.getCasing(stack).getStack(stack.getCount());
			if(ItemIIBullet.hasCore(stack))
			{
				has_core = true;
				core_colour = ItemIIBullet.getCore(stack).getColour();
			}
		}
		else if(stack.getItem() instanceof IBulletCasingType)
		{
			casing = stack;
		}
		else
			return;

		IBulletCasingType ctype = (IBulletCasingType)casing.getItem();

		IBulletModel m = BulletRegistry.INSTANCE.registeredModels.get(ctype.getName());
		float size = ctype.getSize();
		int bullets = casing.getCount();
		int row = (int)Math.ceil(Math.sqrt(bullets));

		GlStateManager.pushMatrix();
		m.getConveyorOffset();
		GlStateManager.scale(0.95f, 0.95f, 0.95f);
		for(int i = 0; i < bullets; i += 1)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(i%row*(1f/row), 0, -Math.floor(i/row)*(1f/row));
			GlStateManager.scale(size*2, size*2, size*2);

			if(coreOnly)
				m.renderCore(core_colour);
			else
				m.renderCasing(has_core, core_colour, gunpowderPercentage);

			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
