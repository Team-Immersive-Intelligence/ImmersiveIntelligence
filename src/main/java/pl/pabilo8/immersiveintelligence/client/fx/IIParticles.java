package pl.pabilo8.immersiveintelligence.client.fx;

import com.google.common.collect.HashMultimap;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleBasicGravity;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleDebris;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleModel;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.IScalableParticle;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.ITexturedParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.DrawStages;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleUtils.PositionGenerator;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties.*;

/**
 * @author Pabilo8
 * @since 08.04.2024
 */
@SuppressWarnings("unchecked")
public class IIParticles
{
	//--- Particle IDs Reference ---//
	//TODO: 28.04.2024 flare + flare_trace,
	//Gunfire and related
	public static final String PARTICLE_GUNFIRE = "gunfire";
	public static final String PARTICLE_ROCKET_TRACE = "rocket_trace";

	//Ricochet
	public static final String PARTICLE_RICOCHET_METAL = "ricochet_metal";
	public static final String PARTICLE_RICOCHET_METAL_SPARK = "ricochet_metal";
	public static final String PARTICLE_RICOCHET_METAL_DUST_CLOUD = "ricochet_metal";

	public static final String PARTICLE_RICOCHET_STONE = "ricochet_stone";
	public static final String PARTICLE_RICOCHET_WOOD = "ricochet_wood";
	public static final String PARTICLE_RICOCHET_GENERIC = "ricochet_generic";

	//Penetration
	public static final String PARTICLE_PENETRATION_METAL_START = "penetration_metal_start";
	public static final String PARTICLE_PENETRATION_METAL_MID = "penetration_metal_mid";
	public static final String PARTICLE_PENETRATION_METAL_END = "penetration_metal_end";

	public static final String PARTICLE_PENETRATION_STONE_START = "penetration_stone_start";
	public static final String PARTICLE_PENETRATION_STONE_MID = "penetration_stone_mid";
	public static final String PARTICLE_PENETRATION_STONE_END = "penetration_stone_end";

	public static final String PARTICLE_PENETRATION_GENERIC_START = "penetration_generic_start";
	public static final String PARTICLE_PENETRATION_GENERIC_MID = "penetration_generic_mid";
	public static final String PARTICLE_PENETRATION_GENERIC_END = "penetration_generic_end";

	//Explosion Debris
	public static final String PARTICLE_DEBRIS_BRANCH_LEAF = "debris_branch_leaf";
	public static final String PARTICLE_DEBRIS_BRANCH_NEEDLE = "debris_branch_needle";
	public static final String PARTICLE_DEBRIS_BRANCH_CACTUS = "debris_branch_cactus";

	public static final String PARTICLE_DEBRIS_BRICK = "debris_brick";
	public static final String PARTICLE_DEBRIS_BRICK_BIG = "debris_big_brick";
	public static final String PARTICLE_DEBRIS_CLOTH = "debris_cloth";
	public static final String PARTICLE_DEBRIS_FLESH = "debris_flesh";
	public static final String PARTICLE_DEBRIS_METAL = "debris_metal";
	public static final String PARTICLE_DEBRIS_GLASS = "debris_glass";
	public static final String PARTICLE_DEBRIS_PEBBLE = "debris_pebble";
	public static final String PARTICLE_DEBRIS_PLANK = "debris_plank";
	public static final String PARTICLE_DEBRIS_STRAW = "debris_straw";
	public static final String PARTICLE_DEBRIS_MECHANICAL = "debris_rivet";
	public static final String PARTICLE_DEBRIS_LIGHT_MACHINE = "debris_light_machine";
	public static final String PARTICLE_DEBRIS_HEAVY_MACHINE = "debris_heavy_machine";

	//Debris Complimentary
	public static final String PARTICLE_BLOCK_CHUNK = "block/block_chunk";
	public static final String PARTICLE_BLOCK_IMPACT = "block/block_impact";
	public static final String PARTICLE_SMOKE_TRACE = "smoke/smoke_trace";
	public static final String PARTICLE_SMOKE_SMALL = "smoke_small";
	public static final String PARTICLE_SMOKE_MEDIUM = "smoke_medium";
	public static final String PARTICLE_SMOKE_BIG = "smoke_big";
	public static final String PARTICLE_DUST_SMALL = "dust_small";
	public static final String PARTICLE_DUST_MEDIUM = "dust_medium";
	public static final String PARTICLE_DUST_BIG = "dust_big";

