void serialEvent(Serial myPort) {

  // read the serial buffer:
  String myString = myPort.readStringUntil(10);

  // if you got any bytes other than the linefeed:
  if (myString != null) {

    myString = trim(myString);

    // split the string at the commas
    // and convert the sections into integers:

    sensors = int(split(myString, ','));


 



    accel_x = sensors[1];
    accel_y = sensors[2];
    accel_z = sensors[3];
    
    
     lblAccelValues[0].setText(str(accel_x));
     lblAccelValues[1].setText(str(accel_y));
     lblAccelValues[2].setText(str(accel_z));
     









    if (accel_x<accel_values[0] && accel_x>10) {

      accel_values[0] = accel_x;
      lblAccelLimits[0].setText(str(accel_values[0]));
 
    }

    if (accel_y<accel_values[1]&& accel_y>10) {

      accel_values[1] = accel_y;
      lblAccelLimits[1].setText(str(accel_values[1]));
 
    }


    if (accel_z<accel_values[2]&& accel_z>10) {

      accel_values[2] = accel_z;
         lblAccelLimits[2].setText(str(accel_values[2]));
 
    }


    if (accel_x>accel_values[3]) {

      accel_values[3] = accel_x;
          lblAccelLimits[3].setText(str(accel_values[3]));
    }



    if (accel_y>accel_values[4]) {

      accel_values[4] = accel_y;
       lblAccelLimits[4].setText(str(accel_values[4]));

    }



    if (accel_z>accel_values[5]) {

      accel_values[5] = accel_z;
            lblAccelLimits[5].setText(str(accel_values[5]));

    }
    
    
    println("x: "+accel_x+"  y: "+accel_y+"   z: "+accel_z+"   xMin: "+accel_values[0]+"   yMin: "+accel_values[1]+"   zMin: "+accel_values[2]+"   xMax: "+accel_values[3]+"   yMax: "+accel_values[4]+"   zMax: "+accel_values[5]);
  }
}

