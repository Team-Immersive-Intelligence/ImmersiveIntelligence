package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoTitleLabel;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.01.2025
 **/
public class DecoBackgroundBuilder<T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>>
{
	private final List<List<DecoBackgroundTile>> backgroundTiles = new ArrayList<>();
	private final List<DecoBackgroundTile> backgroundFrames = new ArrayList<>();
	private final List<DecoSlot> inventorySlots = new ArrayList<>();
	private final DecoGui<T, C> gui;
	int vbo = -1;
	private List<TitleBarData> titleBars = new ArrayList<>();

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
		return withBox(DecoTextures.GUI_BG_STEEL, x, y, width, height);
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
		return withBox(style, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND, x, y, width, height);
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
		return withBox(style, mask, x, y, width, height, IIColor.WHITE);
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
	 * @param color  color to apply to the box
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withBox(ResLoc style, ResLoc mask, int x, int y, int width, int height, IIColor color)
	{
		backgroundTiles.get(backgroundTiles.size()-1).add(new DecoBackgroundTile(x, y, width, height, color, style, mask));
		return this;
	}

	/**
	 * Sets the frame of the most recently added background tile
	 *
	 * @param imageLocation location of the frame texture
	 * @param thickness     thickness of the frame in pixels
	 * @param cornersOnly   if true, only the corners of the framewill be drawn
	 * @param frame         sides of the frame to draw, in order: top, bottom, left, right
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withFrame(ResLoc imageLocation, int thickness, boolean cornersOnly, boolean[] frame)
	{
		List<DecoBackgroundTile> tiles = backgroundTiles.get(backgroundTiles.size()-1);
		DecoBackgroundTile lastTile = tiles.get(tiles.size()-1);
		lastTile.frame = new DecoFrame(imageLocation, false, thickness)
				.withSides(frame[0], frame[1], frame[2], frame[3]);
		return this;
	}

	/**
	 * Sets the frame of the most recently added background tile
	 *
	 * @param imageLocation location of the frame texture
	 * @param thickness     thickness of the frame in pixels
	 * @param cornersOnly   if true, only the corners of the frame will be drawn
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withFrame(ResLoc imageLocation, int thickness, boolean cornersOnly)
	{
		return withFrame(imageLocation, thickness, cornersOnly, new boolean[]{true, true, true, true});
	}

	/**
	 * Adds a standalone frame to the background, which is not attached to any background tile.
	 * This is useful for decorative frames that are not meant to be part of the background layers.
	 *
	 * @param x             x position of the frame
	 * @param y             y position ofthe frame
	 * @param width         width of the frame in pixels
	 * @param height        height of the frame in pixels
	 * @param imageLocation location of the frame texture
	 * @param thickness     thickness of the frame in pixels
	 * @param cornersOnly   if true, only the corners of the frame will be drawn
	 * @param frame         sides of the frame to draw, in order: top, bottom, left, right
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withStandaloneFrame(int x, int y, int width, int height, ResLoc imageLocation, int thickness, boolean cornersOnly, boolean[] frame)
	{
		DecoBackgroundTile tile = new DecoBackgroundTile(x, y, width, height, IIColor.WHITE, imageLocation, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND);
		tile.frame = new DecoFrame(imageLocation, cornersOnly, thickness)
				.withSides(frame[0], frame[1], frame[2], frame[3]);

		backgroundFrames.add(tile);
		return this;
	}

	/**
	 * Adds a standalone frame to the background, which is not attached to any background tile.
	 * This is useful for decorative frames that are not meant to be part of the background layers.
	 *
	 * @param x             x position of the frame
	 * @param y             y position ofthe frame
	 * @param width         width of the frame in pixels
	 * @param height        height of the frame in pixels
	 * @param imageLocation location of the frame texture
	 * @param thickness     thickness of the frame in pixels
	 * @param cornersOnly   if true, only the corners of the frame will be drawn
	 * @return this
	 */
	public DecoBackgroundBuilder<T, C> withStandaloneFrame(int x, int y, int width, int height, ResLoc imageLocation, int thickness, boolean cornersOnly)
	{
		return withStandaloneFrame(x, y, width, height, imageLocation, thickness, cornersOnly, new boolean[]{true, true, true, true});
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
			if(slot!=null)
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
		if(displayName==null&&tile.hasWorld())
		{
			World world = tile.getWorld();
			BlockPos pos = tile.getPos();
			if(world.isBlockLoaded(pos))
			{
				IBlockState state = world.getBlockState(tile.getPos());
				displayName = new TextComponentString(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName());
			}

		}
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
		List<DecoBackgroundTile> currentLayer = backgroundTiles.get(backgroundTiles.size()-1);
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

		for(List<DecoBackgroundTile> layer : backgroundTiles)
			for(DecoBackgroundTile tile : layer)
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
		GlStateManager.enableBlend();
		if(vbo==-1)
		{
			vbo = GlStateManager.glGenLists(1);
			GlStateManager.glNewList(vbo, GL11.GL_COMPILE);

			//Draw background layers
			for(List<DecoBackgroundTile> layer : backgroundTiles)
			{
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

				List<DecoBackgroundTile> tiles = layer.stream()
						.filter(decoBackgroundTile -> decoBackgroundTile.style!=null)
						.collect(Collectors.toList());

				int minXOffset = 0, minYOffset = 0;
				if(!tiles.isEmpty())
				{
					minXOffset = tiles.get(0).x%8;
					minYOffset = tiles.get(0).y%8;
				}

				//Mask
				GL11.glEnable(GL11.GL_STENCIL_TEST);
				GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
				GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
				DecoGuiUtils.drawBackgroundMask(tiles, minXOffset, minYOffset).finish();

				//Background
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
				GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
				DecoGuiUtils.drawBackgroundBlock(tiles, minXOffset, minYOffset).finish();
				GL11.glDisable(GL11.GL_STENCIL_TEST);

				//Overlay
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
				DecoGuiUtils.drawBackgroundMask(tiles, minXOffset, minYOffset).finish();
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

			//Non-blending parts
			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			draw = IIDrawUtils.startTexturedColored();

			//Background images
			for(List<DecoBackgroundTile> layer : backgroundTiles)
				for(DecoBackgroundTile tile : layer)
					if(tile.frame!=null)
						handleFrameDrawing(draw, tile);

			//Standalone Frames
			for(DecoBackgroundTile backgroundFrame : backgroundFrames)
				handleFrameDrawing(draw, backgroundFrame);

			//Non-blending inventory slots
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
					int vOffset = (int)(Math.floor(slot.style.markerOffset/4f)*3);

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

	private void handleFrameDrawing(IIDrawUtils draw, DecoBackgroundTile backgroundFrame)
	{
		DecoFrame frame = backgroundFrame.frame;
		assert frame!=null;
		if(frame.cornersOnly)
			DecoGuiUtils.drawFrameCorners(draw, backgroundFrame.x, backgroundFrame.y, backgroundFrame.width, backgroundFrame.height, frame.style, frame.sides);
		else
			DecoGuiUtils.drawFrame(draw, backgroundFrame.x, backgroundFrame.y, backgroundFrame.width, backgroundFrame.height, frame.style, frame.sides, frame.frameThickness);
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

		//Fallback to steel label background if there is no custom texture
		ResLoc backgroundLocation = data.barRectangle.style.replace("background/", "label/label_");
		TextureAtlasSprite labelBackground = ClientUtils.getSprite(backgroundLocation);
		if(labelBackground==ClientUtils.mc().getTextureMapBlocks().getMissingSprite())
			backgroundLocation = DecoTextures.GUI_LABEL_STEEL;

		DecoLabel decoLabel = new DecoTitleLabel(IIClientUtils.fontRegular, titleBarX, titleBarY)
				.withBackgroundLocation(backgroundLocation)
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

	public DecoBackgroundTile[] getTakenSpace()
	{
		return backgroundTiles.stream().flatMap(List::stream).toArray(DecoBackgroundTile[]::new);
	}

	public DecoBackgroundBuilder<T, C> conditionally(boolean condition, Consumer<DecoBackgroundBuilder<T, C>> action)
	{
		if(condition)
			action.accept(this);
		return this;
	}

	public enum SlotStyle
	{
		//Vanilla MC bevel mask inventory slot
		VANILLA(DecoTextures.RES_TEXTURES_DECO_INVENTORY_SLOT, true, 1),
		//Immersive Engineering style inventory slot
		IE(DecoTextures.RES_TEXTURES_DECO_IE_SLOT, DecoTextures.RES_TEXTURES_DECO_IE_SLOT_MARKER, 2),
		IE_INPUT(IE, 2),
		IE_OUTPUT(IE, 3),
		IE_CUSTOM1(IE, 4),
		IE_CUSTOM2(IE, 5),
		IE_CUSTOM3(IE, 6),
		IE_CUSTOM4(IE, 7),
		//Brass Frame IE style Inventory slot
		IE_BRASS(DecoTextures.RES_TEXTURES_DECO_IE_BRASS_SLOT, DecoTextures.RES_TEXTURES_DECO_IE_BRASS_SLOT_MARKER, 2),
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

	private static class TitleBarData
	{
		final DecoBackgroundTile barRectangle;
		final DecoAlignment barAlignment;
		final String barTitle;

		public TitleBarData(DecoBackgroundTile barRectangle, DecoAlignment barAlignment, String barTitle)
		{
			this.barRectangle = barRectangle;
			this.barAlignment = barAlignment;
			this.barTitle = barTitle;
		}
	}

}
