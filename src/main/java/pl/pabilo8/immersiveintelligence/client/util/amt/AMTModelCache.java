package pl.pabilo8.immersiveintelligence.client.util.amt;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @since 17.02.2023
 */
public abstract class AMTModelCache<T> extends HashMap<String, AMT[]> implements Iterable<AMT>
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
	private final AMT[] base;
	/**
	 * Last used model
	 */
	private AMT[] lastPicked = null;

	public AMTModelCache(@Nonnull OBJModel[] models, @Nonnull BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider,
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

	public AMT[] getVariant(@Nullable String name, T parameter)
	{
		if(containsKey(name))
			return (lastPicked = get(name));
		put(name, lastPicked = getAMTModel(parameter));
		return lastPicked;
	}

	@Nullable
	public AMT[] getBase()
	{
		return base;
	}

	@Nullable
	public AMT[] getLast()
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
		return Iterators.forArray(lastPicked);
	}

	//--- Utils ---//

	@SuppressWarnings("deprecation")
	protected AMT[] getAMTModel(T parameter)
	{

		ArrayList<AMT> models = new ArrayList<>();

		for(OBJModel objModel : this.models)
		{
			//get group list from the unbaked model
			Map<String, Group> groups = objModel.getMatLib().getGroups();
			//turn .obj groups into AMT
			for(String group : groups.keySet())
			{
				//get default, non rotated model state
				OBJState objState = new OBJState(ImmutableList.of(group), true, ModelRotation.X0_Y0);
				//get rotation offset
				Vec3d origin = IIAnimationUtils.getHeaderOffset(header, group);

				//get quads
				BakedQuad[] quads = objModel
						.bake(objState, isBlock?DefaultVertexFormats.BLOCK: DefaultVertexFormats.ITEM, res -> textureProvider.apply(res, parameter))
						.getQuads(null, null, 0L).toArray(new BakedQuad[0]);

				//do not add empty AMTs
				if(quads.length==0)
					continue;

				models.add(new AMTQuads(group, origin, quads));
			}
		}

		//add custom AMTs | item/fluid placeholders
		models.addAll(Arrays.asList(modelProvider.apply(parameter, header)));

		//apply hierarchy from header file
		if(header!=null)
			header.applyHierarchy(models);

		return IIAnimationUtils.organise(models.toArray(new AMT[0])); //remove children from array
	}

	//--- Implementations ---//

	protected abstract T getDefaultParameter();
}
