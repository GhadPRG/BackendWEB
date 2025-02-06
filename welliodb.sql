CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE "UserInfo" (
    user_id INT NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    birth DATE,
    gender VARCHAR(50),
    height DECIMAL(8, 2),
    weight DECIMAL(8, 2),
    daily_kcalories INT,
    CONSTRAINT userinfo_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);

CREATE TABLE dish_info (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    kcalories INT,
    carbs INT,
    proteins INT,
    fats INT,
    fibers INT,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);

CREATE TABLE meals (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    meal_type VARCHAR(50),
    meal_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE,
    UNIQUE (user_id, meal_type, meal_date)
);

CREATE TABLE dishes (
    id SERIAL PRIMARY KEY,
    meal_id INT NOT NULL,
    dish_info_id INT NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    FOREIGN KEY (dish_info_id) REFERENCES dish_info(id) ON DELETE CASCADE
);

-- Tabella Esercizi
CREATE TABLE exercises (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    notes TEXT,
    -- Molto figa sta cosa che ti verifica automaticamente se l'elemento che vuoi inserire è uguale a uno di quelli. FIGO
    muscle_group VARCHAR(50) NOT NULL CHECK (muscle_group IN ('Back', 'Chest', 'Legs', 'Core', 'Arms', 'Shoulders')),
    reps INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    met DECIMAL(5, 2) NOT NULL,
    weight DECIMAL(5, 2),
    created_by INTEGER NOT NULL REFERENCES "User"(id) ON DELETE CASCADE
);

-- Tabella Schede di Esercizi
CREATE TABLE workout_plans (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by INTEGER NOT NULL REFERENCES "User"(id) ON DELETE CASCADE
);

-- Tabella Relazione Scheda-Esercizi
CREATE TABLE workout_plan_exercises (
    id SERIAL PRIMARY KEY,
    workout_plan_id INTEGER NOT NULL REFERENCES workout_plans(id) ON DELETE CASCADE,
    exercise_id INTEGER NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    UNIQUE (workout_plan_id, exercise_id)
);

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE, -- Nome della categoria (es. "Priorità", "Emozioni")
    description TEXT,
    color VARCHAR(100)
);

CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    category_id INT NOT NULL, -- Riferimento alla categoria del tag
    name VARCHAR(100) NOT NULL, -- Nome del tag (es. "Importante", "Felice")
    description TEXT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE notes (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL, -- Riferimento all'utente proprietario della nota
    title VARCHAR(100) NOT NULL, -- Titolo della nota
    content TEXT,
    created_at DATE DEFAULT CURRENT_DATE, -- Data di creazione della nota
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);

CREATE TABLE mood_tracker (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL, -- Riferimento all'utente proprietario
    mood_level INT NOT NULL CHECK (mood_level BETWEEN 1 AND 5), -- Livello di umore (da 1 a 5)
    mood_date DATE NOT NULL DEFAULT CURRENT_DATE, -- Data della registrazione dell'umore
    notes TEXT, -- Note opzionali sull'umore
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);

CREATE TABLE entity_tags (
    entity_tag_id SERIAL PRIMARY KEY,
    entity_id INT NOT NULL, -- ID dell'entità (nota o mood)
    entity_type VARCHAR(50) NOT NULL, -- Tipo di entità ('note' o 'mood')
    tag_id INT NOT NULL, -- Riferimento al tag
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
    UNIQUE (entity_id, entity_type, tag_id)
);

