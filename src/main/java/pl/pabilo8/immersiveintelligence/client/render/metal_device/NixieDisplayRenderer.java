package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTText;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie.TileEntityDoubleNixieDisplay;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie.TileEntityNixieDisplayBase;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie.TileEntityQuadNixieDisplay;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import java.util.Arrays;

/**
 * Renders Nixie characters and glass from the model and its AMT header.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.10.2026
 * @since 26.10.2026
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "device/nixie_display_double", clazz = TileEntityDoubleNixieDisplay.class)
@RegisteredTileRenderer(name = "device/nixie_display_quad", clazz = TileEntityQuadNixieDisplay.class)
public class NixieDisplayRenderer extends IITileRenderer<TileEntityNixieDisplayBase>
{
	private AMTModel model;
	private AMTText[] characters;

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		//Load model
		ResLoc res = IIReference.RES_BLOCK_MODEL.with("metal_device/nixie_display_")
				.with(state.getValue(IIContent.blockMetalDevice.property)==IIBlockTypes_MetalDevice.DOUBLE_NIXIE_DISPLAY?"double": "quad")
				.withExtension(ResLoc.EXT_OBJ);
		this.model = new AMTModel(DefaultVertexFormats.BLOCK, res, AMTModelHeader::createParts);

		//Get AMTText parts
		this.characters = Arrays.stream(this.model.getChildrenRecursive())
				.filter(part -> part instanceof AMTText)
				.toArray(AMTText[]::new);
		for(AMTText character : this.characters)
			character.setShader(Shaders.COLOR_NO_LIGHTMAP, character.getColor().red/255f,
					character.getColor().green/255f, character.getColor().blue/255f);
	}

	@Override
	public void draw(TileEntityNixieDisplayBase te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardRotation(te.getFacing());
		//Set displayed characters
		for(int i = 0; i < characters.length; i++)
			characters[i].setText(i < te.displayedText.length()?String.valueOf(te.displayedText.charAt(i)): "");

		//Render the model
		model.render(tes, buf);
	}

	@Override
	protected void nullifyModels()
	{
		model = AMTUtils.disposeOf(model);
		characters = null;
	}
}
