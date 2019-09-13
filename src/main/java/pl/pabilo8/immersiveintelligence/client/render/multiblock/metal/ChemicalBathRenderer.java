package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelChemicalBath;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityChemicalBath;

/**
 * Created by Pabilo8 on 21-06-2019.
 */
public class ChemicalBathRenderer extends TileEntitySpecialRenderer<TileEntityChemicalBath>
{
	static RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	private static ModelChemicalBath model = new ModelChemicalBath();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/chemical_bath.png";

	@Override
	public void render(TileEntityChemicalBath te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+2, (float)y-2, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			model.getBlockRotation(te.facing, model);
			model.render();

			GlStateManager.pushMatrix();
			float f5 = 1F/16F;

			//4 x pickDrop 2x gotoEdge
			//0.5 - total: 0.75 - bathTime, 0.25 - comebackTime
			float fprocess = te.processTime;
			float pickDropTime = Math.min(25f, te.processTimeMax*0.1f);
			float gotoEdgeTime = Math.min(20f, te.processTimeMax*0.15f);

			float comebackTime = Math.min(45f, (te.processTimeMax-(2*gotoEdgeTime+4*pickDropTime))*0.25f);
			float bathTime = te.processTimeMax-(2*gotoEdgeTime+4*pickDropTime+comebackTime);

			float pickUp = 0;
			float move = 0;
			float door = 0;
			float picking = 0;
			float itemPercent = 0f;
			int itemAnimation = 0;

			if(fprocess < pickDropTime)
			{
				move = 0;

				if(fprocess/pickDropTime < 0.33f)
				{
					door = Math.min(fprocess/(pickDropTime/3f), 1f);
					picking = Math.min(fprocess/(pickDropTime/3f), 1f);
					itemAnimation = 0;
				}
				else if(fprocess/pickDropTime < 0.66f)
				{
					door = 1f;
					picking = 1f;
				}
				else
				{
					door = 1f-Math.min((fprocess/3f)/(pickDropTime/3f), 1f);
					picking = 1f;
				}
				pickUp = fprocess/pickDropTime < 0.5f?Math.min(fprocess/(pickDropTime*0.5f), 1f): 1f-Math.min((fprocess*0.5f)/(pickDropTime*0.5f), 1f);
			}
			else if(fprocess <= pickDropTime+gotoEdgeTime)
			{
				fprocess -= pickDropTime;
				move = 0.875f*(fprocess/gotoEdgeTime);
				picking = 1f;
			}
			else if(fprocess <= 2*pickDropTime+gotoEdgeTime)
			{
				fprocess -= pickDropTime+gotoEdgeTime;
				move = 0.875f;
				pickUp = fprocess/pickDropTime;
				picking = 1f;
			}
			else if(fprocess <= 2*pickDropTime+gotoEdgeTime+bathTime)
			{
				fprocess -= 2*pickDropTime+gotoEdgeTime;
				move = 0.875f+((fprocess/bathTime)*2.5f);
				pickUp = 1f;
				picking = 1f;
				itemPercent = fprocess/bathTime;
			}
			else if(fprocess <= 3*pickDropTime+gotoEdgeTime+bathTime)
			{
				fprocess -= 2*pickDropTime+gotoEdgeTime+bathTime;
				move = 3.375f;
				pickUp = 1f-(fprocess/pickDropTime);
				picking = 1f;
				itemPercent = 1f;
			}
			else if(fprocess <= 3*pickDropTime+2*gotoEdgeTime+bathTime)
			{
				fprocess -= 3*pickDropTime+gotoEdgeTime+bathTime;
				move = 3.375f+(0.875f*(fprocess/gotoEdgeTime));
				picking = 1f;
				itemPercent = 1f;
			}
			else if(fprocess <= 4*pickDropTime+2*gotoEdgeTime+bathTime)
			{
				fprocess -= 3*pickDropTime+(2*gotoEdgeTime)+bathTime;
				move = 4.25f;
				pickUp = fprocess/pickDropTime < 0.5f?Math.min(fprocess/(pickDropTime*0.5f), 1f): 1f-Math.min((fprocess*0.5f)/(pickDropTime*0.5f), 1f);
				picking = Math.min((fprocess*0.5f)/(pickDropTime*0.5f), 1f);
				itemPercent = 1f;
				itemAnimation = 1;
			}
			else if(fprocess <= 4*pickDropTime+2*gotoEdgeTime+bathTime+comebackTime)
			{
				fprocess -= 4*pickDropTime+(2*gotoEdgeTime)+bathTime;
				move = (1f-(fprocess/comebackTime))*4.25f;
				picking = 0f;
				itemPercent = 1f;
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0f, 2f-0.0625, -1f);
			for(ModelRendererTurbo mod : model.itemDoor)
			{
				mod.rotateAngleZ = TmtUtil.AngleToTMT(180f)-(TmtUtil.AngleToTMT(135f)*door);
				mod.render(f5);
			}
			GlStateManager.popMatrix();

			if(picking!=1)
			{
				if(itemAnimation==0)
				{
					GlStateManager.pushMatrix();
					if(te.inventory.get(0)!=ItemStack.EMPTY)
					{
						GlStateManager.rotate(90f, 1f, 0f, 0f);
						GlStateManager.translate(2.375f-picking, -0.5f, -0.5f);

						renderItem.renderItem(te.inventory.get(0), TransformType.GROUND);
					}
					GlStateManager.popMatrix();
				}
				else
				{
					GlStateManager.pushMatrix();
					if(te.inventory.get(0)!=ItemStack.EMPTY)
					{
						GlStateManager.rotate(90f, 1f, 0f, 0f);
						GlStateManager.translate(1.375f+picking, -4.5f, -0.625f);

						renderItem.renderItem(te.effect, TransformType.GROUND);
					}
					GlStateManager.popMatrix();
				}
				ClientUtils.bindTexture(texture);
			}

			GlStateManager.translate(0f, 0f, -move);

			//GlStateManager.translate();
			for(ModelRendererTurbo mod : model.slider)
				mod.render(f5);


			GlStateManager.pushMatrix();
			ClientUtils.bindTexture("immersiveengineering:textures/blocks/wire.png");
			//0x967e6d -
			//FIXME: color is not working
			GlStateManager.color(150, 126, 109);
			GlStateManager.translate(1.3125f, 1.625f, -0.375f);
			GlStateManager.rotate(180, 0f, 0f, 1f);
			GlStateManager.translate(-0.0625f, 0f, 0f);
			ClientUtils.drawTexturedRect(0, 0, 0.0625f, 0.0625f+(0.6875f*pickUp), 0, 0f, 4/16f, 8/16f);
			ClientUtils.drawTexturedRect(0.0625f, 0, -0.0625f, 0.0625f+(0.6875f*pickUp), 0, 0f, 2/16f, 8/16f);
			GlStateManager.rotate(90f, 0f, 1f, 0f);
			GlStateManager.translate(-0.03125f, 0f, 0.03125f);
			ClientUtils.drawTexturedRect(0, 0, 0.0625f, 0.0625f+(0.6875f*pickUp), 0, 0f, 4/16f, 8/16f);
			ClientUtils.drawTexturedRect(0.0625f, 0, -0.0625f, 0.0625f+(0.6875f*pickUp), 0, 0f, 4/16f, 8/16f);

			GlStateManager.popMatrix();
			ClientUtils.bindTexture(texture);

			GlStateManager.translate(0f, -pickUp*0.625, 0f);

			for(ModelRendererTurbo mod : model.slider_lowering)
				mod.render(f5);

			if(picking==1)
			{
				GlStateManager.pushMatrix();
				if(te.inventory.get(0)!=ItemStack.EMPTY)
				{
					GlStateManager.translate(1.375f, 1.385f, -0.5f);
					GlStateManager.rotate(90f, 1f, 0f, 0f);
					renderItem.renderItem(te.inventory.get(0), TransformType.GROUND);
					if(itemPercent > 0)
					{
						GlStateManager.scale(1.25f*itemPercent, 1.25f*itemPercent, 1.25f*itemPercent);
						renderItem.renderItem(te.effect, TransformType.GROUND);
					}
				}
				GlStateManager.popMatrix();
			}


			ClientUtils.bindTexture(texture);
			GlStateManager.translate(1.46875f, 1.5f, -0.375f);

			GlStateManager.pushMatrix();
			GlStateManager.rotate(55f*picking, 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.itemPickerLeftTop)
				mod.render(f5);
			GlStateManager.translate(0f, -0.2f, 0f);
			GlStateManager.rotate((-70f*picking), 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.itemPickerLeftBottom)
				mod.render(f5);

			GlStateManager.popMatrix();

			GlStateManager.translate(-0.25f, 0f, 0f);
			GlStateManager.rotate(-55f*picking, 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.itemPickerRightTop)
				mod.render(f5);
			GlStateManager.translate(0f, -0.2f, 0f);
			GlStateManager.rotate((70f*picking), 0f, 0f, 1f);
			for(ModelRendererTurbo mod : model.itemPickerRightBottom)
				mod.render(f5);

			GlStateManager.popMatrix();


			if(te.tanks[0].getFluidAmount() > 0)
			{
				GlStateManager.pushMatrix();

				GlStateManager.rotate(90, 1f, 0f, 0f);
				GlStateManager.translate(0.0625f, -4f-0.0625f+0.0625f+0.0625f, -0.3125f);
				GlStateManager.scale(0.0625, 0.0625, 0.0625);

				float tfluid = (((float)te.tanks[0].getFluidAmount())/te.tanks[0].getCapacity());
				float hfluid = tfluid/0.5f;

				GlStateManager.translate(0f, 0f, -tfluid*15f);

				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.disableColorMaterial();
				GlStateManager.disableLighting();

				//Draw fluid inside the tank
				ClientUtils.drawRepeatedFluidSprite(te.tanks[0].getFluid(), hfluid < 1f?((1f-hfluid)*6f): 0f, 0f, 46-(hfluid < 1f?((1f-hfluid)*12f): 0f), 46f);
				GlStateManager.disableBlend();


				GlStateManager.popMatrix();

			}


			GlStateManager.popMatrix();

		}
	}
}
