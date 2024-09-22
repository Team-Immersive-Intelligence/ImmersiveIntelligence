package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelBinoculars;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;

/**
 * @author Pabilo8
 * @since 20.01.2021
 */
@SideOnly(Side.CLIENT)
@RegisteredItemRenderer(name = "items/tools/binoculars")
public class BinocularsRenderer implements IReloadableModelContainer<BinocularsRenderer>
{
	public static ModelBinoculars model = new ModelBinoculars();
	public static BinocularsRenderer INSTANCE = new BinocularsRenderer().subscribeToList("binoculars");
	private final static String[] TEXTURES = new String[]{
			ImmersiveIntelligence.MODID+":textures/items/binoculars/model/binoculars.png",
			ImmersiveIntelligence.MODID+":textures/items/binoculars/model/binoculars_infrared_off.png",
			ImmersiveIntelligence.MODID+":textures/items/binoculars/model/binoculars_infrared_on.png"
	};

	public void render(int type, ModelRenderer head, boolean sneaking)
	{
		GlStateManager.pushMatrix();
		int i = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

		GlStateManager.translate(0, (head.rotationPointY+head.offsetY)/16f+(sneaking?0.2f: 0f), 0);


		GlStateManager.rotate(TmtUtil.TMTToAngle(head.rotateAngleY), 0, 1, 0);
		GlStateManager.rotate(TmtUtil.TMTToAngle(head.rotateAngleX), 1, 0, 0);
		GlStateManager.rotate(TmtUtil.TMTToAngle(head.rotateAngleZ), 0, 0, 1);

		ClientUtils.bindTexture(TEXTURES[type]);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();

		GlStateManager.bindTexture(i);
		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelBinoculars();
	}
}
