package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataOperations;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.operations.arithmetic.DataOperationAdd;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataTypeExpression implements IDataType
{
	public IDataType[] data;
	DataOperation operation;
	char requiredVariable = ' ';

	public DataTypeExpression(IDataType[] data, DataOperation operation, char requiredVariable)
	{
		this.data = data;
		this.operation = operation;
		this.requiredVariable = requiredVariable;
	}

	public IDataType getArgument(int index)
	{
		return data[index%data.length];
	}

	public DataOperation getOperation()
	{
		return operation;
	}

	public char getRequiredVariable()
	{
		return requiredVariable;
	}

	public void setOperation(@Nonnull DataOperation operation)
	{
		this.operation = operation;

		IDataType[] newData = new IDataType[operation.allowedTypes.length];

		if(data!=null)
			for(int i = 0; i < Math.min(operation.allowedTypes.length, data.length); i++)
			{
				IDataType t1 = getArgument(i);
				if(t1.getClass()==DataTypeAccessor.class||operation.allowedTypes[i].isAssignableFrom(t1.getClass()))
					newData[i] = t1;
			}

		for(int i = 0; i < operation.allowedTypes.length; i++)
			if(newData[i]==null)
				newData[i] = DataPacket.getVarInstance(operation.allowedTypes[i]);

		this.data = newData;
	}

	public void setRequiredVariable(char requiredVariable)
	{
		this.requiredVariable = requiredVariable;
	}

	public DataTypeExpression()
	{
	}

	public IDataType getValue(DataPacket packet)
	{
		return operation.execute(packet, this);
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "expression";
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		if(operation.expression!=null)
			return String.format(operation.expression, Arrays.stream(data).map(IDataType::valueToString).toArray());
		return operation.name;
	}

	@Override
	public void setDefaultValue()
	{
		this.operation = new DataOperationAdd();
		this.requiredVariable = ' ';
		data = new IDataType[operation.allowedTypes.length];
		for(int i = 0; i < operation.allowedTypes.length; i++)
			data[i] = DataPacket.getVarInstance(operation.allowedTypes[i]);
	}

	@Override
	public void valueFromNBT(NBTTagCompound nbt)
	{
		setDefaultValue();

		operation = DataOperations.getOperationInstance(nbt.getString("Operation"));
		requiredVariable = nbt.getString("requiredVariable").charAt(0);

		data = new IDataType[operation.allowedTypes.length];
		for(int i = 0; i < operation.allowedTypes.length; i++)
			data[i] = DataPacket.getVarFromNBT(nbt.getCompoundTag("Value"+(i+1)));
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();

		nbt.setString("Operation", operation.name);
		nbt.setString("requiredVariable", String.valueOf(requiredVariable));

		for(int i = 0; i < operation.allowedTypes.length; i++)
			nbt.setTag("Value"+(i+1), data[i].valueToNBT());

		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x2a4db4;
	}
}
