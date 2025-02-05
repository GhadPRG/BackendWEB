# To do list
## Sezione User 

- `POST /api/login`: semplice JSON con username e password. Restituisce token JWT in un JSON.

- `POST /api/register`: registrazioni con JSON
Restituisce un JSON con l'esito dell'operazione. 
Esempio di JSON:
```json
{
    "username": "jane_smith",
    "firstname": "Jane",
    "lastname": "Smith",
    "password": "StrongPass!456",
    "email": "jane.smith@example.com",
    "gender": "Female",
    "birthDate": "1992-09-23",
    "height": 1.68,
    "weight": 62.3,
    "dailyCalories": 2200
}
```

- `GET /api/user` (attualmente `/api/auth/user` ma da modificare) : restituisce dati inerenti all'utente.

## Sezione Sportiva

- `GET /api/exercises`: Restituisce tutti gli esercizi creati dall'utente. 
```json
[
  {
    "id": 1,
    "name": "Squat",
    "notes": "Eseguito con bilanciere",
    "muscleGroup": "Legs",
    "reps": 10,
    "sets": 4,
    "kcalPerRep": 0.5,
    "weight": 80
  },
  {
    "id": 3,
    "name": "Bench Press",
    "notes": "Eseguito con bilanciere panca piana",
    "muscleGroup": "Chest",
    "reps": 8,
    "sets": 3,
    "kcalPerRep": 0.7,
    "weight": 100
  }
]
```
- `POST /api/exercises`: crea un esercizio. Restituisce l'id dell'esercizio creato.

Esempio JSON:
```json
{
  "name": "Squat",
  "notes": "Eseguito con bilanciere",
  "muscleGroup": "Legs",
  "reps": 10,
  "sets": 4,
  "kcalPerRep": 0.5,
  "weight": 80
}
```

- `GET /api/exercises/{id}`: Restituisce l'esercizio con quel id.
```json
{
    "id": 3,
    "name": "Bench Press",
    "notes": "Eseguito con bilanciere panca piana",
    "muscleGroup": "Chest",
    "reps": 8,
    "sets": 3,
    "kcalPerRep": 0.7,
    "weight": 100
}
```


ExerciseInfo: GET, tutti i valori -> Da mostrare per Scegliere che esercizio registrare;
Exercise:   GET, tutti i valori -> Per avere informazioni sugli esercizi gia' fatti;
            POsT / DELETE, tutti i valori -> Per poter modificare un esercizio;

## Sezione Alimentare
Meal:   GET, tutti i valori a catena -> Per poter visualizzare i pasti registrati dall'utente; (Fatto)
        POST / DELETE, tutti i valori -> Per poter salvare/modificare pasti; 

Dish:   POST, tutti i valori -> Per poter modificare i valori inseriti dall'utente;
DishInfo: GET, tutti i valori -> Da mostrare per Scegliere che cibo registrare;
          POST, tutti i valori -> Per poter salvare/modificare cibi;

## Sezione Calendario

Category Group: GET, tutti i valori -> per poter vedere che nomi sono registrati;
                POST / DELETE, tutti i valori -> Per Poter modificare categorie;
Category Tag:   GET, tutti i valori -> Per poter vedere i tag associati alle categorie;
                POST/ DELETE, tutti i valori -> Per poter modificare i tag associati alle categorie;

## Da Vedere
Eventi(?)   GET: Per Poter visualizzare gli eventi gia' registrati
            POST/ DELETE, tutti i valori -> Per poter salvare/modificare eventi
## Sezione Mood

DailyMood:  GET, tutti i valori -> Per poter avere i mood da visualizzare;
            POST/ DELETE, tutti i valori -> Per poter modificare i vari mood registrati;

DailyNote:  GET, tutti i valori -> Per poter avere le note associate al giorno da visualizzare;

// Sezione Note
Note:   GET, tutti i valori -> Per poter avere le note da visualizzare;
        POST/ DELETE, tutti i valori -> Per poter modificare le varie note registrate;