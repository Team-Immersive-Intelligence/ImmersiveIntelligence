package pl.pabilo8.immersiveintelligence.common.commands.ii;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemToolUpgrade.ToolUpgrades;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIIArmorUpgrade.ArmorUpgrades;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandIIHans extends CommandBase
{
	/**
	 * The registry of HansSquads - the methods of spawning groups (or single) Hanses
	 */
	static final HashMap<ResourceLocation, HansSquad> squadList = new HashMap<>();

	static
	{
		squadList.clear();

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "rifle"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);
						HansUtils.setRifle(hans, ItemStack.EMPTY);

						ItemStack bullet = IIContent.itemAmmoMachinegun.getBulletWithParams("core_steel", "piercing");
						bullet.setCount(bullet.getMaxStackSize());
						for(int i = 0; i < 6; i++)
							hans.mainInventory.set(i, bullet.copy());
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "smg"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);

						ItemStack magazine = IIContent.itemBulletMagazine.getMagazine(Magazines.SUBMACHINEGUN,
								IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "piercing"));
						HansUtils.setSubmachinegun(hans, magazine);
						for(int i = 0; i < 6; i++)
							hans.mainInventory.set(i, magazine.copy());
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "grenadier"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);
						ItemStack stack = IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "hmx")
								.setStackDisplayName("Sprenghandgranate mk.2");
						stack.setCount(16);
						hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "smg_drum"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);

						ItemStack magazine = IIContent.itemBulletMagazine.getMagazine(Magazines.SUBMACHINEGUN_DRUM,
								IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "piercing"));
						HansUtils.setSubmachinegun(hans, magazine, WeaponUpgrades.BOTTOM_LOADING);
						for(int i = 0; i < 4; i++)
							hans.mainInventory.set(i, magazine.copy());
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "smg_spec_ops"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans, ArmorUpgrades.INFILTRATOR_GEAR, ArmorUpgrades.COMPOSITE_ARMOR_PLATES);

						ItemStack magazine = IIContent.itemBulletMagazine.getMagazine(Magazines.SUBMACHINEGUN_DRUM,
								IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_tungsten", "piercing"));
						HansUtils.setSubmachinegun(hans, magazine, WeaponUpgrades.BOTTOM_LOADING, WeaponUpgrades.FOLDING_STOCK, WeaponUpgrades.SUPPRESSOR);
						for(int i = 0; i < 6; i++)
							hans.mainInventory.set(i, magazine.copy());
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "stg"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);

						ItemStack magazine = IIContent.itemBulletMagazine.getMagazine(Magazines.ASSAULT_RIFLE,
								IIContent.itemAmmoAssaultRifle.getBulletWithParams("core_steel", "piercing"));
						HansUtils.setAssaultRifle(hans, magazine);
						for(int i = 0; i < 6; i++)
							hans.mainInventory.set(i, magazine.copy());
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "revolver"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);
						ItemStack stack = new ItemStack(IEContent.itemRevolver);
						hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "railgun"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);
						ItemStack stack = new ItemStack(IEContent.itemRailgun);
						hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
						ItemStack ammo = IIContent.itemRailgunGrenade.getBulletWithParams("core_brass", "canister", "tnt");
						ammo.setCount(8);
						hans.mainInventory.set(0, ammo.copy());
						hans.mainInventory.set(1, ammo.copy());
						ItemStack backpack = new ItemStack(IIContent.itemAdvancedPowerPack);
						EnergyHelper.insertFlux(backpack, 9999999, false);
						hans.setItemStackToSlot(EntityEquipmentSlot.CHEST, backpack);
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "railgun_sniper"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);
						ItemStack stack = new ItemStack(IEContent.itemRailgun);
						ItemIIRailgunOverride itemRailgun = ((ItemIIRailgunOverride)IEContent.itemRailgun);

						NonNullList<ItemStack> upgrades = NonNullList.withSize(itemRailgun.getSlotCount(stack), ItemStack.EMPTY);
						upgrades.set(0, new ItemStack(IEContent.itemToolUpgrades, 1, ToolUpgrades.RAILGUN_SCOPE.ordinal()));

						itemRailgun.setContainedItems(stack, upgrades);
						itemRailgun.recalculateUpgrades(stack);
						itemRailgun.finishUpgradeRecalculation(stack);

						hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
						ItemStack ammo = new ItemStack(IEContent.itemGraphiteElectrode, 16);
						hans.mainInventory.set(0, ammo.copy());
						hans.mainInventory.set(1, ammo.copy());
						ItemStack backpack = new ItemStack(IIContent.itemAdvancedPowerPack);
						EnergyHelper.insertFlux(backpack, 9999999, false);
						hans.setItemStackToSlot(EntityEquipmentSlot.CHEST, backpack);

					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "chemthrower"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans, ArmorUpgrades.GASMASK);
						ItemStack stack = new ItemStack(IEContent.itemChemthrower);
						hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
						ItemNBTHelper.setTagCompound(stack, "Fluid", new FluidStack(IEContent.fluidCreosote, 2000).writeToNBT(new NBTTagCompound()));
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "observer"),
				new HansSquadHandWeapon()
				{
					@Override
					public void setItems(EntityHans hans, int id)
					{
						HansUtils.setHelmet(hans);
						ItemStack stack = new ItemStack(IIContent.itemBinoculars);
						hans.setHeldItem(EnumHand.MAIN_HAND, stack);
					}
				}
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "heavy_mg"),
				new HansSquadMG()
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "mortar"),
				new HansSquadMortar()
		);

		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "field_howi"),
				new HansSquadFieldHowitzer()
		);


		squadList.put(new ResourceLocation(ImmersiveIntelligence.MODID, "eselschreck"),
				new HansSquadTeamWeapon<EntityDonkey>()
				{
					@Nullable
					@Override
					protected EntityDonkey spawnTeamWeapon(World world, Vec3d pos, Team team, boolean parachute, float yaw, float pitch)
					{
						ItemStack mgstack = new ItemStack(IIContent.itemMachinegun);

						NonNullList<ItemStack> upgrades = NonNullList.withSize(3, ItemStack.EMPTY);
						upgrades.set(0, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.TRIPOD.ordinal()));
						upgrades.set(1, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.HEAVY_BARREL.ordinal()));
						IIContent.itemMachinegun.setContainedItems(mgstack, upgrades);
						IIContent.itemMachinegun.recalculateUpgrades(mgstack);
						IIContent.itemMachinegun.finishUpgradeRecalculation(mgstack);

						EntityMachinegun mg = new EntityMachinegun(world, new BlockPos(pos).down(), yaw, 0, mgstack);
						world.spawnEntity(mg);

						EntityDonkey donkey = new EntityDonkey(world);
						donkey.setPosition(pos.x, pos.y+1, pos.z);

						if(team!=null)
							world.getScoreboard().addPlayerToTeam(donkey.getUniqueID().toString(), team.getName());

						world.spawnEntity(donkey);
						mg.startRiding(donkey, true);

						return donkey;
					}

					@Override
					protected EntityHans addCrewmen(World world, Vec3d pos, Team team, boolean parachute, EntityDonkey teamWeapon)
					{
						EntityHans hans = createCrewman(world, pos, team, parachute);
						teamWeapon.setOwnerUniqueId(hans.getUniqueID());
						teamWeapon.setHorseTamed(true);
						teamWeapon.setHorseSaddled(true);

						if(teamWeapon.getPassengers().size() > 0)
						{
							hans.startRiding(teamWeapon.getPassengers().get(0), true);
						}

						return hans;
					}
				}
		);

	}

	/**
	 * The lines used after a Hans was deployed
	 */
	static final String[] DEPLOYMENT_LINES = {
			"%s ist Einsatzbereit!",
			"Ein neues %s is Bereit zum Kampf",
			"%s - bereit zum Einsatz",
			"%s wartet auf deine Befehle",
			"%s ist Kampfbereit",
			"Wir haben ein neues %s!",
			"Der %s ist bereit!",
			"%s - bereit zum Dienst!",
			"Ein neues %s ist Kriegsbereit!"
	};

	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "hans";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Summons ze H A N S, usage: ii hans <squad_id> <amount> <team> <airborne>";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		try
		{
			if(server.getEntityWorld().isRemote||args.length < 1)
				throw new Exception();

			World world = sender.getEntityWorld();

			Entity commandSenderEntity = sender.getCommandSenderEntity();
			Vec3d spawnPosition = getSpawnPosition(sender);
			HansSquad squad = squadList.get(new ResourceLocation(args[0]));
			int amount = args.length > 1?Integer.parseInt(args[1]): 1;
			Team team = args.length > 2?world.getScoreboard().getTeam(args[2]): null;
			boolean parachute = args.length > 3&&Boolean.parseBoolean(args[3]);
			float yaw = 0, pitch = 0;
			if(commandSenderEntity!=null)
			{
				yaw = MathHelper.wrapDegrees(commandSenderEntity.rotationPitch);
				pitch = commandSenderEntity.rotationPitch;
			}


			if(amount < 1)
				throw new WrongUsageException("Squads MUST contain at least one Hans");

			if(squad!=null)
			{
				EntityHans hans = squad.spawnHanses(world, spawnPosition, amount, team, parachute, yaw, pitch);
				if(amount > 1||squad instanceof HansSquadTeamWeapon)
					hans.commander = true;

				if(commandSenderEntity!=null)
				{
					commandSenderEntity.sendMessage(new TextComponentTranslation(
							DEPLOYMENT_LINES[(int)((DEPLOYMENT_LINES.length-1)*Utils.RAND.nextDouble())],
							hans.getDisplayName().setStyle(
									new Style().setHoverEvent(hans.getHoverEvent())
							)
					));
				}

			}
			else
				throw new WrongUsageException("There is no squad with that id, use tab to see available options");

		}
		//because I'm lazy, feel free to pull request though :)
		catch(Exception e)
		{
			if(e instanceof WrongUsageException)
				throw ((WrongUsageException)e);
			else
				throw new WrongUsageException(getUsage(sender));
		}
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		switch(args.length)
		{
			case 1:
				return getListOfStringsMatchingLastWord(args, squadList.keySet());
			case 2:
				return Collections.singletonList("1");
			case 3:
				return getListOfStringsMatchingLastWord(args, sender.getEntityWorld().getScoreboard().getTeamNames());
			case 4:
				return getListOfStringsMatchingLastWord(args, "true", "false");
			default:
				return Collections.emptyList();
		}
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
	}

	@Nonnull
	public Vec3d getSpawnPosition(@Nonnull ICommandSender sender)
	{
		Entity commandSenderEntity = sender.getCommandSenderEntity();
		if(commandSenderEntity==null)
			return sender.getPositionVector();

		float blockReachDistance = 100f;
		Vec3d vec3d = commandSenderEntity.getPositionEyes(0);
		Vec3d vec3d1 = commandSenderEntity.getLook(0);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);
		RayTraceResult traceResult = commandSenderEntity.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
		if(traceResult==null||traceResult.typeOfHit==Type.MISS)
			return sender.getPositionVector();

		return new Vec3d(traceResult.getBlockPos().up());
	}

	private abstract static class HansSquad
	{
		public abstract EntityHans spawnHanses(World world, Vec3d pos, int amount, @Nullable Team team, boolean parachute, float yaw, float pitch);
	}

	private abstract static class HansSquadHandWeapon extends HansSquad
	{
		@Override
		public EntityHans spawnHanses(World world, Vec3d pos, int amount, @Nullable Team team, boolean parachute, float yaw, float pitch)
		{
			EntityHans firstHans = null;
			if(parachute)
				pos = pos.addVector(0, 60, 0);

			final int rsof = parachute?8: 1;
			final int row = (int)Math.floor(Math.sqrt(amount));
			final int roff = (int)Math.floor(row/2f)*rsof;
			int i = 0, missed = 0;

			while(i < amount)
			{
				int c = i+missed;
				//Vec3d offset = pos.add(new Vec3d(EnumFacing.getHorizontal(i).getDirectionVec()).scale(parachute?3f:1f).scale(1+(Math.floor(i/4f))));
				Vec3d offset = pos.addVector(-roff, 0, -roff).add(
						new Vec3d(Math.floor(c/(float)row)*rsof, 0, (c%row)*rsof)
				);

				if(world.getBlockState(new BlockPos(offset)).causesSuffocation())
				{
					missed += 1;
					continue;
				}

				EntityHans hans = new EntityHans(world);
				hans.setPosition(offset.x+0.5, offset.y, offset.z+0.5);
				if(team!=null)
					world.getScoreboard().addPlayerToTeam(hans.getUniqueID().toString(), team.getName());
				if(parachute)
					HansUtils.setParachute(hans);
				setItems(hans, i);
				hans.updateWeaponTasks();
				world.spawnEntity(hans);

				if(i==0)
					firstHans = hans;
				else firstHans.applyEntityCollision(hans);
				i++;
			}

			return firstHans;
		}

		public abstract void setItems(EntityHans hans, int id);
	}

	private static abstract class HansSquadTeamWeapon<T extends Entity> extends HansSquad
	{
		@Override
		public EntityHans spawnHanses(World world, Vec3d pos, int amount, @Nullable Team team, boolean parachute, float yaw, float pitch)
		{
			if(isAirDroppable()&&parachute)
				pos = pos.addVector(0, 60, 0);

			T teamWeapon = spawnTeamWeapon(world, pos, team, parachute, yaw, pitch);
			if(teamWeapon!=null)
				return addCrewmen(world, pos, team, parachute, teamWeapon);

			return null;
		}

		@Nullable
		protected abstract T spawnTeamWeapon(World world, Vec3d pos, Team team, boolean parachute, float yaw, float pitch);

		protected abstract EntityHans addCrewmen(World world, Vec3d pos, Team team, boolean parachute, T teamWeapon);

		protected EntityHans createCrewman(World world, Vec3d pos, Team team, boolean parachute)
		{
			EntityHans hans = new EntityHans(world);
			hans.setPosition(pos.x, pos.y, pos.z);
			world.spawnEntity(hans);
			HansUtils.setHelmet(hans, ArmorUpgrades.TECHNICIAN_GEAR);
			if(team!=null)
				world.getScoreboard().addPlayerToTeam(hans.getUniqueID().toString(), team.getName());
			if(isAirDroppable()&&parachute)
				HansUtils.setParachute(hans);
			hans.updateWeaponTasks();

			return hans;
		}

		protected boolean isAirDroppable()
		{
			return false;
		}
	}

	private static class HansSquadMG extends HansSquadTeamWeapon<EntityMachinegun>
	{
		@Override
		protected EntityMachinegun spawnTeamWeapon(World world, Vec3d pos, Team team, boolean parachute, float yaw, float pitch)
		{
			ItemStack mgstack = new ItemStack(IIContent.itemMachinegun);

			NonNullList<ItemStack> upgrades = NonNullList.withSize(3, ItemStack.EMPTY);
			upgrades.set(0, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.TRIPOD.ordinal()));
			upgrades.set(1, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.HEAVY_BARREL.ordinal()));
			IIContent.itemMachinegun.setContainedItems(mgstack, upgrades);
			IIContent.itemMachinegun.recalculateUpgrades(mgstack);
			IIContent.itemMachinegun.finishUpgradeRecalculation(mgstack);

			EntityMachinegun mg = new EntityMachinegun(world, new BlockPos(pos).down(), yaw, 0, mgstack);
			world.spawnEntity(mg);
			return mg;
		}

		@Override
		protected EntityHans addCrewmen(World world, Vec3d pos, Team team, boolean parachute, EntityMachinegun teamWeapon)
		{
			EntityHans hans = createCrewman(world, pos, team, parachute);
			hans.startRiding(teamWeapon);
			return hans;
		}
	}

	private static class HansSquadMortar extends HansSquadTeamWeapon<EntityMortar>
	{
		@Override
		protected EntityMortar spawnTeamWeapon(World world, Vec3d pos, Team team, boolean parachute, float yaw, float pitch)
		{
			EntityMortar mortar = new EntityMortar(world);
			mortar.setPositionAndRotation(pos.x, pos.y, pos.z, yaw, 80);
			world.spawnEntity(mortar);
			return mortar;
		}

		@Override
		protected EntityHans addCrewmen(World world, Vec3d pos, Team team, boolean parachute, EntityMortar teamWeapon)
		{
			EntityHans hans = createCrewman(world, pos, team, parachute);
			hans.startRiding(teamWeapon);
			hans.setPositionAndRotation(pos.x, pos.y, pos.z, teamWeapon.rotationYaw, 0);
			hans.setRotationYawHead(teamWeapon.rotationYaw);
			return hans;
		}
	}

	private static class HansSquadFieldHowitzer extends HansSquadTeamWeapon<EntityFieldHowitzer>
	{

		@Nullable
		@Override
		protected EntityFieldHowitzer spawnTeamWeapon(World world, Vec3d pos, Team team, boolean parachute, float yaw, float pitch)
		{
			EntityFieldHowitzer howi = new EntityFieldHowitzer(world);
			howi.setPositionAndRotation(pos.x, pos.y, pos.z, MathHelper.wrapDegrees(yaw+180), 0);
			world.spawnEntity(howi);
			if(parachute)
				HansUtils.setParachute(howi);
			return howi;
		}

		@Override
		protected EntityHans addCrewmen(World world, Vec3d pos, Team team, boolean parachute, EntityFieldHowitzer teamWeapon)
		{
			EntityHans hans1 = createCrewman(world, pos.addVector(parachute?8: 0, 0, 0), team, parachute);
			EntityHans hans2 = createCrewman(world, pos.addVector(parachute?-8: 0, 0, 0), team, parachute);

			if(parachute)
			{
				hans1.markCrewman(EntityVehicleSeat.getOrCreateSeat(teamWeapon, 0));
				hans2.markCrewman(EntityVehicleSeat.getOrCreateSeat(teamWeapon, 1));
			}
			else
			{
				hans1.startRiding(EntityVehicleSeat.getOrCreateSeat(teamWeapon, 0));
				hans2.startRiding(EntityVehicleSeat.getOrCreateSeat(teamWeapon, 1));
			}

			return hans1;
		}

		@Override
		protected boolean isAirDroppable()
		{
			return true;
		}
	}

	/*
	if(args[0].equals("hans"))
						{

							if(num==2)
							{
								EntityHans hans = new EntityHans(server.getEntityWorld());
								hans.setPosition(position.getX()+0.5, position.getY(), position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans);

								ItemStack mgstack = new ItemStack(IIContent.itemMachinegun);

								NonNullList<ItemStack> upgrades = NonNullList.withSize(3, ItemStack.EMPTY);
								upgrades.set(0, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.TRIPOD.ordinal()));
								upgrades.set(1, new ItemStack(IIContent.itemWeaponUpgrade, 1, WeaponUpgrades.HEAVY_BARREL.ordinal()));
								IIContent.itemSubmachinegun.setContainedItems(mgstack, upgrades);
								IIContent.itemSubmachinegun.recalculateUpgrades(mgstack);
								IIContent.itemSubmachinegun.finishUpgradeRecalculation(mgstack);

								EntityMachinegun mg = new EntityMachinegun(server.getEntityWorld(), position.down(), MathHelper.wrapDegrees(commandSenderEntity.getRotationYawHead()), -commandSenderEntity.rotationPitch, mgstack);
								server.getEntityWorld().spawnEntity(mg);
								hans.startRiding(mg);
							}
							else if(num==3)
							{
								EntityFieldHowitzer howi = new EntityFieldHowitzer(server.getEntityWorld());
								howi.setPositionAndRotation(position.getX()+0.5, position.getY(), position.getZ()+0.5, commandSenderEntity.getMirroredYaw(Mirror.FRONT_BACK), -commandSenderEntity.rotationPitch);
								server.getEntityWorld().spawnEntity(howi);

								EntityHans hans1 = new EntityHans(server.getEntityWorld());
								hans1.equipItems(num);
								hans1.setPosition(position.getX()+0.5, position.getY()+4, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans1);

								EntityHans hans2 = new EntityHans(server.getEntityWorld());
								hans2.equipItems(num);
								hans2.setPosition(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans2);

								hans1.startRiding(EntityVehicleSeat.getOrCreateSeat(howi, 0));
								hans2.startRiding(EntityVehicleSeat.getOrCreateSeat(howi, 1));
							}
							else if(num > 2&&num < 7)
							{
								EntityMotorbike motorbike = new EntityMotorbike(server.getEntityWorld());
								motorbike.setPositionAndRotation(position.getX()+0.5, position.getY(), position.getZ()+0.5, commandSenderEntity.getMirroredYaw(Mirror.FRONT_BACK), -commandSenderEntity.rotationPitch);
								motorbike.setUpgrade(num==4?"seat": (num==5?"storage": "tank"));
								server.getEntityWorld().spawnEntity(motorbike);

								EntityHans hans1 = new EntityHans(server.getEntityWorld());
								hans1.equipItems(2);
								hans1.setPosition(position.getX()+0.5, position.getY()+4, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans1);
								hans1.startRiding(EntityVehicleSeat.getOrCreateSeat(motorbike, 0));

								if(num==4)
								{
									EntityHans hans2 = new EntityHans(server.getEntityWorld());
									hans2.equipItems(1);
									hans2.setPosition(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
									server.getEntityWorld().spawnEntity(hans2);
									hans2.startRiding(EntityVehicleSeat.getOrCreateSeat(motorbike, 1));
								}
							}
							else if(num==9)
							{
								EntityHans hans = new EntityHans(server.getEntityWorld());
								hans.setPosition(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans);

								EntityTripodPeriscope tripodPeriscope = new EntityTripodPeriscope(server.getEntityWorld());
								tripodPeriscope.setPosition(position.getX()+0.5, position.getY(), position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(tripodPeriscope);
								hans.startRiding(tripodPeriscope);
							}
							else
							{
								EntityHans hans = new EntityHans(server.getEntityWorld());
								hans.equipItems(num);
								hans.setPosition(position.getX()+0.5, position.getY(), position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans);
							}
						}
						else
	 */
}
