package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.ComponentType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating recipe GUI layouts
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.12.2025
 */
public class IIRecipeLayoutBuilder
{
	private final List<LayoutComponent> components = new ArrayList<>();
	private final List<LayoutComponent> bottomBarComponents = new ArrayList<>();
	private final int recipeWidth;
	private final int recipeHeight;
	private final boolean earlyGame;

	public IIRecipeLayoutBuilder(int recipeWidth, int recipeHeight)
	{
		this(recipeWidth, recipeHeight, false);
	}

	public IIRecipeLayoutBuilder(int recipeWidth, int recipeHeight, boolean earlyGame)
	{
		this.recipeWidth = recipeWidth;
		this.recipeHeight = recipeHeight;
		this.earlyGame = earlyGame;
	}

	//--- Item slot methods ---//

	public IIRecipeLayoutBuilder withSlot(int x, int y, IngredientStack item, IOType ioType)
	{
		return this.withSlot(x, y, item, ioType, "");
	}

	public IIRecipeLayoutBuilder withSlot(int x, int y, ItemStack item, IOType ioType)
	{
		return this.withSlot(x, y, new IngredientStack(item), ioType, "");
	}

	public IIRecipeLayoutBuilder withSlot(int x, int y, IngredientStack item, IOType ioType, String style)
	{
		components.add(LayoutComponent.builder(ComponentType.SLOT, x, y)
				.ioType(ioType)
				.data(item)
				.subtype(style)
				.build());
		return this;
	}


	public IIRecipeLayoutBuilder withSlot(int x, int y, ItemStack item, IOType ioType, String style)
	{
		components.add(LayoutComponent.builder(ComponentType.SLOT, x, y)
				.ioType(ioType)
				.data(item)
				.subtype(style)
				.build());
		return this;
	}

	public IIRecipeLayoutBuilder withInputSlot(int x, int y, IngredientStack item)
	{
		return withSlot(x, y, item, IOType.INPUT, "frame");
	}

	public IIRecipeLayoutBuilder withInputSlot(int x, int y, ItemStack item)
	{
		return withSlot(x, y, item, IOType.INPUT, "frame");
	}

	public IIRecipeLayoutBuilder withOutputSlot(int x, int y, IngredientStack stack)
	{
		return withSlot(x, y, stack, IOType.OUTPUT, "frame");
	}

	public IIRecipeLayoutBuilder withOutputSlot(int x, int y, ItemStack stack)
	{
		return withSlot(x, y, stack, IOType.OUTPUT, "frame");
	}

	//--- Fluid tank methods ---//
	public IIRecipeLayoutBuilder withFluidTank(int x, int y, @Nullable FluidStack fluid)
	{
		return withFluidTank(x, y, fluid, fluid!=null?IOType.INPUT: IOType.NEUTRAL);
	}

	public IIRecipeLayoutBuilder withFluidTank(int x, int y, @Nullable FluidStack fluid, IOType ioType)
	{
		components.add(LayoutComponent.builder(ComponentType.FLUID_TANK, x, y)
				.ioType(ioType)
				.data(fluid)
				.build());
		return this;
	}

	public IIRecipeLayoutBuilder withInputFluidTank(int x, int y, @Nullable FluidStack fluid)
	{
		return withFluidTank(x, y, fluid, IOType.INPUT);
	}

	public IIRecipeLayoutBuilder withOutputFluidTank(int x, int y, @Nullable FluidStack fluid)
	{
		return withFluidTank(x, y, fluid, IOType.OUTPUT);
	}

	//--- Dust tank methods ---//
	public IIRecipeLayoutBuilder withDustTank(int x, int y, DustStack dust)
	{
		return withDustTank(x, y, dust, IOType.INPUT);
	}

	public IIRecipeLayoutBuilder withDustTank(int x, int y, DustStack dust, IOType ioType)
	{
		components.add(LayoutComponent.builder(ComponentType.DUST_TANK, x, y)
				.ioType(ioType)
				.subtype("dust")
				.data(dust)
				.build());
		return this;
	}

	//--- Progress indicators ---//
	public IIRecipeLayoutBuilder withProgressArrow(int x, int y)
	{
		return withProgressArrow(x, y, 24, 17);
	}

	public IIRecipeLayoutBuilder withProgressArrow(int x, int y, int width, int height)
	{
		components.add(LayoutComponent.builder(ComponentType.INFO_DISPLAY, x, y)
				.size(width, height)
				.subtype("arrow")
				.build());
		return this;
	}

	public IIRecipeLayoutBuilder withProgressBar(int x, int y, int width, int height, boolean vertical)
	{
		components.add(LayoutComponent.builder(ComponentType.INFO_DISPLAY, x, y)
				.size(width, height)
				.subtype(vertical?"vertical_bar": "horizontal_bar")
				.build());
		return this;
	}

	//--- Multiblock model display ---//

	public IIRecipeLayoutBuilder withMultiblockModel(int x, int y, int width, int height, String multiblockName)
	{
		components.add(LayoutComponent.builder(ComponentType.MULTIBLOCK_MODEL, x, y)
				.size(width, height)
				.subtype(multiblockName)
				.build());
		return this;
	}

	public IIRecipeLayoutBuilder withMultiblockModel(int x, int y)
	{
		return withMultiblockModel(x, y, 80, 60, "default");
	}

	//--- Bottom bar information displays ---//
	public IIRecipeLayoutBuilder withBottomBarDisplay(int x, String infoType)
	{
		bottomBarComponents.add(LayoutComponent.builder(ComponentType.INFO_DISPLAY, x, recipeHeight-12)
				.subtype(infoType)
				.build());
		return this;
	}

	public IIRecipeLayoutBuilder withTimeInfo()
	{
		return withBottomBarDisplay(0, "time");
	}

	public IIRecipeLayoutBuilder withPowerInfo()
	{
		return withBottomBarDisplay(recipeWidth/2, "power");
	}

	public IIRecipeLayoutBuilder withMechanicalPowerInfo()
	{
		return withBottomBarDisplay(recipeWidth/3, "mechanical_power");
	}

	//--- Finalization ---//
	public IIRecipeLayout build()
	{
		return new IIRecipeLayout(components, bottomBarComponents, recipeWidth, recipeHeight, earlyGame);
	}
}
