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

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 1; // Type of command juggling balll should follow
  payload_data[2] = colours_array[0];
  payload_data[3] = colours_array[1];
  payload_data[4] = colours_array[2];
  payload_data[5] = 0;
  payload_data[6] = 0;
  payload_data[7] = 0;
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = 0;

  send_payload(payload_data);
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

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 5; 
  payload_data[2] = colours_array[0][0];
  payload_data[3] = colours_array[0][1];
  payload_data[4] = colours_array[0][2];
  payload_data[5] = colours_array[1][0];
  payload_data[6] = colours_array[1][1];
  payload_data[7] = colours_array[1][2];
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = delay;

  send_payload(payload_data);
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

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 8; 
  payload_data[2] = 0;
  payload_data[3] = 0;
  payload_data[4] = 0;
  payload_data[5] = 0;
  payload_data[6] = 0;
  payload_data[7] = 0;
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = delay;

  send_payload(payload_data);
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

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = effect_case; // Type of command juggling balll should follow
  payload_data[2] = colours_array[0][0];
  payload_data[3] = colours_array[0][1];
  payload_data[4] = colours_array[0][2];
  payload_data[5] = colours_array[1][0];
  payload_data[6] = colours_array[1][1];
  payload_data[7] = colours_array[1][2];
  payload_data[8] = 1;
  payload_data[9] = delay;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = 0;


  send_payload(payload_data);
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

  send_payload(payload_data);
}

