CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    first_name varchar(20),
    last_name varchar(20),
    id_card varchar(20)
);

INSERT INTO employee(first_name, last_name, id_card)
    values ('man', 'super', 'CARD001'), ('women', 'super', 'CARD002');