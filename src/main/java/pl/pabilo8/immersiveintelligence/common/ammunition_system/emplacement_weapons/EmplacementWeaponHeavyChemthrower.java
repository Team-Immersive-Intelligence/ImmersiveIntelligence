package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

public class EmplacementWeaponHeavyChemthrower extends EmplacementWeapon
{
	int setupDelay = 0;

	private static final Runnable INSERTER_ANIM_LENS = () -> {
		ClientUtils.bindTexture(EmplacementRenderer.textureInfraredObserver);
		GlStateManager.translate(-0.3125, 0.4225, 1.375);
		GlStateManager.rotate(180, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.lensModel)
			mod.render();
	};

	@SideOnly(Side.CLIENT)
	private static final Runnable INSERTER_ANIM_NONE = () -> {
	};

	@Override
	public String getName()
	{
		return "heavy_chemthrower";
	}

	@Override
	public IngredientStack[] getIngredientsRequired()
	{
		return new IngredientStack[]{
				new IngredientStack(new ItemStack(IEContent.itemMaterial, 4, 15)),
				new IngredientStack("blockSteel", 1),
				new IngredientStack("plateSteel", 4),
				new IngredientStack(new ItemStack(IIContent.blockMetalDevice, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())),
				new IngredientStack("engineElectricSmall", 2)
		};
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		super.aimAt(yaw,pitch);
	}

	public boolean isSetUp(boolean door)
	{
		return setupDelay==(door?300: 0);
	}

	@Override
	public void doSetUp(boolean door)
	{
		if(door)
		{
			if(setupDelay < 300)
				setupDelay += 1;
		}
		else
		{
			if(!isAimedAt(0, -90))
			{
				aimAt(0, -90);
				return;
			}
			if(setupDelay > 0)
				setupDelay -= 1;
		}
	}

	@Override
	public void tick()
	{

	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("yaw", yaw);
		tag.setFloat("pitch", pitch);
		tag.setInteger("setupDelay", setupDelay);
		return tag;
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return te.isDoorOpened;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		yaw = tagCompound.getFloat("yaw");
		pitch = tagCompound.getFloat("pitch");
		setupDelay = tagCompound.getInteger("setupDelay");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileEntityEmplacement te, float partialTicks)
	{
		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		pp = pitch+Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks*pitchTurnSpeed;
		yy = yaw+Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks*yawTurnSpeed;
		float setupProgress = (MathHelper.clamp(setupDelay+(pitch==-90?(te.isDoorOpened?(te.progress==Emplacement.lidTime?partialTicks: 0): -partialTicks): 0), 0, 300)/(float)300);

		GlStateManager.enableBlend();


		ClientUtils.bindTexture(EmplacementRenderer.textureHeavyChemthrower);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.baseModel)
			mod.render();

		GlStateManager.rotate(90,0,1,0);
		//GlStateManager.translate(0, 24/16f, 3/16f);
		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.turretModel)
			mod.render();
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelStartModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelMidModel)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelHeavyChemthrower.barrelEndModel)
			mod.render();

		GlStateManager.popMatrix();
	}
}
