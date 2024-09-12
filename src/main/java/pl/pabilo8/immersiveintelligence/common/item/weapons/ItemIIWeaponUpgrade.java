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
import pl.pabilo8.immersiveintelligence.client.util.amt.IIUpgradableItemRendererAMT;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
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
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIWeaponUpgrade extends ItemIISubItemsBase<WeaponUpgrade> implements IUpgrade
{
	public ItemIIWeaponUpgrade()
	{
		super("weapon_upgrade", 1, WeaponUpgrade.values());
	}

	/**
	 * Categories of upgradable weapon and tool types
	 */
	public enum WeaponType implements ISerializableEnum
	{
		MACHINEGUN(0xdc3939, '\u24b6'),
		SUBMACHINEGUN(0xff894d, '\u24b7'),
		RAILGUN(0x3d6753, '\u24b8'),
		REVOLVER(0x3e4481, '\u24b9'),
		AUTOREVOLVER(0x2c305b, '\u24ba'),
		ASSAULT_RIFLE(0xff6440, '\u24bb'),
		SPIGOT_MORTAR(0x7a538d, '\u24bc'),
		RIFLE(0x84820e, '\u24bd');

		public final IIColor color;
		public final char symbol;

		WeaponType(int color, char symbol)
		{
			this.color = IIColor.fromPackedRGB(color);
			this.symbol = symbol;
		}
	}

	/**
	 * Upgrades for weapons and tools
	 */
	@GeneratedItemModels(itemName = "weapon_upgrade")
	public enum WeaponUpgrade implements IIItemEnum
	{
		//--- Machinegun ---//

		//Increases fire rate
		HEAVY_BARREL(WeaponType.MACHINEGUN, "water_cooling"),
		//Uses water to speed up gun cooling
		WATER_COOLING(WeaponType.MACHINEGUN, "heavy_barrel"),
		//Allows to load ammo from ammo crate below the player
		BELT_FED_LOADER(WeaponType.MACHINEGUN, "second_magazine"),
		//Adds a second magazine, increases reload time a little bit
		SECOND_MAGAZINE(WeaponType.MACHINEGUN, "belt_fed_loader"),

		//Speeds up mg setting up time, but increases recoil
		HASTY_BIPOD(WeaponType.MACHINEGUN, "precise_bipod", "tripod"),
		//Slows down mg setting up time, but decreases recoil
		PRECISE_BIPOD(WeaponType.MACHINEGUN, "hasty_bipod", "tripod"),
		//3 x Magnification
		SCOPE(new WeaponType[]{WeaponType.MACHINEGUN, WeaponType.AUTOREVOLVER, WeaponType.ASSAULT_RIFLE, WeaponType.RIFLE}, "infrared_scope"),
		//Allows nightvision + 2 x magnification, uses energy from player's backpack
		INFRARED_SCOPE(new WeaponType[]{WeaponType.MACHINEGUN, WeaponType.ASSAULT_RIFLE},
				(stack, nbt) -> {
					//Assault Rifle
					if(stack.getItem() instanceof ItemIIGunBase)
						nbt.setBoolean("energy", true);
				},
				"scope"),
		//Deflects projectiles
		SHIELD(WeaponType.MACHINEGUN),
		//Slows down mg setting up time, almost eliminates recoil, increases yaw and pitch angles
		TRIPOD(WeaponType.MACHINEGUN, "hasty_bipod", "precise_bipod"),

		//--- Submachinegun ---//

		//Adds a velocity, penetration and suppression boost, lowers the firerate
		STURDY_BARREL(WeaponType.SUBMACHINEGUN),
		//Makes gunshots (almost) silent
		SUPPRESSOR(WeaponType.SUBMACHINEGUN),
		//Allows using drum magazines
		BOTTOM_LOADING(WeaponType.SUBMACHINEGUN),
		//Reduces aiming time
		FOLDING_STOCK(WeaponType.SUBMACHINEGUN),

		//--- Assault Rifle ---//

		//Allows shooting railgun grenades at a lower range, requires energy
		RIFLE_GRENADE_LAUNCHER(WeaponType.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"stereoscopic_rangefinder"),
		//Displays range to point where the gun is aimed at, requires energy
		STEREOSCOPIC_RANGEFINDER(WeaponType.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"rifle_grenade_launcher"),

		//Decreases recoil, requires energy
		GYROSCOPIC_STABILIZER(WeaponType.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"electric_firing_motor", "railgun_assisted_chamber"),
		//Increases firing rate for auto mode, requires energy
		ELECTRIC_FIRING_MOTOR(WeaponType.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"gyroscopic_stabilizer", "railgun_assisted_chamber"),
		//Increases velocity of bullets in manual mode, requires energy
		RAILGUN_ASSISTED_CHAMBER(WeaponType.ASSAULT_RIFLE,
				(stack, nbt) -> nbt.setBoolean("energy", true),
				"gyroscopic_stabilizer", "electric_firing_motor"),

		//--- Autorevolver ---//
//
//		//Increases velocity, penetration and suppression
//		HEAVY_SPRINGBOX(WeaponTypes.AUTOREVOLVER),

		//--- Rifle ---//

		SEMI_AUTOMATIC(WeaponType.RIFLE, "extended_barrel"),
		EXTENDED_BARREL(WeaponType.RIFLE, "semi_automatic");

		public final ImmutableSet<WeaponType> toolset;
		private final BiPredicate<ItemStack, ItemStack> applyCheck;
		private final BiConsumer<ItemStack, NBTTagCompound> function;

		WeaponUpgrade(final WeaponType type, String... incompatible)
		{
			this(new WeaponType[]{type}, incompatible);
		}

		WeaponUpgrade(final WeaponType[] types, final String... incompatible)
		{
			this(types, (stack, nbt) -> {
			}, incompatible);
		}

		WeaponUpgrade(final WeaponType type, @Nullable BiConsumer<ItemStack, NBTTagCompound> appliedTag, final String... incompatible)
		{
			this(new WeaponType[]{type}, appliedTag, incompatible);
		}

		WeaponUpgrade(final WeaponType[] types, @Nullable BiConsumer<ItemStack, NBTTagCompound> appliedTag, final String... incompatible)
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
		WeaponUpgrade sub = stackToSub(stack);
		//add valid weapon types
		for(WeaponType type : sub.toolset)
			list.add(type.color.getHexCol(type.symbol+" "+I18n.format(IIReference.DESC_TOOLUPGRADE+"item."+type.getName())));

		//add description
		String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(
				I18n.format(IIReference.DESCRIPTION_KEY+"toolupgrade."+sub.getName()), 200);
		Arrays.stream(flavour).map(IIStringUtil::getItalicString).forEach(list::add);
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

	private static Predicate<EasyNBT> hasUpgrade(@Nonnull WeaponUpgrade upgrade)
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
		IIUpgradableItemRendererAMT<?> smg = IIContent.itemSubmachinegun.getItemRenderer();
		smg.addUpgradePart(hasUpgrade(WeaponUpgrade.STURDY_BARREL), "sturdy_barrel");
		smg.addUpgradePart(hasUpgrade(WeaponUpgrade.SUPPRESSOR), "suppressor");
		smg.addUpgradePart(hasUpgrade(WeaponUpgrade.BOTTOM_LOADING), "bottom_loading");
		smg.addUpgradePart(hasUpgrade(WeaponUpgrade.FOLDING_STOCK), "folding_stock");
		smg.addUpgradePart(easyNBT -> easyNBT.hasKey("melee"), "bayonet");

		//--- Assault Rifle ---//
		IIUpgradableItemRendererAMT<?> stg = IIContent.itemAssaultRifle.getItemRenderer();
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.SCOPE), "scope");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.INFRARED_SCOPE), "infrared_scope");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.STEREOSCOPIC_RANGEFINDER), "rangefinder");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.RAILGUN_ASSISTED_CHAMBER), "railgun");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.RIFLE_GRENADE_LAUNCHER), "grenade_launcher");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.ELECTRIC_FIRING_MOTOR), "electric_motor");
		stg.addUpgradePart(hasUpgrade(WeaponUpgrade.GYROSCOPIC_STABILIZER), "stabilizer");

		//--- Rifle ---//
		IIUpgradableItemRendererAMT<?> rifle = IIContent.itemRifle.getItemRenderer();
		rifle.addUpgradePart(hasUpgrade(WeaponUpgrade.SCOPE), "scope");
		rifle.addUpgradePart(easyNBT -> easyNBT.hasKey("melee"), "bayonet");
		rifle.addUpgradePart(hasUpgrade(WeaponUpgrade.EXTENDED_BARREL), "extended_barrel");
		rifle.addUpgradePart(hasUpgrade(WeaponUpgrade.SEMI_AUTOMATIC), "semi_automatic");
	}
}
