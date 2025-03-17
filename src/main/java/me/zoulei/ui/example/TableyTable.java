package me.zoulei.ui.example;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 2023年9月12日19:24:42  zoulei
 * 表格配置后读取参数
 */
public class TableyTable extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public JTableHeader header;
    public JTable table;
    public JScrollPane scrollPane;
    public JPopupMenu renamePopup;
    public JTextField text;
    public TableColumn column;
    private boolean headerEditable = false;
    private boolean tableEditable = false;

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int MIN_ROW_HEIGHT = (int)SCREEN_SIZE.getHeight()/36;
    public static final int MIN_ROW_WIDTH = (int)SCREEN_SIZE.getWidth()/108;


    public TableyTable(int row, int column) {
        init(row, column);
    }

    public void init(int row, int column) {
        table = new JTable(row, column) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;


            @Override
            public boolean isCellEditable(int row, int column) {
                if (tableEditable) return true;
                return false;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.changeSelection(0, 0, false, false);

        header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && headerEditable) {
                	
                    editColumnAt(event.getPoint());
                }
            }
        });

        text = new JTextField();
        text.setBorder(null);
        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (headerEditable) renameColumn();
            }
        });

        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(true);


        renamePopup = new JPopupMenu();
        renamePopup.addPopupMenuListener(new PopupMenuListener() {
			
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

        scrollPane = new JScrollPane( table );
        scrollPane.setRowHeaderView(buildRowHeader(table));
        
        table.setRowHeight(MIN_ROW_HEIGHT);
        TableColumnModel cm = table.getColumnModel();
        for(int i = 0; i < table.getColumnModel().getColumnCount(); i++)
            cm.getColumn(i).setWidth(400);
        table.setColumnModel(cm);
        setLayout(new BorderLayout());
        
        add(scrollPane);
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

    private void editColumnAt(Point p) {
        int columnIndex = header.columnAtPoint(p);
        if (columnIndex != -1) { 
            column = header.getColumnModel().getColumn(columnIndex);
            Rectangle columnRectangle = header.getHeaderRect(columnIndex);

            text.setText(column.getHeaderValue().toString());
            renamePopup.setPreferredSize(new Dimension(columnRectangle.width, columnRectangle.height - 1));
            renamePopup.show(header, columnRectangle.x, 0);

            text.requestFocusInWindow();
            text.selectAll();
        }
    }

    private void renameColumn() {
    	System.out.println(text.getText());
        column.setHeaderValue(text.getText());
        renamePopup.setVisible(false);
        header.repaint();
    }

    private static JList<Object> buildRowHeader(JTable table) {
        final Vector<String> headers = new Vector<String>();
        for (int i = 0; i < table.getRowCount(); i++) {
            String name = "";
            if (i < 10) {
                name += "0";
            }
            if (i < 100) {
                name += "0";
            }
            name += i;
            headers.add(name);
        }
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

        final JList<Object> rowHeader = new JList<>(lm);
        rowHeader.setOpaque(false);
        rowHeader.setFixedCellWidth(TableyTable.MIN_ROW_HEIGHT);


        MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
            Cursor oldCursor;
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
                }
            }

            private boolean isResizeCursor() {
                return rowHeader.getCursor() == RESIZE_CURSOR;
            }
        };
        rowHeader.addMouseListener(mouseAdapter);
        rowHeader.addMouseMotionListener(mouseAdapter);
        rowHeader.addMouseWheelListener(mouseAdapter);

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
            list.firePropertyChange("cellRenderer", 0, 1);
            return this;
        }
    }

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



}