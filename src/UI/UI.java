package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import chessBoard.ChessBoard;
import chessBoard.GameStatus;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Promotion;
import chessNetwork.client.Client;
import chessNetwork.messages.Message;
import chessNetwork.messages.MessageProcessor;
import chessNetwork.messages.MessageType;
import chessPieces.ChessPiece;

public class UI implements MessageProcessor, Serializable {
	private static final long serialVersionUID = -6155870626478086508L;
	private final static String USER_HOST = "Host";
	private final static String USER_JOIN = "Join";
	private JFrame frame;
	private ChessboardUI boardUI;
	private transient Client client;
	private JMenuBar menuBar = new JMenuBar();
	private Boolean host;
	private JTextArea moveTextArea = new JTextArea();
	private JTextArea chatTextArea = new JTextArea();
	
	private JLabel thisCountdown = new JLabel("");
	private JLabel opCountdown = new JLabel("");
	
	private Timer thisTimer = null;
	
	private int thisMin;
	private int opMin;
	private int thisSec;
	private int opSec;
	
	private int thisSecLeft;
	
	private boolean initialized = false;
	
	/**
	 * Create the UI for a chess game.
	 */
	public UI() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		// Custom exit handler to alert opponent when user exits.
		WindowListener exitListener = new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {
				if (initialized)
					client.sendEnd("Your opponent has exited the game.");
				System.exit(0);
            }
		};
		frame.addWindowListener(exitListener);
		createMenu();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{217, 232, 0};
		gridBagLayout.rowHeights = new int[]{248, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		createSidePane();
		createBoardPane();
		
		this.frame.setVisible(true);
	}

	public ChessBoard getChessBoard() {
		return boardUI.getChessBoard();
	}
	
	public boolean getInitialized() {
		return initialized;
	}
	
	public Timer getThisTimer() {
		return thisTimer;
	}
	
	public int getThisMin() {
		return thisMin;
	}
	
	public int getThisSec() {
		return thisSec;
	}
	
	public int getThisSecLeft() {
		return thisSecLeft;
	}
	
	public void setThisSecLeft(int time) {
		this.thisSecLeft = time;
	}
	
	/**
	 * Add a move to the move list.
	 * @param move The string representation of a move.
	 */
	public void addToMoveList(String move) {
		String currentLog = moveTextArea.getText();
		currentLog = (currentLog.equals("")) ? currentLog + move : currentLog + '\n' + move; 
		moveTextArea.setText(currentLog);
	}
	
	/**
	 * Create the window's menu bar
	 */
	private void createMenu() {
		menuBar.setMargin(new Insets(5, 5, 5, 5));
		frame.setJMenuBar(menuBar);
		
		final UI that = this;
		JButton btnConnect = new JButton("Connect");
		// On click, show the connect screen.
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectPanel connectionPanel = new ConnectPanel();
				int result = JOptionPane.showConfirmDialog(null, connectionPanel,
		        		"Connect", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					String hostOrJoin = connectionPanel.getGameType();
					String timeLimit = connectionPanel.getTime();
					host = hostOrJoin.equals(USER_HOST);
					boardUI.clearAllPieces();
					
					try {
						client = new Client();
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Couldn't connect.",
							    "",
							    JOptionPane.WARNING_MESSAGE);
						return;
					}
				
					int timerVal = Integer.MAX_VALUE;
					if (host) {
						try {
							if (!timeLimit.equals("No Limit")) {
								try {
									timerVal = Integer.parseInt(timeLimit.split(" ")[0]);
								}
								catch (NumberFormatException e1) {
									System.out.println("This error should not happen. Congrats!");
									try {
										client.close();
									}
									catch (Exception e2) {
										System.out.println("Could not close client.");
									}
									return;
								}
							}
							
							int gameID = client.createNewGameWithTime(timerVal);
							if (gameID < 0) {
								JOptionPane.showMessageDialog(frame,
										"There are too many waiting players. Try again momentarily.",
										"",
										JOptionPane.WARNING_MESSAGE);
								try {
									client.close();
								}
								catch (Exception e1) {
									System.out.println("Could not close client.");
								}
								return;
							}
							
							JOptionPane.showMessageDialog(frame,
								    "Your game ID is " + gameID,
								    "",
								    JOptionPane.PLAIN_MESSAGE);

							client.waitForPeer();
							client.readWrite(that);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {
						try {
							int gameID;
							try {
								gameID = Integer.parseInt(connectionPanel.getID());
							}
							catch (NumberFormatException e1) {
								JOptionPane.showMessageDialog(frame,
									    "Use only an integer.",
									    "",
									    JOptionPane.WARNING_MESSAGE);
								try {
									client.close();
								}
								catch (Exception e2) {
									System.out.println("Could not close client.");
								}
								return;
							}

							timerVal = client.joinExistingGame(gameID);
							if (timerVal >= 0) {
								client.readWrite(that);
							}
							else {
								JOptionPane.showMessageDialog(frame,
									    "There are no games with that ID.",
									    "",
									    JOptionPane.WARNING_MESSAGE);
								try {
									client.close();
								}
								catch (Exception e1) {
									System.out.println("Could not close client.");
								}
								return;
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					try {
						boardUI.addAllPieces(host, client);
						boardUI.setChessBoard(new ChessBoard(host));
						boardUI.setCanMove(host);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					boardUI.getChessBoard().initializeBoard();
					changeMenuButtons();
					initialized = true;
					if (timerVal != Integer.MAX_VALUE) {
						thisTimer = new Timer(1000, new TimeListener());
						setTimers(true, timerVal, 0);
						setTimers(false, timerVal, 0);
					}
				}
			}
		});
		menuBar.add(btnConnect);
		
		JSeparator separator = new JSeparator();
		menuBar.add(separator);
		
		JButton btnResign = new JButton("Resign");
		btnResign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The game has ended", "Resignation", JOptionPane.OK_OPTION, null);
				boardUI.clearAllPieces();
				client.sendEnd("Your opponent has resigned.");
				System.exit(0);
			}
		});
		btnResign.setEnabled(false);
		menuBar.add(btnResign);
		
		JButton btnSave = new JButton("Save");
		// Future release possibility.
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Save game happens here
				String filename = new String("game_" + System.currentTimeMillis() + ".game");
				
				FileOutputStream f_out = null;
				try {
					f_out = new FileOutputStream(filename);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame,
						    "An error occurred while saving the game! (Ref Code: FileOutputStream)",
						    "Error",
						    JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
				ObjectOutputStream obj_out = null;
				try {
					obj_out = new ObjectOutputStream (f_out);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
						    "An error occurred while saving the game! (Ref Code: ObjectOutputStream)",
						    "Error",
						    JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
				try {
					obj_out.writeObject ( boardUI );
					JOptionPane.showMessageDialog(frame,
						    "Game saved successfully as: " + filename,
						    "Saved Successfully",
						    JOptionPane.PLAIN_MESSAGE);
					
							f_out.close();
							obj_out.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
						    "An error occurred while saving the game! (Ref Code: writeObject)",
						    "Error",
						    JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
			
				
			}
		});
		btnSave.setEnabled(false);
		menuBar.add(btnSave);
		
		JButton btnLoad = new JButton("Load");
		// Future release possibility.
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//File Chooser
				JFileChooser fileChooser = new JFileChooser();
				FileInputStream f_in = null;
				int returnVal = fileChooser.showOpenDialog(frame);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			        File file = fileChooser.getSelectedFile();
			        // What to do with the file
       // Read from disk using FileInputStream
					try {
						f_in = new 
							FileInputStream(file.getAbsolutePath());
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(frame,
							    "An error occurred while loading the game! (Ref Code: FileNotFound)",
							    "Error",
							    JOptionPane.PLAIN_MESSAGE);
						e1.printStackTrace();
					}
			    } else {
			        System.out.println("File access cancelled by user.");
			    }
			    

				// Read object using ObjectInputStream
				ObjectInputStream obj_in = null;
				try {
					obj_in = new ObjectInputStream (f_in);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
						    "An error occurred while loading the game! Make sure you selected the correct *.game file! (Ref Code: ObjectInputStream/IOException)",
						    "Error",
						    JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}

				// Read an object
				Object obj = null;
				try {
					obj = obj_in.readObject();
				} catch (ClassNotFoundException | IOException e1) {
					JOptionPane.showMessageDialog(frame,
						    "An error occurred while loading the game! (Ref Code: readObject)",
						    "Error",
						    JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
				
				//LOAD THIS boardUI object!
				
				boardUI = (ChessboardUI) obj;
				boardUI.repaintBoard();
				boardUI.updateUI();
				boardUI.repaint();
				System.out.println(boardUI.getChessBoard().toString());
				
				
				JOptionPane.showMessageDialog(frame,
					    "Game loaded successfully!",
					    "Loading Complete",
					    JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		// Since we don't have save or load working, keep disabled.
		btnLoad.setEnabled(false);
		menuBar.add(btnLoad);
		
		JButton btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
					    "Version: 1.0.0",
					    "",
					    JOptionPane.PLAIN_MESSAGE);
			}
		});
		menuBar.add(btnAbout);
	}
	
	/**
	 * Create the side panel, containing the move list, timers, and chat
	 */
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
	
	/**
	 * Create the move list pane.
	 * @param sideMenuPane The panel to attach the move list to.
	 */
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
		
		DefaultCaret caret = (DefaultCaret)moveTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane moveScrollPane = new JScrollPane(moveTextArea);
		GridBagConstraints gbc_moveScrollPane = new GridBagConstraints();
		gbc_moveScrollPane.gridheight = 3;
		gbc_moveScrollPane.gridwidth = 3;
		gbc_moveScrollPane.fill = GridBagConstraints.BOTH;
		gbc_moveScrollPane.gridx = 0;
		gbc_moveScrollPane.gridy = 0;
		moveBorder.add(moveScrollPane, gbc_moveScrollPane);
	}
	
	/**
	 * Create the timer pane.
	 * @param sideMenuPane The panel to attach the timer pane to.
	 */
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
		
		JLabel whiteTimeLabel = new JLabel("You:");
		whiteTimePane.add(whiteTimeLabel, BorderLayout.NORTH);
		
		whiteTimePane.add(thisCountdown, BorderLayout.CENTER);
		
		JPanel blackTimePane = new JPanel();
		timerBorder.add(blackTimePane);
		blackTimePane.setLayout(new BorderLayout(0, 0));
		
		JLabel blackTimeLabel = new JLabel("Opponent:");
		blackTimePane.add(blackTimeLabel, BorderLayout.NORTH);
		
		blackTimePane.add(opCountdown, BorderLayout.CENTER);
	}
	
	/**
	 * Create the chat pane.
	 * @param sideMenuPane The panel to attach the chat pane to.
	 */
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
		
		final JTextField chatText = new JTextField();
		GridBagConstraints gbc_chatText = new GridBagConstraints();
		gbc_chatText.insets = new Insets(0, 0, 0, 5);
		gbc_chatText.fill = GridBagConstraints.HORIZONTAL;
		gbc_chatText.gridx = 0;
		gbc_chatText.gridy = 4;
		chatBorder.add(chatText, gbc_chatText);
		chatText.setColumns(10);
		
		DefaultCaret caret = (DefaultCaret)chatTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane chatPane = new JScrollPane(chatTextArea);
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
				String messageText = chatText.getText();
				if (!messageText.equals("") && client != null) {
					chatText.setText("");
					String currentLog = chatTextArea.getText();
					currentLog = (currentLog.equals("")) ? currentLog + "You: " + messageText : currentLog + '\n' + "You: " + messageText; 
					chatTextArea.setText(currentLog);
					client.send(messageText);
				}
			}
		});
		GridBagConstraints gbc_chatSendBtn = new GridBagConstraints();
		gbc_chatSendBtn.gridx = 1;
		gbc_chatSendBtn.gridy = 4;
		chatBorder.add(chatSendBtn, gbc_chatSendBtn);
	}
	
	/**
	 * Create the panel which houses the game board.
	 */
	private void createBoardPane() throws IOException {
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
		boardUI = new ChessboardUI(boardPane, this);
		boardBorder.add(boardPane);
	}
	
	/**
	 * Toggle the menu buttons based on state of game.
	 */
	private void changeMenuButtons() {
		JButton connect = (JButton)menuBar.getComponent(0);
		JButton resign = (JButton)menuBar.getComponent(2);
		JButton save = (JButton)menuBar.getComponent(3);
		JButton load = (JButton)menuBar.getComponent(4);
		connect.setEnabled(false);
		resign.setEnabled(true);
		// Since we don't have save/load working, disable buttons
//		save.setEnabled(true);
		load.setEnabled(false);
	}
	
	/**
	 * Set the timer values to display to users.
	 * @param isMe Determine to update own time or opponent's.
	 * @param min The minutes remaining
	 * @param sec The seconds remaining
	 */
	private void setTimers(Boolean isMe, int min, int sec) {
		if (isMe) {
			thisMin = min;
			thisSec = sec;
			thisSecLeft = (60 * min) + sec;
			String secondText = Integer.toString(thisSec);
			if (thisSec < 10) {
				secondText = "0" + Integer.toString(thisSec);
			}
			String timeText = Integer.toString(thisMin) + ":" + secondText;
			thisCountdown.setText(timeText);
		} else {
			opMin = min;
			opSec = sec;
			String secondText = Integer.toString(opSec);
			if (opSec < 10) {
				secondText = "0" + Integer.toString(opSec);
			}
			String timeText = Integer.toString(opMin) + ":" + secondText;
			opCountdown.setText(timeText);
		}
		thisTimer.start();
	}
	
	/**
	 * The timer to be run every second
	 */
	private class TimeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (boardUI.getCanMove()) {
				if (thisSec == 0) {
					if (thisMin == 0) {
						thisTimer.stop();
						boardUI.setCanMove(false);
						client.sendEnd("Your opponent has run out of time. You Win!");
						JOptionPane.showMessageDialog(frame,
							    "No time remaining. You lose.",
							    "",
							    JOptionPane.WARNING_MESSAGE);
					} else {
						thisMin--;
						thisSec = 59;
					}
				}
				else {
					thisSec--;
				}
				String secondText = Integer.toString(thisSec);
				if (thisSec < 10) {
					secondText = "0" + Integer.toString(thisSec);
				}
				String timeText = Integer.toString(thisMin) + ":" + secondText;
				thisCountdown.setText(timeText);
			}
		}
	}

	/**
	 * Handle a message when it comes from the server.
	 * @param message The message from the server.
	 */
	@Override
	public void process(Message message) {
		if (message.getType() == MessageType.CHAT) {
			String messageText = (String) message.getContent();
			String currentLog = chatTextArea.getText();
			currentLog = (currentLog.equals("")) ? currentLog + "Opponent: " + messageText : currentLog + '\n' + "Opponent: " + messageText;
			chatTextArea.setText(currentLog);
		} else if (message.getType() == MessageType.MOVE) {
			Move m = (Move) message.getContent();
			System.out.println("applying move to UI...");
			boardUI.receiveMove(m);
			finishProcess(m);
		} else if (message.getType() == MessageType.PROMOTION) {
			Promotion p = (Promotion) message.getContent();
			System.out.println("applying move to UI...");
			boardUI.receivePromotion(p);
			finishProcess(p.getMove());
		} else if (message.getType() == MessageType.END) {
			String text = (String) message.getContent();
			JOptionPane.showMessageDialog(frame,
				    text,
				    "",
				    JOptionPane.WARNING_MESSAGE);
		}
		System.out.println(message.getContent());
	}
	
	/**
	 * Change timers, tell user messages when relevant.
	 * @param m The move that was made by the opponent.
	 */
	private void finishProcess(Move m) {
		int totalSecTaken = m.getTimeTaken();
		int totalOpTime = (60 * opMin) + opSec;
		int minLeft = (totalOpTime - totalSecTaken) / 60;
		int secLeft = (totalOpTime - totalSecTaken) % 60;
		
		if (totalOpTime - totalSecTaken > 0) {
			setTimers(false, minLeft, secLeft);
			boardUI.setOpTimeLeft(totalOpTime - totalSecTaken);
		}
		String text = "Your move.";
		int type = JOptionPane.PLAIN_MESSAGE;
		ChessBoard chessBoard = boardUI.getChessBoard();
		
		GameStatus gameOver = chessBoard.gameOver();
		if (gameOver == GameStatus.STALEMATE) {
			text = "Stalemate!";
			boardUI.setCanMove(false);
			client.sendEnd("Stalemate!");
			type = JOptionPane.WARNING_MESSAGE;
		} else if (gameOver == GameStatus.CHECKMATE) {
			text = "Checkmate! You lose.";
			boardUI.setCanMove(false);
			client.sendEnd("Checkmate! You Win!");
			type = JOptionPane.WARNING_MESSAGE;
		} else {
			if (chessBoard.kingInCheck()) {
				text = "You are in check.";
				type = JOptionPane.WARNING_MESSAGE;
			}
		}
		JOptionPane.showMessageDialog(frame,
			    text,
			    "",
			    type);
	}
}
