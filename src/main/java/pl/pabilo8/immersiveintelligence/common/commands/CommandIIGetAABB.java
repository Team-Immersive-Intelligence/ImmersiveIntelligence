package pl.pabilo8.immersiveintelligence.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 22/02/2024 - 7:41 PM
 */
public class CommandIIGetAABB extends CommandBase
{
    @Override
    public String getName() {
        return "aabb";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "Create AABB for the multiblock";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        try
        {
        } catch (Exception e) {
            if (e instanceof WrongUsageException) throw ((WrongUsageException)e);
            else throw new WrongUsageException(getUsage(sender));
        }
    }
}
