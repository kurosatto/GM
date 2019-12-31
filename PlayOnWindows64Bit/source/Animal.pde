class Animal {
  PImage image, worriedImage, dizzyImage, angryImage;
  PVector pos, speed, acc;
  int index;
  float diameter;
  float mass;
  float gravityConstant = 0.1;
  float bouncingConstant = 0.4;
  float angle = 0;
  float angSpeed;
  float angAcc = 0;
  boolean isDead = false;
  boolean isWorried = false;
  boolean isWorried2 = false;
  boolean isAngry = false;
  String type;
  float rotatingCons = (2*3.14159265359)/360;
  float inertia = 10000;
  PVector J;
  PVector w;
  PVector collisionPos;

  Animal(String t, PVector p) {
    type = t;
    pos = p;
    if (type.equals("blackhole")) {
      diameter = int(blackholeW);
      mass = 300000;
      angSpeed = 0.06;
      image = blackhole;
      worriedImage = blackhole;
      dizzyImage = blackhole;
    }

    if (type.equals("sheep")) {
      diameter = int(sheepW);
      mass = diameter*diameter;
      angSpeed = 0.1/random(-20, 20);
      image = sheep;
      worriedImage = worriedSheep;
      dizzyImage = dizzySheep;
    }
    
    if (type.equals("rabbit")) {
      diameter = int(rabbitW);
      mass = diameter*diameter;
      angSpeed = 0.1/random(-20, 20);
      image = rabbit;
      worriedImage = rabbit;
      dizzyImage = rabbit;
    }

    if (type.equals("chicken")) {
      diameter = int(chickenW);
      mass = diameter*diameter;
      angSpeed = 0.1/random(-20, 20);
      image = chicken;
      worriedImage = worriedChicken;
      dizzyImage = dizzyChicken;
    }
    if (type.equals("wolf")) {
      diameter = int(wolfW);
      mass = diameter*diameter;
      angSpeed = 0.1/random(-20, 20);
      image = wolf;
      worriedImage = worriedWolf;
      dizzyImage = wolf;
      angryImage = angryWolf;
    }   

    acc = new PVector(0, 0);
    speed = new PVector(0, 0);
    J = new PVector(0, 0);
    w = new PVector(0, 0);
    collisionPos = new PVector(0, 0);
  }

  void update(Animal[] Animals) {
    speed.add(acc.copy().mult(timeStep));
    pos.add(speed.copy().mult(timeStep));
    acc.setMag(0);
    angle = angle + angSpeed * timeStep;
    angSpeed = angSpeed + angAcc;
    angAcc = 0;
    isWorried2 = false;
    isAngry = false;
    for (Animal i : Animals) {

      float r = dist(i.pos.x, i.pos.y, pos.x, pos.y);

      if ((r < (1.5*diameter)) && r != 0 && type.equals("wolf") && (i.type.equals("sheep") || i.type.equals("chicken") )) {
        isAngry = true;
      } 
      if ((r < (1.5*diameter)) && r != 0 && i.type.equals("wolf")  && (type.equals("sheep") || type.equals("chicken") )) {
        isWorried2 = true;
      } else if ((r < (1.5*diameter)) && r != 0 && i.type.equals("blackhole")) {
        isWorried2 = true;
      } 

      if ((r < (i.diameter / 2 + diameter / 2)) && r != 0) {
        if (i.type.equals("wolf")) {
          if (r < i.diameter/2) {
            isDead = true;
          }
        } else if (i.type.equals("blackhole")) {
          if (r < diameter/2) {
            isDead = true;
          }
        } else if (type.equals("blackhole")) {
          if (r < i.diameter/2) {
            i.isDead = true;
          }
        } else if (type.equals("wolf")) {
          if (r < diameter/2) {
            i.isDead = true;
          }
        }
        if (((i.type.equals("rabbit")==true || i.type.equals("sheep")==true || i.type.equals("chicken")==true)  && (type.equals("rabbit")==true || type.equals("sheep")==true || type.equals("chicken")==true)) || (i.type.equals("wolf")==true  && type.equals("wolf")==true)) {           
          PVector collision =  new PVector(pos.x-i.pos.x, pos.y-i.pos.y);
          collision.div(dist(pos.x, pos.y, i.pos.x, i.pos.y));
          collisionPos = i.pos.copy().add(collision.copy().mult(diameter/2));
          i.collisionPos = collisionPos;
          pos = i.pos.copy().add(collision.copy().mult(i.diameter/2 + diameter/2) );

          // Old one
          //float dot1 = speed.copy().dot(collision);
          //float dot2 = i.speed.copy().dot(collision);
          //float v1 = ((dot1 * (mass - i.mass) + 2 * i.mass*dot2)/(mass + i.mass)) * bouncingConstant;
          //float v2 = ((dot2 * (i.mass - mass) + 2 * mass*dot1)/(mass + i.mass)) * bouncingConstant;
          //PVector speedf = speed.copy().add(collision.copy().mult(v1 - dot1));
          //PVector speedf2 = i.speed.copy().add(collision.copy().mult(v2 - dot2));
          //J.set(-mass * (speedf.x + speed.x), -mass * (speedf.y + speed.y));
          //i.J.set(-i.mass * (speedf2.x + i.speed.x), -i.mass * (speedf2.y + i.speed.y));
          //w.set((w.x - ((diameter/2) * (J.x - J.y))) / (0.5 * mass * sq(diameter/2)), (w.y - ((diameter/2) * (J.x - J.y))) / (0.5 * mass * sq(diameter/2)));
          //i.w.set((i.w.x - ((i.diameter/2) * (i.J.x - i.J.y))) / (0.5 * i.mass * sq(i.diameter/2)), (i.w.y - ((i.diameter/2) * (i.J.x - i.J.y))) / (0.5 * i.mass * sq(i.diameter/2)));
          //angSpeed = w.mag()/(diameter/2);
          //i.angSpeed = i.w.mag()/(i.diameter/2);
          //if ((collisionPos.y < pos.y && w.x < 0) || (collisionPos.y > pos.y && w.x > 0)) {
          //  angSpeed *= -1;
          //}
          //if ((i.collisionPos.y < i.pos.y && i.w.x < 0) || (i.collisionPos.y > i.pos.y && i.w.x > 0)) {
          //  i.angSpeed *= -1;
          //}
          //speed.set(speedf);
          //i.speed.set(speedf2);
                 
          // Tried the one in http://www.euclideanspace.com/physics/dynamics/collision/twod/index.htm. Did not work well :(
          float e = 1;
          float ma = mass;
          float mb = i.mass;
          PVector ra = collision.copy().mult(diameter/2);
          PVector rb = collision.copy().mult(- i.diameter/2);
          PVector vai = speed; 
          PVector vbi = i.speed;
          float wai = angSpeed;
          float wbi = i.angSpeed;
          PVector vaf;
          PVector vbf;
          float waf;
          float wbf;
          float Ia = inertia;
          float Ib = inertia;
          float k = 1/(ma*ma)+ 2/(ma*mb) +1/(mb*mb) - ra.x*ra.x/(ma*Ia) - rb.x*rb.x/(ma*Ib)  - ra.y*ra.y/(ma*Ia) - ra.y*ra.y/(mb*Ib) - ra.x*ra.x/(mb*Ia) - rb.x*rb.x/(mb*Ib) - rb.y*rb.y/(ma*Ib) - rb.y*rb.y/(mb*Ib) + ra.y*ra.y*rb.x*rb.x/(Ia*Ib) + ra.x*ra.x*rb.y*rb.y/(Ia*Ib) - 2*ra.x*ra.y*rb.x*rb.y/(Ia*Ib);
          float Jx =  (e+1)/k * (vai.x - vbi.x) * ( 1/ma - ra.x*ra.x/Ia + 1/mb - rb.x*rb.x/Ib) - (e+1)/k * (vai.y - vbi.y) * (ra.x*ra.y/Ia + rb.x*rb.y/Ib );
          float Jy =  (e+1)/k * (vai.y - vbi.y) * ( 1/ma - ra.y*ra.y/Ia + 1/mb - rb.y*rb.y/Ib) + (e+1)/k * (vai.x - vbi.x) * (ra.x*ra.y/Ia + rb.x*rb.y /Ib );
          vaf = new PVector(vai.x - Jx/ma, vai.y - Jy/ma);
          vbf = new PVector(vbi.x + Jx/mb, vbi.y + Jy/mb);
          waf = wai - (Jx * ra.y - Jy * ra.x) /Ia;
          wbf = wbi + (Jx * rb.y - Jy * rb.x) /Ib;
          angSpeed = waf;
          i.angSpeed = wbf;
          speed.set(vaf);
          i.speed.set(vbf);
         

        }
      } else if (r != 0 ) {
        float force = (gravityConstant * mass * i.mass) / (sq(r));
        PVector f = new PVector(i.pos.x - pos.x, i.pos.y - pos.y);
        acc.add(f.copy().mult(force / mass));
      }
    }

    if (manager.screens[manager.current].index >1) {
      if (pos.x < 1.5 * diameter || pos.x > screenW - 1.5 * diameter || pos.y < 1.5 * diameter || pos.y > screenH - 1.5 * diameter) {
        isWorried = true;
      } else {
        isWorried = false;
      }

      if (pos.x > screenW - diameter / 2 || pos.x < diameter / 2 || pos.y > screenH - diameter / 2 || pos.y < diameter / 2) {
        isDead = true;
      }
    } else if (manager.screens[manager.current].index == 1) {
      if (pos.x <-screenW/2+.5*diameter) {
        pos.x = -screenW/2+.5*diameter;
        speed.x = -speed.x;
      }
      if (pos.x >1.5 * screenW - diameter / 2) {
        pos.x = 1.5 * screenW - diameter / 2;
        speed.x = -speed.x;
      }
      if (pos.y <-screenH/2+.5*diameter) {
        pos.y = -screenH/2+.5*diameter;
        speed.y = -speed.y;
      }
      if (pos.y >1.5 * screenH - diameter / 2) {
        pos.y = 1.5 * screenH - diameter / 2;
        speed.y = -speed.y;
      }
    }
    if (drag == 1 && (type=="blackhole"||manager.current==1)) {
      press = false;
      pos.x = mouseX;
      pos.y = mouseY;
      speed.set(new PVector(mouseX-pmouseX, mouseY-pmouseY));
    }
  }
  void pressCheck() {
    if (press == true && (type == "blackhole" || manager.current==1)) {
      if (dist(pos.x, pos.y, mouseX, mouseY) < diameter/2) {
        drag = 1;
      }
    }
  }

  void display() {
    PImage draw;
    if (abs(angSpeed) > 0.2 && type.equals("blackhole")==false) {
      draw = dizzyImage;
    } else if (isWorried == true || isWorried2 == true) {
      draw = worriedImage;
    } else if (isAngry == true) {
      draw = angryImage;
    } else {
      draw = image;
    }

    fill(colors[index]);
    translate(pos.x, pos.y);
    rotate(angle*rotatingCons);
    if(type.equals("rabbit")) {
      translate(-0.2*rabbitW,-.45*rabbitW);
      rotate(20*angle);
      image(ears,0,-.25*rabbitW);
      rotate(-20*angle);
      translate(0.4*rabbitW,0);
      translate(-.1*rabbitW,0);
      rotate(-20*angle);
      image(ears,0,-.25*rabbitW);
      rotate(20*angle);
      translate(.1*rabbitW,0);

      translate(-0.2*rabbitW,.45*rabbitW);
    }
    image(draw, 0, 0);
    rotate(-angle*rotatingCons);
    translate(-pos.x, -pos.y);
  }
}
