package h2

class Serie {
	Long id
	String nome
	/*
	*1 = Educação Infantil
	*2 = Ensino Fundamental
	*3 = Ensino Médio
	*/
	Integer tipo
	Escola escola

    static constraints = {
    	nome unique:true, blank:false, nullable:false,maxSize:255
    	tipo nullable:false, inList:[1,2,3]
    	escola nullable: true
    }

    static mapping = {
    	table 'serie'
    	version true
    	datasource 'DEFAULT' 
    	id (generator:'sequence', params:[sequence:'serie_seq'])
    }
}
