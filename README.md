# LoginTimeoutApp
An Android app developed as part of Aldoid Hackathon to demonstrate auto timeout of logged in user.

# Working

## Logging in
User is first presented with the `LoginActivity`. Once the user enters the corrcect username and password, appropriate data is
written into the `SharedPreferences` and the user is presented with `UserActivity`. A runnable is launched here that keeps
checking the user's logged in state.

## Data Storage
User's loggedin state and metadata is stored in `SharedPreferences`.

## The runnable
The runnable thread is launched once the user logs in and is presented with the `UserActivity`. The thread activates every
second and checks if 20s have elapsed since the user's login. If yes, the user is logged out and presented with the
`LoginActivity`.

## While app is running
Once user logins on the `LoginActivity`, he is taken to the `UserActivity`. The user's login state and login time are stored
in `SharedPreferences`. The above mentioned runnable keeps track of the time elapsed and hence the user's session.

## On app close and resume
The `LoginActicity` is first launched when app starts. If user is already logged in and has a valid session, 
the app proceeds to `UserActivity`, else the user is asked to login. The validity of session is checked using the data stored
in teh `SharedPreferences`.

# Author
Mayank Rajoria
