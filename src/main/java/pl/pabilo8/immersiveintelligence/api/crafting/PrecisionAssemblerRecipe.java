package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;

import java.util.*;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class PrecisionAssemblerRecipe extends MultiblockRecipe
{
	public float timeModifier;

	public ItemStack output;
	public ItemStack trashOutput;
	public IngredientStack[] inputs;
	public String[] tools;
	public String[] animations;

	public static HashMap<String, IPrecisionTool> toolMap = new HashMap<>();
	public static ArrayList<PrecisionAssemblerRecipe> recipeList = new ArrayList<>();

	int totalProcessTime;
	int totalProcessEnergy;

	public PrecisionAssemblerRecipe(ItemStack itemOutput, ItemStack trash, Object[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
	{
		this.output = itemOutput;
		this.trashOutput = trash;

		this.inputs = new IngredientStack[itemInputs.length];
		for(int io = 0; io < itemInputs.length; io++)
			this.inputs[io] = ApiUtils.createIngredientStack(itemInputs[io]);

		//Open time + close time
		int processDuration = 2*PrecisionAssembler.hatchTime;

		for(String animation : animations)
		{
			String[] split = animation.split(" ");

			if(split.length < 2||split[0]==null)
				continue;

			if(toolMap.containsKey(split[0]))
				processDuration += toolMap.get(split[0]).getWorkTime(split[0]);
		}

		this.tools = tools;
		this.animations = animations;

		this.totalProcessEnergy = (int)Math.floor((float)energy);
		this.timeModifier = timeMultiplier;
		this.totalProcessTime = (int)Math.floor((float)processDuration*this.timeModifier);

		this.inputList = Lists.newArrayList(this.inputs);
		this.outputList = ListUtils.fromItem(this.output);
		this.outputList.add(this.trashOutput);

	}

	public static PrecisionAssemblerRecipe addRecipe(ItemStack itemOutput, ItemStack trash, Object[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
	{
		PrecisionAssemblerRecipe r = new PrecisionAssemblerRecipe(itemOutput, trash, itemInputs, tools, animations, energy, timeMultiplier);
		recipeList.add(r);
		return r;
	}

	public static List<PrecisionAssemblerRecipe> removeRecipesForOutput(ItemStack stack)
	{
		List<PrecisionAssemblerRecipe> list = new ArrayList<>();
		Iterator<PrecisionAssemblerRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			PrecisionAssemblerRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.output, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static PrecisionAssemblerRecipe findRecipe(ItemStack[] item_input, ItemStack scheme, ItemStack[] tools)
	{
		if(!(scheme.getItem() instanceof ItemIIAssemblyScheme))
			return null;

		for(PrecisionAssemblerRecipe recipe : recipeList)
		{
			if(!Objects.equals(IIContent.itemAssemblyScheme.getRecipeForStack(scheme), recipe))
				continue;

			//Whether it should be accepted or not.
			boolean jawohl = true;

			if(recipe.inputs.length > item_input.length)
				continue;

			for(int i = 0; i < recipe.inputs.length; i += 1)
			{
				if(!recipe.inputs[i].matches(item_input[i]))
				{
					jawohl = false;
					break;
				}
			}


			if(jawohl)
				if(tools.length < recipe.tools.length)
					continue;


			if(jawohl)
			{
				ArrayList<String> neededTools = new ArrayList<>(Arrays.asList(recipe.tools));

				ArrayList<String> availableTools = new ArrayList<>();
				for(ItemStack toolstack : tools)
				{
					if(!toolstack.isEmpty()&&toolstack.getItem() instanceof IPrecisionTool)
						availableTools.add(((IPrecisionTool)toolstack.getItem()).getPrecisionToolType(toolstack));
				}

				for(String tool : neededTools)
				{
					if(!jawohl)
						break;

					if(availableTools.contains(tool))
					{
						availableTools.remove(tool);
					}
					else
						jawohl = false;
				}


			}

			//Whether ze recipe ist richtig.
			if(jawohl)
				return recipe;
		}
		return null;
	}

	public static List<PrecisionAssemblerRecipe> findIncompleteBathingRecipe(ItemStack[] item_input, ItemStack scheme)
	{
		if(item_input==null||item_input.length==0||scheme==null)
			return null;
		List<PrecisionAssemblerRecipe> list = Lists.newArrayList();

		for(PrecisionAssemblerRecipe recipe : recipeList)
		{
			if(scheme.getItem() instanceof ItemIIAssemblyScheme&&IIContent.itemAssemblyScheme.getRecipeForStack(scheme)!=null&&IIContent.itemAssemblyScheme.getRecipeForStack(scheme).equals(recipe))
			{
				list.add(recipe);
				continue;
			}

			for(int i = 0; i < recipe.inputs.length; i += 1)
			{
				if(recipe.inputs[i].matches(item_input[i]))
				{
					list.add(recipe);
					break;
				}
			}
		}

		return list;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(IngredientStack ingr : this.inputs)
			list.appendTag(ingr.writeToNBT(new NBTTagCompound()));
		nbt.setTag("inputs", list);
		return nbt;
	}

	public static PrecisionAssemblerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		//Not needed?
		return null;
	}

	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}

	public static void registerToolType(String name, IPrecisionTool tool)
	{
		toolMap.put(name, tool);
	}

	public static ItemStack getExampleToolStack(String name)
	{
		if(toolMap.containsKey(name))
		{
			return toolMap.get(name).getToolPresentationStack(name);
		}
		return ItemStack.EMPTY;
	}
}