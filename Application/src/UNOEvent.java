package src;

import java.util.EventObject;

/**
 * UNOEvent sends updates from the model to the view
 *
 * @author Owen Renette 101223576
 * @version October 21, 2023
 */
public class UNOEvent extends EventObject {
    private Player player;
    private CardPile pile;

    /**
     * Initializes the UNOEvent
     * @param model The model of which the event is invoked
     * @param player The player of which the event is invoked
     */
    public UNOEvent(UNOModel model, Player player) {
        super(model);
        this.player = player;
        this.pile = null;
    }
    /**
     * Initializes the UNOEvent
     * @param model The model of which the event is invoked
     * @param pile The CardPile of which the event is invoked
     */
    public UNOEvent(UNOModel model, CardPile pile) {
        super(model);
        this.player = null;
        this.pile = pile;
    }

    /**
     * Initializes the UNOEvent
     * @param model The model of which the event is invoked
     */
    public UNOEvent(UNOModel model) {
        super(model);
        this.player = null;
        this.pile = null;
    }

    /**
     * Initializes the UNOEvent
     * @param model The model of which the event is invoked
     * @param player The player that is drawing the cards
     * @param pile The CardPile of which the event is invoked
     */
    public UNOEvent(UNOModel model, Player player, CardPile pile) {
        super(model);
        this.player = player;
        this.pile = pile;
    }

    /**
     * getPlayer returns the player for which the event was invoked
     * @return The Player of the event
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * getPile returns the player for which the event was invoked
     * @return The CardPile of the event
     */
    public CardPile getCardPile() {
        return this.pile;
    }
}
