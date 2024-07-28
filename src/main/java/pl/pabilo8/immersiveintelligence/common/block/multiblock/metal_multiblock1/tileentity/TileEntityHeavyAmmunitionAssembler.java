package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockHeavyAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityHeavyAmmunitionAssembler extends TileEntityMultiblockProductionSingle<TileEntityHeavyAmmunitionAssembler, AmmunitionAssemblerRecipe> implements ITactileListener
{
	@SyncNBT
	public MultiblockInteractablePart drawer1 = new MultiblockInteractablePart(20);
	@SyncNBT
	public MultiblockInteractablePart drawer2 = new MultiblockInteractablePart(22);
	@SyncNBT
	public MultiblockInteractablePart drawer3 = new MultiblockInteractablePart(19);
	@SyncNBT
	public MultiblockInteractablePart drawer4 = new MultiblockInteractablePart(21);
	private TactileHandler tactileHandler;

	public TileEntityHeavyAmmunitionAssembler()
	{
		super(MultiblockHeavyAmmunitionAssembler.INSTANCE);
		tactileHandler = new TactileHandler(multiblock, this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		tactileHandler = null;
	}

	@Override
	protected void onUpdate()
	{
		//Handle Tactile AMT on server side
		tactileHandler.defaultize();
		float progress = (world.getTotalWorldTime()%320)/320f;
		tactileHandler.update(ResLoc.of(IIReference.RES_II, "heavy_ammunition_assembler/rocket_6bcal"), progress);
	}

	@Override
	protected IIMultiblockProcess<AmmunitionAssemblerRecipe> findNewProductionProcess()
	{
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<AmmunitionAssemblerRecipe> process, boolean simulate)
	{
		return 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<AmmunitionAssemblerRecipe> process)
	{
		return false;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<AmmunitionAssemblerRecipe> process)
	{

	}

	@Override
	public IIGuiList getGUI()
	{
		return null;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		return new int[0];
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Nullable
	@Override
	public TactileHandler getTactileHandler()
	{
		return tactileHandler;
	}

	@Nonnull
	@Override
	public World getTactileWorld()
	{
		return getWorld();
	}

	@Nonnull
	@Override
	public BlockPos getTactilePos()
	{
		return getPos();
	}

	@Nonnull
	@Override
	public EnumFacing getTactileFacing()
	{
		return getIsMirrored()?getFacing().getOpposite(): getFacing();
	}

	@Override
	public boolean getIsTactileMirrored()
	{
		return true;
	}
}
