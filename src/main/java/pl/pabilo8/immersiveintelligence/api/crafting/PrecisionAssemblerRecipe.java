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
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
public class PrecisionAssemblerRecipe extends IIMultiblockRecipe
{
	private static HashMap<String, PrecisionToolInfo> TOOL_MAP = new HashMap<>();

	public final ItemStack output, trashOutput;
	public final IngredientStack[] inputs;
	public final String[] tools;
	private final String[] animations;
	public final String toolHash;

	public IISoundAnimation soundAnimation;

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
			if(TOOL_MAP.containsKey(split[0]))
				processDuration += TOOL_MAP.get(split[0]).getWorkTime()+(PrecisionAssembler.toolMoveTime*2);
		}

		//Sort the tools for easier detection
		this.toolHash = buildToolHash(tools);
		this.animations = animations;
		this.tools = tools;

		setTimeAndEnergy((int)(processDuration*timeMultiplier), energy);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void loadClientSideContent()
	{

	}

	public String[] getAnimations()
	{
		return animations;
	}

	public static String buildToolHash(String[] tools)
	{
		Arrays.sort(tools, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for(String tool : tools)
			sb.append(tool).append(";");
		return sb.toString();
	}

	public static PrecisionToolInfo[] toolsFromHash(String toolHash)
	{
		return Arrays.stream(toolHash.split(";"))
				.map(PrecisionAssemblerRecipe::getToolByName)
				.filter(Objects::nonNull)
				.toArray(PrecisionToolInfo[]::new);
	}

	public static void registerToolType(IPrecisionTool tool, PrecisionToolInfo toolInfo)
	{
		TOOL_MAP.put(toolInfo.getToolName(), toolInfo);
	}

	@Nullable
	public static PrecisionToolInfo getToolByName(String name)
	{
		return TOOL_MAP.get(name);
	}

	@Nonnull
	public static ItemStack getExampleToolStack(String name)
	{
		if(TOOL_MAP.containsKey(name))
			return TOOL_MAP.get(name).getToolPresentationStack();
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
			if(tools.length > i&&PrecisionAssemblerRecipe.TOOL_MAP.containsKey(tools[i]))
				tool = getExampleToolStack(tools[i]);
			builder.withSlot(50+i*20, 44, tool, IOType.INPUT, "frame"+i);
		}

		//Scheme slot
		builder.withSlot(71, 17, IIContent.itemAssemblyScheme.getSchemeStackForRecipe(this), IOType.INPUT, "frame_none");

		return builder
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}

	@ParametersAreNonnullByDefault
	public static class PrecisionToolInfo
	{
		private String toolName;
		private ResLoc toolModelRes;
		private ItemStack toolPresentationStack;
		private int workTime;

		public PrecisionToolInfo(String toolName, ItemStack toolPresentationStack, int workTime)
		{
			this(toolName, toolPresentationStack, workTime,
					IIReference.RES_BLOCK_MODEL.with("multiblock/precision_assembler/tools/", toolName).withExtension(ResLoc.EXT_OBJ)
			);
		}

		public PrecisionToolInfo(String toolName, ItemStack toolPresentationStack, int workTime, ResLoc toolModelRes)
		{
			this.toolName = toolName;
			this.toolModelRes = toolModelRes;
			this.toolPresentationStack = toolPresentationStack;
			this.workTime = workTime;
		}

		public String getToolName()
		{
			return toolName;
		}

		public ResLoc getToolModelRes()
		{
			return toolModelRes;
		}

		public ItemStack getToolPresentationStack()
		{
			return toolPresentationStack;
		}

		public int getWorkTime()
		{
			return workTime;
		}
	}
}
