boolean int2bool(int int_value) {
boolean boolean_value = false;
 
  if (int_value==1) {
    boolean_value = true;
  }
  
  return boolean_value;
  
   
  
}


 void randomColourArrayV2() {
 int colour_at_zero = int(random(0, 3));
    int temp_flag = 1;
 
    while (temp_flag == 1) {
      for (int j = 0; j < 3; j++) {
        if (j==colour_at_zero) {
          random_colour_array[j]=0;
        } 
        else {
          random_colour_array[j] = int(random(0, 255));
        }
      }
      if (random_colour_array[0]+random_colour_array[1]+random_colour_array[2]<100 || random_colour_array[0]+random_colour_array[1]+random_colour_array[2]>700) {
        //   println("minimum brightness not reached, new combination attempt");
        // i=i-1;
        //delay(1);
      } 
      else {
        temp_flag = 0;
      }
    }

}

void randomColourArrayV2_no_repeat() {
  int temp_flag = 1;
 int colour_at_zero;
  while (temp_flag == 1) {  
    colour_at_zero = int(random(0, 3));

    if (colour_at_zero!=previous_colour_at_zero_array) {

      previous_colour_at_zero_array = colour_at_zero;

      temp_flag = 0;
    }
  }
  temp_flag = 1;

 
  while (temp_flag == 1) {
    for (int j = 0; j < 3; j++) {
      if (j==colour_at_zero) {
        random_colour_array[j]=0;
      } 
      else {
        random_colour_array[j] = int(random(0, 255));
      }
    }
    if (random_colour_array[0]+random_colour_array[1]+random_colour_array[2]<100 || random_colour_array[0]+random_colour_array[1]+random_colour_array[2]>700) {
    //  println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  
}



// This is used for the magnitude of the accelerometer reading
int digitalSmooth(int rawIn, int *sensSmoothArray){     // "int *sensSmoothArray" passes an array to the function - the asterisk indicates the array name is a pointer
  int j, k, temp, top, bottom;
  long total;
  static int i;
 // static int raw[filterSamples];
  static int sorted[filterSamples];
  boolean done;

  i = (i + 1) % filterSamples;    // increment counter and roll over if necc. -  % (modulo operator) rolls over variable
  sensSmoothArray[i] = rawIn;                 // input new data into the oldest slot

  // Serial.print("raw = ");

  for (j=0; j<filterSamples; j++){     // transfer data array into anther array for sorting and averaging
    sorted[j] = sensSmoothArray[j];
  }

  done = 0;                // flag to know when we're done sorting              
  while(done != 1){        // simple swap sort, sorts numbers from lowest to highest
    done = 1;
    for (j = 0; j < (filterSamples - 1); j++){
      if (sorted[j] > sorted[j + 1]){     // numbers are out of order - swap
        temp = sorted[j + 1];
        sorted [j+1] =  sorted[j] ;
        sorted [j] = temp;
        done = 0;
      }
    }
  }

/*
  for (j = 0; j < (filterSamples); j++){    // print the array to debug
    Serial.print(sorted[j]); 
    Serial.print("   "); 
  }
  Serial.println();
*/

  // throw out top and bottom 15% of samples - limit to throw out at least one from top and bottom
  bottom = max(((filterSamples * 15)  / 100), 1); 
  top = min((((filterSamples * 85) / 100) + 1  ), (filterSamples - 1));   // the + 1 is to make up for asymmetry caused by integer rounding
  k = 0;
  total = 0;
  for ( j = bottom; j< top; j++){
    total += sorted[j];  // total remaining indices
    k++; 
    // Serial.print(sorted[j]); 
    // Serial.print("   "); 
  }

//  Serial.println();
//  Serial.print("average = ");
//  Serial.println(total/k);
  return total / k;    // divide by number of samples
}
