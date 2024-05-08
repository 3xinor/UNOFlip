package src;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.Serializable;

/**
 * Player class which represents a player in the UNOFlip
 * game
 *
 * @author Nolan Kisser 101222376
 *
 * @version October 20, 2023
 */
public class Player implements Serializable {
    private Hand myHand;
    private int points;
    private String name;

    public Player(String name){
        this.name = name;
        this.myHand = new Hand();
        points = 0;
    }

    @JsonCreator
    public Player(){
        name = null;
        myHand = new Hand();
        points = 0;
    }

    /**
     * Gets the players current points
     * @return points of player
     */
    public int getPoints(){
        return this.points;
    }

    /**
     * Gets the players name
     * @return name of player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the players hand
     * @return hand of player
     */
    public Hand getHand(){
        return myHand;
    }

    /**
     * Clears the player's hand
     */
    public void clearHand(){
        myHand = new Hand();
    }

    /**
     * Draws card from a pile
     * @param pile to be drawn from
     */
    public Card drawCard(CardPile pile){
        Card top = pile.getTop();
        myHand.addCard(top);
        return top;
    }

    /**
     * Adds points to players total points
     * @param points to be added
     */
    public void addPoints(int points){
        this.points += points;
    }

    /**
     * Player calls uno
     * @return true if player has 1 card left in hand
     */
    public boolean callUno(){
        if (myHand.handSize() == 1){
            return true;
        }
        return false;
    }
    /**
     * Checks if player's hand is empty
     * @return true if hand is empty
     */
    public boolean isHandEmpty(){
        return myHand.handSize() == 0;
    }

    /**
     * Determines the amount of points in player's hand.
     * @return number of points in hand
     */
    public int pointsInHand(){
        int handPoints = 0;
        for (Card card : myHand.getHand()){
            if (card.isWild() && (card.getVal() == Values.WILD_DRAW_TWO )){
                handPoints += 50;
            }
            else if(card.isWild() &&card.getVal() == Values.WILD_DRAW_COLOUR){
                handPoints += 60;
            }
            else if (card.isWild()){
                handPoints += 40;
            }
            else if (card.isSpecial()){
                if (card.getVal() == Values.DRAW_ONE){
                    handPoints += 10;
                }
                else if(card.getVal() == Values.SKIP_ALL){
                    handPoints += 30;
                }
                else {
                    handPoints += 20;
                }
            }
            else if (card.isNumbered()){
                if (card.getVal() == Values.ONE){
                    handPoints += 1;
                }
                else if (card.getVal() == Values.TWO){
                    handPoints += 2;
                }
                else if (card.getVal() == Values.THREE){
                    handPoints += 3;
                }
                else if (card.getVal() == Values.FOUR){
                    handPoints += 4;
                }
                else if (card.getVal() == Values.FIVE){
                    handPoints += 5;
                }
                else if (card.getVal() == Values.SIX){
                    handPoints += 6;
                }
                else if (card.getVal() == Values.SEVEN){
                    handPoints += 7;
                }
                else if (card.getVal() == Values.EIGHT){
                    handPoints += 8;
                }
                else if (card.getVal() == Values.NINE){
                    handPoints += 9;
                }
            }
        }
        return handPoints;
    }

    /**
     * Flips the cards from light to dark or vice versa
     */
    public void flipHand(){
        for(Card c: myHand.getHand()){
            if(c.isLight()){
                c.flipDark();
            }
            else {
                c.flipLight();
            }
        }
    }


    /**
     *
     */
    public JsonObject toJSON(){

        JsonObject object = Json.createObjectBuilder()
                .add("name", name)
                .add("points", points)
                .add("myHand", myHand.toJSON())
                .build();

        return object;
    }

    /**
     * Creates a new Player copy of original object
     * @return a new Player instance
     */
    public Player copyPlayer() {
        Player copy = new Player(this.name);
        copy.points = this.points;
        copy.myHand = this.myHand.copyHand();
        return copy;
    }
}
