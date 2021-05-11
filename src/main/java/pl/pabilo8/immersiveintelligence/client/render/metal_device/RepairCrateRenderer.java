package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelCrateInserterUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelRepairCrate;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.precission_assembler.ModelPrecissionWelder;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityRepairCrate;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
public class RepairCrateRenderer extends TileEntitySpecialRenderer<TileEntityRepairCrate> implements IReloadableModelContainer<RepairCrateRenderer>
{
	private static ItemStack STACK;
	private static ModelRepairCrate model;
	private static ModelCrateInserterUpgrade modelUpgrade;
	private static ModelPrecissionWelder modelInserter;

	private static final String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/repair_crate.png";

	@Override
	public void render(TileEntityRepairCrate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			float angle = Math.min(1.5f, Math.max(te.lidAngle+(te.open?0.2f*partialTicks: -0.3f*partialTicks), 0f));
			model.getBlockRotation(te.facing, false);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0625f, 0.5625f, -0.5f);
			GlStateManager.rotate(angle*90f, 0, 0, 1);

			for(ModelRendererTurbo mod : model.lidModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			if(te.lidAngle>0)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.5,0.425,-0.5f);
				GlStateManager.rotate(60,0,0,1);
				GlStateManager.rotate(-45,1,0,0);
				GlStateManager.rotate(90,0,1,0);;
				GlStateManager.scale(0.75,0.75,0.75);
				ClientUtils.mc().getRenderItem().renderItem(STACK, TransformType.FIXED);
				GlStateManager.popMatrix();
			}


			if(te.hasUpgrade(IIContent.UPGRADE_INSERTER))
			{
				ClientUtils.bindTexture(ModelCrateInserterUpgrade.texture);
				for(ModelRendererTurbo mod : modelUpgrade.baseModel)
					mod.render(0.0625f);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.translate(x, y+0.75f+0.125f, z+1);
				float f = te.calculateInserterAnimation(partialTicks);
				modelInserter.renderProgress(f*0.5f, -te.calculateInserterAngle(partialTicks), 1);
			}
			else if(te.getInstallProgress()>0)
			{
				float cc = (int)Math.min(te.clientUpgradeProgress+((partialTicks*(Tools.wrench_upgrade_progress/2f))),te.getMaxClientProgress());
				float progress = MathHelper.clamp(cc/(float)te.getCurrentlyInstalled().getProgressRequired(),0,1);
				int l = modelUpgrade.baseModel.length;

				ClientUtils.bindTexture(ModelCrateInserterUpgrade.texture);
				for(int i = 0; i < l*progress; i++)
				{
					if(1+i>Math.round(l*progress))
					{
						GlStateManager.pushMatrix();
						float scale = 1f-(((progress*l)%1f)/1f);
						GlStateManager.enableBlend();
						GlStateManager.color(1f, 1f, 1f, Math.min(scale, 1));
						GlStateManager.translate(0,scale*1.5f,0);

						modelUpgrade.baseModel[i].render(0.0625f);
						GlStateManager.color(1f, 1f, 1f, 1f);
						GlStateManager.popMatrix();
					}
					else
						modelUpgrade.baseModel[i].render(0.0625f);
				}

				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				GlStateManager.scale(0.98f, 0.98f, 0.98f);
				GlStateManager.translate(0.0625f/2f, 0f, -0.0265f/2f);
				//float flicker = (te.getWorld().rand.nextInt(10)==0)?0.75F: (te.getWorld().rand.nextInt(20)==0?0.5F: 1F);

				ShaderUtil.blueprint_static(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
				for(int i = l-1; i >= Math.max((l*progress)-1,0); i--)
				{
					modelUpgrade.baseModel[i].render(0.0625f);
				}
				ShaderUtil.releaseShader();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.getBlockRotation(EnumFacing.NORTH, false);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			GlStateManager.translate(0.0625f, 0.5625f, -0.5f);
			for(ModelRendererTurbo mod : model.lidModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelRepairCrate();
		modelUpgrade = new ModelCrateInserterUpgrade();
		modelInserter = new ModelPrecissionWelder();
		STACK = new ItemStack(IIContent.itemWrench);
	}

	public static void renderWithUpgrade(MachineUpgrade... upgrades)
	{
		ClientUtils.bindTexture(texture);
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5, 0, 0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		//model.getBlockRotation(EnumFacing.NORTH, false);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render(0.0625f);
		GlStateManager.translate(0.0625f, 0.5625f, -0.5f);
		for(ModelRendererTurbo mod : model.lidModel)
			mod.render(0.0625f);

		for(MachineUpgrade upgrade : upgrades)
		{
			if(upgrade==IIContent.UPGRADE_INSERTER)
			{
				GlStateManager.pushMatrix();
				ClientUtils.bindTexture(ModelCrateInserterUpgrade.texture);
				GlStateManager.translate(-0.0625f, -0.5625f, 0.5f);
				for(ModelRendererTurbo mod : modelUpgrade.baseModel)
					mod.render(0.0625f);
				GlStateManager.translate(0, 0.75f+0.125f, 0);
				modelInserter.renderProgress(0.5f, 0.5f, 1);
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.popMatrix();
	}
}
