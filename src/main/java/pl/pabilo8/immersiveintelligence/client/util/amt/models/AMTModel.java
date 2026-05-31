package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTQuads;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Replacement for AMT[] arrays used before with convenient rendering methods.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.08.2025
 */
public class AMTModel implements Iterable<AMT>, AMTRenderable
{
	@Nonnull
	private final AMT[] model;

	//--- AMT Model from AMTs ---//

	public AMTModel()
	{
		model = new AMT[0];
	}

	public AMTModel(AMT... model)
	{
		this.model = model==null?new AMT[0]: model;
	}

	public AMTModel(Collection<AMT> model)
	{
		this(model.toArray(new AMT[0]));
	}

	public AMTModel(AMTModel... models)
	{
		ArrayList<AMT> modelList = new ArrayList<>();
		for(AMTModel m : models)
			Collections.addAll(modelList, m.model);
		this.model = modelList.toArray(new AMT[0]);
	}

	public AMTModel(AMTModel base, AMT... models)
	{
		ArrayList<AMT> modelList = new ArrayList<>(Arrays.asList(base.model));
		modelList.addAll(Arrays.asList(models));
		this.model = modelList.toArray(new AMT[0]);
	}

	//--- AMTModel with VertexFormats.BLOCK from existing OBJ Model ---//

	//Baked Model

	public AMTModel(@Nullable IBlockState state, OBJBakedModel model)
	{
		this(state, model, null);
	}

	public AMTModel(@Nullable IBlockState state, OBJBakedModel model, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(state, model.getModel(), AMTLoader.loadHeader(model.getModel()), custom);
	}

	//Unbaked Model

	public AMTModel(@Nullable IBlockState state, OBJModel model)
	{
		this(state, model, null);
	}

	public AMTModel(@Nullable IBlockState state, OBJModel model, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(state, model, AMTLoader.loadHeader(model), custom);
	}

	public AMTModel(@Nullable IBlockState state, OBJModel model, @Nullable AMTModelHeader header, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(getModel(state, model, header, custom, DefaultVertexFormats.BLOCK, null));
	}

	public AMTModel(@Nullable IBlockState state, OBJModel model, @Nullable AMTModelHeader header, @Nullable Function<AMTModelHeader, AMT[]> custom,
					@Nullable Function<ResourceLocation, TextureAtlasSprite> textureRemapper)
	{
		this(getModel(state, model, header, custom, DefaultVertexFormats.BLOCK, textureRemapper));
	}

	//VertexFormat

	public AMTModel(VertexFormat format, OBJModel model)
	{
		this(format, model, null);
	}

	public AMTModel(VertexFormat format, OBJModel model, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(format, model, AMTLoader.loadHeader(model), custom);
	}

	public AMTModel(VertexFormat format, OBJModel modelLocation, @Nullable AMTModelHeader header, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(format, modelLocation, header, custom, null);
	}

	public AMTModel(VertexFormat format, OBJModel modelLocation, @Nullable AMTModelHeader header, @Nullable Function<AMTModelHeader, AMT[]> custom,
					@Nullable Function<ResourceLocation, TextureAtlasSprite> textureRemapper)
	{
		this(getModel(null, modelLocation, header, custom, format, textureRemapper));
	}

	//--- AMT Model from ResLoc ---//

	public AMTModel(VertexFormat format, ResourceLocation modelLocation)
	{
		this(format, modelLocation, null);
	}

	public AMTModel(VertexFormat format, ResourceLocation modelLocation, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(getModel(null, modelLocation, AMTLoader.loadHeader(ResLoc.of(modelLocation).withExtension(ResLoc.EXT_OBJAMT)), custom, null, format));
	}

	public AMTModel(VertexFormat format, ResourceLocation modelLocation, @Nullable AMTModelHeader header, @Nullable Function<AMTModelHeader, AMT[]> custom)
	{
		this(getModel(null, modelLocation, header, custom, null, format));
	}

	public AMTModel(VertexFormat format, ResourceLocation modelLocation, @Nullable AMTModelHeader header, @Nullable Function<AMTModelHeader, AMT[]> custom,
					@Nullable Function<ResourceLocation, TextureAtlasSprite> textureRemapper)
	{
		this(getModel(null, modelLocation, header, custom, textureRemapper, format));
	}

	//--- Base Model Method ---//

