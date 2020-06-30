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
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.AmmunitionFactory;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import java.util.List;

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
			ClientUtils.setLightmapDisabled(false);
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
			ClientUtils.bindTexture(textureInserter);

			GlStateManager.pushMatrix();

			float dir = 0, progress = 0.5f;
			ItemStack picked_stack = te.coreQueue.get(1).copy();

			float ip_conv = 0, ip_base = 0, ip_st = 0, ip_current = 0;
			int ip_done = 0, ip_all = te.casingQueue.get(2).getCount(), ip_done_up = 0;
			if(te.casingProgress > 0)
			{
				float size = ((IBulletCasingType)te.casingQueue.get(2).getItem()).getSize();
				int ip_total = (int)(AmmunitionFactory.casingTime*size*ip_all)+AmmunitionFactory.conveyorTime;
				ip_conv = 0;
				ip_base = ip_total-AmmunitionFactory.conveyorTime;

				ip_st = ip_base/ip_all;
				ip_done = (int)Math.floor(te.casingProgress/ip_st);
				ip_done_up = (int)Math.ceil(te.casingProgress/ip_st);
				ip_current = (te.casingProgress-(ip_done*ip_st))/ip_st;
				picked_stack.setCount(0);

				if(ip_current > 0.5)
				{
					ip_done = Math.max(0, ip_done+1);
					ip_done_up = Math.min(ip_all, ip_done_up+1);
					picked_stack.setCount(1);
				}

				if(te.casingProgress >= ip_base)
				{
					ip_done = ip_all;
					ip_done_up = ip_all;
					ip_current = 0;
					ip_conv = te.casingProgress-ip_base;
					picked_stack.setCount(0);
				}
			}

			if(ip_current < 0.5)
			{
				dir = Math.min((ip_current/0.5f)*90, 90);
				if(ip_current >= 0.5f)
					progress = ip_current/0.5f;
				else
					progress = 0;
			}
			else
			{
				float prgrs = (ip_current-0.5f)/0.5f;
				dir = Math.max(90-(prgrs/0.5f)*90, 0);
				if(prgrs >= 0.5)
					progress = (prgrs-0.5f)/0.5f;
				else
					progress = 1;
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

			GlStateManager.translate(-0.25f, 1f, 0f);
			GlStateManager.rotate(180, 1, 0, 0);

			renderBulletOnConveyor(picked_stack, 0f, true, 1);

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

			float gp = (float)te.gunpowderProgress/(float)AmmunitionFactory.gunpowderTime;
			float cgp = Math.min((float)te.conveyorGunpowderProgress/(float)AmmunitionFactory.conveyorTime, 1);
			float ccop = Math.min((float)te.conveyorCoreProgress/(float)AmmunitionFactory.conveyorTime, 1);
			float ccp = Math.min((float)te.conveyorCasingProgress/(float)AmmunitionFactory.conveyorTime, 1);
			float cpp = Math.min((float)te.conveyorPaintProgress/(float)AmmunitionFactory.conveyorTime, 1);
			float cop = Math.min((float)te.conveyorOutputProgress/(float)AmmunitionFactory.conveyorTime, 1);
			boolean is_moved = false;

			GlStateManager.pushMatrix();
			if(!te.outputQueue.get(0).isEmpty())
			{
				GlStateManager.pushMatrix();

				GlStateManager.translate(0f, 0f, cop);

				renderBulletOnConveyor(te.outputQueue.get(0), 1f, false, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(0f, 0f, -1f);

			if(!te.paintQueue.get(1).isEmpty())
			{
				GlStateManager.pushMatrix();
				if(output_can_move[0])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(0f, 0f, cpp);

				renderBulletOnConveyor(te.paintQueue.get(1), 1f, false, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(0f, 0f, -1f);

			if(!te.paintQueue.get(0).isEmpty())
			{
				GlStateManager.pushMatrix();
				if(paint_can_move[1])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(0f, 0f, cpp);
				renderBulletOnConveyor(te.paintQueue.get(0), 1f, false, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(1f, 0f, 0f);

			is_moved = false;

			if(!te.casingQueue.get(2).isEmpty())
			{
				GlStateManager.pushMatrix();
				if(ip_conv > 0)
					GlStateManager.translate(-ip_conv/AmmunitionFactory.conveyorTime, 0f, 0f);

				renderBulletOnConveyor(te.coreQueue.get(1), 1f, false, ip_done_up);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(1f, 0f, 0f);

			if(!te.casingQueue.get(1).isEmpty())
			{
				GlStateManager.pushMatrix();
				if(casing_can_move[2])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(-ccp, 0f, 0f);
				renderBulletOnConveyor(te.casingQueue.get(1), 1f, false, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(1f, 0f, 0f);

			if(!te.casingQueue.get(0).isEmpty())
			{
				GlStateManager.pushMatrix();
				if(casing_can_move[1])
					is_moved = true;

				if(is_moved)
					GlStateManager.translate(-ccp, 0f, 0f);
				renderBulletOnConveyor(te.casingQueue.get(0), 1f, false, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.pushMatrix();

			GlStateManager.translate(-1f, 0f, -1f);

			if(!te.coreQueue.get(1).isEmpty())
			{
				renderBulletOnConveyor(te.coreQueue.get(1), 0f, true, ip_all-ip_done);
			}

			GlStateManager.translate(1f, 0f, 0f);

			if(!te.coreQueue.get(0).isEmpty())
			{
				GlStateManager.pushMatrix();

				if(core_can_move[1])
					GlStateManager.translate(-ccop, 0f, 0f);

				renderBulletOnConveyor(te.coreQueue.get(0), 0f, true, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

			GlStateManager.translate(0f, 0f, 1f);

			if(!gunpowder_can_move[1]&&!(te.gunpowderQueue.get(1).isEmpty()))
			{
				if(casing_can_move[0]&&gp > 0.5)
					GlStateManager.translate(0f, 0f, -Math.min((gp-0.5)/0.5f, 1f));
				renderBulletOnConveyor(te.gunpowderQueue.get(1), Math.min(gp/0.5f, 1f), false, -1);
			}

			GlStateManager.translate(0f, 0f, 1f);

			if(!gunpowder_can_move[0]&&!(te.gunpowderQueue.get(0).isEmpty()))
			{
				GlStateManager.pushMatrix();

				if(gunpowder_can_move[1]||gp > 0.5)
					GlStateManager.translate(0f, 0f, -cgp);
				renderBulletOnConveyor(te.gunpowderQueue.get(0), 0f, false, -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

			ClientUtils.bindTexture(texture);


			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

		}
	}

	void renderBulletOnConveyor(ItemStack stack, float gunpowderPercentage, boolean coreOnly, int cores)
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

			if(coreOnly&&(cores==-1||i < cores))
				m.renderCore(core_colour);
			else if(!coreOnly)
				m.renderCasing(cores==-1?has_core: i <= cores, core_colour, gunpowderPercentage);

			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
