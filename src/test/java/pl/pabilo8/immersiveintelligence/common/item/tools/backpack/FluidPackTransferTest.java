package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Checks fluid conservation and replacement of both held containers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.11.2026
 */
class FluidPackTransferTest extends GameTestBasic
{
	@Test
	void outputAndInputPreserveFluidAcrossBothHands()
	{
		ItemIIFluidPack item = mock(ItemIIFluidPack.class, CALLS_REAL_METHODS);
		ItemStack pack = new ItemStack(new FluidContainerItem());
		FluidUtil.getFluidHandler(pack).fill(new FluidStack(FluidRegistry.WATER, 3000), true);
		Map<EnumHand, ItemStack> held = new EnumMap<>(EnumHand.class);
		for(EnumHand hand : EnumHand.values())
			held.put(hand, new ItemStack(new FluidContainerItem()));
		EntityLivingBase wearer = mock(EntityLivingBase.class);
		wearer.world = mock(World.class);
		wearer.ticksExisted = 10;
		//noinspection SuspiciousMethodCalls
		when(wearer.getHeldItem(any(EnumHand.class))).thenAnswer(call -> held.get(call.getArgument(0)));
		doAnswer(call -> {
			held.put(call.getArgument(0), call.getArgument(1));
			return null;
		})
				.when(wearer).setHeldItem(any(EnumHand.class), any(ItemStack.class));

		item.setOutputsContents(pack, SideConfig.OUTPUT);
		assertTrue(item.onWornTick(wearer, pack));
		//noinspection DataFlowIssue
		assertEquals(1000, FluidUtil.getFluidContained(pack).amount);
		for(ItemStack stack : held.values())
			//noinspection DataFlowIssue
			assertEquals(1000, FluidUtil.getFluidContained(stack).amount);

		item.setOutputsContents(pack, SideConfig.INPUT);
		assertTrue(item.onWornTick(wearer, pack));
		//noinspection DataFlowIssue
		assertEquals(3000, FluidUtil.getFluidContained(pack).amount);
		for(ItemStack stack : held.values())
			assertNull(FluidUtil.getFluidContained(stack));

		item.setOutputsContents(pack, SideConfig.NONE);
		assertFalse(item.onWornTick(wearer, pack));
	}

	/**
	 * Provides a fluid container without loading mod content.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 15.11.2026
	 */
	private static class FluidContainerItem extends Item
	{
		@Override
		public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
		{
			return new FluidHandlerItemStack(stack, 16000);
		}
	}
}