	private static AMT[] getModel(@Nullable IBlockState state, ResourceLocation modelLocation, @Nullable AMTModelHeader header,
								  @Nullable Function<AMTModelHeader, AMT[]> customPartsProvider, @Nullable Function<ResourceLocation, TextureAtlasSprite> textureRemapper,
								  VertexFormat format)
	{
		try
		{
			OBJModel model = AMTUtils.modelFromRes(modelLocation);
			return getModel(state, model, header, customPartsProvider, format, null);

		} catch(Exception e)
		{
			return new AMT[0];
		}
	}

	private static AMT[] getModel(@Nullable IBlockState state, OBJModel model, @Nullable AMTModelHeader header,
								  @Nullable Function<AMTModelHeader, AMT[]> customPartProvider, VertexFormat format,
								  @Nullable Function<ResourceLocation, TextureAtlasSprite> textureRemapper)
	{
		//get group list from the unbaked model
		Map<String, Group> groups = model.getMatLib().getGroups();
		//Provide default texture remapper if not specified otherwise
		Function<ResourceLocation, TextureAtlasSprite> textureMappings = textureRemapper==null?ClientUtils::getSprite: textureRemapper;

		//create an array for AMT
		ArrayList<AMT> models = new ArrayList<>();

		//turn .obj groups into AMT
		for(String group : groups.keySet())
		{
			OBJState objState = new OBJState(ImmutableList.of(group), true, ModelRotation.X0_Y0);
			//Null for Items
			IBlockState modelState = state==null?null: ((IExtendedBlockState)state).withProperty(Properties.AnimationProperty, objState);
			Vec3d origin = header==null?Vec3d.ZERO: header.getOffset(group);

			//Register textures of this AMT to the block atlast
			model.getTextures().forEach((s) -> ApiUtils.getRegisterSprite(ClientUtils.mc().getTextureMapBlocks(), s));

			//get baked quads
			BakedQuad[] quads = model
					.bake(objState, format, textureMappings)
					.getQuads(modelState, null, 0L).toArray(new BakedQuad[0]);

			//do not load empty models, fixes obj models having an additional empty element
			if(quads.length==0)
				continue;

			models.add(new AMTQuads(group, origin, quads));
		}

		//add customPartProvider AMTs | item/fluid placeholders
		if(customPartProvider!=null)
			models.addAll(Arrays.asList(customPartProvider.apply(header)));
		//Apply model part hierarchy
		if(header!=null)
			header.applyHierarchy(models);

		//Organise model parts
		return models.stream().filter(amt -> !amt.isChild()).toArray(AMT[]::new);
	}

	//--- AMTRenderable ---//

	@Override
	public Iterator<AMT> iterator()
	{
		return new Iterator<AMT>()
		{
			private int index = 0;

			@Override
			public boolean hasNext()
			{
				return index < model.length;
			}

			@Override
			public AMT next()
			{
				return model[index++];
			}
		};
	}

	@Override
	public void defaultize()
	{
		for(AMT amt : model)
			amt.defaultize();
	}

	@Override
	public void render(Tessellator tes, BufferBuilder buf)
	{
		for(AMT amt : model)
			amt.render(tes, buf);
	}

	@Override
	public void disposeOf()
	{
		for(AMT amt : model)
			amt.disposeOf();
	}

	//--- Transformations ---//


	/**
	 * @param shrinkAmount the amount to shrink the model by, in blocks
	 * @return a copy of this model with all AMTQuads's vertices positions shrunk by face normals
	 * @implNote 0.01 is usually a good value for most models
	 */
	public AMTModel shrinkByNormals(double shrinkAmount)
	{
		AMT[] shrunkModel = stream()
				.map(amt -> amt instanceof AMTQuads?((AMTQuads)amt).shrinkByNormals(shrinkAmount): amt)
				.toArray(AMT[]::new);
		return new AMTModel(shrunkModel);
	}

	public AMTModel renamePart(String oldName, String newName)
	{
		AMT partRecursive = getPartRecursive(oldName);
		if(partRecursive!=null)
			partRecursive.rename(newName);
		return this;
	}

