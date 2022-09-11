package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorTile;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorVertical;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 29.04.2021
 */
public class ConveyorRubberVertical extends ConveyorVertical
{
	public static ResourceLocation texture_on = new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/conveyors/vertical");
	public static ResourceLocation texture_off = new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/conveyors/vertical_off");

	@Override
	public ResourceLocation getActiveTexture()
	{
		return texture_on;
	}

	@Override
	public ResourceLocation getInactiveTexture()
	{
		return texture_off;
	}

	@Override
	public Vec3d getDirection(TileEntity conveyorTile, Entity entity, EnumFacing facing)
	{
		return super.getDirection(conveyorTile, entity, facing).scale(1.75);
	}

	boolean renderBottomBelt(TileEntity tile, EnumFacing facing)
	{
		TileEntity te = tile.getWorld().getTileEntity(tile.getPos().add(0, -1, 0));
		if(te instanceof IConveyorTile&&((IConveyorTile)te).getConveyorSubtype()!=null)
			for(EnumFacing f : ((IConveyorTile)te).getConveyorSubtype().sigTransportDirections(te, ((IConveyorTile)te).getFacing()))
				if(f==EnumFacing.UP)
					return false;
		for(EnumFacing f : EnumFacing.HORIZONTALS)
			if(f!=facing&&isInwardConveyor(tile, f))
				return true;
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<BakedQuad> modifyQuads(List<BakedQuad> baseModel, @Nullable TileEntity tile, EnumFacing facing)
	{
		if(tile!=null&&this.renderBottomBelt(tile, facing))
		{
			TextureAtlasSprite sprite = ClientUtils.getSprite(isActive(tile)?ConveyorRubber.texture_on: ConveyorRubber.texture_off);
			TextureAtlasSprite spriteColour = ClientUtils.getSprite(getColouredStripesTexture());
			boolean[] walls = {renderBottomWall(tile, facing, 0), renderBottomWall(tile, facing, 1)};
			baseModel.addAll(ModelConveyor.getBaseConveyor(facing, .875f, new Matrix4(facing), ConveyorDirection.HORIZONTAL, sprite, walls, new boolean[]{true, false}, spriteColour, getDyeColour()));
		}
		return baseModel;
	}
}
