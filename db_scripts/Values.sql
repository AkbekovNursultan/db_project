-- Insert Admin into the 'users' table
INSERT INTO users (username, password, name, accountType)
VALUES ('admin', '123', 'Tamchy Kashiev', 'Admin');
-- Insert Teacher 1 into the 'users' table
INSERT INTO users (username, password, name, accountType)
VALUES ('john.doe', '123', 'John Doe', 'Teacher');

-- Insert Teacher 2 into the 'users' table
INSERT INTO users (username, password, name, accountType)
VALUES ('jane.smith', '123', 'Jane Smith', 'Teacher');

-- Insert Teacher 3 into the 'users' table
INSERT INTO users ( username, password, name, accountType)
VALUES ('emily.davis', '123', 'Emily Davis', 'Teacher');




-- Insert Student 1 into the 'users' table
INSERT INTO users ( username, password, name, accountType)
VALUES ('alice.johnson', '123', 'Alice Johnson', 'Student');

-- Insert Student 2 into the 'users' table
INSERT INTO users ( username, password, name, accountType)
VALUES ( 'bob.williams', '123', 'Bob Williams', 'Student');

-- Insert Student 3 into the 'users' table
INSERT INTO users ( username, password, name, accountType)
VALUES ( 'charlie.brown', '123', 'Charlie Brown', 'Student');




-- Insert Assignment 1 for Teacher 1 (John Doe) into the 'assignments' table
INSERT INTO assignments (name, description, teacher_id)
VALUES ('Math Homework 1', '1 + 1 = ?' , (SELECT id FROM teachers WHERE username = 'john.doe'));

-- Insert Assignment 2 for Teacher 2 (Jane Smith) into the 'assignments' table
INSERT INTO assignments (name, description, teacher_id)
VALUES ('Physics Project', 'E = mc^?' , (SELECT id FROM teachers WHERE username = 'jane.smith'));

-- Insert Assignment 3 for Teacher 3 (Emily Davis) into the 'assignments' table
INSERT INTO assignments (name, description, teacher_id)
VALUES ('History Essay', 'First President of Kyrgyzstan?' , (SELECT id FROM teachers WHERE username = 'emily.davis'));




-- Assign Student 1 (Alice Johnson) to Assignment 1 (Math Homework 1)
INSERT INTO students_assignments (student_id, assignment_id, grade, submission)
VALUES
    ((SELECT id FROM students WHERE username = 'alice.johnson'),
    (SELECT id FROM assignments WHERE name = 'Math Homework 1'),
    NULL,  -- No grade yet
    NULL);  -- No submission yet

-- Assign Student 2 (Bob Williams) to Assignment 2 (Physics Project)
INSERT INTO students_assignments (student_id, assignment_id, grade, submission)
VALUES
    ((SELECT id FROM students WHERE username = 'bob.williams'),
    (SELECT id FROM assignments WHERE name = 'Physics Project'),
    NULL,  -- No grade yet
    NULL);  -- No submission yet

-- Assign Student 3 (Charlie Brown) to Assignment 3 (History Essay)
INSERT INTO students_assignments (student_id, assignment_id, grade, submission)
VALUES
    ((SELECT id FROM students WHERE username = 'bob.williams'),
    (SELECT id FROM assignments WHERE name = 'History Essay'),
    NULL,  -- No grade yet
    NULL);  -- No submission yet
