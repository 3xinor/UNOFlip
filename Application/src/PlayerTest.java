package src;

import org.junit.After;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @After
    public void cleanUp() {
        this.player = null;
    }

    @Test
    public void getPoints() {
        this.player = new Player("Joe");
        assertEquals(0, player.getPoints());
    }

    @Test
    public void getName() {
        this.player = new Player("Joe");
        assertEquals("Joe", player.getName());
    }

    @Test
    public void drawCard() {
        this.player = new Player("Joe");
        CardPile pile = new CardPile();

        pile.newFullDeck();
        Card top = pile.showTop();

        player.drawCard(pile);

        assertFalse(player.isHandEmpty());
        assertTrue((player.getHand().getHand()).contains(top));
    }

    @Test
    public void addPoints() {
        this.player = new Player("Joe");
        player.addPoints(20);
        assertEquals(20, player.getPoints());
    }

    @Test
    public void callUno() {
        this.player = new Player("Joe");
        CardPile pile = new CardPile();

        pile.newFullDeck();
        player.drawCard(pile);

        assertTrue(player.callUno());

        player.drawCard(pile);
        assertFalse(player.callUno());
    }

    @Test
    public void isHandEmpty() {
        this.player = new Player("Joe");
        CardPile pile = new CardPile();

        pile.newFullDeck();
        Card top = pile.showTop();

        assertTrue(player.isHandEmpty());
        player.drawCard(pile);
        assertFalse(player.isHandEmpty());

    }

    @Test
    public void pointsInHand() {
        this.player = new Player("Joe");
        assertEquals(0, player.pointsInHand());

        player.getHand().addCard(new Card(Values.WILD_DRAW_TWO, Colours.WILD, Values.WILD_DRAW_COLOUR , Colours.WILD));
        assertEquals(50, player.pointsInHand());

        player.getHand().addCard(new Card(Values.WILD_PICK_COLOUR, Colours.WILD, Values.WILD_PICK_COLOUR, Colours.WILD));
        assertEquals(90, player.pointsInHand());

        player.getHand().addCard(new Card(Values.SKIP, Colours.RED, Values.SKIP, Colours.ORANGE));
        assertEquals(110, player.pointsInHand());

        player.getHand().addCard(new Card(Values.DRAW_ONE, Colours.RED, Values.DRAW_FIVE, Colours.ORANGE));
        assertEquals(120, player.pointsInHand());

        player.getHand().addCard(new Card(Values.NINE, Colours.RED, Values.NINE, Colours.ORANGE));
        assertEquals(129, player.pointsInHand());

    }

    @Test
    public void testFlipHand(){
        this.player = new Player("Joe");
        player.getHand().addCard(new Card(Values.THREE, Colours.RED, Values.THREE , Colours.ORANGE));

        player.flipHand();

        assertFalse(player.getHand().getCard(0).isLight());
    }
}