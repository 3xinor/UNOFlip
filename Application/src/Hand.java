package src;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Hand class which represents a players hand of cards used
 * in UNO Flip Game.
 *
 * @author Dave Exinor 101184298
 *
 * @version October 19, 2023
 */
public class Hand implements Serializable {
    private ArrayList<Card> hand;

    /**
     * Creates a new hand.
     */
    public Hand() {
        hand = new ArrayList<Card>();
    }

    /**
     * Retrieves current Cards in players hand.
     * @return ArrayList of current cards in player hand.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to players hand.
     * @param card the card to be added to hand.
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Attempts to remove a specified card from the hand.
     * @param card card to be removed.
     * @return True if the card was successfully removed, False otherwise.
     */
    public boolean removeCard(Card card) {
        return hand.remove(card);
    }

    /**
     * Checks if the hand is empty.
     * @return True if the hand has no cards, False otherwise.
     */
    public boolean isHandEmpty() {
        return (hand.size() == 0);
    }

    /**
     * Provides the current number of cards in hand.
     * @return number of cards in hand.
     */
    public int handSize() {
        return hand.size();
    }

     /**
     * Gets the card at an index
     * @param index of the card to be got
     * @return card at the given index
     */
    public Card getCard(int index){
        return hand.get(index);
    }

    /**
     * The number of cards with a matching colour to the card in question
     * Used in the NormalAI decision-making to help it decide if it is worth changing the colour.
     * @param card The base card for counting the colours
     * @return The number of cards of colour X
     */
    public int getNumMatchingCol(Card card) {
        int num = 0;
        for (Card currCard: hand) {
            if (currCard.getColour() == card.getColour()) {
                num++;
            }
        }
        return num;
    }

    /**
     * Creates a new Hand copy of original object
     * @return a new Hand instance
     */
    public Hand copyHand() {
        Hand copy = new Hand();
        for(Card c: this.hand) {
            copy.addCard(c.copyCard());
        }
        return copy;
    }



    /**
     *
     */
    public JsonObject toJSON(){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(Card c: hand){
            builder.add(c.toJSON());
        }

        JsonArray ja_cards = builder.build();

        JsonObject js = Json.createObjectBuilder()
                .add("hand", ja_cards)
                .build();

        return js;
    }

}
