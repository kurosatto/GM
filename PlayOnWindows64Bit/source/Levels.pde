Screen createLevel1() {
  int numberAnimal = 5;
  int numberFood = 4;
  Animal []animals = new Animal[numberAnimal];
  Food []foods = new Food[numberFood];
  foods = new Food[numberFood];
  int ballDiameter = int(chickenW);
  PVector rand = new PVector(500, 300);

  animals[0] = new Animal("blackhole", new PVector(screenW/2, screenH/2));
  drag = 0;
  //for (int i=1; i<numberAnimal; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));
  //  animals[i] = new Animal("chicken", rand);
  //}

  animals[1] = new Animal("rabbit", new PVector(screenW/4, screenH/4));
  animals[2] = new Animal("rabbit", new PVector(3*screenW/4, screenH/4));
  animals[3] = new Animal("rabbit", new PVector(screenW/4, 3*screenH/4));
  animals[4] = new Animal("rabbit", new PVector(3*screenW/4, 3*screenH/4));
  foods[0] = new Food("feed", new PVector(screenW/6, screenH/6));
  foods[1] = new Food("feed", new PVector(5*screenW/6, screenH/6));
  foods[2] = new Food("feed", new PVector(screenW/6, 5*screenH/6));
  foods[3] = new Food("feed", new PVector(5*screenW/6, 5*screenH/6));


  Screen screen = new Screen(0, animals, foods, new Text[0], new Button[0], 1, 2,0,0,0,0);
  return screen;
}


Screen createLevel2() {
  int numberAnimal = 5;
  int numberFood = 4;
  Animal []animals = new Animal[numberAnimal];
  Food []foods = new Food[numberFood];
  animals = new Animal[numberAnimal];
  int ballDiameter = int(sheepW);
  PVector rand = new PVector(500, 300);

  animals[0] = new Animal("blackhole", new PVector(screenW/2, screenH/2));
  drag = 0;
  //for (int i=1; i<numberAnimal; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));
  //  animals[i] = new Animal("sheep", rand);
  //}

  animals[1] = new Animal("sheep", new PVector(screenW/4, screenH/4));
  animals[2] = new Animal("sheep", new PVector(3*screenW/4, screenH/4));
  animals[3] = new Animal("sheep", new PVector(screenW/4, 3*screenH/4));
  animals[4] = new Animal("sheep", new PVector(3*screenW/4, 3*screenH/4));
  foods[0] = new Food("hayBale", new PVector(screenW/6, screenH/6));
  foods[1] = new Food("hayBale", new PVector(5*screenW/6, screenH/6));
  foods[2] = new Food("hayBale", new PVector(screenW/6, 5*screenH/6));
  foods[3] = new Food("hayBale", new PVector(5*screenW/6, 5*screenH/6));
  Screen screen  = new Screen(0, animals, foods, new Text[0], new Button[0], 1, 3,0,0,0,0);
  return screen;
}


Screen createLevel3() {
  int numberSheep = 4;
  int numberWolf = 1;    
  int numberFood = 4;
  Food []foods = new Food[numberFood];
  Animal []animals = new Animal[numberSheep + numberWolf + 1];
  int ballDiameter = int(sheepW);
  PVector rand = new PVector(500, 300);

  animals[0] = new Animal("blackhole", new PVector(screenW/2, screenH/2));
  drag = 0;
  //for (int i=1; i<numberSheep + 1; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));

  //  animals[i] = new Animal("sheep", rand);
  //}

  //for (int i=numberSheep + 1; i<numberSheep + numberWolf + 1; i++) {
  //  rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  do {
  //    rand = new PVector(random((spikeH + ballDiameter / 2), (screenW - spikeH - ballDiameter / 2)), random((spikeH + ballDiameter / 2), (screenH - spikeH - ballDiameter / 2)) );
  //  } while ((rand.x <550 & rand.x >450 ) | (rand.y <350 & rand.y >250 ));

  //  animals[i] = new Animal("wolf", rand);
  //}
  animals[1] = new Animal("sheep", new PVector(screenW/4, screenH/4));
  animals[2] = new Animal("sheep", new PVector(3*screenW/4, screenH/4));
  animals[3] = new Animal("sheep", new PVector(screenW/4, 3*screenH/4));
  animals[4] = new Animal("sheep", new PVector(3*screenW/4, 3*screenH/4));
  animals[5] = new Animal("wolf", new PVector(2*screenW/4, 3*screenH/4));
  foods[0] = new Food("hayBale", new PVector(screenW/6, screenH/6));
  foods[1] = new Food("hayBale", new PVector(5*screenW/6, screenH/6));
  foods[2] = new Food("hayBale", new PVector(screenW/6, 5*screenH/6));
  foods[3] = new Food("hayBale", new PVector(5*screenW/6, 5*screenH/6));
  Screen screen  = new Screen(0, animals, foods, new Text[0], new Button[0], 1, 4,0,0,0,0);
  return screen;
}
