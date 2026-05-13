package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISawblade;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSawmill;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSawmill.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.04.2020
 */
public class TileEntitySawmill extends TileEntityMultiblockProductionSingle<TileEntitySawmill, SawmillRecipe> implements IRotationalEnergyBlock, IBooleanAnimatedPartsBlock, IManagedUpgradableDevice<TileEntitySawmill>
{
	@SyncNBT
	public MultiblockInteractablePart vise;
	@SyncNBT
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			return facing==getFacing()?RotationSide.INPUT: RotationSide.NONE;
		}
	};
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntitySawmill> upgradeManager;

	//Inventory Handlers
	private IItemHandler insertionHandler = getSingleInventoryHandler(SLOT_INPUT, true, false);
	private IItemHandler dustExtractionHandler = getSingleInventoryHandler(SLOT_SAWDUST, false, true);
	//Recipe Output Handlers
	private IItemHandler outputHandler = getSingleInventoryHandler(SLOT_OUTPUT), sawdustOutputHandler = getSingleInventoryHandler(SLOT_SAWDUST);
	private SoundHandler sounds;

	public TileEntitySawmill()
	{
		super(MultiblockSawmill.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(0);
		this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		this.vise = new MultiblockInteractablePart(22);
		this.upgradeManager = new UpgradeManager<>(this);
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(world.isRemote)
			sounds = new SoundHandler(this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.outputHandler = this.sawdustOutputHandler = null;
		this.insertionHandler = this.dustExtractionHandler = null;
		this.upgradeManager = null;
		this.rotation = null;
		this.vise = null;

	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ROTARY_INPUT:
				return getPOI("rotary");
			case ITEM_INPUT:
				return getPOI("item_input");
			case ITEM_OUTPUT:
				return getPOI("all_item_output");
			default:
				return new int[0];
		}
	}

	//--- Capabilities ---//
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntitySawmill master = master();
			if(isPOI("item_input"))
				return (T)master.insertionHandler;
			else if(isPOI("sawdust"))
				return (T)master.dustExtractionHandler;
		}
		return super.getCapability(capability, facing);
	}

	//--- Handling ---//

	@Override
	protected void onUpdate()
	{
		this.vise.update();
		this.upgradeManager.update();

		//Self destruct
		if(IIRotaryUtils.destroyIfOverloaded(this, rotation, Sawmill.speedBreaking, Sawmill.torqueBreaking))
			return;

		boolean receivesPower = false;
		//Wheel or mechanical device connected to multiblock
		TileEntity te = world.getTileEntity(getPOIPos(MultiblockPOI.ROTARY_INPUT).offset(facing));
		if(te!=null&&te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite()))
		{
			//Increase internal rotation if powered
			IRotaryEnergy cap = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite());
			assert cap!=null;
			if(rotation.handleRotation(cap, facing.getOpposite()))
			{
				IIPacketHandler.sendToClient(new MessageRotaryPowerSync(world, getPos(), 0, rotation));
				receivesPower = true;
			}
		}

		if(rotation.getTorque() > 0||rotation.getRotationSpeed() > 0)
		{
			//Decrease internal rotation if not powered
			if(!receivesPower)
			{
				rotation.grow(0, 0, 0.98f);
				if(!world.isRemote)
					IIPacketHandler.sendToClient(new MessageRotaryPowerSync(world, getPos(), 0, rotation));
			}

			//Hurt entities stepping on sawblade
			ItemStack sawStack = inventory.get(SLOT_SAWBLADE);

			if(sawStack.getItem() instanceof ISawblade)
			{
				if(world.getTotalWorldTime()%Math.ceil(3-MathHelper.clamp(rotation.getRotationSpeed()/360f, 0, 2))==0)
				{
					int hardness = ((ISawblade)sawStack.getItem()).getHardness(sawStack)*2;
					Vec3i v = facing.getDirectionVec();
					List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class,
							new AxisAlignedBB(getBlockPosForPos(2).offset(EnumFacing.UP)).offset(v.getX()*0.5, v.getY()*0.5, v.getZ()*0.5));
					for(EntityLivingBase l : entities)
						l.attackEntityFrom(IIDamageSources.SAWMILL_DAMAGE, hardness);
				}
			}
		}
		super.onUpdate();

		if(world.isRemote&&currentProcess!=null)
			currentProcess.recipe.getSoundAnimation().handleSounds(sounds, (int)currentProcess.ticks, 1f);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	//--- IGuiTile ---//

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.SAWMILL;
	}

	//--- TileEntityMultiblockProduction ---//

	@Override
	protected IIMultiblockProcess<SawmillRecipe> findNewProductionProcess()
	{
		ItemStack stackSawblade = inventory.get(SLOT_SAWBLADE);

		if(stackSawblade.isEmpty()||!(stackSawblade.getItem() instanceof ISawblade))
			return null;
		ISawblade saw = (ISawblade)stackSawblade.getItem();

		final int sawHardness = saw.getHardness(stackSawblade);
		SawmillRecipe found = SawmillRecipe.streamRecipes(SawmillRecipe.class)
				.filter(recipe -> recipe.itemInput.matchesItemStackIgnoringSize(inventory.get(SLOT_INPUT)))
				.filter(recipe -> recipe.getHardness() <= sawHardness)
				.findFirst()
				.orElse(null);
		if(found==null)
			return null;

		//Store actual input item for display before consuming
		ItemStack displayInput = inventory.get(SLOT_INPUT).copy();
		displayInput.setCount(found.itemInput.inputSize);

		//Look up the correct plank output for this specific log type
		ItemStack correctOutput = lookupPlankOutput(displayInput, found.itemOutput);

		//Consume input
		inventory.get(SLOT_INPUT).shrink(found.itemInput.inputSize);
		return new IIMultiblockProcess<>(found).withNBT(nbt -> {
			nbt.withItemStack("displayInput", displayInput);
			nbt.withItemStack("correctOutput", correctOutput);
		});
	}

	@Override
	protected IIMultiblockProcess<SawmillRecipe> getProcessByName(String name)
	{
		SawmillRecipe recipe = IIMultiblockRecipe.getRecipe(SawmillRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<SawmillRecipe> process, boolean simulate)
	{
		float efficiency;
		if(upgradeManager.has(IIContent.UPGRADE_IMPROVED_GEARBOX))
			efficiency = Sawmill.gearboxUpgradeEfficiency*IIRotaryUtils.getEffectiveEnergy(rotation, Sawmill.speedEfficient, Sawmill.speedGearboxUpgrade, Sawmill.torqueMin, Sawmill.torqueEfficient);
		else
			efficiency = IIRotaryUtils.getEffectiveEnergy(rotation, Sawmill.torqueMin, Sawmill.speedEfficient, Sawmill.torqueMin, Sawmill.torqueEfficient);

		if(inventory.get(SLOT_SAWBLADE).getItem() instanceof ISawblade&&efficiency > 0&&
				inventory.get(SLOT_OUTPUT).getCount()+process.recipe.itemOutput.getCount() <= getSlotLimit(SLOT_OUTPUT))
			return efficiency;
		return 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<SawmillRecipe> process)
	{
		//Use correct output based on actual input wood type
		ItemStack output = process.processData.getItemStack("correctOutput");
		if(output.isEmpty())
			output = process.recipe.itemOutput.copy();
		ItemStack sawdust = process.recipe.itemSecondaryOutput.copy();

		if(upgradeManager.has(IIContent.UPGRADE_SAW_UNREGULATOR))
			if(!sawdust.isEmpty()&&output.getCount() > 1)
			{
				output.shrink(1);
				sawdust.grow(1);
			}

		outputOrDrop(output, outputHandler, facing, getPOI("item_output"));
		outputOrDrop(sawdust, sawdustOutputHandler, EnumFacing.DOWN, getPOI("sawdust"));
		return true;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<SawmillRecipe> process)
	{
		ItemStack sawblade = inventory.get(SLOT_SAWBLADE);
		if(sawblade.getItem() instanceof ISawblade)
			((ISawblade)sawblade.getItem()).damageTool(sawblade, process.recipe.getHardness());
	}

	/**
	 * Looks up the correct plank output for a given log input by checking crafting recipes.
	 * Prefers the most specific recipe (fewest matching ingredients) to ensure the output
	 * plank type matches the input log type.
	 *
	 * @param logInput the actual log item being processed
	 * @param fallback the recipe's default output to use if no crafting recipe match is found
	 * @return the correct plank output
	 */
	private static ItemStack lookupPlankOutput(ItemStack logInput, ItemStack fallback)
	{
		ItemStack testStack = logInput.copy();
		testStack.setCount(1);
		ItemStack bestResult = ItemStack.EMPTY;
		int bestSpecificity = Integer.MAX_VALUE;

		for(IRecipe recipe : CraftingManager.REGISTRY)
		{
			if(Utils.compareToOreName(recipe.getRecipeOutput(), "plankWood"))
			{
				for(Ingredient ingredient : recipe.getIngredients())
				{
					if(ingredient.apply(testStack))
					{
						//Prefer recipes with fewer matching stacks (more specific to this log type)
						int specificity = ingredient.getMatchingStacks().length;
						if(specificity < bestSpecificity)
						{
							bestSpecificity = specificity;
							bestResult = recipe.getRecipeOutput().copy();
							bestResult.setCount(Math.round(bestResult.getCount()*1.5f));
						}
						break;
					}
				}
			}
		}
		return bestResult.isEmpty()?fallback.copy(): bestResult;
	}

	//--- IRotationalEnergyBlock ---//

	@Override
	public void updateRotationStorage(float speed, float torque, int partID)
	{
		if(world.isRemote)
		{
			rotation.setRotationSpeed(speed);
			rotation.setTorque(torque);
		}
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		vise.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(vise.setState(state))
			world.playSound(null, getPos(), state?IISounds.viseOpen: IISounds.viseClose, SoundCategory.BLOCKS, 1f, 1f);
		IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(part, state, this));
	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntitySawmill> getUpgradeManager()
	{
		return upgradeManager;
	}
}



