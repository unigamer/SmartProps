int[]  randomColourArray() {
  int temp_flag = 1;
  int[] random_colour_array = new int[3];
  while (temp_flag == 1) {
    for (int j = 0; j < 3; j++) {
      random_colour_array[j] = int(random(0, 255));
    }
    if (random_colour_array[0]+random_colour_array[1]+random_colour_array[2]<100 || random_colour_array[0]+random_colour_array[1]+random_colour_array[2]>400) {
      println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  return random_colour_array;
}



int[]  randomColourArrayV2() {
  int colour_at_zero = int(random(0, 3));
  int temp_flag = 1;
  int[] random_colour_array = new int[3];
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
      println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  return random_colour_array;
}


int[]  randomColourArrayV2_no_repeat(int ball_number_zero) {
  int temp_flag = 1;
  int colour_at_zero = 0;
  while (temp_flag == 1) {  
    colour_at_zero = int(random(0, 3));

    if (colour_at_zero!=previous_colour_at_zero_array[ball_number_zero]) {

      previous_colour_at_zero_array[ball_number_zero] = colour_at_zero;

      temp_flag = 0;
    }
  }
  temp_flag = 1;

  int[] random_colour_array = new int[3];
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
      println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  return random_colour_array;
}

// The main use of this function is in parsing xml files. If -1 is used then the previous value should be used for the new command. 
int checkIfprevious(int supplied_value, int current_value) {
  if (supplied_value<0) { // Obviously <0 covers more numbers than just -1 but I decided to cover a wider a range!!
    return current_value;
  }  
  else {
    return supplied_value;
  }
}

// Find the index of an array. Eg ball_numbers=[1,2,3]. Ball number 1 has an index 0 (it's the first one in the array). Ball 2 has an index of 1.
int findIndexofArray(int value, int[] the_array) {
  int requiredIndex = -1;
  for (int i = 0; i < the_array.length; i++) {
    if (the_array[i] == value)
      requiredIndex = i;
  }
  return requiredIndex;
}


int findIndexofStringArray(String value, String[] the_array) {
  int requiredIndex = -1;
  for (int i = 0; i < the_array.length; i++) {
    if  (value.equals(the_array[i]) == true) {
      requiredIndex = i;
    }
  }
  return requiredIndex;
}


void updateKnobs(int i) {
  knbRed1[i].setLimits(colour_array_balls[i][0], 0, 255);
  knbGreen1[i].setLimits(colour_array_balls[i][1], 0, 255);
  knbBlue1[i].setLimits(colour_array_balls[i][2], 0, 255);
  knbRed2[i].setLimits(colour_array_balls[i][3], 0, 255);
  knbGreen2[i].setLimits(colour_array_balls[i][4], 0, 255);
  knbBlue2[i].setLimits(colour_array_balls[i][5], 0, 255);
}

