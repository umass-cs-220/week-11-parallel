# Week 11: Parallel Patterns

## Overview

Today more than ever it is important to consider how applications can
exploit parallel execution. This week we will be looking at a few
patterns in parallel programming. In particular, we will look at the
basics of *threads* and how to execute code in parallel using multiple
threads. As part of that we will touch on concurrency issues that
result from data that can be shared by multiple coordinating
threads. We will also look at asynchronous programming techniques
using the `Future` library provided by the Scala libraries. This is an
important concept and has dominated the implementation and most modern
frameworks today. We will see how to use futures to make coffee and
see how it is used in a network server to implement a student
assignment from another course. Lastly, we will touch on the *actor*
model originally made famous by the Erlang language and now
implemented in one of the most powerful (and popular) libraries for
implementing concurrent and distributed systems, namely, Akka.

*To get access to this material [download][zip] the zip archive.*

[zip]: https://github.com/umass-cs-220/week-11-parallel/archive/master.zip

## Readings

* Chapter 32, Odersky (Only the first few sections as this library is
  now deprecated)
* [Welcome to the Future](http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html)
* [Promises and Futures in Practice](http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html)
* [The Actor Approach to Concurrency](http://danielwestheide.com/blog/2013/02/27/the-neophytes-guide-to-scala-part-14-the-actor-approach-to-concurrency.html)

## Resources

* [Akka](http://akka.io)
* [Futures and Promises](https://en.wikipedia.org/wiki/Futures_and_promises), Wikipedia
* [Threads](https://en.wikipedia.org/wiki/Thread_(computing)), Wikipedia
* [Erlang Programming Language](https://en.wikipedia.org/wiki/Erlang_(programming_language))
* [Actor Model](https://en.wikipedia.org/wiki/Actor_model)
