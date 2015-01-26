package UI;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;


public class ConnectPanel extends JPanel {
	private JTextField txtUniqueID;

	/**
	 * Create the panel.
	 */
	public ConnectPanel() {
		this.setLayout(new GridLayout(0, 1));
		ButtonGroup group = new ButtonGroup();
		JRadioButton hostBtn = new JRadioButton("Host");
		hostBtn.setSelected(true);
		hostBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtUniqueID.setEnabled(false);
			}
		});
		this.add(hostBtn);
		group.add(hostBtn);
		JRadioButton joinBtn = new JRadioButton("Join");
		joinBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtUniqueID.setEnabled(true);
			}
		});
		this.add(joinBtn);
		group.add(joinBtn);
		
		JPanel idBorder = new JPanel();
		idBorder.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Unique ID Required", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(idBorder);
		
		txtUniqueID = new JTextField();
		idBorder.add(txtUniqueID);
		txtUniqueID.setColumns(10);
		txtUniqueID.setEnabled(false);
		
		JButton btnStart = new JButton("Start");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		this.add(btnStart);
	}

}
