CREATE TABLE users (
    id INT PRIMARY KEY,  
    username VARCHAR(255) NOT NULL UNIQUE, 
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL, 
    accountType VARCHAR(50) NOT NULL CHECK (accountType IN ('Admin', 'Teacher', 'Student'))
);



CREATE TABLE teachers (
    ID INT,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    primary key (ID),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE

);


CREATE TABLE students (
    ID INT,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    primary key (ID),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE

);


CREATE TABLE assignments (
    id SERIAL PRIMARY KEY,  -- Automatically assigned ID (Primary Key)
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    teacher_id INT,  -- Foreign Key referencing teachers table
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE ON UPDATE CASCADE  -- Cascade on delete
);




CREATE TABLE students_assignments (
    student_id INT,                     -- Foreign key to the students table
    assignment_id INT,                  -- Foreign key to the assignments table
    submission TEXT,
    grade INT CHECK (grade >= 0 AND grade <= 100), -- Grade for the student on the assignment (e.g., 95.50)
    PRIMARY KEY (student_id, assignment_id),  -- Composite primary key (student_id, assignment_id)
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,  -- Ensure referential integrity
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE  -- Ensure referential integrity
);


