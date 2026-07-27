package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.LawForm;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandFactionSetLawForm extends CommandIIBase
{
	public CommandFactionSetLawForm(CommandTreeBase parent)
	{
		super(parent, "lawform");
	}

	@Override
	public String getSyntax()
	{
		return "<lawform>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Change your faction's law form";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
			return getTabCompletionsEnum(args, LawForm.class);
		return Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");

		if(args.length < 1)
			throw new CommandException("Specify a law form.");

		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);
		OwnerIdentity faction = diplomacy.getOwnerIdentityForEntity((EntityPlayer)sender);
		if(faction.isInvalid()||!faction.isOwner(((EntityPlayer)sender).getUniqueID()))
			throw new CommandException("You must be an owner to change the faction's law form.");

		try
		{
			faction.withLawForm(LawForm.valueOf(String.join(" ", args)));
			diplomacy.saveAndSyncIdentity(faction);
			sender.sendMessage(new TextComponentString("Faction law form set to "+args[0]));
		} catch(Exception e)
		{
			throw new CommandException("Invalid law form.");
		}
	}
}
