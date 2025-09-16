package pl.pabilo8.immersiveintelligence.client.fx.factories;

import net.minecraft.client.renderer.texture.TextureMap;
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
import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 13.05.2024
 */
public class ParticleVanillaFactory extends ParticleFactory<ParticleVanilla> implements IReloadableModelContainer<ParticleVanillaFactory>
{
	private static ResLoc EMPTY_TEXTURE = ResLoc.of(IIReference.RES_TEXTURES, "empty").withExtension(ResLoc.EXT_PNG);
	public ResourceLocation[] compiledTextures;
	List<ResourceLocation> textures = new ArrayList<>();

	public ParticleVanillaFactory(BiFunction<World, Vec3d, ParticleVanilla> particleConstructor)
	{
		super(particleConstructor);
	}

	@Override
	public void parseNBT(EasyNBT nbt)
	{
		super.parseNBT(nbt);
		if(nbt.hasKey("textures"))
		{
			nbt.streamList(NBTTagString.class, "textures")
					.map(NBTTagString::getString)
					.map(ResourceLocation::new)
					.forEach(textures::add);
		}
	}

	public ParticleVanillaFactory addTexture(ResourceLocation texture)
	{
		textures.add(texture);
		return this;
	}

	@Nonnull
	@Override
	public ParticleVanilla create(Vec3d position, Vec3d motion, Vector2f rotation)
	{
		ParticleVanilla particle = super.create(position, motion, rotation);

		if(compiledTextures.length==0)
			particle.setTextureSprites(new ResLoc[]{EMPTY_TEXTURE});
		else
			particle.setTextureSprites(compiledTextures);
		return particle;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		for(ResourceLocation texture : textures)
			map.registerSprite(texture);
	}

	@Override
	public void reloadModels()
	{
		//Load textures
		compiledTextures = textures.toArray(new ResourceLocation[0]);
	}
}
