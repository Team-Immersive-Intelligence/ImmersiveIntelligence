package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.crafting.PaintingRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockChemicalPainter.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class TileEntityChemicalPainter extends TileEntityMultiblockProductionSingle<TileEntityChemicalPainter, PaintingRecipe>
		implements ILightEventConsumer, IPlayerInteraction, IAdvancedTextOverlay
{
	@SyncNBT(name = "tank1", events = SyncEvents.TILE_RECIPE_CHANGED)
	public FluidTank tankCyan;
	@SyncNBT(name = "tank2", events = SyncEvents.TILE_RECIPE_CHANGED)
	public FluidTank tankMagenta;
	@SyncNBT(name = "tank3", events = SyncEvents.TILE_RECIPE_CHANGED)
	public FluidTank tankYellow;
	@SyncNBT(name = "tank4", events = SyncEvents.TILE_RECIPE_CHANGED)
	public FluidTank tankBlack;

	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public ItemStack recipeStack = ItemStack.EMPTY;
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public ItemStack resultStack = ItemStack.EMPTY;

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public IIColor color = IIColor.fromPackedRGB(0xff00ff);

	private IItemHandler insertionHandler = getSingleInventoryHandler(SLOT_INPUT);
	private IItemHandler outputHandler = getSingleInventoryHandler(SLOT_OUTPUT);

	private SoundHandler sounds;

	public TileEntityChemicalPainter()
	{
		super(INSTANCE);

		inventory = NonNullList.withSize(4, ItemStack.EMPTY);
		energyStorage = new FluxStorageAdvanced(ChemicalPainter.energyCapacity);
		tankCyan = new FilteredFluidTank(ChemicalPainter.fluidCapacity).withInputFilter(CYAN);
		tankMagenta = new FilteredFluidTank(ChemicalPainter.fluidCapacity).withInputFilter(MAGENTA);
		tankYellow = new FilteredFluidTank(ChemicalPainter.fluidCapacity).withInputFilter(YELLOW);
		tankBlack = new FilteredFluidTank(ChemicalPainter.fluidCapacity).withInputFilter(BLACK);
		sounds = new SoundHandler(this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		tankCyan = tankMagenta = tankYellow = tankBlack = null;
		insertionHandler = outputHandler = null;
		color = null;
		recipeStack = resultStack = ItemStack.EMPTY;
		sounds = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		//Handle sounds on client side
		if(world.isRemote&&this.currentProcess!=null)
			this.currentProcess.recipe.productionAnimation.handleSounds(sounds, (int)this.currentProcess.ticks, 1f);

		//Handle fluid input slot interaction
		IIUtils.handleBucketTankInteraction(tankCyan, inventory, SLOT_BUCKET_INPUT, SLOT_BUCKET_OUTPUT, false, CYAN);
		IIUtils.handleBucketTankInteraction(tankMagenta, inventory, SLOT_BUCKET_INPUT, SLOT_BUCKET_OUTPUT, false, MAGENTA);
		IIUtils.handleBucketTankInteraction(tankYellow, inventory, SLOT_BUCKET_INPUT, SLOT_BUCKET_OUTPUT, false, YELLOW);
		IIUtils.handleBucketTankInteraction(tankBlack, inventory, SLOT_BUCKET_INPUT, SLOT_BUCKET_OUTPUT, false, BLACK);

		//Handle item input
		if(!world.isRemote)
			attemptStackOutput(outputHandler, getDirection("item_output"), getPOI(MultiblockPOI.ITEM_OUTPUT));
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case FLUID_INPUT:
				return getPOI("fluid_inputs");
			case ENERGY_INPUT:
				return getPOI("energy_input");
			case DATA_INPUT:
				return getPOI("data");
			case ITEM_INPUT:
				return getPOI("item_input");
			case ITEM_OUTPUT:
				return getPOI("item_output");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			default:
				return new int[0];
		}
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		DataType p = packet.get('p');
		if(IIDataHandlingUtils.isCallbackPacket(packet))
		{
			DataPacket callback = IIDataHandlingUtils.handleCallback(packet, var -> {
				switch(var)
				{
					case "get_energy":
						return new DataTypeInteger(energyStorage.getEnergyStored());
					case "get_progress":
						return new DataTypeFloat(getProductionProgress(currentProcess, 0));
					case "get_ink_black":
						return new DataTypeInteger(tankBlack.getFluidAmount());
					case "get_ink_cyan":
						return new DataTypeInteger(tankCyan.getFluidAmount());
					case "get_ink_yellow":
						return new DataTypeInteger(tankMagenta.getFluidAmount());
					case "get_ink_magenta":
						return new DataTypeInteger(tankYellow.getFluidAmount());
					case "get_color":
						return new DataTypeInteger(color.getPackedRGB());
				}
				return null;
			});
			sendData(callback, mirrored?facing.rotateY(): facing.rotateYCCW(), getPOI(MultiblockPOI.DATA_INPUT)[0]);
		}
		else if(p instanceof DataTypeInteger)
			color = IIColor.fromPackedRGB(MathHelper.clamp(((DataTypeInteger)p).value, 0, 0xffffff));
		else if(p instanceof DataTypeString)
			color = IIColor.fromHex(((DataTypeString)p).value);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		if(getPOI("input_cyan")[0]==pos)
			return new FluidTank[]{tankCyan};
		else if(getPOI("input_magenta")[0]==pos)
			return new FluidTank[]{tankMagenta};
		else if(getPOI("input_yellow")[0]==pos)
			return new FluidTank[]{tankYellow};
		else if(getPOI("input_black")[0]==pos)
			return new FluidTank[]{tankBlack};

		return super.getFluidTanks(pos, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)master().insertionHandler;

		return super.getCapability(capability, facing);
	}

	@Override
	protected IIMultiblockProcess<PaintingRecipe> findNewProductionProcess()
	{
		if(this.inventory.get(SLOT_INPUT).isEmpty())
			return null;

		//Get recipe
		java.util.Optional<PaintingRecipe> found = IIMultiblockRecipe.streamRecipes(PaintingRecipe.class)
				.filter(r -> r.itemInput.matchesItemStack(inventory.get(SLOT_INPUT)))
				.findFirst();

		if(!found.isPresent())
			return null;
		PaintingRecipe recipe = found.get();

		//Get paint amounts and test if they are available
		int c = recipe.getCyanAmount(color);
		int m = recipe.getMagentaAmount(color);
		int y = recipe.getYellowAmount(color);
		int k = recipe.getBlackAmount(color);

		if(tankCyan.getFluidAmount() < c||tankMagenta.getFluidAmount() < m||tankYellow.getFluidAmount() < y||tankBlack.getFluidAmount() < k)
			return null;

		//Drain fluids
		tankCyan.drain(c, true);
		tankMagenta.drain(m, true);
		tankYellow.drain(y, true);
		tankBlack.drain(k, true);

		//Take stack from inventory
		this.recipeStack = ItemHandlerHelper.copyStackWithSize(inventory.get(SLOT_INPUT), 1);
		this.resultStack = recipe.process.apply(color, this.recipeStack);
		inventory.get(SLOT_INPUT).shrink(1);

		//Return new process
		return new IIMultiblockProcess<>(recipe)
				.withNBT(easyNBT -> easyNBT.withItemStack("effect", this.recipeStack).withColor("color", color));
	}

	@Override
	protected IIMultiblockProcess<PaintingRecipe> getProcessByName(String name)
	{
		return TileEntityMultiblockProductionBase.findRecipeFromList(PaintingRecipe.class, name);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<PaintingRecipe> process, boolean simulate)
	{
		if(energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), true)==process.recipe.getEnergyPerTick())
			return (simulate||(energyStorage.extractEnergy(process.recipe.getEnergyPerTick(), false)) > 0)?1: 0;
		return 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<PaintingRecipe> process)
	{
		ItemStack result = process.processData.getItemStack("effect");
		IIColor color = process.processData.getColor("color");
		if(result.isEmpty())
		{
			IILogger.error("Error on Chemical Painter recipe output, result nbt is null!");
			return true;
		}
		return outputHandler.insertItem(0, process.recipe.process.apply(color, result), false).isEmpty();
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<PaintingRecipe> process)
	{
		this.recipeStack = ItemStack.EMPTY;
		this.resultStack = ItemStack.EMPTY;
	}

	@Nullable
	@Override
	public IIGUI getGUI()
	{
		return IIGUI.CHEMICAL_PAINTER;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent)
	{
		/*if(pos==47)
		{
			TileEntityChemicalPainter master = master();
			if(master!=null&&master.active)
				if(IIMath.inRange(master.processTime, master.processTimeMax, 0.65f, 0.95f))
					gatherLightsEvent.add(Light.builder().pos(getPos()).color(0.8235294f, 0.20392157f, 0.92156863f).radius(5f).build());
		}*/
	}


	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityChemicalPainter master = master();
		if(master==null)
			return false;

		if(isPOI("tank_cyan"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankCyan);
		else if(isPOI("tank_magenta"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankMagenta);
		else if(isPOI("tank_yellow"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankYellow);
		else if(isPOI("tank_black"))
			return FluidUtil.interactWithFluidHandler(player, hand, master.tankBlack);

		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
			return new String[0];

		TileEntityChemicalPainter master = master();
		if(master==null)
			return new String[0];

		if(isPOI("tank_cyan"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tankCyan.getFluid())};
		else if(isPOI("tank_magenta"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tankMagenta.getFluid())};
		else if(isPOI("tank_yellow"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tankYellow.getFluid())};
		else if(isPOI("tank_black"))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tankBlack.getFluid())};

		return new String[0];
	}
}
