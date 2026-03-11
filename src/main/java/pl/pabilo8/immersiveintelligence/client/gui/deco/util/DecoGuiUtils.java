package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.03.2022
 */
@SideOnly(Side.CLIENT)
public class DecoGuiUtils
{
	public static IIDrawUtils drawBackgroundMask(Collection<DecoBackgroundTile> rects, int minXOffset, int minYOffset)
	{
		IIDrawUtils draw = IIDrawUtils.startTextured();

		BiMap<Byte, ResLoc> spriteMap = HashBiMap.create();
		rects.stream()
				.map(rect -> rect.mask)
				.distinct()
				.forEach(resLoc -> spriteMap.put((byte)(spriteMap.size()+1), resLoc));
		byte[][] outline = getBoxesOutline(rects, spriteMap.inverse(), 8, minXOffset, minYOffset);

		for(int x = 0; x < outline.length; x++)
			for(int y = 0; y < outline[x].length; y++)
				if(outline[x][y]!=0)
				{
					boolean hasRight = x!=outline.length-1&&outline[x+1][y]!=0;
					boolean hasLeft = x!=0&&outline[x-1][y]!=0;
					boolean hasBottom = y!=outline[x].length-1&&outline[x][y+1]!=0;
					boolean hasTop = y!=0&&outline[x][y-1]!=0;
					boolean hasAll = hasRight&&hasLeft&&hasTop&&hasBottom;
					int tOffset = hasAll?8: 0;

					int texX = 2, texY = 2;
					//Check for diagonal corners or draw central piece
					if(hasAll)
					{
						boolean hasTL = outline[x-1][y-1]!=0;
						boolean hasTR = outline[x+1][y-1]!=0;
						boolean hasBL = outline[x-1][y+1]!=0;

						texX += !hasTL?-2: (!hasTR?4: 0);
						texY += !hasTL?-2: (!hasBL?4: 0);
					}
					//Check for corner piece
					else
					{
						texX += (!hasLeft)?-2: (!hasRight?4: 0);
						texY += (!hasTop)?-2: (!hasBottom?4: 0);
					}

					ResLoc res = spriteMap.get(outline[x][y]);
					assert res!=null;
					TextureAtlasSprite maskSprite = ClientUtils.getSprite(res);
					draw.drawTexRect(
							minXOffset+x*8, minYOffset+y*8, 8, 8,
							maskSprite.getInterpolatedU(tOffset+texX), maskSprite.getInterpolatedU(tOffset+texX+2),
							maskSprite.getInterpolatedV(texY), maskSprite.getInterpolatedV(texY+2)
					);
				}

		return draw;
	}

	public static byte[][] getBoxesOutline(Collection<DecoBackgroundTile> rects, BiMap<ResLoc, Byte> spriteMap,
										   int unit, int minXOffset, int minYOffset)
	{
		if(rects.isEmpty())
			return new byte[0][0];

		int xx, yy;

		DecoBackgroundTile b = rects.stream().min((o1, o2) -> o2.x+o2.width-(o1.x+o1.width)).orElse(null);
		xx = b.x+b.width-minXOffset;
		b = rects.stream().min((o1, o2) -> o2.y+o2.height-(o1.y+o1.height)).orElse(null);
		yy = b.y+b.height-minYOffset;

		xx /= unit;
		yy /= unit;

		byte[][] fillmap = new byte[xx+1][yy+1];

		//fill box map with 0
		for(int i = 0; i <= xx; i++)
			for(int j = 0; j <= yy; j++)
				fillmap[i][j] = 0;


		//fill box occupied spaces with 1
		for(DecoBackgroundTile rect : rects)
			for(int x = rect.x; x < rect.x+rect.width; x += unit)
				for(int y = rect.y; y < rect.y+rect.height; y += unit)
					fillmap[(x-minXOffset)/unit][(y-minYOffset)/unit] = spriteMap.get(rect.mask);

		return fillmap;

	}

	public static IIDrawUtils drawBackgroundBlock(Collection<DecoBackgroundTile> rects, int minXOffset, int minYOffset)
	{
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		for(DecoBackgroundTile rect : rects)
		{
			float rectX = (int)Math.floor(rect.x/8f)*8f;
			float rectY = (int)Math.floor(rect.y/8f)*8f;
			float rectW = (int)Math.ceil(rect.width/8f)*8f;
			float rectH = (int)Math.ceil(rect.height/8f)*8f;

			for(int yy = 0; yy < rectH; yy += 32)
				for(int xx = 0; xx < rectW; xx += 32)
				{
					TextureAtlasSprite sprite = ClientUtils.getSprite(rect.style);
					draw.drawTexColorRect(rectX+xx+minXOffset, rectY+yy+minYOffset,
							MathHelper.clamp(rectW-xx, 8, 32),
							MathHelper.clamp(rectH-yy, 8, 32),
							rect.color,
							sprite.getMinU(), sprite.getInterpolatedU(Math.min(rectW-xx, 32)/2f),
							sprite.getMinV(), sprite.getInterpolatedV(Math.min(rectH-yy, 32)/2f)
					);
				}
		}

		return draw;
	}

