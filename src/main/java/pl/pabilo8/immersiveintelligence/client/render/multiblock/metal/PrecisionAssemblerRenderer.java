package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe.PrecisionToolInfo;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimationBuilder;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 01.04.2026
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/precision_assembler", clazz = TileEntityPrecisionAssembler.class)
public class PrecisionAssemblerRenderer extends IIMultiblockRenderer<TileEntityPrecisionAssembler>
{
	//Work animation handling
	private AMTCachedModel<TileEntityPrecisionAssembler> model;
	private IntHashMap<IIAnimationCachedMap> workAnimations = new IntHashMap<>();
	private IIAnimation workBegin, workFinish;
	private IIAnimation[] slot1, slot2, slot3;
	//Drawn components
	AMTCrossVariantReference<AMTItem>[] items;
	AMTCrossVariantReference<AMTItem> itemOutput;
	private IIAnimationCachedMap drawer1, drawer2, defaultToolPositions;

	@Override
	public void drawAnimated(TileEntityPrecisionAssembler te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.getVariant(te, te.toolHash);

		//Drawer animations
		drawer1.apply(te.drawer1.getProgress(partialTicks));
		drawer2.apply(te.drawer2.getProgress(partialTicks));

		//Set recipe items on the display platform
		for(int i = 0; i < 4; i++)
			items[i].get().setStack(getDisplayedItem(te.currentProcess, te, i));

		//Work animation
		this.defaultToolPositions.apply(0);
		if(te.currentProcess!=null&&!te.toolHash.isEmpty())
		{
			PrecisionAssemblerRecipe recipe = te.currentProcess.recipe;
			//Apply work animation
			IIAnimationCachedMap animation = getAnimationForRecipe(recipe, te.toolHash);
			if(animation!=null)
				animation.apply(te.getProductionProgress(te.currentProcess, partialTicks));

			itemOutput.get().setStack(te.inventory.get(MultiblockPrecisionAssembler.SLOT_OUTPUT));
		}
		else
			itemOutput.get().setStack(ItemStack.EMPTY);

		applyStandardMirroring(te, true);
		model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.getVariant(null).render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.workAnimations.clearMap();
		this.model = AMTCachedModelBuilder.startTileEntityModel(TileEntityPrecisionAssembler.class)
				.withModel(model)
				.withHeader(AMTLoader.loadHeader(model))
				.withModelProvider((te, header) -> new AMTModel(
						new AMTModel(
								new AMTItem("item1", header),
								new AMTItem("item2", header),
								new AMTItem("item3", header),
								new AMTItem("item4", header),
								new AMTItem("item5", header),
								new AMTItem("print_image", header)
						),
						getPrecisionToolModels(te==null?null: te.toolHash)
				).getParts())
				.withHeaderProvider((te) -> getPrecisionToolHeaders(te==null?null: te.toolHash))
				.build();

		this.drawer1 = IIAnimationCachedMap.create(this.model, IIReference.RES_II.with("precision_assembler/drawer1"));
		this.drawer2 = IIAnimationCachedMap.create(this.model, IIReference.RES_II.with("precision_assembler/drawer2"));
		this.defaultToolPositions = IIAnimationCachedMap.create(this.model, IIReference.RES_II.with("precision_assembler/tools"));

		this.workBegin = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/start"));
		this.workFinish = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/finish"));

		this.slot1 = new IIAnimation[5];
		this.slot2 = new IIAnimation[5];
		this.slot3 = new IIAnimation[5];
		for(int i = 0; i < 5; i++)
		{
			this.slot1[i] = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/slot1_stage"+(i+1)));
			this.slot2[i] = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/slot2_stage"+(i+2)));
			this.slot3[i] = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/slot3_stage"+(i+3)));
		}

		this.items = new AMTCrossVariantReference[]{
				new AMTCrossVariantReference<>("item1", this.model),
				new AMTCrossVariantReference<>("item2", this.model),
				new AMTCrossVariantReference<>("item3", this.model),
				new AMTCrossVariantReference<>("item4", this.model)
		};
		this.itemOutput = new AMTCrossVariantReference<>("item5", this.model);
	}

