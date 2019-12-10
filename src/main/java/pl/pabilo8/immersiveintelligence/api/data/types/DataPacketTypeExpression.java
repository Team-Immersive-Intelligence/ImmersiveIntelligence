package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.operators.arithmetic.DataOperationAdd;

/**
 * Created by Pabilo8 on 05-07-2019.
 */
public class DataPacketTypeExpression implements IDataType
{
	IDataType type1, type2;
	DataOperator operation;
	char requiredVariable = ' ';

	public DataPacketTypeExpression(IDataType type1, IDataType type2, DataOperator operation, char requiredVariable)
	{
		this.type1 = type1;
		this.type2 = type2;
		this.operation = operation;
		this.requiredVariable = requiredVariable;
	}

	public IDataType getType1()
	{
		return type1;
	}

	public IDataType getType2()
	{
		return type2;
	}

	public DataOperator getOperation()
	{
		return operation;
	}

	public char getRequiredVariable()
	{
		return requiredVariable;
	}

	public void setOperation(DataOperator operation)
	{
		this.operation = operation;
	}

	public void setRequiredVariable(char requiredVariable)
	{
		this.requiredVariable = requiredVariable;
	}

	public DataPacketTypeExpression()
	{
	}

	public IDataType getValue(DataPacket packet)
	{
		return operation.execute(packet, this);
	}

	@Override
	public String getName()
	{
		return "expression";
	}

	@Override
	public String valueToString()
	{
		return type1.valueToString()+" "+operation+" "+type2.valueToString();
	}

	@Override
	public void setDefaultValue()
	{
		this.type1 = new DataPacketTypeNull();
		this.type2 = new DataPacketTypeNull();
		this.operation = new DataOperationAdd();
		this.requiredVariable = ' ';
	}

	@Override
	public void valueFromNBT(NBTTagCompound nbt)
	{
		setDefaultValue();

		if(((NBTTagCompound)nbt.getTag("Value1")).hasKey("Type"))
		{
			try
			{
				type1 = (IDataType)DataPacket.varTypes.get(((NBTTagCompound)nbt.getTag("Value1")).getString("Type")).newInstance();
			} catch(InstantiationException e)
			{
				e.printStackTrace();
			} catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}
			type1.valueFromNBT(((NBTTagCompound)nbt.getTag("Value1")));
		}
		else
		{
			type1 = new DataPacketTypeNull();
		}

		//type1.valueFromNBT(nbt.getCompoundTag("Value1"));

		if(((NBTTagCompound)nbt.getTag("Value2")).hasKey("Type"))
		{
			try
			{
				type2 = (IDataType)DataPacket.varTypes.get(((NBTTagCompound)nbt.getTag("Value2")).getString("Type")).newInstance();
			} catch(InstantiationException e)
			{
				e.printStackTrace();
			} catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}
			type2.valueFromNBT(((NBTTagCompound)nbt.getTag("Value2")));
		}
		else
		{
			type2 = new DataPacketTypeNull();
		}

		requiredVariable = nbt.getString("requiredVariable").charAt(0);

		String type = nbt.getString("Operation");
		if(DataOperation.operations.containsKey(type))
		{
			DataOperator data;
			try
			{
				data = (DataOperator)DataOperation.operations.get(type).newInstance();
				operation = data;
				return;
			} catch(InstantiationException e)
			{
				e.printStackTrace();
			} catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}

		}
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Type", "expression");

		nbt.setTag("Value1", type1.valueToNBT());
		nbt.setTag("Value2", type2.valueToNBT());

		nbt.setString("Operation", operation.name);
		nbt.setString("requiredVariable", String.valueOf(requiredVariable));

		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x2a4db4;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 4;
	}
}
