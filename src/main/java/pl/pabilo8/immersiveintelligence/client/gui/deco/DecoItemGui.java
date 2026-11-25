package pl.pabilo8.immersiveintelligence.client.gui.deco;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIItemBase;

/**
 * Deco GUI backed by an ItemStack and a {@link ContainerIIItemBase}.
 */
public abstract class DecoItemGui<C extends ContainerIIItemBase> extends DecoGui<ItemStack, C>
{
	@Getter
	protected final ItemStack itemStack;
	@Getter
	protected final EntityEquipmentSlot slot;

	public DecoItemGui(EntityPlayer player, ItemStack heldStack, EntityEquipmentSlot slot, IIGUI iigui)
	{
		super(player, createContainer(player, heldStack, slot, iigui), heldStack==null?ItemStack.EMPTY: heldStack, iigui);
		this.itemStack = heldStack==null?ItemStack.EMPTY: heldStack;
		this.slot = slot;
	}

	@SuppressWarnings("unchecked")
	private static <C extends ContainerIIItemBase> C createContainer(EntityPlayer player, ItemStack heldStack, EntityEquipmentSlot slot, IIGUI iigui)
	{
		return player==null||heldStack==null||slot==null||iigui.containerFromStack==null?null:
				(C)iigui.containerFromStack.apply(player, heldStack, slot);
	}

	@Override
	protected void onNoBackgroundBuilder()
	{
		//Item GUIs often use a hand-drawn texture and set xSize/ySize in onInit().
	}

	@Override
	protected boolean isStoredGuiDataValid(EasyNBT nbt)
	{
		if(!super.isStoredGuiDataValid(nbt)||slot==null)
			return false;
		final boolean[] valid = {false};
		nbt.checkSetString("slot", s -> valid[0] = slot.name().equals(s));
		return valid[0];
	}

	@Override
	protected EasyNBT createGuiDataTag()
	{
		EasyNBT nbt = super.createGuiDataTag();
		if(slot!=null)
			nbt.withString("slot", slot.name());
		return nbt;
	}
}
