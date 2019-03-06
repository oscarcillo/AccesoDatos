CREATE TABLE CLIENTES (
  IDCLIENTE NUMBER ,
  NOMBRE     VARCHAR2(50),
  DIRECCION  VARCHAR2(50),
  POBLACION  VARCHAR2(50),
  CODPOSTAL  NUMBER(5),
  PROVINCIA  VARCHAR2(40),
  NIF        VARCHAR2(9) UNIQUE,
  TELEFONO1  VARCHAR2(15),
  TELEFONO2  VARCHAR2(15),
  TELEFONO3  VARCHAR2(15),
  constraint PK_CLI PRIMARY KEY(IDCLIENTE)
);
CREATE TABLE PRODUCTOS (
  IDPRODUCTO  NUMBER ,
  DESCRIPCION varchar2(80),
  PVP   NUMBER,
  STOCKACTUAL NUMBER,
  constraint PK_PRO PRIMARY KEY(IDPRODUCTO)
  );	
  
  CREATE TABLE VENTAS(
  IDVENTA     NUMBER,
  IDCLIENTE   NUMBER NOT NULL,
  FECHAVENTA  DATE  ,
  constraint PK_VEN PRIMARY KEY(IDVENTA),
  CONSTRAINT FK_VEN FOREIGN KEY (IDCLIENTE) REFERENCES CLIENTES
);

CREATE TABLE LINEASVENTAS (  
  IDVENTA      NUMBER,
  NUMEROLINEA  NUMBER,
  IDPRODUCTO   NUMBER,
  CANTIDAD     NUMBER, 
  CONSTRAINT FK_LV1 FOREIGN KEY (IDVENTA) REFERENCES VENTAS (IDVENTA),
  CONSTRAINT FK_LV2 FOREIGN KEY (IDPRODUCTO) REFERENCES PRODUCTOS(IDPRODUCTO),  
  constraint PK_LV PRIMARY KEY (IDVENTA,NUMEROLINEA)
);
---------------------------------
CREATE TYPE TIP_TELEFONOS AS VARRAY(3) 
    OF VARCHAR2(15);
/
CREATE TYPE TIP_DIRECCION AS OBJECT(
  CALLE      VARCHAR2(50),
  POBLACION  VARCHAR2(50),
  CODPOSTAL  NUMBER(5),
  PROVINCIA  VARCHAR2(40)
);
/
CREATE TYPE TIP_CLIENTE AS OBJECT(
  IDCLIENTE  NUMBER,
  NOMBRE     VARCHAR2(50),
  DIREC      TIP_DIRECCION,
  NIF        VARCHAR2(9),
  TELEF      TIP_TELEFONOS
);
/	
CREATE TYPE TIP_PRODUCTO AS OBJECT (
  IDPRODUCTO   NUMBER,
  DESCRIPCION  VARCHAR2(80),
  PVP          NUMBER,
  STOCKACTUAL  NUMBER
);
/
CREATE TYPE  TIP_LINEAVENTA AS OBJECT (
  NUMEROLINEA  NUMBER,
  IDPRODUCTO   REF TIP_PRODUCTO,
  CANTIDAD     NUMBER
);
/
SHOW ERRORS;
--Tabla anidada para las lineas de venta
CREATE TYPE  TIP_LINEAS_VENTA  AS TABLE OF TIP_LINEAVENTA;
/
--tipo venta
CREATE or replace TYPE  TIP_VENTA AS OBJECT (
  IDVENTA     NUMBER,   
  IDCLIENTE   REF TIP_CLIENTE,
  FECHAVENTA  DATE,   
  LINEAS      TIP_LINEAS_VENTA,--tabla anidada
  MEMBER FUNCTION TOTAL_VENTA RETURN NUMBER
);
/
CREATE OR REPLACE TYPE BODY TIP_VENTA AS
  MEMBER FUNCTION TOTAL_VENTA RETURN NUMBER IS
   TOTAL NUMBER:=0;
   LINEA   TIP_LINEAVENTA;
   PRODUCT TIP_PRODUCTO;
  BEGIN
    FOR I IN 1..LINEAS.COUNT LOOP
      LINEA:=LINEAS(I);
      SELECT DEREF(LINEA.IDPRODUCTO) INTO PRODUCT  FROM DUAL;
      --PRODUCT:=DEREF(LINEA.IDPRODUCTO); (error si se ejecuta asi)
      TOTAL:=TOTAL + LINEA.CANTIDAD * PRODUCT.PVP;
    END LOOP;
    RETURN TOTAL;
  END;
