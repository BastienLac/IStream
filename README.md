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
In this project, there are several case classes. The first one is ```Voyage```. It's not huge but it has some information :
- ```id``` : A unique identifier
- ```depart``` : The departure city
- ```arrivee``` : The destination city


There are also case classes that will be used to decode the JSON based on the data sent by the API. 
```DistanceResponse``` encapsulates all the other case classes. It represents the structure of the returned JSON and has the following fields:
- ```destination_addresses```: details of the destination address (Lyon, France)
- ```origin_addresses```: details of the origin address (Paris, France)
- ```rows```: which contains different elements
- ```status```: status of the request

```DistanceResponse``` has a List[Row]. ```Row``` is a case class that contains elements.
```Element``` has the following fields:
- ```distance```: information about the distance between the two destinations
- ```duration```: information about the travel time between the two destinations
- ```status```: status of the request

```Distance``` and ```Duration``` are case classes that both have a ```text``` and ```value``` field.

However, if our project were larger, trips would be recorded
in database, and it would be interesting to add these fields directly in the class.  
Once this class was created, we moved on to the rest :

## CSV File
To create a list of available trips, we use a CSV file. It has 3 columns, each corresponding to a field of the case class Voyage.
In our test case, we did not put much travel, but it would be possible to add more. To read this file "voyage.csv", we use ZIO as well as the function "CSVReader.open", then we use an iterator
to browse the entire file. The recovered trips are then put in a list and displayed with "Console.printLine".


After displaying the voyages to the user, they can choose one. We verify that they choose a valid voyage. If the user's input is not a number, an exception is raised. The ```fetchVoyage``` function is then executed. If the entered number does not correspond to any voyage, the code stops and specifies that the chosen voyage does not exist. This allows us to execute the API only if the data is validated.

## API
The API we use is Google Distance Matrix. It allows you to recover the distance and time between two geographical points.
We can call the corresponding url using a key, then we use the function "fromJson" on the result obtained to transform data in ```DistanceResponse```
in order to manipulate the data obtained.

This API is called in ```VoyageStream``` using the ```fetchData``` function. This function takes a destination and a departure as parameters and retrieves information using the API.

