package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

import javax.annotation.Nonnull;

/**
 * Stores and handles construction progress for {@link IAdvancedMultiblock Advanced Multiblocks}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2025
 */
public class MultiblockConstructionManager implements INBTSerializable<NBTTagInt>
{
	@Nonnull
	private final TileEntityMultiblockIIBase<?> tile;
	private final int constructionCost;
	private int construction = 0, clientConstruction = 0;

	public MultiblockConstructionManager(@Nonnull TileEntityMultiblockIIBase<?> tile, int constructionCost)
	{
		this.tile = tile;
		this.constructionCost = constructionCost;
	}

	public int getConstructionCost()
	{
		return constructionCost;
	}

	public int getCurrentConstruction(boolean client)
	{
		return client?clientConstruction: construction;
	}

	public void progressConstruction(int construction)
	{
		this.construction = MathHelper.clamp(this.construction+construction, 0, constructionCost);
		this.tile.updateTileForEvent(SyncEvents.TILE_CONSTRUCTION);
	}

	public boolean update()
	{
		if(clientConstruction < constructionCost)
			clientConstruction = (int)Math.min(clientConstruction+(Tools.electricHammerEnergyPerUseConstruction/4.25f), constructionCost);
		if(isConstructionFinished())
		{
			clientConstruction = construction = constructionCost;
			return true;
		}
		return false;
	}

	public boolean isConstructionFinished()
	{
		return getCurrentConstruction(false) >= getConstructionCost();
	}

	@Override
	public NBTTagInt serializeNBT()
	{
		return new NBTTagInt(construction);
	}

	@Override
	public void deserializeNBT(NBTTagInt nbt)
	{
		construction = nbt.getInt();
	}
}
