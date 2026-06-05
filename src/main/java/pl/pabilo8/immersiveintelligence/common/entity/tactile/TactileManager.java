package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IMirrorAble;
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
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimationCollisionMap;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A generic dynamic collision box (AABB) handler used by various tile entities.<br>
 * Designed to support multiblocks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.10.2023
 */
public class TactileManager
{
	//--- Animation Cache ---//
	private static HashMap<ResLoc, AMTModelHeader> HEADERS = new HashMap<>();
	private static HashMap<ResLoc, IIAnimation> ANIMATIONS = new HashMap<>();
	private static HashMap<ResLoc, SourceData> SOURCE_CACHE = new HashMap<>();

	private static final String CATEGORY_BASE = "base";

	//Final values
	private final ResLoc baseAabbLoc;
	private final Map<String, ResLoc> aabbLocations;
	private final ITactileListener listener;
	private final ArrayList<EntityAMTTactile> entities;
	private final HashMap<ResLoc, IIAnimationCollisionMap> animations;

	//Reloadable values
	private boolean initialized = false;
	private final Supplier<World> worldSupplier;
	private final Supplier<BlockPos> posSupplier;
	private final Supplier<EnumFacing> facingSupplier;
	private final Supplier<Boolean> mirroredSupplier;

	/**
	 * Constructor from an AABB file
	 */
	public TactileManager(ResLoc aabbLoc, ITactileListener listener, Supplier<World> worldSupplier, Supplier<BlockPos> posSupplier,
						  Supplier<EnumFacing> facingSupplier, Supplier<Boolean> mirroredSupplier)
	{
		this.baseAabbLoc = aabbLoc;
		this.aabbLocations = new LinkedHashMap<>();
		this.aabbLocations.put(CATEGORY_BASE, baseAabbLoc);
		this.listener = listener;

		this.worldSupplier = worldSupplier;
		this.posSupplier = posSupplier;
		this.facingSupplier = facingSupplier;
		this.mirroredSupplier = mirroredSupplier;

		//Initialize storage
		entities = new ArrayList<>();
		animations = new HashMap<>();
	}

	/**
	 * Multiblock constructor.
	 *
	 * @param multiblock multiblock with an AABB file
	 */
	public <T extends TileEntityMultiblockPart<T> & ITactileListener> TactileManager(MultiblockStructureBase<T> multiblock, T listener)
	{
		this.baseAabbLoc = multiblock.getAABBFileLocation();
		this.aabbLocations = new LinkedHashMap<>();
		this.aabbLocations.put(CATEGORY_BASE, baseAabbLoc);
		this.listener = listener;

		this.worldSupplier = listener::getWorld;
		this.posSupplier = listener::getPos;
		this.facingSupplier = listener::getFacing;
		if(listener instanceof IMirrorAble)
		{
			IMirrorAble mirrorAble = (IMirrorAble)listener;
			this.mirroredSupplier = mirrorAble::getIsMirrored;
		}
		else
			this.mirroredSupplier = null;

		//Initialize storage
		entities = new ArrayList<>();
		animations = new HashMap<>();
	}

	/**
	 * Add or override an additional AABB/header source.
	 * Passing null marks the category as "do not load".
	 */
	public void setAdditionalModel(@Nullable String category, @Nullable ResLoc location)
	{
		if(category==null||category.isEmpty())
			return;

		aabbLocations.put(category, location);
		forceReload();
	}

	/**
	 * Load the header file.<br>
	 * Animations are loaded and cached dynamically by {@link #update(ResLoc, float)}.
	 *
	 * @return true if the handler was initialized successfully (now or in a previous run), false otherwise
	 */
	private boolean init()
	{
		//:(
		if(worldSupplier.get().isRemote)
			return false;
		if(!Graphics.tactileAMT)
			return false;

		BlockPos mainPos = getPos();

		List<SourceData> sources = new ArrayList<>();
		List<AMTModelHeader> headers = new ArrayList<>();

		for(Entry<String, ResLoc> entry : aabbLocations.entrySet())
		{
			ResLoc loc = entry.getValue();
			if(loc==null)
				continue;

			SourceData data = loadSource(loc);
			if(data!=null)
			{
				sources.add(data);
				headers.add(data.header);
			}
		}

		if(sources.isEmpty())
			return false;

		AMTModelHeader mergedHeader = headers.size()==1?headers.get(0): new AMTModelHeader(headers.toArray(new AMTModelHeader[0]));

		List<EntityAMTTactile> addedAll = new ArrayList<>();
		for(SourceData data : sources)
		{
			ArrayList<EntityAMTTactile> tempEntities = parseTactiles(data.tactile, data.allBounds, mergedHeader, data.globalOffset);
			addedAll.addAll(processTactiles(tempEntities, mainPos, mergedHeader, data.globalOffset));
		}

		mergedHeader.applyHierarchy(addedAll);
		addedAll.forEach(e -> e.setPosition(mainPos.getX(), mainPos.getY(), mainPos.getZ()));
		addedAll.forEach(getWorld()::spawnEntity);

		return true;
	}

