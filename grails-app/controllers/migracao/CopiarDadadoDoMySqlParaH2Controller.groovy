package migracao


import org.grails.core.DefaultGrailsDomainClass

class CopiarDadadoDoMySqlParaH2Controller {
	private int paginacao=500

    def index() { 
    	
    	def classesQueVaoSerCopiadas =[]
    	//classe de origem e depois destino
    	classesQueVaoSerCopiadas.add(["mysql.Escola","h2.Escola"])	

		def hqlConsulta=null
        def classeOriginal
        def classeDestino

        for ( classe in classesQueVaoSerCopiadas){//percorre todas as classes do array
            classeOriginal = classe.get(0)
            classeDestino = classe.get(1)

            println   classeOriginal

            Class clazzOrigem = Class.forName(classeOriginal, true, Thread.currentThread().contextClassLoader)//carrega em tempo de execucao a classe carregada
            Class clazzDestino = Class.forName(classeDestino, true, Thread.currentThread().contextClassLoader)




                primeiro: for (int i=0; ; i+=paginacao){
                    def objetosOriginais = pesquisarObjetos(clazzOrigem, i)


                    if(objetosOriginais== null  || objetosOriginais.isEmpty()){//se na base  ta limpa nao precisa fazer nada
                        break primeiro
                    }

                    for (objetoOriginal in objetosOriginais){

                        def objetoDestino = clazzDestino.newInstance();
                        def camposPersistentes = new DefaultGrailsDomainClass(objetoOriginal.class).persistentProperties

                        //faz um loop em todos os atributos que sao persitidos na base de dados
                        for (campoPersistente in camposPersistentes){
                            def nomeCampo =campoPersistente.getName()
                            def valorCampo =objetoOriginal.getProperty(nomeCampo)

                            
                            if (valorCampo!=null){//se o campo tem valor
                                if (grailsApplication.isDomainClass(valorCampo.getClass())) {//tratamento caso o campo em questao seja um foreign key
                                    valorCampo=  pesquisarObjeto(valorCampo, valorCampo.id)
                                    objetoDestino.setProperty(nomeCampo ,valorCampo )
                                }else{                                    
                                    objetoDestino.setProperty(nomeCampo , valorCampo )
                                }

                            }

                        }

                        objetoDestino.id = objetoOriginal?.id
                        if (!objetoDestino.save(flush:true)){
                            println ""
                            println "erro para salvar" + objetoDestino.errors
                        }




                    }

                    objetosOriginais=null

                }





            


        }



        render "OK"






    }


    /**
    Carrega os dados da base de origem
    *@param classe, indica a classe  de origem
    *@param offset, faz a pagina 
    */
   private def pesquisarObjetos(def classe, int offset){    
	    return classe.createCriteria().list {
	        maxResults(paginacao)
	        firstResult(offset)
	    }
	}



	private def pesquisarObjeto(Object objeto, def id){
        def classeObjeto =  objeto.class.name
        def clazzDestino = null
        for (classe in getClasses()){
            if (classe.get(0) ==classeObjeto){
                clazzDestino=classe.get(1)
                break
            }
        }


        Class clazzPesquisa = Class.forName(clazzDestino, true, Thread.currentThread().contextClassLoader)
        def retorno =  clazzPesquisa.get(id)
        if (!retorno){
            //retorno = clazzPesquisa.findById(id)
            println "erro de banco"
        }
        return retorno

    }


}
