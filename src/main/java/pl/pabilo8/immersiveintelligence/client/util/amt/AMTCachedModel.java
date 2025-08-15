package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.02.2023
 */
public abstract class AMTCachedModel<T> extends HashMap<String, AMTModel> implements AMTRenderable, Iterable<AMT>
{
	@Nonnull
	private final OBJModel[] models;
	@Nonnull
	private final BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider;
	@Nullable
	private final IIModelHeader header;
	@Nonnull
	private final BiFunction<T, IIModelHeader, AMT[]> modelProvider;
	/**
	 * Whether the {@link net.minecraft.client.renderer.vertex.VertexFormat} should be BLOCK or ITEM
	 */
	private final boolean isBlock;

	/**
	 * The base model, from which the other models are derived from
	 */
	private final AMTModel base;
	/**
	 * Last used model
	 */
	private AMTModel lastPicked = null;

	public AMTCachedModel(@Nonnull OBJModel[] models, @Nonnull BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider,
						  @Nullable IIModelHeader[] headers, @Nonnull BiFunction<T, IIModelHeader, AMT[]> modelProvider, boolean isBlock)
	{
		super();
		this.models = models;
		this.textureProvider = textureProvider;

		this.header = new IIModelHeader(headers);
		this.modelProvider = modelProvider;

		this.base = getVariant("", getDefaultParameter());
		this.isBlock = isBlock;
	}

	//--- HashMap ---//

	@Override
	public void clear()
	{
		//Get rid of the cached models
		this.values().forEach(IIAnimationUtils::disposeOf);
		super.clear();
	}

	//--- Utils ---//

	public AMTModel getVariant(@Nullable String name, T parameter)
	{
		if(containsKey(name))
			return (lastPicked = get(name));
		put(name, lastPicked = getModelForParam(parameter));
		return lastPicked;
	}

	@Nullable
	public AMTModel getBase()
	{
		return base;
	}

	@Nullable
	public AMTModel getLast()
	{
		return lastPicked;
	}

	//--- Iterable ---//

	/**
	 * @return iterator of the last picked model
	 */
	@Override
	public Iterator<AMT> iterator()
	{
		if(lastPicked==null)
			return Collections.emptyIterator();
		return lastPicked.iterator();
	}

	//--- Utils ---//

	@SuppressWarnings("deprecation")
	protected AMTModel getModelForParam(T parameter)
	{
		//Compile all AMT quads from the models
		List<AMT> collected = Arrays.stream(this.models)
				.map(model -> new AMTModel(null, model, header, null,
						resourceLocation -> textureProvider.apply(resourceLocation, parameter)))
				.map(AMTModel::getChildrenRecursive)
				.flatMap(Arrays::stream)
				.collect(Collectors.toList());

		//Add dynamic AMT from the model provider
		Collections.addAll(collected, modelProvider.apply(parameter, header));

		//Apply hierarchy from header file
		if(header!=null)
			header.applyHierarchy(collected);

		return new AMTModel(collected);
	}

	//--- Implementations ---//

	protected abstract T getDefaultParameter();

	public void defaultize()
	{
		if(lastPicked!=null)
			for(AMT amt : lastPicked)
				amt.defaultize();
	}

	public void render(Tessellator tes, BufferBuilder buf)
	{
		if(lastPicked!=null)
			for(AMT amt : lastPicked)
				amt.render(tes, buf);
	}

	@Override
	public void disposeOf()
	{
		if(lastPicked!=null)
			for(AMT amt : lastPicked)
				amt.disposeOf();
		lastPicked = null;
	}
}