	@Nullable
	private SourceData loadSource(ResLoc sourceLoc)
	{
		SourceData cached = SOURCE_CACHE.get(sourceLoc);
		if(cached!=null)
			return cached;

		//Load the header json
		JsonObject jsonObject = AMTLoader.readServerFileToJson(sourceLoc, "animation");
		if(jsonObject.size()==0)
			return null;

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
			return null;

		Vec3d globalOffset = Vec3d.ZERO;
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
		ResLoc headerLoc = null;
		if(tactile.has("_schema"))
			headerLoc = ResLoc.of(new ResourceLocation(tactile.get("_schema").getAsString()));

		//Header absent, can't continue loading
		if(headerLoc==null)
			return null;

		AMTModelHeader header = HEADERS.computeIfAbsent(headerLoc, AMTLoader::loadHeaderServer);
		SourceData data = new SourceData(header, tactile, allBounds, globalOffset);
		SOURCE_CACHE.put(sourceLoc, data);
		return data;
	}

	private static class SourceData
	{
		private final AMTModelHeader header;
		private final JsonObject tactile;
		private final Map<String, AxisAlignedBB> allBounds;
		private final Vec3d globalOffset;

		private SourceData(AMTModelHeader header, JsonObject tactile, Map<String, AxisAlignedBB> allBounds, Vec3d globalOffset)
		{
			this.header = header;
			this.tactile = tactile;
			this.allBounds = allBounds;
			this.globalOffset = globalOffset;
		}
	}

