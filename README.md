# compact-multimap

A [multimap] implementation which trades some CPU time in favour of compact memory representation.   
Works well for collecting large amount of "append-only" data in-memory. 

## Details

It basically represents a Map[K, List[V]] as a Map[K, Array[Byte]]. So when a new item is put into the map it gets serialized 
and appended to a byte array. When a user defined byte size is reached (8 kB by default) the array gets zipped and stored separately. 
This separate compation allows better memory footprint while keeping the original array at a constant size therefore the time of insertion 
won't degrade by the number of elements in the list. This in-memory storage is optimized for minimal memory footprint and fast instertion.
For retrieval operations like get, remove and contains it is necessary to unzip and deserialize the byte array in order to access 
the actual objects so those operations are expected to be slower than standard multimap implementations.  
So choose wisely according to your constraints/usage patterns.

## Features

 * Low memory usage due to byte array value list representation.
 * Values are compressed automatically above a certain size.
 * Supports ordered list of values, duplicates are possible. 
 * Specialized implementation for primitive values.
 * Support for custom inner map for efficient primitive key storage ([fastutil], [Koloboke]). 
 * Efficient put operations.
 * Single threaded.
 * Scala wrapper.
 
## Missing features

 * Persistence support.
 * Set support in values.

## Known limitations

Get and remove operations are computationally more expensive compared to standard implementations, especially for large amount of values for a single key.

## Usage

#### Java

```java
import com.github.bezsias.multimap.*;
import java.util.*;

MultiMap<String, String> map = new MultiMapBuilder<String>().objectMap();
map.put("a", "1");
map.put("a", "2");
map.put("b", "1");
map.put("a", "3");

List<String> values = map.get("a");
```

#### Scala

```scala
import com.github.bezsias.multimap.scala._

val map = CompactMultiMap.objectMap[String, String]()
map.put("a", "1");
map.put("a", "2");
map.put("b", "1");
map.put("a", "3");

map.get("a");
res4: List[String] = List(1, 2, 3)

map.get("b");
res5: List[String] = List(1)
```

## Installation

You can get compact-multimap from [maven central].

#### Maven
```
<dependency>
    <groupId>com.github.bezsias</groupId>
    <artifactId>compact-multimap_2.11</artifactId>
    <version>0.13.0</version>
</dependency>
```

#### Sbt

```
libraryDependencies += "com.github.bezsias" %% "compact-multimap" % "0.13.0"
```

## License

Distributed under [MIT license](http://choosealicense.com/licenses/mit/).

[multimap]: https://en.wikipedia.org/wiki/Multimap
[maven central]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22compact-multimap_2.11%22
[Koloboke]: https://github.com/leventov/Koloboke
[fastutil]: https://github.com/vigna/fastutil
