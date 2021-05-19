package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIFunctionalCircuit extends ItemIIBase implements IDataStorageItem
{
	public ItemIIFunctionalCircuit()
	{
		super("circuit_functional", 1, "arithmetic", "advanced_arithmetic", "logic", "comparator", "advanced_logic", "text", "itemstack");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		list.add(I18n.format(CommonProxy.DESCRIPTION_KEY+"functional_circuit"));
		for(String s : getOperationsList(stack))
		{
			list.add("-"+I18n.format(CommonProxy.DATA_KEY+"function."+s));
		}
	}

	@Override
	public DataPacket getStoredData(ItemStack stack)
	{
		NBTTagCompound tag = stack.serializeNBT();
		NBTTagCompound realtag = ItemNBTHelper.getTagCompound(stack, "operations");
		DataPacket data = new DataPacket();
		data.fromNBT(realtag);
		return data;
	}

	@Override
	public void writeDataToItem(DataPacket packet, ItemStack stack)
	{
		ItemNBTHelper.setTagCompound(stack, "operations", packet.toNBT());
	}

	@Override
	public String getDataStorageItemType(ItemStack stack)
	{
		return DataHelper.DATA_STORAGE_TYPE_CIRCUIT_FUNCTIONAL;
	}


	public List<String> getOperationsList(ItemStack stack)
	{
		ArrayList<String> ops = new ArrayList<>();
		switch(stack.getMetadata())
		{
			//Arithmetical
			case 0:
			{
				ops.add("add");
				ops.add("subtract");
				ops.add("multiply");
				ops.add("divide");
				ops.add("modulo");
			}
			break;
			//Advanced Arithmetical
			case 1:
			{
				ops.add("add");
				ops.add("subtract");
				ops.add("multiply");
				ops.add("divide");
				ops.add("modulo");
				ops.add("power");
				ops.add("root");
				ops.add("min");
				ops.add("max");
			}
			break;
			//Logic
			case 2:
			{
				ops.add("and");
				ops.add("or");
				ops.add("not");
			}
			break;
			case 3:
			{
				ops.add("greater");
				ops.add("less");
				ops.add("greater_or_equal");
				ops.add("less_or_equal");
				ops.add("equal");
			}
			break;
			//Advanced Logic
			case 4:
			{
				ops.add("and");
				ops.add("or");
				ops.add("not");
				ops.add("nand");
				ops.add("nor");
				ops.add("xor");
				ops.add("xnor");
			}
			break;
			//Text Processing
			case 5:
			{
				ops.add("join");
				//ops.add("filter");
				//ops.add("format");
			}
			break;
			//ItemStack
			case 6:
			{
				ops.add("get_quantity");
				ops.add("set_quantity");
				ops.add("get_durability");
				ops.add("set_durability");
				ops.add("get_nbt");
				ops.add("set_nbt");
				ops.add("can_stack_with");
				ops.add("matches_oredict");
			}
			break;
		}
		return ops;
	}

	public String getTESRRenderTexture(ItemStack stack)
	{
		switch(stack.getMetadata())
		{
			case 0:
			case 5:
			case 3:
				return "electronic_circuits";
			case 1:
			case 6:
			case 4:
				return "advanced_circuits";
			case 2:
				return "redstone_circuits";
		}
		return "";
	}
}
