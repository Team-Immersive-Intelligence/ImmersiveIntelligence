package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ChatUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.*;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansPathNavigate;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansUtils;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.*;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon.AIHansHandWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle.AIHansKazachok;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle.AIHansSalute;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle.AIHansTimedLookAtEntity;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 11.01.2021
 */
public class EntityHans extends EntityCreature implements INpc
{
	public static boolean INFINITE_AMMO = false;
	private static final IIColor[] EYE_COLORS = new IIColor[]{
			IIColor.fromPackedRGB(0x597179),//cyan
			IIColor.fromPackedRGB(0x536579),//toned blue
			IIColor.fromPackedRGB(0x486479),//blue 2
			IIColor.fromPackedRGB(0x3F795B),//green
			IIColor.fromPackedRGB(0x3B7959),//green/blue
			IIColor.fromPackedRGB(0x54795B),//toned green
			IIColor.fromPackedRGB(0x414832),//khaki
			IIColor.fromPackedRGB(0x3D4827),//olive
			IIColor.fromPackedRGB(0x9D7956),//amber
			IIColor.fromPackedRGB(0x434139),//brown
			IIColor.fromPackedRGB(0x484739),//light brown
			IIColor.fromPackedRGB(0x2F2E28),//dark brown
	};
	private static final DataParameter<String> DATA_MARKER_LEG_ANIMATION = EntityDataManager.createKey(EntityHans.class, DataSerializers.STRING);
	private static final DataParameter<String> DATA_MARKER_ARM_ANIMATION = EntityDataManager.createKey(EntityHans.class, DataSerializers.STRING);
	private static final DataParameter<Integer> DATA_MARKER_EYE_COLOR = EntityDataManager.createKey(EntityHans.class, DataSerializers.VARINT);
	private static final DataParameter<NBTTagCompound> DATA_MARKER_SPEECH = EntityDataManager.createKey(EntityHans.class, DataSerializers.COMPOUND_TAG);

	public HansLegAnimation prevLegAnimation = HansLegAnimation.STANDING;
	public HansLegAnimation legAnimation = HansLegAnimation.STANDING;
	public int legAnimationTimer = 0;

	public HansArmAnimation prevArmAnimation = HansArmAnimation.NORMAL;
	public HansArmAnimation armAnimation = HansArmAnimation.NORMAL;
	public int armAnimationTimer = 8;

	public EyeEmotions eyeEmotion = HansAnimations.EyeEmotions.NEUTRAL;
	public MouthEmotions mouthEmotion = HansAnimations.MouthEmotions.NEUTRAL;
	public MouthShapes mouthShape = HansAnimations.MouthShapes.CLOSED;
	public ArrayList<Tuple<Integer, MouthShapes>> mouthShapeQueue = new ArrayList<>();
	public int speechProgress = 0;
	public IIColor eyeColor;

	/**
	 * A dangerously close distance, most {@link AIHansHandWeapon} tasks will resort to {@link EntityAIAttackMelee}
	 */
	public static final float MELEE_DISTANCE = 1.25f;

	private EntityAIBase vehicleTask = null;
	private AIHansHandWeapon weaponTask = null;
	private AIHansOpenDoor doorTask = null;

	/**
	 * Whether this Hans is a head of a Squad
	 */
	public boolean commander = false;

	public boolean enemyContact = false;
	public boolean hasAmmo = true;

	public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(27, ItemStack.EMPTY);

	private final net.minecraftforge.items.IItemHandlerModifiable handHandler = new net.minecraftforge.items.wrapper.EntityHandsInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable armorHandler = new net.minecraftforge.items.wrapper.EntityArmorInvWrapper(this);
	private final net.minecraftforge.items.IItemHandlerModifiable invHandler = new ItemStackHandler(this.mainInventory);
	private final net.minecraftforge.items.IItemHandler joinedHandler = new net.minecraftforge.items.wrapper.CombinedInvWrapper(armorHandler, handHandler, invHandler);