	public static void drawFrameCorners(IIDrawUtils draw, int x, int y, int width, int height, ResLoc style, boolean[] sides)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(style);
		int cornerSize = 16;

		//Top-left corner
		if(sides[0]&&sides[3])
			draw.drawTexColorRect(x, y, cornerSize, cornerSize, IIColor.WHITE,
					sprite.getMinU(), sprite.getInterpolatedU(8), sprite.getMinV(), sprite.getInterpolatedV(8));
		//Top-right corner
		if(sides[0]&&sides[1])
			draw.drawTexColorRect(x+width-cornerSize, y, cornerSize, cornerSize, IIColor.WHITE,
					sprite.getInterpolatedU(16-8), sprite.getInterpolatedU(16), sprite.getMinV(), sprite.getInterpolatedV(8));
		//Bottom-left corner
		if(sides[2]&&sides[3])
			draw.drawTexColorRect(x, y+height-cornerSize, cornerSize, cornerSize, IIColor.WHITE,
					sprite.getMinU(), sprite.getInterpolatedU(8), sprite.getInterpolatedV(16-8), sprite.getInterpolatedV(16));
		//Bottom-right corner
		if(sides[2]&&sides[1])
			draw.drawTexColorRect(x+width-cornerSize, y+height-cornerSize, cornerSize, cornerSize, IIColor.WHITE,
					sprite.getInterpolatedU(16-8), sprite.getInterpolatedU(16), sprite.getInterpolatedV(16-8), sprite.getInterpolatedV(16));
	}

	public static void drawFrame(IIDrawUtils draw, int x, int y, int width, int height, ResLoc style, boolean[] sides, int frameThickness)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(style);
		//x -= frameThickness/2;
		//y -= frameThickness/2;
