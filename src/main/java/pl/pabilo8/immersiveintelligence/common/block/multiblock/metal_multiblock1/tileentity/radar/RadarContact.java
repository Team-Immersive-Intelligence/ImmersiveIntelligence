package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * Stores a non-entity radar contact and its data and marker expiry times.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.10.2026
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RadarContact implements INBTSerializable<NBTTagCompound>
{
	private String name = "";
	private Vec3d position = Vec3d.ZERO;
	private long expiresAt, markerExpiresAt;

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT().withString("name", name).withVec3d("position", position)
				.withLong("expires_at", expiresAt).withLong("marker_expires_at", markerExpiresAt).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT data = EasyNBT.wrapNBT(nbt);
		name = data.getString("name");
		position = data.getVec3d("position");
		expiresAt = data.getLong("expires_at");
		markerExpiresAt = data.getLong("marker_expires_at");
	}
}
