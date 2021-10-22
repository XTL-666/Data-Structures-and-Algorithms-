## RSA加密原理，解密原理

![img](https://img-blog.csdnimg.cn/20191008011532411.png)

![img](https://img-blog.csdnimg.cn/20191008011729411.png)

简单来说，密文是明文E次之后模上N

公钥中包含的E，N的信息组合

![img](https://img-blog.csdnimg.cn/201910080119101.png)

![img](https://img-blog.csdnimg.cn/2019100801193458.png)

同样的我们需要对密文进行D此幂之后模上N

所以私钥就是D，N的组合

密钥对（E，D，N）

求N：

N是两个互质数的乘积

N = p * q

L（过程数）

求L：

L 是 p－1 和 q－1的最小公倍数

L = lcm（p－1，q－1）

求E：

E必须满足两个条件：E是一个比1大比L小的数，E和L的最大公约数为1；

1 < E < L

gcd（E，L）=1

求D：

数D是由数E计算出来的，数D必须保证足够大

1 < D < L

E＊D mod L ＝ 1

| 求N  | N＝ p ＊ q ；p，q为质数                                 |
| ---- | ------------------------------------------------------- |
| 求L  | L＝lcm（p－1，q－1） ；L为p－1、q－1的最小公倍数        |
| 求E  | 1 < E < L，gcd（E，L）=1；E，L最大公约数为1（E和L互质） |
| 求D  | 1 < D < L，E＊D mod L ＝ 1                              |


### 事务的概念和特性？

概念：事务（Transaction）是一个操作序列，不可分割的工作单位，以BEGIN TRANSACTION开始，以ROLLBACK/COMMIT结束

特性（ACID）：

- **原子性**（Atomicity）：逻辑上是不可分割的操作单元，事务的所有操作要么全部提交成功，要么全部失败回滚（用回滚日志实现，反向执行日志中的操作）；
- **一致性**（Consistency）：事务的执行必须使数据库保持一致性状态。在一致性状态下，所有事务对一个数据的读取结果都是相同的；
- **隔离性**（Isolation）：一个事务所做的修改在最终提交以前，对其它事务是不可见的（并发执行的事务之间不能相互影响）；
- **持久性**（Durability）：一旦事务提交成功，对数据的修改是永久性的



事务的 ACID 特性概念简单，但不是很好理解，主要是因为这几个特性不是一种平级关系：

- 只有满足一致性，事务的执行结果才是正确的。

- 在无并发的情况下，事务串行执行，隔离性一定能够满足。此时只要能满足原子性，就一定能满足一致性。

- 在并发的情况下，多个事务并行执行，事务不仅要满足原子性，还需要满足隔离性，才能满足一致性。

- 事务满足持久化是为了能应对系统崩溃的情况。

  

  

  ![image-20210629160711163](C:\Users\Administrator.WIN-948T8Q810Q2\AppData\Roaming\Typora\typora-user-images\image-20210629160711163.png)



### 如何实现数据库特性？

### 原子性的实现

MySQL数据库事务的原子性是通过undo log实现的。

事务的所有修改操作(增、删、改)的相反操作都会写入undo log,比如事务执行了一条insert语句，那么undo log就会记录一条相应的delete语句。所以undo log是一个逻辑文件，记录的是相应的SQL语句一旦由于故障，导致事务无法成功提交，系统则会执行undo log中相应的撤销操作，达到事务回滚的目的。

### 持久性的实现

MySQl数据库事务的持久性是通过redo log实现的。

事务的所有修改操作(增、删、改)，数据库都会生成一条redo日志记录到redo log.区别于undo log记录SQL语句、redo log记录的是事务对数据库的哪个数据页做了什么修改，属于物理日志。

redo日志应用场景：数据库系统直接崩溃，需要进行恢复，一般数据库都会使用按时间点备份的策略，首先将数据库恢复到最近备份的时间点状态，之后读取该时间点之后的redo log记录，重新执行相应记录，达到最终恢复的目的。

日志文件的刷新策略

undo log和redo log并不是直接写到磁盘，而是写下入log buffer.再等待合适的时机同步到OS buffer,再由操作系统决定刷新到磁盘的时间。如图

MySQL主要由三种日志刷新策略，默认为第一种。三种策略，安全性依次下降，效率依次上升

    每次事务提交写入OS buffer,并调用fsync刷新到磁盘
    每秒写入OS buffer，并调用fsync刷新到磁盘
    每秒提交写入OS buffer,然后每秒调用fync刷新到磁盘

### 隔离性的实现

**未提交读**（Read Uncommited）

**提交读**（Read Commited）

InnoDB在 READ COMMITTED，使用排它锁,读取数据不加锁而是使用了MVCC机制。或者换句话说采用了读写分离机制。
但是该级别会产生不可重读以及幻读问题

##### 为什么提交读下会产生不可重复读

这跟 READ COMMITTED 级别下的MVCC机制有关系，在该隔离级别下每次 select的时候新生成一个版本号，所以每次select的时候读的不是一个副本而是不同的副本。

在每次select之间有其他事务更新了我们读取的数据并提交了，那就出现了不可重复读

**可重复读**（Repeatable Read）

采用读写锁实现



#### 为什么能重复读

只要没有释放读锁，再次读依然是读到第一次的数据

- 优点：实现起来简单
- 缺点：无法做到读写并行



采用MVCC实现：

#### 为什么能重复读

多次读只生成一个版本，读到的是相同的数据

- 优点：读写并行
- 缺点：实现的复杂度高



**可串行化**（Serializable）

强制事务串行执行，读写都只加排它锁，除了不会造成数据不一致之外，没有其他优点了



### 一致性的实现

从数据库层面，数据库通过原子性、隔离性、持久性来保证一致性。

从应用层面，通过代码判断数据库数据是否有效，然后决定回滚还是提交数据

### 什么是 MVCC？

多版本并发控制（Multi-Version Concurrency Control, MVCC），MVCC在每行记录后面都保存有两个隐藏的列，用来存储**创建版本号**和**删除版本号**。

- 创建版本号：创建一个数据行时的事务版本号（**事务版本号**：事务开始时的系统版本号；系统版本号：每开始一个新的事务，系统版本号就会自动递增）；

- 删除版本号：删除操作时的事务版本号；

- 各种操作：

  - 插入操作时，记录创建版本号；
  - 删除操作时，记录删除版本号；
  - 更新操作时，先记录删除版本号，再新增一行记录创建版本号；
  - 查询操作时，要符合以下条件才能被查询出来：删除版本号未定义或大于当前事务版本号（删除操作是在当前事务启动之后做的）；创建版本号小于或等于当前事务版本号（创建操作是事务完成或者在事务启动之前完成）

  

**MVCC带来的好处是？**
 多版本并发控制（MVCC）是一种用来解决`读-写冲突`的**无锁并发控制**，也就是为事务分配单向增长的时间戳，为每个修改保存一个版本，版本与事务时间戳关联，读操作只读该事务开始前的数据库的快照。 所以MVCC可以为数据库解决以下问题

- 在并发读写数据库时，可以做到在读操作时不用阻塞写操作，写操作也不用阻塞读操作，提高了数据库并发读写的性能
- 同时还可以解决脏读，幻读，不可重复读等事务隔离问题，但不能解决更新丢失问题

通过版本号减少了锁的争用，**提高了系统性能**；可以实现**提交读**和**可重复读**两种隔离级别，未提交读无需使用MVCC

**MVCC**在**MySQL InnoDB**中的实现主要是为了提高数据库并发性能，用更好的方式去处理读-写冲突，做到即使有读写冲突时，也能做到不加锁，非阻塞并发读



### 数据库的四种隔离级别？

- **未提交读**（Read Uncommited）：在一个事务提交之前，它的执行结果对其它事务也是可见的。会导致脏读、不可重复读、幻读；

- **提交读**（Read Commited）：一个事务只能看见已经提交的事务所作的改变。可避免脏读问题；

- **可重复读**（Repeatable Read）：可以确保同一个事务在多次读取同样的数据时得到相同的结果。（MySQL的默认隔离级别）。可避免不可重复读；

- **可串行化**（Serializable）：强制事务串行执行，使之不可能相互冲突，从而解决幻读问题。可能导致大量的超时现象和锁竞争，实际很少使用。

  



### AUTOCOMMIT

MySQL 默认采用自动提交模式。也就是说，如果不显式使用`START TRANSACTION`  语句来开始一个事务，那么每个查询操作都会被当做一个事务并自动提交。



### 会出现哪些并发一致性问题？

- **丢失修改**：一个事务对数据进行了修改，在事务提交之前，另一个事务对同一个数据进行了修改，覆盖了之前的修改；
- **脏读**（Dirty Read）：一个事务读取了被另一个事务修改、但未提交（进行了回滚）的数据，造成两个事务得到的数据不一致；
- **不可重复读**（Nonrepeatable Read）：在同一个事务中，某查询操作在一个时间读取某一行数据和之后一个时间读取该行数据，发现数据已经发生修改（针对**update**操作）；
- **幻读**（Phantom Read）：当同一查询多次执行时，由于其它事务在这个数据范围内执行了插入操作，会导致每次返回不同的结果集（和不可重复读的区别：针对的是一个数据整体/范围；并且针对**insert/delete**操作）



### 什么是锁？

锁是实现数据库并发控制的重要手段，可以保证数据库在多人同时操作时能够正常运行。 

### MySQL 中提供了几类锁？

MySQL 提供了全局锁、行级锁、表级锁。其中 InnoDB 支持表级锁和行级锁，MyISAM 只支持表级锁。

### 什么是死锁？

是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象,若无外力作用,它们都将无法推进下去。此时称系统处于死锁状态或系统产生了死锁，这些永远在互相等待的过程称为死锁。


### 如何处理死锁？

对待死锁常见的两种策略：

- 通过 innodblockwait_timeout 来设置超时时间，一直等待直到超时；
- 发起死锁检测，发现死锁之后，主动回滚死锁中的某一个事务，让其它事务继续执行。

### 如何查看死锁？

- 使用命令 show engine innodb status 查看最近的一次死锁。
- InnoDB Lock Monitor 打开锁监控，每 15s 输出一次日志。使用完毕后建议关闭，否则会影响数据库性能。

### 如何避免死锁？

为了在单个 InnoDB 表上执行多个并发写入操作时避免死锁，可以在事务开始时通过为预期要修改的每个元祖（行）使用 SELECT … FOR UPDATE 语句来获取必要的锁，即使这些行的更改语句是在之后才执行的。
在事务中，如果要更新记录，应该直接申请足够级别的锁，即排他锁，而不应先申请共享锁、更新时再申请排他锁，因为这时候当用户再申请排他锁时，其他事务可能又已经获得了相同记录的共享锁，从而造成锁冲突，甚至死锁
如果事务需要修改或锁定多个表，则应在每个事务中以相同的顺序使用加锁语句。在应用中，如果不同的程序会并发存取多个表，应尽量约定以相同的顺序来访问表，这样可以大大降低产生死锁的机会
通过 SELECT … LOCK IN SHARE MODE 获取行的读锁后，如果当前事务再需要对该记录进行更新操作，则很有可能造成死锁。
改变事务隔离级别。

### InnoDB 默认是如何对待死锁的？

InnoDB 默认是使用设置死锁时间来让死锁超时的策略，默认 innodblockwait_timeout 设置的时长是 50s。

### 如何开启死锁检测？

设置 innodbdeadlockdetect 设置为 on 可以主动检测死锁，在 Innodb 中这个值默认就是 on 开启的状态

### 什么是全局锁？它的应用场景有哪些？

全局锁就是对整个数据库实例加锁，它的典型使用场景就是做全库逻辑备份。 这个命令可以使整个库处于只读状态。使用该命令之后，数据更新语句、数据定义语句、更新类事务的提交语句等操作都会被阻塞。

###  使用全局锁会导致什么问题？

如果在主库备份，在备份期间不能更新，业务停摆，所以更新业务会处于等待状态。

如果在从库备份，在备份期间不能执行主库同步的 binlog，导致主从延迟。

###  如何处理逻辑备份时，整个数据库不能插入的情况？

逻辑备份工具 MySQLdump 来解决了这个问题，只需要在使用 MySQLdump 时，使用参数 -single-transaction 就会在导入数据之前启动一个事务来保证数据的一致性，并且这个过程是支持数据更新操作的。

### 共享锁和排他锁

- **排它锁**（Exclusive Lock）/ X锁：事务对数据加上X锁时，只允许此事务读取和修改此数据，并且其它事务不能对该数据加任何锁；

- 排他锁（X)：SELECT * FROM table_name WHERE ... FOR UPDATE。

  其他 session  可以查询该记录，但是不能对该记录加共享锁或排他锁，而是等待获得锁

- **共享锁**（Shared Lock）/ S锁：加了S锁后，该事务只能对数据进行读取而不能修改，并且其它事务只能加S锁，不能加X锁

- 共享锁（S）：SELECT * FROM table_name WHERE ... LOCK IN SHARE MODE。

  其他 session 仍然可以查询记录，并也可以对该记录加 share mode 的共享锁。但是如果当前事务需要对该记录进行更新操作，则很有可能造成死锁。 

  ### 如何设置数据库为全局只读锁？

  使用命令

      flush tables with read lock（简称 FTWRL）

  就可以实现设置数据库为全局只读锁。

  除了使用 FTWRL 外，还可以使用命令 set global readonly=true 设置数据库为只读。

  ### FTWRL 和 set global readonly=true 有什么区别？

  FTWRL 和 set global readonly=true 都是设置整个数据库为只读状态，但他们最大的区别就是，当执行 FTWRL 的客户端断开之后，整个数据库会取消只读，而 set global readonly=true 会一直让数据处于只读状态

### 什么是乐观锁和悲观锁？

- 悲观锁：认为数据随时会被修改，因此每次读取数据之前都会上锁，防止其它事务读取或修改数据；应用于**数据更新比较频繁**的场景；

- 乐观锁：操作数据时不会上锁，但是更新时会判断在此期间有没有别的事务更新这个数据，若被更新过，则失败重试；适用于读多写少的场景。

  乐观锁的实现方式有：

  - 加一个版本号或者时间戳字段，每次数据更新时同时更新这个字段；
  - 先读取想要更新的字段或者所有字段，更新的时候比较一下，只有字段没有变化才进行更新

### InnoDB 如何实现行锁？

行级锁是 MySQL 中粒度最小的一种锁，他能大大减少数据库操作的冲突。

INNODB 的行级锁有共享锁（S LOCK）和排他锁（X LOCK）两种。共享锁允许事物读一行记录，不允许任何线程对该行记录进行修改。排他锁允许当前事物删除或更新一行记录，其他线程不能操作该记录。

共享锁：SELECT … LOCK IN SHARE MODE，MySQL 会对查询结果集中每行都添加共享锁，前提是当前线程没有对该结果集中的任何行使用排他锁，否则申请会阻塞。

排他锁：select * from t where id=1 for update，其中 id 字段必须有索引，MySQL 会对查询结果集中每行都添加排他锁，在事物操作中，任何对记录的更新与删除操作会自动加上排他锁。前提是当前没有线程对该结果集中的任何行使用排他锁或共享锁，否则申请会阻塞



### 如何实现表锁？

MySQL 里标记锁有两种：表级锁、元数据锁（meta data lock）简称 MDL。表锁的语法是 lock tables t read/write。

可以用 unlock tables 主动释放锁，也可以在客户端断开的时候自动释放。lock tables 语法除了会限制别的线程的读写外，也限定了本线程接下来的操作对象。

对于 InnoDB 这种支持行锁的引擎，一般不使用 lock tables 命令来控制并发，毕竟锁住整个表的影响面还是太大。

MDL：不需要显式使用，在访问一个表的时候会被自动加上。

MDL 的作用：保证读写的正确性。

在对一个表做增删改查操作的时候，加 MDL 读锁；当要对表做结构变更操作的时候，加 MDL 写锁。

读锁之间不互斥，读写锁之间，写锁之间是互斥的，用来保证变更表结构操作的安全性。

MDL 会直到事务提交才会释放，在做表结构变更的时候，一定要小心不要导致锁住线上查询和更新。



### 如何优化锁？ 建议？

尽量使用较低的隔离级别。

精心设计索引， 并尽量使用索引访问数据， 使加锁更精确， 从而减少锁冲突的机会。

选择合理的事务大小，小事务发生锁冲突的几率也更小。

给记录集显示加锁时，最好一次性请求足够级别的锁。比如要修改数据的话，最好直接申请排他锁，而不是先申请共享锁，修改时再请求排他锁，这样容易产生死锁。

不同的程序访问一组表时，应尽量约定以相同的顺序访问各表，对一个表而言，尽可能以固定的顺序存取表中的行。这样可以大大减少死锁的机会。

尽量用相等条件访问数据，这样可以避免间隙锁对并发插入的影响。

不要申请超过实际需要的锁级别。

除非必须，查询时不要显示加锁。 MySQL 的 MVCC 可以实现事务中的查询不用加锁，优化事务性能；

MVCC 只在 COMMITTED READ（读提交）和 REPEATABLE READ（可重复读）两种隔离级别下工作。

对于一些特定的事务，可以使用表锁来提高处理速度或减少死锁的可能。



## 索引是什么

`MySQL`官方对索引的定义为：索引(Index)是帮助`MySQL`高效获取数据的数据结构。

`MySQL`中常用的索引在物理上分两类，B-树索引和哈希索引

### B+Tree索引

`B+Tree`是在`B-Tree`基础上的一种优化，使其更适合实现外存储索引结构。在B+Tree中，所有数据记录节点都是按照键值大小顺序存放在同一层的叶子节点上，而非叶子节点上只存储key值信息，这样可以大大加大每个节点存储的key值数量，降低B+Tree的高度。



![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly90dmExLnNpbmFpbWcuY24vbGFyZ2UvMDA3UzhaSWxseTFnaWg3Zm5laDl3ajMyNXMwa3F3aTYuanBn?x-oss-process=image/format,png)



B+Tree上通常有两个头指针，一个指向根节点，另一个指向关键字最小的叶子节点，而且所有叶子节点（即数据节点）之间是一种链式环结构。所以我们除了可以对B+Tree进行主键的范围查找和分页查找，还可以从根节点开始，进行随机查找。

数据库中的B+Tree索引可以分为聚集索引（clustered index）和辅助索引（secondary index）。

上面的B+Tree示例图在数据库中的实现即为聚集索引，聚集索引的B+Tree中的叶子节点存放的是整张表的行记录数据，辅助索引与聚集索引的区别在于辅助索引的叶子节点并不包含行记录的全部数据，而是存储相应行数据的聚集索引键，即主键。

当通过辅助索引来查询数据时，InnoDB存储引擎会遍历辅助索引找到主键，然后再通过主键在聚集索引中找到完整的行记录数据。



#### 1）为什么不采用二叉树呢：

原因：因为假设此刻用普通二叉树记录id系列，我们在每插入一行记录的同时还要维护二叉树索引字段。如果此时找id = 7这一行记录需要找7次，这跟扫描全表也没有什么大的区别。显而易见，二叉树对于这种依次递增的数据列，其实是不适合作为索引的数据结构。



#### 2）为什么不采用Hash表呢：

因为Hash索引不适用于范围查找。



#### 3）为什么不采用红黑树呢：

因为当MySQL数据量很大的时候，索引的体积也会很大，可能内存放不下，所以需要从磁盘上进行相关读写，如果树的层级太高了，则读写磁盘的次数（I/O的交互）就会越多，性能就会越差。红黑树目前唯一的不足点就是它的树的高度是不可控的。



#### 4）那为什么要用B+树，而不用B树呢：

B树 只适合随机检索，而B+树 同时支持随机检索和顺序检索；

B+树 空间利用率更高，可减少I/O次数，磁盘读写代价更低。 一般来说，索引本身也很大，不可能全部存储在内存中，因此索引往往以索引文件的形式存储的磁盘上。这样的话，索引查找过程中就要产生磁盘I/O消耗。B+树 的内部结点并没有指向关键字具体信息的指针，只是作为索引使用，其内部结点比B树小，盘块能容纳的结点中关键字数量更多，一次性读入内存中可以查找的关键字也就越多，相对的，IO读写次数也就降低了。而IO读写次数是影响索引检索效率的最大因素；

B+树的查询效率更加稳定。B树 搜索有可能会在非叶子结点结束，越靠近根节点的记录查找时间越短，只要找到关键字即可确定记录的存在，其性能等价于在关键字全集内做一次二分查找。而在B+树 中，顺序检索比较明显，随机检索时，任何关键字的查找都必须走一条从根节点到叶节点的路，所有关键字的查找路径长度相同，导致每一个关键字的查询效率相当；

B-树 在提高了磁盘IO性能的同时并没有解决元素遍历的效率低下的问题。B+树 的叶子节点使用指针顺序连接在一起，只要遍历叶子节点就可以实现整棵树的遍历。而且在数据库中基于范围的查询是非常频繁的，而B树 不支持这样的操作；

增删文件（节点）时，效率更高。因为B+树 的叶子节点包含所有关键字，并以有序的链表结构存储，这样可很好提高增删效率。

总结：

① B树 的单个结点存储的元素比B+树多，自然在整个过程中的磁盘I/O交互就会比 B+树 多，增加了性能开销；

② 相对B-tree来说，B+树所有的查询最终都会在叶子结点上，这也是B+树性能稳定的原因的一个体现；

③ B+树所有的叶子结点都是通过双向链表相连，范围查询非常方便，这也是B+树最明显的优势

### 哪些情况下索引会失效？

- 以“%(表示任意0个或多个字符)”开头的LIKE语句；
- OR语句前后没有同时使用索引；
- 数据类型出现隐式转化（如varchar不加单引号的话可能会自动转换为int型）；
- 对于多列索引，必须满足 **最左匹配原则**/最左前缀原则 (最左优先，eg：多列索引col1、col2和col3，则 索引生效的情形包括 col1或col1，col2或col1，col2，col3)；
- 如果MySQL估计全表扫描比索引快，则不使用索引（比如非常小的表）

### 使用索引的优点

- 大大加快了数据的**检索速度**；

- 可以显著减少查询中**分组和排序**的时间；

- 通过创建唯一性索引，可以保证数据库表中每一行数据的唯一性；

- 将随机 I/O 变为**顺序 I/O**（B+Tree 索引是有序的，会将相邻的数据都存储在一起）

  

#### 索引可以加快查询速度，提高 MySQL 的处理性能，但是过多地使用索引也会造成以下弊端：

1.创建索引和维护索引要耗费时间，这种时间随着数据量的增加而增加。

2.除了数据表占数据空间之外，每一个索引还要占一定的物理空间。如果要建立聚簇索引，那么需要的空间就会更大。

3.当对表中的数据进行增加、删除和修改的时候，索引也要动态地维护，这样就降低了数据的维护速度。

**索引可以在一些情况下加快查询，但在某些情况下，会降低效率**



### 在哪些地方适合创建索引？

1.在经常需要搜索的列上建立索引，可以加快搜索的速度。

2.在作为主键的列上创建索引，强制该列的唯一性，并组织表中数据的排列结构。

3.在经常使用表连接的列上创建索引，这些列主要是一些外键，可以加快表连接的速度。

4.在经常需要根据范围进行搜索的列上创建索引，因为索引已经排序，所以其指定的范围是连续的。

5.在经常需要排序的列上创建索引，因为索引已经排序，所以查询时可以利用索引的排序，加快排序查询。

6.在经常使用 WHERE 子句的列上创建索引，加快条件的判断速度。



##### 创建索引时需要注意什么？

- 只应建立在**小字段**上，而不要对大文本或图片建立索引（一页存储的数据越多一次IO操作获取的数据越大效率越高）；
- 建立索引的字段应该**非空**，在MySQL中，含有空值的列很难进行查询优化，因为它们使得索引、索引的统计信息以及比较运算更加复杂。应该用0、一个特殊的值或者一个空串代替NULL；
- 选择**数据密度大**（唯一值占总数的百分比很大）的字段作索引



### 索引的分类？

- 普通索引
- 唯一索引 UNIQUE：索引列的值必须唯一，但允许有空值；
- 主键索引 PRIMARY KEY：必须唯一，不允许空值（是一种特殊的唯一索引；MySQL创建主键时默认为聚集索引，但主键也可以是非聚集索引）；
- 单列索引和多列索引/复合索引（Composite）：索引的列数；
- 覆盖（Covering）索引：索引包含了所有满足查询所需要的数据，查询的时候只需要读取索引而不需要回表读取数据；
- 聚集（Clustered）索引/非聚集索引：对磁盘上存放数据的物理地址重新组织以使这些数据按照指定规则排序的一种索引（数据的物理排列顺序和索引排列顺序一致）。因此每张表只能创建一个聚集索引（因为要改变物理存储顺序）。优点是查询速度快，因为可以直接按照顺序得到需要数据的物理地址。缺点是进行修改的速度较慢。对于需要经常搜索范围的值很有效。非聚集索引只记录逻辑顺序，并不改变物理顺序；



### MySQL的两种存储引擎 InnoDB 和 MyISAM 的区别？

- InnoDB**支持事务**，可以进行Commit和Rollback；
- MyISAM 只支持表级锁，而 InnoDB 还**支持行级锁**，提高了并发操作的性能；
- InnoDB **支持外键**；
- MyISAM **崩溃**后发生损坏的概率比 InnoDB 高很多，而且**恢复的速度**也更慢；
- MyISAM 支持**压缩**表和空间数据索引，InnoDB需要更多的内存和存储；
- InnoDB 支持在线**热备份**

#### 应用场景

 **MyISAM** 管理非事务表。它提供高速存储和检索（MyISAM强调的是性能，每次查询具有原子性，其执行速度比InnoDB更快），以及全文搜索能力。如果表比较小，或者是只读数据（有大量的SELECT），还是可以使用MyISAM；

 **InnoDB** 支持事务，并发情况下有很好的性能，基本可以替代MyISAM

#### 热备份和冷备份

- 热备份：在数据库运行的情况下备份的方法。优点：可按表或用户备份，备份时数据库仍可使用，可恢复至任一时间点。但是不能出错
- 冷备份：数据库正常关闭后，将关键性文件复制到另一位置的备份方式。优点：操作简单快速，恢复简单



## 什么是存储过程？有哪些优缺点？



存储过程是事先经过编译并存储在数据库中的一段SQL语句的集合。想要实现相应的功能时，只需要调用这个存储过程就行了（类似于函数，输入具有输出参数）。

优点：

- 预先编译，而不需要每次运行时编译，提高了数据库执行**效率**；
- 封装了一系列操作，对于一些数据交互比较多的操作，相比于单独执行SQL语句，可以**减少网络通信量**；
- 具有**可复用性**，减少了数据库开发的工作量；
- **安全性高**，可以让没有权限的用户通过存储过程间接操作数据库；
- 更**易于维护**



缺点：

- **可移植性差**，存储过程将应用程序绑定到了数据库上；
- **开发调试复杂**：没有好的IDE；
- **修改复杂**，需要重新编译，有时还需要更新程序中的代码以更新调用



### Drop/Delete/Truncate的区别？

- **Delete**用来删除表的全部或者**部分数据**，执行delete之后，用户**需要提交**之后才会执行，会触发表上的DELETE**触发器**（包含一个OLD的虚拟表，可以只读访问被删除的数据），DELETE之后表结构还在，删除很慢，一行一行地删，因为会记录日志，可以利用日志还原数据；
- **Truncate**删除表中的所有数据，这个操作**不能回滚**，也不会触发这个表上的触发器。操作比DELETE快很多（直接把表drop掉，再创建一个新表，删除的数据不能找回）。如果表中有自增（AUTO_INCREMENT）列，则重置为1；
- **Drop**命令从数据库中**删除表**，所有的数据行，索引和约束都会被删除；不能回滚，不会触发触发器；



#### 触发器

触发器（TRIGGER）是由事件（比如INSERT/UPDATE/DELETE）来触发运行的操作（不能被直接调用，不能接收参数）。在数据库里以独立的对象存储，用于**保证数据完整性**（比如可以检验或转换数据）。



#### 有哪些约束类型

约束（Constraint）类型：主键（Primary Key）约束，唯一约束（Unique），检查约束，非空约束，外键（Foreign Key）约束。



### 什么是主从复制？实现原理是什么？



主从复制（Replication）是指数据可以从一个MySQL数据库主服务器复制到一个或多个从服务器，从服务器可以复制主服务器中的所有数据库或者特定的数据库，或者特定的表。默认采用异步模式。

实现原理：

- 主服务器 **binary log dump 线程**：将主服务器中的数据更改（增删改）日志写入 Binary log 中；
- 从服务器 **I/O 线程**：负责从主服务器读取binary log，并写入本地的 Relay log；
- 从服务器 **SQL 线程**：负责读取 Relay log，解析出主服务器已经执行的数据更改，并在从服务器中重新执行（Replay），保证主从数据的一致性



##### 为什么要主从复制？



- 读写分离：主服务器负责写，从服务器负责读
  - 缓解了锁的争用，即使主服务器中加了锁，依然可以进行读操作；
  - 从服务器可以使用 MyISAM，提升查询性能以及节约系统开销；
  - 增加冗余，提高可用性
- 数据实时备份，当系统中某个节点发生故障时，可以方便的故障切换
- 降低单个服务器磁盘I/O访问的频率，提高单个机器的I/O性能



### 什么是主从复制？

主从复制，是用来建立一个和主数据库完全一样的数据库环境，称为从数据库，主数据库一般是准实时的业务数据库。在mysql数据库中，支持单项、异步赋值。在赋值过程中，一个服务器充当主服务器，而另外一台服务器充当从服务器。此时主服务器会将更新信息写入到一个特定的二进制文件中。并会维护文件的一个索引用来跟踪日志循环。这个日志可以记录并发送到从服务器的更新中去。当一台从服务器连接到主服务器时，从服务器会通知主服务器从服务器的日志文件中读取最后一次成功更新的位置。然后从服务器会接收从哪个时刻起发生的任何更新，然后锁住并等到主服务器通知新的更新


### 主从复制的原理？

1.数据库有个bin-log二进制文件，记录了所有sql语句。

2.我们的目标就是把主数据库的bin-log文件的sql语句复制过来。

3.让其在从数据的relay-log重做日志文件中再执行一次这些sql语句即可。



### 说一下主从配置的过程

对于每一个主从复制的连接，都有三个线程。拥有多个从库的主库为每一个连接到主库的从库创建一个binlog输出线程，每一个从库都有它自己的I/O线程和SQL线程。

1.binlog输出线程:每当有从库连接到主库的时候，主库都会创建一个线程然后发送binlog内容到从库。在从库里，当复制开始的时候，从库就会创建两个线程进行处理：
2.从库I/O线程:当START SLAVE语句在从库开始执行之后，从库创建一个I/O线程，该线程连接到主库并请求主库发送binlog里面的更新记录到从库上。从库I/O线程读取主库的binlog输出线程发送的更新并拷贝这些更新到本地文件，其中包括relay log文件。

3.从库的SQL线程:从库创建一个SQL线程，这个线程读取从库I/O线程写到relay log的更新事件并执行。

![img](https://img-blog.csdnimg.cn/img_convert/5173adcaf99d2b037b0431745188ae7a.png)

#### 主从复制的好处

做数据的热备，作为后备数据库，主数据库服务器故障后，可切换到从数据库继续工作，避免数据丢失。
架构的扩展。业务量越来越大,I/O访问频率过高，单机无法满足，此时做多库的存储，降低磁盘I/O访问的评率，提高单个机器的I/O性能。
读写分离，使数据库能支持更大的并发。在报表中尤其重要。由于部分报表sql语句非常的慢，导致锁表，影响前台服务。如果前台使用master，报表使用slave，那么报表sql将不会造成前台锁，保证了前台速度。


好处一:实现服务器负载均衡

通过服务器复制功能，可以在主服务器和从服务器之间实现负载均衡。即可以通过在主服务器和从服务器之间切分处理客户查询的负荷，从而得到更好地客户相应时间。通常情况下，数据库管理员会有两种思路。

一是在主服务器上只实现数据的更新操作。包括数据记录的更新、删除、新建等等作业。而不关心数据的查询作业。数据库管理员将数据的查询请求全部 转发到从服务器中。这在某些应用中会比较有用。如某些应用，像基金净值预测的网站。其数据的更新都是有管理员更新的，即更新的用户比较少。而查询的用户数 量会非常的多。此时就可以设置一台主服务器，专门用来数据的更新。同时设置多台从服务器，用来负责用户信息的查询。将数据更新与查询分别放在不同的服务器 上进行，即可以提高数据的安全性，同时也缩短应用程序的响应时间、提高系统的性能。

二是在主服务器上与从服务器切分查询的作业。在这种思路下，主服务器不单单要完成数据的更新、删除、插入等作业，同时也需要负担一部分查询作 业。而从服务器的话，只负责数据的查询。当主服务器比较忙时，部分查询请求会自动发送到从服务器重，以降低主服务器的工作负荷。当然，像修改数据、插入数 据、删除数据等语句仍然会发送到主服务器中，以便主服务器和从服务器数据的同步。

 

好处二：通过复制实现数据的异地备份

可以定期的将数据从主服务器上复制到从服务器上，这无疑是先了数据的异地备份。在传统的备份体制下，是将数据备份在本地。此时备份 作业与数据库服务器运行在同一台设备上，当备份作业运行时就会影响到服务器的正常运行。有时候会明显的降低服务器的性能。同时，将备份数据存放在本地，也 不是很安全。如硬盘因为电压等原因被损坏或者服务器被失窃，此时由于备份文件仍然存放在硬盘上，数据库管理员无法使用备份文件来恢复数据。这显然会给企业 带来比较大的损失。

而如果使用复制来实现对数据的备份，就可以在从服务器上对数据进行备份。此时不仅不会干扰主服务气的正常运行，而且在备份过程中主服务器可以继 续处理相关的更新作业。同时在数据复制的同时，也实现了对数据的异地备份。除非主服务器和从服务器的两块硬盘同时损坏了，否则的话数据库管理员就可以在最 短时间内恢复数据，减少企业的由此带来的损失。

 

好处三：提高数据库系统的可用性

数据库复制功能实现了主服务器与从服务器之间数据的同步，增加了数据库系统的可用性。当主服务器出现问题时，数据库管理员可以马上让从服务器作为主服务器，用来数据的更新与查询服务。然后回过头来再仔细的检查主服务器的问题。此时一般数据库管理员也会采用两种手段。

一是主服务器故障之后，虽然从服务器取代了主服务器的位置，但是对于主服务器可以采取的操作仍然做了一些限制。如仍然只能够进行数据的查询，而 不能够进行数据的更新、删除等操作。这主要是从数据的安全性考虑。如现在一些银行系统的升级，在升级的过程中，只能够查询余额而不能够取钱。这是同样的道理。

二是从服务器真正变成了主服务器。当从服务器切换为主服务器之后，其地位完全与原先的主服务器相同。此时可以实现对数据的查询、更新、删除等操 作。为此就需要做好数据的安全性工作。即数据的安全策略，要与原先的主服务器完全相同。否则的话，就可能会留下一定的安全隐患。

#### 什么是视图

视图：从数据库的基本表中通过查询选取出来的数据组成的**虚拟表**（数据库中存放视图的定义）。可以对其进行增/删/改/查等操作。视图是对若干张基本表的引用，一张虚表，查询语句执行的结果，不存储具体的数据（基本表数据发生了改变，视图也会跟着改变）；可以跟基本表一样，进行增删改查操作(ps:增删改操作有条件限制)；如连表查询产生的视图无法进行，对视图的增删改会影响原表的数据。

##### 数据视图的优点

1.**数据库视图允许简化复杂查询**：数据库视图由与许多基础表相关联的SQL语句定义。您可以使用数据库视图来隐藏最终用户和外部应用程序的基础表的复杂性。通过数据库视图，您只需使用简单的SQL语句，而不是使用具有多个连接的复杂的SQL语句。

2.**数据库视图有助于限制对特定用户的数据访问**。 您可能不希望所有用户都可以查询敏感数据的子集。可以使用数据库视图将非敏感数据仅显示给特定用户组。

3.**数据库视图提供额外的安全层**。 安全是任何关系数据库管理系统的重要组成部分。 数据库视图为数据库管理系统提供了额外的安全性。 数据库视图允许您创建只读视图，以将只读数据公开给特定用户。 用户只能以只读视图检索数据，但无法更新。

4.**数据库视图启用计算列**。 数据库表不应该具有计算列，但数据库视图可以这样。 假设在orderDetails表中有quantityOrder(产品的数量)和priceEach(产品的价格)列。 但是，orderDetails表没有一个列用来存储订单的每个订单项的总销售额。如果有，数据库模式不是一个好的设计。 在这种情况下，您可以创建一个名为total的计算列，该列是quantityOrder和priceEach的乘积，以表示计算结果。当您从数据库视图中查询数据时，计算列的数据将随机计算产生。

5.**数据库视图实现向后兼容**。 假设你有一个中央数据库，许多应用程序正在使用它。 有一天，您决定重新设计数据库以适应新的业务需求。删除一些表并创建新的表，并且不希望更改影响其他应用程序。在这种情况下，可以创建与将要删除的旧表相同的模式的数据库视图。

##### 数据库视图的缺点

**性能**：从数据库视图查询数据可能会很慢，特别是如果视图是基于其他视图创建的。

**表依赖关系**：将根据数据库的基础表创建一个视图。每当更改与其相关联的表的结构时，都必须更改视图。

#### 视图能更新吗 

能，但不是所有视图都可以更新，遵循以下规则

（1）若视图的字段是来自字段表达式或常数，则不允许对此视图执行INSERT、UPDATE操作，允许执行DELETE操作

（2）若视图的字段是来自库函数，则此视图不允许更新

（3）若视图的定义中有GROUP BY子句或聚集函数时，则此视图不允许更新

（4）若视图的定义中有DISTINCT任选项，则此视图不允许更新

（5）若视图的定义中有嵌套查询，并且嵌套查询的FROM子句中涉及的表也是导出该视图的基表，则此视图不允		  许更新

（6）若视图是由两个以上的基表导出的，此视图不允许更新

（7）一个不允许更新的视图上定义的视图也不允许更新

### 什么是游标

游标（Cursor）：用于定位在查询返回的**结果集的特定行**，以对特定行进行操作。使用游标可以方便地对结果集进行移动遍历，根据需要滚动或对浏览/修改任意行中的数据。主要用于交互式应用。

### 游标的操作

**声明游标—>打开游标—>读取数据—>关闭游标—>删除游标**

1、声明游标——**Declare cursorname Cursor**

语法如下：

**declare cursor_name Corsor [ LOCAL | GLOBAL]  [ FORWARD_ONLY | SCROLL ] [ STATIC | KEYSET | DYNAMIC | FAST_FORWARD ] [ READ_ONLY | SCROLL_LOCKS | OPTIMISTIC ] [ TYPE_WARNING ]**

for

Slect查询的相关语句

上面的彩色字体表示的是，声明游标时的选项，不是必须提供的，每一个都代表游标的相关选项和特性

（1）**LOCAL**：对于在其中创建批处理、存储过程或触发器来说，该游标的作用域是局部的。GLOBAL：指定该游标的作用域是全局的

（2）**FORWARD_ONLY**:指定游标只能从第一行滚动到最后一行。否则默认为FORWARD_ONLY。SCROLL表示游标可以来回滚动

（3）**STATIC**：定义一个游标，以创建将又该游标使用的数据临时复本，对游标的所有请求都从tempdb中的这以临时表中不得到应答；因此，在对该游标进行提取操作时返回的数据中不反映对基表所做的修改，并且该游标不允许修改。

  KEYSET:指定当游标打开时，游标重的行的成员身份和顺序已经固定。对行进行唯一标识的键值内置在tempdb内一个称为keyset的表中。

 **DYNAMIC**:定义一个游标，以反映在滚动游标时对结果集内的各行所做的所有数据更改。行的数据值、顺序和成员身份在每次提取时都会更改，动态游标不支持ABSOLUTE提取选项。

 **FAST_FORWARD**：指定启动了性能优化的FORWARD_ONLY、READ_ONLY游标。如果指定了SCROLL或FOR_UPDATE,则不能指定FAST_FORWARD。

  **SCROLL_LOCKS**：指定通过游标进行的定位更新或删除一定会成功。将行读入游标时SQL Server将锁定这些行，以确保随后可对它们进行修改，如果还指定了FAST_FORWARD或STATIC，则不能指定SCROLL_LOCKS。

   **OPTIMISTIC**：指定如果行自读入游标以来已得到更新，则通过游标进行的定位更新或定位删除不成功。当将行读入游标时，SQL Server不锁定行，它改用timestamp列值比较结果来确定行读入游标后是否发生了修改，如果表不包含timestamp列，它改用校验和值进行确定，如果以修改该行，则尝试进行的定位更新或删除将失败，如果还指定了FAST_FORWARD，则不能指定OPTIMISTIC。

  **TYPE_WARNING**:指定游标从所请求的类型隐式转换为另一种类型时，向客户端发送警告消息。

2、打开游标——**Open Cursorname**

3、取出数据——**Fetch.........From**

**Fetch**   **[ NEXT | PRIOR | FIRST | LAST  | ABSOLUTE { n | @nvar }  | RELATIVE { n | @nvar }]     FROM Cursorname**            

**[ INTO @variable_name [ ,...n ] ]**

**NEXT**:紧跟当前行返回结果行，并且当前行递增为返回行，如果FETCH NEXT为对游标的第一次提取操作，则返回结果集中的第一行。NEXT为默认的游标提取选项。

**PRIOR**：返回紧邻当前行前面的结果行，并且当前行递减为返回行，如果FETCH PRIOR为对游标的第一次提取操作，则没有行返回并且游标置于第一行之前。

**FIRST**：返回游标中的第一行并将其作为当前行。

**LAST**:返回游标中的最后一行并将其作为当前行。

**ABSOLUTE { n | @nvar }**：如果n或@nvar为正，则返回从游标头开始向后n行的第n行，并将返回行变成新的当前行。如果n或@nvar为负，则返回从游标末尾开始向前的n行的第n行，并将返回行变成新的当前行。如果n或@nvar为0，则不返回行。n必须是整数常量，并且@nvar的数据类型必须为int、tinyint或smallint.

**RELATIVE { n | @nvar }**：如果n或@nvar为正，则返回从当前行开始向后的第n行。如果n或@nvar为负，则返回从当前行开始向前的第n行。如果n或@nvar为0，则返回当前行,对游标第一次提取时，如果在将n或@nvar设置为负数或0的情况下指定FETCH RELATIVE,则不返回行，n必须是整数常量，@nvar的数据类型必须是int、tinyint或smallint.

**GLOBAL**：指定cursor_name是全局游标。

**cursor_name**：已声明的游标的名称。如果全局游标和局部游标都使用cursor_name作为其名称，那么如果指定了GLOBAL，则cursor_name指的是全局游标，否则cursor_name指的是局部游标。

**@cursor_variable_name**：游标变量名，引用要从中进行提取操作的打开的游标。

**INTO @variable_name [ ,...n ]**：允许将提取操作的列数据放到局部变量中。列表中的各个变量从左到右与游标结果集中的相应列相关联。各变量的数据类型必须与相应的结果集列的数据类型相匹配，或是结果集列数据类型所支持的隐士转换。变量的数目必须与游标选择列表中的列数一致。

4、关闭游标——**Close Cursorname**

5、删除游标——**Deallocate Cursorname**

#### 操作实例

```sql
declare productcursor cursor scroll      --第一步：声明游标
for 
select * from productinfo where vendname='上海华测'
GO
open productcursor                       --第二步：打开游标
Go
--读取数据开始                            --第三步：获取数据
fetch next from productcursor --读取当前行的下一行，并使其置为当前行(刚开始时游标置于表头的前一行，即若表是从0开始的，游标最初置于-1处，所以第一次读取的是头一行)
fetch prior from productcursor --读取当前行的前一行，并使其置为当前行
fetch first from productcursor --读取游标的第一行，并使其置为当前行（不能用于只进游标）
fetch last from productcursor  --读取游标的最后一行，并使其置为当前行（不能用于只进游标）
fetch absolute 2 from productcursor --读取从游标头开始向后的第2行，并将读取的行作为新的行
fetch relative 3 from productcursor --读取从当前行开始向后的第3行，并将读取的行作为新的行
fetch relative-2 from productcursor --读取当前行的上两行，并将读取的行作为新的行
--读取数据结束
GO
close productcursor                      --第四步：关闭游标
Go
deallocate productcursor                 --第五步：删除游标
Go
```



### InnoDB 存储引擎有几种锁算法？分别介绍一下

MySQL InnoDB支持三种行锁定方式：

行锁（Record Lock）:锁直接加在索引记录上面，锁住的是key。

间隙锁（Gap Lock）:锁定索引记录间隙，确保索引记录的间隙不变。Gap Lock在InnoDB的唯一作用就是防止其他事务的插入操作，以此防止幻读的发生。

间隙锁是针对事务隔离级别为可重复读或以上级别而已的。

Next-Key Lock ：行锁和间隙锁组合起来就叫Next-Key Lock。

默认情况下，InnoDB工作在可重复读隔离级别下，并且会以Next-Key  Lock的方式对数据行进行加锁，这样可以有效防止幻读的发生。Next-Key  Lock是行锁和间隙锁的组合，当InnoDB扫描索引记录的时候，会首先对索引记录加上行锁（Record  Lock），再对索引记录两边的间隙加上间隙锁（Gap Lock）。加上间隙锁之后，其他事务就不能在这个间隙修改或者插入记录

### 数据库的范式？



- **第一范式**（1NF，Normal Form）：**属性不应该是可分的**。举例：如果将“电话”作为一个属性（一列），是不符合1NF的，因为电话这个属性可以分解为家庭电话和移动电话...如果将“移动电话”作为一个属性，就符合1NF；

- **第二范式**

  2NF：每个非主属性

  完全依赖于主属性集（候选键集）；

  - B完全依赖于A，就是说A中的所有属性唯一决定B，属性少了就不能唯一决定，属性多了则有冗余（叫依赖不叫完全依赖）。举例：（学号，课程名）这个主属性集可以唯一决定成绩，但是对于学生姓名这个属性，（学号，课程名）这个属性集就是冗余的，所以学生姓名不完全依赖于（学号，课程名）这一属性集；
  - 主属性集/候选码集：某一组属性能够唯一确定其它的属性（主键就是从候选键集中选的一个键），而其子集不能，这样的属性组中的属性就是主属性；不在候选码集中的属性成为非主属性；
  - 可以通过分解来满足 2NF：将（学号，课程名，成绩）做成一张表；（学号，学生姓名）做成另一张表，避免大量的数据冗余； 满足1NF后，要求表中的所有列，都必须依赖于主键，而不能有任何一列与主键没有关系，也就是说一个表只描述一件事情；

- **第三范式**

  3NF：在 2NF 的基础上，非主属性不传递依赖于主属性

  - 传递依赖：如果C依赖于B，B依赖于A，那么C传递依赖于A；
  - 3NF在2NF的基础上，消除了非主属性之间的依赖；比如一个表中，主属性有（学号），非主属性有（姓名，院系，院长名），可以看到院长名这个非主属性依赖于院系，传递依赖于学号。消除的办法是分解。 必须先满足第二范式（2NF），要求：表中的每一列只与主键直接相关而不是间接相关，（表中的每一列只能依赖于主键）；



### 不符合范式会出现哪些异常？

- 冗余数据：某些同样的数据多次出现（如学生姓名）
- 修改异常：修改了一个记录中的信息，另一个记录中相同的信息却没有修改
- 删除异常：删除一个信息，那么也会丢失其它信息（删除一个课程，丢失了一个学生的信息）
- 插入异常：无法插入（插入一个还没有课程信息的学生） 



### 常见的封锁类型？

意向锁是 InnoDB 自动加的， 不需用户干预。 

对于 UPDATE、 DELETE 和 INSERT 语句， InnoDB 会自动给涉及数据集加排他锁（X)；



对于普通 SELECT 语句，InnoDB 不会加任何锁;

事务可以通过以下语句显式给记录集加共享锁或排他锁： 



共享锁（S）：SELECT * FROM table_name WHERE ... LOCK IN SHARE MODE。

其他 session 仍然可以查询记录，并也可以对该记录加 share mode 的共享锁。但是如果当前事务需要对该记录进行更新操作，则很有可能造成死锁。 

排他锁（X)：SELECT * FROM table_name WHERE ... FOR UPDATE。

其他 session  可以查询该记录，但是不能对该记录加共享锁或排他锁，而是等待获得锁



- **排它锁**（Exclusive Lock）/ X锁：事务对数据加上X锁时，只允许此事务读取和修改此数据，并且其它事务不能对该数据加任何锁；

  

- **共享锁**（Shared Lock）/ S锁：加了S锁后，该事务只能对数据进行读取而不能修改，并且其它事务只能加S锁，不能加X锁

  

- **意向锁**（Intention Locks）：

  - 一个事务在获得某个**数据行**对象的 S 锁之前，必须先获得**整个表**的 IS 锁或更强的锁；
  - 一个事务在获得某个数据行对象的 X 锁之前，必须先获得整个表的 IX 锁；
  - IS/IX 锁之间都是兼容的；
  - 好处：如果一个事务想要对整个表加X锁，就需要先检测是否有其它事务对该表或者该表中的某一行加了锁，这种检测非常耗时。有了意向锁之后，只需要检测整个表是否存在IX/IS/X/S锁就行了

锁的作用：用于管理对共享资源的并发访问，保证数据库的完整性和一致性



### 封锁粒度

MySQL 中提供了两种封锁粒度：**行级锁**以及**表级锁**。

封锁粒度小：

- 好处：锁定的数据量越少，发生锁争用的可能就越小，系统的**并发程度**就越高；
- 坏处：**系统开销**大（加锁、释放锁、检查锁的状态都需要消耗资源）

MySQL加锁

```sql
SELECT ... LOCK In SHARE MODE;
SELECT ... FOR UPDATE;
```



### 什么是三级封锁协议？

- 一级封锁协议：事务在修改数据之前必须先对其加X锁，直到事务结束才释放。可以解决丢失修改问题（两个事务不能同时对一个数据加X锁，避免了修改被覆盖）；

  

- 二级封锁协议：在一级的基础上，事务在读取数据之前必须先加S锁，读完后释放。可以解决脏读问题（如果已经有事务在修改数据，就意味着已经加了X锁，此时想要读取数据的事务并不能加S锁，也就无法进行读取，避免了读取脏数据）；

  

- 三级封锁协议：在二级的基础上，事务在读取数据之前必须先加S锁，直到事务结束才能释放。可以解决不可重复读问题（避免了在事务结束前其它事务对数据加X锁进行修改，保证了事务期间数据不会被其它事务更新）

  

  ### 什么是两段锁协议？

  事务必须严格分为两个阶段对数据进行**加锁和解锁**的操作，第一阶段加锁，第二阶段解锁。也就是说一个事务中一旦释放了锁，就不能再申请新锁了。

  **可串行化调度**是指，通过并发控制，使得并发执行的事务结果与某个串行执行的事务结果相同。事务遵循两段锁协议是保证可串行化调度的充分条件。

  



### 快照读



使用 MVCC 读取的是快照中的数据，这样可以减少加锁所带来的开销：

```sql
select * from table ...;
```



当前读读取的是最新的数据，需要加锁。以下第一个语句需要加 S 锁，其它都需要加 X 锁：

```sql
select * from table where ? lock in share mode;
select * from table where ? for update;
insert;
update;
delete;
```



### 列举几种表连接方式？

[![SQL连接](https://github.com/wolverinn/Waking-Up/raw/master/_v_images/20191207081711185_20242.png)](https://github.com/wolverinn/Waking-Up/blob/master/_v_images/20191207081711185_20242.png)



- 内连接（Inner Join）：仅将两个表中满足连接条件的行组合起来作为结果集
  - 自然连接：只考虑属性相同的元组对；
  - 等值连接：给定条件进行查询
- 外连接（Outer Join）
  - 左连接：左边表的所有数据都有显示出来，右边的表数据只显示共同有的那部分，没有对应的部分补NULL；
  - 右连接：和左连接相反；
  - 全外连接（Full Outer Join）：查询出左表和右表所有数据，但是去除两表的重复数据
- 交叉连接（Cross Join）：返回两表的笛卡尔积（对于所含数据分别为m、n的表，返回m*n的结果）


SSL/TLS是一个安全通信框架，上面可以承载HTTP协议或者SMTP/POP3协议等。
TLS协议的架构

TLS主要分为两层，底层的是TLS记录协议，主要负责使用对称密码对消息进行加密。

上层的是TLS握手协议，主要分为握手协议，密码规格变更协议和应用数据协议4个部分。

    握手协议负责在客户端和服务器端商定密码算法和共享密钥，包括证书认证，是4个协议中最最复杂的部分。

    密码规格变更协议负责向通信对象传达变更密码方式的信号

    警告协议负责在发生错误的时候将错误传达给对方

    应用数据协议负责将TLS承载的应用数据传达给通信对象的协议。

分割子数组异或和为零的最大值

```java
public static int mostEOR(int[] arr){
	int ans = 0;
	int[] dp = new int[arr.length];
	int xor = 0;
	HashMap<Integer,Integer> map = new HashMap<>();
	map.put(0,-1);
	for(int i = 0; i < arr.length; i++){
		xor ^= arr[i];
		if(map.containsKey(xor)){
			int pre = map.get(xor);
 			dp[i] = pre == -1 ? 1 : dp[pre] + 1;
		}
		map.put(xor,i);
		if(i > 0) dp[i] = Math.max(dp[i - 1],dp[i]);
		ans = max(ans,dp[i]);
	}
	return ans;
}

### get于post的区别

# Data-Structures-and-Algorithms-
Data Structures and Algorithms 
### 用setTimeout实现setInterval（）
setInterval(fun,time)：间隔time就执行fun函数一次，重复性的。
setTimeout(fun,time):当过了time时间后，执行fun函数一次，非重复性的，只执行一次。


## 150. Evaluate Reverse Polish Notation

Evaluate the value of an arithmetic expression in [Reverse Polish Notation](http://en.wikipedia.org/wiki/Reverse_Polish_notation).

Valid operators are `+`, `-`, `*`, and `/`. Each operand may be an integer or another expression.

**Note** that division between two integers should truncate toward zero.

It is guaranteed that the given RPN expression is always valid. That  means the expression would always evaluate to a result, and there will  not be any division by zero operation.

**Example 1:**

```
Input: tokens = ["2","1","+","3","*"]
Output: 9
Explanation: ((2 + 1) * 3) = 9
```

**Example 2:**

```
Input: tokens = ["4","13","5","/","+"]
Output: 6
Explanation: (4 + (13 / 5)) = 6
```

**Example 3:**

```
Input: tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
Output: 22
Explanation: ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
= ((10 * (6 / (12 * -11))) + 17) + 5
= ((10 * (6 / -132)) + 17) + 5
= ((10 * 0) + 17) + 5
= (0 + 17) + 5
= 17 + 5
= 22
```

```c++
class Solution{
public:
	int evalRPN(vector<string>& tokens) {
		unordered_map<string,function<int <int,int>> map = {
			{"+",[](int a,int b) {return a + b}},
			{"-",[](int a,int b) {return a - b}},
			{"*",[](int a,int b) {return a * b}},
			{"/",[](int a,int b) {return a / b}}
		};
		stack<int> s1;
		for (auto i : tokens) {
			if(!map.count(i)) {
				s1.push(stoi(i));
			}else {
				int op1 = s1.top();
				s1.pop();
				int op2 = s1.top();
				s1.pop();
				s1.push(map[i](op2,op1));
			}
		}
		return s1.top();
	}
}
```

# 并查集

并查集（Union-find Sets）是一种非常精巧而实用的数据结构，它主要用于处理一些*不相交集合*的合并问题。一些常见的用途有求连通子图、求最小生成树的 Kruskal 算法和求最近公共祖先（Least Common Ancestors, LCA）等。



它有三个哈希表

元素值表:主要用来包装值对应的元素

儿子-父亲表  判断是否为同一集合

rankMap 每个父所对应的rank大小



我们先定义包装值所对应的元素

```java
public class UnionFind {

   public static class Element<V> {
       public V value;
       public Element(V value) {
           this.value = value;
       }
   }
}
```



去定义这三个表

把表装入已有的元素

```java
public static class UionFindSet<V> {
    public HashMap<V,Element<V>> elementMap;
    public HashMap<Element<V>,Element<V>> fatherMap;
    public HashMap<Element<V>,Integer> rankMap;
    
    public UionFindSet(List<V> list) {
    	elementMap = new HashMap<>();
    	fatherMap = new HashMap<>();
    	rankMap = new HashMap<>();
    	
    	for(V value : List) {
    	    Element<V> element = new Element<V>(value);
            elementMap.put(value,element);
            fatherMap.put(element,element);
            rankMap.put(element,1);
    	}
    }
}
```

它有三个重要的函数

findHead： 找到头节点，并且更新它，让路径让的每一个元素都指向头节点

isSameSet ：判断是不是一个集合直接判断是不是头节点就行了

Uion接下来讲

在findHead中我们需要维护一个栈用来记录沿途的元素，以后便于之后连接上他们的头节点

```java
        private Element<V> findHead(Element<V> element) {
            Stack<Element<V>> path = new Stack<>();
            while(element != fatherMap.get(element)) {
                path.push(element);
                element = fatherMap.get(element);
            }
            while(!path.isEmpty()) {
                fatehrMap.put(path.pop(),element);
            }
            return element;
        }
```



接下俩就很简单了 

我们让同一集合元素拥有一个头节点，那么我们只需要确定他们的头节点是否相同就可以了

```
        public boolean isSameSet(V a,V b) {
            if(elementMap.containsKey(a) && elementMap.containsKey(b)) {
                return findHead(elementMap.get(a)) == findHead(elementMap.get(b));
            }
            return false;
        }
```

接下来是Uion操作

在查询是否为统一集合与合并集合之前，我们应该确保他们的值对应元素集合表中的元素



```java
        public void union(V a,V b) {
            if(elementMap.containsKey(a)&&elementMap.containsKey(b)){
                Element<V> aF =  findHead(elementMap.get(a));
                Element<V> bF =  findHead(elementMap.get(b));
                
                //如果aF == bF,我们就可以确定他们的头节点是同一个，那么属于同一集合
                
                if(aF != bF){
                    Element<V> big = rankMap.get(aF) > rankMap.get(bF) ? aF : bF;
                    Element<V> small = big == aF ? bF : aF; //将aF与bF中较小的赋予small
                    fatherMap.put(small,big);
                    rankMap.put(big,rankMap.get(aF) + rankMap.get(bF));
                    rankMap.remove(small);
                } 
            }
        }
```



# 随机池

保存用户给的值，并给标上序号；

需要提供以下操作

insert

getrandom 随机返回流中的任意一个值

删除

定义随机池

```java
public static class Pool<k> {
   
   private HashMap<k,Integer> map1;
   private HashMap<Integer,k> map2;
   private int size;
   
   public Pool() {
      this.map1 = new HashMap<k, Integer>();
      this.map2 = new HashMap<Integer, k>();
      this.size = 0;
   }
}
```

定义插入操作

```
public void insert(k key) {
   if(!this.map1.containsKey(key)) {
   //检查是否存在该元素，如果不存在，再将其加入两个表中
      this.map1.put(key,this.size);
      this.map2.put(this.size++,key);
   }
}
```

定义删除操作

```java
public void delete(k key) {
			if(this.map1.containsKey(key)) {
				int deleteIndex = this.map1.get(key);
				int lastIndex = --this.size;
				k lastKey = this.map2.get(lastIndex);
				this.map1.put(lastKey,deleteIndex);
				this.map2.put(deleteIndex,lastKey);
				this.map1.remove(key);
				this.map2.remove(this.size);
			}
		}
```

定义得到随机数操作

```java
public k getRandom() {
			if(this.size == 0){
				return null;
			}
			int random = (int)(Math.random() * this.size);// 0 ~ this.size
			return this.map2.get(random);
		}
```

完整代码

```java
package class01;

import java.util.HashMap;

public class Code02_RandomPool {

	public static class Pool<k> {

		private HashMap<k,Integer> map1;
		private HashMap<Integer,k> map2;
		private int size;

		public Pool() {
			this.map1 = new HashMap<k, Integer>();
			this.map2 = new HashMap<Integer, k>();
			this.size = 0;
		}

		public void insert(k key) {
			if(!this.map1.containsKey(key)) {
				this.map1.put(key,this.size);
				this.map2.put(this.size++,key);
			}
		}

		public k getRandom() {
			if(this.size == 0){
				return null;
			}
			int random = (int)(Math.random() * this.size);// 0 ~ this.size
			return this.map2.get(random);
		}

		public void delete(k key) {
			if(this.map1.containsKey(key)) {
				int deleteIndex = this.map1.get(key);
				int lastIndex = --this.size;
				k lastKey = this.map2.get(lastIndex);
				this.map1.put(lastKey,deleteIndex);
				this.map2.put(deleteIndex,lastKey);
				this.map1.remove(key);
				this.map2.remove(this.size);
			}
		}
	}

	public static void main(String[] args) {
		Pool<String> pool = new Pool<String>();
		pool.insert("夏");
		pool.insert("天");
		pool.insert("学NM自动化");
		pool.delete("夏");
		for(int i = 0 ;i < 100;i++) {
			System.out.println(pool.getRandom());
			System.out.println(pool.getRandom());
		}
	}

}
```

## 堆排序

按照堆的特点可以把堆分为**大顶堆**和**小顶堆**

大顶堆：每个结点的值都**大于**或**等于**其左右孩子结点的值

小顶堆：每个结点的值都**小于**或**等于**其左右孩子结点的值

给出的是数组形式，但我们用完全二叉树的序号也可以表示它。

```java
public class Code03_HeapSort {

	public static void heapSort(int[] arr) {
		
		if (arr == null || arr.length < 2) {
			return;
		}

		for (int i = 0; i < arr.length; i++) {
			heapInsert(arr, i);
		}

		int size = arr.length;
		swap(arr,0,size--);
		while(size > 0) {
			heapify(arr,0,size);
			swap(arr,0,--size);
		}
	}

	public static void heapInsert(int[] arr,int index) {
		while(arr[index] > arr[(index -1) / 2]) {
			swap(arr,index,(index - 1)/2);
			index = (index - 1)/2;
		}
	}
	
	public static void heapfiy(int[] arr,int index,int size) {
		int left = index * 2 + 1;
		while (left < size ) {
			int larest = left + 1 < size && arr[index + 1] > arr[left] ? left + 1 : left;
			larest = arr[larest] > arr[index] ? larest : index;
			if (largest == index) {
				break;
			}
			swap(arr,largest,index);
			index = largest;
			left = index * 2 + 1;
		}
	}
```

定义swap函数

```java
	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
```

用对数器检验一下策略的正确性

```java
public static void comparator(int[] arr) {
	Arrays.sort(arr);
}
public static int[] generateRandomArray(int maxSize,int maxValue) {
	int[] arr = new int[(int)((maxSize + 1)*Math.random())];
	for (int i = 0; i < arr.length ; i++) {
		arr[i]  = (int)((maxvalue + 1) *Math.random()) - (int) (maxValue * Math.random());
	}
	return arr;
}

public static int[] copyArray(int[] arr) {
	if(arr == null) {
		return null; 
	}
	int[] res = new int[arr.length];
	for (int i = 0 ; i < arr.length;i++) {
		res[i] = arr[i];
	}
	return res;
}
	public static boolean isEqual(int[] arr1, int[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			heapSort(arr1);
			comparator(arr2);
			if (!isEqual(arr1, arr2)) {
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");

		int[] arr = generateRandomArray(maxSize, maxValue);
		printArray(arr);
		heapSort(arr);
		printArray(arr);
	}
```

这个在java中其实已经给定义好了

```
PriorityQueue<Integer> heap = new PriorityQueue<>();
```

问题：已知一个几乎有序的数组，几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离可以不超过k，并且k相对于数组来说比较小。请选择一个合适的排序算法针对这个数据进行排序

显然用堆排序很好解决它

```java
public void sortedArrDistanceLessK(int[] arr, int k) {
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int index = 0;
		for (; index < Math.min(arr.length, k); index++) {
			heap.add(arr[index]);
		}
		int i = 0;
		for (; index < arr.length; i++, index++) {
			heap.add(arr[index]);
			arr[i] = heap.poll();
		}
		while (!heap.isEmpty()) {
			arr[i++] = heap.poll();
		}
	}
```

时间复杂度O（N*logk）

当k较小时，我们可以认为时间复杂度接近O（n）



- #### 堆与栈的区别

（1）**管理方式不同**。栈由操作系统自动分配释放，无需我们手动控制；堆的申请和释放工作由程序员控制，容易产生内存泄漏；

（2）**空间大小不同**。每个进程拥有的栈的大小要远远小于堆的大小。理论上，程序员可申请的堆大小为虚拟内存的大小，进程栈的大小 64bits 的 Windows 默认 1MB，64bits 的 Linux 默认 10MB；

（3）**生长方向不同**。堆的生长方向向上，内存地址由低到高；栈的生长方向向下，内存地址由高到低。

（4）**分配方式不同**。堆都是动态分配的，没有静态分配的堆。栈有2种分配方式：静态分配和动态分配。静态分配是由操作系统完成的，比如局部变量的分配。动态分配由alloca函数进行分配，但是栈的动态分配和堆是不同的，他的动态分配是由操作系统进行释放，无需我们手工实现。

（5）**分配效率不同**。栈由操作系统自动分配，会在硬件层级对栈提供支持：分配专门的寄存器存放栈的地址，压栈出栈都有专门的指令执行，这就决定了栈的效率比较高。堆则是由C/C++提供的库函数或运算符来完成申请与管理，实现机制较为复杂，频繁的内存申请容易产生内存碎片。显然，堆的效率比栈要低得多。

（6）**存放内容不同**。栈存放的内容，函数返回地址、相关参数、局部变量和寄存器内容等。当主函数调用另外一个函数的时候，要对当前函数执行断点进行保存，需要使用栈来实现，首先入栈的是主函数下一条语句的地址，即扩展指针寄存器的内容（EIP），然后是当前栈帧的底部地址，即扩展基址指针寄存器内容（EBP），再然后是被调函数的实参等，一般情况下是按照从右向左的顺序入栈，之后是被调函数的局部变量，注意静态变量是存放在数据段或者BSS段，是不入栈的。出栈的顺序正好相反，最终栈顶指向主函数下一条语句的地址，主程序又从该地址开始执行。堆，一般情况堆顶使用一个字节的空间来存放堆的大小，而堆中具体存放内容是由程序员来填充的。

从以上可以看到，堆和栈相比，由于大量malloc()/free()或new/delete的使用，容易造成大量的内存碎片，并且可能引发用户态和核心态的切换，效率较低。栈相比于堆，在程序中应用较为广泛，最常见的是函数的调用过程由栈来实现，函数返回地址、EBP、实参和局部变量都采用栈的方式存放。虽然栈有众多的好处，但是由于和堆相比不是那么灵活，有时候分配大量的内存空间，主要还是用堆。



## 动态规划

给定一个有序数组arr，代表数轴上从左到右有n个点arr[0]、arr[1]...arr[n－1]， 给定一个正数L，代表一根长度为L的绳子，求绳子最多能覆盖其中的几个点。

小虎去附近的商店买苹果，奸诈的商贩使用了捆绑交易，只提供6个每袋和8个 每袋的包装包装不可拆分。可是小虎现在只想购买恰好n个苹果，小虎想购买尽 量少的袋数方便携带。如果不能购买恰好n个苹果，小虎将不会购买。输入一个 整数n，表示小虎想购买的个苹果，返回最小使用多少袋子。如果无论如何都不 能正好装下，返回-1。
