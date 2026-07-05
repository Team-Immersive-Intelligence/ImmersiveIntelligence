package pl.pabilo8.immersiveintelligence.client.model.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * Baked item model that keeps a standard flat model for GUI slots, but uses a custom
 * perspective model everywhere else.
 * <p>
 * This is primarily intended for AMT/OBJ item renderers using a {@link net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer}:
 * GUI rendering receives the vanilla baked quads, while hand, ground, fixed and head transforms receive the OBJ/TEISR model.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.07.2026
 */
public class ModelDualPerspective implements IBakedModel
{
	@Nonnull
	private final IBakedModel guiModel;
	@Nonnull
	private final IBakedModel perspectiveModel;

	public ModelDualPerspective(@Nonnull IBakedModel guiModel, @Nonnull IBakedModel perspectiveModel)
	{
		this.guiModel = guiModel;
		this.perspectiveModel = perspectiveModel;
	}

	/**
	 * @return the model used for the GUI/inventory perspective
	 */
	@Nonnull
	public IBakedModel getGuiModel()
	{
		return guiModel;
	}

	/**
	 * @return the model used for world and hand perspectives
	 */
	@Nonnull
	public IBakedModel getPerspectiveModel()
	{
		return perspectiveModel;
	}

	@Nonnull
	private IBakedModel getModelForPerspective(TransformType transform)
	{
		return (transform==TransformType.GUI||transform==TransformType.FIXED)?guiModel: perspectiveModel;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return getModelForPerspective(cameraTransformType).handlePerspective(cameraTransformType);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		return guiModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return guiModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return guiModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return guiModel.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return OverrideHandler.INSTANCE;
	}

	private static final class OverrideHandler extends ItemOverrideList
	{
		private static final OverrideHandler INSTANCE = new OverrideHandler();

		private OverrideHandler()
		{
			super(ImmutableList.of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
		{
			ModelDualPerspective model = (ModelDualPerspective)originalModel;
			IBakedModel guiOverride = model.guiModel.getOverrides().handleItemState(model.guiModel, stack, world, entity);
			IBakedModel perspectiveOverride = model.perspectiveModel.getOverrides().handleItemState(model.perspectiveModel, stack, world, entity);

			if(guiOverride==model.guiModel&&perspectiveOverride==model.perspectiveModel)
				return originalModel;
			return new ModelDualPerspective(guiOverride, perspectiveOverride);
		}
	}
}
