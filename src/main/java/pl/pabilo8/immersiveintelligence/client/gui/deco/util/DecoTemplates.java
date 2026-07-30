package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxStorage;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.DecoComponentTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementSorter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar.BarTooltipFormat;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IDamageResistantMultiblock;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Templates to be applied to {@link pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent Deco Components}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 13.02.2026
 */
@SideOnly(Side.CLIENT)
public class DecoTemplates
{
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON = component -> component
			.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER_HIGHLIGHT)
			.withPadding(0, 0, 0, 0)
			.withSize(14, 14);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_CLEAR = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_CLEAR)
					.withIcon(DecoTextures.ICON_ACTION_CLEAR)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.clear")
	);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_DUPLICATE = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_DUPLICATE)
					.withIcon(DecoTextures.ICON_ACTION_DUPLICATE)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.duplicate")
	);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_ADD = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_ADD)
					.withIcon(DecoTextures.ICON_ACTION_ADD)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.add")
	);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_REMOVE = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_REMOVE)
					.withIcon(DecoTextures.ICON_ACTION_REMOVE)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.remove")
	);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_EDIT = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_EDIT)
					.withIcon(DecoTextures.ICON_ACTION_EDIT)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.edit")
	);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_ACCEPT = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_ACCEPT)
					.withIcon(DecoTextures.ICON_ACTION_ACCEPT)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.accept")
	);
	public static final DecoComponentTemplate<DecoButton> ACTION_BUTTON_REJECT = ACTION_BUTTON.and(
			component -> component
					.withBackgroundColor(DecoColors.ACTION_REJECT)
					.withIcon(DecoTextures.ICON_ACTION_REJECT)
					.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"button.reject")
	);

	//--- Mechanical Torque Bar ---//
	public static final Function<IRotaryEnergy, DecoComponentTemplate<DecoBar>> BAR_MECH_TORQUE =
			rotaryEnergy -> component -> component
					.withColors(DecoColors.TORQUE1, DecoColors.TORQUE2)
					.withIconLocation(DecoTextures.ICON_MECH_TORQUE)
					.withValueTooltip("mech_torque.stored", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
					.withLimits(0, 100, () -> (int)rotaryEnergy.getTorque())
					.withSmoothAnimation();
	public static final Function<IRotaryEnergy, DecoComponentTemplate<DecoBar>> BAR_MECH_TORQUE_OUTPUT =
			rotaryEnergy -> component -> component
					.withTemplate(BAR_MECH_TORQUE.apply(rotaryEnergy))
					.withIconLocation(DecoTextures.ICON_MECH_TORQUE_OUTPUT)
					.withValueTooltip("mech_torque.output", BarTooltipFormat.VALUE, TextFormatting.GOLD)
					.withLimits(0, 100, () -> (int)rotaryEnergy.getOutputTorque());
	public static final Function<IRotaryEnergy, DecoComponentTemplate<DecoBar>> BAR_MECH_TORQUE_INPUT =
			rotaryEnergy -> component -> component
					.withTemplate(BAR_MECH_TORQUE.apply(rotaryEnergy))
					.withIconLocation(DecoTextures.ICON_MECH_TORQUE_INPUT)
					.withValueTooltip("mech_torque.input", BarTooltipFormat.VALUE, TextFormatting.GOLD);

	//--- Mechanical Speed Bar ---//
	public static final Function<IRotaryEnergy, DecoComponentTemplate<DecoBar>> BAR_MECH_SPEED =
			rotaryEnergy -> component -> component
					.withColors(DecoColors.SPEED1, DecoColors.SPEED2)
					.withIconLocation(DecoTextures.ICON_MECH_SPEED)
					.withValueTooltip("mech_speed.stored", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
					.withLimits(0, 720, () -> (int)rotaryEnergy.getRotationSpeed())
					.withSmoothAnimation();
	public static final Function<IRotaryEnergy, DecoComponentTemplate<DecoBar>> BAR_MECH_SPEED_OUTPUT =
			rotaryEnergy -> component -> component
					.withTemplate(BAR_MECH_SPEED.apply(rotaryEnergy))
					.withIconLocation(DecoTextures.ICON_MECH_SPEED_OUTPUT)
					.withValueTooltip("mech_speed.output", BarTooltipFormat.VALUE, TextFormatting.GOLD)
					.withLimits(0, 720, () -> (int)rotaryEnergy.getOutputRotationSpeed());
	public static final Function<IRotaryEnergy, DecoComponentTemplate<DecoBar>> BAR_MECH_SPEED_INPUT =
			rotaryEnergy -> component -> component
					.withTemplate(BAR_MECH_SPEED.apply(rotaryEnergy))
					.withIconLocation(DecoTextures.ICON_MECH_SPEED_INPUT)
					.withValueTooltip("mech_speed.input", BarTooltipFormat.VALUE, TextFormatting.GOLD);

	//--- Armor ---//
	public static final DecoComponentTemplate<DecoBar> BAR_ARMOR_INTEGRITY = component -> component
			.withColors(DecoColors.ARMOR_INTEGRITY_1, DecoColors.ARMOR_INTEGRITY_2)
			.withIconLocation(DecoTextures.ICON_ARMOR_INTEGRITY)
			.withValueTooltip("armor_integrity", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
			.withSmoothAnimation();
	public static final DecoComponentTemplate<DecoBar> BAR_REACTIVE_ARMOR_INTEGRITY = component -> component
			.withColors(DecoColors.REACTIVE_ARMOR_INTEGRITY_1, DecoColors.REACTIVE_ARMOR_INTEGRITY_2)
			.withIconLocation(DecoTextures.ICON_ARMOR_INTEGRITY)
			.withValueTooltip("reactive_armor_integrity", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
			.withSmoothAnimation();
	public static final DecoComponentTemplate<DecoBar> BAR_STRUCTURAL_INTEGRITY_BASE = component -> component
			.withColors(DecoColors.STRUCTURAL_INTEGRITY_1, DecoColors.STRUCTURAL_INTEGRITY_2)
			.withIconLocation(DecoTextures.ICON_STRUCTURAL_INTEGRITY)
			.withValueTooltip("structural_integrity", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
			.withSmoothAnimation();
	public static final Function<IDamageResistantMultiblock, DecoComponentTemplate<DecoBar>> BAR_STRUCTURAL_INTEGRITY = mb -> component -> component
			.withTemplate(BAR_STRUCTURAL_INTEGRITY_BASE)
			.withLimits(0, (int)mb.getMaxHealth(), () -> ((int)mb.getHealth()));

	//--- Energy Bar ---//
	public static final DecoComponentTemplate<DecoBar> BAR_ELECTRIC_ENERGY_BASE = component -> component
			.withColors(DecoColors.POWER1, DecoColors.POWER2)
			.withValueTooltip("energy.stored", BarTooltipFormat.VALUE_TO_MAX, TextFormatting.GOLD)
			.withIconLocation(DecoTextures.ICON_ENERGY);
	public static final DecoComponentTemplate<DecoBar> BAR_ELECTRIC_ENERGY_OUTPUT = component -> component
			.withTemplate(BAR_ELECTRIC_ENERGY_BASE)
			.withIconLocation(DecoTextures.ICON_ENERGY_OUTPUT)
			.withValueTooltip("energy.output", BarTooltipFormat.VALUE, TextFormatting.GOLD);
	public static final DecoComponentTemplate<DecoBar> BAR_ELECTRIC_ENERGY_INPUT = component -> component
			.withTemplate(BAR_ELECTRIC_ENERGY_BASE)
			.withIconLocation(DecoTextures.ICON_ENERGY_INPUT)
			.withValueTooltip("energy.input", BarTooltipFormat.VALUE, TextFormatting.GOLD);
	public static final Function<IFluxStorage, DecoComponentTemplate<DecoBar>> BAR_ELECTRIC_ENERGY = energyStorage -> component -> component
			.withTemplate(BAR_ELECTRIC_ENERGY_BASE)
			.withLimits(0, energyStorage.getMaxEnergyStored(), energyStorage::getEnergyStored);

	public static final DecoComponentTemplate<DecoDropdown<EnumDyeColor>> DYE_COLOR_DROPDOWN = component -> component
			.withEntries(EnumDyeColor.values())
			.withDisplayFunction(new DecoEntryPanelBuilder<EnumDyeColor>()
					.withBackground(DecoTextures.BG_STEEL)
					.withHeight(12)
					.withComponent("icon", p -> new DecoImage(2, 1)
							.withSize(8, 8)
							.withImageLocation(DecoTextures.COMPONENT_COLOR, true)
							.withUV(16, 4, 4, 12, 12)
					)
					.withLabel("label", p -> new DecoLabel(IIClientUtils.fontRegular, 12, 1)
							.withSize(48, 12)
							.withAlign(DecoAlignment.LEFT)
							.withText("Core")
					)
					.withElementApplyMethod((dye, builder) -> {
						builder.component("icon", DecoImage.class).withColor(IIColor.fromDye(dye));
						builder.label("label").withText("item.fireworksCharge."+dye.getUnlocalizedName());
					})
			);

	public static DecoEntryPanelBuilder<TypeMetaInfo<?>> getDataTypeEntryDisplay()
	{
		return new DecoEntryPanelBuilder<TypeMetaInfo<?>>()
				.withHeight(18)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				//Type Icon, Label, and Letter
				.withComponent("image", p -> new DecoImage(3, 1)
						.withSize(16, 16))
				.withLabel("typeLabel", p -> new DecoLabel(IIClientUtils.fontRegular, 20, 1)
						.withSize(48, 18)
						.withAlign(DecoAlignment.LEFT)
						.withText("Integer")
				)
				.withElementApplyMethod((typeMeta, panel) -> {
					//type label (f.e. integer)
					panel.label("typeLabel")
							.withText(typeMeta.getTranslatedName())
							.withTextColor(typeMeta.color.withBrightness(0.4f));
					//type icon
					panel.component("image", DecoImage.class)
							.withImageLocation(typeMeta.getTextureLocation(), true);
				})
				.withElementTooltip(TypeMetaInfo::getTranslatedName);
	}

	public static DecoElementSorter<TypeMetaInfo<?>> getDataTypeEntrySorter()
	{
		return new DecoElementSorter<TypeMetaInfo<?>>()
		{
			@Override
			public List<TypeMetaInfo<?>> sort(List<TypeMetaInfo<?>> elements)
			{
				return elements;
			}

			@Nonnull
			@Override
			public List<TypeMetaInfo<?>> autocomplete(List<TypeMetaInfo<?>> elements, String input)
			{
				return elements.stream()
						.filter(e -> e.getTranslatedName().toLowerCase().contains(input.toLowerCase()))
						.collect(Collectors.toList());
			}
		};
	}
}
