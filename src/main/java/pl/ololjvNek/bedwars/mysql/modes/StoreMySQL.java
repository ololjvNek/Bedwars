package pl.ololjvNek.bedwars.mysql.modes;

import org.bukkit.scheduler.BukkitRunnable;
import pl.ololjvNek.bedwars.Main;
import pl.ololjvNek.bedwars.mysql.Callback;
import pl.ololjvNek.bedwars.mysql.Store;
import pl.ololjvNek.bedwars.utils.Logger;
import pl.ololjvNek.bedwars.utils.TimeUtil;
import pl.ololjvNek.bedwars.utils.Timming;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreMySQL implements Store
{
    private final String host;
    private final String user;
    private final String pass;
    private final String name;
    private final String prefix;
    private final int port;
    private Connection conn;
    private long time;
    private Executor executor;
    private AtomicInteger ai;

    public StoreMySQL(final String host, final int port, final String user, final String pass, final String name, final String prefix) {
        super();
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.prefix = prefix;
        this.executor = Executors.newSingleThreadExecutor();
        this.time = System.currentTimeMillis();
        this.ai = new AtomicInteger();
        new BukkitRunnable() {
            public void run() {
                if (System.currentTimeMillis() - StoreMySQL.this.time > TimeUtil.SECOND.getTime(30)) {
                    StoreMySQL.this.update(false, "DO 1");
                }
            }
        }.runTaskTimer(Main.getPlugin(), 600L, 600L);
    }

    public boolean connect() {
        final Timming t = new Timming("MySQL ping").start();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Logger.info("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            Logger.info("Connected to the MySQL server!", "Connection ping " + t.stop().getExecutingTime() + "ms!");
            return true;
        }
        catch (ClassNotFoundException e) {
            Logger.warning("JDBC driver not found!", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        catch (SQLException e2) {
            Logger.warning("Can not connect to a MySQL server!", "Error: " + e2.getMessage());
            e2.printStackTrace();
        }
        return false;
    }

    public void update(final boolean now, final String update) {
        this.time = System.currentTimeMillis();
        final Runnable r = new Runnable() {
            public void run() {
                try {
                    StoreMySQL.this.conn.createStatement().executeUpdate(update.replace("{P}", StoreMySQL.this.prefix));
                }
                catch (SQLException e) {
                    Logger.warning("An error occurred with given query '" + update.replace("{P}", StoreMySQL.this.prefix) + "'!", "Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        if (now) {
            r.run();
        }
        else {
            this.executor.execute(r);
        }
    }

    public ResultSet update(final String update) {
        try {
            final Statement statement = this.conn.createStatement();
            statement.executeUpdate(update.replace("{P}", this.prefix), 1);
            final ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs;
            }
        }
        catch (SQLException e) {
            Logger.warning("An error occurred with given query '" + update.replace("{P}", this.prefix) + "'!", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        if (this.conn != null) {
            try {
                this.conn.close();
            }
            catch (SQLException e) {
                Logger.warning("Can not close the connection to the MySQL server!", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void reconnect() {
        this.connect();
    }

    public boolean isConnected() {
        try {
            return !this.conn.isClosed() || this.conn == null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet query(final String query) {
        try {
            return this.conn.createStatement().executeQuery(query.replace("{P}", this.prefix));
        }
        catch (SQLException e) {
            Logger.warning("An error occurred with given query '" + query.replace("{P}", this.prefix) + "'!", "Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void query(final String query, final Callback<ResultSet> cb) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    final ResultSet rs = StoreMySQL.this.conn.createStatement().executeQuery(query.replace("{P}", StoreMySQL.this.prefix));
                    cb.done(rs);
                }
                catch (SQLException e) {
                    Logger.warning("An error occurred with given query '" + query.replace("{P}", StoreMySQL.this.prefix) + "'!", "Error: " + e.getMessage());
                    cb.error(e);
                    e.printStackTrace();
                }
            }
        }, "MySQL Thread #" + this.ai.getAndIncrement()).start();
    }

    public Connection getConnection() {
        return this.conn;
    }

    public StoreMode getStoreMode() {
        return StoreMode.MYSQL;
    }
}

