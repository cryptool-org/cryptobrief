package sunset.gui;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import ffapl.FFaplInterpreter;
import ffapl.java.logging.FFaplConsoleHandler;
import ffapl.java.logging.FFaplLogger;

import sunset.gui.api.APITreeCellRenderer;
import sunset.gui.api.MutableTreeNodeHead;
import sunset.gui.api.spec.ApiSpecification;
import sunset.gui.api.spec.SampleCode;
import sunset.gui.api.spec.SnippetCode;
import sunset.gui.api.util.ApiUtil;
import sunset.gui.dnd.APITreeDragSource;
import sunset.gui.dnd.DropTargetListenerFile;
import sunset.gui.editor.FFaplRegex;
import sunset.gui.interfaces.IFFaplLang;
import sunset.gui.interfaces.IProperties;
import sunset.gui.lib.ExecuteThread;
import sunset.gui.listener.ActionListenerAbout;
import sunset.gui.listener.ActionListenerCloseAllTab;
import sunset.gui.listener.ActionListenerCloseTab;
import sunset.gui.listener.ActionListenerEditApiEntry;
import sunset.gui.listener.ActionListenerExecuteCode;
import sunset.gui.listener.ActionListenerExit;
import sunset.gui.listener.ActionListenerInputField;
import sunset.gui.listener.ActionListenerNewFile;
import sunset.gui.listener.ActionListenerOpenFile;
import sunset.gui.listener.ActionListenerPreferences;
import sunset.gui.listener.ActionListenerPrintCurrentFile;
import sunset.gui.listener.ActionListenerRedo;
import sunset.gui.listener.ActionListenerSave;
import sunset.gui.listener.ActionListenerSaveAll;
import sunset.gui.listener.ActionListenerSaveAs;
import sunset.gui.listener.ActionListenerShowAPI;
import sunset.gui.listener.ActionListenerUndo;
import sunset.gui.listener.ActionListenerZoom;
import sunset.gui.listener.ChangeListenerSelectedTab;
import sunset.gui.listener.MouseListenerChooseLineNumber;
import sunset.gui.listener.MouseListenerShowAPIDialog;
import sunset.gui.listener.PropertyChangeListenerAPI;
import sunset.gui.listener.WindowListenerFFaplGUI;
import sunset.gui.logic.ApiLogic;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;
import sunset.gui.tabbedpane.JTabbedPaneCode;
import sunset.gui.util.SunsetBundle;
import sunset.gui.util.TranslateGUIElements;

