package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * UNOView allows the user to see an output from the game of UNO they are playing
 *
 * @author Owen Renette 101223576
 * @version October 21, 2023
 */
public class UNOView extends JFrame implements UNOModelView {

    private static String htmlHelp;
    private UNOModel model;
    private UNOController control;

    //Main Game GUI components
    private JPanel gameContentPane;
    private JLabel gameStatus;
    private JPanel panelTable;
    private JLabel otherPlayerStatus;
    private JPanel panelActions;
    private JButton draw;
    private JPanel panelTableCards;
    private JButton drawPile;
    private JButton playerCard;
    private JButton discardPile;
    private JPanel panelCurPlayer;
    private JLabel playerStatus;
    private JPanel panelHand;
    private JScrollPane scrollPane;
    private UnoMenu gameMenu;

    //Start Screen Components
    private JPanel startContentPane;
    private JPanel contentPane;

    //Player selection Pane
    private JPanel playerSelectionPane;
    private JComboBox<Integer> numPlayers;
    private JPanel p1Pane, p2Pane, p3Pane, p4Pane;
    private JTextField player1Name, player2Name, player3Name, player4Name;
    private JRadioButton computerRadioButton2, computerRadioButton3, computerRadioButton4;
    private JComboBox<String> p2Diff, p3Diff, p4Diff;
    public static final String[] difficulties = {"EASY", "NORMAL", "HARD"};

    //End Game Components
    private JPanel endContentPane;


