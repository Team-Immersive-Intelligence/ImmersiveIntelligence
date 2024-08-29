package pl.pabilo8.immersiveintelligence.client.fx.builder;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleVanilla;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 13.05.2024
 */
public class ParticleVanillaBuilder extends ParticleBuilder<ParticleVanilla> implements IReloadableModelContainer<ParticleVanillaBuilder>
{
	private static ResLoc EMPTY_TEXTURE = ResLoc.of(IIReference.RES_TEXTURES, "empty").withExtension(ResLoc.EXT_PNG);

	List<ResourceLocation> textures = new ArrayList<>();
	public TextureAtlasSprite[] compiledTextures;

	public ParticleVanillaBuilder(BiFunction<World, Vec3d, ParticleVanilla> particleConstructor)
	{
		super(particleConstructor);
	}

	@Override
	public void parseBuilderFromJSON(EasyNBT nbt)
	{
		super.parseBuilderFromJSON(nbt);
		if(nbt.hasKey("textures"))
		{
			nbt.streamList(NBTTagString.class, "textures")
					.map(NBTTagString::getString)
					.map(ResourceLocation::new)
					.forEach(textures::add);
		}
	}

	public ParticleVanillaBuilder addTexture(ResourceLocation texture)
	{
		textures.add(texture);
		return this;
	}

	@Nonnull
	@Override
	public ParticleVanilla buildParticle(Vec3d position, Vec3d motion, Vec3d direction)
	{
		ParticleVanilla particle = super.buildParticle(position, motion, direction);

		if(compiledTextures.length==0)
			particle.setTextures(new TextureAtlasSprite[]{ClientUtils.getSprite(EMPTY_TEXTURE)});
		else
			particle.setTextures(compiledTextures);
		return particle;
	}

	@Override
	public void reloadModels()
	{
		//Load textures
		compiledTextures = textures.stream()
				.map(ClientUtils::getSprite)
				.toArray(TextureAtlasSprite[]::new);
	}
}
