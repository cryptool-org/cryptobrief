package sunset.gui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.Element;

import com.sun.nio.file.ExtendedOpenOption;

import sunset.gui.FFaplJFrame;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.interfaces.IProperties;
import sunset.gui.lib.FFaplFileFilter;
import sunset.gui.lib.FFaplUndoManager;
import sunset.gui.listener.AdjustmentListenerLineNumber;
import sunset.gui.listener.AdjustmentListenerScrollPaneVertical;
import sunset.gui.listener.CaretListenerColumnPosition;
import sunset.gui.listener.DocumentListenerErrorLine;
import sunset.gui.listener.DocumentListenerLineNumber;
import sunset.gui.listener.DocumentListenerSaveStatus;
import sunset.gui.listener.DocumentListenerUndoManager;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.util.SunsetBundle;

@SuppressWarnings("serial")
public class JPanelCode extends javax.swing.JPanel {
        
        public static Font codeFont = new java.awt.Font("Monospaced",Font.PLAIN,12);
        
	private JTextPane jTextPane_LineNumber;
	private JPanel jPanel_LineNumber;
	private JTextPane jTextPane_Code;
	private JPanel jPanel_Code;
	private JScrollPane jScrollPane_Code;
	private JScrollPane jScrollPane_LineNumber;
	private File _file;
	private FileChannel _fileChannel;
	private JTabbedPane _owner;
	private Window _window;
	private JPanelTabTitle _tabtitle;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	private JTextPane jTextPane_Console;
	private JLabel _lineColumnPosition;
	public final static String STR_CONSOLE = "jtextpane_console";
        
        private JTextField _inputField;
        	
	/**
	 * 
	 * @param window
	 * @param owner
	 * @param undoComp
	 * @param redoComp
	 */
	public JPanelCode(Window window, JTabbedPane owner, Vector<Component> undoComp, Vector<Component> redoComp,
					  Vector<Component> saveComp, Vector<Component> saveAllComp,
					  JLabel lineColumnPosition) {
		super();
		_file = null;
		_owner = owner;
		_window = window;
		_tabtitle = null;
		_redoComp = redoComp;
		_undoComp = undoComp;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
		_lineColumnPosition = lineColumnPosition;
                _inputField = new JTextField();
                _inputField.setEnabled(false);
		initGUI();
		jTextPane_Console = new JTextPane();
		jTextPane_Console.setName(STR_CONSOLE);
                this.setCodeFont(codeFont);
		//jTextPane_Code.getCaret().setDot(0);
		//jTextPane_Code.requestFocusInWindow();
	}
	
