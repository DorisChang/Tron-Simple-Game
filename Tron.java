//Minya Bai & Doris Chang
//Tron.java

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Tron extends JFrame implements ActionListener
{
    JPanel cards;   	//a panel that uses CardLayout
    CardLayout cLayout = new CardLayout();  
	JButton twoPlayersBtn = new JButton("2 players"); //single player btn on the main screen
	JButton onePlayerBtn = new JButton("1 player"); //two player btn on the main screen
	
	Timer myTimer;
	GamePanel game = new GamePanel();
	
    public Tron ()
    {
		super ("Tron");
		setSize (900, 800);
		twoPlayersBtn.addActionListener(this);
		onePlayerBtn.addActionListener(this);
		
		myTimer = new Timer(10,this);

		ImageIcon back = new ImageIcon("menu.png"); //image for the bkg of the main page
		JLabel backLabel = new JLabel(back);
		JLayeredPane mPage=new JLayeredPane(); 	// LayeredPane, allows control of what shows on top
		mPage.setLayout(null);
		
		backLabel.setSize(900,800);
		backLabel.setLocation(0,0);
		mPage.add(backLabel,1); //adds bkg image to the main page, as the first layer					
		
		//buttons on the main screen
		twoPlayersBtn.setSize(120,30);
		twoPlayersBtn.setLocation(550,700);
		ImageIcon btnTwo = new ImageIcon("btnTwo.png");
		twoPlayersBtn.setIcon(btnTwo);
		mPage.add(twoPlayersBtn,3);
		
		onePlayerBtn.setSize(120,30);
		onePlayerBtn.setLocation(200,700);
		ImageIcon btnOne = new ImageIcon("btnOne.png");
		onePlayerBtn.setIcon(btnOne);
		mPage.add(onePlayerBtn,2);
		
		
		
		ImageIcon oneInstruct = new ImageIcon("onePlayerInstructions.png");
		JLabel oneInstructLabel = new JLabel(oneInstruct);
		
		JLayeredPane oneInstructPage=new JLayeredPane(); //instructions page for 1 player
		oneInstructPage.setLayout(null);
		oneInstructLabel.setSize(900,800);
		oneInstructLabel.setLocation(0,0);
		oneInstructPage.add(oneInstructLabel,1);	
		
		
		ImageIcon twoInstruct = new ImageIcon("twoPlayerInstructions.png");
		JLabel twoInstructLabel = new JLabel(twoInstruct);
		JLayeredPane twoInstructPage=new JLayeredPane(); //instructions page for 1 player
		twoInstructPage.setLayout(null);
		twoInstructLabel.setSize(900,800);
		twoInstructLabel.setLocation(0,0);
		twoInstructPage.add(twoInstructLabel,1);
		
		
		/*JLabel inst1 = new JLabel("When using a null layout manager");
		inst1.setSize(200,30);
		inst1.setLocation(350,100);
		mPage.add(inst1);
		JLabel inst2 = new JLabel("you need to setSize and setLocation");
		inst2.setSize(280,30);
		inst2.setLocation(350,120);
		mPage.add(inst2);
		JLabel inst3 = new JLabel("of everything you add.");
		inst3.setSize(200,30);
		inst3.setLocation(350,140);
		mPage.add(inst3);*/
		
		
		/* THIS IS WHERE THE MAGIC HAPPENS
			This panel will allow us to store the other panels, and show the one we want.
			The panels are being stored (are shown) based on a simple string
		*/
		cards = new JPanel(cLayout);
		cards.add(mPage, "menu");
		cards.add(oneInstructPage, "onePlayerInstructions");
		cards.add(twoInstructPage, "twoPlayerInstructions");
		cards.add(game, "game");

		add(cards);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
		Object source =evt.getSource();
		if(source==onePlayerBtn){
			//cLayout.show(cards,"onePlayerInstructions");
		    cLayout.show(cards,"game"); //uncomment this to show the game part
		    myTimer.start();
		}
		else if(source==twoPlayersBtn){
			//cLayout.show(cards,"twoPlayerInstructions");
		    cLayout.show(cards,"game");
		    myTimer.start();
		}
		else if(source==myTimer){
			game.move();
			game.repaint();
		}
    }

    public static void main(String[] args){
		Tron tron = new Tron();
    }
}

class GamePanel extends JPanel implements MouseListener{
	private int destx,desty,boxx,boxy;

	public GamePanel(){
		addMouseListener(this);
		boxx=200;
		boxy=200;
		destx=500;
		desty=200;
		setSize(800,600);
	}


    public void move() {
		if(boxx<destx){
			boxx+=5;
		}
		if(boxx>destx){
			boxx-=5;
		}
		if(boxy<desty){
			boxy+=5;
		}
		if(boxy>desty){
			boxy-=5;
		}
    }

