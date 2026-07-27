package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import javax.annotation.Nonnull;

/**
 * Lightweight container for GUIs whose only authoritative context is the player.
 * It deliberately exposes no slots; the GUI is not an alternative inventory view.
 *
 * @author Pabilo8
 * @since 22.07.2026
 */
public class ContainerPlayerGui extends Container
{
	private final EntityPlayer player;

	public ContainerPlayerGui(@Nonnull EntityPlayer player)
	{
		this.player = player;
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return playerIn==player&&!player.isDead;
	}
}
