# How to use
Upload the `links.data` file into your hadoops file system with

```
hadoop fs -put links.data /somewhere/you/want
```


Run the jar on hadoop with Hits as main class and the file path to links.data as commandline argument. As second (optional) argument you can specify the number of iterations (if not specified, default is 5)


