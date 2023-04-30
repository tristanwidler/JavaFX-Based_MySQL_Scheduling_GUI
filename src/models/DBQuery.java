package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**This class was retrieved from the C195 Software II Class Code Repository.
 * @author Unknown
 * @version Unknown
 * @since 2021-12-13
 */
public class DBQuery {
    private static PreparedStatement statement;

    //Create Statement Obj
    public static void setPreparedStatement (Connection conn, String sqlStatement) throws SQLException
    {

        statement = conn.prepareStatement(sqlStatement);
    }

    //Return Statement Obj
    public static PreparedStatement getPreparedStatement ()
    {
        return statement;
    }
}
