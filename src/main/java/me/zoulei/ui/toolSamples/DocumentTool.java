package me.zoulei.ui.toolSamples;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
 
@SuppressWarnings("serial")
public class DocumentTool extends JFrame implements ActionListener, DocumentListener {
	
 
	public boolean changed = false;
	private boolean toolBarEnable = true;
	RSyntaxTextArea textArea;
	
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private JMenu view;
	private JMenuItem showstatus;
	private JTextField status;
 
	
	private void initTextArea(RSyntaxTextArea textArea, String syntaxStyle){
		textArea.getDocument().addDocumentListener(this);
		textArea.setSyntaxEditingStyle(syntaxStyle);
		textArea.setCodeFoldingEnabled(true);
		textArea.addCaretListener(new CaretListener() {
			// Each time the caret is moved, it will trigger the listener and its method
			// caretUpdate.
			// It will then pass the event to the update method including the source of the
			// event (which is our textarea control)
			public void caretUpdate(CaretEvent e) {
				JTextArea editArea = (JTextArea) e.getSource();
 
				// Lets start with some default values for the line and column.
				int linenum = 1;
				int columnnum = 1;
 
				// We create a try catch to catch any exceptions. We will simply ignore such an
				// error for our demonstration.
				try {
					// First we find the position of the caret. This is the number of where the
					// caret is in relation to the start of the JTextArea
					// in the upper left corner. We use this position to find offset values (eg what
					// line we are on for the given position as well as
					// what position that line starts on.
					int caretpos = editArea.getCaretPosition();
					linenum = editArea.getLineOfOffset(caretpos);
 
					// We subtract the offset of where our line starts from the overall caret
					// position.
					// So lets say that we are on line 5 and that line starts at caret position 100,
					// if our caret position is currently 106
					// we know that we must be on column 6 of line 5.
					columnnum = caretpos - editArea.getLineStartOffset(linenum);
 
					// We have to add one here because line numbers start at 0 for getLineOfOffset
					// and we want it to start at 1 for display.
					linenum += 1;
					columnnum += 1;
				} catch (Exception ex) {
				}
 
				// Once we know the position of the line and the column, pass it to a helper
				// function for updating the status bar.
				updateStatus(linenum, columnnum);
			}
		});
	}
	
	
	public DocumentTool(String s) {
 
		super("代码查看");
		setSize(1300, 950);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		//创建一个在上面显示标签，布局方式为自动换行的空选项卡
		JTabbedPane tabbedPane = new JTabbedPane();
		pane.add(tabbedPane);
		///hfree/src/me/zoulei/ui/toolSamples/samples/excelImp.txt
		
		RSyntaxTextArea textArea_vue = new RSyntaxTextArea();
		
		initTextArea(textArea_vue,SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea = textArea_vue;
		
 
		RTextScrollPane sp = new RTextScrollPane(textArea_vue);
		//添加选项卡
        tabbedPane.addTab("  java代码  ",  sp);
       
        tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane p = (JTabbedPane) e.getSource();
				RTextScrollPane r = (RTextScrollPane) p.getSelectedComponent();
				JViewport v = (JViewport)r.getComponent(0);
				RSyntaxTextArea s = (RSyntaxTextArea) v.getComponent(0);
				textArea = s;
				//System.out.println(s.getText());
			}
		});
 
 
		menuBar = new JMenuBar();
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
 
 
		//显示菜单。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
		setJMenuBar(menuBar);
 
 
 
 
 
		view = new JMenu("查看");
		menuBar.add(view);
 
		showstatus = new JMenuItem("状态栏");
		showstatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (toolBarEnable == true) {
					toolBar.setVisible(false);
					toolBarEnable = false;
 
				} else {
					toolBar.setVisible(true);
					toolBarEnable = true;
				}
			}
		});
		view.add(showstatus);
 
 
		pane.add(toolBar, BorderLayout.SOUTH);
 
		status = new JTextField();
		status.setText("行: 1 列: 1");
		toolBar.add(status);
		
		
		//下拉框
		JComboBox<String> cbx = new JComboBox<String>(new String[] {"大萨达","发的说法是","wad"});
		pane.add(cbx, BorderLayout.NORTH);
		
 
		setVisible(true);
		//只有该窗口会关闭
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		InputStream is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/toolSamples/samples/excelImp.txt");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	textArea_vue.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
 
	}
 
 
 
 
	public static void main(String[] args) {
 
		new DocumentTool("123");
	}
 
	@Override
	public void insertUpdate(DocumentEvent e) {
		changed = true;
	}
 
	@Override
	public void removeUpdate(DocumentEvent e) {
		changed = true;
	}
 
	@Override
	public void changedUpdate(DocumentEvent e) {
		changed = true;
	}
 
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("查找")) {
			//FindDialogDoc find = new FindDialogDoc(this, true);
			//find.showDialog();
		}
	}
 
// This helper function updates the status bar with the line number and column
// number.
	private void updateStatus(int linenumber, int columnnumber) {
		status.setText("行: " + linenumber + " 列: " + columnnumber);
	}
 
}
