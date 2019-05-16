# orge-ws
Api basée sur spring boot de calcul de frais pour les missions de freelance.
Un premier endpoint permet l'ajout d'une règle de calcul, et le second le calcul des frais selon les paramètres de la mission, du client et du freelance.



Build : 
mvn clean verify

Execution :

Dans le repertoire rest :

mvn spring-boot:run

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
      "firstmission":"2018-04-16 13:24:17.510Z",
      "last_mission":"2018-07-16 14:24:17.510Z"
   }
}



TODO :
- Calcul des frais selon les règles stockées
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
