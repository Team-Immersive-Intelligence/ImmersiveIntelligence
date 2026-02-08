package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Scrollable list/grid of inventory slots backed by real {@link Slot}s.
 * Real slots are hidden off-screen; interaction is forwarded to the container, while rendering is handled here.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 31.01.2026
 */
public class DecoScrollableItemSlots extends DecoComponent<DecoScrollableItemSlots>
{
	private static final int SLOT_SIZE = 18;
	private static final int SLOT_INNER = 16;
	private static final int SCROLLBAR_W = 12;

	private final List<Slot> slots = new ArrayList<>();
	private final Map<Slot, int[]> originalPos = new IdentityHashMap<>();

	private ResourceLocation scrollbarSprite = DecoTextures.RES_TEXTURES_DECO_COMPONENT_SLIDER;

	private int columns = 1;
	private int scrollRows = 0;

	public DecoScrollableItemSlots(int x, int y)
	{
		super(x, y);

		withOnScroll((gui, scroll, mouseX, mouseY) -> {
			if(!hovered)
				return false;
			if(getMaxScrollRows() <= 0)
				return false;
			int delta = Integer.compare(0, scroll);
			if(delta!=0)
				setScrollRows(scrollRows+delta);
			return true;
		});
		withOnPressed(this::handleMouse);
	}

	//--- Setters ---//

	/**
	 * Provide the backing slots (real container slots).
	 * Call before the GUI is shown (or force re-init afterwards).
	 */
	public DecoScrollableItemSlots withSlots(@Nonnull List<Slot> slots)
	{
		this.slots.clear();
		this.slots.addAll(slots);
		this.initialized = false;
		return this;
	}

	/**
	 * Convenience overload.
	 */
	public DecoScrollableItemSlots withSlots(@Nonnull Slot... slots)
	{
		this.slots.clear();
		for(Slot s : slots)
			this.slots.add(s);
		this.initialized = false;
		return this;
	}

	/**
	 * How many columns to draw (grid).
	 */
	public DecoScrollableItemSlots withColumns(int columns)
	{
		this.columns = Math.max(1, columns);
		this.withWidth(columns*18+SCROLLBAR_W);
		this.initialized = false;
		return this;
	}

	public DecoScrollableItemSlots withScrollRows(int rows)
	{
		setScrollRows(rows);
		return this;
	}

	public DecoScrollableItemSlots withScrollbarTrackTexture(ResourceLocation scrollbarTrack)
	{
		this.scrollbarSprite = scrollbarTrack;
		return this;
	}

	//--- Getters ---//

	public int getScrollRows()
	{
		return scrollRows;
	}

	//--- Initialization ---//

	@Override
	protected boolean initialize()
	{
		//Hide real slots off-screen (but keep them functional)
		originalPos.clear();
		for(Slot s : slots)
		{
			if(s==null)
				continue;
			originalPos.put(s, new int[]{s.xPos, s.yPos});
			s.xPos = -10000;
			s.yPos = -10000;
		}

		//Clamp scroll in case size/columns changed
		setScrollRows(scrollRows);
		return true;
	}

	private void setScrollRows(int rows)
	{
		this.scrollRows = MathHelper.clamp(rows, 0, getMaxScrollRows());
	}

