package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimationCollisionMap;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;
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
	private Vec3d globalOffset = Vec3d.ZERO;

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
	public <T extends TileEntityMultiblockPart<T> & ITactileListener> TactileHandler(MultiblockStuctureBase<T> multiblock, T listener)
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
	private boolean init()
	{
		//:(
		if(!Graphics.tactileAMT)
			return false;

		//BUG: 12.08.2024 fix AMTTactile
		if(true)
			return false;

		//Loading is not possible
		if(aabbLoc==null)
			return false;
		BlockPos mainPos = getPos();

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
		if(jsonObject.has("tactile_offset"))
		{
			JsonArray array = jsonObject.get("tactile_offset").getAsJsonArray();
			globalOffset = new Vec3d(
					array.get(0).getAsDouble(),
					array.get(1).getAsDouble(),
					array.get(2).getAsDouble()
			);
		}

		JsonObject tactile = jsonObject.get("tactile").getAsJsonObject();

		//Read header
		if(tactile.has("_schema"))
			headerLoc = ResLoc.of(new ResourceLocation(tactile.remove("_schema").getAsString()));

		//Header absent, can't continue loading
		if(headerLoc==null)
			return false;
		header = HEADERS.computeIfAbsent(headerLoc, IIAnimationLoader::loadHeaderServer);

		//Parse and process tactiles
		ArrayList<EntityAMTTactile> tempEntities = parseTactiles(tactile, allBounds);
		processTactiles(tempEntities, mainPos);

		//Add post fixes
		header.applyHierarchy(entities);
		entities.forEach(e -> e.setPosition(mainPos.getX(), mainPos.getY(), mainPos.getZ()));
		entities.forEach(getWorld()::spawnEntity);


		return true;
	}

	@Nonnull
	private ArrayList<EntityAMTTactile> parseTactiles(JsonObject tactile, Map<String, AxisAlignedBB> allBounds)
	{
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
						).scale(0.0625);
					}

					//Repeated objects may use templates
					if(boxObject.has("type"))
						aabb = allBounds.getOrDefault(boxObject.get("type").getAsString(), aabb);
					else if(boxObject.has("bounds"))
						aabb = getAxisAlignedBB(boxObject.get("bounds").getAsJsonArray());

					//Add entity to the list and create it in the world
					tempEntities.add(new EntityAMTTactile(this, entries.getKey(),
							processOffset(header, entries.getKey(), offset), aabb)
					);

				}
			}
		}
		return tempEntities;
	}

	private Vec3d processOffset(IIModelHeader header, String key, Vec3d offset)
	{
		//.add(new Vec3d(0, 0, -0.5))
		Vec3d total = header.getOffset(key)
				.add(offset)
				.add(globalOffset)
				.addVector(-1, 0, -1.5);
		boolean mirrored = listener.getIsTactileMirrored();
		total = new Vec3d(mirrored?(total.x-1): -total.x, total.y, -total.z);
		//.add(new Vec3d(0.5, 0.5, 1));
		//rotate the vector depending on facing
		EnumFacing facing = listener.getTactileFacing();

		Vec3d apply = new Matrix4(facing)
				.apply(total);
		//Add corrections based on block corner offset
		switch(facing)
		{
			case SOUTH:
				apply = apply.addVector(-1, 0, 0);
				break;
			case EAST:
				apply = apply.addVector(-0.5, 0, 0.5);
				break;
			case WEST:
				apply = apply.addVector(-0.5, 0, -0.5);
				break;
		}

		/*switch(listener.getFacing())
		{
			case NORTH:
				total = new Vec3d((listener.getIsMirrored()?total.x: -total.x+1), total.y, -total.z+2);
				break;
			case SOUTH:
				break;
			case EAST:
				total = new Vec3d(total.z-1, total.y, (listener.getIsMirrored()?total.x+0.5: -total.x+1.5));
				break;
			case WEST:
				total = new Vec3d(-total.z, total.y, -total.x);
				break;
		}*/

		return apply;
	}

	/**
	 * Detect if a there are multiple objects of the same name
	 * If yes -> create a Main Object to optimize the structure/animation and add all objects of the same name as its children, then rename them to OBJ_child[number]
	 * If no -> the object is a Main Object, it will be a part of animation
	 * In both cases, the Main Object may have a parent, which it will base its position on
	 **/
	private void processTactiles(ArrayList<EntityAMTTactile> tempEntities, BlockPos mainPos)
	{
		while(!tempEntities.isEmpty())
		{
			EntityAMTTactile amt = tempEntities.remove(0);
			List<EntityAMTTactile> matching = tempEntities.stream().filter(e -> e.name.equals(amt.name)).collect(Collectors.toList());

			if(matching.isEmpty())
				entities.add(amt);
			else
			{
				//Add parent entity with empty AABB
				EntityAMTTactile parent = new EntityAMTTactile(this, amt.name, processOffset(header, amt.name, Vec3d.ZERO), new AxisAlignedBB(0, 0, 0, 0, 0, 0));
				entities.add(parent);

				//Rename child objects to OBJ_child[n]
				matching.add(0, amt);
				for(int i = 0; i < matching.size(); i++)
				{
					matching.get(i).name += "_child"+i;
					//matching.get(i).offset = matching.get(i).offset.subtract(parent.offset);
					matching.get(i).setParent(parent);
					matching.get(i).setPosition(mainPos.getX(), mainPos.getY(), mainPos.getZ());
					getWorld().spawnEntity(matching.get(i));
				}

				//Continue iteration
				tempEntities.removeAll(matching);
				entities.addAll(matching);
			}
		}
	}

	@Nonnull
	private AxisAlignedBB getAxisAlignedBB(JsonArray array)
	{
		AxisAlignedBB aabb = new AxisAlignedBB(
				array.get(0).getAsDouble()*0.0625,
				array.get(1).getAsDouble()*0.0625,
				array.get(2).getAsDouble()*0.0625,
				array.get(3).getAsDouble()*0.0625,
				array.get(4).getAsDouble()*0.0625,
				array.get(5).getAsDouble()*0.0625
		);
		if(listener.getIsTactileMirrored())
		{
			double xLength = Math.abs(aabb.maxX-aabb.minX);
			aabb = new AxisAlignedBB(-aabb.minX+2, aabb.minY, aabb.minZ, -aabb.maxX+2, aabb.maxY, aabb.maxZ);
		}

		//aabb = new AxisAlignedBB(-0.25, -0.25, -0.25, 0.25, 0.25, 0.25);
		Matrix4 mat = new Matrix4(listener.getTactileFacing());
		Vec3d vMin = mat.apply(new Vec3d(aabb.minX, aabb.minY, aabb.minZ));
		Vec3d vMax = mat.apply(new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ));
		return new AxisAlignedBB(vMin.x, vMin.y, vMin.z, vMax.x, vMax.y, vMax.z);
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

		if(animation==null)
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
			mapped = IIAnimationCollisionMap.create(entities, anim, listener.getTactileFacing(), listener.getIsTactileMirrored());
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
		return listener.getTactileWorld();
	}

	/**
	 * Will recalculate the positions if the {@link ITactileListener} moved (for some reason)
	 *
	 * @return listener's position
	 */
	public BlockPos getPos()
	{
		return listener.getTactilePos();
	}

	/**
	 * Called when a Tactile is attacked
	 *
	 * @param tactile attacked Tactile
	 * @param source  damage source
	 * @param amount  amount of damage dealt
	 */
	public boolean onAttacked(EntityAMTTactile tactile, DamageSource source, float amount)
	{
		return listener.onTactileDamage(tactile, source, amount);
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

	public boolean onCollide(EntityAMTTactile tactile, Entity entity)
	{
		return listener.onTactileCollide(tactile, entity);
	}

	public ArrayList<EntityAMTTactile> getEntities()
	{
		return entities;
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
		 * @return tactile handler instance this listener is using
		 */
		@Nullable
		TactileHandler getTactileHandler();

		/**
		 * @return world this tactile listener is in
		 */
		@Nonnull
		World getTactileWorld();

		/**
		 * @return position in the world
		 */
		@Nonnull
		BlockPos getTactilePos();

		@Nonnull
		EnumFacing getTactileFacing();

		boolean getIsTactileMirrored();

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
		default boolean onTactileDamage(EntityAMTTactile tactile, DamageSource source, float amount)
		{
			return false;
		}

		/**
		 * @return true if interaction happened
		 */
		default boolean onTactileCollide(EntityAMTTactile tactile, Entity entity)
		{
			return false;
		}
	}

}