	private IIAnimationCachedMap getAnimationForRecipe(PrecisionAssemblerRecipe recipe, String toolHash)
	{
		int hash = Objects.hash(recipe, toolHash);
		if(this.workAnimations.containsItem(hash))
			return this.workAnimations.lookup(hash);
		//Invalid tool hash, don't attempt to create animation
		if(!recipe.toolHash.equals(toolHash))
			return null;

		PrecisionToolInfo[] tools = PrecisionAssemblerRecipe.toolsFromHash(toolHash);
		HashMap<String, Integer> orderMap = IntStream.range(0, tools.length).boxed()
				.collect(Collectors.toMap(i -> tools[i].getToolName(), i -> i, (a, b) -> b, HashMap::new));

		IIAnimationBuilder builder = new IIAnimationBuilder(IIReference.RES_II.with("precision_assembler/production/"+recipe.getName()+"_"+toolHash));

		//Opening animation
		builder.addAnimation(0, PrecisionAssembler.hatchTime, workBegin);
		//Initial tool rotation
		builder.addAnimation(0, 1, AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/tools")));
		int processDuration = PrecisionAssembler.hatchTime;

		//Tool animations
		for(String animation : recipe.getAnimations())
		{
			String[] split = animation.split(" ");
			if(split.length < 2)
				continue;

			//Get the inserter target item slot
			int target;
			switch(split[1])
			{
				case "first":
					target = 0;
					break;
				case "second":
					target = 1;
					break;
				case "third":
					target = 2;
					break;
				case "forth":
					target = 3;
					break;
				case "main":
				default:
					target = 4;
					break;
			}

			//Get the tool index and apply tool animation
			PrecisionToolInfo tool = PrecisionAssemblerRecipe.getToolByName(split[0]);
			if(tool!=null)
			{
				int workTime = tool.getWorkTime();
				int endMoment = workTime+PrecisionAssembler.toolMoveTime+tool.getWorkTime();
				//Apply in+out animation
				switch(orderMap.get(tool.getToolName()))
				{
					case 0:
						builder.addAnimation(processDuration, PrecisionAssembler.toolMoveTime, slot1[target]);
						builder.addAnimation(endMoment, PrecisionAssembler.toolMoveTime, slot1[target].getReversedAnimation());
						break;
					case 1:
						builder.addAnimation(processDuration, PrecisionAssembler.toolMoveTime, slot2[target]);
						builder.addAnimation(endMoment, PrecisionAssembler.toolMoveTime, slot2[target].getReversedAnimation());
						break;
					case 2:
						builder.addAnimation(processDuration, PrecisionAssembler.toolMoveTime, slot3[target]);
						builder.addAnimation(endMoment, PrecisionAssembler.toolMoveTime, slot3[target].getReversedAnimation());
						break;
				}

				//Apply tool-specific work animation, if it exists
				/*if(workTime!=0)
					builder.addAnimation(processDuration, PrecisionAssembler.toolMoveTime, slot2[target], 1);*/

				processDuration += PrecisionAssembler.toolMoveTime*2+tool.getWorkTime();
			}

		}

		//Work finish animation
		builder.addAnimation(processDuration, PrecisionAssembler.hatchTime, workFinish);

		IIAnimationCachedMap animation = IIAnimationCachedMap.create(this.model, builder.build());
		this.workAnimations.addKey(hash, animation);
		return animation;
	}

	private AMTModel getPrecisionToolModels(String toolHash)
	{
		if(toolHash==null)
			return new AMTModel();
		PrecisionToolInfo[] tools = PrecisionAssemblerRecipe.toolsFromHash(toolHash);
		AMTModel[] toolModels = new AMTModel[tools.length];
		for(int i = 0; i < tools.length; i++)
		{
			toolModels[i] = new AMTModel(DefaultVertexFormats.BLOCK, tools[i].getToolModelRes());
			toolModels[i].renamePart("inserter", "inserter"+(i+1));
			toolModels[i].renamePart("lower", "lower"+(i+1));
			toolModels[i].renamePart("upper", "upper"+(i+1));
		}
		return new AMTModel(toolModels);
	}

	private AMTModelHeader getPrecisionToolHeaders(@Nullable String toolHash)
	{
		if(toolHash==null)
			return new AMTModelHeader();
		PrecisionToolInfo[] tools = PrecisionAssemblerRecipe.toolsFromHash(toolHash);
		AMTModelHeader[] toolHeaders = new AMTModelHeader[tools.length];
		for(int i = 0; i < tools.length; i++)
		{
			toolHeaders[i] = AMTLoader.loadHeader(tools[i].getToolModelRes().withExtension(ResLoc.EXT_OBJAMT));
			toolHeaders[i].renameElement("inserter", "inserter"+(i+1));
			toolHeaders[i].renameElement("lower", "lower"+(i+1));
			toolHeaders[i].renameElement("upper", "upper"+(i+1));
		}
		return new AMTModelHeader(toolHeaders);
	}

	private ItemStack getDisplayedItem(@Nullable IIMultiblockProcess<PrecisionAssemblerRecipe> process, TileEntityPrecisionAssembler te, int id)
	{
		if(process==null)
			return te.inventory.get(MultiblockPrecisionAssembler.SLOT_INGREDIENT1+id);
		else
		{
			PrecisionAssemblerRecipe recipe = process.recipe;
			if(id > recipe.inputs.length-1)
				return ItemStack.EMPTY;
			else if(!recipe.inputs[id].matchesItemStack(te.inventory.get(MultiblockPrecisionAssembler.SLOT_INGREDIENT1+id)))
				return recipe.inputs[id].getExampleStack();
			else
				return te.inventory.get(MultiblockPrecisionAssembler.SLOT_INGREDIENT1+id);
		}
	}
}
