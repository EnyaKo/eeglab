# Add achartengine-1.0.0.jar as Library
1. put the jar (achartengine-1.0.0.jar) into libs folder
2. add the following line in "dependencies" in the app/src/build.gradle file  
   -> compile files ('libs/achartengine-1.0.0.jar')
3. Click on the "Sync Project with Gradle files"(Left to AVD manager Button on the topbar)

# Get Time from Timer
- minute: MainActivity.min
- second: MainActivity.sec

# Run the Project
1. Run
2. Click on the Button "Start Recording"
3. Start playing music from java class playMusic; in the mean while, a thread handler will start the timer \and another thread handler will display the current eeg signal on screen
4. After the music stop, click on the Button "Stop Recording"
5. It will jump into another activity, showing subject's result, including the chord and interval chart and if the subject is a musician.

# Musician Evaluation
- An evaluation to evaluate if the subject is a musician--show as a scale, ex. 7/10
- set boolean musician in class Result
