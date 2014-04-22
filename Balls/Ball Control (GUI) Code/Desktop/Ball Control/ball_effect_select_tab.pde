// The following function is called when a new effect is required for a given ball. It's main purpose to change the GUI to reflect the new effect.
// For instance, the delay of a flash effect is a different range to that of a fade effect so the sliders must be updated. This function also gets the
// correct values for colour and delay from the knobs and sliders.
void ball_effect_select(int ball_number) {

  if (millis()-time_since_last_command[ball_number] > 30) { // This conditional is used to stop commands being sent too quickly to the balls. For instance, as you drag the slider you go through a range. If the number is small then new commands will be repeatedly sent and saturate the serial and wireless connections.
    time_since_last_command[ball_number] = millis();
 

    if (effect_balls[ball_number].equals("solid")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("flash")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 50, 2000);
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("fade")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 1, 60);
      cycle_balls[ball_number] = int(cbxRepeat[ball_number].isSelected());
      flag_balls[ball_number] = 1;
    }
    
    if (effect_balls[ball_number].equals("random_fade_b")) {
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 1, 60);
      flag_balls[ball_number] = 1;
    }
    
        if (effect_balls[ball_number].equals("throw_random_b")) {
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("fade_impact")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 1, 60);
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("meteor")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("two_state")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("two_state_random")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("meteor_random")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("random_colours")) {
      flag_balls[ball_number] = 1;
    }
    
    if (effect_balls[ball_number].equals("disco")) {
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 50, 2000);
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("random_fade")) {
      flag_balls[ball_number] = 1;
    }
    
    if (effect_balls[ball_number].equals("two_state_b")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      flag_balls[ball_number] = 1;
    }
  }
}