//		width += frameThickness;
//		height += frameThickness;

		//Top-Left mappings
		float minU = sprite.getMinU();
		float minUU = sprite.getInterpolatedU(frameThickness/2f);
		float minV = sprite.getMinV();
		float minVV = sprite.getInterpolatedV(frameThickness/2f);
		//Bottom-Right mappings
		float maxU = sprite.getInterpolatedU(16-frameThickness/2f);
		float maxUU = sprite.getInterpolatedU(16);
		float maxV = sprite.getInterpolatedV(16-frameThickness/2f);
		float maxVV = sprite.getInterpolatedV(16);

		//Draw main frame

		//Top
		if(sides[0])
			draw.drawRepeatedTexColorRect(x+frameThickness, y, width-frameThickness*2, frameThickness, IIColor.WHITE,
					32-2*frameThickness, frameThickness, minUU, maxU, minV, minVV);
		//Bottom
		if(sides[1])
			draw.drawRepeatedTexColorRect(x+frameThickness, y+height-frameThickness, width-frameThickness*2, frameThickness, IIColor.WHITE,
					32-2*frameThickness, frameThickness, minUU, maxU, maxV, maxVV);
		//Left
		if(sides[2])
			draw.drawRepeatedTexColorRect(x, y+frameThickness, frameThickness, height-frameThickness*2, IIColor.WHITE,
					frameThickness, 32-2*frameThickness, minU, minUU, minVV, maxV);
		//Right
		if(sides[3])
			draw.drawRepeatedTexColorRect(x+width-frameThickness, y+frameThickness, frameThickness, height-frameThickness*2, IIColor.WHITE,
					frameThickness, 32-2*frameThickness, maxU, maxUU, minVV, maxV);

		//Draw squares on frame edges
		if(sides[0]||sides[3])
			draw.drawTexColorRect(x, y, frameThickness, frameThickness, IIColor.WHITE,
					minU, minUU, minV, minVV);
		if(sides[0]||sides[1])
			draw.drawTexColorRect(x+width-frameThickness, y, frameThickness, frameThickness, IIColor.WHITE,
					maxU, maxUU, minV, minVV);
		if(sides[2]||sides[3])
			draw.drawTexColorRect(x, y+height-frameThickness, frameThickness, frameThickness, IIColor.WHITE,
					minU, minUU, maxV, maxVV);
		if(sides[2]||sides[1])
			draw.drawTexColorRect(x+width-frameThickness, y+height-frameThickness, frameThickness, frameThickness, IIColor.WHITE,
					maxU, maxUU, maxV, maxVV);
	}


	/**
	 * Draws a repeated texture in a rectangle, respecting corners
	 *
	 * @param draw    draw utils instance
	 * @param texSize size of the texture to calculate corners from (it's drawn 1:1)
	 */
	public static void drawRepeatedRect(IIDrawUtils draw, int x, int y, int width, int height,
										ResourceLocation spriteLocation, IIColor color, int texSize, int borderSize)
	{
		int iSize = Math.min(texSize-2*borderSize, Math.min(width, height)/2);
		float tSize = (iSize/(float)texSize)*16;
		float tStart = (borderSize/(float)texSize)*16;

		TextureAtlasSprite sprite = ClientUtils.getSprite(spriteLocation);
		for(int yy = 0; yy < height; yy += iSize)
		{
			int drawHeight = Math.min(iSize, height-yy);
			boolean isTop = yy==0;
			boolean isBottom = yy+drawHeight >= height;

			for(int xx = 0; xx < width; xx += iSize)
			{
				int drawWidth = Math.min(iSize, width-xx);
				boolean isLeft = xx==0;
				boolean isRight = xx+drawWidth >= width;

				float texX = isLeft?0: (isRight?16-((drawWidth/(float)texSize)*16): tStart);
				float texY = isTop?0: (isBottom?16-((drawHeight/(float)texSize)*16): tStart);

				draw.drawTexColorRect(
						x+xx, y+yy, drawWidth, drawHeight, color,
						sprite.getInterpolatedU(texX), sprite.getInterpolatedU(texX+(drawWidth/(float)texSize)*16),
						sprite.getInterpolatedV(texY), sprite.getInterpolatedV(texY+(drawHeight/(float)texSize)*16)
				);
			}
		}
	}

	/**
	 * Sets the system clipboard string, with a fallback to Minecraft's clipboard handling
	 *
	 * @param string String to set
	 */
	public static void setClipboardString(String string)
	{
		try
		{
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
		} catch(Exception ignored)
		{
			GuiScreen.setClipboardString(string);
		}
	}

	/**
	 * Sets NBT to the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @param compound NBT to set
	 */
	public static void setClipboardNBT(NBTTagCompound compound)
	{
		setClipboardString(compound.toString());
	}

	/**
	 * Sets EasyNBT to the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @param nbt EasyNBT to set
	 */
	public static void setClipboardEasyNBT(EasyNBT nbt)
	{
		setClipboardNBT(nbt.unwrap());
	}

	/**
	 * Reads a string from the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @return Clipboard string
	 */
	public static String getClipboardString()
	{
		try
		{
			Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if(t!=null&&t.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String)t.getTransferData(DataFlavor.stringFlavor);
		} catch(Exception ignored) {}
		return GuiScreen.getClipboardString();
	}

	/**
	 * Reads NBT from the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @return Clipboard NBT
	 */
	public static NBTTagCompound getClipboardNBT()
	{
		String clipboardString = getClipboardString();
		try
		{
			return EasyNBT.parseNBT(clipboardString);
		} catch(Exception e)
		{
			return new NBTTagCompound();
		}
	}

	/**
	 * Reads EasyNBT from the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @return Clipboard EasyNBT
	 */
	public static EasyNBT getClipboardEasyNBT()
	{
		String clipboardString = getClipboardString();
		try
		{
			return EasyNBT.parseEasyNBT(clipboardString);
		} catch(Exception e)
		{
			return EasyNBT.newNBT();
		}
	}

	public static <T extends TileEntityMultiblockProductionMulti<T, R>, R extends IIIMultiblockRecipe> Function<Float, Float>
	getMultiblockProductionMultiProgress(TileEntityMultiblockProductionMulti<T, R> tile)
	{
		return partialTicks -> {
			if(tile.processQueue.isEmpty())
				return 0f;
			return tile.getProductionProgress(tile.processQueue.get(0), partialTicks);
		};
	}

	public static <T extends TileEntityMultiblockProductionMulti<T, R>, R extends IIIMultiblockRecipe> Function<Float, Float>
	getMultiblockMultiProgress(TileEntityMultiblockProductionMulti<T, R> tile, float startFraction, float endFraction)
	{
		final float duration = endFraction-startFraction;
		return partialTicks -> {
			if(tile.processQueue.isEmpty())
				return 0f;
			float progress = tile.getProductionProgress(tile.processQueue.get(0), partialTicks);
			return MathHelper.clamp((progress-startFraction)/duration, 0, 1);
		};
	}

	public static <T extends TileEntityMultiblockProductionSingle<T, R>, R extends IIIMultiblockRecipe> Function<Float, Float>
	getMultiblockProductionSingleProgress(TileEntityMultiblockProductionSingle<T, R> tile)
	{
		return partialTicks -> {
			if(tile.currentProcess==null)
				return 0f;
			return tile.getProductionProgress(tile.currentProcess, partialTicks);
		};
	}

	public static <T extends TileEntityMultiblockProductionSingle<T, R>, R extends IIIMultiblockRecipe> Function<Float, Float>
	getMultiblockProductionSingleProgress(TileEntityMultiblockProductionSingle<T, R> tile, float startFraction, float endFraction)
	{
		final float duration = endFraction-startFraction;
		return partialTicks -> {
			if(tile.currentProcess==null)
				return 0f;
			float progress = tile.getProductionProgress(tile.currentProcess, partialTicks);
			return MathHelper.clamp((progress-startFraction)/duration, 0, 1);
		};
	}
}
