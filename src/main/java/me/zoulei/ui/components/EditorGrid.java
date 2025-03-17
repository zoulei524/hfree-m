package me.zoulei.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import me.zoulei.Constants;
import me.zoulei.MainApp;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.ui.components.south.FlowComponentCenter;
import me.zoulei.ui.components.south.FlowSearchComponentNorth;
import me.zoulei.ui.frame.AutoCompletion;

/**
 * 2023年9月12日19:24:42  zoulei
 * 用户表格配置后读取参数
 */
public class EditorGrid extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    

    public JTableHeader header;
    public JTable table;
    public JScrollPane scrollPane;
    public JPopupMenu renamePopup;
    public JTextArea text;
    public TableColumn column;
    private boolean headerEditable = false;
    private boolean tableEditable = false;
    /**
     * 是否流程业务表
     */
    private boolean isFlowBusinessTable = false;
    ////是否有增删改查功能的控件
    public JCheckBox crudCheckBox;
    ////是否有分页功能的控件
    public JCheckBox paginationCheckBox;
    //是否有导出excel功能
    public JCheckBox excelCheckBox;
    //包名输入
    public JTextField packageInput;
    //维护日志
    public JCheckBox logCheckBox;
    //所有的code_type
    public List<String[]> codetypes = new ArrayList<String[]>() {
    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		this.forEach(ct->{
    			sb.append("\t\t\t\t" +ct[0]+": [], //" + ct[1]+"\n");
    		});
    		return sb.toString();
    	}
    };

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int MIN_ROW_HEIGHT = (int)SCREEN_SIZE.getHeight()/36;
    public static final int MIN_ROW_WIDTH = (int)SCREEN_SIZE.getWidth()/108;
    TableModel model;
    
    List<HashMap<String, String>> tableMetaData;



	
    

	

    public EditorGrid(String tablename,String owner) {
    	try {
    		//* 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
    		tableMetaData = new TableMetaDataConfig(tablename,owner,"").getTableMetaData();
		} catch (Exception e) {
			e.printStackTrace();
		}
        init();
    }

    public EditorGrid(String tablename, String owner, boolean isFlowBusinessTable) {
		this.isFlowBusinessTable = isFlowBusinessTable;
		try {
    		//* 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
    		tableMetaData = new TableMetaDataConfig(tablename,owner,"").getTableMetaData();
		} catch (Exception e) {
			e.printStackTrace();
		}
        init();
	}

	public void init() {
    	Object[][] tableDate = new Object[7][tableMetaData.size()];
    	String[] colnames = new String[tableMetaData.size()];
    	for(int i = 0; i<tableMetaData.size(); i++ ) {
    		HashMap<String, String> metaData = tableMetaData.get(i);
    		String comments = metaData.get("comments");
    		String column_name = metaData.get("column_name");
    		String data_type = metaData.get("data_type");
    		String data_length = metaData.get("data_length");
    		String p = metaData.get("p");
    		
    		//这样可以自动换行
    		colnames[i] = this.formateColName(comments);
    		tableDate[0][i] = column_name;
    		tableDate[1][i] = data_type+":"+data_length+":"+p;
    		//设置默认值
    		tableDate[3][i] = ItemEnum.A.toString();
    		
    		tableDate[4][i] = "center";
    		//是否必输项校验
    		tableDate[5][i] = "非必填";
    		//表单控件类型
    		tableDate[6][i] = "文本";
    		
    		//tableDate[7][i] = "否";
    		
    		if("1".equals(p)) {//主键
    			tableDate[3][i] = ItemEnum.B.toString();
    			tableDate[5][i] = "必填";
    		}
    	}
        table = new JTable(tableDate,colnames) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;


            @Override
            public boolean isCellEditable(int row, int column) {
            	//第一行是字段名，第二行宽，通过拖拉改变，不可编辑。
            	if(row==0||row==1||row==2)return false;
            	//维护日志勾选才可维护
//            	if(row==7&&!logCheckBox.isSelected()) {
//            		JOptionPane.showMessageDialog(MainApp.mainFrame, "请先勾选日志维护复选框");  
//            		return false;
//            	}
                if (tableEditable) return true;
                return false;
            }
        };
        //table.putClientProperty("terminateEditOnFocusLost", true);
        this.model = table.getModel();
        
        //单元格居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(Object.class, centerRenderer);//第一个参数填Object,因为表格数据使用的是二维Object数组
        
        //改变列宽是，其他列的列宽不自动变化
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        
        //默认选中第一个单元格
        table.changeSelection(0, 0, false, false);
        
        header = table.getTableHeader();
        //设置高度2行 设置了这个表头宽度就固定了，列宽变了后 总宽不会自动调整， 在列模型上加事件解决
        header.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 80));
        
        
        //添加双击更改表头名称事件
        header.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && headerEditable) {
                	
                    editColumnAt(event.getPoint());
                }
            }
            
        });
        
        
        text = new JTextArea();
        text.setBorder(null);
        text.setLineWrap(true);
        /*
        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (headerEditable) renameColumn();
            }
        });
        */
        //不允许选中行
        table.setRowSelectionAllowed(false);
        //可选中单元格
        table.setCellSelectionEnabled(true);

        //弹出菜单，里面放了一个文本框，用户改表头的名称，改完后更新表头
        renamePopup = new JPopupMenu();
        renamePopup.addPopupMenuListener(new  PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				if (headerEditable) renameColumn();
			}
		});
        renamePopup.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        renamePopup.add(text);

        
        //表格上边工具栏
        JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