    public UNOView() {

        super("UNO Flip");
        this.setLayout(new BorderLayout());
        gameContentPane = new JPanel(new BorderLayout());

        model = new UNOModel();
        model.addUNOView(this);

        control = model.getController();
        control.setView(this);

        createGamePanel(); // gameContentPane
        createStartPanel(); // startContentPane
        createMenuBar(); // menu bar initialization
        createPlayerSelectionPane(); // player info init

        //setting the Frame
        contentPane = (JPanel) this.getContentPane();
        contentPane.add(startContentPane);
        this.setJMenuBar(gameMenu);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 700));
        this.pack();
        this.setVisible(true);
        this.setResizable(true);


        //model.playGame();
    }

    /**
     * help explains the rules of the game
     */
    public static void showHelp() {
        System.out.println("UNO RULES:");
        System.out.println("The objective of the game is to win by getting 500 points.");
        System.out.println("To win points, you have to win rounds by emptying your hand");
        System.out.println("This is done by playing your cards on your turn, according to a few rules:");
        System.out.println("    1. The played card is dependent off the previous discarded card;");
        System.out.println("        a. Number cards must match the colour or number;");
        System.out.println("        b. Reverse, Skip, and Draw cards must match the colour;");
        System.out.println("        c. Wild cards can be played no matter what;");
        System.out.println("    2. If you cannot play a card you have (or if you don't want to), draw a card from the draw pile;");
        System.out.println("        a. If the new card is playable, play it right away;");
        System.out.println("        b. If the new card is not playable, keep it and pass your turn;");
        System.out.println("    3. If you have one card left call UNO (failure to do so will be +2 cards)");
        System.out.println("    4. IF you have no card remaining, you win the round");
        System.out.println("        SCORING:    Number cards - face value");
        System.out.println("                    Draw One - 10P");
        System.out.println("                    Draw Five/Reverse/Skip/Flip - 20P");
        System.out.println("                    Skip Everyone - 30P");
        System.out.println("                    Wild - 40P");
        System.out.println("                    Wild Draw Two - 50P");
        System.out.println("                    Wild Draw Colour - 60P");
        System.out.println("    5. The turn falls to the next player");
        System.out.println("For more information visit the online rules: https://www.ultraboardgames.com/uno/flip-game-rules.php");
    }

    /**
     * Creates a new Menu Bar object to be added to the game
     */
    public void createMenuBar() {
        /* Initialization of all items is done in separate class for readability/maintainability */
        gameMenu = new UnoMenu();

        gameMenu.getScoreMenuItem().addActionListener(control);
        gameMenu.getRoundMenuItem().addActionListener(control);
        gameMenu.getExitMenuItem().addActionListener(control);
        gameMenu.getRestartMenuItem().addActionListener(control);
        gameMenu.getSaveMenuItem().addActionListener(control);
        gameMenu.getLoadMenuItem().addActionListener(control);
        gameMenu.getUndoMenuItem().addActionListener(control);
        gameMenu.getRedoMenuItem().addActionListener(control);
    }

    /**
     * Enables the status menu
     */
    public void enableStatusMenu() {
        gameMenu.getStatusMenu().setEnabled(true);
    }

    /**
     * creates the panel for the Game
     */
    private void createGamePanel() {
        /*
         Setting Top of Frame --
         * Status of Game, PLayer's and Points
         */
        gameStatus = new JLabel("", SwingConstants.CENTER);
        gameStatus.setText("Playing...");
        gameStatus.setPreferredSize(new Dimension(1000, 50));
        gameStatus.setBackground(Color.GREEN);
        gameStatus.setOpaque(true);
        gameContentPane.add(gameStatus, BorderLayout.NORTH);

        /*
        Setting Middle of Frame --
         * Panel containing the Table (Card Piles)
         * Contains PLayer Rotation
         */
        panelTable = new JPanel(new BorderLayout());
        /*
         * Sets Player Turn Rotations
         * See Game messages
         */
        otherPlayerStatus = new JLabel("Other Player", SwingConstants.CENTER);
        otherPlayerStatus.setPreferredSize(new Dimension(1000, 75));
        otherPlayerStatus.setBackground(Color.GREEN);
        otherPlayerStatus.setOpaque(true);
        otherPlayerStatus.setText("<html> Turn Order <br> Player x: Hand Size <br>Player x :Hand Size <html>");

        panelTable.add(otherPlayerStatus, BorderLayout.NORTH);


        /*
         * Sets the possible player actions during their turn
         * Draw -- should draw a card
         * Play -- should allow for cards to be played, cards click should be disabled until this is clicked
         */
        panelActions = new JPanel();
        draw = new JButton("Draw");
        draw.setActionCommand("Draw");
        panelActions.add(draw);
        panelTable.add(panelActions, BorderLayout.SOUTH);

        /*
         * Sets the Card Piles on the Table
         * Draw Pile -- Always disabled and shows back of card
         *            -- Maybe enable and make it alternative to draw button
         * Discard Pile -- Always disabled and shows the top card on the pile
         */
        panelTableCards = new JPanel();
        ImageIcon drawPileCard = new ImageIcon(new ImageIcon(getClass().getResource("LightCards/Back.png")).getImage().getScaledInstance(120, 191, Image.SCALE_DEFAULT));
        drawPile = new JButton(drawPileCard);
        drawPile.setActionCommand("Draw");
        discardPile = new JButton();
        panelTableCards.add(drawPile);
        panelTableCards.add(discardPile);
        panelTable.add(panelTableCards, BorderLayout.CENTER);

        gameContentPane.add(panelTable, BorderLayout.CENTER);

        /*
         Setting Bottom of Frame --
         *  Panel containing current player info
         *  Contains Player Hand with buttons for cards
         */
        panelCurPlayer = new JPanel(new BorderLayout());

        /*
         * Sets the Current Player's status
         */
        playerStatus = new JLabel("Current Player Status", SwingConstants.CENTER);
        playerStatus.setBackground(Color.CYAN);
        playerStatus.setOpaque(true);
        playerStatus.setPreferredSize(new Dimension(1000, 50));
        panelCurPlayer.add(playerStatus, BorderLayout.NORTH);

        //Cards Hand
        panelHand = new JPanel(new FlowLayout());

        panelCurPlayer.add(panelHand, BorderLayout.CENTER);

        scrollPane = new JScrollPane(panelHand, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panelCurPlayer.add(scrollPane, BorderLayout.SOUTH);

        panelCurPlayer.setSize(1000, 300);
        gameContentPane.add(panelCurPlayer, BorderLayout.SOUTH);

        drawPile.addActionListener(control);
        draw.addActionListener(control);

    }

    /**
     * creates the panel for the start menu
     */
    public void createStartPanel() {
        startContentPane = new JPanel(new BorderLayout());

        JLabel welcome = new JLabel("", SwingConstants.CENTER);
        welcome.setText("<html> <br>WELCOME TO UNO <br><br>" +
                "To play simply click the Play button <html>");

        /*
        Initializes Button to start game
         */
        JPanel startButtons = new JPanel();
        JButton playGame = new JButton("Play Game");
        playGame.setActionCommand("StartGame");
        playGame.addActionListener(control);
        playGame.setSize(new Dimension(200, 50));

        startButtons.add(playGame);

        JLabel helpMessage = new JLabel("", SwingConstants.CENTER);
        htmlHelp = ("<html>----------------------------------------------------------------------------------------------------------" +
                ("<br>UNO RULES:") +
                ("<br>The objective of the game is to win by getting 500 points.") +
                ("<br>To win points, you have to win rounds by emptying your hand") +
                ("<br>This is done by playing your cards on your turn, according to a few rules:") +
                ("<br>    1. The played card is dependent off the previous discarded card;") +
                ("<br>        a. Number cards must match the colour or number;") +
                ("<br>        b. Reverse, Skip, and Draw cards must match the colour;") +
                ("<br>        c. Wild cards can be played no matter what;") +
                ("<br><br>    2. If you cannot play a card you have (or if you don't want to), draw a card from the draw pile;") +
                ("<br>        a. If the new card is playable, play it right away;") +
                ("<br>        b. If the new card is not playable, keep it and pass your turn;") +
                ("<br><br>    3. If you have one card left call UNO (failure to do so will be +2 cards)") +
                ("<br><br>    4. IF you have no card remaining, you win the round") +
                ("<br>        SCORING:    Number cards - face value") +
                ("<br>                    Draw One - 10P") +
                ("<br>                    Draw Five/Reverse/Skip/Flip - 20P") +
                ("<br>                    Skip Everyone - 30P") +
                ("<br>                    Wild - 40P") +
                ("<br>                    Wild Draw Two - 50P") +
                ("<br>                    Wild Draw Colour - 60P") +
                ("<br><br>    5. The turn falls to the next player") +
                ("<br>For more information visit the online rules: https://www.ultraboardgames.com/uno/flip-game-rules.php<html>") +
                ("<br>----------------------------------------------------------------------------------------------------------")
        );
        helpMessage.setText(htmlHelp);

        /*
        Adds UNOFlip image to menu
         */
        ImageIcon background = new ImageIcon(getClass().getResource("LightCards/Start.png"));
        background = new ImageIcon(background.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT));
        helpMessage.setIcon(background);

        startContentPane.add(welcome, BorderLayout.NORTH);
        startContentPane.add(startButtons, BorderLayout.CENTER);
        startContentPane.add(helpMessage, BorderLayout.SOUTH);
    }

    /**
     * Creates a content pane, where users can configure the number of players and their names, and if
     * they want computer players as well.
     */
    public void createPlayerSelectionPane() {
        playerSelectionPane = new JPanel(new BorderLayout());

        //Num Player selection
        JPanel numPlayersPane = new JPanel(new GridLayout(2,1));
        JLabel title = new JLabel("Select Number of Players:", SwingConstants.CENTER);
        numPlayersPane.add(title);
        Integer[] choices = {2, 3, 4};
        numPlayers = new JComboBox<>(choices);
        numPlayers.setActionCommand("numPlayerUpdate");
        numPlayers.addActionListener(control);
        numPlayersPane.add(numPlayers);
        playerSelectionPane.add(numPlayersPane, BorderLayout.NORTH);

        //Confirm selection button
        JButton confirm = new JButton("Confirm");
        confirm.setActionCommand("PlayerConfigConfirmed");
        confirm.addActionListener(control);
        confirm.setSize(new Dimension(500, 300));
        playerSelectionPane.add(confirm, BorderLayout.SOUTH);

        //Player Info
        JPanel playerInfoPane = new JPanel(new GridLayout(4,1,50, 50));
        //P1
        p1Pane = new JPanel(new FlowLayout());
        p1Pane.add(new JLabel("Player 1: "));
        JRadioButton computerRadioButton1 = new JRadioButton("Computer");
        computerRadioButton1.setEnabled(false);
        p1Pane.add(computerRadioButton1);
        player1Name = new JTextField(15);
        player1Name.setText("Player_1");
        p1Pane.add(player1Name);
        playerInfoPane.add(p1Pane);
        //P2
        p2Pane = new JPanel(new FlowLayout());
        p2Pane.add(new JLabel("Player 2: "));
        computerRadioButton2 = new JRadioButton("Computer");
        computerRadioButton2.setActionCommand("player2Computer");
        computerRadioButton2.addActionListener(control);
        p2Pane.add(computerRadioButton2);
        player2Name = new JTextField(15);
        player2Name.setText("Player_2");
        p2Diff = new JComboBox<>(difficulties);
        p2Diff.setVisible(false);
        p2Pane.add(player2Name);
        p2Pane.add(p2Diff);
        playerInfoPane.add(p2Pane);
        //P3
        p3Pane = new JPanel(new FlowLayout());
        p3Pane.add(new JLabel("Player 3: "));
        computerRadioButton3 = new JRadioButton("Computer");
        computerRadioButton3.setActionCommand("player3Computer");
        computerRadioButton3.addActionListener(control);
        p3Pane.add(computerRadioButton3);
        player3Name = new JTextField(15);
        player3Name.setText("Player_3");
        p3Diff = new JComboBox<>(difficulties);
        p3Diff.setVisible(false);
        p3Pane.add(player3Name);
        p3Pane.add(p3Diff);
        p3Pane.setVisible(false);
        playerInfoPane.add(p3Pane);
        //P4
        p4Pane = new JPanel(new FlowLayout());
        p4Pane.add(new JLabel("Player 4: "));
        computerRadioButton4 = new JRadioButton("Computer");
        computerRadioButton4.setActionCommand("player4Computer");
        computerRadioButton4.addActionListener(control);
        p4Pane.add(computerRadioButton4);
        player4Name = new JTextField(15);
        player4Name.setText("Player_4");
        p4Diff = new JComboBox<>(difficulties);
        p4Diff.setVisible(false);
        p4Pane.add(player4Name);
        p4Pane.add(p4Diff);
        p4Pane.setVisible(false);
        playerInfoPane.add(p4Pane);

        playerSelectionPane.add(playerInfoPane, BorderLayout.CENTER);

        playerSelectionPane.revalidate();
        playerSelectionPane.repaint();
    }

    /**
     * When the number of players selected in the player selection Pane changes,
     * the information to fill out will be updated
     */
    public void displaySelectedNumPlayers() {
        p3Pane.setVisible(false);
        p4Pane.setVisible(false);
        for (int i = 3; i <= getNumPlayers(); i++) {
            if (i == 3) {
                p3Pane.setVisible(true);
                p3Pane.revalidate();
                p3Pane.repaint();
            } else if (i == 4) {
                p4Pane.setVisible(true);
                p4Pane.revalidate();
                p4Pane.repaint();
            }
        }
    }

    /**
     * When a computer player is selected/unselected, the difficulty menu will be toggled here,
     * as human players should not have a selectable difficulty
     */
    public void updateDifficultyMenus() {
        p2Diff.setVisible(isComputer("p2"));
        p2Diff.revalidate();
        p2Diff.repaint();

        p3Diff.setVisible(isComputer("p3"));
        p3Diff.revalidate();
        p3Diff.repaint();

        p4Diff.setVisible(isComputer("p4"));
        p4Diff.revalidate();
        p4Diff.repaint();
    }

    /**
     * Gets the selected item from the player number drop down box
     * @return The number of players selected by the user.
     */
    public int getNumPlayers() {
        return (int) numPlayers.getSelectedItem();
    }

    /**
     * Returns the name of Player X to the Controller for play initialization in the model
     * If the field is not filled, it will assign a default player name
     * @param player String on the format of "pX", where X is the player number
     * @return The Name of the player.
     */
    public String getPlayerName(String player) {
        String name;
        if ("p1".equals(player)) {
            name = player1Name.getText();
            if (name.equals("")) {name = "Player_1";}
        } else if ("p2".equals(player)) {
            name = player2Name.getText();
            if (name.equals("")) {name = "Player_2";}
        } else if ("p3".equals(player)) {
            name = player3Name.getText();
            if (name.equals("")) {name = "Player_3";}
        } else if  ("p4".equals(player)) {
            name = player4Name.getText();
            if (name.equals("")) {name = "Player_4";}
        } else {
            name = "ERROR";
        }
        return name;
    }

    /**
     * Determined where the selected player is supposed to be human or AI
     * @param player String specifying "pX", where X is the player number
     * @return true if the player is a computer, false if human
     */
    public boolean isComputer(String player) {
        return switch (player) {
            case "p2" -> computerRadioButton2.isSelected();
            case "p3" -> computerRadioButton3.isSelected();
            case "p4" -> computerRadioButton4.isSelected();
            default -> false;
        };
    }

    /**
     * Retrieves the user selected difficulty of the AI they added to the game
     * @param player String specifying "pX", where X is the player number
     * @return String of difficulty
     */
    public String getComputerDifficulty(String player) {
        return switch (player) {
            case "p2" -> (String) p2Diff.getSelectedItem();
            case "p3" -> (String) p3Diff.getSelectedItem();
            case "p4" -> (String) p4Diff.getSelectedItem();
            default -> "NORMAL";
        };
    }

    /**
     * Creates the content pane for the ending
     */
    public void createEndContentPane() {
        endContentPane = new JPanel(new GridLayout(2, 1));

        JLabel winner = new JLabel("", SwingConstants.CENTER);
        winner.setText(model.getTurn().getName() + " won the game!");
        winner.setBackground(Color.GREEN);
        winner.setOpaque(true);
        winner.setForeground(Color.BLUE);
        winner.setFont(new Font("Serif", Font.PLAIN, 32));
        endContentPane.add(winner);

        JLabel points = new JLabel("", SwingConstants.CENTER);
        points.setText(model.getPlayersPointsString());
        points.setBackground(Color.GREEN);
        points.setOpaque(true);
        points.setFont(new Font("Serif", Font.PLAIN, 24));
        endContentPane.add(points);
    }

    /**
     * inputWildColours Creates a popup menu to select the colour of a wild card
     */
    public Colours inputWildColours(boolean light) {
        String colour;
        if(light) {
            String[] validColoursString = {"RED", "BLUE", "GREEN", "YELLOW"};
            colour = "RED";
            Object selection;
            do {
                selection = JOptionPane.showInputDialog(null, "Pick a colour for your Wild:", "Colours", JOptionPane.QUESTION_MESSAGE, null, validColoursString, "Red");
                if (selection != null) {
                    colour = selection.toString();
                }
            } while (selection == null);
        }
        else {
            String[] validColoursString = {"ORANGE", "PURPLE", "CYAN", "PINK"};
            colour = "ORANGE";
            Object selection;
            do {
                selection = JOptionPane.showInputDialog(null, "Pick a colour for your Wild:", "Colours", JOptionPane.QUESTION_MESSAGE, null, validColoursString, "Orange");
                if (selection != null) {
                    colour = selection.toString();
                }
            } while (selection == null);
        }

        return Colours.valueOf(colour);
    }

    /**
     * inputWildChallenge Creates a popup menu to ask the opposed player if they wish to challenge the wild card
     *
     * @return String The yes/no option selected
     */
    public boolean inputWildChallenge(String name) {
        int selection = JOptionPane.showConfirmDialog(null, name + ", do you want to challenge?", "Challenge", JOptionPane.YES_NO_OPTION);

        return selection == JOptionPane.YES_OPTION;
    }

    /**
     * asks for the filename the user wants to save their game state as
     *
     * @return String of the fileName
     */
    public String inputFileName(){

        return JOptionPane.showInputDialog(null, "What do you wan tto save your game as?", "Save", JOptionPane.PLAIN_MESSAGE );

    }

    /**
     * asks for which file to load from all loaded states this game
     *
     * @return String of fileName
     */
    public String loadFileName(){
        String name;
        ArrayList<String> valid = model.getSaves();
        String validString[] = valid.toArray(new String[valid.size()]);
        name = valid.get(0);
        Object selection;
        do {
            selection = JOptionPane.showInputDialog(null, "Pick a File:", "Files", JOptionPane.QUESTION_MESSAGE, null, validString, validString[0]);
            if (selection != null) {
                name = selection.toString();
            }
        } while (selection == null);

        return String.valueOf(selection);
    }


    /**
     * Changes Current ContentPane to the Game Panel
     */
    public void setToGamePanel() {
        contentPane.removeAll();
        contentPane.add(gameContentPane);
        contentPane.revalidate();
        contentPane.repaint();
        showPlayerNames();
    }

    /**
     * Changes Current ContentPane to the Start Panel
     */
    public void setToStartPanel() {
        contentPane.removeAll();
        contentPane.add(startContentPane);
        contentPane.revalidate();
        contentPane.repaint();
    }

    /**
     * Changes start screen to Player info panel
     */
    public void setToPlayerCreationPanel() {
        contentPane.removeAll();
        contentPane.add(playerSelectionPane);
        contentPane.revalidate();
        contentPane.repaint();
    }

    /**
     * Changes Current ContentPane to the End Panel
     */
    public void setToEndPanel() {
        contentPane.removeAll();
        contentPane.add(endContentPane);
        contentPane.revalidate();
        contentPane.repaint();
    }

    /**
     * Updates the Status Bar of the game to show active events
     */
    public void updateGameStatus() {
        String textUpdate = "";
        boolean eventActive = false;
        for (Player player : model.getPlayers()) {
            if (player.getHand().handSize() == 1) {
                textUpdate += player.getName() + " called UNO!  ";
                eventActive = true;
            }
            if (player.isHandEmpty()) {
                textUpdate = player.getName() + " wins the Round!";
                eventActive = true;
                break;
            }
            if (player.getPoints() >= 500) {
                textUpdate = player.getName() + " wins the Game!";
                eventActive = true;
                break;
            }
        }
        gameStatus.setText("<html>" + textUpdate + "<html>");
        if (!eventActive) {
            gameStatus.setText("<html>Playing...<html>");
        }
    }

    /**
     * Updates the Game panel to show the player names
     */
    public void showPlayerNames() {
        //Get direction
        String direction;
        if (model.getDirection()) {
            direction = "Clockwise";
        } else {
            direction = "Counter-Clockwise";
        }
        if (model.getWildColour() == null) {
            playerStatus.setText("<html>" + model.getTurn().getName() + "'s Turn (" + model.getTurn().getPoints() + " pts) <html>");
        } else {
            playerStatus.setText("<html>" + model.getTurn().getName() + "'s Turn (" + model.getTurn().getPoints() + " pts) <br>"
                    + "Wild Colour: " + model.getWildColour() + "<html>");
        }

        String otherPlayerText = "Direction: " + direction + "<br>";

        // Update Player info
        if (model.getDirection()) {
            for (int i = 0; i < model.getNumPlayers(); i++) {
                if (model.getPlayersByClockwise().get(i) != model.getTurn()) {
                    otherPlayerText += model.getPlayersByClockwise().get(i).getName() + " -> Hand Size " + model.getPlayersByClockwise().get(i).getHand().handSize() + " (" + model.getPlayersByClockwise().get(i).getPoints() + " pts) <br>";
                }
            }
        } else {
            for (int i = model.getNumPlayers() - 1; i > -1; i--) {
                if (model.getPlayersByClockwise().get(i) != model.getTurn()) {
                    otherPlayerText += model.getPlayersByClockwise().get(i).getName() + " -> Hand Size " + model.getPlayersByClockwise().get(i).getHand().handSize() + " (" + model.getPlayersByClockwise().get(i).getPoints() + " pts) <br>";
                }
            }
        }
        otherPlayerStatus.setText("<html>" + otherPlayerText + "<html>");
    }

    /**
     * showTurnHand prints the output of the current players hand
     */
    public void showTurnHand(Player player) {
        System.out.println("\n\u001B[33m" + player.getName() + "'s Hand:\u001B[0m");
        ArrayList<Card> cards = player.getHand().getHand();
        showPlayerNames();
        clearPanelHand();
        int i = 0;
        for (Card c : cards) {
            playerCard = new JButton(c.getIcon());
            playerCard.setActionCommand("Card"); // For card playability
            playerCard.addActionListener(control);
            panelHand.add(playerCard);
            i++;
            System.out.println(i + ". " + c);
        }
        if (player instanceof AI){
            disablePanelHand();
            disableDrawButtons();
        } else {
            enablePanelHand();
            enableDrawButtons();
        }
        this.repaint();
        this.revalidate();
    }

    /**
     * clearPanelHand removes all components from the hand panel container
     */
    public void clearPanelHand() {
        this.panelHand.removeAll();
        this.panelHand.repaint();
    }

    /**
     * clearPanelTableCards removes all components from the table panel container
     */
    public void clearPanelTableCards() {
        this.panelTableCards.removeAll();
        this.panelTableCards.repaint();
    }

    /**
     * showTableCards displays current table on GUI
     */
    public void showTableCards() {
        clearPanelTableCards();
        panelTableCards.add(drawPile);
        panelTableCards.add(discardPile);
        this.repaint();
        this.revalidate();
    }

    /**
     * showDrawnCard prints the output of the card that was picked up by the player that turn
     * should be done before actually drawing the card
     */
    public void showDrawnCard(Player player, CardPile pile) {
        System.out.println("\u001B[34m" + player.getName() + " draws a card: " + pile.showTop() + "\u001B[0m");
        //Updates the message on GUI
    }

    /**
     * showDiscardedCard prints the output of the card that was discarded by thr player that turn
     * should be done after discarding the card
     */
    public void showDiscardedCard(CardPile pile) {
        System.out.println("\u001B[36m" + "Table Card: " + pile.showTop() + "\u001B[0m");
        ImageIcon topCard = pile.showTop().getIcon();
        //Updates the Discard Pile Card Image
        discardPile.setIcon(topCard);
        discardPile.setDisabledIcon(topCard);
        discardPile.setEnabled(false);
    }

    /**
     * disablePanelHand disables the hand panels buttons, so that no user input will be processed
     */
    public void disablePanelHand() {
        for (int i = 0; i < panelHand.getComponentCount(); i++) {
            panelHand.getComponent(i).setEnabled(false);
        }
    }

    /**
     * enablePanelHand enables the hand panels buttons, so that user input will be processed
     */
    public void enablePanelHand() {
        for (int i = 0; i < panelHand.getComponentCount(); i++) {
            panelHand.getComponent(i).setEnabled(true);
        }
    }

    /**
     * disableDrawButtons disables the draw buttons, so that no user input will be processed
     */
    public void disableDrawButtons() {
        draw.setEnabled(false);
        drawPile.setEnabled(false);
    }

    /**
     * enableDrawButtons enables the draw buttons, so that user input will be processed
     */
    public void enableDrawButtons() {
        draw.setEnabled(true);
        drawPile.setEnabled(true);
    }

    /**
     * showGameWinner prints the output for the game win message
     */
    public void showGameWinner(String name, int points) {
        System.out.println("\u001B[32m" + name + " WINS THE GAME (" + points + " points)!" + "\u001B[0m");

        JOptionPane.showConfirmDialog(null, model.getTurn().getName() + " won the game!", "Game Winner!", JOptionPane.PLAIN_MESSAGE);
        createEndContentPane();
        setToEndPanel();
    }

    /**
     * showRoundWinner prints the output for the round win message
     */
    public void showRoundWinner(String name, int points) {
        System.out.println("\u001B[32m" + name + " won the round (+" + points + " points)!" + "\u001B[0m");

        JOptionPane.showConfirmDialog(null, model.getTurn().getName() + " won the round!", "Round Won!", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * showRoundNumber displays the current round of the game being played
     *
     * @param round an integer representing round #
     */
    public void showRoundNumber(int round) {
        JOptionPane.showMessageDialog(null, "Currently in round " + round, "Round", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * showAllPoints Prints the points for each player
     */
    public void showAllPoints(UNOModel model) {
        System.out.println("\u001B[32m" + model.getPlayersPointsString() + "\u001B[0m");

        JOptionPane.showConfirmDialog(null, model.getPlayersPointsString(), "Total Scores", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * showCallUNO Prints the UNO call of the player
     */
    public void showCallUNO(Player player) {
        System.out.println("\u001B[34m" + player.getName() + " called UNO!" + "\u001B[0m");
        JOptionPane.showConfirmDialog(null, model.getTurn().getName() + " has called UNO!", "UNO!", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * showInvalidCard Prints the invalid card output
     */
    public void showInvalidCard() {
        System.out.println("\u001B[31m" + "Card is not valid, pick another one." + "\u001B[0m");
    }

    /**
     * showNoDraw Prints that no cards cna be drawn anymore
     */
    public void showNoDraw() {
        System.out.println("\u001B[31m" + "No More drawable cards as all cards are in hands." + "\u001B[0m");
        JOptionPane.showMessageDialog(null, "No More drawable cards as all cards are in hands.");

    }

    /**
     * showNoDraw Prints that no saves have been made
     */
    public void showNoSaves() {
        System.out.println("\u001B[31m" + "No Saves made" + "\u001B[0m");
        JOptionPane.showMessageDialog(null, "No Saves have been made");

    }

    /**
     * showChallengeFailed Prints that the player's challenge failed, and that they must draw 4 cards.
     */
    public void showChallengeFailed() {
        System.out.println("\u001B[31m" + "Your Challenge Failed! You must draw 4 Cards!" + "\u001B[0m");
        JOptionPane.showMessageDialog(null, "Your Challenge Failed! You must draw 4 Cards!");
    }

    /**
     * showChallengeSuccess Prints that the player's challenge succeeded, and the player they challenged must draw 2 cards.
     */
    public void showChallengeSuccess() {
        System.out.println("\u001B[32m" + "Your Challenge was a Success! The previous player must draw 2 Cards!" + "\u001B[0m");
        JOptionPane.showMessageDialog(null, "Your Challenge was a Success! The previous player must draw 2 Cards!");
    }

    /**
     * disableUndo disables the edit menu item for undo
     */
    public void disableUndo(){gameMenu.disableUndoMenuItem();}

    /**
     * enableUndo enables the edit menu item for undo
     */
    public void enableUndo(){gameMenu.enableUndoMenuItem();}

    /**
     * disableRedo disables the edit menu item for redo
     */
    public void disableRedo(){gameMenu.disableRedoMenuItem();}

    /**
     * enableRedo disables the edit menu item for redo
     */
    public void enableRedo(){gameMenu.enableRedoMenuItem();}


    @Override
    public void handleUNOTurnUpdate(UNOEvent e) {
        showTurnHand(e.getPlayer());
    }

    @Override
    public void handleUNODrawUpdate(UNOEvent e) {
        showDrawnCard(e.getPlayer(), e.getCardPile());
        //Adds Drawn card to hand on GUI
        panelHand.add(new JButton(e.getCardPile().showTop().getIcon()));
    }

    @Override
    public void handleUNODiscardUpdate(UNOEvent e) {
        showDiscardedCard(e.getCardPile());
    }

    @Override
    public void handleUNORoundUpdate(UNOEvent e) {
        showRoundWinner(e.getPlayer().getName(), ((UNOModel) e.getSource()).getRoundPoints());
    }

    @Override
    public void handleUNOGameUpdate(UNOEvent e) {
        showGameWinner(e.getPlayer().getName(), e.getPlayer().getPoints());
    }

    @Override
    public void handleUNOShowAllPoints(UNOEvent e) {
        showAllPoints(((UNOModel) e.getSource()));
    }

    @Override
    public void handleUNOHelpUpdate() {
        showHelp();
    }

    @Override
    public void handleUNOCallUNO(UNOEvent e) {
        showCallUNO(e.getPlayer());
    }

    @Override
    public void handleUNOShowInvalid() {
        showInvalidCard();
    }

    @Override
    public void handleUNONoDraw() {
        showNoDraw();
    }

    @Override
    public void handleUNOShowChallengeFailed() {
        showChallengeFailed();
    }

    @Override
    public void handleUNOShowChallengeSuccess() {
        showChallengeSuccess();
    }

    /**
     * handleUNOPlayDrawn handles playing a card you just picked up off the top of the draw pile
     *
     * @param top The top card in the draw pile
     */
    @Override
    public void handleUNOPlayDrawn(Card top) {
        if (model.getTurn() instanceof AI){
            if (((AI) model.getTurn()).smartPlayDrawnCard(top)){
                model.playerPlayCard(top);
            }
            else{
                model.nextTurn();
            }
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Do you want to play drawn " + top.toString() + "?", "Yes Or No", JOptionPane.YES_NO_OPTION);
            if (selection == JOptionPane.YES_OPTION) {
                model.playerPlayCard(top);
            } else {
                model.nextTurn();
            }
        }
    }

    /**
     * Updates the status bar at the top of the GUI
     */
    @Override
    public void handleUNOStatusUpdate() {
        updateGameStatus();
    }

}