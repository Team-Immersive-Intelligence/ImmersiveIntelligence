package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.util.amt.MachineCachedUpgradeModel.MachineCachedUpgradeModelBuilder;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.03.2023
 */
public final class AMTCachedModelBuilder<T>
{
	private final Supplier<T> defaultValue;
	private List<OBJModel> models = new ArrayList<>();
	private List<IIModelHeader> headers = new ArrayList<>();
	private BiFunction<ResourceLocation, T, TextureAtlasSprite> textureProvider = (res, t) -> ClientUtils.getSprite(res);
	private BiFunction<T, IIModelHeader, AMT[]> modelProvider = (t, h) -> new AMT[0];
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
		return withModel(IIAnimationUtils.modelFromRes(res));
	}

	public <K extends TileEntity & IUpgradableMachine> AMTCachedModelBuilder<K> withModel(MachineCachedUpgradeModelBuilder<K> modelBuilder)
	{
		return (AMTCachedModelBuilder<K>)this;
	}

	public AMTCachedModelBuilder<T> withModels(OBJModel... model)
	{
		this.models.addAll(Arrays.asList(model));
		return this;
	}

	public AMTCachedModelBuilder<T> withHeader(IIModelHeader header)
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

	public AMTCachedModelBuilder<T> withModelProvider(BiFunction<T, IIModelHeader, AMT[]> modelProvider)
	{
		this.modelProvider = modelProvider;
		return this;
	}

	public AMTCachedModel<T> build()
	{
		return this.buildResult = new AMTCachedModel<T>(
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

	public AMTCachedModel<T> getBuildResult()
	{
		return buildResult;
	}
}