	//--- Drawing ---//

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		//Scrollbar
		drawScrollbar();
		//Background for list area
		drawSlots();
	}

	private void drawScrollbar()
	{
		final int sbX = x+width-SCROLLBAR_W;

		//Track (textured background)
		bindAtlas();

		TextureAtlasSprite sprite = ClientUtils.getSprite(scrollbarSprite);
		IIDrawUtils draw = IIDrawUtils.startTexturedColored()
				//Track background (tiled/connected style)
				.drawConnectedTexColorRect(
						sbX, y, SCROLLBAR_W-2, height,
						IIColor.WHITE, 10, 32, 2, 8,
						sprite.getInterpolatedU(0), sprite.getInterpolatedU(5),
						sprite.getMinV(), sprite.getMaxV()
				);

		final int max = getMaxScrollRows();
		final int trackH = height-2;

		final int thumbH;
		final int thumbY;
		if(max <= 0)
		{
			thumbH = height-2;
			thumbY = y+1;
		}
		else
		{
			thumbH = Math.max(10, (int)((getVisibleRows()/(float)getTotalRows())*trackH));
			thumbY = y+1+(int)((scrollRows/(float)max)*(trackH-thumbH));
		}

		//Handle (sprite stretched to thumb size)
		draw.drawConnectedTexColorRect(
						sbX, thumbY, SCROLLBAR_W-2, thumbH,
						IIColor.WHITE, 10, 32, 2, 8,
						sprite.getInterpolatedU(5), sprite.getInterpolatedU(10),
						sprite.getMinV(), sprite.getMaxV()
				)
				.finish();
	}

	private void drawSlots()
	{
		final int cols = Math.max(1, columns);
		final int visible = getVisibleRows()*cols;
		final int listRight = x+getListWidth();

		IIDrawUtils draw = IIDrawUtils.startColored();
		for(int cell = 0; cell < visible; cell++)
		{
			final int sx = x+(cell%cols)*SLOT_SIZE+1;
			final int sy = y+(cell/cols)*SLOT_SIZE+1;

			if(sx+SLOT_INNER > listRight)
				continue;
			Slot slot = getSlotByVisibleIndex(cell);
			if(slot==null)
				continue;
			slot.xPos = sx-width-7;
			slot.yPos = sy-height/2+13;
			draw.drawColorRect(sx-1, sy-1, SLOT_SIZE, SLOT_SIZE,
					IIColor.fromPackedARGB(0x33000000));
		}
		draw.finish();
	}

	@Override
	public void cleanup()
	{
		//Restore real slot positions
		for(Map.Entry<Slot, int[]> e : originalPos.entrySet())
		{
			Slot s = e.getKey();
			int[] pos = e.getValue();
			if(s!=null&&pos!=null&&pos.length==2)
			{
				s.xPos = pos[0];
				s.yPos = pos[1];
			}
		}
		originalPos.clear();
	}

	@Override
	protected void updateInvisibleComponent()
	{
		if(initialized)
		{
			initialized = false;
			cleanup();
		}
	}

	//--- Utils ---//

	private int getVisibleRows()
	{
		return Math.max(1, height/SLOT_SIZE);
	}

	private int getVisibleSlotCount()
	{
		return getVisibleRows()*Math.max(1, columns);
	}

	private int getTotalRows()
	{
		if(slots.isEmpty())
			return 0;
		return (int)Math.ceil(slots.size()/(double)Math.max(1, columns));
	}

	private int getMaxScrollRows()
	{
		return Math.max(0, getTotalRows()-getVisibleRows());
	}

	private int getSlotIndexFromVisibleCell(int cellIndex)
	{
		int topIndex = scrollRows*Math.max(1, columns);
		return topIndex+cellIndex;
	}

	@Nullable
	private Slot getSlotByVisibleIndex(int visibleCellIndex)
	{
		int idx = getSlotIndexFromVisibleCell(visibleCellIndex);
		if(idx < 0||idx >= slots.size())
			return null;
		return slots.get(idx);
	}

	private int getListWidth()
	{
		return width-SCROLLBAR_W;
	}

	private boolean isOverScrollbar(int mouseX, int mouseY)
	{
		return mouseX >= x+width-SCROLLBAR_W&&mouseX < x+width
				&&mouseY >= y&&mouseY < y+height;
	}

	private void setScrollFromMouseY(int mouseY)
	{
		int max = getMaxScrollRows();
		if(max <= 0)
		{
			scrollRows = 0;
			return;
		}

		int trackTop = y+1;
		int trackH = height-2;

		float t = (mouseY-trackTop)/(float)Math.max(1, trackH);
		t = MathHelper.clamp(t, 0f, 1f);

		setScrollRows(Math.round(t*max));
	}

	private boolean handleMouse(DecoScrollableItemSlots gui, MouseButton button, int mouseX, int mouseY)
	{
		if(parentGui==null)
			return false;
		if(!hovered)
			return false;
		//Scrollbar interaction
		if(isOverScrollbar(mouseX, mouseY))
		{
			if(getMaxScrollRows() <= 0)
				return true;
			setScrollFromMouseY(mouseY);
			return true;
		}
		return false;
	}
}
