# Esercitazione03
Esercitazione 03 del corso di Applicazioni Internet

## Contenuto

- PostGisDBCreator: app java standalone per popolare la versione geografica della tabella a partire dalla tabella BusStop (lat, long)
- MinPathCalc: app java standalone per calcolare le distanze fra le varie fermate (leggendo GeoDB) e memorizzarle in MongoDB
- PathFinder: app web estensione della precedente MapViewer che interroga postgis e mongoDB per restituire i percorsi più brevi

## Configurazione dei container

### PostGis

Il container di postgis è quello già configurato nel lab 2. Per farlo partire eseguire `docker start postgis`.

### MongoDB

Le istruzioni per creare il container con MongoDB sono le seguenti:

```bash
# creare un volume docker con il nome
docker volume create Laboratorio3
# scaricare l'immagine di mongo
docker pull mongo
# esegue l'immagine dando nome mongodb al container
# -d modalità detached
docker run --name mongodb -v Laboratorio3:/data/db -p 27017:27017 -d mongo
# controllare che sia in esecuzione e con la porta 27017
docker ps
```

Dopo che è stato creato il container, per i successivi avvii è sufficiente gestirne il ciclo di vita con i comandi `docker start mongodb` e `docker stop mongodb`.

### Generazione dei dati per l'applicazione web

Per eseguire l'applicazione è necessario generare i dati:

- eseguire il progetto `PostGisDBCreator` per generare la versione geografica della tabella `BusLineStop` in postgis
- eseguire il progetto `MinPathCalc` per generare la collection di mongo
