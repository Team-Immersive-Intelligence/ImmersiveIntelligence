package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.08.2022
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public class IIReference
{
	//--- Tool Classes ---//
	//IE
	public static final String TOOL_HAMMER = Lib.TOOL_HAMMER;
	public static final String TOOL_WIRECUTTER = Lib.TOOL_WIRECUTTER;
	//II
	public static final String TOOL_ADVANCED_HAMMER = "II_ADVANCED_HAMMER";
	public static final String TOOL_WRENCH = "II_WRENCH";
	public static final String TOOL_ADVANCED_WRENCH = "II_ADVANCED_WRENCH";
	public static final String TOOL_CROWBAR = "II_CROWBAR";
	public static final String TOOL_TACHOMETER = "TOOL_TACHOMETER";

	//--- Language Keys ---//
	public static final String DESCRIPTION_KEY = "desc.immersiveintelligence.";
	public static final String INFO_KEY = "info.immersiveintelligence.";
	public static final String DATA_KEY = "datasystem.immersiveintelligence.";
	public static final String ROTARY_KEY = "rotary.immersiveintelligence.";
	public static final String BLOCK_KEY = "tile.immersiveintelligence.";
	public static final String GUI_LABEL_KEY = "ii.gui.";
	public static final String GUI_TOOLTIP_KEY = "ii.gui_tooltip.";
	public static final String GUI_HELPBOX_LABEL_KEY = "ii.gui_help.";

	//TODO: 09.08.2024 add energy and fluid I18n
	public static final String INFO_KEY_SPEED = INFO_KEY+"tachometer.torque";
	public static final String INFO_KEY_TORQUE = INFO_KEY+"tachometer.speed";

	public static final String INFO_KEY_TOOL_DURABILITY = INFO_KEY+"tool_durability";
	public static final String DESC_TOOLUPGRADE = DESCRIPTION_KEY+"toolupgrade.";
	public static final String DESC_BULLETS = DESCRIPTION_KEY+"bullets.";

	public static final String DESC_HOLD_CTRL = DESCRIPTION_KEY+"info.holdControl";
	public static final String DESC_HOLD_SHIFT = DESCRIPTION_KEY+"info.holdShift";
	public static final String DESC_HOLD_ALT = DESCRIPTION_KEY+"info.holdAlt";
	public static final String DESC_HOLD_TAB = DESCRIPTION_KEY+"info.holdTab";

	//--- Font Icons ---//
	public static final char CHARICON_SPEED = '\u29c1';
	public static final char CHARICON_TORQUE = '\u2296';
	public static final char CHARICON_ENERGY = '\u2607';
	public static final char CHARICON_RADIATION = '\u2622';
	public static final char CHARICON_BULLET = '\u2023';
	public static final char CHARICON_BULLET_CONTENTS = '\u29b3';
	public static final char CHARICON_PENETRATION = '\u29b4';
	public static final char CHARICON_SKULL = '\u2295';
	public static final char CHARICON_CONTACT = '\u29b0';
	public static final char CHARICON_PROXIMITY = '\u29b1';
	public static final char CHARICON_TIMED = '\u29b2';
	public static final char CHARICON_FOLDER = '\u2348';
	public static final char CHARICON_MG = '\u24b6';
	public static final char CHARICON_SMG = '\u24b7';
	public static final char CHARICON_RAILGUN = '\u24b8';
	public static final char CHARICON_REVOLVER = '\u24b9';
	public static final char CHARICON_AUTOREVOLVER = '\u24ba';
	public static final char CHARICON_STG = '\u24bb';
	public static final char CHARICON_SPIGOT_MORTAR = '\u24bc';
	public static final char CHARICON_RIFLE = '\u24bd';
	public static final char CHARICON_HELMET = '\u24be';
	public static final char CHARICON_CHESTPLATE = '\u24bf';
	public static final char CHARICON_LEGGINGS = '\u24c0';
	public static final char CHARICON_BOOTS = '\u24c1';

	//--- Paths ---//
	public static final ResLoc RES_II = ResLoc.root(ImmersiveIntelligence.MODID);
	public static final ResLoc RES_IE = ResLoc.root(ImmersiveEngineering.MODID);
	public static final ResLoc RES_MC = ResLoc.root("minecraft");

	//Models
	public static final ResLoc RES_ITEM_MODEL = ResLoc.of(RES_II, "models/item/");
	public static final ResLoc RES_BLOCK_MODEL = ResLoc.of(RES_II, "models/block/");
	public static final ResLoc RES_ENTITY_MODEL = ResLoc.of(RES_II, "models/entity/");
	public static final ResLoc RES_PARTICLE_MODEL = ResLoc.of(RES_II, "models/particle/");

	//AABBs and Multiblock Config
	public static final ResLoc RES_AABB = ResLoc.of(RES_II, "aabb/");

	//Particles
	public static final ResLoc RES_PARTICLES = ResLoc.of(RES_II, "particles/");

	//Sounds
	public static final ResLoc RES_SOUND = ResLoc.of(RES_II, "sounds/");

	//Textures
	public static final ResLoc RES_TEXTURES = ResLoc.of(RES_II, "textures/");
	public static final ResLoc RES_TEXTURES_GUI = ResLoc.of(RES_TEXTURES, "gui/");
	public static final ResLoc RES_TEXTURES_GUI_TABS = ResLoc.of(RES_TEXTURES_GUI, "tabs/");
	public static final ResLoc RES_TEXTURES_CREATIVE = ResLoc.of(RES_TEXTURES_GUI, "creative_gui/");
	public static final ResLoc RES_TEXTURES_MANUAL = ResLoc.of(RES_TEXTURES_GUI, "manual/");
	public static final ResLoc RES_TEXTURES_SKIN = ResLoc.of(RES_TEXTURES, "skins/");
	public static final ResLoc RES_TEXTURES_ITEM = ResLoc.of(RES_TEXTURES, "items/");
	public static final ResLoc RES_TEXTURES_BLOCK = ResLoc.of(RES_TEXTURES, "blocks/");
	@Deprecated
	public static final String SKIN_LOCATION = "immersiveintelligence:textures/skins/";

	public static final ResLoc RES_CONTEXT_DATA_CALLBACK = ResLoc.of(IIReference.RES_II, "gui/data_types/context/callback");
	public static final ResLoc RES_CONTEXT_DATA_IN = ResLoc.of(IIReference.RES_II, "gui/data_types/context/input");
	public static final ResLoc RES_CONTEXT_DATA_OUT = ResLoc.of(IIReference.RES_II, "gui/data_types/context/output");
	public static final ResLoc RES_CONTEXT_DATA_EVENT = ResLoc.of(IIReference.RES_II, "gui/data_types/context/event");

	//TODO: 03.08.2025 move Deco textures to a separate class and automate registration
	//Deco Base
	public static final ResLoc RES_TEXTURES_DECO = ResLoc.of(RES_II, "gui/deco/");
	public static final ResLoc RES_TEXTURES_DECO_BACKGROUND = ResLoc.of(RES_TEXTURES_DECO, "background/");
	//Deco Backgrounds
	public static final ResLoc GUI_BG_WOODEN = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "wooden");
	public static final ResLoc GUI_BG_STEEL = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "steel");
	public static final ResLoc GUI_BG_STEEL_ROUGH = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "steel_rough");
	public static final ResLoc GUI_BG_SHEETMETAL = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "sheetmetal");
	public static final ResLoc GUI_BG_SHEETMETAL_STEEL = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "sheetmetal_steel");
	public static final ResLoc GUI_BG_PAPER = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "paper");
	public static final ResLoc GUI_BG_BLUEPRINT = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "blueprint");
	public static final ResLoc GUI_BG_DARK = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "dark");
	public static final ResLoc GUI_BG_DARK_TANK = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "dark_tank");
	public static final ResLoc RES_TEXTURES_DECO_FRAME = ResLoc.of(RES_TEXTURES_DECO, "frame/");
	public static final ResLoc GUI_FRAME_WOODEN_THIN = ResLoc.of(RES_TEXTURES_DECO_FRAME, "wooden_thin");
	public static final ResLoc GUI_FRAME_STEEL_THIN = ResLoc.of(RES_TEXTURES_DECO_FRAME, "steel_thin");
	public static final ResLoc GUI_FRAME_CORNERS_BRASS = ResLoc.of(RES_TEXTURES_DECO_FRAME, "corners_brass");
	public static final ResLoc GUI_FRAME_CORNERS_SILVER = ResLoc.of(RES_TEXTURES_DECO_FRAME, "corners_silver");
	//Deco Templates
	public static final ResLoc RES_TEXTURES_DECO_TEMPLATE_ROUND = ResLoc.of(RES_TEXTURES_DECO, "template/round");
	public static final ResLoc RES_TEXTURES_DECO_TEMPLATE_SQUARE = ResLoc.of(RES_TEXTURES_DECO, "template/square");
	public static final ResLoc RES_TEXTURES_DECO_TEMPLATE_TICKET = ResLoc.of(RES_TEXTURES_DECO, "template/ticket");
	public static final ResLoc RES_TEXTURES_DECO_TEMPLATE_PAPER = ResLoc.of(RES_TEXTURES_DECO, "template/paper");
	//Deco Inventory Slots
	public static final ResLoc RES_TEXTURES_DECO_INVENTORY_SLOT = ResLoc.of(RES_TEXTURES_DECO, "slot/vanilla");
	public static final ResLoc RES_TEXTURES_DECO_IE_SLOT = ResLoc.of(RES_TEXTURES_DECO, "slot/steel");
	public static final ResLoc RES_TEXTURES_DECO_IE_SLOT_MARKER = ResLoc.of(RES_TEXTURES_DECO, "slot/steel_marker");
	public static final ResLoc RES_TEXTURES_DECO_IE_BRASS_SLOT = ResLoc.of(RES_TEXTURES_DECO, "slot/brass");
	public static final ResLoc RES_TEXTURES_DECO_IE_BRASS_SLOT_MARKER = ResLoc.of(RES_TEXTURES_DECO, "slot/brass_marker");
	//Deco Labels
	public static final ResLoc GUI_LABEL_WOODEN = ResLoc.of(RES_TEXTURES_DECO, "label/label_wooden");
	public static final ResLoc GUI_LABEL_STEEL = ResLoc.of(RES_TEXTURES_DECO, "label/label_steel");
	public static final ResLoc GUI_LABEL_STEEL_ROUGH = ResLoc.of(RES_TEXTURES_DECO, "label/label_steel_rough");
	public static final ResLoc GUI_LABEL_HAZARD = ResLoc.of(RES_TEXTURES_DECO, "label/label_hazard");
	//Deco Components
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_BUTTON = ResLoc.of(RES_TEXTURES_DECO, "component/button");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_TEXT_FIELD = ResLoc.of(RES_TEXTURES_DECO, "component/text_field");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_TAB = ResLoc.of(RES_TEXTURES_DECO, "component/tab");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_TAB_WIDGET = ResLoc.of(RES_TEXTURES_DECO, "component/tab_widget");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_CHECKBOX = ResLoc.of(RES_TEXTURES_DECO, "component/checkbox");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_SWITCH = ResLoc.of(RES_TEXTURES_DECO, "component/switch");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_SWITCH_MOVING = ResLoc.of(RES_TEXTURES_DECO, "component/switch_moving");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_DROPDOWN_SYMBOL = ResLoc.of(RES_TEXTURES_DECO, "component/dropdown");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_DROPDOWN_DATA_LETTER = ResLoc.of(RES_TEXTURES_DECO, "component/data_letter_dropdown");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_SLIDER = ResLoc.of(RES_TEXTURES_DECO, "component/slider");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_ARROWS = ResLoc.of(RES_TEXTURES_DECO, "component/arrows");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_FRAME = ResLoc.of(RES_TEXTURES_DECO, "component/frame");
	public static final ResLoc RES_TEXTURES_DECO_BAR_ICON_BACKGROUND = ResLoc.of(RES_TEXTURES_DECO, "component/bar_icon_background");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_TANK = ResLoc.of(RES_TEXTURES_DECO, "component/tank");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_TANK_DUST = ResLoc.of(RES_TEXTURES_DECO, "component/dust");

	//Deco Icons
	public static final ResLoc RES_TEXTURES_DECO_ICON = ResLoc.of(RES_TEXTURES_DECO, "icons/");
	public static final ResLoc RES_TEXTURES_DECO_ICON_ACTION_ADD = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_add");
	public static final ResLoc RES_TEXTURES_DECO_ICON_ACTION_REMOVE = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_remove");
	public static final ResLoc RES_TEXTURES_DECO_ICON_ACTION_EDIT = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_edit");
	public static final ResLoc RES_TEXTURES_DECO_ICON_ACTION_DUPLICATE = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_duplicate");
	public static final ResLoc RES_TEXTURES_DECO_ICON_ACTION_CLEAR = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_clear");
	public static final ResLoc[] RES_ACTION_ICONS = new ResLoc[]{
			RES_TEXTURES_DECO_ICON, RES_TEXTURES_DECO_ICON_ACTION_ADD,
			RES_TEXTURES_DECO_ICON_ACTION_REMOVE, RES_TEXTURES_DECO_ICON_ACTION_EDIT,
			RES_TEXTURES_DECO_ICON_ACTION_DUPLICATE, RES_TEXTURES_DECO_ICON_ACTION_CLEAR
	};

	//Deco Bar Icons
	public static final ResLoc RES_ICON_ENERGY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_energy");
	public static final ResLoc RES_ICON_ENERGY_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_energy_input");
	public static final ResLoc RES_ICON_ENERGY_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_energy_output");

	public static final ResLoc RES_ICON_AIR_PRESSURE = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_air_pressure");
	public static final ResLoc RES_ICON_AIR_PRESSURE_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_air_pressure_input");
	public static final ResLoc RES_ICON_AIR_PRESSURE_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_air_pressure_output");

	public static final ResLoc RES_ICON_HEAT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_heat");
	public static final ResLoc RES_ICON_HEAT_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_heat_input");
	public static final ResLoc RES_ICON_HEAT_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_heat_output");

	public static final ResLoc RES_ICON_MECH_SPEED = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_speed");
	public static final ResLoc RES_ICON_MECH_SPEED_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_speed_input");
	public static final ResLoc RES_ICON_MECH_SPEED_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_speed_output");

	public static final ResLoc RES_ICON_MECH_TORQUE = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_torque");
	public static final ResLoc RES_ICON_MECH_TORQUE_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_torque_input");
	public static final ResLoc RES_ICON_MECH_TORQUE_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_torque_output");

	public static final ResLoc RES_ICON_STRUCTURAL_INTEGRITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_structural_integrity");
	public static final ResLoc RES_ICON_ARMOR_INTEGRITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_armor_integrity");
	public static final ResLoc RES_ICON_EXREAC_ARMOR_INTEGRITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_exreac_armor_integrity");

	public static final ResLoc RES_ICON_SOIL_FERTILITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_soil_fertility");

	public static final ResLoc RES_ICON_PROGRESS = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_progress");
	public static final ResLoc RES_ICON_SPEED = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_speed");
	public static final ResLoc RES_ICON_TIME = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_time");
	public static final ResLoc RES_ICON_FUEL = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_fuel");
	public static final ResLoc[] RES_ICONS = new ResLoc[]{
			RES_ICON_ENERGY, RES_ICON_ENERGY_INPUT, RES_ICON_ENERGY_OUTPUT,
			RES_ICON_AIR_PRESSURE, RES_ICON_AIR_PRESSURE_INPUT, RES_ICON_AIR_PRESSURE_OUTPUT,
			RES_ICON_HEAT, RES_ICON_HEAT_INPUT, RES_ICON_HEAT_OUTPUT,
			RES_ICON_MECH_SPEED, RES_ICON_MECH_SPEED_INPUT, RES_ICON_MECH_SPEED_OUTPUT,
			RES_ICON_MECH_TORQUE, RES_ICON_MECH_TORQUE_INPUT, RES_ICON_MECH_TORQUE_OUTPUT,
			RES_ICON_STRUCTURAL_INTEGRITY, RES_ICON_ARMOR_INTEGRITY, RES_ICON_EXREAC_ARMOR_INTEGRITY,
			RES_ICON_SOIL_FERTILITY,
			RES_ICON_PROGRESS, RES_ICON_SPEED, RES_ICON_TIME,
			RES_ICON_FUEL
	};

	//Custom Deco Component Textures
	public static final ResLoc RES_TEXTURES_DECO_BUTTON_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/button_paper");
	public static final ResLoc RES_TEXTURES_DECO_BUTTON_PAPER_HIGHLIGHT = ResLoc.of(RES_TEXTURES_DECO, "component/button_paper_highlight");
	public static final ResLoc RES_TEXTURES_DECO_BUTTON_HANGING = ResLoc.of(RES_TEXTURES_DECO, "component/button_hanging");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_ARROWS_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/arrows_paper");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_BUTTON_ROUND = ResLoc.of(RES_TEXTURES_DECO, "component/button_round");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_SLIDER_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/slider_paper");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_DROPDOWN_DATA_LETTER_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/data_letter_dropdown_paper");
	public static final ResLoc RES_TEXTURES_DECO_COMPONENT_DROPDOWN_SYMBOL_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/dropdown_paper");


	//--- GameRules ---//
	public static final String GAMERULE_AMMO_BREAKS_BLOCKS = "ammoBreaksBlocks";
	public static final String GAMERULE_AMMO_EXPLODES_BLOCKS = "ammoExplodesBlocks";
	public static final String GAMERULE_AMMO_RICOCHETS = "ammoRicochets";
	public static final String GAMERULE_AMMO_DECAY = "ammoDecay";
	public static final String GAMERULE_AMMO_SLOWMO = "ammoSlowmo";
	//public static final String GAMERULE_AMMO_NUCLEAR_DEVICE = "ammoNuclearDevice";

	public static final String GAMERULE_HANS_INFINITE_AMMO = "hansInfiniteAmmo";


	//--- Manual ---//

	public static final String CAT_DATA = "ii_data";
	public static final String CAT_WARFARE = "ii_warfare";
	public static final String CAT_LOGISTICS = "ii_logistics";
	public static final String CAT_INTELLIGENCE = "ii_intel";
	public static final String CAT_MOTORWORKS = "ii_motorworks";
	public static final String CAT_OTHER = "ii_other";

	//--- GUI ---//

	public static final IIColor COLOR_POWERBAR1 = IIColor.fromPackedRGB(0xb51500);
	public static final IIColor COLOR_POWERBAR2 = IIColor.fromPackedRGB(0x600b00);
	public static final IIColor COLOR_ARMORBAR1 = IIColor.fromPackedRGB(0xcfcfcf);
	public static final IIColor COLOR_ARMORBAR2 = IIColor.fromPackedRGB(0xfcfcfc);

	public static final IIColor COLOR_H1 = IIColor.fromPackedRGB(0x0a0a0a);
	public static final IIColor COLOR_H2 = IIColor.fromPackedRGB(0x1a1a1a);
	public static final IIColor COLOR_GUI_BRASS = IIColor.fromPackedRGB(0xd99747);

	public static final IIColor COLOR_ENGINEERS_BLUE = IIColor.fromPackedRGB(0x486c94);
	public static final IIColor COLOR_IMMERSIVE_ORANGE = IIColor.fromPackedRGB(0xf78034);
	public static final IIColor COLOR_GOLD = IIColor.fromPackedRGB(0xFFAA00);
	public static final IIColor COLOR_LIGHT_BLUE = IIColor.fromPackedRGB(0x5555FF);

	public static final IIColor COLOR_SWITCH_ON = COLOR_IMMERSIVE_ORANGE;
	public static final IIColor COLOR_SWITCH_OFF = IIColor.fromPackedRGB(0x8c8c8c);

	public static final IIColor COLOR_NIXIE_ORANGE = IIColor.fromPackedRGB(Lib.colour_nixieTubeText);
	/**
	 * Standard color palette for use in GUI tooltips
	 */
	public static final IIColor[] COLORS_STANDARD = new IIColor[]{COLOR_ENGINEERS_BLUE, COLOR_IMMERSIVE_ORANGE, COLOR_GOLD, COLOR_LIGHT_BLUE};
}
