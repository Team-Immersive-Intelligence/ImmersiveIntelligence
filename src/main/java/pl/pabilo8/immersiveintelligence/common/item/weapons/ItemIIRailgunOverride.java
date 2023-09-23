package pl.pabilo8.immersiveintelligence.common.item.weapons;

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
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Triple;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Railgun;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoRailgunGrenade;

/**
 * @author Pabilo8
 * The man that added grenades
 * @author BluSunrize
 * The man that did the rest of the code
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

		if(Railgun.disableRailgunOffhand&&hand==EnumHand.OFF_HAND)
			return new ActionResult<>(EnumActionResult.PASS, stack);
		
		int energy = IEConfig.Tools.railgun_consumption;
		float energyMod = 1+this.getUpgrades(stack).getFloat("consumption");
		energy = (int)(energy*energyMod);
		if(this.extractEnergy(stack, energy, true)==energy&&!findAmmo(((EntityLivingBase)player)).isEmpty())
		{
			player.setActiveHand(hand);
			player.world.playSound(null, player.posX, player.posY, player.posZ, getChargeTime(stack) <= 20?IESounds.chargeFast: IESounds.chargeSlow, SoundCategory.PLAYERS, 1.5f, 1f);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase user, int timeLeft)
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
				ItemStack ammo = findAmmo(user);
				if(!ammo.isEmpty())
				{
					Vec3d vec = user.getLookVec();
					float speed = 20;
					float mass = 0.25f;

					world.playSound(null, user.posX, user.posY, user.posZ, IESounds.railgunFire, SoundCategory.PLAYERS, 1, .5f+(.5f*user.getRNG().nextFloat()));
					this.extractEnergy(stack, energy, false);

					if(!world.isRemote)
					{
						if(ammo.getItem() instanceof ItemIIAmmoRailgunGrenade)
						{
							Vec3d vv = user.getPositionVector().addVector(0, (double)user.getEyeHeight()-0.10000000149011612D, 0);
							EntityBullet a = AmmoUtils.createBullet(world, Utils.copyStackWithAmount(ammo, 1), vv, vec);
							a.setShooters(user);
							mass = a.mass;
							world.spawnEntity(a);
						}
						else
						{
							EntityRailgunShot shot = new EntityRailgunShot(user.world, user, vec.x*speed, vec.y*speed, vec.z*speed, Utils.copyStackWithAmount(ammo, 1));
							world.spawnEntity(shot);
						}
					}

					ammo.shrink(1);

					if(Railgun.railgunRecoil)
						user.move(MoverType.PISTON, -vec.x*mass*0.25f, 0, -vec.z*mass*0.25f);

					Triple<ItemStack, ShaderRegistryEntry, ShaderCase> shader = ShaderRegistry.getStoredShaderAndCase(stack);
					if(shader!=null)
					{
						Vec3d pos = Utils.getLivingFrontPos(user, .75, user.height*.75, getActiveSide(user), false, 1);
						shader.getMiddle().getEffectFunction().execute(world, shader.getLeft(), stack, shader.getRight().getShaderType(), pos, user.getLookVec(), .125f);
					}
				}
			}
	}

	public static ItemStack findAmmo(EntityLivingBase entity)
	{
		if(isAmmo(entity.getHeldItem(EnumHand.OFF_HAND)))
			return entity.getHeldItem(EnumHand.OFF_HAND);
		else if(isAmmo(entity.getHeldItem(EnumHand.MAIN_HAND)))
			return entity.getHeldItem(EnumHand.MAIN_HAND);
		else if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			final IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability==null)
				return ItemStack.EMPTY;

			for(int i = 0; i < capability.getSlots(); i++)
			{
				ItemStack itemstack = capability.getStackInSlot(i);
				if(isAmmo(itemstack))
					return itemstack;
			}
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
