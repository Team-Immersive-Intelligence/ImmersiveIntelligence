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

	//--- NBT Keys ---//
	public static final String NBT_DAMAGE = Lib.NBT_DAMAGE.toLowerCase();

	//--- Language Keys ---//
	public static final String DESCRIPTION_KEY = "desc.immersiveintelligence.";
	public static final String INFO_KEY = "info.immersiveintelligence.";
	public static final String DATA_KEY = "datasystem.immersiveintelligence.";
	public static final String ROTARY_KEY = "rotary.immersiveintelligence.";
	public static final String BLOCK_KEY = "tile.immersiveintelligence.";
	public static final String GUI_LABEL_KEY = "ii.gui.";
	public static final String GUI_TOOLTIP_KEY = "ii.gui_tooltip.";
	public static final String GUI_HELPBOX_LABEL_KEY = "ii.gui_help.";

	public static final String INFO_KEY_SPEED_IN = GUI_TOOLTIP_KEY+"mech_speed.input";
	public static final String INFO_KEY_SPEED_OUT = GUI_TOOLTIP_KEY+"mech_speed.output";
	public static final String INFO_KEY_SPEED_BOTH = GUI_TOOLTIP_KEY+"mech_speed.stored";

	public static final String INFO_KEY_TORQUE_IN = GUI_TOOLTIP_KEY+"mech_torque.input";
	public static final String INFO_KEY_TORQUE_OUT = GUI_TOOLTIP_KEY+"mech_torque.output";
	public static final String INFO_KEY_TORQUE_BOTH = GUI_TOOLTIP_KEY+"mech_torque.stored";

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
	public static final ResLoc RES_TEXTURES_GUI = RES_TEXTURES.with("gui/");
	public static final ResLoc RES_TEXTURES_CREATIVE = RES_TEXTURES_GUI.with("creative_gui/");
	public static final ResLoc RES_TEXTURES_MANUAL = RES_TEXTURES_GUI.with("manual/");
	public static final ResLoc RES_TEXTURES_SKIN = RES_TEXTURES.with("skins/");
	public static final ResLoc RES_TEXTURES_ITEM = RES_TEXTURES.with("items/");
	public static final ResLoc RES_TEXTURES_BLOCK = RES_TEXTURES.with("blocks/");
	@Deprecated
	public static final String SKIN_LOCATION = "immersiveintelligence:textures/skins/";

	public static final ResLoc RES_CONTEXT_DATA_CALLBACK = ResLoc.of(IIReference.RES_II, "gui/data_types/context/callback");
	public static final ResLoc RES_CONTEXT_DATA_IN = ResLoc.of(IIReference.RES_II, "gui/data_types/context/input");
	public static final ResLoc RES_CONTEXT_DATA_OUT = ResLoc.of(IIReference.RES_II, "gui/data_types/context/output");
	public static final ResLoc RES_CONTEXT_DATA_EVENT = ResLoc.of(IIReference.RES_II, "gui/data_types/context/event");

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