	public EntityHans(World worldIn)
	{
		super(worldIn);
		this.enablePersistence();
		setSneaking(false);
		this.dataManager.register(DATA_MARKER_LEG_ANIMATION, legAnimation.name().toLowerCase());
		this.dataManager.register(DATA_MARKER_ARM_ANIMATION, armAnimation.name().toLowerCase());
		this.dataManager.register(DATA_MARKER_EYE_COLOR, (eyeColor = EYE_COLORS[rand.nextInt(EYE_COLORS.length)]).getPackedRGB());
		this.dataManager.register(DATA_MARKER_SPEECH, new NBTTagCompound());
		setHealth(20);
	}

	@Override
	protected PathNavigate createNavigator(World worldIn)
	{
		return new HansPathNavigate(this, world);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		tasks.taskEntries.removeIf(entry -> entry.action instanceof AIHansBase&&((AIHansBase)entry.action).shouldBeRemoved());

		if(world.isRemote)
		{
			if(dataManager.isDirty())
			{
				eyeColor = IIColor.fromPackedRGB(dataManager.get(DATA_MARKER_EYE_COLOR));
				legAnimation = getLegAnimationFromString(dataManager.get(DATA_MARKER_LEG_ANIMATION));
				armAnimation = getArmAnimationFromString(dataManager.get(DATA_MARKER_ARM_ANIMATION));

				NBTTagCompound speech = dataManager.get(DATA_MARKER_SPEECH);
				if(mouthShapeQueue.size()==0)
				{
					NBTTagList durations = speech.getTagList("durations", NBT.TAG_INT);
					NBTTagList shapes = speech.getTagList("shapes", NBT.TAG_STRING);

					for(int i = 0; i < durations.tagCount(); i++)
					{
						NBTTagString s = (NBTTagString)shapes.get(i);
						NBTTagInt d = (NBTTagInt)durations.get(i);
						mouthShapeQueue.add(new Tuple<>(d.getInt(), MouthShapes.v(s.getString())));
					}
					dataManager.set(DATA_MARKER_SPEECH, new NBTTagCompound());
				}

			}

			handleSpeech();

			if(this.getAttackTarget()!=null)
			{
				eyeEmotion = EyeEmotions.FROWNING;
				mouthEmotion = MouthEmotions.ANGRY;
			}
			else
			{
				mouthEmotion = MouthEmotions.ANGRY;

				if(legAnimation==HansLegAnimation.KAZACHOK)
					mouthEmotion = MouthEmotions.HAPPY;

				float hp = getHealth()/getMaxHealth();
				if(hp > 0.75||legAnimation==HansLegAnimation.KAZACHOK)
					eyeEmotion = EyeEmotions.HAPPY;
				else if(hp > 0.5)
					eyeEmotion = EyeEmotions.NEUTRAL;
				else
					eyeEmotion = EyeEmotions.FROWNING;
			}
		}
		else
		{
			dataManager.set(DATA_MARKER_EYE_COLOR, eyeColor.getPackedRGB());
			//check if enemies are around
			if(ticksExisted%25==0)
				enemyContact = world.getEntitiesWithinAABB(Entity.class,
								new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).grow(14), this::isValidTarget)
						.stream().findAny().isPresent();

			//idle + combat animations
			HansLegAnimation currentLeg = legAnimation;
			HansArmAnimation currentArm = armAnimation;
			legAnimation = isInWater()?HansLegAnimation.SWIMMING: HansLegAnimation.STANDING;
			armAnimation = HansArmAnimation.NORMAL;

			for(EntityAITaskEntry entry : tasks.taskEntries)
			{
				if(entry.action instanceof AIHansBase)
					((AIHansBase)entry.action).setRequiredAnimation();
			}

			if(currentLeg!=legAnimation)
			{
				this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getWalkSpeed());
				dataManager.set(DATA_MARKER_LEG_ANIMATION, legAnimation.name().toLowerCase());
			}

			//dataManager.set(DATA_MARKER_ARM_ANIMATION, HansArmAnimation.NORMAL.name().toLowerCase());

