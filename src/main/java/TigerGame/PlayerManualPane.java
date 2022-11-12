package TigerGame;


import acm.graphics.*;
 
public class PlayerManualPane extends GraphicsPane {

    private MainApplication program;
    GImage manualPage = new GImage("sounds/manual_menu.png");
    public GRect backButton = new GRect(25,22,86,34);

    public PlayerManualPane(MainApplication app) {
        super();
        program = app;
        backButton.setLineWidth(0);
    }

    public void clickedAt(GObject objIn) {
        if (objIn == backButton) {
        	program.switchToMainMenu();
        }
    }
    
    
    @Override
    public void showContents() {
        program.add(manualPage);
        program.add(backButton);

    }

    @Override
    public void hideContents() {
        program.remove(manualPage);
        program.remove(backButton);
    }

			
	}