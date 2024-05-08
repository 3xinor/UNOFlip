package src;

import java.util.Random;

/**
 * NormalAI models an "AI" player with "Normal: level of play and play-style
 * How it thinks:
 * It will try to play colour before value.
 * If it detects a card of equal value, it will check if the colour swap will give them more available to play cards,
 * Which is strictly based on colour (as values change more frequently).
 * If it happens to have a wild, it will only play it if there is no other choice.
 * It has no bias towards whether it want to flip the play-side or not.
 * It will randomly challenge the player
 * It selects colour changes according to the colours it has the most of.
 * It will always play the drawn card if it is playable
 *
 * @author Owen Renette 101223576
 * @author Nolan Kisser 101222376
 * @version November 16, 2023
 */
public class NormalAI extends Player implements AI {

    /**
     * The default constructor for class NormalAI
     * @param name The name selected (currently being overridden)
     */
    public NormalAI(String name) {
        super(name);
    }

    /**
     * Once the decision was made to play a card, it now must decide which one it would like to play
     * @param discardedCard the card on the top on the discard pile
     * @return The card that the NormalAI decided to play
     */
    @Override
    public Card smartPlayCard(Card discardedCard, Colours wildColour) {
        Card playableCard = null;

        for (int i = 0; i < this.getHand().handSize(); i++) {
            // The current card in question
            Card card = this.getHand().getCard(i);

            // If cards have same colour.
            if (card.getColour() == discardedCard.getColour() || card.getColour() == wildColour) {
                // If no selected card or the current selection is wild, then switch
                if (playableCard == null || playableCard.isWild()) {
                    playableCard = card;
                }
            }
            // If cards have same value.
            if (card.getVal() == discardedCard.getVal()) {
                // If there is no valid play yet, select
                if (playableCard == null || playableCard.isWild()) {
                    playableCard = card;
                } else {
                    // Otherwise, check if the colour change would increase your future playable cards
                    int cardNumColour = this.getHand().getNumMatchingCol(card);
                    int playableCardNumColour = this.getHand().getNumMatchingCol(playableCard);
                    if (cardNumColour > playableCardNumColour) {
                        playableCard = card;
                    }
                }
            }
            // If the card is wild and there is no playable card yet
            if (card.isWild() && playableCard == null) {
                playableCard = card;
            }
        }
        // Since the AI made the choice to play earlier, it has to have a playable card
        return playableCard;
    }

    /**
     * The NormalAI must start its turn by deciding its action of drawing or picking up a card.
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
     * Normal AI will randomly challenge the player and will not take into account the players hand
     * @return Whether the AI challenges or not
     */
    @Override
    public boolean smartChallenge(UNOModel model){
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     * Normal AI will choose a colour by checking which colour he has the most cards of.
     * @return Colour selected
     */
    @Override
    public Colours smartColourSelect(boolean light){
        Colours colour;
        //Assign default colour selection in case of last card being wild, or an empty hand
        if (light) {
            colour = Colours.RED;
        } else {
            colour = Colours.ORANGE;
        }
        int maxNumColour = 0;
        for (Card card: this.getHand().getHand()) {
            int currCount = this.getHand().getNumMatchingCol(card);
            // If there is a non-wild card that has more cards of one colour than the previous selection
            if (!(card.isWild()) && currCount > maxNumColour) {
                maxNumColour = currCount;
                colour = card.getColour();
            }
        }
        return colour;
    }

    /**
     * Normal AI will always play their drawn card
     * @return whether to play drawn card
     */
    @Override
    public boolean smartPlayDrawnCard(Card top){
        return true;
    }
}
