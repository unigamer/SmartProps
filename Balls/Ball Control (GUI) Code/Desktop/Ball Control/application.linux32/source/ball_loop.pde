void ball_loop() {

  for (int i = 0; i<ball_numbers.length; i++) { // For each ball check for changes required

    // SOLID
    if (effect_balls[i].equals("solid") == true) {
      if (flag_balls[i] == 1) {
        int[] colours_array = new int[3];
        colours_array[0] = colour_array_balls[i][0];
        colours_array[1] = colour_array_balls[i][1];
        colours_array[2] = colour_array_balls[i][2];
        solid_effect(colours_array, ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // FLASH
    if (effect_balls[i].equals("flash") == true) {
      if (flag_balls[i] == 1) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flash_effect(colours_array, delay_balls[i], ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // DISCO
    if (effect_balls[i].equals("disco") == true) {
      if (flag_balls[i] == 1) {
        disco_effect(delay_balls[i], ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // FADE
    if (effect_balls[i].equals("fade") == true) {
      if (flag_balls[i] == 1) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flag_balls[i] = 0;
        fade_effect(colours_array, delay_balls[i], cycle_balls[i], ball_numbers[i]);
      }
    }

    // FADE IMPACT
    if (effect_balls[i].equals("fade_impact") == true) { // CURRENTLY NOT WORKING ONTHE BALLS!!!
      if (flag_balls[i] == 1) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flag_balls[i] = 0;
        fade_impact_effect(colours_array, delay_balls[i], ball_numbers[i]);
      }
    }
    
    // METEOR
    if (effect_balls[i].equals("meteor") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught   
          println("ball caught " + i);
          time_in_air_balls[i] = millis()-last_event_balls[i];
          println("Time in air" + time_in_air_balls[i] );
        }
        if (state_balls[i] == 1) { // ball has just been thrown
          println("ball thrown " + i);
          timer_balls[i] = millis() + time_in_air_balls[i]/2 -100;
          flag_balls[i] = 1;
          last_event_balls[i] = millis();
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];
          solid_effect(colours_array, ball_numbers[i]);
        }
      }
      if ((millis()>timer_balls[i]) && (flag_balls[i] == 1)) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        fade_effect(colours_array, (time_in_air_balls[i]/2)/255, 0, ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // METEOR RANDOM
    if (effect_balls[i].equals("meteor_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught
          println("ball caught " + i);
          time_in_air_balls[i] = millis()-last_event_balls[i];
          println("Time in air" + time_in_air_balls[i] );
        }
        if (state_balls[i] == 1) { // ball has just been thrown
          println("ball thrown " + i);
          timer_balls[i] = millis() + time_in_air_balls[i]/2 -100;
          flag_balls[i] = 1;
          last_event_balls[i] = millis();
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];

          solid_effect(colours_array, ball_numbers[i]);
        }
      }

      if ((millis()>timer_balls[i]) && (flag_balls[i] == 1)) {


        int[][] colours_array = new int[2][3];

        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];

        int[] random_colours = randomColourArrayV2();

        colours_array[1][0] = random_colours[0];
        colours_array[1][1] = random_colours[1];
        colours_array[1][2] = random_colours[2];

        colour_array_balls[i][3] = random_colours[0];
        colour_array_balls[i][4] = random_colours[1];
        colour_array_balls[i][5] = random_colours[2];

        fade_effect(colours_array, (time_in_air_balls[i]/2)/255, 0, ball_numbers[i]);

        flag_balls[i] = 0;
      }
    }

    // TWO STATE
    if (effect_balls[i].equals("two_state") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];
          solid_effect(colours_array, ball_numbers[i]);
        } 
        else if (state_balls[i] == 1) { // ball has just been thrown
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][3];
          colours_array[1] = colour_array_balls[i][4];
          colours_array[2] = colour_array_balls[i][5];
          solid_effect(colours_array, ball_numbers[i]);
        }
      }
    }

    // TWO STATE RANDOM
    if (effect_balls[i].equals("two_state_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];
          solid_effect(colours_array, ball_numbers[i]);
        } 
        else if (state_balls[i] == 1) { // ball has just been thrown
          if (millis()>sync_timer_throw_random) {
            sync_flag_throw_random = false;
          }
          if (state_balls[i] == 1) { // ball has just been thrown
            if (sync_flag_throw_random==true) {
              colour_array_balls[i][3] = colour_array_shared_sync_throw_random[0];
              colour_array_balls[i][4] =  colour_array_shared_sync_throw_random[1];
              colour_array_balls[i][5] =  colour_array_shared_sync_throw_random[2];
              int[] random_colours = new int[3];
              random_colours[0] = colour_array_shared_sync_throw_random[0];
              random_colours[1] = colour_array_shared_sync_throw_random[1];
              random_colours[2] = colour_array_shared_sync_throw_random[2];
              solid_effect(random_colours, ball_numbers[i]);
            } 
            else {
              int[] random_colours = randomColourArrayV2();
              colour_array_shared_sync_throw_random[0] = random_colours[0];
              colour_array_shared_sync_throw_random[1] = random_colours[1];
              colour_array_shared_sync_throw_random[2] = random_colours[2];
              colour_array_balls[i][3] = random_colours[0];
              colour_array_balls[i][4] =  random_colours[1];
              colour_array_balls[i][5] =  random_colours[2];
              solid_effect(random_colours, ball_numbers[i]);
              sync_timer_throw_random = millis() + 200;
              sync_flag_throw_random = true;
            }
          }
        }
      }
    }

    // THROW RANDOM
    if (effect_balls[i].equals("throw_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 1) { // ball has just been thrown
          int[] random_colours = randomColourArrayV2_no_repeat(i);
          colour_array_balls[i][0] = random_colours[0];
          colour_array_balls[i][1] =  random_colours[1];
          colour_array_balls[i][2] =  random_colours[2];
          solid_effect(random_colours, ball_numbers[i]);
        }
      }
    }



    if (effect_balls[i].equals("sync_throw_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (millis()>sync_timer_throw_random) {
          sync_flag_throw_random = false;
        }
        if (state_balls[i] == 1) { // ball has just been thrown
          if (sync_flag_throw_random==true) {
            colour_array_balls[i][0] = colour_array_shared_sync_throw_random[0];
            colour_array_balls[i][1] =  colour_array_shared_sync_throw_random[1];
            colour_array_balls[i][2] =  colour_array_shared_sync_throw_random[2];
            int[] random_colours = new int[3];
            random_colours[0] = colour_array_shared_sync_throw_random[0];
            random_colours[1] = colour_array_shared_sync_throw_random[1];
            random_colours[2] = colour_array_shared_sync_throw_random[2];
            solid_effect(random_colours, ball_numbers[i]);
          } 
          else {
            int[] random_colours = randomColourArrayV2();
            colour_array_shared_sync_throw_random[0] = random_colours[0];
            colour_array_shared_sync_throw_random[1] = random_colours[1];
            colour_array_shared_sync_throw_random[2] = random_colours[2];
            colour_array_balls[i][0] = random_colours[0];
            colour_array_balls[i][1] =  random_colours[1];
            colour_array_balls[i][2] =  random_colours[2];
            solid_effect(random_colours, ball_numbers[i]);
            sync_timer_throw_random = millis() + 200;
            sync_flag_throw_random = true;
          }
        }
      }
    }


    // SYNC RANDOM
    if (effect_balls[i].equals("sync_random") == true) {
      if (millis()>sync_timer_random) {
        int[] random_colours = randomColourArrayV2();
        colour_array_shared_sync_random[0] = random_colours[0];
        colour_array_shared_sync_random[1] = random_colours[1];
        colour_array_shared_sync_random[2] = random_colours[2];
        colour_array_balls[i][0] = random_colours[0];
        colour_array_balls[i][1] =  random_colours[1];
        colour_array_balls[i][2] =  random_colours[2];
        sync_timer_random = millis() + sync_random_delay;
        solid_effect(random_colours, ball_numbers[i]);
      } 
      else if (colour_array_balls[i][0]!=colour_array_shared_sync_random[0] || colour_array_balls[i][1]!=colour_array_shared_sync_random[1]  || colour_array_balls[i][2]!=colour_array_shared_sync_random[2]  ) {
        colour_array_balls[i][0] = colour_array_shared_sync_random[0];
        colour_array_balls[i][1] =  colour_array_shared_sync_random[1];
        colour_array_balls[i][2] =  colour_array_shared_sync_random[2];
        int[] random_colours = new int[3];
        random_colours[0] = colour_array_shared_sync_random[0];
        random_colours[1] = colour_array_shared_sync_random[1];
        random_colours[2] = colour_array_shared_sync_random[2];
        solid_effect(random_colours, ball_numbers[i]);
      }
    }


    if (effect_balls[i].equals("sync_random_fade") == true) {
      if (millis()>sync_timer_random_fade) {
        int[] random_colours = randomColourArrayV2();
        //  int[] colour_array_shared_random_fade = new int[2][3]
        if (colour_array_shared_sync_random_fade[1][0]==0 && colour_array_shared_sync_random_fade[1][1]==0 && colour_array_shared_sync_random_fade[1][2]==0) {
          colour_array_shared_sync_random_fade[1][0] = colour_array_balls[i][0];
          colour_array_shared_sync_random_fade[1][1] = colour_array_balls[i][1]  ;
          colour_array_shared_sync_random_fade[1][2] = colour_array_balls[i][2] ;
        }
        colour_array_shared_sync_random_fade[0][0] = colour_array_shared_sync_random_fade[1][0];
        colour_array_shared_sync_random_fade[0][1] = colour_array_shared_sync_random_fade[1][1];
        colour_array_shared_sync_random_fade[0][2] = colour_array_shared_sync_random_fade[1][2];
        if (colour_array_balls[i][3]==255 && colour_array_balls[i][4]==255 && colour_array_balls[i][5]==255 ) { /// THIS IS A HACK FOR STYLO ROUTINE
          colour_array_shared_sync_random_fade[0][0] = colour_array_balls[i][3];
          colour_array_shared_sync_random_fade[0][1] = colour_array_balls[i][4];
          colour_array_shared_sync_random_fade[0][2] = colour_array_balls[i][5];
        }

        colour_array_shared_sync_random_fade[1][0] = random_colours[0];
        colour_array_shared_sync_random_fade[1][1] = random_colours[1];
        colour_array_shared_sync_random_fade[1][2] = random_colours[2];
        colour_array_balls[i][0] = colour_array_shared_sync_random_fade[0][0];
        colour_array_balls[i][1] =  colour_array_shared_sync_random_fade[0][1];
        colour_array_balls[i][2] =  colour_array_shared_sync_random_fade[0][2];
        colour_array_balls[i][3] = colour_array_shared_sync_random_fade[1][0];
        colour_array_balls[i][4] =  colour_array_shared_sync_random_fade[1][01];
        colour_array_balls[i][5] =  colour_array_shared_sync_random_fade[1][2];
        sync_timer_random_fade = millis() + 255*sync_fade_delay+400;
        fade_effect(colour_array_shared_sync_random_fade, sync_fade_delay, 0, ball_numbers[i]);
      } 
      else if (colour_array_balls[i][0]!=colour_array_shared_sync_random_fade[0][0] || colour_array_balls[i][1]!=colour_array_shared_sync_random_fade[0][1]  || colour_array_balls[i][2]!=colour_array_shared_sync_random_fade[0][2] || colour_array_balls[i][3]!=colour_array_shared_sync_random_fade[1][0] || colour_array_balls[i][4]!=colour_array_shared_sync_random_fade[1][1]  || colour_array_balls[i][5]!=colour_array_shared_sync_random_fade[1][2]  ) {
        colour_array_balls[i][0] = colour_array_shared_sync_random_fade[0][0];
        colour_array_balls[i][1] =  colour_array_shared_sync_random_fade[0][1];
        colour_array_balls[i][2] =  colour_array_shared_sync_random_fade[0][2];
        colour_array_balls[i][3] = colour_array_shared_sync_random_fade[1][0];
        colour_array_balls[i][4] = colour_array_shared_sync_random_fade[1][1];
        colour_array_balls[i][5] =  colour_array_shared_sync_random_fade[1][2];
        fade_effect(colour_array_shared_sync_random_fade, sync_fade_delay, 0, ball_numbers[i]);
      }
    }

    // RANDOM COLOURS
    if (effect_balls[i].equals("random_colours") == true) {
      if (flag_balls[i] == 1) {
        timer_balls[i] = millis() + sdrDelay[i].getValueI();
        flag_balls[i] = 0;
      }
      if ((millis()>timer_balls[i])) {
        int[] random_colours = randomColourArrayV2();
        colour_array_balls[i][0] = random_colours[0];
        colour_array_balls[i][1] =  random_colours[1];
        colour_array_balls[i][2] =  random_colours[2];
        solid_effect(random_colours, ball_numbers[i]);
        timer_balls[i] = millis() + sdrDelay[i].getValueI();
      }
    }
    
    // RANDOM FADE
    if (effect_balls[i].equals("random_fade") == true) {
      if (flag_balls[i] == 1) {
        timer_balls[i] = millis() + 255*sdrDelay[i].getValueI();
        flag_balls[i] = 0;
      }
      if ((millis()>timer_balls[i])) {
        int[] random_colours1 = randomColourArrayV2();
        colour_array_balls[i][0] = colour_array_balls[i][3] ;
        colour_array_balls[i][1] =  colour_array_balls[i][4] ;
        colour_array_balls[i][2] =  colour_array_balls[i][5] ;
        colour_array_balls[i][3] = random_colours1[0];
        colour_array_balls[i][4] =  random_colours1[1];
        colour_array_balls[i][5] =  random_colours1[2];
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flag_balls[i] = 0;
        fade_effect(colours_array, sdrDelay[i].getValueI(), 0, ball_numbers[i]);
        timer_balls[i] = millis() + 255*sdrDelay[i].getValueI()+100;
      }
    }
  }
}

