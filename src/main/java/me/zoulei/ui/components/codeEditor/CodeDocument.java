package me.zoulei.ui.components.codeEditor;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

import org.apache.commons.lang.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import me.zoulei.MainApp;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.gencode.Gencode2Files;
 
/**
 * 
* @author zoulei 
* @date 2023年9月
* @description 用于展示生成的代码。 使用RSyntaxTextArea编辑控件
 */
@SuppressWarnings("serial")
public class CodeDocument extends JFrame implements ActionListener, DocumentListener {
	private void newFile() {
		if (changed)
			saveFile();
		file = null;
		textArea.setText("");
		changed = false;
		setTitle("Editor");
	}
 
	private String readFile(File file) {
		StringBuilder result = new StringBuilder();
		try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr);) {
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot read file !", "Error !", JOptionPane.ERROR_MESSAGE);
		}
		return result.toString();
	}
 
	private void saveAs(String dialogTitle) {
		JFileChooser dialog = new JFileChooser(System.getProperty("Document.this"));
		dialog.setDialogTitle(dialogTitle);
		int result = dialog.showSaveDialog(this);
		if (result != JFileChooser.APPROVE_OPTION)
			return;
		file = dialog.getSelectedFile();
		try (PrintWriter writer = new PrintWriter(file);) {
			writer.write(textArea.getText());
			changed = false;
			setTitle("Editor - " + file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
 
	private void saveFile() {
		if (changed) {
			int ans = JOptionPane.showConfirmDialog(null, "The file has changed. You want to save it?", "Save file",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (ans == JOptionPane.NO_OPTION)
				return;
		}
		if (file == null) {
			saveAs("Save");
			return;
		}
		String text = textArea.getText();
		System.out.println(text);
		try (PrintWriter writer = new PrintWriter(file);) {
			if (!file.canWrite())
				throw new Exception("Cannot write file!");
			writer.write(text);
			changed = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	private void loadFile() {
		JFileChooser dialog = new JFileChooser(System.getProperty("Document.this"));
		dialog.setMultiSelectionEnabled(false);
		try {
			int result = dialog.showOpenDialog(this);
			if (result == JFileChooser.CANCEL_OPTION)
				return;
			if (result == JFileChooser.APPROVE_OPTION) {
				if (changed)
					saveFile();
				file = dialog.getSelectedFile();
				textArea.setText(readFile(file));
				changed = false;
				setTitle("Editor - " + file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
 
	private File file;
	public boolean changed = false;
	private boolean toolBarEnable = true;
	RSyntaxTextArea textArea;
	
 
	JFrame frame = new JFrame("Input Dialog Box Frame");
	JButton button = new JButton("Show Input Dialog Box");
	JFileChooser fc = new JFileChooser();
 
	//private JTextArea ta;
	private int count;
	private JMenuBar menuBar;
	private String pad;
	private JToolBar toolBar;
	private JMenu file_1;
	private JMenuItem n;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem saveas;
	private JMenuItem 退出;
	private JMenu edit;
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	private JMenuItem find;
	private JMenuItem sall;
	private JMenu format;
	private JMenuItem font;
	private JMenu view;
	private JMenuItem showstatus;
	private JMenu help;
	private JMenuItem about;
	private JTextField status;
 
	
	private void initTextArea(RSyntaxTextArea textArea, String syntaxStyle){
		InputStream in = getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml");
		Theme theme = null;
		try {
			theme = Theme.load(in);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		theme.apply(textArea);
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
	
	public CodeDocument(Map<String,String[]> codemap) {
		this(codemap, null);
	}
	
	public CodeDocument(Map<String,String[]> codemap, TableMetaDataConfig config) {
 
		super("代码查看");
		setSize(1300, 950);
		setLocationRelativeTo(null);
 
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		//创建一个在上面显示标签，布局方式为自动换行的空选项卡
		JTabbedPane tabbedPane = new JTabbedPane();
		pane.add(tabbedPane);
		
		int i=0;
		for(String tabname : codemap.keySet()) {
			RSyntaxTextArea textArea = new RSyntaxTextArea(codemap.get(tabname)[0]);
			initTextArea(textArea, codemap.get(tabname)[1]);
			//添加选项卡
	        tabbedPane.addTab(tabname,  new RTextScrollPane(textArea));
	        if(i==0) {
	        	this.textArea = textArea;
	        }
	        i++;
		}
		
		
        
        
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
 
		count = 0;
		pad = " ";
		//ta = new JTextArea(); // textarea
 
		menuBar = new JMenuBar();
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
 
		//ta.setLineWrap(true);
		//ta.setWrapStyleWord(true);
 
		//隐藏菜单。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
		//菜单用来生成目录结构  前端和后台的，根据名字生成
		setJMenuBar(menuBar);
		
		if(config!=null) {
			file_1 = new JMenu("操作");
			//file_1.setMnemonic('F');
			menuBar.add(file_1);
			n = new JMenuItem("生成目录结构");
			n.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String name = JOptionPane.showInputDialog("请输入英文名称，用来拼接java类名：");
					if(StringUtils.isEmpty(name)) {
						JOptionPane.showMessageDialog(MainPanel.mainFrame, "请输入名称！"); 
						return;
					}
					boolean matche = name.matches("^[A-Za-z_][A-Za-z_0-9]*$");
					if(!matche) {
						JOptionPane.showMessageDialog(MainPanel.mainFrame, "名称不合法，请重新输入！"); 
						return;
					}
					new Gencode2Files(codemap,config,name);
					
				}
			});
			//n.setMnemonic('N');
			file_1.add(n);
		}
		
 
		/*
		file_1 = new JMenu("文件");
		file_1.setMnemonic('F');
		menuBar.add(file_1);
 
		n = new JMenuItem("新建");
		n.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile();
			}
		});
		n.setMnemonic('N');
		file_1.add(n);
 
		open = new JMenuItem("打开");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadFile();
 
			}
		});
		open.setMnemonic('O');
		file_1.add(open);
 
		save = new JMenuItem("保存");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		save.setMnemonic('S');
		file_1.add(save);
 
		saveas = new JMenuItem("另存为...");
		saveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAs("另存为...");
			}
		});
		file_1.add(saveas);
 
		退出 = new JMenuItem("退出");
		退出.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (changed)
					saveFile();
 
				System.exit(0);
			}
		});
		退出.setMnemonic('Q');
		file_1.add(退出);
 
		edit = new JMenu("编辑");
		edit.setMnemonic('E');
		menuBar.add(edit);
 
		cut = new JMenuItem("剪切");
		cut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
			}
		});
		cut.setMnemonic('T');
		edit.add(cut);
 
		copy = new JMenuItem("复制");
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
			}
		});
		copy.setMnemonic('C');
		edit.add(copy);
 
		paste = new JMenuItem("粘贴");
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
			}
		});
		paste.setMnemonic('P');
		edit.add(paste);
 
		find = new JMenuItem("查找");
		find.addActionListener(this);
		find.setMnemonic('F');
		edit.add(find);
 
		sall = new JMenuItem("选择全部");
		sall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.selectAll();
			}
		});
		sall.setMnemonic('A');
		edit.add(sall);
 
		format = new JMenu("格式");
		menuBar.add(format);
 
		font = new JMenuItem("字体");
		font.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFontDialog fontDialog = new JFontDialog();
				fontDialog.setVisible(true);
				Font selectedFont = fontDialog.getFont();
				System.out.println(selectedFont.toString());
				textArea.setFont(selectedFont);
 
			}
		});
		format.add(font);
 
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
 
		help = new JMenu("帮助");
		menuBar.add(help);
 
		about = new JMenuItem("关于");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Text Editor in Java. \n\n 2021", "Text Editor", 1);
			}
		});
		help.add(about);
		
		*/
 
		pane.add(toolBar, BorderLayout.SOUTH);
 
		status = new JTextField();
		status.setText("行: 1 列: 1");
		toolBar.add(status);
 
		
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("me/zoulei/logo.png");
			setIconImage(ImageIO.read(inputStream));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setVisible(true);
		//只有该窗口会关闭
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 
	}
 
	
	void writetofile(File ff) throws Exception {
		FileWriter fw = new FileWriter(ff.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(textArea.getText());
		bw.close();
	}
 
	private static void printLines(String name, InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			System.out.println(name + " " + line);
		}
	}
 
	private static void runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		printLines(command + "\n============================================================\nOUTPUT:\n\n\n\n",
				pro.getInputStream());
		printLines(command + " stderr:", pro.getErrorStream());
		pro.waitFor();
	}
 
//	public void actionPerformed(ActionEvent e) {
//		JMenuItem choice = (JMenuItem) e.getSource();
//
//	}
 

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
			FindDialogDoc find = new FindDialogDoc(this, true);
			find.showDialog();
		}
	}
 
// This helper function updates the status bar with the line number and column
// number.
	private void updateStatus(int linenumber, int columnnumber) {
		status.setText("行: " + linenumber + " 列: " + columnnumber);
	}
 
}
