-- Script SQL para criar a tabela de Postagens

CREATE TABLE postagens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    turma_id UUID NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    conteudo TEXT NOT NULL,
    possui_imagem BOOLEAN DEFAULT false,
    url_imagem VARCHAR(250),
    CONSTRAINT fk_postagens_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_postagens_turmas FOREIGN KEY (turma_id) REFERENCES turmas(id) ON DELETE CASCADE
);
