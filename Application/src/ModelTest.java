package src;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ModelTest is the JUnit4 testing class of class UNOModel.java
 * Dependent on class Card, Player, and Hand.
 *
 * @author Nolan Kisser 101222376
 * @version November 14, 2023
 */
public class ModelTest {

    UNOModel model;
    @Before
    public void setUp(){
        this.model = new UNOModel();
        model.addPlayer("Test");
        model.start();
    }

    @After
    public void cleanUp(){
        this.model = null;
    }

    @Test
    public void testGetController(){
        assertNotNull(model.getController());
    }
    @Test
    public void testGetPlayers(){
        assertNotNull(model.getPlayers());
    }

    @Test
    public void testGetDirection(){
        assertTrue(model.getDirection()); // Direction starts as true
    }

    @Test
    public void testGetNumPlayers(){
        assertEquals(model.getNumPlayers(), 1); // One player has been added
        model.addPlayer("Test1");
        model.addPlayer("Test2");
        assertEquals(model.getNumPlayers(), 3); // Two more players have been added
    }

    @Test
    public void testGetWildColour(){
        assertNull(model.getWildColour()); //No colour has been set
    }

    @Test
    public void testGetRoundPoints(){
        assertEquals(model.getRoundPoints(), 0); //No rounds have been played
    }

    @Test
    public void testGetRound(){
        assertEquals(model.getRound(), 1); //First round of the game
    }

    @Test
    public void testCheckDrawPile(){
        assertFalse(model.checkDrawPile()); // Pile is not empty at start of game
    }

    @Test
    public void testHasPlayableCard(){
        assertTrue(model.hasPlayableCard());
    }
}


