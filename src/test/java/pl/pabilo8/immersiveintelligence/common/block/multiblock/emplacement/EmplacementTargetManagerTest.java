package pl.pabilo8.immersiveintelligence.common.block.multiblock.emplacement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager.EmplacementFireMission;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager.FireMissionEdit;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetPresetDefaults;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Verifies manager section isolation and Request-before-Job scheduling.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
class EmplacementTargetManagerTest
{
	@Test
	void roundTripsManagerConfigurationAndFireMissions()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager();
		manager.getTargetConfiguration().setActivePresetId(TargetPresetDefaults.EVERYONE);
		manager.addPositionMission(new BlockPos(4, 5, 6), 3);

		EmplacementTargetManager copy = new EmplacementTargetManager();
		copy.deserializeNBT(manager.serializeNBT());
		assertEquals(manager.serializeNBT(), copy.serializeNBT());
		assertEquals(TargetPresetDefaults.EVERYONE, copy.getTargetConfiguration().getActivePresetId());
		assertEquals(1, copy.getFireMissions().size());
	}

	@Test
	void targetConfigurationUpdateDoesNotReplaceLiveRequests()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager();
		manager.addPositionMission(new BlockPos(1, 2, 3), 2);
		manager.getTargetConfiguration().setActivePresetId(TargetPresetDefaults.EVERYONE);

		assertTrue(manager.applyClientUpdate(manager.createTargetConfigurationUpdate(
				manager.copyTargetConfiguration())));
		assertEquals(1, manager.getFireMissions().size());
		assertEquals(new BlockPos(1, 2, 3), manager.getFireMissions().get(0).target.getPosition());
	}

	@Test
	void rejectsStaleFireMissionEditWithoutReplacingNewRequests()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager();
		FireMissionEdit stale = manager.createFireMissionEdit();
		manager.addPositionMission(new BlockPos(7, 8, 9), 1);

		assertFalse(manager.applyClientUpdate(stale.serializeNBT()));
		assertEquals(1, manager.getFireMissions().size());
		assertEquals(new BlockPos(7, 8, 9), manager.getFireMissions().get(0).target.getPosition());
	}

	@Test
	void fireMissionEditDoesNotReplaceTargetConfiguration()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager();
		manager.getTargetConfiguration().setActivePresetId(TargetPresetDefaults.SHELLS);
		FireMissionEdit edit = manager.createFireMissionEdit();
		edit.getMissions().add(new EmplacementFireMission().withPosition(new BlockPos(3, 2, 1)));

		assertTrue(manager.applyClientUpdate(edit.serializeNBT()));
		assertEquals(TargetPresetDefaults.SHELLS, manager.getTargetConfiguration().getActivePresetId());
		assertEquals(1, manager.getFireMissions().size());
	}

	@Test
	void skipsUnavailableRequestThenResumesJobAfterRequestCompletes()
	{
		World world = mock(World.class);
		EmplacementWeapon weapon = mock(EmplacementWeapon.class);
		TileEntityEmplacement emplacement = mock(TileEntityEmplacement.class);
		Entity entity = mock(Entity.class);
		AxisAlignedBB detection = new AxisAlignedBB(-16, -16, -16, 16, 16, 16);

		emplacement.currentWeapon = weapon;
		emplacement.dataControlEnabled = false;
		when(emplacement.getWorld()).thenReturn(world);
		when(emplacement.getWeaponCenter()).thenReturn(Vec3d.ZERO);
		when(weapon.getDetectionRangeBB()).thenReturn(detection);
		when(weapon.isVisibleTarget(entity)).thenReturn(true);
		when(weapon.canSelectAutonomousTarget(entity)).thenReturn(true);
		when(entity.getEntityId()).thenReturn(42);
		when(world.getEntitiesWithinAABB(Entity.class, detection)).thenReturn(Collections.singletonList(entity));

		EmplacementTargetManager manager = new EmplacementTargetManager(() -> world);
		manager.getTargetConfiguration().setActivePresetId(TargetPresetDefaults.EVERYONE);
		manager.addPositionMission(new BlockPos(100, 0, 0), 1);
		manager.addPositionMission(new BlockPos(2, 0, 0), 1);
		TargetCoordinateReference unavailable = manager.getFireMissions().get(0).target;
		TargetCoordinateReference executable = manager.getFireMissions().get(1).target;
		when(weapon.canExecuteFireMission(any(TargetCoordinateReference.class)))
				.thenAnswer(invocation -> invocation.getArgument(0)==executable);

		assertSame(executable, manager.updateAndGetTarget(emplacement, null));
		assertEquals(2, manager.getFireMissions().size());
		manager.notifyAfterShot(executable);

		TargetCoordinateReference job = manager.updateAndGetTarget(emplacement, executable);
		assertNotNull(job);
		assertSame(entity, job.getEntity());
		assertEquals(1, manager.getFireMissions().size());
		assertSame(unavailable, manager.getFireMissions().get(0).target);
		verify(world, times(1)).getEntitiesWithinAABB(Entity.class, detection);
	}

	@Test
	void rejectsFireMissionEditAfterLiveShotProgress()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager();
		manager.addPositionMission(new BlockPos(1, 2, 3), 2);
		FireMissionEdit stale = manager.createFireMissionEdit();
		TargetCoordinateReference liveTarget = manager.getFireMissions().get(0).target;

		manager.notifyAfterShot(liveTarget);

		assertFalse(manager.applyClientUpdate(stale.serializeNBT()));
		assertEquals(1, liveTarget.getShotsRemaining());
	}

	@Test
	void keepsCurrentEntityWhenEqualScoresAreScannedInAnotherOrder()
	{
		World world = mock(World.class);
		EmplacementWeapon weapon = mock(EmplacementWeapon.class);
		TileEntityEmplacement emplacement = mock(TileEntityEmplacement.class);
		Entity first = mock(Entity.class);
		Entity second = mock(Entity.class);
		AxisAlignedBB detection = new AxisAlignedBB(-16, -16, -16, 16, 16, 16);
		AxisAlignedBB entityBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

		emplacement.currentWeapon = weapon;
		emplacement.dataControlEnabled = false;
		when(emplacement.getWorld()).thenReturn(world);
		when(emplacement.getWeaponCenter()).thenReturn(Vec3d.ZERO);
		when(emplacement.getPos()).thenReturn(BlockPos.ORIGIN);
		when(world.getTotalWorldTime()).thenReturn(8L);
		when(weapon.getDetectionRangeBB()).thenReturn(detection);
		when(weapon.isVisibleTarget(first)).thenReturn(true);
		when(weapon.isVisibleTarget(second)).thenReturn(true);
		when(weapon.canSelectAutonomousTarget(first)).thenReturn(true);
		when(weapon.canSelectAutonomousTarget(second)).thenReturn(true);
		when(first.getEntityId()).thenReturn(1);
		when(second.getEntityId()).thenReturn(2);
		when(first.getEntityBoundingBox()).thenReturn(entityBox);
		when(second.getEntityBoundingBox()).thenReturn(entityBox);
		when(world.getEntitiesWithinAABB(Entity.class, detection)).thenReturn(
				Arrays.asList(first, second), Arrays.asList(second, first));

		EmplacementTargetManager manager = new EmplacementTargetManager(() -> world);
		manager.getTargetConfiguration().setActivePresetId(TargetPresetDefaults.EVERYONE);
		TargetCoordinateReference selected = manager.updateAndGetTarget(emplacement, null);
		assertSame(first, selected.getEntity());
		assertSame(first, manager.updateAndGetTarget(emplacement, selected).getEntity());
		verify(world, times(2)).getEntitiesWithinAABB(Entity.class, detection);
	}

	@Test
	void pauseNoWeaponAndInactiveJobReturnNoTarget()
	{
		World world = mock(World.class);
		TileEntityEmplacement emplacement = mock(TileEntityEmplacement.class);
		emplacement.currentWeapon = null;
		emplacement.dataControlEnabled = false;
		when(emplacement.getWorld()).thenReturn(world);
		EmplacementTargetManager manager = new EmplacementTargetManager(() -> world);
		assertNull(manager.updateAndGetTarget(emplacement, null));

		manager.stopTask(true);
		assertNull(manager.updateAndGetTarget(emplacement, null));
		manager.resumeTask(true);
		manager.getTargetConfiguration().setActivePresetId("");
		assertNull(manager.updateAndGetTarget(emplacement, null));
		verifyNoInteractions(world);
	}

	@Test
	void oldestExecutableAndInfiniteRequestsRemainOrdered()
	{
		World world = mock(World.class);
		EmplacementWeapon weapon = mock(EmplacementWeapon.class);
		TileEntityEmplacement emplacement = mock(TileEntityEmplacement.class);
		emplacement.currentWeapon = weapon;
		emplacement.dataControlEnabled = false;
		when(emplacement.getWorld()).thenReturn(world);
		when(weapon.canExecuteFireMission(any(TargetCoordinateReference.class))).thenReturn(true);

		EmplacementTargetManager manager = new EmplacementTargetManager(() -> world);
		manager.addPositionMission(new BlockPos(1, 0, 0), 0);
		manager.addPositionMission(new BlockPos(2, 0, 0), 0);
		TargetCoordinateReference first = manager.getFireMissions().get(0).target;
		assertSame(first, manager.updateAndGetTarget(emplacement, null));
		assertEquals(2, manager.getFireMissions().size());
		assertFalse(first.hasFiniteShots());
	}

	@Test
	void sharesSelectedTargetFirstCapsOutputAndSuppressesUnchangedScans()
	{
		World world = mock(World.class);
		EmplacementWeapon weapon = mock(EmplacementWeapon.class);
		TileEntityEmplacement emplacement = mock(TileEntityEmplacement.class);
		AxisAlignedBB detection = new AxisAlignedBB(-64, -64, -64, 64, 64, 64);
		AxisAlignedBB entityBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
		List<Entity> entities = new ArrayList<>();
		for(int i = 0; i < 40; i++)
		{
			Entity entity = mock(Entity.class);
			when(entity.getEntityId()).thenReturn(i+1);
			when(entity.getEntityBoundingBox()).thenReturn(entityBox);
			entities.add(entity);
		}

		emplacement.currentWeapon = weapon;
		emplacement.dataControlEnabled = true;
		when(emplacement.getWorld()).thenReturn(world);
		when(emplacement.getWeaponCenter()).thenReturn(Vec3d.ZERO);
		when(emplacement.getPos()).thenReturn(BlockPos.ORIGIN);
		when(world.getTotalWorldTime()).thenReturn(0L, 8L, 8L);
		when(world.getEntitiesWithinAABB(Entity.class, detection)).thenReturn(entities);
		when(weapon.getDetectionRangeBB()).thenReturn(detection);
		when(weapon.isVisibleTarget(any(Entity.class))).thenReturn(true);
		when(weapon.canSelectAutonomousTarget(any(Entity.class))).thenReturn(true);

		EmplacementTargetManager manager = new EmplacementTargetManager(() -> world);
		manager.getTargetConfiguration().setActivePresetId(TargetPresetDefaults.EVERYONE);
		TargetCoordinateReference selected = manager.updateAndGetTarget(emplacement, null);
		assertSame(entities.get(0), selected.getEntity());
		manager.updateAndGetTarget(emplacement, selected);

		ArgumentCaptor<Entity[]> output = ArgumentCaptor.forClass(Entity[].class);
		verify(emplacement, times(1)).handleSendingEnemyPos(output.capture());
		assertEquals(32, output.getValue().length);
		assertSame(entities.get(0), output.getValue()[0]);
		verify(world, times(2)).getEntitiesWithinAABB(Entity.class, detection);
	}
}
