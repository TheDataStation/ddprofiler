package store;

import core.config.ProfilerConfig;

public class StoreFactory {

    public static Store makeHttpElasticStore(ProfilerConfig pc) {
        return new HttpElasticStore(pc);
    }

    public static Store makeNativeElasticStore(ProfilerConfig pc) {
        return new NativeElasticStore(pc);
    }

    public static Store makeDataCatalogStore(ProfilerConfig pc) {
        return new DataCatalogStore(pc);
    }

    public static Store makeNullStore(ProfilerConfig pc) {
        return new NullStore();
    }

    public static Store makeStoreOfType(int type, ProfilerConfig pc) {
        Store s = null;
        if (type == StoreType.NULL.ofType()) {
            s = StoreFactory.makeNullStore(pc);
        } else if (type == StoreType.ELASTIC_HTTP.ofType()) {
            s = StoreFactory.makeHttpElasticStore(pc);
        } else if (type == StoreType.ELASTIC_NATIVE.ofType()) {
            s = StoreFactory.makeNativeElasticStore(pc);
        } else if (type == StoreType.DATA_CATALOG_HTTP.ofType()) {
            s = StoreFactory.makeDataCatalogStore(pc);
        }

        return s;
    }
}
