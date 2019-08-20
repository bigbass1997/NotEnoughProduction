[![License: CC BY-NC-SA 4.0](https://licensebuttons.net/l/by-nc-sa/4.0/80x15.png)](https://creativecommons.org/licenses/by-nc-sa/4.0/)
### Description
NotEnoughProduction is an ongoing project to provide a versatile tool primarily to modded minecraft players. The tool will be capable of viewing recipes without running Minecraft. But more importantly, it will be able to display as many recipes simultaneously as needed while providing features to assist in building complex production chains.

In short, NEP is a combination of NEI, Excel, and flowcharts, wrapped up into a single independent tool.

Recipes are loaded into the program in a JSON formatted file. RecEx is a minecraft mod being developed alongside this tool which allows users to export recipes from the game automatically, during runtime. This means that any recipe changes that occur during postInit, or through MineTweaker, will be active in the exported data.

NEP will include a selection of pre-exported recipe sets, via automated download from a remote host. That way, most users will not need to install any addtional mods to their game to make use of this tool.

### Feature Roadmap
All planned and WIP features can be found in [this repo's Github Projects](https://github.com/bigbass1997/NotEnoughProduction/projects). Everything is roughly laid out in order of priority (top has highest priority), although I may skip around here and there depending on what the feature involves, code-wise. New ideas for features will be added whenever I can, so as to keep the community as up to date as possible.

### FAQ
##### What language is this made in?
Java, using the LibGDX framework, and some other libraries (check the build.gradle files for specifics).
##### What platforms will you support?
Only desktop for the foreseeable future. Android has weird quirks that make developing for it a pain, especially when using static objects. LibGDX can deploy to the web, via GWT, but this has even more limitations, and is just not feasible for this type of application. iOS is just as wonky, and Apple requires developers to pay $100+ just to attempt to submit apps to their appstore.

### Usage & Downloads
Pre-compiled versions of the program are found via [Releases](https://github.com/bigbass1997/NotEnoughProduction/releases). For the time being, please refer to the first release (v0.0.1) for important details regarding the operation of the program. This section of the README will be updated later with more detailed instructions.

### Building
Gradle is used as the dependency and build management software. Included in the repo is the Gradle Wrapper which allows you to run gradle commands from the root directory of the project. You can compile and run the program with `gradlew run`. To build the source into a runnable jar, execute `gradlew lwjgl3:jar`.

If you need to add a new dependency (e.g. library, framework, etc), open `core/build.gradle` and add the maven identifier for whatever you are adding. Please try to use dependencies that are available via maven, instead of jar files that are found locally.

### License
This project/program is licensed under Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International, except where other licenses are included.

A full copy of the project license can be viewed in "LICENSE.txt", and a summary plus additional links are provided in "LICENSE.md".

### Contributions
Thank you to all those who will contribute or have contributed to this project already! Special thanks to Sampsa, as the recipe GUI formatting/styles were greatly inspired by their [Excel flowcharts](https://gtnh.miraheze.org/wiki/Sampsa's_Excellent_Flowcharts).

If you are interested in contributing, feel free to submit issues or pull requests as you see fit. While I am open to pull requests, please understand that the project is under heavy development at this time. So PR's may be rejected due to a rapidly changing backend, or because it may conflict with future plans for the program's source.

Code reviews or performance improvement pull requests may also be kept open until the core program features are in place.