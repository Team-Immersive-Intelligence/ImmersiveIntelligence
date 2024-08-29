package pl.pabilo8.immersiveintelligence.client.fx.builder;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.prefab.ParticleAbstractModel;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * Builder for particle effects using 3D AMT models.
 *
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 07.04.2024
 */
@SideOnly(Side.CLIENT)
public class ParticleModelBuilder<T extends ParticleAbstractModel> extends ParticleBuilder<T> implements IReloadableModelContainer<ParticleModelBuilder<T>>
{
	/**
	 * Resource Locations of the models used by this particle
	 */
	private final List<ResLoc> modelLocations = new ArrayList<>();
	/**
	 * Loaded models
	 */
	private final List<ParticleModel> loadedModels = new ArrayList<>();
	/**
	 * Mode of how the model(s) is going to be handled
	 */
	private ModelMode mode = null;

	public ParticleModelBuilder(BiFunction<World, Vec3d, T> particleConstructor)
	{
		super(particleConstructor);
	}

	@Override
	public void parseBuilderFromJSON(EasyNBT nbt)
	{
		super.parseBuilderFromJSON(nbt);
		if(nbt.hasKey("model"))
			withModel(IIReference.RES_PARTICLE_MODEL.with(nbt.getString("model")).withExtension(ResLoc.EXT_OBJ));
		else if(nbt.hasKey("models"))
			withModelVariant(nbt.streamList(NBTTagString.class, "models", EasyNBT.TAG_STRING)
					.map(NBTTagString::getString)
					.map(s -> ResLoc.of(IIReference.RES_PARTICLE_MODEL, s).withExtension(ResLoc.EXT_OBJ))
					.toArray(ResLoc[]::new)
			);
		else if(nbt.hasKey("model_range"))
		{
			EasyNBT inside = nbt.getEasyCompound("model_range");
			withModelVariant(
					IIReference.RES_PARTICLE_MODEL.with(nbt.getString("model")).withExtension(ResLoc.EXT_OBJ),
					inside.getInt("first"),
					inside.getInt("last")
			);
		}
	}

	//--- Models ---//

	/**
	 * Adds a single model location to the particle.
	 * For multiple models use {@link #withModelVariant(ResLoc...)} or {@link #withModelVariant(ResLoc, int, int)} instead.
	 *
	 * @param modelLocation Resource Location of the model
	 * @return this
	 */
	public ParticleModelBuilder<T> withModel(ResLoc modelLocation)
	{
		if(errorInvalidModelMode(modelLocation, ModelMode.SINGLE_MODEL))
			return this;

		modelLocations.add(modelLocation);
		mode = ModelMode.SINGLE_MODEL;
		return this;
	}

	/**
	 * Adds one or more models (of a set of models) to the particle to be randomly picked on creation.<br>
	 * For particles with single model use {@link #withModel(ResLoc)}.
	 *
	 * @param modelLocation Resource Locations of the model(s)
	 * @return this
	 */
	public ParticleModelBuilder<T> withModelVariant(ResLoc... modelLocation)
	{
		if(errorInvalidModelMode(modelLocation[0], ModelMode.MULTI_MODEL))
			return this;

		Collections.addAll(modelLocations, modelLocation);
		mode = ModelMode.MULTI_MODEL;
		return this;
	}

	/**
	 * Adds a range of models locations suffixed with index number to the particle to be randomly picked on creation.<br>
	 *
	 * @param modelLocation Resource Location of the model
	 * @param firstID       First index number
	 * @param lastID        Last index number
	 * @return this
	 */
	public ParticleModelBuilder<T> withModelVariant(ResLoc modelLocation, int firstID, int lastID)
	{
		return withModelVariant(IntStream.range(firstID, lastID).mapToObj(i -> ResLoc.of(modelLocation, i)).toArray(ResLoc[]::new));
	}

	/**
	 * Displays a standard error message if the model mode is invalid.
	 *
	 * @param modelLocation   Resource Location of the model
	 * @param destinationMode Expected model mode
	 * @return true if the model mode is invalid
	 */
	@Nullable
	private boolean errorInvalidModelMode(ResLoc modelLocation, ModelMode destinationMode)
	{
		if(mode==null||mode==destinationMode)
			return false;

		IILogger.error("Could not add particle model {}. Only one model mode is allowed at a time.", modelLocation);
		return true;
	}

	//--- Builder Methods ---//

	@Nonnull
	@Override
	public T buildParticle(Vec3d position, Vec3d motion, Vec3d direction)
	{
		T particle = super.buildParticle(position, motion, direction);

		if(loadedModels.isEmpty())
			return particle;

		switch(mode)
		{
			case SINGLE_MODEL:
				//get the first model
				particle.setModel(loadedModels.get(0));
				break;
			case MULTI_MODEL:
				//get a random model from
				particle.setModel(loadedModels.get(Utils.RAND.nextInt(loadedModels.size())));
				break;
			default:
				break;
		}

		return particle;
	}

	//--- IReloadableModelContainer ---//

	@Override
	public void reloadModels()
	{
		loadedModels.clear();

		//no baking, just direct loading
		for(ResLoc modelLocation : modelLocations)
		{
			//load a raw obj model
			OBJModel objModel = IIAnimationUtils.modelFromRes(modelLocation);
			if(objModel==null)
				continue;

			MaterialLibrary matLib = objModel.getMatLib();

			Material[] materials = matLib.getMaterialNames().stream()
					.sorted()
					.filter(s -> !(s.equals(Material.DEFAULT_NAME)||s.equals(Material.WHITE_NAME)))
					.filter(s -> matLib.getMaterial(s).getTexture()!=Texture.WHITE)
					.map(matLib::getMaterial)
					.toArray(Material[]::new);

			ParticleModel model = new ParticleModel(
					matLib.getGroups().values().stream()
							.map(Group::getFaces)
							.flatMap(Collection::stream)
							.toArray(Face[]::new),
					materials
			);

			//add to the list
			loadedModels.add(model);
		}
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		//load textures from MTL referenced by OBJ
		for(ResLoc modelLocation : modelLocations)
			IIAnimationLoader.preloadTexturesFromOBJ(modelLocation, map);
	}

	//--- Supporting Classes ---//

	public static class ParticleModel
	{
		public final int elementsCount;
		public final Vec3d[] positions;
		public final Vec3d[] normals;
		public final Vec2f[] uv;
		public final byte[] tex;

		public TextureAtlasSprite[] textures;

		public ParticleModel(Face[] faces, Material[] materials)
		{
			elementsCount = faces.length*4;
			positions = new Vec3d[elementsCount];
			normals = new Vec3d[elementsCount];
			uv = new Vec2f[elementsCount];
			tex = new byte[elementsCount];

			this.textures = Arrays.stream(materials)
					.map(s -> s.getTexture().getTextureLocation())
					.map(ClientUtils::getSprite)
					.toArray(TextureAtlasSprite[]::new);

			HashMap<Material, Integer> textureMap = new HashMap<>();
			for(int i = 0; i < materials.length; i++)
				textureMap.put(materials[i], i);


			for(int i = 0; i < faces.length; i++)
			{
				Face face = faces[i];
				for(int j = 0; j < 4; j++)
				{
					Vertex vertex = face.getVertices()[j];
					positions[i*4+j] = new Vec3d(vertex.getPos().x, vertex.getPos().y, vertex.getPos().z);
					normals[i*4+j] = new Vec3d(vertex.getNormal().x, vertex.getNormal().y, vertex.getNormal().z);
					uv[i*4+j] = new Vec2f(vertex.getTextureCoordinate().u, vertex.getTextureCoordinate().v);

					Integer texID = textureMap.get(vertex.getMaterial());
					tex[i*4+j] = texID==null?0: texID.byteValue();
				}
			}
		}
	}

	private enum ModelMode
	{
		SINGLE_MODEL,
		MULTI_MODEL
	}
}
