package com.example.ink4youapp.models

data class TatuadorDTO (
    val id_tatuador: Int?,
    val nome: String,
    val username: String,
    val data_nascimento: String?,
    val cnpj: String,
    val cep: String,
    val logradouro: String?,
    val numero_logradouro: String,
    val telefone: String,
    val email: String,
    val senha: String,
    val conta_instagram: String?,
    val foto_perfil: String?,
    val uf: String?,
    val idade: Int?,
    val sobre: String?,
    val estilos: List<String>?
)

class TatuadorDTOBuilder {
    var id_tatuador: Int? = 0;
    var nome: String = "";
    var username: String = "";
    var data_nascimento: String? = "";
    var cnpj: String = "";
    var cep: String = "";
    var logradouro: String? = "";
    var numero_logradouro: String = "";
    var telefone: String = "";
    var email: String = "";
    var senha: String = "";
    var conta_instagram: String? = "";
    var foto_perfil: String? = "";
    var uf: String? = "";
    var idade: Int? = 0;
    var sobre: String? = "";
    var estilos: List<String>? = null;

    fun build() : TatuadorDTO =
        TatuadorDTO(
            id_tatuador,
            nome,
            username,
            data_nascimento,
            cnpj,
            cep,
            logradouro,
            numero_logradouro,
            telefone,
            email,
            senha,
            conta_instagram,
            foto_perfil,
            uf,
            idade,
            sobre,
            estilos
        );
}

fun tatuador(block: TatuadorDTOBuilder.() -> Unit) : TatuadorDTO = TatuadorDTOBuilder().apply(block).build();

fun fakeTatuadores() = mutableListOf(
    tatuador {
        id_tatuador = 1;
        nome = "Dougras";
        username = "Dougtattoo";
        data_nascimento = "2000-10-10";
        cnpj = "000000000000";
        cep = "08150126";
        logradouro = "Rua Guabiroba";
        numero_logradouro = "127";
        telefone = "11951303748"
        email = "doug@email.com";
        senha = "123";
        conta_instagram = "dougtattoo";
        uf = "SP";
        idade = 21;
        sobre = "Tatuador brabo";
        estilos = null
    },
    tatuador {
        id_tatuador = 2;
        nome = "raines";
        username = "Rainestattoo";
        data_nascimento = "2000-10-10";
        cnpj = "000000000000";
        cep = "08150126";
        logradouro = "Rua dos tolos";
        numero_logradouro = "0";
        telefone = "11951303748"
        email = "raines@email.com";
        senha = "123";
        conta_instagram = "rainestattoo";
        uf = "SP";
        idade = 22;
        sobre = "Chhrahrhuashuhasuh raines";
        estilos = null
    },
    tatuador {
        id_tatuador = 3;
        nome = "Ale";
        username = "Aletattoo";
        data_nascimento = "2002-10-10";
        cnpj = "000000000000";
        cep = "08150126";
        logradouro = "Travessa Bom Jesus de goiás";
        numero_logradouro = "32";
        telefone = "11951303748"
        email = "ale@email.com";
        senha = "666";
        conta_instagram = "aletattoo";
        uf = "SP";
        idade = 19;
        sobre = "Suicidio é a solução :)";
        estilos = null
    }
)