package TigerGame;
import acm.graphics.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import acm.util.RandomGenerator;
import javax.swing.*;


public class Level extends GraphicsPane implements ActionListener  {

	private MainApplication program;
	private RandomGenerator rgen;
	public Player player;
	private Timer NewObstacleTimer;
	private Timer collisionCheckTimer;
	private Timer powerUpTimer;
	
	private ArrayList<MapElement> clouds;
	private ArrayList<MapElement> bushes;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<PowerUp> powers;

	GImage backgroundImg = new GImage("sounds/blank_background.png");
	GImage gameOver = new GImage("sounds/game-over.png");
	private GRect button1 = new GRect(174,92,251,54);
	private GRect button2 = new GRect(174,159,251,54);

	public Level(MainApplication app) {

		super();
		rgen = RandomGenerator.getInstance();
		program = app;
		program.add(backgroundImg);

		// Add clouds
		clouds = new ArrayList<MapElement>();
		for(int i = 0; i <= 2; i++) {
			clouds.add(new MapElement(program, MapElementType.CLOUD, i * 200));
		}

		// Add bushes
		bushes = new ArrayList<MapElement>();
		for(int i = 0; i <= 1; i++) {
			bushes.add(new MapElement(program, MapElementType.BUSH, i * 370));
		}

		// Add obstacles
		obstacles = new ArrayList<Obstacle>();

		// Add player
		player = new Player(program);
		
		// Add powerUp
		//currentPowerUp = new PowerUp(program);
		powers = new ArrayList<PowerUp>();
		
		// Add timer
		NewObstacleTimer = new Timer(rgen.nextInt(2000,4000), this);
		NewObstacleTimer.start();
		
		collisionCheckTimer = new Timer(100, this);
		collisionCheckTimer.start();
		
		powerUpTimer = new Timer(rgen.nextInt(5000,7000), this);
		powerUpTimer.start();
		
		// Button for game over popup
		button1.setLineWidth(0);
		button2.setLineWidth(0);
		
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == NewObstacleTimer) {
			obstacles.add(new Obstacle(program));
			NewObstacleTimer.stop();
			NewObstacleTimer = new Timer(rgen.nextInt(1000,2000), this);
			NewObstacleTimer.start();
			//System.out.println("Number of obstacles: " + obstacles.size());
		}

		for(Obstacle obstacle: obstacles) {
			if(obstacle != null) {
				if(player.isCollided(obstacle)) {
					System.out.println("Player has collided with obstacle");
					stopAllTimersOnce();
					//showContents();
					program.switchToDeathScreenPane();
					
				}
				if(obstacle.getX() + obstacle.getWidth() < 0) {
					obstacles.remove(obstacle);
					break;
					
				}
			}
		}
		
		if(e.getSource() == powerUpTimer) {
			powers.add(new PowerUp(program));
			powerUpTimer.stop();
			powerUpTimer = new Timer(rgen.nextInt(8000, 12000), this);
			powerUpTimer.start();
		}

		for(PowerUp power: powers) {
			if(power != null) {
				if(player.isCollided(power)) {
					program.remove(power.getGImage());
				}
				if(power.getX() + power.getY() < 0) {
					powers.remove(power);
					break;
				}
			}
		}
		
	}

	public void stopAllTimersOnce() {
		for(Obstacle obs: obstacles) {
			obs.getObsMoveTimer().stop();
		}
		for(MapElement cloud: clouds) {
			cloud.getObsMoveTimer().stop();
		}
		for(MapElement bush: bushes) {
			bush.getObsMoveTimer().stop();
		}
		for(PowerUp power: powers) {
			power.getPowerTimer().stop();
		}
		player.getGravityTimer().stop();
		NewObstacleTimer.stop();
		collisionCheckTimer.stop();	
		powerUpTimer.stop();
	}
	
	//#TODO fix so button registers
	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		System.out.println(e.getX()+" "+ e.getY());
		
		if (obj == button1) {
			program.switchToSinglePlayer();
		}
		if (obj == button2) {
			program.switchToMainMenu();
		}
	}
	
//	public void clickedAt(GObject objIn) {
//		if (objIn == button1) {
//			program.switchToSinglePlayer();
//		}
//		if (objIn == button2) {
//			program.switchToMainMenu();
//		}
//    }
	
	public void jump(GObject playerIn) {
		player.jump();
	}

	public void gravity(GObject playerIn) {
		player.fall();
	}
	
	//adds end screen
	@Override
    public void showContents() {
		program.add(gameOver);
		program.add(button1);
		program.add(button2);
    }

    @Override
    public void hideContents() {
    	program.remove(backgroundImg);
    	program.remove(gameOver);
    }

}