/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Config.SQLServerConnect;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import models.LoginModel;
import views.LoginView;

/**
 *
 * @author Admin
 */
public class LoginController {

    LoginView loginView;
    LoginModel loginModel;
    SQLServerConnect sqlServerConnect;
    Connection connection;

    public LoginController() {
        this.loginView = new LoginView();
        sqlServerConnect = new SQLServerConnect();
        connection = sqlServerConnect.connect();
        loginView.getTxtTaiKhoan().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
                {
                    btnDangNhapPerformed();
                }
            }
            //Giới hạn ký tự nhập
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (loginView.getTxtTaiKhoan().getText().length() > 32) // limit to 32 characters
                {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    e.consume();                  
                }
            }
        });
        loginView.getTxtMatKhau().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
                {
                    btnDangNhapPerformed();
                }
            }
            //Giới hạn ký tự nhập
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (loginView.getTxtMatKhau().getPassword().length > 32) // limit to 32 characters
                {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    e.consume();                  
                }
            }
        });
        loginView.getBtnDangNhap().addActionListener((ae) -> btnDangNhapPerformed());
        loginView.getLabDK().addMouseListener(labDKMouseListener());       
        loginView.getLabQuenMK().addMouseListener(labQuenMKMouseListener());
    }

    public void btnDangNhapPerformed() {
        loginModel = getModel();
        if (!loginModel.isEmpty()) {
            LoginModel lg = loginValidator(loginModel.getTaiKhoan(), loginModel.getMatKhau());
            if (lg !=null) {
                System.out.println(lg);
                String tenTK = loginModel.getTaiKhoan();
                loginView.dispose();
                JOptionPane.showMessageDialog(main.app.mainFrame, "Xin chào " + tenTK);
                new SanphamController();
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep();
                loginView.getjLabel1().setText("Tài khoản hoặc mật khẩu chưa đúng. Vui lòng thử lại");
                loginView.getTxtTaiKhoan().setText("");
                loginView.getTxtMatKhau().setText("");
            }
        } else {
            loginView.getjLabel1().setText("Tài khoản và mật khẩu không được để trống");
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
    public LoginModel getModel() {
        String taiKhoan = loginView.getTxtTaiKhoan().getText();
        String matKhau = String.valueOf(loginView.getTxtMatKhau().getPassword());
        return new LoginModel(taiKhoan, matKhau);
    }

    LoginModel loginValidator(String username, String password) {
        try {
            System.out.println("login...");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select * from TAIKHOAN");
            while (rs.next()) {
                if (rs.getString("tai_khoan").equals(username) && rs.getString("mat_khau").equals(password)) {
                    return new LoginModel(rs.getString("tai_khoan"),rs.getString("mat_khau"));
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println("loi sql");
        }
        return null;
    }

    private MouseListener labDKMouseListener() {
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loginView.dispose();
                new DangKiController();
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {
                loginView.getLabDK().setForeground(new Color(0, 153, 255));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                loginView.getLabDK().setForeground(new Color(0, 0, 0));
            }
        };
        return ml;
    }

    private MouseListener labQuenMKMouseListener() {
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loginView.dispose();
                QuenMKController quenMKController = new QuenMKController();
            }

            @Override
            public void mousePressed(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                loginView.getLabQuenMK().setForeground(new Color(0, 153, 255));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                loginView.getLabQuenMK().setForeground(new Color(0, 0, 0));
            }
        };
        return ml;
    }

}
