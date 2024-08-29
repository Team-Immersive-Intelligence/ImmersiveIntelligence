package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.MaterialLibrary;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.lambda.ArraylistJoinCollector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.*;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 02.04.2022
 */
@SideOnly(Side.CLIENT)
public class IIAnimationUtils
{
	//--- Empty OBJ Model Placeholder ---//
	private static final OBJModel EMPTY = new OBJModel(new MaterialLibrary(), ResLoc.of(IIReference.RES_BLOCK_MODEL, "empty.obj"));

	//--- Time Calculation ---//

	@SideOnly(Side.CLIENT)
	public static float getDebugProgress(float max, float partialTicks)
	{
		return (ClientUtils.mc().world.getTotalWorldTime()%max+partialTicks)/max;
	}

	public static float getAnimationOffsetProgress(float current, float begin, float end, float partialTicks)
	{
		if(current < begin)
			return 0;
		if(current > end)
			return 1;
		return (current-begin+partialTicks)/(end-begin);
	}

	public static float getAnimationProgress(float current, float max, boolean invert, float partialTicks)
	{
		return MathHelper.clamp(invert?(1f-((current-partialTicks)/max)): ((current+partialTicks)/max), 0, 1);
	}

	public static float getAnimationProgress(float current, float max, boolean shouldAnimate, boolean reverse, float posStep, float negStep, float partialTicks)
	{
		return current==0?0: (current==max?1: (MathHelper.clamp((current+(shouldAnimate?((reverse?-negStep: posStep)*partialTicks): 0))/max, 0f, 1f)));
	}

	/**
	 * @param progress of the full animation
	 * @param offset   of the current animation
	 * @param duration of the current animation
	 * @return animation time 0-1
	 */
	public static float getOffset(float progress, float offset, float duration)
	{
		return MathHelper.clamp((progress-offset)/duration, 0, 1);
	}

	//--- Manual Rendering Stuff ---//

