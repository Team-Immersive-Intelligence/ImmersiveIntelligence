package pl.pabilo8.immersiveintelligence.common.block.multiblock.radar;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetConfiguration;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetPresetDefaults;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar.RadarTargetManager;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Verifies Radar target configuration and output caching.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.09.2026
 */
class RadarTargetManagerTest
{
	@Test
	void roundTripsTargetConfigurationAndRejectsUnmarkedUpdates()
	{
		RadarTargetManager manager = new RadarTargetManager();
		TargetConfiguration configuration = manager.copyTargetConfiguration();
		configuration.setActivePresetId(TargetPresetDefaults.EVERYONE);

		assertFalse(manager.applyClientUpdate(configuration.serializeNBT()));
		assertTrue(manager.applyClientUpdate(manager.createTargetConfigurationUpdate(configuration)));

		RadarTargetManager copy = new RadarTargetManager();
		copy.deserializeNBT(manager.serializeNBT());
		assertEquals(TargetPresetDefaults.EVERYONE,
				copy.copyTargetConfiguration().getActivePresetId());
		assertEquals(manager.serializeNBT(), copy.serializeNBT());
	}

	@Test
	void scansAtIntervalAndSuppressesUnchangedOutputUntilHeartbeat()
	{
		World world = mock(World.class);
		TileEntityRadar radar = mock(TileEntityRadar.class);
		Entity entity = mock(Entity.class);
		AxisAlignedBB range = new AxisAlignedBB(-4, -4, -4, 4, 4, 4);
		AtomicLong worldTime = new AtomicLong();
		when(radar.getWorld()).thenReturn(world);
		when(radar.getRadarCenter()).thenReturn(Vec3d.ZERO);
		when(radar.getDetectionRangeBB()).thenReturn(range);
		when(entity.isEntityAlive()).thenReturn(true);
		when(entity.getEntityId()).thenReturn(7);
		when(world.getEntitiesWithinAABB(Entity.class, range)).thenReturn(Collections.singletonList(entity));
		when(world.getTotalWorldTime()).thenAnswer(invocation -> worldTime.get());

		RadarTargetManager manager = new RadarTargetManager();
		TargetConfiguration configuration = manager.copyTargetConfiguration();
		configuration.setActivePresetId(TargetPresetDefaults.EVERYONE);
		assertTrue(manager.applyClientUpdate(manager.createTargetConfigurationUpdate(configuration)));

		manager.update(radar, true, true);
		worldTime.set(1L);
		manager.update(radar, true, true);
		worldTime.set(20L);
		manager.update(radar, true, true);
		worldTime.set(40L);
		manager.update(radar, true, true);

		verify(world, times(3)).getEntitiesWithinAABB(Entity.class, range);
		verify(radar, times(2)).sendDetectedTargets(any(Entity[].class));
		verify(radar).getTerrainVisibilityIgnoredPositions();
	}

	@Test
	void inactiveRadarPublishesOneEmptySnapshot()
	{
		World world = mock(World.class);
		TileEntityRadar radar = mock(TileEntityRadar.class);
		when(radar.getWorld()).thenReturn(world);

		RadarTargetManager manager = new RadarTargetManager();
		manager.update(radar, false, true);
		manager.update(radar, false, true);

		verify(radar, times(1)).sendDetectedTargets(argThat(targets -> targets.length==0));
	}

	@Test
	void reEnablingOutputForcesAFreshScan()
	{
		World world = mock(World.class);
		TileEntityRadar radar = mock(TileEntityRadar.class);
		AxisAlignedBB range = new AxisAlignedBB(-4, -4, -4, 4, 4, 4);
		when(radar.getWorld()).thenReturn(world);
		when(radar.getRadarCenter()).thenReturn(Vec3d.ZERO);
		when(radar.getDetectionRangeBB()).thenReturn(range);
		when(world.getEntitiesWithinAABB(Entity.class, range)).thenReturn(Collections.emptyList());

		RadarTargetManager manager = new RadarTargetManager();
		manager.update(radar, true, true);
		manager.update(radar, true, false);
		manager.update(radar, true, true);

		verify(world, times(2)).getEntitiesWithinAABB(Entity.class, range);
		verify(radar, times(2)).sendDetectedTargets(argThat(targets -> targets.length==0));
	}
}
