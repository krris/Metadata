#INSTALACJA PLAY FRAMEWORK:


1) Sciągnij najnowszą wersję Play'a:
http://www.playframework.org/download

2) Dodaj scieżkę Play'a do PATH

    export PATH=$PATH:/path/to/play

3) Aby sprawdzić czy Play jest poprawnie zainstalowany:

    $ play help

Dokumentacja odnośnie instalacji na stronie Play:
[http://www.playframework.org/documentation/2.0.2/Installing](http://www.playframework.org/documentation/2.0.2/Installing)


#TWORZENIE I URUCHOMIENIE PIERWSZEJ APLIKACJ:

1) Uruchom w terminalu poleceni:

    $ play new firstApp

I wybierz: "Create a simple Java application"

2) Przejdź do foloderu z aplikacją:

    $ cd firstApp

3) Uruchom Play'a:

    $ play

4) Uruchom aplikację:

    $ run

5) Teraz wpisz w przeglądarce internetowej:

    http://localhost:9000/



#UŻYWANIE IDE:

http://www.playframework.org/documentation/2.0.2/IDE


#ANATOMIA APLIKACJI W PLAY:

http://www.playframework.org/documentation/2.0.2/Anatomy


#URUCHOMIENIE APLIKACJI METADATA:

1) Aby uruchomić aplikację, potrzebny jest zainstalowany MySQL.
   Do obsługi graficznej bazy danych można zainstalować "MySql Workbench"

2) Stwórz nową, pustą bazę danych w MySQL'u, np: "metadata_db"

3) Edytuj plik application.conf, który znajduje się w folderze /conf
   i zmień następujące linie:

    db.default.url="jdbc:mysql://localhost/BAZA_DANYCH_PROJEKTU"
    db.default.user=UZYTKOWNIK
    db.default.password=HASLO

Przykładowa konfiguracja może wyglądać następująco:

    db.default.url="jdbc:mysql://localhost/metadata_db"
    db.default.user=root
    db.default.password=niesamowicie_mocne_haslo

4) Uruchom terminal i przejdź do katalogu projektu i uruchom:
    
    $ play
    $ run

5) Aplikacja będzie widoczna w przeglądarce:
    http://localhost:9000/



#OGOLNE RADY PODCZAS KORZYSTANIA Z FRAMEWORK'U PLAY:

1) Dokumentacja dla osób piszących w Javie na stronie:
    http://www.playframework.org/documentation/2.0.2/JavaHome

2) Najważniejsze tematy z dokumentacj, które pomogą w zrozumieniu aplikacji "metadata":
* Actions, Controllers and Results 
  http://www.playframework.org/documentation/2.0.2/JavaActions

* HTTP routing
	http://www.playframework.org/documentation/2.0.2/JavaRouting

* Session and Flash scopes
	http://www.playframework.org/documentation/2.0.2/JavaSessionFlash
    
* The template engine (czyli w skrócie zrozumienie kodu, który jest w plikach o rozszerzeniu .scala.html w folderze /views) 
	http://www.playframework.org/documentation/2.0.2/JavaTemplates
	http://www.playframework.org/documentation/2.0.2/JavaTemplateUseCases

* Using the Ebean ORM
	http://www.playframework.org/documentation/2.0.2/JavaEbean
    
* Form template helpers
	http://www.playframework.org/documentation/2.0.2/JavaFormHelpers


3) W aplikacji wykorzystany jest "Bootstrap, from Twitter", który pomaga w tworzeniu ładniejszego wyglądu strony.
    http://twitter.github.com/bootstrap/index.html
Pliki .css i .js są już zaimportowane do projektu w pliku main.scala.html.
    
Aby dodać własnie pliki .css lub .js konieczne jest ich skopiowanie do odpowiednio:
- pliki .css do folderu /public/stylesheets
- pliki .js do folderu /public/javascripts

Następnie pliki .css dodajemy w kodzie main.scala.html w sekcji <head>, np:
``` html
<link rel="stylesheet" media="screen" href="@routes.Assets.at("stylesheets/beautiful.css")">
```

A pliki .js tak samo w main.scala.html tylko pod koniec sekcji <body>
``` html
    <script src="@routes.Assets.at("javascripts/bootstrap-fileupload.js")"></script> 
```

4) Do ułatwienia korzystania z bazy danych wykorzystany jest EBean ORM:
* dokumentacja: http://www.avaje.org/ebean/documentation.html 
* prosty przykład: http://www.playframework.org/documentation/2.0.2/JavaEbean



#URUCHOMIENIE APLIKACJI NA SERWERZE ZEWNĘTRZNYM:

1) Przed instalacją aplikacji, edytuj plik `application.conf`, który znajduje 
   się w folderze `/conf` i zmień następującą linie:

    XForwardedHost="http://ip-krris.rhcloud.com/"

Zamiast `http://ip-krris.rhcloud.com/` wpisz swój adres strony, na której będzie aplikacja.

2) Aby aplikacja działała, potrzebny jest MySQL. Stwórz nową, pustą bazę danych w MySQL'u.

3) Edytuj plik application.conf, który znajduje się w folderze `/conf` i zmień następujące linie zgodnie z konfiguracją:

    db.default.url="jdbc:mysql://localhost/BAZA_DANYCH_PROJEKTU"
    db.default.user=UZYTKOWNIK
    db.default.password=HASLO


#URUCHOMIENIE APLIKACJI NA OpenShift (https://openshift.redhat.com):

1) Stwórz konto na OpenShift: 
    https://openshift.redhat.com/app/account/new 

2) Przejdź do katalogu z aplikacją:
    $ cd metadata
    $ git init

3) Stwórz diy (do-it-yourself) aplikację
    $ rhc app create -a metadata -t diy-0.1 --nogit -l TWOJ_LOGIN
    
Skopiuj pokazany "git url". Zapamiętaj również podany adres url strony.

4) Dodaj "git url" jako "remote repo"
    
    $ git remote add origin TWOJ_GIT_URL
    $ git pull -s recursive -X theirs origin master
    $ git add .
    $ git commit -m "initial deploy"

5)

    $ git remote add quickstart -m master git://github.com/opensas/play2-openshift-quickstart.git
    $ git pull -s recursive -X theirs quickstart master

6)  Edytuj plik application.conf, który znajduje się w folderze metadata/conf i zmień następującą linie:

    XForwardedHost="http://ip-krris.rhcloud.com/"

  Zamiast `http://ip-krris.rhcloud.com/` wpisz wcześniej zapamiętany adres strony.

7)  Dodaj obsługę MySQL'a i phpMyAdmin:

    $ rhc app cartridge add -a metadata -c mysql-5.1
    $ rhc app cartridge add -a metadata -c phpmyadmin-3.4

8) Edytuj plik `/conf/openshift.conf` i odkomentuj nastęþujące linie:

    # openshift mysql database
    db.default.driver=com.mysql.jdbc.Driver
    db.default.url="jdbc:mysql://"${OPENSHIFT_DB_HOST}":"${OPENSHIFT_DB_PORT}/${OPENSHIFT_APP_NAME}
    db.default.user=${OPENSHIFT_DB_USERNAME}
    db.default.password=${OPENSHIFT_DB_PASSWORD}

9)  Następnie kompiluj program:
    
    $ play clean compile stage

10)  Dodaj zmiany do repozytorium:

    $ git add .
    $ git commit -m "a nice message"
    $ git push origin

11) Teraz twoja aplikacja jest dostępna w sieci.

(Więcej informacji odnośnie instalacji aplikacji Play na OpenShift:
https://github.com/opensas/play2-openshift-quickstart )


