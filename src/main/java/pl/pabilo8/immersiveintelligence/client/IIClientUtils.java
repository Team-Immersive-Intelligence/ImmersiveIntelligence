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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRendererCustomGlyphs;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
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
	private static final HashMap<Fluid, IIColor> CACHED_COLORS = new HashMap<>();

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
	@Deprecated
	public static void drawStringCentered(FontRenderer fontRenderer, String string, int x, int y, int w, int h, int colour)
	{
		fontRenderer.drawString(string, x+(w/2)-(fontRenderer.getStringWidth(string)/2), y+h, colour);
	}

	//Cheers, Blu ^^
	@SideOnly(Side.CLIENT)
	public static void drawBlockBreak(WorldClient world, float partialTicks, DamageBlockPos... positions)
	{
		if(positions.length==0)
			return;

		Entity viewEntity = mc().getRenderViewEntity();
		if(viewEntity==null)
			return;

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		BlockRendererDispatcher brd = mc().getBlockRendererDispatcher();

		//Get the rendering centre position.
		double posX = viewEntity.lastTickPosX+(viewEntity.posX-viewEntity.lastTickPosX)*(double)partialTicks;
		double posY = viewEntity.lastTickPosY+(viewEntity.posY-viewEntity.lastTickPosY)*(double)partialTicks;
		double posZ = viewEntity.lastTickPosZ+(viewEntity.posZ-viewEntity.lastTickPosZ)*(double)partialTicks;

		TextureMap atlas = mc().getTextureMapBlocks();
		bindAtlas();
		atlas.setBlurMipmap(false, false);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(
				GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
		);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.doPolygonOffset(-3.0F, -3.0F);
		GlStateManager.enablePolygonOffset();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.enableAlpha();

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
		atlas.restoreLastBlurMipmap();

		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0.0F, 0.0F);
		GlStateManager.disablePolygonOffset();
		GlStateManager.tryBlendFuncSeparate(
				GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
		);
		GlStateManager.disableBlend();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	@Nonnull
	public static IIColor getFluidTextureColor(@Nonnull FluidStack fluid)
	{
		int color = fluid.getFluid().getColor(fluid);
		if(color!=0xFFFFFFFF)
			return IIColor.fromPackedARGB(color);
		return getFluidTextureColor(fluid.getFluid());
	}

	@Nonnull
	public static IIColor getFluidTextureColor(@Nonnull Fluid fluid)
	{
		if(CACHED_COLORS.containsKey(fluid))
			return CACHED_COLORS.get(fluid);

		if(fluid.getColor()!=0xFFFFFFFF)
			return IIColor.fromPackedARGB(fluid.getColor());

		InputStream is;
		BufferedImage image;
		IIColor color;
		try
		{
			final ResourceLocation f = new ResourceLocation(fluid.getStill().getResourceDomain(), "textures/"+fluid.getStill().getResourcePath()+".png");
			final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(f);
			is = resource.getInputStream();
			image = ImageIO.read(is);

			//Calculate mean color from the entire image
			long sumR = 0, sumG = 0, sumB = 0;
			int pixelCount = 0;
			int width = image.getWidth();
			int height = image.getHeight();

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++)
				{
					int rgb = image.getRGB(x, y);
					int alpha = (rgb>>24)&0xFF;
					//Only count non-transparent pixels
					if(alpha > 0)
					{
						sumR += (rgb>>16)&0xFF;
						sumG += (rgb>>8)&0xFF;
						sumB += rgb&0xFF;
						pixelCount++;
					}
				}

			if(pixelCount > 0)
			{
				int avgR = (int)(sumR/pixelCount);
				int avgG = (int)(sumG/pixelCount);
				int avgB = (int)(sumB/pixelCount);
				color = IIColor.fromPackedRGB((avgR<<16)|(avgG<<8)|avgB);
			}
			else
				color = IIColor.WHITE;
		} catch(IOException e)
		{
			IILogger.error("Could not load fluid texture file for color analysis");
			color = IIColor.WHITE;
		}
		CACHED_COLORS.put(fluid, color);
		return color;
	}

	//Thanks Blu, these stencil buffers look really capable
	@Deprecated
	public static void drawArmorBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, DecoColors.ARMOR_INTEGRITY_1, DecoColors.ARMOR_INTEGRITY_2, progress);
	}

	@Deprecated
	public static void drawPowerBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, DecoColors.POWER1, DecoColors.POWER2, progress);
	}

	@Deprecated
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

	public static void bindAtlas()
	{
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
