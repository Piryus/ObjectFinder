
# ObjectFinder

A simple program to find where models are placed in WoW. Just feed it ADTs and the name of a model, ObjectFinder will find where it's located on these ADTs, its scale and will even provide you with a command to copy/paste in-game in order to teleport yourself directly on the object.

## Getting started

There is two ways to get ObjectFinder. The easiest is to get the latest stable version from the release section on GitHub. You can also compile your own version, which allows you to get the last bug fixes and features without having to wait for a stable version to be released.

### Download

#### Stable version (already compiled)

You will need JRE 9, which is available to download [here](http://www.oracle.com/technetwork/java/javase/downloads/jre9-downloads-3848532.html).
1. Download the latest release of ObjectFinder [here](https://github.com/Piryus/ObjectFinder/releases).
2. Extract both ObjectFinder_X.jar and MapIdList.txt. The list may be outdated if you are searching for models on recently released maps, so please download the latest one [here](https://github.com/Piryus/ObjectFinder/blob/master/MapIdList.txt). Make sure to place the list in the same folder as the JAR.
3. Launch ObjectFinder_X.jar, either by opening it with "Java(TM) Platform SE binary" or with the following command :
```
java -jar ObjectFinder_X.jar
```
(Replace ObjectFinder_X.jar with the JAR's name, it could be ObjectFinder_1.0.0.jar)

#### Compile your own version
Explanations coming soon.

## Usage

![demo](http://piryus.fr/OF/demo.gif)
1. In the "Path" field, enter the path to the folder containing the ADTs you want to scan. Every subfolders will be included in the search. You can also use the "Browse..." button.
2. In the "Objects" field, enter the models you are searching for. They must be coma-separated, with no spaces. Example :
	 ```
	 stormwind.wmo,test.m2,dalaran.wmo
	 ```
3. Choose in which coordinate system the positions of the models will be displayed.
	* **Classic** : The most common coordinate system used by the game. For more information, please visit the [ADT page on the WoWDev wiki](https://wowdev.wiki/ADT/v18#An_important_note_about_the_coordinate_system_used).
	* **!tele-ready** : Copy/paste-ready for some sandboxes and servers. Same as **Classic**, but with MapID. The MapID will only be displayed if it's from an official map, the IDs are extracted from [map.db2](https://wowdev.wiki/DB/Map). **MapID on custom maps won't appear.**
	* **worldport-ready** : Same as **!tele-ready** but for official servers. Only GMs can use that command. However, the command was accessible for some hours to players on RPT not long ago. So, always be ready.
4. Click the "Search !" button. Once the search finished, a popup alert will invite you to open report. Do so and you will have everything you need ! 
