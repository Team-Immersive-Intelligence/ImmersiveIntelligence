package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.client.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiWidgetManualWrapper;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class IIManualPageDataType extends ManualPages.Table
{
	IDataType type;

	public IIManualPageDataType(ManualInstance manual, IDataType type)
	{
		super(manual, "data_variable_types_"+type.getName(), type.getTypeInfoTable(), true);
		this.type = type;
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		super.initPage(gui, x, y+18, pageButtons);
		pageButtons.add(new GuiButtonDatatype(pageButtons.size(), x+52, y, type));

	}

	@Override
	public void renderPage(GuiManual gui, int x, int y, int mx, int my)
	{
		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(type.textureLocation());
		Gui.drawModalRectWithCustomSizedTexture(x+52, y, 0, 0, 16, 16, 16, 16);
		super.renderPage(gui, x, y+18, mx, my);
		GlStateManager.popMatrix();
	}

	@Override
	public void buttonPressed(GuiManual gui, GuiButton button)
	{
		super.buttonPressed(gui, button);
		if(button instanceof GuiButtonDatatype&&Minecraft.getMinecraft().currentScreen instanceof IDataMachineGui)
		{
			// TODO: 20.01.2022 get current variable
			((IDataMachineGui)Minecraft.getMinecraft().currentScreen).editVariable('a', new DataPacket().getVarInType(type.getClass(), new DataTypeNull()));
		}
	}

	@Override
	public boolean listForSearch(String searchTag)
	{
		return false;
	}

	public static class GuiButtonDatatype extends GuiButtonIE
	{
		@Nonnull
		public final IDataType entry;

		public GuiButtonDatatype(int buttonId, int x, int y, IDataType entry)
		{
			super(100+buttonId, x, y, 16, 16, "",entry.textureLocation(), 0, 0);
			this.entry = entry;
			this.enabled = ManualHelper.getManual().getGui() instanceof GuiWidgetManualWrapper;
		}
	}
}
