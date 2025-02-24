package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoTitleLabel;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.01.2025
 **/
public class DecoBackgroundBuilder<T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>>
{
	private final List<List<DecoRectangle>> backgroundTiles = new ArrayList<>();
	private final List<DecoSlot> inventorySlots = new ArrayList<>();
	private final DecoGui<T, C> gui;

	private List<TitleBarData> titleBars = new ArrayList<>();
	int vbo = -1;

	public DecoBackgroundBuilder(DecoGui<T, C> gui)
	{
		this.gui = gui;
		//Initialize with the first layer
		backgroundTiles.add(new ArrayList<>());
	}

	/**
	 * Adds a box to the current background layer with a steel filling texture and round mask
	 *
	 * @param x      x position of the box
	 * @param y      y position of the box
	 * @param width  width of the box in pixels; must be a multiple of 8 or will get rounded up
	 * @param height height of the box in pixels; must be a multiple of 8 or will get rounded up
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withBox(int x, int y, int width, int height)
	{
		return withBox(IIReference.GUI_BG_STEEL, x, y, width, height);
	}

	/**
	 * Adds a box to the current background layer with a round mask
	 *
	 * @param style  filling texture of the box
	 * @param x      x position of the box
	 * @param y      y position of the box
	 * @param width  width of the box in pixels; must be a multiple of 8 or will get rounded up
	 * @param height height of the box in pixels; must be a multiple of 8 or will get rounded up
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withBox(ResLoc style, int x, int y, int width, int height)
	{
		return withBox(style, IIReference.RES_TEXTURES_DECO_TEMPLATE_ROUND, x, y, width, height);
	}

	/**
	 * Adds a box to the current background layer
	 *
	 * @param style  filling texture of the box
	 * @param mask   mask texture of the box
	 * @param x      x position of the box
	 * @param y      y position of the box
	 * @param width  width of the box in pixels; must be a multiple of 8 or will get rounded up
	 * @param height height of the box in pixels; must be a multiple of 8 or will get rounded up
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withBox(ResLoc style, ResLoc mask, int x, int y, int width, int height)
	{
		backgroundTiles.get(backgroundTiles.size()-1).add(new DecoRectangle(x, y, width, height, style, mask));
		return this;
	}

	/**
	 * Starts a new background layer
	 *
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withNextLayer()
	{
		backgroundTiles.add(new ArrayList<>());
		return this;
	}

	/**
	 * Adds a display for item slots on top of background layers
	 *
	 * @param style style of the slots
	 * @param slots slots to display
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withInventorySlots(SlotStyle style, Collection<Slot> slots)
	{
		return this.withInventorySlots(style, slots.toArray(new Slot[0]));
	}

	/**
	 * Adds a display for item slots on top of background layers
	 *
	 * @param style style of the slots
	 * @param slots slots to display
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withInventorySlots(SlotStyle style, Slot... slots)
	{
		for(Slot slot : slots)
			inventorySlots.add(new DecoSlot(slot.xPos, slot.yPos, 16, 16, style));
		return this;
	}

	/**
	 * Adds a title bar component to the GUI
	 *
	 * @param title title of the title bar
	 */
	public DecoBackgroundBuilder<T, C> withTitleBar(String title)
	{
		return withTitleBar(title, DecoAlignment.TOP);
	}

	/**
	 * Adds a title bar component to the GUI
	 *
	 * @param tile tile to get the title from
	 */
	public DecoBackgroundBuilder<T, C> withTitleBar(T tile)
	{
		ITextComponent displayName = tile.getDisplayName();
		if(displayName==null)
			return this;
		return withTitleBar(displayName.getUnformattedText(), DecoAlignment.TOP);
	}

	/**
	 * Adds an Inventory title bar (like ones present in vanilla Minecraft chest, furnace GUIs)
	 */
	public DecoBackgroundBuilder<T, C> withInventoryTitleBar()
	{
		return withTitleBar(I18n.format("container.inventory"), DecoAlignment.TOP_LEFT);
	}

