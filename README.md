# Just One Game Server (FS 20 Group 3)

## Introduction

###### The original Game:
Just One is a cooperative board game for 3 to 7 players consisting of 13 rounds. In each round of the game, one of the players plays the role of a ‘guesser’, while the remaining players play the role of ‘clue writers’. The round’s guesser arbitrarily chooses a number which corresponds to a mystery word without taking a look at the word. The other players, ‘clue writers’, write down a one word clue for the round’s guesser who must figure out the mystery word based on the clues provided. But before the clues are given to the ‘guesser’, all duplicates are removed and thus the game becomes more interesting as giving clues which are too obvious may lead to backfire.

###### Our Project
Our project, titled ‘Just One Game’, is a web implementation of the same board game and our aim is to provide a similar experience to the players from the comfort of their home. Modern web technology allows us to integrate more immersive and collaborative features which make the gameplay experience more enjoyable.

###### Additional Features
- ‘Rival Mode’ where players can challenge each other and configure game settings such as change round timer and score individual points.
- ‘Clue Validation’ feature which automatically validates the clues provided by the clue writers and eliminates similar clues.
- ‘Virtual Players’ with configurable intelligence levels (friendly or malicious).
- ‘Global Leaderboard’ which keeps track of not just the team scores but also the individual player scores.
## Technologies

The application is built with the SpringBoot framework.

It uses RESTful webservices to provide usage to clients. The requests are formatted in JSON.

The bot player utilizes the "Data Muse" API, which is an API for the English language that allows the use of RESTful calls to find words used in the same context as a specified word as well as their frequency

## High-level components

The application contains several entities to store users, players, games, guesses, clues, etc. The entities are connected to each other by relationships.

The service classes take care of the functionality of the game. Each service is responsible for only one entity (single relationship principle).

The controller classes contain the REST requests. 

[main class](src/main/java/ch/uzh/ifi/seal/soprafs20/Application.java)

## Launch & Deployment

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

##### Build

```bash
./gradlew build
```

##### Run

```bash
./gradlew bootRun
```

##### Test

```bash
./gradlew test
```

##### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`


## Roadmap

Ideas for additional features:

- [ ] add a friendslist to each player and implement possibility to add friends
- [ ] add possibility to play private games and invite friends
- [ ] add timer to backend to make it possible to continue the game if a player leaves
- [ ] mark clues that contain multiple words as invalid

## Authors and acknowledgement


## License
MIT License

Copyright (c) [2020] [Shubhankar Joshi, Taylor McCants, Hannah Rohe, Tom Wartmann, Jonas Wittwer]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.