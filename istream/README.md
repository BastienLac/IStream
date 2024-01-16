# Projet Scala
CHABAS Tom - LACOUR Bastien - VOURIOT Marie  
Lien github : https://github.com/BastienLac/IStream

## Contexte
Pour ce projet, nous sommes partis sur l'idée d'une application de voyage.
Le but est de pouvoir consulter une liste de voyage disponibles, de pouvoir en choisir un et de pouvoir afficher des informations
sur ce dernier tel que le temps ou la distance nécéssaire.

Pour ce faire, nous sommes partis sur l'idée de combiner différentes méthodes de traitements de données comme l'utilisation
d'une API ou la lecture d'un fichier CSV.

## Case class
La case class au centre de notre projet est "Voyage". Elle n'est pas très grande mais comporte quelques attributs :
- id : Un identifiant unique
- depart : La ville de départ
- arrivee : La ville d'arrivée

Comme dit plus haut, d'autres attributs tels que la distance et le temps ne sont pas présent dans la classe car ils sont 
récupérés à l'aide de l'API. Cependant, si notre projet était d'une plus grande envergure, les voyages seraient enregistrer
en base de données et il serait intéressant d'ajouter ces champs directement dans la classe.  
Une fois cette classe créée, nous sommes passés sur le reste :

## Fichier CSV
Pour créer une liste de voyages disponibles, nous utilisons un fichier CSV. Il comporte 3 colonnes correspondantes chacune
à un champ de la case class Voyage. Dans notre cas de test, nous n'avons pas mis beaucoup de voyage mais cela serait tout à
fait possible d'en ajouter plus.  
Pour lire ce fichier "voyage.csv", nous utilisons ZIO ainsi que la fonction "CSVReader.open", puis nous utilisons un iterator
afin de parcourir l'entièreté du fichier.
Les voyages récupérés sont ensuite mis dans une liste et affichés avec "Console.printLine"

## API
L'API que nous utilisons est Google Distance Matrix. Elle permet de récuperer la distance et le temps entre deux points géographiques.
Nous pouvons appeler l'url correspondante à l'aide d'une clé, puis nous utilisons la fonction "fromJson" sur le résultat obtenu
afin de pouvoir manipuler par la suite les données obtenues.

