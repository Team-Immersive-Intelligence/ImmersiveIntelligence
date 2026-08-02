package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Vulcanizer;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockVulcanizer;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionMulti;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.05.2026
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityVulcanizer extends TileEntityMultiblockProductionMulti<TileEntityVulcanizer, VulcanizerRecipe> implements IPlayerInteraction
{
	@SyncNBT
	public ItemStack mold = ItemStack.EMPTY;
	@SyncNBT
	public boolean recipePerformed = false;

	private IItemHandler insertionHandlerRubber, insertionHandlerCompound, insertionHandlerSulfur;
	private SoundHandler soundHandler;

	public TileEntityVulcanizer()
	{
		super(MultiblockVulcanizer.INSTANCE);

		this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
		this.energyStorage = new FluxStorageAdvanced(Vulcanizer.energyCapacity);

		this.insertionHandlerRubber = new IEInventoryHandler(1, this, 0, true, false);
		this.insertionHandlerCompound = new IEInventoryHandler(1, this, 1, true, false);
		this.insertionHandlerSulfur = new IEInventoryHandler(1, this, 2, true, false);

		this.soundHandler = new SoundHandler(this);
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote&&!processQueue.isEmpty())
			MultiblockVulcanizer.INSTANCE.workAnimation.handleSounds(soundHandler,
					(int)(getProductionProgress(processQueue.get(0), 0)*1000), 1f);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case ITEM_INPUT:
				return getPOI("inputs");
			case ITEM_OUTPUT:
				return getPOI("outputs");
			default:
				return new int[0];
		}
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		switch(i)
		{
			case MultiblockVulcanizer.SLOT_RUBBER:
				return IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
						.anyMatch(r -> r.input.matchesItemStackIgnoringSize(itemStack));
			case MultiblockVulcanizer.SLOT_COMPOUND:
				return IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
						.anyMatch(r -> r.compoundInput.matchesItemStackIgnoringSize(itemStack));
			case MultiblockVulcanizer.SLOT_SULFUR:
				return IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
						.anyMatch(r -> r.sulfurInput.matchesItemStackIgnoringSize(itemStack));
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(master()!=null&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityVulcanizer master = master();
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
				inventory.get(MultiblockVulcanizer.SLOT_RUBBER),
				inventory.get(MultiblockVulcanizer.SLOT_COMPOUND),
				inventory.get(MultiblockVulcanizer.SLOT_SULFUR),
				mold
		);
	}

	@Override
	public float getMinProductionOffset()
	{
		return 0.84f;
	}

	@Override
	public int getMaxProductionQueue()
	{
		return 2;
	}

	@Override
	protected IIMultiblockProcess<VulcanizerRecipe> findNewProductionProcess()
	{
		//A recipe requires a mold installed
		if(mold.isEmpty())
			return null;

		//Find recipe
		ComparableItemStack comparableMold = new ComparableItemStack(mold);
		Optional<VulcanizerRecipe> first = IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
				.filter(r -> r.mold.equals(comparableMold))
				.filter(recipe -> recipe.input.matchesItemStack(inventory.get(MultiblockVulcanizer.SLOT_RUBBER)))
				.filter(recipe -> recipe.compoundInput.matchesItemStack(inventory.get(MultiblockVulcanizer.SLOT_COMPOUND)))
				.filter(recipe -> recipe.sulfurInput.matchesItemStack(inventory.get(MultiblockVulcanizer.SLOT_SULFUR)))
				.findFirst();

		if(first.isPresent())
		{
			VulcanizerRecipe recipe = first.get();
			//Take items from inventory
			inventory.get(MultiblockVulcanizer.SLOT_RUBBER).shrink(recipe.input.inputSize);
			inventory.get(MultiblockVulcanizer.SLOT_COMPOUND).shrink(recipe.compoundInput.inputSize);
			inventory.get(MultiblockVulcanizer.SLOT_SULFUR).shrink(recipe.sulfurInput.inputSize);

			//Return new recipe
			return new IIMultiblockProcess<>(recipe);
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
		if(!ApiUtils.createComparableItemStack(mold, true).equals(process.recipe.mold))
			return 0;

		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		return energyStorage.extractEnergy(perTick, simulate)==perTick?1: 0;
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
		//actual output
		int count = output.getCount();
		for(int i = 0; i < count; i++)
		{
			ItemStack stack = output.copy();
			stack.setCount(1);
			outputOrDrop(stack, null, getDirection("output"), getPOI(MultiblockPOI.ITEM_OUTPUT));
		}
	}

	@Nullable
	@Override
	public IIGUI getGUI()
	{
		return IIGUI.VULCANIZER;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		//TODO: 10.05.2026 simplify
		TileEntityVulcanizer master = master();
		if(master!=null&&master.processQueue.isEmpty())
		{
			if(player.isSneaking()&&!master.mold.isEmpty())
			{
				if(heldItem.isEmpty())
					player.setHeldItem(hand, master.mold.copy());
				else if(!world.isRemote)
					player.entityDropItem(master.mold.copy(), 0);
				master.mold = ItemStack.EMPTY;
				this.updateMasterBlock(null, true);
				return true;
			}
			else if(VulcanizerRecipe.isValidMold(heldItem))
			{
				ItemStack tempMold = !master.mold.isEmpty()?master.mold.copy(): ItemStack.EMPTY;
				master.mold = Utils.copyStackWithAmount(heldItem, 1);
				heldItem.shrink(1);
				if(heldItem.getCount() <= 0)
					heldItem = ItemStack.EMPTY;
				else
					player.setHeldItem(hand, heldItem);
				if(!tempMold.isEmpty())
					if(heldItem.isEmpty())
						player.setHeldItem(hand, tempMold);
					else if(!world.isRemote)
						player.entityDropItem(tempMold, 0);
				this.updateMasterBlock(null, true);
				return true;
			}
		}
		return false;
	}

	//--- Client ---//

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		Vec3d facing = new Vec3d(getDirection("output").getDirectionVec()).scale(0.25f);
		facing = facing.scale(0.65f);

		for(int p = 48; p <= 50; p++)
			for(int i = 0; i < 6; i++)
			{
				Vec3d bpos = new Vec3d(getBlockPosForPos(p)).addVector(0.5, -0.5, 0.5).add(facing);
				float mod = Utils.RAND.nextFloat();

				/*ParticleShockwave particle = new ParticleShockwave(ClientUtils.mc().world,
						bpos.addVector(0, 0.125*i, 0),
						facing.scale(mod).addVector(0, -mod*0.1, 0),
						i+mod);
				ParticleRegistry.IIParticleSystem.addEffect(particle);*/
			}
	}
}
