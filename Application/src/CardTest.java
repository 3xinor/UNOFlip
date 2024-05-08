package src;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * CardTest class is the Junit4 test class for class Card
 * in this UNO Flip game.
 *
 * @author Dave Exinor 101184298
 * @version October 20, 2023
 */
public class CardTest {
    private Card card;

    /**
     * tearDown sets the card to null after each test
     */
    @After
    public void tearDown() {
        card = null;
    }

    /**
     * testMakeAllCards checks to see if all combinations
     * of Cards can be instantiated correctly
     */
    @Test
    public void testMakeAllCards() {
        for (Colours colour : Colours.values()) {
            for (Values value : Values.values()) {
                card = new Card(value, colour, value, colour);
                assertNotNull(card);
            }
        }
    }

    /**
     * testFlipDark checks to see if the cards can flip to dark
     * is returned by method flipDark()
     */
    @Test
    public void testFlipDark() {
        card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        card.flipDark();
        assertEquals(card.getColour(), Colours.ORANGE);
    }

    /**
     * testFlipLight checks to see if the cards can flip to light
     * is returned by method flipLight()
     */
    @Test
    public void testFlipLight() {
        card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        card.flipDark();
        card.flipLight();
        assertEquals(card.getColour(), Colours.RED);
    }

    /**
     * testGetColour checks to see if the cards originally set colour
     * is returned by method getColour()
     */
    @Test
    public void testGetColour() {
        card = new Card(Values.ONE, Colours.BLUE, Values.ONE, Colours.ORANGE);
        assertEquals(card.getColour(), Colours.BLUE);
    }

    /**
     * tetsGetVal checks to see if the cards originally set value
     * is returned by method getVal()
     */
    @Test
    public void testGetVal() {
        card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        assertEquals(card.getVal(), Values.ONE);
    }

    /**
     * testIsNumbered checks if original method correctly
     * identifies if a card is numbered or not and returns
     * correct bool value
     */
    @Test
    public void testIsNumbered() {
        card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        assertTrue(card.isNumbered());
        card = new Card(Values.SKIP, Colours.RED, Values.SKIP_ALL, Colours.ORANGE);
        assertFalse(card.isNumbered());
    }

    /**
     * testIsSpecial checks if original method correctly
     * identifies if a card is special or not and returns
     * correct bool value
     */
    @Test
    public void testIsSpecial() {
        card = new Card(Values.DRAW_ONE, Colours.RED, Values.DRAW_FIVE, Colours.ORANGE);
        assertTrue(card.isSpecial());
        card.flipDark();
        assertTrue(card.isSpecial());
        card = new Card(Values.WILD_DRAW_TWO, Colours.WILD, Values.WILD_DRAW_COLOUR, Colours.WILD);
        assertFalse(card.isSpecial());
    }

    /**
     * testIsWild checks if original method correctly
     * identifies if a card is wild or not and returns
     * correct bool value
     */
    @Test
    public void testIsWild() {
        card = new Card(Values.ONE, Colours.WILD, Values.ONE, Colours.WILD);
        assertTrue(card.isWild());
        card = new Card(Values.WILD_DRAW_TWO, Colours.RED, Values.WILD_DRAW_COLOUR, Colours.ORANGE);
        assertTrue(card.isWild());
        card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        assertFalse(card.isWild());
    }

    /**
     * testToString compares actual string representation of
     * a card to the expected format
     */
    @Test
    public void testToString() {
        card = new Card(Values.WILD_DRAW_TWO, Colours.WILD, Values.WILD_DRAW_COLOUR, Colours.WILD);
        assertEquals("WILD_DRAW_TWO", card.toString());
        card = new Card(Values.THREE, Colours.RED, Values.THREE, Colours.ORANGE);
        assertEquals("RED THREE", card.toString());
    }

    /**
     *  testGetIcon compares the actual imageIcon associated
     *  with the card to the expected
     */
    @Test
    public void testGetIcon(){
        card = new Card(Values.ONE, Colours.RED, Values.ONE, Colours.ORANGE);
        ImageIcon icon = new ImageIcon("LightCards/Red1.png");
        assertSame(icon.toString(), card.getIcon().toString());
    }
}
