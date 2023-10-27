package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCollisionMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A generic dynamic collision box (AABB) handler used by various tile entities.<br>
 * Designed to support multiblocks.
 *
 * @author Pabilo8
 * @since 11.10.2023
 */
public class TactileHandler
{
	//TODO: 12.10.2023 reloading animation cache with relmod

	//--- Animation Cache ---//
	private static HashMap<ResLoc, IIModelHeader> HEADERS = new HashMap<>();
	private static HashMap<ResLoc, IIAnimation> ANIMATIONS = new HashMap<>();

	//Final values
	private final ResLoc aabbLoc;
	private ResLoc headerLoc;
	private final ITactileListener listener;

	//Reloadable values
	private boolean initialized = false;
	private IIModelHeader header;
	private final ArrayList<EntityAMTTactile> entities;
	private final HashMap<ResLoc, IIAnimationCollisionMap> animations;

	/**
	 * Constructor from an AABB file
	 */
	public TactileHandler(ResLoc aabbLoc, ITactileListener listener)
	{
		this.aabbLoc = aabbLoc;
		this.listener = listener;

		//Initialize storage
		entities = new ArrayList<>();
		animations = new HashMap<>();
	}

	/**
	 * Multiblock constructor.
	 *
	 * @param multiblock multiblock with an AABB file
	 */
	public TactileHandler(MultiblockStuctureBase<?> multiblock, ITactileListener listener)
	{
		this.aabbLoc = multiblock.getAABBFileLocation();
		this.listener = listener;

		//Initialize storage
		entities = new ArrayList<>();
		animations = new HashMap<>();
	}

	/**
	 * Load the header file.<br>
	 * Animations are loaded and cached dynamically by {@link #update(ResLoc, float)}.
	 *
	 * @return
	 */
	public boolean init()
	{
		//Loading is not possible
		if(aabbLoc==null)
			return false;

		//Load the header json
		JsonObject jsonObject = IIAnimationLoader.readServerFileToJson(aabbLoc);
		if(jsonObject.size()==0)
			return false;

		//Collect AABB dictionary
		Map<String, AxisAlignedBB> allBounds = jsonObject.get("bounds").getAsJsonObject()
				.entrySet().stream()
				.filter(e -> e.getValue() instanceof JsonArray)
				.map(e -> {
					JsonArray array = e.getValue().getAsJsonArray();
					return new Tuple<>(e.getKey(), getAxisAlignedBB(array));
				})
				.collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

		if(!jsonObject.has("tactile"))
			return false;
		JsonObject tactile = jsonObject.get("tactile").getAsJsonObject();

		//Read header
		if(tactile.has("_schema"))
			headerLoc = ResLoc.of(new ResourceLocation(tactile.remove("_schema").getAsString()));

		//Header absent, can't continue loading
		if(headerLoc==null)
			return false;
		header = HEADERS.computeIfAbsent(headerLoc, IIAnimationLoader::loadHeaderServer);

		//Load entities
		ArrayList<EntityAMTTactile> tempEntities = new ArrayList<>();
		for(Entry<String, JsonElement> entries : tactile.entrySet())
		{
			//AABB entry tree
			if(entries.getValue().isJsonArray())
			{
				//AABB entries
				JsonArray array = entries.getValue().getAsJsonArray();
				for(JsonElement box : array)
				{
					//Accept only objects
					if(!box.isJsonObject())
						continue;
					JsonObject boxObject = box.getAsJsonObject();

					//Default values
					Vec3d offset = Vec3d.ZERO;
					AxisAlignedBB aabb = new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);

					//Additional Offset
					if(boxObject.has("offset"))
					{
						JsonArray offsetArray = boxObject.get("offset").getAsJsonArray();
						offset = new Vec3d(
								offsetArray.get(0).getAsDouble(),
								offsetArray.get(1).getAsDouble(),
								offsetArray.get(2).getAsDouble()
						);
					}

					//Repeated objects may use templates
					if(boxObject.has("type"))
						aabb = allBounds.getOrDefault("type", aabb);
					else if(boxObject.has("bounds"))
						aabb = getAxisAlignedBB(boxObject.get("bounds").getAsJsonArray());

					//Add entity to the list and create it in the world
					tempEntities.add(new EntityAMTTactile(this, entries.getKey(),
							header.getOffset(entries.getKey()).add(offset), aabb)
					);

				}
			}
		}

		/*
		Detect if a there are multiple objects of the same name
		If yes -> create a Main Object to optimize the structure/animation and add all objects of the same name as its children, then rename them to OBJ_child[number]
		If no -> the object is a Main Object, it will be a part of animation
		In both cases, the Main Object may have a parent, which it will base its position on
		 */

