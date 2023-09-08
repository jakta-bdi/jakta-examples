# JaKtA systems examples
This repository contains some JaKtA implementation to test the syntax and the execution of the agent framework.


## Prerequisites
You need:
* A terminal, bash compatible preferred
* [Java 11+](https://adoptium.net/) installed and working
    - Java can be downloaded from [https://adoptium.net/](https://adoptium.net/)
* [Git](https://git-scm.com) installed and working
    - Git can be downloaded from https://git-scm.com

## Extremely-Quick-Start -- `(ba|z|fi)?sh` users only

* requires a Unix terminal (`(ba|z|fi)?sh`)
* `curl` must be installed
* run
```
curl https://raw.githubusercontent.com/jakta-bdi/jakta-examples/main/jakta-example.sh | bash
```
* the repository is in your `Downloads` folder for further inspection

## Quick-Start
On a terminal, type the following commands:
1. ```git clone https://github.com/jakta-bdi/jakta-examples.git```
2. ```cd jakta-examples```
3. Explore the `src` folder, containing examples implemented using `JaKtA` DSL. 
Choose one file that you want to launch, for example `PingPong`.
4. Depending on the platform, launch:
   * Bash compatible (Linux, MacOS X, Git Bash, Cygwin): `./gradlew jaktaPingPong`
   * Windows native (cmd.exe, Powershell): `gradlew.bat jaktaPingPong`

In addition, you can run `./gradlew tasks` (or `gradlew.bat tasks` if using Windows) to see all the available examples that you can run.
JaKtA examples are visible under the `JaKtA examples tasks` section.

## Create a JaKtA project
You can create your own project importing `JaKtA DSL` library from the maven central [repository](https://central.sonatype.com/artifact/it.unibo.jakta/jakta-dsl).
