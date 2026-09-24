package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.ClipboardEntry;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.DecoClipboardUtils;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * DecoList specialised for editable, provider-backed clipboard entries.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class DecoClipboardList extends DecoList<ClipboardEntry>
{
	@Nullable
	private ClipboardEntry selected;
	private final Consumer<ClipboardEntry> onChanged;

	public DecoClipboardList(int x, int y, Consumer<ClipboardEntry> onChanged)
	{
		super(x, y);
		this.onChanged = onChanged;
		withBackground(DecoTextures.BG_PAPER);
		withListBackground(DecoTextures.BG_PAPER);
		withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER);

		withDisplayFunction(DecoClipboardUtils.createEntryPanel(this::removeClipboardEntry, onChanged));
		withCreateAction(() -> {
			ClipboardEntry entry = DecoClipboardUtils.createEntry("");
			if(entry!=null)
				onChanged.accept(entry);
			return entry;
		});
		withOnEntryClicked(entry -> selected = entry);
	}

	private void removeClipboardEntry(ClipboardEntry entry)
	{
		removeEntry(entry);
		onChanged.accept(entry);
		if(entry==selected)
			selected = null;
	}

	@Override
	public boolean addEntry(@Nonnull ClipboardEntry entry)
	{
		return getEntries().size() < ItemIIClipboard.MAX_ENTRIES&&super.addEntry(entry);
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		ClipboardEntry current = getHoveredEntry()!=null?getHoveredEntry(): selected;
		switch(event)
		{
			case COPY ->
			{
				if(current!=null)
					DecoClipboardUtils.copy(current.getValue());
			}
			case CUT ->
			{
				if(current!=null)
				{
					DecoClipboardUtils.copy(current.getValue());
					removeClipboardEntry(current);
				}
			}
			case PASTE ->
			{
				ClipboardEntry pasted = DecoClipboardUtils.pasteEntry();
				if(pasted!=null)
				{
					addEntry(pasted);
					selected = pasted;
					onChanged.accept(pasted);
				}
			}
			default -> super.onGuiEvent(event);
		}
	}
}
