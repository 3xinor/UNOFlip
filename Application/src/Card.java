package src;
import java.awt.*;
import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;

/**
 * Card is Object representation of each Card and its colour and value
 *
 * @author Sebi Magyar-Samoila 101223588
 * @version October 18, 2023
 */
public class Card implements Serializable {
    private final Values light_val;
    private final Colours light_colour;
    private final ImageIcon light_icon;

    private final Values dark_val;
    private final Colours dark_colour;
    private final ImageIcon dark_icon;

    private Values val;
    private Colours colour;
    private ImageIcon icon;
    private boolean light;

    public Card(Values light_val, Colours light_colour, Values dark_val, Colours dark_colour){
        this.light_val = this.val = light_val;
        this.light_colour = this.colour = light_colour;
        this.light_icon = this.icon = new ImageIcon(new ImageIcon(getClass().getResource("LightCards/"+light_colour+"_"+light_val+".png")).getImage().getScaledInstance(120, 191, Image.SCALE_DEFAULT));
        light = true;

        this.dark_val = dark_val;
        this.dark_colour = dark_colour;
        this.dark_icon = new ImageIcon(new ImageIcon(getClass().getResource("DarkCards/"+dark_colour+"_"+dark_val+".png")).getImage().getScaledInstance(120, 191, Image.SCALE_DEFAULT));
    }

    /**
     * @return Colour of card
     */
    public Colours getColour() {
        return colour;
    }

    /**
     * @return Value of card
     */
    public Values getVal() {
        return val;
    }

    /**
     * @return Image of card
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Checsk if card is light or dark
     * @return true if it is light
     */
    public boolean isLight(){
        return light;
    }

    /**
     * Flip to dark colours
     */
    public void flipDark(){
        this.val = dark_val;
        this.colour = dark_colour;
        this.icon = dark_icon;
        light = false;
    }

    /**
     * Flip to light colours
     */
    public void flipLight(){
        this.val = light_val;
        this.colour = light_colour;
        this.icon = light_icon;
        light = true;
    }

    /**
     * Checks if Card is a Number
     * @return True if Card is a number
     */
    public boolean isNumbered(){
        return val == Values.ONE ||
                val == Values.TWO ||
                val == Values.THREE ||
                val == Values.FOUR ||
                val == Values.FIVE ||
                val == Values.SIX ||
                val == Values.SEVEN ||
                val == Values.EIGHT ||
                val == Values.NINE;
    }

    /**
     * Checks if Card is a Special Card
     * @return True if Card is a special
     */
    public boolean isSpecial(){
        return val == Values.DRAW_ONE ||
                val == Values.REVERSE ||
                val == Values.SKIP ||
                val == Values.SKIP_ALL ||
                val == Values.DRAW_FIVE ||
                val == Values.FLIP;
    }

    /**
     * Checks if Card is a Wild
     * @return True if Card is a Wild
     */
    public boolean isWild(){
        return colour == Colours.WILD ||
                val == Values.WILD_DRAW_TWO ||
                val == Values.WILD_PICK_COLOUR ||
                val == Values.WILD_DRAW_COLOUR;
    }

    /**
     * updates the String of the object
     * @return the new String value of the Object
     */
    @Override
    public String toString() {
        if (isWild()) {
            return val.toString();
        } else {
            return colour + " " + val;
        }
    }

    /**
     * Creates a new Card copy of original object
     * @return a new Card instance
     */
    public Card copyCard() {
        Card copy = new Card(this.light_val,this.light_colour,
                this.dark_val,this.dark_colour);
        return copy;
    }

    /**
     *
     */
    public JsonObject toJSON(){

        JsonObject js = Json.createObjectBuilder()
                .add("colour", colour.toString())
                .add("val", val.toString())
                .add("light_colour", light_colour.toString())
                .add("light_val", light_val.toString())
                .add("dark_colour", dark_colour.toString())
                .add("dark_val", dark_val.toString())
                .build();

        return js;
    }
}

