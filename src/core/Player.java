package core;

/**
 * Player Class
 */
public class Player {

    //INSTANCE METHODS
    protected String playerName;
    protected String playerToken;
    protected boolean isHuman;

    /** Empty constructor */
    public Player() {}

    /**
     * Human player constructor
     * @param name name of player
     * @param token token used by player
     */
    public Player(String name, String token)
    {
        this.playerName  = name;
        this.playerToken = token;
        isHuman = true;
    }

    /**
     * @return Player Token
     */
    public String getPlayerToken() { return playerToken; }

    /**
     * @return Player Name
     */
    public String getPlayerName()
    {
        return playerName;
    }

    /**
     * @return true if player is human
     */
    public boolean isHuman() { return isHuman; }
}
