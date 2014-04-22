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
