package me.maploop.mapdungeons.data;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.session.DungeonSession;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SQLData
{
    private final static MapDungeons plugin = MapDungeons.getPlugin();

    private final File file;

    public SQLData() {
        File file = new File(MapDungeons.getPlugin().getDataFolder(), "database.db");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                MapDungeons.getPlugin().saveResource("database.db", false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.file = file;
    }

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            if (connection != null) {
                PreparedStatement prp = connection.prepareStatement(CREATE_TABLE);
                PreparedStatement prp2 = connection.prepareStatement(CREATE_TABLE_SESSIONS);
                prp.execute();
                prp2.execute();
                return connection;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getMobKills(UUID uuid) {
        try (Connection connection = MapDungeons.getPlugin().sql.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();

            return set.getInt("kills");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public List<DungeonSession> getSessions(UUID owner) {
        return getAllSessions().stream().filter((session) -> session.getOwner().equals(owner)).collect(Collectors.toList());
    }

    public double getAverageKPS(UUID uuid) {
        List<DungeonSession> sessions = getSessions(uuid);
        AtomicReference<Double> all = new AtomicReference<>((double) 0);
        sessions.forEach(s -> all.updateAndGet(v -> v + s.getKills()));
        return all.get() / sessions.size();
    }

    public int getDeaths(UUID uuid) {
        try (Connection connection = MapDungeons.getPlugin().sql.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();

            return set.getInt("deaths");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public List<DungeonSession> getAllSessions() {
        List<DungeonSession> sessions = new ArrayList<>();
        try (Connection connection = MapDungeons.getPlugin().sql.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                sessions.add(new DungeonSession(set.getString("id"), UUID.fromString(set.getString("owner")),
                        set.getString("map"),
                        Arrays.stream(set.getString("timestamps").split(";")).map(Long::parseLong).collect(Collectors.toList()),
                        set.getInt("kills")));
            }

            return sessions;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sessions;
    }

    public boolean exists(UUID uuid) {
        try (Connection connection = plugin.sql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET);
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean sessionExists(String s) {
        try (Connection connection = plugin.sql.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_SESSION);
            statement.setString(1, s);
            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void setMobKills(UUID uuid, int kills) {

    }

    public void saveSession(DungeonSession session) {
        if (!sessionExists(session.getId())) {
            try (Connection connection = plugin.sql.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_SESSION);
                statement.setString(1, session.getId());
                statement.setString(2, session.getOwner().toString());
                statement.setString(3, session.getMap());
                statement.setInt(4, session.getKills());
                statement.setString(5, session.getSerializedKillTimestamps());

                statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertPlayer(UUID uuid) {
        if (!exists(uuid)) {
            MapDungeons.logger().info("Could not find a profile for \"" + uuid + "\"! Creating one right now...");
            try (Connection connection = plugin.sql.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT);
                statement.setString(1, uuid.toString());
                statement.setInt(2, 0);
                statement.setInt(3, 0);

                statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `dungeons` (" +
            "`id` TEXT," +
            "`kills` INT(32)," +
            "`deaths` INT(32))";

    private final String CREATE_TABLE_SESSIONS = "CREATE TABLE IF NOT EXISTS `sessions` (" +
            "`id` TEXT," +
            "`owner` TEXT," +
            "`map` TEXT," +
            "`kills` INT(32)," +
            "`timestamps` TEXT)";

    private final String INSERT = "INSERT INTO `dungeons` (`id`, `kills`, `deaths`) VALUES (?, ?, ?)";
    private final String INSERT_SESSION = "INSERT INTO `sessions` (`id`, `owner`, `map`, `kills`, `timestamps`) VALUES (?, ?, ?)";
    private final String SET_MOB_KILLS = "UPDATE `dungeons` SET `kills`=? where `id`=?";
    private final String SET_DEATHS = "UPDATE `dungeons` SET `deaths`=? where `id`=?";
    private final String SET_SESSIONS = "UPDATE `dungeons` SET `sessions`=? where `id`=?";

    private final String GET = "SELECT * FROM `dungeons` WHERE `id`=?";
    private final String GET_SESSION = "SELECT * FROM `sessions` WHERE `id`=?";
    private final String GET_ALL = "SELECT * FROM `dungeons`";
}
