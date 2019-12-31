class Food {
  PImage image;
  PVector pos;
  int index;
  float diameter;
  String type;
  boolean isCollected = false;




  Food(String t, PVector p) {
    type = t;
    pos = p;
    if (type.equals("hayBale")) {
      diameter = int(blackholeW);
      image = hayBale;
    }

    if (type.equals("feed")) {
      diameter = int(sheepW);
      image = feed;
    }
  }


  void update(Animal[] Animals) {


    for (Animal i : Animals) {
      if (dist(i.pos.x, i.pos.y, pos.x, pos.y) < 50 && !i.type.equals("blackhole")) {
        isCollected = true;
      }
    }
  }

  void display() {
    if (isCollected == false) {
      translate(pos.x, pos.y);
      image(image, 0, 0);
      translate(-pos.x, -pos.y);
    }
    if (isCollected == true) {
      image(image, 0, 0);
    }
  }
}
