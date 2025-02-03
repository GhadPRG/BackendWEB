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
    kcal_per_rep DECIMAL(5, 2) NOT NULL,
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

CREATE TABLE "CategoryGroup" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE "CategoryTag" (
    id SERIAL PRIMARY KEY,
    group_id INT,
    tag VARCHAR(255),
    description TEXT,
    CONSTRAINT categorytag_group_id_fkey FOREIGN KEY (group_id) REFERENCES "CategoryGroup"(id)
);

CREATE TABLE "Note" (
    id SERIAL PRIMARY KEY,
    user_id INT,
    categoryTag_id BIGINT NOT NULL,
    title VARCHAR(255),
    description TEXT,
    CONSTRAINT note_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE,
    CONSTRAINT note_categorytag_id_fkey FOREIGN KEY (categoryTag_id) REFERENCES "CategoryTag"(id)
);

CREATE TABLE "DailyMood" (
    id BIGINT NOT NULL PRIMARY KEY,
    categoryTag_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT dailymood_categorytag_id_fkey FOREIGN KEY (categoryTag_id) REFERENCES "CategoryTag"(id),
    CONSTRAINT dailymood_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);

CREATE TABLE "DailyNote" (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    date TIMESTAMP NOT NULL,
    title VARCHAR(255),
    description TEXT,
    CONSTRAINT dailynote_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE
);


