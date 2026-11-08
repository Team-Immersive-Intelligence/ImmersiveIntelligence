package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.energy.DieselHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DieselEngine;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockDieselEngine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Diesel Engine fuel use, rotary output and client input.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.10.2026
 * @since 14.10.2026
 */
class TileEntityDieselEngineTest extends GameTestBasic
{
	private static Fluid fuel;

	@BeforeAll
	static void initialiseEngine()
	{
		new MultiblockDieselEngine();
		IILogger.logger = LogManager.getLogger("DieselEngineTest");
		fuel = new Fluid("ii_test_diesel_engine", new ResourceLocation("minecraft", "blocks/water_still"),
				new ResourceLocation("minecraft", "blocks/water_flow"));
		FluidRegistry.registerFluid(fuel);
	}

	@Test
	void usesOneQuarterOfGeneratorFuel()
	{
		DieselHandler.registerFuel(fuel, 200);
		TileEntityDieselEngine tile = engineWithFuel(1000);
		for(int tick = 0; tick < 800; tick++)
			assertTrue(tile.consumeFuel());
		assertEquals(0, tile.fuelTank.getFluidAmount());
		assertFalse(tile.consumeFuel());
	}

	@Test
	void accumulatesFractionalFuelUseBetweenTicks()
	{
		DieselHandler.registerFuel(fuel, 1000);
		TileEntityDieselEngine tile = engineWithFuel(10);
		assertTrue(tile.consumeFuel());
		assertTrue(tile.consumeFuel());
		assertEquals(0.5, tile.fuelRemainder);
		assertEquals(10, tile.fuelTank.getFluidAmount());
		assertTrue(tile.consumeFuel());
		assertTrue(tile.consumeFuel());
		assertEquals(0, tile.fuelRemainder);
		assertEquals(9, tile.fuelTank.getFluidAmount());
	}

	@Test
	void longBurnTimesStillConsumeFuel()
	{
		DieselHandler.registerFuel(fuel, 2000);
		TileEntityDieselEngine tile = engineWithFuel(1);
		for(int tick = 0; tick < 8; tick++)
			assertTrue(tile.consumeFuel());
		assertFalse(tile.consumeFuel());
	}

	@Test
	void rejectsRemovedAndNonPositiveFuels()
	{
		TileEntityDieselEngine tile = engineWithFuel(10);
		DieselHandler.removeFuel(fuel);
		assertFalse(tile.consumeFuel());
		DieselHandler.registerFuel(fuel, 0);
		assertFalse(tile.consumeFuel());
		DieselHandler.registerFuel(fuel, -1);
		assertFalse(tile.consumeFuel());
		assertEquals(10, tile.fuelTank.getFluidAmount());
	}

	@Test
	void ignoresClientSuppliedEngineState()
	{
		TileEntityDieselEngine tile = engineWithFuel(10);
		tile.receiveMessageFromClient(EasyNBT.newNBT()
				.withBoolean("active", true)
				.withDouble("fuel_remainder", 100)
				.unwrap()
		);
		assertFalse(tile.workAnimator.getState());
		assertEquals(0, tile.fuelRemainder);
		assertEquals(10, tile.fuelTank.getFluidAmount());
	}

	@Test
	void ratioTradesSpeedForTorqueWithoutChangingPower()
	{
		TileEntityDieselEngine tile = engineWithFuel(10);
		tile.formed = true;
		assertOutputAtProgress(tile, 0);
		tile.workAnimator.setState(true);
		assertOutputAtProgress(tile, 0);
		advanceEngine(tile, 45);
		assertOutputAtProgress(tile, 0.5f);
		advanceEngine(tile, 45);
		assertTrue(tile.workAnimator.isFullyOpened());
		assertOutputAtProgress(tile, 1);
		tile.workAnimator.setState(false);
		assertOutputAtProgress(tile, 1);
		advanceEngine(tile, 45);
		assertOutputAtProgress(tile, 0.5f);
		advanceEngine(tile, 45);
		assertTrue(tile.workAnimator.isFullyClosed());
		assertOutputAtProgress(tile, 0);
	}

	@Test
	void ratioMessagesAreClampedAndRejectNonFiniteValues()
	{
		TileEntityDieselEngine tile = engineWithFuel(10);
		NBTTagCompound message = new NBTTagCompound();
		message.setFloat("torque_speed_ratio", DieselEngine.minRatio/2);
		tile.receiveMessageFromClient(message);
		assertEquals(DieselEngine.minRatio, tile.torqueSpeedRatio);
		message.setFloat("torque_speed_ratio", DieselEngine.maxRatio*2);
		tile.receiveMessageFromClient(message);
		assertEquals(DieselEngine.maxRatio, tile.torqueSpeedRatio);
		for(float invalid : new float[]{Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY})
		{
			message.setFloat("torque_speed_ratio", invalid);
			tile.receiveMessageFromClient(message);
			assertEquals(DieselEngine.maxRatio, tile.torqueSpeedRatio);
		}
	}

	private void assertOutputAtProgress(TileEntityDieselEngine tile, float progress)
	{
		assertEquals(progress, tile.workAnimator.getProgress(0));
		for(float ratio : new float[]{DieselEngine.minRatio, (float)Math.sqrt(DieselEngine.minRatio*DieselEngine.maxRatio), DieselEngine.maxRatio})
		{
			tile.setTorqueSpeedRatio(ratio);
			float torque = DieselEngine.producedTorque*progress;
			float speed = DieselEngine.producedSpeed*progress;
			assertEquals(torque*ratio, tile.rotation.getOutputTorque(), 0.001f);
			assertEquals(speed/ratio, tile.rotation.getOutputRotationSpeed(), 0.001f);
			assertEquals(torque*speed, tile.rotation.getOutputTorque()*tile.rotation.getOutputRotationSpeed(), 0.001f);
		}
	}

	private void advanceEngine(TileEntityDieselEngine tile, int ticks)
	{
		for(int tick = 0; tick < ticks; tick++)
			tile.workAnimator.update();
	}

	private TileEntityDieselEngine engineWithFuel(int amount)
	{
		TileEntityDieselEngine tile = new TileEntityDieselEngine();
		tile.fuelTank.fill(new FluidStack(fuel, amount), true);
		return tile;
	}
}
