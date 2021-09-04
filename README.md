
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
		map.put(0,-1);
		if(i > 0) dp[i] = Math.max(dp[i - 1],dp[i]);
		ans = max(ans,dp[i]);
	}
	return ans;
}
```


```



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