	/**
	 * Initialize Gui
	 */
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			BorderLayout jPanel_codeLayout = new BorderLayout();
			setLayout(jPanel_codeLayout);
			jScrollPane_LineNumber = new JScrollPane();
			jScrollPane_LineNumber.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane_LineNumber.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jScrollPane_LineNumber.setEnabled(false);
			this.add(jScrollPane_LineNumber, BorderLayout.WEST);
			jScrollPane_LineNumber.setPreferredSize(new java.awt.Dimension(39, 300));
			{
				jPanel_LineNumber = new JPanel();
				BorderLayout jPanel_LineNumberLayout = new BorderLayout();
				jPanel_LineNumber.setLayout(jPanel_LineNumberLayout);
				jScrollPane_LineNumber.setViewportView(jPanel_LineNumber);
				{
					jTextPane_LineNumber = new JTextPane();
					jPanel_LineNumber.add(jTextPane_LineNumber, BorderLayout.CENTER);                                     
                                        jTextPane_LineNumber.setFont(codeFont);
					jTextPane_LineNumber.setBackground(new java.awt.Color(223,223,223));
					jTextPane_LineNumber.setText("");
					jTextPane_LineNumber.setEditable(false);
					jTextPane_LineNumber.setEnabled(false);
					jTextPane_LineNumber.setForeground(new java.awt.Color(128,128,128));
				}
			}
			{
				jScrollPane_Code = new JScrollPane();
				//for synchronisation
				
				
				this.add(jScrollPane_Code, BorderLayout.CENTER);
				{
					jPanel_Code = new JPanel();
					BorderLayout jPanel_CodeLayout = new BorderLayout();
					jPanel_Code.setLayout(jPanel_CodeLayout);
					jScrollPane_Code.setViewportView(jPanel_Code);
					{
						jTextPane_Code = new FFaplCodeTextPane();
						jPanel_Code.add(jTextPane_Code, BorderLayout.CENTER);
                                                jTextPane_Code.setFont(codeFont);
					}
				}
			}
			initListeners();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * init Listeners
	 */
	private void initListeners(){
		
		jTextPane_Code.getDocument().addDocumentListener(new DocumentListenerLineNumber(jTextPane_LineNumber));
		jTextPane_Code.setText("program calculate{\r\n\t\r\n\t\r\n}");
		jTextPane_Code.addCaretListener(new CaretListenerColumnPosition(_lineColumnPosition));
		jTextPane_Code.getDocument().addDocumentListener(
				new DocumentListenerSaveStatus(this, 
												_owner,
												_saveComp,
												_saveAllComp));
		//Synch errorline
		jTextPane_Code.getDocument().addDocumentListener(
				new DocumentListenerErrorLine((FFaplCodeTextPane) jTextPane_Code));
		((FFaplCodeTextPane)jTextPane_Code).getManager().discardAllEdits();
		
		//Undo manager
		jTextPane_Code.getDocument().addDocumentListener(
				new DocumentListenerUndoManager((FFaplCodeTextPane) jTextPane_Code, _undoComp, _redoComp));
		//needed if Scrollbar is dragged
		jScrollPane_Code.getVerticalScrollBar().addMouseMotionListener(new AdjustmentListenerScrollPaneVertical(jScrollPane_LineNumber));
		jScrollPane_Code.getVerticalScrollBar().setUnitIncrement(16);
		//needed if LineNumberPane is draggeed
		jScrollPane_LineNumber.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListenerLineNumber(jScrollPane_Code));
		//Syncronize LineNumber ScrollPane
		jScrollPane_Code.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListenerScrollPaneVertical(jScrollPane_LineNumber));
		
		jTextPane_Code.getCaret().setDot(0);
		((FFaplCodeTextPane) jTextPane_Code).getActiveView().updateSyntaxHighlighting();
	}
	/** 
	 * sets the tabtitle of the Pane
	 * @param tabtitle
	 */
	public void setTabTitle(JPanelTabTitle tabtitle){
		_tabtitle = tabtitle;
	}
	
	/**
	 * loads a File into the Pane and stores the link
	 * @param file
	 * @return 
	 */
	public boolean loadFile(File file){
		String filename = "";
		String title = "";
		String msg = "";
		
		_file = file;
		
		ByteBuffer bytebuffer;
		
		if(_file != null ){
			if(_file.canRead()){
				try {
					_fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.READ, 
							StandardOpenOption.WRITE, ExtendedOpenOption.NOSHARE_WRITE, ExtendedOpenOption.NOSHARE_DELETE);
					bytebuffer = ByteBuffer.allocate((int) _file.length());
					_fileChannel.read(bytebuffer);
				    jTextPane_Code.setText(new String(bytebuffer.array()));
				    ((FFaplCodeTextPane)jTextPane_Code).getManager().discardAllEdits();
				    jTextPane_Code.getCaret().setDot(0);
				    jTextPane_LineNumber.getCaret().setDot(0);
				    //jScrollPane_LineNumber.getVerticalScrollBar().setValue(0);
				} catch (IOException e) {
					 filename = _file.getName();
					 releaseFile();
					 title = SunsetBundle.getInstance().getProperty("file_locked_title");
					 msg = SunsetBundle.getInstance().getProperty("file_locked");
					 
					 if(title == null){
							title = "File Locked!";
						}
						if(msg == null){
							msg = "The process can not access '" + filename + "', due to a file lock from another process!";
						}else{
							msg = MessageFormat.format(msg, "'" + filename + "'");
						}
						
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
					   
					return false;
				}                                
			}
			((FFaplCodeTextPane)jTextPane_Code).inputSaved(true);
			return true;
		}else{
			return false;
		}
		
		
	}
	
	/**
	 * Close the Tab, release file locks, ask for save
	 * @return
	 */
	public boolean close(){
		boolean isSaved;
		String filename = _tabtitle.getTitle();
		String title = SunsetBundle.getInstance().getProperty("question_savechange_title");
		String msg = SunsetBundle.getInstance().getProperty("question_savechange");
		int answer;
                if(!((FFaplCodeTextPane)jTextPane_Code).isSaved()){
                        if(title == null){
                                title = "Save changes?";
                        }
                        if(msg == null){
                                msg = "Save changes from '" + filename + "'?";
                        }else{
                                msg = MessageFormat.format(msg, "'" + filename + "'");
                        }

                        answer = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);

                        if(answer == JOptionPane.YES_OPTION){
                                isSaved = save();
                        }else if(answer == JOptionPane.CANCEL_OPTION){
                                isSaved = false; //abort
                        }else{
                                isSaved = true; //do not save
                        }
                }else{
                        isSaved = true;
                }

                if(isSaved){
                        releaseFile();
                        FFaplJFrame.getStartComp().remove(_tabtitle.getButton());
                        return true;
                }else{//not saved abroad
                        return false;
                }
	}
		
	/**
	 * Saves the input to the specified File or opens File Chooser if
	 * no File is set.
	 * @return
	 */
	public boolean save(){
		int length;
		String txt;
		byte[] txtBytes;		
		ByteBuffer byteBuffer;
		boolean saved = false;
		
		if(!((FFaplCodeTextPane)jTextPane_Code).isSaved()){
			txt = jTextPane_Code.getText();
			byteBuffer = ByteBuffer.allocate(txt.length()+1); //necessary because of buffer overflow!
			if(_file != null){
				//there exists a file in the background
				txtBytes = txt.getBytes();
				byteBuffer.put(txtBytes);
				byteBuffer.flip();
				try {
					_fileChannel.truncate(0);
					length = _fileChannel.write(byteBuffer);
					
					//System.out.println(length + " <-> " + txtBytes.length);
					if(txtBytes.length != length ){
						//TODO make output message
						System.err.println("not everything written to file");
						saved = false;
					}else{
						saved = true;
                                                _fileChannel.force(saved);
					}
				} catch (IOException e) {
					e.printStackTrace();
					saved = false;
				}
				
			}else{
				saved = saveAs();
			}
		}else{
			//already Saved	
			saved = true;
		}
		
		if(saved){
			//set current state manager to saved state
			((FFaplUndoManager)((FFaplCodeTextPane)jTextPane_Code).getManager()).setSaved();
			
			
			if(_tabtitle != null && _file != null){
				_tabtitle.setTitle(_file.getName());
			}else{
				//TODO output
				System.err.println("error during file saving: no tab title set");
			}
		}
		
		((FFaplCodeTextPane)jTextPane_Code).inputSaved(saved);
		return saved;
	}
	
	/**
	 * Opens File chooser and saves the input to the new File
	 * @return
	 */
	public boolean saveAs(){
		int returnVal;
		String dir;
		JFileChooser fileChooser;
		boolean saved = false;
		File file;
		String filename = "";
		String title = "";
		String msg = "";
		int answer;
		
		fileChooser = new JFileChooser();
			
		//set Filter
		fileChooser.addChoosableFileFilter(new FFaplFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		//set current directory
		dir = GUIPropertiesLogic.getInstance().getProperty(IProperties.LASTDIRSAVE);
		if(dir != null){
			fileChooser.setCurrentDirectory(new File(dir));
		}
		
		returnVal = fileChooser.showSaveDialog(_window);
		if (returnVal == JFileChooser.APPROVE_OPTION){
			//releaseFile();
			file = fileChooser.getSelectedFile();
			
			if(!file.getName().endsWith(IProperties.FILEEXTENTION)){
				file = new File(file.getAbsoluteFile() + IProperties.FILEEXTENTION);
			}
			
			if(file.exists()){//override
				//Handle override
				
				filename = file.getName();
				title = SunsetBundle.getInstance().getProperty("question_fileoverride_title");
				msg = SunsetBundle.getInstance().getProperty("question_fileoverride");
				
				//Get Translations
				if(title == null){
					title = "Override File?";
				}
				if(msg == null){
					msg = "Override file '" + filename + "'?";
				}else{
					msg = MessageFormat.format(msg, "'" + filename + "'");
				}
				
				answer = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION){
					//nothing todo
				}else{
					return false; //do not save
				}
				
				
			}
			//overrides File if a file exists
			releaseFile();
			_file = file;
			
			GUIPropertiesLogic.getInstance().setProperty(IProperties.LASTDIRSAVE, _file.getAbsolutePath());
			
			try {
				_fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.READ, 
					StandardOpenOption.WRITE, ExtendedOpenOption.NOSHARE_WRITE, ExtendedOpenOption.NOSHARE_DELETE);
				
				((FFaplCodeTextPane)jTextPane_Code).inputSaved(false);
				saved = save();
			} catch (IOException e) {
				//System.out.println("File is Locked");
				   title = SunsetBundle.getInstance().getProperty("warning_title");
				   msg = SunsetBundle.getInstance().getProperty("info_cannotoverride"); 
				   if(title == null){
						title = "Warning";
					}
					if(msg == null){
						msg = "Can not override file. File '" + filename + "' is currently in use.";
					}else{
						msg = MessageFormat.format(msg, "'" + filename + "'");
					}
					
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				   
				   saved = false;
			}
			
		}
		return saved;
	}

	/**
	 * Returns the Panel of the Code
	 * @return
	 */
	public JTextPane getCodePane(){
		return jTextPane_Code;
	}
	
	public JTextPane getConsole(){
		return jTextPane_Console;
	}
        
        public JTextField getInputField(){
                return _inputField;
        }
  	
        /**
         * Sets the font of the line numbers and the code panel
         * @param newFont 
         */
        public void setCodeFont(Font newFont){
            
            codeFont = newFont;
            
            jTextPane_Code.setFont(newFont);
            jTextPane_LineNumber.setFont(newFont);
            jTextPane_Console.setFont(jTextPane_Console.getFont().deriveFont((float)newFont.getSize()));
            ((FFaplCodeTextPane)jTextPane_Code).getActiveView().updateSyntaxHighlighting();
            jTextPane_Code.repaint();
            jTextPane_LineNumber.repaint();
            jTextPane_Console.repaint();
        }
        
	/**
	 * release the File
	 * @return
	 */
	private boolean releaseFile(){
		try {
			if (_fileChannel != null)
				_fileChannel.close();
			_file = null;
			_fileChannel = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the title of the file
	 * @return
	 */
	public String getTitle() {
		if(this._tabtitle != null){
			return _tabtitle.getTitle();
		}else{
			if(_file != null){
				return _file.getName();
			}else{
				return "";
			}
		}
	}

	public void setLineColumn() {
		Element root, e;
		root = jTextPane_Code.getDocument().getRootElements()[0];
		int position = jTextPane_Code.getCaret().getDot();
		
		for(int i = 0; i < root.getElementCount(); i++){
			e = root.getElement(i);
			if(e.getStartOffset() <= position && position < e.getEndOffset() ){
				if(_lineColumnPosition != null){
					_lineColumnPosition.setText((i+1) + " : " + (position - e.getStartOffset()));
				}
				break;
			}
		}		
	}
}
