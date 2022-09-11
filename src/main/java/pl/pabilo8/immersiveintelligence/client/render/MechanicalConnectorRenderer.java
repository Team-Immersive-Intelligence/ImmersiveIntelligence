package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltData;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
@SideOnly(Side.CLIENT)
public class MechanicalConnectorRenderer extends TileEntitySpecialRenderer<TileEntityMechanicalConnectable> implements IReloadableModelContainer<MechanicalConnectorRenderer>
{
	public static HashMap<Connection, MotorBeltData> cache = new HashMap<>();

	@Nullable
	public static MotorBeltData getBeltData(Connection connection, TileEntityMechanicalConnectable start, TileEntityMechanicalConnectable end)
	{
		return cache.containsKey(connection)?cache.get(connection): MotorBeltData.createBeltData(connection, start, end);
	}

	@Override
	public void render(TileEntityMechanicalConnectable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.color(255, 255, 255, 255);
		GlStateManager.translate((float)x+0.5f, (float)y+0.5f, (float)z+0.5f);

		if(te!=null)
		{

			double world_rpm = (((double)te.getConnectorWorld().getTotalWorldTime())%(double)RotaryUtils.getRPMMax())/(double)RotaryUtils.getRPMMax();
			Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(te.getWorld(), Utils.toCC(te));
			if(outputs!=null)
			{
				for(Connection con : outputs)
				{
					TileEntity end = getWorld().getTileEntity(con.end);
					if(end instanceof TileEntityMechanicalConnectable)
					{
						MotorBeltData data = getBeltData(con, te, (TileEntityMechanicalConnectable)end);
						RotaryUtils.tessellateMotorBelt(data, te.getOutputRPM(), world_rpm);
					}
				}
			}

		}
		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		cache.clear();
	}
}
