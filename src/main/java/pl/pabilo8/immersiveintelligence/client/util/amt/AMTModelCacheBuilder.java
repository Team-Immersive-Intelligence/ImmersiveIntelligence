package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 07.03.2023
 */
public final class AMTModelCacheBuilder<T>
{
	private List<OBJModel> models = new ArrayList<>();
	private List<IIModelHeader> headers = new ArrayList<>();
	private BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider = (res, t) -> ClientUtils.getSprite(res);
	private BiFunction<T, IIModelHeader, AMT[]> modelProvider = (t, h) -> new AMT[0];
	private final Supplier<T> defaultValue;
	private boolean isBlock = false;

	private AMTModelCacheBuilder(Supplier<T> defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public static AMTModelCacheBuilder<ItemStack> startItemModel()
	{
		return new AMTModelCacheBuilder<>(() -> ItemStack.EMPTY);
	}

	public static AMTModelCacheBuilder<IBlockState> startBlockModel()
	{
		AMTModelCacheBuilder<IBlockState> builder = new AMTModelCacheBuilder<>(() -> null);
		builder.isBlock = true;
		return builder;
	}

	public static AMTModelCacheBuilder<TileEntity> startTileEntityModel()
	{
		AMTModelCacheBuilder<TileEntity> builder = new AMTModelCacheBuilder<>(() -> null);
		builder.isBlock = true;
		return builder;
	}

	public AMTModelCacheBuilder<T> withModel(OBJModel model)
	{
		this.models.add(model);
		return this;
	}

	public AMTModelCacheBuilder<T> withModel(ResourceLocation res)
	{
		return withModel(IIAnimationUtils.modelFromRes(res));
	}

	public AMTModelCacheBuilder<T> withModels(OBJModel... model)
	{
		this.models.addAll(Arrays.asList(model));
		return this;
	}

	public AMTModelCacheBuilder<T> withHeader(IIModelHeader header)
	{
		this.headers.add(header);
		return this;
	}

	public AMTModelCacheBuilder<T> withHeader(ResourceLocation res)
	{
		return withHeader(IIAnimationLoader.loadHeader(res));
	}

	public AMTModelCacheBuilder<T> withTextureProvider(BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider)
	{
		this.textureProvider = textureProvider;
		return this;
	}

	public AMTModelCacheBuilder<T> withModelProvider(BiFunction<T, IIModelHeader, AMT[]> modelProvider)
	{
		this.modelProvider = modelProvider;
		return this;
	}

	public AMTModelCache<T> build()
	{
		return new AMTModelCache<T>(
				models.toArray(new OBJModel[0]),
				textureProvider,
				headers.toArray(new IIModelHeader[0]),
				modelProvider,
				isBlock
		)
		{
			@Override
			protected T getDefaultParameter()
			{
				return defaultValue.get();
			}
		};
	}
}
