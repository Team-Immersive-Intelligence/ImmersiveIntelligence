package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 02.11.2022
 */
public class IIManualDataVariable extends IIManualObject
{
	@Nonnull
	DataType type = new DataTypeNull();
	/**
	 * Name and basic description of this variable
	 */
	String letter, name, description, value;
	/**
	 * Used for variables that have a list of permitted values.<br>
	 * Formatted as Name - Description
	 */
	HashMap<String, String> values;
	/**
	 * What variables are required for this one to be applicable
	 */
	HashMap<Character, String> requirements;
	/**
	 * Whether it's an input or output variable<br>
	 * takes value in/out in nbt
	 */
	boolean inputVariable;

	//--- Setup ---//

	public IIManualDataVariable(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);
		this.type = IIDataTypeUtils.getVarInstance(dataSource.getString("type"));

		dataSource.checkSetString("letter", s -> letter = s, "");
		dataSource.checkSetString("name", s -> name = s, "");
		dataSource.checkSetString("description", s -> description = s, "");
		dataSource.checkSetString("direction", b -> inputVariable = b.equals("in"), "out");

		value = null;
		values = null;
		if(dataSource.hasKey("values"))
		{
			values = new HashMap<>();
			values.putAll(
					dataSource.streamList(NBTTagList.class, "values")
							.collect(Collectors.toMap(
									nbt -> ((NBTTagString)nbt.get(0)).getString(),
									nbt -> ((NBTTagString)nbt.get(1)).getString())
							)
			);
		}
		else
			dataSource.checkSetString("value", v -> value = v);

		if(dataSource.hasKey("requirements"))
		{
			requirements = new HashMap<>();
			EasyNBT nbt = dataSource.getEasyCompound("requirements");
			for(char c : DataPacket.VARIABLE_NAMES)
				nbt.checkSetString(String.valueOf(c),
						tag -> requirements.put(c, tag)
				);
		}
		else
			requirements = null;

		boolean unicodeFlag = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);
		this.height += Math.max(0,
				manual.fontRenderer.getWordWrappedHeight(description, width)-4
		);
		manual.fontRenderer.setUnicodeFlag(unicodeFlag);

	}

	@Override
	protected int getDefaultHeight()
	{
		return 16;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		ClientUtils.bindAtlas();
		GlStateManager.color(1f, 1f, 1f, 1f);
		TextureAtlasSprite typeTexture = ClientUtils.getSprite(type.getTextureLocation());
		TextureAtlasSprite contextTexture = ClientUtils.getSprite(inputVariable?IIReference.RES_CONTEXT_DATA_IN: IIReference.RES_CONTEXT_DATA_OUT);
		IIDrawUtils.startTextured()
				.drawTexRect(x, y, 16, 16,
						typeTexture.getMinU(), typeTexture.getMaxU(), typeTexture.getMinV(), typeTexture.getMaxV())
				.drawTexRect(x-3, y, 16, 16,
						contextTexture.getMinU(), contextTexture.getMaxU(), contextTexture.getMinV(), contextTexture.getMaxV())
				.finish();

		boolean unicodeFlag = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);
		if(letter!=null)
			manual.fontRenderer.drawString(TextFormatting.BOLD+letter, x+18, y+4, type.getTypeColor().getPackedRGB());
		manual.fontRenderer.drawString(TextFormatting.BOLD+name, x+(letter==null?18: 24), y-4, manual.getTextColour());
		manual.fontRenderer.drawSplitString(description, x+(letter==null?18: 24), y+4, (letter==null?110: 104), manual.getTextColour());
		manual.fontRenderer.setUnicodeFlag(unicodeFlag);


	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		boolean b = super.mousePressed(mc, mouseX, mouseY);
		if(b&&letter!=null&&Minecraft.getMinecraft().currentScreen instanceof IDataMachineGui)
			((IDataMachineGui)Minecraft.getMinecraft().currentScreen).editVariable(letter.charAt(0),
					new DataPacket().getVarInType(type.getClass(), new DataTypeNull())
			);
		return b;
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
					name
			));

			lines.addAll(manual.fontRenderer.listFormattedStringToWidth(TextFormatting.GRAY+IIStringUtil.getItalicString(description), 140));

			if(values!=null)
			{
				lines.add("");
				lines.add(I18n.format("ie.manual.entry.data_variable.allowed_values"));
				values.forEach((key, value) -> lines.addAll(manual.fontRenderer.listFormattedStringToWidth(
						TextFormatting.BOLD+" > "+key+TextFormatting.RESET+TextFormatting.GRAY+" - "+value,
						160)));
			}
			else if(value!=null)
				lines.add(I18n.format("ie.manual.entry.data_variable.allowed_value")+" "+TextFormatting.GRAY+value);

			if(requirements!=null)
			{
				lines.add("");
				lines.add(I18n.format("ie.manual.entry.data_variable.required_variables"));
				requirements.forEach((key, value) -> lines.addAll(manual.fontRenderer.listFormattedStringToWidth(
						TextFormatting.BOLD+" > "+key+TextFormatting.RESET+" = ["+TextFormatting.GRAY+value+TextFormatting.RESET+"]",
						160)));
			}

			return lines;
		}
		return null;
	}
}
