### Disclaimer
[IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended for working with the project, although you can use any IDE to work on the project, as long as your code's formatting will match one provided in the `.editorconfig` file. 

### Technical Information
This project uses a proprietary setup script called **Mk.4/5 Mod Setup Script** made by **Team Immersive Intelligence**.  
The setup script is meant to be modular (to a degree) and consists of multiple scripts-modules contained in the [./gradle/](./gradle/) folder, as well as the , [gradle.properties](./gradle.properties) and [./custom.gradle](./custom.gradle) files.
A more detailed description of it is avaliable after running `gradlew modSetupDocs`

### IntelliJ Setup
Import the project folder using `File>New...>Project from Existing Sources`
The project should automatically be recognized as a Gradle project and load the scripts, if it didn't load them, do it manually by using `Gradle>Reload All Gradle Projects`.

Navigate to `File>Project Structure` and set SDK to a Java 8 (1.8) SDK. In Team II we use Amazon Corretto 8 both locally and on the GitHub Build Action.
Navigate to `File>Settings>Build, Execution, Deployment>Build Tools>Gradle` and select 'Build and Run using' to `IntelliJ`, set Gradle's Java version to the same Amazon Coretto 8.

Run the `gradlew setupDecompWorkspace` Gradle task located in `forgegradle` category and proceed with the setup.
The task may error – in that case reload the Gradle project and run the task again.

Run `gradlew genIntellijRuns` task to create Run Configurations.
When selecting a run configuration, set the Classpath (-cp) to `ImmersiveIntelligence.Main`

Depending on your system, some additional settings may not have been applied. If you encounter issues, run `gradlew additionalIntellijSettings`.

You can display additional information about the **Mk.4/5 Mod Setup Script** using `gradlew modSetupDocs` .

### Non-Intellij Setup
In case you decide not to use IntelliJ, setup looks as follows:  
First, make sure java is installed and assigned to PATH, then in the root directory run the command: `gradlew setupDecompWorkspace`.
This should set up all required dependencies for II to build and run.
You can run the game by using `gradlew runClient` or `gradlew runServer`.

### Building a .jar file
To build a jarfile, use `gradlew build`. Note that by default it won't be signed and will be flagged as an unofficial build in the game.  
Credentials can be entered in the [./gradle.properties](./gradle.properties) file. 

### About Technology we use
OBJ-based models use our proprietary **AMT** library, included in `client.util.amt` in this mod's source code.  
3D Models are made using Blockbench and exported through our [IIToolkit](https://github.com/Team-Immersive-Intelligence/ii-blockbench-plugin) plugin.
Some code and assets are automatically generated through [ModWorks](https://github.com/Team-Immersive-Intelligence/ModworksProcessor), our annotation processor.  
If you wish to use these libraries in your project, message [@Pabilo8](https://github.com/Pabilo8/) directly at discord or `pabilo (at) iiteam (dot) net`.