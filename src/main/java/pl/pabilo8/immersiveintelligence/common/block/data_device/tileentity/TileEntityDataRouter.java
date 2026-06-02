package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * A device that routes incoming {@link DataPacket DataPackets} according to an ordered rule list.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 * @since 17.05.2019
 */
public class TileEntityDataRouter extends TileEntityIEBase implements IDataDevice, IIIGuiMultiblockTile, IIIInventory
{
	public EasyCollection<DataRoutingRule, NBTTagCompound> routingRules;

	public TileEntityDataRouter()
	{
		super();
		this.routingRules = new EasyCollection<>(DataRoutingRule::new);
		migrateLegacyVariableRouter("0");
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("rules"))
			routingRules.deserializeNBT(nbt.getTagList("rules", 10));
		else if(nbt.hasKey("variable"))
			migrateLegacyVariableRouter(nbt.getString("variable"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setTag("rules", routingRules.serializeNBT());
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("rules"))
		{
			routingRules.deserializeNBT(message.getTagList("rules", 10));
			markDirty();
			if(world!=null)
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		}
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(world==null||world.isRemote||packet==null||side==null)
			return;

		boolean changed = false;
		for(DataRoutingRule rule : routingRules)
		{
			if(!rule.matches(packet, side))
				continue;

			rule.consumeUse();
			changed = true;

			if(rule.action==RoutingAction.DENY)
				break;

			DataPacket outgoingPacket = rule.getOutgoingPacket(packet);
			IIDataHandlingUtils.sendPacketAdjacently(outgoingPacket, world, pos, rule.outgoingSide);
		}

		if(changed)
		{
			routingRules.removeIf(DataRoutingRule::isExpired);
			markDirty();
			if(world!=null)
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		}
	}

	//--- IIIGuiMultiblockTile ---//

	@Override
	public boolean canOpenGui()
	{
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
		return IIGUI.DATA_ROUTER;
	}

