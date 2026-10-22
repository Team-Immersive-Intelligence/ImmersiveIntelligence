package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Vulcanizer;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockVulcanizer;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.IIEnergyStorage;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Processes rubber recipes in two alternating moulds.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.10.2026
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityVulcanizer extends TileEntityMultiblockProductionMulti<TileEntityVulcanizer, VulcanizerRecipe>
		implements IPlayerInteraction, ITactileListener
{
	private static final ResLoc[] WORK_ANIMATIONS = {
			IIReference.RES_II.with("vulcanizer/work1"),
			IIReference.RES_II.with("vulcanizer/work2")
	};

	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public ItemStack mold = ItemStack.EMPTY;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public boolean nextWork2 = false;
	private TactileManager tactileManager;
	private ResLoc[] tactileAnimations;
	private float[] tactileTimes;

	private IItemHandler insertionHandlerRubber, insertionHandlerCompound, insertionHandlerSulfur;
	private SoundHandler soundHandler;
	private int[] lastSoundTimes;

	public TileEntityVulcanizer()
	{
		super(MultiblockVulcanizer.INSTANCE);

		this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
		this.energyStorage = new IIEnergyStorage(Vulcanizer.energyCapacity);

		this.insertionHandlerRubber = new IEInventoryHandler(1, this, MultiblockVulcanizer.SLOT_RUBBER, true, false);
		this.insertionHandlerCompound = new IEInventoryHandler(1, this, MultiblockVulcanizer.SLOT_COMPOUND, true, false);
		this.insertionHandlerSulfur = new IEInventoryHandler(1, this, MultiblockVulcanizer.SLOT_SULFUR, true, false);
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(!world.isRemote)
		{
			tactileManager = new TactileManager(multiblock, this);
			tactileAnimations = new ResLoc[2];
			tactileTimes = new float[2];
		}
		else
		{
			soundHandler = new SoundHandler(this);
			lastSoundTimes = new int[]{-1, -1};
		}
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		insertionHandlerRubber = insertionHandlerCompound = insertionHandlerSulfur = null;
		tactileManager = null;
		tactileAnimations = null;
		tactileTimes = null;
		soundHandler = null;
		lastSoundTimes = null;
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(isDummy()||!nbt.hasKey("processQueue"))
			return;

		//Assign stable mould sides to processes saved before the animation update.
		boolean work2 = false, migrated = false;
		for(IIMultiblockProcess<VulcanizerRecipe> process : processQueue)
		{
			if(process.processData.hasKey("work2"))
				work2 = process.processData.getBoolean("work2");
			else
			{
				process.processData.withBoolean("work2", work2);
				migrated = true;
			}
			work2 = !work2;
		}
		if(migrated)
			nextWork2 = work2;
	}

	/**
	 * Returns the saved mould side, independent of the process queue position.
	 */
	public int getWorkAnimation(IIMultiblockProcess<VulcanizerRecipe> process)
	{
		return process.processData.getBoolean("work2")?1: 0;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();
		if(!world.isRemote)
			updateTactiles();
		else
			for(IIMultiblockProcess<VulcanizerRecipe> process : processQueue)
			{
				int side = getWorkAnimation(process);
				int time = (int)(process.ticks/process.maxTicks*MultiblockVulcanizer.ANIMATION_DURATION);
				if(time!=lastSoundTimes[side])
				{
					MultiblockVulcanizer.INSTANCE.workAnimation.handleSounds(soundHandler, time, 1f);
					lastSoundTimes[side] = time;
				}
			}
	}

	private void updateTactiles()
	{
		//Apply the oldest cycle last so it controls the shared platform.
		for(int i = 0; i < tactileAnimations.length; i++)
		{
			int processIndex = processQueue.size()-1-i;
			if(processIndex >= 0)
			{
				IIMultiblockProcess<VulcanizerRecipe> process = processQueue.get(processIndex);
				tactileAnimations[i] = WORK_ANIMATIONS[getWorkAnimation(process)];
				tactileTimes[i] = process.maxTicks <= 0?0: MathHelper.clamp(process.ticks/process.maxTicks, 0, 1);
			}
			else
			{
				tactileAnimations[i] = i==0?WORK_ANIMATIONS[nextWork2?1: 0]: null;
				tactileTimes[i] = 0;
			}
		}
		tactileManager.update(tactileAnimations, tactileTimes);
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		return switch(i)
		{
			case MultiblockVulcanizer.SLOT_RUBBER -> IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
					.anyMatch(r -> r.input.matchesItemStackIgnoringSize(itemStack));
			case MultiblockVulcanizer.SLOT_COMPOUND -> IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
					.anyMatch(r -> r.compoundInput.matchesItemStackIgnoringSize(itemStack));
			case MultiblockVulcanizer.SLOT_SULFUR -> IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
					.anyMatch(r -> r.sulfurInput.matchesItemStackIgnoringSize(itemStack));
			default -> false;
		};
	}

	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		TileEntityVulcanizer master = master();
		if(master!=null&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(isPOI("input_sulfur"))
				return (T)master.insertionHandlerSulfur;
			if(isPOI("input_compound"))
				return (T)master.insertionHandlerCompound;
			if(isPOI("input_rubber"))
				return (T)master.insertionHandlerRubber;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public NonNullList<ItemStack> getDroppedItems()
	{
		return NonNullList.from(
				ItemStack.EMPTY,
				inventory.get(MultiblockVulcanizer.SLOT_RUBBER),
				inventory.get(MultiblockVulcanizer.SLOT_COMPOUND),
				inventory.get(MultiblockVulcanizer.SLOT_SULFUR),
				mold
		);
	}

	@Override
	public float getMinProductionOffset()
	{
		return MultiblockVulcanizer.PRODUCTION_OFFSET;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 2;
	}

	@Override
	protected IIMultiblockProcess<VulcanizerRecipe> findNewProductionProcess()
	{
		if(mold.isEmpty())
			return null;

		ComparableItemStack comparableMold = new ComparableItemStack(mold);
		ItemStack rubber = inventory.get(MultiblockVulcanizer.SLOT_RUBBER);
		ItemStack compound = inventory.get(MultiblockVulcanizer.SLOT_COMPOUND);
		ItemStack sulfur = inventory.get(MultiblockVulcanizer.SLOT_SULFUR);
		for(VulcanizerRecipe recipe : IIMultiblockRecipe.getRecipes(VulcanizerRecipe.class))
		{
			if(!recipe.mold.equals(comparableMold)||!recipe.input.matchesItemStack(rubber)
					||!recipe.compoundInput.matchesItemStack(compound)||!recipe.sulfurInput.matchesItemStack(sulfur))
				continue;

			rubber.shrink(recipe.input.inputSize);
			compound.shrink(recipe.compoundInput.inputSize);
			sulfur.shrink(recipe.sulfurInput.inputSize);
			IIMultiblockProcess<VulcanizerRecipe> process = new IIMultiblockProcess<>(recipe)
					.withNBT(nbt -> nbt.withBoolean("work2", nextWork2));
			nextWork2 = !nextWork2;
			markDirty();
			return process;
		}
		return null;
	}

	@Override
	protected IIMultiblockProcess<VulcanizerRecipe> getProcessByName(String name)
	{
		VulcanizerRecipe recipe = IIMultiblockRecipe.getRecipe(VulcanizerRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<VulcanizerRecipe> process, boolean simulate)
	{
		if(process.maxTicks <= 0)
			return 0;
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		return energyStorage.tryConsumeEnergy(perTick, simulate)?1: 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<VulcanizerRecipe> process)
	{
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<VulcanizerRecipe> process)
	{
		ItemStack output = process.recipe.output;
		EnumFacing direction = getDirection("outputs");
		int[] outputs = getPOI(MultiblockPOI.ITEM_OUTPUT);
		int count = output.getCount();
		for(int i = 0; i < count; i++)
		{
			ItemStack stack = output.copy();
			stack.setCount(1);
			outputOrDrop(stack, null, direction, outputs);
		}
		markDirty();
	}

	@Nullable
	@Override
	public IIGUI getGUI()
	{
		return IIGUI.VULCANIZER;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityVulcanizer master = master();
		if(master==null||!master.processQueue.isEmpty())
			return false;

		boolean removeMold = player.isSneaking()&&!master.mold.isEmpty();
		if(!removeMold&&!VulcanizerRecipe.isValidMold(heldItem))
			return false;
		if(world.isRemote)
			return true;

		ItemStack previousMold = master.mold;
		if(removeMold)
			master.mold = ItemStack.EMPTY;
		else
		{
			master.mold = Utils.copyStackWithAmount(heldItem, 1);
			heldItem.shrink(1);
		}
		if(!previousMold.isEmpty())
		{
			if(heldItem.isEmpty())
				player.setHeldItem(hand, previousMold);
			else
				player.entityDropItem(previousMold, 0);
		}
		master.markDirty();
		master.updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		return true;
	}

	@Nullable
	@Override
	public TactileManager getTactileHandler()
	{
		return tactileManager;
	}

	@Override
	public boolean onTactileInteract(EntityAMTTactile tactile, EntityPlayer player, EnumHand hand)
	{
		player.openGui(ImmersiveIntelligence.INSTANCE, getGuiID(), getWorld(),
				getPos().getX(), getPos().getY(), getPos().getZ());
		return true;
	}
}
