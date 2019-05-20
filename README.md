# orge-ws
Api basée sur spring boot de calcul de frais pour les missions de freelance.
Un premier endpoint permet l'ajout d'une règle de calcul, et le second le calcul des frais selon les paramètres de la mission, du client et du freelance.

Build :
mvn clean verify

Execution :

Dans le repertoire rest :

mvn clean package -DskipTests && java -jar target/orge-ws-rest-1.0.0-SNAPSHOT.jar

Attention l'execution via le plugin maven n'est plus possible à cause de l'utilisation de la reflection.
Pour valider une demande de calcul de frais avec une règle, l'application génère et compile du code Java
 au Runtime (via le framework openfht).
 Toutefois il a été impossible d'utiliser le classpath de l'application, pour contourner ce problème
 le fat jar de l'application est décompresser dans un répertoire temporaire pour en extraire seulement le jar
 du module service, et de configurer le classpath de la lib avec.

L'accès au flux est soumis à authentification via API-Key, voici les paramètres : 
Nom du Header : Authorization (Champ Authorization dans swaggerUi)
Valeur : trb5tr94bs651v3r1v6serv51e6EC16E5c136c3e51qzc1z

Accès à la console H2 :

http://localhost:8080/h2/
login : sa
password : password

Accès à la console swagger :

http://localhost:8080

L'application redirige "/" vers "swagger-ui.html"

Flux d'exemple : 

/rule :
{  
   "name":"spain or repeat",
   "rate":{  
      "percent":8
   },
   "restrictions":{  
      "@or":[  
         {  
            "@mission.duration":{  
               "gt":"2months"
            }
         },
         {  
            "@commercialrelation.duration":{  
               "gt":"2months"
            }
         }
      ],
      "@client.location":{  
         "country":"ES"
      },
      "@freelancer.location":{  
         "country":"ES"
      }
   }
}

 - pour les durées ("mission.duration" et "commercialrelation.duration") les valeurs saisies doivent correspondre à la reg ex : "^([0-9]{1,5})(days|months|years)$"

 On peut donc avoir les possibles :

    - 2days
    - 13months
    - 25years
    - 12345days
En base de données, les durées sont stockées en nombre de jours, les mois correspondent à 30 jours.


 Pour le noeud restriction, seules les opérandes logiques "@and" et "@or" sont autorisées.
 Les opérandes de comparaison sont :

    - "gt" : " > " (greater than)
    - "lt" : " < " (lower than)
    - "gte" : " >= " (greater than or equals)
    - "lte" : " <= " (lower than or equals)
    - "neq" : " != " (not equals)
    - "eq" : " == " (equals)

/fee :

{  
   "client":{  
      "ip":"217.127.206.227"
   },
   "freelancer":{  
      "ip":"217.127.206.227"
   },
   "mission":{  
      "length":"4months"
   },   
   "commercialrelation":{  
      "firstmission":"2018-04-16T13:24:17.510Z",
      "last_mission":"2018-07-16T14:24:17.510Z"
   }
}



TODO :

- Corrige l'accès à la console H2 (demande de login)
- Ajout du script de gitlab-ci
- Support de https
- Ajouter des tests sur les services d'accès bdd
- Supprimer les imports inutiles
- Mise en place d'un système d'authentification (avec jeton JWT) et un endpoint spécifique:
  - puis via authentification login/password stockés en bdd

DONE:
- Initialisation du projet, avec découpage en module (mise en commun des dependances via module bom)
- Ecriture du fichier swagger de description des interfaces, et générations des DTOs
- Accès de l'interface SwaggerUI
- Création du service de Geolocation avec IpStack
- Support des datetime en entrée avec ThreeTen
- Mise en place des Mapper MyBatis
- Création du model de données pour le stockage des règles, et des frais
- Stockage des règles en bdd (base H2 actuellement)
- Ajout de l'incrément sur la PK des FeeRule
- Ajouter les champs country client, et freelancer
- Parsing des durées et stockage en bdd
- Calcul des frais selon les règles stockées
- Prise en compte du pays du client et du freelance (via la geolocalisation de leur IP)
- Mise en place d'un système d'authentification (avec jeton JWT) et un endpoint spécifique:
  - d'abord via API-KEY stockée en dur dans un fichier de conf
- Mise en place de règles de sécurité (csrf, cors, xss etc...)
- Refactoring (y'en a surement besoin) : parsing des règles en entrée avec GSON
- Etoffer les Tests unitaires, et d'intégration

NOT TO DO ANYMORE :
- Ajout d'un package Core notamment pour la gestion des exceptions et erreur
