package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.EventHandler;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * Sent to clients to update a gamerule value.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.09.2025
 */
public class MessageIIGameruleUpdate extends IIMessage
{
	private String gameRuleName, gameRuleValue;

	public MessageIIGameruleUpdate()
	{
	}

	public MessageIIGameruleUpdate(String gameRuleName, String gameRuleValue)
	{
		this.gameRuleName = gameRuleName;
		this.gameRuleValue = gameRuleValue;
	}

	public MessageIIGameruleUpdate(String gameRuleName, GameRules rules)
	{
		this(gameRuleName, rules.getString(gameRuleName));
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		//Carver Donut
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		GameRules rules = world.getGameRules();
		rules.setOrCreateGameRule(gameRuleName, gameRuleValue);
		EventHandler.applyGameRuleValue(rules, gameRuleName);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		gameRuleName = readString(buf);
		gameRuleValue = readString(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeString(buf, gameRuleName);
		writeString(buf, gameRuleValue);
	}
}
