package java.awt;

import javax.accessibility.Accessible;

import org.pulsebot.Client;

public class Canvas extends Component implements Accessible {

	private static final long serialVersionUID = -8767070661250344807L;
 	
	@Override
	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		return Client.drawGraphics(g);
	}

	@Override
	public void setLocation(int x, int y) {}
	
}