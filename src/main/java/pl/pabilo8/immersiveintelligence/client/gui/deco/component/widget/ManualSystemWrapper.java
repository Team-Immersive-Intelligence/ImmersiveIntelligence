package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.gui.GuiButtonManualNavigation;
import blusunrize.lib.manual.gui.GuiClickableList;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.06.2025
 */
public class ManualSystemWrapper extends GuiManual
{
	private final DecoManualWidget parent;
	//not to be confused with GuiManual's manual variable
	private ManualInstance manualInstance;

	public ManualSystemWrapper(DecoManualWidget parent, ManualInstance instance, String texture)
	{
		super(instance, texture);
		this.parent = parent;

		int x = parent.x-20;
		int y = parent.y;
		ReflectionHelper.setPrivateValue(GuiManual.class, this, x, "guiLeft");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, y, "guiTop");
		ReflectionHelper.setPrivateValue(GuiManual.class, this,
				ReflectionHelper.getPrivateValue(GuiManual.class, parent.ieManualGUI, "previousSelectedEntry"), "previousSelectedEntry");
		ReflectionHelper.setPrivateValue(GuiManual.class, this,
				ReflectionHelper.getPrivateValue(GuiManual.class, parent.ieManualGUI, "selectedEntry"), "selectedEntry");
		ReflectionHelper.setPrivateValue(GuiManual.class, this,
				ReflectionHelper.getPrivateValue(GuiManual.class, parent.ieManualGUI, "selectedCategory"), "selectedCategory");
		this.mc = Minecraft.getMinecraft();
		this.fontRenderer = mc.fontRenderer;
		this.page = parent.ieManualGUI.page;
		activeManual = this;
	}

	@Override
	public void initGui()
	{
		int x = parent.x-20;
		int y = parent.y;

		// Init
		manualInstance = ReflectionHelper.getPrivateValue(GuiManual.class, this, "manual");
		manualInstance.openManual();
		activeManual = this;

		ReflectionHelper.setPrivateValue(GuiManual.class, this, manualInstance, "manual");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, x, "guiLeft");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, y, "guiTop");

		ScaledResolution res = new ScaledResolution(this.mc);
		this.width = res.getScaledWidth();
		this.height = res.getScaledHeight();

		this.buttonList.clear();
		List<GuiButton> pageButtons = ReflectionHelper.getPrivateValue(GuiManual.class, this, "pageButtons");
		pageButtons.clear();

		ReflectionHelper.setPrivateValue(GuiManual.class, this, new String[0], "headers");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, new String[0], "suggestionHeaders");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, -1, "hasSuggestions");

		ManualEntry entry = manualInstance.getEntry(getSelectedEntry());
		if(entry!=null)
		{
			IManualPage mPage = (page < 0||page >= entry.getPages().length)?null: entry.getPages()[page];
			if(mPage!=null)
			{
				mPage.initPage(this, x+32, y+28, pageButtons);
				buttonList.addAll(pageButtons);
			}
		}
		else if(manualInstance.getSortedCategoryList()==null||manualInstance.getSortedCategoryList().length <= 1)
		{
			ArrayList<String> lHeaders = new ArrayList<>();
			for(ManualEntry e : manualInstance.manualContents.values())
				if(manualInstance.showEntryInList(e))
					lHeaders.add(e.getName());
			initializeHeaders(lHeaders.toArray(new String[0]), x, y, false);
			setupSearchField(x, y);
		}
		else if(manualInstance.manualContents.containsKey(selectedCategory))
		{
			ArrayList<String> lHeaders = new ArrayList<>();
			for(ManualEntry e : manualInstance.manualContents.get(selectedCategory))
				if(manualInstance.showEntryInList(e))
					lHeaders.add(e.getName());
			initializeHeaders(lHeaders.toArray(new String[0]), x, y, false);
			setupSearchField(x, y);
		}
		else
		{
			ArrayList<String> lHeaders = new ArrayList<>();
			for(String cat : manualInstance.getSortedCategoryList())
				if(manualInstance.showCategoryInList(cat))
					lHeaders.add(cat);
			initializeHeaders(lHeaders.toArray(new String[0]), x, y, true);
			setupSearchField(x, y);
		}

		if(manualInstance.manualContents.containsKey(selectedCategory)||entry!=null)
			this.buttonList.add(new GuiButtonManualNavigation(this, 1, x+24, y+10, 10, 10, 0));

		this.setFocused(true);
	}

	private void initializeHeaders(String[] headers, int x, int y, boolean isCategory)
	{
		ReflectionHelper.setPrivateValue(GuiManual.class, this, headers, "headers");
		this.buttonList.add(new GuiClickableList(this, 0, x+40, y+20, 100, 168, 1f, isCategory?0: 1, headers));
	}

	private void setupSearchField(int x, int y)
	{
		GuiTextField searchField = new GuiTextField(99, this.fontRenderer, x+166, y+78, 120, 12);
		searchField.setTextColor(-1);
		searchField.setDisabledTextColour(-1);
		searchField.setEnableBackgroundDrawing(false);
		searchField.setMaxStringLength(17);
		searchField.setFocused(true);
		searchField.setCanLoseFocus(false);

		ReflectionHelper.setPrivateValue(GuiManual.class, this, searchField, "searchField");
	}

	@Override
	public void drawScreen(int mx, int my, float f)
	{
		ReflectionHelper.setPrivateValue(GuiManual.class, this, -1, "hasSuggestions");

		GlStateManager.pushMatrix();

		scissor(parent.x, parent.y, width, height);
		super.drawScreen(mx, my, f);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void mouseClicked(int mx, int my, int button)
	{
		try
		{
			super.mouseClicked(mx, my, button);
		} catch(IOException e)
		{
			IILogger.warn(e.getMessage());
		}
	}

	@Override
	public void mouseReleased(int mx, int my, int action)
	{
		super.mouseReleased(mx, my, action);
	}

	@Override
	public void mouseClickMove(int mx, int my, int button, long time)
	{
		super.mouseClickMove(mx, my, button, time);
	}

	@Override
	public void keyTyped(char c, int i)
	{
		try
		{
			super.keyTyped(c, i);
		} catch(IOException e)
		{
			IILogger.warn(e.getMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void drawHoveringText(List text, int x, int y, FontRenderer font)
	{
		parent.setTooltip(((List<String>)text));
	}

	@Override
	public List<String> getItemToolTip(ItemStack stack)
	{
		List<String> tooltip = super.getItemToolTip(stack);
		parent.setTooltip(tooltip);
		return Collections.emptyList();
	}

	private void scissor(int x, int y, int xSize, int ySize)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		ScaledResolution res = new ScaledResolution(ClientUtils.mc());
		x = x*res.getScaleFactor();
		ySize = ySize*res.getScaleFactor();
		y = ClientUtils.mc().displayHeight-(y*res.getScaleFactor())-ySize;
		xSize = xSize*res.getScaleFactor();
		GL11.glScissor(x, y, xSize, ySize);
	}

	public void onScroll(int mouseScroll)
	{
		ManualEntry entry = manualInstance.getEntry(getSelectedEntry());
		if(mouseScroll!=0&&entry!=null)
			if(mouseScroll > 0&&page > 0)
			{
				page--;
				this.initGui();
			}
			else if(mouseScroll < 0&&page < entry.getPages().length-1)
			{
				page++;
				this.initGui();
			}
	}
}
