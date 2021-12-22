/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Config.SQLServerConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import views.DangkyJdialog;

/**
 *
 * @author Admin
 */
public class DangKiController {

    DangkyJdialog dangKiJdialog;
    SQLServerConnect sqlServerConnect;
    Connection connection;

    public DangKiController() {

        dangKiJdialog = new DangkyJdialog(new javax.swing.JFrame(), false);
        dangKiJdialog.setVisible(true);
        sqlServerConnect = new SQLServerConnect();
        connection = sqlServerConnect.connect();
        dangKiJdialog.getBtnTaoTK().addActionListener(al -> btnDK());
        dangKiJdialog.getBtnThoat().addActionListener(al -> btnThoat());
    }

    private void btnDK() {
        System.out.println("ok");
        try {
            if (!checkNull()) {
                String sql = "INSERT INTO TAIKHOAN(tai_khoan, mat_khau, cauhoi, traloi )"
                        + "values(?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, dangKiJdialog.getTxtTaiKhoan().getText());
                ps.setString(2, dangKiJdialog.getTxtMK().getText());
                ps.setString(3, dangKiJdialog.getCboHoi().getSelectedItem().toString());
                ps.setString(4, dangKiJdialog.getTxtTraLoi().getText());
                int i = ps.executeUpdate();
                if (i > 0) {
                    JOptionPane.showMessageDialog(dangKiJdialog, "Đăng kí thành công!");
                }
            }

        } catch (SQLException ex) {
            if (ex.toString().indexOf("PRIMARY KEY") > 0) {
                JOptionPane.showMessageDialog(dangKiJdialog, "Tài khoản đã tồn tại!");
            } else {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void btnThoat() {

        new LoginController();

        dangKiJdialog.dispose();
    }

    public boolean checkNull() {
        if (dangKiJdialog.getTxtMK().getText().equals("") || dangKiJdialog.gettxtMK2().getText().equals("") || dangKiJdialog.getTxtTraLoi().getText().equals("")) {
            JOptionPane.showMessageDialog(dangKiJdialog, "Vui lòng nhập thông tin đầy đủ!");
            return true;
        }
        if (!dangKiJdialog.getCheckBox().isSelected()) {
            JOptionPane.showMessageDialog(dangKiJdialog, "Are you robot?? $&*$#&!%**%");
            return true;
        }
        if (dangKiJdialog.getTxtMK().getText().toString().compareTo(dangKiJdialog.gettxtMK2().getText().toString()) != 0) {
            JOptionPane.showMessageDialog(dangKiJdialog, "Mật khẩu không trùng!");
            return true;
        }
        return false;
    }
}
