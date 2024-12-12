package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataTypeExpression extends DataType
{
	public DataType[] data = DataOperationNull.INSTANCE_TYPES;
	DataOperation operation = DataOperationNull.INSTANCE;
	DataOperationMeta meta = DataOperationNull.INSTANCE_META;
	char requiredVariable = ' ';

	public DataTypeExpression(DataType[] data, DataOperation operation, char requiredVariable)
	{
		this.data = data;
		this.operation = operation;
		this.meta = operation.getMeta();
		this.requiredVariable = requiredVariable;
	}

	public DataTypeExpression()
	{

	}

	public DataType getArgument(int index)
	{
		return data[index%data.length];
	}

	public DataOperation getOperation()
	{
		return operation;
	}

	public DataOperationMeta getMeta()
	{
		return meta;
	}

	public char getRequiredVariable()
	{
		return requiredVariable;
	}

	public void setOperation(@Nonnull DataOperation operation)
	{
		this.operation = operation;
		this.meta = operation.getMeta();
		Class<? extends DataType>[] allowedTypes = meta.allowedTypes();

		DataType[] newData = new DataType[allowedTypes.length];

		if(data!=null)
			for(int i = 0; i < Math.min(allowedTypes.length, data.length); i++)
			{
				DataType t1 = getArgument(i);
				if(t1.getClass()==DataTypeAccessor.class||allowedTypes[i].isAssignableFrom(t1.getClass()))
					newData[i] = t1;
			}

		for(int i = 0; i < allowedTypes.length; i++)
			if(newData[i]==null)
				newData[i] = IIDataTypeUtils.getVarInstance(allowedTypes[i]);

		this.data = newData;
	}

	public void setRequiredVariable(char requiredVariable)
	{
		this.requiredVariable = requiredVariable;
	}

	public DataType getValue(DataPacket packet)
	{
		return operation.execute(packet, this);
	}

	@Override
	public void valueFromNBT(NBTTagCompound nbt)
	{
		this.operation = IIDataOperationUtils.getOperationInstance(nbt.getString("Operation"));
		this.meta = this.operation.getMeta();
		this.requiredVariable = nbt.getString("requiredVariable").charAt(0);

		this.data = new DataType[meta.allowedTypes().length];
		for(int i = 0; i < meta.allowedTypes().length; i++)
			this.data[i] = IIDataTypeUtils.getVarFromNBT(nbt.getCompoundTag("Value"+(i+1)));
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();

		nbt.setString("Operation", meta.name());
		nbt.setString("requiredVariable", String.valueOf(requiredVariable));

		for(int i = 0; i < meta.allowedTypes().length; i++)
			nbt.setTag("Value"+(i+1), data[i].valueToNBT());

		return nbt;
	}

	@Override
	public String toString()
	{
		String symbol = operation.getMeta().expression();
		if(!symbol.isEmpty())
			return String.format(symbol, Arrays.stream(data).map(DataType::toString).toArray());
		return operation.getMeta().name();
	}
}
