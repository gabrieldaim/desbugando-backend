-- Script SQL para criar a tabela de Usuarios
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(100) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(100),
    tipo VARCHAR(50) NOT NULL,
    url_foto VARCHAR(255),
    url_linkedin VARCHAR(255),
    url_github VARCHAR(255),
    primeiro_acesso BOOLEAN DEFAULT true,
    senha_generica VARCHAR(5)
);
