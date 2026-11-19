package pl.pabilo8.immersiveintelligence.common.item.tools.backpack;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Deploys a parachute for a falling player.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.11.2026
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "parachute_backpack", texturePath = "backpack/parachute_backpack")
public class ItemIIParachutePack extends ItemIIBackpackBase
{
	public static final String NBT_KEY_USED = "used";

	public ItemIIParachutePack()
	{
		super("parachute_backpack", "parachute_backpack");
	}

	//--- Setters ---//

	public boolean isUsed(ItemStack stack)
	{
		return ItemNBTHelper.getBoolean(stack, NBT_KEY_USED);
	}

	public void setUsed(ItemStack stack, boolean used)
	{
		ItemNBTHelper.setBoolean(stack, NBT_KEY_USED, used);
	}

	//--- Update ---//

	@Override
	public boolean onBackpackAction(EntityPlayer player, WornBackpack worn)
	{
		ItemStack stack = worn.getStack();
		if(isUsed(stack))
			return false;
		deployParachute(player, stack);
		worn.save(stack);
		return isUsed(stack);
	}

	@Override
	public boolean handleWearingPlayerInteraction(ItemStack stack, EntityPlayer wearer, EntityPlayer visitor, EnumHand hand)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String[] getBackpackOverlayText(ItemStack stack)
	{
		return new String[]{stack.getDisplayName(), I18n.format(IIReference.DESCRIPTION_KEY+"parachute_backpack."+(isUsed(stack)?"used": "ready"))};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"parachute_backpack."+(isUsed(stack)?"used": "ready")));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return isUsed(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return isUsed(stack)?0d: 1d;
	}

	/**
	 * Deploys a parachute entity and mounts the wearer on it
	 *
	 * @param player wearer
	 * @param stack  parachute itemstack
	 */
	public void deployParachute(EntityPlayer player, ItemStack stack)
	{
		if(isUsed(stack)||player.world.isRemote||!player.isEntityAlive()||player.isSpectator()||player.onGround||player.isRiding()
				||player.capabilities.isFlying||player.isElytraFlying()||player.isInWater()||player.isInLava()||player.motionY >= 0)
			return;

		EntityParachute parachute = new EntityParachute(player.world);
		//Keep the rider at the same height when the canopy is deployed.
		parachute.setPositionAndRotation(player.posX, player.posY-parachute.getMountedYOffset()-player.getYOffset(), player.posZ,
				player.rotationYaw, 0);
		parachute.motionX = player.motionX;
		parachute.motionZ = player.motionZ;
		if(player.world.spawnEntity(parachute))
		{
			if(player.startRiding(parachute))
			{
				player.fallDistance = 0;
				setUsed(stack, true);
			}
			else
				parachute.setDead();
		}
	}
}
