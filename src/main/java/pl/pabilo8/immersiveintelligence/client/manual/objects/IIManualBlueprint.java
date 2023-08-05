package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages.PositionedItemStack;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiButtonManualNavigation;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
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
	NonNullList<ItemStack> stacks;
	ItemStack highlighted = ItemStack.EMPTY;
	final ArrayList<PositionedItemStack[]> recipes = new ArrayList<>();
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

		//create default
		stacks = NonNullList.create();
		//add a single item
		dataSource.checkSetCompound("item", nbt -> stacks.add(new ItemStack(nbt)));
		//add an item list
		if(dataSource.hasKey("items"))
			dataSource.streamList(NBTTagCompound.class, "items", NBT.TAG_COMPOUND)
					.map(ItemStack::new)
					.forEach(stacks::add);

		recalculateCraftingRecipes(page);
		if(this.recipes.size() > 1)
		{
			buttonPrev = new GuiButtonManualNavigation(gui, 100, x-2, y+yOff/2-3, 8, 10, 0);
			buttonNext = new GuiButtonManualNavigation(gui, 100+1, x+122-16, y+yOff/2-3, 8, 10, 1);
		}

		height = yOff;
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
		List<String> cmCategories = BlueprintCraftingRecipe.blueprintCategories;
		ArrayListMultimap<String, BlueprintCraftingRecipe> cmRecipes = BlueprintCraftingRecipe.recipeList;

		for(String category : cmCategories)
			for(BlueprintCraftingRecipe recipe : cmRecipes.get(category))
				for(int iStack = 0; iStack < stacks.size(); iStack++)
				{
					ItemStack output = stacks.get(iStack);
					if(!recipe.output.isEmpty()&&ManualUtils.stackMatchesObject(recipe.output, output)&&recipe.inputs!=null&&recipe.inputs.length > 0)
					{
						int h = (int)Math.ceil(recipe.inputs.length/2f);
						PositionedItemStack[] pIngredients = new PositionedItemStack[recipe.inputs.length+2];
						for(int i = 0; i < recipe.inputs.length; i++)
							pIngredients[i] = new PositionedItemStack(recipe.inputs[i].getSizedStackList(), 32+i%2*18, i/2*18);
						int middle = (int)(h/2f*18)-8;
						pIngredients[pIngredients.length-2] = new PositionedItemStack(recipe.output, 86, middle);
						pIngredients[pIngredients.length-1] = new PositionedItemStack(BlueprintCraftingRecipe.getTypedBlueprint(category), 8, middle);

						if(iStack < this.recipes.size())
							this.recipes.add(iStack, pIngredients);
						else
							this.recipes.add(pIngredients);
						if(h*18 > yOff)
							yOff = h*18;
					}
				}
		stacks.forEach(page::addProvidedItem);
	}

	private void handleRecipe(IRecipe recipe, int iStack)
	{
		int w, h;
		NonNullList<Ingredient> ingredientsPre = recipe.getIngredients();

		if(recipe instanceof ShapelessRecipes||recipe instanceof ShapelessOreRecipe)
		{
			w = ingredientsPre.size() > 6?3: ingredientsPre.size() > 1?2: 1;
			h = ingredientsPre.size() > 4?3: ingredientsPre.size() > 2?2: 1;
		}
		else if(recipe instanceof IShapedRecipe)
		{
			w = ((IShapedRecipe)recipe).getRecipeWidth();
			h = ((IShapedRecipe)recipe).getRecipeWidth();
		}
		else
			return;

		PositionedItemStack[] pIngredients = new PositionedItemStack[ingredientsPre.size()+1];
		int xBase = (120-(w+2)*18)/2;
		for(int hh = 0; hh < h; hh++)
			for(int ww = 0; ww < w; ww++)
				if(hh*w+ww < ingredientsPre.size())
					pIngredients[hh*w+ww] = new PositionedItemStack(ingredientsPre.get(hh*w+ww), xBase+ww*18, hh*18);
		pIngredients[pIngredients.length-1] = new PositionedItemStack(recipe.getRecipeOutput(), xBase+w*18+18, (int)(h/2f*18)-8);
		if(iStack < this.recipes.size())
			this.recipes.add(iStack, pIngredients);
		else
			this.recipes.add(pIngredients);
		if(h*18 > yOff)
			yOff = h*18;
	}
}
