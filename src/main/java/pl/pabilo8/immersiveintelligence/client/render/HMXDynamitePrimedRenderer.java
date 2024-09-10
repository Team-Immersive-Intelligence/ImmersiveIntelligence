package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHMXDynamitePrimed;

/**
 * Renderer for HMX Dynamite Primed
 */
public class HMXDynamitePrimedRenderer extends Render<EntityHMXDynamitePrimed> {

    public static final String texture = ImmersiveIntelligence.MODID + ":textures/blocks/hmx/all.png";
    public static final ModelSkyCrate model = new ModelSkyCrate(); // Create or use a block model for HMX Dynamite

    public HMXDynamitePrimedRenderer(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F; // Optional: adjust shadow size
    }

    @Override
    public void doRender(EntityHMXDynamitePrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();

        // Translate the entity to the correct position in the world
        GlStateManager.translate(x, y, z);

        // Scale and rotate the entity for proper orientation
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);

        // Bind the texture for rendering
        this.bindEntityTexture(entity);

        // Render the model of the dynamite
        model.render(entity, 0, 0, 0, 0, 0, 0.0625F);

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHMXDynamitePrimed entity) {
        return new ResourceLocation(texture); // The texture for HMX dynamite
    }
}