//		JLabel dslabel= new JLabel("表格的属性设置: ", JLabel.LEFT);
//		dslabel.setFont(new Font("宋体", Font.PLAIN, 18));
//		toolBar.add(dslabel);
		//是否增删改查功能
		crudCheckBox = new JCheckBox("增删改查功能",true);
		toolBar.add(crudCheckBox);
		//是否分页功能
		paginationCheckBox = new JCheckBox("是否分页",false);
		toolBar.add(paginationCheckBox);
		
		//是否有导出excel功能
		excelCheckBox = new JCheckBox("导出excel数据",false);
		toolBar.add(excelCheckBox);
		
		//是否记录日志，未开发
		logCheckBox = new JCheckBox("维护日志（未开发）",false);
		toolBar.add(logCheckBox);
		
		
		if(!isFlowBusinessTable) {
			//添加间隙
			toolBar.add(Box.createHorizontalStrut(350));
			//输入包名 2024年1月22日16:39:05
			JLabel label_1 = new JLabel("请输入包名：");
			toolBar.add(label_1);
			packageInput = new JTextField(Constants.OUTPUT_PACKAGE, 62); 
			//packageInput.setMaximumSize(packageInput.getPreferredSize());
			toolBar.add(packageInput);
			packageInput.addFocusListener(new FocusListener(){

				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					Constants.OUTPUT_PACKAGE = packageInput.getText();
				}
				
			});
		}
		
		
        scrollPane = new JScrollPane( table );
        //scrollPane.setBorder(new EmptyBorder(0, 14, 0, 14));
        
        //设置表格行表头  第一列
        scrollPane.setRowHeaderView(buildRowHeader(table));
        
        table.setRowHeight(MIN_ROW_HEIGHT);
        TableColumnModel cm = table.getColumnModel();
        cm.addColumnModelListener(new TableColumnModelListener() {
			
			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
			}
			
			@Override
			public void columnRemoved(TableColumnModelEvent e) {
			}
			
			@Override
			public void columnMoved(TableColumnModelEvent e) {
			}
			
			@Override
			public void columnMarginChanged(ChangeEvent e) {
				header.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 80));
				//改变宽的值
				for(int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
					table.setValueAt(cm.getColumn(i).getWidth(), 2, i);
				}
		            
				
			}
			
			@Override
			public void columnAdded(TableColumnModelEvent e) {
			}
		});
        
        for(int i = 0; i < cm.getColumnCount(); i++) {
        	//setWidth没有用   PreferredWidth首选宽度
            cm.getColumn(i).setPreferredWidth(120);
            //设置编辑器
            JBoxTestCell jc = new JBoxTestCell();// 第四行第五行为下拉框，其余行为文本框
            cm.getColumn(i).setCellEditor(jc);
            
        }
        	
        table.setColumnModel(cm);
        setLayout(new BorderLayout());
        
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane);
    }

    
    private String formateColName(String text) {
    	return "<html><div style='height:55px;'>" + text + "</div></html>";
    }
    
    private String getColNameText(String text) {
    	return text.replace("<html><div style='height:55px;'>", "").replace("</div></html>", "");
    }
    
    public void setHeaderEditable(boolean b) {
        headerEditable = b;
    }

    public boolean isHeaderEditable() {
        return headerEditable;
    }

    public void setTableEditable(boolean b) {
        tableEditable = b;
    }

    public boolean isTableEditable() {
        return tableEditable;
    }

    //添加双击更改表头名称
    private void editColumnAt(Point p) {
        int columnIndex = header.columnAtPoint(p);
        if (columnIndex != -1) { 
            column = header.getColumnModel().getColumn(columnIndex);
            Rectangle columnRectangle = header.getHeaderRect(columnIndex);
            //把html换掉  "<html><div style='height:72px;> " + comments + "</div></html>"
            text.setText(this.getColNameText(column.getHeaderValue().toString()));
            renamePopup.setPreferredSize(new Dimension(columnRectangle.width, columnRectangle.height - 1));
            renamePopup.show(header, columnRectangle.x, 0);

            text.requestFocusInWindow();
            text.selectAll();
        }
    }
    //表头名称改完后替换
    private void renameColumn() {
    	//把html加回去
        column.setHeaderValue(this.formateColName(text.getText()));
        renamePopup.setVisible(false);
        header.repaint();
    }

    /**
     * 生成行的表头标题  与table是独立的
     * @param table
     * @return
     */
    private static JList<Object> buildRowHeader(JTable table) {
        final Vector<String> headers = new Vector<String>();
        headers.add("字段名");
        headers.add("数据类型:长度:主键");
        headers.add("列宽");
        headers.add("显示维护");
        headers.add("水平对齐");
        headers.add("前端校验项");
        //表单控件类型 文本、公务员常用时间控件、codetype
        headers.add("表单控件类型(单选)");
        //记日志
        //headers.add("code_table_col脚本");

        ListModel<Object> lm = new AbstractListModel<Object>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public int getSize() {
                return headers.size();
            }

            public Object getElementAt(int index) {
                return headers.get(index);
            }
        };

        //行表头
        final JList<Object> rowHeader = new JList<>(lm);
        rowHeader.setOpaque(true);
        //表头固定宽
        rowHeader.setFixedCellWidth(120);

        //行事件 可以拖动改变行高
        MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
            Cursor oldCursor;
            //鼠标样式 用户调整行高
            Cursor RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            int index = -1;
            int oldY = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int previ = getLocationToIndex(new Point(e.getX(), e.getY() - 3));
                int nexti = getLocationToIndex(new Point(e.getX(), e.getY() + 3));
                if (previ != -1 && previ != nexti) {
                    if (!isResizeCursor()) {
                        oldCursor = rowHeader.getCursor();
                        rowHeader.setCursor(RESIZE_CURSOR);
                        index = previ;
                    }
                } else if (isResizeCursor()) {
                    rowHeader.setCursor(oldCursor);
                }
            }

            private int getLocationToIndex(Point point) {
                int i = rowHeader.locationToIndex(point);
                if (!rowHeader.getCellBounds(i, i).contains(point)) {
                    i = -1;
                }
                return i;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (isResizeCursor()) {
                    rowHeader.setCursor(oldCursor);
                    index = -1;
                    oldY = -1;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                //2024年1月22日10:46:57  改变行表头以及行的高
                rowHeader.firePropertyChange("cellRenderer", 0, 1);
                if (isResizeCursor() && index != -1) {
                    int y = e.getY();
                    if (oldY != -1) {
                        int inc = y - oldY;
                        int oldRowHeight = table.getRowHeight(index);
                        int oldNextRowHeight = table.getRowHeight(index+1);
                        if (oldRowHeight > MIN_ROW_HEIGHT || inc > 0) {
                            int rowHeight = Math.max(MIN_ROW_HEIGHT, oldRowHeight + inc);
                            table.setRowHeight(index, rowHeight);
                            if (rowHeader.getModel().getSize() > index + 1) {
                                int rowHeight1 = table.getRowHeight(index + 1) - inc;
                                rowHeight1 = Math.max(MIN_ROW_HEIGHT, rowHeight1);
                                table.setRowHeight(index + 1, rowHeight1);
                            }
                        }
                        if (table.getRowCount()>index+1)
                            table.setRowHeight(1+index, oldNextRowHeight);
                        else System.out.println("HI");
                    }
                    oldY = y;
                    
                    //rowHeader.firePropertyChange("cellRenderer", 0, 1);
                }
                
            }

            private boolean isResizeCursor() {
                return rowHeader.getCursor() == RESIZE_CURSOR;
            }
        };
        rowHeader.addMouseListener(mouseAdapter);
        rowHeader.addMouseMotionListener(mouseAdapter);
        rowHeader.addMouseWheelListener(mouseAdapter);

        //2024年1月22日10:46:57   这里RowHeaderRenderer(table)调用导致render循环调用，CPU占用一直处在高位
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        
        rowHeader.setBackground(table.getBackground());
        rowHeader.setForeground(table.getForeground());
        return rowHeader;
    }

    static class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object> {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private JTable table;

        RowHeaderRenderer(JTable table) {
            this.table = table;
            JTableHeader header = this.table.getTableHeader();
            setOpaque(true);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(CENTER);
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
            setDoubleBuffered(true);
        }

        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText((value == null) ? "" : value.toString());
            setPreferredSize(null);
            setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), table.getRowHeight(index)));
            //2024年1月22日10:46:57   这里调用导致render循环调用，也就是这个方法会被一直调用，下面的输出一直打印CPU占用一直处在高位  放在鼠标移动时调用
            //list.firePropertyChange("cellRenderer", 0, 1);
            //System.out.println(index);
            return this;
        }
    }