END;
/
SHOW ERRORS;

--CREAMOS TABLAS
CREATE TABLE TABLA_CLIENTES OF TIP_CLIENTE (
  IDCLIENTE PRIMARY KEY,
  NIF UNIQUE
);
/
CREATE TABLE TABLA_PRODUCTOS OF TIP_PRODUCTO (
  IDPRODUCTO PRIMARY KEY
);
/
CREATE TABLE TABLA_VENTAS OF TIP_VENTA (
  IDVENTA PRIMARY KEY
)NESTED TABLE LINEAS STORE AS TABLA_LINEAS;
/

DELETE TABLA_VENTAS;
DELETE TABLA_PRODUCTOS;
DELETE TABLA_CLIENTES;
COMMIT;

--insercion de datos
INSERT INTO TABLA_CLIENTES VALUES 
(1, 'Luis Gracia',
 TIP_DIRECCION ('C/Las Flores 23', 'Guadalajara', '19003', 'Guadalajara'), 
 '34343434L', 
 TIP_TELEFONOS( '949876655', '949876655')
);

INSERT INTO TABLA_CLIENTES VALUES 
(2, 'Ana Serrano',
 TIP_DIRECCION ('C/Galiana 6', 'Guadalajara', 
         '19004', 'Guadalajara'), 
  '76767667F', 
  TIP_TELEFONOS('94980009')
);

INSERT INTO TABLA_PRODUCTOS VALUES (1, 'CAJA DE CRISTAL DE MURANO', 100, 5);
INSERT INTO TABLA_PRODUCTOS VALUES (2, 'BICICLETA CITY', 120, 15);
INSERT INTO TABLA_PRODUCTOS VALUES (3, '100 L�?PICES DE COLORES', 20, 5);
INSERT INTO TABLA_PRODUCTOS VALUES (4, 'OPERACIONES CON BD', 25, 5);
INSERT INTO TABLA_PRODUCTOS VALUES (5, 'APLICACIONES WEB', 25.50, 10);

COMMIT;
--inserta una venta con id 1 para el cliente 1
INSERT INTO TABLA_VENTAS 
SELECT 1, REF(C), SYSDATE, TIP_LINEAS_VENTA()
FROM TABLA_CLIENTES C WHERE C.IDCLIENTE=1;

SELECT * FROM TABLA_VENTAS;

--inserta 2 lineas de venta producto 1 y producto 2
INSERT INTO TABLE 
(SELECT V.LINEAS FROM TABLA_VENTAS V WHERE V.IDVENTA=1) 
(SELECT 1, REF(P), 1 
 FROM TABLA_PRODUCTOS P WHERE P.IDPRODUCTO=1);

INSERT INTO TABLE 
(SELECT V.LINEAS FROM TABLA_VENTAS V WHERE V.IDVENTA=1) 
(SELECT 2, REF(P), 2 
 FROM TABLA_PRODUCTOS P WHERE P.IDPRODUCTO=2);

--Inserto en TABLA_VENTAS la venta con IDVENTA 2 
--para el IDCLIENTE 1:
INSERT INTO TABLA_VENTAS 
SELECT 2, REF(C), SYSDATE, TIP_LINEAS_VENTA()
FROM TABLA_CLIENTES C WHERE C.IDCLIENTE=1;
--inserto 3 lineas de venta
--productos 1, 4 y 5
INSERT INTO TABLE 
(SELECT V.LINEAS FROM TABLA_VENTAS V WHERE V.IDVENTA=2) 
(SELECT 1, REF(P), 2 FROM TABLA_PRODUCTOS P 
 WHERE P.IDPRODUCTO=1);

