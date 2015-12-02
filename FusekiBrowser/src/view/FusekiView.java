package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FusekiView extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTable mainTable;
	private JPanel contentpane;
	
	private JPanel connectPanel;
	private JTextField connectEntry;
	private JButton connectButton;
	private JLabel countLabel;
	
	private JTextField subjectSearchEntry;
	private JTextField predicateSearchEntry;
	private JTextField objectSearchEntry;
	
	private JButton backButton;
	private JButton filterButton;
	
	private String[] columnNames = {"Subject", "Predicate", "Object"};
	private DefaultTableModel tableModel;
	private TableRowSorter<TableModel> rowSorter;
	
	// Constructor - Init fuseki browser
	public FusekiView()
	{
		super("Fuseki Browsser");	// Application Title
		
		contentpane = createGUI();
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	// Makes sure application actually terminates on closing
		this.setContentPane(contentpane);
		setSize(1000, 600);
	}
	
	private JPanel createGUI()
	{
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 960, 500);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		contentpane = new JPanel();
		contentpane.setLayout(null);
		contentpane.add(panel);
		
        // Create tableModel an add it to table
		tableModel = new DefaultTableModel(columnNames, 0);
		mainTable = new JTable(tableModel);
		
		// Create rowsorter and add it to table
		rowSorter = new TableRowSorter<>(mainTable.getModel());
		mainTable.setRowSorter(rowSorter);		
		
		panel.add(createConnectBar());
		
		countLabel = new JLabel("Not connected");
		panel.add(countLabel);
		
		JScrollPane srcpane = new JScrollPane(mainTable);
		panel.add(srcpane);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(0, 0, 960, 50);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
		
		subjectSearchEntry = new JTextField(25);
		predicateSearchEntry = new JTextField(25);
		objectSearchEntry = new JTextField(25);

		searchPanel.add(createSearchPanel(subjectSearchEntry, "Subject"));
		searchPanel.add(createSearchPanel(predicateSearchEntry, "Predicate"));
		searchPanel.add(createSearchPanel(objectSearchEntry, "Object"));
		panel.add(new JLabel("Local filter:"));
		panel.add(searchPanel);
		
		//filterButton = new JButton("Filter");
		//panel.add(filterButton);
		
		addLocalFilter(subjectSearchEntry, 0);
		addLocalFilter(predicateSearchEntry, 1);
		addLocalFilter(objectSearchEntry, 2);
		
		return contentpane;
	}
	
	private JPanel createSearchPanel(JTextField target, String name)
	{
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		JLabel myLabel = new JLabel(name);
		
		myPanel.add(myLabel);
		myPanel.add(target);
		
		return myPanel;
	}
	
	// Add connect bar with entry and connect button
	private JPanel createConnectBar()
	{
		connectPanel = new JPanel();
		connectPanel.setBounds(10, 10, 960, 50);
		connectPanel.setLayout(new FlowLayout());
		
		connectEntry = new JTextField(25);
		connectEntry.setBounds(10,10,900,50);
		connectEntry.setText("http://localhost:3030/elvis/query");
		//connectEntry.setText("http://dbpedia.org/sparql");
		
		connectButton = new JButton("Connect");		
		backButton = new JButton("Back");
		backButton.setEnabled(false);
		
		connectPanel.add(backButton);
		connectPanel.add(new JLabel("Vul de URL van RDF store in"));
		connectPanel.add(connectEntry);
		connectPanel.add(connectButton);

		return connectPanel;
	}
	
	private void addLocalFilter(JTextField entry, int column)
	{
		entry.getDocument().addDocumentListener(new DocumentListener(){
			
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	            String text = entry.getText();
	
	            if (text.trim().length() == 0) {
	                rowSorter.setRowFilter(null);
	            } else {
	            	
	            	RowFilter<TableModel, Object> rf = null;
	                try { 
	                    rf = RowFilter.regexFilter("(?i)" + text, column);    
	                }  
	                catch (java.util.regex.PatternSyntaxException ex)  
	                {  
	                    return;  
	                }
	                rowSorter.setRowFilter(rf);  
	            }
	        }
	
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            String text = entry.getText();
	
	            if (text.trim().length() == 0) {
	                rowSorter.setRowFilter(null);
	            } else {
	            	RowFilter<TableModel, Object> rf = null;
	                try { 
	                    rf = RowFilter.regexFilter("(?i)" + text, column);    
	                }  
	                catch (java.util.regex.PatternSyntaxException ex)  
	                {  
	                    return;  
	                }
	                rowSorter.setRowFilter(rf);
	            }
	        }
	
	        @Override
	        public void changedUpdate(DocumentEvent e) {
	            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	        }

	    });
	}
	
	public String getConnectEntryText()
	{
		return connectEntry.getText();
	}
	
	public String getObjectSearchEntryText()
	{
		return objectSearchEntry.getText();
	}
	
	public String getPredicateSearchEntryText()
	{
		return predicateSearchEntry.getText();
	}
	
	public String getSubjectSearchEntryText()
	{
		return subjectSearchEntry.getText();
	}
	
	public void setObjectSearchEntryText(String text)
	{
		objectSearchEntry.setText(text);
	}
	
	public void setPredicateSearchEntryText(String text)
	{
		predicateSearchEntry.setText(text);
	}
	
	public void setSubjectSearchEntryText(String text)
	{
		subjectSearchEntry.setText(text);
	}
	
	// Loads the RDF data to table
	public void setTableData(ArrayList<Object[]> data)
	{
		// Always clear current data first
		tableModel.setRowCount(0);
		
		for (Object[] row: data){
			tableModel.addRow(row);
		}
		
		countLabel.setText("Rows: " + tableModel.getRowCount());
	}
	
	// Enables or disables back button
	public void setEnabledBackButton(boolean isEnabled)
	{
		backButton.setEnabled(isEnabled);
	}
	
	public void addFilterListener(ActionListener actionListener)
	{
		filterButton.addActionListener(actionListener);	
	}
	
	// Adds event listener for back button
	public void addBackListener(ActionListener actionListener)
	{
		backButton.addActionListener(actionListener);
	}
	
	// Adds event listener for connect button
	public void addConnectionListener(ActionListener actionListener)
	{
		connectButton.addActionListener(actionListener);
	}
	
	// Adds event listener for table selections
	public void addSelectionListener(MouseListener mouseListener)
	{
		mainTable.addMouseListener(mouseListener);
	}
}