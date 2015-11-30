package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;

import model.FusekiModel;
import view.FusekiView;

public class FusekiController {
	
	private FusekiView theView;
	private FusekiModel theModel;
	
	public FusekiController(FusekiView theView, FusekiModel theModel)
	{
		this.theView = theView;
		this.theModel = theModel;
		
		this.theView.addSelectionListener(new SelectListener());
		this.theView.addConnectionListener(new ConnectionListener());
	}
	
	class ConnectionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean connected = theModel.connect("");
			
			if (connected)
			{
				ArrayList<Object[]> data = theModel.execQuery(null);
				theView.SetTableData(data);
			}
			else {
				// TODO not connected error
			}
		}
	}
	
	class SelectListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) 
		{
    		if (e.getClickCount() == 1) 
    		{
    			System.out.println("REACHED!");
    			JTable target = (JTable)e.getSource();
    			RowSorter rowSorter = target.getRowSorter();
    			TableModel tableModel= target.getModel();
    			
    			// Retrieve selected location
    			int row = target.getSelectedRow();
    			int column = target.getSelectedColumn();
			 
    			// Retrieve selected object	
    			row = rowSorter.convertRowIndexToModel(row); // conversion is needed when filters have been applied to table
    			Object selectedObject = tableModel.getValueAt(row, column);
    			
    			String query = "";
    			switch(column)
    			{
        			case 0: query = "SELECT * WHERE {?x ?r ?y .FILTER (?x = <" + selectedObject.toString() + ">)}";
        			break;
        			case 1: query = "SELECT * WHERE {?x ?r ?y .FILTER (?r = <" + selectedObject.toString() + ">)}";
        			break;
        			case 2: query = "SELECT * WHERE {?x ?r ?y .FILTER (?y = <" + selectedObject.toString() + ">)}";
        			break;
    			}
    			
    			ArrayList<Object[]> data = theModel.execQuery(query);
				theView.SetTableData(data);
    	    }
    	}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
}
