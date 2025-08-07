import java.sql.*;
import java.util.Scanner;

public class CrudOperation {

    private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USERNAME ="postgres";
    private static final String PASSWORD = "246446";

    private static final Scanner scanner = new Scanner(System.in);

    public void createUser() throws SQLException {
        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);

        System.out.print("Enter User id : ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter User name :");
        String name = scanner.nextLine();
        System.out.print("Enter User age : ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(id,name,age);


        String sql = """
                insert into users
                values (?,?,?)
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,user.getId());
        ps.setString(2, user.getName());
        ps.setInt(3,user.getAge());

        int rowAffected =  ps.executeUpdate();
        if (rowAffected>0 ){
            System.out.println("Successful");
        }else {
            System.out.println("Fail");
        }
        conn.close();
        ps.close();
    }

    public void readUserbyId()throws SQLException{
        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        System.out.print("Enter User id to find : ");
        int id = Integer.parseInt(scanner.nextLine());

        if(!exitByid(id)){
            System.out.println("User does not exit!!");
        }

        String sql= """
                select * from users
                where id = ?
                """;
        PreparedStatement ps= conn.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
            );
            System.out.println(user);
        }

    }

    public static boolean exitByid(int id)throws SQLException{
        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        String sql = """
                select 1 from users where id =?
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public void updateUserId()throws SQLException{
        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        System.out.print("Enter User id to Update : ");
        int id = Integer.parseInt(scanner.nextLine());

        if(!exitByid(id)){
            System.out.println("User not found");
            return ;
        }

        System.out.print("Enter New Name : ");
        String newname = scanner.nextLine();
        System.out.print("Enter New age : ");
        int newage = Integer.parseInt(scanner.nextLine());

        String sql = """
                update users
                set name =?,age=?
                where id = ?
               
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,newname);
        ps.setInt(2,newage);
        ps.setInt(3,id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
            );
            System.out.println(user);
        }
        int rowAffected =  ps.executeUpdate();
        if (rowAffected>0 ){
            System.out.println("Successful");
        }else {
            System.out.println("Fail");
        }
        conn.close();
        ps.close();

    }

    public void deleteUserByID() throws SQLException{
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.println("-------- Delete By ID ----------- ");
        System.out.print("Enter userID to Delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = """
                delete from users where id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,id);
        int rowsAffected = ps.executeUpdate();
        if(rowsAffected > 0){
            System.out.println("Delete Successfully");
        }else {
            System.out.println("User not found!");
        }
        conn.close();
        ps.close();
    }

    public void readAllUser() throws SQLException{
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.println("-------- Get all User --------- ");
        String sql = """
                select id, name, age from users
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        try{
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");

                System.out.printf("ID: %d, Name: %s, Age: %s%n", id, name, age);
            }
        }finally {
            rs.close();
            ps.close();
            conn.close();
        }
    }

    public static void main(String[] args) {
        CrudOperation operation = new CrudOperation();
        while (true){
            System.out.println("""
                    1.Add User
                    2.Read by Id
                    3.Update by Id
                    4.Delete by Id
                    5.Read all
                    0.Exit program
                    """);
            System.out.print("Enter your option : ");
            int op = Integer.parseInt(scanner.nextLine());
            if(op == 0)break;
            try {
                switch (op){
                    case 1 -> operation.createUser();
                    case 2 -> operation.readUserbyId();
                    case 3 -> operation.updateUserId();
                    case 4 -> operation.deleteUserByID();
                    case 5 -> operation.readAllUser();

                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
