package pl.pabilo8.immersiveintelligence.common.block.simple;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack.*;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIBackpackBase;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Provides the placed forms of the five backpack items.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class BlockIIBackpack extends BlockIITileProvider<BackpackType>
{
	public BlockIIBackpack()
	{
		super("backpack", Material.CLOTH, PropertyEnum.create("type", BackpackType.class), null,
				IEProperties.FACING_HORIZONTAL, IEProperties.CONNECTIONS, IOBJModelCallback.PROPERTY
		);
		setHardness(0.5f);
		setSoundType(SoundType.CLOTH);
		setFullCube(false);
		setLightOpacity(0);
	}

	public enum BackpackType implements IITileProviderEnum
	{
		STORAGE(TileEntityStorageBackpack.class, () -> IIContent.itemBackpack),
		CANISTER(TileEntityCanisterBackpack.class, () -> IIContent.itemFluidPack),
		RADIO(TileEntityRadioBackpack.class, () -> IIContent.itemRadioPack),
		CAPACITOR(TileEntityCapacitorBackpack.class, () -> IIContent.itemAdvancedPowerPack),
		PARACHUTE(TileEntityParachuteBackpack.class, () -> IIContent.itemParachutePack);

		private final Class<? extends TileEntity> tile;
		private final Supplier<ItemIIBackpackBase> item;

		BackpackType(Class<? extends TileEntity> tile, Supplier<ItemIIBackpackBase> item)
		{
			this.tile = tile;
			this.item = item;
		}

		@Override
		public Class<? extends TileEntity> getTile()
		{
			return tile;
		}

		public ItemIIBackpackBase getItem()
		{
			return item.get();
		}

		@Nullable
		public static BackpackType forItem(ItemIIBackpackBase item)
		{
			for(BackpackType type : values())
				if(type.getItem()==item)
					return type;
			return null;
		}
	}
}
