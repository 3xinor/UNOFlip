package src;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Collections;

/**
 * CardPile class which is used by UNO Flip game to
 * provide the functionality of a deck.
 *
 * @author Dave Exinor 101184298
 *
 * @version October 19, 2023
 */

public class CardPile implements Serializable {
    private Stack<Card> deck;

    /**
     * Creates new card pile.
     */
    public CardPile() {
        deck = new Stack<Card>();
    }

    /**
     * Adds all unoflip cards to the deck in
     * accordance with official rules.
     */
    public void newFullDeck() {
        // adds 18 numbered cards of each colour
        //adds pair of blue, green, red & yellow numbered cards to deck
        for (int j = 2; j > 0; j--) {
            deck.push(new Card(Values.ONE, Colours.RED,  Values.ONE, Colours.ORANGE));
            deck.push(new Card(Values.TWO, Colours.RED, Values.TWO, Colours.ORANGE));
            deck.push(new Card(Values.THREE, Colours.RED, Values.THREE, Colours.ORANGE));
            deck.push(new Card(Values.FOUR, Colours.RED, Values.FOUR, Colours.ORANGE));
            deck.push(new Card(Values.FIVE, Colours.RED, Values.FIVE, Colours.ORANGE));
            deck.push(new Card(Values.SIX, Colours.RED, Values.SIX, Colours.ORANGE));
            deck.push(new Card(Values.SEVEN, Colours.RED, Values.SEVEN, Colours.ORANGE));
            deck.push(new Card(Values.EIGHT, Colours.RED, Values.EIGHT, Colours.ORANGE));
            deck.push(new Card(Values.NINE, Colours.RED, Values.NINE, Colours.ORANGE));

            deck.push(new Card(Values.ONE, Colours.BLUE, Values.ONE, Colours.PURPLE));
            deck.push(new Card(Values.TWO, Colours.BLUE, Values.TWO, Colours.PURPLE));
            deck.push(new Card(Values.THREE, Colours.BLUE, Values.THREE, Colours.PURPLE));
            deck.push(new Card(Values.FOUR, Colours.BLUE, Values.FOUR, Colours.PURPLE));
            deck.push(new Card(Values.FIVE, Colours.BLUE,  Values.FIVE, Colours.PURPLE));
            deck.push(new Card(Values.SIX, Colours.BLUE, Values.SIX, Colours.PURPLE));
            deck.push(new Card(Values.SEVEN, Colours.BLUE,  Values.SEVEN, Colours.PURPLE));
            deck.push(new Card(Values.EIGHT, Colours.BLUE, Values.EIGHT, Colours.PURPLE));
            deck.push(new Card(Values.NINE, Colours.BLUE, Values.NINE, Colours.PURPLE));

            deck.push(new Card(Values.ONE, Colours.GREEN, Values.ONE, Colours.CYAN));
            deck.push(new Card(Values.TWO, Colours.GREEN, Values.TWO, Colours.CYAN));
            deck.push(new Card(Values.THREE, Colours.GREEN,  Values.THREE, Colours.CYAN));
            deck.push(new Card(Values.FOUR, Colours.GREEN, Values.FOUR, Colours.CYAN));
            deck.push(new Card(Values.FIVE, Colours.GREEN, Values.FIVE, Colours.CYAN));
            deck.push(new Card(Values.SIX, Colours.GREEN, Values.SIX, Colours.CYAN));
            deck.push(new Card(Values.SEVEN, Colours.GREEN, Values.SEVEN, Colours.CYAN));
            deck.push(new Card(Values.EIGHT, Colours.GREEN, Values.EIGHT, Colours.CYAN));
            deck.push(new Card(Values.NINE, Colours.GREEN, Values.NINE, Colours.CYAN));

            deck.push(new Card(Values.ONE, Colours.YELLOW, Values.ONE, Colours.PINK));
            deck.push(new Card(Values.TWO, Colours.YELLOW, Values.TWO, Colours.PINK));
            deck.push(new Card(Values.THREE, Colours.YELLOW, Values.THREE, Colours.PINK));
            deck.push(new Card(Values.FOUR, Colours.YELLOW, Values.FOUR, Colours.PINK));
            deck.push(new Card(Values.FIVE, Colours.YELLOW, Values.FIVE, Colours.PINK));
            deck.push(new Card(Values.SIX, Colours.YELLOW, Values.SIX, Colours.PINK));
            deck.push(new Card(Values.SEVEN, Colours.YELLOW, Values.SEVEN, Colours.PINK));
            deck.push(new Card(Values.EIGHT, Colours.YELLOW, Values.EIGHT, Colours.PINK));
            deck.push(new Card(Values.NINE, Colours.YELLOW, Values.NINE, Colours.PINK));

        }
        

        //adds 8 draw one, reverse, & skip cards
        for(int i = 2; i > 0; i-- ) {
            deck.push(new Card(Values.DRAW_ONE, Colours.RED, Values.DRAW_FIVE, Colours.ORANGE));
            deck.push(new Card(Values.DRAW_ONE, Colours.GREEN,  Values.DRAW_FIVE, Colours.CYAN));
            deck.push(new Card(Values.DRAW_ONE, Colours.BLUE, Values.DRAW_FIVE, Colours.PURPLE));
            deck.push(new Card(Values.DRAW_ONE, Colours.YELLOW, Values.DRAW_FIVE, Colours.PINK));

            deck.push(new Card(Values.REVERSE, Colours.RED, Values.REVERSE, Colours.ORANGE));
            deck.push(new Card(Values.REVERSE, Colours.GREEN, Values.REVERSE, Colours.CYAN));
            deck.push(new Card(Values.REVERSE, Colours.BLUE, Values.REVERSE, Colours.PURPLE));
            deck.push(new Card(Values.REVERSE, Colours.YELLOW, Values.REVERSE, Colours.PINK ));

            deck.push(new Card(Values.SKIP, Colours.RED, Values.SKIP_ALL, Colours.ORANGE));
            deck.push(new Card(Values.SKIP, Colours.GREEN, Values.SKIP_ALL, Colours.CYAN));
            deck.push(new Card(Values.SKIP, Colours.BLUE, Values.SKIP_ALL, Colours.PURPLE));
            deck.push(new Card(Values.SKIP, Colours.YELLOW,Values.SKIP_ALL, Colours.PINK));

            deck.push(new Card(Values.FLIP, Colours.RED, Values.FLIP, Colours.ORANGE));
            deck.push(new Card(Values.FLIP, Colours.GREEN,Values.FLIP, Colours.CYAN));
            deck.push(new Card(Values.FLIP, Colours.BLUE, Values.FLIP, Colours.PURPLE));
            deck.push(new Card(Values.FLIP, Colours.YELLOW, Values.FLIP, Colours.PINK));
        }

        //adds 4 Wild and Wild draw 2 cards
        for(int i = 4; i > 0; i-- ) {
            deck.push(new Card(Values.WILD_PICK_COLOUR, Colours.WILD, Values.WILD_PICK_COLOUR, Colours.WILD));
            deck.push(new Card(Values.WILD_DRAW_TWO, Colours.WILD, Values.WILD_DRAW_COLOUR, Colours.WILD));
        }

        // randomizes the card pile organization
        shuffle();
    }

