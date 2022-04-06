package pl.pabilo8.immersiveintelligence.client.animation;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimation.IIAnimationGroup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 02.04.2022
 */
@SideOnly(Side.CLIENT)
public class IIAnimationUtils
{
	//--- Time Calculation ---//

	public static float getAnimationProgress(float current, float max, boolean invert, float partialTicks)
	{
		return MathHelper.clamp(invert?(1f-((current-partialTicks)/max)): ((current+partialTicks)/max), 0, 1);
	}
	/**
	 * @param progress of the full animation
	 * @param offset   of the current animation
	 * @param duration of the current animation
	 * @return
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

	/**
	 * Called before {@link #renderModel(Tessellator, World, Tuple, BlockPos, List, TRSRTransformation)}
	 */
	public static void startRender(Tessellator tessellator, BlockPos pos)
	{
		BufferBuilder buffer = tessellator.getBuffer();

		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(7425);
		else
			GlStateManager.shadeModel(7424);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		buffer.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
		buffer.color(255, 255, 255, 255);

	}

	/**
	 * Renders an .obj model part<br>
	 * Call {@link #startRender(Tessellator, BlockPos)} before and {@link #finishRender(Tessellator)} after.<br>
	 * Method can be executed multiple times between start and finish.<br>
	 */
	public static void renderModel(Tessellator tessellator, World world, Tuple<IBlockState, IBakedModel> model, BlockPos pos, List<String> parts, TRSRTransformation transform)
	{
		BufferBuilder buffer = tessellator.getBuffer();
		IBlockState state = model.getFirst();
		if(state instanceof IExtendedBlockState)
			state = ((IExtendedBlockState)state).withProperty(Properties.AnimationProperty, new OBJState(parts, false, null));

		getBRD().getBlockModelRenderer().renderModel(world, model.getSecond(), state, pos, buffer, true);
	}

	/**
	 * Called after {@link #renderModel(Tessellator, World, Tuple, BlockPos, List, TRSRTransformation)}
	 */
	public static void finishRender(Tessellator tessellator)
	{
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.setTranslation(0.0D, 0.0D, 0.0D);
		tessellator.draw();
	}

	//--- AMT Based Animations ---//

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

	}

	public static AMT[] getAMT(Tuple<IBlockState, IBakedModel> model)
	{
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

			models.add(
					new AMT(group, Vec3d.ZERO,
							baked.getModel()
									.bake(objState, DefaultVertexFormats.BLOCK, ClientUtils::getSprite)
									.getQuads(bstate, null, 0L).toArray(new BakedQuad[0])
					)
			);
		}


		return models.toArray(new AMT[0]);
	}

	public static AMT[] disposeOf(@Nullable AMT[] array)
	{
		if(array!=null)
			Arrays.stream(array).forEach(AMT::disposeOf);
		return array;
	}

	//--- JSON Parsing ---//
	public static Vec3d jsonToVec3d(JsonArray array)
	{
		return new Vec3d(
				array.get(0).getAsNumber().doubleValue(),
				array.get(1).getAsNumber().doubleValue(),
				array.get(2).getAsNumber().doubleValue()
		);
	}
}
