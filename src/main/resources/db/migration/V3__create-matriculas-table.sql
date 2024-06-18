-- Script SQL para criar a tabela de Matriculas

CREATE TABLE matriculas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    turma_id UUID NOT NULL,
    data_matricula TIMESTAMP NOT NULL,
    CONSTRAINT fk_matriculas_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_matriculas_turmas FOREIGN KEY (turma_id) REFERENCES turmas(id) ON DELETE CASCADE
);
