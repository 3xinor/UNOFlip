package src;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.json.*;
import java.io.*;
import java.util.*;

/**
 * --- All rights belong to UNO ---
 * UNOModel is the internal UNO logic. When changes to the state of the program
 * are made, it notifies the views of the model.
 *
 * @author Owen Renette 101223576
 * @author Sebi Magyar-Samoila 101223588
 * @version October 21, 2023
 */

public class UNOModel implements Serializable {
    transient private ArrayList<UNOModelView> views;
    private ArrayList<Player> players; // Contains all Players in the game
    private CardPile drawPile;
    private CardPile discardPile;
    private Player turn; // Stores the Player whose turn it is
    private Card lastCardPlayed; // Used for determining legal moves
    private boolean direction; // True for clockwise, false for counterclockwise
    private Random random; // Used to determine a starting player for first round
    transient private UNOController controller;
    private Colours wildColour; // Used for determining if card plays are valid off wild cards
    private int round;
    private UNOModel undoState;
    private UNOModel redoState;

    private ArrayList<String> saves;

    /**
     * The default constructor for the UNO! Game class.
     */
    public UNOModel() {
        this.controller = new UNOController(this);
        this.views = new ArrayList<>();
        this.players = new ArrayList<>();
        this.drawPile = (new CardPile());
        this.drawPile.newFullDeck();
        this.discardPile = (new CardPile());
        this.discardPile.newEmptyDeck();
        this.turn = null;
        this.lastCardPlayed = null;
        this.direction = true;
        this.random = new Random();
        this.wildColour = Colours.WILD;
        round = 1;
        this.saves = new ArrayList<>();
    }

    /**
     * Get the controller for the game
     *
     * @return UNOController
     */
    public UNOController getController() {
        return controller;
    }

    /**
     * Get the players in the game
     *
     * @return ArrayList of Players in the game
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Get the players according to their clockwise order
     *
     * @return ArrayList of Players according to clockwise order of play
     */
    public ArrayList<Player> getPlayersByClockwise() {
        int currentPlayerIndex = players.indexOf(turn);
        ArrayList<Player> playersByDirection = new ArrayList<>();
        for (int i = currentPlayerIndex; playersByDirection.size() < players.size(); i = (i + 1) % players.size()) {
            playersByDirection.add(players.get(i));
        }
        return playersByDirection;
    }

    /**
     * @return True for clockwise, False for counter-clockwise
     */
    public boolean getDirection() {
        return this.direction;
    }

    /**
     * getNumPlayers returns the number of players
     *
     * @return Number of players
     */
    public int getNumPlayers() {
        return players.size();
    }

    /**
     * Get the current player turn
     *
     * @return turn the Current player
     */
    public Player getTurn() {
        return turn;
    }

    /**
     * Get the current Wild Colour
     *
     * @return wildColour
     */
    public Colours getWildColour() {
        return wildColour;
    }

    /**
     * Adds a viewer to the model
     *
     * @param view The view to be added.
     */
    public void addUNOView(UNOModelView view) {
        this.views.add(view);
    }

    /**
     * Removes a viewer to the model
     *
     * @param view The view to be removed.
     */
    public void removeUNOView(UNOModelView view) {
        this.views.remove(view);
    }

    /**
     * addPlayer adds a Player to the game
     *
     * @param name The name of the Player
     */
    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    /**
     * addComputerPlayer adds an AI Player to the game
     * @param name The name of the AI Player
     * @param difficulty The difficulty of the AI Player
     */
    public void addComputerPlayer(String name, String difficulty) {
        if (difficulty.equals(UNOView.difficulties[0])) {
            //Easy difficulty
            players.add(new EasyAI(name));
        } else if (difficulty.equals(UNOView.difficulties[1])) {
            //Normal difficulty
            players.add(new NormalAI(name));
        } else if (difficulty.equals(UNOView.difficulties[2])) {
            //Hard difficulty
            players.add(new HardAI(name));
        }
    }

    /**
     * gameWinner checks if a Player has won the game (not the round, the whole game)
     */
    private void gameWinner() {
        for (UNOModelView view : views) {
            view.handleUNOGameUpdate(new UNOEvent(this, turn));
            view.handleUNOShowAllPoints(new UNOEvent(this));
        }
    }

