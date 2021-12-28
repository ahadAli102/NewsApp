## NewsApp

This is an android app for online news.

The used components are:
1.MVVM architecture pattern
2.Intent Service
 3.Navigation Component
 4.Realtime Database 
 5.HandlerThead for multithreading
6. CACHE MEMORY
 
 
 Here user can SEE news, SEARCH news, FILTER news based upon his\her taste. SAVE the any particular news user likes.
 
 Filter type will be stored to shared preferences database and saved news will be stored in ROOM database.
 
As there is no saved filter pattern and no saved news, app will fetch by default query: 

https://newsdata.io/api/1/news?apikey=pub_2320f0868f525d0141226a957169cc43ec65&category=sports,health&q=madrid&country=gb

here base url is https://newsdata.io/api/1/ and filter pattern will be "news?apikey=pub_2320f0868f525d0141226a957169cc43ec65&category=sports,health&q=madrid&country=gb"

in every hit, user can get 10 news. after scrool to last position of the list new API call will be hit for thenext page data like

https://newsdata.io/api/1/news?apikey=pub_2320f0868f525d0141226a957169cc43ec65&category=sports,health&q=madrid&country=gb&page=1

here page=1 indicate data from 11-20 

to see the official documantation for the api visit https://newsapi.org/docs/get-started

Some video are given below to see the working of the app.

Frist the use of the app from frist install here API call and storing functionality are shown.

https://user-images.githubusercontent.com/79190482/147532791-c175033b-a471-432a-96b8-14ecb6996e96.mp4


Below the filter strategy is shown 

https://user-images.githubusercontent.com/79190482/147533091-966d1218-fff8-4561-99b5-e3761e865f7a.mp4


here is the prove that app is truly build on MVVM pattern


https://user-images.githubusercontent.com/79190482/147533296-551b4d10-26c3-47e9-ad0d-de6d0aee2999.mp4


app won't crush if internet isn't avaiable if internet isn't available then user will only see SAVED news from ROOM database.
and the image for every saved news will be stored in CACHE MEMORY

https://user-images.githubusercontent.com/79190482/147533854-ef77bb85-45ea-4d4c-a6fd-1cfe7630c7ec.mp4


here the workflow of the app

![image](https://user-images.githubusercontent.com/79190482/147534788-ef000be8-e532-4a70-8e9c-cf32824e28e4.png)
