package src;

/**
 * HardAI models an "AI" player with "Hard: level of play and play-style
 * How it thinks:
 * It will try to play colour before value.
 * If it detects a card of equal value, it will check if the colour swap will give them more available to play cards,
 * Which is strictly based on colour (as values change more frequently).
 * If it happens to have a wild, it will only play it if there is no other choice.
 * It is more aggressive and likes to play on the dark side where it can do more damage to other players.
 * It chooses colour based on which colour is more common in its hand.
 * It only challenges when it will succeed.
 *
 * @author Nolan Kisser 101222376
 * @author Owen Renette 101223576
 * @version November 26, 2023
 */
public class HardAI extends Player implements AI {
    /**
     * The default constructor for class HardAI
     * @param name The name selected (currently being overridden)
     */
    public HardAI(String name) {
        super(name);
    }

    public Colours numColoursHand(Colours c1, Colours c2, Colours c3, Colours c4){
        int n1 =0, n2=0, n3=0, n4 = 0;
        for(int i=0; i < this.getHand().handSize(); i++){
            Card card = this.getHand().getCard(i);
            if(card.getColour() == c1){
                n1++;
            } else if (card.getColour() == c2){
                n2++;
            } else if (card.getColour() == c3){
                n3++;
            } else if (card.getColour() == c4){
                n4++;
            }
        }
        if (n1 > n2 && n1 > n3 && n1 > n4) {
            return c1;
        } else if (n2 > n3 && n2 > n4) {
            return c2;
        } else if (n3 > n4) {
            return c3;
        } else {
            return c4;
        }
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
     * The HardAI must start its turn by deciding its action of drawing or picking up a card.
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
     * HardAI will only challenge when it will succeed
     * @return Whether the AI challenges or not
     */
    @Override
    public boolean smartChallenge(UNOModel model){
        if (model.hasPlayableColour()){
            return true;
        }
        return false;
    }

    /**
     * Hard AI will choose the colour that they have the most of in their hand
     * @return Colour selected
     */
    @Override
    public Colours smartColourSelect(boolean light){
        if (light){
            return numColoursHand(Colours.RED, Colours.BLUE, Colours.GREEN, Colours.YELLOW);
        }
        return numColoursHand(Colours.PINK, Colours.CYAN, Colours.PURPLE, Colours.ORANGE);
    }

    /**
     * Hard AI will only play their drawn card if it is not a wild
     * @return whether to play drawn card
     */
    @Override
    public boolean smartPlayDrawnCard(Card top){
        return !top.isWild();
    }
}
