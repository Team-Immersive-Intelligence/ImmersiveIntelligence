package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.MechanicalPump;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalPump;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.05.2019
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "mechanical/pump", clazz = TileEntityMechanicalPump.class)
public class MechanicalPumpRenderer extends IITileRenderer<TileEntityMechanicalPump>
{
	private IIAnimationCompiledMap rotation, pumping;
	private AMTModel model;

	@Override
	public void draw(TileEntityMechanicalPump te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		if(te.dummy)
			return;

		//pumping calculation
		boolean b = te.getWorld().isBlockIndirectlyGettingPowered(te.getPos()) > 0;
		int a = (int)(30-(25*(te.rotation.getRotationSpeed()/(float)MechanicalPump.rpmBreakingMax)));
		float pumpingProgress = 0;
		if(te.rotation.getRotationSpeed() > 0)
			pumpingProgress = ((te.getWorld().getTotalWorldTime()%a)+partialTicks)/(float)a*(b?1f: 0f);

		//apply animation
		rotation.apply(IIRotaryUtils.getDisplayRotation(te, te.rotation, partialTicks));
		pumping.apply(pumpingProgress);

		GlStateManager.translate(0, 1, 0);

		//apply rotation for block facing
		applyStandardRotation(te.getFacing());

		//render
		model.render(tes, buf);

	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);

		this.rotation = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "mechanical_pump/rotation"));
		this.pumping = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "mechanical_pump/pumping"));
	}

	@Override
	protected void nullifyModels()
	{
		rotation = pumping = null;
		this.model = IIAnimationUtils.disposeOf(this.model);
	}
}
