package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleModelFactory;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleVanillaFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.*;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils.PositionGenerator;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleOffspring;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProgram;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIFileUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIFileUtils.ResourceException;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.vecmath.Vector3f;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.04.2024
 */
public class IIParticles
{
	//--- Particle IDs Reference ---//
	//Gunfire and related
	public static final String PARTICLE_GUNFIRE = "ammo/gunfire";

	@SideOnly(Side.CLIENT)
	public static void preInit()
	{
		//Register particle types
		ParticleRegistry.registerParticleType("ParticleModel", () -> new ParticleModelFactory<>(ParticleAMTModel::new));
		ParticleRegistry.registerParticleType("ParticleVanilla", () -> new ParticleVanillaFactory(ParticleVanilla::new));
		ParticleRegistry.registerParticleType("ParticleRibbon", () -> new ParticleVanillaFactory(ParticleRibbon::new));
		ParticleRegistry.registerParticleType("ParticleGlow", () -> new ParticleFactory<>(ParticleGlow::new));
		ParticleRegistry.registerParticleType("ParticleLightning", () -> new ParticleFactory<>(ParticleLightning::new));

		//Register programs
		ParticleRegistry.registerProgram(() -> new ParticleProgram("dust_transition")
		{
			@Override
			public void onParticleRender(AbstractParticle particle, float partialTicks)
			{
				float progress = particle.getProgress(partialTicks);
				particle.setProperty(ParticleProperties.SCALE, 1f+progress*0.5f);
				particle.setProperty(ParticleProperties.ALPHA, 1f-progress);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("smoke_transition")
		{
			float defaultAlpha = 1f;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				if(args.length > 0)
					defaultAlpha = Float.parseFloat(args[0]);
			}

			@Override
			public void onParticleRender(AbstractParticle particle, float partialTicks)
			{
				float progress = particle.getProgress(partialTicks);
				particle.setProperty(ParticleProperties.SCALE, 1+progress);

				float alpha;
				if(progress < 0.2)
					alpha = (progress/0.2f)*0.75f;
				else if(progress < 0.7)
					alpha = 0.75f-0.5f*((progress-0.2f)/0.5f);
				else
					alpha = 0.25f*(1-(progress-0.7f)/0.3f);

				particle.setProperty(ParticleProperties.ALPHA, alpha*defaultAlpha);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("shockwave_transition")
		{
			@Override
			public void onParticleRender(AbstractParticle particle, float partialTicks)
			{
				float progress = particle.getProgress(partialTicks);
				particle.setProperty(ParticleProperties.SCALE, 1+progress*2);
				particle.setProperty(ParticleProperties.ALPHA, 1-progress);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("color_transition")
		{
			IIColor[] colors = new IIColor[]{IIColor.WHITE, IIColor.WHITE};

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				if(args.length < 2)
					throw new ParticleArgumentException("Color transition requires at least two hexadecimal colors.");

				colors = new IIColor[args.length];
				for(int i = 0; i < args.length; i++)
				{
					String hex = args[i].trim();
					if(hex.startsWith("#"))
						hex = hex.substring(1);
					else if(hex.startsWith("0x")||hex.startsWith("0X"))
						hex = hex.substring(2);

					if(!hex.matches("(?i)[0-9a-f]{6}([0-9a-f]{2})?"))
						throw new ParticleArgumentException("Invalid hexadecimal color: "+args[i]);
					colors[i] = IIColor.fromHex(hex);
				}
			}

			@Override
			public void onParticleRender(AbstractParticle particle, float partialTicks)
			{
				float scaledProgress = particle.getProgress(partialTicks)*(colors.length-1);
				int segment = Math.min(colors.length-2, MathHelper.floor(scaledProgress));
				float segmentProgress = scaledProgress-segment;
				float[] rgb = colors[segment].mixedWith(colors[segment+1], segmentProgress).getFloatRGB();

				//Set RGB separately so that another program can control alpha.
				particle.setProperty(ParticleProperties.RED, rgb[0]);
				particle.setProperty(ParticleProperties.GREEN, rgb[1]);
				particle.setProperty(ParticleProperties.BLUE, rgb[2]);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("lifetime_retexture")
		{
			@Override
			public void onParticleRender(AbstractParticle particle, float partialTicks)
			{
				int textures = (int)particle.getProperty(ParticleProperties.TEXTURES_COUNT);
				particle.setProperty(ParticleProperties.TEXTURE_SHIFT, (int)MathHelper.clamp(particle.getProgress(partialTicks)*textures, 0, textures));
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("retexture")
		{
			int textureID = -1;
			ResLoc res = null;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				try
				{
					textureID = Integer.parseInt(args[0]);
					res = ResLoc.of(args[1]);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Retexture program requires a texture ID and a resource location as an arguments!");
				}
			}

			@Override
			public void onParticleCreation(AbstractParticle particle)
			{
				if(textureID==-1||res==null||!(particle instanceof ParticleAbstractModel))
					return;
				((ParticleAbstractModel)particle).retexture(textureID, res);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("gravity")
		{
			float dampen = 1, gravity = 0;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				try
				{
					dampen = Float.parseFloat(args[0]);
					gravity = Float.parseFloat(args[1]);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Gravity program requires a motion dampening and gravity value as an arguments!");
				}
			}

			@Override
			public void onParticleMovement(AbstractParticle particle)
			{
				Vector3f motion = (Vector3f)particle.getProperty(ParticleProperties.MOTION);
				motion.scale(dampen);
				motion.y -= gravity;
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("set_rotation")
		{
			float yaw = 0, pitch = 0;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				try
				{
					yaw = Float.parseFloat(args[0]);
					pitch = Float.parseFloat(args[1]);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Set rotation program requires a yaw and pitch value as an arguments!");
				}
			}

			@Override
			public void onParticleCreation(AbstractParticle particle)
			{
				super.onParticleCreation(particle);
				particle.setProperty(ParticleProperties.ROTATION_YAW, yaw);
				particle.setProperty(ParticleProperties.ROTATION_PITCH, pitch);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("rotation")
		{
			float yawSpeed = 0, pitchSpeed = 0;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				try
				{
					yawSpeed = Float.parseFloat(args[0]);
					pitchSpeed = Float.parseFloat(args[1]);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Rotation program requires a yaw and pitch value as an arguments!");
				}
			}

			@Override
			public void onParticleMovement(AbstractParticle particle)
			{
				if(!((Boolean)particle.getProperty(ParticleProperties.ON_GROUND)))
				{
					float yaw = MathHelper.wrapDegrees((float)particle.getProperty(ParticleProperties.ROTATION_YAW)+yawSpeed);
					float pitch = MathHelper.wrapDegrees((float)particle.getProperty(ParticleProperties.ROTATION_PITCH)+pitchSpeed);
					particle.setProperty(ParticleProperties.ROTATION_YAW, yaw);
					particle.setProperty(ParticleProperties.ROTATION_PITCH, pitch);
				}
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("emitter")
		{
			int interval = 20;
			ParticleOffspring<?> offspring = null;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				try
				{
					interval = Integer.parseInt(args[0]);
					List<ParticleProperties> inheritedProperties = new ArrayList<>();
					for(int i = 7; i < args.length; i++)
						inheritedProperties.add(ParticleProperties.valueOf(args[i]));

					offspring = new ParticleOffspring<>(
							args[1],
							PositionGenerator.valueOf(args[2].toUpperCase()),
							Float.parseFloat(args[3]),
							Integer.parseInt(args[4]),
							args.length > 5?Integer.parseInt(args[5]): Integer.parseInt(args[4]),
							inheritedProperties
					);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Could not parse emitter program arguments, "+e.getMessage());
				}
			}

			@Override
			public void onParticleMovement(AbstractParticle particle)
			{
				if((int)particle.getProperty(ParticleProperties.LIFETIME)%interval==0)
					offspring.spawn(particle);
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("grass_color")
		{
			@Override
			public void onParticleCreation(AbstractParticle particle)
			{
				super.onParticleCreation(particle);
				//Get block position
				Vector3f position = (Vector3f)particle.getProperty(ParticleProperties.POSITION);
				BlockPos blockPos = new BlockPos(position.x, position.y, position.z);
				Biome biome = particle.getWorld().getBiome(blockPos);
				//Set color
				particle.setProperty(ParticleProperties.COLOR, IIColor.fromPackedRGB(biome.getGrassColorAtPos(blockPos)));
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("foliage_color")
		{
			@Override
			public void onParticleCreation(AbstractParticle particle)
			{
				super.onParticleCreation(particle);
				//Get block position
				Vector3f position = (Vector3f)particle.getProperty(ParticleProperties.POSITION);
				BlockPos blockPos = new BlockPos(position.x, position.y, position.z);
				Biome biome = particle.getWorld().getBiome(blockPos);
				//Set color
				particle.setProperty(ParticleProperties.COLOR, IIColor.fromPackedRGB(biome.getFoliageColorAtPos(blockPos)));
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("block_color")
		{
			int sampleRadius = 1;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				super.initArguments(args);
				try
				{
					sampleRadius = IIStringUtil.parseInt(args[0]);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Block color program requires a radius as an argument!");
				}
			}

			@Override
			public void onParticleCreation(AbstractParticle particle)
			{
				super.onParticleCreation(particle);
				//Get block position
				Vector3f position = (Vector3f)particle.getProperty(ParticleProperties.POSITION);
				BlockPos center = new BlockPos(position.x, position.y, position.z);
				World world = particle.getWorld();

				//Get color from block
				IIUtils.getBlocksInOrb(world, center, sampleRadius, false)
						.stream()
						.map(pos -> ClientUtils.getSideTexture(world.getBlockState(pos), EnumFacing.WEST))
						.filter(Objects::nonNull)
						.findFirst()
						.map(ResLoc::of)
						.map(IIClientUtils::getColorFromTexture)
						.ifPresent(color -> particle.setProperty(ParticleProperties.COLOR, color));
			}
		});
		ParticleRegistry.registerProgram(() -> new ParticleProgram("fluid_color")
		{
			int sampleRadius = 1;

			@Override
			public void initArguments(String... args) throws ParticleArgumentException
			{
				super.initArguments(args);
				try
				{
					sampleRadius = IIStringUtil.parseInt(args[0]);
				} catch(Exception e)
				{
					throw new ParticleArgumentException("Fluid color program requires a radius as an argument!");
				}
			}

			@Override
			public void onParticleCreation(AbstractParticle particle)
			{
				super.onParticleCreation(particle);
				//Get block position
				Vector3f position = (Vector3f)particle.getProperty(ParticleProperties.POSITION);
				BlockPos center = new BlockPos(position.x, position.y, position.z);
				World world = particle.getWorld();

				//Get color from block
				for(BlockPos pos : IIUtils.getBlocksInOrb(world, center, sampleRadius, false))
				{
					IBlockState state = world.getBlockState(pos);
					Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
					if(fluid!=null)
					{
						particle.setProperty(ParticleProperties.COLOR, IIClientUtils.getFluidTextureColor(fluid));
						break;
					}
				}
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public static void init()
	{
		//Load particles from resource packs and jar
		List<ResourceLocation> locations = new ArrayList<>();
		try
		{
			for(IResourcePack pack : IIFileUtils.getAllResourcePacks())
				locations.addAll(IIFileUtils.getLocationsInResourcePack(pack, "particles", f -> f.endsWith(".fx.amt")));
		} catch(ResourceException e)
		{

			IILogger.error("Failed to load particles from jar! ");
			IILogger.error(e);
		}

		//Register listed particles
		locations.stream().distinct().forEach(location -> {
			String path = location.getResourcePath();
			String name = path.substring(path.indexOf("particles")+10, path.length()-7)
					.replace(File.separator, "/");
			ParticleRegistry.registerParticle(name);
		});
	}
}
