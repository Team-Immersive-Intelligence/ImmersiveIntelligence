package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Pabilo8
 * @since 15-05-2020
 */
public class IIManualPages
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/gui/manual.png";

	public static class BulletComponentDisplay extends ManualPages
	{
		protected String name;
		protected String localizedName;
		protected String localizedLore;
		protected String localizedType;
		ItemStack stack;
		EnumComponentRole type;
		float density;


		public BulletComponentDisplay(ManualInstance manual, IBulletComponent component)
		{
			this(manual, component.getName(), component.getMaterial().getExampleStack(), component.getRole(), component.getDensity());
		}

		public BulletComponentDisplay(ManualInstance manual, String name, ItemStack stack, EnumComponentRole type, float density)
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

				localizedType = I18n.format(CommonProxy.description_key+"bullet_type."+type.getName());

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
				ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD.toString()+TextFormatting.UNDERLINE.toString()+localizedName, x+40, y, 90, manual.getTextColour());

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

			if(Utils.isPointInRectangle(x+4, y, x+36, y+32, mx, my))
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

	public static class BulletCoreDisplay extends BulletComponentDisplay
	{
		float damageMod, penMod, blastMod;

		public BulletCoreDisplay(ManualInstance manual, IBulletCoreType coreType)
		{
			super(manual, coreType.getName(), coreType.getMaterial().getExampleStack(), coreType.getRole(), coreType.getDensity());
			this.text = "bullet_core."+name;
			damageMod = coreType.getDamageModifier(new NBTTagCompound());
			penMod = coreType.getPenetrationModifier(new NBTTagCompound());
			blastMod = coreType.getExplosionModifier();
		}

		@Override
		int renderInfo(GuiManual gui, int x, int y)
		{
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.dmg_mod", damageMod), x, y+20, 120, manual.getTextColour());
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.pen_mod", penMod), x, y+30, 120, manual.getTextColour());
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+I18n.format("ie.manual.entry.bullet_components.blast_mod", blastMod), x, y+40, 120, manual.getTextColour());

			return super.renderInfo(gui, x, y)+30;
		}
	}

	public static class DataVariablesDisplay extends ManualPages
	{
		List<DisplayEntry> entries = new ArrayList<>();
		boolean input;
		String title;
		int titleShift;

		public DataVariablesDisplay(ManualInstance manual, String name, boolean input)
		{
			super(manual, name+(input?".vars_in": ".vars_out"));
			this.input = input;
		}

		public DataVariablesDisplay addEntry(IDataType type, char... c)
		{
			if(c.length > 0)
				entries.add(new DisplayEntry(type, c, this.text+"."+c[0]+".main", this.text+"."+c[0]+".sub"));
			return this;
		}

		@Override
		public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
		{
			highlighted = ItemStack.EMPTY;

			boolean uni = manual.fontRenderer.getUnicodeFlag();
			manual.fontRenderer.setUnicodeFlag(true);

			title = I18n.format("ie.manual.entry."+(input?"variables_input": "variables_output"));
			titleShift = 55-(manual.fontRenderer.getStringWidth(title)/2);

			int spacing = 15;
			for(DisplayEntry entry : entries)
			{
				entry.init(manual, gui, x, y+spacing, pageButtons);
				spacing += entry.getSpacing();
			}

			manual.fontRenderer.setUnicodeFlag(uni);

		}

		@Override
		public void renderPage(GuiManual gui, int x, int y, int mx, int my)
		{
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD.toString()+TextFormatting.UNDERLINE.toString()+title, x+titleShift, y, 120, manual.getTextColour());

			DisplayEntry tooltip = null;
			int down = 15;
			for(DisplayEntry entry : entries)
			{
				GlStateManager.enableBlend();
				GlStateManager.color(0.9f, 0.9f, 0.85f, 0.85f);
				ClientUtils.bindTexture(entry.dataType.textureLocation());
				gui.drawTexturedModalRect(x-4, y+down-3, 40, entry.dataType.getFrameOffset()*20, 16, 16);

				if(Utils.isPointInRectangle(x-4, y+down-3, x+12, y+down+13, mx, my))
					tooltip = entry;

				char toDraw = entry.c[(int)Math.ceil((gui.mc.world.getTotalWorldTime()%entry.prezTimeTotal)/20)];

				GlStateManager.pushMatrix();
				GlStateManager.scale(1.25, 1.25, 1.25);
				ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+String.valueOf(toDraw), (int)((x+15)/1.25f), (int)((y+down-1)/1.25f), 100, entry.dataType.getTypeColour());
				GlStateManager.popMatrix();

				GlStateManager.disableBlend();
				ManualUtils.drawSplitString(manual.fontRenderer, entry.localizedText, x+24, y+down, 100, manual.getTextColour());
				down += entry.spacingMain;
				ManualUtils.drawSplitString(manual.fontRenderer, entry.localizedSubText, x, y+down, 120, manual.getTextColour());
				down += entry.spacingSub;

			}

			manual.fontRenderer.setUnicodeFlag(false);
			if(tooltip!=null)
			{
				ArrayList<String> toDraw = new ArrayList<>();
				toDraw.add(I18n.format(CommonProxy.data_key+"datatype."+tooltip.dataType.getName()));
				ClientUtils.drawHoveringText(toDraw, mx, my, manual.fontRenderer, -1, -1);
			}
			manual.fontRenderer.setUnicodeFlag(true);

		}

		@Override
		public boolean listForSearch(String searchTag)
		{
			return false;
		}


		public static class DisplayEntry
		{
			IDataType dataType;
			char[] c;
			int prezTimeTotal;
			String text, subtext;
			String localizedText, localizedSubText;
			int spacingMain;
			int spacingSub;

			public DisplayEntry(IDataType dataType, char[] c, String text, String subtext)
			{
				this.dataType = dataType;
				this.c = c;
				this.text = text;
				this.subtext = subtext;
			}

			public void init(ManualInstance manual, GuiManual gui, int x, int y, List<GuiButton> pageButtons)
			{
				this.prezTimeTotal = c.length*20;

				this.localizedText = manual.formatText(text);
				this.localizedSubText = TextFormatting.ITALIC+manual.formatText(subtext);

				this.localizedText = addLinks(manual, gui, this.localizedText, x+24, y, 80, pageButtons);
				this.spacingMain = manual.fontRenderer.getWordWrappedHeight(this.localizedText, 120)+4;
				if(this.localizedText==null)
					this.localizedText = "";

				this.localizedSubText = addLinks(manual, gui, this.localizedSubText, x, y+spacingMain, 100, pageButtons);
				this.spacingSub = manual.fontRenderer.getWordWrappedHeight(this.localizedSubText, 160);
				if(this.localizedSubText==null)
					this.localizedSubText = "";

				if(this.localizedSubText.equals(TextFormatting.ITALIC.toString()))
					this.spacingSub = 4;
				else
					this.spacingSub += 10;
			}

			public int getSpacing()
			{
				return spacingMain+spacingSub;
			}
		}
	}

}
