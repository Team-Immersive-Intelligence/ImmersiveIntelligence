package pl.pabilo8.immersiveintelligence.api.data.operations;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer.Cryptographer;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IterableDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Verifies registered data operations and their metadata contracts.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @since 09.08.2026
 */
public class DataOperationTest extends GameTestBasic
{
	/**
	 * Registers the data system before the tests.
	 */
	@BeforeAll
	public static void init()
	{
		IIDataOperationUtils.registerDataOperations();
		IIDataTypeUtils.registerDataTypes();
	}

	/**
	 * @return one metadata contract test for each registered operation
	 */
	@TestFactory
	public Stream<DynamicTest> operationMetadataMatchesExecution()
	{
		return IIDataOperationUtils.getAllOperationNames()
				.stream()
				.sorted()
				.map(name -> dynamicTest(name, () -> verifyOperation(name)));
	}

	/**
	 * Checks that every registered operation is available from a functional circuit.
	 */
	@Test
	public void registeredOperationsAreAvailableFromCircuits()
	{
		Set<String> registered = new HashSet<>(IIDataOperationUtils.getAllOperationNames());
		Set<String> available = Arrays.stream(Circuits.values())
				.flatMap(circuit -> Arrays.stream(circuit.getFunctions()))
				.collect(Collectors.toSet());

		assertEquals(registered, available, "Functional circuits and registered data operations differ");
	}

	private static void verifyOperation(String name)
	{
		DataOperation operation = IIDataOperationUtils.getOperationInstance(name);
		DataOperationMeta meta = operation.getMeta();

		assertNotNull(meta, "Operation metadata is missing: "+name);
		assertEquals(name, meta.name(), "Registered operation name differs from metadata");
		assertEquals(meta.allowedTypes().length, meta.params().length, "Parameter metadata differs for "+name);

		DataType[] arguments = Arrays.stream(meta.allowedTypes())
				.map(DataOperationTest::createArgument)
				.toArray(DataType[]::new);
		prepareArguments(name, arguments);

		DataType result = operation.execute(new DataPacket(), new DataTypeExpression(arguments, operation, ' '));
		assertNotNull(result, "Operation returned Java null: "+name);
		assertTrue(meta.expectedResult().isInstance(result),
				"Operation "+name+" declares "+meta.expectedResult().getSimpleName()+" but returned "+result.getClass().getSimpleName());

		if(!meta.resultMatters())
		{
			assertEquals(DataTypeNull.class, meta.expectedResult(), "Void operation must declare DataTypeNull: "+name);
			assertTrue(result instanceof DataTypeNull, "Void operation must return DataTypeNull: "+name);
		}
	}

	private static DataType createArgument(Class<? extends DataType> type)
	{
		if(type==DataType.class)
			return new DataTypeString("2");
		if(type==NumericDataType.class)
			return new DataTypeInteger(2);
		if(type==IterableDataType.class)
			return createArray();
		if(type==DataTypeInteger.class)
			return new DataTypeInteger(1);
		if(type==DataTypeFloat.class)
			return new DataTypeFloat(2f);
		if(type==DataTypeBoolean.class)
			return new DataTypeBoolean(true);
		if(type==DataTypeString.class)
			return new DataTypeString("test");
		if(type==DataTypeArray.class)
			return createArray();
		if(type==DataTypeMap.class)
			return new DataTypeMap(new DataTypeString("test"), new DataTypeInteger(2));
		if(type==DataTypeItemStack.class)
			return createItemStack();
		if(type==DataTypeFluidStack.class)
			return new DataTypeFluidStack(new FluidStack(FluidRegistry.WATER, 1000));
		if(type==DataTypeEntity.class)
		{
			DataTypeEntity entity = new DataTypeEntity();
			entity.entityID = 1;
			entity.dimensionID = 0;
			entity.entityClass = "test:entity";
			entity.customName = "Test Entity";
			entity.lastPos = new Vec3d(1, 2, 3);
			return entity;
		}
		if(type==DataTypeVector.class)
			return new DataTypeVector(1, 2, 3);
		if(type==DataTypeEncrypted.class)
			return new DataTypeEncrypted(Cryptographer.encrypt("2", "test"));

		return IIDataTypeUtils.getVarInstance(type);
	}

	private static DataTypeItemStack createItemStack()
	{
		Item item = new Item().setRegistryName(new ResourceLocation("minecraft", "operation_test_item"));
		ItemStack stack = new ItemStack(item);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList pages = new NBTTagList();
		pages.appendTag(new NBTTagString("{\"text\":\"Test page one\"}"));
		pages.appendTag(new NBTTagString("{\"text\":\"Test page two\"}"));
		pages.appendTag(new NBTTagString("{\"text\":\"Test page three\"}"));
		tag.setTag("pages", pages);
		tag.setString("author", "Pabilo8");
		tag.setString("title", "Test Document");
		stack.setTagCompound(tag);
		return new DataTypeItemStack(stack);
	}

	private static DataTypeArray createArray()
	{
		return new DataTypeArray(
				new DataTypeString("test"),
				new DataTypeString("other"),
				new DataTypeString("third")
		);
	}

	private static void prepareArguments(String name, DataType[] arguments)
	{
		if(name.equals("item_set_nbt")||name.equals("fluid_set_nbt"))
			arguments[1] = new DataTypeString("{test:1}");
	}
}
