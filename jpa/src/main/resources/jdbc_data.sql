drop table  IF EXISTS employee_asset;
drop table  IF EXISTS employee;
CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    first_name varchar(20),
    last_name varchar(20),
    id_card varchar(20)
);

CREATE TABLE employee_asset (
    id BIGSERIAL PRIMARY KEY,
    employee_id bigint not null REFERENCES employee (id),
    asset_name varchar(200) not null
);

INSERT INTO employee(first_name, last_name, id_card)
    values ('man', 'super', 'CARD001'), ('women', 'super', 'CARD002');
