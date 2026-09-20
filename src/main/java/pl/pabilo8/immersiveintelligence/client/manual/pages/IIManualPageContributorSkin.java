package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkinnable;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a contributor skin and cycles through all compatible skinnable items.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.09.2026
 * @since 07.08.2021
 */
public class IIManualPageContributorSkin extends IIManualPages
{
	private static final int DISPLAY_TIME = 30;

	public final IISpecialSkin skin;
	public final List<ItemStack> renderStacks = new ArrayList<>();
	protected String localizedName;
	protected String localizedLore;
	private final int maxTimer;

	public IIManualPageContributorSkin(ManualInstance manual, IISpecialSkin skin)
	{
		super(manual, "contributor_skin_"+skin.name);
		this.skin = skin;

		for(Item item : IIContent.ITEMS)
			if(item instanceof ISkinnable skinnable&&skin.doesApply(skinnable.getSkinnableName()))
			{
				ItemStack stack = new ItemStack(item);
				skinnable.applySkinnableSkin(stack, skin.name);
				renderStacks.add(stack);
			}

		if(renderStacks.isEmpty())
			renderStacks.add(ItemStack.EMPTY);
		maxTimer = renderStacks.size()*DISPLAY_TIME;
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		highlighted = ItemStack.EMPTY;
		if(text!=null&&!text.isEmpty())
		{
			boolean uni = manual.fontRenderer.getUnicodeFlag();
			manual.fontRenderer.setUnicodeFlag(true);
			this.localizedName = I18n.format("skin.immersiveintelligence."+skin.name+".name");
			this.localizedLore = I18n.format("skin.immersiveintelligence."+skin.name+".desc");
			this.localizedText = I18n.format("skin.immersiveintelligence."+skin.name+".text");

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
			IIClientUtils.drawStringCentered(manual.fontRenderer, TextFormatting.BOLD.toString()+TextFormatting.UNDERLINE+localizedName, x+10, y+40, 100, 0, manual.getTextColour());

		if(localizedLore!=null&&!localizedLore.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.ITALIC+localizedLore, x, y+52, 120, manual.getTextColour());

		int shift = localizedLore!=null?manual.fontRenderer.getWordWrappedHeight(localizedLore, 120): 0;

		if(localizedText!=null&&!localizedText.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, localizedText, x, y+56+shift, 120, manual.getTextColour());
		drawOrnamentalFrame(gui, x+42, y+4);
		GlStateManager.enableBlend();

		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.translate(x+42, y+4, 0);
		GlStateManager.scale(2, 2, 2);
		ManualUtils.renderItem().renderItemAndEffectIntoGUI(getCurrentlyDisplayedStack(), 0, 0);
		GlStateManager.popMatrix();
	}

	private ItemStack getCurrentlyDisplayedStack()
	{
		float progress = AMTUtils.getDebugProgress(maxTimer, 0);
		return renderStacks.get((int)MathHelper.clamp(progress*renderStacks.size(), 0, renderStacks.size()-1));
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
		return false;
	}
}