	//--- IIEInventory ---//

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	public enum RoutingAction implements ILocalizedEnum
	{
		ALLOW,
		DENY;

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"data_router.action.";
		}
	}

	public static class DataRoutingRule implements INBTSerializable<NBTTagCompound>
	{
		public final boolean[] receivingSides = new boolean[6];
		public EnumFacing outgoingSide = EnumFacing.NORTH;

		public boolean useColorFilter = false;
		public EnumDyeColor packetColor = EnumDyeColor.WHITE;
		public boolean useAddressFilter = false;
		public int packetAddress = -1;

		public String requiredVariables = "";
		public boolean useValueFilter = false;
		public char valueVariable = 'a';
		public DataType expectedValue = new DataTypeNull();

		/**
		 * -1 means infinite, any positive number means request-style finite use count.
		 */
		public int usageLimit = -1;
		public RoutingAction action = RoutingAction.ALLOW;
		public boolean removeControlVariable = false;

		public DataRoutingRule()
		{
			Arrays.fill(receivingSides, true);
		}

		public boolean isJob()
		{
			return usageLimit < 0;
		}

		public boolean isExpired()
		{
			return usageLimit==0;
		}

		public void consumeUse()
		{
			if(usageLimit > 0)
				usageLimit--;
		}

		public boolean matches(@Nonnull DataPacket packet, @Nonnull EnumFacing receivedFrom)
		{
			if(isExpired()||!receivingSides[receivedFrom.ordinal()])
				return false;

			NBTTagCompound packetNBT = null;
			if(useColorFilter||useAddressFilter)
				packetNBT = packet.serializeNBT();

			if(useColorFilter)
			{
				int color = packetNBT.hasKey("color")?packetNBT.getInteger("color"): EnumDyeColor.WHITE.getMetadata();
				if(color!=packetColor.getMetadata())
					return false;
			}

			if(useAddressFilter)
			{
				int address = packetNBT.hasKey("address")?packetNBT.getInteger("address"): -1;
				if(address!=packetAddress)
					return false;
			}

			for(char variable : requiredVariables.toCharArray())
				if(DataPacket.isValidVariable(variable)&&!packet.has(variable))
					return false;

			if(useValueFilter)
			{
				if(!DataPacket.isValidVariable(valueVariable)||!packet.has(valueVariable))
					return false;

				DataType actual = packet.get(valueVariable);
				if(actual==null||actual.getClass()!=expectedValue.getClass())
					return false;

				return actual.valueToNBT().equals(expectedValue.valueToNBT());
			}

			return true;
		}

		public DataPacket getOutgoingPacket(@Nonnull DataPacket source)
		{
			DataPacket outgoing = source.clone();
			if(removeControlVariable)
			{
				if(useValueFilter&&DataPacket.isValidVariable(valueVariable))
					outgoing.remove(valueVariable);
				else
					for(char variable : requiredVariables.toCharArray())
						if(DataPacket.isValidVariable(variable))
							outgoing.remove(variable);
			}
			return outgoing;
		}

		@Nonnull
		public DataRoutingRule copy()
		{
			DataRoutingRule rule = new DataRoutingRule();
			rule.deserializeNBT(this.serializeNBT());
			return rule;
		}

		//--- NBT ---//

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			int[] receiving = new int[receivingSides.length];
			for(int i = 0; i < receivingSides.length; i++)
				receiving[i] = receivingSides[i]?1: 0;

			nbt.setIntArray("receivingSides", receiving);
			nbt.setInteger("outgoingSide", outgoingSide.ordinal());

			nbt.setBoolean("useColorFilter", useColorFilter);
			nbt.setInteger("packetColor", packetColor.getMetadata());
			nbt.setBoolean("useAddressFilter", useAddressFilter);
			nbt.setInteger("packetAddress", packetAddress);

			nbt.setString("requiredVariables", sanitizeVariables(requiredVariables, true));
			nbt.setBoolean("useValueFilter", useValueFilter);
			nbt.setString("valueVariable", String.valueOf(valueVariable));
			nbt.setTag("expectedValue", expectedValue.valueToNBT());

			nbt.setInteger("usageLimit", usageLimit);
			nbt.setString("action", action.name());
			nbt.setBoolean("removeControlVariable", removeControlVariable);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			Arrays.fill(receivingSides, true);
			int[] receiving = nbt.getIntArray("receivingSides");
			for(int i = 0; i < Math.min(receiving.length, receivingSides.length); i++)
				receivingSides[i] = receiving[i]!=0;

			outgoingSide = getFacingSafe(nbt.getInteger("outgoingSide"));

			useColorFilter = nbt.getBoolean("useColorFilter");
			packetColor = EnumDyeColor.byMetadata(nbt.getInteger("packetColor"));
			useAddressFilter = nbt.getBoolean("useAddressFilter");
			packetAddress = nbt.hasKey("packetAddress")?nbt.getInteger("packetAddress"): -1;

			requiredVariables = sanitizeVariables(nbt.getString("requiredVariables"), true);
			useValueFilter = nbt.getBoolean("useValueFilter");
			String valueVariableString = nbt.getString("valueVariable");
			valueVariable = sanitizeVariable(valueVariableString.isEmpty()?'a': valueVariableString.charAt(0), 'a');
			expectedValue = nbt.hasKey("expectedValue")?IIDataTypeUtils.getVarFromNBT(nbt.getCompoundTag("expectedValue")): new DataTypeNull();

			usageLimit = nbt.hasKey("usageLimit")?nbt.getInteger("usageLimit"): -1;
			try
			{
				action = RoutingAction.valueOf(nbt.getString("action"));
			} catch(IllegalArgumentException exception)
			{
				action = RoutingAction.ALLOW;
			}
			removeControlVariable = nbt.getBoolean("removeControlVariable");
		}

		//--- Utilities ---//

		public static String sanitizeVariables(@Nullable String variables, boolean keepUnique)
		{
			if(variables==null||variables.isEmpty())
				return "";

			StringBuilder builder = new StringBuilder();
			for(char c : variables.toCharArray())
				if(DataPacket.isValidVariable(c)&&(!keepUnique||builder.indexOf(String.valueOf(c)) < 0))
					builder.append(c);
			return builder.toString();
		}

		public static char sanitizeVariable(char variable, char fallback)
		{
			return DataPacket.isValidVariable(variable)?variable: fallback;
		}

		private static EnumFacing getFacingSafe(int ordinal)
		{
			return ordinal >= 0&&ordinal < EnumFacing.VALUES.length?EnumFacing.VALUES[ordinal]: EnumFacing.NORTH;
		}
	}

	/**
	 * Migrates the old router rules based around a single control variable
	 *
	 * @param variableName old control variable
	 */
	private void migrateLegacyVariableRouter(String variableName)
	{
		if(variableName==null||variableName.isEmpty())
			return;
		migrateLegacyVariableRouter(variableName.charAt(0));
	}

	private void migrateLegacyVariableRouter(char variable)
	{
		if(!DataPacket.isValidVariable(variable))
			return;

		routingRules.clear();
		for(EnumFacing side : EnumFacing.VALUES)
		{
			DataRoutingRule rule = new DataRoutingRule();
			rule.outgoingSide = side;
			rule.useValueFilter = true;
			rule.valueVariable = variable;
			rule.expectedValue = new DataTypeInteger(side.ordinal());
			rule.action = RoutingAction.ALLOW;
			rule.removeControlVariable = false;
			rule.usageLimit = -1;
			routingRules.add(rule);
		}
	}
}
