package prog09;
import java.util.Random;

public class SortTest<E extends Comparable<E>> {
  public void test (Sorter<E> sorter, E[] array) {
    E[] copy = array.clone();
    long start = System.nanoTime();
    sorter.sort(copy);
    long end = System.nanoTime();
    double time = (double)((end - start)/1000.0);
    
    if (array.length < 100){
    	System.out.println(sorter);
    for (int i = 0; i < copy.length; i++)
      System.out.print(copy[i] + " ");
    }
    System.out.println();
    System.out.println("Took " + time + " microseconds");
  }
  
  public static void main (String[] args) {
    Integer[] array = { 3, 1, 4, 1, 5, 9, 2, 6 };

    if (args.length > 0) {
      // Print out command line argument if there is one.
      System.out.println("args[0] = " + args[0]);

      // Create a random object to call random.nextInt() on.
      Random random = new Random(0);

      // Make array.length equal to args[0] and fill it with random
      Integer[] randArray = new Integer[Integer.parseInt(args[0])];
      // integers:
      for (int i = 0; i < randArray.length; i++) {
          randArray[i] = random.nextInt();
      }
      SortTest<Integer> tester = new SortTest<Integer>();
      //tester.test(new InsertionSort<Integer>(), randArray);
      /*Times for Insertion sort:
       * n= 1000:  8427.285 microseconds
       * n= 10000:  211820.208 microseconds
       * n= 100000:  1.7062884766E7 microseconds
       * n= 1000000: I terminated it after 20 minutes
       * n= 10000000: I terminated it after 20 minutes
       */
      //tester.test(new HeapSort<Integer>(), randArray);
      /*Times for Heap sort:
       * n= 1000: 1793.222 microseconds
       * n= 10000: 16372.197 microseconds
       * n= 100000: 66731.211 microseconds
       * n= 1000000: 872839.795 microseconds
       * n= 10000000: 1.4372748281E7 microseconds
       */
      //tester.test(new QuickSort<Integer>(), randArray);
      /*Times for Quick sort:
       * n= 1000: 2431.824 microseconds
       * n= 10000: 12704.218 microseconds
       * n= 100000: 100528.73 microseconds
       * n= 1000000: 437301.969 microseconds
       * n= 10000000: 6154849.366 microseconds
       */
      tester.test(new MergeSort<Integer>(), randArray);
      /*Times for Merge sort:
       * n= 1000: 1701.422 microseconds
       * n= 10000: 6461.299 microseconds
       * n= 100000: 85665.804 microseconds
       * n= 1000000: 615113.691 microseconds
       * n= 10000000: 6272618.679 microseconds
       */

    } else {

    SortTest<Integer> tester = new SortTest<Integer>();
    tester.test(new InsertionSort<Integer>(), array);
     tester.test(new HeapSort<Integer>(), array);
     tester.test(new QuickSort<Integer>(), array);
    tester.test(new MergeSort<Integer>(), array);
    }
  }
}

class InsertionSort<E extends Comparable<E>>
  implements Sorter<E> {
  public void sort (E[] array) {
    for (int dataIndex = 0; dataIndex < array.length; dataIndex++) {
      E data = array[dataIndex];
      int i = dataIndex;
      // while array[i-1] > data move array[i-1] to array[i] and
      // decrement i
      while (i > 0 && array[i-1].compareTo(data) > 0) {
    	  array[i] = array[i-1];
    	  i--;
      }
      array[i] = data;
    }
  }
}

class HeapSort<E extends Comparable<E>>
  implements Sorter<E> {
  
  private E[] array;
  
  private void swap (int i, int j) {
    E data = array[i];
    array[i] = array[j];
    array[j] = data;
  }
  
  public void sort (E[] array) {
    this.array = array;
    
    for (int i = parent(array.length - 1); i >= 0; i--)
      swapDown(i, array.length - 1);
    
    for (int n = array.length - 1; n >= 0; n--) {
      swap(0, n);
      swapDown(0, n - 1);
    }
  }
  
  public void swapDown (int root, int end) {
    // Calculate the left child of root.
	  int leftChild = left(root);
	  int rightChild;
	  int biggerChild;
	  
	  while (leftChild <= end) {
		  rightChild = right(root);
		  if (rightChild <= end) {
			  if (array[leftChild].compareTo(array[rightChild]) < 0) {
				  biggerChild = rightChild;
			  } else {
				  biggerChild = leftChild;
			  }
		  } else {
			  biggerChild = leftChild;
		  }
		  if (array[root].compareTo(array[biggerChild]) >= 0) {
			  return;
		  }
		  swap(root, biggerChild);
		  root = biggerChild;
		  leftChild = left(root);
	  }

	// while the left child is still in the array
	//   calculate the right child
	//   if the right child is in the array and 
    //      it is bigger than than the left child
	//     bigger child is right child
    //   else
	//     bigger child is left child
    //   if the root is not less than the bigger child
	//     return
    //   swap the root with the bigger child
	//   update root and calculate left child
  }
  
  private int left (int i) { return 2 * i + 1; }
  private int right (int i) { return 2 * i + 2; }
  private int parent (int i) { return (i - 1) / 2; }
}

class QuickSort<E extends Comparable<E>>
  implements Sorter<E> {
  
  private E[] array;
  private void swap (int i, int j) {
    E data = array[i];
    array[i] = array[j];
    array[j] = data;
  }
  
  public void sort (E[] array) {
    this.array = array;
    sort(0, array.length-1);
  }
  
  private void sort(int left, int right) {
    if (left >= right)
      return;
    
    swap(left, (left + right) / 2);
    
    E pivot = array[left];
    int a = left + 1;
    int b = right;
    while (a <= b) {
      // Move a forward if array[a] <= pivot
    	if (array[a].compareTo(pivot) <= 0){
    		a++;
    	}
      // Move b backward if array[b] > pivot
    	if (array[b].compareTo(pivot) > 0) {
    		b--;
    	}
      // Otherwise swap array[a] and array[b]
    	if (a <= b) {
    		swap (a,b);
    	}
    }
    
    swap(left, b);
    
    sort(left, b-1);
    sort(b+1, right);
  }
}

class MergeSort<E extends Comparable<E>>
  implements Sorter<E> {
  
  private E[] array, array2;
  
  public void sort (E[] array) {
    this.array = array;
    array2 = array.clone();
    sort(0, array.length-1);
  }
  
  private void sort(int left, int right) {
    if (left >= right)
      return;
    
    int middle = (left + right) / 2;
    sort(left, middle);
    sort(middle+1, right);
    
    int i = left;
    int a = left;
    int b = middle+1;
    while (a <= middle || b <= right) {
      // If both a <= middle and b <= right
    	if (a <= middle && b <= right) {
            if (array[a].compareTo(array[b]) <= 0) {
                array2[i++] = array[a++];
            } else {
                array2[i++] = array[b++];
            }
    	} else if (b <= right) {
    		array2[i++] = array[b++];
    	} else if (a <= middle) {
    		array2[i++] = array[a++];
    	}
      // copy the smaller of array[a] or array[b] to array2[i]
      // Otherwise just copy the remaining elements to array2
    }
    
    System.arraycopy(array2, left, array, left, right - left + 1);
  }
}
