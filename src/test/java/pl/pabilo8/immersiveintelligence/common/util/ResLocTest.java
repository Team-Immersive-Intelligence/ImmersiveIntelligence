package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResLocTest
{
	final static ResLoc domain = ResLoc.root("domain");
	final static ResLoc resourceOf = ResLoc.of(domain, "path/to/resource.png");
	final static ResLoc resourceWith = domain.with("path/to/resource.png");

	@Test
	public void ofEqualsWith()
	{
		assertEquals(resourceOf, resourceWith);
	}

	@Test
	@BeforeEach
	public void testRoot()
	{
		ResLoc rootResLoc = ResLoc.root("domain");
		assertEquals("domain", rootResLoc.getResourceDomain());
		assertEquals("", rootResLoc.getResourcePath());
	}

	@Test
	public void testConstructorAndBasicProperties()
	{
		assertEquals("domain", resourceOf.getResourceDomain());
		assertEquals("path/to/resource.png", resourceOf.getResourcePath());
		assertEquals(".png", resourceOf.getExtension());
	}

	@Test
	public void testWithDomain()
	{
		ResLoc newResLoc = resourceOf.withDomain("newdomain");
		assertEquals("newdomain", newResLoc.getResourceDomain());
		assertEquals("path/to/resource.png", newResLoc.getResourcePath());
	}

	@Test
	public void testAsDirectory()
	{
		ResLoc dirResLoc = resourceOf.asDirectory();
		assertEquals("path/to/", dirResLoc.getResourcePath());
	}

	@Test
	public void testWithExtension()
	{
		ResLoc resLoc = resourceOf.withExtension(".json");
		ResLoc newResLoc = resLoc.withExtension(".json");
		assertEquals("path/to/resource.json", newResLoc.getResourcePath());
		assertEquals(".json", newResLoc.getExtension());
	}

	@Test
	public void testReplace()
	{
		ResLoc newResLoc = resourceOf.replace("resource", "newresource");
		assertEquals("path/to/newresource.png", newResLoc.getResourcePath());
	}

	@Test
	public void testGetModelResLoc()
	{
		ResLoc resLoc = ResLoc.of(domain, "path/to/resource");
		ModelResourceLocation modelResLoc = resLoc.getModelResLoc();
		assertEquals("domain:path/to/resource#normal", modelResLoc.toString());
	}
}
