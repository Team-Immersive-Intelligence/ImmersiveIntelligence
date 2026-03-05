package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.06.2025
 */
public class DecoManualWidget extends DecoComponentWidgetBase<DecoManualWidget>
{
	public final GuiManual ieManualGUI;
	private final ManualSystemWrapper wrapper;
	private List<String> tooltip;

	public DecoManualWidget()
	{
		super();

		//Do not draw a background box
		withBackground(null);
		withBackgroundMask(null);

		//Initialize the default manual, if it wasn't already
		GuiManual trueManual = ManualHelper.getManual().getGui();
		if(trueManual==null)
			trueManual = new GuiManual(ManualHelper.getManual(), ManualHelper.getManual().texture);
		this.ieManualGUI = trueManual;

		//Initialize the wrapper
		wrapper = new ManualSystemWrapper(this, ManualHelper.getManual(), ManualHelper.getManual().texture);
		withSize(186-20, 198);
		withOnScroll((gui, mouseScroll, mouseX, mouseY) -> {
			wrapper.onScroll(mouseScroll);
			return true;
		});
		withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
			wrapper.mouseClicked(mouseX, mouseY, mouseButton.ordinal());
			return true;
		});
		withOnReleased((gui, mouseButton, mouseX, mouseY) -> {
			wrapper.mouseReleased(mouseX, mouseY, 0);
			return true;
		});
		withOnDragged((gui, mouseButton, mouseX, mouseY) -> {
			wrapper.mouseClickMove(mouseX, mouseY, mouseButton.ordinal(), 0);
			return true;
		});
		withOnKeyTyped((gui, charTyped, keyCode) ->
		{
			wrapper.keyTyped(charTyped, keyCode);
			return true;
		});
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		//Prepare
		GlStateManager.color(1, 1, 1, 1);
		tooltip = null;

		//Draw
		wrapper.drawScreen(mouseX, mouseY, partialTicks);

		//Cleanup
		RenderHelper.disableStandardItemLighting();
		GlStateManager.color(1, 1, 1, 1);

		super.draw(mouseX, mouseY, partialTicks);
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn)
	{

	}

	@Override
	protected boolean initialize()
	{
		if(super.initialize())
		{
			wrapper.initGui();
			return true;
		}
		return false;
	}

	@Override
	public void cleanup()
	{
		wrapper.onGuiClosed();
		GuiManual.activeManual = ieManualGUI;
		ieManualGUI.setSelectedEntry(wrapper.getSelectedEntry());
		ieManualGUI.selectedCategory = wrapper.selectedCategory;
		ieManualGUI.previousSelectedEntry = wrapper.previousSelectedEntry;
		ieManualGUI.page = wrapper.page;
	}

	@Override
	public List<String> getTooltip()
	{
		return tooltip==null?tooltip = new ArrayList<>(): tooltip;
	}

	public void setTooltip(List<String> tooltip)
	{
		this.tooltip = tooltip;
	}

	@Override
	public String getName()
	{
		return "manual";
	}

	@Override
	public int getWidgetWidth()
	{
		return width-20;
	}

	@Nonnull
	@Override
	public DecoTab provideTab()
	{
		return (DecoTab)new DecoTab()
				.withBackground(DecoTextures.COMPONENT_TAB_WIDGET)
				.withBackgroundColor(IIColor.fromPackedRGB(0x3C3C5F))
				.withPadding(6, 2, 2, 2)
				.withIconAlignment(DecoAlignment.CENTER)
				//Engineer's Manual
				.withIcon(new ItemStack(IEContent.itemTool, 1, 3))
				.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"widget.manual.show");
	}

}