    /**
     * Remove all cards from deck.
     */
    public void newEmptyDeck() {
        //clears current deck
        deck.clear();
    }

    /**
     * Creates a deck with 1 card.
     * @param card card to be added to deck of 1 card.
     */
    public void newSingleDeck(Card card) {
        //clears deck and adds one card
        deck.clear();
        deck.push(card);
    }

    /**
     * Adds card to deck.
     * @param card card to be added to the deck.
     */
    public void addCard(Card card) {
        deck.push(card);
    }


    /**
     * Removes and returns top card in deck.
     * @return top card from the deck, null if empty
     */
    public Card getTop() {
        Card card;
        try {
            card = deck.pop();
        } catch (EmptyStackException e) {
            card = null;
        }
        return card;
    }

    /**
     * Displays the top card of the deck
     * @return top card in the deck, null if empty
     */
    public Card showTop() {
        Card card;
        try {
            card = deck.peek();
        } catch (EmptyStackException e) {
            card = null;
        }
        return card;
    }

    /**
     * Randomizes order of cards in deck.
     */
    public void shuffle() {
        ArrayList<Card> stackContents = new ArrayList<Card>(deck);
        Collections.shuffle(stackContents);
        deck = new Stack<Card>();
        for(Card c: stackContents){
            deck.push(c);
        }
    }

    /**
     * Flips the cards colour and values
     */
    public void flipDeck(){
        Card card;
        Stack<Card> new_deck = new Stack<Card>();
        while(showTop() != null){
            card = getTop();
            if(card.isLight()) {
                card.flipDark();
            }
            else {
                card.flipLight();
            }
            new_deck.push(card);
        }
        deck = new_deck;
    }

    /**
     * Creates a new CardPile copy of original object
     * @return a new CardPile instance
     */
    public CardPile copyPile() {
        CardPile copy = new CardPile();
        ArrayList<Card> stackContents = new ArrayList<Card>(this.deck);
        for(Card c: stackContents){
            copy.deck.push(c);
        }
        return copy;
    }

    /**
     *
     */
    public JsonObject toJSON(){
        CardPile copy = copyPile();
        JsonArrayBuilder builder = Json.createArrayBuilder();

        while(copy.showTop() != null){
            builder.add(copy.getTop().toJSON());
        }
        JsonArray jd = builder.build();

        JsonObject js = Json.createObjectBuilder()
                .add("deck", jd).
                build();
       return js;
    }
}
