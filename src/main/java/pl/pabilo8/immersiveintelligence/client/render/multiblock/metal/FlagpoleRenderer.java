package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelFlagpole;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;

public class FlagpoleRenderer extends TileEntitySpecialRenderer<TileEntityFlagpole> implements IReloadableModelContainer<FlagpoleRenderer>
{
	private static ModelFlagpole model;
	private static ModelFlagpole modelFlipped;
	private static final String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/flagpole.png";
	private static final TileEntityBanner banner = new TileEntityBanner();

	@Override
	public void render(TileEntityFlagpole te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		///tp @p -100 5 -761 126.4 -0.3
		if(te!=null)
		{
			if(te.isDummy())
				return;
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+0.5f, y-1, z+0.5f);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				Vec3i offset = te.facing.getDirectionVec();
				GlStateManager.translate(offset.getX(), 0, offset.getZ());
				GlStateManager.rotate(te.facing.getOpposite().getHorizontalAngle()*(te.facing.getAxis()==Axis.Z?1f:-1f), 0F, 1F, 0F);
			}

			for(ModelRendererTurbo mod : (te.mirrored?modelFlipped:model).baseModel)
				mod.render();

			if(!te.flag.isEmpty())
			{
				banner.setItemValues(te.flag, false);
				double f = ((Math.abs((((getWorld().getTotalWorldTime()+partialTicks)%200)/200f)-0.5f)/0.5f)-0.5f)/0.5f;
				ResourceLocation res = BannerTextures.BANNER_DESIGNS.getResourceLocation(banner.getPatternResourceLocation(), banner.getPatternList(), banner.getColorList());
				if(res!=null)
				{
					ClientUtils.mc().getTextureManager().bindTexture(res);
					GlStateManager.translate(0,5f-0.125f,0);
					GlStateManager.rotate(90,0,0,1);

					drawFlag(3,f);
					//ClientUtils.drawTexturedRect(0f,0f,1f,40/21f, 0.015625f,21/64f,0.015625f,1/40f);
				}


			}

			GlStateManager.popMatrix();

		}
		else
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.35, y-1.1, z-0.35);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.rotate(-7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.4, 0.4, 0.4);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ClientUtils.bindTexture(texture);
			model.render();

			GlStateManager.popMatrix();
		}
	}

	private void drawFlag(int n, double rot)
	{
		// TODO: 27.07.2021 fix uv
		float length=40/(float)n;
		for(int i = 0; i < n; i++)
		{
			GlStateManager.rotate((float)(rot*6.5f*n*(n%2==0?1:-1)),1,0,0);
			ClientUtils.drawTexturedRect(0f,0f,1f,length/21f, 0.015625f,21/64f,0.015625f+(i*length/64f),(length*(i+1))/64f);
			GlStateManager.translate(0f,0f,0.0625f);
			ClientUtils.drawTexturedRect(0f,length/21f,1f,-length/21f, 0.015625f,21/64f,0.015625f+((i+1)*length/64f),(length*i)/64f);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90,0,1,0);
			ClientUtils.drawTexturedRect(0f,0f,0.0625f,length/21f, 0.015625f, 0.015625f,0.015625f+(i*length/64f),(length*(i+1))/64f);
			GlStateManager.translate(0f,0f,1f);
			ClientUtils.drawTexturedRect(0f,length/21f,0.0625f,-length/21f, 0.328125f, 0.328125f,0.015625f+((i+1)*length/64f),(length*i)/64f);

			GlStateManager.popMatrix();
			GlStateManager.translate(0f,length/24f,-0.0625f);
		}
		//GlStateManager.rotate(90,0,0,1);
		GlStateManager.rotate(90,1,0,0);
		GlStateManager.translate(0f,0f,-0.0625f);
		ClientUtils.drawTexturedRect(0f,0f,1f,1.5f/21f, 0.015625f,0.015625f,0.625f, 0.625f);

	}

	@Override
	public void reloadModels()
	{
		model = new ModelFlagpole();
		modelFlipped = new ModelFlagpole();
		modelFlipped.flipAllX();
	}
}
