// The following functions are used to handle GUI events.

void handleDropListEvents(GDropList list, GEvent event)
{
  if (list.tag.equals("dpl_effect_select")) {
    String selected_effect =  available_effects[list.getSelectedIndex()];
    effect_balls[list.tagNo] = selected_effect;
    ball_effect_select(list.tagNo);
  }
  
  if (list == dplRoutine)
  {
    if  ((cbxXmlPlay.isSelected() == true) && (xml_has_time==true)) {
      xml_flag=true;
      next_section = dplRoutine.getSelectedIndex();     
      XML[] section_children = xml.getChildren("section");   
      next_section_time = int(section_children[next_section].getString("time"));
      routine_start_time = millis()-next_section_time ;
      //   xml_section_select(routine_path, next_section);
    } 
    else {
      xml_flag=true;
      xml_section_select(routine_path, dplRoutine.getSelectedIndex());
    }
  }
  
}

public void handleButtonEvents(GButton button, GEvent event) {
  if ( event == GEvent.CLICKED) {
    if (button.tag.equals("RoutineSelect") == true) {
      selectInput("Select a Routine (.xml)", "fileSelected");
    }
    if (button.tag.equals("Photo") == true) {
      photo();
    }
    if (button.tag.equals("Video") == true) {
      video();
    }
  }
}


void handleKnobEvents(GValueControl knob, GEvent event) {
  ball_effect_select(knob.tagNo);
}

void handleSliderEvents(GValueControl slider, GEvent event) {
  ball_effect_select(slider.tagNo);
}

public void handleToggleControlEvents(GToggleControl option, GEvent event) {
  ball_effect_select(option.tagNo);
  if (option.tag.equals("XmlPlay")) {
    if  ((cbxXmlPlay.isSelected() == true) && (xml_has_time==true)) {
      xml_flag=true;
      next_section = dplRoutine.getSelectedIndex();     
      XML[] section_children = xml.getChildren("section");   
      next_section_time = int(section_children[next_section].getString("time"));
      routine_start_time = millis()-next_section_time ;
    }
  } 
}


void fileSelected(File selection)
{

  delay(5000);
  routine_path = (selection.getAbsolutePath());
  xml_flag = false;
  process_routine_path();
}

// Good luck understanding this one!
void process_routine_path() {
  xml = loadXML(routine_path);
  setup_routine_buttons();
  XML[] section_children = xml.getChildren("section");
  XML routine = section_children[0].getParent();
  int num_balls = routine.getInt("number_balls");
  btnRoutineSelect.setText(routine.getString("name"));  
  if (routine.hasAttribute("loop")==true) {
    String xml_loop_string = routine.getString("loop");
    if ((xml_loop_string.equals("true"))== true) {
      loop_xml = 1;
      println("Loop Routine: True");
    } 
    else {
      loop_xml = 0;
      println("Loop Routine: False");
    }
  } 
  else {
    println("Loop Routine: False");
    loop_xml = 0;
  }
  if (section_children[0].hasAttribute("time")==true) {
    xml_has_time = true;
  } 
  else {
    xml_has_time = false;
  }
  if (routine.hasAttribute("music")==true && cbxXmlPlay.isSelected()==true) {
    String music_path = routine.getString("music");
    minim = new Minim(this);
    player = minim.loadFile(music_path);
    player.play();
  } 
  else {
    //  player.pause();
  }
  xml_flag=true;
  routine_start_time = millis();
  next_section = 0;
  xml_section_select(routine_path, 0); // load first section
}

