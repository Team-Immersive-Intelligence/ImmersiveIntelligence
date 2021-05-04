package pl.pabilo8.immersiveintelligence.common.entity.hans_tasks;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIISubmachinegun;

/**
 * @author Pabilo8
 * @since 02.05.2021
 */
public class AIHansHolsterWeapon extends EntityAIBase
{
	private final EntityHans hans;
	private int timer = 0;

	public AIHansHolsterWeapon(EntityHans hans)
	{
		this.hans = hans;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute()
	{
		timer = Math.max(--timer, 0);
		return timer==0&&((hans.getAttackTarget()==null)^(isWeapon(hans.getHeldItem(EnumHand.OFF_HAND))&&!isWeapon(hans.getHeldItem(EnumHand.MAIN_HAND))));
	}

	@Override
	public void updateTask()
	{
		ItemStack offhand = hans.getHeldItem(EnumHand.OFF_HAND);
		ItemStack mainhand = hans.getHeldItem(EnumHand.MAIN_HAND);
		hans.setHeldItem(EnumHand.MAIN_HAND, offhand);
		hans.setHeldItem(EnumHand.OFF_HAND, mainhand);

		if(hans.getAttackTarget()!=null)
			this.timer = 20;
	}

	public boolean isWeapon(ItemStack stack)
	{
		return stack.getItem() instanceof ItemIISubmachinegun;
	}
}
