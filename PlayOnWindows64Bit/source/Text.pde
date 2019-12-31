class Text {
  String text;
  float x;
  float y;

  Text(String t, float xcoord, float ycoord) {
    text = t;
    x = xcoord;
    y = ycoord;
  }

  void update() {
    fill(0);
    text(text, x, y);
  }
}
