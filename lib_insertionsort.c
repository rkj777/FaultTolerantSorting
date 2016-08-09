#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "InsertationSort.h"
#include <time.h>

int insSort(jint *, int length);


JNIEXPORT jintArray JNICALL Java_InsertationSort_insearch
  (JNIEnv *env, jobject object, jintArray array, jdouble failProb){
	jsize len;
	int length;
	jint *myCopy;
	jint result;
	jboolean *is_copy = 0;
	double myFailProb;
	int access;
	len = (*env)->GetArrayLength(env, array);
	myCopy = (jint *) (*env)->GetIntArrayElements(env, array, is_copy);
	if (myCopy == NULL){
    		printf("Cannot obtain array from JVM\n");
   		exit(0);
  	}
	length = (int)len;
	access = insSort(myCopy,length);
	
	myFailProb = (double) failProb;
	
	double hazard = access * myFailProb;
	
	srand(time(NULL));
	double threshold =((double) rand()/ (double) RAND_MAX);
	
	if(threshold > 0.5 && threshold < (0.5 + hazard)){
		return NULL;
	}else{
		(*env)->ReleaseIntArrayElements(env, array, myCopy, 0);
		return array;
	}

	
};

int insSort(jint *array, int length){
	int i,j;
	int access = 0;
	for(i=1; i<length;i++){
		for(j=i; j>0 && (array[j-1] > array[j]);j--){
			int temp = array[j];
			array[j] = array[j-1];
			array[j-1] = temp;
			access+=4;
		}
	access+= (length*2);
	}
	return access;
}
