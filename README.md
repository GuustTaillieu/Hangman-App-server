# Hangman Server

## Description

This is a server for the Hangman App. It is built with Spring Boot and uses memory as a database since the app is not meant to be used by many users at the same time. And the app is also designed to remove all unused data after the user disconnects from the server.
I have tried to deploy the server on Heroku and Render but I have not been able to make it work. This means that the app can only be used locally. So thus not as an application and not on the web.
If the server would be deployed on the web, the app would be able to be used as a downloadable application and also on the web. Unfortunately, this is not the case.

## How to run the server

1. So before running the client, we will need to run the server locally. To do this, we will need to have `Java installed on our computer`. If you do not have Java installed, you can download it from [here](https://www.java.com/en/download/).

2. After installing Java, we will need to `download the server`. This can be done by clicking on the green button that says "Code" and then clicking on "Download ZIP".

3. After downloading the server, we will need to `unzip the file`.

4. After unzipping the file, we will need to `open the terminal and navigate to the folder where the server is located`. This can be done by typing the following command in the terminal:

```bash
cd path/to/the/server

(Example: cd C:\Users\{user}\Downloads\hangman-app-server)
```

After navigating to the folder where the server is located, we will need to run the server. This can be done by typing the following command in the terminal:

```bash
java -jar target/hangman-0.0.1-SNAPSHOT.jar
```

After running the server, we will need to run the client. This can be done by following the instructions in the README.md file in the client repository. The client repository can be found [here](https://github.com/GuustTaillieu/Hangman-App-client).
