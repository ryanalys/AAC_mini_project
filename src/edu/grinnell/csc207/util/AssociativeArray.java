package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Alyssa Ryan
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  public int size;

  /**
   * The array of key/value pairs.
   */
  public KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newArr = new AssociativeArray<K, V>();
    newArr.pairs = java.util.Arrays.copyOf(newArr.pairs, this.size);
    KVPair<K, V> current = new KVPair<K, V>();
    for (int i = 0; i < this.size; i++) {
      current = this.pairs[i].clone();
      try {
        newArr.set(current.key, current.val);
      } catch (NullKeyException e) {
        //deals with the possibility of a null key
      } //try/catch
    } //for
    return newArr;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String output = "";
    if (this.size == 0) {
      return "{}";
    } else {
      for (int i = 0; i < this.size; i++) {
        output = output + pairs[i].toString();
      } //for
    } //if
    return output;
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param key
   *   The key whose value we are seeting.
   * @param value
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    } else if (hasKey(key)) {
      try {
        int index = find(key);
        pairs[index].val = value;
      } catch (Exception e) {
        throw new NullKeyException();
      } //try/catch
    } else {
      this.pairs = java.util.Arrays.copyOf(this.pairs, this.size + 1);
      KVPair<K, V> newPair = new KVPair<K, V>(key, value);
      pairs[this.size] = newPair;
      size++;
    } //if/else if/else
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @return
   *   The corresponding value
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    if (!hasKey(key)) {
      throw new KeyNotFoundException();
    } //if
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return this.pairs[i].val;
      } //if
    } //for
    throw new KeyNotFoundException();
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key, since it cannot appear.
   *
   * @param key
   *   The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return true;
      } //if
    } //for
    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   *
   * @param key
   *   The key to remove.
   */
  public void remove(K key) {
    for (int i = 0; i < this.size; i++) {
      if (pairs[i].key.equals(key)) {
        for (int j = 0; j < this.size - 1; j++) {
          pairs[j].key = pairs[j + 1].key;
          pairs[j].val = pairs[j + 1].val;
        } //for
        size--;
        this.pairs = java.util.Arrays.copyOf(this.pairs, size);
      } //if
    } //for
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  /**
   * Returns the key from the element at index
   * @param index The index from which to get the key
   * @return The key from the element at index
   */
  public K returnKey(int index){
    return pairs[index].key;
  }

  /**
   * Returns the value from the element at index
   * @param index The index from which to get the value
   * @return The value from the element at index
   */
  public V returnVal(int index){
    return pairs[index].val;
  }

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @return
   *   The index of the key, if found.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   */
  public int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      } //if
    } //for
    throw new KeyNotFoundException();
  } // find(K)
} // class AssociativeArray
