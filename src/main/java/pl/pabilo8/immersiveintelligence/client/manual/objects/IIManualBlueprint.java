package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.lib.manual.ManualPages.PositionedItemStack;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiButtonManualNavigation;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays a crafting recipe for item(s).
 *
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualBlueprint extends IIManualObject
{
	GuiButtonManualNavigation buttonNext, buttonPrev;
	ItemStack highlighted = ItemStack.EMPTY;
	final ArrayList<PositionedItemStack[]> recipes = new ArrayList<>();
	private String blueprintName;
	int recipePage;
	int yOff;

	public IIManualBlueprint(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		//add a single item
		dataSource.checkSetString("blueprint", s -> blueprintName = s);
		recalculateCraftingRecipes(page);
		if(this.recipes.size() > 1)
		{
			buttonPrev = new GuiButtonManualNavigation(gui, 100, x-2, y+yOff/2-3, 8, 10, 0);
			buttonNext = new GuiButtonManualNavigation(gui, 100+1, x+122-16, y+yOff/2-3, 8, 10, 1);
		}

		height = yOff+12;
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);
		highlighted = ItemStack.EMPTY;

		GlStateManager.pushMatrix();
		if(recipes.size() > 1)
		{
			buttonNext.drawButton(mc, mx, my, partialTicks);
			buttonPrev.drawButton(mc, mx, my, partialTicks);
		}

		boolean flag = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);
		IIClientUtils.drawStringCentered(manual.fontRenderer, TextFormatting.ITALIC+"Engineer's Workbench", x, y+yOff, width, 0, manual.getPagenumberColour());
		manual.fontRenderer.setUnicodeFlag(flag);

		if(!recipes.isEmpty()&&recipePage >= 0&&recipePage < this.recipes.size())
		{
			GlStateManager.enableRescaleNormal();
			RenderHelper.enableGUIStandardItemLighting();

			int maxX = 0;
			for(PositionedItemStack pstack : recipes.get(recipePage))
				if(pstack!=null)
				{
					if(pstack.x > maxX)
						maxX = pstack.x;
					gui.drawGradientRect(x+pstack.x, y+pstack.y, x+pstack.x+16, y+pstack.y+16, 0x33666666, 0x33666666);
				}
			ManualUtils.bindTexture(manual.texture);
			ManualUtils.drawTexturedRect(x+maxX-17, y+yOff/2-5, 16, 10, 0/256f, 16/256f, 226/256f, 236/256f);

		}

		GlStateManager.translate(0, 0, 300);

		manual.fontRenderer.setUnicodeFlag(false);
		//RenderItem.getInstance().renderWithColor=true;
		if(!recipes.isEmpty()&&recipePage >= 0&&recipePage < this.recipes.size())
		{
			for(PositionedItemStack pstack : recipes.get(recipePage))
				if(pstack!=null)
					if(!pstack.getStack().isEmpty())
					{
						ManualUtils.renderItem().renderItemAndEffectIntoGUI(pstack.getStack(), x+pstack.x, y+pstack.y);
						ManualUtils.renderItem().renderItemOverlayIntoGUI(manual.fontRenderer, pstack.getStack(), x+pstack.x, y+pstack.y, null);

						if(mx >= x+pstack.x&&mx < x+pstack.x+16&&my >= y+pstack.y&&my < y+pstack.y+16)
							highlighted = pstack.getStack();
					}
		}

		GlStateManager.translate(0, 0, -300);
		GlStateManager.disableRescaleNormal();
		GlStateManager.enableBlend();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();

	}

	@Override
	protected int getDefaultHeight()
	{
		return 20;
	}

	@Override
	public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY)
	{
		if(recipes.size() > 1&&super.mousePressed(mc, mouseX, mouseY))
		{
			if(buttonPrev.mousePressed(mc, mouseX, mouseY))
			{
				if(recipePage > 0)
					recipePage--;
			}
			else if(buttonNext.mousePressed(mc, mouseX, mouseY))
			{
				if(recipePage < recipes.size()-1)
					recipePage++;
			}
			else
				return false;
			return true;
		}

		return false;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return (!highlighted.isEmpty())?this.gui.getItemToolTip(highlighted): null;
	}

	//--- Private Methods (for parsing) ---//

	private void recalculateCraftingRecipes(IIManualPage page)
	{
		this.recipes.clear();
		ArrayListMultimap<String, BlueprintCraftingRecipe> cmRecipes = BlueprintCraftingRecipe.recipeList;
		List<BlueprintCraftingRecipe> recipeList = cmRecipes.get(blueprintName);
		for(BlueprintCraftingRecipe recipe : recipeList)
		{
			PositionedItemStack[] pIngredients = new PositionedItemStack[recipe.inputs.length+1];
			int h = (int)Math.ceil(recipe.inputs.length/2f);
			for(int i = 0; i < recipe.inputs.length; i++)
				pIngredients[i] = new PositionedItemStack(recipe.inputs[i].getSizedStackList(), 32+i%2*18, i/2*18);
			int middle = (int)(h/2f*18)-8;
			pIngredients[pIngredients.length-1] = new PositionedItemStack(recipe.output, 86, middle);

			this.recipes.add(pIngredients);
			if(h*18 > yOff)
				yOff = h*18;

			page.addProvidedItem(recipe.output);
		}
	}
}
