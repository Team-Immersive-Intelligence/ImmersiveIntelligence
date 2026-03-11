package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class SyncNBTTest
{
	@Test
	public void testSyncNBTFieldsArePublic()
	{
		//Scan using Reflections to find the annotated classes
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("pl.pabilo8.immersiveintelligence"))
				//Nie wolno, nie można, nie potrzeba nam tego
				.filterInputsBy(new FilterBuilder().excludePattern(".*com/elytradev/mirage/lighting/ILightEventConsumer.*"))
				.setScanners(Scanners.FieldsAnnotated));
		Set<Field> syncNBTFields = reflections.getFieldsAnnotatedWith(SyncNBT.class);

		//Skip test if no SyncNBT fields were found
		Assumptions.assumeFalse(syncNBTFields.isEmpty(), "No fields with @SyncNBT annotation found. Skipping test.");

		//Collect fields that are not public
		List<Field> nonPublicFields = new ArrayList<>();
		for(Field field : syncNBTFields)
			if(!Modifier.isPublic(field.getModifiers()))
				nonPublicFields.add(field);

		//Generate error message
		StringBuilder errorMessage = new StringBuilder();
		if(!nonPublicFields.isEmpty())
		{
			errorMessage.append("Following @SyncNBT annotated fields are not public:\n");
			for(Field field : nonPublicFields)
				errorMessage.append("- ")
						.append(field.getDeclaringClass().getName())
						.append(".")
						.append(field.getName())
						.append("\n");
		}
		assertFalse(nonPublicFields.isEmpty(), errorMessage.toString());
	}
}
