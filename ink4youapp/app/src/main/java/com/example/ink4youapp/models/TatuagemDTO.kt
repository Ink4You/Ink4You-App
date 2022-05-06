package com.example.ink4youapp.models

data class TatuagemDTO (
    val id_tatuagem: Int?,
    val titulo: String?,
    val src_imagem: String,
    val id_tatuador: Int?,
    val nome: String,
    val uf: String?,
    val foto_perfil: String?
)

class TatuagemDTOBuilder {
    var id_tatuagem: Int? = 0;
    var titulo: String? = "";
    var src_imagem: String = "";
    var id_tatuador: Int? = 0;
    var nome: String = "";
    var uf: String = "";
    var foto_perfil: String? = "";

    fun build() : TatuagemDTO = TatuagemDTO(id_tatuagem, titulo, src_imagem, id_tatuador, nome, uf, foto_perfil);
}

fun tatuagem(block: TatuagemDTOBuilder.() -> Unit) : TatuagemDTO = TatuagemDTOBuilder().apply(block).build();

fun fakeTatuagens() = mutableListOf(
    tatuagem {
        id_tatuador = 1;
        titulo = "The last of Us";
        src_imagem = "https://i.pinimg.com/originals/f1/ef/2a/f1ef2a0a3c8637a82e915eaa6f69de8c.png";
        id_tatuador = 1;
        nome = "Osvaldo Pateta";
        uf = "SP";
        foto_perfil = "https://www.osaogoncalo.com.br/img/inline/100000/tatuadordascelebridades-3_00105058_1.jpg?xid=289596"
    },
    tatuagem {
        id_tatuador = 2;
        titulo = "Pata de dog";
        src_imagem = "https://i.pinimg.com/originals/4c/fc/17/4cfc173ea26db98b8df480620f4694e8.jpg";
        id_tatuador = 2;
        nome = "Gabriel Aleatório";
        uf = "SP";
        foto_perfil = "https://api.inkclub.tattoo/Content/images/tatuadores/273_25_10_2019_negocio-de-tatuagem.jpg"
    },
    tatuagem {
        id_tatuador = 3;
        titulo = "Gato";
        src_imagem = "https://i.pinimg.com/originals/02/16/c1/0216c1391eb85ad43a1b09df30939da6.jpg";
        id_tatuador = 3;
        nome = "David Surfista";
        uf = "SP";
        foto_perfil = "https://claudia.abril.com.br/wp-content/uploads/2016/09/miro-dantas-tatuador.jpg"
    },
    tatuagem {
        id_tatuador = 4;
        titulo = "Astronauta";
        src_imagem = "https://i.pinimg.com/originals/26/d6/17/26d6173a666510c879b609bb86c13bdc.jpg";
        id_tatuador = 4;
        nome = "Alex Maluco";
        uf = "SP";
        foto_perfil = "https://2.bp.blogspot.com/-xWfpo0cmrzY/WvH4iR7YywI/AAAAAAAALKk/NqJCvzN3xasweLkUZy0RKovIx5g9CYdUACLcBGAs/s1600/Hugo%2B4.jpg"
    },
    tatuagem {
        id_tatuador = 1;
        titulo = "The last of Us";
        src_imagem = "https://i.pinimg.com/originals/f1/ef/2a/f1ef2a0a3c8637a82e915eaa6f69de8c.png";
        id_tatuador = 1;
        nome = "Osvaldo Pateta";
        uf = "SP";
        foto_perfil = "https://www.osaogoncalo.com.br/img/inline/100000/tatuadordascelebridades-3_00105058_1.jpg?xid=289596"
    },
    tatuagem {
        id_tatuador = 2;
        titulo = "Pata de dog";
        src_imagem = "https://i.pinimg.com/originals/4c/fc/17/4cfc173ea26db98b8df480620f4694e8.jpg";
        id_tatuador = 2;
        nome = "Gabriel Aleatório";
        uf = "SP";
        foto_perfil = "https://api.inkclub.tattoo/Content/images/tatuadores/273_25_10_2019_negocio-de-tatuagem.jpg"
    },
    tatuagem {
        id_tatuador = 3;
        titulo = "Gato";
        src_imagem = "https://i.pinimg.com/originals/02/16/c1/0216c1391eb85ad43a1b09df30939da6.jpg";
        id_tatuador = 3;
        nome = "David Surfista";
        uf = "SP";
        foto_perfil = "https://claudia.abril.com.br/wp-content/uploads/2016/09/miro-dantas-tatuador.jpg"
    },
    tatuagem {
        id_tatuador = 4;
        titulo = "Astronauta";
        src_imagem = "https://i.pinimg.com/originals/26/d6/17/26d6173a666510c879b609bb86c13bdc.jpg";
        id_tatuador = 4;
        nome = "Alex Maluco";
        uf = "SP";
        foto_perfil = "https://2.bp.blogspot.com/-xWfpo0cmrzY/WvH4iR7YywI/AAAAAAAALKk/NqJCvzN3xasweLkUZy0RKovIx5g9CYdUACLcBGAs/s1600/Hugo%2B4.jpg"
    }
)