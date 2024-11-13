package pl.pabilo8.immersiveintelligence.client.manual.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 02.11.2022
 */
public class IIManualDataType extends IIManualObject
{
	@Nonnull
	DataType type = new DataTypeNull();

	//--- Setup ---//

	public IIManualDataType(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);
		this.type = IIDataTypeUtils.getVarInstance(dataSource.getString("type"));
	}

	@Override
	protected int getDefaultHeight()
	{
		return 16;
	}

	@Override
	protected int getDefaultWidth()
	{
		return 16;
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
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, 16, 16);
		GlStateManager.popMatrix();

	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(hovered)
			return Collections.singletonList(I18n.format(IIReference.DATA_KEY+"datatype."+type.getName()));
		return null;
	}
}