	public static BlockRendererDispatcher getBRD()
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher();
	}

	/**
	 * @param tile currently rendered
	 * @return an animated model specified in the blockstate file
	 */
	@Nonnull
	public static Tuple<IBlockState, IBakedModel> getAnimationBakedModel(TileEntity tile)
	{
		//Grab model + dynamic render state
		final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());

		state = state.getBlock().getActualState(state, tile.getWorld(), tile.getPos());
		state = state.withProperty(IEProperties.DYNAMICRENDER, true);
		return new Tuple<>(state, blockRenderer.getBlockModelShapes().getModelForState(state));
	}

	//--- AMT Based Animations ---//

	/**
	 * Applies animation to an AMT
	 *
	 * @param model the AMT
	 * @param group an animation group providing the transforms
	 * @param time  of the animation in range 0.0-1.0
	 */
	public static void setModelAnimations(AMT model, IIAnimationGroup group, float time)
	{
		if(group.visibility!=null)
			model.visible = group.visibility.getForTime(time);

		if(group.position!=null)
			model.off = group.position.getForTime(time);
		if(group.rotation!=null)
			model.rot = group.rotation.getForTime(time);
		if(group.scale!=null)
			model.scale = group.scale.getForTime(time);

		if(group.shader!=null)
		{
			model.shader = group.shader.getShader();
			model.shaderValue = group.shader.getForTime(time);
		}

		if(group.property!=null)
			model.property = group.property.getForTime(time);

	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void setModelVisibility(AMT[] models, boolean visible)
	{
		for(AMT model : models)
			model.visible = visible;
	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void setModelVisibility(AMT model, boolean visible)
	{
		model.visible = visible;
	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void setModelRotation(AMT model, double pitch, double yaw, double roll)
	{
		model.rot = new Vec3d(pitch, yaw, roll);
	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void addModelRotation(AMT model, double pitch, double yaw, double roll)
	{
		if(model.rot==null)
			setModelRotation(model, pitch, yaw, roll);
		else model.rot = new Vec3d(model.rot.x+pitch, model.rot.y+yaw, model.rot.z+roll);
	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void setModelRotation(AMT amt, Vec3d rotation)
	{
		setModelRotation(amt, rotation.x, rotation.y, rotation.z);
	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void setModelTranslation(AMT model, Vec3d vec)
	{
		model.off = vec;
	}

	/**
	 * Manual approach, use in things requiring a direct value instead of an animation
	 */
	public static void addModelTranslation(AMT model, Vec3d vec)
	{
		if(model.off==null)
			setModelTranslation(model, vec);
		else model.off = model.off.add(vec);
	}

	public static AMT[] getAMT(Tuple<IBlockState, IBakedModel> model, @Nullable IIModelHeader header)
	{
		return getAMT(model, header, h -> new AMT[0]);
	}

	/**
	 * @param model  the model to turn into AMTs
	 * @param header the header file providing the group offsets and hierarchy
	 * @return Animated Model Thingies
	 */
	public static AMT[] getAMT(Tuple<IBlockState, IBakedModel> model, @Nullable IIModelHeader header, @Nonnull Function<IIModelHeader, AMT[]> custom)
	{
		// TODO: 17.07.2022 improve
		IESmartObjModel baked = (IESmartObjModel)(model.getSecond());

		//get group list from the unbaked model
		Map<String, Group> groups = baked.getModel().getMatLib().getGroups();
		//create an array for AMT

		ArrayList<AMT> models = new ArrayList<>();

		//turn .obj groups into AMT
		for(String group : groups.keySet())
		{
			OBJState objState = new OBJState(ImmutableList.of(group), true, ModelRotation.X0_Y0);
			IExtendedBlockState bstate = ((IExtendedBlockState)model.getFirst()).withProperty(Properties.AnimationProperty, objState);

			Vec3d origin = getHeaderOffset(header, group);

			baked.getTextures().forEach((s, textureAtlasSprite) -> ApiUtils.getRegisterSprite(ClientUtils.mc().getTextureMapBlocks(), textureAtlasSprite.getIconName()));

			//get baked quads
			BakedQuad[] quads = baked.getModel()
					.bake(objState, DefaultVertexFormats.BLOCK, ClientUtils::getSprite)
					.getQuads(bstate, null, 0L).toArray(new BakedQuad[0]);

			//do not load empty models, fixes obj models having an additional empty element
			if(quads.length==0)
				continue;

			models.add(new AMTQuads(group, origin, quads));
		}

		//add custom AMTs | item/fluid placeholders
		models.addAll(Arrays.asList(custom.apply(header)));

		if(header!=null)
			header.applyHierarchy(models);

		return organise(models.toArray(new AMT[0])); //remove children from array
	}

	public static Vec3d getHeaderOffset(IIModelHeader header, String name)
	{
		if(header!=null)
			return header.getOffset(name);
		return Vec3d.ZERO;
	}

	public static OBJModel modelFromRes(ResourceLocation res)
	{
		try
		{
			OBJModel model = (OBJModel)OBJLoader.INSTANCE.loadModel(res);
			return ((OBJModel)model.process(ImmutableMap.of("flip-v", String.valueOf(true))));
		} catch(Exception ignored)
		{
			IILogger.error("Couldn't load model for {}, either the path used is incorrect or a model file may be missing!", res);
			return EMPTY;
//			return ((OBJModel)ModelLoaderRegistry.getMissingModel());
		}
	}

	public static AMT[] getAMTFromRes(ResourceLocation res, @Nullable ResourceLocation headerRes)
	{
		return getAMTFromRes(res, headerRes, h -> new AMT[0]);
	}

	public static AMT[] getAMTFromRes(ResourceLocation res, @Nullable ResourceLocation headerRes, @Nonnull Function<IIModelHeader, AMT[]> custom)
	{
		try
		{
			/*IIAnimationLoader.preloadTexturesFromMTL(new ResourceLocation(
					res.getResourceDomain(), res.getResourcePath().replace(".obj.ie", ".mtl")));*/
			OBJModel model = (OBJModel)OBJLoader.INSTANCE.loadModel(res).process(ImmutableMap.of("flip-v", String.valueOf(true)));

			IIModelHeader header = headerRes==null?null: IIAnimationLoader.loadHeader(headerRes);

			return getAMTInternal(null, model, header, custom);

		} catch(Exception ignored)
		{
		}
		return new AMT[0];
	}

	private static AMT[] getAMTInternal(@Nullable IBlockState bState, @Nonnull OBJModel model, @Nullable IIModelHeader header, @Nonnull Function<IIModelHeader, AMT[]> custom)
	{
		//get group list from the unbaked model
		Map<String, Group> groups = model.getMatLib().getGroups();

		ArrayList<AMT> models = new ArrayList<>();

		//turn .obj groups into AMT
		for(String group : groups.keySet())
		{
			//get default, non rotated model state
			OBJState objState = new OBJState(ImmutableList.of(group), true, ModelRotation.X0_Y0);

			//get rotation offset
			Vec3d origin = getHeaderOffset(header, group);

			//get quads
			BakedQuad[] quads = model
					.bake(objState, DefaultVertexFormats.BLOCK, ClientUtils::getSprite)
					.getQuads(bState, null, 0L).toArray(new BakedQuad[0]);

			//do not add empty AMTs
			if(quads.length==0)
				continue;

			models.add(new AMTQuads(group, origin, quads));
		}

		//add custom AMTs | item/fluid placeholders
		models.addAll(Arrays.asList(custom.apply(header)));

		//apply hierarchy from header file
		if(header!=null)
			header.applyHierarchy(models);

		return organise(models.toArray(new AMT[0])); //remove children from array
	}

	public static AMT[] getAMTItemModel(@Nonnull OBJModel model, @Nullable IIModelHeader header, @Nonnull Function<IIModelHeader, AMT[]> custom)
	{
		//get group list from the unbaked model
		Map<String, Group> groups = model.getMatLib().getGroups();

		ArrayList<AMT> models = new ArrayList<>();

		//turn .obj groups into AMT
		for(String group : groups.keySet())
		{
			//get default, non rotated model state
			OBJState objState = new OBJState(ImmutableList.of(group), true, ModelRotation.X0_Y0);

			//get rotation offset
			Vec3d origin = getHeaderOffset(header, group);

			//get quads
			BakedQuad[] quads = model
					.bake(objState, DefaultVertexFormats.ITEM, ClientUtils::getSprite)
					.getQuads(null, null, 0L).toArray(new BakedQuad[0]);

			//do not add empty AMTs
			if(quads.length==0)
				continue;

			models.add(new AMTQuads(group, origin, quads));
		}

		//add custom AMTs | item/fluid placeholders
		models.addAll(Arrays.asList(custom.apply(header)));

		//apply hierarchy from header file
		if(header!=null)
			header.applyHierarchy(models);

		return organise(models.toArray(new AMT[0])); //remove children from array
	}

	@Nonnull(when = When.NEVER)
	public static AMT disposeOf(@Nullable AMT model)
	{
		if(model!=null)
			model.disposeOf();
		return null;
	}

	/**
	 * If passed a non-null value, disposes of the AMTs' GLCallLists to free up memory. <br>
	 * Call upon destruction
	 */
	@Nonnull(when = When.NEVER)
	public static AMT[] disposeOf(@Nullable AMT[] array)
	{
		if(array!=null)
			Arrays.stream(array).filter(Objects::nonNull).forEach(AMT::disposeOf);
		return array;
	}

	public static void disposeOf(@Nullable AMTModelCache<?> model)
	{
		if(model!=null)
			model.clear();
	}

	public static IIMachineUpgradeModel disposeOf(@Nullable IIMachineUpgradeModel model)
	{
		if(model!=null)
			return model.disposeOf();
		return null;
	}

	public static AMT[] organise(AMT[] array)
	{
		return Arrays.stream(array).filter(amt -> !amt.isChild()).toArray(AMT[]::new);
	}

	public static AMT[] getChildrenRecursive(AMT[] array)
	{
		return Arrays.stream(array)
				.map(AMT::getChildrenRecursive)
				.collect(new ArraylistJoinCollector<>()).toArray(new AMT[0]);
	}

	@Nullable
	public static AMT getPart(AMT[] array, String name)
	{
		return Arrays.stream(getChildrenRecursive(array))
				.filter(amt -> amt.name.equals(name))
				.findFirst().orElse(null);
	}

	/**
	 * Creates a single model out of multiple quads for more performant rendering
	 *
	 * @param model multiple AMTs
	 * @param name  batched element name
	 * @return batched {@link AMTQuads} or {@link AMTLocator} if there's nothing to display
	 */
	public static AMT batchMultipleAMTQuads(AMT[] model, String name)
	{
		//Add all quads to list
		List<BakedQuad> quads = new ArrayList<>();
		for(AMT amt : getChildrenRecursive(model))
			if(amt instanceof AMTQuads)
				quads.addAll(Arrays.asList(((AMTQuads)amt).quads));

		//Output
		return quads.isEmpty()?
				new AMTLocator(name, Vec3d.ZERO):
				new AMTQuads(name, Vec3d.ZERO, quads.toArray(new BakedQuad[0]));
	}

	//--- JSON Parsing ---//

	/**
	 * Turns a json [x,y,z] array into Vec3D
	 */
	public static Vec3d jsonToVec3d(JsonArray array)
	{
		return new Vec3d(
				array.get(0).getAsNumber().doubleValue(),
				array.get(1).getAsNumber().doubleValue(),
				array.get(2).getAsNumber().doubleValue()
		);
	}
}
