package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockRedstoneNetwork;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.03.2026
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityRedstoneDataInterface extends TileEntityMultiblockIIConnectable<TileEntityRedstoneDataInterface> implements IDataDevice, IRedstoneConnector, IIIGuiMultiblockTile
{
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<ConversionSetting, NBTTagCompound> dataSettings;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<ConversionSetting, NBTTagCompound> redstoneSettings;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED})
	public NonNullList<ItemStack> inventory;
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED})
	public int punchtapeReadProgress = 0;

	protected MultiblockRedstoneNetwork<TileEntityRedstoneDataInterface> redstoneNetwork;

	byte[] redstoneOutput = new byte[16];

	public TileEntityRedstoneDataInterface()
	{
		super(MultiblockRedstoneInterface.INSTANCE);
		this.dataSettings = new EasyCollection<>(ConversionSetting::new);
		this.redstoneSettings = new EasyCollection<>(ConversionSetting::new);
		this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
		this.redstoneNetwork = new MultiblockRedstoneNetwork<>(this);
	}

	@Override
	protected void dummyCleanup()
	{
		this.dataSettings = this.redstoneSettings = null;
		this.inventory = null;
		this.redstoneNetwork = null;
		this.redstoneOutput = null;
	}

	@Override
	protected void onUpdate()
	{
		//Progress punchtape reading
		ItemStack punchtapeRedstone = inventory.get(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_REDSTONE);
		ItemStack punchtapeData = inventory.get(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_DATA);

		if(!punchtapeRedstone.isEmpty()||!punchtapeData.isEmpty())
			this.punchtapeReadProgress = Math.min(this.punchtapeReadProgress+1, 100);
		else
			this.punchtapeReadProgress = 0;

		//Check for possibility to output
		if(punchtapeReadProgress >= 100&&inventory.get(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_OUTPUT).isEmpty())
		{
			punchtapeReadProgress = 0;
			//Read and transfer punchtape to output slot
			if(!punchtapeRedstone.isEmpty())
			{
				inventory.set(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_OUTPUT, processPunchtape(punchtapeRedstone, redstoneSettings));
				inventory.set(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_REDSTONE, ItemStack.EMPTY);
				reactToRedstoneChange();
			}
			else if(!punchtapeData.isEmpty())
			{
				inventory.set(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_OUTPUT, processPunchtape(punchtapeData, dataSettings));
				inventory.set(MultiblockRedstoneInterface.SLOT_PUNCHTAPE_DATA, ItemStack.EMPTY);
			}
			updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}

	}

	private ItemStack processPunchtape(ItemStack punchtape, Collection<ConversionSetting> settings)
	{
		//Write
		if(new IngredientStack("punchtapeEmpty").matchesItemStack(punchtape))
		{
			punchtape = IIContent.itemPunchtape.getStack(1);
			DataPacket packet = new DataPacket();
			//Add the settings as new variables
			char c = DataPacket.VARIABLE_NAMES[DataPacket.VARIABLE_NAMES.length-1];
			for(ConversionSetting setting : settings)
				packet.set(c = IIUtils.cycleDataPacketChars(c, true, false), setting.toDataVariable());
			IIContent.itemPunchtape.writeDataToItem(punchtape, packet);
			return punchtape;
		}
		//Read
		assert punchtape.getItem() instanceof IDataStorageItem;
		IDataStorageItem storage = (IDataStorageItem)punchtape.getItem();
		settings.clear();
		//Add the settings
		storage.getStoredData(punchtape).stream()
				.map(DataVariable::getValue)
				.filter(dataType -> dataType instanceof DataTypeMap)
				.map(dataType -> (DataTypeMap)dataType)
				.map(ConversionSetting::new)
				.forEach(settings::add);
		return punchtape;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case REDSTONE_CABLE_MOUNT:
				return getPOI("redstone");
			case DATA:
				return getPOI("data");
			default:
				return new int[0];
		}
	}

	@Override
	public boolean isRelay()
	{
		return true;
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		for(ConversionSetting setting : redstoneSettings)
			redstoneOutput[setting.getColor().getMetadata()] = setting.getRedstoneFromData(packet);
		redstoneNetwork.getNetwork().updateValues();
	}

	@Override
	public RedstoneWireNetwork getNetwork()
	{
		TileEntityRedstoneDataInterface master = master();
		return master==null?redstoneNetwork.getNetwork(): master.redstoneNetwork.getNetwork();
	}

	@Override
	public void setNetwork(RedstoneWireNetwork net)
	{
		TileEntityRedstoneDataInterface master = master();
		if(master!=null)
			master.redstoneNetwork.setNetwork(net);
	}

	@Override
	public void onChange()
	{
		TileEntityRedstoneDataInterface master = master();
		if(master!=null)
			master.reactToRedstoneChange();
	}

	private void reactToRedstoneChange()
	{
		DataPacket packet = new DataPacket();
		//Go through all the redstone->data conversion rules
		for(ConversionSetting setting : dataSettings)
		{
			byte value = redstoneOutput[setting.getColor().getMetadata()];
			packet.set(setting.getVariable(), setting.getDataFromRedstone(value));
		}
		//Send the packet
		if(!packet.isEmpty())
			sendData(packet, getDirection("data"), multiblock.getPointOfInterest("data"));
	}

	@Override
	public World getConnectorWorld()
	{
		return world;
	}

	@Override
	public void updateInput(byte[] signals)
	{
		TileEntityRedstoneDataInterface m = master();
		if(m!=null)
			for(int i = 0; i < 16; i += 1)
				if(signals[i] < m.redstoneOutput[i])
					signals[i] = m.redstoneOutput[i];

	}

	@Override
	protected boolean isMatchingCable(WireType cableType)
	{
		return Objects.equals(cableType.getCategory(), WireType.REDSTONE_CATEGORY);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5f, 0.5f, 0.5f);
	}

	@Override
	@Nonnull
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return stack.getItem()==IIContent.itemPunchtape;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	//--- IIIGuiMultiblockTile ---//

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE;
	}

	//--- Utils ---//

	public static class ConversionSetting implements INBTSerializable<NBTTagCompound>
	{
		private ConversionMode mode = ConversionMode.BOOLEAN;
		private EnumDyeColor color = EnumDyeColor.WHITE;
		private char variable = 'a';

		public ConversionSetting()
		{

		}

		public ConversionSetting(DataTypeMap settings)
		{
			if(settings.size() < 3)
				return;
			DataType mode = settings.get("mode");
			DataType color = settings.get("color");
			DataType variable = settings.get("variable");

			if(mode instanceof DataTypeString)
				this.mode = IIUtils.enumValue(ConversionMode.class, ((DataTypeString)mode).value);
			if(color instanceof DataTypeInteger)
				this.color = EnumDyeColor.byMetadata(((DataTypeInteger)color).value);
			if(variable instanceof DataTypeString&&!variable.toString().isEmpty())
			{
				char variableName = variable.toString().charAt(0);
				if(DataPacket.isValidVariable(variableName))
					this.variable = variableName;
			}
		}

		public DataTypeMap toDataVariable()
		{
			DataTypeMap map = new DataTypeMap();
			map.put("variable", new DataTypeString(String.valueOf(this.variable)));
			map.put("color", new DataTypeInteger(this.color.ordinal()));
			map.put("mode", new DataTypeString(String.valueOf(this.mode)));
			return map;
		}

		//--- Setters ---//

		public void setMode(ConversionMode mode)
		{
			this.mode = mode;
		}

		public void setColor(EnumDyeColor color)
		{
			this.color = color;
		}

		public void setVariable(char variable)
		{
			this.variable = variable;
		}

		//--- Getters ---//

		public ConversionMode getMode()
		{
			return mode;
		}

		public EnumDyeColor getColor()
		{
			return color;
		}

		public char getVariable()
		{
			return variable;
		}

		//--- Conversion Methods ---//

		/**
		 * Converts a redstone value to a data variable, based on the mode of this setting
		 *
		 * @param value the redstone value to convert
		 * @return a data variable based on the value
		 */
		private DataType getDataFromRedstone(byte value)
		{
			switch(mode)
			{
				case BOOLEAN:
					return new DataTypeBoolean(value > 0);
				case INTEGER_15:
					return new DataTypeInteger(value);
				case INTEGER_255:
					return new DataTypeInteger((int)((float)value/15f*255));
				case INTEGER_100:
					return new DataTypeInteger((int)((float)value/15f*100));
				case FLOAT:
					return new DataTypeFloat((float)value/15f);
				case STRING:
					String s;
					if(value==15)
						s = "on";
					else if(value >= 12)
						s = "high";
					else if(value >= 8)
						s = "med";
					else if(value >= 4)
						s = "low";
					else
						s = "off";
					return new DataTypeString(s);
				default:
					return new DataTypeNull();
			}
		}

		/**
		 * @param packet the packet to read from
		 * @return the redstone value to output, based on the value and the data in the packet
		 */
		private byte getRedstoneFromData(DataPacket packet)
		{
			switch(mode)
			{
				case BOOLEAN:
					return (byte)(IIDataHandlingUtils.asBoolean(variable, packet)?15: 0);
				case INTEGER_15:
					return (byte)MathHelper.clamp(IIDataHandlingUtils.asInt(variable, packet), 0, 15);
				case INTEGER_255:
					return (byte)MathHelper.clamp((float)IIDataHandlingUtils.asInt(variable, packet)/255f*15, 0, 15);
				case INTEGER_100:
					return (byte)MathHelper.clamp((float)IIDataHandlingUtils.asInt(variable, packet)/100f*15, 0, 15);
				case FLOAT:
					return (byte)MathHelper.clamp((int)(IIDataHandlingUtils.asFloat(variable, packet)*15), 0, 15);
				case STRING:
					switch(IIDataHandlingUtils.asString(variable, packet))
					{
						case "on":
							return 15;
						case "off":
							return 0;
						case "low":
							return 4;
						case "high":
							return 12;
						case "med":
							return 8;
						default:
							return 0;
					}
				default:
					return 0;
			}
		}

		//--- NBT ---//

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withEnum("mode", mode)
					.withInt("color", color.ordinal())
					.withChar("variable", variable)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			EasyNBT enbt = EasyNBT.wrapNBT(nbt);
			this.mode = enbt.getEnum("mode", ConversionMode.class);
			this.color = EnumDyeColor.byMetadata(enbt.getInt("color"));
			this.variable = enbt.getChar("variable");
		}
	}

	public enum ConversionMode implements ILocalizedEnum
	{
		BOOLEAN,
		INTEGER_15,
		INTEGER_255,
		INTEGER_100,
		FLOAT,
		STRING;

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"redstone_data_interface.mode.";
		}
	}
}
