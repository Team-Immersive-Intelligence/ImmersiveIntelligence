package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.MechanicalPump;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelMechanicalPumpTop;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalPump;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
public class MechanicalPumpRenderer extends TileEntitySpecialRenderer<TileEntityMechanicalPump> implements IReloadableModelContainer<MechanicalPumpRenderer>
{
	private static ModelMechanicalPumpTop model;

	@Override
	public void render(TileEntityMechanicalPump te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		model.axleModel[0].rotateAngleY = 0;
		model.pullThingy1Model[0].offsetY = 0;
		model.pullThingy2Model[0].offsetY = 0;
		model.pullThingy3Model[0].offsetY = 0;
		model.pullThingy4Model[0].offsetY = 0;

		String texture = ImmersiveIntelligence.MODID+":textures/blocks/mechanical_device/pump/pump_top.png";
		if(te!=null)
		{
			if(!te.dummy)
			{
				ClientUtils.bindTexture(texture);
				GlStateManager.pushMatrix();
				GlStateManager.translate(x+1, y+1, z);

				GlStateManager.disableLighting();
				RenderHelper.enableStandardItemLighting();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

				double world_rpm = (te.getWorld().getTotalWorldTime()+partialTicks%RotaryUtils.getRPMMax())/RotaryUtils.getRPMMax();

				boolean b = te.getWorld().isBlockIndirectlyGettingPowered(te.getPos()) > 0;
				int a = (int)(30-(25*(te.rotation.getRotationSpeed()/(float)MechanicalPump.rpmBreakingMax)));
				float progress = 0;
				if(te.rotation.getRotationSpeed() > 0)
					progress = ((te.getWorld().getTotalWorldTime()+partialTicks)%a)/(float)a*(b?1f: 0f);

				model.getBlockRotation(te.facing, false);

				for(ModelRendererTurbo mod : model.baseModel)
					mod.render(0.0625f);

				model.axleModel[0].rotateAngleY = TmtUtil.AngleToTMT((float)(world_rpm*360f*te.rotation.getRotationSpeed()));
				for(ModelRendererTurbo mod : model.axleModel)
					mod.render(0.0625f);

				model.pullThingy1Model[0].offsetY = -12f*Math.abs(progress-0.5f);
				model.pullThingy2Model[0].offsetY = -12f*Math.abs(((progress+0.25f)%1f)-0.5f);
				model.pullThingy3Model[0].offsetY = -12f*Math.abs(((progress+0.35f)%1f)-0.5f);
				model.pullThingy4Model[0].offsetY = -12f*Math.abs(((progress+0.75f)%1f)-0.5f);
				for(ModelRendererTurbo mod : model.pullThingy1Model)
					mod.render(0.0625f);
				for(ModelRendererTurbo mod : model.pullThingy2Model)
					mod.render(0.0625f);
				for(ModelRendererTurbo mod : model.pullThingy3Model)
					mod.render(0.0625f);
				for(ModelRendererTurbo mod : model.pullThingy4Model)
					mod.render(0.0625f);

				GlStateManager.popMatrix();
			}

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			GlStateManager.translate(0.25f, 0.5f, 0.75f);

			model.getBlockRotation(EnumFacing.NORTH, false);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.axleModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.pullThingy1Model)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.pullThingy2Model)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.pullThingy3Model)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.pullThingy4Model)
				mod.render(0.0625f);

			ClientUtils.bindAtlas();
			IBlockState state = IIContent.block_mechanical_device1.getDefaultState().withProperty(IEProperties.MULTIBLOCKSLAVE, false);
			GlStateManager.translate(0, -1, 0);
			ClientUtils.mc().getBlockRendererDispatcher().renderBlockBrightness(state, 1f);


			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMechanicalPumpTop();
	}
}
