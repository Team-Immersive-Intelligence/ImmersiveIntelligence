package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.List;
import java.util.Locale;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class IIManualPageBulletComponent extends IIManualPages
{
	protected String name;
	protected String localizedName;
	protected String localizedLore;
	protected String localizedType;
	ItemStack stack;
	EnumComponentRole type;
	float density;

	public IIManualPageBulletComponent(ManualInstance manual, IAmmoComponent component)
	{
		this(manual, component.getName(), component.getMaterial().getExampleStack(), component.getRole(), component.getDensity());
	}

	public IIManualPageBulletComponent(ManualInstance manual, String name, ItemStack stack, EnumComponentRole type, float density)
	{
		super(manual, name);
		this.name = name;
		this.text = "bullet_component."+name;
		this.stack = stack;
		this.type = type;
		this.density = density;
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		highlighted = ItemStack.EMPTY;
		if(text!=null&&!text.isEmpty())
		{
			boolean uni = manual.fontRenderer.getUnicodeFlag();
			manual.fontRenderer.setUnicodeFlag(true);
			this.localizedText = manual.formatText(text+".desc");
			this.localizedLore = manual.formatText(text+".lore");
			this.localizedName = manual.formatText(text);

			localizedType = I18n.format(IIReference.DESCRIPTION_KEY+"bullet_type."+type.getName());

			this.localizedText = addLinks(manual, gui, this.localizedText, x, y+60, 60, pageButtons);
			if(this.localizedText==null)
				this.localizedText = "";
			manual.fontRenderer.setUnicodeFlag(uni);
		}
	}

	@Override
	public void renderPage(GuiManual gui, int x, int y, int mx, int my)
	{
		if(localizedName!=null&&!localizedName.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD.toString()+TextFormatting.UNDERLINE+localizedName, x+40, y, 90, manual.getTextColour());

		if(localizedLore!=null&&!localizedLore.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.ITALIC+localizedLore, x+40, y+10, 90, manual.getTextColour());

		int shift = renderInfo(gui, x+8, y+50)+50;

		if(localizedText!=null&&!localizedText.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, localizedText, x, y+shift, 120, manual.getTextColour());

		drawOrnamentalFrame(gui, x, y+4);

		GlStateManager.enableBlend();


		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.scale(2, 2, 2);
		ManualUtils.renderItem().renderItemAndEffectIntoGUI(stack, x/2, (y+4)/2);
		GlStateManager.popMatrix();

		if(IIUtils.isPointInRectangle(x+4, y, x+36, y+32, mx, my))
			gui.renderToolTip(stack, mx, my);
		//				manual.fontRenderer.drawSplitString(localizedText, x,y, 120, manual.getTextColour());
	}

	int renderInfo(GuiManual gui, int x, int y)
	{
		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.type", localizedType), x, y, 120, manual.getTextColour());
		ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.density", density), x, y+10, 120, manual.getTextColour());

		return 20;
	}

	void drawOrnamentalFrame(GuiManual gui, int x, int y)
	{
		GlStateManager.pushMatrix();
		GlStateManager.color(0.25f, 0.25f, 0.25f, 0.5f);
		ManualUtils.bindTexture(texture);
		ManualUtils.drawTexturedRect(x-5, y-5, 15, 15, 0, 15/255f, 0, 15/255f);
		ManualUtils.drawTexturedRect(x+22, y-5, 15, 15, 15/255f, 30/255f, 0, 15/255f);
		ManualUtils.drawTexturedRect(x-5, y+20, 15, 15, 0, 15/255f, 15/255f, 30/255f);
		ManualUtils.drawTexturedRect(x+22, y+20, 15, 15, 15/255f, 30/255f, 15/255f, 30/255f);

		GlStateManager.popMatrix();
	}

	@Override
	public boolean listForSearch(String searchTag)
	{
		return stack.getDisplayName().toLowerCase(Locale.ENGLISH).contains(searchTag);
	}
}
