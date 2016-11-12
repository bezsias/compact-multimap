# compact-multimap

A [multimap] implementation which trades some CPU time in favour of compact memory representation.  
Works well for collecting large amount of "append-only" data in-memory. 

## Features

 * Low memory usage due to byte array representation in values.
 * Values are compressed automatically above a certain size.
 * Supports ordered list of values, duplicates are possible. 
 * Efficient put operations.
 * Single threaded.
 * Scala wrapper.
 
## Planned features

 * Specialized implementation for primitive values. (*in progress*)
 * Specialized implementation for efficient primitive key support.

## Missing features

 * Persistence support.
 * Set support in values.

## Known limitations

Get and remove operations are computationally more expensive compared to standard implementations, especially for large amount of values for a single key.

## Usage

```scala
import com.github.bezsias.multimap.scala._
val map = CompactMultiMap.objMap[String, String]()
map.put("a", "1");
map.put("a", "2");
map.put("b", "1");
map.put("a", "3");

map.get("a");
res4: List[String] = List(1, 2, 3)

map.get("b");
res5: List[String] = List(1)
```

## Copyright and License

Copyright © Bela Ezsias.

Distributed under [MIT license](http://choosealicense.com/licenses/mit/).

[multimap]: https://en.wikipedia.org/wiki/Multimap
