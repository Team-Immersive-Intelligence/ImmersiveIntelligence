package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ParticleGasCloud extends Particle {
	private final Fluid fluid;
	private final float initialSize;

	public ParticleGasCloud(World world, Vec3d position, float size, Fluid fluid) {
		super(world, position.x, position.y - 1.0D, position.z); // Lower the spawn point by 1 block
		this.fluid = fluid;
		this.initialSize = size * 2.0F; // Make the initial size larger

		// Set the max age of the particle
		this.particleMaxAge = (int) (80 * this.initialSize);

		// Get the color from the fluid
		int color = fluid.getColor();
		this.particleRed = (color >> 16 & 255) / 255.0F;
		this.particleGreen = (color >> 8 & 255) / 255.0F;
		this.particleBlue = (color & 255) / 255.0F;

		// Set the initial scale
		this.particleScale = size;
	}

	public static void spawnParticles(World world, Vec3d position, float size, Fluid fluid) {
		int particleCount = 10; // Adjust this to control how many particles to spawn
		for (int i = 0; i < particleCount; i++) {
			// Random offsets for spreading particles
			double offsetX = (world.rand.nextDouble() - 0.5) * size; // Spread in X
			double offsetY = (world.rand.nextDouble() - 0.5) * size; // Spread in Y
			double offsetZ = (world.rand.nextDouble() - 0.5) * size; // Spread in Z

			// Create a new particle at the offset position
			//ParticleGasCloud newParticle = new ParticleGasCloud(world, position.add(offsetX, offsetY, offsetZ), size, fluid);
			// Assuming you have a method to add the particle to the world or particle manager
			// Example: world.spawnParticle(newParticle);
			// Make sure to replace the above line with your specific method to register particles
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		// Slowly decrease the size of the particle as it ages
		float ageFactor = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleScale = this.initialSize * (1.0F - (ageFactor * 0.5F)); // Adjust the rate of size reduction
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		// Call the superclass render method or provide a custom rendering implementation
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}
}