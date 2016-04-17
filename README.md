# GlobalGameRules

[![Build Status](https://travis-ci.org/GoryMoon/GlobalGameRules.svg?branch=1.8)](https://travis-ci.org/GoryMoon/GlobalGameRules)

A mod that allows for global GameRules


Installation
---

```sh
git clone [git-repo-url] GlobalGameRules
cd GlobalGameRules
```
If you don't have [Gradle][1] installed on your computer you can use `gradlew` or `gradlew.bat` instead
If you're on Linux/Mac OS prepend the command with `./`

Start by running:
```sh
gradle setupDecompWorkspace
```
Then depending on your IDE follow the instructions below

##### Installing for Intellij IDEA
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Once it's finished you must close IntelliJ and run the following command:

```sh
gradle genIntellijRuns
```

##### Installing for Eclipse
```sh
gradle eclipse
```
Open Eclipse and switch your workspace to /eclipse/

License
----

GNU LGLP v.3


[1]:http://www.gradle.org/
