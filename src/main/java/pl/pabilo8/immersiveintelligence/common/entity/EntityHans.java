package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.util.ChatUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansEmotions;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansEmotions.EyeEmotions;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansEmotions.MouthEmotions;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansEmotions.MouthShapes;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.*;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon.AIHansChemthrower;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon.AIHansRailgun;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon.AIHansSubmachinegun;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerHelmet;

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
	public boolean isKneeling = false;

	public EyeEmotions eyeEmotion = HansEmotions.EyeEmotions.NEUTRAL;
	public MouthEmotions mouthEmotion = HansEmotions.MouthEmotions.NEUTRAL;
	public MouthShapes mouthShape = HansEmotions.MouthShapes.CLOSED;
	public ArrayList<Tuple<Integer, MouthShapes>> mouthShapeQueue = new ArrayList<>();
	public int speechProgress = 0;

	public final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

	private final net.minecraftforge.items.IItemHandlerModifiable handHandler = new net.minecraftforge.items.wrapper.EntityHandsInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable armorHandler = new net.minecraftforge.items.wrapper.EntityArmorInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable invHandler = new ItemStackHandler(this.mainInventory);
	private final net.minecraftforge.items.IItemHandler joinedHandler = new net.minecraftforge.items.wrapper.CombinedInvWrapper(armorHandler, handHandler, invHandler);

	public EntityHans(World worldIn)
	{
		super(worldIn);
		this.enablePersistence();
		setSneaking(false);
	}

	public void equipItems(int id)
	{

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote)
		{
			if(mouthShapeQueue.size() > 0)
			{
				if(speechProgress++ >= mouthShapeQueue.get(0).getFirst())
				{
					this.mouthShape = mouthShapeQueue.get(0).getSecond();
					this.mouthShapeQueue.remove(0);
					this.speechProgress = 0;
				}
			}
			else
			{
				this.mouthShape = MouthShapes.CLOSED;
				this.speechProgress = 0;
			}

			if(this.getAttackTarget()!=null)
			{
				eyeEmotion = EyeEmotions.FROWNING;
				mouthEmotion = MouthEmotions.ANGRY;
			}
			else
			{
				mouthEmotion = MouthEmotions.ANGRY;

				float hp = getHealth()/getMaxHealth();
				if(hp > 0.75)
					eyeEmotion = EyeEmotions.HAPPY;
				else if(hp > 0.5)
					eyeEmotion = EyeEmotions.NEUTRAL;
				else
					eyeEmotion = EyeEmotions.FROWNING;
			}
		}
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();

		//howi AI
		tasks.addTask(0, new AIHansHowitzer(this));
		tasks.addTask(0, new AIHansMortar(this));
		tasks.addTask(0, new AIHansMachinegun(this));

		//Attack mobs
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 1, false, false,
				input -> input instanceof IMob&&input.isEntityAlive()
		));
		//Attack entities with different team, stay neutral on default
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 1, false, false,
				input -> input!=null&&isValidTarget(input)
		));
		//Call other hanses for help when attacked
		this.targetTasks.addTask(2, new AIHansAlertOthers(this, true));

		this.tasks.addTask(2, new AIHansHolsterWeapon(this));
		this.tasks.addTask(3, new AIHansSubmachinegun(this, 1f, 6, 20));
		this.tasks.addTask(3, new AIHansRailgun(this, 0.95f, 50, 30));
		this.tasks.addTask(3, new AIHansChemthrower(this, 0.95f, 50, 8));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.125f, true));

		this.tasks.addTask(5, new EntityAIAvoidEntity<>(this, EntityGasCloud.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityLiving.class, 6.0F));
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
		isKneeling = compound.getBoolean("isKneeling");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setTag("npc_inventory", Utils.writeInventory(mainInventory));
		compound.setBoolean("crewman", crewman);
		compound.setBoolean("isKneeling", isKneeling);
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
		if(player.isSpectator()||hand==EnumHand.OFF_HAND)
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
		else
		{
			if(mouthShapeQueue.size() > 0)
				return EnumActionResult.PASS;


			//Uses Rhubarb, the voice to lip shapes thingy, if you want to see how it works, check out their github
			//Disabled for now, might see service Soon™

			/*
			//German Test
			//Text: Guten Tag, mein Name ist Hans.
			this.speechProgress=0;
			HansEmotions.putMouthShape(this, 'X', 0.00, 0.35);
			HansEmotions.putMouthShape(this, 'F', 0.35, 0.63);
			HansEmotions.putMouthShape(this, 'B', 0.63, 0.70);
			HansEmotions.putMouthShape(this, 'C', 0.70, 0.84);
			HansEmotions.putMouthShape(this, 'B', 0.84, 0.98);
			HansEmotions.putMouthShape(this, 'X', 0.98, 1.27);
			HansEmotions.putMouthShape(this, 'B', 1.27, 1.34);
			HansEmotions.putMouthShape(this, 'A', 1.34, 1.40);
			HansEmotions.putMouthShape(this, 'C', 1.40, 1.45);
			HansEmotions.putMouthShape(this, 'B', 1.45, 0.63);
			HansEmotions.putMouthShape(this, 'E', 1.56, 0.63);
			HansEmotions.putMouthShape(this, 'A', 1.63, 1.72);
			HansEmotions.putMouthShape(this, 'E', 1.72, 1.84);
			HansEmotions.putMouthShape(this, 'D', 1.84, 2.12);
			HansEmotions.putMouthShape(this, 'C', 2.12, 2.19);
			HansEmotions.putMouthShape(this, 'B', 2.19, 2.40);
			HansEmotions.putMouthShape(this, 'X', 2.40, 2.97);
			HansEmotions.putMouthShape(this, 'A', 2.97, 3.10);
			world.playSound(ClientUtils.mc().player, posX, posY, posZ, IISounds.hans_test_de, SoundCategory.NEUTRAL, 0.5f, 1f);
			 */

			/*
			//Polish Test
			//Text: Dzien dobry! Nazywam sie Grzegorz Brzeczyszczykiewicz i mieszkam w Chrzeszczyrzewoszycach w powiecie Lekolodzkim
			HansEmotions.putMouthShape(this, 'X',0.00, 0.44);
			HansEmotions.putMouthShape(this, 'B',0.44, 0.61);
			HansEmotions.putMouthShape(this, 'A',0.61, 0.69);
			HansEmotions.putMouthShape(this, 'E',0.69, 0.97);
			HansEmotions.putMouthShape(this, 'X',0.97, 1.42);
			HansEmotions.putMouthShape(this, 'B',1.42, 1.48);
			HansEmotions.putMouthShape(this, 'C',1.48, 1.54);
			HansEmotions.putMouthShape(this, 'F',1.54, 1.68);
			HansEmotions.putMouthShape(this, 'E',1.68, 1.89);
			HansEmotions.putMouthShape(this, 'F',1.89, 2.24);
			HansEmotions.putMouthShape(this, 'G',2.24, 2.31);
			HansEmotions.putMouthShape(this, 'B',2.31, 2.87);
			HansEmotions.putMouthShape(this, 'A',2.87, 2.95);
			HansEmotions.putMouthShape(this, 'B',2.95, 3.43);
			HansEmotions.putMouthShape(this, 'C',3.43, 3.76);
			HansEmotions.putMouthShape(this, 'G',3.76, 3.97);
			HansEmotions.putMouthShape(this, 'C',3.97, 4.04);
			HansEmotions.putMouthShape(this, 'F',4.04, 4.25);
			HansEmotions.putMouthShape(this, 'B',4.25, 4.32);
			HansEmotions.putMouthShape(this, 'G',4.32, 4.39);
			HansEmotions.putMouthShape(this, 'F',4.39, 4.46);
			HansEmotions.putMouthShape(this, 'B',4.46, 4.67);
			HansEmotions.putMouthShape(this, 'C',4.67, 4.74);
			HansEmotions.putMouthShape(this, 'B',4.74, 4.81);
			HansEmotions.putMouthShape(this, 'A',4.81, 4.95);
			HansEmotions.putMouthShape(this, 'B',4.95, 5.23);
			HansEmotions.putMouthShape(this, 'F',5.23, 5.30);
			HansEmotions.putMouthShape(this, 'B',5.30, 5.51);
			HansEmotions.putMouthShape(this, 'F',5.51, 5.72);
			HansEmotions.putMouthShape(this, 'B',5.72, 5.86);
			HansEmotions.putMouthShape(this, 'X',5.86, 6.93);
			world.playSound(ClientUtils.mc().player, posX, posY, posZ, IISounds.hans_test_pl, SoundCategory.NEUTRAL, 1f, 1f);
			 */
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
		return entity instanceof IMob||((entity instanceof EntityPlayer||entity instanceof EntityHans||entity instanceof EntityEmplacementWeapon||entity instanceof EntityIronGolem)&&entity.getTeam()!=this.getTeam());
	}

	public void sendPlayerMessage(EntityPlayer player, String text)
	{
		ItemStack helmet = getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if(helmet.getItem() instanceof ItemIILightEngineerHelmet&&IIContent.itemLightEngineerHelmet.getUpgrades(helmet).hasKey("gasmask"))
			ChatUtils.sendServerNoSpamMessages(player, new TextComponentTranslation("chat.type.text", this.getDisplayName(), net.minecraftforge.common.ForgeHooks.newChatWithLinks("*Hans Gasmask Noises*")));
		else
			ChatUtils.sendServerNoSpamMessages(player, new TextComponentTranslation("chat.type.text", this.getDisplayName(), net.minecraftforge.common.ForgeHooks.newChatWithLinks(text)));
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		if(damageSourceIn==DamageSource.ON_FIRE)
		{
			return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
		}
		else
		{
			return damageSourceIn==DamageSource.DROWN?SoundEvents.ENTITY_PLAYER_HURT_DROWN: SoundEvents.ENTITY_PLAYER_HURT;
		}
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	protected SoundEvent getSwimSound()
	{
		return SoundEvents.ENTITY_PLAYER_SWIM;
	}

	protected SoundEvent getSplashSound()
	{
		return SoundEvents.ENTITY_PLAYER_SPLASH;
	}

	protected SoundEvent getFallSound(int heightIn)
	{
		return heightIn > 4?SoundEvents.ENTITY_PLAYER_BIG_FALL: SoundEvents.ENTITY_PLAYER_SMALL_FALL;
	}

	@Override
	public HoverEvent getHoverEvent()
	{
		return super.getHoverEvent();
	}

	@Override
	public boolean isSneaking()
	{
		return super.isSneaking();
	}
}