/*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tabley");

        TableyTable table = new TableyTable(100, 1878);
        table.setHeaderEditable(true);
        table.setTableEditable(true);

        frame.add(table);
        frame.setSize(new Dimension(1700, 500));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

*/

    
    /**
     * 点击生成代码的时候，根据表格上的设置获取表格配置
     */
    public List<HashMap<String, String>> genTableMetaData(){
    	List<HashMap<String, String>> tmd = new ArrayList<HashMap<String,String>>();
    	//* 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
    	HashMap<String, String> field = null;
    	//int rowCount = this.model.getRowCount();
    	int colCount = this.model.getColumnCount();
    	this.codetypes.clear();
    	//this.model 是原始的排序  this.table对表格排序之后的
    	for(int c=0; c<colCount; c++) {
    		//字段配置
    		field = new HashMap<String, String>();
    		tmd.add(field);
    		//字段备注
    		String comments = this.getColNameText(this.header.getColumnModel().getColumn(c).getHeaderValue().toString());
    		field.put("comments", comments);
    		//原备注
    		//this.getColNameText(this.model.getColumnName(c))
    		field.put("comments2", comments);
    		
    		//字段名
    		String column_name = this.table.getValueAt(0, c).toString();
    		field.put("column_name", column_name);
    		//其他信息
    		String orther = this.table.getValueAt(1, c).toString();
    		String[] orthers = orther.split(":");
    		field.put("data_type", orthers[0]);
    		field.put("data_length", orthers[1]);
    		field.put("p", orthers[2]);
    		//列宽
    		String width = this.table.getValueAt(2, c).toString();
    		field.put("width", width);
    		//是否显示
    		String visible = this.table.getValueAt(3, c).toString();
    		field.put("visible", visible);
    		//水平对齐
    		String align = this.table.getValueAt(4, c).toString();
    		field.put("align", align);
    		//2023年10月18日17:13:19  加上保存校验项
    		String validate = this.table.getValueAt(5, c).toString();
    		field.put("validate", validate);
    		//2023年10月31日16:49:06  加上代码类型的字段
    		String codetype = this.table.getValueAt(6, c).toString();
    		if("文本".equals(codetype)||"公务员常用时间控件".equals(codetype)) {
    			field.put("codetype", "");
    			field.put("editortype", codetype);
    		}else {
    			codetype = codetype + ":a";
    			String[] codetypes = codetype.split(":");//ZB01:下拉选:备注 ZB01:弹出框:备注
    			field.put("codetype", codetypes[0]);
    			field.put("editortype", codetypes[1]);
    			//只需要下拉选要初始化代码
    			if("下拉选".equals(codetypes[1])) {
    				this.codetypes.add(new String[] {codetypes[0],codetypes[2]});
    			}
    		}
    		//2024年3月4日10:35:08  加上日志sql脚本
    		//String islog = this.table.getValueAt(7, c).toString();
    		field.put("islog", "是");
    	}
    	return tmd;
    }
    
    
    
}











