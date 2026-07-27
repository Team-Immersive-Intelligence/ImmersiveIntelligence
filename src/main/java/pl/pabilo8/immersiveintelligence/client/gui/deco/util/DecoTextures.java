package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.lang.reflect.Field;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.08.2025
 */
public class DecoTextures
{
	//--- Special Textures ---//
	public static final ResLoc TEXTURE_WHITE = IIReference.RES_IE.with("items/white");

	//--- Base Directories ---//
	public static final ResLoc RES_TEXTURES_DECO = ResLoc.of(IIReference.RES_II, "gui/deco/");
	public static final ResLoc RES_TEXTURES_DECO_BACKGROUND = ResLoc.of(RES_TEXTURES_DECO, "background/");

	//--- Backgrounds ---//
	public static final ResLoc BG_WOODEN = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "wooden");
	public static final ResLoc BG_STEEL = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "steel");
	public static final ResLoc BG_STEEL_ROUGH = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "steel_rough");
	public static final ResLoc BG_ALUMINIUM = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "aluminium");
	public static final ResLoc BG_PAPER = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "paper");
	public static final ResLoc BG_BLUEPRINT = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "blueprint");
	public static final ResLoc BG_BRICKS = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "bricks");
	public static final ResLoc BG_CONCRETE = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "concrete");
	public static final ResLoc BG_SANDBAGS = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "sandbags");
	public static final ResLoc BG_DARK = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "dark");
	public static final ResLoc BG_DARK_TANK = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "dark_tank");
	public static final ResLoc BG_VANILLA = ResLoc.of(RES_TEXTURES_DECO_BACKGROUND, "vanilla");

	//--- Frames ---//
	public static final ResLoc RES_TEXTURES_DECO_FRAME = ResLoc.of(RES_TEXTURES_DECO, "frame/");
	public static final ResLoc FRAME_CORNERS_SILVER = ResLoc.of(RES_TEXTURES_DECO_FRAME, "corners_silver");
	public static final ResLoc FRAME_CORNERS_BRASS = ResLoc.of(RES_TEXTURES_DECO_FRAME, "corners_brass");
	public static final ResLoc FRAME_STEEL_THIN = ResLoc.of(RES_TEXTURES_DECO_FRAME, "steel_thin");
	public static final ResLoc FRAME_STEEL = ResLoc.of(RES_TEXTURES_DECO_FRAME, "steel");
	public static final ResLoc FRAME_WOODEN_THIN = ResLoc.of(RES_TEXTURES_DECO_FRAME, "wooden_thin");
	public static final ResLoc FRAME_PAPER = ResLoc.of(RES_TEXTURES_DECO_FRAME, "manual");

	//--- Templates ---//
	public static final ResLoc TEMPLATE_ROUND = ResLoc.of(RES_TEXTURES_DECO, "template/round");
	public static final ResLoc TEMPLATE_SQUARE = ResLoc.of(RES_TEXTURES_DECO, "template/square");
	public static final ResLoc TEMPLATE_ROUND_WOODEN = ResLoc.of(RES_TEXTURES_DECO, "template/round_wooden");
	public static final ResLoc TEMPLATE_TICKET = ResLoc.of(RES_TEXTURES_DECO, "template/ticket");
	public static final ResLoc TEMPLATE_PAPER = ResLoc.of(RES_TEXTURES_DECO, "template/paper");

	//--- Inventory Slots ---//
	public static final ResLoc SLOT_VANILLA = ResLoc.of(RES_TEXTURES_DECO, "slot/vanilla");
	public static final ResLoc SLOT_IE = ResLoc.of(RES_TEXTURES_DECO, "slot/steel");
	public static final ResLoc SLOT_IE_MARKER = ResLoc.of(RES_TEXTURES_DECO, "slot/steel_marker");
	public static final ResLoc SLOT_IE_BRASS = ResLoc.of(RES_TEXTURES_DECO, "slot/brass");
	public static final ResLoc SLOT_IE_BRASS_MARKER = ResLoc.of(RES_TEXTURES_DECO, "slot/brass_marker");
	public static final ResLoc SLOT_IE_MANUAL = ResLoc.of(RES_TEXTURES_DECO, "slot/manual");
	public static final ResLoc SLOT_IE_MANUAL_MARKER = ResLoc.of(RES_TEXTURES_DECO, "slot/manual_marker");

	//--- Label Backgrounds ---//
	public static final ResLoc LABEL_WOODEN = ResLoc.of(RES_TEXTURES_DECO, "label/label_wooden");
	public static final ResLoc LABEL_STEEL = ResLoc.of(RES_TEXTURES_DECO, "label/label_steel");
	public static final ResLoc LABEL_ALUMINIUM = ResLoc.of(RES_TEXTURES_DECO, "label/label_aluminium");
	public static final ResLoc LABEL_STEEL_ROUGH = ResLoc.of(RES_TEXTURES_DECO, "label/label_steel_rough");
	public static final ResLoc LABEL_HAZARD = ResLoc.of(RES_TEXTURES_DECO, "label/label_hazard");
	public static final ResLoc LABEL_PAPER = ResLoc.of(RES_TEXTURES_DECO, "label/label_paper");
	public static final ResLoc LABEL_BLUEPRINT = ResLoc.of(RES_TEXTURES_DECO, "label/label_blueprint");
	public static final ResLoc LABEL_VANILLA = ResLoc.of(RES_TEXTURES_DECO, "label/label_vanilla");

	//--- Deco Components ---//
	public static final ResLoc COMPONENT_BUTTON = ResLoc.of(RES_TEXTURES_DECO, "component/button");
	public static final ResLoc COMPONENT_TEXT_FIELD = ResLoc.of(RES_TEXTURES_DECO, "component/text_field");
	public static final ResLoc COMPONENT_TAB = ResLoc.of(RES_TEXTURES_DECO, "component/tab");
	public static final ResLoc COMPONENT_TAB_VERTICAL = ResLoc.of(RES_TEXTURES_DECO, "component/tab_vertical");
	public static final ResLoc COMPONENT_TAB_WIDGET = ResLoc.of(RES_TEXTURES_DECO, "component/tab_widget");
	public static final ResLoc COMPONENT_CHECKBOX = ResLoc.of(RES_TEXTURES_DECO, "component/checkbox");
	public static final ResLoc COMPONENT_SWITCH = ResLoc.of(RES_TEXTURES_DECO, "component/switch");
	public static final ResLoc COMPONENT_SWITCH_MOVING = ResLoc.of(RES_TEXTURES_DECO, "component/switch_moving");
	public static final ResLoc COMPONENT_DROPDOWN_SYMBOL = ResLoc.of(RES_TEXTURES_DECO, "component/dropdown");
	public static final ResLoc COMPONENT_DROPDOWN_DATA_LETTER = ResLoc.of(RES_TEXTURES_DECO, "component/data_letter_dropdown");
	public static final ResLoc COMPONENT_SLIDER = ResLoc.of(RES_TEXTURES_DECO, "component/slider");
	public static final ResLoc COMPONENT_SLIDER_BAR = ResLoc.of(RES_TEXTURES_DECO, "component/slider_bar");
	public static final ResLoc COMPONENT_ARROWS = ResLoc.of(RES_TEXTURES_DECO, "component/arrows");
	public static final ResLoc COMPONENT_FRAME = ResLoc.of(RES_TEXTURES_DECO, "component/frame");
	public static final ResLoc BAR_ICON_BACKGROUND = ResLoc.of(RES_TEXTURES_DECO, "component/bar_icon_background");
	public static final ResLoc COMPONENT_TANK = ResLoc.of(RES_TEXTURES_DECO, "component/tank");
	public static final ResLoc COMPONENT_TANK_MARKER = ResLoc.of(RES_TEXTURES_DECO, "component/tank_marker");
	public static final ResLoc COMPONENT_TANK_DUST = ResLoc.of(RES_TEXTURES_DECO, "component/dust");
	public static final ResLoc COMPONENT_COLOR = ResLoc.of(RES_TEXTURES_DECO, "component/color");

	//--- Standard Deco Tab Icons ---//
	public static final ResLoc TABS = IIReference.RES_II.with("gui/tab_icons/");
	public static final ResLoc ICON_STORAGE = TABS.with("storage");
	public static final ResLoc ICON_MEMORY = TABS.with("memory");
	public static final ResLoc ICON_VARIABLES = TABS.with("variables");

	public static final ResLoc ICON_TASKS = TABS.with("tasks");
	public static final ResLoc ICON_STATUS = TABS.with("status");
	public static final ResLoc ICON_CONFIG = TABS.with("config");
	public static final ResLoc ICON_TARGETS = TABS.with("targets");
	public static final ResLoc ICON_FIRE_MISSIONS = TABS.with("fire_missions");

	public static final ResLoc ICON_STYLE = TABS.with("style");
	public static final ResLoc ICON_OWNERSHIP = TABS.with("ownership");
	public static final ResLoc ICON_MAP = TABS.with("map");
	public static final ResLoc ICON_FACTION_CONFIG = TABS.with("faction_management");

	//--- Colored 12x Icons ---//
	public static final ResLoc RES_TEXTURES_DECO_ICON = ResLoc.of(RES_TEXTURES_DECO, "icons/");
	public static final ResLoc ICON_FUEL = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_fuel");
	public static final ResLoc ICON_TIME = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_time");
	public static final ResLoc ICON_SPEED = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_speed");
	public static final ResLoc ICON_PROGRESS = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_progress");
	public static final ResLoc ICON_SOIL_FERTILITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_soil_fertility");
	public static final ResLoc ICON_EXREAC_ARMOR_INTEGRITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_exreac_armor_integrity");
	public static final ResLoc ICON_ARMOR_INTEGRITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_armor_integrity");
	public static final ResLoc ICON_STRUCTURAL_INTEGRITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_structural_integrity");
	public static final ResLoc ICON_MECH_TORQUE_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_torque_output");
	public static final ResLoc ICON_MECH_TORQUE_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_torque_input");
	public static final ResLoc ICON_MECH_TORQUE = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_torque");
	public static final ResLoc ICON_MECH_SPEED_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_speed_output");
	public static final ResLoc ICON_MECH_SPEED_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_speed_input");
	public static final ResLoc ICON_MECH_SPEED = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_mech_speed");
	public static final ResLoc ICON_HEAT_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_heat_output");
	public static final ResLoc ICON_HEAT_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_heat_input");
	public static final ResLoc ICON_HEAT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_heat");
	public static final ResLoc ICON_AIR_PRESSURE_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_air_pressure_output");
	public static final ResLoc ICON_AIR_PRESSURE_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_air_pressure_input");
	public static final ResLoc ICON_AIR_PRESSURE = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_air_pressure");
	public static final ResLoc ICON_ENERGY_OUTPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_energy_output");
	public static final ResLoc ICON_ENERGY_INPUT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_energy_input");
	public static final ResLoc ICON_ENERGY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_energy");

	//--- Ammo Icons ---//
	public static final ResLoc ICON_CONTACT = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_fuse_contact");
	public static final ResLoc ICON_TIMED = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_fuse_timed");
	public static final ResLoc ICON_PROXIMITY = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_fuse_proximity");

	//--- Deco Bar Icons ---//
	public static final ResLoc ICON_ACTION_DUPLICATE = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_duplicate");
	public static final ResLoc ICON_ACTION_EDIT = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_edit");
	public static final ResLoc ICON_ACTION_REMOVE = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_remove");
	public static final ResLoc ICON_ACTION_ADD = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_add");
	public static final ResLoc ICON_ACTION_CLEAR = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_clear");
	public static final ResLoc ICON_ACTION_ACCEPT = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_accept");
	public static final ResLoc ICON_ACTION_REJECT = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_reject");
	public static final ResLoc ICON_ACTION_HELP = ResLoc.of(RES_TEXTURES_DECO_ICON, "action_help");

	//--- Custom Deco Component Textures ---//
	public static final ResLoc COMPONENT_BUTTON_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/button_paper");
	public static final ResLoc COMPONENT_BUTTON_PAPER_HIGHLIGHT = ResLoc.of(RES_TEXTURES_DECO, "component/button_paper_highlight");
	public static final ResLoc COMPONENT_BUTTON_HANGING = ResLoc.of(RES_TEXTURES_DECO, "component/button_hanging");
	public static final ResLoc COMPONENT_ARROWS_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/arrows_paper");
	public static final ResLoc COMPONENT_BUTTON_ROUND = ResLoc.of(RES_TEXTURES_DECO, "component/button_round");
	public static final ResLoc COMPONENT_SLIDER_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/slider_paper");
	public static final ResLoc COMPONENT_SLIDER_VANILLA = ResLoc.of(RES_TEXTURES_DECO, "component/slider_vanilla");
	public static final ResLoc COMPONENT_DROPDOWN_DATA_LETTER_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/data_letter_dropdown_paper");
	public static final ResLoc COMPONENT_DROPDOWN_SYMBOL_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/dropdown_paper");
	public static final ResLoc COMPONENT_TANK_PAPER = ResLoc.of(RES_TEXTURES_DECO, "component/tank_manual");

	public static final ResLoc MAP_MARKER_ENTITY = ResLoc.of(RES_TEXTURES_DECO, "map/marker/entity");
	public static final ResLoc MAP_MARKER_VEHICLE = ResLoc.of(RES_TEXTURES_DECO, "map/marker/vehicle");
	public static final ResLoc MAP_MARKER_TRAINCAR = ResLoc.of(RES_TEXTURES_DECO, "map/marker/traincar");
	public static final ResLoc MAP_MARKER_AIRCRAFT = ResLoc.of(RES_TEXTURES_DECO, "map/marker/aircraft");
	public static final ResLoc MAP_MARKER_DRONE = ResLoc.of(RES_TEXTURES_DECO, "map/marker/drone");
	public static final ResLoc MAP_MARKER_BULLET = ResLoc.of(RES_TEXTURES_DECO, "map/marker/bullet");
	public static final ResLoc MAP_MARKER_MISSILE = ResLoc.of(RES_TEXTURES_DECO, "map/marker/missile");
	public static final ResLoc MAP_MARKER_DRAGON = ResLoc.of(RES_TEXTURES_DECO, "map/marker/dragon");
	public static final ResLoc MAP_MARKER_WYRM = ResLoc.of(RES_TEXTURES_DECO, "map/marker/wyrm");

	public static final ResLoc MAP_MARKER_FLAGPOLE = ResLoc.of(RES_TEXTURES_DECO, "map/marker/flagpole");
	public static final ResLoc MAP_MARKER_EMPLACEMENT = ResLoc.of(RES_TEXTURES_DECO, "map/marker/emplacement");
	public static final ResLoc MAP_MARKER_ARTILLERY_HOWITZER = ResLoc.of(RES_TEXTURES_DECO, "map/marker/artillery_howitzer");
	public static final ResLoc MAP_MARKER_MISSILE_SILO = ResLoc.of(RES_TEXTURES_DECO, "map/marker/missile_silo");
	public static final ResLoc MAP_MARKER_RADAR = ResLoc.of(RES_TEXTURES_DECO, "map/marker/radar");
	public static final ResLoc MAP_MARKER_RADIO_STATION = ResLoc.of(RES_TEXTURES_DECO, "map/marker/radio_station");

	public static final ResLoc ICON_INVENTORY_FACTION_INVITES = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_faction_invites");
	public static final ResLoc ICON_INVENTORY_FACTION_INVITES_ACTIVE = ResLoc.of(RES_TEXTURES_DECO_ICON, "icon_faction_invites_active");

	public static void registerAllTextures(TextureMap map)
	{
		for(Field field : DecoTextures.class.getDeclaredFields())
			if(field.getType()==ResLoc.class)
				try
				{
					ResLoc resLoc = (ResLoc)field.get(null);
					if(!resLoc.isDirectory())
						ApiUtils.getRegisterSprite(map, resLoc);
				} catch(IllegalAccessException e)
				{
					IILogger.error("Failed to register texture: "+field.getName(), e);
				}
	}
}
