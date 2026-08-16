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
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimationBuilder;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Renders the Precision Assembler and composes tool-slot-specific work animations.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 07.08.2026
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/precision_assembler", clazz = TileEntityPrecisionAssembler.class)
public class PrecisionAssemblerRenderer extends IIMultiblockRenderer<TileEntityPrecisionAssembler>
{
	//Work animation handling
	private AMTCachedModel<TileEntityPrecisionAssembler> model;
	private IntHashMap<SlotItemStates> slotStateCache = new IntHashMap<>();
	private IntHashMap<IIAnimationCachedMap> workAnimations = new IntHashMap<>();
	private IIAnimation workBegin, workFinish;
	private IIAnimation[] slot1, slot2, slot3;
	//Drawn components
	AMTCrossVariantReference<AMTItem>[] items, itemsHeld;
	AMTCrossVariantReference<AMT> schematic;
	AMTCrossVariantReference<AMTItem> itemOutput, itemSchematic;
	private IIAnimationCachedMap drawer1, drawer2, defaultToolPositions;

	@Override
	public void drawAnimated(TileEntityPrecisionAssembler te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		String toolOrder = getToolOrder(te);
		model.getVariant(te, toolOrder);

		//Drawer animations
		drawer1.apply(te.drawer1.getProgress(partialTicks));
		drawer2.apply(te.drawer2.getProgress(partialTicks));

		//Set initial items on the display platform (will be overridden by slot states during animation)
		for(int i = 0; i < 4; i++)
			items[i].get().setStack(getDisplayedItem(te.currentProcess, te, i));
		itemOutput.get().setStack(ItemStack.EMPTY);
		for(AMTCrossVariantReference<AMTItem> held : itemsHeld)
			held.get().setStack(ItemStack.EMPTY);

		//Display blueprint
		boolean displaySchematic = !te.getInventory().get(MultiblockPrecisionAssembler.SLOT_SCHEME).isEmpty();
		schematic.get().setVisible(displaySchematic);
		if(displaySchematic)
		{
			ItemStack output = IIContent.itemAssemblyScheme.getSchemeOutput(te.getInventory().get(MultiblockPrecisionAssembler.SLOT_SCHEME));
			itemSchematic.get().setStack(output);
		}

		//Work animation
		this.defaultToolPositions.apply(0);
		if(te.currentProcess!=null&&!te.toolHash.isEmpty())
		{
			PrecisionAssemblerRecipe recipe = te.currentProcess.recipe;
			//Apply work animation
			IIAnimationCachedMap animation = getAnimationForRecipe(recipe, toolOrder, te.toolHash);
			if(animation!=null)
			{
				float productionProgress = te.getProductionProgress(te.currentProcess, partialTicks);
				animation.apply(productionProgress);
				getSlotStatesForRecipe(recipe, toolOrder).apply(productionProgress);
			}

			//Output slot is now handled by SlotItemStates, so we don't set it here
		}

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
		this.slotStateCache.clearMap();
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

								new AMTItem("held1", header),
								new AMTItem("held2", header),
								new AMTItem("held3", header),

								new AMTParticle("particle1", header),
								new AMTParticle("particle2", header),
								new AMTParticle("particle3", header),

								new AMTItem("print_image", header)
						),
						getPrecisionToolModels(getToolOrder(te))
				).getParts())
				.withHeaderProvider((te) -> getPrecisionToolHeaders(getToolOrder(te)))
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
			this.slot2[i] = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/slot2_stage"+(i+1)));
			this.slot3[i] = AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/slot3_stage"+(i+1)));
		}

		this.items = new AMTCrossVariantReference[]{
				new AMTCrossVariantReference<>("item1", this.model),
				new AMTCrossVariantReference<>("item2", this.model),
				new AMTCrossVariantReference<>("item3", this.model),
				new AMTCrossVariantReference<>("item4", this.model)
		};
		this.itemsHeld = new AMTCrossVariantReference[]{
				new AMTCrossVariantReference<>("held1", this.model),
				new AMTCrossVariantReference<>("held2", this.model),
				new AMTCrossVariantReference<>("held3", this.model)
		};
		this.itemOutput = new AMTCrossVariantReference<>("item5", this.model);

		this.schematic = new AMTCrossVariantReference<>("print", this.model);
		this.itemSchematic = new AMTCrossVariantReference<>("print_image", this.model);
	}

	private SlotItemStates getSlotStatesForRecipe(PrecisionAssemblerRecipe recipe, String toolOrder)
	{
		int hash = Objects.hash(recipe, toolOrder);
		return this.slotStateCache.lookup(hash);
	}

	private IIAnimationCachedMap getAnimationForRecipe(PrecisionAssemblerRecipe recipe, String toolOrder, String toolHash)
	{
		int hash = Objects.hash(recipe, toolOrder);
		if(this.workAnimations.containsItem(hash))
			return this.workAnimations.lookup(hash);

		//Invalid tool hash, don't attempt to create animation
		if(!recipe.toolHash.equals(toolHash))
			return null;

		PrecisionToolInfo[] tools = getToolsBySlot(toolOrder);
		HashMap<String, Integer> orderMap = new HashMap<>();
		for(int i = 0; i < tools.length; i++)
			if(tools[i]!=null)
				orderMap.put(tools[i].getToolName(), i);

		IIAnimationBuilder builder = new IIAnimationBuilder(IIReference.RES_II.with(
				"precision_assembler/production/"+recipe.getName()+"_"+Integer.toHexString(toolOrder.hashCode())
		));

		//Opening animation
		builder.addAnimation(0, PrecisionAssembler.hatchTime, workBegin);
		//Initial tool rotation
		builder.addAnimation(0, recipe.getTotalProcessTime(), AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/tools")));
		int processDuration = PrecisionAssembler.hatchTime;

		//Initial slot stacks from recipe inputs (output slot starts empty)
		ItemStack[] initialSlots = new ItemStack[5];
		for(int i = 0; i < 4; i++)
			initialSlots[i] = i < recipe.inputs.length?recipe.inputs[i].getExampleStack().copy(): ItemStack.EMPTY;
		initialSlots[4] = ItemStack.EMPTY; //output slot

		SlotItemStates slotStates = new SlotItemStates(initialSlots);

		//Current state simulation (5 slots: 0-3 ingredients, 4 output)
		ItemStack[] slotStacks = new ItemStack[5];
		for(int i = 0; i < 5; i++) slotStacks[i] = initialSlots[i].copy();
		ItemStack[] heldStacks = new ItemStack[3];
		Arrays.fill(heldStacks, ItemStack.EMPTY);

		//Pending normalizations (absolute tick -> normalized time)
		List<Runnable> pendingNormalizations = new ArrayList<>();

		//Track whether any operation has occurred (to know when to set output)
		boolean anyOperation = false;

		//Tool animations
		for(String animation : recipe.getAnimations())
		{
			String[] split = animation.split(" ");
			if(split.length < 2)
				continue;

			String toolName = split[0];
			String action = split[1];
			String slotName = split.length > 2?split[2]: "main";

			//Get the target item slot (0-3 = ingredient, 4 = main/output)
			int target;
			switch(slotName)
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
			PrecisionToolInfo tool = PrecisionAssemblerRecipe.getToolByName(toolName);
			if(tool!=null)
			{
				anyOperation = true;
				int workTime = tool.getWorkTime();
				int toolArrivalTime = processDuration+PrecisionAssembler.toolMoveTime;
				int endMoment = toolArrivalTime+workTime;

				//Apply in+out animation
				Integer toolIdxObj = orderMap.get(tool.getToolName());
				if(toolIdxObj!=null)
				{
					int toolIdx = toolIdxObj;
					//Pick and drop transfer the item halfway through their action animation.
					float transferMoment = processDuration+PrecisionAssembler.toolMoveTime*0.33f;
					IIAnimation toolWorkAnimation = new IIAnimationBuilder(
							IIReference.RES_II.with("precision_assembler/tools/generated/"+tool.getToolName()))
							.addAnimation(0, 1, AMTLoader.loadAnimation(IIReference.RES_II.with("precision_assembler/tools/"+tool.getToolName())))
							.renameGroup("inserter", "inserter"+(toolIdx+1))
							.renameGroup("lower", "lower"+(toolIdx+1))
							.renameGroup("upper", "upper"+(toolIdx+1))
							.renameGroup("held", "held"+(toolIdx+1))
							.renameGroup("particle", "particle"+(toolIdx+1))
							.build();

					switch(toolIdx)
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

					//Handle pick/drop actions
					switch(action)
					{
						case "pick":
						{
							if(toolWorkAnimation!=null)
								builder.addAnimation(processDuration, PrecisionAssembler.toolMoveTime, toolWorkAnimation, 1);

							//Source slot (0-4)
							ItemStack picked = slotStacks[target].copy();
							slotStacks[target] = ItemStack.EMPTY;
							heldStacks[toolIdx] = picked.copy();

							pendingNormalizations.add(() -> {
								float t = transferMoment/recipe.getTotalProcessTime();
								slotStates.addSlotChange(target, t, ItemStack.EMPTY);
								slotStates.addHeldChange(toolIdx, t, picked.copy());
							});
							break;
						}
						case "drop":
						{
							if(toolWorkAnimation!=null)
								builder.addAnimation(processDuration, PrecisionAssembler.toolMoveTime, toolWorkAnimation.getReversedAnimation(), 1);

							ItemStack dropped = heldStacks[toolIdx].copy();
							heldStacks[toolIdx] = ItemStack.EMPTY;

							//Only replace if the slot is currently empty
							if(slotStacks[target].isEmpty())
							{
								slotStacks[target] = dropped.copy();
								pendingNormalizations.add(() -> {
									float t = transferMoment/recipe.getTotalProcessTime();
									slotStates.addSlotChange(target, t, dropped.copy());
									slotStates.addHeldChange(toolIdx, t, ItemStack.EMPTY);
								});
							}
							else
							{
								//Slot not empty, just clear held
								pendingNormalizations.add(() -> {
									float t = transferMoment/recipe.getTotalProcessTime();
									slotStates.addHeldChange(toolIdx, t, ItemStack.EMPTY);
								});
							}
							break;
						}
						case "work":
						{
							if(toolWorkAnimation!=null)
								builder.addAnimation(toolArrivalTime, workTime, toolWorkAnimation, 1);
							break;
						}
						default:
							break;
					}
				}

				processDuration += PrecisionAssembler.toolMoveTime*2+workTime;
			}
		}

		//After all operations, set the output slot to the recipe output at the very end of the process
		if(anyOperation)
		{
			pendingNormalizations.add(() -> {
				float t = 1.0f; //end of process
				slotStates.addSlotChange(4, t, recipe.output.copy());
			});
		}

		//Work finish animation
		builder.addAnimation(processDuration, PrecisionAssembler.hatchTime, workFinish);

		//Normalize all pending events using the final total duration
		for(Runnable r : pendingNormalizations)
			r.run();

		//Cache slot states
		slotStateCache.addKey(hash, slotStates);

		//Build and cache animation
		IIAnimationCachedMap animation = IIAnimationCachedMap.create(this.model, builder.build());
		this.workAnimations.addKey(hash, animation);
		return animation;
	}

	private AMTModel getPrecisionToolModels(@Nullable String toolOrder)
	{
		PrecisionToolInfo[] tools = getToolsBySlot(toolOrder);
		List<AMTModel> toolModels = new ArrayList<>();
		for(int i = 0; i < tools.length; i++)
		{
			PrecisionToolInfo tool = tools[i];
			if(tool==null)
				continue;

			AMTModel toolModel = new AMTModel(DefaultVertexFormats.BLOCK, tool.getToolModelRes());
			toolModel.renamePart("inserter", "inserter"+(i+1));
			toolModel.renamePart("lower", "lower"+(i+1));
			toolModel.renamePart("upper", "upper"+(i+1));
			toolModels.add(toolModel);
		}
		return new AMTModel(toolModels.toArray(new AMTModel[0]));
	}

	private AMTModelHeader getPrecisionToolHeaders(@Nullable String toolOrder)
	{
		PrecisionToolInfo[] tools = getToolsBySlot(toolOrder);
		List<AMTModelHeader> toolHeaders = new ArrayList<>();
		for(int i = 0; i < tools.length; i++)
		{
			PrecisionToolInfo tool = tools[i];
			if(tool==null)
				continue;

			AMTModelHeader toolHeader = AMTLoader.loadHeader(tool.getToolModelRes().withExtension(ResLoc.EXT_OBJAMT));
			toolHeader.renameElement("inserter", "inserter"+(i+1));
			toolHeader.renameElement("lower", "lower"+(i+1));
			toolHeader.renameElement("upper", "upper"+(i+1));
			toolHeader.renameElement("held", "held"+(i+1));
			toolHeader.renameElement("particle", "particle"+(i+1));
			toolHeaders.add(toolHeader);
		}
		return new AMTModelHeader(toolHeaders.toArray(new AMTModelHeader[0]));
	}

	@Nullable
	private String getToolOrder(@Nullable TileEntityPrecisionAssembler te)
	{
		if(te==null)
			return null;
		if(!te.toolOrder.isEmpty())
			return te.toolOrder;

		//Fallback for tiles saved before slot order was stored.
		String[] packedTools = te.toolHash.split(";");
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 3; i++)
		{
			if(i > 0)
				builder.append(';');
			if(i < packedTools.length)
				builder.append(packedTools[i]);
		}
		return builder.toString();
	}

	private PrecisionToolInfo[] getToolsBySlot(@Nullable String toolOrder)
	{
		PrecisionToolInfo[] tools = new PrecisionToolInfo[3];
		if(toolOrder==null||toolOrder.isEmpty())
			return tools;

		String[] toolNames = toolOrder.split(";", -1);
		for(int i = 0; i < tools.length&&i < toolNames.length; i++)
			if(!toolNames[i].isEmpty())
				tools[i] = PrecisionAssemblerRecipe.getToolByName(toolNames[i]);
		return tools;
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

	private class SlotItemStates
	{
		private class TimedChange
		{
			final float time;
			final ItemStack stack;

			TimedChange(float time, ItemStack stack)
			{
				this.time = time;
				this.stack = stack;
			}
		}

		private final List<TimedChange>[] slotChanges = new List[5]; //now includes output slot (index 4)
		private final List<TimedChange>[] heldChanges = new List[3];
		private final ItemStack[] initialSlots = new ItemStack[5];

		@SuppressWarnings("unchecked")
		SlotItemStates(ItemStack[] initialSlotStacks)
		{
			for(int i = 0; i < 5; i++)
			{
				slotChanges[i] = new ArrayList<>();
				initialSlots[i] = initialSlotStacks[i].copy();
			}
			for(int i = 0; i < 3; i++)
				heldChanges[i] = new ArrayList<>();
		}

		void addSlotChange(int slot, float time, ItemStack stack)
		{
			if(slot < 0||slot >= 5) return;
			slotChanges[slot].add(new TimedChange(time, stack));
		}

		void addHeldChange(int heldIndex, float time, ItemStack stack)
		{
			if(heldIndex < 0||heldIndex >= 3) return;
			heldChanges[heldIndex].add(new TimedChange(time, stack));
		}

		void apply(float progress)
		{
			//Apply slot changes (0-3 ingredients, 4 output)
			for(int i = 0; i < 5; i++)
			{
				ItemStack stack = initialSlots[i];
				for(TimedChange change : slotChanges[i])
					if(progress >= change.time)
						stack = change.stack;
				if(i < 4)
					items[i].get().setStack(stack);
				else
					itemOutput.get().setStack(stack);
			}
			//Apply held changes
			for(int i = 0; i < 3; i++)
			{
				ItemStack stack = ItemStack.EMPTY;
				for(TimedChange change : heldChanges[i])
					if(progress >= change.time)
						stack = change.stack;
				itemsHeld[i].get().setStack(stack);
			}
		}
	}
}
