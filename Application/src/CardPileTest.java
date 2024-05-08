package src;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * CardPileTest is the JUnit4 test class for class CardPile
 *
 * @author Owen Renette 101223576
 * @version October 20, 2023
 */
public class CardPileTest {
    private CardPile pile;

    /**
     * setUp sets pile equal to a new CardPile before each test
     */
    @Before
    public void setUp() {
        this.pile = new CardPile();
        this.pile.newFullDeck();
        }

    /**
     * cleanUp sets pile to null after each test
     */
    @After
    public void cleanUp() {
        this.pile = null;
    }

    /**
     * newFullDeck tests setting a CardPile full of Cards
     */
    @Test
    public void newFullDeck() {
        this.pile.newFullDeck();
        assertNotNull(this.pile);
    }

    /**
     * newEmptyDeck tests setting a CardPile to no Card
     */
    @Test
    public void newEmptyDeck() {
        this.pile.newEmptyDeck();
        assertNotNull(this.pile);
    }

    /**
     * newSingleDeck tests setting a CardPile to one Card
     */
    @Test
    public void newSingleDeck() {
        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        this.pile.newSingleDeck(card);
        assertNotNull(this.pile);
    }

    /**
     * addCard relies on the accuracy of showTop to produce an accurate response
     */
    @Test
    public void addCard() {
        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        this.pile.addCard(card);
        assertSame(this.pile.showTop(), card);
    }


    /**
     * getTop tests removing and receiving the top car of the CardPile
     */
    @Test
    public void getTop() {
        assertNotNull(this.pile.getTop());
    }

    /**
     * showTop tests peeking at the top of the CardPile
     */
    @Test
    public void showTop() {
        assertNotNull(this.pile.showTop());
    }

    /**
     * shuffle relies on the accuracy of getTop to produce an accurate result
     */
    @Test
    public void shuffle() {
        CardPile shuffled = this.pile;
        shuffled.shuffle();
        boolean empty = false;
        while (!empty) {
            Card shuffleCard = shuffled.getTop();
            Card originalCard = this.pile.getTop();
            if (shuffleCard == null || originalCard == null) {
                empty = true;
            }
            if (shuffleCard != originalCard) {
                break;
            }
        }
        if (empty) {
            assertTrue(false);
        } else {
            assertTrue(true);
        }
    }

    @Test
    public void testFlipDeck() {
        pile.flipDeck();

        assertFalse(pile.showTop().isLight());
    }
}
