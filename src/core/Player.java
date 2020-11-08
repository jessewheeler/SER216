package core;

/**
 * Player Class
 * @author Jesse Wheeler
 * @version 1.0
 */
public class Player {

    //INSTANCE METHODS
    protected String playerName;
    protected String playerToken;
    protected String playerColor;
    protected boolean isHuman;

    /** Empty constructor */
    public Player() {}

    /**
     * Human player constructor
     * @param name name of player
     * @param token token used by player
     */
    public Player(String name, String token, String color)
    {
        this.playerName  = name;
        this.playerToken = token;
        this.playerColor = color;
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
     * @return Player token color - used in GUI only
     */
    public String getPlayerColor() { return playerColor; }

    /**
     * @return true if player is human
     */
    public boolean isHuman() { return isHuman; }
}
