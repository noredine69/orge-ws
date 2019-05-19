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

- Ajout d'un package Core notamment pour la gestion des exceptions et erreur
- Refactoring (y'en a surement besoin) : parsing des règles en entrée avec GSON
- Mise en place d'un système d'authentification (avec jeton JWT) et un endpoint spécifique:
  - d'abord via API-KEY stockée en dur dans un fichier de conf
  - puis via authentification login/password stockés en bdd
- Mise en place de règles de sécurité (csrf, cors, xss etc...)
- Support de https
- Etoffer les Tests unitaires, et d'intégration
- Supprimer les imports inutiles


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