		while(!tempEntities.isEmpty())
		{
			EntityAMTTactile amt = tempEntities.remove(0);
			List<EntityAMTTactile> matching = tempEntities.stream().filter(e -> e.name.equals(amt.name)).collect(Collectors.toList());

			if(matching.isEmpty())
				entities.add(amt);
			else
			{
				//Add parent entity with empty AABB
				EntityAMTTactile parent = new EntityAMTTactile(this, amt.name, header.getOffset(amt.name), new AxisAlignedBB(0, 0, 0, 0, 0, 0));
				entities.add(parent);

				//Rename child objects to OBJ_child[n]
				matching.add(0, amt);
				for(int i = 0; i < matching.size(); i++)
				{
					matching.get(i).name += "_child"+i;
					matching.get(i).setParent(parent);
				}

				//Continue iteration
				tempEntities.removeAll(matching);
			}
		}

		return true;
	}

	@Nonnull
	private static AxisAlignedBB getAxisAlignedBB(JsonArray array)
	{
		return new AxisAlignedBB(
				array.get(0).getAsDouble(),
				array.get(1).getAsDouble(),
				array.get(2).getAsDouble(),
				array.get(3).getAsDouble(),
				array.get(4).getAsDouble(),
				array.get(5).getAsDouble()
		);
	}

	//--- Called by Listener ---//

	/**
	 * Resets all the Tactiles to their default position.<br>
	 * Can be called instead of {@link #update(ResLoc, float)}
	 */
	public void defaultize()
	{
		//Must be initialized before applying animation
		if(!initialized&&!(initialized = init()))
			return;

		entities.forEach(EntityAMTTactile::defaultizeAnimation);
	}

	/**
	 * @param animation resource location of the animation
	 * @param time      time 0-1 of this animation
	 */
	public void update(@Nullable ResLoc animation, float time)
	{
		//Must be initialized before applying animation
		if(!initialized&&!(initialized = init()))
			return;

		//Load a cached animation or from JSON
		IIAnimationCollisionMap anim;
		if(!animations.containsKey(animation))
			anim = loadAnimation(animation);
		else
			anim = animations.get(animation);

		if(anim==null)
			return;

		anim.apply(time);

	}

	/**
	 * Loads an AMT animation on server side and maps it for this handler's Tactiles.
	 *
	 * @param res animation to be loaded
	 * @return a mapped animation
	 */
	private IIAnimationCollisionMap loadAnimation(ResLoc res)
	{
		IIAnimation anim = ANIMATIONS.get(res);
		//Animation not found in cache, it must be loaded from json
		if(anim==null)
		{
			IIAnimation animation = IIAnimationLoader.loadAnimationServer(res);
			ANIMATIONS.put(res, animation);
		}

		//Attempt mapping and caching the animation
		IIAnimationCollisionMap mapped = null;
		if(anim!=null)
		{
			mapped = IIAnimationCollisionMap.create(entities, anim);
			animations.put(res, mapped);
		}

		//Whether loaded or not, return it
		return mapped;
	}

	/**
	 * Reloads all properties of this Tactile Handler.<br>
	 * Called during multiblock reload.
	 */
	public void forceReload()
	{
		this.initialized = false;
		this.entities.forEach(Entity::setDead);
		this.entities.clear();
		this.animations.clear();
	}

	//--- Tactile Handling ---//

	/**
	 * @return the world the Tactiles are in
	 */
	public World getWorld()
	{
		return listener.getWorld();
	}

	/**
	 * Will recalculate the positions if the {@link ITactileListener} moved (for some reason)
	 *
	 * @return listener's position
	 */
	public BlockPos getPos()
	{
		return listener.getPos();
	}

	/**
	 * Called when a Tactile is attacked
	 *
	 * @param source damage source
	 * @param amount amount of damage dealt
	 */
	public void onAttacked(DamageSource source, float amount)
	{
		listener.onTactileDamage(source, amount);
	}

	/**
	 * Called when a player right-clicks a Tactile
	 *
	 * @param tactile tactile clicked
	 * @param player  player that clicked
	 * @param hand    player's hand
	 * @return whether interaction was successful
	 */
	public boolean onInteract(EntityAMTTactile tactile, EntityPlayer player, EnumHand hand)
	{
		return listener.onTactileInteract(tactile, player, hand);
	}

	/**
	 * Listener class for Tactile events
	 *
	 * @author Pabilo8
	 * @since 11.10.2023
	 */
	public interface ITactileListener
	{
		/**
		 * @return world this tactile listener is in
		 */
		World getWorld();

		/**
		 * @return position in the world
		 */
		BlockPos getPos();

		/**
		 * @return true if interaction happened
		 */
		default boolean onTactileInteract(EntityAMTTactile tactile, EntityPlayer player, EnumHand hand)
		{
			return false;
		}

		/**
		 * @return true if damage had effect
		 */
		default boolean onTactileDamage(DamageSource source, float amount)
		{
			return false;
		}
	}

}
