package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 29.08.2022
 */
@SideOnly(Side.CLIENT)
public class IIClientUtils
{

	@SideOnly(Side.CLIENT)
	public static void drawStringCentered(FontRenderer fontRenderer, String string, int x, int y, int w, int h, int colour)
	{
		fontRenderer.drawString(string, x+(w/2)-(fontRenderer.getStringWidth(string)/2), y+h, colour);
	}

	@SideOnly(Side.CLIENT)
	public static void drawStringCenteredScaled(FontRenderer fontRenderer, String string, int x, int y, int w, int h, float scale, int colour)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+((w/2)-(fontRenderer.getStringWidth(string)*scale/2)), y+h, 0);
		GlStateManager.scale(scale, scale, 1);
		fontRenderer.drawString(string, 0, 0, colour);
		GlStateManager.popMatrix();
	}

	//Cheers, Blu ^^
	@SideOnly(Side.CLIENT)
	public static void tesselateBlockBreak(Tessellator tessellatorIn, WorldClient world, DamageBlockPos blockpos, float partialTicks)
	{
		tesselateBlockBreak(tessellatorIn, world, blockpos, blockpos.damage, partialTicks);
	}

	//Cheers, Blu ^^
	@SideOnly(Side.CLIENT)
	public static void tesselateBlockBreak(Tessellator tessellatorIn, WorldClient world, DimensionBlockPos blockpos, Float value, float partialTicks)
	{
		BufferBuilder worldRendererIn = tessellatorIn.getBuffer();
		EntityPlayer player = ClientUtils.mc().player;
		double d0 = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double d1 = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double d2 = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;
		TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
		int progress = 9-(int)MathHelper.clamp(value*10f, 0f, 10f); // 0-10
		if(progress < 0)
			return;
		renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//preRenderDamagedBlocks BEGIN
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(774, 768, 1, 1);
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.doPolygonOffset(-3.0F, -3.0F);
		GlStateManager.enablePolygonOffset();
		//GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableAlpha();


		worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		worldRendererIn.setTranslation(-d0, -d1, -d2);

		Block block = world.getBlockState(blockpos).getBlock();
		TileEntity te = world.getTileEntity(blockpos);
		boolean hasBreak = block instanceof BlockChest||block instanceof BlockEnderChest
				||block instanceof BlockSign||block instanceof BlockSkull||block instanceof BlockIIMultiblock<?>;
		if(!hasBreak) hasBreak = te!=null&&te.canRenderBreaking();
		if(!hasBreak)
		{
			IBlockState iblockstate = world.getBlockState(blockpos);
			if(iblockstate.getMaterial()!=Material.AIR)
			{
				TextureAtlasSprite textureatlassprite = ClientUtils.destroyBlockIcons[progress];
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
				blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, world);
			}
		}
		tessellatorIn.draw();

		worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
		// postRenderDamagedBlocks BEGIN
		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0.0F, 0.0F);
		GlStateManager.disablePolygonOffset();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_COLOR, GL11.GL_DST_COLOR, 1, 1);
		GlStateManager.enableAlpha();
		GlStateManager.color(1f, 1f, 1f, 1f);

		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();

	}

	//Thanks Blu, these stencil buffers look really capable
	public static void drawArmorBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, IILib.COLOR_ARMORBAR_1, IILib.COLOR_ARMORBAR_2, progress);
	}

	public static void drawPowerBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, IILib.COLOR_POWERBAR_1, IILib.COLOR_POWERBAR_2, progress);
	}

	public static void drawGradientBar(int x, int y, int w, int h, int color1, int color2, float progress)
	{
		int stored = (int)(h*progress);
		ClientUtils.drawGradientRect(x, y+(h-stored), x+w, y+h, color1, color2);
	}

	@SideOnly(Side.CLIENT)
	public static void bindTexture(ResourceLocation path)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(path);
	}

	@SideOnly(Side.CLIENT)
	public static void drawRope(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz, double xdiff, double zdiff)
	{
		buff.pos(x+xdiff, y, z-zdiff).tex(0f, 0f).endVertex();
		buff.pos(xx+xdiff, yy, zz-zdiff).tex(0f, 1f).endVertex();
		buff.pos(xx-xdiff, yy, zz+zdiff).tex(0.125f, 1f).endVertex();
		buff.pos(x-xdiff, y, z+zdiff).tex(0.125f, 0f).endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static void drawFace(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz, double u, double uu, double v, double vv)
	{
		buff.pos(x, y, z).tex(u, v).endVertex();
		buff.pos(x, yy, z).tex(u, vv).endVertex();
		buff.pos(xx, yy, zz).tex(uu, vv).endVertex();
		buff.pos(xx, y, zz).tex(uu, v).endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static void drawFluidBlock(@Nonnull FluidStack fs, boolean flowing, double x, double y, double z, double xx, double yy, double zz, boolean drawEnd)
	{
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		String tex = (flowing?fs.getFluid().getFlowing(fs): fs.getFluid().getStill(fs)).toString();
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tex);

		if(sprite!=null)
		{
			int col = fs.getFluid().getColor(fs);
			double u = sprite.getMinU(), uu = sprite.getMaxU(), v = sprite.getMinV(), vv = v+((sprite.getMaxV()-v)*Math.min(1, (yy-y)/sprite.getIconHeight()));
			double ux = u+((uu-u)*Math.min(1, (xx-x)/sprite.getIconWidth())), uz = u+(uu*Math.min(1, (zz-z)/sprite.getIconWidth()));

			GlStateManager.color((col>>16&255)/255.0f, (col>>8&255)/255.0f, (col&255)/255.0f, 1);

			BufferBuilder buff = Tessellator.getInstance().getBuffer();

			buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			drawFace(buff, x, yy, z, xx, y, z, u, ux, v, vv);
			drawFace(buff, x, yy, zz, xx, y, zz, u, ux, v, vv);
			drawFace(buff, x, yy, z, x, y, zz, u, ux, v, vv);
			drawFace(buff, xx, yy, z, xx, y, zz, u, ux, v, vv);

			Tessellator.getInstance().draw();

			GlStateManager.color(1f, 1f, 1f);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawItemProgress(ItemStack stack1, ItemStack stack2, float progress, TransformType type, Tessellator tessellator, float scale)
	{
		GlStateManager.scale(scale, scale, scale);

		if(progress==0)
			ClientUtils.mc().getRenderItem().renderItem(stack1, type);
		else if(progress==1)
			ClientUtils.mc().getRenderItem().renderItem(stack2, type);
		else
		{
			float h0 = -.5f;
			float h1 = h0+progress;

			BufferBuilder worldrenderer = tessellator.getBuffer();

			GL11.glEnable(GL11.GL_STENCIL_TEST);

			GlStateManager.colorMask(false, false, false, false);
			GlStateManager.depthMask(false);

			GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

			GL11.glStencilMask(0xFF);
			GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);

			GlStateManager.rotate(90.0F-ClientUtils.mc().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

			GlStateManager.disableTexture2D();
			worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			ClientUtils.renderBox(worldrenderer, -.5, h0, -.5, .5, h1, .5);
			tessellator.draw();
			GlStateManager.enableTexture2D();

			GlStateManager.rotate(-(90.0F-ClientUtils.mc().getRenderManager().playerViewY), 0.0F, 1.0F, 0.0F);

			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.depthMask(true);

			GL11.glStencilMask(0x00);

			GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
			ClientUtils.mc().getRenderItem().renderItem(stack1, type);

			GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
			ClientUtils.mc().getRenderItem().renderItem(stack2, type);

			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}

		GlStateManager.scale(1/scale, 1/scale, 1/scale);
	}

	@SideOnly(Side.CLIENT)
	public static ModelRendererTurbo[] createConstructionModel(@Nullable MachineUpgrade upgrade, ModelIIBase model)
	{
		int partCount = model.parts.values().stream().mapToInt(modelRendererTurbos -> modelRendererTurbos.length).sum();
		if(upgrade!=null)
			upgrade.setRequiredSteps(partCount);
		ModelRendererTurbo[] output = new ModelRendererTurbo[partCount];
		int i = 0;
		for(ModelRendererTurbo[] value : model.parts.values())
		{
			Arrays.sort(value, (o1, o2) -> (int)(o1.rotationPointY-o2.rotationPointY));
			for(ModelRendererTurbo mod : value)
			{
				output[i] = mod;
				i++;
			}
		}

		return output;
	}
}
