/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import Config.SQLServerConnect;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import models.SanPhamModel;
import views.SanPhamFrame;
import views.SanPhamJdialog;

/**
 *
 * @author Admin
 */
public class SanphamController {

    private final String[] tableHeaders = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá"};
    SQLServerConnect sqlServerConnect;
    Connection connection;
    private static TableRowSorter<TableModel> rowSorter = null;
    SanPhamFrame view = null;
    SanPhamJdialog viewSp = null;
    SanPhamModel model = null;

    public SanphamController() {
        sqlServerConnect = new SQLServerConnect();
        connection = sqlServerConnect.connect();
        this.view = new SanPhamFrame();
        view.setVisible(true);
        view.setLocationRelativeTo(null);
        setHeaderForTable();
        getDataTotable();
        rowSorter = new TableRowSorter<>(view.getTblBang().getModel());
        view.getTblBang().setRowSorter(rowSorter);
        view.getjtfSearch().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = view.getjtfSearch().getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = view.getjtfSearch().getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

//        view.getAdd().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                viewSp = new NhanVienJdialog(main.app.mainFrame, true);
//                viewSp.getTxtMaKH().setEditable(false);
//
//                viewSp.getBtnThem().addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        insertDataToDB();
//                        getDataTotable();
//                    }
//                });
//                viewSp.getBtnReset().addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        reset();
//                    }
//                });
//                viewSp.setLocationRelativeTo(null);
//                viewSp.setResizable(false);
//                viewSp.setVisible(true);
//            }
//        });
        view.getTblBang().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && view.getTblBang().getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) view.getTblBang().getModel();
                    int selectedRowIndex = view.getTblBang().convertRowIndexToModel(view.getTblBang().getSelectedRow());

                    String masp = model.getValueAt(selectedRowIndex, 0).toString() != null
                            ? model.getValueAt(selectedRowIndex, 0).toString() : "";
                    String tenSp = model.getValueAt(selectedRowIndex, 1).toString() != null
                            ? model.getValueAt(selectedRowIndex, 1).toString() : "";
                    String soLuong = model.getValueAt(selectedRowIndex, 2).toString() != null
                            ? model.getValueAt(selectedRowIndex, 2).toString() : "";

                    String donGia = model.getValueAt(selectedRowIndex, 3).toString() != null
                            ? model.getValueAt(selectedRowIndex, 3).toString() : "";

                    SanPhamModel nv = new SanPhamModel(Integer.parseInt(masp), tenSp, Integer.parseInt(soLuong), Integer.parseInt(donGia));

                    viewSp = new SanPhamJdialog(main.app.mainFrame, true);

