package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.LatexCollector;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLog;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLog.RubberLogs;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.06.2026
 * @ii-approved 0.3.1
 * @since 19.05.2021
 */
public class TileEntityLatexCollector extends TileEntityIIDirectional implements IPlayerInteraction, ITickable, IBlockBounds, IAdvancedTextOverlay
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL);
	@SyncNBT
	public EnumFacing facing = EnumFacing.NORTH;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ItemStack bucket = ItemStack.EMPTY;
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2})
	public int collectedLatex = 0, collectionTimer = 0;
	@SyncNBT(events = {SyncEvents.TILE_CUSTOM1})
	public int bucketTime = 10;

	@Override
	public void update()
	{
		//Handle bucket placement
		if(bucket.isEmpty()||!bucket.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
			return;
		if(bucketTime > 0)
		{
			bucketTime -= 1;
			return;
		}

		//Stop collecting if the bucket is full
		IFluidHandlerItem capability = bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(capability==null||capability.drain(1000, false)!=null)
			return;

		//Handle latex collection
		if(collectionTimer < LatexCollector.dropTimer)
			collectionTimer += 1;
		else
		{
			//Increment collected amount
			if(collectedLatex < 1000)
			{
				collectedLatex += (int)(LatexCollector.dropAmount*getIncomeModifier());
				collectionTimer = 0;
			}
			else
			{
				//Fill the bucket item (and stop collection next tick)
				this.collectedLatex = 0;
				if(!world.isRemote)
				{
					bucket = capability.getContainer();
					updateTileForEvent(SyncEvents.ENTITY_CUSTOM1);
				}
			}
		}
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem,
	                        float hitX, float hitY, float hitZ)
	{
		//Handle bucket placement
		if(bucket.isEmpty()&&heldItem.getItem()==Items.BUCKET)
		{
			bucket = heldItem.copy();
			bucket.setCount(1);
			heldItem.shrink(1);
			updateTileForEvent(SyncEvents.ENTITY_CUSTOM1);
			this.collectedLatex = 0;
			return true;
		}
		//Handle bucket removal
		else if(!bucket.isEmpty()&&heldItem.isEmpty())
		{
			player.inventory.addItemStackToInventory(bucket.copy());
			bucket = ItemStack.EMPTY;
			updateTileForEvent(SyncEvents.ENTITY_CUSTOM1);
			this.collectedLatex = 0;
			return true;
		}
		else return heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
	}

	//--- Utils ---//

	/**
	 * @return available latex stored in the collector's bucket, in mB (0..1000).
	 */
	public int getAvailableLatexMilliBuckets()
	{
		FluidStack fs = FluidUtil.getFluidContained(bucket);
		return MathHelper.clamp(fs==null?collectedLatex: fs.amount, 0, 1000);
	}

	/**
	 * Drains a partial amount of latex by reducing the stored collector amount.
	 *
	 * @param amountMb requested mB
	 * @param doDrain  whether to actually drain
	 * @return drained mB
	 */
	public int drainLatexMilliBuckets(int amountMb, boolean doDrain)
	{
		if(world==null||world.isRemote||amountMb <= 0)
			return 0;

		//Drain bucket item
		FluidStack fs = FluidUtil.getFluidContained(bucket);
		if(fs!=null)
		{
			IFluidHandlerItem capability = bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			assert capability!=null;
			if(doDrain)
			{
				capability.drain(1000, true);
				this.bucket = capability.getContainer();
				//Put back the remaining latex into the collector's stored amount; a bucket can only
				this.collectedLatex = 1000-Math.min(amountMb, 1000);
				updateTileForEvent(SyncEvents.TILE_CUSTOM2);
			}
			return Math.min(amountMb, 1000);
		}

		//Drain the collector's stored latex
		int collected = Math.min(amountMb, collectedLatex);
		if(doDrain)
		{
			collectedLatex = collectedLatex-collected;
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
		return collected;
	}

	public boolean isNextToTree()
	{
		IBlockState log = world.getBlockState(pos.offset(facing).up());
		return log.getBlock() instanceof BlockIIRubberLog&&log.getValue(IIContent.blockRubberLog.property)==RubberLogs.STRIPPED;
	}

	public float getIncomeModifier()
	{
		float def = 1f;
		if(world.getTileEntity(pos.offset(facing, 2)) instanceof TileEntityLatexCollector)
			def -= LatexCollector.dropPenalty;
		if(world.getTileEntity(pos.offset(facing).offset(facing.rotateY())) instanceof TileEntityLatexCollector)
			def -= LatexCollector.dropPenalty;
		if(world.getTileEntity(pos.offset(facing).offset(facing.rotateYCCW())) instanceof TileEntityLatexCollector)
			def -= LatexCollector.dropPenalty;
		return def;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		int latex = (int)MathHelper.clamp(collectedLatex, 0f, 1000);
		FluidStack contained = FluidUtil.getFluidContained(bucket);
		if(contained!=null&&contained.amount > latex)
			latex = MathHelper.clamp(contained.amount, 0, 1000);

		if(latex==0)
			return new String[]{I18n.format("gui.immersiveengineering.empty")};
		return new String[]{
				IIContent.fluidLatex.getLocalizedName(new FluidStack(IIContent.fluidLatex, latex)),
				TextFormatting.GRAY.toString()+latex+"/1000 mB"
		};
	}

	//--- Facing ---//

	@Override
	@Nonnull
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	//--- Block Bounds ---//

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, .875f, 1f};
	}
}
