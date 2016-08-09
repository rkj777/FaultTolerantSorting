import java.util.Random;

public class HeapSort {

	private int[] array;
	private int length;
	private int accessCount;
	private double primaryFailureProb;

	public HeapSort(int[] unsortedArray, double primaryFailureProb){
		array = unsortedArray;
		length = array.length -1;
		this.primaryFailureProb = primaryFailureProb;
		accessCount = 0;
	}
	
	int[] heapSort() throws ThreadFailException{
		heapify();
		
		for( int i= length; i>0; i--){
			swap(0,i);
			length = length -1;
			maxNumber(0);
		}

		double hazard = accessCount * primaryFailureProb;
		Random generator = new Random();
		double thresholdValue = generator.nextDouble();
		System.out.println(thresholdValue);
		System.out.println(accessCount);
		System.out.println(hazard);
		if(thresholdValue > 0.5 && thresholdValue < (0.5 + hazard)){
			System.out.println("Failed in heapsort");
			throw new ThreadFailException();
			
		}else{
			return array;
		}
	}
	
	void heapify() {
		for (int i = length/2; i>= 0; i--){
			maxNumber(i);
		}
	}
	void swap(int firstNumber, int secondNumber){
		int tmp = array[firstNumber];
		array[firstNumber] =array[secondNumber];
		array[secondNumber] = tmp;
		accessCount += 3;
	}
	
	void maxNumber (int index){
		int left = 2*index;
		int right = 2*index +1;
		int largest = index;
		
		if (left <= length && array[left] >array[index] ){
			largest= left;
			accessCount+=2;
		} 
		if (right <= length && array[right] > array[largest]){
			largest = right;
			accessCount+=2;
		}
		if (largest != index){
			swap(index, largest);
			maxNumber(largest);
		}
		
	}
}
