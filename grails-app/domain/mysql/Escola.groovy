package mysql

class Escola {
   	Long id
	Long cnpj
	String nomeFantasia
	String razaoSocial
	String endereco


    static constraints = {
    	cnpj nullable:false, unique:true
    	nomeFantasia nullable:false, blank:false, maxSize:400
    	razaoSocial nullable:false, blank:false, maxSize:400
    	endereco nullable:true, maxSize:255

    }

    static mapping = {
    	table 'ESCOLA'
    	version true
    	datasource 'lookup'
    	id  generator:'identity'
    }
}