	/**
	 * Creates a single AMT out of all AMTQuads inside this AMTModel for more performant rendering
	 *
	 * @param name batched element name
	 * @return batched {@link AMTQuads} or {@link AMTLocator} if there's nothing to display
	 */
	public AMT batch(String name)
	{
		//Add all quads to list
		List<BakedQuad> quads = new ArrayList<>();
		for(AMT amt : getChildrenRecursive())
			if(amt instanceof AMTQuads)
				quads.addAll(Arrays.asList(((AMTQuads)amt).getQuads()));

		//Output
		return quads.isEmpty()?
				new AMTLocator(name, Vec3d.ZERO):
				new AMTQuads(name, Vec3d.ZERO, quads.toArray(new BakedQuad[0]));
	}

	public AMT[] getChildrenRecursive()
	{
		return stream()
				.map(AMT::getChildrenRecursive)
				.flatMap(Collection::stream)
				.toArray(AMT[]::new);
	}

	@Nullable
	public AMT getPart(String name)
	{
		return stream()
				.filter(amt -> amt.getName().equals(name))
				.findFirst().orElse(null);
	}

	@Nullable
	public AMT getPartRecursive(String name)
	{
		return stream()
				.map(AMT::getChildrenRecursive)
				.flatMap(Collection::stream)
				.filter(amt -> amt.getName().equals(name))
				.findFirst().orElse(null);
	}

	//--- Streams and Iteration ---//

	public Stream<AMT> stream()
	{
		return Arrays.stream(model);
	}

	public boolean isEmpty()
	{
		return model.length==0;
	}

	public AMT[] getParts()
	{
		return model;
	}

	//--- Setters ---//

	public void setVisible(boolean visible)
	{
		for(AMT amt : model)
			amt.setVisible(visible);
	}

	public void setPosition(Vec3d off)
	{
		for(AMT amt : model)
			amt.setPosition(off);
	}

	public void setScale(Vec3d scale)
	{
		for(AMT amt : model)
			amt.setScale(scale);
	}

	public void setRotation(Vec3d rot)
	{
		for(AMT amt : model)
			amt.setRotation(rot);
	}

	public void setShader(@Nullable Shaders shader, @Nonnull Float... shaderValue)
	{
		for(AMT amt : model)
			amt.setShader(shader, shaderValue);
	}

	public void setProperty(float property)
	{
		for(AMT amt : model)
			amt.setProperty(property);
	}

	//--- Utils ---//

	public Tuple<Vec3d, Vec3d> findModelBounds()
	{
		Vec3d min = new Vec3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		Vec3d max = new Vec3d(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);

		//Find the furthest vertices in each direction
		for(AMT amt : getChildrenRecursive())
			if(amt instanceof AMTQuads)
				for(BakedQuad quad : ((AMTQuads)amt).getQuads())
					for(int i = 0; i < 4; i++)
					{
						//Extract vertex position from quad data
						int vertexIndex = i*DefaultVertexFormats.BLOCK.getIntegerSize();
						int[] vertexData = quad.getVertexData();
						double x = Float.intBitsToFloat(vertexData[vertexIndex]);
						double y = Float.intBitsToFloat(vertexData[vertexIndex+1]);
						double z = Float.intBitsToFloat(vertexData[vertexIndex+2]);

						min = new Vec3d(
								Math.min(min.x, x),
								Math.min(min.y, y),
								Math.min(min.z, z)
						);
						max = new Vec3d(
								Math.max(max.x, x),
								Math.max(max.y, y),
								Math.max(max.z, z)
						);
					}
		return new Tuple<>(min, max);
	}

	public Vec3d findModelSize()
	{
		//Find model bounds
		Tuple<Vec3d, Vec3d> bounds = findModelBounds();
		Vec3d min = bounds.getFirst();
		Vec3d max = bounds.getSecond();

		//Fallback, for when the model has no quads
		if(min.x==Double.MIN_VALUE||min.y==Double.MIN_VALUE||min.z==Double.MIN_VALUE)
			return Vec3d.ZERO;

		//Calculate size from bounds
		return new Vec3d(
				max.x-min.x,
				max.y-min.y,
				max.z-min.z
		);
	}

	public Vec3d findActualModelCenter()
	{
		//Find model bounds
		Tuple<Vec3d, Vec3d> bounds = findModelBounds();
		Vec3d min = bounds.getFirst();
		Vec3d max = bounds.getSecond();

		//Calculate center from bounds
		return new Vec3d(
				(min.x+max.x)/2,
				(min.y+max.y)/2,
				(min.z+max.z)/2
		);
	}
}
