package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.render.TileRenderAutoWorkbench;
import blusunrize.immersiveengineering.client.render.TileRenderAutoWorkbench.BlueprintLines;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.precision_assembler.*;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class PrecisionAssemblerRenderer extends TileEntitySpecialRenderer<TileEntityPrecisionAssembler> implements IReloadableModelContainer<PrecisionAssemblerRenderer>
{
	//Tool Models (if you want to add custom tools from your mod, you have to init them in your own class)
	public static ModelPrecisionInserter modelInserter = new ModelPrecisionInserter();
	public static ModelPrecisionDrill modelDrill = new ModelPrecisionDrill();
	public static ModelPrecisionBuzzsaw modelBuzzsaw = new ModelPrecisionBuzzsaw();
	public static ModelPrecisionSolderer modelSolderer = new ModelPrecisionSolderer();
	public static ModelPrecisionWelder modelWelder = new ModelPrecisionWelder();
	public static ModelPrecisionHammer modelHammer = new ModelPrecisionHammer();
	private static final RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelPrecisionAssembler model;
	private static ModelPrecisionAssembler modelFlipped;

	@Override
	public void render(TileEntityPrecisionAssembler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/precission_assembler.png";
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+2, (float)y-1, (float)z-1);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			//A bit of trick here, because it's pointless to rewrite the whole code just because only one part is mirrored :v
			GlStateManager.pushMatrix();
			ModelPrecisionAssembler currentModel = te.mirrored?modelFlipped: model;
			currentModel.getBlockRotation(te.facing, te.mirrored);
			currentModel.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-MathHelper.clamp(te.drawerAngle[0]+(te.isDrawerOpened[0]?0.4f: -0.5f)*partialTicks, 0f, 5f)/16f, 0, 0);
			for(ModelRendererTurbo mod : currentModel.drawer1Model)
			{
				mod.render(0.0625f);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-MathHelper.clamp(te.drawerAngle[1]+(te.isDrawerOpened[1]?0.4f: -0.5f)*partialTicks, 0f, 5f)/16f, 0, 0);
			for(ModelRendererTurbo mod : currentModel.drawer2Model)
			{
				mod.render(0.0625f);
			}
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();

			model.getBlockRotation(te.facing, false);

			float f5 = 1F/16F;

			float hatchProgress = te.active?135f: 0f;
			if(te.active&&te.processTime < PrecisionAssembler.hatchTime)
				hatchProgress = 135f*((te.processTime+(partialTicks/20f))/PrecisionAssembler.hatchTime);
			else if(te.active&&te.processTime > te.processTimeMax-PrecisionAssembler.hatchTime)
			{

				hatchProgress = 135f*(te.processTimeMax-(te.processTime+(partialTicks/20f)))/(float)PrecisionAssembler.hatchTime;
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(24f/16f, 17f/16f, -31f/16f);
			GlStateManager.rotate(hatchProgress, 1f, 0f, 0f);
			for(ModelRendererTurbo mod : model.doorLeftModel)
			{
				mod.render(0.0625f);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(24f/16f, 17f/16f, -49f/16f);
			GlStateManager.rotate(-hatchProgress, 1f, 0f, 0f);
			for(ModelRendererTurbo mod : model.doorRightModel)
			{
				mod.render(0.0625f);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();


			int time = 20;
			float max_progress = 1f;
			float time_between = 0f;
			int moved_tool = -1;
			String[] action = new String[]{};

			float angle = 0;

			check:
			if(te.active)
			{
				if(te.animationPrepared.size() > 0&&te.processTime > PrecisionAssembler.hatchTime&&te.processTime < te.processTimeMax-PrecisionAssembler.hatchTime)
				{
					for(int i = 0; i < te.animationPrepared.size(); i += 1)
					{
						if(te.processTime <= time+te.animationPrepared.get(i).getFirst())
						{
							moved_tool = ArrayUtils.indexOf(te.toolOrder, te.animationPrepared.get(i).getSecond().split(" ")[0]);
							time_between = (te.processTime-time)/(float)te.animationPrepared.get(i).getFirst();
							action = te.animationPrepared.get(i).getSecond().split(" ");
							break;
						}
						else
							time += te.animationPrepared.get(i).getFirst();
					}

					if(!(action.length > 0))
						break check;

					if(action[1].equals("pick"))
					{
						if(time_between==0.5f)
						{
							switch(action[2])
							{
								case "first":
								{
									te.stack1Visible = false;
									if(moved_tool==0)
										te.stackPicked1 = te.inventory.get(5);
									else if(moved_tool==1)
										te.stackPicked2 = te.inventory.get(5);
									else if(moved_tool==2)
										te.stackPicked3 = te.inventory.get(5);

								}
								break;
								case "second":
								{
									te.stack2Visible = false;
									if(moved_tool==0)
										te.stackPicked1 = te.inventory.get(6);
									else if(moved_tool==1)
										te.stackPicked2 = te.inventory.get(6);
									else if(moved_tool==2)
										te.stackPicked3 = te.inventory.get(6);
								}
								break;
								case "third":
								{
									te.stack3Visible = false;
									if(moved_tool==0)
										te.stackPicked1 = te.inventory.get(7);
									else if(moved_tool==1)
										te.stackPicked2 = te.inventory.get(7);
									else if(moved_tool==2)
										te.stackPicked3 = te.inventory.get(7);
								}
								break;
							}
						}
					}
					else if(action[1].equals("drop"))
					{
						if(time_between==0.5f)
						{
							if("main".equals(action[2]))
							{
								if(moved_tool==0)
									te.stackPicked1 = ItemStack.EMPTY;
								else if(moved_tool==1)
									te.stackPicked2 = ItemStack.EMPTY;
								else if(moved_tool==2)
									te.stackPicked3 = ItemStack.EMPTY;
								//NOP I DON"T WANT TO DO THAT
							}
						}
					}

					switch(action[2])
					{
						case "main":
							angle = moved_tool==0?22.5f: moved_tool==1?0: -22.5f;
							max_progress = moved_tool==0?0.65f: moved_tool==1?0.55f: 0.65f;
							break;
						case "first":
							angle = moved_tool==0?0: moved_tool==1?-22.5f: -35;
							max_progress = moved_tool==0?0.75f: moved_tool==1?0.85f: 1f;
							break;
						case "second":
							angle = moved_tool==0?22.5f: moved_tool==1?0: -22.5f;
							max_progress = moved_tool==0?0.65f: moved_tool==1?0.5f: 0.65f;
							break;
						case "third":
							angle = moved_tool==0?-35: moved_tool==1?-22.5f: 0;
							max_progress = moved_tool==0?1f: moved_tool==1?0.85f: 0.75f;
							break;
					}

				}
			}


			GlStateManager.pushMatrix();
			GlStateManager.translate(2.0625f, 1.125f, -3.5f);
			GlStateManager.rotate(-90, 0f, 1f, 0f);

			if(!te.inventory.get(0).isEmpty())
				((IPrecisionTool)te.inventory.get(0).getItem()).renderInMachine(te.inventory.get(0), moved_tool==0?time_between: 0f, moved_tool==0?angle: 0, max_progress, te.stackPicked1);

			GlStateManager.translate(0.5f, 0, 0);
			if(!te.inventory.get(1).isEmpty())
				((IPrecisionTool)te.inventory.get(1).getItem()).renderInMachine(te.inventory.get(1), moved_tool==1?time_between: 0f, moved_tool==1?angle: 0, max_progress, te.stackPicked2);

			GlStateManager.translate(0.5f, 0, 0);

			if(!te.inventory.get(2).isEmpty())
				((IPrecisionTool)te.inventory.get(2).getItem()).renderInMachine(te.inventory.get(2), moved_tool==2?time_between: 0f, moved_tool==2?angle: 0, max_progress, te.stackPicked3);

			GlStateManager.popMatrix();

			ClientUtils.bindTexture(texture);

			GlStateManager.pushMatrix();

			hatchProgress = te.active?1f: 0f;
			if(te.active&&te.processTime < PrecisionAssembler.hatchTime)
				hatchProgress = ((te.processTime+(partialTicks/20f))/PrecisionAssembler.hatchTime);
			else if(te.active&&te.processTime > te.processTimeMax-PrecisionAssembler.hatchTime)
			{
				hatchProgress = 1f-(((te.processTime-te.processTimeMax+PrecisionAssembler.hatchTime)+(partialTicks/20f))/PrecisionAssembler.hatchTime);
			}

			GlStateManager.translate(0f, 0.75f*hatchProgress, 0f);

			for(ModelRendererTurbo mod : model.lowerBox)
				mod.render(0.0625f);

			if(te.active)
			{
				GlStateManager.translate(1.625f, 0.385, -2.625f);
				GlStateManager.rotate(90, 1f, 0f, 0f);

				GlStateManager.pushMatrix();
				GlStateManager.scale(1.25f, 1.25f, 1.25f);
				if(te.processTime < te.processTimeMax-PrecisionAssembler.hatchTime)
					renderItem.renderItem(te.inventory.get(4), TransformType.GROUND);
				else
					renderItem.renderItem(te.effect, TransformType.GROUND);
				GlStateManager.popMatrix();

				GlStateManager.translate(-.385f, -0.3125f, 0);

				if(!te.inventory.get(5).isEmpty()&&te.stack1Visible)
					renderItem.renderItem(te.inventory.get(5), TransformType.GROUND);

				GlStateManager.translate(0, .25f, 0);

				if(!te.inventory.get(6).isEmpty()&&te.stack2Visible)
					renderItem.renderItem(te.inventory.get(6), TransformType.GROUND);

				GlStateManager.translate(0, .25f, 0);

				if(!te.inventory.get(7).isEmpty()&&te.stack3Visible)
					renderItem.renderItem(te.inventory.get(7), TransformType.GROUND);

			}

			GlStateManager.popMatrix();


			GlStateManager.popMatrix();

			if(!te.inventory.get(3).isEmpty())
			{
				GlStateManager.color(1f, 1f, 1f, 1f);
				ClientUtils.bindTexture(texture);
				for(ModelRendererTurbo model : model.schemeModel)
					model.render(0.0625f);
				ItemStack drawStack = IIContent.itemAssemblyScheme.getProducedStack(te.inventory.get(3));

				double playerDistanceSq = ClientUtils.mc().player.getDistanceSq(te.getPos());
				float lineWidth = playerDistanceSq < 25?1: playerDistanceSq < 40?.5f: .1f;
				GlStateManager.color(0.65f, 0.65f, 0.65f);

				GlStateManager.translate(0, 1, -2.75);
				GlStateManager.rotate(270, 0, 1, 0);
				GlStateManager.rotate(30+75, 1, 0, 0);
				GlStateManager.translate(0, -0.625, 0.0625/2f);
				BlueprintLines blueprint = TileRenderAutoWorkbench.getBlueprintDrawable(drawStack, te.getWorld());
				if(blueprint!=null&&playerDistanceSq < 120)
				{
					GlStateManager.translate(0, 0, 0);

					//Width depends on distance
					GlStateManager.disableCull();
					GlStateManager.disableTexture2D();
					GlStateManager.enableBlend();
					float texScale = 32f;
					GlStateManager.scale(1/texScale, 1/texScale, 1/texScale);
					GlStateManager.color(1, 1, 1, (float)(0.25f*MathHelper.clamp(1f-(playerDistanceSq/120f), 0, 1)));
					blueprint.draw(lineWidth*2f);
					GlStateManager.scale(texScale, texScale, texScale);
					GlStateManager.enableAlpha();
					GlStateManager.enableTexture2D();
					GlStateManager.enableCull();
					GlStateManager.translate(-1, 1, 0);
				}
			}

			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.35, y-1.15, z-0.6);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.rotate(-7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.3, 0.3, 0.3);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.drawer1Model)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.drawer2Model)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		modelInserter = new ModelPrecisionInserter();
		modelDrill = new ModelPrecisionDrill();
		modelBuzzsaw = new ModelPrecisionBuzzsaw();
		modelSolderer = new ModelPrecisionSolderer();
		modelWelder = new ModelPrecisionWelder();
		modelHammer = new ModelPrecisionHammer();

		model = new ModelPrecisionAssembler(false);
		modelFlipped = new ModelPrecisionAssembler(true);
		modelFlipped.flipAllZ();
	}
}