	//Dust Pile
	public static final String PARTICLE_DUST_PILE_TINY = "dust_pile_tiny";
	public static final String PARTICLE_DUST_PILE_SMALL = "dust_pile_small";
	public static final String PARTICLE_DUST_PILE_MEDIUM = "dust_pile_medium";
	public static final String PARTICLE_DUST_PILE_BIG = "dust_pile_big";
	public static final String PARTICLE_DUST_PILE_LARGE = "dust_pile_large";

	//Explosions
	public static final String PARTICLE_SHOCKWAVE = "block/shockwave";
	public static final String PARTICLE_SHOCKWAVE_BIT = "explosion_shockwave_bit";
	public static final String PARTICLE_EXPLOSION_TNT = "explosion_tnt";
	public static final String PARTICLE_EXPLOSION_TNT_LIGHT = "explosion_tnt_light";

	@SideOnly(Side.CLIENT)
	public static void preInit()
	{
		ParticleRegistry.registerProgram("dust_transition", IIParticles::getDustTransitionProgram);
		ParticleRegistry.registerProgram("smoke_transition", IIParticles::getSmokeTransitionProgram);
		ParticleRegistry.registerProgram("shockwave_transition", IIParticles::getShockwaveTransitionProgram);
		ParticleRegistry.registerProgram("lifetime_retexture", (particle, partialTicks) -> {
			assert particle instanceof ITexturedParticle;
			((ITexturedParticle)particle).setTextureShift(
					(int)(particle.getProgress(partialTicks)*
							((ITexturedParticle)particle).getTexturesCount())
			);
		});
	}

	@SideOnly(Side.CLIENT)
	public static void init()
	{
		ParticleRegistry.registerParticleFile(PARTICLE_BLOCK_CHUNK);
		ParticleRegistry.registerParticleFile(PARTICLE_BLOCK_IMPACT);
		ParticleRegistry.registerParticleFile(PARTICLE_SMOKE_TRACE);
		ParticleRegistry.registerParticleFile(PARTICLE_SHOCKWAVE);
		ParticleRegistry.registerParticleFile(PARTICLE_GUNFIRE);

		registerSmokeFX();
		registerDustPilesFX();
		registerDebrisFX();
		registerExplosivesFX();
	}

	//--- Registration Methods ---//

