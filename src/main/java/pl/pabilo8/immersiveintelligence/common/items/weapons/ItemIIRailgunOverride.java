package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.api.shader.ShaderCase;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry.ShaderRegistryEntry;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.entities.EntityRailgunShot;
import blusunrize.immersiveengineering.common.items.ItemRailgun;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIAmmoRailgunGrenade;

/**
 * The man that did the code
 * @author BluSunrize
 * The man that added grenades
 * @author Pabilo8
 * @since 27.11.2020
 */
public class ItemIIRailgunOverride extends ItemRailgun
{
	public ItemIIRailgunOverride()
	{
		super();
		IEContent.registeredIEItems.removeIf(item -> item instanceof ItemRailgun);
		IEContent.registeredIEItems.add(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		int energy = IEConfig.Tools.railgun_consumption;
		float energyMod = 1+this.getUpgrades(stack).getFloat("consumption");
		energy = (int)(energy*energyMod);
		if(this.extractEnergy(stack, energy, true)==energy&&!findAmmo(player).isEmpty())
		{
			player.setActiveHand(hand);
			player.world.playSound(null, player.posX, player.posY, player.posZ, getChargeTime(stack) <= 20?IESounds.chargeFast: IESounds.chargeSlow, SoundCategory.PLAYERS, 1.5f, 1f);
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase user, int timeLeft)
	{
		if(user instanceof EntityPlayer)
		{
			int inUse = this.getMaxItemUseDuration(stack)-timeLeft;
			ItemNBTHelper.remove(stack, "inUse");
			if(inUse < getChargeTime(stack))
				return;
			int energy = IEConfig.Tools.railgun_consumption;
			float energyMod = 1+this.getUpgrades(stack).getFloat("consumption");
			energy = (int)(energy*energyMod);
			if(this.extractEnergy(stack, energy, true)==energy)
			{
				ItemStack ammo = findAmmo((EntityPlayer)user);
				if(!ammo.isEmpty())
				{
					Vec3d vec = user.getLookVec();
					float speed = 20;

					world.playSound(null, user.posX, user.posY, user.posZ, IESounds.railgunFire, SoundCategory.PLAYERS, 1, .5f+(.5f*user.getRNG().nextFloat()));
					this.extractEnergy(stack, energy, false);

					if(!world.isRemote)
					{
						if(ammo.getItem() instanceof ItemIIAmmoRailgunGrenade)
						{
							Vec3d vv = user.getPositionVector().addVector(0, (double)user.getEyeHeight()-0.10000000149011612D, 0);
							EntityBullet a = BulletHelper.createBullet(world, Utils.copyStackWithAmount(ammo, 1), vv, vec, speed/4f);
							a.setShooters(user);
							world.spawnEntity(a);
						}
						else
						{
							EntityRailgunShot shot = new EntityRailgunShot(user.world, user, vec.x*speed, vec.y*speed, vec.z*speed, Utils.copyStackWithAmount(ammo, 1));
							world.spawnEntity(shot);
						}
					}

					ammo.shrink(1);
					if(ammo.getCount() <= 0)
						((EntityPlayer)user).inventory.deleteStack(ammo);

					Triple<ItemStack, ShaderRegistryEntry, ShaderCase> shader = ShaderRegistry.getStoredShaderAndCase(stack);
					if(shader!=null)
					{
						Vec3d pos = Utils.getLivingFrontPos(user, .75, user.height*.75, getActiveSide(user), false, 1);
						shader.getMiddle().getEffectFunction().execute(world, shader.getLeft(), stack, shader.getRight().getShaderType(), pos, user.getLookVec(), .125f);
					}
				}
			}
		}
	}

	public static ItemStack findAmmo(EntityPlayer player)
	{
		if(isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
			return player.getHeldItem(EnumHand.OFF_HAND);
		else if(isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		else
			for(int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				if(isAmmo(itemstack))
					return itemstack;
			}
		return ItemStack.EMPTY;
	}

	public static boolean isAmmo(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		if(stack.getItem() instanceof ItemIIAmmoRailgunGrenade)
			return true;
		return RailgunHandler.getProjectileProperties(stack)!=null;
	}

	private EnumHandSide getActiveSide(EntityLivingBase user)
	{
		EnumHandSide ret = user.getPrimaryHand();
		if(user.getActiveHand()!=EnumHand.MAIN_HAND)
			ret = ret==EnumHandSide.LEFT?EnumHandSide.RIGHT: EnumHandSide.LEFT;
		return ret;

	}
}