INSERT INTO TABLE 
(SELECT V.LINEAS FROM TABLA_VENTAS V WHERE V.IDVENTA=2) 
(SELECT 2, REF(P), 1 FROM TABLA_PRODUCTOS P 
 WHERE P.IDPRODUCTO=4);

INSERT INTO TABLE 
(SELECT V.LINEAS FROM TABLA_VENTAS V WHERE V.IDVENTA=2) 
(SELECT 3, REF(P), 4 FROM TABLA_PRODUCTOS P
 WHERE P.IDPRODUCTO=5);

COMMIT;
--
SELECT * FROM TABLA_VENTAS;
--
--CONSULTAS
SELECT  IDVENTA, DEREF(IDCLIENTE).NOMBRE NOMBRE,
DEREF(IDCLIENTE).IDCLIENTE IDCLIENTE, T.TOTAL_VENTA() TOTAL
FROM TABLA_VENTAS T;

SELECT P.IDVENTA IDV, DEREF(P.IDCLIENTE).NOMBRE NOMBRE,
       DETALLE.NUMEROLINEA LINEA,
       DEREF(DETALLE.IDPRODUCTO).DESCRIPCION PRODUCTO,
       DETALLE.CANTIDAD,
       DETALLE.CANTIDAD * DEREF(DETALLE.IDPRODUCTO).PVP IMPORTE,
       DEREF(DETALLE.IDPRODUCTO).PVP PVP,
       DEREF(DETALLE.IDPRODUCTO).STOCKACTUAL STOCK
FROM TABLA_VENTAS P, TABLE(P.LINEAS) DETALLE;


--
CREATE OR REPLACE PROCEDURE VER_VENTA (ID NUMBER) AS
  IMPORTE NUMBER;  
  TOTAL_V NUMBER;
  CLI TIP_CLIENTE:=TIP_CLIENTE(NULL,NULL,NULL,NULL, NULL);
  DIR TIP_DIRECCION:=TIP_DIRECCION(NULL,NULL,NULL,NULL);
  FEC DATE;
  --cursor para recorrer la tabla anidada del idventa
  --que se recibe
  CURSOR C1 IS 
    SELECT NUMEROLINEA LIN, DEREF(IDPRODUCTO) PROD, 
           CANTIDAD 
    FROM THE 
     (SELECT T.LINEAS FROM TABLA_VENTAS T WHERE IDVENTA=ID);
BEGIN
  SELECT DEREF(IDCLIENTE), FECHAVENTA, V.TOTAL_VENTA() 
       INTO CLI, FEC, TOTAL_V
  FROM TABLA_VENTAS V WHERE IDVENTA=ID;
  
  --DIR :=CLI.DIREC;
  DBMS_OUTPUT.PUT_LINE('=============================================');
  DBMS_OUTPUT.PUT_LINE('NUMERO DE VENTA: '||ID|| 
           ' * Fecha de venta: '|| FEC);
  DBMS_OUTPUT.PUT_LINE('CLIENTE: '||CLI.NOMBRE);  
  DBMS_OUTPUT.PUT_LINE('DIRECCION: '||CLI.DIREC.CALLE);
  DBMS_OUTPUT.PUT_LINE('=============================================');
 
  FOR I IN C1 LOOP  
    IMPORTE:= I.CANTIDAD * I.PROD.PVP;
    DBMS_OUTPUT.PUT_LINE(I.LIN||'* '||
        I.PROD.DESCRIPCION ||'* '||I.PROD.PVP||'* '||
        I.CANTIDAD||'* '||IMPORTE);
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('Total Venta: '||TOTAL_V);
EXCEPTION
WHEN NO_data_FOUND THEN
  DBMS_OUTPUT.PUT_LINE('Identificador '|| id || ' de venta inexistente');
END VER_VENTA;
/

BEGIN
  VER_VENTA(1);
END;





