# Scala Project
CHABAS Tom - LACOUR Bastien - VOURIOT Marie  
Github link : https://github.com/BastienLac/IStream

To start this project : run the "VoyageStream" file.
(Sometimes it doesn't work from the sbt shell cause the csv file is not found, so try running it with the green arrow)
## Context
For this project, we started with the idea of a travel app.
The goal is to be able to consult a list of available trips, to be able to choose one and to be able to display information
such as time or distance necessary.

To do this, we started with the idea of combining different methods of data processing such as using
an API or reading a CSV file.

## Case class
The important case class in our project is "Voyage". It's not huge but it has some information :
- id : A unique identifier
- depart : The departure city
- arrivee : The destination city


As said above, other attributes such as distance and time are not present in the class as they are
recovered using the API. However, if our project were larger, trips would be recorded
in database, and it would be interesting to add these fields directly in the class.  
Once this class was created, we moved on to the rest :

## CSV File
To create a list of available trips, we use a CSV file. It has 3 columns, each corresponding to a field of the case class Voyage.
In our test case, we did not put much travel, but it would be possible to add more.To read this file "voyage.csv", we use ZIO as well as the function "CSVReader.open", then we use an iterator
to browse the entire file. The recovered trips are then put in a list and displayed with "Console.printLine"

## API
The API we use is Google Distance Matrix. It allows you to recover the distance and time between two geographical points.
We can call the corresponding url using a key, then we use the function "fromJson" on the result obtained
in order to manipulate the data obtained.
