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
    CONSTRAINT userinfo_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id)
);

CREATE TABLE "Meal" (
    id SERIAL PRIMARY KEY,
    user_id INT,
    date TIMESTAMP,
    type VARCHAR(255),
    description VARCHAR(255) NOT NULL,
    CONSTRAINT meal_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id)
);

CREATE TABLE "Dish" (
    id SERIAL PRIMARY KEY,
    meal_id INT,
    quantity INT,
    CONSTRAINT dish_meal_id_fkey FOREIGN KEY (meal_id) REFERENCES "Meal"(id)
);

CREATE TABLE "DishInfo" (
    id SERIAL PRIMARY KEY,
    dish_id BIGINT,
    kcalories INT,
    carbs INT,
    proteins INT,
    fats INT,
    fibers INT,
    CONSTRAINT dishinfo_dish_id_fkey FOREIGN KEY (dish_id) REFERENCES "Dish"(id)
);

CREATE TABLE "Exercise" (
    id SERIAL PRIMARY KEY,
    user_id INT,
    date TIMESTAMP,
    sets INT,
    reps INT,
    weight_used INT,
    time_passed INT,
    CONSTRAINT exercise_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id)
);

CREATE TABLE "ExerciseInfo" (
    id SERIAL PRIMARY KEY,
    exercise_id BIGINT NOT NULL,
    name VARCHAR(255),
    target_muscle_group VARCHAR(255),
    kcalories_burn_per_rep INT,
    CONSTRAINT exerciseinfo_exercise_id_fkey FOREIGN KEY (exercise_id) REFERENCES "Exercise"(id)
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
    CONSTRAINT note_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id),
    CONSTRAINT note_categorytag_id_fkey FOREIGN KEY (categoryTag_id) REFERENCES "CategoryTag"(id)
);

CREATE TABLE "DailyMood" (
    id BIGINT NOT NULL PRIMARY KEY,
    categoryTag_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT dailymood_categorytag_id_fkey FOREIGN KEY (categoryTag_id) REFERENCES "CategoryTag"(id),
    CONSTRAINT dailymood_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id)
);

CREATE TABLE "DailyNote" (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    date TIMESTAMP NOT NULL,
    title VARCHAR(255),
    description TEXT,
    CONSTRAINT dailynote_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id)
);
