package org.pulsebot.injection.interfaces;

import org.pulsebot.api.NPC;
import org.pulsebot.api.Player;

public interface IClient {

	public IPlayer[] getPlayers();
	public INPC[] getNPCs();
    public int getCameraYaw();
	
}
