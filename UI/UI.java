package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import chessBoard.ChessBoard;


public class UI {
	private JFrame frame;
	private ChessboardUI board;
	
	public UI(ChessBoard chessBoard) throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{217, 232, 0};
		gridBagLayout.rowHeights = new int[]{248, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		createSidePane();
		createBoardPane(chessBoard);
		
		this.frame.setVisible(true);
	}
	
	public ChessBoard getChessBoard() {
		return board.getChessBoard();
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(5, 5, 5, 5));
		frame.setJMenuBar(menuBar);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFrame connectFrame = new JFrame ("Connect");
				connectFrame.getContentPane().add (new ConnectPanel());
				connectFrame.pack();
				connectFrame.setVisible (true);
			}
		});
		menuBar.add(btnConnect);
		
		JSeparator separator = new JSeparator();
		menuBar.add(separator);
		
		JButton btnResign = new JButton("Resign");
		btnResign.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		menuBar.add(btnResign);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		menuBar.add(btnSave);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		menuBar.add(btnLoad);
	}
	
	private void createSidePane() {
		JPanel sideMenuBorder = new JPanel();
		sideMenuBorder.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagConstraints gbc_sideMenuBorder = new GridBagConstraints();
		gbc_sideMenuBorder.gridwidth = 1;
		gbc_sideMenuBorder.fill = GridBagConstraints.BOTH;
		gbc_sideMenuBorder.insets = new Insets(0, 0, 0, 5);
		gbc_sideMenuBorder.gridx = 0;
		gbc_sideMenuBorder.gridy = 0;
		frame.getContentPane().add(sideMenuBorder, gbc_sideMenuBorder);
		GridBagLayout gbl_sideMenuBorder = new GridBagLayout();
		gbl_sideMenuBorder.columnWidths = new int[]{217, 0};
		gbl_sideMenuBorder.rowHeights = new int[]{248, 0};
		gbl_sideMenuBorder.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_sideMenuBorder.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		sideMenuBorder.setLayout(gbl_sideMenuBorder);
		
		JPanel sideMenuPane = new JPanel();
		GridBagConstraints gbc_sideMenuPane = new GridBagConstraints();
		gbc_sideMenuPane.fill = GridBagConstraints.BOTH;
		gbc_sideMenuPane.gridx = 0;
		gbc_sideMenuPane.gridy = 0;
		sideMenuBorder.add(sideMenuPane, gbc_sideMenuPane);
		GridBagLayout gbl_sideMenuPane = new GridBagLayout();
		gbl_sideMenuPane.columnWidths = new int[]{0, 0};
		gbl_sideMenuPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_sideMenuPane.columnWeights = new double[]{1.0, 1.0};
		gbl_sideMenuPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		sideMenuPane.setLayout(gbl_sideMenuPane);
		
		createMovePane(sideMenuPane);
		createTimerPane(sideMenuPane);
		createChatPane(sideMenuPane);
	}
	
	private void createMovePane(JPanel sideMenuPane) {
		JPanel moveBorder = new JPanel();
		moveBorder.setBorder(new TitledBorder(null, "Move List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_moveBorder = new GridBagConstraints();
		gbc_moveBorder.gridheight = 3;
		gbc_moveBorder.insets = new Insets(0, 0, 5, 5);
		gbc_moveBorder.gridwidth = 1;
		gbc_moveBorder.fill = GridBagConstraints.BOTH;
		gbc_moveBorder.gridx = 0;
		gbc_moveBorder.gridy = 0;
		sideMenuPane.add(moveBorder, gbc_moveBorder);
		GridBagLayout gbl_moveBorder = new GridBagLayout();
		gbl_moveBorder.columnWidths = new int[]{0, 0, 0, 0};
		gbl_moveBorder.rowHeights = new int[]{0, 0, 0, 0};
		gbl_moveBorder.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_moveBorder.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		moveBorder.setLayout(gbl_moveBorder);
		
		JScrollPane moveScrollPane = new JScrollPane();
		GridBagConstraints gbc_moveScrollPane = new GridBagConstraints();
		gbc_moveScrollPane.gridheight = 3;
		gbc_moveScrollPane.gridwidth = 3;
		gbc_moveScrollPane.fill = GridBagConstraints.BOTH;
		gbc_moveScrollPane.gridx = 0;
		gbc_moveScrollPane.gridy = 0;
		moveBorder.add(moveScrollPane, gbc_moveScrollPane);
	}
	
	private void createTimerPane(JPanel sideMenuPane) {
		JPanel timerBorder = new JPanel();
		timerBorder.setBorder(new TitledBorder(null, "Timer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_timerBorder = new GridBagConstraints();
		gbc_timerBorder.gridheight = 3;
		gbc_timerBorder.insets = new Insets(0, 0, 5, 0);
		gbc_timerBorder.fill = GridBagConstraints.BOTH;
		gbc_timerBorder.gridx = 1;
		gbc_timerBorder.gridy = 0;
		sideMenuPane.add(timerBorder, gbc_timerBorder);
		timerBorder.setLayout(new BoxLayout(timerBorder, BoxLayout.Y_AXIS));
		
		JPanel whiteTimePane = new JPanel();
		timerBorder.add(whiteTimePane);
		whiteTimePane.setLayout(new BorderLayout(0, 0));
		
		JLabel whiteTimeLabel = new JLabel("White:");
		whiteTimePane.add(whiteTimeLabel, BorderLayout.NORTH);
		
		JLabel whiteTimeRemaining = new JLabel("");
		whiteTimePane.add(whiteTimeRemaining, BorderLayout.CENTER);
		
		JPanel blackTimePane = new JPanel();
		timerBorder.add(blackTimePane);
		blackTimePane.setLayout(new BorderLayout(0, 0));
		
		JLabel blackTimeLabel = new JLabel("Black:");
		blackTimePane.add(blackTimeLabel, BorderLayout.NORTH);
		
		JLabel blackTimeRemaining = new JLabel("");
		blackTimePane.add(blackTimeRemaining, BorderLayout.CENTER);
	}
	
	private void createChatPane(JPanel sideMenuPane) {
		JPanel chatBorder = new JPanel();
		chatBorder.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_chatBorder = new GridBagConstraints();
		gbc_chatBorder.gridheight = 2;
		gbc_chatBorder.gridwidth = 2;
		gbc_chatBorder.fill = GridBagConstraints.BOTH;
		gbc_chatBorder.gridx = 0;
		gbc_chatBorder.gridy = 3;
		sideMenuPane.add(chatBorder, gbc_chatBorder);
		GridBagLayout gbl_chatBorder = new GridBagLayout();
		gbl_chatBorder.columnWidths = new int[]{0, 0, 0};
		gbl_chatBorder.rowHeights = new int[]{0, 0, 0};
		gbl_chatBorder.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_chatBorder.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		chatBorder.setLayout(gbl_chatBorder);
		
		JTextField chatText = new JTextField();
		GridBagConstraints gbc_chatText = new GridBagConstraints();
		gbc_chatText.insets = new Insets(0, 0, 0, 5);
		gbc_chatText.fill = GridBagConstraints.HORIZONTAL;
		gbc_chatText.gridx = 0;
		gbc_chatText.gridy = 4;
		chatBorder.add(chatText, gbc_chatText);
		chatText.setColumns(10);
		
		JScrollPane chatPane = new JScrollPane();
		GridBagConstraints gbc_chatPane = new GridBagConstraints();
		gbc_chatPane.gridwidth = 2;
		gbc_chatPane.insets = new Insets(0, 0, 5, 5);
		gbc_chatPane.gridheight = 4;
		gbc_chatPane.fill = GridBagConstraints.BOTH;
		gbc_chatPane.gridx = 0;
		gbc_chatPane.gridy = 0;
		chatBorder.add(chatPane, gbc_chatPane);
		
		JButton chatSendBtn = new JButton("Send");
		chatSendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		GridBagConstraints gbc_chatSendBtn = new GridBagConstraints();
		gbc_chatSendBtn.gridx = 1;
		gbc_chatSendBtn.gridy = 4;
		chatBorder.add(chatSendBtn, gbc_chatSendBtn);
	}
	
	private void createBoardPane(ChessBoard chessBoard) throws IOException {
		JPanel boardBorder = new JPanel();
		boardBorder.setBorder(new EmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc_boardBorder = new GridBagConstraints();
		gbc_boardBorder.gridwidth = 2;
		gbc_boardBorder.fill = GridBagConstraints.BOTH;
		gbc_boardBorder.gridx = 1;
		gbc_boardBorder.gridy = 0;
		frame.getContentPane().add(boardBorder, gbc_boardBorder);
		GridBagLayout gbl_boardBorder = new GridBagLayout();
		gbl_boardBorder.columnWidths = new int[]{0, 0};
		gbl_boardBorder.rowHeights = new int[]{0, 0};
		gbl_boardBorder.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_boardBorder.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		boardBorder.setLayout(gbl_boardBorder);
		boardBorder.setBackground(new Color(219, 99, 12));
		
		JPanel boardPane = new JPanel(new GridLayout(8, 8, 0, 0));
		board = new ChessboardUI(boardPane, chessBoard);
		boardBorder.add(boardPane);
	}
}
