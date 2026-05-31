package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.03.2023
 */
public final class AMTCachedModelBuilder<T>
{
	private final Supplier<T> defaultValue;
	private List<Tuple<Predicate<T>, OBJModel>> conditionalModels = new ArrayList<>();
	private List<OBJModel> models = new ArrayList<>();
	private List<AMTModelHeader> headers = new ArrayList<>();
	private BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider = (res, t) -> ClientUtils.getSprite(res);
	private BiFunction<T, AMTModelHeader, AMT[]> modelProvider = (t, h) -> new AMT[0];
	private Function<T, AMTModelHeader> headerProvider = t -> null;
	private boolean isBlock = false;
	private AMTCachedModel<T> buildResult;

	private AMTCachedModelBuilder(Supplier<T> defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public static AMTCachedModelBuilder<ItemStack> startItemModel()
	{
		return new AMTCachedModelBuilder<>(() -> ItemStack.EMPTY);
	}

	public static <T extends Entity> AMTCachedModelBuilder<T> startEntityModel(Class<T> klass)
	{
		AMTCachedModelBuilder<T> builder = new AMTCachedModelBuilder<>(() -> null);
		builder.isBlock = false;
		return builder;
	}

	public static AMTCachedModelBuilder<IBlockState> startBlockModel()
	{
		AMTCachedModelBuilder<IBlockState> builder = new AMTCachedModelBuilder<>(() -> null);
		builder.isBlock = true;
		return builder;
	}

	public static <T extends TileEntity> AMTCachedModelBuilder<T> startTileEntityModel(Class<T> klass)
	{
		AMTCachedModelBuilder<T> builder = new AMTCachedModelBuilder<>(() -> null);
		builder.isBlock = true;
		return builder;
	}

	public AMTCachedModelBuilder<T> withModel(OBJModel model)
	{
		this.models.add(model);
		return this;
	}

	public AMTCachedModelBuilder<T> withModel(ResourceLocation res)
	{
		return withModel(AMTUtils.modelFromRes(res));
	}

	public AMTCachedModelBuilder<T> withModel(Predicate<T> predicate, OBJModel model)
	{
		this.conditionalModels.add(new Tuple<>(predicate, model));
		return this;
	}

	public AMTCachedModelBuilder<T> withModel(Predicate<T> predicate, ResourceLocation res)
	{
		return withModel(predicate, AMTUtils.modelFromRes(res));
	}

	public AMTCachedModelBuilder<T> withModels(OBJModel... model)
	{
		this.models.addAll(Arrays.asList(model));
		return this;
	}

	public AMTCachedModelBuilder<T> withHeader(AMTModelHeader header)
	{
		this.headers.add(header);
		return this;
	}

	public AMTCachedModelBuilder<T> withHeader(ResourceLocation res)
	{
		return withHeader(AMTLoader.loadHeader(res));
	}

	public AMTCachedModelBuilder<T> withTextureProvider(BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider)
	{
		this.textureProvider = textureProvider;
		return this;
	}

	public AMTCachedModelBuilder<T> withModelProvider(BiFunction<T, AMTModelHeader, AMT[]> modelProvider)
	{
		this.modelProvider = modelProvider;
		return this;
	}

	public AMTCachedModelBuilder<T> withHeaderProvider(Function<T, AMTModelHeader> headerProvider)
	{
		this.headerProvider = headerProvider;
		return this;
	}

	public AMTCachedModel<T> build()
	{
		//noinspection unchecked
		return this.buildResult = new AMTCachedModel<T>(
				models.toArray(new OBJModel[0]),
				conditionalModels.toArray(new Tuple[0]),
				textureProvider,
				headers.toArray(new AMTModelHeader[0]),
				modelProvider,
				headerProvider,
				isBlock)
		{
			@Override
			protected T getDefaultParameter()
			{
				return defaultValue.get();
			}
		};
	}

	public AMTCachedModel<T> getBuildResult()
	{
		return buildResult;
	}
}