/**
 * 自定义celleditor实现
 * 指定单元格设置下拉框，其他单元格设置文本框
 *
 */
class JBoxTestCell extends AbstractCellEditor implements TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int row;
	int column;
	private JComboBox<String> jbox2;//第四行 
	private JComboBox<String> jbox3;//第五行 
	private JComboBox<String> jbox4;//校验项第6行
	private JComboBox<String> jbox5;//第7行表单控件类型 文本、公务员常用时间控件、codetype
	private JComboBox<String> jbox6;//第8行 是否记录维护日志
	private JTextField textfield;

	public JBoxTestCell() {
		JBoxTestCell boxcell = this;
		//是否显示列配置
		jbox2 = new JComboBox<String>(new String[] {ItemEnum.A.toString(),ItemEnum.B.toString(),ItemEnum.C.toString(),ItemEnum.D.toString()});
		//选择后触发编辑完毕
		jbox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boxcell.stopCellEditing();
			}
		});
		//水平位置配置
		jbox3 = new JComboBox<String>(new String[] {"left","center","right"});
		//选择后触发编辑完毕
		jbox3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boxcell.stopCellEditing();
			}
		});
		jbox3.setSelectedIndex(1);
		
		//校验项
		jbox4 = new JComboBox<String>(new String[] {"非必填","必填"});
		//选择后触发编辑完毕
		jbox4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boxcell.stopCellEditing();
			}
		});
		//表单控件类型 文本、公务员常用时间控件、codetype
		jbox5 = new JComboBox<String>(Constants.codetype_items);
		
		//选择后触发编辑完毕
		jbox5.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyReleased(KeyEvent event) {
		        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
		        	boxcell.stopCellEditing();
		        }
		    }
		});
		
//		jbox6 = new JComboBox<String>(new String[] {"是","否"});
//		jbox6.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				boxcell.stopCellEditing();
//			}
//		});
//		jbox6.setSelectedIndex(1);
		
		//实现搜索
		AutoCompletion.enable(jbox5);
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	

	public Object getCellEditorValue() {
		switch (this.row) {
		
			case 3:
				String v3 = jbox2.getSelectedItem().toString();
				return v3;
			case 4:
				String v4 = jbox3.getSelectedItem().toString();
				return v4;
			case 5:
				String v5 = jbox4.getSelectedItem().toString();
				return v5;
			case 6:
				String v6 = jbox5.getSelectedItem().toString();
				return v6;
//			case 7:
//				String v7 = jbox6.getSelectedItem().toString();
//				return v7;
			default:
				return this.textfield.getText().toString();
		}
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		this.column = column;
		
		switch (this.row) {
		
			case 3:
				return this.jbox2;
			case 4:
				return this.jbox3;
			case 5:
				return this.jbox4;
			case 6:
				return this.jbox5;
//			case 7:
//				return this.jbox6;
			default:
				JTextField result = new JTextField();
				result.setText(value==null?"":value.toString());   
				this.textfield = result;
				return result;
		}
	}
}