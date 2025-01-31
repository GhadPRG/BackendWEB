ALTER TABLE "UserInfo" DROP CONSTRAINT userinfo_user_id_fkey;
ALTER TABLE "UserInfo"
    ADD CONSTRAINT userinfo_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;
ALTER TABLE "Meal" DROP CONSTRAINT meal_user_id_fkey;
ALTER TABLE "Meal"
    ADD CONSTRAINT meal_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;
ALTER TABLE "Exercise" DROP CONSTRAINT exercise_user_id_fkey;
ALTER TABLE "Exercise"
    ADD CONSTRAINT exercise_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;
ALTER TABLE "Note" DROP CONSTRAINT note_user_id_fkey;
ALTER TABLE "Note"
    ADD CONSTRAINT note_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;
ALTER TABLE "DailyMood" DROP CONSTRAINT dailymood_user_id_fkey;
ALTER TABLE "DailyMood"
    ADD CONSTRAINT dailymood_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;
ALTER TABLE "DailyNote" DROP CONSTRAINT dailynote_user_id_fkey;
ALTER TABLE "DailyNote"
    ADD CONSTRAINT dailynote_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE;