package pl.pabilo8.immersiveintelligence.client.fx;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleModelFactory;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleVanillaFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleAMTModel;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleAbstractModel;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleVanilla;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils.PositionGenerator;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleOffspring;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProgram;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIFileUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIFileUtils.ResourceException;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.vecmath.Vector3f;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

				particle.setProperty(ParticleProperties.ALPHA, alpha);
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
