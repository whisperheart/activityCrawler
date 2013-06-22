package editor;

import java.sql.*;   
import java.io.*;   

public class DBTest {
    static    String    driver    =    "com.microsoft.jdbc.sqlserver.SQLServerDriver";   
    static    String    url    =    "jdbc:microsoft:sqlserver://192.168.0.202:9999999999;DatabaseName=dddd";   
    static    String    user    =    "sa";   
    static    String    passwd    =    "ps";   
    
    public    static    void    main(String[]    args)    throws    Exception    {   
        Connection    conn    =    null;   
        try    {   
            Class.forName(driver);   
            conn    =    DriverManager.getConnection(url,user,passwd);   
            int    op    =    0;   
            //²åÈë   
            if    (op    ==    0)    {   
                PreparedStatement    ps    =    conn.prepareStatement("insert    into    tb_file    values    (?,?)");   
                ps.setString(1,    "aaa.exe");   
                InputStream    in    =    new    FileInputStream("d:/aaa.exe");   
                ps.setBinaryStream(2,in,in.available());   
                ps.executeUpdate();   
                ps.close();   
            }   
            else    {   
                //È¡³ö   
                PreparedStatement    ps    =    conn.prepareStatement("select    *    from      tb_file    where    filename    =    ?");   
                ps.setString(1,    "aaa.exe");   
                ResultSet    rs    =    ps.executeQuery();   
                rs.next();   
                InputStream    in    =    rs.getBinaryStream("filecontent");   
                System.out.println(in.available());   
                FileOutputStream    out    =    new    FileOutputStream("d:/bbb.exe");   
                byte[]    b    =    new    byte[1024];   
                int    len    =    0;   
                while    (    (len    =    in.read(b))    !=    -1)    {   
                    out.write(b,    0,    len);   
                    out.flush();   
                }   
                out.close();   
                in.close();   
                rs.close();   
                ps.close();   
            }   
        }   
        catch    (Exception    ex)    {   
            ex.printStackTrace(System.out);   
        }   
        finally    {   
            try    {conn.close();}   
            catch    (Exception    ex)    {    }   
        }   
    }   
}
