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
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIMachineUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class FenceGateRenderer<T extends TileEntityGateBase<T>> extends IIMultiblockRenderer<T>
{
	private AMT[] model;
	IIMachineUpgradeModel fluidUpgrade, energyUpgrade;
	private IIAnimationCompiledMap open;

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
		open.apply(IIAnimationUtils.getAnimationProgress(
				te.openProgress, TileEntityGateBase.MAX_OPEN_PROGRESS,
				true, !te.open,
				1, 2, partialTicks)
		);

		if(fluidUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
		{
			fluidUpgrade.defaultize();
			fluidUpgrade.render(tes, buf);
		}
		else if(energyUpgrade.renderConstruction(te, tes, buf, partialTicks)==UpgradeStage.INSTALLED)
		{
			energyUpgrade.defaultize();
			energyUpgrade.render(tes, buf);
		}

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
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/packer_construction.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/block/multiblock/packer_construction.obj.amt")
		);
		fluidUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_REDSTONE_ACTIVATION, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_fluid"));
		energyUpgrade = new IIMachineUpgradeModel(IIContent.UPGRADE_RAZOR_WIRE, modelUpgrades,
				new ResourceLocation(ImmersiveIntelligence.MODID, "packer/upgrade_energy"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = IIAnimationUtils.disposeOf(model);
	}

	/*
	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			if(!te.formed||te.isDummy()||!te.getWorld().isBlockLoaded(te.getPos(), false))
				return;

			final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
			BlockPos blockPos = te.getPos();
			IBlockState state = getWorld().getBlockState(blockPos);

			state = state.getBlock().getActualState(state, getWorld(), blockPos);
			state = state.withProperty(IEProperties.DYNAMICRENDER, true);
			IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);
			IBlockState fenceState = te.getFenceState(null);

			boolean[] connections = new boolean[8];

			if(te.hasWorld())
			{
				EnumFacing fR = te.mirrored?te.facing.rotateYCCW(): te.facing.rotateY();
				EnumFacing fL = te.mirrored?te.facing.rotateY(): te.facing.rotateYCCW();

				for(int i = 0; i < 4; i++)
				{
					connections[i] = Utils.canFenceConnectTo(te.getWorld(),
							blockPos.down().up(i), fL, state.getMaterial()
					);
					connections[i+4] = Utils.canFenceConnectTo(te.getWorld(),
							blockPos.down().up(i).offset(fR, 7), fR, state.getMaterial()
					);
				}
			}
			else
				connections = new boolean[]{false, false, false, false, false, false, false, false};


			*//*IBlockState fenceStateR = te.getFenceState();
			IBlockState fenceStateL = te.getFenceState();
*//*
			IBlockState fenceStateR = te.getFenceState(EnumFacing.WEST);
			IBlockState fenceStateL = te.getFenceState(EnumFacing.EAST);
			IBakedModel modelFence = blockRenderer.getBlockModelShapes().getModelForState(fenceState);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldRenderer = tessellator.getBuffer();

			ClientUtils.bindAtlas();
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+0.5, y-1, z+0.5);
			//GlStateManager.translate(.5, 1.5, .5);

			float angle = MathHelper.clamp(te.gateAngle+(partialTicks*(te.open?3.5f: -6f)), 0f, 115f);

			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			if(Minecraft.isAmbientOcclusionEnabled())
				GlStateManager.shadeModel(7425);
			else
				GlStateManager.shadeModel(7424);

			GlStateManager.rotate(te.facing.getHorizontalAngle()+(te.mirrored?0: 180), 0, 1, 0);

			if(te.facing.getAxis()==Axis.X)
				GlStateManager.translate(-7, 0, 0);


			GlStateManager.pushMatrix();

			// TODO: 23.12.2021 efficiency
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX()-0.5, -blockPos.getY(), -blockPos.getZ()-0.5);
			worldRenderer.color(255, 255, 255, 255);
			for(int i = 0; i < 4; i++)
				blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), modelFence,
						connections[te.facing.getAxis()==Axis.X?(i+4): i]?fenceStateR: fenceState,
						blockPos.up(i), worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();

			GlStateManager.rotate(90+angle, 0, 1, 0);
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
			worldRenderer.color(255, 255, 255, 255);
			blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			GlStateManager.popMatrix();

			GlStateManager.translate(7, 0, 0);

			GlStateManager.pushMatrix();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX()-0.5, -blockPos.getY(), -blockPos.getZ()-0.5);
			worldRenderer.color(255, 255, 255, 255);
			for(int i = 0; i < 4; i++)
				blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), modelFence,
						connections[te.facing.getAxis()==Axis.X?i: (i+4)]?fenceStateL: fenceState,
						blockPos.up(i), worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();

			GlStateManager.rotate(-90-angle, 0, 1, 0);
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
			worldRenderer.color(255, 255, 255, 255);
			blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			GlStateManager.popMatrix();

			RenderHelper.enableStandardItemLighting();

			GlStateManager.popMatrix();
		}
	}
	*/
}
