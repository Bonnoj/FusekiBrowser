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
		
		
		this.theView.addBackListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("test");
				ArrayList<Object[]> data = theModel.goBackInStack();
				theView.setTableData(data);
				
				if (theModel.isStackEmpty())
				{
					theView.setEnabledBackButton(false);
				}
			}
		});

	}
	
	class ConnectionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			theView.setEnabledBackButton(false);
			
			boolean connected = theModel.connect(theView.getConnectEntryText());
			
			if (connected)
			{
				ArrayList<Object[]> data = theModel.execQuery(null);
				theView.setTableData(data);
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
    			clearTextFilters();
    			JTable target = (JTable)e.getSource();
    			
    			@SuppressWarnings("rawtypes")
				RowSorter rowSorter = target.getRowSorter();
    			TableModel tableModel= target.getModel();
    			
    			// Retrieve selected location
    			int row = target.getSelectedRow();
    			int column = target.getSelectedColumn();
			 
    			// Retrieve selected object	
    			row = rowSorter.convertRowIndexToModel(row); // conversion is needed when filters have been applied to table
    			Object selectedObject = tableModel.getValueAt(row, column);
    			;
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
				theView.setTableData(data);
				theView.setEnabledBackButton(true);
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
	
	private void clearTextFilters()
	{
		theView.setObjectSearchEntryText("");
		theView.setPredicateSearchEntryText("");
		theView.setSubjectSearchEntryText("");
	}
}

//SELECT ?subject ?predicate ?object
//		WHERE {
//		  ?subject ?predicate ?object . FILTER regex(str(?subject), "Mosc") .
//		}
//		LIMIT 1000

/*
this.theView.addFilterListener(new ActionListener()
{
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("test2");
		
		//String subtext = theView.getSubjectSearchEntryText();
		
		String query = "";
		int column = 0;
		switch(column)
		{
			case 0: query = "SELECT * WHERE {?x ?r ?y .FILTER regex(str(?x), \"" + "Potter" + "\")}";
			break;
			case 1: query = "SELECT * WHERE {?x ?r ?y .FILTER (?r = <" + ">)}";
			break;
			case 2: query = "SELECT * WHERE {?x ?r ?y .FILTER (?y = <" + ">)}";
			break;
		}
		
		System.out.println(query);
		
		ArrayList<Object[]> data = theModel.execQuery(query);
		theView.setTableData(data);
		theView.setEnabledBackButton(true);
		
		theView.setTableData(data);
		
		if (theModel.isStackEmpty())
		{
			theView.setEnabledBackButton(false);
		}
	}
});*/