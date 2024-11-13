package pl.pabilo8.immersiveintelligence.client.manual.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 02.11.2022
 */
//TODO: 07.08.2023 clickable links
public class IIManualDataCallback extends IIManualObject
{
	private final static ResLoc TEXTURE_CALLBACK = ResLoc.of(IIReference.RES_TEXTURES_MANUAL, "data/callback").withExtension(ResLoc.EXT_PNG);

	@Nonnull
	DataType type = new DataTypeNull();
	String name, label, returns;

	//--- Setup ---//

	public IIManualDataCallback(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);
		this.type = IIDataTypeUtils.getVarInstance(dataSource.getString("type"));

		dataSource.checkSetString("name", s -> name = s, "missingno");
		dataSource.checkSetString("label", s -> label = s, name);
		dataSource.checkSetString("returns", s -> returns = s, "void");

	}

	@Override
	protected int getDefaultHeight()
	{
		return 18;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		GlStateManager.pushMatrix();
		IIClientUtils.bindTexture(type.getTextureLocation());
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);

		IIClientUtils.bindTexture(TEXTURE_CALLBACK);
		Gui.drawModalRectWithCustomSizedTexture(x-1, y-1, 0, 0, 16, 16, 16, 16);
		GlStateManager.popMatrix();

		boolean unicodeFlag = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);
		drawString(manual.fontRenderer, name, x+18, y+2, manual.getTextColour());
		manual.fontRenderer.setUnicodeFlag(unicodeFlag);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		//TODO: 07.08.2023 rework how data callback works
		/*boolean b = super.mousePressed(mc, mouseX, mouseY);
		if(b&&Minecraft.getMinecraft().currentScreen instanceof IDataMachineGui)
			((IDataMachineGui)Minecraft.getMinecraft().currentScreen).editVariable(letter.charAt(0),
					new DataPacket().getVarInType(type.getClass(), new DataTypeNull())
			);*/
		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(hovered)
		{
			ArrayList<String> lines = new ArrayList<>();
			lines.add(String.format(
					"<%s> %s",
					type.getTypeColor().getHexCol(I18n.format(IIReference.DATA_KEY+"datatype."+type.getName())),
					IIStringUtil.getItalicString(name)
			));

			lines.add(TextFormatting.GRAY+label);
			lines.add(I18n.format("ie.manual.entry.callback_returns")+" "+TextFormatting.GRAY+IIStringUtil.getItalicString(returns));

			return lines;
		}
		return null;
	}
}
