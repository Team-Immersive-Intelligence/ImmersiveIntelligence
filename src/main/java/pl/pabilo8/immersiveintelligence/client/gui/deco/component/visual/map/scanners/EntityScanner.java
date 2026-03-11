package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.CustomMapData;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Scanner for detecting entities.
 */
public class EntityScanner extends MapScanner
{
	private final List<EntityFilter> filters = new ArrayList<>();
	private Class<? extends Entity> entityClass = Entity.class;

	public EntityScanner(String layerName)
	{
		super(layerName);
	}

	/**
	 * Clear all existing entity filters.
	 */
	public EntityScanner clearFilters()
	{
		filters.clear();
		return this;
	}

	/**
	 * Add a custom entity filter with custom marker appearance.
	 */
	public EntityScanner withFilter(Predicate<Entity> predicate, ResLoc markerTexture,
									IIColor color, float size)
	{
		filters.add(new EntityFilter(predicate, markerTexture, color, size));
		return this;
	}

	/**
	 * Filter by entity class.
	 */
	public EntityScanner withEntityClass(Class<? extends Entity> entityClass)
	{
		this.entityClass = entityClass;
		return this;
	}

	@Override
	protected void scanArea(DecoMapDisplay mapDisplay, World world, CustomMapData mapData,
							MapLayerBuilder layer, int minX, int maxX, int minZ, int maxZ)
	{
		//Calculate bounding box for entity search
		AxisAlignedBB searchBox = new AxisAlignedBB(minX, 0, minZ, maxX, world.getHeight(), maxZ);

		//Get entities in the area
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, searchBox);

		for(Entity entity : entities)
		{
			if(!entityClass.isAssignableFrom(entity.getClass()))
				continue;

			//Find the first matching filter
			EntityFilter matchingFilter = filters.stream()
					.filter(filter -> filter.predicate.test(entity))
					.findFirst().orElse(null);

			//Skip if no filter matches
			if(matchingFilter==null)
				continue;

			//Add marker at entity position
			int entityX = (int)entity.posX;
			int entityZ = (int)entity.posZ;

			ResLoc texture = matchingFilter.markerTexture!=null?matchingFilter.markerTexture: markerTexture;
			IIColor color = matchingFilter.color!=null?matchingFilter.color: markerColor;
			float size = matchingFilter.size!=null?matchingFilter.size: markerSize;

			if(texture!=null)
				layer.withSprite(texture, true, entityX, entityZ,
						size, color, 180-entity.rotationYaw-90
				);
		}
	}

	/**
	 * Internal class representing an entity filter with custom marker appearance.
	 */
	private static class EntityFilter
	{
		final Predicate<Entity> predicate;
		final ResLoc markerTexture;
		final IIColor color;
		final Float size;

		EntityFilter(Predicate<Entity> predicate, ResLoc markerTexture,
					 IIColor color, Float size)
		{
			this.predicate = predicate;
			this.markerTexture = markerTexture;
			this.color = color;
			this.size = size;
		}
	}
}
