package pl.pabilo8.immersiveintelligence.api;

import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;

/**
 * @author Pabilo8
 * @since 26-12-2019
 */
public interface ISkyCrateConnector
{
	boolean onSkycrateMeeting(EntitySkyCrate skyCrate);
}
