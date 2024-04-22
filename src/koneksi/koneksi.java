/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PREDATOR
 */
public class koneksi {
    private Connection koneksii;
        public Connection connect(){
            try{
                Class.forName("com.mysql.jdbc.Driver"); 
                System.out.println("berhasil connect");
            }catch (ClassNotFoundException ex) {
                    System.out.println("gagal koneksi"+ex);
                }
                String url = "jdbc:mysql://localhost:3306/posyandu";
            try{
                koneksii = DriverManager.getConnection(url,"root","");
                System.out.println("berhasil mengkoneksi database");
            }catch (SQLException ex) {
                    System.out.println("gagal mengkoneksi database"+ex);
                }
                return koneksii;
        }
}
