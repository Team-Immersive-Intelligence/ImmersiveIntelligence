package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 * @ii-approved 0.3.1
 * @since 17.05.2019
 */
public class TileEntityDataMerger extends TileEntityIIDirectional implements IPlayerInteraction, IBlockBounds, IDataDevice, IIIGuiMultiblockTile, IIIInventory
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL);

	@SyncNBT(name = "rules", events = {SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_GUI_OPENED})
	public EasyCollection<DataMergeRule, NBTTagCompound> mergeRules = new EasyCollection<>(DataMergeRule::new);
	@SyncNBT
	public DataPacket packetLeft = new DataPacket();
	@SyncNBT
	public DataPacket packetRight = new DataPacket();

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("packet", Constants.NBT.TAG_COMPOUND))
			migrateLegacySettings(new DataPacket(nbt.getCompoundTag("packet")));
		super.readCustomNBT(nbt, descPacket);
	}

	private void migrateLegacySettings(DataPacket settingsPacket)
	{
		mergeRules.clear();

		for(char c : DataPacket.VARIABLE_NAMES)
			IIDataHandlingUtils.optionalInt(c, settingsPacket).ifPresent(integer -> {
				int mode = integer+2;
				if(mode < 0||mode >= VariableMergeMode.values().length)
					return;

				VariableMergeMode legacy = VariableMergeMode.values()[mode];
				if(legacy==VariableMergeMode.RETAIN_ORIGINAL)
					return;

				DataMergeRule rule = new DataMergeRule();
				rule.variable = c;
				rule.triggerForwarding = true;
				rule.usageLimit = -1;

				switch(legacy)
				{
					case PREFER_RIGHT:
						rule.acceptLeft = true;
						rule.acceptRight = true;
						rule.rightOverridesCachedValue = true;
						rule.rightOverridesValue = true;
						break;
					case FORCE_RIGHT:
						rule.acceptLeft = false;
						rule.acceptRight = true;
						rule.rightOverridesCachedValue = true;
						rule.rightOverridesValue = true;
						break;
					case PREFER_LEFT:
						rule.acceptLeft = true;
						rule.acceptRight = true;
						rule.rightOverridesCachedValue = false;
						rule.rightOverridesValue = true;
						break;
					case FORCE_LEFT:
						rule.acceptLeft = true;
						rule.acceptRight = false;
						rule.rightOverridesCachedValue = false;
						rule.rightOverridesValue = true;
						break;
				}
				mergeRules.add(rule);
			});
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, .875f, 1f};
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		boolean fromLeft = side==facing.rotateYCCW();
		boolean fromRight = side==facing.rotateY();

		//The merger only listens to its two input sides.
		if(!fromLeft&&!fromRight)
			return;

		DataPacket incoming = packet.clone();
		DataPacket cache = fromLeft?packetLeft: packetRight;
		boolean shouldForward = false;
		List<DataMergeRule> expiredRules = new ArrayList<>();

		//Accept variables into the side cache and check if this packet should trigger output.
		for(DataMergeRule rule : mergeRules)
			if(rule.acceptsSide(fromLeft, fromRight)&&incoming.has(rule.variable))
			{
				cache.set(rule.variable, incoming.get(rule.variable).clone());
				if(rule.triggerForwarding)
					shouldForward = true;

				rule.consumeUse();
				if(rule.isExpired())
					expiredRules.add(rule);
			}

		if(!shouldForward)
		{
			if(!expiredRules.isEmpty())
			{
				mergeRules.removeAll(expiredRules);
				markDirty();
			}
			return;
		}

		DataPacket outgoing = incoming.clone();
		for(DataMergeRule rule : mergeRules)
			rule.applyTo(outgoing, incoming, packetLeft, packetRight);

		if(!expiredRules.isEmpty())
		{
			mergeRules.removeAll(expiredRules);
			markDirty();
		}

		IIDataHandlingUtils.sendPacketAdjacently(outgoing, world, this.pos, facing);
	}

	@Override
	public boolean canOpenGui()
	{
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		return true;
	}

	@Override
	public TileEntity master()
	{
		return this;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.DATA_MERGER;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	public static class DataMergeRule implements INBTSerializable<NBTTagCompound>
	{
		public char variable = 'a';
		public boolean acceptLeft = true;
		public boolean acceptRight = true;
		public boolean triggerForwarding = true;

		/**
		 * When both side caches hold the variable, true gives priority to right; false gives priority to left.
		 */
		public boolean rightOverridesCachedValue = true;

		/**
		 * When true, the selected cached value can replace the variable already present in the outgoing packet.
		 * When false, the incoming packet's value is preserved and the cache only fills missing variables.
		 */
		public boolean rightOverridesValue = true;

		/**
		 * -1 means infinite Job. Non-negative values are Requests and are decremented when the rule accepts a variable.
		 */
		public int usageLimit = -1;

		public boolean isJob()
		{
			return usageLimit < 0;
		}

		public boolean acceptsSide(boolean fromLeft, boolean fromRight)
		{
			return (fromLeft&&acceptLeft)||(fromRight&&acceptRight);
		}

		public void consumeUse()
		{
			if(usageLimit > 0)
				usageLimit--;
		}

		public boolean isExpired()
		{
			return usageLimit==0;
		}

		public DataMergeRule copy()
		{
			DataMergeRule copy = new DataMergeRule();
			copy.deserializeNBT(serializeNBT());
			return copy;
		}

		public void applyTo(DataPacket outgoing, DataPacket incoming, DataPacket leftCache, DataPacket rightCache)
		{
			DataType cached = getSelectedCachedValue(leftCache, rightCache);
			if(cached==null)
				return;

			//Preserve the incoming packet value unless this rule is allowed to override it.
			if(incoming.has(variable)&&!rightOverridesValue)
				return;

			outgoing.set(variable, cached.clone());
		}

		private DataType getSelectedCachedValue(DataPacket leftCache, DataPacket rightCache)
		{
			boolean hasLeft = acceptLeft&&leftCache.has(variable);
			boolean hasRight = acceptRight&&rightCache.has(variable);

			if(hasLeft&&hasRight)
				return rightOverridesCachedValue?rightCache.get(variable): leftCache.get(variable);
			else if(hasRight)
				return rightCache.get(variable);
			else if(hasLeft)
				return leftCache.get(variable);
			return null;
		}

		@Nonnull
		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("variable", String.valueOf(variable));
			nbt.setBoolean("accept_left", acceptLeft);
			nbt.setBoolean("accept_right", acceptRight);
			nbt.setBoolean("trigger_forwarding", triggerForwarding);
			nbt.setBoolean("right_overrides_cached", rightOverridesCachedValue);
			nbt.setBoolean("right_overrides_value", rightOverridesValue);
			nbt.setInteger("usage_limit", usageLimit);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			String variableString = nbt.getString("variable");
			variable = variableString.isEmpty()?'a': variableString.charAt(0);

			acceptLeft = !nbt.hasKey("accept_left")||nbt.getBoolean("accept_left");
			acceptRight = !nbt.hasKey("accept_right")||nbt.getBoolean("accept_right");
			triggerForwarding = !nbt.hasKey("trigger_forwarding")||nbt.getBoolean("trigger_forwarding");
			rightOverridesCachedValue = !nbt.hasKey("right_overrides_cached")||nbt.getBoolean("right_overrides_cached");
			rightOverridesValue = !nbt.hasKey("right_overrides_value")||nbt.getBoolean("right_overrides_value");
			usageLimit = nbt.hasKey("usage_limit")?nbt.getInteger("usage_limit"): -1;
		}
	}

	private enum VariableMergeMode
	{
		PREFER_RIGHT,
		FORCE_RIGHT,
		RETAIN_ORIGINAL,
		PREFER_LEFT,
		FORCE_LEFT
	}
}
