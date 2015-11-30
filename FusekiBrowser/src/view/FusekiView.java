package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class FusekiView extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTable mainTable;
	private JPanel contentpane;
	
	private JPanel connectPanel;
	private JTextField connectEntry;
	private JButton connectButton;
	private JLabel countLabel;
	
	private JButton backButton;
	
	private String[] columnNames = {"Subject", "Predicate", "Object"};
	private DefaultTableModel tableModel;
	private TableRowSorter<TableModel> rowSorter;
	
	// Constructor - Init fuseki browser
	public FusekiView()
	{
		super("Fuseki Browsser");	// Application Title
		
		contentpane = createGUI();
		
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
		panel.add(new JLabel("Specify a word to match:"), BorderLayout.WEST);
		
		JScrollPane srcpane = new JScrollPane(mainTable);
		panel.add(srcpane);
		
		countLabel = new JLabel("Not connected");
		panel.add(countLabel);
		
		return contentpane;
	}
	
	// Add connect bar with entry and connect button
	private JPanel createConnectBar()
	{
		connectPanel = new JPanel();
		connectPanel.setBounds(10, 10, 960, 50);
		connectPanel.setLayout(new FlowLayout());
		
		connectEntry = new JTextField(25);
		connectEntry.setBounds(10,10,900,50);
		connectEntry.setText("http://dbpedia.org/sparql");
		
		connectButton = new JButton("Connect");		
		backButton = new JButton("Back");
		backButton.setEnabled(false);
		
		connectPanel.add(backButton);
		connectPanel.add(new JLabel("Vul de naam van RDF store in"));
		connectPanel.add(connectEntry);
		connectPanel.add(connectButton);
		
		return connectPanel;
	}
	
	public String getConnectEntryText()
	{
		return connectEntry.getText();
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