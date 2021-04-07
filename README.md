## Data Station Aurum-Based Profiler

This Aurum-based profiler implementation extracts signatures from data sources,
which the `Data Station` context are interpreted as `WHAT-profiles`.

From a user's perspective, the following configurations & operations are supported.
* Data sources to be profiled - metadata extraction. Sources can be either char-separated flat files, or JDBC databases.
* Data Catalog API to be used for storing the extracted profiles (See `DataCatalogStore` class)
  
After the profiling phase has been completed, the network builder can be used to extract and store the relationships betwen data assets;
these relationships are then stored to the data catalog

* Data Catalog API to store the relationships graph between resulting Data Assets (also available as a binary `networkx` object )

### Example Walkthrough
As shown in the main `Makefile`

* `make profile-chemball-mini.yml`
* `make store-relationship-graph-chemball-mini`
* `curl ... localhost:5000/profiles/`

### Example
```yaml
api_version: 0

# In sources we include as many data sources as desired
sources:

  # Include a source for each data source to configure

  # name to identify each source, e.g., migration_database, production, lake, etc.
  - name: "csv_chemball_mini"
    # indicate the type of source, one of [csv, postgres, mysql, oracle10g, oracle11g]
    type: csv
    config:
      # path indicates where the CSV files live
      path: "/Users/flo/TheDataStation/data/chemball-mini"    # separator used in the CSV files
      separator: ';'
      catalog_endpoint: 'http://localhost:5000'
```