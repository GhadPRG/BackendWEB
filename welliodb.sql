CREATE TABLE "User" (
                        id integer NOT NULL,
                        username character varying(255) NOT NULL,
                        password character varying(255) NOT NULL
);


CREATE TABLE "UserInfo" (
                            user_id integer NOT NULL,
                            first_name character varying(255),
                            last_name character varying(255),
                            email character varying(255),
                            birth date,
                            gender character varying(50),
                            height numeric(8,2),
                            weight numeric(8,2),
                            daily_kcalories integer
);



CREATE TABLE calendar_events (
                                 id integer NOT NULL,
                                 user_id integer NOT NULL,
                                 category_id integer,
                                 title character varying(255) NOT NULL,
                                 description text,
                                 start_event timestamp without time zone NOT NULL,
                                 end_event timestamp without time zone NOT NULL
);


CREATE TABLE categories (
                            id integer NOT NULL,
                            name character varying(100) NOT NULL,
                            description text,
                            color character varying(100)
);



CREATE TABLE dish_info (
                           id integer NOT NULL,
                           nome character varying(255),
                           kcalories integer,
                           carbs integer,
                           proteins integer,
                           fats integer,
                           fibers integer,
                           user_id integer
);



CREATE TABLE dishes (
                        id integer NOT NULL,
                        meal_id integer NOT NULL,
                        dish_info_id integer NOT NULL,
                        quantity integer DEFAULT 1,
                        unit character varying(255)
);



CREATE TABLE entity_tags (
                             entity_tag_id integer NOT NULL,
                             entity_id integer NOT NULL,
                             entity_type character varying(50) NOT NULL,
                             tag_id integer NOT NULL,
                             CONSTRAINT chk_entity_type CHECK (((entity_type)::text = ANY ((ARRAY['note'::character varying, 'mood'::character varying, 'calendar_event'::character varying])::text[])))
);


CREATE TABLE exercises (
                           id integer NOT NULL,
                           name character varying(100) NOT NULL,
                           notes text,
                           muscle_group character varying(50) NOT NULL,
                           reps integer NOT NULL,
                           sets integer NOT NULL,
                           kcal_per_rep numeric(5,2) NOT NULL,
                           weight numeric(5,2),
                           created_by integer NOT NULL,
                           CONSTRAINT exercises_muscle_group_check CHECK (((muscle_group)::text = ANY ((ARRAY['Back'::character varying, 'Chest'::character varying, 'Legs'::character varying, 'Core'::character varying, 'Arms'::character varying, 'Shoulders'::character varying])::text[])))
);


CREATE TABLE meals (
                       id integer NOT NULL,
                       user_id integer NOT NULL,
                       meal_type character varying(50),
                       meal_date date NOT NULL
);

CREATE TABLE mood_tracker (
                              id integer NOT NULL,
                              user_id integer NOT NULL,
                              mood_level integer NOT NULL,
                              mood_date date DEFAULT CURRENT_DATE NOT NULL,
                              notes text,
                              note_id integer,
                              CONSTRAINT mood_tracker_mood_level_check CHECK (((mood_level >= 1) AND (mood_level <= 5)))
);


CREATE TABLE notes (
                       id integer NOT NULL,
                       user_id integer NOT NULL,
                       title character varying(100) NOT NULL,
                       content text,
                       created_at date DEFAULT CURRENT_DATE
);



CREATE TABLE tags (
                      id integer NOT NULL,
                      category_id integer NOT NULL,
                      name character varying(100) NOT NULL,
                      description text
);


ALTER TABLE ONLY "UserInfo"
    ADD CONSTRAINT "UserInfo_email_key" UNIQUE (email);



ALTER TABLE ONLY "UserInfo"
    ADD CONSTRAINT "UserInfo_pkey" PRIMARY KEY (user_id);



ALTER TABLE ONLY "User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id);



ALTER TABLE ONLY "User"
    ADD CONSTRAINT "User_username_key" UNIQUE (username);



ALTER TABLE ONLY calendar_events
    ADD CONSTRAINT calendar_events_pkey PRIMARY KEY (id);


ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_name_key UNIQUE (name);


ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);



ALTER TABLE ONLY dish_info
    ADD CONSTRAINT dish_info_pkey PRIMARY KEY (id);



ALTER TABLE ONLY dishes
    ADD CONSTRAINT dishes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY entity_tags
    ADD CONSTRAINT entity_tags_entity_id_entity_type_tag_id_key UNIQUE (entity_id, entity_type, tag_id);



ALTER TABLE ONLY entity_tags
    ADD CONSTRAINT entity_tags_pkey PRIMARY KEY (entity_tag_id);


ALTER TABLE ONLY exercises
    ADD CONSTRAINT exercises_pkey PRIMARY KEY (id);


ALTER TABLE ONLY meals
    ADD CONSTRAINT meals_pkey PRIMARY KEY (id);


ALTER TABLE ONLY meals
    ADD CONSTRAINT meals_user_id_meal_type_meal_date_key UNIQUE (user_id, meal_type, meal_date);



ALTER TABLE ONLY mood_tracker
    ADD CONSTRAINT mood_tracker_pkey PRIMARY KEY (id);



ALTER TABLE ONLY notes
    ADD CONSTRAINT notes_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


ALTER TABLE ONLY dishes
    ADD CONSTRAINT dishes_dish_info_id_fkey FOREIGN KEY (dish_info_id) REFERENCES dish_info(id) ON DELETE CASCADE;


ALTER TABLE ONLY dishes
    ADD CONSTRAINT dishes_meal_id_fkey FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE;



ALTER TABLE ONLY dish_info
    ADD CONSTRAINT dishinfo_user_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;


ALTER TABLE ONLY entity_tags
    ADD CONSTRAINT entity_tags_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE;

ALTER TABLE ONLY exercises
    ADD CONSTRAINT exercises_created_by_fkey FOREIGN KEY (created_by) REFERENCES "User"(id) ON DELETE CASCADE;

ALTER TABLE ONLY mood_tracker
    ADD CONSTRAINT fk_note FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE SET NULL;


ALTER TABLE ONLY calendar_events
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;



ALTER TABLE ONLY meals
    ADD CONSTRAINT meals_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;


ALTER TABLE ONLY mood_tracker
    ADD CONSTRAINT mood_tracker_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;

ALTER TABLE ONLY notes
    ADD CONSTRAINT notes_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;



ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_category_id_fkey FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE;



ALTER TABLE ONLY "UserInfo"
    ADD CONSTRAINT userinfo_user_id_fkey FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;


