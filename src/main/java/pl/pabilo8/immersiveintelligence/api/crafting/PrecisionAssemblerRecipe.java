package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IPrecisionTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
public class PrecisionAssemblerRecipe extends IIMultiblockRecipe
{
	public static HashMap<String, IPrecisionTool> toolMap = new HashMap<>();
	public final ItemStack output, trashOutput;
	public final IngredientStack[] inputs;
	public final String[] tools;
	public final String toolHash;

	public IISoundAnimation soundAnimation;
	public IIAnimation animation;

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

		//Sort the tools for easier detection
		this.toolHash = buildToolHash(tools);
		this.tools = tools;

		setTimeAndEnergy((int)(processDuration*timeMultiplier), energy);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void loadClientSideContent()
	{

	}

	public static String buildToolHash(String[] tools)
	{
		Arrays.sort(tools, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for(String tool : tools)
			sb.append(tool).append(";");
		return sb.toString();
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
			builder.withSlot(50+i*20, 44, tool, IOType.INPUT, "frame"+i);
		}

		//Scheme slot
		builder.withSlot(71, 17, IIContent.itemAssemblyScheme.getSchemeStackForRecipe(this), IOType.INPUT, "frame_none");

		return builder
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
