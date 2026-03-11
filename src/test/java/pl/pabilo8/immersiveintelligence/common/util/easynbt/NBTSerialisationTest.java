package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NBTSerialisationTest
{
	@Test
	public void testSerializerCorrectTypes() throws Exception
	{
		//Create reflections scanner to find annotated fields
		IILogger.logger = LogManager.getLogger("immersiveintelligence-test");
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("pl.pabilo8.immersiveintelligence"))
				.filterInputsBy(new FilterBuilder().excludePattern(".*com/elytradev/mirage/lighting/ILightEventConsumer.*"))
				.setScanners(Scanners.FieldsAnnotated));

		//Find all fields annotated with @SyncNBT
		Set<Field> syncNBTFields = reflections.getFieldsAnnotatedWith(SyncNBT.class);
		NBTSerialisation.preInit();
		NBTSerialisation.postInit();

		//Get access to the serializerRegistry field using reflection
		Field serializerRegistryField = NBTSerialisation.class.getDeclaredField("serializerRegistry");
		serializerRegistryField.setAccessible(true);

		//Get the serializerRegistry map
		@SuppressWarnings("unchecked")
		Set<Class<?>> registeredTypes = ((java.util.HashMap<Class<?>, ?>)serializerRegistryField.get(null)).keySet();

		//Find fields that don't have a serializer
		List<Field> fieldsWithoutSerializer = new ArrayList<>();

		for(Field field : syncNBTFields)
		{
			boolean hasSerializer;
			Class<?> fieldType = field.getType();

			// Check if any registered serializer can handle this field type
			hasSerializer = registeredTypes.stream()
					.anyMatch(registeredType -> registeredType.isAssignableFrom(fieldType));

			if(!hasSerializer)
				fieldsWithoutSerializer.add(field);
		}

		//Generate detailed error message if fields without serializer found
		if(!fieldsWithoutSerializer.isEmpty())
		{
			StringBuilder errorMessage = new StringBuilder("The following @SyncNBT annotated fields don't have a registered serializer:\n");

			for(Field field : fieldsWithoutSerializer)
				errorMessage.append("- ")
						.append(field.getDeclaringClass().getName())
						.append(".")
						.append(field.getName())
						.append(" (type: ")
						.append(field.getType().getName())
						.append(")\n");

			// List available serializers for reference
			errorMessage.append("\nAvailable serializers are registered for types:\n");
			for(Class<?> type : registeredTypes)
				errorMessage.append("- ").append(type.getName()).append("\n");

			assertTrue(fieldsWithoutSerializer.isEmpty(), errorMessage.toString());
		}
	}
}
