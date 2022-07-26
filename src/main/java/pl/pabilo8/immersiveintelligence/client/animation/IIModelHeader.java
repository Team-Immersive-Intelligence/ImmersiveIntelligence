package pl.pabilo8.immersiveintelligence.client.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Defines offsets and hierarchy for an animated model's parts
 *
 * @author Pabilo8
 * @since 09.04.2022
 */
public class IIModelHeader
{
	private final HashMap<String, String> hierarchy;
	private final HashMap<String, Vec3d> offsets;

	public IIModelHeader(JsonObject json)
	{
		offsets = new HashMap<>();
		hierarchy = new HashMap<>();

		//get origins, if not contained Vec3D.ZERO will be used
		if(json.has("origins"))
		{
			JsonObject origins = json.getAsJsonObject("origins");
			for(Entry<String, JsonElement> entry : origins.entrySet())
				offsets.put(entry.getKey(),
						IIAnimationUtils.jsonToVec3d(entry.getValue().getAsJsonArray()).scale(0.0625));
		}

		//if doesn't contain key, is not a child
		if(json.has("hierarchy"))
		{
			JsonObject hh = json.getAsJsonObject("hierarchy");
			for(Entry<String, JsonElement> entry : hh.entrySet())
			{
				//put child -> parent onto hierarchy map
				if(!entry.getValue().getAsString().isEmpty())
					hierarchy.put(entry.getKey(), entry.getValue().getAsString());
			}
		}
	}

	/**
	 * @param name of the model group
	 * @return the offset or {@link Vec3d#ZERO} if not specified
	 */
	@Nonnull
	public Vec3d getOffset(String name)
	{
		return offsets.getOrDefault(name, Vec3d.ZERO);
	}

	/**
	 * Applies parent/child hierarchy to AMTs<br>
	 * Children will be rendered as having offset from parents
	 *
	 * @param amts
	 */
	public void applyHierarchy(Collection<AMT> amts)
	{
		for(AMT amt : amts)
		{
			AMT[] children = amts.stream()
					.filter(child -> hierarchy.getOrDefault(child.name, "")
							.equals(amt.name))
					.map(AMT::setChild)
					.toArray(AMT[]::new);

			if(children.length > 0)
				amt.setChildren(children);
		}

	}
}