                    setModel(nv);
                    viewSp.getBtnThem().addActionListener(al -> btnSuaPerformed());
                    viewSp.getBtnReset().addActionListener(al -> reset());
                    viewSp.setLocationRelativeTo(null);
                    viewSp.setResizable(false);
                    viewSp.setVisible(true);

                }
            }

            //Hiển thị Jpopupmenu khi chọn để sửa hoặc xoá
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = view.getTblBang().rowAtPoint(e.getPoint());
                if (r >= 0 && r < view.getTblBang().getRowCount()) {
                    view.getTblBang().setRowSelectionInterval(r, r);
                } else {
                    view.getTblBang().clearSelection();
                }

                //row index is found...
                int rowindex = view.getTblBang().getSelectedRow();
                if (rowindex < 0) {
                    return;
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    JPopupMenu popup = createEditAndDeletePopUp(rowindex, view.getTblBang());
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

        view.getTblBang().getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        view.getTblBang().getTableHeader().setPreferredSize(new Dimension(100, 50));
        view.getTblBang().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        view.getTblBang().setRowHeight(50);
        view.getTblBang().validate();
        view.getTblBang().repaint();
    }

    private void setHeaderForTable() {
        view.getDtm().setColumnIdentifiers(tableHeaders);
    }

    public void getDataTotable() {
        try {
            view.getDtm().setRowCount(0);
            String sqlQuery = "SELECT * FROM Sanpham";
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maSP = rs.getInt("maSP");
                String tenSP = rs.getString("tenSp");
                int soluong = rs.getInt("soLuong");
                int dongia = rs.getInt("dongia");
                SanPhamModel sp = new SanPhamModel(maSP, tenSP, soluong, dongia);
                view.getDtm().addRow(sp.toArray());
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(SanphamController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void reset() {
        viewSp.getTxttensp().setText("");
        viewSp.getTxtDonGia().setText("");
        viewSp.gettxtSoluong().setText("");
    }

//    public void insertDataToDB() {
//        model = getModel();
//        if (model != null) {
//            try {
//
//                String sqlQueryInsert = "INSERT NHANVIEN(hoTen,ngaySinh,gioiTinh, diaChi) "
//                        + "VALUES (?, ? ,? ,? )";
//                PreparedStatement ps = connection.prepareStatement(sqlQueryInsert);
//                ps.setString(1, model.getTenNV());
//                ps.setDate(2, model.utilDateToSQLDate(model.getNgaySinh()));
//                ps.setString(3, model.getGioiTinh());
//                ps.setString(4, model.getDiaChi());
//
//                int result = ps.executeUpdate();
//                if (result == 1) {
//                    {
//                        JOptionPane.showMessageDialog(view, "Thêm thành công!");
//                        reset();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(view, "Thêm thất bại!");
//                }
//                ps.close();
//            } catch (SQLException ex) {
//                if (ex.toString().contains("PRIMARY KEY")) {
//                    JOptionPane.showMessageDialog(view, "Trùng khoá chính!");
//                } else if (ex.toString().contains("String or binary data would be truncated")) {
//                    JOptionPane.showMessageDialog(view, "Không thể để 1 trường quá dài!");
//                } else {
//                    Logger.getLogger(SanphamController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//
//    }
    public void btnSuaPerformed() {
        model = getModel();
        if (model != null) {
            try {
                String sql = "UPDATE [dbo].[SanPham]"
                        + "       SET"
                        + "       [tenSp] = ?"
                        + "      ,[soLuong] = ?"
                        + "      ,[donGia] = ?"
                        + " WHERE maSp = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, model.getTenSP());
                ps.setInt(2, model.getSoLuong());
                ps.setInt(3, model.getDongia());
                 ps.setInt(4, model.getMaSp());
                int result = ps.executeUpdate();
                if (result == 1) {
                    JOptionPane.showMessageDialog(view, "Thay đổi giá trị thành công!");
                    viewSp.dispose();
                }
                getDataTotable();
                ps.close();

            } catch (SQLException ex) {
                if (ex.toString().contains("String or binary data would be truncated")) {
                    JOptionPane.showMessageDialog(view, "Không thể để 1 trường quá dài!");
                } else {
                    Logger.getLogger(SanphamController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(view, "Không thể cập nhật giá trị! \nCó lỗi xảy ra! ");
                }
            }
        }
    }

    public SanPhamModel getModel() {
        int maSp1 = 0;
        if (viewSp.getTxttensp().getText().isEmpty() || viewSp.getTxtDonGia().getText().isEmpty()|| viewSp.gettxtSoluong().getText().isEmpty()) {
            JOptionPane.showMessageDialog(viewSp, "Phải điền tất cả thông tin!");
        } else {
            String tenSp = viewSp.getTxttensp().getText();
            String maSp = viewSp.getTXtMasp().getText();
            if (!maSp.equals("")){
                maSp1 = Integer.parseInt(maSp);
            }
            String donGia = viewSp.getTxtDonGia().getText();
            String soLuong = viewSp.gettxtSoluong().getText();
            return new SanPhamModel(maSp1, tenSp, Integer.parseInt(soLuong),Integer.parseInt(donGia));
        }
        return null;
    }
    public JPopupMenu createEditAndDeletePopUp(int rowindex, JTable table) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Chỉnh sửa");
        edit.setIcon(new javax.swing.ImageIcon(SanphamController.class.getResource("/images/edit.png"))); // NOI18N
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedRowIndex = table.convertRowIndexToModel(table.getSelectedRow());

                String masp = model.getValueAt(selectedRowIndex, 0).toString() != null
                        ? model.getValueAt(selectedRowIndex, 0).toString() : "";
                String tenSp = model.getValueAt(selectedRowIndex, 1).toString() != null
                        ? model.getValueAt(selectedRowIndex, 1).toString() : "";
                String soLuong = model.getValueAt(selectedRowIndex, 2).toString() != null
                        ? model.getValueAt(selectedRowIndex, 2).toString() : "";

                String donGia = model.getValueAt(selectedRowIndex, 3).toString() != null
                        ? model.getValueAt(selectedRowIndex, 3).toString() : "";

                SanPhamModel nv = new SanPhamModel(Integer.parseInt(masp), tenSp, Integer.parseInt(soLuong), Integer.parseInt(donGia));

                viewSp = new SanPhamJdialog(main.app.mainFrame, true);

                setModel(nv);
                viewSp.getBtnThem().addActionListener(al -> btnSuaPerformed());
                viewSp.getBtnReset().addActionListener(al -> reset());
                viewSp.setLocationRelativeTo(null);
                viewSp.setResizable(false);
                viewSp.setVisible(true);

            }
        });
        JMenuItem delete = new JMenuItem("Xoá");
        delete.setIcon(new javax.swing.ImageIcon(SanphamController.class.getResource("/images/delete.png"))); // NOI18N
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(main.app.mainFrame,
                        "Bạn muốn xoá nhân viên này không?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int selectedRowIndex = table.convertRowIndexToModel(table.getSelectedRow());
                    String masp = model.getValueAt(selectedRowIndex, 0).toString() != null
                            ? model.getValueAt(selectedRowIndex, 0).toString() : "";
                    String tenSp = model.getValueAt(selectedRowIndex, 1).toString() != null
                            ? model.getValueAt(selectedRowIndex, 1).toString() : "";
                    String soLuong = model.getValueAt(selectedRowIndex, 2).toString() != null
                            ? model.getValueAt(selectedRowIndex, 2).toString() : "";

                    String donGia = model.getValueAt(selectedRowIndex, 3).toString() != null
                            ? model.getValueAt(selectedRowIndex, 3).toString() : "";
                    try {
                        String sqlQuery = "DELETE FROM Sanpham WHERE maSp=?";

                        PreparedStatement ps = connection.prepareStatement(sqlQuery);
                        ps.setInt(1, Integer.parseInt(masp));
                        int result = ps.executeUpdate();
                        if (result == 1) {

                            JOptionPane.showMessageDialog(view, "Xóa thành công!");
                        } else {

                        }
                        getDataTotable();
                        ps.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SanphamController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
        popup.add(edit);
        popup.add(delete);
        return popup;
    }

    public void setModel(SanPhamModel sp) {
        viewSp.getTXtMasp().setText(String.format("%03d", sp.getMaSp()));
        viewSp.getTxttensp().setText(sp.getTenSP());
        viewSp.getTXtMasp().setEditable(false);
        viewSp.gettxtSoluong().setText(String.valueOf(sp.getSoLuong()));
        viewSp.getTxtDonGia().setText(String.valueOf(sp.getDongia()));

    }

    public static void main(String[] args) {

        new SanphamController();
    }

}
