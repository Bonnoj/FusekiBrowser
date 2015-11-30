import view.FusekiView;
import model.FusekiModel;
import controller.FusekiController;

public class FusekiBrowser {
	
	public static void main(String[] args) {
		
		FusekiView theView = new FusekiView();
		FusekiModel theModel = new FusekiModel();
		FusekiController theController = new FusekiController(theView, theModel);
		
		theView.setVisible(true);
	}
}
