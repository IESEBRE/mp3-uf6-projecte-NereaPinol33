-- Taula Recepta
CREATE TABLE RECEPTA (
    id NUMBER PRIMARY KEY,
    nom VARCHAR2(255) NOT NULL,
    temps NUMBER 
);

--Taula Recepta-Ingredient
CREATE TABLE RECEPTA_INGREDIENT (
    id NUMBER PRIMARY KEY,
    recepta_id NUMBER NOT NULL,
    ingredient_nom VARCHAR2(255) NOT NULL,
    quantitat VARCHAR2(100),
    FOREIGN KEY (recepta_id) REFERENCES RECEPTA(id)
);

