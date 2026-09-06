package pl.pabilo8.immersiveintelligence.common.block.multiblock.emplacement;

import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTree;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTreeNode;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.*;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;
import pl.pabilo8.immersiveintelligence.test.GameTestWorld;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Verifies weighted branch scoring and lazily read target properties.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
class TargetDecisionTreeScoringTest extends GameTestWorld
{
	@Test
	void rejectsRootAndPrunesRejectedSubtrees()
	{
		Entity entity = namedEntity("accepted");
		TargetEvaluationContext context = context(entity);
		TargetDecisionTree rejectedRoot = new TargetDecisionTree(
				new TargetDecisionTreeNode(new NameTargetFilter("other"), 10));
		assertEquals(TargetDecisionTree.NO_MATCH, rejectedRoot.score(context));

		TargetDecisionTree tree = new TargetDecisionTree(new TargetDecisionTreeNode(new AnyTargetFilter(), 1));
		TargetDecisionTreeNode rejected = tree.addChild(tree.getRoot(), new NameTargetFilter("other"), 100);
		tree.addChild(rejected, new AnyTargetFilter(), 1000);
		tree.addChild(tree.getRoot(), new AnyTargetFilter(), 2);
		assertEquals(3, tree.score(context));
	}

	@Test
	void accumulatesDeepAndSiblingWeightsIncludingNegativeValues()
	{
		TargetDecisionTree tree = new TargetDecisionTree(new TargetDecisionTreeNode(new AnyTargetFilter(), -5));
		TargetDecisionTreeNode deep = tree.addChild(tree.getRoot(), new AnyTargetFilter(), 7);
		tree.addChild(deep, new AnyTargetFilter(), 11);
		tree.addChild(tree.getRoot(), new AnyTargetFilter(), -3);
		assertEquals(10, tree.score(context(namedEntity("target"))));
	}

	@Test
	void matchesNamesHealthAndNonHealthEntities()
	{
		EntityLivingBase living = livingEntity("Target", 12.5f, 20f);
		TargetEvaluationContext livingContext = context(living);
		assertTrue(new NameTargetFilter("target").matches(livingContext));
		assertTrue(new HealthTargetFilter(NumericComparison.EQUAL, 12.5).matches(livingContext));
		assertTrue(new MaxHealthTargetFilter(NumericComparison.GREATER_OR_EQUAL, 20).matches(livingContext));

		Entity nonHealth = namedEntity("target");
		assertFalse(new HealthTargetFilter(NumericComparison.GREATER_OR_EQUAL, 0).matches(context(nonHealth)));
		assertFalse(new MaxHealthTargetFilter(NumericComparison.GREATER_OR_EQUAL, 0).matches(context(nonHealth)));
	}

	@Test
	void matchesEntityRegistryIdAndNamespaceFromOneLookup()
	{
		World world = testManager.getWorld(0);
		EntityItem item = new EntityItem(world);
		world.spawnEntity(item);
		TargetEvaluationContext context = new TargetEvaluationContext().reset(world, null, Vec3d.ZERO, item);
		assertTrue(new EntityIdTargetFilter("minecraft:item").matches(context));
		assertTrue(new ModIdTargetFilter("minecraft").matches(context));
		assertFalse(new ModIdTargetFilter("immersiveintelligence").matches(context));
	}

	@Test
	void cachesDynamicPropertiesWithinOneCandidateEvaluation()
	{
		EntityLivingBase living = livingEntity("Target", 10f, 20f);
		OwnerIdentity owner = mock(OwnerIdentity.class);
		when(owner.getRelationTowards(living)).thenReturn(DiplomaticStatus.ENEMY);
		TargetEvaluationContext context = new TargetEvaluationContext().reset(testManager.getWorld(0), owner, Vec3d.ZERO, living);

		assertTrue(new HealthTargetFilter(NumericComparison.GREATER_THAN, 5).matches(context));
		living.setHealth(20f);
		assertEquals(20f, living.getHealth());
		assertTrue(new HealthTargetFilter(NumericComparison.LESS_THAN, 15).matches(context));
		assertTrue(new FactionRelationshipTargetFilter(DiplomaticStatus.ENEMY).matches(context));
		assertTrue(new FactionRelationshipTargetFilter(DiplomaticStatus.ENEMY).matches(context));
		verify(owner, times(1)).getRelationTowards(living);
	}

	@Test
	void readsVehicleDurabilityAsHealth()
	{
		EntityVehicleBase<?> vehicle = mock(EntityVehicleBase.class);
		vehicle.durabilityMain = new SyncedDurability(100, 0);
		vehicle.durabilityMain.attackFrom(null, 25);
		TargetEvaluationContext context = context(vehicle);
		assertTrue(new HealthTargetFilter(NumericComparison.EQUAL, 75).matches(context));
		assertTrue(new MaxHealthTargetFilter(NumericComparison.EQUAL, 100).matches(context));
	}

