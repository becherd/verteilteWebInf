package de.tum.benchmark;

/**
 * The base class for benchmark tests
 *
 * @author Hannes Dorfmann
 */
public abstract class Benchmark<T> {

  private long insertTime;
  private long queryTime;
  private long deleteTime;
  private long cleanUpTime;
  private long setupTime;
  private String name;

  public Benchmark(String name) {
    this.name = name;
  }

  /**
   * Executes the insert benchmark
   */
  public abstract void insert(T items) throws Exception;

  /**
   * Executes the query benchmark
   */
  public abstract void query(T items) throws Exception;

  /**
   * Executes the delete benchmark
   */
  public abstract void delete(T items) throws Exception;

  /**
   * Do some cleanup. Will be called once after all benchmarks tests has been run successfully.
   *
   * @throws Exception
   */
  public abstract void cleanUp(T items) throws Exception;

  /**
   * Do some setup staff, will be called once before the benchmark starts
   *
   * @throws Exception
   */
  public abstract void setup(T items) throws Exception;

  /**
   * Executes the benchmark
   *
   * @throws Exception
   */
  public Benchmark<T> start(T items) throws Exception {

    // Benchmark setup
    long startTime = System.currentTimeMillis();
    setup(items);
    setupTime = System.currentTimeMillis() - startTime;

    // Benchmark inserting
    startTime = System.currentTimeMillis();
    insert(items);
    insertTime = System.currentTimeMillis() - startTime;

    // Benchmark quering
    startTime = System.currentTimeMillis();
    query(items);
    queryTime = System.currentTimeMillis() - startTime;

    // Benchmark deleting
    startTime = System.currentTimeMillis();
    delete(items);
    deleteTime = System.currentTimeMillis() - startTime;

    // Benchmarking cleanup
    startTime = System.currentTimeMillis();
    cleanUp(items);
    cleanUpTime = System.currentTimeMillis() - startTime;

    // print the Results
    printResults();

    return this;
  }

  /**
   * Print the benchmark results
   */
  public void printResults() {
    System.out.println("--- " + name + " ---");
    System.out.println("Insert:  " + insertTime + " ms");
    System.out.println("Query:   " + queryTime + " ms");
    System.out.println("Delete:  " + deleteTime + " ms");
    System.out.println(".................");
    System.out.println("Setup:   " + setupTime + " ms");
    System.out.println("Cleanup: " + cleanUpTime + " ms\n\n");
  }
}
