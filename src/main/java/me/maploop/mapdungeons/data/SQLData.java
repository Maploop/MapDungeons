package me.maploop.mapdungeons.data;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.session.DungeonSession;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SQLData
{
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
                connection.prepareStatement(CREATE_TABLE);
                connection.prepareStatement(CREATE_TABLE_SESSIONS);
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
        return 0;
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

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `dungeons` (" +
            "`id` TEXT," +
            "`owner` TEXT," +
            "`map` TEXT," +
            "`kills` INT(32)," +
            "`sessions` TEXT," +
            "`deaths` INT(32))";

    private final String CREATE_TABLE_SESSIONS = "CREATE TABLE IF NOT EXISTS `sessions` (" +
            "`id` TEXT," +
            "`kills` INT(32)," +
            "`timestamps` TEXT)";

    private final String INSERT = "INSERT INTO `dungeons` (`id`, `kills`, `sessions`, `deaths`) VALUES (?, ?, ?, ?)";
    private final String SET_MOB_KILLS = "UPDATE `dungeons` SET `kills`=? where `id`=?";
    private final String SET_DEATHS = "UPDATE `dungeons` SET `deaths`=? where `id`=?";
    private final String SET_SESSIONS = "UPDATE `dungeons` SET `sessions`=? where `id`=?";

    private final String GET = "SELECT * FROM `dungeons` WHERE `id`=?";
    private final String GET_ALL = "SELECT * FROM `dungeons`";
}
