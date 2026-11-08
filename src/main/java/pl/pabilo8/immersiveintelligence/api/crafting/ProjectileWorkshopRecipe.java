package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeDisplay;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockProjectileWorkshop;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 08.10.2026
 * @since 08.06.2025
 **/
public class ProjectileWorkshopRecipe extends IIMultiblockRecipe
{
	public static final String FILLING_RECIPE_NAME = "core_filling";
	public static final String CORE_PRODUCTION_RECIPE_PREFIX = "core_production_";
	public static final ProjectileWorkshopRecipe CORE_FILLING = new ProjectileWorkshopRecipe();

	@Nullable
	public IAmmoTypeItem<?, ?> ammo;
	public boolean isFilling;
	public final boolean advanced;
	public ItemStack effect = ItemStack.EMPTY, ingredient = ItemStack.EMPTY;
	private IngredientStack material;

	/**
	 * Single hard-coded recipe used by Core Filler mode.
	 */
	private ProjectileWorkshopRecipe()
	{
		super(FILLING_RECIPE_NAME);
		this.isFilling = true;
		this.advanced = false; //The filler checks the input ammo type when creating a process.

		this.setTimeAndEnergy(
				ProjectileWorkshop.fillingTime,
				ProjectileWorkshop.fillingEnergyUsage
		);
	}

	/**
	 * Production recipe
	 *
	 * @param ammo     Ammo to be produced
	 * @param coreType Core type
	 */
	public ProjectileWorkshopRecipe(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType coreType)
	{
		super(core, coreType);
		this.ammo = ammo;
		this.isFilling = false;
		this.advanced = ammo.requiresAdvancedAssembly();

		this.material = new IngredientStack(core.getMaterial());
		this.material.inputSize = ammo.getCoreMaterialNeeded();
		this.ingredient = material.getExampleStack().copy();
		this.ingredient.setCount(material.inputSize);
		this.effect = ammo.getAmmoCoreStack(core, coreType);

		this.setTimeAndEnergy(
				ProjectileWorkshop.productionTime*ammo.getCaliber(),
				ProjectileWorkshop.productionEnergyUsage*ammo.getCaliber()
		);
		setName(getProductionName(ammo, core, coreType));
	}

	public ItemStack getEffect()
	{
		return effect.copy();
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		if(isFilling)
			return null;
		return new IIRecipeLayoutBuilder(144, 64)
				.withInputSlot(10, 21, material).withOutputSlot(118, 21, effect)
				.withMultiblockModel(30, -2).withTimeInfo().withPowerInfo().build();
	}

	public static String getProductionName(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType type)
	{
		return CORE_PRODUCTION_RECIPE_PREFIX+ammo.getName()+"|"+core.getName()+"|"+type.getName();
	}

	@Override
	public boolean matchesSubCategory(String category)
	{
		return !isFilling&&!advanced;
	}

	public static void registerCoreRecipes()
	{
		for(IAmmoTypeItem<?, ?> ammo : AmmoRegistry.getAllAmmoItems())
			if(!ammo.requiresAdvancedAssembly())
				for(AmmoCore core : AmmoRegistry.getAllCores())
					if(core.getMaterial().getStackList().stream().anyMatch(stack -> !stack.isEmpty()))
						for(CoreType type : ammo.getAllowedCoreTypes())
							if(IIMultiblockRecipe.getRecipe(ProjectileWorkshopRecipe.class, getProductionName(ammo, core, type))==null)
								new ProjectileWorkshopRecipe(ammo, core, type);
	}

	/**
	 * One panel per ammunition item, cycling its registered production variants.
	 */
	@SideOnly(Side.CLIENT)
	public static List<IIMultiblockRecipe> getProductionDisplays()
	{
		Map<IAmmoTypeItem<?, ?>, List<IIRecipeLayout>> grouped = new LinkedHashMap<>();
		for(ProjectileWorkshopRecipe recipe : IIMultiblockRecipe.getRecipes(ProjectileWorkshopRecipe.class))
			if(!recipe.isFilling&&!recipe.advanced&&recipe.ammo!=null&&
					!IIRecipeLayout.getDisplayItems(recipe.material).isEmpty())
				grouped.computeIfAbsent(recipe.ammo, ammo -> new ArrayList<>()).add(recipe.getRecipeLayout());

		List<IIMultiblockRecipe> displays = new ArrayList<>();
		for(Map.Entry<IAmmoTypeItem<?, ?>, List<IIRecipeLayout>> entry : grouped.entrySet())
		{
			IAmmoTypeItem<?, ?> ammo = entry.getKey();
			displays.add(new IIRecipeDisplay("production_"+ammo.getName(),
					ProjectileWorkshop.productionTime*ammo.getCaliber(),
					ProjectileWorkshop.productionEnergyUsage*ammo.getCaliber(), entry.getValue()));
		}
		return displays;
	}

	/**
	 * One iron heavy-artillery example per component. Extra fills repeat this cost.
	 */
	@SideOnly(Side.CLIENT)
	public static List<IIMultiblockRecipe> getFillingDisplays()
	{
		List<IIMultiblockRecipe> displays = new ArrayList<>();
		IAmmoTypeItem<?, ?> ammo = IIContent.itemAmmoHeavyArtillery;
		CoreType type = CoreType.CANISTER;
		for(AmmoComponent component : AmmoRegistry.getAllComponents())
		{
			IngredientStack source = component.getMaterial();
			if(!component.matchesBullet(ammo)||component.getSlotsTaken() > type.getComponentSlots()||
					(source.fluid==null&&source.getStackList().stream().noneMatch(stack -> !stack.isEmpty())))
				continue;
			ItemStack input = ammo.getAmmoCoreStack(IIContent.ammoCoreIron, type);
			ItemStack output = input.copy();
			ammo.addComponents(output, component, new NBTTagCompound());
			IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(144, 84)
					.withInputSlot(10, 6, input).withOutputSlot(118, 21, output)
					.withMultiblockModel(54, 0, 56, 48, "default").withTimeInfo().withPowerInfo()
					.withInfo(4, 60, 136, component.isSpaciousComponent()?
							"desc.immersiveintelligence.recipe.filling_inserter": "desc.immersiveintelligence.recipe.filling");
			int units = ammo.getComponentUsed();
			if(source.fluid!=null)
				builder.withInputFluidTank(32, 0, new FluidStack(source.fluid,
						units*Math.max(1, ProjectileWorkshop.componentTankCapacity/ProjectileWorkshop.componentCapacity)));
			else
			{
				IngredientStack items = new IngredientStack(source);
				items.inputSize = (units+MultiblockProjectileWorkshop.COMPONENT_AMOUNT_PER_ITEM-1)/MultiblockProjectileWorkshop.COMPONENT_AMOUNT_PER_ITEM;
				builder.withInputSlot(10, 30, items);
			}
			displays.add(new IIRecipeDisplay("filling_"+component.getName(),
					ProjectileWorkshop.fillingTime*ammo.getCaliber(), ProjectileWorkshop.fillingEnergyUsage*ammo.getCaliber(), builder.build()));
		}
		return displays;
	}

}
