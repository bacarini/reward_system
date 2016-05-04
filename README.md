# Reward system
It is a reward system that will give a customer points for each confirmed invitation they played a part into.

The solution is based on [Sparse matrix](https://en.wikipedia.org/wiki/Sparse_matrix) and [directed graph](https://en.wikipedia.org/wiki/Directed_graph).
___

## Prerequisites

You will need Leiningen 2 installed. The code was developed & tested with these versions:

```
> lein --version
Leiningen 2.5.3 on Java 1.8.0_60 Java HotSpot(TM) 64-Bit Server VM
```
___
## Running

To start a web server for the application, run:
```
lein ring server
```

#### Home
You can invite yours friends by input one by one or send a file with multi invitation at the same time.

![](resources/private/home.png)

#### Error
In case of your file isn't format correct an error might shows up. Don't worry, you can fix and try it again.

![](resources/private/error.png)

#### Confirmation of your invite
All good? So now, you can go back where you were and invite more of your friend or see are ranking.

![](resources/private/invitation.png)

#### Ranking
![](resources/private/ranking.png)

#### Reset
If you would like to reset all the counting, you also can reset the data.

___
## Testing

```
> lein test

lein test reward_system.core_test

lein test reward_system.handler_test

lein test reward_system.matrix_test

Ran 3 tests containing 53 assertions.
0 failures, 0 errors.
```