    public void paintComponent(Graphics g){
		 g.setColor(new Color(222,222,255));
         g.fillRect(0,0,getWidth(),getHeight());
         g.setColor(new Color(255,111,111));
         g.fillOval(destx,desty,10,10);
         g.setColor(Color.green);
         g.fillRect(boxx,boxy,20,20);


    }

    // ------------ MouseListener ------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e){}

    public void mousePressed(MouseEvent e){
		destx = e.getX();
		desty = e.getY();
	}

}



/*import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Tron extends JFrame implements ActionListener//, KeyListener
{
    JPanel cards;   	//a panel that uses CardLayout
    CardLayout cLayout = new CardLayout();  // I could make this dynamically, then use getLayout
					    //to get the reference to it, but this is easier.
	
	JButton onePlayerBtn = new JButton("One Player");
	JButton twoPlayersBtn = new JButton("Two Players");
	Timer myTimer;
	GamePanel gamePage = new GamePanel();

    public Tron ()
    {
		super ("TRON");
		setSize (900, 800);
		
		ImageIcon menuBkg = new ImageIcon("images/menu.png");
		JLabel menuLabel = new JLabel(menuBkg);
		JLayeredPane mPage=new JLayeredPane(); 	// LayeredPane allows my to control what shows on top
		mPage.setLayout(null);
		
		backLabel.setSize(800,600);
		backLabel.setLocation(0,0);
		mPage.add(backLabel,1);
		
		onePlayerBtn.addActionListener(this);
		twoPlayersBtn.addActionListener(this);
		myTimer = new Timer(10,this);

		JPanel mPage = new JPanel();	// Main Page - it just a panel
		mPage.setLayout(null);
		//mPage.setImage(menu,0,0,this);
		
		JPanel iPage = new JPanel();
		iPage.setLayout(null);
		
		onePlayerBtn.setSize(120,30);
		onePlayerBtn.setLocation(200,700);
		twoPlayersBtn.setSize(120,30);
		twoPlayersBtn.setLocation(550,700);
		add(onePlayerBtn);
		add(twoPlayersBtn);
		
		/*JLabel inst1 = new JLabel("When using a null layout manager");
		inst1.setSize(200,30);
		inst1.setLocation(350,100);
		add(inst1);
		JLabel inst2 = new JLabel("you need to setSize and setLocation");
		inst2.setSize(280,30);
		inst2.setLocation(350,120);
		add(inst2);
		JLabel inst3 = new JLabel("of everything you add.");
		inst3.setSize(200,30);
		inst3.setLocation(350,140);
		add(inst3);*/
		
		
		/* THIS IS WHERE THE MAGIC HAPPENS
			This panel will allow us to store the other panels, and show the one we want.
			The panels are being stored (are shown) based on a simple string
		
		cards = new JPanel(cLayout);
		cards.add(mPage, "menu");
		cards.add(iPage, "instructions");
		cards.add(gamePage, "game");


		add(cards);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible (true);
    }

    public void actionPerformed(ActionEvent evt) {
		Object source =evt.getSource();
		if(source==onePlayerBtn){
			onePlayerBtn.setVisible(false);
			twoPlayersBtn.setVisible(false);
			cLayout.show(cards,"game");
		    //cLayout.show(cards,"instructions");
		    myTimer.start();
		}
		else if(source == twoPlayersBtn){
			onePlayerBtn.setVisible(false);
			twoPlayersBtn.setVisible(false);
			cLayout.show(cards,"game");
			//cLayout.show(cards,"instructions");
		    myTimer.start();
			}
		else if(source==myTimer){
			gamePage.move();
			gamePage.repaint();
		}

    }
    
    /*public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
    	gamePage.setKey(e.getKeyCode(),true);
    	if (keys[KeyEvent.VK_RIGHT]){
    		myTimer.start();
    		}
    }

    public void keyReleased(KeyEvent e) {
    	gamePage.setKey(e.getKeyCode(),false);
    }

    public static void main(String[] args){
		Tron menuEg = new Tron();
    }
}

class GamePanel extends JPanel implements MouseListener{
	private int destx,desty,boxx,boxy;
	private boolean []keys;
	
	public GamePanel(){
		addMouseListener(this);
		boxx=200;
		boxy=200;
		destx=500;
		desty=200;
		setSize(800,600);
	}


    public void move() {
		if(boxx<destx){
			boxx+=5;
		}
		if(boxx>destx){
			boxx-=5;
		}
		if(boxy<desty){
			boxy+=5;
		}
		if(boxy>desty){
			boxy-=5;
		}
    }

    public void paintComponent(Graphics g){
		 g.setColor(new Color(222,222,255));
         g.fillRect(0,0,getWidth(),getHeight());
         g.setColor(new Color(255,111,111));
         g.fillOval(destx,desty,10,10);
         g.setColor(Color.green);
         g.fillRect(boxx,boxy,20,20);
		 //g.drawImage(menu,0,0,this);
    }

    // ------------ MouseListener ------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e){}

    public void mousePressed(MouseEvent e){
		destx = e.getX();
		desty = e.getY();
	}

}*/



