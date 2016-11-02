# Special Configurations

- Nomad config file path (a.k.a `closp.edn`) can be set by `CLOSP_CONFIG_PATH` environment variable.
- Data directory (data-dir) should be set in `closp.edn` file; Otherwise when running in OpenShift is retrieves the value of `OPENSHIFT_DATA_DIR` environment variable. (and data-dir will be DIR/public)
- Media directory could be set separately from data dir. If left alone, it's assumed to be in data-dir/media (and data-dir could be set from everywhere)