    /**
     * roundWinner calculates the round points and adds it to the winners total
     */
    private void roundWinner() {
        int points;
        points = getRoundPoints();
        this.turn.addPoints(points);

        for (UNOModelView view : views) {
            view.handleUNORoundUpdate(new UNOEvent(this, turn));
            view.handleUNOShowAllPoints(new UNOEvent(this));
        }
    }

    /**
     * getRoundPoints returns the number of points gained at the end of the round
     *
     * @return The point total of the round
     */
    public int getRoundPoints() {
        int points = 0;
        for (Player player : players) {
            if (player != this.turn) {
                points += player.pointsInHand();
            }
        }
        return points;
    }

    /**
     * showAllPoints Prints the points for each player
     */
    public String getPlayersPointsString() {
        String playersPoints = "<html>Player Points<br>";
        for (Player player : players) {
            playersPoints += player.getName() + ": " + player.getPoints() + "<br>";
        }
        return playersPoints + "<html>";
    }

    /**
     * Initializes the starting state of the game
     */
    public void start() {
        //NOTE: may be subject to change based off the Card & Pile class Changes
        //Select a random player to start the game

        this.turn = players.get(random.nextInt(players.size()));

        //Initialize the decks and deal
        drawPile.shuffle();
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.drawCard(drawPile);
            }
        }

        while (drawPile.showTop().isSpecial() || drawPile.showTop().isWild()) {
            drawPile.shuffle();
        }
        lastCardPlayed = drawPile.getTop();
        discardPile.addCard(lastCardPlayed);
        playCard(lastCardPlayed);

        handleTurnDiscard();
    }

    /**
     * getRound
     *
     * @return Current round of the game
     */
    public int getRound() {
        return round;
    }

    /**
     * Handling of the Turn and Discard view updates
     */
    public void handleTurnDiscard(){
        for (UNOModelView view : views) {
            view.handleUNOTurnUpdate(new UNOEvent(this, turn));
            view.handleUNODiscardUpdate(new UNOEvent(this, discardPile));
        }
    }

    /**
     * nextTurn changes the current player's turn to the next player's turn
     */
    public void nextTurn() {
        int playerIndex = players.indexOf(turn);
        if (direction) {
            playerIndex = (playerIndex + 1) % (players.size());
        } else {
            playerIndex -= 1;
            if (playerIndex < 0) {
                playerIndex = players.size() - 1;
            }
        }
        this.turn = players.get(playerIndex);
        handleTurnDiscard();

        for (UNOModelView view: views) {
            view.handleUNOStatusUpdate();
        }
    }

    /**
     * drawCard makes the given player draw a card from the draw pile
     *
     * @param player The player that will draw a card
     */
    public Card drawCard(Player player) {
        checkDrawPile();
        for (UNOModelView view : views) {
            view.handleUNODrawUpdate(new UNOEvent(this, player, drawPile));
        }
        return player.drawCard(drawPile);
    }

    /**
     * nextRound clears the table by resetting the card piles and re-dealing the players hands
     */
    private void nextRound() {
        direction = true;
        wildColour = Colours.WILD;
        //NOTE: Player, Card, and CardPile will require changing
        drawPile.newEmptyDeck();
        drawPile.newFullDeck();
        drawPile.shuffle();
        discardPile.newEmptyDeck();

        //Initialize the decks and deal
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                if (i == 0) {
                    player.clearHand();
                }
                player.drawCard(drawPile);
            }
        }

        while (drawPile.showTop().isSpecial() || drawPile.showTop().isWild()) {
            drawPile.shuffle();
        }

        lastCardPlayed = drawPile.getTop();
        //drawPile.removeTop();
        discardPile.addCard(lastCardPlayed);

        for (UNOModelView view: views) {
            view.handleUNOStatusUpdate();
        }
    }

    /**
     * Determines what type of card was selected and plays it
     *
     * @param card the card played
     */
    private void playCard(Card card) {
        if (card.isNumbered()) {
            playNumbered(card);
        } else {
            playSpecial(card);
        }
    }

    /**
     * Determines if the card selected is a valid play this turn
     *
     * @param card the card played
     * @return True if it is valid
     */
    public boolean isValidCard(Card card) {
        if (wildColour != Colours.WILD) {
            return card.getColour() == wildColour || card.getColour() == Colours.WILD;
        } else {
            return card.getColour() == discardPile.showTop().getColour() ||
                    card.getVal() == discardPile.showTop().getVal() ||
                    card.isWild();
        }
    }

    /**
     * Plays a Numbered Card and adds it to the top of the discard CardPile
     *
     * @param card the card played
     */
    private void playNumbered(Card card) {
        discardPile.addCard(card);
    }



    /**
     * Determines what the special value is and executes that action,
     * if it is a Wild then it redirects to running the Wild,
     * then adds it to the top of the discard CardPile
     *
     * @param card the card played
     */
    private void playSpecial(Card card) {
        //NEUTRAL Actions
        if( card.getVal() == Values.FLIP){
            if(card.isLight()){
                card.flipDark();
            }
            else{
                card.flipLight();
            }
            drawPile.flipDeck();
            discardPile.flipDeck();
            for(Player p: players){
                p.flipHand();
            }
        }
        else if (card.getVal() == Values.REVERSE) {
            direction = !direction;
        }
        //Light Actions
        else if(card.isLight()) {
            if (card.getVal() == Values.DRAW_ONE) {
                drawOne();
                if (!turn.isHandEmpty()) {
                    nextTurn();
                }
            } else if (card.getVal() == Values.SKIP) {
                if (!turn.isHandEmpty()) {
                    nextTurn();
                }
            } else if (card.isWild()) {
                playWild(card);
            }
        }
        //Dark Actions
        else {
            if(card.getVal() == Values.DRAW_FIVE){
                for(int i = 0; i < 5; i++){
                    drawOne();
                }
                if (!turn.isHandEmpty()) {
                    nextTurn();
                }
            }
            else if(card.getVal() == Values.SKIP_ALL){
                for(int i = 0; i < players.size()-1; i ++){
                    if (!turn.isHandEmpty()) {
                        nextTurn();
                    }
                }
            }
            else if (card.isWild()) {
                playWild(card);
            }
        }
        discardPile.addCard(card);
    }

    /**
     * Determines which player has to draw after a draw one card has been played
     */
    private void drawOne(){
        int curPlayerIndex = players.indexOf(turn);
        Player nextPlayer;
        if (direction) {
            nextPlayer = players.get((curPlayerIndex + 1) % players.size());
            drawCard(nextPlayer);

        } else {
            if (curPlayerIndex != 0) {
                nextPlayer = players.get(curPlayerIndex - 1);
            } else {
                nextPlayer = players.get((players.size()) - 1);
            }
            drawCard(nextPlayer);
        }
    }

    /**
     * Determines if the challenge was successful or unsuccessful
     */
    public void challenge() {
        if (hasPlayableColour()) {
            for (UNOModelView view : views) {
                view.handleUNOShowChallengeSuccess();
            }
            for(int i = 0; i <2; i++) {
                checkDrawPile();
                if (drawPile.showTop() != null) {
                    drawCard(turn);
                }
            }
        } else {
            nextTurn();
            for (UNOModelView view : views) {
                view.handleUNOShowChallengeFailed();
            }
            for (int i = 0; i < 4; i++) {
                checkDrawPile();
                if (drawPile.showTop() != null) {
                    drawCard(turn);
                }
            }
        }
    }

    /**
     * Depending on the Wild Card, plays either just selecting the colour,
     * or making next player draw 2 and skip their turn,
     * and adds it to the top of the discard CardPile
     *
     * @param card the card played
     */
    private void playWild(Card card) {
        boolean choice;
        Colours tempWildColour;
        //Light Action
        if (card.getVal() == Values.WILD_DRAW_TWO) {
            int curPlayerIndex = players.indexOf(turn);
            Player nextPlayer;
            if (direction) {
                nextPlayer = players.get((curPlayerIndex + 1) % players.size());
            } else {
                if (curPlayerIndex != 0) {
                    nextPlayer = players.get(curPlayerIndex - 1);
                } else {
                    nextPlayer = players.get((players.size()) - 1);
                }
            }
            if(!turn.isHandEmpty()) {
                if (turn instanceof AI){
                    tempWildColour = ((AI) turn).smartColourSelect(card.isLight()); // AI choose Colour
                } else{
                    tempWildColour = controller.askColour(card.isLight()); // Player choose colour
                }
                if (nextPlayer instanceof AI){
                    choice = ((AI) nextPlayer).smartChallenge(this); // AI challenge
                }else{
                    choice = controller.askChallenge(nextPlayer.getName()); // Player challenge
                }
                if (choice) {
                    challenge();
                } else {
                    for (int i = 0; i < 2; i++) {
                        checkDrawPile();
                        if (drawPile.showTop() != null) {
                            drawCard(nextPlayer);
                        }
                    }
                    if (!turn.isHandEmpty()) {
                        nextTurn();
                    }
                }
                wildColour = tempWildColour;
            }

        }
        //Dark Action
        else if(card.getVal() == Values.WILD_DRAW_COLOUR){
            int curPlayerIndex = players.indexOf(turn);
            Player nextPlayer;
            if (direction) {
                nextPlayer = players.get((curPlayerIndex + 1) % players.size());
            } else {
                if (curPlayerIndex != 0) {
                    nextPlayer = players.get(curPlayerIndex - 1);
                } else {
                    nextPlayer = players.get((players.size()) - 1);
                }
            }
            if(!turn.isHandEmpty()) {
                if (turn instanceof AI){
                    tempWildColour = ((AI) turn).smartColourSelect(card.isLight()); // AI choose Colour
                } else{
                    tempWildColour = controller.askColour(card.isLight()); // Player choose colour
                }
                if (nextPlayer instanceof AI){
                    choice = ((AI) nextPlayer).smartChallenge(this); // AI challenge
                }else{
                    choice = controller.askChallenge(nextPlayer.getName()); // Player challenge
                }
                if (choice) {
                    challenge();
                } else {
                    Card drawnCard = lastCardPlayed;
                    //Draws Card until the colour is reached
                    while(drawnCard.getColour() != tempWildColour){
                        checkDrawPile();
                        if (drawPile.showTop() != null) {
                            drawnCard = drawCard(nextPlayer);
                        }
                    }
                    if (!turn.isHandEmpty()) {
                        nextTurn();
                    }
                }
                wildColour = tempWildColour;
            }
        }
        else {
            if (turn instanceof AI){
                wildColour = ((AI) turn).smartColourSelect(card.isLight()); // AI choose Colour
            }else{
                wildColour = controller.askColour(card.isLight()); // Player choose colour
            }
        }
    }

    /**
     * hasPlayableCard determines if the current player can play a card of from their hand
     *
     * @return True if at least 1 card is playable, False otherwise.
     */
    public boolean hasPlayableCard() {
        ArrayList<Card> hand = turn.getHand().getHand();
        for (Card c : hand) {
            if (isValidCard(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * hasPlayableColour determines if the current player can play a card of matching colour
     *
     * @return True if at least 1 card is playable, False otherwise.
     */
    public boolean hasPlayableColour() {
        ArrayList<Card> hand = turn.getHand().getHand();
        for (Card c : hand) {
            if (c.getColour() == discardPile.showTop().getColour()) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the draw pile is empty, we need to take the cards in the discard pile and re-shuffle into the new draw pile
     */
    public boolean checkDrawPile() {
        if (drawPile.showTop() == null) {
            drawPile.newEmptyDeck();
            while (discardPile.showTop() != null) {
                if (discardPile.showTop() != lastCardPlayed) {
                    drawPile.addCard(discardPile.getTop());
                } else {
                    discardPile.getTop();
                }
            }
            drawPile.shuffle();
            discardPile.newSingleDeck(lastCardPlayed);
            return true;
        } else {
            return false;
        }
    }

    /**
     * GUI implementation for Draw Card
     * Can use either Draw button or Draw Pile
     * @return The card that was drawn
     */
    public Card playerDrawCard(Player curTurn) {
        Card top = null;
        checkDrawPile();

        if (drawPile.showTop() == null) {
            for (UNOModelView view : views) {
                view.handleUNONoDraw();
            }
        } else {
            top = drawPile.showTop();
            drawCard(curTurn);
            if (isValidCard(top)) {
                //Add play Drawn card if player wants
                for (UNOModelView view : views) {
                    view.handleUNOPlayDrawn(top);
                }
            } else {
                nextTurn(); //Ensures that only go next turn if player draws card
            }
        }

        handleTurnDiscard();
        return top;
    }

    /**
     * Plays the card that was clicked on or drawn
     * @param card the Card that is being played
     */
    public void playerPlayCard(Card card) {
        Card cardPlayed;
        cardPlayed = card;

        // Check if the player hand has any valid cards to play
        if (hasPlayableCard()) {
            if (!isValidCard(cardPlayed)) {
                handleTurnDiscard();
                return;
            }
            // Once a valid card is selected, update the piles, lastCardPlayed, and remove it from hand of player
            lastCardPlayed = cardPlayed;
            turn.getHand().removeCard(lastCardPlayed);

            if (turn.callUno()) {
                for (UNOModelView view : views) {
                    view.handleUNOCallUNO(new UNOEvent(this, turn));
                }
            }

            playCard(lastCardPlayed);

            handleTurnDiscard();

        }

        if (!lastCardPlayed.isWild()) {
            wildColour = Colours.WILD;
        }
        checkDrawPile();

        //Check if the current player is out of cards
        if (turn.isHandEmpty()) {
            roundWinner();
            // Check if the player won the game
            if (turn.getPoints() >= 500) {
                gameWinner();
            } else {
                // Otherwise, start a new round
                nextRound();
                handleTurnDiscard();
                round++;
            }
        } else {
            nextTurn();
            handleTurnDiscard();
        }
    }

    /**
     * Retrieves the top card of the discard pile
     * @return  top card of discard pile, or null
     */
    public Card getLastCardPlayed(){return lastCardPlayed;}

    /**
     * Loads the last state of UNOModel
     * @return UNOModel instance, or null if any error occurs
     */
    public UNOModel loadLastState() {return undoState;}

    /**
     * Loads the most recent redo state of UNOModel
     * @return UNOModel instance, or null if any error occurs
     */
    public UNOModel loadRedoState() {return redoState;}

    /**
     * Saves model instance state
     * Note: certain UNOFlip objects are copied to create unique objects
     * instead of passing references
     */
    public void saveLastState() {
        UNOModel lastModel = new UNOModel();
        for(Player p: this.players) {
            lastModel.players.add(p.copyPlayer());
        }
        for(Player p: lastModel.players) {
            if(p.getName().equals(this.turn.getName())) {
                lastModel.turn = p;
            }
        }
        lastModel.drawPile = this.drawPile.copyPile();
        lastModel.discardPile = this.discardPile.copyPile();
        lastModel.lastCardPlayed = this.lastCardPlayed.copyCard();
        lastModel.direction = this.direction;
        lastModel.wildColour = this.wildColour;
        lastModel.round = this.round;
        undoState = lastModel;
    }

    /**
     * saveRedoState saves a model instance state
     * Note: certain UNOFlip objects are copied to create unique objects
     * instead of passing references
     */
    public void saveRedoState() {
        UNOModel redoModel = new UNOModel();
        for(Player p: this.players) {
            redoModel.players.add(p.copyPlayer());
        }
        for(Player p: redoModel.players) {
            if(p.getName().equals(this.turn.getName())) {
                redoModel.turn = p;
            }
        }
        redoModel.drawPile = this.drawPile.copyPile();
        redoModel.discardPile = this.discardPile.copyPile();
        redoModel.lastCardPlayed = this.lastCardPlayed.copyCard();
        redoModel.direction = this.direction;
        redoModel.wildColour = this.wildColour;
        redoModel.round = this.round;
        redoState = redoModel;
    }

    /**
     * Updates the state of model attributes
     */
    public void updateState(UNOModel model){
        this.players = model.players;
        this.drawPile = model.drawPile;
        this.discardPile = model.discardPile;
        this.turn = model.turn;
        this.lastCardPlayed = model.lastCardPlayed;
        this.direction = model.direction;
        this.wildColour = model.wildColour;
        this.round = model.round;
    }

    /**
     * Retrieves the discard pile
     * @return discardPile
     */
    public CardPile getDiscardPile() {return discardPile;}

    /**
     * @return returns the array list of saves
     */
    public ArrayList<String> getSaves() {
        return saves;
    }

    public void serialize(String fileName) {
        try{
            saves.add(fileName);

            //Create output stream and write to it
            FileOutputStream fileOut =new FileOutputStream(fileName + ".txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.flush();
            out.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void unserialize(String fileName) {
        try{
            //Create input stream
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + ".txt"));
            UNOModel um = (UNOModel) in.readObject();

            //this.views = um.views;
            this.players = um.players;
            this.drawPile = um.drawPile;
            this.discardPile = um.discardPile;
            this.turn = um.turn;
            this.lastCardPlayed = um.lastCardPlayed;
            this.direction = um.direction;
            this.random = um.random;
            //this.controller = new UNOController(this);
            this.wildColour = um.wildColour;
            this.round = um.round;
            this.undoState = um.undoState;
            this.redoState = um.redoState;
            this.saves = um.saves;

            in.close();

        }catch(Exception e) {
            System.out.println(e);
        }
    }


    public JsonObject toJSON(){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(Player p: players){
            builder.add(p.toJSON());
        }

        JsonArray ja_players = builder.build();

        JsonObject object = Json.createObjectBuilder()
                .add("players", ja_players)
                .add("turn", turn.toJSON())
                .add("drawPile", drawPile.toJSON())
                .add("discardPile", discardPile.toJSON())
                .add("lastCardPlayed", lastCardPlayed.toJSON())
                .add("direction", direction)
                .add("wildColour", wildColour.toString())
                .add("round", round)
                .build();

        return object;
    }

    public void exportToJSON(String fileName) throws IOException {

        saves.add(fileName);

        try(PrintWriter writer = new PrintWriter(new FileWriter(fileName + ".json"))){
            JsonObject jsonObject = toJSON();
            writer.println(jsonObject);
        }
    }

    public void  restoreFromJsonFile(String fileName) throws IOException {
        try {
            fileName += ".json";
            FileReader fileReader = new FileReader(fileName);
            JsonReader jsonReader = Json.createReader(fileReader);
            this.players = new ArrayList<>();

            FileInputStream fileInputStream = new FileInputStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            ObjectMapper objectMapper = new ObjectMapper();
            String line;

            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("players");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject playerObject = jsonArray.getJsonObject(i);
                    objectMapper = new ObjectMapper();
                    // Create a StringWriter to write JSON string
                    StringWriter stringWriter = new StringWriter();

                    // Create a JsonWriter from StringWriter
                    try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
                        // Write the JsonObject to the JsonWriter
                        jsonWriter.writeObject(playerObject);
                    }
                    // Get the JSON string from StringWriter
                    String jsonString = stringWriter.toString();
                    Player player = objectMapper.readValue(jsonString, Player.class);
                    this.players.add(player);
                }
            }
            jsonReader.close();
            fileReader.close();

            line = reader.readLine();
            UNOModel model = objectMapper.readValue(line, UNOModel.class);
            updateState(model);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checkHandSideAndFlip ensures that the discard pile cards are always facing the same side as the player cards.
     * Used when undoing a players move
     */
    public void checkHandSideAndFlip() {
        try {
            if (!(turn.getHand().getCard(0).isLight() == discardPile.showTop().isLight())) {
                discardPile.flipDeck();
            }

            if (!(turn.getHand().getCard(0).isLight() == drawPile.showTop().isLight())) {
                drawPile.flipDeck();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * checkDiscardSideAndFlip ensures that the player hands are always facing the same side as the discard pile.
     * Used when redoing a players Flip Card
     */
    public  void checkDiscardSideAndFlip() {
        try {
            if (!(discardPile.showTop().isLight() == turn.getHand().getCard(0).isLight())) {
                for (Player p : players) {
                    p.flipHand();
                }
            }
            if (!(discardPile.showTop().isLight() == drawPile.showTop().isLight())) {
                drawPile.flipDeck();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
