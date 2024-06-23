-- Script SQL para criar a tabela de Postagens

CREATE TABLE comentarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    postagem_id UUID NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    conteudo TEXT NOT NULL,
    possui_imagem BOOLEAN DEFAULT false,
    url_imagem VARCHAR(250),
    CONSTRAINT fk_comentarios_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_comentarios_postagens FOREIGN KEY (postagem_id) REFERENCES postagens(id) ON DELETE CASCADE
);
