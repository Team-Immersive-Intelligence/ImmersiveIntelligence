package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 29.08.2022
 */
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

	public static final String INFO_KEY_TOOL_DURABILITY = INFO_KEY+"tool_durability";
	public static final String DESC_TOOLUPGRADE = DESCRIPTION_KEY+"toolupgrade.";
	public static final String DESC_BULLETS = DESCRIPTION_KEY+"bullets.";

	public static final String DESC_HOLD_CTRL = DESCRIPTION_KEY+"info.holdControl";
	public static final String DESC_HOLD_SHIFT = DESCRIPTION_KEY+"info.holdShift";
	public static final String DESC_HOLD_ALT = DESCRIPTION_KEY+"info.holdAlt";
	public static final String DESC_HOLD_TAB = DESCRIPTION_KEY+"info.holdTab";

	//--- Font Icons ---//
	public static final char CHARICON_SPEED = '\u29c1'; //speed
	public static final char CHARICON_TORQUE = '\u2296'; //torque
	public static final char CHARICON_ENERGY = '\u2607'; //energy
	public static final char CHARICON_RADIATION = '\u2622'; //nuclear / radiation
	public static final char CHARICON_BULLET = '\u2023'; //bullet
	public static final char CHARICON_BULLET_CONTENTS = '\u29b3'; //bullet contents
	public static final char CHARICON_PENETRATION = '\u29b4'; //penetration
	public static final char CHARICON_SKULL = '\u2295'; //damage / skull
	public static final char CHARICON_CONTACT = '\u29b0'; //contact
	public static final char CHARICON_PROXIMITY = '\u29b1'; //proximity
	public static final char CHARICON_TIMED = '\u29b2'; //timed
	public static final char CHARICON_FOLDER = '\u2348'; //folder
	public static final char CHARICON_MG = '\u24b6'; //mg
	public static final char CHARICON_SMG = '\u24b7'; //smg
	public static final char CHARICON_RAILGUN = '\u24b8'; //railgun
	public static final char CHARICON_REVOLVER = '\u24b9'; //revolver
	public static final char CHARICON_AUTOREVOLVER = '\u24ba'; //autorevolver
	public static final char CHARICON_STG = '\u24bb'; //stg
	public static final char CHARICON_SPIGOT_MORTAR = '\u24bc'; //spigot mortar
	public static final char CHARICON_RIFLE = '\u24bd'; //rifle
	public static final char CHARICON_HELMET = '\u24be'; //helmet
	public static final char CHARICON_CHESTPLATE = '\u24bf'; //chestplate
	public static final char CHARICON_LEGGINGS = '\u24c0'; //leggings
	public static final char CHARICON_BOOTS = '\u24c1'; //boots

	//--- Patterns ---//
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
	public static final ResLoc RES_TEXTURES_CREATIVE = ResLoc.of(RES_TEXTURES_GUI, "creative_gui/");
	public static final ResLoc RES_TEXTURES_MANUAL = ResLoc.of(RES_TEXTURES_GUI, "/manual/");
	public static final ResLoc RES_TEXTURES_SKIN = ResLoc.of(RES_TEXTURES, "skins/");
	public static final ResLoc RES_TEXTURES_ITEM = ResLoc.of(RES_TEXTURES, "items/");
	public static final ResLoc RES_TEXTURES_BLOCK = ResLoc.of(RES_TEXTURES, "blocks/");

	//--- GameRules ---//
	public static final String GAMERULE_AMMO_BREAKS_BLOCKS = "ammoBreaksBlocks";
	public static final String GAMERULE_AMMO_EXPLODES_BLOCKS = "ammoExplodesBlocks";
	public static final String GAMERULE_AMMO_RICOCHETS = "ammoRicochets";
	public static final String GAMERULE_AMMO_DECAY = "ammoDecay";
	public static final String GAMERULE_AMMO_SLOWMO = "ammoSlowmo";
	//public static final String GAMERULE_AMMO_NUCLEAR_DEVICE = "ammoNuclearDevice";

	public static final String GAMERULE_HANS_INFINITE_AMMO = "hansInfiniteAmmo";

	//--- Paths ---//
	public static final String SKIN_LOCATION = "immersiveintelligence:textures/skins/";


	//--- Manual ---//

	public static final String CAT_DATA = "ii_data";
	public static final String CAT_WARFARE = "ii_warfare";
	public static final String CAT_LOGISTICS = "ii_logi";
	public static final String CAT_INTELLIGENCE = "ii_intel";
	public static final String CAT_MOTORWORKS = "ii_motorworks";

	//--- GUI ---//

	public static final int COLOR_POWERBAR_1 = 0xffb51500;
	public static final int COLOR_POWERBAR_2 = 0xff600b00;
	public static final int COLOR_ARMORBAR_1 = 0xcfcfcfcf;
	public static final int COLOR_ARMORBAR_2 = 0x0cfcfcfc;
	public static final int COLOR_H1 = 0x0a0a0a;
	public static final int COLOR_H2 = 0x1a1a1a;

	public static final int[] COLORS_HIGHLIGHT_I = new int[]{
			0x486c94, //prussian blue
			0xf78034, //immersive orange
			0xFFAA00, //gold
			0x5555FF //light blue
	};

	//why copy if you can *process*
	public static final String[] COLORS_HIGHLIGHT_S = Arrays.stream(COLORS_HIGHLIGHT_I)
			.mapToObj(Integer::toHexString).toArray(String[]::new);
}
