package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Displays a whole list of item stacks inside a Deco GUI as a grid.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.01.2026
 */
@ParametersAreNonnullByDefault
public class DecoItemStackListDisplay extends DecoComponent<DecoItemStackListDisplay>
{
	private Supplier<List<ItemStack>> stacksSupplier = Collections::emptyList;

	protected int[] padding = new int[]{2, 2, 2, 2};
	private ResourceLocation backgroundTexture;

	private DecoAlignment contentAlignment = DecoAlignment.CENTER;

	private int slotSize = 16;
	private int forcedSlotsPerRow = -1;

	private int cachedContentX, cachedContentY;
	private int cachedSlotsPerRow = 1;
	private int cachedRows = 1;
	private int cachedContentW = 0, cachedContentH = 0;
	private int lastMouseX, lastMouseY;

	private TooltipFlags tooltipFlag;

	public DecoItemStackListDisplay(int x, int y)
	{
		super(x, y);
		withForcedAdvancedTooltip(false);
		withOnTooltip(this::onDisplayTooltip);
	}

	public DecoItemStackListDisplay withStacks(List<ItemStack> stacks)
	{
		this.stacksSupplier = () -> stacks;
		this.initialized = false;
		return this;
	}

	public DecoItemStackListDisplay withStacks(Supplier<List<ItemStack>> stacksSupplier)
	{
		this.stacksSupplier = stacksSupplier;
		this.initialized = false;
		return this;
	}

	public DecoItemStackListDisplay withBackgroundTexture(@Nullable ResourceLocation backgroundTexture)
	{
		this.backgroundTexture = backgroundTexture;
		return this;
	}

	public DecoItemStackListDisplay withPadding(int[] padding)
	{
		this.padding = padding;
		this.initialized = false;
		return this;
	}

	public DecoItemStackListDisplay withContentAlignment(DecoAlignment alignment)
	{
		this.contentAlignment = alignment;
		this.initialized = false;
		return this;
	}

	public DecoItemStackListDisplay withSlotSize(int slotSize)
	{
		this.slotSize = MathHelper.clamp(slotSize, 8, 32);
		this.initialized = false;
		return this;
	}

	/**
	 * Set to -1 to auto-compute.
	 */
	public DecoItemStackListDisplay withForcedSlotsPerRow(int forcedSlotsPerRow)
	{
		this.forcedSlotsPerRow = forcedSlotsPerRow;
		this.initialized = false;
		return this;
	}

	public DecoItemStackListDisplay withForcedAdvancedTooltip(boolean forcedAdvancedTooltip)
	{
		this.tooltipFlag = forcedAdvancedTooltip||ClientUtils.mc().gameSettings.advancedItemTooltips?
				TooltipFlags.ADVANCED: TooltipFlags.NORMAL;
		this.initialized = false;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		int xPad = padding[0]+padding[2];
		int yPad = padding[1]+padding[3];

		int contentBoxW = Math.max(0, width-xPad);
		int contentBoxH = Math.max(0, height-yPad);

		int computed = contentBoxW/slotSize;
		if(forcedSlotsPerRow > 0)
			cachedSlotsPerRow = forcedSlotsPerRow;
		else
			cachedSlotsPerRow = Math.max(1, computed);

		// Derive rows from current stack count (0 -> 1 row, renders nothing but alignment stays stable)
		int count = Math.max(0, getStacks().size());
		cachedRows = Math.max(1, (int)Math.ceil(count/(double)cachedSlotsPerRow));

		// Actual grid size (may exceed box vertically; caller can size component accordingly)
		cachedContentW = cachedSlotsPerRow*slotSize;
		cachedContentH = cachedRows*slotSize;

		cachedContentX = contentAlignment.getAlignX(x+padding[0], width, cachedContentW);
		cachedContentY = contentAlignment.getAlignY(y+padding[1], height, cachedContentH);

		return true;
	}

	private List<ItemStack> getStacks()
	{
		List<ItemStack> s = stacksSupplier.get();
		return s==null?Collections.emptyList(): s;
	}

	private int getHoveredIndex(int mouseX, int mouseY)
	{
		if(mouseX < cachedContentX||mouseY < cachedContentY)
			return -1;

		int localX = mouseX-cachedContentX;
		int localY = mouseY-cachedContentY;

		int col = localX/slotSize;
		int row = localY/slotSize;

		if(col < 0||row < 0||col >= cachedSlotsPerRow||row >= cachedRows)
			return -1;

		int idx = row*cachedSlotsPerRow+col;
		return idx;
	}

	private ItemStack getStackAt(int index)
	{
		List<ItemStack> stacks = getStacks();
		if(index < 0||index >= stacks.size())
			return ItemStack.EMPTY;
		return stacks.get(index);
	}

	private List<String> onDisplayTooltip(DecoItemStackListDisplay gui)
	{
		int hovered = getHoveredIndex(gui.lastMouseX, gui.lastMouseY);
		ItemStack stack = getStackAt(hovered);
		if(stack.isEmpty())
			return Collections.emptyList();

		List<String> tooltip = stack.getTooltip(ClientUtils.mc().player, tooltipFlag);
		for(int i = 1; i < tooltip.size(); i++)
			tooltip.set(i, TextFormatting.GRAY+tooltip.get(i));
		return tooltip;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.pushMatrix();

		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
		if(backgroundTexture!=null)
		{
			bindAtlas();
			IIDrawUtils.startTexturedColored()
					.drawConnectedTexColorRect(x-padding[0], y-padding[1], width, height, IIColor.WHITE, backgroundTexture, 32, 32, 8, 8)
					.finish();
		}

		List<ItemStack> stacks = getStacks();
		if(!stacks.isEmpty())
		{
			GlStateManager.enableDepth();
			GlStateManager.enableRescaleNormal();
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			RenderHelper.enableGUIStandardItemLighting();

			for(int i = 0; i < stacks.size(); i++)
			{
				int col = i%cachedSlotsPerRow;
				int row = i/cachedSlotsPerRow;

				int px = cachedContentX+col*slotSize;
				int py = cachedContentY+row*slotSize;

				GlStateManager.pushMatrix();
				GlStateManager.translate(px, py, 0);

				// Render in a "slotSize x slotSize" square by scaling vanilla 16x16 item render
				float scale = 16f/(float)slotSize;
				GlStateManager.scale(1f/scale, 1f/scale, 1f);

				ItemStack stack = stacks.get(i);
				ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
				ClientUtils.mc().getRenderItem().renderItemOverlayIntoGUI(IIClientUtils.fontRegular, stack, 0, 0, null);

				GlStateManager.popMatrix();
			}

			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableDepth();
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void cleanup()
	{

	}

	@Nullable
	@Override
	public Object getProvidedIngredient()
	{
		int hovered = getHoveredIndex(this.lastMouseX, this.lastMouseY);
		ItemStack stack = getStackAt(hovered);
		return stack.isEmpty()?null: stack;
	}
}

