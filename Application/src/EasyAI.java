package src;

import java.util.Random;

/**
 * EasyAI models an "AI" player with "Easy: level of play and play-style
 * How it thinks:
 * It is a passive player, meaning it does not like to hold onto wild cards, and does not like to challenge other players
 * It will play wild cards first.
 * It will then prioritize coloured cards over matching value.
 * It has no bias towards whether it want to flip the play-side or not.
 * It will never play a drawn card.
 *
 * @author Nolan Kisser 101222376
 * @author Owen Renette 101223576
 * @version November 24, 2023
 */
public class EasyAI extends Player implements AI {
    /**
     * The default constructor for class EasyAI
     * @param name The name selected (currently being overridden)
     */
    public EasyAI(String name) {
        super(name);
    }

    /**
     * Once the decision was made to play a card, it now must decide which one it would like to play
     * @param discardedCard the card on the top on the discard pile
     * @return The card that the EasyAI decided to play
     */
    @Override
    public Card smartPlayCard(Card discardedCard, Colours wildColour) {
        Card playableCard = null;

        for (int i = 0; i < this.getHand().handSize(); i++) {
            // The current card in question
            Card card = this.getHand().getCard(i);

            // If cards have same colour.
            if (card.getColour() == discardedCard.getColour() || card.getColour() == wildColour) {
                // If no selected card or the current selection is not wild, then switch
                if (playableCard == null || !playableCard.isWild()) {
                    playableCard = card;
                }
            }
            // If cards have same value.
            if (card.getVal() == discardedCard.getVal()) {
                // If there is no valid play yet, or card is not wild
                if (playableCard == null || !playableCard.isWild()) {
                    playableCard = card;
                }
            }
            // If the card is wild play the card
            if (card.isWild()) {
                playableCard = card;
            }
        }
        // Since the AI made the choice to play earlier, it has to have a playable card
        return playableCard;
    }

    /**
     * The EasyAI must start its turn by deciding its action of drawing or picking up a card.
     * @param discardedCard the card on the top on the discard pile
     * @return The AIDecision that was made (DRAW, or PICK_UP)
     */
    @Override
    public AIDecisions smartDecision(Card discardedCard) {
        for (int i = 0; i < this.getHand().handSize(); i++) {
            // The current card in question
            Card card = this.getHand().getCard(i);
            // If cards have same colour.
            if (card.getColour() == discardedCard.getColour()) {
                return AIDecisions.PLAY_CARD;
            }
            // If cards have same value.
            else if (card.getVal() == discardedCard.getVal()) {
                // Check num cards of eah colour
                return AIDecisions.PLAY_CARD;
            }
            // Wild always Playable
            else if (card.isWild()) {
                return AIDecisions.PLAY_CARD;
            }
        }
        // Otherwise the decision is to draw
        return AIDecisions.DRAW;
    }

    /**
     * Easy AI will never challenge the player and will not take into account the players hand
     * @return Whether the AI challenges or not
     */
    @Override
    public boolean smartChallenge(UNOModel model){
        return false;
    }

    /**
     * EasyAI will randomly choose a colour
     * @return Colour selected
     */
    @Override
    public Colours smartColourSelect(boolean light){
        Random random = new Random();
        int colour = random.nextInt(1,4);
        if (light){
            if (colour == 1){
                return Colours.RED;
            } else if (colour == 2) {
                return Colours.BLUE;
            } else if (colour == 3){
                return Colours.GREEN;
            }
            return Colours.YELLOW;
        } else {
            if (colour == 1){
                return Colours.PINK;
            } else if (colour == 2) {
                return Colours.CYAN;
            } else if (colour == 3){
                return Colours.PURPLE;
            }
            return Colours.ORANGE;
        }

    }

    /**
     * EasyAI will never play their drawn card
     * @return whether to play drawn card
     */
    @Override
    public boolean smartPlayDrawnCard(Card top){
        return false;
    }
}
