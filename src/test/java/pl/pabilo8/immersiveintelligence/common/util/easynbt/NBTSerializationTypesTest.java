package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.util.TriConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation.NBTSerializer;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.test.ListAppender;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.08.2025
 */
@SuppressWarnings("unchecked")
public class NBTSerializationTypesTest
{
	private TestClass testClassA, testClassB;
	private ListAppender listAppender;

	@BeforeEach
	public void init()
	{
		testClassA = new TestClass();
		testClassB = new TestClass();

		//Set all fields to be accessible (some runners may error, because it's a class in the test project module)
		for(Field declaredField : TestClass.class.getDeclaredFields())
			declaredField.setAccessible(true);

		//Create a list appender, which can be queried for exceptions logged
		IILogger.logger = LogManager.getLogger("immersiveintelligence-test");
		listAppender = new ListAppender();
		listAppender.start();

		//Clear default appenders (into terminal) and add a custom one
		Logger logger = (Logger)IILogger.logger;
		Collection<Appender> appenders = logger.getAppenders().values();
		appenders.forEach(logger::removeAppender);
		logger.addAppender(listAppender);
	}

	public void serializeFor(TriConsumer<NBTTagCompound, NBTSerializer, TestClass> action)
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(testClassA, (serializer, testClass) -> action.accept(tag, serializer, testClass));
		NBTSerialisation.synchroniseFor(testClassB, (serializer, object) -> serializer.deserializeAll(object, tag, true));

		//Log fields that could not serialize
		String collected = listAppender.getEvents().stream()
				.filter(logEvent -> logEvent.getLevel().isMoreSpecificThan(Level.INFO))
				.map(logEvent -> logEvent.getMessage()+"\n")
				.collect(Collectors.joining());
		if(!collected.isEmpty())
			Assertions.fail("Serialization failed, reason:\n"+collected);
	}

	@Test
	public void testSerializersAll()
	{
		//Modify class
		testClassA.anInt *= 4;
		testClassA.aBoolean = false;
		testClassA.aFloat *= 2;
		testClassA.aString += " modified";

		//Serialize and deserialize
		serializeFor((tag, serializer, object) -> serializer.serializeAll(object, tag));

		//Check for correct serialization
		Assertions.assertEquals(testClassA.anInt, testClassB.anInt);
		Assertions.assertEquals(testClassA.aBoolean, testClassB.aBoolean);
		Assertions.assertEquals(testClassA.aFloat, testClassB.aFloat, 0.001);
		Assertions.assertEquals(testClassA.aString, testClassB.aString);
	}

	@Test
	public void testSerializersForTime()
	{
		//Modify class
		testClassA.anInt *= 2;
		testClassA.aBoolean = false;
		testClassA.aFloat *= 3;

		//Serialize and deserialize
		serializeFor((tag, serializer, object) -> serializer.serializeForTime(object, tag, 10));

		//Check for correct serialization
		Assertions.assertEquals(testClassA.anInt, testClassB.anInt);
		Assertions.assertEquals(testClassA.aBoolean, testClassB.aBoolean);
		Assertions.assertNotEquals(testClassA.aFloat, testClassB.aFloat, 0.001);
	}

	@Test
	public void testSerializersForEvent()
	{
		testClassA.anInt *= 2;
		testClassA.aBoolean = false;
		testClassA.aFloat *= 3;
		testClassA.aString += " modified";

		//Serialize for TILE_CUSTOM1 event, float and string fields should not be modified
		serializeFor((tag, serializer, object) -> serializer.serializeForEvent(object, tag, SyncEvents.TILE_CUSTOM1));
		Assertions.assertEquals(testClassA.anInt, testClassB.anInt);
		Assertions.assertEquals(testClassA.aBoolean, testClassB.aBoolean);
		Assertions.assertNotEquals(testClassA.aFloat, testClassB.aFloat, 0.001);
		Assertions.assertNotEquals(testClassA.aString, testClassB.aString);

		//Serialize for TILE_CUSTOM2 event, only the string field should remain unchanged
		serializeFor((tag, serializer, object) -> serializer.serializeForEvent(object, tag, SyncEvents.TILE_CUSTOM2));
		Assertions.assertEquals(testClassA.anInt, testClassB.anInt);
		Assertions.assertEquals(testClassA.aBoolean, testClassB.aBoolean);
		Assertions.assertEquals(testClassA.aFloat, testClassB.aFloat, 0.001);
		Assertions.assertNotEquals(testClassA.aString, testClassB.aString);
	}

	public static class TestClass
	{
		@SyncNBT(time = 5, events = SyncEvents.TILE_CUSTOM1)
		public int anInt = 50;
		@SyncNBT
		public String aString = "test";
		@SyncNBT(time = 3, events = SyncEvents.TILE_CUSTOM2)
		public float aFloat = 3.14f;
		@SyncNBT(time = 10, events = SyncEvents.TILE_CUSTOM1)
		public boolean aBoolean = true;
	}
}
