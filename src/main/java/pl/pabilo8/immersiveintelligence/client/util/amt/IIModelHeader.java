package pl.pabilo8.immersiveintelligence.client.util.amt;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

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
	private final HashMap<String, EasyNBT> properties;

	public IIModelHeader(JsonObject json)
	{
		offsets = new HashMap<>();
		hierarchy = new HashMap<>();
		properties = new HashMap<>();

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

		if(json.has("properties"))
		{
			JsonObject prop = json.getAsJsonObject("properties");
			for(Entry<String, JsonElement> entry : prop.entrySet())
			{
				//put into property map
				if(entry.getValue().isJsonObject())
					properties.put(entry.getKey(), EasyNBT.parseEasyNBT(entry.getValue().toString()));
			}
		}
	}

	/**
	 * Joins multiple headers into one
	 *
	 * @param headers model headers
	 */
	public IIModelHeader(@Nullable IIModelHeader... headers)
	{
		hierarchy = new HashMap<>();
		offsets = new HashMap<>();
		properties = new HashMap<>();

		if(headers!=null)
			for(IIModelHeader h : headers)
			{
				hierarchy.putAll(h.hierarchy);
				offsets.putAll(h.offsets);
				properties.putAll(h.properties);
			}

	}

	public Set<String> getAffectedElements()
	{
		return offsets.keySet();
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
	 */
	@SideOnly(Side.CLIENT)
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

			//Apply properties
			EasyNBT nbt = properties.get(amt.name);
			//REFACTOR: 30.07.2023 more universal handling
			if(nbt!=null)
			{
				if(amt instanceof AMTQuads)
					nbt.checkSetBoolean("lighting", ((AMTQuads)amt)::setLighting);
			}
		}

	}
}
