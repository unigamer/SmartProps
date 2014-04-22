// XML Parsing Stuff
/* This stuff is a bit hard to follow. It's used to read an xml file describing the effects of the balls. This can change over time or be static requiring
the user to change the effects. To understand the format of the xml files it's best to look at example ones but the basic idea is
<routine><section><ball><effect></effect></ball></section></routine>.

xml_section_select() is called up when a new section is required to parsed. It then passes off each ball effect to select_effect_xml().

*/
void xml_section_select(String xml_name, int section_number) {
  // print("Song Time: ");
  //  print(player.time);
  //  println("");
  print("Routine Time: ");
  print(millis()-routine_start_time);
  println("");
  XML[] section_children = xml.getChildren("section");
  XML[] section_children_ball = section_children[section_number].getChildren("ball");
  String all_balls_check = section_children_ball[0].getString("number");
  String name = section_children[section_number].getString("name");
  print("Section name: ");
  print(name);
  println("");
  
  if ((all_balls_check.equals("all") )== true) {
    for (int j = 0; j<ball_numbers.length; j++) {
      int ball_number = ball_numbers[j];
      XML[] section_children_ball_effect = section_children_ball[0].getChildren("effect");
      select_effect_xml(section_children_ball_effect, ball_number);
    }
  } 
  else {
    for (int j = 0; j<section_children_ball.length; j++) {
      int ball_number = int(section_children_ball[j].getString("number"));
      XML[] section_children_ball_effect = section_children_ball[j].getChildren("effect");
      select_effect_xml(section_children_ball_effect, ball_number);
    }
  }
  
  if (xml_has_time == true && cbxXmlPlay.isSelected()==true) {
    if ((next_section+2)>section_children.length) {
      println("ROUTINE COMPLETE");  
      if (loop_xml == 1) {
        next_section = 0;
        next_section_time = 0;
        routine_start_time = millis();
      } 
      else {
        xml_flag=false;
      }
    } 
    else {

      next_section = next_section+1;
      next_section_time = int(section_children[next_section].getString("time"));
    }
  }
}



void select_effect_xml(XML[] section_children_ball_effect, int ball_number) {

  if (ball_number == 0) { // ball_number=0 is used for photo and video effects only. The CANNOT be an actual ball with the number zero.
    String effect_type = section_children_ball_effect[0].getString("type"); // [0] because only one effect per ball
    if ((effect_type.equals("photo") )== true) { 
      photo();
    }
    if ((effect_type.equals("video") )== true) { 
      video();
    }
  } 
  
  else {
    ball_number = findIndexofArray(ball_number, ball_numbers); // Work with the actual index array
    String effect_type = section_children_ball_effect[0].getString("type"); // [0] because only one effect per ball
    effect_balls[ball_number] = effect_type;
    
    // Solid
    if ((effect_type.equals("solid") )== true) { 
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours = (trim(section_children_ball_effect_colour[0].getContent()));
      int[] colour_array = int(split(colours, ','));
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array[2], colour_array_balls[ball_number][2]), 0, 255);
      ball_effect_select(ball_number);
    } 
    

    if ((effect_type.equals("flash"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sdrDelay[ball_number].setLimits(int(delay_string), 5, 2000);
      int[] colour_array1 = int(split(colours1, ',')); 
      int[] colour_array2 = int(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      ball_effect_select(ball_number);
    } 


    if ((effect_type.equals("fade") )== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      if (section_children_ball_effect[0].hasAttribute("cycle")==true) {
        String fade_cycle = section_children_ball_effect[0].getString("cycle");
        if ((fade_cycle.equals("true"))== true) {
          cbxRepeat[ball_number].setSelected(true);
        } 
        else {
          cbxRepeat[ball_number].setSelected(false);
        }
      } 
      else {
        cbxRepeat[ball_number].setSelected(false);
      }

      int[] colour_array1 = int(split(colours1, ',')); 
      int[] colour_array2 = int(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      sdrDelay[ball_number].setLimits(int(delay_string), 5, 100);
      ball_effect_select(ball_number);
    } 

    if ((effect_type.equals("fade_impact"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      int[] colour_array1 = int(split(colours1, ',')); 
      int[] colour_array2 = int(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      sdrDelay[ball_number].setLimits(int(delay_string), 1, 100);
      ball_effect_select(ball_number);
    }



    if ((effect_type.equals("meteor"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      int[] colour_array1 = int(split(colours1, ',')); 
      int[] colour_array2 = int(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("meteor_random"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      int[] colour_array1 = int(split(colours1, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      ball_effect_select(ball_number);
    }

    if ((effect_type.equals("two_state"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      int[] colour_array1 = int(split(colours1, ',')); 
      int[] colour_array2 = int(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("two_state_random"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      int[] colour_array1 = int(split(colours1, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      ball_effect_select(ball_number);
    }

    if ((effect_type.equals("throw_random"))== true) {
      ball_effect_select(ball_number);
    }



    if ((effect_type.equals("random_colours"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sdrDelay[ball_number].setLimits(int(delay_string), 50, 5000);
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("sync_random"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sync_random_delay =checkIfprevious(int(delay_string), sync_random_delay); 
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("sync_random_fade"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sync_fade_delay =checkIfprevious(int(delay_string), sync_fade_delay); 
      ball_effect_select(ball_number);
    }

    if ((effect_type.equals("sync_throw_random"))== true) {
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("random_fade"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sdrDelay[ball_number].setLimits(int(delay_string), 5, 40);
      ball_effect_select(ball_number);
    }

    dplEffectSelect[ball_number].setItems(available_effects, findIndexofStringArray(effect_type, available_effects)); // Update the dropdown list to the correct effect for the ball
  }
}