	@Nonnull
	private ArrayList<EntityAMTTactile> parseTactiles(JsonObject tactile, Map<String, AxisAlignedBB> allBounds,
													  AMTModelHeader header, Vec3d globalOffset)
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
							processOffset(header, globalOffset, entries.getKey(), offset), aabb)
					);

				}
			}
		}
		return tempEntities;
	}

	private Vec3d processOffset(AMTModelHeader header, Vec3d globalOffset, String key, Vec3d offset)
	{
		//.add(new Vec3d(0, 0, -0.5))
		Vec3d total = header.getOffset(key)
				.add(offset)
				.add(globalOffset)
				.addVector(-1, 0, -1.5);
		boolean mirrored = getIsMirrored();
		total = new Vec3d(mirrored?(total.x-1): -total.x, total.y, -total.z);
		//.add(new Vec3d(0.5, 0.5, 1));
		//rotate the vector depending on facing
		EnumFacing facing = getFacing();

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
		return apply;
	}

	/**
	 * Detect if a there are multiple objects of the same name
	 * If yes -> create a Main Object to optimize the structure/animation and add all objects of the same name as its children, then rename them to OBJ_child[number]
	 * If no -> the object is a Main Object, it will be a part of animation
	 * In both cases, the Main Object may have a parent, which it will base its position on
	 **/
	private List<EntityAMTTactile> processTactiles(ArrayList<EntityAMTTactile> tempEntities, BlockPos mainPos,
												   AMTModelHeader header, Vec3d globalOffset)
	{
		List<EntityAMTTactile> added = new ArrayList<>();
		while(!tempEntities.isEmpty())
		{
			EntityAMTTactile amt = tempEntities.remove(0);
			List<EntityAMTTactile> matching = tempEntities.stream().filter(e -> e.name.equals(amt.name)).collect(Collectors.toList());

			if(matching.isEmpty())
			{
				entities.add(amt);
				added.add(amt);
			}
			else
			{
				//Add parent entity with empty AABB
				EntityAMTTactile parent = new EntityAMTTactile(this, amt.name,
						processOffset(header, globalOffset, amt.name, Vec3d.ZERO),
						new AxisAlignedBB(0, 0, 0, 0, 0, 0));
				entities.add(parent);
				added.add(parent);

				//Rename child objects to OBJ_child[n]
				matching.add(0, amt);
				for(int i = 0; i < matching.size(); i++)
				{
					matching.get(i).name += "_child"+i;
					matching.get(i).setParent(parent);
					added.add(matching.get(i));
				}

				//Continue iteration
				tempEntities.removeAll(matching);
				entities.addAll(matching);
			}
		}
		return added;
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
		if(getIsMirrored())
		{
			double xLength = Math.abs(aabb.maxX-aabb.minX);
			aabb = new AxisAlignedBB(-aabb.minX+2, aabb.minY, aabb.minZ, -aabb.maxX+2, aabb.maxY, aabb.maxZ);
		}

		//aabb = new AxisAlignedBB(-0.25, -0.25, -0.25, 0.25, 0.25, 0.25);
		Matrix4 mat = new Matrix4(getFacing());
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
		if(worldSupplier.get().isRemote)
			return;
		if(!initialized)
			initialized = init();
		if(!initialized)
			return;

		entities.forEach(EntityAMTTactile::defaultizeAnimation);
		applyAnimationPositions();
	}

	/**
	 * @param animation resource location of the animation
	 * @param time      time 0-1 of this animation
	 */
	public void update(@Nullable ResLoc animation, float time)
	{
		if(worldSupplier.get().isRemote)
			return;
		//Must be initialized before applying animation
		if(!initialized&&!(initialized = init()))
			return;

		if(animation==null)
		{
			applyAnimationPositions();
			return;
		}

		//Load a cached animation or from JSON
		IIAnimationCollisionMap anim;
		if(!animations.containsKey(animation))
			anim = loadAnimation(animation);
		else
			anim = animations.get(animation);

		if(anim==null)
			return;

		anim.apply(time);
		applyAnimationPositions();
	}

	private void applyAnimationPositions()
	{
		BlockPos handlerPos = getPos();
		Set<EntityAMTTactile> applied = new HashSet<>();
		for(EntityAMTTactile e : entities)
			applyEntityTransformRecursive(e, handlerPos, applied);
	}

	private void applyEntityTransformRecursive(EntityAMTTactile e, BlockPos handlerPos, Set<EntityAMTTactile> applied)
	{
		if(applied.contains(e))
			return;

		EntityAMTTactile parent = e.getParent();
		if(parent!=null)
			applyEntityTransformRecursive(parent, handlerPos, applied);

		e.prevPosX = e.posX;
		e.prevPosY = e.posY;
		e.prevPosZ = e.posZ;

		if(parent==null)
		{
			e.posX = handlerPos.getX()+e.offset.x+e.translation.x;
			e.posY = handlerPos.getY()+e.offset.y+e.translation.y;
			e.posZ = handlerPos.getZ()-0.5+e.offset.z+e.translation.z;
			e.rotationPitch = (float)e.rotation.x;
			e.rotationYaw = (float)e.rotation.y;
			e.setRotationRoll((float)e.rotation.z);
		}
		else
		{
			Vec3d relativeOffset = e.offset.subtract(parent.offset);
			e.rotationYaw = (float)(parent.rotationYaw+e.rotation.y);
			e.rotationPitch = (float)(parent.rotationPitch+e.rotation.x);
			e.setRotationRoll((float)(parent.getRotationRoll()+e.rotation.z));

			Vec3d angle = new Matrix4().setIdentity()
					.rotate(Math.toRadians(-e.rotationYaw), 0, 1, 0)
					.rotate(Math.toRadians(e.getRotationRoll()), 0, 0, 1)
					.rotate(Math.toRadians(e.rotationPitch), 1, 0, 0)
					.apply(relativeOffset.add(e.translation));

			e.posX = parent.posX+angle.x;
			e.posY = parent.posY+angle.y;
			e.posZ = parent.posZ+angle.z;
		}

		e.motionX = e.posX-e.prevPosX;
		e.motionY = e.posY-e.prevPosY;
		e.motionZ = e.posZ-e.prevPosZ;

		applied.add(e);
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
			IIAnimation animation = AMTLoader.loadAnimationServer(res);
			ANIMATIONS.put(res, animation);
		}

		//Attempt mapping and caching the animation
		IIAnimationCollisionMap mapped = null;
		if(anim!=null)
		{
			mapped = IIAnimationCollisionMap.create(entities, anim, getFacing(), getIsMirrored());
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

		SOURCE_CACHE.clear();
		HEADERS.clear();
		ANIMATIONS.clear();
	}

	//--- Tactile Handling ---//

	/**
	 * @return the world the Tactiles are in
	 */
	public World getWorld()
	{
		return worldSupplier.get();
	}

	/**
	 * Will recalculate the positions if the {@link ITactileListener} moved (for some reason)
	 *
	 * @return listener's position
	 */
	public BlockPos getPos()
	{
		return posSupplier.get();
	}

	public EnumFacing getFacing()
	{
		return facingSupplier.get();
	}

	public boolean getIsMirrored()
	{
		return mirroredSupplier.get();
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
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 11.10.2023
	 */
	public interface ITactileListener
	{
		/**
		 * @return tactile handler instance this listener is using
		 */
		@Nullable
		TactileManager getTactileHandler();

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
