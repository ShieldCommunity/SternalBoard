package com.xIsm4.plugins.nCache;

import java.util.UUID;

public abstract class PlayerCache {

    public abstract void add(String pName, UUID pUUID, int pPlayerID);

    public abstract Integer getPlayerID(String pName);

    public abstract Integer getPlayerID(UUID pUUID);

    public abstract String getName(int pID);

    public abstract UUID getUUID(int pID);

    public abstract void updateUUID(int pPlayerID, UUID pNewUUID);

    public abstract void updateName(int pPlayerID, String pNewPlayerName);

    public abstract void deletePlayer(int pPlayerID);
}