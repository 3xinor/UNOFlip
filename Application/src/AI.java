package src;

/**
 * AI interface model for UNO game
 * Declares the required methods necessary to implement and AI model into the UNO Flip game
 *
 * @author Dave Exinor 101184298
 * @version November 15, 2023
 */
public interface AI {
    /**
     * smartPlayCard determines which Card in the hand an AI Player will play
     * @param discardedCard the card on the top on the discard pile
     * @param wildColour The colour of the current wild card (null if not active)
     * @return a Card in AI players hand
     */
    Card smartPlayCard(Card discardedCard, Colours wildColour);

    /**
     * smartDecision Determines whether AI Player will play a card or draw from deck
     * @param discardedCard the card on the top on the discard pile
     * @return enumerated AIDecision
     */
    AIDecisions smartDecision(Card discardedCard);

    /**
     * smartChallenge Determines whether AI Player will challenge the player or not
     * @return True if challenged, False if not
     */
    boolean smartChallenge(UNOModel model);

    /**
     * smartColourSelect Determines which colour the AI Player will change the colour to
     * @param light True if the game is on light side, False if on the dark side
     * @return the colour selected
     */
    Colours smartColourSelect(boolean light);

    /**
     * smartPlayDrawnCard Determines if the AI Player will play their drawn card.
     * @return True if the AI will play the drawn card, False otherwise
     */
    boolean smartPlayDrawnCard(Card top);
}