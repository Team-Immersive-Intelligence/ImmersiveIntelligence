package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.*;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients.JEIDustStackRenderer;
import pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients.JEIFluidStackRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Displays a multiblock recipe in JEI.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.10.2026
 * @since 07.05.2021
 */
public class IIRecipeJEIWrapper<T extends IIMultiblockRecipe> implements IRecipeWrapper
{
	public static final ResLoc TEXTURE = IIReference.RES_TEXTURES_GUI.with("jei_stuff", ResLoc.EXT_PNG);
	private final ItemStack machineStack;
	public final T recipe;
	private final List<IIRecipeLayout> layouts;
	private final List<DisplayIngredients> variants = new ArrayList<>();
	private final List<Integer> visibleVariants = new ArrayList<>();
	private IRecipeLayout jeiLayout;
	private int variantIndex, ingredientCycle;
	private long lastCycle;

	public IIRecipeJEIWrapper(T recipe, ItemStack machineStack)
	{
		//Prepare thyself
		this.recipe = recipe;
		this.machineStack = machineStack;
		//Thy end is now
		recipe.getRecipeLayout();
		this.layouts = recipe instanceof IIRecipeDisplay?((IIRecipeDisplay)recipe).getLayouts():
				recipe.getRecipeLayout()==null?Collections.emptyList(): Collections.singletonList(recipe.getRecipeLayout());
		for(IIRecipeLayout layout : layouts)
		{
			visibleVariants.add(variants.size());
			variants.add(collectIngredients(layout));
		}
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, mergeAlternatives(v -> v.itemInputs, ItemStack::areItemStacksEqual));
		ingredients.setOutputLists(VanillaTypes.ITEM, mergeAlternatives(v -> v.itemOutputs, ItemStack::areItemStacksEqual));
		ingredients.setInputLists(VanillaTypes.FLUID, mergeAlternatives(v -> v.fluidInputs, FluidStack::isFluidStackIdentical));
		ingredients.setOutputLists(VanillaTypes.FLUID, mergeAlternatives(v -> v.fluidOutputs, FluidStack::isFluidStackIdentical));
		ingredients.setInputLists(JEIHelper.DUSTSTACK, mergeAlternatives(v -> v.dustInputs, (a, b) -> a.name.equals(b.name)&&a.amount==b.amount));
		ingredients.setOutputLists(JEIHelper.DUSTSTACK, mergeAlternatives(v -> v.dustOutputs, (a, b) -> a.name.equals(b.name)&&a.amount==b.amount));
	}

	private DisplayIngredients collectIngredients(IIRecipeLayout recipeLayout)
	{
		//Collect ingredients from layout components
		List<List<ItemStack>> itemInputs = new ArrayList<>();
		List<List<ItemStack>> itemOutputs = new ArrayList<>();
		List<List<FluidStack>> fluidInputs = new ArrayList<>();
		List<List<FluidStack>> fluidOutputs = new ArrayList<>();
		List<List<DustStack>> dustInputs = new ArrayList<>();
		List<List<DustStack>> dustOutputs = new ArrayList<>();

		for(LayoutComponent component : recipeLayout.getComponents())
		{
			if(component.getData()==null||component.getIoType()==IOType.NEUTRAL)
				continue;
			switch(component.getType())
			{
				case SLOT:
				{
					List<ItemStack> displayStacks = IIRecipeLayout.getDisplayItems(component.getData());
					if(component.getIoType()==IOType.INPUT)
						itemInputs.add(displayStacks);
					else
						itemOutputs.add(displayStacks);
				}
				break;
				case FLUID_TANK:
				{
					assert component.getData()!=null;
					FluidStack fs = (FluidStack)component.getData();
					List<FluidStack> fluids = fs.amount > 0?Collections.singletonList(fs.copy()): Collections.emptyList();
					if(component.getIoType()==IOType.INPUT)
						fluidInputs.add(fluids);
					else
						fluidOutputs.add(fluids);
				}
				break;
				case DUST_TANK:
				{
					assert component.getData()!=null;
					DustStack dustStack = (DustStack)component.getData();
					if(component.getIoType()==IOType.INPUT)
						dustInputs.add(Collections.singletonList(dustStack));
					else
						dustOutputs.add(Collections.singletonList(dustStack));
				}
				break;
				default:
					break;
			}
		}

		//Get dust stack items for reference
		for(List<DustStack> dustInput : dustInputs)
			itemInputs.add(DustUtils.getDustStacks(dustInput.get(0).name));
		for(List<DustStack> dustOutput : dustOutputs)
			itemOutputs.add(DustUtils.getDustStacks(dustOutput.get(0).name));

		return new DisplayIngredients(itemInputs, itemOutputs, fluidInputs, fluidOutputs, dustInputs, dustOutputs);
	}

	private <V> List<List<V>> mergeAlternatives(Function<DisplayIngredients, List<List<V>>> getter, BiPredicate<V, V> equal)
	{
		List<List<V>> merged = new ArrayList<>();
		for(DisplayIngredients variant : variants)
		{
			List<List<V>> slots = getter.apply(variant);
			for(int i = 0; i < slots.size(); i++)
			{
				while(merged.size() <= i)
					merged.add(new ArrayList<>());
				List<V> alternatives = merged.get(i);
				for(V value : slots.get(i))
					if(alternatives.stream().noneMatch(existing -> equal.test(existing, value)))
						alternatives.add(value);
			}
		}
		return merged;
	}

	/**
	 * Keep related inputs, outputs and fluid costs on the same display variant.
	 */
	public void bindDisplay(IRecipeLayout layout)
	{
		this.jeiLayout = null;
		if(layouts.isEmpty())
			return;
		if(layouts.size()==1)
		{
			setupDisplayedTanks(layout, layouts.get(0));
			return;
		}
		this.jeiLayout = layout;
		visibleVariants.clear();
		for(int i = 0; i < variants.size(); i++)
			visibleVariants.add(i);
		IFocus<?> focus = layout.getFocus();
		if(focus!=null)
		{
			List<Integer> matching = findFocusedVariants(focus, true);
			if(matching.isEmpty())
				matching = findFocusedVariants(focus, false);
			if(!matching.isEmpty())
			{
				visibleVariants.clear();
				visibleVariants.addAll(matching);
			}
		}
		variantIndex = ingredientCycle = 0;
		lastCycle = Minecraft.getSystemTime();
		updateDisplayedIngredients();
	}

	private List<Integer> findFocusedVariants(IFocus<?> focus, boolean exact)
	{
		List<Integer> matching = new ArrayList<>();
		boolean input = focus.getMode()==IFocus.Mode.INPUT;
		for(int i = 0; i < variants.size(); i++)
		{
			DisplayIngredients variant = variants.get(i);
			Object value = focus.getValue();
			if(value instanceof ItemStack)
			{
				List<List<ItemStack>> slots = input?variant.itemInputs: variant.itemOutputs;
				if(slots.stream().anyMatch(items -> matchesItemFocus(items, (ItemStack)value, exact)))
					matching.add(i);
			}
			else if(value instanceof FluidStack)
			{
				List<List<FluidStack>> tanks = input?variant.fluidInputs: variant.fluidOutputs;
				if(tanks.stream().flatMap(List::stream).anyMatch(fluid -> fluid.isFluidEqual((FluidStack)value)))
					matching.add(i);
			}
		}
		return matching;
	}

	private boolean matchesItemFocus(List<ItemStack> items, ItemStack focus, boolean exact)
	{
		return exact?items.stream().anyMatch(item -> ItemStack.areItemsEqual(item, focus)&&ItemStack.areItemStackTagsEqual(item, focus)):
				JEIHelper.modRegistry.getIngredientRegistry().getIngredientHelper(VanillaTypes.ITEM).getMatch(items, focus)!=null;
	}

	private IIRecipeLayout getDisplayedLayout()
	{
		return layouts.isEmpty()?null: layouts.get(visibleVariants.get(variantIndex));
	}

	private void updateDisplayedIngredients()
	{
		DisplayIngredients variant = variants.get(visibleVariants.get(variantIndex));
		setDisplayedItems(variant.itemInputs, true, 0);
		setDisplayedItems(variant.itemOutputs, false, variant.itemInputs.size());
		setupDisplayedTanks(jeiLayout, getDisplayedLayout());
	}

	/**
	 * Use one capacity for all tanks of the same ingredient type in the displayed
	 * variant. Zero-cost tanks retain their positions without lowering the mean.
	 */
	private double getTankDisplayCapacity(IIRecipeLayout layout, IIRecipeLayout.ComponentType type)
	{
		long total = 0;
		int count = 0, maximum = 0;
		for(LayoutComponent component : layout.getComponents())
		{
			if(component.getType()!=type||component.getData()==null||component.getIoType()==IOType.NEUTRAL)
				continue;
			int amount = type==IIRecipeLayout.ComponentType.FLUID_TANK?
					((FluidStack)component.getData()).amount: ((DustStack)component.getData()).amount;
			if(type==IIRecipeLayout.ComponentType.DUST_TANK&&((DustStack)component.getData()).name.isEmpty())
				continue;
			if(amount <= 0)
				continue;
			total += amount;
			count++;
			maximum = Math.max(maximum, amount);
		}
		//Twice the mean places a single tank halfway up. Keep large outliers visible.
		return count==0?1: Math.max(maximum, 2.0*total/count);
	}

	private void setupDisplayedTanks(IRecipeLayout jeiLayout, IIRecipeLayout layout)
	{
		double fluidCapacity = getTankDisplayCapacity(layout, IIRecipeLayout.ComponentType.FLUID_TANK);
		double dustCapacity = getTankDisplayCapacity(layout, IIRecipeLayout.ComponentType.DUST_TANK);
		int fluidInputs = 0, dustInputs = 0;
		for(LayoutComponent component : layout.getComponents())
			if(component.getData()!=null&&component.getIoType()==IOType.INPUT)
			{
				if(component.getType()==IIRecipeLayout.ComponentType.FLUID_TANK)
					fluidInputs++;
				else if(component.getType()==IIRecipeLayout.ComponentType.DUST_TANK)
					dustInputs++;
			}
		int fluidInputIndex = 0, fluidOutputIndex = 0;
		int dustInputIndex = 0, dustOutputIndex = 0;
		for(LayoutComponent component : layout.getComponents())
		{
			if(component.getData()==null||component.getIoType()==IOType.NEUTRAL)
				continue;
			boolean input = component.getIoType()==IOType.INPUT;
			int x = component.getX(), y = component.getY();
			int width = component.getWidth(), height = component.getHeight();
			if(component.getType()==IIRecipeLayout.ComponentType.FLUID_TANK)
			{
				int index = input?fluidInputIndex++: fluidInputs+fluidOutputIndex++;
				FluidStack fluid = (FluidStack)component.getData();
				IGuiFluidStackGroup tanks = jeiLayout.getFluidStacks();
				tanks.init(index, input, new JEIFluidStackRenderer(width-2, height-1, fluidCapacity),
						x, y, width, height, 1, 1);
				tanks.set(index, fluid.amount > 0?Collections.singletonList(fluid.copy()): Collections.emptyList());
			}
			else if(component.getType()==IIRecipeLayout.ComponentType.DUST_TANK)
			{
				int index = input?dustInputIndex++: dustInputs+dustOutputIndex++;
				DustStack dust = (DustStack)component.getData();
				IGuiIngredientGroup<DustStack> tanks = jeiLayout.getIngredientsGroup(JEIHelper.DUSTSTACK);
				tanks.init(index, input, new JEIDustStackRenderer(width-2, height-1, dustCapacity),
						x, y, width, height, 1, 1);
				tanks.set(index, dust.amount > 0&&!dust.name.isEmpty()?Collections.singletonList(dust.copy()): Collections.emptyList());
			}
		}
	}

	private void setDisplayedItems(List<List<ItemStack>> slots, boolean input, int offset)
	{
		IFocus<?> focus = jeiLayout.getFocus();
		IIngredientHelper<ItemStack> helper = JEIHelper.modRegistry.getIngredientRegistry().getIngredientHelper(VanillaTypes.ITEM);
		for(int i = 0; i < slots.size(); i++)
		{
			if(!jeiLayout.getItemStacks().getGuiIngredients().containsKey(offset+i))
				continue;
			List<ItemStack> items = slots.get(i);
			ItemStack shown = items.isEmpty()?ItemStack.EMPTY: items.get(ingredientCycle%items.size());
			if(focus!=null&&focus.getValue() instanceof ItemStack&&input==(focus.getMode()==IFocus.Mode.INPUT))
			{
				ItemStack focused = (ItemStack)focus.getValue();
				ItemStack match = items.stream().filter(item -> ItemStack.areItemsEqual(item, focused)&&
						ItemStack.areItemStackTagsEqual(item, focused)).findFirst().orElseGet(() -> helper.getMatch(items, focused));
				if(match!=null)
					shown = match;
			}
			jeiLayout.getItemStacks().set(offset+i, shown.isEmpty()?Collections.emptyList(): Collections.singletonList(shown.copy()));
		}
	}

	@Override
	public void drawInfo(@Nonnull Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		if(jeiLayout!=null)
		{
			long now = Minecraft.getSystemTime();
			if(GuiScreen.isShiftKeyDown())
				lastCycle = now;
			else if(now-lastCycle >= 1000)
			{
				variantIndex = (variantIndex+1)%visibleVariants.size();
				ingredientCycle++;
				lastCycle = now;
				updateDisplayedIngredients();
			}
		}
		IIRecipeLayout recipeLayout = getDisplayedLayout();
		if(recipeLayout==null)
			return;

		//Draw 3D multiblock model in the background
		//drawMultiblock(mc, recipeWidth, recipeHeight);
		for(LayoutComponent component : recipeLayout.getComponents())
			if(component.getType()==IIRecipeLayout.ComponentType.MULTIBLOCK_MODEL)
				drawMultiblock(mc, component);

		//Draw components
		for(LayoutComponent component : recipeLayout.getComponents())
		{
			if(component.getType()==IIRecipeLayout.ComponentType.MULTIBLOCK_MODEL||
					(component.getData()==null&&component.getType()!=IIRecipeLayout.ComponentType.INFO_DISPLAY))
				continue;
			int x = component.getX();
			int y = component.getY();
			int width = component.getWidth();
			int height = component.getHeight();
			String subtype = component.getSubtype();

			if(component.getData()==null||component.getIoType()==IOType.NEUTRAL)
			{
				if(component.getType()==IIRecipeLayout.ComponentType.INFO_DISPLAY)
					drawInfoDisplay(mc, component, x, y, width, height, subtype);
				continue;
			}
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f);
			switch(component.getType())
			{
				case SLOT -> drawSlot(mc, component, x, y, width, height, subtype);
				case DUST_TANK, FLUID_TANK -> drawTank(x, y, width, height);
				default -> {}
			}
			GlStateManager.popMatrix();
		}

		//Draw bottom bar for components
		drawBottomBar(mc, recipeWidth, recipeHeight, recipeLayout.getBottomBarComponents());
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		List<String> tooltip = new ArrayList<>();
		IIRecipeLayout layout = getDisplayedLayout();
		if(layout==null)
			return tooltip;
		for(LayoutComponent component : layout.getComponents())
		{
			if(component.getType()==IIRecipeLayout.ComponentType.FLUID_TANK&&component.getData() instanceof FluidStack&&
					((FluidStack)component.getData()).amount==0&&
					IIMath.isPointInRectangle(component.getX(), component.getY(),
							component.getX()+component.getWidth(), component.getY()+component.getHeight(), mouseX, mouseY))
				tooltip.add(((FluidStack)component.getData()).getLocalizedName()+": 0 mB");
		}
		return tooltip;
	}

	private static class DisplayIngredients
	{
		private final List<List<ItemStack>> itemInputs, itemOutputs;
		private final List<List<FluidStack>> fluidInputs, fluidOutputs;
		private final List<List<DustStack>> dustInputs, dustOutputs;

		private DisplayIngredients(List<List<ItemStack>> itemInputs, List<List<ItemStack>> itemOutputs,
								   List<List<FluidStack>> fluidInputs, List<List<FluidStack>> fluidOutputs,
								   List<List<DustStack>> dustInputs, List<List<DustStack>> dustOutputs)
		{
			this.itemInputs = itemInputs;
			this.itemOutputs = itemOutputs;
			this.fluidInputs = fluidInputs;
			this.fluidOutputs = fluidOutputs;
			this.dustInputs = dustInputs;
			this.dustOutputs = dustOutputs;
		}
	}

	private void drawBottomBar(Minecraft mc, int recipeWidth, int recipeHeight, List<LayoutComponent> bottomBarComponents)
	{
		if(bottomBarComponents.isEmpty())
			return;

		//Draw the bar
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		TextureAtlasSprite label = mc.getTextureMapBlocks()
				.getAtlasSprite((recipe.getRecipeLayout().isEarlyGame()?DecoTextures.LABEL_WOODEN: DecoTextures.LABEL_STEEL).toString());
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(-4, recipeHeight+2-16+1, recipeWidth+8, 16, IIColor.WHITE,
						32, 16, 8, 4, label.getMinU(), label.getMaxU(), label.getMinV(), label.getInterpolatedV(8))
				.finish();
		GlStateManager.popMatrix();

		//Draw components
		for(LayoutComponent component : bottomBarComponents)
		{
			int x = component.getX();
			int y = component.getY();
			int width = component.getWidth();
			int height = component.getHeight();
			String subtype = component.getSubtype();
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f);
			drawInfoDisplay(mc, component, x, y, width, height, subtype);
			GlStateManager.popMatrix();
		}
	}

	private void drawMultiblock(Minecraft mc, LayoutComponent component)
	{
		if(this.machineStack.isEmpty())
			return;
		float scale = Math.min(component.getWidth(), component.getHeight());

		GlStateManager.pushMatrix();
		GlStateManager.translate(component.getX()+(component.getWidth()/2f), component.getY()+(component.getHeight()/2f), scale);
		GlStateManager.enableDepth();
		GlStateManager.scale(scale, -scale, scale);
		mc.getRenderItem().renderItem(this.machineStack, TransformType.GUI);
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();
	}

	private void drawSlot(Minecraft mc, LayoutComponent component, int x, int y, int width, int height, String subtype)
	{
		//Draw vanilla slot
		if(!subtype.contains("frame"))
		{
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR);
			ClientUtils.drawSlot(x+1, y+1, width, height, 128);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			return;
		}
		//Replace subtype
		subtype = subtype.replace("frame", "")
				.replace("_", "");

		//Draw an IE styled slot
		ClientUtils.bindAtlas();
		TextureAtlasSprite label = mc.getTextureMapBlocks()
				.getAtlasSprite(DecoTextures.SLOT_IE_MARKER.toString());
		int labelI = 0;
		switch(subtype)
		{
			case "red", "0" -> labelI = 4;
			case "blue", "1" -> labelI = 5;
			case "green", "2" -> labelI = 6;
			case "yellow", "3" -> labelI = 7;
			case "purple", "4" -> labelI = 8;
			case "cyan", "5" -> labelI = 9;
			case "black", "6" -> labelI = 10;
			case "white", "7" -> labelI = 11;
			case "none" ->
				//noinspection DataFlowIssue
					labelI = 0;
			default ->
			{
				switch(component.getIoType())
				{
					case INPUT:
						labelI = 2;
						break;
					case OUTPUT:
						labelI = 3;
						break;
					default:
						break;
				}
			}
		}

		float labelU = label.getInterpolatedU(labelI%4*4), labelUU = label.getInterpolatedU(labelI%4*4+4);
		//noinspection IntegerDivisionInFloatingPointContext
		float labelV = label.getInterpolatedV((labelI/4)*3), labelVV = label.getInterpolatedV((labelI/4)*3+3);
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE, DecoTextures.SLOT_IE, 32, 32, 4, 4)
				.drawTexColorRect(x-2+width*0.5f, y-1-2, 4, 3, IIColor.WHITE, labelU, labelUU, labelV, labelVV)
				.finish();
	}

	private void drawInfoDisplay(Minecraft mc, LayoutComponent component,
								 int x, int y, int width, int height, String subtype)
	{
		switch(subtype)
		{
			case "note" ->
			{
				String[] parts = IIRecipeLayout.getNoteParts(component);
				Object[] arguments = java.util.Arrays.copyOfRange(parts, 1, parts.length);
				IIClientUtils.fontRegular.drawString(net.minecraft.client.resources.I18n.format(parts[0], arguments), x, y,
						DecoColors.H1.getPackedRGB());
			}
			case "arrow" -> drawProgressArrow(mc, x, y, width, height);
			case "time" -> drawTimeInfo(x, y);
			case "power" -> drawPowerInfo(x, y);
			case "mechanical_power" ->
			{
				if(recipe instanceof RotaryMachineRecipe)
					drawMechanicalPowerInfo(x, y, (RotaryMachineRecipe)recipe);
			}
		}
	}

	private void drawProgressArrow(Minecraft mc, int x, int y, int width, int height)
	{
		//Draw arrow background
		mc.getTextureManager().bindTexture(TEXTURE);
		ClientUtils.drawTexturedRect(x, y, width, height,
				0/256f, width/256f, 0/256f, height/256f);

		//Draw progress overlay (animated)
		long worldTime = mc.world.getTotalWorldTime();
		float progress = (worldTime%100)/100f; //Simple animation
		int progressWidth = (int)(width*progress);

		ClientUtils.drawTexturedRect(x, y, progressWidth, height,
				width/256f, (width+progressWidth)/256f, 0/256f, height/256f);
	}

	private void drawTimeInfo(int x, int y)
	{
		//Draw time icon
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-2, 16, 16, DecoTextures.ICON_TIME)
				.finish();

		//Draw time text
		String timeStr = GuiScreen.isShiftKeyDown()?String.format("%.2f d", recipe.getTotalProcessTime()/24000f): recipe.getTotalProcessTime()+" t";
		ClientUtils.mc().fontRenderer.drawString(timeStr, x+14, y+3, DecoColors.H1.getPackedRGB());
	}

	private void drawPowerInfo(int x, int y)
	{
		//Draw power icon
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-2, 16, 16, DecoTextures.ICON_ENERGY_INPUT)
				.finish();

		//Draw power text
		String powerStr = GuiScreen.isShiftKeyDown()?recipe.getTotalProcessEnergy()+" IF": String.format("%d IF/t", recipe.getEnergyPerTick());
		ClientUtils.mc().fontRenderer.drawString(powerStr, x+14, y+3, DecoColors.H1.getPackedRGB());
	}

	private void drawMechanicalPowerInfo(int x, int y, RotaryMachineRecipe recipe)
	{
		ClientUtils.bindAtlas();
		IIDrawUtils.startTextured()
				.drawTexSprite(x-2, y-1, 16, 16, DecoTextures.ICON_MECH_TORQUE_INPUT)
				.drawTexSprite(x-2+54, y-1, 16, 16, DecoTextures.ICON_MECH_SPEED_INPUT)
				.finish();

		IIClientUtils.fontRegular.drawString(recipe.getTorque()+" IT", x+16, y+3, DecoColors.H1.getPackedRGB());
		IIClientUtils.fontRegular.drawString((GuiScreen.isShiftKeyDown()?recipe.getMaxSpeed(): recipe.getMinSpeed())+" D/t", x+16+54, y+3,
				DecoColors.H1.getPackedRGB());
	}

	private void drawTank(int x, int y, int width, int height)
	{
		//Draw dust tank background
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE,
						DecoTextures.BG_DARK_TANK, 64, 64, 8, 8)
				.drawConnectedTexColorRect(x-1, y-1, width+2, height+2, IIColor.WHITE,
						DecoTextures.COMPONENT_TANK, 64, 64, 16, 16)
				.finish();
	}
}
