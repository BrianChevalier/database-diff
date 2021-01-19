# Database Diff Homework Assignment

## Assumptions

The following assumption is made:

* The datasets will always be able to fit in memory


## Usage

Run the following to start the old database in a docker container:

``` sh
make db/old
```

Run the following to start the new database in a docker container:

``` sh
make db/new
```

Run the following to diff the two databases. The output will be written to `diff.edn`. Output will be a sequence of maps with the account id and the associated status `:missing`, `:new`, or `:corrupt`

``` sh
make db/diff
```

## Testing

To run the tests:

``` sh
make test
```
