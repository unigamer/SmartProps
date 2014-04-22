// These are the base effects that are actually programmed into the juggling ball. For instance random fade
// just sends a fade command from a specified colour to another. The random colour is not selected onboard the
// juggling ball. This is slowly changing as I add more "smart" functionality" onto the balls. This means less
// processing needs to be done on the main computer. Eventually the central Arduino (or embedded computer)
// could potentially handle all the smart stuff and just host an interface for the user to connect to.
// --------------------------------------------------------------------------------------------------------------

void solid_effect(int[] colours_array, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   
  print("Ball Number: ");
  print(ball_number);
  println(""); 
  println("Effect type: Solid");
  print("Red: ");
  print(colours_array[0]);
  print("\t Green: ");
  print(colours_array[1]);
  print("\t Blue: ");
  print(colours_array[2]);
  println("");

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = 1; // Effect number
  payload_data[2] = colours_array[0]; // R1
  payload_data[3] = colours_array[1]; // G1
  payload_data[4] = colours_array[2]; // B1
  payload_data[5] = 0; // R2
  payload_data[6] = 0; // G2
  payload_data[7] = 0; // B2
  payload_data[8] = 0; // fade_delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;

  send_payload(ball_number,payload_data);
}


void flash_effect(int[][] colours_array, int delay, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Flash");
  print("Delay: ");
  print(delay);
  println("");

  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = 5; // Effect Number
  payload_data[2] = colours_array[0][0]; // R1
  payload_data[3] = colours_array[0][1]; // G1
  payload_data[4] = colours_array[0][2]; // B1
  payload_data[5] = colours_array[1][0]; // R2
  payload_data[6] = colours_array[1][1]; // G2
  payload_data[7] = colours_array[1][2]; // B2
  payload_data[8] = delay; // delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;
 

  send_payload(ball_number, payload_data);
}



void two_state_b(int[][] colours_array,int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Two state b");
  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = 9; // Effect Number
  payload_data[2] = colours_array[0][0]; // R1
  payload_data[3] = colours_array[0][1]; // G1
  payload_data[4] = colours_array[0][2]; // B1
  payload_data[5] = colours_array[1][0]; // R2
  payload_data[6] = colours_array[1][1]; // G2
  payload_data[7] = colours_array[1][2]; // B2
  payload_data[8] = 0; // delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;
 
  send_payload(ball_number, payload_data);
}

void throw_random_b(int ball_number) {
  
  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Throw random b");
 

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = 10; // Effect Number
  payload_data[2] = 0; // R1
  payload_data[3] = 0; // G1
  payload_data[4] = 0; // B1
  payload_data[5] = 0; // R2
  payload_data[6] = 0; // G2
  payload_data[7] = 0; // B2
  payload_data[8] = 0; // delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;
 
  send_payload(ball_number, payload_data);
}




void random_fade_b(int delay ,int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Random Fade b");
  print("Delay: ");
  print(delay);
  println("");

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = 11; // Effect Number
  payload_data[2] = 0; // R1
  payload_data[3] = 0; // G1
  payload_data[4] = 0; // B1
  payload_data[5] = 0; // R2
  payload_data[6] = 0; // G2
  payload_data[7] = 0; // B2
  payload_data[8] = delay; // delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;
 
  send_payload(ball_number, payload_data);
}




void disco_effect(int delay, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Disco");
  print("Delay: ");
  print(delay);
  println("");

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = 8; // Effect Number
  payload_data[2] = 0; // R1
  payload_data[3] = 0; // G1
  payload_data[4] = 0; // B1
  payload_data[5] = 0; // R2
  payload_data[6] = 0; // G2
  payload_data[7] = 0; // B2
  payload_data[8] = delay; // delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;
 
  send_payload(ball_number, payload_data);
}

void fade_effect(int[][] colours_array, int delay, int cycle, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  int effect_case;
  String cycle_string;

  if (cycle==1) { // Cycle means go from one colour to the other and back again until a new command is received
    effect_case = 4;
    cycle_string = "True";
  } 
  else {

    effect_case = 2;
    cycle_string = "False";
  }

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Fade Effect");
  print("Delay: ");
  print(delay);
  println("");
  print("Cycle: ");
  print(cycle_string);
  println("");

  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[12];

  payload_data[0] = 1; // Type of command
  payload_data[1] = effect_case; // Effect Number
  payload_data[2] = colours_array[0][0]; // R1
  payload_data[3] = colours_array[0][1]; // G1
  payload_data[4] = colours_array[0][2]; // B1
  payload_data[5] = colours_array[1][0]; // R2
  payload_data[6] = colours_array[1][1]; // G2
  payload_data[7] = colours_array[1][2]; // B2
  payload_data[8] = delay; // delay
  payload_data[9] = 1; // send_state (0=don't send state, 1=send state);
  payload_data[10] = 0;
  payload_data[11] = 0;
 
  send_payload(ball_number, payload_data);
}


void fade_impact_effect(int[][] colours_array, int delay, int ball_number) { // CURRENTLY NOT IMPLEMENTED ON THE BALLS!!

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  int decrement = 10;
  int effect_impact = 30; // magnitude of change required to trigger a colour change

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Fade Impact Effect");
  print("Delay between each fade step (ms): ");
  print(delay);
  println("");

  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 3; // Type of command juggling balll should follow
  payload_data[2] = colours_array[0][0];
  payload_data[3] = colours_array[0][1];
  payload_data[4] = colours_array[0][2];
  payload_data[5] = colours_array[1][0];
  payload_data[6] = colours_array[1][1];
  payload_data[7] = colours_array[1][2];
  payload_data[8] = 1;
  payload_data[9] = delay;
  payload_data[10] = effect_impact;
  payload_data[11] = 0;
  payload_data[12] = 0;

  send_payload(ball_number,payload_data);
}