	/**
	 * Adds a title bar component to the GUI
	 *
	 * @param title     title of the title bar
	 * @param alignment alignment of the title bar
	 */
	public DecoBackgroundBuilder<T, C> withTitleBar(String title, DecoAlignment alignment)
	{
		if(backgroundTiles.isEmpty())
			return this;
		//Attach the title bar to the last passed background tile
		List<DecoRectangle> currentLayer = backgroundTiles.get(backgroundTiles.size()-1);
		if(!currentLayer.isEmpty())
			titleBars.add(new TitleBarData(currentLayer.get(currentLayer.size()-1), alignment, I18n.format(title)));
		return this;
	}

	/**
	 * Builds the background, passing the size data to the GUI
	 */
	public void build()
	{
		if(backgroundTiles.isEmpty()) return;

		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

		for(List<DecoRectangle> layer : backgroundTiles)
			for(DecoRectangle tile : layer)
			{
				minX = Math.min(minX, tile.x);
				minY = Math.min(minY, tile.y);
				maxX = Math.max(maxX, tile.x+tile.width);
				maxY = Math.max(maxY, tile.y+tile.height);
			}

		gui.xSize = maxX-minX;
		gui.ySize = maxY-minY;
	}

	public void draw()
	{
		ClientUtils.bindAtlas();
		if(vbo==-1)
		{
			vbo = GlStateManager.glGenLists(1);
			GlStateManager.glNewList(vbo, GL11.GL_COMPILE);

			//Draw background layers
			for(List<DecoRectangle> layer : backgroundTiles)
			{
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

				//Mask
				GL11.glEnable(GL11.GL_STENCIL_TEST);
				GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
				GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
				DecoGuiUtils.drawBackgroundMask(layer, 0, 0).finish();

				//Background
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
				GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
				DecoGuiUtils.drawBackgroundBlock(layer).finish();
				GL11.glDisable(GL11.GL_STENCIL_TEST);

				//Overlay
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
				DecoGuiUtils.drawBackgroundMask(layer, 0, 0).finish();
			}

			//Blending inventory slots (using previous blend func.)
			IIDrawUtils draw = IIDrawUtils.startTexturedColored();
			for(DecoSlot slot : inventorySlots)
				if(slot.style.blending)
				{
					int off = slot.style.borderSize;
					int toff = 2*off;
					DecoGuiUtils.drawRepeatedRect(draw, slot.x-off, slot.y-off, slot.width+toff, slot.height+toff, slot.style.backgroundLocation, IIColor.WHITE, 32, 8);
				}
			draw.finish();

			//Non-blending inventory slots
			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			draw = IIDrawUtils.startTexturedColored();
			for(DecoSlot slot : inventorySlots)
			{
				if(slot.style.blending)
					continue;
				int off = slot.style.borderSize;
				int toff = 2*off;
				DecoGuiUtils.drawRepeatedRect(draw, slot.x-off, slot.y-off,
						slot.width+toff, slot.height+toff,
						slot.style.backgroundLocation, IIColor.WHITE, 32, 8);
				if(slot.style.markerLocation!=null&&slot.style.markerOffset!=-1)
				{
					TextureAtlasSprite sprite = ClientUtils.getSprite(slot.style.markerLocation);
					int offset = (slot.style.markerOffset*4)%16;
					int vOffset = (int)(Math.floor(offset/16f)*3);

					draw.drawTexColorRect(slot.x+slot.width/2f-2f, slot.y-off-3, 4, 3,
							IIColor.WHITE,
							sprite.getInterpolatedU(offset), sprite.getInterpolatedU(offset+4),
							sprite.getInterpolatedV(vOffset), sprite.getInterpolatedV(vOffset+3)
					);
				}
			}
			draw.finish();

			//Ahh, free at last
			GlStateManager.glEndList();
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(gui.guiLeft, gui.guiTop, 0);
		GlStateManager.callList(vbo);

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
	}

	@Nonnull
	public DecoTitleLabel[] getTitleLabel()
	{
		return titleBars.stream().map(this::createTitleLabel)
				.filter(Objects::nonNull)
				.toArray(DecoTitleLabel[]::new);
	}

	private DecoTitleLabel createTitleLabel(TitleBarData data)
	{
		if(data.barTitle==null||data.barTitle.isEmpty())
			return null;
		int stringWidth = Math.min(data.barRectangle.width, IIClientUtils.fontRegular.getStringWidth(data.barTitle));
		int stringHeight = IIClientUtils.fontRegular.getWordWrappedHeight(data.barTitle, stringWidth);

		int titleBarX = data.barAlignment.getAlignX(data.barRectangle.x+8, stringWidth, data.barRectangle.width-16);
		int titleBarY = data.barAlignment.getAlignY(data.barRectangle.y-2, stringHeight, data.barRectangle.height+4);

		DecoLabel decoLabel = new DecoTitleLabel(IIClientUtils.fontRegular, titleBarX, titleBarY)
				.withBackgroundLocation(data.barRectangle.style.replace("background/", "label/label_"))
				.withAlign(data.barAlignment)
				.withSize(stringWidth, stringHeight)
				.withRawText(data.barTitle);

		return ((DecoTitleLabel)decoLabel);
	}

	public void cleanup()
	{
		if(vbo!=-1)
		{
			GlStateManager.glDeleteLists(vbo, 1);
			vbo = -1;
		}
	}

	public DecoRectangle[] getTakenSpace()
	{
		return backgroundTiles.stream().flatMap(List::stream).toArray(DecoRectangle[]::new);
	}

	private static class DecoSlot
	{
		final int x, y, width, height;
		final SlotStyle style;

		public DecoSlot(int x, int y, int width, int height, SlotStyle style)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.style = style;
		}
	}

