package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Set your faction colour.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionSetColor extends CommandFactionBase
{
	public CommandFactionSetColor(CommandTreeBase parent)
	{
		super(parent, "color", PermissionCategory.MODIFY_INSIGNIA);
	}

	@Override
	public String getSyntax()
	{
		return "<dye_color|#RRGGBB>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Set your faction colour";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return args.length==1?getListOfStringsMatchingLastWord(args, Arrays.stream(EnumDyeColor.values())
				.map(EnumDyeColor::getName).toArray(String[]::new)): Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		requireArgumentCount(sender, args, 1);
		OwnerIdentity faction = getFaction(sender);
		String value = args[0].toLowerCase(Locale.ROOT);
		String hex = value.startsWith("#")?value.substring(1): value;
		IIColor color = null;
		if(hex.matches("[0-9a-f]{6}"))
			color = IIColor.fromHex(hex);
		else
		{
			for(EnumDyeColor dye : EnumDyeColor.values())
				if(dye.getName().equalsIgnoreCase(value))
				{
					color = IIColor.fromDye(dye);
					break;
				}
			if(color==null)
			{
				TextFormatting formatting = TextFormatting.getValueByName(value);
				if(formatting!=null&&formatting.isColor())
					color = IIColor.fromTextFormatting(formatting);
			}
		}
		if(color==null)
			throw new CommandException("Invalid colour. Use a dye colour or #RRGGBB.");
		faction.withColor(color);
		DiplomacyHandler.getInstance(false).saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Faction colour set to #"+color.getHexRGB()));
	}
}
