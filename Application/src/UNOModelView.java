package src;

/**
 * UNOModelView interfaces between the Model and the view when changes are made to the model
 * When an action is preformed in model, it tells the views to update.
 *
 * @author Owen Renette 101223576
 * @version October 21, 2023
 */
public interface UNOModelView {
    void handleUNOTurnUpdate(UNOEvent e);
    void handleUNODrawUpdate(UNOEvent e);
    void handleUNODiscardUpdate(UNOEvent e);
    void handleUNORoundUpdate(UNOEvent e);
    void handleUNOGameUpdate(UNOEvent e);
    void handleUNOShowAllPoints(UNOEvent e);
    void handleUNOHelpUpdate();
    void handleUNOCallUNO(UNOEvent e);
    void handleUNOShowInvalid();
    void handleUNONoDraw();
    void handleUNOShowChallengeFailed();
    void handleUNOShowChallengeSuccess();
    void handleUNOPlayDrawn(Card top);
    void handleUNOStatusUpdate();
}
