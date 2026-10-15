package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.CustomMapData;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar.RadarContact;

import java.util.function.Supplier;

/**
 * Shows server-synchronised radar contacts until their marker expiry time.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.10.2026
 */
public class RadarContactScanner extends MapScanner
{
	private final Supplier<? extends Iterable<RadarContact>> contacts;

	/**
	 * Creates a scanner for a tile contact collection.
	 */
	public RadarContactScanner(String layerName, Supplier<? extends Iterable<RadarContact>> contacts)
	{
		super(layerName);
		this.contacts = contacts;
		this.updateInterval = 1;
	}

	@Override
	protected void scanArea(DecoMapDisplay mapDisplay, World world, CustomMapData mapData,
							MapLayerBuilder layer, int minX, int maxX, int minZ, int maxZ)
	{
		long now = world.getTotalWorldTime();
		for(RadarContact contact : contacts.get())
		{
			long remaining = contact.getMarkerExpiresAt()-now;
			if(remaining <= 0)
				continue;
			Vec3d position = contact.getPosition();
			int x = MathHelper.floor(position.x), z = MathHelper.floor(position.z);
			if(x < minX||x > maxX||z < minZ||z > maxZ||markerTexture==null)
				continue;
			float alpha = MathHelper.clamp(remaining/(float)TileEntityRadar.CONTACT_MARKER_DURATION, 0f, 1f);
			layer.withSprite(markerTexture, true, x, z, markerSize, markerColor.withAlpha(alpha), markerRotation);
		}
	}
}
