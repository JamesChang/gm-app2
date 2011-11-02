package cn.gamemate.app.test.dbunit;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class ExportDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Class driverClass = Class.forName("com.mysql.jdbc.Driver");
		Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:mysql://dev.gamemate.cn/gamemate", "jameszhang", "ZBQ-gamemate");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		// partial database export
        QueryDataSet partialDataSet = new QueryDataSet(connection);
        partialDataSet.addTable("gm_user", "SELECT * FROM gm_user WHERE id<100");
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));
	}

}
