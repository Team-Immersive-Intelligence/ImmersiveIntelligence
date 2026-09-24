package pl.pabilo8.immersiveintelligence.client.render.item;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

/**
 * Loads and renders the Engineer's Clipboard AMT model outside its flat GUI representation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
@RegisteredItemRenderer(name = "items/tools/clipboard")
public class ClipboardRenderer extends IIItemRendererAMT<ItemIIClipboard>
{
	private AMTModel model;

	public ClipboardRenderer()
	{
		super(IIContent.itemClipboard, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/clipboard").withExtension(ResLoc.EXT_OBJ), true);
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		model.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel obj, AMTModelHeader header)
	{
		this.model = new AMTModel(DefaultVertexFormats.ITEM, obj, header, null);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}
}
