package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.SubmachinegunItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIUpgradableItemRendererAMT;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIIWeaponUpgrade extends ItemIISubItemsBase<WeaponUpgrades> implements IUpgrade
{
	public ItemIIWeaponUpgrade()
	{
		super("weapon_upgrade", 1, WeaponUpgrades.values());
	}

	public enum WeaponTypes implements ISerializableEnum
	{
		MACHINEGUN(0xdc3939, '\u24b6'),
		SUBMACHINEGUN(0xff894d, '\u24b7'),
		RAILGUN(0x3d6753, '\u24b8'),
		REVOLVER(0x3e4481, '\u24b9'),
		AUTOREVOLVER(0x2c305b, '\u24ba'),
		ASSAULT_RIFLE(0xff6440, '\u24bb'),
		SPIGOT_MORTAR(0x7a538d, '\u24bc'),
		RIFLE(0x84820e, '\u24bd');

		public final int color;
		public final char symbol;

		WeaponTypes(int color, char symbol)
		{
			this.color = color;
			this.symbol = symbol;
		}
	}

	@GeneratedItemModels(itemName = "weapon_upgrade")
	public enum WeaponUpgrades implements IIItemEnum
	{
		//--- Machinegun ---//

		//Increases fire rate
		HEAVY_BARREL(WeaponTypes.MACHINEGUN, "water_cooling"),
		//Uses water to speed up gun cooling
		WATER_COOLING(WeaponTypes.MACHINEGUN, "heavy_barrel"),
		//Allows to load ammo from ammo crate below the player
		BELT_FED_LOADER(WeaponTypes.MACHINEGUN, "second_magazine"),
		//Adds a second magazine, increases reload time a little bit
		SECOND_MAGAZINE(WeaponTypes.MACHINEGUN, "belt_fed_loader"),

		//Speeds up mg setting up time, but increases recoil
		HASTY_BIPOD(WeaponTypes.MACHINEGUN, "precise_bipod", "tripod"),
		//Slows down mg setting up time, but decreases recoil
		PRECISE_BIPOD(WeaponTypes.MACHINEGUN, "hasty_bipod", "tripod"),
		//3 x Magnification
		SCOPE(new WeaponTypes[]{WeaponTypes.MACHINEGUN, WeaponTypes.AUTOREVOLVER, WeaponTypes.ASSAULT_RIFLE, WeaponTypes.RIFLE}, "infrared_scope"),
		//Allows nightvision + 2 x magnification, uses energy from player's backpack
		INFRARED_SCOPE(new WeaponTypes[]{WeaponTypes.MACHINEGUN, WeaponTypes.ASSAULT_RIFLE},
				(stack, nbt) -> {
					//Assault Rifle
					if(stack.getItem() instanceof ItemIIGunBase)
						nbt.setBoolean("energy", true);
				},
				"scope"),
		//Deflects projectiles
		SHIELD(WeaponTypes.MACHINEGUN),
		//Slows down mg setting up time, almost eliminates recoil, increases yaw and pitch angles
		TRIPOD(WeaponTypes.MACHINEGUN, "hasty_bipod", "precise_bipod"),

		//--- Submachinegun ---//

		//Adds a velocity, penetration and suppression boost, lowers the firerate
		STURDY_BARREL(WeaponTypes.SUBMACHINEGUN),
		//Makes gunshots (almost) silent
		SUPPRESSOR(WeaponTypes.SUBMACHINEGUN),
		//Allows using drum magazines
		BOTTOM_LOADING(WeaponTypes.SUBMACHINEGUN),
		//Reduces aiming time
		FOLDING_STOCK(WeaponTypes.SUBMACHINEGUN),

		//--- Assault Rifle ---//

		//Allows shooting railgun grenades at a lower range, requires energy
		RIFLE_GRENADE_LAUNCHER(WeaponTypes.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"stereoscopic_rangefinder"),
		//Displays range to point where the gun is aimed at, requires energy
		STEREOSCOPIC_RANGEFINDER(WeaponTypes.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"rifle_grenade_launcher"),

		//Decreases recoil, requires energy
		GYROSCOPIC_STABILIZER(WeaponTypes.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"electric_firing_motor", "railgun_assisted_chamber"),
		//Increases firing rate for auto mode, requires energy
		ELECTRIC_FIRING_MOTOR(WeaponTypes.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"gyroscopic_stabilizer", "railgun_assisted_chamber"),
		//Increases velocity of bullets in manual mode, requires energy
		RAILGUN_ASSISTED_CHAMBER(WeaponTypes.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"gyroscopic_stabilizer", "electric_firing_motor"),

		//--- Autorevolver ---//
//
//		//Increases velocity, penetration and suppression
//		HEAVY_SPRINGBOX(WeaponTypes.AUTOREVOLVER),

		//--- Rifle ---//

		SEMI_AUTOMATIC(WeaponTypes.RIFLE, "extended_barrel"),
		EXTENDED_BARREL(WeaponTypes.RIFLE, "semi_automatic");

		public final ImmutableSet<WeaponTypes> toolset;
		private final BiPredicate<ItemStack, ItemStack> applyCheck;
		private final BiConsumer<ItemStack, NBTTagCompound> function;

		WeaponUpgrades(final WeaponTypes type, String... incompatible)
		{
			this(new WeaponTypes[]{type}, incompatible);
		}

		WeaponUpgrades(final WeaponTypes[] types, final String... incompatible)
		{
			this(types, (stack, nbt) -> {
			}, incompatible);
		}

		WeaponUpgrades(final WeaponTypes type, @Nullable BiConsumer<ItemStack, NBTTagCompound> appliedTag, final String... incompatible)
		{
			this(new WeaponTypes[]{type}, appliedTag, incompatible);
		}

		WeaponUpgrades(final WeaponTypes[] types, @Nullable BiConsumer<ItemStack, NBTTagCompound> appliedTag, final String... incompatible)
		{
			this.toolset = ImmutableSet.copyOf(types);
			this.applyCheck = (target, upgrade) -> {
				//check for incompatible upgrades
				NBTTagCompound upgrades = ((IUpgradeableTool)target.getItem()).getUpgrades(target);

				//one upgrade can't be installed 2 times
				if(upgrades.hasKey(getName()))
					return false;

				for(String s : incompatible)
					if(upgrades.hasKey(s))
						return false; //found
				//not found
				return true;
			};

			//Set function applying NBT data to item
			BiConsumer<ItemStack, NBTTagCompound> nbtFunction = (upgrade, modifications) -> modifications.setBoolean(getName(), true);
			this.function = (appliedTag!=null)?nbtFunction.andThen(appliedTag): nbtFunction;
		}
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag)
	{
		WeaponUpgrades sub = stackToSub(stack);
		//add valid weapon types
		for(WeaponTypes type : sub.toolset)
			list.add(IIUtils.getHexCol(type.color, type.symbol+" "+I18n.format(IIReference.DESC_TOOLUPGRADE+"item."+type.getName())));

		//add description
		String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(
				I18n.format(IIReference.DESCRIPTION_KEY+"toolupgrade."+sub.getName()), 200);
		Arrays.stream(flavour).map(IIUtils::getItalicString).forEach(list::add);
	}

	@Override
	public Set<String> getUpgradeTypes(ItemStack stack)
	{
		return stackToSub(stack).toolset.stream()
				.map(ISerializableEnum::getName)
				.map(String::toUpperCase)
				.collect(Collectors.toSet());
	}

	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		if(target.getItem() instanceof IUpgradeableTool)
			return stackToSub(upgrade).applyCheck.test(target, upgrade);
		return false;
	}

	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		stackToSub(upgrade).function.accept(upgrade, modifications);
	}

	private static Predicate<EasyNBT> hasUpgrade(@Nonnull WeaponUpgrades upgrade)
	{
		return easyNBT -> easyNBT.hasKey(upgrade.getName());
	}

	@SideOnly(Side.CLIENT)
	public static void addUpgradesToRender()
	{
		//--- Machinegun ---//

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("water_cooling")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.waterCoolingBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("heavy_barrel")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.heavyBarrelBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("second_magazine")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.secondMagazineMainBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.secondMagazineMagBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("belt_fed_loader")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.ammoBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.beltFedLoaderBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("scope")),
				(stack, tmtNamedBoxGroups) ->
						tmtNamedBoxGroups.add(MachinegunRenderer.model.scopeBox)
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("infrared_scope")),
				(stack, tmtNamedBoxGroups) ->
						tmtNamedBoxGroups.add(MachinegunRenderer.model.infraredScopeBox)
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("hasty_bipod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.hastyBipodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("precise_bipod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.preciseBipodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("tripod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.tripodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("shield")),
				(stack, tmtNamedBoxGroups) ->
						tmtNamedBoxGroups.add(MachinegunRenderer.model.shieldBox)
		);

		//--- Submachinegun ---//

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("sturdy_barrel")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.sturdyBarrelBox);
				}
		);

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("suppressor")),
				(stack, tmtNamedBoxGroups) ->
						tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.silencerBox)
		);

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("bottom_loading")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.ammoBox);
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.gripBox);
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.loaderBox);

					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.bottomLoaderBox);
				}
		);

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("folding_stock")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.stockBox);

					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.foldingStockHolderBox);
					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.foldingStockBox);
				}
		);

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getInteger("melee") > 0),
				(stack, tmtNamedBoxGroups) ->
						tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.bayonetBox)
		);

		//--- Assault Rifle ---//
		IIUpgradableItemRendererAMT<?> stg = IIContent.itemAssaultRifle.getItemRenderer();
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.SCOPE), "scope");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.INFRARED_SCOPE), "infrared_scope");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.STEREOSCOPIC_RANGEFINDER), "rangefinder");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.RAILGUN_ASSISTED_CHAMBER), "railgun");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.RIFLE_GRENADE_LAUNCHER), "grenade_launcher");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.ELECTRIC_FIRING_MOTOR), "electric_motor");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrades.GYROSCOPIC_STABILIZER), "stabilizer");

		//--- Rifle ---//
		IIUpgradableItemRendererAMT<?> rifle = IIContent.itemRifle.getItemRenderer();
		rifle.addUpgradePart(hasUpgrade(WeaponUpgrades.SCOPE), "scope");
		rifle.addUpgradePart(easyNBT -> easyNBT.hasKey("melee"), "bayonet");
		rifle.addUpgradePart(hasUpgrade(WeaponUpgrades.EXTENDED_BARREL), "extended_barrel");
		rifle.addUpgradePart(hasUpgrade(WeaponUpgrades.SEMI_AUTOMATIC), "semi_automatic");
	}
}
