
CREATE TABLE IF NOT EXISTS book (
    id uuid,
    title text,
    author text,
    numberOfBooks int,
    availability boolean,
    issuedDate date,
    returningDate date,
    stackNumber text,
    departmentType text,
    PRIMARY KEY(id)
);
