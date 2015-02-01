package UI;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;


public class ConnectPanel extends JPanel {
	private static JTextField txtUniqueID;
	private ButtonGroup gameGroup = new ButtonGroup();
	private ButtonGroup timeGroup = new ButtonGroup();
	
	/**
	 * Create the panel.
	 */
	public ConnectPanel() {
		this.setLayout(new GridLayout(0, 1));
		JRadioButton hostBtn = new JRadioButton("Host");
		hostBtn.setSelected(true);
		hostBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtUniqueID.setEnabled(false);
			}
		});
		this.add(hostBtn);
		gameGroup.add(hostBtn);
		JRadioButton joinBtn = new JRadioButton("Join");
		joinBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtUniqueID.setEnabled(true);
			}
		});
		this.add(joinBtn);
		gameGroup.add(joinBtn);
		
		JPanel idBorder = new JPanel();
		idBorder.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Unique ID Required", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(idBorder);
		
		txtUniqueID = new JTextField();
		idBorder.add(txtUniqueID);
		txtUniqueID.setColumns(10);
		txtUniqueID.setEnabled(false);
		
		
		JPanel timePanel = new JPanel();
		timePanel.setBorder(new TitledBorder(null, "Time Limit", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(timePanel);
		timePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JRadioButton noTimeBtn = new JRadioButton("No Limit");
		noTimeBtn.setSelected(true);
		timePanel.add(noTimeBtn);
		timeGroup.add(noTimeBtn);
		JRadioButton thirtyBtn = new JRadioButton("30 Minutes");
		timePanel.add(thirtyBtn);
		timeGroup.add(thirtyBtn);
		JRadioButton fourtyFiveBtn = new JRadioButton("45 Minutes");
		timePanel.add(fourtyFiveBtn);
		timeGroup.add(fourtyFiveBtn);
		JRadioButton sixtyBtn = new JRadioButton("60 Minutes");
		timePanel.add(sixtyBtn);
		timeGroup.add(sixtyBtn);
		
	}
	
	public String getID() {
		return this.txtUniqueID.getText();
	}
	
	public String getGameType() {
		Enumeration<AbstractButton> components = this.gameGroup.getElements();
		while (components.hasMoreElements()) {
			AbstractButton temp = components.nextElement();
			if (temp.isSelected()) {
				return temp.getText();
			}
		}
		return "";
	}
	
	public String getTime() {
		Enumeration<AbstractButton> components = this.timeGroup.getElements();
		while (components.hasMoreElements()) {
			AbstractButton temp = components.nextElement();
			if (temp.isSelected()) {
				return temp.getText();
			}
		}
		return "";
	}
}
