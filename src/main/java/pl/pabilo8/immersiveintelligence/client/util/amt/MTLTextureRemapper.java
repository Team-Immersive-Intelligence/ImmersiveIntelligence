package pl.pabilo8.immersiveintelligence.client.util.amt;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.MaterialLibrary;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 18.02.2023
 */
public class MTLTextureRemapper implements Function<ResourceLocation, ResourceLocation>
{
	private final Map<ResourceLocation, ResourceLocation> map;

	public MTLTextureRemapper(Map<String, ResourceLocation> source, Map<String, ResourceLocation> destination)
	{
		map = source.entrySet()
				.stream()
				.filter(e -> destination.containsKey(e.getKey()))
				.collect(Collectors.toMap(
						Entry::getValue,
						e -> destination.get(e.getKey())
				));
	}

	public MTLTextureRemapper(OBJModel source, ResourceLocation destination)
	{
		this(getSourceMap(source), IIAnimationLoader.loadMTL(destination));
	}

	public MTLTextureRemapper(OBJBakedModel source, ResourceLocation destination)
	{
		this(source.getModel(), destination);
	}

	/**
	 *
	 * @param source source OBJ model
	 * @return material->texture map of this model
	 */
	private static Map<String, ResourceLocation> getSourceMap(OBJModel source)
	{
		MaterialLibrary matLib = source.getMatLib();
		ImmutableList<String> materials = matLib.getMaterialNames();
		return materials.stream()
				.collect(Collectors.toMap(m -> m, m -> matLib.getMaterial(m).getTexture().getTextureLocation()));
	}

	@Override
	public ResourceLocation apply(ResourceLocation res)
	{
		return map.getOrDefault(res, res);
	}
}
