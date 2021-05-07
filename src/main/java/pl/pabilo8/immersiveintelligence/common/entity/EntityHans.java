package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.util.ChatUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.hans_tasks.*;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 11.01.2021
 */
public class EntityHans extends EntityCreature implements INpc
{
	public boolean crewman = false;
	public boolean hasAmmo = true;

	public final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

	private final net.minecraftforge.items.IItemHandlerModifiable handHandler = new net.minecraftforge.items.wrapper.EntityHandsInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable armorHandler = new net.minecraftforge.items.wrapper.EntityArmorInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable invHandler = new ItemStackHandler(this.mainInventory);
	private final net.minecraftforge.items.IItemHandler joinedHandler = new net.minecraftforge.items.wrapper.CombinedInvWrapper(armorHandler, handHandler, invHandler);

	public EntityHans(World worldIn)
	{
		super(worldIn);
		this.enablePersistence();
	}

	public void equipItems(int id)
	{
		ItemStack helmet = new ItemStack(IIContent.itemLightEngineerHelmet);
		if(id < 2)
		{
			ItemStack stack = new ItemStack(IIContent.itemSubmachinegun);
			NonNullList<ItemStack> upgrades = NonNullList.withSize(2, ItemStack.EMPTY);
			upgrades.set(0, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.SUPPRESSOR.ordinal()));
			upgrades.set(1, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.BOTTOM_LOADING.ordinal()));
			IIContent.itemSubmachinegun.setContainedItems(stack, upgrades);
			IIContent.itemSubmachinegun.recalculateUpgrades(stack);
			IIContent.itemSubmachinegun.finishUpgradeRecalculation(stack);
			ItemStack magazine = ItemIIBulletMagazine.getMagazine(id==0?"submachinegun": "submachinegun_drum", IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "piercing"));
			ItemNBTHelper.setItemStack(stack, "magazine", magazine);
			mainInventory.set(0, magazine.copy());
			mainInventory.set(1, magazine.copy());
			mainInventory.set(2, magazine.copy());

			setHeldItem(EnumHand.MAIN_HAND, stack);

			/*
			upgrades = NonNullList.withSize(3, ItemStack.EMPTY);
			upgrades.set(0, new ItemStack(IIContent.itemArmorUpgrade, 1, ArmorUpgrades.GASMASK.ordinal()));
			IIContent.itemLightEngineerHelmet.setContainedItems(helmet, upgrades);
			IIContent.itemLightEngineerHelmet.recalculateUpgrades(helmet);
			IIContent.itemLightEngineerHelmet.finishUpgradeRecalculation(helmet);
			 */
		}
		else if(id==7)
		{
			crewman=true;
		}
		else if(id==8)
		{
			ItemStack stack = new ItemStack(IIContent.itemBinoculars);
			setHeldItem(EnumHand.MAIN_HAND, stack);
			setSneaking(true);
		}

		setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);

	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		//howi AI
		tasks.addTask(0, new AIHansHowitzer(this));
		tasks.addTask(0, new AIHansMachinegun(this));

		//Attack mobs
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 1, false, false,
				input -> input instanceof IMob&&input.getHealth()>0
		));
		//Attack players and Hanses with different team, stay neutral on default
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityHans.class, 1, false, false,
				input -> input!=null&&input.getTeam()!=this.getTeam()&&input.getHealth()>0
		));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 1, false, false,
				input -> input!=null&&input.getTeam()!=this.getTeam()&&input.getHealth()>0
		));
		this.targetTasks.addTask(2, new AIHansAlertOthers(this, true));

		this.tasks.addTask(2, new AIHansHolsterWeapon(this));
		this.tasks.addTask(3, new AIHansSubmachinegun(this, 1f, 6, 20));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1f, true));

		this.tasks.addTask(5, new EntityAIAvoidEntity<>(this, EntityGasCloud.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityHans.class, 3.0F));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1D, 0f));
		this.tasks.addTask(8, new EntityAILookIdle(this));

		//this.tasks.addTask(4, new AIHansEnterVehicle(this));

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIRestrictOpenDoor(this));

		//this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, avEntity-> this.hasAmmunition()&&avEntity!=null&&avEntity.getRevengeTarget()==this, 8.0F, 0.6D, 0.6D));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(facing==null) return (T)joinedHandler;
			else if(facing.getAxis().isVertical()) return (T)handHandler;
			else if(facing.getAxis().isHorizontal()) return (T)armorHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		readInventory(compound.getTagList("npc_inventory", 10));
		crewman = compound.getBoolean("crewman");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setTag("npc_inventory", Utils.writeInventory(mainInventory));
		compound.setBoolean("crewman", crewman);
	}

	private void readInventory(NBTTagList npc_inventory)
	{
		int max = npc_inventory.tagCount();
		for(int i = 0; i < max; i++)
		{
			NBTTagCompound itemTag = npc_inventory.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot")&255;
			if(slot >= 0&&slot < this.mainInventory.size())
				this.mainInventory.set(slot, new ItemStack(itemTag));
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(52f);
	}

	public boolean attackEntityAsMob(Entity entityIn)
	{
		this.world.setEntityState(this, (byte)4);
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7+this.rand.nextInt(15)));

		if(flag)
		{
			entityIn.motionY += 0.4000000059604645D;
			this.applyEnchantments(this, entityIn);
		}

		this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
		return flag;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		return !player.isSpectator()&&hand==EnumHand.MAIN_HAND&&player.getPositionVector().distanceTo(this.getPositionVector()) <= 1d;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
	{
		if(hand==EnumHand.OFF_HAND)
			return EnumActionResult.FAIL;
		if(!world.isRemote)
		{
			faceEntity(player, 360f, 360f);

			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.isEmpty())
			{
				sendPlayerMessage(player,
						String.format("Guten %1$s, %2$s!",
								getGermanTimeName(world.getWorldTime()),
								player.getName()
						));
			}
			else if(IIContent.itemSubmachinegun.isAmmo(heldItem, getActiveItemStack()))
			{
				int i = 0;
				while(i < mainInventory.size()&&!heldItem.isEmpty())
				{
					heldItem = invHandler.insertItem(i, heldItem.copy(), false);
					i++;
				}
				if(heldItem.isEmpty())
					sendPlayerMessage(player, "Danke für diese neue Patronen. Meine Maschinenpistole war sehr hungrig!");
				else
					sendPlayerMessage(player, "Danke für diese neue Patronen, aber habe ich sie genug.");
			}
			player.setHeldItem(EnumHand.MAIN_HAND, heldItem);
			player.swingArm(hand);

		}
		return EnumActionResult.PASS;
	}

	private String getGermanTimeName(long time)
	{
		if(time < 11500)
			return "Morgen";
		else if(time < 18000)
			return "Tag";
		else
			return "Abend";
	}

	public boolean isValidTarget(Entity entity)
	{
		return entity instanceof IMob||((entity instanceof EntityPlayer||entity instanceof EntityHans)&&entity.getTeam()!=this.getTeam());
	}

	public void sendPlayerMessage(EntityPlayer player, String text)
	{
		ArrayList<ITextComponent> arr = new ArrayList<>();
		ChatUtils.sendServerNoSpamMessages(player, new TextComponentTranslation("chat.type.text", this.getDisplayName(), net.minecraftforge.common.ForgeHooks.newChatWithLinks(text)));
	}
}
