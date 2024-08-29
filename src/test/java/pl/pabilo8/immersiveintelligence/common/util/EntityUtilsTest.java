package pl.pabilo8.immersiveintelligence.common.util;

import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;
import pl.pabilo8.immersiveintelligence.test.GameTestWorld;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityUtilsTest extends GameTestWorld
{
	EntitySheep entity;

	@BeforeEach
	public void setUp()
	{
		FakeWorldServer world = testManager.getWorld(0);
		entity = new EntitySheep(world);
		entity.setPosition(0, 0, 0);
		world.spawnEntity(entity);
	}

	@Test
	public void testGetEntityCenter()
	{
		float width = entity.width;
		float height = entity.height;
		Vec3d center = entity.getPositionVector().addVector(-width/2, height/2, -width/2);

		assertEquals(center, IIEntityUtils.getEntityCenter(entity));
	}

	@Test
	public void testGetFacingBetweenPos()
	{
		BlockPos fromPos = new BlockPos(1, 2, 3);
		HashMap<EnumFacing, BlockPos> toPos = new HashMap<>();

		toPos.put(EnumFacing.NORTH, new BlockPos(1, 4, -9));
		toPos.put(EnumFacing.EAST, new BlockPos(5, 2, 2));
		toPos.put(EnumFacing.UP, new BlockPos(1, 9, 2));

		toPos.forEach((facing, pos) -> {
			EnumFacing result = IIEntityUtils.getFacingBetweenPos(fromPos, pos).getOpposite();
			assertEquals(facing, result);
		});
	}

	@Test
	public void testSetEntityVelocity()
	{
		IIEntityUtils.setEntityMotion(entity, 1.0, 2.0, 3.0);
		assertEquals(1.0, entity.motionX);
		assertEquals(2.0, entity.motionY);
		assertEquals(3.0, entity.motionZ);
	}

	@Test
	public void testGetEntityMotion()
	{
		entity.motionX = 1.0;
		entity.motionY = 2.0;
		entity.motionZ = 3.0;
		Vec3d motion = IIEntityUtils.getEntityMotion(entity);
		assertEquals(new Vec3d(1.0, 2.0, 3.0), motion);
	}
}