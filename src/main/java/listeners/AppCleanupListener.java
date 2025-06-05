package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class AppCleanupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Optional: logic during startup (currently unused)
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Shut down MySQL background thread to avoid memory leaks
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Deregister JDBC drivers loaded by this classloader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
