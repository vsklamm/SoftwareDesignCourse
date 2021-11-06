CREATE TABLE TasksLists (
    list_id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(list_id)
);

CREATE TABLE Tasks (
   task_id INT GENERATED ALWAYS AS IDENTITY,
   list_id INT NOT NULL,
   description VARCHAR(255) NOT NULL,
   status INT NOT NULL,
   PRIMARY KEY(task_id),
   CONSTRAINT task_owner FOREIGN KEY(list_id)
       REFERENCES TasksLists(list_id)
);