	public enum SlotStyle
	{
		//Vanilla MC bevel mask inventory slot
		VANILLA(IIReference.RES_TEXTURES_DECO_INVENTORY_SLOT, true, 1),
		//Immersive Engineering style inventory slot
		IE(IIReference.RES_TEXTURES_DECO_IE_SLOT, IIReference.RES_TEXTURES_DECO_IE_SLOT_MARKER, 2),
		IE_INPUT(IE, 2),
		IE_OUTPUT(IE, 3),
		IE_CUSTOM1(IE, 4),
		IE_CUSTOM2(IE, 5),
		IE_CUSTOM3(IE, 6),
		IE_CUSTOM4(IE, 7),
		//Brass Frame IE style Inventory slot
		IE_BRASS(IIReference.RES_TEXTURES_DECO_IE_BRASS_SLOT, IIReference.RES_TEXTURES_DECO_IE_BRASS_SLOT_MARKER, 2),
		IE_BRASS_INPUT(IE_BRASS, 2),
		IE_BRASS_OUTPUT(IE_BRASS, 3),
		IE_BRASS_CUSTOM1(IE_BRASS, 4),
		IE_BRASS_CUSTOM2(IE_BRASS, 5),
		IE_BRASS_CUSTOM3(IE_BRASS, 6),
		IE_BRASS_CUSTOM4(IE_BRASS, 7),
		;

		final int borderSize;
		final boolean blending;
		final ResLoc backgroundLocation, markerLocation;
		final int markerOffset;

		SlotStyle(ResLoc backgroundLocation, boolean blending, int borderSize)
		{
			this.blending = blending;
			this.backgroundLocation = backgroundLocation;
			this.markerLocation = null;
			this.borderSize = borderSize;
			this.markerOffset = -1;
		}

		SlotStyle(ResLoc backgroundLocation, ResLoc markerLocation, int borderSize)
		{
			this.blending = false;
			this.backgroundLocation = backgroundLocation;
			this.markerLocation = markerLocation;
			this.borderSize = borderSize;
			this.markerOffset = -1;
		}

		SlotStyle(SlotStyle base, int markerOffset)
		{
			this.blending = false;
			this.backgroundLocation = base.backgroundLocation;
			this.markerLocation = base.markerLocation;
			this.borderSize = base.borderSize;
			this.markerOffset = markerOffset;
		}
	}

	private static class TitleBarData
	{
		final DecoRectangle barRectangle;
		final DecoAlignment barAlignment;
		final String barTitle;

		public TitleBarData(DecoRectangle barRectangle, DecoAlignment barAlignment, String barTitle)
		{
			this.barRectangle = barRectangle;
			this.barAlignment = barAlignment;
			this.barTitle = barTitle;
		}
	}

}
