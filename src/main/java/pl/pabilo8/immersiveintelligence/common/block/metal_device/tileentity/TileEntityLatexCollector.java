package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.LatexCollector;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLog;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIRubberLog.RubberLogs;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.05.2021
 */
public class TileEntityLatexCollector extends TileEntityIEBase implements IPlayerInteraction, ITickable, IBlockBounds, IDirectionalTile, IAdvancedTextOverlay
{
	public EnumFacing facing = EnumFacing.NORTH;
	public ItemStack bucket = ItemStack.EMPTY;
	public float timer = 0;
	//Purely decorational, client only
	public int bucketTime = 10;

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
		bucket = new ItemStack(nbt.getCompoundTag("bucket"));
		if(nbt.hasKey("noSetup"))
			bucketTime = 0;
		this.timer = nbt.getFloat("timer");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setInteger("facing", facing.ordinal());
		nbt.setTag("bucket", bucket.serializeNBT());
		if(bucketTime < 10)
			nbt.setBoolean("noSetup", true);
		nbt.setFloat("timer", timer);
	}

	/**
	 * @return available latex based on current progress fraction, in mB (0..1000).
	 * Fraction: floor((timer/collectTime)*1000).
	 */
	public int getAvailableLatexMilliBuckets()
	{
		if(LatexCollector.collectTime <= 0)
			return 0;
		if(!world.getBiome(pos).isHighHumidity())
			return 0;
		float frac = Math.max(0f, Math.min(1f, timer/LatexCollector.collectTime));
		return (int)Math.floor(frac*1000f);
	}

	/**
	 * Drains a partial amount of latex by reducing internal progress accordingly.
	 *
	 * @param amountMb requested mB
	 * @param doDrain  whether to actually drain
	 * @return drained mB
	 */
	public int drainLatexMilliBuckets(int amountMb, boolean doDrain)
	{
		if(world==null||world.isRemote)
			return 0;

		int available = getAvailableLatexMilliBuckets();
		int drained = Math.max(0, Math.min(amountMb, available));
		if(drained <= 0)
			return 0;

		if(doDrain)
		{
			float deltaTimer = (drained/1000f)*LatexCollector.collectTime;
			timer = Math.max(0f, timer-deltaTimer);
			markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		}

		return drained;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(bucket.isEmpty()&&heldItem.getItem()==Items.BUCKET)
		{
			bucket = heldItem.copy();
			bucket.setCount(1);
			heldItem.shrink(1);
			updateBucket();
			this.timer = 0;
			return true;
		}
		else if(!bucket.isEmpty()&&heldItem.isEmpty())
		{
			player.inventory.addItemStackToInventory(bucket.copy());
			bucket = ItemStack.EMPTY;
			updateBucket();
			this.timer = 0;
			return true;
		}
		else return heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
	}

	private void updateBucket()
	{
		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT().withItemStack("bucket", bucket)));
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("bucket"))
		{
			bucket = new ItemStack(message.getCompoundTag("bucket"));
			bucketTime = 10;
		}
	}

	@Override
	public void update()
	{
		if(bucketTime > 0)
			bucketTime -= 1;

		if(!bucket.isEmpty()&&bucket.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem capability = bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(capability==null)
				return;

			if(capability.fill(new FluidStack(IIContent.fluidLatex, 1000), false)==1000&&timer < LatexCollector.collectTime&&isNextToTree())
				timer = Math.min(timer+getIncomeModifier(), LatexCollector.collectTime);
			else if(timer==LatexCollector.collectTime)
				if(capability.fill(new FluidStack(IIContent.fluidLatex, 1000), true)==1000)
				{
					this.timer = 0;
					if(!world.isRemote)
					{
						bucket = capability.getContainer();
						updateBucket();
					}
				}
		}


	}

	public boolean isNextToTree()
	{
		IBlockState log = world.getBlockState(pos.offset(facing).up());
		return log.getBlock() instanceof BlockIIRubberLog&&log.getValue(IIContent.blockRubberLog.property)==RubberLogs.STRIPPED;
	}

	public float getIncomeModifier()
	{
		int def = 4;
		if(world.getTileEntity(pos.offset(facing, 2)) instanceof TileEntityLatexCollector)
			def--;
		if(world.getTileEntity(pos.offset(facing).offset(facing.rotateY())) instanceof TileEntityLatexCollector)
			def--;
		if(world.getTileEntity(pos.offset(facing).offset(facing.rotateYCCW())) instanceof TileEntityLatexCollector)
			def--;

		return def/4f;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, .875f, 1f};
	}

	@Override
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return false;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		int latex = (int)MathHelper.clamp(timer/LatexCollector.collectTime*1000, 0f, 1000f);
		if(latex==0)
			return new String[]{I18n.format("gui.immersiveengineering.empty")};

		return new String[]{
				IIContent.fluidLatex.getLocalizedName(new FluidStack(IIContent.fluidLatex, latex)),
				TextFormatting.GRAY.toString()+latex+"/1000 mB"
		};
	}
}
