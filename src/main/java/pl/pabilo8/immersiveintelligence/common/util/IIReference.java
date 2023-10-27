package pl.pabilo8.immersiveintelligence.common.util;

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

	public static final String DESC_HOLD_CTRL = DESCRIPTION_KEY+"info.holdControl";
	public static final String DESC_HOLD_SHIFT = DESCRIPTION_KEY+"info.holdShift";
	public static final String DESC_HOLD_ALT = DESCRIPTION_KEY+"info.holdAlt";
	public static final String DESC_HOLD_TAB = DESCRIPTION_KEY+"info.holdTab";

	//--- Patterns ---//
	public static final ResLoc RES_II = ResLoc.root(ImmersiveIntelligence.MODID);

	//Models
	public static final ResLoc RES_ITEM_MODEL = ResLoc.of(RES_II, "models/item/");
	public static final ResLoc RES_BLOCK_MODEL = ResLoc.of(RES_II, "models/block/");
	public static final ResLoc RES_AABB = ResLoc.of(RES_II, "aabb/");

	//Sounds
	public static final ResLoc RES_SOUND = ResLoc.of(RES_II, "sounds/");

	public static final ResLoc RES_TEXTURES = ResLoc.of(RES_II, "textures/");
	public static final ResLoc RES_TEXTURES_GUI = ResLoc.of(RES_TEXTURES, "gui");
	public static final ResLoc RES_TEXTURES_MANUAL = ResLoc.of(RES_TEXTURES, "gui/manual/");
	public static final ResLoc RES_TEXTURES_SKIN = ResLoc.of(RES_TEXTURES, "skins/");
	public static final ResLoc RES_TEXTURES_ITEM = ResLoc.of(RES_TEXTURES, "items/");

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
