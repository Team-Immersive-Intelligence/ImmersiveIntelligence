package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeCachedModel.MachineCachedUpgradeModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/arithmetic_logic_machine", clazz = TileEntityArithmeticLogicMachine.class)
public class ArithmeticLogicMachineRenderer extends IIMultiblockRenderer<TileEntityArithmeticLogicMachine>
{
	private AMTCachedModel<TileEntityArithmeticLogicMachine> model;
	private AMTUpgradeCachedModel<TileEntityArithmeticLogicMachine> upgradeCircuitRacks, upgradeMemory;
	private IIAnimationCachedMap animationDrawer, animationDoor, animationKeyboard;

	@Override
	public void drawAnimated(TileEntityArithmeticLogicMachine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Get model from cache
		StringBuilder cacheKey = new StringBuilder();
		for(int i = 0; i < MultiblockArithmeticLogicMachine.CIRCUITS_UPGRADED; i++)
		{
			ItemStack stack = te.inventory.get(i);
			cacheKey.append(stack.isEmpty()?"_": IIContent.itemCircuit.stackToSub(te.inventory.get(i)).ordinal());
		}
		this.model.getVariant(te, cacheKey.toString());

		//Reset model to default state
		this.model.defaultize();
		this.upgradeCircuitRacks.apply(te, tes, buf, partialTicks);
		this.upgradeMemory.apply(te, tes, buf, partialTicks);

		animationDrawer.apply(te.drawer.getProgress(partialTicks));
		animationDoor.apply(te.door.getProgress(partialTicks));
		animationKeyboard.apply(te.keyboard.getProgress(partialTicks));

		//Draw
		applyStandardMirroring(te, true);

		//Render
		model.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//reset model to default state
		model.defaultize();
		//Render
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		ResLoc resFolder = IIReference.RES_II.with("models/block/multiblock/arithmetic_logic_machine/");

		//model loading start
		final Pattern CIRCUIT_PATTERN = Pattern.compile("circuits/circuit([1-6])$");
		AMTCachedModelBuilder<TileEntityArithmeticLogicMachine> modelBuilder =
				AMTCachedModelBuilder.startTileEntityModel(TileEntityArithmeticLogicMachine.class)
						.withModel(model)
						.withHeader(resFolder.with("arithmetic_logic_machine.obj.amt"))
						.withModel(resFolder.with("circuits.obj"))
						.withTextureProvider((res, tile) -> {
							//Default
							if(tile==null||!tile.hasWorld())
								return ClientUtils.getSprite(res);

							Matcher matcher = CIRCUIT_PATTERN.matcher(res.getResourcePath());
							if(matcher.find())
							{
								Integer circuitID = Integer.valueOf(matcher.group(1));
								ItemStack stack = tile.inventory.get(MathHelper.clamp(circuitID-1, 0, tile.inventory.size()-1));

								if(!stack.isEmpty()&&stack.getItem() instanceof ItemIIFunctionalCircuit)
									return ClientUtils.getSprite(ResLoc.of(res).replace(String.valueOf(circuitID),
											"_"+IIContent.itemCircuit.stackToSub(stack).tier.getName()));
							}

							return ClientUtils.getSprite(res);
						});

		//upgrade models
		this.upgradeCircuitRacks = new MachineCachedUpgradeModelBuilder<>(modelBuilder)
				.withUpgrade(IIContent.UPGRADE_CIRCUIT_RACKS)
				.withConstructionModel(resFolder.with("upgrades/circuit_racks.obj"))
				.withAnimation(ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/upgrade_circuit_racks"))
				.build();
		this.upgradeMemory = new MachineCachedUpgradeModelBuilder<>(modelBuilder)
				.withUpgrade(IIContent.UPGRADE_MEMORY)
				.withConstructionModel(resFolder.with("upgrades/memory.obj"))
				.withAnimation(ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/upgrade_memory"))
				.build();

		//finish main model
		this.model = modelBuilder.build();

		//animations
		this.animationDrawer = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/open_drawer"));
		this.animationDoor = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/open_door"));
		this.animationKeyboard = IIAnimationCachedMap.create(this.model, ResLoc.of(IIReference.RES_II, "arithmetic_logic_machine/open_keyboard"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		AMTUtils.disposeOf(model);
	}
}
