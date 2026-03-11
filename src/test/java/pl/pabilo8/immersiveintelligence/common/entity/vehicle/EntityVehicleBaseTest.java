package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import com.ibm.icu.impl.Assert;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 05.10.2025
 */
class EntityVehicleBaseTest
{
	@Test
	void entityInit()
	{
		ArrayList<String> failedClasses = new ArrayList<>();

		//Scan using Reflections to find the annotated classes
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("pl.pabilo8.immersiveintelligence.common.entity"))
				//Nie wolno, nie można, nie potrzeba nam tego
				.filterInputsBy(new FilterBuilder().excludePattern(".*com/elytradev/mirage/lighting/ILightEventConsumer.*"))
				.setScanners(Scanners.SubTypes));

		//Check all subclasses of EntityVehicleBase
		//noinspection rawtypes
		for(Class<? extends EntityVehicleBase> c : reflections.getSubTypesOf(EntityVehicleBase.class))
			if(!c.isAnnotationPresent(VehicleBlueprint.class)&&!Modifier.isAbstract(c.getModifiers()))
				failedClasses.add(c.getName());

		//If there are any failed classes, fail the test with a message
		if(!failedClasses.isEmpty())
		{
			StringBuilder reason = new StringBuilder("The following classes are missing the @VehicleBlueprint annotation:");
			for(String klass : failedClasses)
				reason.append("\n\t - ").append(klass);
			Assert.fail(reason.toString());
		}

	}
}