	@Test
	void comparesLinearDistanceWithoutChangingSquaredSemantics()
	{
		Entity entity = namedEntity("target");
		entity.posX = 3;
		entity.posY = -entity.height*0.5;
		entity.posZ = 4;
		TargetEvaluationContext context = context(entity);
		assertFalse(new DistanceTargetFilter(NumericComparison.LESS_THAN, 5).matches(context));
		assertTrue(new DistanceTargetFilter(NumericComparison.LESS_OR_EQUAL, 5).matches(context));
		assertTrue(new DistanceTargetFilter(NumericComparison.EQUAL, 5).matches(context));
		assertTrue(new DistanceTargetFilter(NumericComparison.GREATER_OR_EQUAL, 5).matches(context));
		assertFalse(new DistanceTargetFilter(NumericComparison.GREATER_THAN, 5).matches(context));
		assertTrue(new DistanceTargetFilter(NumericComparison.LESS_THAN, 6).matches(context));
		assertTrue(new DistanceTargetFilter(NumericComparison.GREATER_THAN, 4).matches(context));
	}

	@Test
	void matchesBothBooleanStates()
	{
		Entity entity = namedEntity("target");
		entity.onGround = true;
		when(entity.isInWater()).thenReturn(false);
		when(entity.isBurning()).thenReturn(true);
		TargetEvaluationContext context = context(entity);
		assertTrue(new OnGroundTargetFilter(true).matches(context));
		assertTrue(new InWaterTargetFilter(false).matches(context));
		assertTrue(new OnFireTargetFilter(true).matches(context));
		assertFalse(new OnGroundTargetFilter(false).matches(context));
		assertFalse(new InWaterTargetFilter(true).matches(context));
		assertFalse(new OnFireTargetFilter(false).matches(context));
	}

	@Test
	void matchesEveryDiplomaticStatusAndUsesNeutralFallback()
	{
		EntityLivingBase living = mock(EntityLivingBase.class);
		OwnerIdentity owner = mock(OwnerIdentity.class);
		for(DiplomaticStatus status : DiplomaticStatus.values())
		{
			when(owner.getRelationTowards(living)).thenReturn(status);
			TargetEvaluationContext context = new TargetEvaluationContext().reset(null, owner, Vec3d.ZERO, living);
			assertTrue(new FactionRelationshipTargetFilter(status).matches(context));
		}
		assertTrue(new FactionRelationshipTargetFilter(DiplomaticStatus.NEUTRAL)
				.matches(new TargetEvaluationContext().reset(null, owner, Vec3d.ZERO, namedEntity("unowned"))));
	}

	@Test
	void classifiesAllSupportedEntityCategories()
	{
		Entity mob = mock(Entity.class, withSettings().extraInterfaces(IMob.class));
		EntityAnimal animal = mock(EntityAnimal.class);
		EntityPlayer player = mock(EntityPlayer.class);
		Entity npc = mock(Entity.class, withSettings().extraInterfaces(INpc.class));
		EntityVehicleBase<?> vehicle = mock(EntityVehicleBase.class);
		EntityAmmoArtilleryProjectile projectile = mock(EntityAmmoArtilleryProjectile.class);
		assertType(TargetEntityType.MOB, mob);
		assertType(TargetEntityType.ANIMAL, animal);
		assertType(TargetEntityType.PLAYER, player);
		assertType(TargetEntityType.NPC, npc);
		assertType(TargetEntityType.VEHICLE, vehicle);
		assertType(TargetEntityType.PROJECTILE, projectile);
	}

	@Test
	void normalisesMultipartCandidatesOnce()
	{
		Entity parent = mock(Entity.class, withSettings().extraInterfaces(IEntityMultiPart.class));
		MultiPartEntityPart part = new MultiPartEntityPart((IEntityMultiPart)parent, "part", 1, 1);
		TargetEvaluationContext context = new TargetEvaluationContext().reset(null, null, Vec3d.ZERO, part);
		assertSame(part, context.getRawEntity());
		assertSame(parent, context.getEntity());
	}

	private static Entity namedEntity(String name)
	{
		Entity entity = mock(Entity.class);
		when(entity.getName()).thenReturn(name);
		return entity;
	}

	private EntitySheep livingEntity(String name, float health, float maxHealth)
	{
		World world = testManager.getWorld(0);
		EntitySheep entity = new EntitySheep(world);
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(health);
		entity.setCustomNameTag(name);
		world.spawnEntity(entity);
		return entity;
	}

	private TargetEvaluationContext context(Entity entity)
	{
		return new TargetEvaluationContext().reset(testManager.getWorld(0), null, Vec3d.ZERO, entity);
	}

	private void assertType(TargetEntityType type, Entity entity)
	{
		assertTrue(new EntityTypeTargetFilter(type).matches(context(entity)));
	}
}
