# Android Developer Nanodegree

Projects while attending the Android Developer Nanodegree program at Udacity

IMPORTANT API KEY note for Project 1/2 - Popular Movies App:

Use your own API key for the usage of The Movie DB API. Place a file called 'themoviedb.txt' in the
resources folder /res/raw (create if not exists). The file only has one single line, namely the API key.

##P0 - My Portfolio App

This simple app shows buttons, which start the apps developed in the course projects. 

<img style="position: center;" src="static/screenshots/P0 - My Portfolio App.png" width="300">

##P1 - Popular Movies Stage 1

Popular Movies is an app, which enables the user to instantly look up the most popular (or top rated) movies. Starting in a grid view, the posters of the movies are shown. When the user selects a movie, a detail view shows further information, like title, year, rating, and a short description. Movie data is asynchronously fetched from [TheMovieDB.org](https://www.themoviedb.org/). The user is able to sort the movies by rating, or popularity. 


[Screenrecord on Youtube](https://www.youtube.com/watch?v=ZUfGoRsqetg)
    
##P2 - Popular Movies Stage 2

Stage 2 of Popular Movies enhances the experience of the app. The user is now able to watch trailers, read reviews, and also an offline functionality is introduced. When a movie is marked as a "favorite", the movie information gets stored in a local database, which preserves the data in a persistent manner. So the user can see movie information of favorites also when there is no internet connection. To access the favorites, another toolbar entry is added. 
Finally, the app design is optimized for tablet usage, providing a master-detail layout.

[Screenrecord Phone Layout on Youtube](https://www.youtube.com/watch?v=iGYWjl--L5s)

<img style="position: center;" src="static/screenshots/P2 - Popular Movies Stage 2_Phone_1.png" width="300">
<img style="position: center;" src="static/screenshots/P2 - Popular Movies Stage 2_Phone_2.png" width="300">

<img style="position: center;" src="static/screenshots/P2 - Popular Movies Stage 2_Tablet_1.png" width="600">
<img style="position: center;" src="static/screenshots/P2 - Popular Movies Stage 2_Tablet_2.png" width="600">
<img style="position: center;" src="static/screenshots/P2 - Popular Movies Stage 2_Tablet_3.png" width="600">

##P3 - Stock Hawk

During this project a basically working app should be enhanced in a way, so it is "ready for production", for publishment e.g. via the Google Play Store. Stock Hawk is an app, which provides data from the Yahoo Finance API. The user can search for stock symbols and add them to his watch list (stored in a local database). 

Based on user feedback, certain aspects should be enhanced/implemented, including Accessibility (A11y) features, localization (L10n), adding a widget, providing a chart which shows the values over time and finally, some error handling and better user feedback.

