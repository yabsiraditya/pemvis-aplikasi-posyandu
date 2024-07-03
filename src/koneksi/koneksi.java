/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 *
 * @author PREDATOR
 */
public class koneksi {

    public static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private Connection koneksi;
        public Connection connect(){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver"); 
                System.out.println("berhasil connect");
            }catch (ClassNotFoundException ex) {
                    System.out.println("gagal koneksi"+ex);
                }
                String url = "jdbc:mysql://localhost:3306/posyandu3";
            try{
                koneksi = DriverManager.getConnection(url,"root","");
                System.out.println("berhasil mengkoneksi database");
            }catch (SQLException ex) {
                    System.out.println("gagal mengkoneksi database"+ex);
                }
                return koneksi;
        }
}
