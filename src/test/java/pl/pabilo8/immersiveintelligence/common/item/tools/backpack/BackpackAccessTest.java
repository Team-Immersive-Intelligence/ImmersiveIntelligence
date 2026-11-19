package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

/**
 * Checks access defaults and owner access across privacy modes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.11.2026
 */
class BackpackAccessTest extends GameTestBasic
{
	@Test
	void wearerRetainsAccessInEveryMode()
	{
		ItemIIBackpackBase item = mock(ItemIIBackpackBase.class, CALLS_REAL_METHODS);
		ItemStack stack = new ItemStack(Items.PAPER);
		EntityPlayer wearer = mock(EntityPlayer.class);
		for(BackpackAccess access : BackpackAccess.values())
		{
			item.setBackpackAccess(stack, access);
			assertTrue(access.canAccess(wearer, wearer));
		}
	}

	@Test
	void publicAccessDoesNotRequireFactionData()
	{
		ItemIIBackpackBase item = mock(ItemIIBackpackBase.class, CALLS_REAL_METHODS);
		ItemStack stack = new ItemStack(Items.PAPER);
		item.setBackpackAccess(stack, BackpackAccess.PUBLIC);
		BackpackAccess access = item.getBackpackAccess(stack);
		assertTrue(access.canAccess(mock(EntityPlayer.class), mock(EntityPlayer.class)));
	}
}
