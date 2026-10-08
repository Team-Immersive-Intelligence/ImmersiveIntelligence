package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.LayoutComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 07.10.2026
 * @since 05.12.2025
 */
@ParametersAreNonnullByDefault
public class IIRecipeJEICategory<T extends IIMultiblockRecipe> implements IRecipeCategory<IIRecipeJEIWrapper<T>>, IRecipeWrapperFactory<T>
{
	private final String uniqueName;
	private final String localizedName;
	private final Class<T> recipeClass;
	private java.util.function.Supplier<List<IIMultiblockRecipe>> displayRecipes;
	private ItemStack[] displayStacks;
	private IDrawable background;

	/**
	 * @param recipeClass The class of the recipe this category represents
	 * @param uniqueName  A unique name for this recipe category
	 * @param localKey    The localization key for this recipe category's name
	 */
	public IIRecipeJEICategory(Class<T> recipeClass, String uniqueName, String localKey)
	{
		this.uniqueName = uniqueName;
		this.localizedName = I18n.format(localKey);
		this.background = null;
		this.recipeClass = recipeClass;
		List<T> recipes = IIMultiblockRecipe.getRecipes(recipeClass);
		if(!recipes.isEmpty())
		{
			IIRecipeLayout layout = recipes.stream().filter(r -> r.matchesSubCategory(getUid()))
					.map(IIMultiblockRecipe::getRecipeLayout).filter(java.util.Objects::nonNull).findFirst().orElse(null);
			if(layout!=null)
				this.background = new MostExcellentDrawableImplementation(layout.getGridWidth(), layout.getGridHeight(),
						layout.isEarlyGame()?DecoTextures.BG_WOODEN: DecoTextures.BG_STEEL,
						layout.isEarlyGame()?DecoTextures.TEMPLATE_ROUND_WOODEN: DecoTextures.TEMPLATE_ROUND
				);
		}
		if(this.background==null)
			this.background = new MostExcellentDrawableImplementation(150, 60,
					DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER);

		this.displayStacks = new ItemStack[0];
	}

	public IIRecipeJEICategory(Class<T> recipeClass, ItemStack machineStack)
	{
		this(recipeClass, IIMultiblockRecipe.getRecipeClassName(recipeClass), machineStack.getUnlocalizedName()+".name");
		this.displayStacks = new ItemStack[]{machineStack};
	}

	public IIRecipeJEICategory(Class<T> recipeClass, ItemStack machineStack, String recipeName)
	{
		this(recipeClass, recipeName, "desc.immersiveintelligence.jei."+recipeName+"_recipe");
		this.displayStacks = new ItemStack[]{machineStack};
	}

	public IIRecipeJEICategory<T> withDisplayRecipes(java.util.function.Supplier<List<IIMultiblockRecipe>> supplier)
	{
		this.displayRecipes = supplier;
		List<IIMultiblockRecipe> displays = supplier.get();
		if(!displays.isEmpty())
		{
			IIRecipeLayout layout = displays.get(0).getRecipeLayout();
			if(layout!=null)
				background = new MostExcellentDrawableImplementation(layout.getGridWidth(), layout.getGridHeight(),
						DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND);
		}
		return this;
	}

