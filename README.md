# LibraryAssistant
Library Assistant detects beacons associated with specific artifacts and get their information from a API
For now we are getting Art and Artist information from the API and displays the information on the screen

Because this project is in initial stage, we are just getting data through a network layer and using callback to send the data back to UI
In future I will rewrite current implementation and will use MVVM architecture to query the API and receive the result.

For now this project is using basic bluetooth code through which you can detect beacon with specific UUID and their major, minor and proximity range (Near, Far, Away)
In future I might convert this into a beacon detection library or use another beacon librar.

This project is using:

Glide library to displaying Images

Retrofit library to make network calls
