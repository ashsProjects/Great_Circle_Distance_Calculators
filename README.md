# Great Circle Distance Calculator
This program calculates the distances between coordinates using a possible of three formulas: Vincenty, Cosines, or Haversine. You can choose to use places through the command line or through a file. I would recommend using the file for more than 5 places as it becomes cumbersome to keep adding it through the shell. You can also choose to generate any number of places and then do the calculations. See input for options on interacting with the program, and see outputs for the output format.

## Project Structure
```
This package includes the following files.
|--src/main/java
    |--Cosines.java [Used to calculate distances between coordinates using the Cosines formula]
    |--Distances.java [Extends the LinkedHashMap for storing distance information]
    |--FormulaFactory.java [Used by Main.java to get the appropriate calculator using a Singleton Factory]
    |--GreatCircleDistance.java [An interface that all calculators implement]
    |--Haversine.java [Used to calculate distances between coordinates using the Haversine formula]
    |--Main.java [Coordinates calculations using inputs from users; main file]
    |--Place.java [Extends LinkedHashMap to store name, lat, and lon about places entered through shell or file]
    |--Places.java [Extends ArrayList to store place objects]
    |--Vincenty.java [Used to calculate distances between coordinates using the Vincenty formula; is default]
|--build.gradle [The file used to build this assignment]
|--output.txt [Output produced by the program that contains distances from one place to the next and the final total distance]
|--places.txt [List of places generated by the program]
|--README.txt [This file]
```

## Input
You can interact with the program using the console. Here is a breakdown of the possible inputs:
```
Initial prompt
    |--1 (read from command line)
        |-- 2 (number of places to use)
            |-- name1, -90, 180 (info for place 1; notice name)
            |-- -23.00, 78.12 (info for place 2; notice no name)
            |-- 1 (formula to use)
            |-- 3959 (radius to use)
    |--2 (read from file)
        |--1 (run from existing file)
            |--example.txt
            |-- (formula to use; nothing entered for default)
            |-- (radius to use; nothing entered for default)
        |-- 2 (generate new file with places)
            |-- 10 (number of places to generate)
            |-- (seed to use for coordinates; nothing entered for no seed)
            |-- 2 (formula to use)
            |-- 1010 (radius to use)
    |--q (exit)
```
./places.txt contains 15 places generated by the program using no random seed to be used for calculation

## Output
After selecting all of the options, the output will be written to output.txt. In here, you can see the distances between pairs of places. For example, the first line is the distance between place1 and place2. The last line is the distance between the last line and the first line, which is the same as the first line. If there is only one place entered, the distance will always be 0. The total distance is given at the end along with information on what the parameters were used i.e. number of places, formula used for calculation (default is Vincenty), and the earth radius (default is 3959 for miles). 

See ./output for example on the previously mentioned 15 places in input

## Errors
Possible errors and how to fix them:
1. *Invalid radius*: make sure the radius you entered is a number or sinply press enter for default
2. *Invalid formula*: make sure the formula is valid; enter (1, 2, or 3) or sinply press enter for default
3. *File not found*: make sure the file exists and the path to that file is correct
4. *Invalid lat/lon found in file*: make sure all lat/lon value are numbers when using own file
5. *Invalid input found in file*: make sure there are either 2 (lat,lon) or 3 (name, lat, lon) values only and that they are separated by commas; spaces do not matter 
6. *Coordinates out of bounds*: make sure all lats are within (-90, 90) and all lons are within (-180, 180)

## Dependencies
- Java 20
- Gradle 8.7

## Compiling
Clone the repository using:
```bash
$ git clone https://github.com/ashsProjects/Great_Circle_Distance_Calculator.git
```
Compile the Program:
```bash
$ gradle build
```
Run the Program:
```bash
$ java -jar ./build/libs/Great_Circle_Distance_Calculators.jar
```
To clean:
```bash
$ gradle clean
```