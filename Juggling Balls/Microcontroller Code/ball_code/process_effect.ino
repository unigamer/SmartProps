void process_effect() {
  // This is where the ball's colour is processed depending on what effect is selected. 
  // It's entirely possible (and likely) that no change is required from the previous time this function was called.
  // In which case, nothing willl change and we'll leave process_effect()

  switch (effect_number) {

  case 0:
    update_parameters_1(); // Update some parameters - this was used for some debugging stuff
    break;

  case 1:
    effect_solid(0); // Solid Colour
    break;

  case 2:
    effect_fade(); // Fade - Single change, no cycling or repeating  
    break;

  case 3:
    effect_fade_impact(); // Fade on Impact - THIS EFFECT CURRENTLY DOES NOT WORK. I broke it when I changed how the ball's state was detected.  // fade on impact from one colour back to no impact colour
    break;

  case 4:
    effect_cycle_fade(); // Cycle Fade - Go back and fourth between colours
    break;   

  case 5:
    effect_flash_balls();  // Flash balls - Go between two colours at a specified frequency (1/fade_delay).
    break;  

  case 6:
    send_sensor_data(); // This is used to calibrate the balls by sending their x,y & z accelerations readings continously.
    break;  

  case 7:
    update_parameters_2();     //  Get updated accelerometer scaling parameters and update the eeprom
    break;  

  case 8:
    effect_disco_mode(); //  Disco Mode
    break;
    
  case 9:
    effect_two_state(); //  Two State
    break;
    
   case 10:
    effect_throw_random(); //  Throw Random
    break;
    
   case 11:
    effect_random_fade(); //  Random Fade
    break;
    
   case 13:
    effect_two_state_random(); //  Two State Random
    break;
   
  } 
}





