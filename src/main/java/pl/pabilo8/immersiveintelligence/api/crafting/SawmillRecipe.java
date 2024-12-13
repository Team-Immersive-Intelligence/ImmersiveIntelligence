package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IISoundAnimation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Pabilo8
 * @since 14-04-2020
 */

//Recipe for Sawmill planks is handled in IIRecipes
public class SawmillRecipe extends MultiblockRecipe implements IIIMultiblockRecipe
{
	public static ArrayList<SawmillRecipe> RECIPES = new ArrayList<>();
	//REFACTOR: 12.09.2024 convert to hex
	private static final IIColor DEFAULT_COLOR = IIColor.fromFloatRGB(0.22392157f, 0.21372549019607842f, 0.15176470588235294f);

	//The tier of the saw required, 1 for cutting wood (bronze), 2 iron, 3 steel, 4 tungsten
	public final IngredientStack itemInput;
	public final ItemStack itemOutput, itemSecondaryOutput;

	public static HashMap<String, ISawblade> toolMap = new HashMap<>();
	int totalProcessTime;
	IISoundAnimation soundAnimation;

	public int getTorque()
	{
		return torque;
	}

	public int getHardness()
	{
		return hardness;
	}

	final int torque;
	final int hardness;
	final IIColor dustColor;

	public SawmillRecipe(ItemStack itemOutput, Object itemInput, ItemStack itemSecondaryOutput, int torque, int time, int hardness, IIColor dustColor)
	{
		this.itemOutput = itemOutput;
		this.itemSecondaryOutput = itemSecondaryOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.torque = torque;
		this.totalProcessTime = time;
		this.hardness = hardness;

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = ListUtils.fromItems(this.itemOutput, this.itemSecondaryOutput);
		this.dustColor = dustColor;

//		0 - 0.1 - grabbing sound
//		0.1 - 1 - cutting,
//		sections each through 0.4/3-1.5/3
//		rolling each 1.5/3 - 2.5/3, landing 3/3

		double cuttingTimeStart = totalProcessTime*0.1;
		double cuttingSection = (totalProcessTime*0.9)/itemOutput.getCount();

		this.soundAnimation = new IISoundAnimation(totalProcessTime)
				.withSound(0.05, IISounds.sawmillInserterStart);

		for(int i = 0; i < itemOutput.getCount(); i++)
			this.soundAnimation
					.withSound(cuttingTimeStart+cuttingSection*i, IISounds.sawmillInserterStart)
					.withRepeatedSound(cuttingTimeStart+cuttingSection*(i+0.13),
							cuttingTimeStart+cuttingSection*(i+0.5), IISounds.sawmillRunning)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.76), IISounds.sawmillWoodTumble)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.83), IISounds.sawmillWoodTumble)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.85), IISounds.sawmillInserterEnd)
					.withSound(cuttingTimeStart+cuttingSection*(i+0.9), IISounds.sawmillWoodTumble);
		this.soundAnimation.compile(totalProcessTime);
	}

	public IISoundAnimation getSoundAnimation()
	{
		return soundAnimation;
	}

	public static SawmillRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, ItemStack itemSecondaryOutput, int torque, int time, int hardness)
	{
		return addRecipe(itemOutput, itemInput, itemSecondaryOutput, torque, time, hardness, DEFAULT_COLOR);
	}

	public static SawmillRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, ItemStack itemSecondaryOutput, int torque, int time, int hardness, IIColor dustColor)
	{
		SawmillRecipe r = new SawmillRecipe(itemOutput, itemInput, itemSecondaryOutput, torque, time, hardness, dustColor);
		RECIPES.add(r);
		return r;
	}

	public static List<SawmillRecipe> removeRecipesForOutput(ItemStack stack)
	{
		List<SawmillRecipe> list = new ArrayList<>();
		Iterator<SawmillRecipe> it = RECIPES.iterator();
		while(it.hasNext())
		{
			SawmillRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.itemOutput, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static SawmillRecipe findRecipe(ItemStack item_input)
	{
		for(SawmillRecipe recipe : RECIPES)
		{
			if(recipe.itemInput.matchesItemStackIgnoringSize(item_input))
			{
				return recipe;
			}
		}
		return null;
	}

	public static void registerSawblade(String name, ISawblade blade)
	{
		toolMap.putIfAbsent(name, blade);
	}

	public static boolean isValidRecipeInput(ItemStack stack)
	{
		for(SawmillRecipe recipe : RECIPES)
			if(recipe.itemInput.matchesItemStack(stack))
				return true;
		return false;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		return writeToNBT().unwrap();
	}

	@Override
	public EasyNBT writeToNBT()
	{
		return EasyNBT.newNBT()
				.withIngredientStack("item_input", itemInput);
	}

	public static SawmillRecipe loadFromNBT(NBTTagCompound nbt)
	{
		IngredientStack item_input = IngredientStack.readFromNBT(nbt.getCompoundTag("item_input"));
		return findRecipe(item_input.stack);
	}

	@Override
	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	public int getTotalProcessTorque()
	{
		return this.torque;
	}

	public IIColor getDustColor()
	{
		return dustColor;
	}
}