public class FFaplJFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
					.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private JTree jTree_API;
	private JMenuBar jMenuBarMain;
	private JMenu jMenuFile;
	private JMenu jMenuExtras;
	private JMenu jMenuEdit;
	private JMenu jMenuRun;
	private JMenu jMenuView;
	private JTabbedPane jTabbedPane_info;
	private JTabbedPane jTabbedPane_Code;
	private JSplitPane jSplitPane_main;
	private JSplitPane jSplitPane_mainmain;
	// private JSplitPane jSplitPane_API;
	private JPanel jPanel_status;
	private JScrollPane jScrollPane_API;
	// private JPanel jPanel_APIText;
	// private JScrollPane jScrollPane_APIText;
	private JButton jButton_Run;
	private JButton jButton_Undo;
	private JButton jButton_Redo;
	private JButton jButton_Save;
	private JButton jButton_SaveAll;
	private JButton jButton_Open;
	private JButton jButton_ZoomIn;
	private JButton jButton_ZoomOut;
	private JButton jButton_NewFile;
	private JMenuItem jMenuItem_Preferences;
	private JTextPane jTextPane_Console;
	// private JTextPane jTextPane_API;
	// private JPanel jPanel_Console;
	private JScrollPane jScrollPane_Console;
	private JPanelCode jPanel_code;
	private JButton jButton_Terminate;
	private JToolBar jToolBar_Interpreter;
	private JMenuItem jMenuItem_Run;
	private JMenuItem jMenuItem_Terminate;
	private JMenuItem jMenuItem_Exit;
	private JMenuItem jMenuItem_SaveAs;
	private JMenuItem jMenuItem_SaveAll;
	private JMenuItem jMenuItem_About;
	private JMenuItem jMenuItem_Redo;
	private JMenuItem jMenuItem_Undo;
	private JMenuItem jMenuItem_Cut;
	private JMenuItem jMenuItem_Copy;
	private JMenuItem jMenuItem_Paste;
	private JMenuItem jMenuItem_CloseTab;
	private JMenuItem jMenuItem_CloseAllTab;
	private JSeparator jSeparator_Close;
	private JSeparator jSeparator_CloseTab;
	private JMenuItem jMenuItem_Save;
	private JSeparator jSeparator_Save;
	private JMenuItem jMenuItem_Open;
	private JMenuItem jMenuItem_Print;
	private JMenuItem jMenuItem_New;
	private JCheckBoxMenuItem jCheckBoxMenuItem_Api;
	private JMenuItem jMenuItem_ZoomIn;
	private JMenuItem jMenuItem_ZoomOut;
	private JMenu jMenuHelp;
	private JLabel _lineColumnPosition;
	private JLabel _lineColumnTxt;
	private JToolBar.Separator jToolBar_seperator_1;
	private JToolBar.Separator jToolBar_seperator_2;
	private static JTextField jTextField_input;
	private JPanel jPanel_console;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	private static Vector<Component> _startComp;
	private Vector<Component> _stopComp;
	private Vector<Component> _closeTabComp;
	private Vector<Component> _closeAllTabComp;
	private Vector<Component> _zoomInComp;
	private Vector<Component> _zoomOutComp;

	private File _openFile;// File to open on program load
	private JMenuItem jMenuItem_snippet;
	private JButton jButton_Print;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		final String[] arguments = args;


		//Run on command line
		if (arguments.length == 1 && arguments[0].equals("--cmd"))
		{
			try {
				String pid = ManagementFactory.getRuntimeMXBean().getName();
				System.out.println("process ID:" + pid);

				//Reader reader = new FileReader(arguments[1]);
				BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));

				String ffaplProgram = new String();
				String s = new String();

				while( (s = stdinReader.readLine()) != null && !s.equals("END"))
				{
					ffaplProgram += (s + '\n');
				}

				Reader reader = new StringReader(ffaplProgram);

				FFaplLogger logger = new FFaplLogger("FFaplLog");
				logger.addObserver(new FFaplConsoleHandler());
				FFaplInterpreter _running = new FFaplInterpreter(logger, reader);
				ExecuteThread executeThread = new ExecuteThread(_running, null, null);
				executeThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}





		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FFaplJFrame inst = new FFaplJFrame(arguments);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public FFaplJFrame(String[] args) {
		super();

		if (args != null && args.length > 0) {
			_openFile = new File(args[0]);
		} else {
			_openFile = null;
		}

		List<Image> icons = new ArrayList<Image>();
		icons.add((new ImageIcon(getClass().getClassLoader().getResource(
				IProperties.IMAGEPATH + "sunset16.png")).getImage()));
		icons.add((new ImageIcon(getClass().getClassLoader().getResource(
				IProperties.IMAGEPATH + "sunset20.png")).getImage()));
		icons.add((new ImageIcon(getClass().getClassLoader().getResource(
				IProperties.IMAGEPATH + "sunset24.png")).getImage()));
		icons.add((new ImageIcon(getClass().getClassLoader().getResource(
				IProperties.IMAGEPATH + "sunset32.png")).getImage()));
		icons.add((new ImageIcon(getClass().getClassLoader().getResource(
				IProperties.IMAGEPATH + "sunset64.png")).getImage()));
		this.setIconImages(icons);
		initGUIProperties();
		initGUI();
	}

	private void initGUIProperties() {
		// initGUIDeclarations
		// XMLReader.initGuiDeclarations(GUIPropertiesLogic.getInstance().getProperty(IProperties.LANGUAGE));
		SunsetBundle.getInstance().setLocale(
				new Locale(GUIPropertiesLogic.getInstance().getProperty(
						IProperties.LANGUAGE)));
		FFaplRegex.init();

	}

	/**
	 * Closes the code Tab
	 * 
	 * @return
	 */
	public boolean closeCodeTab() {
		boolean isClosed = true;
		while (jTabbedPane_Code.getTabCount() > 0) {
			JPanelCode panel = (JPanelCode) this.jTabbedPane_Code
					.getComponentAt(0);
			isClosed = panel.close();
			if (isClosed) {
				jTabbedPane_Code.removeTabAt(0);
			} else {
				return false;
			}
		}
		return isClosed;
	}

	/**
	 * Initiates the Language of the GUI
	 * 
	 */
	public void initLanguage() {
		// translate Menubar MenusBar
		TranslateGUIElements.translateMenuBar(jMenuBarMain);

		// Buttons
		TranslateGUIElements.translateButtonToolTipText(jButton_Run);
		TranslateGUIElements.translateButtonToolTipText(jButton_Terminate);
		TranslateGUIElements.translateButtonToolTipText(jButton_Undo);
		TranslateGUIElements.translateButtonToolTipText(jButton_Redo);
		TranslateGUIElements.translateButtonToolTipText(jButton_Save);
		TranslateGUIElements.translateButtonToolTipText(jButton_SaveAll);
		TranslateGUIElements.translateButtonToolTipText(jButton_Print);
		TranslateGUIElements.translateButtonToolTipText(jButton_ZoomIn);
		TranslateGUIElements.translateButtonToolTipText(jButton_ZoomOut);
		TranslateGUIElements.translateButtonToolTipText(_lineColumnTxt);
		// TranslateGUIElements.translateTappedPane(this.jTabbedPane_Code);
		TranslateGUIElements.translateTappedPane(this.jTabbedPane_info);

		initAPI();
	}

	/**
	 * Store the GUI Properties to the file
	 */
	public void storeGUIProperties() {
		GUIPropertiesLogic.getInstance().setProperty(IProperties.GUI_HEIGHT,
				String.valueOf(this.getHeight()));
		GUIPropertiesLogic.getInstance().setIntegerProperty(
				IProperties.GUI_WIDTH, this.getWidth());
		GUIPropertiesLogic.getInstance().setBooleanProperty(
				IProperties.GUI_MAXIMIZED,
				this.getExtendedState() == Frame.MAXIMIZED_BOTH);
		GUIPropertiesLogic.getInstance().setIntegerProperty(
				IProperties.GUI_DIVIDER_API,
				jSplitPane_mainmain.getDividerLocation());
		GUIPropertiesLogic.getInstance().setIntegerProperty(
				IProperties.GUI_DIVIDER_CONSOLE,
				jSplitPane_main.getDividerLocation());
		GUIPropertiesLogic.getInstance().storePropertyFile();
	}

	private void initGUI() {

		boolean showAPI = GUIPropertiesLogic.getInstance().getBooleanProperty(
				IProperties.SHOW_API);

		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			_undoComp = new Vector<Component>();
			_redoComp = new Vector<Component>();
			_saveComp = new Vector<Component>();
			_saveAllComp = new Vector<Component>();
			_startComp = new Vector<Component>();
			_stopComp = new Vector<Component>();
			_closeTabComp = new Vector<Component>();
			_closeAllTabComp = new Vector<Component>();
			_zoomInComp = new Vector<Component>();
			_zoomOutComp = new Vector<Component>();

			{
				jToolBar_Interpreter = new JToolBar();
				getContentPane().add(jToolBar_Interpreter, BorderLayout.NORTH);
				jToolBar_Interpreter.setName("jToolBar_interpreter");
				{
					jButton_NewFile = new JButton();
					jToolBar_Interpreter.add(jButton_NewFile);
					jButton_NewFile.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "newfile24.png")));
					jButton_NewFile.setToolTipText("New File");
					jButton_NewFile.setName("button_newfile");
				}
				{
					jButton_Open = new JButton();
					jToolBar_Interpreter.add(jButton_Open);
					jButton_Open.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "open.png")));
					jButton_Open.setToolTipText("Open");
					jButton_Open.setName("button_open");


				}
				{
					jButton_Save = new JButton();
					jToolBar_Interpreter.add(jButton_Save);
					jButton_Save.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "save24.png")));
					jButton_Save.setToolTipText("Save");
					jButton_Save.setName("button_save");
					jButton_Save.setEnabled(false);
					_saveComp.add(jButton_Save);

				}
				{
					jButton_SaveAll = new JButton();
					jToolBar_Interpreter.add(jButton_SaveAll);
					jButton_SaveAll.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "save24all.png")));
					jButton_SaveAll.setToolTipText("Save All");
					jButton_SaveAll.setName("button_saveall");
					jButton_SaveAll.setEnabled(false);
					_saveAllComp.add(jButton_SaveAll);
				}
				{
					jToolBar_Interpreter.add(new JToolBar.Separator());
				}
				{
					jButton_Run = new JButton();
					jToolBar_Interpreter.add(jButton_Run);
					jButton_Run.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "run24.png")));
					jButton_Run.setToolTipText("Run");
					jButton_Run.setName(IFFaplLang.BUTTON_RUN);
					_startComp.add(jButton_Run);
				}
				{
					jButton_Terminate = new JButton();
					jToolBar_Interpreter.add(jButton_Terminate);
					jButton_Terminate
					.setIcon(new ImageIcon(getClass().getClassLoader()
							.getResource(
									IProperties.IMAGEPATH
									+ "terminate24.png")));
					jButton_Terminate.setName(IFFaplLang.BUTTON_TERMINATE);
					jButton_Terminate.setToolTipText("Terminate");
					_stopComp.add(jButton_Terminate);
				}
				{
					jToolBar_seperator_1 = new JToolBar.Separator();
					jToolBar_Interpreter.add(jToolBar_seperator_1);
				}
				{
					jButton_Undo = new JButton();
					jToolBar_Interpreter.add(jButton_Undo);
					jButton_Undo.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "undo24.png")));
					jButton_Undo.setToolTipText("Undo");
					jButton_Undo.setName("button_undo");
					jButton_Undo.setEnabled(false);
					_undoComp.add(jButton_Undo);
				}
				{
					jButton_Redo = new JButton();
					jToolBar_Interpreter.add(jButton_Redo);
					jButton_Redo.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "redo24.png")));
					jButton_Redo.setToolTipText("Redo");
					jButton_Redo.setName("button_redo");
					jButton_Redo.setEnabled(false);
					_redoComp.add(jButton_Redo);
				}
				{
					jToolBar_seperator_2 = new JToolBar.Separator();
					jToolBar_Interpreter.add(jToolBar_seperator_2);
				}
				{
					jButton_Print = new JButton();
					jToolBar_Interpreter.add(jButton_Print);
					jButton_Print.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "print24.png")));
					jButton_Print.setToolTipText("Print");
					jButton_Print.setName("button_print");
				}
				{
					jToolBar_Interpreter.add(new JToolBar.Separator());
				}
				{
					jButton_ZoomIn = new JButton();
					jToolBar_Interpreter.add(jButton_ZoomIn);
					jButton_ZoomIn.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "zoomin24.png")));
					jButton_ZoomIn.setToolTipText("Zoom In");
					jButton_ZoomIn.setName("button_zoomin");                                        
					_zoomInComp.add(jButton_ZoomIn);
				}
				{
					jButton_ZoomOut = new JButton();
					jToolBar_Interpreter.add(jButton_ZoomOut);
					jButton_ZoomOut.setIcon(new ImageIcon(getClass()
							.getClassLoader().getResource(
									IProperties.IMAGEPATH + "zoomout24.png")));
					jButton_ZoomOut.setToolTipText("Zoom Out");
					jButton_ZoomOut.setName("button_zoomout");
					jButton_ZoomOut.setEnabled(false);
					_zoomOutComp.add(jButton_ZoomOut);
				}
			}
			{
				jPanel_status = new JPanel();
				jPanel_status.setLayout(new FlowLayout(FlowLayout.LEADING));
				getContentPane().add(jPanel_status, BorderLayout.SOUTH);
				{
					_lineColumnTxt = new JLabel();
					_lineColumnTxt.setText("line : column -> ");
					_lineColumnTxt.setName("label_linecolumntxt");
					jPanel_status.add(_lineColumnTxt);

					_lineColumnPosition = new JLabel();
					_lineColumnPosition.setText("0 : 0");
					_lineColumnPosition.setName("label_linecolumnposition");
					jPanel_status.add(_lineColumnPosition);
				}
			}
			{
				jSplitPane_mainmain = new JSplitPane();

				getContentPane().add(jSplitPane_mainmain, BorderLayout.CENTER);
				jSplitPane_mainmain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
				// jSplitPane_mainmain.setPreferredSize(new
				// java.awt.Dimension(954, 489));

				jScrollPane_API = new JScrollPane();
				// jPanel_API.setLayout(new BorderLayout());
				jSplitPane_mainmain.add(jScrollPane_API, JSplitPane.BOTTOM);
				jSplitPane_mainmain.getBottomComponent().setVisible(showAPI);

				{
					// jSplitPane_API = new JSplitPane();
					// jSplitPane_API.setOrientation(JSplitPane.VERTICAL_SPLIT);
					// jSplitPane_API.setDividerLocation(0.5);
					// jPanel_API.add(jSplitPane_API, BorderLayout.CENTER);

					{
						// jPanel_APIText = new JPanel();
						// jPanel_APIText.setLayout(new BorderLayout());
						// jSplitPane_API.setBottomComponent(jPanel_APIText);
						//

						{
							/*
							 * jScrollPane_APIText = new JScrollPane();
							 * jScrollPane_APIText
							 * .setHorizontalScrollBarPolicy(JScrollPane
							 * .HORIZONTAL_SCROLLBAR_NEVER);
							 * 
							 * jPanel_APIText.add(jScrollPane_APIText,
							 * BorderLayout.CENTER); { jTextPane_API = new
							 * JTextPane(); jTextPane_API.setEditable(false);
							 * jTextPane_API
							 * .setEditorKitForContentType("text/ffapl", new
							 * FFaplEditorKit(jTextPane_API));
							 * jTextPane_API.setContentType("text/ffapl");
							 * jScrollPane_APIText
							 * .setViewportView(jTextPane_API); }
							 */
						}

					}
				}

			}

			{
				jSplitPane_main = new JSplitPane();
				jSplitPane_mainmain.add(jSplitPane_main, JSplitPane.TOP);
				// getContentPane().add(jSplitPane_main, BorderLayout.CENTER);
				jSplitPane_main.setOrientation(JSplitPane.VERTICAL_SPLIT);

				{
					jTabbedPane_Code = new JTabbedPaneCode();
					jSplitPane_main.add(jTabbedPane_Code, JSplitPane.TOP);
					// jTabbedPane_Code.setPreferredSize(new
					// java.awt.Dimension(952, 328));
					// jTabbedPane_Code.setFont(new
					// java.awt.Font("Segoe UI",0,12));
					jTabbedPane_Code.setName("TabbedPane_Code");
					{
						String filename = SunsetBundle.getInstance()
								.getProperty(IFFaplLang.NEW_FILE);
						if (filename == null) {
							filename = "New" + IProperties.FILEEXTENTION;
						} else {
							filename = filename + IProperties.FILEEXTENTION;
						}
						jPanel_code = new JPanelCode(this, jTabbedPane_Code,
								_undoComp, _redoComp, _saveComp, _saveAllComp,
								_lineColumnPosition);
						if (((JPanelCode) jPanel_code).loadFile(_openFile)) {
							filename = _openFile.getName();
						}
						jTabbedPane_Code.addTab(filename, jPanel_code);
						jTabbedPane_Code.setTabComponentAt(0,
								new JPanelTabTitle(jTabbedPane_Code,
										jTabbedPane_Code.getTitleAt(0)));
						jPanel_code
						.setTabTitle((JPanelTabTitle) jTabbedPane_Code
								.getTabComponentAt(0));
						this.setTitle(MessageFormat.format(
								IProperties.APPTITLE, "- " + filename + " -"));
						jTabbedPane_Code.setSelectedIndex(0);
					}
				}
				{
					jTabbedPane_info = new JTabbedPane();
					jSplitPane_main.add(jTabbedPane_info, JSplitPane.BOTTOM);
					// jTabbedPane_info.setPreferredSize(new
					// java.awt.Dimension(952, 162));
					// jTabbedPane_info.setFont(new
					// java.awt.Font("Segoe UI",0,12));
					{
						jScrollPane_Console = new JScrollPane();
						jTextField_input = new JTextField();
						jTextField_input.setEnabled(false);
						jPanel_console = new JPanel();
						jPanel_console.setLayout(new BorderLayout());
						jPanel_console.add(jScrollPane_Console, BorderLayout.CENTER);
						jPanel_console.add(jTextField_input, BorderLayout.AFTER_LAST_LINE);
						jTabbedPane_info.addTab("Console", null,
								jPanel_console, null);
						jScrollPane_Console
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						// jScrollPane_Console.setAutoscrolls(true);
						jScrollPane_Console.setName("tabbedPane_console");
						{
							// jPanel_Console = new JPanel();
							// BorderLayout jPanel_ConsoleLayout = new
							// BorderLayout();
							// jPanel_Console.setLayout(jPanel_ConsoleLayout);

							// jScrollPane_Console.setViewportView(jPanel_Console);
							{
								jTextPane_Console = ((JPanelCode) jPanel_code)
										.getConsole();
								// jPanel_Console.add(jTextPane_Console,
								// BorderLayout.CENTER);
								jTextPane_Console.setEditable(false);
								// jTextPane_Console.setFont(new
								// java.awt.Font("Segoe UI",0,12));
								jScrollPane_Console
								.setViewportView(jTextPane_Console);
							}
						}
					}
				}
			}
			{
				jMenuBarMain = new JMenuBar();
				setJMenuBar(jMenuBarMain);
				{
					jMenuFile = new JMenu();
					jMenuBarMain.add(jMenuFile);
					jMenuFile.setText("File");
					jMenuFile.setName("menu_file");
					{
						jMenuItem_New = new JMenuItem();
						jMenuFile.add(jMenuItem_New);
						jMenuItem_New.setText("New");
						jMenuItem_New
						.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "newfile16.png")));
						jMenuItem_New.setName("menuitem_new");
						jMenuItem_New.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_N, ActionEvent.CTRL_MASK));
					}
					{
						jMenuItem_Open = new JMenuItem();
						jMenuFile.add(jMenuItem_Open);
						jMenuItem_Open.setText("Open File");
						jMenuItem_Open.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "mydocuments.png")));
						jMenuItem_Open.setName("menuitem_openfile");
						jMenuItem_Open.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_O, ActionEvent.CTRL_MASK));
					}
					{
						jSeparator_CloseTab = new JSeparator();
						jMenuFile.add(jSeparator_CloseTab);
					}
					{
						jMenuItem_Print = new JMenuItem();
						jMenuFile.add(jMenuItem_Print);
						jMenuItem_Print.setText("Print File");
						jMenuItem_Print.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "print16.png")));
						jMenuItem_Print.setName("menuitem_printfile");
						jMenuItem_Print.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_P,
										ActionEvent.CTRL_MASK));
					}
					{
						jSeparator_CloseTab = new JSeparator();
						jMenuFile.add(jSeparator_CloseTab);
					}
					{
						jMenuItem_CloseTab = new JMenuItem();
						jMenuFile.add(jMenuItem_CloseTab);
						jMenuItem_CloseTab.setText("Close");
						jMenuItem_CloseTab.setName("menuitem_closetab");
						jMenuItem_CloseTab.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_W,
										ActionEvent.CTRL_MASK));
						_closeTabComp.add(jMenuItem_CloseTab);
					}
					{
						jMenuItem_CloseAllTab = new JMenuItem();
						jMenuFile.add(jMenuItem_CloseAllTab);
						jMenuItem_CloseAllTab.setText("Close All");
						jMenuItem_CloseAllTab.setName("menuitem_closealltab");
						jMenuItem_CloseAllTab.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_W,
										ActionEvent.CTRL_MASK
										| ActionEvent.SHIFT_MASK));
						_closeAllTabComp.add(jMenuItem_CloseAllTab);
					}
					{
						jSeparator_Save = new JSeparator();
						jMenuFile.add(jSeparator_Save);
					}
					{
						jMenuItem_Save = new JMenuItem();
						jMenuFile.add(jMenuItem_Save);
						jMenuItem_Save.setText("Save");
						jMenuItem_Save.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "save.png")));
						jMenuItem_Save.setName("menuitem_save");
						jMenuItem_Save.setEnabled(false);
						_saveComp.add(jMenuItem_Save);
						jMenuItem_Save.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_S, ActionEvent.CTRL_MASK));
					}
					{
						jMenuItem_SaveAs = new JMenuItem();
						jMenuFile.add(jMenuItem_SaveAs);
						jMenuItem_SaveAs.setText("Save as...");
						jMenuItem_SaveAs.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "saveas.png")));
						jMenuItem_SaveAs.setName("menuitem_saveas");
					}
					{
						jMenuItem_SaveAll = new JMenuItem();
						jMenuFile.add(jMenuItem_SaveAll);
						jMenuItem_SaveAll.setText("Save All");
						jMenuItem_SaveAll
						.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "saveall.png")));
						jMenuItem_SaveAll.setName("menuitem_saveall");
						jMenuItem_SaveAll.setEnabled(false);
						jMenuItem_SaveAll.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_S,
										ActionEvent.CTRL_MASK
										| ActionEvent.SHIFT_MASK));
						_saveAllComp.add(jMenuItem_SaveAll);
					}
					{
						jSeparator_Close = new JSeparator();
						jMenuFile.add(jSeparator_Close);
					}
					{
						jMenuItem_Exit = new JMenuItem();
						jMenuFile.add(jMenuItem_Exit);
						jMenuItem_Exit.setText("Exit");
						jMenuItem_Exit.setName("menuitem_exit");
					}

				}
				{
					jMenuEdit = new JMenu();
					jMenuBarMain.add(jMenuEdit);
					jMenuEdit.setText("Edit");
					jMenuEdit.setName("menu_edit");
					{
						jMenuItem_Undo = new JMenuItem();
						jMenuEdit.add(jMenuItem_Undo);
						jMenuItem_Undo.setText("Undo");
						jMenuItem_Undo.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "undo.png")));
						jMenuItem_Undo.setName("menuitem_undo");
						jMenuItem_Undo.setEnabled(false);
						jMenuItem_Undo.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
						_undoComp.add(jMenuItem_Undo);
					}
					{
						jMenuItem_Redo = new JMenuItem();
						jMenuEdit.add(jMenuItem_Redo);
						jMenuItem_Redo.setText("Redo");
						jMenuItem_Redo.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "redo.png")));
						jMenuItem_Redo.setName("menuitem_redo");
						jMenuItem_Redo.setEnabled(false);
						jMenuItem_Redo.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
						_redoComp.add(jMenuItem_Redo);
					}
					{
						jSeparator_Close = new JSeparator();
						jMenuEdit.add(jSeparator_Close);
					}
					{
						jMenuItem_Cut = new JMenuItem(new DefaultEditorKit.CutAction());
						jMenuEdit.add(jMenuItem_Cut);
						jMenuItem_Cut.setText("Cut");
						jMenuItem_Cut.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "cut16.png")));
						jMenuItem_Cut.setName("menuitem_cut");
						jMenuItem_Cut.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_X, ActionEvent.CTRL_MASK));
					}
					{
						jMenuItem_Copy = new JMenuItem(new DefaultEditorKit.CopyAction());
						jMenuEdit.add(jMenuItem_Copy);
						jMenuItem_Copy.setText("Copy");
						jMenuItem_Copy.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "copy16.png")));
						jMenuItem_Copy.setName("menuitem_copy");
						jMenuItem_Copy.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_C, ActionEvent.CTRL_MASK));
					}
					{
						jMenuItem_Paste = new JMenuItem(new DefaultEditorKit.PasteAction());
						jMenuEdit.add(jMenuItem_Paste);
						jMenuItem_Paste.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "paste16.png")));
						jMenuItem_Paste.setText("Paste");
						jMenuItem_Paste.setName("menuitem_paste");
						jMenuItem_Paste.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_V, ActionEvent.CTRL_MASK));
					}

					jMenuRun = new JMenu();
					jMenuBarMain.add(jMenuRun);
					jMenuRun.setText("Run");
					jMenuRun.setName("menu_run");
					{
						jMenuItem_Run = new JMenuItem();
						jMenuRun.add(jMenuItem_Run);
						jMenuItem_Run.setText("Run");
						jMenuItem_Run.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "run16.png")));
						jMenuItem_Run.setName("button_run");
						jMenuItem_Run.setEnabled(true);
						jMenuItem_Run.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_R, ActionEvent.CTRL_MASK));
						_startComp.add(jMenuItem_Run);

						jMenuItem_Terminate = new JMenuItem();
						jMenuRun.add(jMenuItem_Terminate);
						jMenuItem_Terminate.setText("Redo");
						jMenuItem_Terminate.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "terminate16.png")));
						jMenuItem_Terminate.setName("button_terminate");
						jMenuItem_Terminate.setEnabled(false);
						jMenuItem_Terminate.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_T,
										ActionEvent.CTRL_MASK));
						_stopComp.add(jMenuItem_Terminate);
					}
				}
				{
					jMenuView = new JMenu();
					jMenuBarMain.add(jMenuView);
					jMenuView.setText("View");
					jMenuView.setName("menu_view");
					{                                        
						jMenuItem_ZoomIn = new JMenuItem();
						jMenuItem_ZoomIn.setText("Zoom In");
						jMenuItem_ZoomIn.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "zoomin16.png")));
						jMenuItem_ZoomIn.setName("button_zoomin");
						jMenuItem_ZoomIn.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_PLUS,
										ActionEvent.CTRL_MASK));                                                
						jMenuView.add(jMenuItem_ZoomIn);
						_zoomInComp.add(jMenuItem_ZoomIn);

						jMenuItem_ZoomOut = new JMenuItem();
						jMenuItem_ZoomOut.setText("Zoom Out");
						jMenuItem_ZoomOut.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "zoomout16.png")));
						jMenuItem_ZoomOut.setName("button_zoomout");
						jMenuItem_ZoomOut.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_MINUS,
										ActionEvent.CTRL_MASK));
						jMenuItem_ZoomOut.setEnabled(false);
						jMenuView.add(jMenuItem_ZoomOut);
						_zoomOutComp.add(jMenuItem_ZoomOut);
					}
					{
						jSeparator_CloseTab = new JSeparator();
						jMenuView.add(jSeparator_CloseTab);
					}
					{
						jCheckBoxMenuItem_Api = new JCheckBoxMenuItem();
						jCheckBoxMenuItem_Api.setText("Show API");
						jCheckBoxMenuItem_Api.setName("menuitem_showapi");
						jCheckBoxMenuItem_Api.setSelected(showAPI);
						jMenuView.add(jCheckBoxMenuItem_Api);
					}

				}

				{
					jMenuExtras = new JMenu();
					jMenuBarMain.add(jMenuExtras);
					jMenuExtras.setText("Extras");
					jMenuExtras.setName("menu_extras");
					{

						jMenuItem_snippet = new JMenuItem(SunsetBundle.getInstance().getProperty("menuitem_addSnippet"));
						jMenuItem_snippet.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "new16.png")));

						jMenuExtras.add(jMenuItem_snippet);

						jMenuItem_Preferences = new JMenuItem();
						jMenuExtras.add(jMenuItem_Preferences);
						jMenuItem_Preferences.setText("Preferences");
						jMenuItem_Preferences.setName("menuitem_preferences");
						jMenuItem_Preferences
						.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH
										+ "config16.png")));
					}
				}
				{
					jMenuHelp = new JMenu();
					jMenuBarMain.add(jMenuHelp);
					jMenuHelp.setText("Help");
					jMenuHelp.setName("menu_help");
					{
						jMenuItem_About = new JMenuItem();
						jMenuHelp.add(jMenuItem_About);
						jMenuItem_About.setText("About");
						jMenuItem_About.setName("menuitem_about");
						jMenuItem_About.setIcon(new ImageIcon(getClass()
								.getClassLoader().getResource(
										IProperties.IMAGEPATH + "info16.png")));
					}
				}
			}

			initLanguage();

			try {
				if (GUIPropertiesLogic.getInstance().getBooleanProperty(
						IProperties.GUI_MAXIMIZED)) {
					this.setExtendedState(Frame.MAXIMIZED_BOTH);
				} else {
					this.setSize(
							GUIPropertiesLogic.getInstance()
							.getIntegerProperty(IProperties.GUI_WIDTH),
							GUIPropertiesLogic.getInstance()
							.getIntegerProperty(
									(IProperties.GUI_HEIGHT)));
				}
				// pack();
				jSplitPane_main.setDividerLocation(GUIPropertiesLogic
						.getInstance().getIntegerProperty(
								IProperties.GUI_DIVIDER_CONSOLE));
				jSplitPane_mainmain.setDividerLocation(GUIPropertiesLogic
						.getInstance().getIntegerProperty(
								IProperties.GUI_DIVIDER_API));

			} catch (Exception ee) {
				this.setSize(970, 600);
				jSplitPane_main.setDividerLocation(380);
				jSplitPane_mainmain.setDividerLocation(500);
			}

			initListener();
			initComponents();
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	/**
	 * inits all Listener
	 */
	private void initListener() {

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ActionListenerExecuteCode runTerminate = new ActionListenerExecuteCode(
				(JTabbedPaneCode) jTabbedPane_Code, _startComp, _stopComp);
		jButton_Run.addActionListener(runTerminate);
		jButton_Terminate.addActionListener(runTerminate);
		jMenuItem_Run.addActionListener(runTerminate);
		jMenuItem_Terminate.addActionListener(runTerminate);
		jMenuItem_Exit.addActionListener(new ActionListenerExit(this,
				runTerminate));
		this.addWindowListener(new WindowListenerFFaplGUI(runTerminate));
		jMenuItem_Preferences.addActionListener(new ActionListenerPreferences(
				this));
		jMenuItem_About.addActionListener(new ActionListenerAbout(this));
		jMenuItem_New.addActionListener(new ActionListenerNewFile(this,
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp, _lineColumnPosition));
		jButton_NewFile.addActionListener(new ActionListenerNewFile(this,
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp, _lineColumnPosition));
		jMenuItem_Open.addActionListener(new ActionListenerOpenFile(this,
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp, _lineColumnPosition));
		jButton_Open.addActionListener(new ActionListenerOpenFile(this,
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp, _lineColumnPosition));

		jMenuItem_Save.addActionListener(new ActionListenerSave(
				(JTabbedPaneCode) jTabbedPane_Code, _saveComp, _saveAllComp));
		jMenuItem_SaveAs.addActionListener(new ActionListenerSaveAs(
				(JTabbedPaneCode) jTabbedPane_Code, _saveComp, _saveAllComp));

		jButton_Undo.addActionListener(new ActionListenerUndo(
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp));
		jButton_Redo.addActionListener(new ActionListenerRedo(
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp));
		jMenuItem_Undo.addActionListener(new ActionListenerUndo(
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp));
		jMenuItem_Redo.addActionListener(new ActionListenerRedo(
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp));
		jButton_Save.addActionListener(new ActionListenerSave(
				(JTabbedPaneCode) jTabbedPane_Code, _saveComp, _saveAllComp));
		jMenuItem_Print.addActionListener(new ActionListenerPrintCurrentFile(
				(JTabbedPaneCode) jTabbedPane_Code));
		jButton_Print.addActionListener(new ActionListenerPrintCurrentFile(
				(JTabbedPaneCode) jTabbedPane_Code));
		jButton_SaveAll.addActionListener(new ActionListenerSaveAll(
				(JTabbedPaneCode) jTabbedPane_Code, _saveComp, _saveAllComp));
		jMenuItem_SaveAll.addActionListener(new ActionListenerSaveAll(
				(JTabbedPaneCode) jTabbedPane_Code, _saveComp, _saveAllComp));
		jMenuItem_CloseTab.addActionListener(new ActionListenerCloseTab(
				(JTabbedPaneCode) jTabbedPane_Code));
		jMenuItem_CloseAllTab.addActionListener(new ActionListenerCloseAllTab(
				(JTabbedPaneCode) jTabbedPane_Code));
		jMenuItem_ZoomIn.addActionListener(new ActionListenerZoom(
				(JTabbedPaneCode) jTabbedPane_Code, _zoomInComp, _zoomOutComp));
		jMenuItem_ZoomOut.addActionListener(new ActionListenerZoom(
				(JTabbedPaneCode) jTabbedPane_Code, _zoomInComp, _zoomOutComp));
		jButton_ZoomIn.addActionListener(new ActionListenerZoom(
				(JTabbedPaneCode) jTabbedPane_Code, _zoomInComp, _zoomOutComp));
		jButton_ZoomOut.addActionListener(new ActionListenerZoom(
				(JTabbedPaneCode) jTabbedPane_Code, _zoomInComp, _zoomOutComp));
		jCheckBoxMenuItem_Api.addActionListener(new ActionListenerShowAPI(
				jSplitPane_mainmain, jScrollPane_API));
		jSplitPane_mainmain
		.addPropertyChangeListener(new PropertyChangeListenerAPI(0.9));
		setDropTarget(new DropTarget(this, new DropTargetListenerFile(this,
				(JTabbedPaneCode) jTabbedPane_Code, _undoComp, _redoComp,
				_saveComp, _saveAllComp, _lineColumnPosition)));
		jTabbedPane_Code.addChangeListener(new ChangeListenerSelectedTab(this,
				jScrollPane_Console, jTextField_input, _undoComp, _redoComp, _saveComp,
				_saveAllComp, _closeTabComp, _closeAllTabComp, jPanel_status));
		_lineColumnPosition.addMouseListener(new MouseListenerChooseLineNumber(
				this, (JTabbedPaneCode) jTabbedPane_Code));
		_lineColumnTxt.addMouseListener(new MouseListenerChooseLineNumber(this,
				(JTabbedPaneCode) jTabbedPane_Code));
		jMenuItem_snippet.addActionListener(new ActionListenerEditApiEntry(this, null));
		jTextField_input.addActionListener(new ActionListenerInputField((JTabbedPaneCode) jTabbedPane_Code));
	}

	private void initComponents() {
		jButton_NewFile.setFocusable(false);
		jButton_Terminate.setEnabled(false);
		jButton_Terminate.setFocusable(false);
		jButton_Run.setFocusable(false);
		jButton_Save.setFocusable(false);
		jButton_SaveAll.setFocusable(false);
		jButton_Undo.setFocusable(false);
		jButton_Redo.setFocusable(false);
		jButton_Print.setFocusable(false);
		jButton_ZoomIn.setFocusable(false);
		jButton_ZoomOut.setFocusable(false);
	}

	private void initAPI() {
		MutableTreeNode rootAPI, root, rootSnippets, rootExample, node, subnode;
		String procedure;
		String function;
		String types, samples, custom;
		DefaultTreeModel model;
		// APITreeDragSource ds;
		int i;
		DefaultTreeCellRenderer renderer = new APITreeCellRenderer();
		if (jTree_API != null) {
			jTree_API.removeAll();
		} else {
			jTree_API = new JTree();
			new APITreeDragSource(jTree_API);
			// jPanel_API.add(jTree_API, BorderLayout.CENTER);
			jScrollPane_API.setViewportView(jTree_API);
			jTree_API.addMouseListener(new MouseListenerShowAPIDialog(this));

		}

		jTree_API.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		// jPanel_APIText.setTopComponent(jTree_API);

		// jSplitPane_mainmain.setBottomComponent(jTree_API);

		procedure = SunsetBundle.getInstance().getProperty("apitree_procedure");
		function = SunsetBundle.getInstance().getProperty("apitree_function");
		types = SunsetBundle.getInstance().getProperty("apitree_type");
		custom = SunsetBundle.getInstance().getProperty("apitree_customCode");

		samples = SunsetBundle.getInstance().getProperty("apitree_sample");

		if (procedure == null) {
			procedure = "procedures";
		}
		if (function == null) {
			function = "functions";
		}
		if (types == null) {
			types = "types";
		}
		if (samples == null) {
			samples = "samples";
		}

		model = (DefaultTreeModel) jTree_API.getModel();
		jTree_API.setCellRenderer(renderer);
		root = new DefaultMutableTreeNode(SunsetBundle.getInstance().getProperty("dialog_api"));
		rootAPI = new DefaultMutableTreeNode(SunsetBundle.getInstance().getProperty("apitree_specification"));
		root.insert(rootAPI, root.getChildCount());
		node = new DefaultMutableTreeNode(types);
		
		ApiSpecification api = ApiLogic.getInstance().getApiSpecification();
		
		if (api != null) {
			rootAPI.insert(node, rootAPI.getChildCount());
			i = 0;
			i = ApiUtil.addToTreeNode(node, api.getTypeList(), i);

			node = new DefaultMutableTreeNode(function);
			rootAPI.insert(node, rootAPI.getChildCount());
			i = 0;
			i = ApiUtil.addToTreeNode(node, api.getFunctionList(), i);

			node = new DefaultMutableTreeNode(procedure);
			rootAPI.insert(node, rootAPI.getChildCount());
			i = 0;
			i = ApiUtil.addToTreeNode(node, api.getProcedureList(), i);
		}		

		SampleCode sampleCodes = ApiLogic.getInstance().getSamples();
		
		if (sampleCodes != null) {
			rootExample = new DefaultMutableTreeNode(samples);
			root.insert(rootExample, root.getChildCount());

			subnode = new DefaultMutableTreeNode(function);
			rootExample.insert(subnode, rootExample.getChildCount());
			i = 0;
			ApiUtil.addToTreeNode(subnode, sampleCodes.getFunctionList(), i);

			subnode = new DefaultMutableTreeNode(procedure);
			rootExample.insert(subnode, rootExample.getChildCount());
			i = 0;
			ApiUtil.addToTreeNode(subnode, sampleCodes.getProcedureList(), i);
		}

		rootSnippets = new DefaultMutableTreeNode(SunsetBundle.getInstance().getProperty("snippet_label"));

		SnippetCode snippetCodes = ApiLogic.getInstance().getSnippetCode();
		
		if (snippetCodes != null) {
			rootSnippets = new MutableTreeNodeHead(custom);
			root.insert(rootSnippets, root.getChildCount());

			ApiUtil.addToTreeNode(rootSnippets, snippetCodes.getSnippetList(), 0, true);
		}

		model.setRoot(root);
		jTree_API.setRootVisible(true);
	}

	public static JTextField getInputField(){
		return jTextField_input;
	}

	public static Vector<Component> getStartComp(){
		return _startComp;
	}
}
