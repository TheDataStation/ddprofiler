package store;

public enum StoreType {
  NULL(0),
  ELASTIC_HTTP(1),
  ELASTIC_NATIVE(2),
  DATA_CATALOG_HTTP(3);

  private int type;

  StoreType(int type) { this.type = type; }

  public int ofType() { return type; }
}
