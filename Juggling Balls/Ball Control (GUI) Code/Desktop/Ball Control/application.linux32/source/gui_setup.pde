// The following functions are called when GUI elements need to be created

void setup_controls() { // This is the main function for creating GUI elements add is called once at startup.

  int x_centre = 300;
  int x_seperation = 200;

  for (int i = 0; i < ball_numbers.length; i++) {
    r1[i] = colour_array_balls[i][0];
    g1[i] = colour_array_balls[i][1];
    b1[i] = colour_array_balls[i][2];
    kx1[i] = x_centre-50  + i * x_seperation;
    ky1[i] = 160;

    kx2[i] = x_centre-50  + i * x_seperation;
    ky2[i] = 350;

    knbRed1[i] = new GKnob(this, kx1[i], ky1[i], 150, 150, 0.8);
    knbRed1[i].setTurnRange(150, 270);
    knbRed1[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbRed1[i].setArcPolicy(true, true, true);
    knbRed1[i].setLimits(r1[i], 0, 255);
    knbRed1[i].setNbrTicks(9);
    knbRed1[i].setLocalColorScheme(G4P.RED_SCHEME); 
    knbRed1[i].tagNo = i;  
    knbRed1[i].tag = "red1";

    knbGreen1[i] = new GKnob(this, kx1[i] + 8, ky1[i], 150, 150, 0.8);
    knbGreen1[i].setTurnRange(270, 30);
    knbGreen1[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbGreen1[i].setArcPolicy(true, true, true);
    knbGreen1[i].setLimits(g1[i], 0, 255);
    knbGreen1[i].setNbrTicks(9);
    knbGreen1[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    knbGreen1[i].tagNo = i;  
    knbGreen1[i].tag = "green1"; 

    knbBlue1[i] = new GKnob(this, kx1[i] + 4, ky1[i] + 9, 150, 150, 0.8);
    knbBlue1[i].setTurnRange(30, 150);
    knbBlue1[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbBlue1[i].setArcPolicy(true, true, true);
    knbBlue1[i].setLimits(b1[i], 0, 255);
    knbBlue1[i].setNbrTicks(9);
    knbBlue1[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    knbBlue1[i].tagNo = i;
    knbBlue1[i].tag = "blue1";  


    knbRed2[i] = new GKnob(this, kx2[i], ky2[i], 150, 150, 0.8);
    knbRed2[i].setTurnRange(150, 270);
    knbRed2[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbRed2[i].setArcPolicy(true, true, true);
    knbRed2[i].setLimits(r2[i], 0, 255);
    knbRed2[i].setNbrTicks(9);
    knbRed2[i].setLocalColorScheme(G4P.RED_SCHEME); 
    knbRed2[i].tagNo = i;  
    knbRed2[i].tag = "red2";

    knbGreen2[i] = new GKnob(this, kx2[i] + 8, ky2[i], 150, 150, 0.8);
    knbGreen2[i].setTurnRange(270, 30);
    knbGreen2[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbGreen2[i].setArcPolicy(true, true, true);
    knbGreen2[i].setLimits(g2[i], 0, 255);
    knbGreen2[i].setNbrTicks(9);
    knbGreen2[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    knbGreen2[i].tagNo = i;  
    knbGreen2[i].tag = "green2"; 

    knbBlue2[i] = new GKnob(this, kx2[i] + 4, ky2[i] + 9, 150, 150, 0.8);
    knbBlue2[i].setTurnRange(30, 150);
    knbBlue2[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbBlue2[i].setArcPolicy(true, true, true);
    knbBlue2[i].setLimits(b2[i], 0, 255);
    knbBlue2[i].setNbrTicks(9);
    knbBlue2[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    knbBlue2[i].tagNo = i;
    knbBlue2[i].tag = "blue2";  

    stroke(0);
    strokeWeight(3);
    rectMode(CORNERS);
  }   

  for (int i = 0; i < lblRed1.length; i++) {
    lblRed1[i] = new GLabel(this, x_centre-50  + i * x_seperation, 135, 60, 18, "Red");
    lblRed1[i].tag = "red1";
    lblRed1[i].setLocalColorScheme(G4P.RED_SCHEME);
    lblRed1[i].tagNo = i;
    lblRed1[i].setText("red1");
  } 


  for (int i = 0; i < lblGreen1.length; i++) {
    lblGreen1[i] = new GLabel(this, x_centre  + i * x_seperation, 135, 60, 18, "Green");
    lblGreen1[i].tag = "green1";
    lblGreen1[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    lblGreen1[i].tagNo = i;
    lblGreen1[i].setText("red1");
  } 


  for (int i = 0; i < lblBlue1.length; i++) {
    lblBlue1[i] = new GLabel(this, x_centre+50  + i * x_seperation, 135, 60, 18, "Blue");
    lblBlue1[i].tag = "blue1";
    lblBlue1[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblBlue1[i].tagNo = i;
    lblBlue1[i].setText("blue1");
  } 



  for (int i = 0; i < lblRed2.length; i++) {
    lblRed2[i] = new GLabel(this, x_centre-50  + i * x_seperation, 330, 60, 18, "Red");
    lblRed2[i].tag = "red2";
    lblRed2[i].setLocalColorScheme(G4P.RED_SCHEME);
    lblRed2[i].tagNo = i;
    lblRed2[i].setText("red2");
  } 


  for (int i = 0; i < lblGreen2.length; i++) {
    lblGreen2[i] = new GLabel(this, x_centre  + i * x_seperation, 330, 60, 18, "Green");
    lblGreen2[i].tag = "green2";
    lblGreen2[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    lblGreen2[i].tagNo = i;
    lblGreen2[i].setText("red2");
  } 


  for (int i = 0; i < lblBlue2.length; i++) {
    lblBlue2[i] = new GLabel(this, x_centre+50  + i * x_seperation, 330, 60, 18, "Blue");
    lblBlue2[i].tag = "blue2";
    lblBlue2[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblBlue2[i].tagNo = i;
    lblBlue2[i].setText("blue2");
  } 

  for (int i = 0; i < dplEffectSelect.length; i++) {
    dplEffectSelect[i] = new GDropList(this, x_centre-40  + i * x_seperation, 90, 150, 100, 6);
    dplEffectSelect[i].tag = "dpl_effect_select";
    dplEffectSelect[i].setItems(available_effects, 0); 
    dplEffectSelect[i].tagNo = i;
    // dplEffectSelect[i].expanded(true);
    //   btnSolid[i].setEnabled(false);
  } 

  btnPhoto  =  new GButton(this, 25, 15, 200, 18, "Photo");
  btnPhoto.tag = "Photo";

  btnVideo  =  new GButton(this, 25, 50, 200, 18, "Video");
  btnVideo.tag = "Video";



  btnRoutineSelect  =  new GButton(this, 25, 90, 200, 18, "No Routine Selected");
  btnRoutineSelect.tag = "RoutineSelect";


  cbxXmlPlay=  new GCheckbox(this, 25, 120, 100, 20, "Play Routine");
  cbxXmlPlay.tag = "XmlPlay";

  cbxXmlPlay.setSelected(false);

  for (int i = 0; i < sdrDelay.length; i++) {
    sdrDelay[i] = new GCustomSlider(this, x_centre-50  + i * x_seperation, 530, 150, 50, "Delay"); 
    sdrDelay[i].tag = "Delay";
    sdrDelay[i].setShowDecor(false, true, true, true);
    sdrDelay[i].setNbrTicks(5);
    sdrDelay[i].tagNo = i;
    sdrDelay[i].setLimits(40, 5, 2000);
  }


  for (int i = 0; i < cbxRepeat.length; i++) {
    cbxRepeat[i] =  new GCheckbox(this, x_centre-25  + i * x_seperation, 590, 100, 20, "Repeat");
    cbxRepeat[i].tag = "Repeat";
    cbxRepeat[i].tagNo = i;
    cbxRepeat[i].setSelected(true);
  }
}

void setup_routine_buttons()
{
  XML[] section_children = xml.getChildren("section");
  String[] xml_sections = new String[section_children.length];
  for (int j = 0; j<section_children.length; j++) {
    String section_name = section_children[j].getString("name"); // [0] because only one effect per ball
    xml_sections[j] = section_name;
  }
 
  if (dplRoutineExists == false) {
    dplRoutine = new GDropList(this, 25, 150, 200, 500, 20);

    dplRoutineExists = true;
  }
  dplRoutine.setItems(xml_sections, 0); 
  dplRoutine.tag = "section_select";
}

