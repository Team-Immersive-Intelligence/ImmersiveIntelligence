package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxStorage;
import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase.DecoComponentTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar.BarTooltipFormat;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.03.2022
 */
@SideOnly(Side.CLIENT)
public class DecoGuiUtils
{
	public static DecoComponentTemplate<DecoButton> LIST_BUTTON_TEMPLATE = component -> component
			.withBackground(IIReference.RES_TEXTURES_DECO_BUTTON_PAPER)
			.withPadding(0, 0, 0, 0)
			.withSize(14, 14);
	public static DecoComponentTemplate<DecoButton> LIST_BUTTON_EDIT_TEMPLATE = LIST_BUTTON_TEMPLATE.and(
			component -> component
					.withBackgroundColor(IIColor.fromPackedRGB(0x8a7d67))
					.withIcon(IIReference.RES_TEXTURES_DECO_ICON_ACTION_EDIT)
	);
	public static DecoComponentTemplate<DecoButton> LIST_BUTTON_REMOVE_TEMPLATE = LIST_BUTTON_TEMPLATE.and(
			component -> component
					.withBackgroundColor(IIColor.fromPackedRGB(0x8a6865))
					.withIcon(IIReference.RES_TEXTURES_DECO_ICON_ACTION_REMOVE)
	);
	public static DecoComponentTemplate<DecoButton> LIST_BUTTON_ADD_TEMPLATE = LIST_BUTTON_TEMPLATE.and(
			component -> component
					.withBackgroundColor(IIColor.fromPackedRGB(0x778a78))
					.withIcon(IIReference.RES_TEXTURES_DECO_ICON_ACTION_ADD)
	);

	public static Function<IFluxStorage, DecoComponentTemplate<DecoBar>> BAR_ELECTRIC_ENERGY =
			energyStorage -> component -> component
					.withIconLocation(IIReference.RES_ICON_ENERGY)
					.withColors(IIColor.fromPackedRGB(0xb37e28), IIColor.fromPackedRGB(0x663f26))
					.withValueTooltip("energy.stored", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
					.withLimits(0, energyStorage.getMaxEnergyStored(), energyStorage::getEnergyStored);

	public static IIDrawUtils drawBackgroundMask(Collection<DecoRectangle> rects, int minXOffset, int minYOffset)
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

	public static byte[][] getBoxesOutline(Collection<DecoRectangle> rects, BiMap<ResLoc, Byte> spriteMap,
										   int unit, int minXOffset, int minYOffset)
	{
		if(rects.isEmpty())
			return new byte[0][0];

		int xx, yy;

		DecoRectangle b = rects.stream().min((o1, o2) -> o2.x+o2.width-(o1.x+o1.width)).orElse(null);
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
		for(DecoRectangle rect : rects)
			for(int x = rect.x; x < rect.x+rect.width; x += unit)
				for(int y = rect.y; y < rect.y+rect.height; y += unit)
					fillmap[(x-minXOffset)/unit][(y-minYOffset)/unit] = spriteMap.get(rect.mask);

		return fillmap;

	}

	public static IIDrawUtils drawBackgroundBlock(Collection<DecoRectangle> rects)
	{
		IIDrawUtils draw = IIDrawUtils.startTextured();
		for(DecoRectangle rect : rects)
		{
			float rectX = (int)Math.floor(rect.x/8f)*8f;
			float rectY = (int)Math.floor(rect.y/8f)*8f;
			float rectW = (int)Math.ceil(rect.width/8f)*8f;
			float rectH = (int)Math.ceil(rect.height/8f)*8f;

			for(int yy = 0; yy < rectH; yy += 32)
				for(int xx = 0; xx < rectW; xx += 32)
				{
					TextureAtlasSprite sprite = ClientUtils.getSprite(rect.style);
					draw.drawTexRect(rectX+xx, rectY+yy,
							MathHelper.clamp(rectW-xx, 8, 32),
							MathHelper.clamp(rectH-yy, 8, 32),
							sprite.getMinU(), sprite.getInterpolatedU(Math.min(rectW-xx, 32)/2f),
							sprite.getMinV(), sprite.getInterpolatedV(Math.min(rectH-yy, 32)/2f)
					);
				}
		}

		return draw;
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
			for(int xx = 0; xx < width; xx += iSize)
			{
				boolean isLeft = xx==0;
				boolean isRight = xx+iSize >= width;
				boolean isTop = yy==0;
				boolean isBottom = yy+iSize >= height;

				float texX = isLeft?0: (isRight?16-tSize: tStart);
				float texY = isTop?0: (isBottom?16-tSize: tStart);

				int drawWidth = Math.min(iSize, width-xx);
				int drawHeight = Math.min(iSize, height-yy);

				draw.drawTexColorRect(
						x+xx, y+yy, drawWidth, drawHeight, color,
						sprite.getInterpolatedU(texX), sprite.getInterpolatedU(texX+(drawWidth/(float)texSize)*16),
						sprite.getInterpolatedV(texY), sprite.getInterpolatedV(texY+(drawHeight/(float)texSize)*16)
				);
			}
		}
	}

	public static void drawRepeatedRect(IIDrawUtils draw, DecoRectangle rect, IIColor color, int borderSize)
	{
		drawRepeatedRect(draw, rect.x, rect.y, rect.width, rect.height, rect.style, color, 32, borderSize);
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

	public static <T extends TileEntityMultiblockProductionSingle<T, R>, R extends IIIMultiblockRecipe> Function<Float, Float>
	getMultiblockProductionSingleProgress(TileEntityMultiblockProductionSingle<T, R> tile)
	{
		return partialTicks -> {
			if(tile.currentProcess==null)
				return 0f;
			return tile.getProductionProgress(tile.currentProcess, partialTicks);
		};
	}
}
