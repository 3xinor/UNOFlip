package src;

import javax.swing.*;

/**
 * UNOMenu is used to represent the menu bar of a UNO game
 *
 * @author Dave Exinor 101184298
 * @version November 11, 2023
 */
public class UnoMenu extends JMenuBar {

    private JMenu gameMenu;
    private JMenu statusMenu;
    private JMenuItem scoreMenuItem;
    private JMenuItem roundMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem restartMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenu editMenu;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;


    /**
     * The default construct for the UNO Menu
     */
    public UnoMenu() {

        // instantiate menus and menu items
        gameMenu = new JMenu("Game");
        statusMenu = new JMenu("Status");
        scoreMenuItem = new JMenuItem("Scores");
        roundMenuItem = new JMenuItem("Round");
        restartMenuItem = new JMenuItem("Restart");
        saveMenuItem = new JMenuItem("Save");
        loadMenuItem = new JMenuItem("Load");
        exitMenuItem = new JMenuItem("Exit");
        editMenu = new JMenu("Edit");
        undoMenuItem = new JMenuItem("undo");
        redoMenuItem = new JMenuItem("redo");
        undoMenuItem.setEnabled(false);
        redoMenuItem.setEnabled(false);

        // Add menu items to correlating menus
        gameMenu.add(restartMenuItem);
        gameMenu.add(saveMenuItem);
        gameMenu.add(loadMenuItem);
        gameMenu.add(exitMenuItem);

        statusMenu.add(scoreMenuItem);
        statusMenu.add(roundMenuItem);

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        // Add menus to menubar
        this.add(gameMenu);
        this.add(statusMenu);
        this.add(editMenu);

        roundMenuItem.setActionCommand("Round");
        scoreMenuItem.setActionCommand("Scores");
        restartMenuItem.setActionCommand("Restart");
        saveMenuItem.setActionCommand("Save");
        loadMenuItem.setActionCommand("Load");
        exitMenuItem.setActionCommand("Exit");
        undoMenuItem.setActionCommand("Undo");
        redoMenuItem.setActionCommand("Redo");
    }

    /**
     * Get the scoreMenuItem from a UNO menu object
     * @return scoreMenu object
     */
    public JMenuItem getScoreMenuItem() {return scoreMenuItem;}

    /**
     * Get the exitMenuItem from a UNO menu object
     * @return  exitMenuItem object
     */
    public JMenuItem getExitMenuItem() {return exitMenuItem;}

    /**
     * Get the roundMenuITem from a UNO menu object
     * @return roundMenuItem object
     */
    public JMenuItem getRoundMenuItem() {return roundMenuItem;}

    /**
     * Get the statusMenu from a UNO menu object
     * @return statusMenuObject
     */
    public JMenu getStatusMenu() {return statusMenu;}

    /**
     * Get the restartMenuItem from a UNO menu object
     * @return restartMenuItem
     */
    public JMenuItem getRestartMenuItem() {return restartMenuItem;}

    /**
     * Get the saveMenuItem from a UNO menu object
     * @return saveMenuItem
     */
    public JMenuItem getSaveMenuItem() {return saveMenuItem;}

    /**
     * Get the loadMenuItem from a UNO menu object
     * @return loadMenuItem
     */
    public JMenuItem getLoadMenuItem() {return loadMenuItem;}

    /**
     * Get the undoMenuItem from a UNO menu object
     * @return undoMenuItem
     */
    public JMenuItem getUndoMenuItem() {return undoMenuItem;}

    /**
     * Get the redoMenuItem from a UNO menu object
     * @return redoMenuItem
     */
    public JMenuItem getRedoMenuItem() {return redoMenuItem;}

    /**
     * disableUndoItem disables the edit menu item for undo
     */
    public void disableUndoMenuItem(){undoMenuItem.setEnabled(false);}

    /**
     * enableUndoItem enables the edit menu item for undo
     */
    public void enableUndoMenuItem(){undoMenuItem.setEnabled(true);}

    /**
     * disableRedoItem disables the edit menu item for redo
     */
    public void disableRedoMenuItem(){redoMenuItem.setEnabled(false);}

    /**
     * enableRedoItem disables the edit menu item for redo
     */
    public void enableRedoMenuItem(){redoMenuItem.setEnabled(true);}
}
