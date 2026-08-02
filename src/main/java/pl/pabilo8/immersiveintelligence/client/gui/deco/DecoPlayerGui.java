package pl.pabilo8.immersiveintelligence.client.gui.deco;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import pl.pabilo8.immersiveintelligence.common.IIGUI;

/**
 * Deco GUI whose sole server-side context is the player opening it.
 *
 * @param <C> player inventory container type
 * @author Pabilo8
 * @since 22.07.2026
 */
public abstract class DecoPlayerGui<C extends Container> extends DecoGui<EntityPlayer, C>
{
	protected final EntityPlayer player;

	protected DecoPlayerGui(EntityPlayer player, IIGUI iigui)
	{
		super(player, createContainer(player, iigui), player, iigui);
		this.player = player;
	}

	@SuppressWarnings("unchecked")
	private static <C extends Container> C createContainer(EntityPlayer player, IIGUI iigui)
	{
		return player==null||iigui.containerFromPlayer==null?null: (C)iigui.containerFromPlayer.apply(player);
	}
}
