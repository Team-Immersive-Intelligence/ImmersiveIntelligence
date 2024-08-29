package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIMachineUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class FenceGateRenderer<T extends TileEntityGateBase<T>> extends IIMultiblockRenderer<T>
{
	private AMT[] model;
	IIMachineUpgradeModel redstoneUpgrade, razorUpgrade;
	private IIAnimationCompiledMap open, redstone, razor;

	public FenceGateRenderer(String name)
	{
		super();
		subscribeToList(name);
	}

	@Override
	public void drawAnimated(T te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardRotation(te.facing);
		if(!te.mirrored)
			mirrorRender();

		//Apply door animation
		open.apply(te.gate.getProgress(partialTicks));

		//Apply upgrade or their construction animations
		redstone.apply((redstoneUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)?1: 0);
		razor.apply((razorUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)?1: 0);

		for(AMT amt : model)
			amt.render(tes, buf);

		drawConnectedFences(te, buf, tes);

		if(!te.mirrored)
			unMirrorRender();
	}

	private void drawConnectedFences(T te, BufferBuilder buf, Tessellator tes)
	{
		boolean[] connections = new boolean[8];
		BlockPos blockPos = te.getPos();
		IBlockState state = getWorld().getBlockState(blockPos);
		state = state.getBlock().getActualState(state, getWorld(), blockPos);
		state = state.withProperty(IEProperties.DYNAMICRENDER, true);

		EnumFacing fR = te.mirrored?te.facing.rotateYCCW(): te.facing.rotateY();
		EnumFacing fL = te.mirrored?te.facing.rotateY(): te.facing.rotateYCCW();

		IBlockState fenceState = te.getFenceState(null);
		IBlockState fenceStateR = te.getFenceState(EnumFacing.WEST);
		IBlockState fenceStateL = te.getFenceState(EnumFacing.EAST);

		IBakedModel modelFence = IIAnimationUtils.getBRD().getBlockModelShapes().getModelForState(fenceState);

		for(int i = 0; i < 4; i++)
		{
			connections[i] = Utils.canFenceConnectTo(te.getWorld(),
					blockPos.down().up(i), fL, state.getMaterial());
			connections[i+4] = Utils.canFenceConnectTo(te.getWorld(),
					blockPos.down().up(i).offset(fR, 7), fR, state.getMaterial());
		}

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		buf.setTranslation(0.01, -1.01, 0.01);
		buf.color(255, 255, 255, 255);
		for(int i = 0; i < 4; i++)
		{
			IIAnimationUtils.getBRD().getBlockModelRenderer().renderModel(te.getWorld(), modelFence,
					connections[te.facing.getAxis()==Axis.X?(i+4): i]?fenceStateR: fenceState,
					new BlockPos(0, i, 0), buf, true);
			IIAnimationUtils.getBRD().getBlockModelRenderer().renderModel(te.getWorld(), modelFence,
					connections[te.facing.getAxis()==Axis.X?i: (i+4)]?fenceStateL: fenceState,
					new BlockPos(7, i, 0), buf, true);
		}

		buf.setTranslation(0.0D, 0.0D, 0.0D);
		tes.draw();
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
		open = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "gate/open"));

		AMT[] modelUpgrades = IIAnimationUtils.getAMTFromRes(
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/gate_construction.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/gate_construction.obj.amt")
		);

		redstone = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "gate/redstone"));
		razor = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "gate/razor"));

		redstoneUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_REDSTONE_ACTIVATION, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "gate/upgrade_redstone"));
		razorUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_RAZOR_WIRE, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "gate/upgrade_razor"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = IIAnimationUtils.disposeOf(model);
	}
}
