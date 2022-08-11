package pl.pabilo8.immersiveintelligence.common.entity.hans;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 06.02.2022
 */
public class HansPlatoon
{
	EntityHans commander;
	ArrayList<EntityHans> members;
	HashMap<EntityHans, FormationPosition> positions;

	public boolean inCombat=false;

	public enum FormationPosition
	{
		FRONT,
		RIGHT_FLANK,
		LEFT_FLANK
	}

	public void updateSquad()
	{
		//squad doesn't exist
		if(members.size()==0)
			return;

		if(commander==null)
			commander = members.get(0);

		positions.clear();


		// TODO: 06.02.2022 proper squad position handling
	}

	public void moveSquad(BlockPos pos)
	{
		updateSquad();

		//squad doesn't exist
		if(members.size()==0)
			return;

		commander.getNavigator().setPath(commander.getNavigator().getPathToPos(pos), 1f);
		Vec3d direction = new Vec3d(commander.getPosition().subtract(pos)).normalize();



	}
}
