package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.03.2022
 */
@SideOnly(Side.CLIENT)
public class DecoGuiUtils
{

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
}
