package pl.pabilo8.immersiveintelligence.client.gui.deco;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIItemBase;

/**
 * Deco GUI backed by an ItemStack and a {@link ContainerIIItemBase}.
 */
public abstract class DecoItemGui<C extends ContainerIIItemBase> extends DecoGui<ItemStack, C>
{
	protected final ItemStack itemStack;
	protected final EnumHand hand;

	public DecoItemGui(EntityPlayer player, ItemStack heldStack, EnumHand hand, IIGUI iigui)
	{
		super(player, createContainer(player, heldStack, hand, iigui), heldStack==null?ItemStack.EMPTY: heldStack, iigui);
		this.itemStack = heldStack==null?ItemStack.EMPTY: heldStack;
		this.hand = hand;
	}

	@SuppressWarnings("unchecked")
	private static <C extends ContainerIIItemBase> C createContainer(EntityPlayer player, ItemStack heldStack, EnumHand hand, IIGUI iigui)
	{
		return player==null||heldStack==null||hand==null||iigui.containerFromStack==null?null:
				(C)iigui.containerFromStack.apply(player, heldStack, hand);
	}

	@Override
	protected void onNoBackgroundBuilder()
	{
		// Item GUIs often use a hand-drawn texture and set xSize/ySize in onInit().
	}

	@Override
	protected boolean isStoredGuiDataValid(EasyNBT nbt)
	{
		if(!super.isStoredGuiDataValid(nbt)||hand==null)
			return false;
		final boolean[] valid = {false};
		nbt.checkSetString("hand", s -> valid[0] = hand.name().equals(s));
		return valid[0];
	}

	@Override
	protected EasyNBT createGuiDataTag()
	{
		EasyNBT nbt = super.createGuiDataTag();
		if(hand!=null)
			nbt.withString("hand", hand.name());
		return nbt;
	}

	public ItemStack getItemStack()
	{
		return itemStack;
	}

	public EnumHand getHand()
	{
		return hand;
	}
}
