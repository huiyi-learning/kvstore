# kvstore #

CSDI project. Aims to build a key-value in-memory database that distinguishes hot/cold values and compresses cold values to save memory space.

## package ##

Go to project's home and type
```
mvn package
```
After package, there's a kvstore-1.0.zip in target directory. Unzip it.
```
kvstore-1.0
    --bin (shell exectables)
    --conf (configuration files)
    --lib (code in jars)
    README.md
```

## execute ##

go to bin folder and run run.sh