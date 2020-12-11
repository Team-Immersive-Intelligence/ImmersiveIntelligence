package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IBulletContainer;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import blusunrize.immersiveengineering.common.items.ItemRevolver;
import blusunrize.immersiveengineering.common.items.ItemSpeedloader;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.EffectCrates.ammoCrateEnergyPerAction;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class TileEntityAmmunitionCrate extends TileEntityEffectCrate
{
	public TileEntityAmmunitionCrate()
	{
		inventory = NonNullList.withSize(38, ItemStack.EMPTY);
		insertionHandler = new IEInventoryHandler(38, this);
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_AMMUNITION_CRATE.ordinal();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot < 20)
		{
			return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
		}
		if(slot < 29)
		{
			return stack.getItem() instanceof ItemBullet&&(stack.isItemEqual(BulletHandler.emptyCasing))||stack.isItemEqual(BulletHandler.emptyShell)&&!stack.hasTagCompound();
		}
		if(slot < 37)
		{
			return stack.getItem() instanceof ItemBullet&&!(stack.equals(BulletHandler.emptyCasing)||stack.equals(BulletHandler.emptyShell));
		}
		if(slot==37)
			return stack.getItem() instanceof ItemRevolver||stack.getItem() instanceof ItemSpeedloader;
		return false;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(heldItem.getItem().getToolClasses(heldItem).contains(CommonProxy.TOOL_WRENCH))
		{
			return addUpgrade(IIContent.UPGRADE_INSERTER, false);
		}
		else if(player.isSneaking())
		{
			open = !open;
			IIPacketHandler.INSTANCE.sendToDimension(new MessageBooleanAnimatedPartsSync(open, 0, this.pos), this.world.provider.getDimension());
			return true;
		}
		else if(open)
		{
			reloadRevolver(player, hand);
			//affectEntityBasic(player);
			return true;
		}
		return false;
	}

	@Override
	boolean isSupplied()
	{
		return true;
	}

	@Override
	void useSupplies()
	{

	}

	@Override
	void affectEntity(Entity entity, boolean upgraded)
	{
		if(!upgraded||(ammoCrateEnergyPerAction <= energyStorage))
		{
			boolean healed = false;
			if(entity instanceof EntityLivingBase)
			{
				if(!((EntityLivingBase)entity).isPotionActive(IIPotions.well_supplied))
				{
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(IIPotions.well_supplied, 80, 0, true, true));
					healed = true;
				}
			}
			if(!upgraded&&healed)
				energyStorage -= ammoCrateEnergyPerAction;
		}
	}

	@Override
	boolean checkEntity(Entity entity)
	{
		return entity instanceof EntityLivingBase;
	}

	private void reloadRevolver(EntityPlayer player, EnumHand hand)
	{
		ItemStack heldItem = player.getHeldItem(hand);

		if(!heldItem.isEmpty()&&heldItem.getItem() instanceof IBulletContainer)
		{
			if(heldItem.getItem() instanceof ItemRevolver)
				ItemNBTHelper.setInt(heldItem, "reload", Math.round(30*Tools.ammunition_crate_resupply_time));

			IItemHandler bullethandler = heldItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

			if(bullethandler==null)
				return;

			for(int i = 0; i < 8; i++)
			{
				ItemStack s = bullethandler.extractItem(i, 1, false);
				if(s.isItemEqual(BulletHandler.emptyCasing)||s.isItemEqual(BulletHandler.emptyShell))
				{
					for(int j = 20; j < 29; j++)
					{
						s = insertionHandler.insertItem(j, s, false);
						if(s.isEmpty())
							break;
					}
				}
				else
				{
					for(int j = 0; j < 20; j++)
					{
						s = insertionHandler.insertItem(j, s, false);
						if(s.isEmpty())
							break;
					}
				}
			}

			for(int j = 0; j < 8; j++)
			{
				ItemStack required = inventory.get(29+j);
				if(!required.isEmpty())
				{
					ItemStack s = ItemStack.EMPTY;
					for(int k = 0; k < 20; k++)
					{
						if(!insertionHandler.getStackInSlot(k).isEmpty()&&insertionHandler.getStackInSlot(k).getTagCompound().equals(required.getTagCompound()))
						{
							s = insertionHandler.extractItem(k, 1, false);
							break;
						}
					}

					bullethandler.insertItem(j, s, false);
				}
			}
		}

	}


	@Override
	public void update()
	{
		// TODO: 06.09.2020
		super.update();
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5);
	}
}
