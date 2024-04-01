package chess.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

    private static final String PROPERTIES_PATH = "properties/chess.properties";
    private static final Properties properties;

    static {
        properties = getProperties();
    }

    private DBConnector() {
    }

    public static Connection getConnection() {
        String username = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        try {
            Connection connection = DriverManager.getConnection(createDatabaseURL(), username, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (final SQLException e) {
            throw new IllegalStateException("DB 연결 오류 : " + e.getMessage());
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream in = DBConnector.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_PATH)) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Properties 파일 로딩 오류 : " + e.getMessage());
        }
        return properties;
    }

    private static String createDatabaseURL() {
        return String.format(
                "jdbc:mysql://%s/%s%s",
                properties.getProperty("database.url"),
                properties.getProperty("database.name"),
                properties.getProperty("database.option")
        );
    }
}
