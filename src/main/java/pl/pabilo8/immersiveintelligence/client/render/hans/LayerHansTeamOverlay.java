package pl.pabilo8.immersiveintelligence.client.render.hans;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 02.05.2021
 */
public class LayerHansTeamOverlay implements LayerRenderer<EntityHans>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/entity/hans_team_overlay.png");
	private final HansRenderer hansRenderer;

	public LayerHansTeamOverlay(HansRenderer renderer)
	{
		this.hansRenderer = renderer;
	}

	public void doRenderLayer(EntityHans hans, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		Team team = hans.getTeam();
		if(team!=null)
		{
			this.hansRenderer.bindTexture(TEXTURE);
			IIColor.fromTextFormatting(team.getColor()).glColor();
			hansRenderer.getMainModel().render(hans, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	public boolean shouldCombineTextures()
	{
		return true;
	}
}