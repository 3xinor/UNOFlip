package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controls the user inputs for the UNO game, and updates the model to reflect the changes
 *
 * @author Sebi Magyar-Samoila 101223588
 * @author Owen Renette 101223576
 *
 * @version October 21, 2023
 */

public class UNOController implements ActionListener {
    private UNOModel model;
    private UNOView view;
    private ScheduledExecutorService timer;

    public UNOController(UNOModel model) {
        this.model = model;
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Takes the user input for the user Challenge
     * @return if the player challenges
     */
    public boolean askChallenge(String name){
        System.out.println("\nWould the next player like to challenge? Yes (1) or No (2)");
        return view.inputWildChallenge(name);
    }

    /**
     * askColour invokes a pop-up to ask the player the colour change they want to make for Light.
     * @return colour The colour that was selected.
     */
    public Colours askColour(boolean light){
        return view.inputWildColours(light);
    }


    /**
     * Sets the view for the controller
     * @param view is set to the corresponding view
     */
    public void setView(UNOView view){
        this.view = view;
    }

    /**
     * Associates the action event with a corresponding action and performs set action
     * @param e used to find the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        Player turn = model.getTurn();
        Card card = null;
        switch(action.toLowerCase()){
            /*
             * Start Game Panel
             */
            case "startgame":
                //Set to the player configuration screen
                view.setToPlayerCreationPanel();
                break;
            case "numplayerupdate":
                //Updates the current number of players selected during player configuration
                view.displaySelectedNumPlayers();
                break;
            case "player2computer", "player3computer", "player4computer":
                //Updates player 2 human/AI toggling
                view.updateDifficultyMenus();
                break;
            case "playerconfigconfirmed":
                //The player configuration was confirmed, and not the game needs to be set up
                //Get player info
                int numPlayers = view.getNumPlayers();
                for (int i = 1; i <= numPlayers; i++) {
                    if (!(view.isComputer("p" + i))) {
                        model.addPlayer(view.getPlayerName("p" + i));
                    } else {
                        model.addComputerPlayer(view.getPlayerName("p" + i), view.getComputerDifficulty("p" + i));
                    }
                }

                //Initialize the game
                model.start();

                // Enable a fixed rate timer which runs a subroutine every 5 seconds
                this.timer.scheduleAtFixedRate(this::AITurnCheck,0, 5, TimeUnit.SECONDS);

                //Change Panel
                System.out.println("Setting Panel");
                view.setToGamePanel();
                view.showTurnHand(model.getTurn());
                view.enableStatusMenu();
                break;
            /*
             * Play Game Panel
             */
            case "draw":
                model.saveLastState();
                model.playerDrawCard(turn);
                view.showTurnHand(model.getTurn());
                view.enableUndo();
                view.disableRedo();


                break;
            /*
             * For Each card Clicked
             */
            case "card" :
                if (model.hasPlayableCard()){
                    for(Card c: turn.getHand().getHand()){
                        if(c.getIcon() == ((JButton) e.getSource()).getIcon()){
                            card = c;
                        }
                    }
                    System.out.println(card);
                    model.saveLastState();
                    model.playerPlayCard(card);
                    view.showTurnHand(model.getTurn());
                }
                view.enableUndo();
                view.disableRedo();
                break;

            /* Handle Menu actions */

            case "scores":
                view.showAllPoints(model);
                break;

            case "round":
                view.showRoundNumber(model.getRound());
                break;

            case "exit":
                System.exit(0);
                break;

            case "restart":
                //ToDo
                break;

            case "save":
                String fileName = view.inputFileName();
                //try {
                model.serialize(fileName);
                //} catch (IOException ex) {
                //    throw new RuntimeException(ex);
                //}
                //break;

            case "load":
                if(model.getSaves().size() > 0) {
                    String name = view.loadFileName();
                    //try {
                    model.unserialize(name);
                    view.updateGameStatus();
                    view.showTurnHand(model.getTurn());
                    model.handleTurnDiscard();
                    //view.showDiscardedCard(model.getDiscardPile().showTop());

                    //} catch (IOException ex) {
                    //    throw new RuntimeException(ex);
                    //}
                }
                else {
                    view.showNoSaves();
                }
                break;

            case "undo":
                try {
                    model.saveRedoState();
                    UNOModel lastModel = model.loadLastState();
                    // check and see if a flip is being undone
                    model.updateState(lastModel);
                    model.checkHandSideAndFlip();
                    view.updateGameStatus();
                    view.showDiscardedCard(model.getDiscardPile());
                    view.showTurnHand(model.getTurn());
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }
                view.disableUndo();
                view.enableRedo();
                break;

            case "redo":
                try {
                    model.saveLastState();
                    UNOModel redoModel = model.loadRedoState();
                    model.updateState(redoModel);
                    model.checkDiscardSideAndFlip();
                    view.updateGameStatus();
                    view.showDiscardedCard(model.getDiscardPile());
                    view.showTurnHand(model.getTurn());
                } catch(NullPointerException e1) {
                    e1.printStackTrace();
                }
                view.disableRedo();
                view.enableUndo();
                break;

            default:
                break;
        }
    }

    /**
     * AITurnCheck is the service routine for a timer interrupt which periodically checks if the current player is AI.
     * If so, this function simulates the AI players turn.
     */
    private void AITurnCheck() {
        if (model.getTurn() instanceof AI) {
            view.disableUndo();
            view.disableRedo();
            AIDecisions decision = ((AI) model.getTurn()).smartDecision(model.getLastCardPlayed());

            switch(decision) {
                case DRAW:
                    model.playerDrawCard(model.getTurn());
                    break;

                case PLAY_CARD:
                    Card playCard = ((AI) model.getTurn()).smartPlayCard(model.getLastCardPlayed(), model.getWildColour());
                    model.playerPlayCard(playCard);
                    break;
            }
        }
        if (!(model.getTurn() instanceof AI)){
            view.enablePanelHand();
            view.enableDrawButtons();
        }
    }
}