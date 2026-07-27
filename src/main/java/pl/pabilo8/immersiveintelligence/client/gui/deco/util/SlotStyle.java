package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 23.07.2026
 */
public enum SlotStyle
{
	//Vanilla MC bevel mask inventory slot
	VANILLA(DecoTextures.SLOT_VANILLA, true, 1),
	//Immersive Engineering style inventory slot
	IE(DecoTextures.SLOT_IE, DecoTextures.SLOT_IE_MARKER, 2),
	IE_INPUT(IE, 2),
	IE_OUTPUT(IE, 3),
	IE_CUSTOM1(IE, 4),
	IE_CUSTOM2(IE, 5),
	IE_CUSTOM3(IE, 6),
	IE_CUSTOM4(IE, 7),
	//Brass Frame IE style Inventory slot
	IE_BRASS(DecoTextures.SLOT_IE_BRASS, DecoTextures.SLOT_IE_BRASS_MARKER, 2),
	IE_BRASS_INPUT(IE_BRASS, 2),
	IE_BRASS_OUTPUT(IE_BRASS, 3),
	IE_BRASS_CUSTOM1(IE_BRASS, 4),
	IE_BRASS_CUSTOM2(IE_BRASS, 5),
	IE_BRASS_CUSTOM3(IE_BRASS, 6),
	IE_BRASS_CUSTOM4(IE_BRASS, 7),
	;

	final int borderSize;
	final boolean blending;
	final ResLoc backgroundLocation, markerLocation;
	final int markerOffset;

	SlotStyle(ResLoc backgroundLocation, boolean blending, int borderSize)
	{
		this.blending = blending;
		this.backgroundLocation = backgroundLocation;
		this.markerLocation = null;
		this.borderSize = borderSize;
		this.markerOffset = -1;
	}

	SlotStyle(ResLoc backgroundLocation, ResLoc markerLocation, int borderSize)
	{
		this.blending = false;
		this.backgroundLocation = backgroundLocation;
		this.markerLocation = markerLocation;
		this.borderSize = borderSize;
		this.markerOffset = -1;
	}

	SlotStyle(SlotStyle base, int markerOffset)
	{
		this.blending = false;
		this.backgroundLocation = base.backgroundLocation;
		this.markerLocation = base.markerLocation;
		this.borderSize = base.borderSize;
		this.markerOffset = markerOffset;
	}
}
