package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public enum LawForm implements ISerializableEnum
{
	/**
	 * No special law form. Does not have a development tree. Default permissions apply.
	 */
	DEFAULT,
	/**
	 * A company is owned by private individuals or shareholders. It operates for profit and is managed by a board of directors.<br>
	 * Rank in a company ladder depends on the number of shares you own.
	 */
	COMPANY,
	/**
	 * A republic is governed by elected officials who represent its members. Leadership rotates based on scheduled elections. <br>
	 * Rank is earned through election results.
	 */
	REPUBLIC,
	/**
	 * A commune is a collective organization where all members share ownership equally. Decisions are made by consensus or direct vote.<br>
	 * Rank is nonexistent - authority rests in collective agreement.
	 */
	COMMUNE,
	/**
	 * A commissariat is an administrative structure ruled by appointed officials, often backed by ideology or military power.<br>
	 * Rank is assigned by the central authority, not by consent.
	 */
	COMMISARIAT,
	/**
	 * A free city is governed by a city council, each councillor represents a specialization: industry, agriculture, trade, logistics and military.<br>
	 * Rank is based on influence within the city’s institutions.
	 */
	FREE_CITY,
	/**
	 * Authority is inherited through bloodline. Ownership passes to heirs upon death (despite respawn).
	 */
	DYNASTY
}
