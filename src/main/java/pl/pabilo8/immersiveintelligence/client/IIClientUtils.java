package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRendererCustomGlyphs;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 29.08.2022
 */
@SideOnly(Side.CLIENT)
public class IIClientUtils
{
	@SideOnly(Side.CLIENT)
	public static IIFontRenderer fontRegular;
	@SideOnly(Side.CLIENT)
	public static IIFontRendererCustomGlyphs fontEngineerTimes, fontNormung, fontKaiser, fontTinkerer;

	@SideOnly(Side.CLIENT)
	private static Minecraft mc()
	{
		return Minecraft.getMinecraft();
	}

	@SideOnly(Side.CLIENT)
	public static Vec3d[] extractVertexPositions(BakedQuad quad)
	{
		int[] vertexData = quad.getVertexData();
		VertexFormat format = quad.getFormat();
		int positionIndex = -1;

		//Find the position element index in the VertexFormat
		for(int i = 0; i < format.getElementCount(); i++)
		{
			VertexFormatElement element = format.getElement(i);
			if(element.getUsage()==VertexFormatElement.EnumUsage.POSITION)
			{
				positionIndex = i;
				break;
			}
		}

		if(positionIndex==-1)
			throw new IllegalStateException("Vertex format does not contain position data");

		//Size of one vertex in the vertexData array
		int vertexSize = format.getIntegerSize();
		//4 vertices
		Vec3d[] positions = new Vec3d[4];

		for(int i = 0; i < 4; i++)
		{
			//Base index for the position data of the i-th vertex
			int baseIndex = i*vertexSize+positionIndex*4;
			float x = Float.intBitsToFloat(vertexData[baseIndex]);
			float y = Float.intBitsToFloat(vertexData[baseIndex+1]);
			float z = Float.intBitsToFloat(vertexData[baseIndex+2]);
			positions[i] = new Vec3d(x, y, z);
		}

		return positions;
	}

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
	public static void drawBlockBreak(WorldClient world, float partialTicks, DamageBlockPos... positions)
	{
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		BlockRendererDispatcher brd = mc().getBlockRendererDispatcher();
		TextureManager tex = mc().renderEngine;
		EntityPlayer player = ClientUtils.mc().player;

		//get rendering centre position
		double posX = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double posY = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double posZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;

		tex.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(774, 768, 1, 1);
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
		GlStateManager.doPolygonOffset(-3.0F, -3.0F);
		GlStateManager.enablePolygonOffset();


		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		buf.setTranslation(-posX, -posY, -posZ);
		buf.noColor();

		for(DamageBlockPos pos : positions)
		{
			IBlockState state = world.getBlockState(pos);
			int progress = 9-(int)MathHelper.clamp(pos.damage*10f, 0f, 10f); // 0-10
			if(progress < 0||state.getMaterial()==Material.AIR)
				continue;

			Block block = state.getBlock();
			TileEntity te = world.getTileEntity(pos);
			boolean shouldOmit = block instanceof BlockIIMultiblock<?>;
			if(!shouldOmit)
				shouldOmit = te!=null&&te.canRenderBreaking();

			if(shouldOmit)
				continue;

			brd.renderBlockDamage(state, pos, ClientUtils.destroyBlockIcons[progress], world);
		}


		tes.draw();
		buf.setTranslation(0.0D, 0.0D, 0.0D);
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
		drawGradientBar(x, y, w, h, IIReference.COLOR_ARMORBAR1, IIReference.COLOR_ARMORBAR2, progress);
	}

	public static void drawPowerBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, IIReference.COLOR_POWERBAR1, IIReference.COLOR_POWERBAR2, progress);
	}

	public static void drawGradientBar(int x, int y, int w, int h, IIColor colorFrom, IIColor colorTo, float progress)
	{
		int stored = (int)(h*progress);
		ClientUtils.drawGradientRect(x, y+(h-stored), x+w, y+h, colorFrom.getPackedARGB(), colorTo.getPackedARGB());
	}

	@SideOnly(Side.CLIENT)
	public static void bindTexture(ResourceLocation path)
	{
		mc().getTextureManager().bindTexture(path);
	}

	@SideOnly(Side.CLIENT)
	public static void displayScreen(GuiScreen screen)
	{
		mc().displayGuiScreen(screen);
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

	@SideOnly(Side.CLIENT)
	public static EntityPlayer getPlayer()
	{
		return mc().player;
	}

	public static void addTooltip(List<String> tooltip, char charIcon, String line, Object... arguments)
	{
		if(charIcon==' ')
			tooltip.add(I18n.format(line, arguments));
		else
			tooltip.add(charIcon+" "+I18n.format(line, arguments));
	}

	public static void addTooltip(List<String> tooltip, String line, Object... arguments)
	{
		addTooltip(tooltip, ' ', line, arguments);
	}
}
