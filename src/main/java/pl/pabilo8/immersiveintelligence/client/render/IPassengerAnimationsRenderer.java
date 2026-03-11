package pl.pabilo8.immersiveintelligence.client.render;


import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.10.2025
 */
public interface IPassengerAnimationsRenderer<T extends Entity>
{
	boolean handleBipedRotations(ModelBiped model, T entity, EntityLivingBase passenger, float partialTicks);
}
