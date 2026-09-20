package pl.pabilo8.immersiveintelligence.client.gui.deco.tree.target;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementSorter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoTreeDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.TreeLayout.Orientation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplates;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.*;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Provides a shared target decision-tree editor for target-detecting devices.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.09.2026
 */
public abstract class GuiTargetDecisionTree<T extends TileEntityIEBase & IIEInventory, C extends ContainerIITileBase<T>>
		extends DecoTileGui<T, C>
{
	private static final String TREE_KEY = "ii.gui.emplacement.target_tree.";
	private static final String PRESET_KEY = "ii.gui.emplacement.target_preset.";
	private static final String FILTER_KEY = "ii.gui.emplacement.target_filter.";
	private static final TargetEntityType[] ENTITY_TYPES = TargetEntityType.values();
	private static final DiplomaticStatus[] RELATIONSHIPS = {
			DiplomaticStatus.MEMBER, DiplomaticStatus.ALLIED,
			DiplomaticStatus.NEUTRAL, DiplomaticStatus.ENEMY
	};

	private TargetConfiguration configuration;
	private TargetConfiguration openedConfiguration;
	@Nullable
	private TargetDecisionTreePreset selectedPreset;
	@Nullable
	private TargetDecisionTreeNode selectedNode;
	private TargetDecisionTreeWrapper treeWrapper;
	private DecoTreeDisplay<TargetDecisionTreeNode> treeDisplay;
	private DecoPanel topBar, treePanel, editorPanel;
	private DecoCheckbox currentTaskCheckbox;

	protected GuiTargetDecisionTree(EntityPlayer player, T tile, IIGUI gui,
									@Nullable TargetConfiguration initialConfiguration)
	{
		super(player, tile, gui);
		configuration = initialConfiguration==null?TargetConfiguration.createDefault(): initialConfiguration.copy();
		openedConfiguration = configuration.copy();
		selectedPreset = preferredPreset(configuration);
	}

	@Override
	public void onInit()
	{
		initTargetEditorGui();
		if(configuration==null)
		{
			configuration = TargetConfiguration.createDefault();
			openedConfiguration = configuration.copy();
			selectedPreset = preferredPreset(configuration);
		}

		addComponents(
				topBar = new DecoPanel(2, 2+8)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(152+96-4, 18),
				treePanel = new DecoPanel(2, 2+19+8)
						.withBackground(DecoTextures.BG_STEEL)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(122-1, 152-24),
				editorPanel = new DecoPanel(2+122+1, 2+19+8)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withSize(122-1, 152-24)
		);

		refreshTopBar();
		selectPreset(selectedPreset);
	}

	private void refreshTopBar()
	{
		if(topBar==null)
			return;
		topBar.cleanup();
		DecoDropdown<TargetDecisionTreePreset> presets = new DecoDropdown<TargetDecisionTreePreset>(2, 2)
				.withSize(140, 16)
				.withDropdownWidth(140)
				.withMaxDisplayedEntries(8)
				.withSortFunction(createPresetSorter())
				.withEntries(configuration.getPresets())
				.withDisplayFunction(createPresetDisplay())
				.withCreateLaterAction(this::addPreset)
				.withSelectedEntry(selectedPreset)
				.withOnSelectedEntry((oldPreset, newPreset) -> selectPreset(newPreset));

		topBar.addComponents(
				presets,
				new DecoButton(144, 2)
						.withSize(31, 14)
						.withText(PRESET_KEY+"reset")
						.withTranslatedTooltip(PRESET_KEY+"reset.tooltip")
						.withOnLMBPressed(this::resetSelectedPreset)
						.withDisabled(selectedPreset==null),
				currentTaskCheckbox = new DecoCheckbox(178, 4)
						.withRawText(I18n.format(PRESET_KEY+"current"))
						.withSize(64, 11)
						.withTranslatedTooltip(PRESET_KEY+"current.tooltip")
						.withChecked(isSelectedPresetActive())
						.withOnToggle(this::setSelectedPresetActive)
						.withDisabled(selectedPreset==null)
		);
	}

	private DecoEntryPanelBuilder<TargetDecisionTreePreset> createPresetDisplay()
	{
		return new DecoEntryPanelBuilder<TargetDecisionTreePreset>()
				.withHeight(16)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
				.withLabel("name", panel -> new DecoLabel(fontRenderer, 3, 3)
						.withSize(panel.width-37, 10)
						.withAlign(DecoAlignment.LEFT))
				.withComponent(panel -> new DecoButton(panel.width-31, 1)
						.withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
						.withTranslatedTooltip(PRESET_KEY+"duplicate.tooltip")
						.withOnLMBPressed(() -> duplicatePreset(panel.getCurrentElement()))
						.withDisabled(configuration.getPresets().size() >= TargetingLimits.MAX_PRESETS))
				.withComponent(panel -> new DecoButton(panel.width-16, 1)
						.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
						.withTranslatedTooltip(PRESET_KEY+"remove.tooltip")
						.withOnLMBPressed(() -> removePreset(panel.getCurrentElement())))
				.withElementApplyMethod((preset, panel) -> panel.label("name")
						.withRawText(getPresetDisplayName(preset)));
	}

	private DecoElementSorter<TargetDecisionTreePreset> createPresetSorter()
	{
		return new DecoElementSorter<TargetDecisionTreePreset>()
		{
			@Override
			public List<TargetDecisionTreePreset> sort(List<TargetDecisionTreePreset> elements)
			{
				return elements;
			}

			@Nullable
			@Override
			public List<TargetDecisionTreePreset> autocomplete(List<TargetDecisionTreePreset> elements, String input)
			{
				String searched = input==null?"": input.toLowerCase(Locale.ENGLISH);
				List<TargetDecisionTreePreset> result = new ArrayList<>();
				for(TargetDecisionTreePreset preset : elements)
					if(getPresetDisplayName(preset).toLowerCase(Locale.ENGLISH).contains(searched))
						result.add(preset);
				return result;
			}
		};
	}

	private void selectPreset(@Nullable TargetDecisionTreePreset preset)
	{
		selectedPreset = preset;
		selectedNode = preset==null?null: preset.getTree().getRoot();
		if(currentTaskCheckbox!=null)
			currentTaskCheckbox.withChecked(isSelectedPresetActive());
		refreshTreeDisplay();
		refreshEditor();
	}

	private void refreshTreeDisplay()
	{
		if(treePanel==null)
			return;
		treePanel.cleanup();
		if(selectedPreset==null)
			return;
		treeWrapper = new TargetDecisionTreeWrapper(selectedPreset.getTree(), node -> {
			selectedNode = node;
			refreshEditor();
		});
		treeDisplay = treePanel.addComponent(new DecoTreeDisplay<TargetDecisionTreeNode>(4, 4)
				.withTree(treeWrapper)
				.withNodeRenderer(new TargetDecisionTreeNodeRenderer())
				.withLayoutOrientation(Orientation.VERTICAL_TOP_TO_BOTTOM)
				.withVirtualRoot(false)
				.withBackground(null)
				.withSize(treePanel.width-8, treePanel.height-8));
	}

	private void refreshEditor()
	{
		if(editorPanel==null)
			return;
		editorPanel.cleanup();
		if(selectedPreset==null||selectedNode==null)
			return;

		int y = 4;
		if(!selectedPreset.isBuiltin())
		{
			editorPanel.addLabel(PRESET_KEY+"name", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
			editorPanel.addComponent(new DecoTextField(48, y)
					.withSize(editorPanel.width-52, 14)
					.withMaxStringLength(TargetingLimits.MAX_STRING_LENGTH)
					.withText(selectedPreset.getName())
					.withOnTextChanged(text -> {
						selectedPreset.setName(text);
						saveTargetConfiguration();
					}));
			y += 17;
		}

		editorPanel.addLabel(TREE_KEY+"node", 4, y)
				.withSize(editorPanel.width-8, 10)
				.withAlign(DecoAlignment.CENTER);
		y += 13;

		editorPanel.addLabel(TREE_KEY+"weight", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
		editorPanel.addComponent(new DecoTextField(48, y)
				.withSize(editorPanel.width-52, 14)
				.withFilter(TextFilter.DECIMAL)
				.withText(selectedNode.getWeight())
				.withTranslatedTooltip(TREE_KEY+"weight.tooltip")
				.withOnTextChanged(text -> {
					selectedNode.setWeight(TextFilter.DECIMAL.parseInt(text, selectedNode.getWeight()));
					saveTargetConfiguration();
				}));
		y += 17;

		editorPanel.addLabel(TREE_KEY+"filter", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
		editorPanel.addComponent(new DecoDropdown<TargetFilterType>(48, y)
				.withSize(editorPanel.width-52, 14)
				.withDropdownWidth(editorPanel.width-52)
				.withMaxDisplayedEntries(8)
				.withEntries(getSelectableFilterTypes())
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(this::getFilterDisplayName))
				.withSelectedEntry(selectedNode.getFilter().getType())
				.withTranslatedTooltip(TREE_KEY+"filter.tooltip")
				.withOnSelectedEntry((oldType, newType) -> {
					if(newType!=null&&newType!=selectedNode.getFilter().getType())
					{
						selectedNode.setFilter(newType.createDefault());
						saveTargetConfiguration();
						refreshEditor();
					}
				}));
		y += 17;

		addFilterValueEditor(y);

		int buttonY = editorPanel.height-18;
		editorPanel.addComponent(new DecoButton(4, buttonY)
				.withSize(54, 14)
				.withText(TREE_KEY+"add_child")
				.withTranslatedTooltip(TREE_KEY+"add_child.tooltip")
				.withOnLMBPressed(this::addChildNode));
		editorPanel.addComponent(new DecoButton(61, buttonY)
				.withSize(editorPanel.width-65, 14)
				.withText(TREE_KEY+"remove")
				.withTranslatedTooltip(TREE_KEY+"remove.tooltip")
				.withOnLMBPressed(this::removeSelectedNode)
				.withDisabled(selectedNode==selectedPreset.getTree().getRoot()));
	}

	private void addFilterValueEditor(int y)
	{
		TargetFilter filter = selectedNode.getFilter();
		switch(filter.getType())
		{
			case ENTITY_ID:
			{
				EntityIdTargetFilter idFilter = (EntityIdTargetFilter)filter;
				List<String> ids = getRegisteredEntityIds();
				if(!idFilter.getEntityId().isEmpty()&&!ids.contains(idFilter.getEntityId()))
					ids.add(0, idFilter.getEntityId());
				addStringDropdown(y, TREE_KEY+"entity_id", ids, idFilter.getEntityId(), idFilter::setEntityId);
			}
			break;
			case MOD_ID:
			{
				ModIdTargetFilter modFilter = (ModIdTargetFilter)filter;
				List<String> ids = getRegisteredModIds();
				if(!modFilter.getModId().isEmpty()&&!ids.contains(modFilter.getModId()))
					ids.add(0, modFilter.getModId());
				addStringDropdown(y, TREE_KEY+"mod_id", ids, modFilter.getModId(), modFilter::setModId);
			}
			break;
			case ENTITY_TYPE:
			{
				EntityTypeTargetFilter typeFilter = (EntityTypeTargetFilter)filter;
				editorPanel.addLabel(TREE_KEY+"entity_type", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
				editorPanel.addComponent(new DecoDropdown<TargetEntityType>(48, y)
						.withSize(editorPanel.width-52, 14)
						.withDropdownWidth(editorPanel.width-52)
						.withEntries(ENTITY_TYPES)
						.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(this::getEntityTypeDisplayName))
						.withSelectedEntry(typeFilter.getTargetType())
						.withOnSelectedEntry((oldValue, newValue) -> {
							if(newValue!=null)
							{
								typeFilter.setTargetType(newValue);
								saveTargetConfiguration();
							}
						}));
			}
			break;
			case NAME:
			{
				NameTargetFilter nameFilter = (NameTargetFilter)filter;
				editorPanel.addLabel(TREE_KEY+"name", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
				editorPanel.addComponent(new DecoTextField(48, y)
						.withSize(editorPanel.width-52, 14)
						.withMaxStringLength(TargetingLimits.MAX_STRING_LENGTH)
						.withText(nameFilter.getName())
						.withOnTextChanged(value -> {
							nameFilter.setName(value);
							saveTargetConfiguration();
						}));
			}
			break;
			case HEALTH:
			case MAX_HEALTH:
			case DISTANCE:
			case HORIZONTAL_DISTANCE:
				addNumericEditor(y, (NumericTargetFilter)filter);
				break;
			case ON_GROUND:
			case IN_WATER:
			case ON_FIRE:
			{
				BooleanTargetFilter booleanFilter = (BooleanTargetFilter)filter;
				editorPanel.addLabel(TREE_KEY+"expected", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
				editorPanel.addComponent(new DecoSwitch(48, y+2)
						.withRawText(I18n.format(TREE_KEY+"expected_state"))
						.withSize(editorPanel.width-52, 11)
						.withCurrentState(booleanFilter.isExpected())
						.withOnToggle(value -> {
							booleanFilter.setExpected(value);
							saveTargetConfiguration();
						}));
			}
			break;
			case FACTION_RELATIONSHIP:
			{
				FactionRelationshipTargetFilter relationFilter = (FactionRelationshipTargetFilter)filter;
				editorPanel.addLabel(TREE_KEY+"relationship", 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
				editorPanel.addComponent(new DecoDropdown<DiplomaticStatus>(48, y)
						.withSize(editorPanel.width-52, 14)
						.withDropdownWidth(editorPanel.width-52)
						.withEntries(RELATIONSHIPS)
						.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(this::getRelationshipDisplayName))
						.withSelectedEntry(relationFilter.getRelationship())
						.withOnSelectedEntry((oldValue, newValue) -> {
							if(newValue!=null)
							{
								relationFilter.setRelationship(newValue);
								saveTargetConfiguration();
							}
						}));
			}
			break;
			case ANY:
			case INVALID:
			default:
				break;
		}
	}

	private void addStringDropdown(int y, String labelKey, List<String> entries, String selected,
								   java.util.function.Consumer<String> onSelected)
	{
		editorPanel.addLabel(labelKey, 4, y).withSize(44, 14).withAlign(DecoAlignment.LEFT);
		editorPanel.addComponent(new DecoDropdown<String>(48, y)
				.withSize(editorPanel.width-52, 14)
				.withDropdownWidth(Math.max(96, editorPanel.width-52))
				.withMaxDisplayedEntries(7)
				.withSortFunction(createStringSorter())
				.withEntries(entries)
				.withSelectedEntry(selected)
				.withOnSelectedEntry((oldValue, newValue) -> {
					if(newValue!=null)
					{
						onSelected.accept(newValue);
						saveTargetConfiguration();
					}
				}));
	}

	private DecoElementSorter<String> createStringSorter()
	{
		return new DecoElementSorter<String>()
		{
			@Override
			public List<String> sort(List<String> elements)
			{
				return elements;
			}

			@Nullable
			@Override
			public List<String> autocomplete(List<String> elements, String input)
			{
				String searched = input==null?"": input.toLowerCase(Locale.ENGLISH);
				List<String> result = new ArrayList<>();
				for(String element : elements)
					if(element.toLowerCase(Locale.ENGLISH).contains(searched))
						result.add(element);
				return result;
			}
		};
	}

	private void addNumericEditor(int y, NumericTargetFilter numeric)
	{
		editorPanel.addLabel(TREE_KEY+"comparison", 4, y).withSize(32, 14).withAlign(DecoAlignment.LEFT);
		editorPanel.addComponent(new DecoDropdown<NumericComparison>(36, y)
				.withSize(34, 14)
				.withDropdownWidth(48)
				.withEntries(NumericComparison.values())
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(NumericComparison::getSymbol))
				.withSelectedEntry(numeric.getComparison())
				.withOnSelectedEntry((oldValue, newValue) -> {
					if(newValue!=null)
					{
						numeric.setComparison(newValue);
						saveTargetConfiguration();
					}
				}));
		editorPanel.addComponent(new DecoTextField(73, y)
				.withSize(editorPanel.width-77, 14)
				.withFilter(TextFilter.FLOAT)
				.withText(numeric.getValue())
				.withOnTextChanged(text -> {
					numeric.setValue(IIMath.getDouble(text, numeric.getValue()));
					saveTargetConfiguration();
				}));
	}

	private void addChildNode()
	{
		if(selectedPreset==null||selectedNode==null)
			return;
		try
		{
			TargetDecisionTreeNode child = selectedPreset.getTree().addChild(selectedNode, new AnyTargetFilter(), 0);
			saveTargetConfiguration();
			treeWrapper.refreshStructure();
			treeDisplay.refreshLayout();
			treeWrapper.select(child);
		} catch(IllegalArgumentException ignored)
		{
			//The configured tree limit prevents this operation.
		}
	}

	private void removeSelectedNode()
	{
		if(selectedPreset==null||selectedNode==null||selectedNode==selectedPreset.getTree().getRoot())
			return;
		TargetDecisionTreeNode parent = selectedNode.getParent();
		if(selectedPreset.getTree().removeSubtree(selectedNode))
		{
			saveTargetConfiguration();
			treeWrapper.refreshStructure();
			treeDisplay.refreshLayout();
			treeWrapper.select(parent);
		}
	}

	private void addPreset()
	{
		if(configuration.getPresets().size() >= TargetingLimits.MAX_PRESETS)
			return;
		TargetDecisionTreePreset created = TargetDecisionTreePreset.createCustom(I18n.format(PRESET_KEY+"new_name"));
		if(configuration.addPreset(created))
		{
			selectedPreset = created;
			saveTargetConfiguration();
			refreshTopBar();
			selectPreset(created);
		}
	}

	private void duplicatePreset(@Nullable TargetDecisionTreePreset preset)
	{
		if(preset==null||configuration.getPresets().size() >= TargetingLimits.MAX_PRESETS)
			return;
		String name = I18n.format(PRESET_KEY+"copy_name", getPresetDisplayName(preset));
		TargetDecisionTreePreset duplicate = preset.duplicate(TargetingLimits.clampString(name));
		if(configuration.addPreset(duplicate))
		{
			selectedPreset = duplicate;
			saveTargetConfiguration();
			refreshTopBar();
			selectPreset(duplicate);
		}
	}

	private void removePreset(@Nullable TargetDecisionTreePreset preset)
	{
		if(preset==null)
			return;
		int index = configuration.getPresets().indexOf(preset);
		if(configuration.removePreset(preset.getId()))
		{
			if(preset==selectedPreset)
				selectedPreset = configuration.getPresets().isEmpty()?null:
						configuration.getPresets().get(Math.min(index, configuration.getPresets().size()-1));
			saveTargetConfiguration();
			refreshTopBar();
			selectPreset(selectedPreset);
		}
	}

	private void resetSelectedPreset()
	{
		if(selectedPreset==null)
			return;
		String id = selectedPreset.getId();
		TargetDecisionTreePreset replacement = null;
		if(selectedPreset.isBuiltin())
			replacement = TargetPresetDefaults.create(id);
		else
		{
			TargetDecisionTreePreset opened = openedConfiguration.findPreset(id);
			replacement = opened==null?new TargetDecisionTreePreset(id, selectedPreset.getName(), false,
					new TargetDecisionTree(new TargetDecisionTreeNode(new AnyTargetFilter(), 0))): opened.copy();
		}
		if(replacement==null)
			return;
		int index = configuration.getPresets().indexOf(selectedPreset);
		configuration.getPresets().set(index, replacement);
		selectedPreset = replacement;
		saveTargetConfiguration();
		refreshTopBar();
		selectPreset(replacement);
	}

	private void setSelectedPresetActive(boolean active)
	{
		String activeId = active&&selectedPreset!=null?selectedPreset.getId(): "";
		if(activeId.equals(configuration.getActivePresetId()))
			return;
		configuration.setActivePresetId(activeId);
		saveTargetConfiguration();
	}

	private boolean isSelectedPresetActive()
	{
		return selectedPreset!=null&&selectedPreset.getId().equals(configuration.getActivePresetId());
	}

	private void saveTargetConfiguration()
	{
		if(!configuration.isValid())
			return;
		sendTargetConfigurationUpdate(configuration);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		//Each valid target configuration change is sent immediately.
		return EasyNBT.newNBT();
	}

	private String getPresetDisplayName(TargetDecisionTreePreset preset)
	{
		return preset.isBuiltin()?I18n.format(preset.getName()): preset.getName();
	}

	private String getFilterDisplayName(TargetFilterType type)
	{
		return I18n.format(FILTER_KEY+type.getId());
	}

	private String getEntityTypeDisplayName(TargetEntityType type)
	{
		return I18n.format(FILTER_KEY+"entity_type."+type.getName());
	}

	private String getRelationshipDisplayName(DiplomaticStatus status)
	{
		return I18n.format(FILTER_KEY+"relationship."+status.getName());
	}

	private List<TargetFilterType> getSelectableFilterTypes()
	{
		List<TargetFilterType> result = new ArrayList<>();
		for(TargetFilterType type : TargetFilterType.values())
			if(type.isUserSelectable())
				result.add(type);
		return result;
	}

	private List<String> getRegisteredEntityIds()
	{
		List<String> result = new ArrayList<>();
		for(ResourceLocation id : ForgeRegistries.ENTITIES.getKeys())
			result.add(id.toString());
		Collections.sort(result);
		return result;
	}

	private List<String> getRegisteredModIds()
	{
		Set<String> namespaces = new TreeSet<>();
		for(ResourceLocation id : ForgeRegistries.ENTITIES.getKeys())
			namespaces.add(id.getResourceDomain());
		return new ArrayList<>(namespaces);
	}

	protected abstract void initTargetEditorGui();

	protected abstract void sendTargetConfigurationUpdate(TargetConfiguration configuration);

	@Nullable
	private static TargetDecisionTreePreset preferredPreset(TargetConfiguration configuration)
	{
		TargetDecisionTreePreset active = configuration.getActivePreset();
		return active!=null?active: configuration.getPresets().isEmpty()?null: configuration.getPresets().get(0);
	}
}
