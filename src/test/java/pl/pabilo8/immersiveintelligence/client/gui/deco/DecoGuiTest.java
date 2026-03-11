package pl.pabilo8.immersiveintelligence.client.gui.deco;

import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.08.2025
 */
public class DecoGuiTest
{
	@Test
	public void testAnnotationPresent()
	{
		//Scan for all classes extending DecoGui
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("pl.pabilo8.immersiveintelligence"))
				.filterInputsBy(new FilterBuilder().excludePattern(".*com/elytradev/mirage/lighting/ILightEventConsumer.*"))
				.setScanners(Scanners.SubTypes));
		Set<Class<? extends DecoGui>> decoGuiClasses = reflections.getSubTypesOf(DecoGui.class);

		List<String> errors = new ArrayList<>();

		for(Class<? extends DecoGui> decoGuiClass : decoGuiClasses)
		{
			//Skip abstract classes
			if(Modifier.isAbstract(decoGuiClass.getModifiers()))
				continue;

			//Check if the class has the @DecoTemplate annotation
			if(decoGuiClass.getAnnotation(DecoTemplate.class)==null)
				errors.add("Class "+decoGuiClass.getSimpleName()+" should have @DecoTemplate annotation");

			//Check all fields for ResourceLocation or ResLoc type and @DecoResource annotation
			for(Field field : decoGuiClass.getDeclaredFields())
			{
				Class<?> fieldType = field.getType();
				if(fieldType.equals(ResourceLocation.class)||fieldType.equals(ResLoc.class))
					if(field.getAnnotation(DecoResource.class)==null)
						errors.add("Field "+field.getName()+" in "+decoGuiClass.getSimpleName()
								+" should have @DecoResource annotation");
			}
		}

		//If there are any errors, fail the test and display all errors
		if(!errors.isEmpty())
			fail("Annotation validation failed:\n"+String.join("\n", errors));
	}
}
