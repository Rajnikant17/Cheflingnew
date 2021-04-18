# Cheflingnew

Library used :

1 Navigation component
2 Hilt Dependency injection
3 View binding
4 LiveData
5 ViewBinding
6 Glide
7 Room Database
8 Retrofit
9 Coroutine
10 Workmanager
11 Databinding
12 Fused Location Provider Api

Used MVVM architecture.

There are three modules in this project , "App" , "MyApiServicesModule"  and "localDatabase" . "App" is the default module and a separate
module is being created for storing the class related to Api Services and a seprate module i.e "localDatabase"
for storing the data in roomdatabase . Used navigation component to achieve a single activity app eventhough here only one screen is there
but still i have used navigation component just to demonstrate how would i have done if there is more than one screen .


Brief workflow.

Initially when app is launched for first time then Periodic Work Request of Workmanager
will get initiallized with time interval of 12 hours , from inside the Worker class of
WorkManager api will get called in every 12 hours and then data will get updated into the Room Database and Whenever the
Data is updated inside RoomDatabase then Live Data will observe the Data from Roomdatabase and update the UI .

Two button are there at the bottom of the UI for Turning off the automatic update of the UI and the other one is for
latest weather info at any point of time and then Updated the UI using Live data.

Note*

1. Since Workmanger is the Api which take care of Batter optimization more than any other api's while doing the background task for
deferrable and guranteed work but still with some chinese mobile with some API Level , it doesn't executed when the app is killed
but rescheduled when the app is starts again , this is alredy a bug reported to google for chinese OEM , but the same behaviour is also
observed for other api's as well i.e even they also doesn't get executed in background when the app is killed .
So that's why workmanager may work in little unexpectedly behaviour but there is no work around with other Api's also .
But The work manager worked when I provided the application with "autostart permission".

2. Since google has updated it's policy that location shouldn't be fetched from background for that we need to use the Foreground Services but here
Foreground Services is not required so that's why it will fetch the updated location each time app is started and storted in the SharedPreference.

3. Alert data is not getting from the Api that's why not showing.