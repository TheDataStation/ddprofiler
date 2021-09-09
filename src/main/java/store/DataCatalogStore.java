package store;

import core.WorkerTaskResult;
import core.config.ProfilerConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.common.xcontent.XContentBuilder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataCatalogStore implements Store {

    String storeUrl;

    public DataCatalogStore(ProfilerConfig pc) {
        String storeServer = pc.getString(ProfilerConfig.STORE_SERVER);
        int storeHttpPort = pc.getInt(ProfilerConfig.STORE_HTTP_PORT);
        this.storeUrl = "http://" + storeServer + ":" + storeHttpPort;
    }

    @Override
    public void initStore() {
        //TBD necessary for catalog ?
        //System.out.println("Initializing store");
    }

    @Override
    public boolean indexData(long id, String dbName, String path, String sourceName, String columnName, List<String> values) {
        //System.out.println(this.storeUrl + " " + id + " " + dbName + " " + path + " " + sourceName + " " + columnName);
        return false;
    }


    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + System.getProperty("user.home") + "/catalog.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    @Override
    public boolean storeDocument(WorkerTaskResult wtr) {
        String strId = Long.toString(wtr.getId());

        XContentBuilder builder = null;
        try {
            builder = jsonBuilder()
                    .startObject()
                    .field("item_id", wtr.getId())
                    .field("what_profile_id", wtr.getId())
                    .field("asset_id", wtr.getPath() + '.' + wtr.getColumnName())
                    .startObject("what_profile_schema")
                    .field("path", wtr.getPath())
                    .field("sourceName", wtr.getSourceName())
                    .field("columnNameNA", wtr.getColumnName())
                    .field("columnName", wtr.getColumnName())
                    .field("dataType", wtr.getDataType())
                    .field("totalValues", wtr.getTotalValues())
                    .field("uniqueValues", wtr.getUniqueValues())
                    .field("entities", wtr.getEntities().toString())
                    .startArray("minhash");

            long[] mh = wtr.getMH();
            if (mh != null) { // that's an integer column
                for (long i : mh) {
                    builder.value(i);
                }
            } else {
                builder.value(-1);
            }

            builder.endArray()

                    .field("minValue", wtr.getMinValue())
                    .field("maxValue", wtr.getMaxValue())
                    .field("avgValue", wtr.getAvgValue())
                    .field("median", wtr.getMedian())
                    .field("iqr", wtr.getIQR())
                    .endObject()
                    .endObject();

            String jsonPayload = builder.string();
            //System.out.println(builder.string());

            // WRITE DIRECTLY TO CATALOG DBs

            String sql = "INSERT INTO what_profile(item_id, what_profile_id, version, timestamp, asset_id, what_profile_schema, user_item_id) " +
                    "VALUES(?,?,?,?,?,?,?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, wtr.toString());
                pstmt.setString(2, wtr.toString());
                pstmt.setString(3, "0.1");
                pstmt.setString(4, "");
                pstmt.setString(5, wtr.getPath() + '.' + wtr.getColumnName());
                pstmt.setString(6, jsonPayload);
                pstmt.setString(7, null);


                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }


//            // WRITE VIA HTTP
//            URL url = new URL(this.storeUrl + "/profile/what");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/json; utf-8");
//            con.setRequestProperty("Accept", "application/json");
//            con.setDoOutput(true);
//            try (OutputStream os = con.getOutputStream()) {
//                byte[] input = jsonPayload.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            try (BufferedReader br = new BufferedReader(
//                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine = null;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                System.out.println(response.toString());
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
        return true;
    }

    @Override
    public void tearDownStore() {

    }
}
