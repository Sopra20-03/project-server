# Just One Game Server (FS 20 Group 3)

## Introduction

(here: the projects aim)

Just One is a cooperative board game for 3 to 7 players consisting of 13 rounds. In each round of the game, one of the players plays the role of a ‘guesser’, while the remaining players play the role of ‘clue writers’. The round’s guesser arbitrarily chooses a number which corresponds to a mystery word without taking a look at the word. The other players, ‘clue writers’, write down a one word clue for the round’s guesser who must figure out the mystery word based on the clues provided. But before the clues are given to the ‘guesser’, all duplicates are removed and thus the game becomes more interesting as giving clues which are too obvious may lead to backfire.

Our project, titled ‘Just One Game’, is a web implementation of the same board game and our aim is to provide a similar experience to the players from the comfort of their home. Modern web technology allows us to integrate more immersive and collaborative features which make the gameplay experience more enjoyable. Some of the additional features in the web game include:

‘Rival Mode’ where players can challenge each other and configure game settings such as change round timer, set streak points etc. to earn more score points.
‘Clue Validation’ feature which automatically validates the clues provided by the clue writers based on the difficulty settings and eliminates similar clues.
‘Virtual Players’ with configurable intelligence levels such as Friendly, Malicious & Double Agent.
‘Global Leaderboard’ which keeps track of not just the team scores but also the individual player scores which help determine statistics like who has the most points, average points per game and so on.

## Technologies

The application is built with the SpringBoot framework.

This service uses RESTful webservices to provide usage to clients. The requests are formatted in JSON.

The bot player utilizes the "Data Muse" API, which is an API for the English language that allows the use of RESTful calls to find words used in the same context as a specified word as well as their frequency

## High-level components

(here: 3-5 main components, their role, their correlation, reference main class with link)

## Launch & Deployment

(here: steps a new developer would have to take to get started with the application: commands to build and run locally, run tests, external dependencies, how to do releases)

## Roadmap

(here: top 2-3 features that new developers could add)

## Authors and acknowledgement

## License