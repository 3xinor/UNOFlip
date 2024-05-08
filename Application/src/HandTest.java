package src;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * HandTest is the JUnit4 testing class of class Hand
 * Dependent on class Card.
 *
 * @author Owen Renette 101223576
 * @version October 20, 2023
 */
public class HandTest {
    private Hand hand;
    /**
     * setUp will create a new hand before each test
     */
    @Before
    public void setUp() {
        this.hand = new Hand();
    }
    /**
     * cleanUp will set the hand to null after each test
     */
    @After
    public void cleanUp() {
        this.hand = null;
    }
    /**
     * Should create a new Empty Hand, which is an ArrayList of Cards
     */
    @Test
    public void getHand() {
        ArrayList<Card> cards;
        cards = this.hand.getHand();
        assertNotNull(this.hand.getHand());
        assertTrue(this.hand.getHand().size() == 0);

    }

    /**
     * addCard requires getHand to be accurate in order to deliver the accurate test status
     */
    @Test
    public void addCard() {
        int size;
        ArrayList<Card> cards;
        cards = this.hand.getHand();
        assertTrue(cards.size() == 0);
        size = 0;

        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);//Passing in Null as we dont care about image in this test

        this.hand.addCard(card);//Passing in Null as we dont care about image in this test
        cards = this.hand.getHand();
        assertTrue(cards.size() == size + 1);
    }
    /**
     * removeCard requires getHand and addCard to be accurate in order to deliver the accurate test status
     */
    @Test
    public void removeCard() {
        int size;
        ArrayList<Card> cards;
        cards = this.hand.getHand();
        assertTrue(cards.size() == 0);
        size = 0;

        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);//Passing in Null as we dont care about image in this test
        this.hand.addCard(card);
        cards = this.hand.getHand();
        assertTrue(cards.size() == size + 1);
        size += 1;

        this.hand.removeCard(card);
        cards = this.hand.getHand();
        assertTrue(cards.size() == size - 1);
    }
    /**
     * isHandEmpty requires addCard and removeCard to be accurate in order to deliver the accurate test status
     */
    @Test
    public void isHandEmpty() {
        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);//Passing in Null as we dont care about image in this test
        assertTrue(this.hand.isHandEmpty());
        this.hand.addCard(card);
        assertFalse(this.hand.isHandEmpty());
        this.hand.removeCard(card);
        assertTrue(this.hand.isHandEmpty());
    }
    /**
     * handSize requires addCard and removeCard to be accurate in order to deliver the accurate test status
     */
    @Test
    public void handSize() {
        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);//Passing in Null as we dont care about image in this test
        assertTrue(this.hand.handSize() == 0);
        this.hand.addCard(card);
        assertTrue(this.hand.handSize() == 1);
        this.hand.removeCard(card);
        assertTrue(this.hand.handSize() == 0);
    }
    /**
     * getCard requires addCard to be accurate in order to deliver the accurate test status
     */
    @Test
    public void getCard() {
        Card card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);//Passing in Null as we dont care about image in this test
        this.hand.addCard(card);
        assertTrue(this.hand.getCard(0) == card);
    }
}
