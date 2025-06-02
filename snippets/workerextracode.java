package worker;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.sql.*;
import org.json.JSONObject;
import java.util.List;

class Worker {
  public static void main(String[] args) {
    try {
      JedisPoolConfig poolConfig = new JedisPoolConfig();
      JedisPool jedisPool = connectToRedis("redis", poolConfig);
      Connection dbConn = connectToDB("db");
      
      createVotesTable(dbConn);

      System.err.println("Watching vote queue");

      while (true) {
        try (Jedis redis = jedisPool.getResource()) {
          List<String> voteData = redis.blpop(0, "votes");
          String voteJSON = voteData.get(1);
          JSONObject voteObj = new JSONObject(voteJSON);
          String voterID = voteObj.getString("voter_id");
          String vote = voteObj.getString("vote");

          System.err.printf("Processing vote for '%s' by '%s'\n", vote, voterID);
          updateVote(dbConn, voterID, vote);
        } catch (Exception e) {
          System.err.println("Error processing vote: " + e.getMessage());
          e.printStackTrace();
        }
      }
    } catch (SQLException e) {
      System.err.println("Database connection error: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  static void createVotesTable(Connection dbConn) throws SQLException {
    try (Statement st = dbConn.createStatement()) {
      st.executeUpdate(
        "CREATE TABLE IF NOT EXISTS votes (id VARCHAR(255) PRIMARY KEY, vote VARCHAR(255) NOT NULL)");
      System.err.println("Ensured that 'votes' table exists");
    }
  }

  static void updateVote(Connection dbConn, String voterID, String vote) throws SQLException {
    String checkSQL = "SELECT vote FROM votes WHERE id = ?";
    String upsertSQL = "INSERT INTO votes (id, vote) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET vote = EXCLUDED.vote";
    
    try (PreparedStatement checkStmt = dbConn.prepareStatement(checkSQL);
         PreparedStatement upsertStmt = dbConn.prepareStatement(upsertSQL)) {
      
      checkStmt.setString(1, voterID);
      try (ResultSet rs = checkStmt.executeQuery()) {
        if (rs.next()) {
          String existingVote = rs.getString("vote");
          if (!existingVote.equals(vote)) {
            System.err.printf("Changing vote for voter %s from %s to %s\n", voterID, existingVote, vote);
          } else {
            System.err.printf("Vote for voter %s remains unchanged (%s)\n", voterID, vote);
          }
        } else {
          System.err.printf("New vote from voter %s for %s\n", voterID, vote);
        }
      }
      
      upsertStmt.setString(1, voterID);
      upsertStmt.setString(2, vote);
      int affectedRows = upsertStmt.executeUpdate();
      System.err.printf("Upserted vote. Rows affected: %d\n", affectedRows);
    }
  }

  static JedisPool connectToRedis(String host, JedisPoolConfig poolConfig) {
    JedisPool pool = new JedisPool(poolConfig, host, 6379);

    try (Jedis jedis = pool.getResource()) {
      jedis.ping();
      System.err.println("Connected to Redis");
    } catch (JedisConnectionException e) {
      System.err.println("Waiting for Redis");
      sleep(1000);
      return connectToRedis(host, poolConfig);
    }

    return pool;
  }

  static Connection connectToDB(String host) throws SQLException {
    Connection conn = null;

    try {
      Class.forName("org.postgresql.Driver");
      String url = "jdbc:postgresql://" + host + "/postgres";

      while (conn == null) {
        try {
          conn = DriverManager.getConnection(url, "postgres", "postgres");
        } catch (SQLException e) {
          System.err.println("Waiting for db");
          sleep(1000);
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.err.println("Connected to db");
    return conn;
  }

  static void sleep(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      System.exit(1);
    }
  }
static class FizzBuzz {
    public static void generate(int limit) {
      for (int i = 1; i <= limit; i++) {
        if (i % 3 == 0 && i % 5 == 0) {
          System.out.println("FizzBuzz");
        } else if (i % 3 == 0) {
          System.out.println("Fizz");
        } else if (i % 5 == 0) {
          System.out.println("Buzz");
        } else {
          System.out.println(i);
        }
      }
    }

    // Example of how it could be called, not part of the main application flow
    public static void main(String[] args) {
        System.out.println("FizzBuzz demonstration:");
        generate(20); // Generate FizzBuzz up to 20
    }
  }
}

