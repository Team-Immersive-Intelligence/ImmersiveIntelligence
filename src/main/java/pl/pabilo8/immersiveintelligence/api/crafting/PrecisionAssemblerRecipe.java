package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIAssemblyScheme;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
//REFACTOR: 06.12.2025 move to new system fully
public class PrecisionAssemblerRecipe extends IIMultiblockRecipe
{
	public static HashMap<String, IPrecisionTool> toolMap = new HashMap<>();
	public static ArrayList<PrecisionAssemblerRecipe> recipeList = new ArrayList<>();
	public ItemStack output;
	public ItemStack trashOutput;
	public IngredientStack[] inputs;
	public String[] tools;
	public String[] animations;

	public PrecisionAssemblerRecipe(ItemStack itemOutput, ItemStack trash, Object[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
	{
		super(itemOutput, itemInputs);
		this.output = itemOutput;
		this.trashOutput = trash;

		this.inputs = new IngredientStack[itemInputs.length];
		for(int io = 0; io < itemInputs.length; io++)
			this.inputs[io] = ApiUtils.createIngredientStack(itemInputs[io]);

		//Open time + close time
		int processDuration = 2*PrecisionAssembler.hatchTime;
		//Tool times
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

		setTimeAndEnergy(processDuration, energy);
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
				if(!recipe.inputs[i].matches(item_input[i]))
				{
					jawohl = false;
					break;
				}


			if(jawohl)
				if(tools.length < recipe.tools.length)
					continue;


			if(jawohl)
			{
				ArrayList<String> neededTools = new ArrayList<>(Arrays.asList(recipe.tools));

				ArrayList<String> availableTools = new ArrayList<>();
				for(ItemStack toolstack : tools)
					if(!toolstack.isEmpty()&&toolstack.getItem() instanceof IPrecisionTool)
						availableTools.add(((IPrecisionTool)toolstack.getItem()).getPrecisionToolType(toolstack));

				for(String tool : neededTools)
				{
					if(!jawohl)
						break;

					if(availableTools.contains(tool))
						availableTools.remove(tool);
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

	public static PrecisionAssemblerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		//Not needed?
		return null;
	}

	public static void registerToolType(String name, IPrecisionTool tool)
	{
		toolMap.put(name, tool);
	}

	public static ItemStack getExampleToolStack(String name)
	{
		if(toolMap.containsKey(name))
			return toolMap.get(name).getToolPresentationStack(name);
		return ItemStack.EMPTY;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(156, 74);

		//Input Slots
		builder.withSlot(20, 20, inputs[0], IOType.INPUT, "frame");
		for(int i = 1; i < 4; i++)
			builder.withSlot(0, (i-1)*20, inputs.length > i?inputs[i]: new IngredientStack(ItemStack.EMPTY), IOType.INPUT, "frame_none");

		builder.withSlot(134, 9, output, IOType.OUTPUT, "frame");
		builder.withSlot(134, 9+20, trashOutput, IOType.OUTPUT, "frame_none");

		//Tool slots
		for(int i = 0; i < 3; i++)
		{
			ItemStack tool = ItemStack.EMPTY;
			if(tools.length > i&&PrecisionAssemblerRecipe.toolMap.containsKey(tools[i]))
				tool = PrecisionAssemblerRecipe.toolMap.get(tools[i])
						.getToolPresentationStack(tools[i]);
			builder.withSlot(54+i*20, 44, tool, IOType.INPUT, "frame"+i);
		}

		//Scheme slot
		builder.withSlot(71, 17, IIContent.itemAssemblyScheme.getStackForRecipe(this), IOType.INPUT, "frame_none");

		return builder
				.withTimeInfo()
				.withPowerInfo()
				.build();
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
}