	public void addCatalysts(IModRegistry registry)
	{
		for(ItemStack stack : displayStacks)
			registry.addRecipeCatalyst(stack, getUid());
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return "ii."+uniqueName;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	public String getRecipeCategoryUid()
	{
		return "ii."+uniqueName;
	}

	@Nonnull
	@Override
	public String getModName()
	{
		return ImmersiveIntelligence.MODID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IIRecipeJEIWrapper<T> recipeWrapper, IIngredients ingredients)
	{
		IIMultiblockRecipe recipe = recipeWrapper.recipe;
		IIRecipeLayout layout = recipe.getRecipeLayout();

		if(layout!=null)
		{
			IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

			//Track indices for automatic assignment
			int itemInputIndex = 0, itemOutputIndex = 0;

			List<LayoutComponent> components = layout.getComponents();

			java.util.Set<Integer> toolSlots = new java.util.HashSet<>();
			for(LayoutComponent component : components)
			{
				if(component.getData()==null||component.getIoType()==IOType.NEUTRAL)
					continue;
				int x = component.getX();
				int y = component.getY();

				switch(component.getType())
				{
					case SLOT:
						if(component.isToolSlot())
							toolSlots.add(getSlotIndex(component.getIoType()==IOType.INPUT, component.getIoType()==IOType.INPUT?itemInputIndex: itemOutputIndex, ingredients));
						setupSlot(component, itemStacks, ingredients, x, y, itemInputIndex, itemOutputIndex);
						if(component.getIoType()==IOType.INPUT)
							itemInputIndex++;
						else if(component.getIoType()==IOType.OUTPUT)
							itemOutputIndex++;
						break;
					default:
						break;
				}
			}
			itemStacks.addTooltipCallback((slot, input, stack, tooltip) -> {
				if(toolSlots.contains(slot))
					tooltip.add(I18n.format("desc.immersiveintelligence.recipe.tool_slot"));
			});
			//The wrapper scales all tanks together and keeps grouped variants in sync.
			recipeWrapper.bindDisplay(recipeLayout);
		}
	}

	//--- Layout setup methods ---//

	private void setupSlot(LayoutComponent component, IGuiItemStackGroup itemStacks,
						   IIngredients ingredients, int x, int y,
						   int itemInputIndex, int itemOutputIndex)
	{

		IOType ioType = component.getIoType();
		boolean isInput = ioType==IOType.INPUT;

		if(isInput)
		{
			//Assign input slot
			if(itemInputIndex < ingredients.getInputs(VanillaTypes.ITEM).size())
			{
				int slotIndex = getSlotIndex(true, itemInputIndex, ingredients);
				itemStacks.init(slotIndex, true, x, y);
				itemStacks.set(slotIndex, ingredients.getInputs(VanillaTypes.ITEM).get(itemInputIndex));
			}
		}
		else //Assign output slot
			if(ioType==IOType.OUTPUT)
				if(itemOutputIndex < ingredients.getOutputs(VanillaTypes.ITEM).size())
				{
					int slotIndex = getSlotIndex(false, itemOutputIndex, ingredients);
					itemStacks.init(slotIndex, false, x, y);
					itemStacks.set(slotIndex, ingredients.getOutputs(VanillaTypes.ITEM).get(itemOutputIndex));
				}
	}

	private int getSlotIndex(boolean isInput, int index, IIngredients ingredients)
	{
		//Output slots start after all input slots
		return isInput?index: ingredients.getInputs(VanillaTypes.ITEM).size()+index;
	}

	//--- Recipe Wrapper ---//

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(T recipe)
	{
		return new IIRecipeJEIWrapper<>(recipe, this.displayStacks.length > 0?this.displayStacks[0]: ItemStack.EMPTY);
	}

	public void register(IModRegistry modRegistry)
	{
		addCatalysts(modRegistry);
		if(displayRecipes!=null)
		{
			modRegistry.addRecipes(displayRecipes.get().stream()
					.map(display -> new IIRecipeJEIWrapper<>(display, displayStacks.length > 0?displayStacks[0]: ItemStack.EMPTY))
					.collect(Collectors.toList()), getUid());
			return;
		}
		modRegistry.handleRecipes(recipeClass, this, getRecipeCategoryUid());
		modRegistry.addRecipes(IIMultiblockRecipe.streamRecipes(recipeClass)
				.filter(t -> t.matchesSubCategory(getUid())&&t.getRecipeLayout()!=null)
				.collect(Collectors.toList()), getUid()
		);
		IILogger.info("Registered JEI compat for "+recipeClass.getSimpleName());
	}

	public static class MostExcellentDrawableImplementation implements IDrawableStatic
	{
		private final int width, height;
		private final ResLoc texture, mask;

		public MostExcellentDrawableImplementation(int width, int height, ResLoc texture, ResLoc mask)
		{
			this.width = width;
			this.height = height;
			this.texture = texture;
			this.mask = mask;
		}

		@Override
		public void draw(Minecraft minecraft, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight)
		{
			ClientUtils.bindAtlas();
			IIDrawUtils.startTexturedColored()
					.addOffset(xOffset, yOffset)
					.drawRepeatedTexColorRect(xOffset, yOffset, width, height, IIColor.WHITE, texture, 16)
					.finish();
		}

		@Override
		public int getWidth()
		{
			return width;
		}

		@Override
		public int getHeight()
		{
			return height;
		}

		@Override
		public void draw(Minecraft mc, int xOffset, int yOffset)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f, 1f);
			ClientUtils.bindAtlas();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			//Mask
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);

			TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(mask.toString());
			float u = sprite.getMinU(), uu = sprite.getInterpolatedU(8);
			float v = sprite.getMinV(), vv = sprite.getInterpolatedV(8);
			IIDrawUtils.startTexturedColored()
					.drawConnectedTexColorRect(xOffset-4, yOffset-4, width+8, height+8, IIColor.WHITE,
							32, 32, 8, 8, u, uu, v, vv)
					.finish();

			//Background
			TextureAtlasSprite spriteBack = mc.getTextureMapBlocks().getAtlasSprite(texture.toString());
			float uBack = spriteBack.getMinU(), uuBack = spriteBack.getMaxU();
			float vBack = spriteBack.getMinV(), vvBack = spriteBack.getMaxV();
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
			GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
			IIDrawUtils.startTexturedColored()
					.drawConnectedTexColorRect(xOffset-4, yOffset-4, width+8, height+8, IIColor.WHITE,
							32, 32, 8, 8, uBack, uuBack, vBack, vvBack)
					.finish();
			GL11.glDisable(GL11.GL_STENCIL_TEST);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
			GlStateManager.disableTexture2D();
			IIDrawUtils.startColored()
					.drawColorGradient(xOffset, yOffset, width, height/2,
							IIColor.fromARGB(255, 128, 128, 128),
							IIColor.fromARGB(0, 128, 128, 128),
							IIColor.fromARGB(255, 0, 0, 0),
							IIColor.fromARGB(0, 0, 0, 0))
					.finish();
			GlStateManager.enableTexture2D();

			IIDrawUtils.startTexturedColored()
					.drawConnectedTexColorRect(xOffset-4, yOffset-4, width+8, height+8, IIColor.WHITE,
							32, 32, 8, 8, u, uu, v, vv)
					.finish();

			GlStateManager.depthFunc(GL11.GL_LEQUAL);
			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();

			GlStateManager.popMatrix();
		}
	}
}