			if(currentArm!=armAnimation)
				dataManager.set(DATA_MARKER_ARM_ANIMATION, armAnimation.name().toLowerCase());

		}

		//update held item
		getHeldItemMainhand().getItem().onUpdate(getHeldItemMainhand(), world, this, 0, true);

		if(prevLegAnimation!=legAnimation)
		{
			if(legAnimationTimer > 0)
				legAnimationTimer--;
			else
			{
				legAnimationTimer = 8;
				prevLegAnimation = legAnimation;
				setSize(legAnimation.aabbWidth, legAnimation.aabbHeight);
			}
		}

		if(prevArmAnimation!=armAnimation)
		{
			if(armAnimationTimer > 0)
				armAnimationTimer--;
			else
			{
				armAnimationTimer = 8;
				prevArmAnimation = armAnimation;
			}
		}
	}

	private void handleSpeech()
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
	}

	private double getWalkSpeed()
	{
		return 0.25D*legAnimation.walkSpeedModifier;
	}

	private HansLegAnimation getLegAnimationFromString(String s)
	{
		return Arrays.stream(HansLegAnimation.values())
				.filter(anim -> s.equalsIgnoreCase(anim.name()))
				.findFirst().orElse(HansLegAnimation.STANDING);
	}

	private HansArmAnimation getArmAnimationFromString(String s)
	{
		return Arrays.stream(HansArmAnimation.values())
				.filter(anim -> s.equalsIgnoreCase(anim.name()))
				.findFirst().orElse(HansArmAnimation.NORMAL);
	}

	@Nonnull
	@Override
	public HansPathNavigate getNavigator()
	{
		return ((HansPathNavigate)super.getNavigator());
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();

		//Attack mobs and enemies focused on the Hans first
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, 1, true, false,
						input -> input.isEntityAlive()&&(input instanceof IMob||input.getAttackTarget()==this))
				{
					@Override
					protected AxisAlignedBB getTargetableArea(double targetDistance)
					{
						return this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance*0.66f, targetDistance);
					}
				}

		);
		//Attack entities with different team, stay neutral on default
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityLivingBase>(this, EntityLivingBase.class, 1, true, false,
						input -> input!=null&&isValidTarget(input))
				{
					@Override
					protected AxisAlignedBB getTargetableArea(double targetDistance)
					{
						return this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance*0.66f, targetDistance);
					}
				}
		);
		//Call other hanses for help when attacked
		this.targetTasks.addTask(2, new AIHansAlertOthers(this, true));

		this.tasks.addTask(2, new AIHansHolsterWeapon(this));
		updateWeaponTasks();

		this.tasks.addTask(5, new EntityAIAvoidEntity<>(this, EntityGasCloud.class, 8.0F, 0.6f, 0.7f));
		//this.tasks.addTask(6, new AIHansIdle(this));
		//this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 6.0F));

		// TODO: 04.02.2022 swimming anim
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new AIHansClimbLadder(this));
		this.tasks.addTask(0, doorTask = new AIHansOpenDoor(this, true));

		//this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, avEntity-> this.hasAmmunition()&&avEntity!=null&&avEntity.getRevengeTarget()==this, 8.0F, 0.6D, 0.6D));
	}

	public double getSprintSpeed()
	{
		return getWalkSpeed()*1.25f;
	}

	public void updateWeaponTasks()
	{
		if(weaponTask!=null)
			this.tasks.removeTask(weaponTask);
		this.weaponTask = HansUtils.getHandWeaponTask(this);
		if(weaponTask!=null)
			this.tasks.addTask(3, weaponTask);
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.125f, true));
	}

	@Override
	public boolean startRiding(@Nonnull Entity entity)
	{
		if(super.startRiding(entity))
		{
			if(entity instanceof EntityMachinegun)
				tasks.addTask(0, vehicleTask = new AIHansMachinegun(this));
			else if(entity instanceof EntityMortar)
				tasks.addTask(0, vehicleTask = new AIHansMortar(this));
			else if(entity.getLowestRidingEntity() instanceof EntityFieldHowitzer)
				tasks.addTask(0, vehicleTask = new AIHansHowitzer(this));
			return true;
		}
		return false;
	}

	public boolean markCrewman(@Nonnull Entity entity)
	{
		if(vehicleTask!=null)
			return false;

		tasks.addTask(0, new AIHansCrewman(this, entity));
		return true;
	}

	@Override
	public void dismountEntity(@Nonnull Entity entity)
	{
		super.dismountEntity(entity);
		if(vehicleTask!=null)
			tasks.removeTask(vehicleTask);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(facing==null) return (T)invHandler;
			else if(facing.getAxis().isVertical()) return (T)handHandler;
			else if(facing.getAxis().isHorizontal()) return (T)armorHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readEntityFromNBT(@Nonnull NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		readInventory(compound.getTagList("npc_inventory", 10));

		//hey, Mojang guys, do you know you can make a field accessible instead of doubling it for each extending class?
		//seriously, you consider yourself real programmers?
		if(compound.hasKey("HandItems"))
		{
			NBTTagList handInventory = compound.getTagList("HandItems", 10);
			setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((NBTTagCompound)handInventory.get(0))));
			setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack((NBTTagCompound)handInventory.get(1)));
		}

		this.legAnimation = getLegAnimationFromString(compound.getString("leg_animation"));
		this.armAnimation = getArmAnimationFromString(compound.getString("arm_animation"));
		this.eyeColor = IIColor.fromPackedRGB(compound.getInteger("eye_color"));
		this.commander = compound.getBoolean("commander");
		updateWeaponTasks();
	}

	@Override
	public void writeEntityToNBT(@Nonnull NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setTag("npc_inventory", Utils.writeInventory(mainInventory));
		compound.setString("leg_animation", legAnimation.name().toLowerCase());
		compound.setString("arm_animation", armAnimation.name().toLowerCase());
		compound.setInteger("eye_color", eyeColor.getPackedRGB());
		compound.setBoolean("commander", commander);
	}

	private void readInventory(NBTTagList npc_inventory)
	{
		int max = npc_inventory.tagCount();
		for(int i = 0; i < max; i++)
		{
			NBTTagCompound itemTag = npc_inventory.getCompoundTagAt(i);
			int slot = itemTag.getByte("Slot")&255;
			if(slot < this.mainInventory.size())
				this.mainInventory.set(slot, new ItemStack(itemTag));
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25f);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(52f);
	}

	public boolean attackEntityAsMob(Entity entityIn)
	{
		this.world.setEntityState(this, (byte)4);
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(1+this.rand.nextInt(2)));

		if(flag)
			this.applyEnchantments(this, entityIn);

		this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
		return flag;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand)
	{
		return !player.isSpectator()&&hand==EnumHand.MAIN_HAND&&player.getPositionVector().distanceTo(this.getPositionVector()) <= 1d;
	}

	@Nonnull
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, @Nonnull Vec3d vec, @Nonnull EnumHand hand)
	{
		if(player.isSpectator()||hand==EnumHand.OFF_HAND)
			return EnumActionResult.FAIL;

		if(!world.isRemote)
		{
			this.prevRotationYaw = this.rotationYawHead;
			this.rotationYaw = this.rotationYawHead;

			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.isEmpty())
			{
				tasks.addTask(2, new AIHansSalute(this, player));
				greetPlayer(player);
			}
			else if(tasks.taskEntries.stream().noneMatch(entry -> entry.action instanceof AIHansKazachok)&&Utils.isFluidRelatedItemStack(heldItem))
			{
				IFluidHandlerItem capability = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

				if(capability!=null)
				{
					FluidStack drain = capability.drain(new FluidStack(IEContent.fluidEthanol, 1000), false); // TODO: 08.12.2021 make an actual fix
					if(drain!=null)
					{
						tasks.addTask(2, new AIHansTimedLookAtEntity(this, player, 60, 1f));
						tasks.addTask(2, new AIHansKazachok(this, drain.amount/1000f));
					}
				}
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

				tasks.addTask(2, new AIHansSalute(this, player));
			}
			player.setHeldItem(EnumHand.MAIN_HAND, heldItem);
			player.swingArm(hand);

		}
		else
		{
			if(mouthShapeQueue.size() > 0)
				return EnumActionResult.PASS;
		}
		return EnumActionResult.PASS;
	}

	private void greetPlayer(EntityPlayer player)
	{
		sendPlayerMessage(player,
				String.format("Guten %1$s, %2$s!",
						HansUtils.getGermanTimeName(world.getWorldTime()),
						player.getName()
				));

		//Uses Rhubarb, the voice to lip shapes thingy, if you want to see how it works, check out their github
		//Disabled for now, might see service Soon™

		// TODO: 28.12.2021 decide on how speech will be performed (server or client)
		this.mouthShapeQueue.clear();
		this.speechProgress = 0;
		HansAnimations.putMouthShape(this, 'X', 0.00, 0.35);
		HansAnimations.putMouthShape(this, 'F', 0.35, 0.63);
		HansAnimations.putMouthShape(this, 'B', 0.63, 0.70);
		HansAnimations.putMouthShape(this, 'C', 0.70, 0.84);
		HansAnimations.putMouthShape(this, 'B', 0.84, 0.98);
		HansAnimations.putMouthShape(this, 'X', 0.98, 1.27);
		HansAnimations.putMouthShape(this, 'B', 1.27, 1.34);
		HansAnimations.putMouthShape(this, 'A', 1.34, 1.40);
		HansAnimations.putMouthShape(this, 'C', 1.40, 1.45);
		HansAnimations.putMouthShape(this, 'B', 1.45, 0.63);
		HansAnimations.putMouthShape(this, 'E', 1.56, 0.63);
		HansAnimations.putMouthShape(this, 'A', 1.63, 1.72);
		HansAnimations.putMouthShape(this, 'E', 1.72, 1.84);
		HansAnimations.putMouthShape(this, 'D', 1.84, 2.12);
		HansAnimations.putMouthShape(this, 'C', 2.12, 2.19);
		HansAnimations.putMouthShape(this, 'B', 2.19, 2.40);
		HansAnimations.putMouthShape(this, 'X', 2.40, 2.97);
		HansAnimations.putMouthShape(this, 'A', 2.97, 3.10);
		dataManager.set(DATA_MARKER_SPEECH, createServerSpeechTagCompound());
	}

	@Nonnull
	private NBTTagCompound createServerSpeechTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList durations = new NBTTagList();
		NBTTagList shapes = new NBTTagList();

		for(Tuple<Integer, MouthShapes> element : mouthShapeQueue)
		{
			durations.appendTag(new NBTTagInt(element.getFirst()));
			shapes.appendTag(new NBTTagString(element.getSecond().name()));
		}

		tag.setTag("durations", durations);
		tag.setTag("shapes", shapes);

		this.mouthShapeQueue.clear();
		return tag;
	}

	@Override
	public HoverEvent getHoverEvent()
	{
		return super.getHoverEvent();
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

	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn)
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

	@Nonnull
	protected SoundEvent getSwimSound()
	{
		return SoundEvents.ENTITY_PLAYER_SWIM;
	}

	@Nonnull
	protected SoundEvent getSplashSound()
	{
		return SoundEvents.ENTITY_PLAYER_SPLASH;
	}

	@Nonnull
	protected SoundEvent getFallSound(int heightIn)
	{
		return heightIn > 4?SoundEvents.ENTITY_PLAYER_BIG_FALL: SoundEvents.ENTITY_PLAYER_SMALL_FALL;
	}

	public HansLegAnimation getLegAnimation()
	{
		return legAnimation==HansLegAnimation.STANDING&&isSneaking()?HansLegAnimation.SNEAKING: legAnimation;
	}

	public AIHansOpenDoor getDoorTask()
	{
		return doorTask;
	}
}
