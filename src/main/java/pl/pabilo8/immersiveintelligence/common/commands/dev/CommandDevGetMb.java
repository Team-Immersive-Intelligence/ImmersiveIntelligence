package pl.pabilo8.immersiveintelligence.common.commands.dev;

import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * Gets information about a multiblock the sender is looking at
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class CommandDevGetMb extends CommandIIBase
{
	public CommandDevGetMb(CommandTreeBase parent)
	{
		super(parent, "get_mb");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Gets the internal data of a multiblock player is looking at";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, 40f);
		if(traceResult==null||traceResult.typeOfHit==RayTraceResult.Type.MISS) return;
		TileEntity te = senderEntity.getEntityWorld().getTileEntity(traceResult.getBlockPos());
		if(te instanceof TileEntityMultiblockPart<?>)
		{
			TileEntityMultiblockPart<?> mb = (TileEntityMultiblockPart<?>)te;
			ITextComponent message = new TextComponentString(TextFormatting.GOLD+"ID: "+TextFormatting.RESET+mb.pos+" | ")
					.appendSibling(new TextComponentString(TextFormatting.GOLD+"Mirrored: "+TextFormatting.RESET+mb.mirrored+" | "))
					.appendSibling(new TextComponentString(TextFormatting.GOLD+"Facing: "+TextFormatting.RESET+mb.facing.name()));
			senderEntity.sendMessage(message);

			if(mb instanceof TileEntityMultiblockIIBase)
			{
				TileEntityMultiblockIIBase<?> iiMb = (TileEntityMultiblockIIBase<?>)mb;
				message = new TextComponentString(TextFormatting.GOLD+"POIs: "+TextFormatting.RESET);
				boolean anyPOIs = true;
				for(MultiblockPOI poi : MultiblockPOI.values())
					if(iiMb.isPOI(poi))
					{
						message.appendSibling(new TextComponentString(((anyPOIs)?"": ", ")+poi.name()));
						anyPOIs = false;
					}
				if(!anyPOIs)
					senderEntity.sendMessage(message);
			}
			if(mb instanceof IOwnableProperty)
			{
				IOwnableProperty property = (IOwnableProperty)mb.master();
				if(property!=null)
					senderEntity.sendMessage(new TextComponentString(TextFormatting.GOLD+"Owner: "+TextFormatting.RESET+property.getOwnerIdentity()));
			}
		}
	}
}
