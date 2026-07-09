package pl.pabilo8.immersiveintelligence.client.gui.deco;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoManualWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoOwnershipWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoStyleWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;

/**
 * Deco GUI backed by a TileEntity.
 */
public abstract class DecoTileGui<T extends TileEntityIEBase & IIEInventory, C extends ContainerIITileBase<T>> extends DecoGui<T, C>
{
	protected final T tile;

	public DecoTileGui(EntityPlayer player, T tile, IIGUI iigui)
	{
		super(player, createContainer(player, tile, iigui), tile, iigui);
		this.tile = tile;
	}

	@SuppressWarnings("unchecked")
	private static <T extends TileEntityIEBase & IIEInventory, C extends ContainerIITileBase<T>> C createContainer(EntityPlayer player, T tile, IIGUI iigui)
	{
		return player==null?null: (C)iigui.containerFromTile.apply(player, tile);
	}

	@Override
	protected void onInitStandardAddons()
	{
		if(category==DecoGuiCategory.DATA_TILE||category==DecoGuiCategory.PRODUCTION_TILE||category==DecoGuiCategory.TERRITORY_CONTROL_TILE)
			addWidget(new DecoManualWidget());
		if(tile instanceof IOwnableProperty)
			addWidget(new DecoOwnershipWidget(((IOwnableProperty)tile)));
		if(tile instanceof IStyleCustomizable)
			addWidget(new DecoStyleWidget(((IStyleCustomizable)tile)));
	}

	@Override
	protected boolean isStoredGuiDataValid(EasyNBT nbt)
	{
		return tile!=null&&nbt.hasKey("pos")&&new DimensionBlockPos(tile).equals(nbt.getDimPos("pos"));
	}

	@Override
	protected EasyNBT createGuiDataTag()
	{
		return super.createGuiDataTag()
				.withDimPos("pos", new DimensionBlockPos(tile));
	}

	@Override
	protected void onGuiClosedWithoutTransition()
	{
		EasyNBT nbt = onSaveTileData();
		if(!nbt.isEmpty())
			IIPacketHandler.sendToServer(new MessageIITileSync(tile, nbt));
	}

	@Override
	protected void onBeforeGuiChange(@Nullable EasyNBT tileData)
	{
		EasyNBT nbt = onSaveTileData().conditionally(tileData!=null, e -> e.mergeWith(tileData));
		if(!nbt.isEmpty())
			IIPacketHandler.sendToServer(new MessageIITileSync(tile, nbt));
	}

	@Override
	protected boolean sendGuiChangeMessage(@Nullable IIGUI newGUI)
	{
		if(newGUI==null)
			IIPacketHandler.sendToServer(MessageGuiNBT.closeGuiMessage());
		else if(newGUI!=gui)
			IIPacketHandler.sendToServer(new MessageGuiNBT(newGUI, tile));
		else
			refreshGUIFlag = true;
		return true;
	}

	/**
	 * Called upon closing or changing the GUI, collects data to be passed in a {@link MessageIITileSync} to the tile entity.
	 *
	 * @return The NBT compound containing the data
	 */
	protected EasyNBT onSaveTileData()
	{
		EasyNBT nbt = EasyNBT.newNBT();
		NBTSerialisation.synchroniseFor(this, (tag, gui) -> tag.serializeForEvent(gui, nbt.unwrap(), SyncEvents.TILE_CLIENT_MESSAGE));
		return nbt;
	}

	/**
	 * @return the tile entity associated with this GUI
	 */
	public T getTile()
	{
		return tile;
	}

	public void syncAnimatedParts(MultiblockInteractablePart part, boolean state)
	{
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(part.getID(), state, tile));
	}
}
