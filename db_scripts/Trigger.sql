
-- Define the function
CREATE OR REPLACE FUNCTION insert_teacher_or_student()
RETURNS TRIGGER AS $$
BEGIN
    -- If the accountType is 'Teacher', insert into the teachers table
    IF NEW.accountType = 'Teacher' THEN
        INSERT INTO teachers (id, username, name) 
        VALUES (NEW.id, NEW.username, NEW.name);
    
    -- If the accountType is 'Student', insert into the students table
    ELSIF NEW.accountType = 'Student' THEN
        INSERT INTO students (id, username, name) 
        VALUES (NEW.id, NEW.username, NEW.name);
    END IF;
    
    -- Return the new row to be inserted into the users table
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Create the trigger with the correct function name
CREATE OR REPLACE TRIGGER trg_insert_student_or_teacher
AFTER INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION insert_teacher_or_student();