package pl.pabilo8.immersiveintelligence.client.model.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Pabilo8 on 13-07-2019.
 * Based on jabelar's tutorial
 * http://jabelarminecraft.blogspot.com/p/minecraft-modding-custom-fluid-handling.html
 * https://github.com/jabelar/ExampleMod-1.12/blob/master/src/main/java/com/blogspot/jabelarminecraft/examplemod/client/models/ModelSlimeBag.java
 * Also used the bottles' transform code from Rustic (https://github.com/the-realest-stu/Rustic/blob/master/src/main/java/rustic/common/items/ItemFluidBottle.java)
 */

public class ModelMeasuringCup extends ModelAbstractItem
{
	public static final ModelResourceLocation LOCATION = new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "measuring_cup"), "inventory");
	public static final ModelMeasuringCup MODEL = new ModelMeasuringCup();
	// minimal Z offset to prevent depth-fighting
	private static final float NORTH_Z_FLUID = 7.498f/16f;
	private static final float SOUTH_Z_FLUID = 8.502f/16f;
	private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1),
			null);
	@Nullable
	private final ResourceLocation emptyLocation = new ResourceLocation(ImmersiveIntelligence.MODID, "items/measuring_cup");
	@Nullable
	private final ResourceLocation filledLocation = new ResourceLocation(ImmersiveIntelligence.MODID, "items/measuring_cup_fluid");
	@Nullable
	private final Fluid fluid;

	public ModelMeasuringCup()
	{
		this(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.minecraftforge.client.model.IModel#getTextures()
	 */

	public ModelMeasuringCup(Fluid parFluid)
	{
		fluid = parFluid;
	}

	private static ImmutableMap<TransformType, TRSRTransformation> itemTransforms()
	{
		TRSRTransformation thirdperson = get(0, 4.0f, 0.5f, 0, -90, 55, 0.55f);
		TRSRTransformation firstperson = get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f);
		ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
		builder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
		builder.put(TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
		builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
		builder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdperson));
		builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, firstperson);
		builder.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstperson));
		return builder.build();
	}

	private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
	{
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
				new Vector3f(tx/16, ty/16, tz/16),
				TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)), new Vector3f(s, s, s), null));
	}

	private static TRSRTransformation leftify(TRSRTransformation transform)
	{
		return TRSRTransformation.blockCenterToCorner(
				flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
	}

	@Override
	public ModelResourceLocation getLocation()
	{
		return LOCATION;
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
		if(filledLocation!=null)
			builder.add(filledLocation);
		if(emptyLocation!=null)
			builder.add(emptyLocation);

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.minecraftforge.client.model.IModel#getDependencies()
	 */
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
							Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
		TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());

		TextureAtlasSprite fluidSprite = null;
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

		if(emptyLocation!=null)
		{
			IBakedModel model = (new ItemLayerModel(ImmutableList.of(emptyLocation))).bake(state, format, bakedTextureGetter);
			builder.addAll(model.getQuads(null, null, 0));
		}

		if(fluid!=null)
		{
			fluidSprite = bakedTextureGetter.apply(fluid.getStill());
		}

		if(filledLocation!=null&&fluidSprite!=null)
		{
			TextureAtlasSprite filledTexture = bakedTextureGetter.apply(filledLocation);
			builder.addAll(
					ItemTextureQuadConverter.convertTexture(format, transform, filledTexture, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, fluid.getColor()));
			builder.addAll(
					ItemTextureQuadConverter.convertTexture(format, transform, filledTexture, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, fluid.getColor()));
		}

		return new Baked(this, builder.build(), fluidSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

	@Override
	public ModelMeasuringCup process(ImmutableMap<String, String> customData)
	{
		String fluidName = customData.get("fluid");
		Fluid fluid = FluidRegistry.getFluid(fluidName);

		if(fluid==null)
			fluid = this.fluid;

		// create new model with correct liquid
		return new ModelMeasuringCup(fluid);
	}

	@Override
	public ModelMeasuringCup retexture(ImmutableMap<String, String> textures)
	{
		return new ModelMeasuringCup(fluid);
	}

	public enum MeasuringCupModelLoader implements ICustomModelLoader
	{
		INSTANCE;

		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return modelLocation.getResourceDomain().equals(ImmersiveIntelligence.MODID)&&modelLocation.getResourcePath().contains("measuring_cup");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation)
		{
			return MODEL;
		}

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			// no need to clear cache since we create a new model INSTANCE
		}
	}

	private static final class BakedOverrideHandler extends ItemOverrideList
	{
		public static final BakedOverrideHandler INSTANCE = new BakedOverrideHandler();

		private BakedOverrideHandler()
		{
			super(ImmutableList.of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			FluidStack fluidStack = null;
			if(stack.hasTagCompound()&&stack.getTagCompound().hasKey(FluidHandlerItemStack.FLUID_NBT_KEY))
			{
				fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(FluidHandlerItemStack.FLUID_NBT_KEY));
			}

			if(fluidStack==null||fluidStack.amount <= 0)
			{
				return originalModel;
			}

			Baked model = (Baked)originalModel;

			Fluid fluid = fluidStack.getFluid();
			String name = fluid.getName();

			if(!model.cache.containsKey(name))
			{
				IModel parent = model.parent.process(ImmutableMap.of("fluid", name));
				Function<ResourceLocation, TextureAtlasSprite> textureGetter;
				textureGetter = location -> ClientUtils.mc().getTextureMapBlocks().getAtlasSprite(location.toString());
				IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.format,
						textureGetter);
				model.cache.put(name, bakedModel);
				return bakedModel;
			}
			return model.cache.get(name);
		}
	}

	// the filled bucket is based on the empty bucket
	private static final class Baked implements IBakedModel
	{

		private final ModelMeasuringCup parent;
		private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
		private final ImmutableMap<TransformType, TRSRTransformation> transforms;
		private final ImmutableList<BakedQuad> quads;
		private final TextureAtlasSprite particle;
		private final VertexFormat format;

		public Baked(ModelMeasuringCup parent,
					 ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format,
					 ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
					 Map<String, IBakedModel> cache)
		{
			this.quads = quads;
			this.particle = particle;
			this.format = format;
			this.parent = parent;
			this.transforms = itemTransforms();
			this.cache = cache;
		}

		@Override
		public ItemOverrideList getOverrides()
		{
			return BakedOverrideHandler.INSTANCE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
		{
			return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
		{
			if(side==null)
				return quads;
			return ImmutableList.of();
		}

		@Override
		public boolean isAmbientOcclusion()
		{
			return true;
		}

		@Override
		public boolean isGui3d()
		{
			return false;
		}

		@Override
		public boolean isBuiltInRenderer()
		{
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			return particle;
		}
	}
}