	@SideOnly(Side.CLIENT)
	private static void registerDustPilesFX()
	{
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_PILE_TINY)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "dust_pile/dust_pile_tiny.obj"))
				.withLifeTime(40)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(0.0625f)
				.withProgram(IIParticles::getDustTransitionProgram);
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_PILE_SMALL)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "dust_pile/dust_pile_small.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(0.125f)
				.withProgram(IIParticles::getDustTransitionProgram);
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_PILE_MEDIUM)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "dust_pile/dust_pile_medium.obj"))
				.withLifeTime(80)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(0.1875f)
				.withProgram(IIParticles::getDustTransitionProgram);
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_PILE_BIG)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "dust_pile/dust_pile_big.obj"))
				.withLifeTime(100)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(0.25f);
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_PILE_LARGE, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "dust_pile/dust_pile_large.obj"))
				.withLifeTime(150)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(0.3125f);
	}

	@SideOnly(Side.CLIENT)
	private static void registerSmokeFX()
	{
		//Small
		ParticleRegistry.registerModelParticle(PARTICLE_SMOKE_SMALL, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "smoke/smoke_small.obj"))
				.withLifeTime(40)
				.withDrawStage(DrawStages.CUSTOM_SMOKE_NOISE_SHADER)
				.withBoundingBox(0.5f)
				.withDynamicProperty((par, easyNBT) ->
						easyNBT.checkSetDouble(SIZE, size -> par.setLifeTime(0, (int)(size*par.getMaxLifeTime()))))
				.withProgram(IIParticles::getSmokeTransitionProgram);
		//Medium
		ParticleRegistry.registerModelParticle(PARTICLE_SMOKE_MEDIUM, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "smoke/smoke_medium.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SMOKE_NOISE_SHADER)
				.withScheduled((smoke) -> {
					ParticleUtils.position(PositionGenerator.RAND_XZ, smoke, 4, 0.5f, (pos, direction) ->
							ParticleRegistry.spawnParticle(PARTICLE_SMOKE_SMALL, pos.add(direction), Vec3d.ZERO, Vec3d.ZERO)
									.inherit(smoke, COLOR, GRAVITY));
				}, 10)
				.withBoundingBox(1f)
				.withDynamicProperty((par, easyNBT) ->
						easyNBT.checkSetDouble(SIZE, size -> par.setLifeTime(0, (int)(size*par.getMaxLifeTime()))))
				.withProgram(IIParticles::getSmokeTransitionProgram);
		//Large
		ParticleRegistry.registerModelParticle(PARTICLE_SMOKE_BIG, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "smoke/smoke_big.obj"))
				.withLifeTime(100)
				.withDrawStage(DrawStages.CUSTOM_SMOKE_NOISE_SHADER)
				.withScheduled((smoke) -> {
					ParticleUtils.position(PositionGenerator.RAND_XZ, smoke, 4, 0.25f, (pos, direction) ->
							ParticleRegistry.spawnParticle(PARTICLE_SMOKE_MEDIUM, pos.add(direction), Vec3d.ZERO, Vec3d.ZERO)
									.inherit(smoke, COLOR, GRAVITY));
				}, 40)
				.withBoundingBox(1.6875f)
				.withDynamicProperty((par, easyNBT) ->
						easyNBT.checkSetDouble(SIZE, size -> par.setLifeTime(0, (int)(size*par.getMaxLifeTime()))))
				.withProgram(IIParticles::getSmokeTransitionProgram);

		//Dust Small
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_SMALL, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "smoke/dust_small.obj"))
				.withLifeTime(80)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(0.5f)
				.withProperty(GRAVITY, 0.5f)
				.withDynamicProperty((par, easyNBT) ->
						easyNBT.checkSetDouble(SIZE, size -> par.setLifeTime(0, (int)(size*par.getMaxLifeTime()))))
				.withProgram(IIParticles::getDustTransitionProgram)
				/*.withChained(dust -> ParticleRegistry.spawnParticle(PARTICLE_SMOKE_SMALL, dust.getPosition(), Vec3d.ZERO, Vec3d.ZERO)
						.inherit(dust, IIParticleProperties.COLOR, IIParticleProperties.GRAVITY))*/;
		//Dust Medium
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_MEDIUM, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "smoke/dust_medium.obj"))
				.withLifeTime(140)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(1f)
				.withProperty(GRAVITY, 0.5f)
				.withDynamicProperty((par, easyNBT) ->
						easyNBT.checkSetDouble(SIZE, size -> par.setLifeTime(0, (int)(size*par.getMaxLifeTime()))))
				.withProgram(IIParticles::getDustTransitionProgram)
				/*.withChained(dust -> ParticleRegistry.spawnParticle(PARTICLE_SMOKE_MEDIUM, dust.getPosition(), Vec3d.ZERO, Vec3d.ZERO)
						.inherit(dust, IIParticleProperties.COLOR, IIParticleProperties.GRAVITY))*/;
		//Dust Big
		ParticleRegistry.registerModelParticle(PARTICLE_DUST_BIG, ParticleBasicGravity::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "smoke/dust_big.obj"))
				.withLifeTime(180)
				.withDrawStage(DrawStages.CUSTOM)
				.withBoundingBox(1.6875f)
				.withProperty(GRAVITY, 0.5f)
				.withDynamicProperty((par, easyNBT) ->
						easyNBT.checkSetDouble(SIZE, size -> par.setLifeTime(0, (int)(size*par.getMaxLifeTime()))))
				.withProgram(IIParticles::getDustTransitionProgram)
				/*.withChained(dust -> ParticleRegistry.spawnParticle(PARTICLE_SMOKE_BIG, dust.getPosition(), Vec3d.ZERO, Vec3d.ZERO)
						.inherit(dust, IIParticleProperties.COLOR, IIParticleProperties.GRAVITY))*/;
	}

	@SideOnly(Side.CLIENT)
	private static void registerDebrisFX()
	{
		//Branches
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_BRANCH_LEAF, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_leaf1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_leaf2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_leaf3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_leaf4.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.25f)
				.withDynamicProperty(ParticleUtils::getFoliageColor);
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_BRANCH_NEEDLE, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_needle1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_needle2.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.3f);
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_BRANCH_CACTUS, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_cactus1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/branch/branch_cactus2.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.2f);

		//Bricks, Stones
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_BRICK_BIG, ParticleDebris::new)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/big_brick/big_brick.obj"))
				.withLifeTime(100)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.25f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_DEBRIS_PEBBLE, PARTICLE_DUST_MEDIUM, PARTICLE_SMOKE_TRACE, 4, 8, 0.5f));

		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_BRICK, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/brick/brick1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/brick/brick2.obj"))
				.withLifeTime(80)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.125f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 4, 7, 0.35f));

		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_PEBBLE, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/pebble/pebble1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/pebble/pebble2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/pebble/pebble3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/pebble/pebble4.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.0625f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Metal
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_METAL, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank4.obj"))
				.withLifeTime(80)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.125f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Machines
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_MECHANICAL, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/gear1.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.0625f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_LIGHT_MACHINE, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/gear1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/wire/wire1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/wire/wire2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/wire/wire3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/wire/wire4.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.0625f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_HEAVY_MACHINE, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank4.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/rivet/rivet3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/gear1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/wire1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/wire2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/wire3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/gear/wire4.obj"))
				.withLifeTime(60)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.0625f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Wood
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_PLANK, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/plank/plank4.obj"))
				.withLifeTime(50)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.125f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Grass
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_STRAW, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/straw/straw1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/straw/straw2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/straw/straw3.obj"))
				.withLifeTime(40)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.125f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Glass
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_GLASS, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/glass/glass1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/glass/glass2.obj"))
				.withLifeTime(40)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.0625f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Cloth
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_CLOTH, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/cloth/cloth1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/cloth/cloth2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/cloth/cloth3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/cloth/cloth4.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/cloth/cloth5.obj"))
				.withLifeTime(40)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.25f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));

		//Flesh
		ParticleRegistry.registerModelParticle(PARTICLE_DEBRIS_FLESH, ParticleDebris::new)
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/flesh/flesh1.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/flesh/flesh2.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/flesh/flesh3.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/flesh/flesh4.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/flesh/flesh5.obj"))
				.withModelVariant(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "debris/flesh/flesh6.obj"))
				.withLifeTime(80)
				.withDrawStage(DrawStages.CUSTOM_SOLID)
				.withBoundingBox(0.25f)
				.withChained(spawnDebrisBreakParticles(PARTICLE_BLOCK_CHUNK, PARTICLE_DUST_SMALL, null, 3, 8, 0.65f));
	}

	@SideOnly(Side.CLIENT)
	private static void registerExplosivesFX()
	{
		//TNT Light
		ParticleRegistry.registerModelParticle(PARTICLE_EXPLOSION_TNT_LIGHT)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "explosion/explosion_tnt.obj"))
				.withLifeTime(5)
				.withDrawStage(DrawStages.CUSTOM_ADDITIVE)
				.withDynamicProperty(ParticleUtils.getRandomColorMixer(
						IIColor.fromPackedRGB(0xf0c673),
						IIColor.fromPackedRGB(0xf0a154))
				)
				.withProgram((par, ticks) -> getExplosionTransitionProgram(par, ticks, 2.5f));

		//TNT
		ParticleRegistry.registerModelParticle(PARTICLE_EXPLOSION_TNT)
				.withModel(ResLoc.of(IIReference.RES_PARTICLE_MODEL, "explosion/explosion_tnt.obj"))
				.withLifeTime(3)
				.withDrawStage(DrawStages.CUSTOM_ADDITIVE)
				.withDynamicProperty(ParticleUtils.getRandomColorMixer(
						IIColor.fromPackedRGB(0xf0c673),
						IIColor.fromPackedRGB(0xf0a154))
				)
				.withScheduled(par -> ParticleRegistry.spawnParticle(PARTICLE_SHOCKWAVE, par.getPosition(), par.getRotation(), Vec3d.ZERO)
						.setProperties(EasyNBT.newNBT()
								.withColor(COLOR, IIColor.fromPackedRGB(0xFFDBA7).withAlpha(0.5f))
								.withVec3d("scale", par.getScale())), 2
				)
				.withScheduled(par -> ParticleRegistry.spawnParticle(PARTICLE_EXPLOSION_TNT_LIGHT, par.getPosition().addVector(0, 0.5, 0), Vec3d.ZERO, Vec3d.ZERO)
								.inherit(par, "scale"),
						0)
				.withScheduled(par ->
						ParticleUtils.position(PositionGenerator.RAND_XZ, par, scaleParticleAmount(par, 8), 1.05f, (pos, direction) ->
								{
									IIParticle par2 = ParticleRegistry.spawnParticle(PARTICLE_SMOKE_BIG, pos.add(direction).addVector(0, 1, 0), direction,
											direction.scale(0.03125f*ParticleUtils.randFloat.get()).addVector(0, 0.15*ParticleUtils.randFloat.get(), 0));
									par2.setProperties(EasyNBT.newNBT()
											.withColor(COLOR, IIColor.fromPackedARGB(0x603e3c3c)
													.mixedWith(IIColor.fromPackedARGB(0x20494747), ParticleUtils.randFloat.get()))
											.withDouble(GRAVITY, 0.005f)
									);
									par2.setLifeTime(0, 20);
									par2.setScheduledParticles(HashMultimap.create());
								}
						), 0)
				.withScheduled(par -> ParticleUtils.position(PositionGenerator.RAND_XZ, par, scaleParticleAmount(par, 6), 2f, (pos, direction) ->
						ParticleRegistry.spawnParticle(PARTICLE_DUST_BIG, pos.add(direction).addVector(0, 0.25, 0), Vec3d.ZERO, Vec3d.ZERO)
								.setProperties(EasyNBT.newNBT()
										.withColor(COLOR, IIColor.fromPackedRGB(0x272626)
												.mixedWith(IIColor.fromPackedRGB(0x3d3c3c), ParticleUtils.randFloat.get()).withAlpha(0.6f))
										.withFloat(GRAVITY, 0.002f)
								)), 1
				)
				.withScheduled(smoke -> ParticleUtils.position(PositionGenerator.RAND_XZ, smoke, 2, 1.125f, (pos, direction) ->
						ParticleRegistry.spawnParticle(PARTICLE_SMOKE_MEDIUM, pos.add(direction).addVector(0, 0.5, 0), Vec3d.ZERO, direction.scale(0.1))
								.setProperties(EasyNBT.newNBT()
										.withColor(COLOR, IIColor.fromPackedRGB(0x353333)
												.mixedWith(IIColor.fromPackedRGB(0x575656), ParticleUtils.randFloat.get()).withAlpha(0.3f))
										.withFloat(GRAVITY, 0f)
								)), 0
				)
				.withProgram((par, ticks) -> getExplosionTransitionProgram(par, ticks, 1));

	}

	/**
	 * Scales the number of particles based on the particle scale
	 *
	 * @param particle The particle
	 * @param amount   number of particles for scale = 1
	 * @return scaled number of particles
	 */
	private static int scaleParticleAmount(ParticleModel particle, int amount)
	{
		return (int)(amount*particle.getSize());
	}

	//--- Utils ---//

	@Nonnull
	@SideOnly(Side.CLIENT)
	private static <T extends ParticleBasicGravity> Consumer<T> spawnDebrisBreakParticles(
			@Nullable String chunkName, @Nullable String smokeName, @Nullable String traceName, int minDebris, int maxDebris, float range)
	{
		return (deb) -> {
			int amount = Math.max(minDebris, ParticleUtils.randInt.get()%maxDebris);

			//Spawn smoke cloud
			Vec3d pos = deb.getPosition();
			ParticleRegistry.spawnParticle(smokeName, pos, Vec3d.ZERO, Vec3d.ZERO)
					.setProperties(EasyNBT.newNBT().withColor(COLOR, IIColor.fromPackedARGB(0x603e3c3c)));
			ClientEventHandler.addScreenshakeSource(pos, 0.3f, 1f);
			ParticleBuilder<?> smokeTrace = ParticleRegistry.getParticleBuilder(traceName);

			//Spawn chunks
			if(chunkName!=null)
			{
				ParticleBuilder<? extends IIParticle> builder = ParticleRegistry.getParticleBuilder(chunkName);
				for(int i = 0; i < amount; i++)
				{
					Vec3d direction = ParticleUtils.withY(ParticleUtils.getRandXZ().scale(range), 0.1f);
					//spawn smoke trace outside the cloud
					if(smokeTrace!=null)
					{
						IIParticle spawnedTrace = smokeTrace.spawnParticle(pos, Vec3d.ZERO, direction.normalize());
						((ParticleModel)spawnedTrace).setScale(new Vec3d(direction.lengthVector(), 1, 1));
						//TODO: 28.04.2024 use hit block color
						((ParticleModel)spawnedTrace).setColor(
								IIColor.fromPackedARGB(0x603e3c3c)
										.mixedWith(IIColor.fromPackedARGB(0x20494747), ParticleUtils.randFloat.get())
						);
					}
					//spawn smaller fragments
					IIParticle particle = builder.spawnParticle(pos, direction, direction.normalize());
					if(particle instanceof ParticleModel)
						((ParticleModel)particle).retextureModel(deb);
				}
			}
		};
	}

	@SideOnly(Side.CLIENT)
	private static void getDustTransitionProgram(ParticleModel particle, float partialTicks)
	{
		float progress = particle.getProgress(0);
		particle.setScale(IIUtils.ONE.scale(1-progress*0.5).scale(particle.getSize()));
		particle.setColor(particle.getColor().withAlpha(1-progress));
	}

	@SideOnly(Side.CLIENT)
	private static void getSmokeTransitionProgram(IIParticle particle, float partialTicks)
	{
		assert particle instanceof ITexturedParticle&&particle instanceof IScalableParticle;
		IScalableParticle scal = (IScalableParticle)particle;
		ITexturedParticle tex = (ITexturedParticle)particle;

		float progress = particle.getProgress(0);
		scal.setScale(IIUtils.ONE.scale(1+progress).scale(scal.getSize()));

		float alpha;
		if(progress < 0.2)
			alpha = (progress/0.2f)*0.75f;
		else if(progress < 0.7)
			alpha = 0.75f-0.5f*((progress-0.2f)/0.5f);
		else
			alpha = 0.25f*(1-(progress-0.7f)/0.3f);

		tex.setColor(tex.getColor().withAlpha(alpha));
	}

	@SideOnly(Side.CLIENT)
	private static void getExplosionTransitionProgram(ParticleModel particle, float partialTicks, float scale)
	{
		float progress = particle.getProgress(0);
		particle.setScale(IIUtils.ONE.scale(1+progress*scale).scale(particle.getSize()));

		float alpha;
		if(progress < 0.2)
			alpha = (progress/0.2f)*0.75f;
		else if(progress < 0.7)
			alpha = 0.75f-0.5f*((progress-0.2f)/0.5f);
		else
			alpha = 0.25f*(1-(progress-0.7f)/0.3f);

		particle.setColor(particle.getColor().withAlpha(alpha));
	}

	@SideOnly(Side.CLIENT)
	private static void getShockwaveTransitionProgram(ParticleModel particle, float partialTicks)
	{
		float progress = particle.getProgress(0);
		particle.setScale(IIUtils.ONE.scale(1+progress*particle.getMaxLifeTime()));
		particle.setColor(particle.getColor().withAlpha(1-progress));
	}
}
