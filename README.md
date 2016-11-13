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

#### Java

```java
import com.github.bezsias.multimap.*;
import java.util.*;

MultiMap<String, String> map = CompactMultiMap.objMultiMap(8);
map.put("a", "1");
map.put("a", "2");
map.put("b", "1");
map.put("a", "3");

List<String> values = map.get("a");
```

#### Scala

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

## Installation

You can get compact-multimap from [maven central].

#### Maven
```
<dependency>
    <groupId>com.github.bezsias</groupId>
    <artifactId>compact-multimap_2.11</artifactId>
    <version>0.8.1</version>
</dependency>
```

#### Sbt

```
libraryDependencies += "com.github.bezsias" % "compact-multimap_2.11" % "0.8.1"
```

## License

Distributed under [MIT license](http://choosealicense.com/licenses/mit/).

[multimap]: https://en.wikipedia.org/wiki/Multimap
[maven central]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22compact-multimap_2.11%22
