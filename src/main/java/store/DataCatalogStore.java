package store;

import core.WorkerTaskResult;
import core.config.ProfilerConfig;

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

    @Override
    public boolean storeDocument(WorkerTaskResult wtr) {
        /*
        long id,
        String dbName,
        String path,
        String sourceName,
        String columnName,
        String dataType,
        int totalValues,
        int uniqueValues,
        String entities,
        long[] minhash
         */

        System.out.println(wtr.getSourceName());
        System.out.println(wtr.getColumnName());
        System.out.println(wtr.getDataType());


        System.out.println("========");
        return false;
    }

    @Override
    public void tearDownStore() {

    }
}
