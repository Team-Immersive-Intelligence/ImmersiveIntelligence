package pl.pabilo8.immersiveintelligence.client.gui.elements;

import blusunrize.immersiveengineering.api.ManualHelper;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for wrapping IE's Manual to a GuiButton
 * Also, would be nice if Blu learned about 'protected' modifier, helps addons a lot
 *
 * @author Pabilo8
 * @since 04.09.2021
 */
// TODO: 06.09.2021 scrolling
public class GuiWidgetManualWrapper extends GuiManual
{
	//See, isn't that hard to do
	public int x;
	public int y;
	public int manualTime;
	private boolean opened = false;
	//not to be confused with GuiManual's manual variable
	private ManualInstance manualInstance;

	public GuiWidgetManualWrapper(GuiManual manual, int x, int y, boolean opened)
	{
		super(ManualHelper.getManual(), ManualHelper.getManual().texture);
		this.x = x;
		this.y = y;
		ReflectionHelper.setPrivateValue(GuiManual.class, this, x, "guiLeft");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, y, "guiTop");
		ReflectionHelper.setPrivateValue(GuiManual.class, this,
				ReflectionHelper.getPrivateValue(GuiManual.class, manual, "previousSelectedEntry"), "previousSelectedEntry");
		ReflectionHelper.setPrivateValue(GuiManual.class, this,
				ReflectionHelper.getPrivateValue(GuiManual.class, manual, "selectedEntry"), "selectedEntry");
		ReflectionHelper.setPrivateValue(GuiManual.class, this,
				ReflectionHelper.getPrivateValue(GuiManual.class, manual, "selectedCategory"), "selectedCategory");
		this.mc = Minecraft.getMinecraft();
		this.fontRenderer = mc.fontRenderer;
		this.page = manual.page;
		activeManual = this;
		this.initGui();
		manualTime = opened?100: 0;
		this.setFocused(true);
	}

	public void setOpened(boolean opened)
	{
		this.opened = opened;
	}

	/**
	 * Pls forgive me, I had no other way to do it
	 */
	@Override
	public void initGui()
	{
		manualInstance = ReflectionHelper.getPrivateValue(GuiManual.class, this, "manual");
		manualInstance.openManual();
		activeManual = this;

		ReflectionHelper.setPrivateValue(GuiManual.class, this, manualInstance, "manual");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, x, "guiLeft");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, y, "guiTop");
		boolean textField = false;

		ScaledResolution res = new ScaledResolution(this.mc);
		this.width = res.getScaledWidth();
		this.height = res.getScaledHeight();

		this.buttonList.clear();
		List<GuiButton> pageButtons = ReflectionHelper.getPrivateValue(GuiManual.class, this, "pageButtons");
		pageButtons.clear();

		ReflectionHelper.setPrivateValue(GuiManual.class, this, new String[0], "headers");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, new String[0], "suggestionHeaders");
		ReflectionHelper.setPrivateValue(GuiManual.class, this, -1, "hasSuggestions");
		if(manualInstance.getEntry(getSelectedEntry())!=null)
		{
			ManualEntry entry = manualInstance.getEntry(getSelectedEntry());
			IManualPage mPage = (page < 0||page >= entry.getPages().length)?null: entry.getPages()[page];
			if(mPage!=null)
				mPage.initPage(this, x+32, y+28, pageButtons);
			buttonList.addAll(pageButtons);
		}
		else if(manualInstance.getSortedCategoryList()==null||manualInstance.getSortedCategoryList().length <= 1)
		{
			ArrayList<String> lHeaders = new ArrayList<>();
			for(ManualEntry e : manualInstance.manualContents.values())
				if(manualInstance.showEntryInList(e))
					lHeaders.add(e.getName());
			ReflectionHelper.setPrivateValue(GuiManual.class, this, lHeaders.toArray(new String[0]), "headers");
			this.buttonList.add(new GuiClickableList(this, 0, x+40, y+20, 100, 168, 1f, 1,
					ReflectionHelper.getPrivateValue(GuiManual.class, this, "headers")
			));
			textField = true;
		}
		else if(manualInstance.manualContents.containsKey(selectedCategory))
		{
			ArrayList<String> lHeaders = new ArrayList<>();
			for(ManualEntry e : manualInstance.manualContents.get(selectedCategory))
				if(manualInstance.showEntryInList(e))
					lHeaders.add(e.getName());
			ReflectionHelper.setPrivateValue(GuiManual.class, this, lHeaders.toArray(new String[0]), "headers");
			this.buttonList.add(new GuiClickableList(this, 0, x+40, y+20, 100, 168, 1f, 1,
					ReflectionHelper.getPrivateValue(GuiManual.class, this, "headers")
			));
			textField = true;
		}
		else
		{
			ArrayList<String> lHeaders = new ArrayList<>();
			for(String cat : manualInstance.getSortedCategoryList())
				if(manualInstance.showCategoryInList(cat))
					lHeaders.add(cat);
			ReflectionHelper.setPrivateValue(GuiManual.class, this, lHeaders.toArray(new String[0]), "headers");
			this.buttonList.add(new GuiClickableList(this, 0, x+40, y+20, 100, 168, 1f, 0,
					ReflectionHelper.getPrivateValue(GuiManual.class, this, "headers")
			));
			textField = true;
		}
		if(manualInstance.manualContents.containsKey(selectedCategory)||manualInstance.getEntry(getSelectedEntry())!=null)
			this.buttonList.add(new GuiButtonManualNavigation(this, 1, x+24, y+10, 10, 10, 0));

		if(textField)
		{
			Keyboard.enableRepeatEvents(true);

			GuiTextField searchField = new GuiTextField(99, this.fontRenderer, x+166, y+78, 120, 12);
			searchField.setTextColor(-1);
			searchField.setDisabledTextColour(-1);
			searchField.setEnableBackgroundDrawing(false);
			searchField.setMaxStringLength(17);
			searchField.setFocused(true);
			searchField.setCanLoseFocus(false);

			ReflectionHelper.setPrivateValue(GuiManual.class, this, searchField, "searchField");

		}
		else if(ReflectionHelper.getPrivateValue(GuiManual.class, this, "searchField")!=null)
			ReflectionHelper.setPrivateValue(GuiManual.class, this, null, "searchField");
	}

	@Override
	public void drawScreen(int mx, int my, float f)
	{
		manualTime = MathHelper.clamp(manualTime+(opened?6: -6), 0, 100);
		ReflectionHelper.setPrivateValue(GuiManual.class, this, -1, "hasSuggestions");

		GlStateManager.pushMatrix();
		scissor(x, y, 186, 198);
		GlStateManager.translate(-(146*(1f-(manualTime/100f))), 0, 0);
		super.drawScreen(mx, my, f);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

	@Override
	public void drawHoveringText(List text, int x, int y, FontRenderer font)
	{
		/*
		manualInstance.tooltipRenderPre();
		super.drawHoveringText(text, x, y, font);
		manualInstance.tooltipRenderPost();
		 */
	}
}
