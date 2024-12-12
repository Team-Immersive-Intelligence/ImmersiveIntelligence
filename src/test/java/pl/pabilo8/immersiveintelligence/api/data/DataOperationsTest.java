package pl.pabilo8.immersiveintelligence.api.data;

import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.12.2024
 **/
public class DataOperationsTest
{
	@Test
	public void testUniqueOperationNamesAndExpressions()
	{
		Set<String> names = new HashSet<>();
		Set<String> expressions = new HashSet<>();

		for(String operationName : IIDataOperationUtils.getAllOperationNames())
		{
			DataOperationMeta meta = IIDataOperationUtils.getOperationMeta(operationName);

			// Check for unique names
			assertTrue(names.add(meta.name()), "Duplicate operation name found: "+meta.name());

			// Check for unique expressions if applicable
			if(!meta.expression().isEmpty())
				assertTrue(expressions.add(meta.expression()), "Duplicate operation expression found: "+meta.expression());
		}
	}


	@Test
	public void testOperationNamesInLangFile() throws IOException
	{
		// Load the en_us.lang file
		ClassLoader classLoader = DataOperationsTest.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("assets/immersiveintelligence/lang/en_us.lang");
		assert inputStream!=null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> langFileLines = reader.lines().collect(Collectors.toList());

		// Extract all keys from the lang file
		Set<String> langKeys = langFileLines.stream()
				.filter(line -> line.contains("="))
				.map(line -> line.split("=")[0].trim())
				.collect(Collectors.toSet());

		// Check each operation
		for(String operationName : IIDataOperationUtils.getAllOperationNames())
		{
			DataOperationMeta meta = IIDataOperationUtils.getOperationMeta(operationName);
			String expectedKey = "datasystem.immersiveintelligence.function."+meta.name();
			assertTrue(langKeys.contains(expectedKey), "Missing lang entry for operation: "+meta.name());
		}
	}
}
